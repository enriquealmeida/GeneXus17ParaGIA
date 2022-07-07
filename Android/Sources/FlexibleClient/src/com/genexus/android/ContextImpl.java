package com.genexus.android;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Locale;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.artech.base.services.IAndroidImageUtil;
import com.artech.base.services.IAndroidSession;
import com.artech.base.services.IBluetoothPrinter;
import com.artech.base.services.IContext;
import com.artech.base.services.IEntity;
import com.artech.base.services.IEntityList;
import com.artech.base.services.IPropertiesObject;
import com.genexus.android.core.actions.CallGxObjectAction;
import com.genexus.android.core.base.metadata.IDataSourceDefinition;
import com.genexus.android.core.base.metadata.LevelDefinition;
import com.genexus.android.core.base.metadata.StructureDataType;
import com.genexus.android.core.base.metadata.StructureDefinition;
import com.genexus.android.core.base.metadata.expressions.Expression;
import com.genexus.android.core.base.model.Entity;
import com.genexus.android.core.base.model.EntityFactory;
import com.genexus.android.core.base.model.EntityList;
import com.genexus.android.core.base.model.EntityParentInfo;
import com.genexus.android.core.base.model.PropertiesObject;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.common.HttpHeaders;
import com.genexus.android.core.common.ImageActionsHelper;
import com.genexus.android.core.common.StorageHelper;
import com.artech.synchronization.ISynchronizationHelper;
import com.genexus.android.reports.BluetoothPrinter;

public class ContextImpl implements IContext
{
	private Context mContext;

	public ContextImpl(Context context) {
		mContext = context;
	}

	public Context getContext() {
		return mContext;
	}

