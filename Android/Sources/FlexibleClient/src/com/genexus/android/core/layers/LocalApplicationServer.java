package com.genexus.android.core.layers;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import android.net.Uri;
import android.util.Pair;

import com.genexus.android.core.base.application.IBusinessComponent;
import com.genexus.android.core.base.application.IGxObject;
import com.genexus.android.core.base.application.IProcedure;
import com.genexus.android.core.base.controls.MappedValue;
import com.genexus.android.core.base.metadata.DataProviderDefinition;
import com.genexus.android.core.base.metadata.GxObjectDefinition;
import com.genexus.android.core.base.metadata.IGxObjectDefinition;
import com.genexus.android.core.base.metadata.ProcedureDefinition;
import com.genexus.android.core.base.metadata.enums.Connectivity;
import com.genexus.android.core.base.model.Entity;
import com.genexus.android.core.base.providers.GxUri;
import com.genexus.android.core.base.providers.IApplicationServer;
import com.genexus.android.core.base.providers.IDataSourceResult;
import com.genexus.android.core.base.services.LogCategory;
import com.genexus.android.core.base.services.LogLevel;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.ResultDetail;
import com.genexus.android.core.common.IProgressListener;
import com.genexus.GXDbFile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

class LocalApplicationServer implements IApplicationServer
{
	private static final String LOG_TAG = "Genexus-DB";

	@Override
	public boolean supportsCaching()
	{
		// Local DP do not process, or return, "Last-Modified" / "If-Modified-Since" tags.
		return false;
	}

	@Override
	public IBusinessComponent getBusinessComponent(String name)
	{
		validateObjectConnectivity(Services.Application.getDefinition().getBusinessComponent(name));
		return new LocalBusinessComponent(name);
	}

	@Override
	public IGxObject getGxObject(String name)
	{
		GxObjectDefinition gxObjectDefinition = Services.Application.getDefinition().getGxObject(name);
		validateObjectConnectivity(gxObjectDefinition);

		if (gxObjectDefinition != null && gxObjectDefinition instanceof ProcedureDefinition)
			return new LocalProcedure(name);
		else if (gxObjectDefinition != null && gxObjectDefinition instanceof DataProviderDefinition)
			return new LocalDataProvider(name);
		else
			return new DummyObject(name);
	}

	@Override
	public IGxObject getGxObject(@NonNull String packageName, String name)
	{
		GxObjectDefinition gxObjectDefinition = Services.Application.getDefinition().getGxObject(name);
		validateObjectConnectivity(gxObjectDefinition);
		if (gxObjectDefinition != null && gxObjectDefinition instanceof ProcedureDefinition)
			return new LocalProcedure(packageName, name);

		return getGxObject(name);
	}

	@Override
	public IProcedure getProcedure(String name)
	{
		validateObjectConnectivity(Services.Application.getDefinition().getGxObject(name));
		return new LocalProcedure(name);
	}

	@Override
	public IDataSourceResult getData(GxUri uri, int sessionId, int start, int count, Date ifModifiedSince)
	{
		validateObjectConnectivity(uri.getDataSource().getParent());

		if (Services.Log.getLevel(LogCategory.DATA_DATABASE) >= LogLevel.INFO)
			Services.Log.debug(LogCategory.DATA_DATABASE, LOG_TAG, "Query: " + uri.getName() + "?" + uri.getQuery());

		LocalDataSource ds = new LocalDataSource(uri.getDataSource());
		return ds.execute(uri, sessionId, start, count);
	}

	@Override
	public Entity getData(GxUri uri, int sessionId)
	{
		validateObjectConnectivity(uri.getDataSource().getParent());
		return CommonUtils.getDataSingle(this, uri, sessionId);
	}

	@Override
	public @NonNull ResultDetail<String> uploadBinary(@NonNull File file, @NonNull String objectName, @Nullable IProgressListener progressListener) throws IOException
	{
		String blobBasePath = com.genexus.Preferences.getDefaultPreferences().getBLOB_PATH();

		// Path outside database, should be unique.
		String newFileName = GXDbFile.addTokenToFileName("binary", FilenameUtils.getExtension(file.getPath()));
		String newFilePath = blobBasePath + newFileName;

		File newFile = new File(newFilePath);

		FileUtils.copyFile(file, newFile);

		// This never fails, so always return OK.
		String fileUri = Uri.fromFile(newFile).toString();
		return ResultDetail.ok(fileUri);
	}

	@Override
	public List<String> getDependentValues(String service, Map<String, String> input)
	{
		return LocalServices.getDependentValues(service, input);
	}

	@Override
	public List<Pair<String, String>> getDynamicComboValues(String serviceName, Map<String, String> input)
	{
		return LocalServices.getDynamicComboValues(serviceName, input);
	}

	@Override
	public List<String> getSuggestions(String serviceName, Map<String, String> input)
	{
		return LocalServices.getSuggestions(serviceName, input);
	}

	@Override
	public MappedValue getMappedValue(String serviceName, Map<String, String> input)
	{
		return LocalServices.getMappedValue(serviceName, input);
	}

	private static void validateObjectConnectivity(IGxObjectDefinition gxObject)
	{
		// Sanity check.
		if (gxObject != null && gxObject.getConnectivitySupport() == Connectivity.Online)
			throw new IllegalStateException(String.format("Object %s only supports ONLINE but is being called via LocalApplicationServer.",
					gxObject.getName()));
	}
}
