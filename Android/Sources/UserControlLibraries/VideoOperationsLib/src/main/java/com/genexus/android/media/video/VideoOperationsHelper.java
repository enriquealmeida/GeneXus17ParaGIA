package com.genexus.android.media.video;

import com.genexus.android.core.actions.ApiAction;
import com.genexus.android.core.utils.TaskRunner;
import com.arthenica.mobileffmpeg.FFprobe;
import com.arthenica.mobileffmpeg.MediaInformation;
import com.arthenica.mobileffmpeg.StreamInformation;

class VideoOperationsHelper {

	private static final String ARGS_STARTING = "-y -i ";
	private static final String ARGS_VIDEO = "%s ";
	private static final String ARGS_CODEC_265 = "-vcodec libx265 ";
	private static final String ARGS_CODEC_264 = "-vcodec libx264 ";
	private static final String ARGS_CRF = "-crf %s ";
	private static final String ARGS_3GP = "-s %s ";

	private ApiAction mCurrentApiAction = null;

	public VideoOperationsHelper() {}

	public void setCurrentAction(ApiAction apiAction) {
		mCurrentApiAction = apiAction;
	}

	boolean applyConversion(String videoUri, boolean shouldConvert, int targetFormat,
							boolean shouldReduceQuality, int targetQuality) {

		int conversion = needsConversion(videoUri, shouldConvert, targetFormat, shouldReduceQuality, targetQuality);
		if (conversion == ConversionRequired.FULL) {
			convertAndReduceQuality(videoUri, targetFormat, targetQuality);
		} else if (conversion == ConversionRequired.FORMAT) {
			convertFormat(videoUri, targetFormat);
		} else if (conversion == ConversionRequired.QUALITY) {
			reduceQuality(videoUri, targetQuality);
		} else {
			return false;
		}

		return true;
	}

	int needsConversion(String videoUri, boolean shouldConvert, int targetFormat,
	                    boolean shouldReduceQuality, int targetQuality) {

		//targetQuality = 7 is High, thus video quality won't be reduced
		if (shouldConvert && !videoUri.contains(parseVideoFormatDomain(targetFormat)) && shouldReduceQuality && targetQuality != 7) {
			return ConversionRequired.FULL;
		} else if (shouldConvert && !videoUri.contains(parseVideoFormatDomain(targetFormat))) {
			return ConversionRequired.FORMAT;
		} else if (shouldReduceQuality && targetQuality != 7) {
			return ConversionRequired.QUALITY;
		} else {
			return ConversionRequired.NONE;
		}
	}

	void convertAndReduceQuality(String videoUri, int targetFormat, int targetQuality) {
		String newFileName = changeVideoNameExtension(videoUri, parseVideoFormatDomain(targetFormat));
		newFileName = changeVideoNameQuality(newFileName, parseVideoQualityDomain(targetQuality));
		String command = buildCommand(videoUri, newFileName, targetFormat, targetQuality);
		executeFFmpegCommand(command, videoUri, newFileName);
	}

	void reduceQuality(String videoUri, int targetQuality) {
		String newFileName = changeVideoNameQuality(videoUri, parseVideoQualityDomain(targetQuality));
		String command = buildCommand(videoUri, newFileName, 0, targetQuality);
		executeFFmpegCommand(command, videoUri, newFileName);
	}

	void convertFormat(String videoUri, int targetFormat) {
		String newFileName = changeVideoNameExtension(videoUri, parseVideoFormatDomain(targetFormat));
		String command = buildCommand(videoUri, newFileName, targetFormat, 7);
		executeFFmpegCommand(command, videoUri, newFileName);
	}

