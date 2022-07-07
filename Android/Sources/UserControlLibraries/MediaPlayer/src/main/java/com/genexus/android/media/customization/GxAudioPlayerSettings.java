package com.genexus.android.media.customization;

import com.genexus.android.core.base.model.Entity;
import com.genexus.android.core.base.model.EntityList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Audio Player Settings
 */
public class GxAudioPlayerSettings
{
	private final boolean mSupportFavorite;
	private final boolean mSupportRepeat;
	private final boolean mSupportShuffle;
	private final List<GxAudioPlayerCustomAction> mCustomActions;

	public static final GxAudioPlayerSettings DEFAULT = new GxAudioPlayerSettings();

	private GxAudioPlayerSettings()
	{
		mSupportFavorite = false;
		mSupportRepeat = false;
		mSupportShuffle = false;
		mCustomActions = Collections.emptyList();
	}

	public GxAudioPlayerSettings(Entity sdt)
	{
		mSupportFavorite = sdt.optBooleanProperty("SupportsFavorite");
		mSupportRepeat = sdt.optBooleanProperty("SupportsRepeat");
		mSupportShuffle = sdt.optBooleanProperty("SupportsShuffle");
		mCustomActions = new ArrayList<>();

		EntityList actionsCollection = sdt.getProperty(EntityList.class, "CustomActions");
		if (actionsCollection != null)
		{
			for (Entity actionSdt : actionsCollection)
				mCustomActions.add(new GxAudioPlayerCustomAction(actionSdt));
		}
	}

	public boolean getSupportFavorite()
	{
		return mSupportFavorite;
	}

	public boolean getSupportRepeat()
	{
		return mSupportRepeat;
	}

	public boolean getSupportShuffle()
	{
		return mSupportShuffle;
	}

	public List<GxAudioPlayerCustomAction> getCustomActions()
	{
		return mCustomActions;
	}
}
