package com.genexus.android.media.video;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.genexus.android.core.actions.ActionExecution;
import com.genexus.android.core.actions.ApiAction;
import com.genexus.android.core.base.metadata.expressions.Expression;
import com.genexus.android.core.base.model.Entity;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.externalapi.ExternalApi;
import com.genexus.android.core.externalapi.ExternalApiResult;
import com.genexus.android.core.utils.FileUtils2;

import java.util.List;

public class VideoOperationsAPI extends ExternalApi {

	public static final String OBJECT_NAME = "GeneXus.SD.Media.VideoOperations";

	private static final String SDT_VIDEO_CONVERSION_CONVERT_FORMAT = "ConvertFormat";
	private static final String SDT_VIDEO_CONVERSION_TARGET_FORMAT = "TargetFormat";
	private static final String SDT_VIDEO_CONVERSION_REDUCE_QUALITY = "ReduceQuality";
	private static final String SDT_VIDEO_CONVERSION_TARGET_QUALITY = "TargetQuality";

	private static final String METHOD_APPLY_CONVERSIONS = "ApplyConversions";
	private static final String METHOD_CONVERT_FORMAT = "ConvertFormat";
	private static final String METHOD_REDUCE_QUALITY = "ReduceQuality";

	private static final String PROPERTY_FILE_SIZE = "FileSize";
	private static final String PROPERTY_VIDEO_CODEC = "VideoCodec";
	private static final String PROPERTY_VIDEO_DURATION = "VideoDuration";
	private static final String PROPERTY_VIDEO_HEIGHT = "VideoHeight";
	private static final String PROPERTY_VIDEO_WIDTH = "VideoWidth";

	private static final String SCHEME_CONTENT = "content://";

	private VideoOperationsHelper mHelper;
	private Uri mVideoUri;
	private boolean mShouldReduceQuality;
	private boolean mShouldConvert;
	private int mTargetQuality;
	private int mTargetFormat;

	public VideoOperationsAPI(ApiAction action) {
		super(action);

		mHelper = new VideoOperationsHelper();
		addMethodHandler(METHOD_APPLY_CONVERSIONS, 2, mApplyConversionsMethodHandler);
		addMethodHandler(METHOD_CONVERT_FORMAT, 2, mConvertFormatMethodHandler);
		addMethodHandler(METHOD_REDUCE_QUALITY, 2, mReduceQualityMethodHandler);
		addReadonlyPropertyHandler(PROPERTY_FILE_SIZE, mFileSizePropertyHandler);
		addReadonlyPropertyHandler(PROPERTY_VIDEO_CODEC, mVideoCodecPropertyHandler);
		addReadonlyPropertyHandler(PROPERTY_VIDEO_DURATION, mVideoDurationPropertyHandler);
		addReadonlyPropertyHandler(PROPERTY_VIDEO_HEIGHT, mVideoHeightPropertyHandler);
		addReadonlyPropertyHandler(PROPERTY_VIDEO_WIDTH, mVideoWidthPropertyHandler);
	}

	private final IMethodInvoker mApplyConversionsMethodHandler = parameters -> {
		String videoUri = (String) parameters.get(0);
		Entity conversionsSDT = (Entity) parameters.get(1);
		if (conversionsSDT == null)
			return ExternalApiResult.FAILURE;

		boolean shouldReduceQuality = conversionsSDT.optBooleanProperty(SDT_VIDEO_CONVERSION_REDUCE_QUALITY);
		boolean shouldConvert = conversionsSDT.optBooleanProperty(SDT_VIDEO_CONVERSION_CONVERT_FORMAT);
		int targetQuality = conversionsSDT.optIntProperty(SDT_VIDEO_CONVERSION_TARGET_QUALITY);
		int targetFormat = conversionsSDT.optIntProperty(SDT_VIDEO_CONVERSION_TARGET_FORMAT);

		return applyConversion(videoUri, shouldReduceQuality, shouldConvert, targetQuality, targetFormat);
	};