	private String buildCommand(String videoFileName, String newVideoFileName, int videoFormat, int targetQuality) {
		StringBuilder sb = new StringBuilder();
		sb.append(ARGS_STARTING);
		sb.append(String.format(ARGS_VIDEO, videoFileName));

		if (targetQuality < 7) {
			if ((videoFormat == 0 && isFormat(videoFileName, "mp4")) || videoFormat == 101)
				sb.append(ARGS_CODEC_265); //H265 has te best loseless compression factor but can't be used for some formats
			else if (!isFormat(videoFileName, "mp4"))
				sb.append(ARGS_CODEC_264);

			int crf = getTargetCrf(targetQuality); //Constant rate factor for optimal compression
			sb.append(String.format(ARGS_CRF, crf));
		}

		//If target format is .3gp or .3g2 and video resolution is higher than 1408x1152,
		// then it has to be lowered because video compression will fail since those formats doesn't support higher values
		if (videoFormat == 102 || videoFormat == 103) {
			int[] originalResolution = getVideoResolution(videoFileName);
			int[] newResolution = calculateNewResolution(originalResolution);
			if (!(originalResolution[0] == newResolution[0] && originalResolution[1] == newResolution[1])) {
				if (!sb.toString().contains("-vcodec"))
					sb.append(ARGS_CODEC_264); //H264 has to be used for 3GP since H263 doesn't support 16:9 aspect ratio nor H265 encoder

				String res = newResolution[0] + "x" + newResolution[1];
				sb.append(String.format(ARGS_3GP, res));
			}
		}

		sb.append(String.format(ARGS_VIDEO, newVideoFileName));
		return sb.toString();
	}

	private String changeVideoNameExtension(String fileName, String newExtension) {
		int extensionIndex = fileName.lastIndexOf(".");
		return fileName.substring(0, extensionIndex) + newExtension;
	}

	private String changeVideoNameQuality(String fileName, String quality) {
		int extensionIndex = fileName.lastIndexOf(".");
		String extension = fileName.substring(extensionIndex);
		return fileName.substring(0, extensionIndex) + "_" + quality + extension;
	}

	//Higher CRF values mean lower quality
	private int getTargetCrf(int targetQuality) {
		return targetQuality < 5 ? 28 : 25;
	}

	private int[] calculateNewResolution(int[] originalResolution) {
		if (originalResolution[0] > 1408) {
			int width = 1408;
			int height = (originalResolution[1] * width) / originalResolution[0];
			return new int[]{width, height};
		} else if (originalResolution[1] > 1152) {
			int height = 1152;
			int width = (originalResolution[0] * height) / originalResolution[1];
			return new int[]{width, height};
		}

		return originalResolution;
	}

	private boolean isFormat(String fileName, String checkExtension) {
		int extensionIndex = fileName.lastIndexOf(".") + 1;
		String extension = fileName.substring(extensionIndex);
		return extension.equalsIgnoreCase(checkExtension);
	}

	private MediaInformation extractVideoInformation(String video) {
		return FFprobe.getMediaInformation(video);
	}

	private void executeFFmpegCommand(String command, String originalVideo, String modifiedVideo) {
		if (mCurrentApiAction == null)
			throw new IllegalStateException("Current ApiAction cannot be null");

		TaskRunner.execute(new FFmpegExecuteTask(command, originalVideo, modifiedVideo, new ExecuteCallback(mCurrentApiAction)));
	}

	private String parseVideoQualityDomain(int targetQuality) {
		switch (targetQuality) {
			case 3:
				return "low";
			case 5:
				return "medium";
			case 7:
				return "high";
		}

		throw new IllegalArgumentException("CameraAPIQuality value should be either 3, 5 or 7");
	}

	private String parseVideoFormatDomain(int videoFormat) {
		switch (videoFormat) {
			case 101:
				return ".mp4";
			case 102:
				return ".3gp";
			case 103:
				return ".3g2";
			case 201:
				return ".qt";
			case 202:
				return ".m4v";
		}

		throw new IllegalArgumentException("VideoFormat value not supported");
	}

	private int[] getVideoResolution(String video) {
		int[] res = new int[2];
		MediaInformation mediaInformation = extractVideoInformation(video);
		if (mediaInformation != null)
			for (StreamInformation info : mediaInformation.getStreams())
				if (info.getType().equalsIgnoreCase("video")) {
					res[0] = info.getWidth().intValue();
					res[1] = info.getHeight().intValue();
					break;
				}

		return res;
	}
}