	@Override
	public void saveMinorVersion(String prefFileName,  long value) {
		SharedPreferences settings = mContext.getSharedPreferences(prefFileName, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putLong("MinorVersion", value);
		editor.commit();
	}

	@Override
	public long getMinorVersion(String prefFileName, long def)
	{
		SharedPreferences settings = mContext.getSharedPreferences(prefFileName, 0);
		// SharedPreferences.Editor editor = settings.edit();
		return settings.getLong("MinorVersion", def);
	}

	@Override
	public String getAssetsFontPath(String fontName) {
		return Services.Assets.copyAssetFontToStorage(fontName);
	}

	@Override
	public int getResource(String data, String namespace) {
		return mContext.getResources().getIdentifier(data, namespace, mContext.getPackageName());
		//return 0;
	}

	@Override
	public int getDataImageResourceId(String imageUri)
	{
		return Services.Resources.getDataImageResourceId(imageUri);
	}
	
	@Override
	public InputStream openFileInput(String name)
	{
		try
		{
			return mContext.openFileInput(name);
		}
		catch (FileNotFoundException e)
		{
			return null;
		}
	}

	@Override
	public InputStream openRawResource(int arg0) {
		return mContext.getResources().openRawResource(arg0);
	}

	@Override
	public InputStream getResourceStream(String data, String namespace) {
		int resId = getResource(data, namespace);
		if (resId!=0)
			return openRawResource(resId);
		return null;
	}

	@Override
	public @NonNull
	String getDataBaseFilePath()
	{
		File dbDirectory = StorageHelper.getStorageDirectory("db", useExternalStorage());
		String oldPath = dbDirectory.getAbsolutePath() + "/" + Services.Application.get().getName() + ".sqlite";
		// if exists use old name
		File oldFile = new File(oldPath);
		if (oldFile.exists())
			return oldPath;
		// new name
		return dbDirectory.getAbsolutePath() + "/" + Services.Application.get().getAppEntry().toLowerCase(Locale.US) + ".sqlite";
	}

	@Override
	public @NonNull String getDataBaseSyncFilePath()
	{
		File dbDirectory = StorageHelper.getStorageDirectory("db", useExternalStorage());
		return dbDirectory.getAbsolutePath() + "/" + Services.Application.get().getAppEntry().toLowerCase(Locale.US) + "_sync" + ".data";
	}

	@Override
	public @NonNull String getDataBaseSyncHashesFilePath()
	{
		File dbDirectory = StorageHelper.getStorageDirectory("db", useExternalStorage());
		return dbDirectory.getAbsolutePath() + "/" + Services.Application.get().getAppEntry().toLowerCase(Locale.US) + "_hashes" + ".json";
	}

	@Override
	public @NonNull String getFilesSubApplicationDirectory(String directoryName)
	{
		File blobsUploadDirectory = StorageHelper.getStorageDirectory(directoryName, useExternalStorage());
		return blobsUploadDirectory.getAbsolutePath();
	}
	
	@Override
	public @NonNull String getFilesBlobsApplicationDirectory()
	{
		File blobsDirectory = StorageHelper.getStorageDirectory("blobs", useExternalStorage());
		return blobsDirectory.getAbsolutePath();
	}

	@Override
	public String getApplicationDataPath()
	{
		return StorageHelper.getApplicationDataPath();
	}
	
	@Override
	public String getTemporaryFilesPath()
	{
		return StorageHelper.getTemporaryFilesPath();
	}
	
	@Override
	public String getExternalFilesPath()
	{
		return StorageHelper.getExternalFilesPath();
	}
	
	private static boolean useExternalStorage()
	{
		return (!Services.Application.get().getUseInternalStorageForDatabase());
	}

	@Override
	public IPropertiesObject getEmptyPropertiesObject()
	{
		return new PropertiesObject();
	}

	@Override
	public IEntity createEntity(String module, String name, IEntity parent)
	{
		// This function can be used with or without a parent, it was programed this way in the begining
		Entity entity = createEntity(module, name);
		if (parent != null)
			entity.addParentInfo(EntityParentInfo.memberOf((Entity)parent, name));
		return entity;
	}

	@Override
	public IEntity createEntity(String module, String name, IEntity parent, IEntityList collection)
	{
		// In this function the parent is required, it is a new function with that convention
		Entity entity = createEntity(module, name);
		entity.addParentInfo(EntityParentInfo.collectionMemberOf((Entity)parent, name, (EntityList)collection));
		return entity;
	}

	@Override
	public IEntityList createEntityList()
	{
		EntityList list = new EntityList();
		list.setItemType(Expression.Type.SDT);
		return list;
	}

	@Override
	public IPropertiesObject createPropertyObject()
	{
		return new PropertiesObject();
	}

	@Override
	public IPropertiesObject runGxObjectFromProcedure(String objectToCall, IPropertiesObject parameters)
	{
		return CallGxObjectAction.runGxObjectFromProcedure(objectToCall, (PropertiesObject)parameters);
	}

	// cache of splits names, improve performance temporary.
	private static transient HashMap splitCacheNames = new LinkedHashMap();

	private Entity createEntity(String module, String name)
	{
		// "name" can be a DP, BC or SDT structure name, or an inner part thereof.
		//String[] nameComponents = Services.Strings.split(name, Strings.DOT);
		String[] nameComponents = (String[])splitCacheNames.get(name);
		if (nameComponents==null)
		{
			nameComponents = Services.Strings.split(name, Strings.DOT);
			splitCacheNames.put(name, nameComponents);
		}

		String rootName = nameComponents[0];

		if (Strings.hasValue(module))
		{
			rootName = module + "." + rootName;
		}
		
		// Get the main definition first...
		StructureDefinition structure = getStructureDefinition(rootName);
		if (structure != null)
		{
			if (nameComponents.length > 1)
			{
				// ... then look up the inner part (e.g. SDT subitem, BC level).
				String[] innerNames = new String[nameComponents.length - 1];
				System.arraycopy(nameComponents, 1, innerNames, 0, nameComponents.length - 1);

				LevelDefinition level = structure.Root;
				for (int i = 0; level != null && i < innerNames.length ; i++)
					level = level.getLevel(innerNames[i]);

				if (level != null)
					return EntityFactory.newEntity(structure, level); // Inner-level structure.

			}
			else
				return EntityFactory.newEntity(structure); // Root-level structure.
		}

		Services.Log.error(String.format("Name '%s' not found as a structure in module '%s'. Returning generic entity.", name, module));
		return EntityFactory.newEntity();
	}

	private StructureDefinition getStructureDefinition(String name)
	{
		IDataSourceDefinition dataSource = Services.Application.getDefinition().getDataSource(name);
		if (dataSource != null)
			return dataSource.getStructure();

		StructureDefinition bc = Services.Application.getDefinition().getBusinessComponent(name);
		if (bc != null)
			return bc;

		StructureDataType sdt = Services.Application.getDefinition().getSDT(name);
		if (sdt != null)
			return sdt.getStructure();

		return null;
	}

	private static AndroidSession androidSession = new AndroidSession();

	@Override
	public IAndroidSession getAndroidSession()
	{
		return androidSession;
	}

	@Override
	public IAndroidImageUtil getAndroidImageUtil() {
		return new ImageActionsHelper();
	}

	@Override
	public boolean getUseUtcConversion()
	{
		if (Services.Application != null && Services.Application.getDefinition().getPatternSettings() != null)
			return Services.Application.getDefinition().getPatternSettings().useUtcConversion();
		else
			return true;
	}

	@Override
	public int getRemoteHandle()
	{
		return Services.Application.get().getRemoteHandle();
	}

	@Override
	public String makeImagePath(String imagePartialPath)
	{
		return Services.Application.get().UriMaker.getImageUrl(imagePartialPath);
	}

	@Override
	public String getBaseImagesUri()
	{
		return Services.Application.get().UriMaker.getBaseImagesUrl();
	}

	@Override
	public String getRootUri()
	{
		return Services.Application.get().UriMaker.getRootUrl();
	}
	
	@Override
	public boolean getSynchronizerSavePendingEvents()
	{
		return Services.Application.get().getSynchronizerSavePendingEvents();
	}

	@Override
	public String getLanguageName()
	{
		return Services.Language.getCurrentLanguage();
	}

	@Override
	public ISynchronizationHelper getSynchronizationHelper() {
		return Services.Sync;
	}

	@Override
	public void addSDHeaders(String host, String baseUrl, Hashtable<String, String> headerToSend) {
		HttpHeaders.addSDHeader(host, baseUrl, headerToSend);
	}

	@Override
	public IBluetoothPrinter getBluetoothPrinter() {
		return new BluetoothPrinter();
	}
}
