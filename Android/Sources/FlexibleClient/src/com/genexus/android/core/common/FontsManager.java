package com.genexus.android.core.common;

import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.genexus.android.FileDownloader;
import com.genexus.android.core.base.metadata.theme.ThemeDefinition;
import com.genexus.android.core.base.metadata.theme.ThemeFontFamilyDefinition;
import com.genexus.android.core.base.services.IFontsService;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.common.handlers.fonts.OnReceiveFontHandler;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

public class FontsManager implements IFontsService {

	// Cache Typeface objects. For performance reasons, and also to avoid a memory leak
	// in some versions (http://code.google.com/p/android/issues/detail?id=9904).
	private final HashMap<String, Typeface> mTypefacesCache = new HashMap<>();
	private final List<FontItem> mPendingViews = new ArrayList<>();

	public static final String FONTS_DIRECTORY = "Fonts";

	@Override
	public @Nullable Typeface getFontFamily(ThemeDefinition theme, String name, int weight, boolean italic, OnReceiveFontHandler receiver) {
		String key = getCacheKey(name, weight, italic);
		if (mTypefacesCache.containsKey(key))
			return mTypefacesCache.get(key);

		if (currentlyDownloading(key)) {
			mPendingViews.add(new FontItem(key, receiver));
			return null;
		}

		Typeface typeface = resolveTypeface(name, theme, weight, italic);
		if (typeface == null) {
			mPendingViews.add(new FontItem(key, receiver));
		} else {
			mTypefacesCache.put(key, typeface);
		}

		return typeface;
	}

	private @Nullable String getFontFileName(String name, ThemeDefinition theme) {
		ThemeFontFamilyDefinition fontFamily = theme.getFontFamily(name);
		if (fontFamily == null) {
			Services.Log.error("Font '" + name + "' not found in theme class '" + theme.getName() + "'");
			return null;
		}

		String fontFileName = fontFamily.getFileName();
		if (fontFileName == null) {
			Services.Log.error("Font file name was empty for theme '" + theme.getName() + "'");
			return null;
		}

		return fontFileName;
	}

	/**
	 * Attempts to resolve the font first by searching for it in the embedded fonts directory in the
	 * APK, then in the downloaded files, then on the server and finally by name in the system fonts.
	 */
	private @Nullable Typeface resolveTypeface(String name, ThemeDefinition theme, int weight, boolean italic) {
		String fontFileName = getFontFileName(name, theme);
		Typeface typeFace = null;

		if (Strings.hasValue(fontFileName)) {
			typeFace = Services.Assets.getFont(fontFileName, weight, italic);

			if (typeFace == null)
				typeFace = getFontFromFiles(fontFileName, weight, italic);

			if (typeFace == null)
				if (getFontFromServer(fontFileName, name, weight, italic))
					return null; //Downloaded in background so View font will be updated later
		}

		if (typeFace == null)
			typeFace = getSystemFont(fontFileName, weight, italic);

		return typeFace;
	}

	private @NonNull Typeface getSystemFont(String fontFamilyName, int weight, boolean italic) {
		Typeface defaultFont = Typeface.DEFAULT;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
			defaultFont = Typeface.create(defaultFont, weight, italic);

		if (fontFamilyName != null && !fontFamilyName.isEmpty()) {
			String[] fontNames = Services.Strings.split(fontFamilyName, ',');
			for (String fontName : fontNames) {
				Typeface myFont = Typeface.create(fontName, Typeface.NORMAL);
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
					myFont = Typeface.create(myFont, weight, italic);
				if (!defaultFont.equals(myFont))
					return myFont;
			}
		}

		return defaultFont;
	}

	private @Nullable Typeface getFontFromFiles(String fontFamilyName, int weight, boolean italic) {
		File fontPath = new File(Services.Application.getAppContext().getExternalFilesDir(FONTS_DIRECTORY), fontFamilyName);
		if (!fontPath.exists())
			return null;

		Typeface typeface;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
			typeface = new Typeface.Builder(fontPath).setWeight(weight).setItalic(italic).build();
			if (typeface != null)
				return typeface;
		}

		typeface = Typeface.createFromFile(fontPath);
		return (typeface == null || typeface.equals(Typeface.DEFAULT)) ? null : typeface;
	}

	private boolean getFontFromServer(String fontFamilyName, String name, int weight, boolean italic) {
		if (!(fontFamilyName.endsWith(".ttf") || fontFamilyName.endsWith(".otf"))) {
			Services.Log.error(String.format("Unsupported Font file format for %s", fontFamilyName));
			return false;
		}

		String key = getCacheKey(name, weight, italic);
		if (currentlyDownloading(key) || !Services.HttpService.isOnline())
			return false;

		String uri = Services.Application.get().UriMaker.getFontUrl(fontFamilyName);
		FileDownloader.enqueue(Uri.parse(uri), Services.Application.getAppContext(), FONTS_DIRECTORY, new FileDownloader.FileDownloaderListener() {
			@Override
			public void onDownloadProgressUpdate(int progressPercentage) {
			}

			@Override
			public void onDownloadSuccessful(Uri fileUri, String fileName) {
				String fontFileName = Services.HttpService.uriDecode(fileUri.toString().substring(fileUri.toString().lastIndexOf("/") + 1));
				Typeface typeface = getFontFromFiles(fontFileName, weight, italic);
				if (typeface == null) {
					Services.Log.error(String.format("Font '%s' file not found at %s", key, fileUri.toString()));
					typeface = getSystemFont(fontFamilyName, weight, italic);
				}

				fontDownloaded(key, typeface);
			}

			@Override
			public void onDownloadFailed() {
				Services.Log.error(String.format("Font '%s' download failed from %s", fontFamilyName, uri));
			}

		});
		return true;
	}

	private String getCacheKey(String fontFamilyName, int weight, boolean italic) {
		return fontFamilyName.replace(".", "_") + "," + weight + "," + italic;
	}

	private void fontDownloaded(String key, Typeface typeface) {
		mTypefacesCache.put(key, typeface);
		updatePendingViews(key);
	}

	private boolean currentlyDownloading(String key) {
		for (FontItem item : mPendingViews)
			if (Strings.hasValue(item.getKey()) && item.getKey().equals(key))
				return true;

		return false;
	}

	private void updatePendingViews(String key) {
		ListIterator<FontItem> iterator = mPendingViews.listIterator();
		while (iterator.hasNext()) {
			FontItem fontItem = iterator.next();
			if (Strings.hasValue(fontItem.getKey()) && fontItem.getKey().equals(key) && fontItem.getReceiver() != null) {
				fontItem.getReceiver().receive(mTypefacesCache.get(key));
				iterator.remove();
			}
		}
	}

	private static class FontItem {

		private final String mKey;
		private final OnReceiveFontHandler mReceiver;

		public FontItem(String key, OnReceiveFontHandler receiver) {
			mKey = key;
			mReceiver = receiver;
		}

		public String getKey() { return mKey; }

		public OnReceiveFontHandler getReceiver() {
			return mReceiver;
		}
	}
}
