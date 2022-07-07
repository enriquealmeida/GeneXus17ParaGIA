package com.genexus.android.core.base.metadata;

import java.lang.ref.WeakReference;
import java.util.TreeMap;

import androidx.collection.LruCache;

import com.genexus.android.core.base.metadata.images.ImageCatalog;
import com.genexus.android.core.base.metadata.languages.LanguageCatalog;
import com.genexus.android.core.base.metadata.loader.MetadataLoader;
import com.genexus.android.core.base.metadata.settings.WorkWithSettings;
import com.genexus.android.core.base.metadata.theme.ThemeDefinition;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.NameMap;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.providers.DatabaseDefinition;

public class ApplicationDefinition
{
	// Version id, for serialization. Important: change this number if metadata structure changes!
	private static final long serialVersionUID = 1L;

	private boolean mIsLoaded;
	private boolean mIsLoadingMetadata;

	// Global settings.
	private WorkWithSettings mWorkWithSettings;

	// Panels.
	private final transient PatternInfoCache mPatternInfoCache = new PatternInfoCache();
	private final TreeMap<String, PatternInfo> mPatterns = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
	private final TreeMap<String, DataSourceInfo> mDataSources = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
	private final TreeMap<String, DataViewInfo> mDataViews = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

	// Other objects.
	private final NameMap<GxObjectDefinition> mObjects = new NameMap<>();

	// Data definitions (BCs, SDTs, attributes, domains).
	private final TreeMap<String, StructureDefinition> mBusinessComponents = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
	private final TreeMap<String, StructureDataType> mStructuredTypes = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
	private final TreeMap<String, AttributeDefinition> mAttributes = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
	private final TreeMap<String, DomainDefinition> mDomains = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
	private final TreeMap<String, String> mDeepLink = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

	// Resources (themes, images, languages).
	private final TreeMap<String, ThemeDefinition> mThemes = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
	private final NameMap<String> mTokenDefaultOptions = new NameMap<>();
	private final NameMap<String> mTokenOptions = new NameMap<>();
	private ImageCatalog mImageCatalog = new ImageCatalog();
	private LanguageCatalog mLanguageCatalog = new LanguageCatalog();

	// Caching database.
	private DatabaseDefinition mCacheDatabase;

	public boolean isLoaded()
	{
		return mIsLoaded;
	}

	public void setLoaded(boolean value)
	{
		mIsLoaded = value;
	}

	public boolean isLoadingMetadata()
	{
		return mIsLoadingMetadata;
	}

	public void setLoadingMetadata(boolean value)
	{
		mIsLoadingMetadata = value;
	}


	// -------- Global settings --------

	public void putPatternSettings(WorkWithSettings settings)
	{
		mWorkWithSettings = settings;
	}

	public WorkWithSettings getPatternSettings()
	{
		if (mWorkWithSettings == null)
		{
			Services.Log.error("Settings have not been loaded. Returning empty settings.");
			return WorkWithSettings.empty();
		}

		return mWorkWithSettings;
	}

	// -------- Main objects (WWSDs, Dashboards, SDPanels) --------

	private class PatternInfo
	{
		private static final long serialVersionUID = 1L;
		
		private final MetadataLoader mLoader;
		private final String mName;
		private final String mFilename;
		private WeakReference<IPatternMetadata> mValue;
		@SuppressWarnings("unused")
		private IPatternMetadata mStrongReference;
		private String mBusinessComponent;

		private PatternInfo(IPatternMetadata value, MetadataLoader loader, String filename)
		{
			mLoader = loader;
			mName = value.getName();
			mFilename = filename;
			mValue = new WeakReference<>(value);

			mBusinessComponent = null;
			if (value instanceof WorkWithDefinition)
			{
				WorkWithDefinition ww = (WorkWithDefinition)value;
				if (ww.getBusinessComponent() != null)
					mBusinessComponent = ww.getBusinessComponent().getName();
			}
			
			if (filename == null) {
				// Prevents the object referenced by mValue from being gc'd.
				mStrongReference = value;
			}
		}

		public IPatternMetadata getValue()
		{
			IPatternMetadata value = mValue.get();
			if (value == null)
			{
				value = mLoader.load(Services.Application.getAppContext(), mFilename);
				value.setName(mName);

				mValue = new WeakReference<>(value);
			}

			if (mFilename != null) {
				mPatternInfoCache.put(mFilename, value); // Update LRU cache.
			}
			return value;
		}

		public String getBusinessComponent()
		{
			return mBusinessComponent;
		}
	}

