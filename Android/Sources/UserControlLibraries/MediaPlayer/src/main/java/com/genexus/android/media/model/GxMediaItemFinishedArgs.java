package com.genexus.android.media.model;

import android.content.Intent;

import com.genexus.android.core.base.model.Entity;
import com.genexus.android.core.base.model.EntityFactory;
import com.genexus.android.core.base.utils.Strings;

/**
 * Media item finished event args.
 */
public class GxMediaItemFinishedArgs
{
	public static final int FINISH_REASON_UNKNOWN = 0;
	public static final int FINISH_REASON_PLAYBACK_COMPLETE = 1;
	public static final int FINISH_REASON_STOPPED = 2;
	public static final int FINISH_REASON_SKIPPED = 3;
	public static final int FINISH_REASON_QUEUE_REPLACED = 4;

	public static final String FIELD_ITEM_ID = "ItemId";
	public static final String FIELD_QUEUE_POSITION = "QueuePosition";
	public static final String FIELD_TRACK_POSITION = "TrackPosition";
	public static final String FIELD_REASON = "Reason";

	private final String mItemId;
	private final long mQueuePosition;
	private final long mTrackPosition;
	private final int mReason;

	public GxMediaItemFinishedArgs(Intent intent)
	{
		String itemId = intent.getStringExtra(FIELD_ITEM_ID);
		if (itemId == null)
			itemId = Strings.EMPTY;

		mItemId = itemId;
		mQueuePosition = intent.getLongExtra(FIELD_QUEUE_POSITION, 0);
		mTrackPosition = intent.getLongExtra(FIELD_TRACK_POSITION, 0);
		mReason = intent.getIntExtra(FIELD_REASON, FINISH_REASON_UNKNOWN);
	}

	@Override
	public String toString()
	{
		return String.format("QueueItemFinished {ItemId:%s, QueuePosition:%s, TrackPosition:%s, Reason:%s}",
				mItemId,
				mQueuePosition,
				mTrackPosition,
				mReason);
	}

	public Entity toSdt()
	{
		Entity sdt = EntityFactory.newSdt("GeneXus.SD.Media.MediaItemFinishedInfo");
		sdt.setProperty(FIELD_ITEM_ID, mItemId);
		sdt.setProperty(FIELD_QUEUE_POSITION, mQueuePosition);
		sdt.setProperty(FIELD_TRACK_POSITION, mTrackPosition);
		sdt.setProperty(FIELD_REASON, mReason);
		return sdt;
	}
}
