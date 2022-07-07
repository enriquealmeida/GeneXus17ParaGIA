package com.genexus.android.core.controllers;

import com.genexus.android.core.base.model.Entity;
import com.genexus.android.core.base.model.EntityFactory;
import com.genexus.android.core.base.model.EntityList;
import com.genexus.android.core.base.model.EntityParentInfo;
import com.genexus.android.core.base.model.ValueCollection;
import com.genexus.android.core.base.providers.GxUri;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.common.DataRequest;

import static com.genexus.android.core.adapters.AdaptersHelper.VALUE_COLLECTION_IN_ENTITY;

public class ViewData
{
	private final GxUri mUri;
	private final String mResponseId;
	private final int mResponseSource;

	private EntityList mData;
	private final boolean mMoreAvailable;

	private final int mStatusCode;
	private final String mStatusMessage;

	private final boolean mDataUnchanged;

	private boolean mMoveToTop = false;

	ViewData(GxUri uri, String responseId, int responseSource, EntityList data, boolean moreAvailable, int statusCode, String statusMessage, boolean dataUnchanged)
	{
		mUri = uri;
		mResponseId = responseId;
		mResponseSource = responseSource;

		mData = data;
		mMoreAvailable = moreAvailable;
		mStatusCode = statusCode;
		mStatusMessage = statusMessage;
		mDataUnchanged = dataUnchanged;
	}

	public static ViewData memberData(ViewData sourceData, EntityList data)
	{
		// Member data should not have the same uri, because order, filters, &c are not the same.
		return new ViewData(null, sourceData.getResponseId(), sourceData.getResponseSource(), data, false, DataRequest.ERROR_NONE, Strings.EMPTY, false);
	}

	public static ViewData empty(boolean moreAvailable)
	{
		return new ViewData(null, Strings.EMPTY, DataRequest.RESULT_SOURCE_SERVER, new EntityList(), moreAvailable, DataRequest.ERROR_NONE, Strings.EMPTY, false);
	}

	public static ViewData customData(EntityList data, int responseSource)
	{
		return new ViewData(null, Strings.EMPTY, responseSource, data, false, DataRequest.ERROR_NONE, Strings.EMPTY, false);
	}

	public static ViewData customData(Entity data, int responseSource)
	{
		EntityList list = new EntityList(data.getDefinition());
		list.add(data);
		return customData(list, responseSource);
	}

	public static ViewData customData(ValueCollection valueCollection, int responseSource) {
		EntityList entities = new EntityList();
		for (String value : valueCollection) {
			Entity entity = EntityFactory.newEntity();
			entity.setProperty(VALUE_COLLECTION_IN_ENTITY, value);
			entities.addEntity(entity);
		}
		return customData(entities, responseSource);
	}

	@Override
	public String toString()
	{
		return String.format("<DATA Records: %s, Source: %s, More: %s, Status: %s>", mData.size(), mResponseSource, mMoreAvailable, mStatusMessage);
	}

	public GxUri getUri() { return mUri; }
	public String getResponseId() { return mResponseId; }
	public int getResponseSource() { return mResponseSource; }

	public int getCount() { return mData.size(); }
	public EntityList getEntities() { return mData; }
	public Entity getSingleEntity() { return (mData.size() != 0 ? mData.get(0) : null); }
	public void replaceEntities(EntityParentInfo parentInfo) {
		mData = new EntityList(mData, parentInfo);
	}
	public boolean isMoreAvailable() { return mMoreAvailable; }

	public int getStatusCode() { return mStatusCode; }
	public String getStatusMessage() { return mStatusMessage; }

	public boolean getDataUnchanged() { return mDataUnchanged; }

	public boolean hasErrors()
	{
		return mStatusCode != DataRequest.ERROR_NONE;
	}

	public void setMoveToTop(boolean moveToTop) { mMoveToTop = moveToTop; }
	public boolean getMoveToTop() { return mMoveToTop; }

}
