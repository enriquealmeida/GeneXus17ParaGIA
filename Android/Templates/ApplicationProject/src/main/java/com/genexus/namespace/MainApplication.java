package $Main.AndroidPackageName$;
$if(Main.MultiDexBuild)$
import android.content.Context;
import androidx.multidex.MultiDex;
$endif$

import com.artech.base.services.AndroidContext;
import com.artech.base.services.IGxProcedure;

import com.genexus.android.ContextImpl;
import com.genexus.android.core.application.MyApplication;
import com.genexus.android.core.base.metadata.GenexusApplication;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.providers.EntityDataProvider;

import com.genexus.Application;
import com.genexus.ClientContext;

public class MainApplication extends MyApplication
{
	@Override
	public final void onCreate()
	{
		GenexusApplication application = new GenexusApplication();
		application.setName("$Model.AppName$");
		application.setAPIUri("$Model.AndroidTargetUrl$");
		application.setAppEntry("$Main.FullName$");
		application.setMajorVersion($Main.AndroidMajorVersion$);
		application.setMinorVersion($Main.AndroidMinorVersion$);

		// Extensibility Point for Logging
                $logging$ 

		// Security
		application.setIsSecure($Main.EnableIntegratedSecurity$);
		application.setEnableAnonymousUser($Main.EnableAnonymousUser$);
		application.setClientId("$Main.SecurityClientId$");
		application.setLoginObject("$Main.LoginObjectForSd$");
		application.setNotAuthorizedObject("$Main.NotAuthorizedObjectForSd$");
		application.setChangePasswordObject("$Main.ChangePasswordObjectForSd$");
		//application.setCompleteUserDataObject("$Main.CompleteUserDataObjectForSd$");

		application.setAllowWebViewExecuteJavascripts($Main.AllowWebViewExecuteJavascripts$);
		application.setAllowWebViewExecuteLocalFiles($Main.AllowWebViewExecuteLocalFiles$);

		application.setShareSessionToWebView($Main.ShareSessionToWebView$);

		// Dynamic Url		
		application.setUseDynamicUrl($Model.DynamicUrl$);
		application.setDynamicUrlAppId("$Model.KBName$");
		application.setDynamicUrlPanel("$Model.DynamicUrlConfigObject$");

		// Notifications
		application.setUseNotification($Main.EnableNotification$);
		application.setNotificationSenderId("$Main.NotificationSenderId$");
		application.setNotificationRegistrationHandler("$Main.NotificationRegistrationHandler$");

		setApp(application);

		$Main.AndroidModuleClasses:{ moduleClass |
registerModule(new $moduleClass$());
		}; separator="\n"$

		$if(IsHuawei)$
			$if(Main.UseHuaweiAnalytics)$
		registerModule(new com.genexus.android.analytics.hms.HMSAnalyticsModule());
			$endif$
			$if(Main.UseHuaweiNotifications)$
		registerModule(new com.genexus.android.notifications.hms.HmsOneSignalModule());
			$endif$
			$if(Main.UseHuaweiInAppBilling)$
		registerModule(new com.genexus.android.payments.hms.HmsInAppBillingModule());
			$endif$
			$if(Main.UseHuaweiMaps)$
		registerModule(new com.genexus.android.controls.maps.hms.HuaweiMapsModule());
			$endif$
		$endif$	

		super.onCreate();

$if (Main.IsOffline)$		
		// Turn on offline support.
		application.setIsOfflineApplication(true);
		application.setUseInternalStorageForDatabase(true);
		application.setReorMD5Hash("$Main.AndroidReorScriptMD5$");
		
		//application.setSynchronizerReceiveCustomProcedure("");
	
$endif$		

$if (Main.IsOffline || Main.HasOfflineReferences || Main.HasAssociatedUITests)$
		// init offline std classes.
		Application.init(IGxProcedure.class);
		int remoteHandle = Application.getNewRemoteHandle(ClientContext.getModelContext());
		//set this handle as app handle, store in as int in App. Use in all reflection calls
		application.setRemoteHandle(remoteHandle);
$endif$
    }

	@Override
	public Class<? extends com.genexus.android.core.services.EntityService> getEntityServiceClass()
	{
		return AppEntityService.class;
	}

	@Override
	public EntityDataProvider getProvider()
	{
		return new AppEntityDataProvider();
	}

$if(Main.MultiDexBuild)$
	@Override
	protected void attachBaseContext(Context base)
	{
		super.attachBaseContext(base);
		MultiDex.install(this);
	}
$endif$

}
