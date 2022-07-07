/**
 *
 */
package com.genexus.android.core.base.metadata.enums;

public class ImageUploadModes {

	public static final short ACTUALSIZE = 1; 
	public static final short LARGE = 2; // Default
	public static final short MEDIUM = 3;
	public static final short SMALL = 4;
	public static final short ASK = 5;

	public static int resolveModeForUpload(int callerMode, int calledMode) {
		return Math.max(callerMode, calledMode);
	}

	// formula =-0.0062*x*x+0.4391*x+1.5103
	// formula2 = 0.2629*x + 2.2807

	public static int getScaleRatioFromCoefficient(double ratio)
	{
		int result = 1;
		//if image already small , not resize.
		if (ratio< result)
			return result;

		//double scaleTo = (-0.0062*ratio*ratio) + (0.4391*ratio) + (double)1.5103;
		double scaleTo = (0.2629*ratio) + 2.2807;

		result = (int) Math.ceil(scaleTo); //round

		if (result < 1)
			result = 1;

		return result;
	}

	public static int getModeFromString(String maxUploadSize) {
		if (maxUploadSize.equalsIgnoreCase("small"))
			return ImageUploadModes.SMALL;
		else if (maxUploadSize.equalsIgnoreCase("medium"))
			return ImageUploadModes.MEDIUM;
		else if (maxUploadSize.equalsIgnoreCase("actualsize") || maxUploadSize.equalsIgnoreCase("actual"))
			return ImageUploadModes.ACTUALSIZE;
		//default
		return ImageUploadModes.LARGE;
	}
}