	private final IMethodInvoker mConvertFormatMethodHandler = parameters -> {
		String videoUri = (String) parameters.get(0);
		int targetFormat = Integer.parseInt((String) parameters.get(1));

		return applyConversion(videoUri, false, true, 0, targetFormat);
	};

	private final IMethodInvoker mReduceQualityMethodHandler = parameters -> {
		String videoUri = (String) parameters.get(0);
		int targetQuality = Integer.parseInt((String) parameters.get(1));

		return applyConversion(videoUri, true, false, targetQuality, 0);
	};

	private final IMethodInvoker mFileSizePropertyHandler = new IMethodInvoker() {
		@NonNull
		@Override
		public ExternalApiResult invoke(List<Object> parameters) {
			return null;
		}
	};

	private final IMethodInvoker mVideoCodecPropertyHandler = new IMethodInvoker() {
		@NonNull
		@Override
		public ExternalApiResult invoke(List<Object> parameters) {
			return null;
		}
	};

	private final IMethodInvoker mVideoDurationPropertyHandler = new IMethodInvoker() {
		@NonNull
		@Override
		public ExternalApiResult invoke(List<Object> parameters) {
			return null;
		}
	};

	private final IMethodInvoker mVideoHeightPropertyHandler = new IMethodInvoker() {
		@NonNull
		@Override
		public ExternalApiResult invoke(List<Object> parameters) {
			return null;
		}
	};

	private final IMethodInvoker mVideoWidthPropertyHandler = new IMethodInvoker() {
		@NonNull
		@Override
		public ExternalApiResult invoke(List<Object> parameters) {
			return null;
		}
	};

	private ExternalApiResult applyConversion(String videoUri, boolean shouldReduceQuality, boolean shouldConvert,
	                                          int targetQuality, int targetFormat) {
		if (videoUri.isEmpty())
			return ExternalApiResult.FAILURE;

		if (mHelper.needsConversion(videoUri, shouldConvert, targetFormat, shouldReduceQuality, targetQuality) == ConversionRequired.NONE) {
			Services.Log.debug("Returning original video since required parameters were not met");
			return ExternalApiResult.success(videoUri);
		}

		setVariables(shouldReduceQuality, shouldConvert, targetQuality, targetFormat);

		setCurrentAction();
		mVideoUri = Uri.parse(videoUri);
		if (videoUri.startsWith(SCHEME_CONTENT)) {
			copyVideo(mVideoUri);
			return ExternalApiResult.SUCCESS_WAIT;
		}

		mHelper.applyConversion(videoUri, shouldConvert, targetFormat, shouldReduceQuality, targetQuality);
		return ExternalApiResult.SUCCESS_WAIT;
	}

	private void copyVideo(Uri videoUri) {
		FileUtils2.copyDataToFileAsync(getContext(), videoUri, getContext().getCacheDir(), mCopyDataToFileListener);
	}

	private final FileUtils2.CopyDataToFileListener mCopyDataToFileListener = (successful, destFile) -> {
		ApiAction apiAction = getAction();
		if (!successful) {
			ActionExecution.cancelCurrent(apiAction);
			return;
		}

		String actualVideoPath = mVideoUri.toString();
		mVideoUri = Uri.fromFile(destFile);
		if (!mHelper.applyConversion(mVideoUri.toString(), mShouldConvert, mTargetFormat, mShouldReduceQuality, mTargetQuality)) {
			Services.Log.debug("Returning original video since required parameters were not met");
			apiAction.setOutputValue(Expression.Value.newString(actualVideoPath));
			ActionExecution.continueCurrent(getActivity(), true, apiAction);
		}

		//If applyConversion returns True, it means that the conversion process has been started and
		//execution will resume the current ApiAction with the resulting video from ExecuteCallback
	};

	private void setVariables(boolean shouldReduceQuality, boolean shouldConvert, int targetQuality, int targetFormat) {
		mShouldReduceQuality = shouldReduceQuality;
		mShouldConvert = shouldConvert;
		mTargetQuality = targetQuality;
		mTargetFormat = targetFormat;
	}

	private void setCurrentAction() {
		mHelper.setCurrentAction(getAction());
	}
}
