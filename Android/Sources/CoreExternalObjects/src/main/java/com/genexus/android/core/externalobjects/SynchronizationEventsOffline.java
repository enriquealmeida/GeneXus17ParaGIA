package com.genexus.android.core.externalobjects;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.genexus.android.core.base.model.EntityList;
import com.genexus.android.core.base.model.PropertiesObject;
import com.artech.base.services.AndroidContext;
import com.artech.base.services.IEntity;
import com.genexus.android.core.base.services.Services;
import com.artech.base.synchronization.dps.SdtGxSynchroEventSDT_GxSynchroEventSDTItem;
import com.artech.base.synchronization.dps.deletependingeventsbyid;
import com.artech.base.synchronization.dps.getpendingeventbytimestamp;
import com.artech.base.synchronization.dps.markpendingeventsbyid;
import com.genexus.android.core.base.utils.ReflectionHelper;
import com.genexus.android.core.layers.LocalUtils;
import com.genexus.GXBaseCollection;

/**
 * This class allow access to SynchronizationEvents.
 */
@SuppressWarnings("unchecked")
public class SynchronizationEventsOffline {
	//private static final String OUTPUT_PARAMETER = "ReturnValue";

	// When call from inside proc shold not begintranscation , either commit it.
	public static boolean hasEvents(Integer status) {
		EntityList pendings = Services.Sync.getPendingEventsList(status.toString());
		return pendings.size() > 0;
	}


	// From client events
	public static EntityList getEventsLocal(Integer status) {
		return Services.Sync.getPendingEventsList(status.toString());
	}

	// When call from inside procedure should not beginTranscation , either commit it.
	public static GXBaseCollection getEvents(Integer status) {
		// change getpendingeventbytimestamp implementation and stop using  GxObjectCollection
		// must return a GXBaseCollection
		String className = "com.genexuscore.genexus.sd.synchronization.SdtSynchronizationEventList_SynchronizationEventListItem";
		Class<?> clazzGen = ReflectionHelper.getClass(Object.class, className);

		// Convert Object collection items from  to SdtGxSynchroEventSDT_GxSynchroEventSDTItem
		if (clazzGen != null) {
			Object sdttypedGen = ReflectionHelper.createDefaultInstance(clazzGen, true);
			if (sdttypedGen != null) {
				GXBaseCollection gxObjectCollection = new GXBaseCollection();
				// populate object colection from proc.
				//Only with this status

				//call DP to get pending events
				getpendingeventbytimestamp pendingEvent = new getpendingeventbytimestamp(Services.Application.get().getRemoteHandle());

				pendingEvent.execute(status.shortValue(), new GXBaseCollection[]{gxObjectCollection});

				GXBaseCollection gxBaseCollectionGen = new GXBaseCollection(clazzGen, "SynchronizationEventList.SynchronizationEventListItem", "SynchronizationEvents", Services.Application.get().getRemoteHandle());

				GXBaseCollection collectionBase = gxObjectCollection;
				for (int i = 0; i < collectionBase.size(); i++) {
					sdttypedGen = ReflectionHelper.createDefaultInstance(clazzGen, true);
					SdtGxSynchroEventSDT_GxSynchroEventSDTItem sdttyped = (SdtGxSynchroEventSDT_GxSynchroEventSDTItem) collectionBase.elementAt(i);

					// in root module
					IEntity objOutElement = AndroidContext.ApplicationContext.createEntity("genexus.sd.synchronization", "SynchronizationEventList.SynchronizationEventListItem", null);

					sdttyped.sdttoentity(objOutElement);

					Method methodGen = ReflectionHelper.getMethodEntity(clazzGen, "entitytosdt");
					if (methodGen != null) {
						try {
							methodGen.invoke(sdttypedGen, objOutElement);
						} catch (IllegalAccessException | InvocationTargetException e) {
						}
					}
					gxBaseCollectionGen.addBase(sdttypedGen);
				}

				// return collection converted if possible.
				return gxBaseCollectionGen;
			}

		}
		Services.Log.error("SdtGxSynchroEventSDT_GxSynchroEventSDTItem not found ");
		return null;
	}

	/*
	public static void markEventAsPendingLocal(String guid)
	{
		java.util.UUID guidVal = java.util.UUID.fromString(guid);
		markEventAsPending(guidVal, true);
	}
	*/

	// java.util.UUID
	public static void markEventAsPending(java.util.UUID guidVal) {
		markEventAsPending(guidVal, false);
	}

	public static void markEventAsPending(java.util.UUID guid, boolean applyTransaction) {
		PropertiesObject parameters = new PropertiesObject();

		//Only this event
		parameters.setProperty("PendingEventId", guid.toString());
		parameters.setProperty("PendingEventStatus", "1"); // Pending

		//EntityList resultData = new EntityList();

		//call Proc to mark pending events
		markpendingeventsbyid markPendingEvent = new markpendingeventsbyid(Services.Application.get().getRemoteHandle());

		if (applyTransaction)
			LocalUtils.beginTransaction();

		try {
			markPendingEvent.execute(parameters);

			// Commit?
			if (applyTransaction)
				LocalUtils.commit();
			// not read output
		} finally {
			if (applyTransaction)
				LocalUtils.endTransaction();
		}
	}


	// java.util.UUID
	public static void removeEvent(java.util.UUID guidVal) {
		removeEvent(guidVal, false);
	}

	// java.util.UUID
	public static void removeEvent(java.util.UUID guid, boolean applyTransaction) {
		PropertiesObject parameters = new PropertiesObject();

		//Only this event
		parameters.setProperty("PendingEventId", guid.toString());

		//EntityList resultData = new EntityList();

		//call Proc to mark pending events
		deletependingeventsbyid removePendingEvent = new deletependingeventsbyid(Services.Application.get().getRemoteHandle());

		if (applyTransaction)
			LocalUtils.beginTransaction();

		try {
			removePendingEvent.execute(parameters);

			// Commit?
			if (applyTransaction)
				LocalUtils.commit();
			// not read output
		} finally {
			if (applyTransaction)
				LocalUtils.endTransaction();
		}

	}
}