	private static class PatternInfoCache extends LruCache<String, IPatternMetadata> 
	{
		private static final int CACHED_INSTANCES = 8;

		public PatternInfoCache()
		{
			super(CACHED_INSTANCES);
		}
	}

	private static class DataViewInfo
	{
		private static final long serialVersionUID = 1L;
		
		private final PatternInfo mParent;
		private final String mName;
		private WeakReference<IDataViewDefinition> mValue;

		private DataViewInfo(PatternInfo parent, IDataViewDefinition value)
		{
			mParent = parent;
			mName = value.getName();
			mValue = new WeakReference<>(value);
		}

		public IDataViewDefinition getValue()
		{
			IDataViewDefinition value = mValue.get();
			if (value == null)
			{
				WorkWithDefinition pattern = (WorkWithDefinition)mParent.getValue();
				for (IDataViewDefinition dv : pattern.getDataViews())
				{
					if (dv.getName().equalsIgnoreCase(mName))
					{
						mValue = new WeakReference<>(dv);
						value = dv;
						break;
					}
				}
			}

			return value;
		}
	}

	private static class DataSourceInfo
	{
		private static final long serialVersionUID = 1L;
		
		private final DataViewInfo mParent;
		private final String mName;
		private WeakReference<IDataSourceDefinition> mValue;

		private DataSourceInfo(DataViewInfo parent, IDataSourceDefinition value)
		{
			mParent = parent;
			mName = value.getName();
			mValue = new WeakReference<>(value);
		}

		public IDataSourceDefinition getValue()
		{
			IDataSourceDefinition value = mValue.get();
			if (value == null)
			{
				IDataViewDefinition dv = mParent.getValue();
				for (IDataSourceDefinition ds : dv.getDataSources())
				{
					if (ds.getName().equalsIgnoreCase(mName))
					{
						mValue = new WeakReference<>(ds);
						value = ds;
						break;
					}
				}
			}

			return value;
		}
	}

	public void putPattern(IPatternMetadata pattern, MetadataLoader loader, String filename) {
		putObject(pattern, loader, filename);
	}

	public void putObject(IPatternMetadata pattern, MetadataLoader loader, String filename)
	{
		PatternInfo patternInfo = new PatternInfo(pattern, loader, filename);

		mPatterns.put(pattern.getName(), patternInfo);
		if (pattern instanceof WorkWithDefinition)
		{
			WorkWithDefinition wwInstance = (WorkWithDefinition)pattern;

			// Register all data views and data sources in the WW.
			for (IDataViewDefinition dataView : wwInstance.getDataViews())
			{
				DataViewInfo dataViewInfo = new DataViewInfo(patternInfo, dataView);
				mDataViews.put(dataView.getName(), dataViewInfo);

				for (IDataSourceDefinition dataSource : dataView.getDataSources())
				{
					DataSourceInfo dataSourceInfo = new DataSourceInfo(dataViewInfo, dataSource);
					mDataSources.put(dataSource.getName(), dataSourceInfo);

					// Register DS information for cache table.
					getCacheDatabase().addTableFor(dataSource);
				}
			}
		}
	}

	public IPatternMetadata getPattern(String name) {
		return getObject(name);
	}

	public IPatternMetadata getObject(String name)
	{
		if (!Strings.hasValue(name))
			return null;

		PatternInfo info = mPatterns.get(name);
		return (info != null ? info.getValue() : null);
	}

	public IViewDefinition getView(String name)
	{
		if (!Strings.hasValue(name))
			return null;

		// View may be a fully qualified Data View (e.g. WWCustomer.Customer.Detail), or a pattern name
		// (e.g. Dashboard1 or WWCustomer). If it's a WW, return the first view it contains.
		IViewDefinition view = getDataView(name);
		if (view == null)
		{
			IPatternMetadata pattern = getObject(name);
			if (pattern instanceof WorkWithDefinition)
			{
				WorkWithDefinition workWith = (WorkWithDefinition)pattern;
				if (workWith.getLevels().size() != 0)
				{
					WWLevelDefinition wwLevel = workWith.getLevel(0);
					if (wwLevel != null)
					{
						if (wwLevel.getList() != null)
							view = wwLevel.getList();
						else if (wwLevel.getDetail() != null)
							view = wwLevel.getDetail();
					}
				}
			}
			else if (pattern instanceof DashboardMetadata)
			{
				view = (DashboardMetadata)pattern;
			}
		}

		return view;
	}

