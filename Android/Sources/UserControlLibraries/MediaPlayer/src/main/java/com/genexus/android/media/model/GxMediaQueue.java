package com.genexus.android.media.model;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.media.session.MediaSessionCompat;

import com.genexus.android.core.base.model.Entity;
import com.genexus.android.core.base.model.EntityFactory;
import com.genexus.android.core.base.model.EntityList;

/**
 * Media Queue.
 */
public class GxMediaQueue
{
	private String mTitle;
	private ArrayList<GxMediaItem> mItems;

	public GxMediaQueue()
	{
		mItems = new ArrayList<>();
	}

	public GxMediaQueue(Entity sdt)
	{
		this();
		mTitle = sdt.optStringProperty("Title");

		EntityList itemsCollection = sdt.getProperty(EntityList.class, "Items");
		if (itemsCollection != null)
		{
			for (Entity itemSdt : itemsCollection)
				mItems.add(new GxMediaItem(itemSdt));
		}
	}

	public static GxMediaQueue single(GxMediaItem item)
	{
		GxMediaQueue queue = new GxMediaQueue();
		queue.mItems.add(item);
		return queue;
	}

	public CharSequence getTitle()
	{
		return mTitle;
	}

	public List<GxMediaItem> getItems()
	{
		return mItems;
	}

	public List<MediaSessionCompat.QueueItem> toMediaSessionQueue()
	{
		ArrayList<MediaSessionCompat.QueueItem> queue = new ArrayList<>();

		int queuePosition = 1;
		for (GxMediaItem mediaItem : mItems)
			queue.add(mediaItem.toQueueItem(queuePosition++));

		return queue;
	}

	public Entity toSdt()
	{
		Entity sdt = EntityFactory.newSdt("GeneXus.SD.Media.MediaQueue");
		sdt.setProperty("Title", mTitle);

		EntityList sdtQueueItems = new EntityList();
		for (GxMediaItem item : mItems)
			sdtQueueItems.add(item.toSdt());

		sdt.setProperty("Items", sdtQueueItems);
		return sdt;
	}

	public GxMediaItem findItem(String mediaId)
	{
		for (GxMediaItem mediaItem : mItems)
		{
			if (mediaItem.getMediaId().equals(mediaId))
				return mediaItem;
		}

		return null;
	}
}
