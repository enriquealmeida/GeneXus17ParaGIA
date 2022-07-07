package com.genexus.android.core.actions;

import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;

import com.genexus.android.R;
import com.genexus.android.api.EventDispatcher;
import com.genexus.android.core.base.application.OutputResult;
import com.genexus.android.core.base.metadata.ActionDefinition;
import com.genexus.android.core.base.metadata.ActionParameter;
import com.genexus.android.core.base.metadata.enums.RequestCodes;
import com.genexus.android.core.base.metadata.expressions.Expression.Value;
import com.genexus.android.core.base.metadata.expressions.ExpressionValueBridge;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.externalapi.ExternalApi;
import com.genexus.android.core.externalapi.ExternalApiFactory;
import com.genexus.android.core.externalapi.ExternalApiResult;

public class ApiAction extends Action implements IActionWithOutput
{
	private final boolean mIsComposite;
	private final String mApiName;
	private final String mMethod;
	private final boolean mIsEvent;
	private final ActionParameter mReturnValue;

	private String mErrorMessage = Strings.EMPTY;
	private final ExternalApiFactory mExternalApiFactory;
	private ExternalApi mApiInstance = null;

	public boolean finishReturn = false;
	public Intent finishReturnResult = null;
	public int finishReturnRequestCode = RequestCodes.ACTION;
	public int finishReturnResultCode = Activity.RESULT_OK;
	public Activity finishReturnCurrentActivity = null;

	private Boolean mCatchOnActivityResult;

	public ApiAction(UIContext context, ActionDefinition definition, ActionParameters parameters, boolean isComposite)
	{
		super(context, definition, parameters);
		mIsComposite = isComposite;
		mApiName = definition.getGxObject();
		mMethod = definition.optStringProperty("@exoMethod");
		mIsEvent = definition.optBooleanProperty("@isEvent");
		mReturnValue = ActionHelper.newAssignmentParameter(definition, "@returnValue", ActionHelper.ASSIGN_LEFT_EXPRESSION);
		mExternalApiFactory = Services.Application.getExternalApiFactory();
	}

	@Override
	public boolean Do()
	{
		Services.Log.debug("ApiAction Do , action: api: " + mApiName + " method: " + mMethod + " action: " + getDefinition().getName());
		mCatchOnActivityResult = false;
		return mIsEvent ? fireExternalObjectEvent() : runExternalObjectMethod();
	}

	public boolean getIsComposite()
	{
		return mIsComposite;
	}

	private boolean fireExternalObjectEvent()
	{
		EventDispatcher.fireEvent(getContext(), mApiName, mMethod, getParameterValues());
		return true;
	}

	private boolean runExternalObjectMethod()
	{
		if (mApiInstance == null)
			mApiInstance = mExternalApiFactory.getInstance(mApiName, this);

		if (mApiInstance == null) {
			mErrorMessage = String.format(Services.Strings.getResource(R.string.GXM_InvalidDefinition), mApiName);
			return false;
		}

		if (!mApiInstance.allowedByContext(mMethod))
			return false;

		ExternalApiResult result = mApiInstance.execute(mMethod, getParameterObjectValues());

		//noinspection ConstantConditions
		if (result == null) // This is a sanity check -- supposedly never happens because execute() is annotated as @NonNull.
			throw new IllegalStateException("External API '" + mApiInstance.toString() + " returned a null ExternalAPIResult. This should never happen!");

		if (result.getReturnValue() != null)
			setOutputValue(mReturnValue, Value.newValueObject(result.getReturnValue())); // GetMyLocation() returns JSONObject, GetLocationHistory() returns JSONArray, those are set as Value of Type.OBJECT and are processed in EntitySerializer.deserializeStructureItem()

		mErrorMessage = result.getMessage();
		mCatchOnActivityResult = result.getActionResult() == ActionResult.SUCCESS_WAIT;

		return result.getActionResult().isSuccess();
	}

	public String getMethodName() {
		return mMethod;
	}

	private List<Object> getParameterObjectValues() {
		return ExpressionValueBridge.convertValuesToEntityFormat(getParameterValues());
	}

