package com.genexus.android.core.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;

import com.artech.base.services.IAndroidImageUtil;
import com.genexus.android.core.activities.ActivityHelper;
import com.genexus.android.core.base.metadata.expressions.Expression;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.utils.FileUtils2;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.genexus.android.core.utils.FileUtils2.saveBitmapToFile;

public class ImageActionsHelper implements IAndroidImageUtil {

	private static final String PROPERTY_HEIGHT = "ImageHeight";
	private static final String PROPERTY_WIDTH = "ImageWidth";
	private static final String PROPERTY_SIZE = "FileSize";
	private static final String PROPERTY_URI = "ImageURI";
	private static final String PROPERTY_NAME = "ImageName";
	private static final String PROPERTY_TYPE = "ImageType";

	private static final String METHOD_SET_EMPTY = "SetEmpty";
	private static final String METHOD_IS_EMPTY = "IsEmpty";
	private static final String METHOD_FROM_URL = "FromURL";
	private static final String METHOD_FROM_IMAGE = "FromImage";
	private static final String METHOD_CROP = "Crop";
	private static final String METHOD_FLIP_HORIZONTALLY = "FlipHorizontally";
	private static final String METHOD_FLIP_VERTICALLY = "FlipVertically";
	private static final String METHOD_RESIZE = "Resize";
	private static final String METHOD_ROTATE = "Rotate";
	private static final String METHOD_SCALE = "Scale";

	public static Expression.Value handleImageProperty(Context context, Expression.Value value, String property) {

		if (PROPERTY_URI.equalsIgnoreCase(property))
			return Expression.Value.newString(value.coerceToString());
		else if (PROPERTY_NAME.equalsIgnoreCase(property)) {
			String name = FileUtils2.getFileName(value.coerceToString());
			return Expression.Value.newString(name);
		} else if (PROPERTY_TYPE.equalsIgnoreCase(property)) {
			String type = FileUtils2.getFileExtension(value.coerceToString());
			return Expression.Value.newString(type);
		}

		Long calculatedValue = null;
		Bitmap bitmap = getBitmap(context, value.coerceToString());
		if (bitmap == null)
			return Expression.Value.newInteger(0);
		else {
			if (PROPERTY_HEIGHT.equalsIgnoreCase(property))
				calculatedValue = (long) getBitmapHeight(bitmap);
			else if (PROPERTY_WIDTH.equalsIgnoreCase(property))
				calculatedValue = (long) getBitmapWidth(bitmap);
			else if (PROPERTY_SIZE.equalsIgnoreCase(property))
				calculatedValue = getBitmapSize(context, value.coerceToString());
		}

		if (calculatedValue != null)
			return Expression.Value.newInteger(calculatedValue);

		throw new IllegalArgumentException(String.format("Unknown property ('%s').", property));
	}

	public static Expression.Value handleImageMethod(Context context, Expression.Value value, String method, List<Expression.Value> parameters) {

		if ((METHOD_FROM_URL.equalsIgnoreCase(method) || METHOD_FROM_IMAGE.equalsIgnoreCase(method)) && parameters.size() == 1) {
			return Expression.Value.newString(parameters.get(0).coerceToString());
		} else if (METHOD_IS_EMPTY.equalsIgnoreCase(method))
			return Expression.Value.newBoolean(value.coerceToString().trim().isEmpty());
		else if (METHOD_SET_EMPTY.equalsIgnoreCase(method))
			return Expression.Value.newString(Strings.EMPTY);

		Bitmap modifiedBitmap = null;
		Bitmap bitmap = getBitmap(context, value.coerceToString());
		if (bitmap == null)
			return Expression.Value.newString(Strings.EMPTY);
		else {
			try {
				if (METHOD_CROP.equalsIgnoreCase(method) && parameters.size() == 4) {
					int left = parameters.get(0).coerceToInt();
					int top = parameters.get(1).coerceToInt();
					int width = parameters.get(2).coerceToInt();
					int height = parameters.get(3).coerceToInt();
					modifiedBitmap = crop(bitmap, left, top, width, height);
				} else if (METHOD_FLIP_HORIZONTALLY.equalsIgnoreCase(method) && parameters.size() == 0) {
					modifiedBitmap = flipHorizontally(bitmap);
				} else if (METHOD_FLIP_VERTICALLY.equalsIgnoreCase(method) && parameters.size() == 0) {
					modifiedBitmap = flipVertically(bitmap);
				} else if (METHOD_RESIZE.equalsIgnoreCase(method) && parameters.size() == 3) {
					int width = parameters.get(0).coerceToInt();
					int height = parameters.get(1).coerceToInt();
					boolean keepAspect = parameters.get(2).coerceToBoolean();
					modifiedBitmap = resize(bitmap, width, height, keepAspect);
				} else if (METHOD_ROTATE.equalsIgnoreCase(method) && parameters.size() == 1) {
					int angle = parameters.get(0).coerceToInt();
					modifiedBitmap = rotate(bitmap, angle);
				} else if (METHOD_SCALE.equalsIgnoreCase(method) && parameters.size() == 1) {
					int percent = parameters.get(0).coerceToInt();
					modifiedBitmap = scale(bitmap, percent);
				} else {
					throw new IllegalArgumentException(String.format("Unknown method (%s/%s).", method, parameters.size()));
				}
			} catch (IllegalArgumentException exception) {
				Services.Log.error(String.format("Image manipulation method %s failed. Returning same image", method), exception);
			}
		}

		if (modifiedBitmap != null) {
			String imagePath = saveBitmapToFile(context, modifiedBitmap, getFileExtension(value.coerceToString()));
			if (Strings.hasValue(imagePath))
				return Expression.Value.newString(imagePath);
		}

		return Expression.Value.newString(value.coerceToString());
	}

