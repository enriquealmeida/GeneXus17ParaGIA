package com.genexus.android.core.base.providers;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.Pair;

import com.genexus.android.core.base.application.IBusinessComponent;
import com.genexus.android.core.base.application.IGxObject;
import com.genexus.android.core.base.application.IProcedure;
import com.genexus.android.core.base.controls.MappedValue;
import com.genexus.android.core.base.model.Entity;
import com.genexus.android.core.base.utils.ResultDetail;
import com.genexus.android.core.common.IProgressListener;

public interface IApplicationServer
{
	/**
	 * Gets the implementation of the Business Component for this Application Server.
	 * @param name Business component name.
	 */
	IBusinessComponent getBusinessComponent(String name);

	/**
	 * Gets the implementation of the callable GxObject for this Application Server.
	 * @param name Object name.
	 */
	IGxObject getGxObject(String name);

	/**
	 * Gets the implementation of the callable GxObject for this Application Server.
	 * @param name Object name.
	 */
	IGxObject getGxObject(@NonNull String packageName, String name);

	/**
	 * Gets the implementation of the Procedure for this Application Server.
	 * @param name Procedure name.
	 */
	IProcedure getProcedure(String name);

	// Descriptor methods.
	boolean supportsCaching();

	// Operations on data providers.
	IDataSourceResult getData(GxUri uri, int sessionId, int start, int count, Date ifModifiedSince);
	Entity getData(GxUri uri, int sessionId);

	/**
	 * Call the service on the Application Server that calculates inferred attribute values, based on
	 * the values of other attributes (e.g. CountryName given a CountryId).
	 * @param service Service to invoke.
	 * @param input A dictionary of the determining values (e.g. {CountryId=1}).
	 * @return A list of the dependent values (e.g. [Swizterland, Swiss Confederation]).
	 */
	List<String> getDependentValues(String service, Map<String, String> input);

	/**
	 * Sends to the server binary data to be used in a multimedia variable or field later.
	 * @param file The file being uploaded.
	 * @param progressListener Progress listener.
	 * @return Binary reference (can be set as a BC attribute value, for example).
	 */
	@NonNull ResultDetail<String> uploadBinary(@NonNull File file, @NonNull String objectName, @Nullable IProgressListener progressListener) throws IOException;

	// Operations for dynamic combos
	List<Pair<String, String>> getDynamicComboValues(String serviceName, Map<String, String> input);

	// Operations for Suggest and/or Input Value Type Description.
	List<String> getSuggestions(String serviceName, Map<String, String> input);
	MappedValue getMappedValue(String serviceName, Map<String, String> input);
}
