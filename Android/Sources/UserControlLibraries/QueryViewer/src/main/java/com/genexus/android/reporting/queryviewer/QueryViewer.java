package com.genexus.android.reporting.queryviewer;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Base64;
import android.view.ViewTreeObserver;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.genexus.android.content.FileProviderUtils;
import com.genexus.android.core.base.controls.IGxControlRuntime;
import com.genexus.android.core.base.metadata.GenexusApplication;
import com.genexus.android.core.base.metadata.expressions.Expression.Value;
import com.genexus.android.core.base.metadata.layout.LayoutItemDefinition;
import com.genexus.android.core.base.model.EntityList;
import com.genexus.android.core.base.serialization.INodeCollection;
import com.genexus.android.core.base.serialization.INodeObject;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.services.UrlBuilder;
import com.genexus.android.core.ui.Coordinator;
import com.genexus.android.core.usercontrols.IGxUserControl;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryViewer extends WebView implements IGxUserControl, IGxControlRuntime {

    public static final String NAME                                     = "SDQueryViewer";

    //private static final String EVENT_ON_CLICK                        = "OnClick";
    private static final String CONTROL_ID                              = "@SDQueryViewer";
    private static final String UTF8                                    = "UTF-8";
    private static final String SERVICE_NAME_NET                        = "gxqueryviewerforsd";
    private static final String SERVICE_NAME_JAVA                       = "qviewer.services.gxqueryviewerforsd";
	private static final String PROPERTY_OBJECT		                    = "ObjectCall";
    private static final String PROPERTY_OBJECT_NAME                    = "ObjectName";
	private static final String PROPERTY_AXES						    = "Axes";
    private static final String PROPERTY_ELEMENTS						= "Elements"; //Design-time property name is Axes
    private static final String PROPERTY_PARAMETERS                     = "Parameters";
    private static final String PROPERTY_TYPE                           = "Type";
    private static final String PROPERTY_CHART_TYPE                     = "ChartType";
    private static final String PROPERTY_PAGING                         = "Paging";
    private static final String PROPERTY_PAGE_SIZE                      = "PageSize";
    private static final String PROPERTY_SHOW_DATA_LABELS_IN            = "ShowDataLabelsIn";
	private static final String PROPERTY_TOTAL_FOR_ROWS                 = "TotalForRows";
	private static final String PROPERTY_TOTAL_FOR_COLUMNS              = "TotalForColumns";
    private static final String PROPERTY_PLOT_SERIES                    = "PlotSeries";
    private static final String PROPERTY_XAXIS_LABELS                   = "XAxisLabels";
    private static final String PROPERTY_XAXIS_INTERSECTION_AT_ZERO     = "XAxisIntersectionAtZero";
    private static final String PROPERTY_SHOW_VALUES                    = "ShowValues";
    private static final String PROPERTY_XAXIS_TITLE                    = "XAxisTitle";
    private static final String PROPERTY_YAXIS_TITLE                    = "YAxisTitle";
    private static final String PROPERTY_SHOW_DATA_AS                   = "ShowDataAs";
    private static final String PROPERTY_INCLUDE_TREND                  = "IncludeTrend";
    private static final String PROPERTY_TREND_PERIOD                   = "TrendPeriod";
    private static final String PROPERTY_REMEMBER_LAYOUT                = "RememberLayout";
    private static final String PROPERTY_LANGUAGE                       = "Language";
    private static final String PROPERTY_WIDTH                          = "Width";
    private static final String PROPERTY_HEIGHT                         = "Height";
    private static final String PROPERTY_ORIENTATION                    = "Orientation";
    private static final String PROPERTY_INCLUDE_SPARKLINE              = "IncludeSparkline";
    private static final String PROPERTY_INCLUDE_MAX_MIN                = "IncludeMaxAndMin";
    private static final String PROPERTY_THEME                          = "Theme";


	private static final String PROPERTY_OBJECT_ID                      = "ObjectId";
	private static final String PROPERTY_OBJECT_TYPE                    = "ObjectType";
	private static final String PROPERTY_IS_EXTERNAL_QUERY              = "IsExternalQuery";
	private static final String PROPERTY_ALLOW_ELEMENTS_ORDER_CHANGE 	= "AllowElementsOrderChange";
	private static final String PROPERTY_AUTO_RESIZE                    = "AutoResize";
	private static final String PROPERTY_AUTO_RESIZE_TYPE               = "AutoResizeType";
	private static final String PROPERTY_FONT_FAMILY                    = "FontFamily";
	private static final String PROPERTY_FONT_SIZE                      = "FontSize";
	private static final String PROPERTY_FONT_COLOR                     = "FontColor";
	private static final String PROPERTY_AUTO_REFRESH_GROUP             = "AutoRefreshGroup";
	private static final String PROPERTY_DISABLE_COLUMN_SORT            = "DisableColumnSort";
	private static final String PROPERTY_ALLOW_SELECTION                = "AllowSelection";
	private static final String PROPERTY_EXPORT_TO_XML                  = "ExportToXML";
	private static final String PROPERTY_EXPORT_TO_HTML                 = "ExportToHTML";
	private static final String PROPERTY_EXPORT_TO_XLS                  = "ExportToXLS";
	private static final String PROPERTY_EXPORT_TO_XLSX                 = "ExportToXLSX";
	private static final String PROPERTY_EXPORT_TO_PDF                  = "ExportToPDF";
	private static final String PROPERTY_TITLE							= "Title";

	private static final String PDF										= "pdf";
	private static final String XML										= "xml";
	private static final String XLS										= "xls";
	private static final String XLSX									= "xlsx";


    private static final String METHOD_REFRESH                          = "Refresh";

    private String mLanguage;
    private String mObject;
    private String mObjectName;
    private String mElements;
    private String mParameters;
    private String mType;
    private String mChartType;
    private boolean mPaging;
    private int mPageSize;
    private String mShowDataLabelsIn;
	private String mTotalForRows;
	private String mTotalForColumns;
    private String mPlotSeries;
    private String mXAxisLabels;
    private boolean mXAxisIntersectionAtZero;
    private boolean mShowValues;
    private String mXAxisTitle;
    private String mYAxisTitle;
    private String mShowDataAs;
    private boolean mIncludeTrend;
    private String mTrendPeriod;
    private boolean mRememberLayout;
    private String mWidth = "";
    private String mHeight = "";
    private String mOrientation = "";
    private boolean mIncludeSparkline;
    private boolean mIncludeMaxMin;
    private String mTheme;

	private String mObjectId;
	private String mObjectType;
	private boolean mIsExternalQuery;
	private boolean mAllowElementsOrderChange;
	private boolean mAutoResize;
	private String mAutoResizeType;
	private String mFontFamily;
	private int mFontSize;
	private String mFontColor;
	private String mAutoRefreshGroup;
	private boolean mDisableColumnSort;
	private boolean mAllowSelection;
	private boolean mExportToXML;
	private boolean mExportToHTML;
	private boolean mExportToXLS;
	private boolean mExportToXLSX;
	private boolean mExportToPDF;
	private String mTitle;

    private Context mContext;
    private Coordinator mCoordinator;
    private LayoutItemDefinition mLayoutDefinition;
    private String mServicesURL;
    private String strParameters = "";

    public QueryViewer(Context context, Coordinator coordinator, LayoutItemDefinition definition) {
        super(context);
        mContext = context;
        mCoordinator = coordinator;
        mLayoutDefinition = definition;
        initializeControl();
    }

    private void initializeControl() {
        initializeWebView();
        initializeServiceURL();
        initializeProperties();

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                float density = getResources().getDisplayMetrics().density;
                int width = Math.round(getMeasuredWidth()/density);
                int height = Math.round(getMeasuredHeight()/density);
                if (width > 0) {
                    mWidth = String.valueOf(width); //width is ready
                    mHeight = String.valueOf(height); //height is ready
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    show();
                }
            }
        });
    }

    private void initializeServiceURL() {

        GenexusApplication mApplication = Services.Application.get();
        int mServerType = mApplication.getServerType();

        String objectName;
        if (mServerType == UrlBuilder.JAVA_SERVER)
            objectName = SERVICE_NAME_JAVA;
        else
            objectName = SERVICE_NAME_NET;

        mServicesURL = mApplication.link(objectName);

    }

    @SuppressWarnings("deprecation")
    @SuppressLint("SetJavaScriptEnabled")
    private void initializeWebView() {

		WebSettings settings = getSettings();
		settings.setLoadWithOverviewMode(true);
		settings.setUseWideViewPort(true);
		settings.setJavaScriptEnabled(true);
		settings.setDomStorageEnabled(true);
		settings.setBuiltInZoomControls(true);

		this.setWebViewClient(new WebViewClient() {

			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
				Toast.makeText(mContext, description, Toast.LENGTH_SHORT).show();

			}

			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});

		this.setDownloadListener(new DownloadListener() {
			@Override
			public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
				String fileType = getFileType(mimetype);
				if (fileType != null) {
					File file = createAndSaveFileFromUrl(url, fileType);
					Intent intent = getIntent(mContext, file, mimetype);
					mContext.startActivity(intent);
				}
			}
		});
	}

	private String getFileType(String mimeType) {
    	if (mimeType.contains(PDF)) {
			return PDF;
		}
    	else if (mimeType.contains("spreadsheet")) {
    		return XLSX;
		}
    	else if (mimeType.contains(XML)) {
    		return XML;
		}
    	else if (mimeType.contains("excel")) {
    		return XLS;
		}
    	return null;
	}

	private static Intent getIntent(@NonNull Context context, File file, String mimeType) {

		Intent intent = new Intent(Intent.ACTION_SEND);

		// Secondly, we cannot actually share file URIs in Android N (and it was discouraged
		// before) so convert file:// uris inside our own directories to content:// ones.
		if (FileProviderUtils.canShareFile(context, file)) {
			Uri providerUri = FileProviderUtils.getUriForFile(context, file);
			intent.setDataAndType(providerUri, mimeType);
			intent.putExtra(Intent.EXTRA_STREAM,providerUri );
			// This should not be necessary, but is.
			// See http://stackoverflow.com/a/33652695/82788
			FileProviderUtils.grantReadPermissions(context, intent);
		} else
			intent.setDataAndType(Uri.fromFile(file), mimeType);
		return intent;
	}

	private File createAndSaveFileFromUrl(String url, String fileType) {
		File path = mContext.getCacheDir();

		String filename = mObjectName + "." + fileType;
		File file = new File(path, filename);
		try {
			if (!path.exists())
				path.mkdirs();
			if (!file.exists())
				file.createNewFile();

			String encodedString = url.substring(url.indexOf(",") + 1);
			byte[] decodedBytes = null;

			if (fileType.equals(XLS) || fileType.equals(XML)) {
				String decodedHtml = URLDecoder.decode(encodedString, UTF8);
				decodedBytes = decodedHtml.getBytes(UTF8);
			}
			else {
				decodedBytes = Base64.decode(encodedString, Base64.DEFAULT);
			}

			OutputStream os = new FileOutputStream(file);
			os.write(decodedBytes);
			os.close();
		} catch (UnsupportedEncodingException | FileNotFoundException e) {
			Services.Log.error(e);
		} catch (IOException e) {
			Services.Log.error(e);
		}
		return file;
	}


    private void initializeProperties() {

        mLanguage = Services.Language.getCurrentLanguage();
        mTheme = getDefinitionProperty(PROPERTY_THEME);

        mChartType                  = getDefinitionProperty(PROPERTY_CHART_TYPE);
        mIncludeTrend               = Boolean.parseBoolean(getDefinitionProperty(PROPERTY_INCLUDE_TREND));
		mObject						= getDefinitionProperty(PROPERTY_OBJECT);
        mObjectName                 = getDefinitionProperty(PROPERTY_OBJECT_NAME);
        mPageSize                   = Integer.parseInt(getDefinitionProperty(PROPERTY_PAGE_SIZE));
        mPaging                     = Boolean.parseBoolean(getDefinitionProperty(PROPERTY_PAGING));
        mPlotSeries                 = getDefinitionProperty(PROPERTY_PLOT_SERIES);
        mRememberLayout             = Boolean.parseBoolean(getDefinitionProperty(PROPERTY_REMEMBER_LAYOUT));
        mShowDataAs                 = getDefinitionProperty(PROPERTY_SHOW_DATA_AS);
        mShowDataLabelsIn           = getDefinitionProperty(PROPERTY_SHOW_DATA_LABELS_IN);
        mTotalForRows               = getDefinitionProperty(PROPERTY_TOTAL_FOR_ROWS);
        mTotalForColumns            = getDefinitionProperty(PROPERTY_TOTAL_FOR_COLUMNS);
        mShowValues                 = Boolean.parseBoolean(getDefinitionProperty(PROPERTY_SHOW_VALUES));
        mTrendPeriod                = getDefinitionProperty(PROPERTY_TREND_PERIOD);
        mType                       = getDefinitionProperty(PROPERTY_TYPE);
        mXAxisIntersectionAtZero    = Boolean.parseBoolean(getDefinitionProperty(PROPERTY_XAXIS_INTERSECTION_AT_ZERO));
        mXAxisLabels                = getDefinitionProperty(PROPERTY_XAXIS_LABELS);
        mXAxisTitle                 = getDefinitionProperty(PROPERTY_XAXIS_TITLE);
        mYAxisTitle                 = getDefinitionProperty(PROPERTY_YAXIS_TITLE);
        mOrientation                = getDefinitionProperty(PROPERTY_ORIENTATION);
        mIncludeSparkline           = Boolean.parseBoolean(getDefinitionProperty(PROPERTY_INCLUDE_SPARKLINE));
        mIncludeMaxMin              = Boolean.parseBoolean(getDefinitionProperty(PROPERTY_INCLUDE_MAX_MIN));
        mParameters					= getDefinitionProperty(PROPERTY_PARAMETERS);
        mElements 					= getDefinitionProperty(PROPERTY_AXES); //Design-time Elements property name is actually Axes


		mObjectId               	= getDefinitionProperty(PROPERTY_OBJECT_ID);
		mObjectType             	= getDefinitionProperty(PROPERTY_OBJECT_TYPE);
		mIsExternalQuery            = Boolean.parseBoolean(getDefinitionProperty(PROPERTY_IS_EXTERNAL_QUERY));
		mAllowElementsOrderChange 	= Boolean.parseBoolean(getDefinitionProperty(PROPERTY_ALLOW_ELEMENTS_ORDER_CHANGE));
		mAutoResize             	= Boolean.parseBoolean(getDefinitionProperty(PROPERTY_AUTO_RESIZE));
		mAutoResizeType             = getDefinitionProperty(PROPERTY_AUTO_RESIZE_TYPE);
		mFontFamily             	= getDefinitionProperty(PROPERTY_FONT_FAMILY);
		mFontSize             		= Integer.parseInt(getDefinitionProperty(PROPERTY_FONT_SIZE));
		mFontColor             		= getDefinitionProperty(PROPERTY_FONT_COLOR);
		mAutoRefreshGroup           = getDefinitionProperty(PROPERTY_AUTO_REFRESH_GROUP);
		mDisableColumnSort          = Boolean.parseBoolean(getDefinitionProperty(PROPERTY_DISABLE_COLUMN_SORT));
		mAllowSelection             = Boolean.parseBoolean(getDefinitionProperty(PROPERTY_ALLOW_SELECTION));
		mExportToXML             	= Boolean.parseBoolean(getDefinitionProperty(PROPERTY_EXPORT_TO_XML));
		mExportToHTML             	= Boolean.parseBoolean(getDefinitionProperty(PROPERTY_EXPORT_TO_HTML));
		mExportToXLS             	= Boolean.parseBoolean(getDefinitionProperty(PROPERTY_EXPORT_TO_XLS));
		mExportToXLSX             	= Boolean.parseBoolean(getDefinitionProperty(PROPERTY_EXPORT_TO_XLSX));
		mExportToPDF             	= Boolean.parseBoolean(getDefinitionProperty(PROPERTY_EXPORT_TO_PDF));
		mTitle             			= getDefinitionProperty(PROPERTY_TITLE);
    }

    private String getDefinitionProperty(String propertyName) {
        return mLayoutDefinition.getControlInfo().optStringProperty(CONTROL_ID + propertyName);
    }

	@Override
	public Value callMethod(String name, List<Value> parameters) {
		if (METHOD_REFRESH.equalsIgnoreCase(name)) {
			show();
		}
		return null;
	}

    @Override
    public void setPropertyValue(String name, Value value) {
        if (PROPERTY_CHART_TYPE.equalsIgnoreCase(name)) {
            mChartType = value.coerceToString();
        }
        else if (PROPERTY_INCLUDE_TREND.equalsIgnoreCase(name)) {
            mIncludeTrend = value.coerceToBoolean();
        }
        else if (PROPERTY_OBJECT.equalsIgnoreCase(name)) {
        	try {
				setObjectCall(value.coerceToString());
			}
        	catch (UnsupportedEncodingException e)  {
				Services.Log.error(e);
			}
		}
        else if (PROPERTY_OBJECT_NAME.equalsIgnoreCase(name)) {
            mObjectName = value.coerceToString();
        }
        else if (PROPERTY_PAGE_SIZE.equalsIgnoreCase(name)) {
            mPageSize = value.coerceToInt();
        }
        else if (PROPERTY_PAGING.equalsIgnoreCase(name)) {
            mPaging = value.coerceToBoolean();
        }
        else if (PROPERTY_PLOT_SERIES.equalsIgnoreCase(name)) {
            mPlotSeries = value.coerceToString();
        }
        else if (PROPERTY_REMEMBER_LAYOUT.equalsIgnoreCase(name)) {
            mRememberLayout = value.coerceToBoolean();
        }
        else if (PROPERTY_SHOW_DATA_AS.equalsIgnoreCase(name)) {
            mShowDataAs = value.coerceToString();
        }
        else if (PROPERTY_SHOW_DATA_LABELS_IN.equalsIgnoreCase(name)) {
            mShowDataLabelsIn = value.coerceToString();
        }
		else if (PROPERTY_TOTAL_FOR_ROWS.equalsIgnoreCase(name)) {
			mTotalForRows = value.coerceToString();
		}
		else if (PROPERTY_TOTAL_FOR_COLUMNS.equalsIgnoreCase(name)) {
			mTotalForColumns = value.coerceToString();
		}
        else if (PROPERTY_SHOW_VALUES.equalsIgnoreCase(name)) {
            mShowValues = value.coerceToBoolean();
        }
        else if (PROPERTY_TREND_PERIOD.equalsIgnoreCase(name)) {
            mTrendPeriod = value.coerceToString();
        }
        else if (PROPERTY_TYPE.equalsIgnoreCase(name)) {
            mType = value.coerceToString();
        }
        else if (PROPERTY_XAXIS_INTERSECTION_AT_ZERO.equalsIgnoreCase(name)) {
            mXAxisIntersectionAtZero = value.coerceToBoolean();
        }
        else if (PROPERTY_XAXIS_LABELS.equalsIgnoreCase(name)) {
            mXAxisLabels = value.coerceToString();
        }
        else if (PROPERTY_XAXIS_TITLE.equalsIgnoreCase(name)) {
            mXAxisTitle = value.coerceToString();
        }
        else if (PROPERTY_YAXIS_TITLE.equalsIgnoreCase(name)) {
            mYAxisTitle = value.coerceToString();
        }
        else if (PROPERTY_ELEMENTS.equalsIgnoreCase(name)) {
            mElements = value.coerceToString();
        }
        else if (PROPERTY_PARAMETERS.equalsIgnoreCase(name)) {
        	mParameters = value.coerceToString();
        }
        else if (PROPERTY_ORIENTATION.equalsIgnoreCase(name)) {
            mOrientation = value.coerceToString();
        }
        else if (PROPERTY_INCLUDE_SPARKLINE.equalsIgnoreCase(name)) {
            mIncludeSparkline = value.coerceToBoolean();
        }
        else if (PROPERTY_INCLUDE_MAX_MIN.equalsIgnoreCase(name)) {
            mIncludeMaxMin = value.coerceToBoolean();
        }
		else if (PROPERTY_OBJECT_ID.equalsIgnoreCase(name)) {
			mObjectId = value.coerceToString();
		}
		else if (PROPERTY_OBJECT_TYPE.equalsIgnoreCase(name)) {
			mObjectType = value.coerceToString();
		}
		else if (PROPERTY_IS_EXTERNAL_QUERY.equalsIgnoreCase(name)) {
			mIsExternalQuery = value.coerceToBoolean();
		}
		else if (PROPERTY_ALLOW_ELEMENTS_ORDER_CHANGE.equalsIgnoreCase(name)) {
			mAllowElementsOrderChange = value.coerceToBoolean();
		}
		else if (PROPERTY_AUTO_RESIZE.equalsIgnoreCase(name)) {
			mAutoResize = value.coerceToBoolean();
		}
		else if (PROPERTY_AUTO_RESIZE_TYPE.equalsIgnoreCase(name)) {
			mAutoResizeType = value.coerceToString();
		}
		else if (PROPERTY_FONT_FAMILY.equalsIgnoreCase(name)) {
			mFontFamily = value.coerceToString();
		}
		else if (PROPERTY_FONT_SIZE.equalsIgnoreCase(name)) {
			mFontSize = value.coerceToInt();
		}
		else if (PROPERTY_FONT_COLOR.equalsIgnoreCase(name)) {
			mFontColor = value.coerceToString();
		}
		else if (PROPERTY_AUTO_REFRESH_GROUP.equalsIgnoreCase(name)) {
			mAutoRefreshGroup = value.coerceToString();
		}
		else if (PROPERTY_DISABLE_COLUMN_SORT.equalsIgnoreCase(name)) {
			mDisableColumnSort = value.coerceToBoolean();
		}
		else if (PROPERTY_ALLOW_SELECTION.equalsIgnoreCase(name)) {
			mAllowSelection = value.coerceToBoolean();
		}
		else if (PROPERTY_EXPORT_TO_XML.equalsIgnoreCase(name)) {
			mExportToXML = value.coerceToBoolean();
		}
		else if (PROPERTY_EXPORT_TO_HTML.equalsIgnoreCase(name)) {
			mExportToHTML = value.coerceToBoolean();
		}
		else if (PROPERTY_EXPORT_TO_XLS.equalsIgnoreCase(name)) {
			mExportToXLS = value.coerceToBoolean();
		}
		else if (PROPERTY_EXPORT_TO_XLSX.equalsIgnoreCase(name)) {
			mExportToXLSX = value.coerceToBoolean();
		}
		else if (PROPERTY_EXPORT_TO_PDF.equalsIgnoreCase(name)) {
			mExportToPDF = value.coerceToBoolean();
		}
		else if (PROPERTY_TITLE.equalsIgnoreCase(name)) {
			mTitle = value.coerceToString();
		}
    }

    @Override
    public Value getPropertyValue(String name) {
        if (PROPERTY_CHART_TYPE.equalsIgnoreCase(name)) {
            return Value.newString(mChartType);
        }
        else if (PROPERTY_INCLUDE_TREND.equalsIgnoreCase(name)) {
            return Value.newBoolean(mIncludeTrend);
        }
        else if (PROPERTY_OBJECT.equalsIgnoreCase(name)) {
        	return Value.newString(mObject);
		}
        else if (PROPERTY_OBJECT_NAME.equalsIgnoreCase(name)) {
            return Value.newString(mObjectName);
        }
        else if (PROPERTY_PAGE_SIZE.equalsIgnoreCase(name)) {
            return Value.newInteger(mPageSize);
        }
        else if (PROPERTY_PAGING.equalsIgnoreCase(name)) {
            return Value.newBoolean(mPaging);
        }
        else if (PROPERTY_PLOT_SERIES.equalsIgnoreCase(name)) {
            return Value.newString(mPlotSeries);
        }
        else if (PROPERTY_REMEMBER_LAYOUT.equalsIgnoreCase(name)) {
            return Value.newBoolean(mRememberLayout);
        }
        else if (PROPERTY_SHOW_DATA_AS.equalsIgnoreCase(name)) {
            return Value.newString(mShowDataAs);
        }
        else if (PROPERTY_SHOW_DATA_LABELS_IN.equalsIgnoreCase(name)) {
            return Value.newString(mShowDataLabelsIn);
        }
		else if (PROPERTY_TOTAL_FOR_ROWS.equalsIgnoreCase(name)) {
			return Value.newString(mTotalForRows);
		}
		else if (PROPERTY_TOTAL_FOR_COLUMNS.equalsIgnoreCase(name)) {
			return Value.newString(mTotalForColumns);
		}
        else if (PROPERTY_SHOW_VALUES.equalsIgnoreCase(name)) {
            return Value.newBoolean(mShowValues);
        }
        else if (PROPERTY_TREND_PERIOD.equalsIgnoreCase(name)) {
            return Value.newString(mTrendPeriod);
        }
        else if (PROPERTY_TYPE.equalsIgnoreCase(name)) {
            return Value.newString(mType);
        }
        else if (PROPERTY_XAXIS_INTERSECTION_AT_ZERO.equalsIgnoreCase(name)) {
            return Value.newBoolean(mXAxisIntersectionAtZero);
        }
        else if (PROPERTY_XAXIS_LABELS.equalsIgnoreCase(name)) {
            return Value.newString(mXAxisLabels);
        }
        else if (PROPERTY_XAXIS_TITLE.equalsIgnoreCase(name)) {
            return Value.newString(mXAxisTitle);
        }
        else if (PROPERTY_YAXIS_TITLE.equalsIgnoreCase(name)) {
            return Value.newString(mYAxisTitle);
        }
        else if (PROPERTY_ELEMENTS.equalsIgnoreCase(name)) {
            return Value.newString(mElements);
        }
        else if (PROPERTY_PARAMETERS.equalsIgnoreCase(name)) {
            return Value.newString(mParameters);
        }
        else if (PROPERTY_ORIENTATION.equalsIgnoreCase(name)) {
            return Value.newString(mOrientation);
        }
        else if (PROPERTY_INCLUDE_SPARKLINE.equalsIgnoreCase(name)) {
            return Value.newBoolean(mIncludeSparkline);
        }
        else if (PROPERTY_INCLUDE_MAX_MIN.equalsIgnoreCase(name)) {
            return Value.newBoolean(mIncludeMaxMin);
        }
		else if (PROPERTY_OBJECT_ID.equalsIgnoreCase(name)) {
			return Value.newString(mObjectId);
		}
		else if (PROPERTY_OBJECT_TYPE.equalsIgnoreCase(name)) {
			return Value.newString(mObjectType);
		}
		else if (PROPERTY_IS_EXTERNAL_QUERY.equalsIgnoreCase(name)) {
			return Value.newBoolean(mIsExternalQuery);
		}
		else if (PROPERTY_ALLOW_ELEMENTS_ORDER_CHANGE.equalsIgnoreCase(name)) {
			return Value.newBoolean(mAllowElementsOrderChange);
		}
		else if (PROPERTY_AUTO_RESIZE.equalsIgnoreCase(name)) {
			return Value.newBoolean(mAutoResize);
		}
		else if (PROPERTY_AUTO_RESIZE_TYPE.equalsIgnoreCase(name)) {
			return Value.newString(mAutoResizeType);
		}
		else if (PROPERTY_FONT_FAMILY.equalsIgnoreCase(name)) {
			return Value.newString(mFontFamily);
		}
		else if (PROPERTY_FONT_SIZE.equalsIgnoreCase(name)) {
			return Value.newInteger(mFontSize);
		}
		else if (PROPERTY_FONT_COLOR.equalsIgnoreCase(name)) {
			return Value.newString(mFontColor);
		}
		else if (PROPERTY_AUTO_REFRESH_GROUP.equalsIgnoreCase(name)) {
			return Value.newString(mAutoRefreshGroup);
		}
		else if (PROPERTY_DISABLE_COLUMN_SORT.equalsIgnoreCase(name)) {
			return Value.newBoolean(mDisableColumnSort);
		}
		else if (PROPERTY_ALLOW_SELECTION.equalsIgnoreCase(name)) {
			return Value.newBoolean(mAllowSelection);
		}
		else if (PROPERTY_EXPORT_TO_XML.equalsIgnoreCase(name)) {
			return Value.newBoolean(mExportToXML);
		}
		else if (PROPERTY_EXPORT_TO_HTML.equalsIgnoreCase(name)) {
			return Value.newBoolean(mExportToHTML);
		}
		else if (PROPERTY_EXPORT_TO_XLS.equalsIgnoreCase(name)) {
			return Value.newBoolean(mExportToXLS);
		}
		else if (PROPERTY_EXPORT_TO_XLSX.equalsIgnoreCase(name)) {
			return Value.newBoolean(mExportToXLSX);
		}
		else if (PROPERTY_EXPORT_TO_PDF.equalsIgnoreCase(name)) {
			return Value.newBoolean(mExportToPDF);
		}
		else if (PROPERTY_TITLE.equalsIgnoreCase(name)) {
			return Value.newString(mTitle);
		}
        else {
            return null;
        }
    }

    public void show() {
        this.postUrl(mServicesURL, getPostData());
    }

    public byte[] getPostData() {

        HashMap<String, String> params = new HashMap<>();

        params.put(PROPERTY_LANGUAGE, mLanguage);
        params.put(PROPERTY_THEME, mTheme);
        params.put(PROPERTY_OBJECT, mObject);
        params.put(PROPERTY_OBJECT_NAME, mObjectName);
        params.put(PROPERTY_TYPE, mType);
        params.put(PROPERTY_CHART_TYPE, mChartType);
        params.put(PROPERTY_ELEMENTS, convertToJSON(mElements));
        params.put(PROPERTY_PARAMETERS, strParameters.isEmpty() ? convertToJSON(mParameters): strParameters);
        params.put(PROPERTY_WIDTH, mWidth);
        params.put(PROPERTY_HEIGHT, mHeight);
        params.put(PROPERTY_PAGING, String.valueOf(mPaging));
        params.put(PROPERTY_PAGE_SIZE, String.valueOf(mPageSize));
        params.put(PROPERTY_PLOT_SERIES, mPlotSeries);
        params.put(PROPERTY_INCLUDE_TREND, String.valueOf(mIncludeTrend));
        params.put(PROPERTY_REMEMBER_LAYOUT, String.valueOf(mRememberLayout));
        params.put(PROPERTY_SHOW_DATA_AS, String.valueOf(mShowDataAs));
        params.put(PROPERTY_SHOW_DATA_LABELS_IN, mShowDataLabelsIn);
		params.put(PROPERTY_TOTAL_FOR_ROWS, mTotalForRows);
		params.put(PROPERTY_TOTAL_FOR_COLUMNS, mTotalForColumns);
        params.put(PROPERTY_TREND_PERIOD, mTrendPeriod);
        params.put(PROPERTY_XAXIS_INTERSECTION_AT_ZERO, String.valueOf(mXAxisIntersectionAtZero));
        params.put(PROPERTY_XAXIS_LABELS, mXAxisLabels);
        params.put(PROPERTY_XAXIS_TITLE, mXAxisTitle);
        params.put(PROPERTY_YAXIS_TITLE, mYAxisTitle);
        params.put(PROPERTY_SHOW_VALUES, String.valueOf(mShowValues));
        params.put(PROPERTY_ORIENTATION, mOrientation);
        params.put(PROPERTY_INCLUDE_SPARKLINE, String.valueOf(mIncludeSparkline));
        params.put(PROPERTY_INCLUDE_MAX_MIN, String.valueOf(mIncludeMaxMin));
		params.put(PROPERTY_OBJECT_ID, mObjectId);
		params.put(PROPERTY_OBJECT_TYPE, mObjectType);
		params.put(PROPERTY_IS_EXTERNAL_QUERY, String.valueOf(mIsExternalQuery));
		params.put(PROPERTY_ALLOW_ELEMENTS_ORDER_CHANGE, String.valueOf(mAllowElementsOrderChange));
		params.put(PROPERTY_AUTO_RESIZE, String.valueOf(mAutoResize));
		params.put(PROPERTY_AUTO_RESIZE_TYPE, mAutoResizeType);
		params.put(PROPERTY_FONT_FAMILY, mFontFamily);
		params.put(PROPERTY_FONT_SIZE,String.valueOf( mFontSize));
		params.put(PROPERTY_FONT_COLOR, mFontColor);
		params.put(PROPERTY_AUTO_REFRESH_GROUP, mAutoRefreshGroup);
		params.put(PROPERTY_DISABLE_COLUMN_SORT, String.valueOf(mDisableColumnSort));
		params.put(PROPERTY_ALLOW_SELECTION, String.valueOf(mAllowSelection));
		params.put(PROPERTY_EXPORT_TO_XML, String.valueOf(mExportToXML));
		params.put(PROPERTY_EXPORT_TO_HTML, String.valueOf(mExportToHTML));
		params.put(PROPERTY_EXPORT_TO_XLS, String.valueOf(mExportToXLS));
		params.put(PROPERTY_EXPORT_TO_XLSX, String.valueOf(mExportToXLSX));
		params.put(PROPERTY_EXPORT_TO_PDF, String.valueOf(mExportToPDF));
		params.put(PROPERTY_TITLE, mTitle);

        return getPostDataString(params).getBytes(StandardCharsets.UTF_8);
    }

    public String getPostDataString(HashMap<String, String> params) {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (Map.Entry<String, String> entry : params.entrySet()) {

            if (first)
                first = false;
            else
                result.append("&");
            try {
                result.append(URLEncoder.encode(entry.getKey(), UTF8));
                result.append("=");
                result.append(URLEncoder.encode(entry.getValue(), UTF8));
            } catch (UnsupportedEncodingException e) {
            }

        }
        return result.toString();
    }

    public String convertToJSON(String variable) {
    	if (variable != null && !variable.isEmpty()) {
    		if (variable.startsWith("&")) {
				variable = variable.replaceFirst("&", "");
			}
			EntityList entityList = (EntityList) mCoordinator.getValue(variable);
			if (entityList != null) {
				return entityList.toString();
			}
		}
    	return "";
    }

    public void setObjectCall(String objectCall) throws UnsupportedEncodingException {
    	strParameters = "";

    	objectCall = objectCall.replace("[", "");
		objectCall = objectCall.replace("]", "");
		objectCall = objectCall.replace("\"", "");
		objectCall = objectCall.replace("\\", ".");
		String[] splitted = objectCall.split(",", -1);
		mObjectType = splitted[0].trim();
		mObjectName = splitted[1].trim();

		if (splitted.length > 2) {
			INodeCollection params = Services.Serializer.createCollection();
			for (int i = 2; i < splitted.length; i++) {
				INodeObject node = Services.Serializer.createNode();
				node.put("Value", URLEncoder.encode(splitted[i].trim(), UTF8));
				params.put(node);
			}
			strParameters = params.toString();
		}
	}
}
