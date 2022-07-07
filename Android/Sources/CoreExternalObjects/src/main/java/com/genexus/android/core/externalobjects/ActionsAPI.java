package com.genexus.android.core.externalobjects;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.io.IOUtils;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;

import androidx.annotation.NonNull;

import com.genexus.android.core.actions.ActionExecution;
import com.genexus.android.core.actions.ActionFactory;
import com.genexus.android.core.actions.ActionParameters;
import com.genexus.android.core.actions.ApiAction;
import com.genexus.android.core.actions.CompositeAction;
import com.genexus.android.core.activities.ActivityFlowControl;
import com.genexus.android.core.activities.IntentFactory;
import com.genexus.android.core.activities.IGxActivity;
import com.genexus.android.core.base.metadata.ActionDefinition;
import com.genexus.android.core.base.metadata.IViewDefinition;
import com.genexus.android.core.base.metadata.languages.Language;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.common.SecurityHelper;
import com.genexus.android.core.controllers.RefreshParameters;
import com.genexus.android.core.externalapi.ExternalApiResult;
import com.genexus.android.core.externalapi.superapps.SuperAppExternalApi;
import com.genexus.android.core.fragments.BaseFragment;
import com.genexus.android.core.fragments.IDataView;
import com.genexus.android.core.fragments.LayoutFragment;
import com.genexus.android.core.fragments.LayoutFragmentEditBC;
import com.genexus.android.core.utils.Cast;

public class ActionsAPI extends SuperAppExternalApi {
	public static final String OBJECT_NAME = "GeneXus.SD.Actions";
	private static final String METHOD_RETURN = "Return";
	private static final String METHOD_EXIT = "Exit";
	private static final String METHOD_GO_HOME = "GoHome";
	private static final String METHOD_REFRESH = "Refresh";
	private static final String PARAMETER_REFRESH_KEEP = "keep";
	private static final String METHOD_SAVE = "Save";
	private static final String METHOD_CANCEL = "Cancel";
	private static final String METHOD_LOGIN = "Login";
	private static final String METHOD_LOGOUT = "Logout";
	private static final String METHOD_RETURN_TO = "ReturnTo";
	private static final String METHOD_CANCEL_TO = "CancelTo";
	private static final String METHOD_TAKE_APP_SCREENSHOT = "TakeApplicationScreenshot";
	private static final String METHOD_SHOW_TARGET = "ShowTarget";
	private static final String METHOD_DO_SUB = "Do";
	private static final String METHOD_SET_LANGUAGE = "SetLanguage";
	private static final String METHOD_SET_THEME = "SetTheme";

	public ActionsAPI(ApiAction action)
	{
		super(action);
		addMethodHandler(METHOD_RETURN, 0, mMethodReturn);
		addMethodHandler(METHOD_EXIT, 0, mMethodExit);
		addMethodHandler(METHOD_GO_HOME, 0, mMethodGoHome);
		addMethodHandler(METHOD_REFRESH, 0, mMethodRefresh);
		addMethodHandler(METHOD_REFRESH, 1, mMethodRefreshKeep);
		addMethodHandler(METHOD_SAVE, 0, mMethodSave);
		addMethodHandler(METHOD_CANCEL, 0, mMethodCancel);
		addMethodHandler(METHOD_LOGIN, 0, mMethodLogin);
		addMethodHandler(METHOD_LOGOUT, 0, mMethodLogout);
		addMethodHandler(METHOD_RETURN_TO, 1, mMethodReturnTo);
		addMethodHandler(METHOD_CANCEL_TO, 1, mMethodCancelTo);
		addMethodHandler(METHOD_TAKE_APP_SCREENSHOT, 0, mMethodTakeAppScreenshot);
		addMethodHandler(METHOD_SHOW_TARGET, 1, mMethodShowTarget);
		addMethodHandler(METHOD_DO_SUB, 1, mMethodDoSub);
		addMethodHandler(METHOD_SET_LANGUAGE,1, mMethodSetLanguage);
		addMethodHandler(METHOD_SET_THEME,1, mMethodSetTheme);
	}

	private final IMethodInvoker mMethodReturn = parameters -> {
		LayoutFragment fragment = Cast.as(LayoutFragment.class, getContext().getDataView());
		// The return action should consider if it is a fragment inside a dialog or not (dialog = popup | callout).
		if (fragment == null || fragment.getDialog() == null) // TODO: Should remove the call to .getDialog so the CoreExternalObjects module doesn't have to depend on appcompat.
			SDActionsHelper.returnAction(getActivity());
		else
			fragment.returnOK();

		return ExternalApiResult.SUCCESS_CONTINUE;
	};

