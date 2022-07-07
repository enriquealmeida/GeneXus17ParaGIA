package com.genexus.android;

import java.util.ArrayList;
import java.util.WeakHashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;

import com.genexus.android.core.base.utils.Function;

/**
 * Manager of resources per activity.
 * Resources are API components bound to an Activity that must receive notifications from it (e.g. ProgressIndicator,
 * ActivityAudio, &c). Methods are called on the corresponding lifecycle events of their owner activities.
 */
public class ActivityResources
{
	private static final Object LOCK = new Object();
	private static ArrayList<IActivityResource> sApplicationResources = new ArrayList<>();
	private static WeakHashMap<Activity, ArrayList<IActivityResource>> sActivityResources = new WeakHashMap<>();
	private static WeakHashMap<Activity, Bundle> sSavedInstanceStates = new WeakHashMap<>();
	
	/**
	 * Adds a resource that will participate in the lifecycle events for ALL activities.
	 */
	public static void addApplicationResource(IActivityResource applicationResource)
	{
		sApplicationResources.add(applicationResource);
	}

	/**
	 * Tries to get the resource of the specified class associated to the activity.
	 * @param activity Activity that manages the resource.
	 * @param t Resource type.
	 * @return The previously existing resource, or null.
	 */
	public static <ResourceT extends IActivityResource> ResourceT getResource(Activity activity, Class<ResourceT> t)
	{
		synchronized (LOCK)
		{
			ArrayList<IActivityResource> resources = sActivityResources.get(activity);
			if (resources != null)
			{
				for (IActivityResource resource : resources)
					if (t.isInstance(resource))
						return t.cast(resource);
			}

			return null;
		}
	}

	/**
	 * Tries to get the resource of the specified class associated to the activity.
	 * If the resource is not present and a factory function is provided, it will be created and added.
	 * @param activity Activity that manages the resource.
	 * @param t Resource type.
	 * @param resourceFactory Function to create the resource if it doesn't exist yet. Can be null.
	 * @return The previously existing resource, or a new one if a factory is provided, or null.
	 */
	public static <ResourceT extends IActivityResource> ResourceT getResource(Activity activity, Class<ResourceT> t, Function<Activity, ResourceT> resourceFactory)
	{
		synchronized (LOCK)
		{
			ArrayList<IActivityResource> resources = sActivityResources.get(activity);
			if (resources != null)
			{
				for (IActivityResource resource : resources)
					if (t.isInstance(resource))
						return t.cast(resource);
			}

			// Resource is not present, create it if a factory was provider.
			if (resourceFactory != null)
			{
				ResourceT newResource = resourceFactory.run(activity);
				setResource(activity, t, newResource);
				return newResource;
			}

			return null;
		}
	}

	/**
	 * Associates a previously created resource to the activity.
	 * @param activity Activity that manages the resource.
	 * @param t Resource type.
	 * @param resource Resource instance.
	 */
	public static <ResourceT extends IActivityResource> void setResource(Activity activity, Class<ResourceT> t, ResourceT resource)
	{
		synchronized (LOCK)
		{
			ArrayList<IActivityResource> resources = sActivityResources.get(activity);
			if (resources == null)
			{
				resources = new ArrayList<>();
				sActivityResources.put(activity, resources);
			}

			Bundle savedInstanceState = sSavedInstanceStates.get(activity);
			resource.onCreate(activity, savedInstanceState);
			resources.add(resource);
		}
	}

	/**
	 * Save an Activity state. Activity receives the state onCreate and this state is used
	 * to create controls that need to know this state.
	 */
	public static void onCreate(final Activity activity, final Bundle savedInstanceState)
	{
		synchronized (LOCK)
		{
			// This will fire for application-wide resources only.
			onActivityEvent(activity, new Function<IActivityResource, Void>()
			{
				@Override
				public Void run(IActivityResource input)
				{
					input.onCreate(activity, savedInstanceState);
					return null;
				}
			});

			// Store the savedInstanceState for resources that are added later and need it.
			sSavedInstanceStates.put(activity, savedInstanceState);
		}
	}