	private static Bitmap crop(Bitmap bitmap, int left, int top, int width, int height) {
		if (width > 0 && height > 0) {
			if (top < bitmap.getHeight() && left < bitmap.getWidth()) {
				if (width > (bitmap.getWidth() - left))
					width = bitmap.getWidth() - left;

				if (height > bitmap.getHeight() - top)
					height = bitmap.getHeight() - top;

				return Bitmap.createBitmap(bitmap, left, top, width, height);
			}
		} else {
			Services.Log.debug("Crop returning same bitmap since width and height must be greater than zero.");
			return bitmap;
		}

		throw new IllegalArgumentException("Parameters out of Crop boundaries");
	}

	private static Bitmap flipHorizontally(Bitmap bitmap) {
		Matrix matrix = new Matrix();
		matrix.postScale(-1f, 1f);
		return Bitmap.createBitmap(bitmap, 0,0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
	}

	private static Bitmap flipVertically(Bitmap bitmap) {
		Matrix matrix = new Matrix();
		matrix.postScale(1f, -1f);
		return Bitmap.createBitmap(bitmap, 0,0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
	}

	private static Bitmap resize(Bitmap bitmap, int width, int height, boolean keepAspect) {
		if (width > 0 && height > 0) {
			if (keepAspect)
				return Bitmap.createScaledBitmap(bitmap, width, height, true);
			else
				return Bitmap.createBitmap(bitmap, 0, 0, width, height);
		} else {
			Services.Log.debug("Resize returning same bitmap since width and height must be greater than zero.");
			return bitmap;
		}
	}

	private static Bitmap rotate(Bitmap bitmap, int angle) {
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
	}

	private static Bitmap scale(Bitmap bitmap, int percent) {
		int newWidth = bitmap.getWidth() / 100 * percent;
		int newHeight = bitmap.getHeight() / 100 * percent;
		return resize(bitmap, newWidth, newHeight, true);
	}

	private static int getBitmapHeight(Bitmap bitmap) {
		return bitmap.getHeight();
	}

	private static int getBitmapWidth(Bitmap bitmap) {
		return bitmap.getWidth();
	}

	private static long getBitmapSize(Context context, String filePath) {
		return Services.Images.getImageFile(context, filePath).length();
	}

	private static Bitmap getBitmap(Context context, String imageUrl) {
		return Services.Images.getBitmap(context, imageUrl);
	}

	private static String getFileExtension(String path) {
		int index = path.lastIndexOf(".");
		if (index != -1)
			return path.substring(index);
		else
			return Strings.EMPTY;
	}

	// method for Offline use, from Gx Procedure (IAndroidImageUtil).
	@Override
	public long getFileSize(String imageFile) {
		return handleImageProperty(getActivityContext(), Expression.Value.newString(imageFile), PROPERTY_SIZE).coerceToNumber().longValue();
	}

	@Override
	public int getImageHeight(String imageFile) {
		return handleImageProperty(getActivityContext(), Expression.Value.newString(imageFile), PROPERTY_HEIGHT).coerceToInt();
	}

	@Override
	public int getImageWidth(String imageFile) {
		return handleImageProperty(getActivityContext(), Expression.Value.newString(imageFile), PROPERTY_WIDTH).coerceToInt();
	}

	@Override
	public String crop(String imageFile, int x, int y, int width, int height) {
		List<Expression.Value> parameters = Arrays.asList(Expression.Value.newInteger(x),Expression.Value.newInteger(y),Expression.Value.newInteger(width),Expression.Value.newInteger(height));
		return handleImageMethod(getActivityContext(), Expression.Value.newString(imageFile), METHOD_CROP, parameters ).coerceToString();
	}

	@Override
	public String flipHorizontally(String imageFile) {
		return handleImageMethod(getActivityContext(), Expression.Value.newString(imageFile), METHOD_FLIP_HORIZONTALLY, Collections.EMPTY_LIST).coerceToString();
	}

	@Override
	public String flipVertically(String imageFile) {
		return handleImageMethod(getActivityContext(), Expression.Value.newString(imageFile), METHOD_FLIP_VERTICALLY, Collections.EMPTY_LIST).coerceToString();
	}

	@Override
	public String resize(String imageFile, int width, int height, boolean keepAspectRatio) {
		List<Expression.Value> parameters = Arrays.asList(Expression.Value.newInteger(width),Expression.Value.newInteger(height),Expression.Value.newBoolean(keepAspectRatio));
		return handleImageMethod(getActivityContext(), Expression.Value.newString(imageFile), METHOD_RESIZE, parameters ).coerceToString();
	}

	@Override
	public String scale(String imageFile, short percent) {
		return handleImageMethod(getActivityContext(), Expression.Value.newString(imageFile), METHOD_SCALE, Collections.singletonList(Expression.Value.newInteger(percent))).coerceToString();
	}

	@Override
	public String rotate(String imageFile, short angle) {
		return handleImageMethod(getActivityContext(), Expression.Value.newString(imageFile), METHOD_ROTATE, Collections.singletonList(Expression.Value.newInteger(angle))).coerceToString();
	}

	private static Context getActivityContext() {
		// need activity context to use ImageLoader
		return ActivityHelper.getCurrentActivity();
	}
}
