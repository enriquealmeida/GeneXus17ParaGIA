package com.genexus.android.core.framework;

import android.content.Context;

import com.genexus.android.core.activities.IIntentHandler;
import com.genexus.android.core.activities.IntentHandlers;
import com.genexus.android.core.base.services.IMaps;
import com.genexus.android.core.controls.maps.common.IMapsProvider;
import com.genexus.android.core.externalapi.ExternalApiDefinition;
import com.genexus.android.core.externalapi.ExternalApiFactory;
import com.genexus.android.core.usercontrols.UcFactory;
import com.genexus.android.core.usercontrols.UserControlDefinition;

/**
 * A "Module" is a component of the application that is not part of FlexibleClient but adds
 * functionality to applications (such as new user controls, external object implementations,
 * intent handlers, &c).
 */
public interface GenexusModule
{
	/**
	 * This method will be called at application start-up, before metadata has been loaded.
	 * From here you should call methods to register external objects, user controls, &c.
	 * Some methods to be aware of:
	 * <ul>
	 *     <li>{@link ExternalApiFactory#addApi(ExternalApiDefinition)} </li>
	 *     <li>{@link UcFactory#addControl(UserControlDefinition)}</li>
	 *     <li>{@link IMaps#addProvider(IMapsProvider)}</li>
	 *     <li>{@link IntentHandlers#addHandler(IIntentHandler)}</li>
	 * </ul>
	 *
	 * @param context Application context.
	 */
	void initialize(Context context);
}