	@Override
	public boolean catchOnActivityResult()
	{
		// Services.Log.debug("ApiAction catchOnActivityResult, action: api: " + mApiName + " method: " + mMethod + " action: " + this.toString());

		if (mCatchOnActivityResult == null)
		{
			Services.Log.error("catchOnActivityResult() has been called BEFORE Do(). This is not allowed.");
			Services.Log.error("ApiAction catchOnActivityResult , action: api: " + mApiName + " method: " + mMethod + " action: " + this.toString());

			// wait until do is done?
			try
			{
				// This seems to be happens with the Sub .Do() action in some cases.
				Thread.sleep(1000);
			}
			catch (InterruptedException e) { }
			if (mCatchOnActivityResult == null)
			{
				throw new IllegalStateException("catchOnActivityResult() has been called BEFORE Do(). This is not allowed.");
			}
			Services.Log.debug("ApiAction catchOnActivityResult , api mCatchOnActivityResult: " + mCatchOnActivityResult);

		}
		// Services.Log.debug("ApiAction catchOnActivityResult , api mCatchOnActivityResult: " + mCatchOnActivityResult);
		return mCatchOnActivityResult;
	}

	@Override
	public ActionResult afterActivityResult(int requestCode, int resultCode, Intent result)
	{
		if (mApiInstance != null)
		{
			ExternalApiResult out = mApiInstance.afterActivityResult(requestCode, resultCode, result, mMethod, getParameterObjectValues());
			return translateResult(out);
		}

		return ActionResult.SUCCESS_CONTINUE;
	}

	@Override
	public ActionResult afterRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
	{
		if (mApiInstance != null)
		{
			ExternalApiResult out = mApiInstance.afterRequestPermissionsResult(requestCode, permissions, grantResults);
			return translateResult(out);
		}

		return ActionResult.SUCCESS_CONTINUE;
	}

	private ActionResult translateResult(ExternalApiResult apiResult)
	{
		if (apiResult != null)
		{
			if (apiResult.getReturnValue() != null) {
				setOutputValue(Value.newValueObject(apiResult.getReturnValue()));
			}

			if (apiResult.getActionResult() != null)
				return apiResult.getActionResult();
		}

		return ActionResult.SUCCESS_CONTINUE;
	}

	public boolean hasOutput()
	{
		return mReturnValue.getExpression() != null || Strings.hasValue(mReturnValue.getValue());
	}

	public void setOutputValue(Value outValue)
	{
		setOutputValue(mReturnValue, outValue);
	}

	@Override
	public String getErrorMessage() {
		return mErrorMessage;
	}

	public boolean isReturnAction()
	{
		return mApiName.equalsIgnoreCase("GeneXus.SD.Actions") && mMethod.equalsIgnoreCase("return");
	}

	public boolean isCancelAction()
	{
		return mApiName.equalsIgnoreCase("GeneXus.SD.Actions") && mMethod.toLowerCase(Locale.US).startsWith("cancel");
	}

	public boolean isLoginAction()
	{
		return mApiName.equalsIgnoreCase("GeneXus.SD.Actions") && mMethod.equalsIgnoreCase("login");
	}

	public boolean isLoginExternalAction()
	{
		return mApiName.equalsIgnoreCase("GeneXus.SD.Actions") && mMethod.equalsIgnoreCase("loginexternal");
	}

	public boolean isRefreshAction()
	{
		return mApiName.equalsIgnoreCase("GeneXus.SD.Actions") && mMethod.equalsIgnoreCase("refresh");
	}

	public boolean isSubDoAction()
	{
		return mApiName.equalsIgnoreCase("GeneXus.SD.Actions") && mMethod.equalsIgnoreCase("do");
	}

	@Override
	public Activity getActivity()
	{
		return super.getActivity();
	}

	@Override
	public OutputResult getOutput()
	{
		// Return output message error if exists.
		if (Services.Strings.hasValue(mErrorMessage))
			return OutputResult.error(mErrorMessage);
		else
			return OutputResult.ok();
	}
}
