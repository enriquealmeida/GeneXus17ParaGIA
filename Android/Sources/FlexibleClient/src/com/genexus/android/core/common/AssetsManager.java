package com.genexus.android.core.common;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.genexus.android.core.base.services.IAssetsService;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class AssetsManager implements IAssetsService {

	private final Context mContext;
	private HashMap<String, String> mAnimationFilenamesCache = new LinkedHashMap<>();

	public AssetsManager(Context context) {
		mContext = context;
	}

	@Override
	public boolean hasAsset(String assetName) {
		try (InputStream ignored = mContext.getAssets().open(assetName)) {
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	@Override
	public Typeface getFont(String fontFileName, int weight, boolean italic) {
		AssetManager assetManager = mContext.getAssets();
		String assetPath = getFontAssetPath(fontFileName);

		if (!hasAsset(assetPath))
			return null;

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			Typeface typeface = new Typeface.Builder(assetManager, assetPath).setWeight(weight).setItalic(italic).build();
			if (typeface != null)
				return typeface;
		}

		return Typeface.createFromAsset(assetManager, assetPath);
	}

	@Override
	public String copyAssetFontToStorage(String fontName) {
		String fontFileName = getActualFontFileName(fontName);
		if (!Strings.hasValue(fontFileName))
			return null;

		String copiedFilePath = StorageHelper.getStorageDirectory("fonts") + File.separator + fontFileName;
		File tempFile = new File(copiedFilePath);
		if (tempFile.exists())
			return copiedFilePath;

		try {
			if (!tempFile.createNewFile())
				return null;
		} catch (IOException ignored) {
			return null;
		}

		try (InputStream is = mContext.getAssets().open(getFontAssetPath(fontFileName));
			 OutputStream os = new FileOutputStream(tempFile)) {

			IOUtils.copy(is, os);
			return copiedFilePath;
		} catch (IOException e) {
			Services.Log.error(e);
			return null;
		}
	}

	private @NonNull String getFontAssetPath(String fontName) {
		return "fonts" + File.separator + fontName;
	}

	/**
	 * @param fontName might not be the actual fileName created under assets at compilation time
	 * @return The actual file name in case there is one matching font or null otherwise
	 */
	private @Nullable String getActualFontFileName(String fontName) {
		if (hasAsset(getFontAssetPath(fontName)))
			return fontName;

		AssetManager assetsManager = mContext.getAssets();

		try {
			for (String fileName : assetsManager.list("fonts"))
				if (fileName.toLowerCase().startsWith(fontName.toLowerCase())) {
					String extension = fileName.substring(fileName.lastIndexOf("."));
					if (extension.equalsIgnoreCase(".otf") || extension.equalsIgnoreCase(".ttf"))
						return fileName;
				}
		} catch (IOException ignored) { }

		return null;
	}

	@Override
	public String getAnimationFilename(String animationName) {
		if (mAnimationFilenamesCache.containsKey(animationName)) {
			return mAnimationFilenamesCache.get(animationName);
		}

		String fileName = animationName.toLowerCase() + "_animation.json";
		if (!hasAsset(fileName)) {
			//try with custom file name
			fileName = animationName + ".json";
			if (!hasAsset(fileName)) {
				//return to default one, will fail later
				fileName = animationName.toLowerCase() + "_animation.json";
			}
		}

		mAnimationFilenamesCache.put(animationName, fileName);

		return fileName;
	}
}
