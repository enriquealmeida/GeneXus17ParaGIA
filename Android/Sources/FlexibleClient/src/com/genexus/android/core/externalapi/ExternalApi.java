package com.genexus.android.core.externalapi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;

import com.genexus.android.core.actions.Action;
import com.genexus.android.core.actions.ActionResult;
import com.genexus.android.core.actions.ApiAction;
import com.genexus.android.core.actions.UIContext;
import com.genexus.android.core.activities.ActivityController;
import com.genexus.android.core.activities.ActivityHelper;
import com.genexus.android.PermissionUtil;
import com.genexus.android.WithBackgroundPermission;
import com.genexus.android.WithPermission;
import com.genexus.android.core.base.metadata.ActionDefinition;
import com.genexus.android.core.base.metadata.expressions.Expression;
import com.genexus.android.core.base.metadata.expressions.Expression.Value;
import com.genexus.android.core.base.metadata.expressions.IExpressionContext;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Function;
import com.genexus.android.core.base.utils.ResultRunnable;
import com.genexus.android.core.base.utils.SafeBoundsList;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.externalapi.superapps.IMethodRestrictions;
import com.genexus.android.core.externalapi.superapps.MiniAppExternalApi;
import com.genexus.android.core.externalapi.superapps.SuperAppExternalApi;
import com.genexus.android.core.utils.Cast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public abstract class ExternalApi {
	private String mDebugTag = null;
	private Action mAction;

	public ExternalApi(@Nullable ApiAction action) {
		setAction(action);
	}

	public final void setDebugTag(String tag) {
		mDebugTag = tag;
	}

	public final ApiAction getAction() { return Cast.as(ApiAction.class, mAction); }
	public final ActionDefinition getDefinition() { return mAction.getDefinition(); }
	public final UIContext getContext() { return mAction.getContext(); }
	public final Activity getActivity() { return mAction.getMyActivity(); }
	public final void setAction(@Nullable Action action) {
		Action oldAction = mAction;
		mAction = action;
		onActionChanged(oldAction, mAction);
		Activity oldActivity = oldAction == null ? null : oldAction.getMyActivity();
		Activity newActivity = mAction == null ? null : mAction.getMyActivity();
		if (oldActivity != newActivity) {
			onActivityChanged(oldActivity, newActivity);
		}
	}

	/**
	 * Called when the action changes, including in initialization, usefull for Api with recrateWithExecution = false
	 * @param oldAction the action before the change
	 * @param newAction the current action
	 */
	public void onActionChanged(@Nullable Action oldAction, @Nullable Action newAction) { }

	/**
	 * Called when the activity changes, including in initialization, usefull for Api with recrateWithExecution = false
	 * @param oldActivity the activity before the change
	 * @param newActivity the current activity
	 */
	public void onActivityChanged(@Nullable Activity oldActivity, @Nullable Activity newActivity) { }

	private final HashMap<HashKey, IMethodInvoker> mHandlers = new HashMap<>();
	private WithPermission<ExternalApiResult> mWaitForRequestPermissionResult;

	/**
	 * Utility method to convert parameter values from objects to strings.
	 * Should be used at the start of execute() if only string values are to be received.
	 */
	protected static SafeBoundsList<String> toString(List<Object> values) {
		SafeBoundsList<String> strValues = new SafeBoundsList<>();
		for (Object obj : values)
			strValues.add(obj != null ? obj.toString() : Strings.EMPTY);

		return strValues;
	}

	public @NonNull
	ExternalApiResult execute(String method, List<Object> parameters) {
		return invokeMethod(method, parameters);
	}

	public @NonNull
	Value execute(Expression expression, IExpressionContext context, String method, List<Object> parameters) {
		ExternalApiResult result;
		if (context.isActivityResult(expression))
			result = afterActivityResult(context.requestCode(), context.resultCode(), context.result(), method, parameters);
		else if (context.isRequestPermissionsResult(expression))
			result = afterRequestPermissionsResult(context.requestCode(), context.permissions(), context.grantResults());
		else
			result = execute(method, parameters);

		if (result != null && result.getActionResult() == ActionResult.FAILURE) {
			if (Services.Strings.hasValue(result.getMessage()))
				Services.Log.error(result.getMessage());
			return Value.FAIL;
		} else if (result == ExternalApiResult.SUCCESS_WAIT) {
			return Value.WAIT;
		}

		Object returnValue = result == null ? null : result.getReturnValue();
		if (returnValue == null)
			return Value.newString(null); // return something to avoid exception
		else
			return Value.newValue(returnValue);
	}

	private ExternalApiResult request(String[] requestPermissions, String[] neededPermissions, ResultRunnable<ExternalApiResult> successCode) {
		return new WithPermission.Builder<ExternalApiResult>(getActivity())
				.setBlockThread(false)
				.onPermissionRequested(mOnPermissionRequested)
				.require(neededPermissions)
				.request(requestPermissions)
				.onSuccess(successCode)
				.onFailure(() -> ExternalApiResult.FAILURE)
				.lockAlternativeCode() // These two handlers CANNOT be changed.
				.build()
				.run();
	}

	private ExternalApiResult requestBackground(String[] requestPermissions, String[] neededPermissions, ResultRunnable<ExternalApiResult> successCode) {
		WithBackgroundPermission<ExternalApiResult> request = new WithBackgroundPermission<>(getActivity(), requestPermissions, neededPermissions);
		request.setSuccessCode(successCode);
		request.setFailureCode(() -> ExternalApiResult.FAILURE);
		request.setOnPermissionRequested(mOnPermissionRequested);
		return request.run();
	}

	private final Function<WithPermission<ExternalApiResult>, ExternalApiResult> mOnPermissionRequested = new Function<WithPermission<ExternalApiResult>, ExternalApiResult>() {
		@Override
		public ExternalApiResult run(WithPermission<ExternalApiResult> runner) {
			// Register this object as waiting for the permission request result.
			mWaitForRequestPermissionResult = runner;

			ActivityController controller = ActivityController.from(getActivity());
			if (controller != null)
				controller.setActionWaitingForPermissionsResult(mAction);

			return ExternalApiResult.SUCCESS_WAIT;
		}
	};

	public ExternalApiResult afterActivityResult(int requestCode, int resultCode, Intent result, String method, List<Object> methodParameters) {
		IMethodInvoker handler = mHandlers.get(new HashKey(method, methodParameters.size()));

		if (handler instanceof IMethodInvokerWithActivityResult) {
			IMethodInvokerWithActivityResult activityResultHandler = (IMethodInvokerWithActivityResult) handler;
			return activityResultHandler.handleActivityResult(requestCode, resultCode, result);
		}

		return null;
	}

	public final ExternalApiResult afterRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		if (mWaitForRequestPermissionResult != null)
			return mWaitForRequestPermissionResult.onRequestPermissionsResult(requestCode, permissions, grantResults);

		return null;
	}

	public @Nullable Iterator<?> iterator() {
		return null;
	}

	/**
	 * Register a pair of IMethodInvokers (getter & setter) to handle a particular property of the External Object.
	 */
	protected final void addPropertyHandler(String property, IMethodInvoker getterHandler, IMethodInvoker setterHandler) {
		mHandlers.put(new HashKey(property, 0), getterHandler);
		mHandlers.put(new HashKey("set" + property, 1), setterHandler);
	}

	/**
	 * Register a IMethodInvokers to get the value of a particular property of the External Object.
	 */
	protected final void addReadonlyPropertyHandler(String property, IMethodInvoker getterHandler) {
		mHandlers.put(new HashKey(property, 0), getterHandler);
	}

	/**
	 * Register a IMethodInvokers to get the value of a particular property of the External Object,
	 * but only if the needed permissions are already held by the application or if the uses
	 * accepts granting them.
	 */
	protected final void addReadonlyPropertyHandlerRequestingPermission(String property, final String[] permissions, IMethodInvoker getterHandler) {
		addMethodHandlerRequestingPermissions(property, 0, permissions, getterHandler);
	}

	/**
	 * Register a IMethodInvoker to handle a particular method of the External Object.
	 */
	protected final void addMethodHandler(String method, int argsCount, IMethodInvoker handler) {
		mHandlers.put(new HashKey(method, argsCount), handler);
	}

	/**
	 * Register a ISimpleMethodInvoker to handle a particular method of the External Object.
	 * Unlike addMethodHandler, this method doesn't have to return an ExternalApiResult, and
	 * is assumed to always succeed.
	 */
	protected final void addSimpleMethodHandler(String method, int argsCount, final ISimpleMethodInvoker handler) {
		// Use a wrapper to map a "simple" output to ExternalApiResult.
		mHandlers.put(new HashKey(method, argsCount), parameters -> {
			Object returnValue = handler.invoke(parameters);
			return ExternalApiResult.success(returnValue);
		});
	}

	protected final void addMethodHandlerRequestingPermissions(String method, int argsCount, final String[] permissions, final IMethodInvoker handler) {
		executeWithPermissionHandlerWrapper(method, argsCount, null, permissions, handler);
	}

	protected final void addMethodHandlerRequestingPermissions(String method, int argsCount, String[] requestPermissions, final String[] neededPermissions, final IMethodInvoker handler) {
		executeWithPermissionHandlerWrapper(method, argsCount, requestPermissions, neededPermissions, handler);
	}

	/**
	 * Register an IMethodHandler to handle a particular method of the External Object, but
	 * only if the needed permissions are already held by the application or if the uses
	 * accepts granting them.
	 *
	 * @param method             Method name
	 * @param argsCount          Argument count (for overloading)
	 * @param neededPermissions  Needed permissions
	 * @param requestPermissions Request permissions
	 * @param handler            Handler to be called
	 */
	protected final void executeWithPermissionHandlerWrapper(String method, int argsCount, String[] requestPermissions, final String[] neededPermissions, final IMethodInvoker handler) {
		// Use a wrapper that requests the permissions and then calls the original handler.
		if (handler instanceof IMethodInvokerWithActivityResult) {
			addMethodHandler(method, argsCount, new IMethodInvokerWithActivityResult() {
				@Override
				public @NonNull
				ExternalApiResult handleActivityResult(int requestCode, int resultCode, @Nullable Intent result) {
					return ((IMethodInvokerWithActivityResult) handler).handleActivityResult(requestCode, resultCode, result);
				}

				@Override
				public @NonNull ExternalApiResult invoke(List<Object> parameters) {
					if (requestPermissions == null || PermissionUtil.checkSelfPermissions(Services.Application.getAppContext(), neededPermissions))
						return executeRequestingPermissions(neededPermissions, () -> handler.invoke(parameters));
					else
						return executeRequestingPermissions(requestPermissions, neededPermissions, () -> handler.invoke(parameters));
				}
			});
		} else {
			if (requestPermissions == null || PermissionUtil.checkSelfPermissions(Services.Application.getAppContext(), neededPermissions))
				addMethodHandler(method, argsCount, parameters -> executeRequestingPermissions(neededPermissions, () -> handler.invoke(parameters)));
			else
				addMethodHandler(method, argsCount, parameters -> executeRequestingPermissions(requestPermissions, neededPermissions, () -> handler.invoke(parameters)));
		}
	}

	protected ExternalApiResult executeRequestingPermissions(String[] neededPermissions, ResultRunnable<ExternalApiResult> code) {
		return executeRequestingPermissions(null, neededPermissions, code);
	}

	protected ExternalApiResult executeRequestingPermissions(String[] requestPermissions, String[] neededPermissions, ResultRunnable<ExternalApiResult> code) {
		if (PermissionUtil.shouldRequestBackgroundLocationSeparately(getActivity(), new ArrayList<>(Arrays.asList(neededPermissions))))
			return executeRequestingBackgroundPermission(requestPermissions, neededPermissions, code);

		return request(requestPermissions, neededPermissions, code);
	}

	private ExternalApiResult executeRequestingBackgroundPermission(String[] requestPermissions, String[] neededPermissions, ResultRunnable<ExternalApiResult> code) {
		return requestBackground(requestPermissions, neededPermissions, code);
	}

	protected @NonNull
	ExternalApiResult invokeMethod(String method, List<Object> parameters) {
		if (!allowedByContext(method))
			return ExternalApiResult.failureContextNotAllowed(this);

		if (!TextUtils.isEmpty(mDebugTag))
			logMethodInvocation(mDebugTag, method, parameters);

		IMethodInvoker handler = mHandlers.get(new HashKey(method, parameters.size()));

		if (handler != null)
			return handler.invoke(parameters);
		else
			return ExternalApiResult.failureUnknownMethod(this, method);
	}

	protected @NonNull
	ExternalApiResult invokeMethod(String method) {
		return invokeMethod(method, Collections.emptyList());
	}

	protected final void startActivityForResult(Intent intent, int requestCode) {
		ActivityHelper.registerActionRequestCode(requestCode);
		mAction.getMyActivity().startActivityForResult(intent, requestCode);
	}

	private void logMethodInvocation(String tag, String method, List<Object> params) {
		StringBuilder message = new StringBuilder("CALL\n    " + method + "\nPARAMETERS\n");
		if (params.isEmpty()) {
			message.append("    None\n");
		} else {
			for (Object param : params) {
				message.append("    ").append(param).append("\n");
			}
		}
		Services.Log.debug(tag, message.toString());
	}

	protected interface IMethodInvoker {
		@NonNull
		ExternalApiResult invoke(List<Object> parameters);
	}

	protected interface IMethodInvokerWithActivityResult extends IMethodInvoker {
		@NonNull
		ExternalApiResult handleActivityResult(int requestCode, int resultCode, @Nullable Intent result);
	}

	protected interface ISimpleMethodInvoker {
		Object invoke(List<Object> parameters);
	}

	private static class HashKey {
		private final String mMethodName;
		private final int mArgsCount;
		private final int mHashCode;

		HashKey(String methodName, int argsCount) {
			mMethodName = methodName;
			mArgsCount = argsCount;
			mHashCode = methodName.toLowerCase(Locale.US).hashCode() * 31 + argsCount;
		}

		@Override
		public boolean equals(Object o) {
			if (!(o instanceof HashKey))
				return false;

			HashKey other = (HashKey) o;
			return this.mArgsCount == other.mArgsCount && this.mMethodName.equalsIgnoreCase(other.mMethodName);
		}

		@Override
		public int hashCode() {
			return mHashCode;
		}
	}

	public boolean allowedByContext(String method) {
		boolean allowed = true;
		if (this instanceof MiniAppExternalApi && !Services.Application.hasActiveMiniApp())
			allowed = false;
		else if (this instanceof SuperAppExternalApi && Services.Application.hasActiveMiniApp()) {
			List<String> restrictions = ((IMethodRestrictions) this).restrictedMethods();
			allowed = !(restrictions.isEmpty() || restrictions.contains(method));
		}

		if (!allowed)
			Services.Log.error(String.format("Context not allowed for '%s'", method));

		return allowed;
	}
}