	public IDataViewDefinition getDataView(String name)
	{
		if (!Strings.hasValue(name))
			return null;

		DataViewInfo info = mDataViews.get(name);
		return (info != null ? info.getValue() : null);
	}

	public IDataSourceDefinition getDataSource(String dpName)
	{
		if (!Strings.hasValue(dpName))
			return null;

		DataSourceInfo info = mDataSources.get(dpName);
		return (info != null ? info.getValue() : null);
	}

	public WorkWithDefinition getWorkWithForBC(String businessComponent)
	{
		for (PatternInfo patternInfo : mPatterns.values())
		{
			if (patternInfo.getBusinessComponent() != null && patternInfo.getBusinessComponent().equalsIgnoreCase(businessComponent))
				return (WorkWithDefinition)patternInfo.getValue();
		}

		return null;
	}

	// -------- Other objects --------

	public void putGxObject(GxObjectDefinition gxObject)
	{
		mObjects.put(gxObject.getName(), gxObject);
	}

	public GxObjectDefinition getGxObject(String name) {
		return getGxObject(GxObjectDefinition.class, name);
	}

	public ProcedureDefinition getProcedure(String name) {
		return getGxObject(ProcedureDefinition.class, name);
	}

	private <T extends GxObjectDefinition> T getGxObject(Class<T> objClass, String name)
	{
		GxObjectDefinition gxObject = mObjects.get(name);

		if (objClass.isInstance(gxObject))
			return objClass.cast(gxObject);
		else
			return null;
	}

	// -------- Data definitions (BCs, SDTs, attributes, domains) --------

	public void putBusinessComponent(StructureDefinition bc)
	{
		mBusinessComponents.put(bc.getName(), bc);
	}

	public StructureDefinition getBusinessComponent(String name)
	{
		return mBusinessComponents.get(name);
	}

	public boolean hasBusinessComponents() 
	{
		return mBusinessComponents.size()>0;
	}
	
	public void putSDT(StructureDataType sdt)
	{
		mStructuredTypes.put(sdt.getName(), sdt);
	}

	public StructureDataType getSDT(String name)
	{
		return mStructuredTypes.get(name);
	}

	public AttributeDefinition putAttribute(AttributeDefinition attribute)
	{
		return mAttributes.put(attribute.getName(), attribute);
	}

	public AttributeDefinition getAttribute(String name)
	{
		return mAttributes.get(name);
	}

	public DomainDefinition putDomain(DomainDefinition domain)
	{
		return mDomains.put(domain.getName(), domain);
	}

	public DomainDefinition getDomain(String name)
	{
		return mDomains.get(name);
	}

	public String getDeepLink(String link) {
		return mDeepLink.get(link);
	}

	public void putDeepLink(String link, String panel) {
		mDeepLink.put(link, panel);
	}

	// -------- Resources (themes, images, languages) --------

	public ThemeDefinition getTheme(String name) {
		return mThemes.get(name);
	}

	public ThemeDefinition putTheme(ThemeDefinition theme) {
		return mThemes.put(theme.getName(), theme);
	}

	private void clearThemesCache() {
		for (ThemeDefinition themeDefinition: mThemes.values())
			themeDefinition.clearCache();
	}

	public String getOption(String name) {
		if (mTokenOptions.containsKey(name)) {
			return mTokenOptions.get(name);
		} else {
			return mTokenDefaultOptions.get(name);
		}
	}

	public void setDefaultOption(String name, String value) {
		mTokenDefaultOptions.put(name.toLowerCase(), value);
		clearThemesCache();
	}

	public void clearAllDefaultOptions() {
		mTokenDefaultOptions.clear();
	}

	public void setOption(String name, String value) {
		mTokenOptions.put(name.toLowerCase(), value);
		clearThemesCache();
	}

	public void clearOption(String name) {
		mTokenOptions.remove(name.toLowerCase());
		clearThemesCache();
	}

	public void setImageCatalog(ImageCatalog catalog)
	{
		mImageCatalog = catalog;
	}

	public ImageCatalog getImageCatalog()
	{
		return mImageCatalog;
	}

	public void setLanguageCatalog(LanguageCatalog catalog)
	{
		mLanguageCatalog = catalog;
	}

	public LanguageCatalog getLanguageCatalog()
	{
		return mLanguageCatalog;
	}

	// -------- Caching database --------

	public DatabaseDefinition getCacheDatabase()
	{
		if (mCacheDatabase == null)
			mCacheDatabase = new DatabaseDefinition(Services.Application.get().getName());

		return mCacheDatabase;
	}

	
}