	private final IMethodInvoker mMethodExit = parameters -> {
		CompositeAction action = getAction().getParentComposite();
		action.setAsDone();
		action.setLoopCondition(null);
		return ExternalApiResult.SUCCESS_CONTINUE;
	};

	private final IMethodInvoker mMethodGoHome = parameters -> {
		// Go Home and clear stack.
		IViewDefinition mainView = Services.Application.get().getMain();
		Intent intent = IntentFactory.getMainObject(mainView, getActivity(), true);
		getActivity().startActivity(intent);
		return ExternalApiResult.SUCCESS_WAIT;
	};

	private ExternalApiResult methodRefresh(boolean keepPosition) {
		final IGxActivity currentActivity = Cast.as(IGxActivity.class, getActivity());
		final IDataView currentComponent = getContext().getDataView();

		final RefreshParameters refreshParams = new RefreshParameters(RefreshParameters.Reason.MANUAL, keepPosition);

		Services.Device.invokeOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (currentComponent != null) {
					// do finishEdit in UI thread.
					BaseFragment fragment = Cast.as(BaseFragment.class, currentComponent);
					if (fragment != null)
						fragment.finishEdit();
					// do refresh data
					currentComponent.refreshData(refreshParams);
				}
				else
					currentActivity.refreshData(refreshParams);
			}
		});

		return ExternalApiResult.SUCCESS_CONTINUE;
	}

	private final IMethodInvoker mMethodRefresh = parameters -> methodRefresh(false);

	private final IMethodInvoker mMethodRefreshKeep = parameters -> {
		boolean keepPosition = parameters.get(0) != null && PARAMETER_REFRESH_KEEP.equalsIgnoreCase(parameters.get(0).toString());
		return methodRefresh(keepPosition);
	};

	private final IMethodInvoker mMethodSave = parameters -> {
		if (getContext().getDataView() instanceof LayoutFragmentEditBC) {
			final LayoutFragmentEditBC editFragment = (LayoutFragmentEditBC) getContext().getDataView();
			Services.Device.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					editFragment.runSaveAction();
				}
			});

			return ExternalApiResult.SUCCESS_WAIT;
		} else
			return ExternalApiResult.SUCCESS_CONTINUE;
	};

	private final IMethodInvoker mMethodCancel = parameters -> {
		// The Cancel() action should consider if it is a fragment inside a dialog or not (dialog = popup | callout).
		LayoutFragment fragment = Cast.as(LayoutFragment.class, getContext().getDataView());
		if (fragment == null || fragment.getDialog() == null) // TODO: Should remove the call to .getDialog so the CoreExternalObjects module doesn't have to depend on appcompat.
			ActivityFlowControl.finishWithCancel(getActivity());
		else
			fragment.returnCancel();

		return ExternalApiResult.SUCCESS_WAIT;
	};

	private final IMethodInvoker mMethodLogin = parameters -> {
		//Solve this as CallLogin action.
		throw new IllegalStateException("SDActions.Login should've been handled by CallLoginAction.");
	};

	private final IMethodInvoker mMethodLogout = parameters -> {
		// Clear token and cache, and call server.
		SecurityHelper.logout();
		return ExternalApiResult.SUCCESS_CONTINUE;
	};

	private final IMethodInvoker mMethodReturnTo = parameters -> {
		String objectToReturn = toString(parameters).get(0);
		if (Services.Strings.hasValue(objectToReturn))
			ActivityFlowControl.returnTo(getActivity(), getContext().getDataView(), objectToReturn);

		return ExternalApiResult.SUCCESS_WAIT;
	};

	private final IMethodInvoker mMethodCancelTo = parameters -> {
		String objectToReturn = toString(parameters).get(0);
		if (Services.Strings.hasValue(objectToReturn))
			ActivityFlowControl.cancelTo(getActivity(), getContext().getDataView(), objectToReturn);

		return ExternalApiResult.SUCCESS_WAIT;
	};

	// TODO: Migrate to PixelCopy API for Screenshot taking. Unify this with Live Editing logic perhaps.
	@SuppressWarnings("deprecation")
	private Uri getViewImage(View view) {
		try {
			File outputDir = getActivity().getCacheDir(); // context being the Activity pointer
			File outputFile = File.createTempFile("screen", ".png", outputDir);

			view.setDrawingCacheEnabled(true);
			Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
			try {
				view.setDrawingCacheEnabled(false);
				FileOutputStream out = new FileOutputStream(outputFile);
				try {
					bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
					out.flush();
					return Uri.fromFile(outputFile);
				} finally {
					IOUtils.closeQuietly(out);
				}
			} finally {
				bitmap.recycle();
			}
		} catch (IOException e) {
			Services.Log.error(e);
			return null;
		}
	}

	private Uri getApplicationScreenshot() {
		return Services.Device.invokeOnUiThread(() -> {
			View contentView = getActivity().getWindow().getDecorView().findViewById(android.R.id.content);
			return getViewImage(contentView);
		});
	}

	private final IMethodInvoker mMethodTakeAppScreenshot = parameters -> {
		Uri uri = getApplicationScreenshot();
		return ExternalApiResult.success(uri != null ? uri.toString() : Strings.EMPTY);
	};

	private final IMethodInvoker mMethodShowTarget = parameters -> {
		NavigationAPI.showTarget(getActivity(), String.valueOf(parameters.get(0)));
		return ExternalApiResult.SUCCESS_CONTINUE;
	};

	private final IMethodInvoker mMethodDoSub = parameters -> {
		// "Subroutine" .Do() call
		//Execute a sub rutine here. Continue this event after sub rutine event finish.
		String subToCall = toString(parameters).get(0);

		// get action from definition.
		IViewDefinition viewDef = this.getAction().getDefinition().getViewDefinition();
		ActionDefinition action = viewDef.getEvent(subToCall);

		// Copy parameter entity
		ActionParameters actionParameters = new ActionParameters(this.getAction().getParameterEntity());

		Services.Log.debug("call Sub: " + subToCall + " Do");

		// Call the new "Event"
		boolean isComposite = getAction().getIsComposite() || action.getIsComposite(); // use composite if caller or sub has composite
		CompositeAction newRunningAction = ActionFactory.getAction(getContext(), action, actionParameters, isComposite);
		// set parent composite for continue sub later.
		// TODO: not working correctly, see why?
		//if (getAction()!=null)
		//{
		//	newRunningAction.setCallerEventComposite(getAction().getParentComposite());
		//}
		ActionExecution exec = new ActionExecution(newRunningAction);
		exec.executeAction();

		return ExternalApiResult.SUCCESS_WAIT;
	};

	private final IMethodInvoker mMethodSetLanguage = parameters -> {
		String newLanguage = toString(parameters).get(0);

		if (!Services.Strings.hasValue(newLanguage)) {
			Services.Log.debug("Reset to system's default locale");
			Services.Language.setLocaleToSystemDefault(getActivity());
			return ExternalApiResult.success(0);
		}

		Language language = Services.Application.getDefinition().getLanguageCatalog().getLanguage(newLanguage);
		if (language == null) {
			Services.Log.warning("Language '" + newLanguage + "' not found in catalog");
			return ExternalApiResult.success(-1);
		}

		Locale previousLocale = Services.Language.getLocales().get(0);
		Locale newLocale = new Locale(language.getLanguageCode(), language.getCountryCode());

		if (!previousLocale.toString().equalsIgnoreCase(newLocale.toString())) {
			Services.Language.setLanguageAndLocale(getActivity(), language.getName(), newLocale, true);
		}

		return ExternalApiResult.success(0);
	};

	private final IMethodInvoker mMethodSetTheme = parameters -> {
		String themeToSet = toString(parameters).get(0);

		if (Services.Strings.hasValue(themeToSet)) {
			Services.Log.debug("set Theme to: " + themeToSet);
			if (Services.Themes.setCurrentTheme(getActivity(), themeToSet)) {
				Services.Themes.applyCurrentTheme(getActivity());
				return ExternalApiResult.success(1);
			}
			else
				Services.Log.warning("set Theme failed. Theme " + themeToSet + " not found.");
		}
		else {
			Services.Log.debug("set Theme empty. return to default theme");
			Services.Themes.reset();
		}

		return ExternalApiResult.success(0);
	};

	@NonNull
	@Override
	public List<String> restrictedMethods() {
		List<String> restrictions = new ArrayList<>();
		restrictions.add(METHOD_LOGIN);
		restrictions.add(METHOD_LOGOUT);
		return restrictions;
	}
}
