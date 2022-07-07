package com.genexus.android.core.base.services;

import android.graphics.Typeface;

public interface IAssetsService {
	boolean hasAsset(String assetName);
	Typeface getFont(String fontFileName, int weight, boolean italic);
	String copyAssetFontToStorage(String fontName);
	String getAnimationFilename(String animationName);
}