	/**
	 * Notifies all the activity's resources about its onStart() life-cycle event.
	 */
	public static void onStart(final Activity activity)
	{
		onActivityEvent(activity, new Function<IActivityResource, Void>()
		{
			@Override
			public Void run(IActivityResource input)
			{
				input.onStart(activity);
				return null;
			}
		});
	}

	/**
	 * Notifies all the activity's resources about its onResume() life-cycle event.
	 */
	public static void onResume(final Activity activity)
	{
		onActivityEvent(activity, new Function<IActivityResource, Void>()
		{
			@Override
			public Void run(IActivityResource input)
			{
				input.onResume(activity);
				return null;
			}
		});
	}

	/**
	 * Notifies all the activity's resources about its onPause() life-cycle event.
	 */
	public static void onPause(final Activity activity)
	{
		onActivityEvent(activity, new Function<IActivityResource, Void>()
		{
			@Override
			public Void run(IActivityResource input)
			{
				input.onPause(activity);
				return null;
			}
		});
	}

	/**
	 * Notifies all the activity's resources about its onStop() life-cycle event.
	 */
	public static void onStop(final Activity activity)
	{
		onActivityEvent(activity, new Function<IActivityResource, Void>()
		{
			@Override
			public Void run(IActivityResource input)
			{
				input.onStop(activity);
				return null;
			}
		});
	}

	/**
	 * Notifies all the activity's resources about its onDestroy() life-cycle event.
	 */
	public static void onDestroy(final Activity activity)
	{
		onActivityEvent(activity, new Function<IActivityResource, Void>()
		{
			@Override
			public Void run(IActivityResource input)
			{
				input.onDestroy(activity);
				return null;
			}
		});

		// After running all resource's onDestroy(), release them (they won't be called again).
		sActivityResources.remove(activity);
	}

	/**
	 * Notifies all the activity's resources about its onNewIntent() life-cycle event.
	 */
	public static void onNewIntent(final Activity activity, final Intent intent)
	{
		onActivityEvent(activity, new Function<IActivityResource, Void>()
		{
			@Override
			public Void run(IActivityResource input)
			{
				input.onNewIntent(activity, intent);
				return null;
			}
		});
	}

	/**
	 * Notifies all the activity's resources about its onSaveInstanceState() life-cycle event.
	 */
	public static void onSaveInstanceState(final Activity activity, final Bundle outState)
	{
		onActivityEvent(activity, new Function<IActivityResource, Void>()
		{
			@Override
			public Void run(IActivityResource input)
			{
				input.onSaveInstanceState(activity, outState);
				return null;
			}
		});
	}

	/**
	 * Notifies all the activity's resources about its onActivityResult() life-cycle event.
	 */
	public static void onActivityResult(final Activity activity,final int requestCode, final int resultCode, final Intent data)
	{
		onActivityEvent(activity, new Function<IActivityResource, Void>()
		{
			@Override
			public Void run(IActivityResource input)
			{
				input.onActivityResult(activity, requestCode, resultCode, data);
				return null;
			}
		});
	}

	private static <T> void onActivityEvent(Activity activity, Function<IActivityResource, T> event)
	{
		synchronized (LOCK)
		{
			ArrayList<IActivityResource> resources = getActivityResources(activity);
			for (IActivityResource resource : resources)
				event.run(resource);
		}
	}

	private static @NonNull ArrayList<IActivityResource> getActivityResources(Activity activity)
	{
		ArrayList<IActivityResource> resources = sActivityResources.get(activity);

		// Always make a copy of the returned collection, because we will add the global ones.
		if (resources != null)
			resources = new ArrayList<>(resources);
		else
			resources = new ArrayList<>();

		resources.addAll(0, sApplicationResources);
		return resources;
	}
}
