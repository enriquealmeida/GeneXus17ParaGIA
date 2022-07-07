package com.genexus.android.media;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import android.util.Pair;

import com.genexus.android.R;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;

public class VideoUtils
{
	public static class YouTubeVideoInfo
	{
		private YouTubeVideoInfo(String id, Integer startTimeSeconds, Integer endTimeSeconds)
		{
			this.id = id;
			this.startTimeSeconds = startTimeSeconds;
			this.endTimeSeconds = endTimeSeconds;
		}

		public final String id;
		public final Integer startTimeSeconds;
		public final Integer endTimeSeconds;
	}

	private static final Pattern YOUTUBE_URL_PATTERN = Pattern.compile(
			"https?:\\/\\/(?:[0-9A-Z-]+\\.)?(?:youtu\\.be\\/|youtube\\.com\\S*[^\\w\\-\\s])([\\w\\-]{11})(?=[^\\w\\-]|$)(?![?=&+%\\w]*(?:['\"][^<>]*>|<\\/a>))[?=&+%\\w]*",
			Pattern.CASE_INSENSITIVE);

	public static boolean isYouTubeUrl(String url)
	{
		return url != null && YOUTUBE_URL_PATTERN.matcher(url).matches();
	}

	public static @Nullable YouTubeVideoInfo getYouTubeVideo(@NonNull String url)
	{
		String videoId = getYouTubeVideoId(url);
		if (videoId != null)
		{
			Pair<Integer, Integer> segment = getYouTubeVideoSegment(url);
			return new YouTubeVideoInfo(videoId, segment.first, segment.second);
		}
		else
			return null;
	}

	public static String getYouTubeVideoId(@NonNull String url)
	{
		Matcher matcher = YOUTUBE_URL_PATTERN.matcher(url);
		return matcher.find() ? matcher.group(1) : null;
	}

	private static @NonNull Pair<Integer, Integer> getYouTubeVideoSegment(@NonNull String url)
	{
		// Some known formats:
		// https://www.youtube.com/watch?v=frdj1zb9sMY&t=1m51s
		// https://youtu.be/frdj1zb9sMY?t=1m51s
		// https://www.youtube.com/embed/frdj1zb9sMY?start=111
		Uri uri = Uri.parse(url);
		Integer startSecond = Services.Strings.tryParseInt(uri.getQueryParameter("start"));
		Integer endSecond = Services.Strings.tryParseInt(uri.getQueryParameter("end"));

		if (startSecond == null)
		{
			String startTime = uri.getQueryParameter("t");
			if (Strings.hasValue(startTime))
			{
				// Convert something like "1h15m18s" into seconds.
				Pattern timePattern = Pattern.compile("^(\\d+h)?(\\d+m)?(\\d+s)?$");
				Matcher matcher = timePattern.matcher(startTime);
				if (matcher.matches())
				{
					int hours = parseTimeComponent(matcher.group(1));
					int minutes = parseTimeComponent(matcher.group(2));
					int seconds =  parseTimeComponent(matcher.group(3));

					int totalSeconds = hours * 3600 + minutes * 60 + seconds;
					if (totalSeconds > 0)
						startSecond = totalSeconds;
				}
			}
		}

		return new Pair<>(startSecond, endSecond);
	}

	private static int parseTimeComponent(String str)
	{
		if (Strings.hasValue(str))
		{
			// Remove the unit and parse the integer part.
			str = str.substring(0, str.length() - 1);
			return Services.Strings.tryParseInt(str, 0);
		}
		else
			return 0;
	}


	public static boolean openVideoIntent(final Context context, Uri videoUri) {
		final Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(videoUri, "video/*");

		List<ResolveInfo> appsList = context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
		boolean videoPlayersAvailable = appsList.size() > 0;

		if (videoPlayersAvailable) {
			new AlertDialog.Builder(context)
					.setTitle(Services.Strings.getResource(R.string.GXM_VideoErrorTitle))
					.setMessage(Services.Strings.getResource(R.string.GXM_VideoErrorMsg))
					.setPositiveButton(R.string.GXM_Yes, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int id) {
							context.startActivity(intent);
						}
					})
					.setNegativeButton(R.string.GXM_No, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int id) {
							dialog.dismiss();
						}
					})
					.show();
		}

		return videoPlayersAvailable;
	}
}
