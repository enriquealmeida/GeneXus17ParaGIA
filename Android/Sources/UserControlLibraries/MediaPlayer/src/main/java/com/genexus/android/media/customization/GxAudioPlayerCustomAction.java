package com.genexus.android.media.customization;

import com.genexus.android.core.base.model.Entity;

/**
 * Audio Player Custom Action
 */
public class GxAudioPlayerCustomAction
{
	private String mId;
	private String mTitle;
	private String mImage;

	public GxAudioPlayerCustomAction(Entity sdt)
	{
		mId = sdt.optStringProperty("Id");
		mTitle = sdt.optStringProperty("Title");
		mImage = sdt.optStringProperty("Image");
	}

	public String getId()
	{
		return mId;
	}

	public String getTitle()
	{
		return mTitle;
	}

	public String getImage()
	{
		return mImage;
	}
}
