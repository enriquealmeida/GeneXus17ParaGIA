package com.genexus.android.core.actions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;

import com.genexus.android.core.activities.ActivityLauncher;
import com.genexus.android.core.activities.IntentFactory;
import com.genexus.android.core.activities.IntentParameters;
import com.genexus.android.core.app.ComponentParameters;
import com.genexus.android.core.base.metadata.ActionDefinition;
import com.genexus.android.core.base.metadata.ActionParameter;
import com.genexus.android.core.base.metadata.IDataViewDefinition;
import com.genexus.android.core.base.metadata.IViewDefinition;
import com.genexus.android.core.base.metadata.ObjectParameterDefinition;
import com.genexus.android.core.base.metadata.WorkWithDefinition;
import com.genexus.android.core.base.metadata.enums.DisplayModes;
import com.genexus.android.core.base.metadata.enums.RequestCodes;
import com.genexus.android.core.base.metadata.expressions.Expression.Value;
import com.genexus.android.core.base.model.Entity;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.common.IntentHelper;
import com.genexus.android.core.ui.navigation.CallOptionsHelper;
import com.genexus.android.core.ui.navigation.CallType;
import com.genexus.android.core.ui.navigation.Navigation;
import com.genexus.android.core.ui.navigation.NavigationHandled;
import com.genexus.android.core.ui.navigation.UIObjectCall;

public class WorkWithAction extends Action
{
	private final String mPattern;
	private final String mComponent;
	private final String mBCVariableName;

	private boolean mWaitForResult;
	private boolean mIsReplace;
	private String mErrorMessage;

	public static final String EXTRA_IS_BC_INSERT = "com.artech.actions.WorkWithAction.IS_BC_INSERT";
	public static final String EXTRA_INSERTED_ENTITY = "com.artech.actions.WorkWithAction.INSERTED_ENTITY";

	WorkWithAction(UIContext context, ActionDefinition definition, ActionParameters parameters)
	{
		super(context, definition, parameters);

		mPattern = getDefinition().getGxObject();
		mComponent = getDefinition().optStringProperty("@instanceComponent");
		mBCVariableName = getDefinition().optStringProperty("@bcVariable");

		mWaitForResult = true;
	}

	@Override
	public boolean Do()
	{
		mWaitForResult = true;
		mIsReplace = false;

		Intent intent = getIntentForAction();
		if (intent == null)
		{
			mErrorMessage = "DataView not found " + mPattern;
			return false;
		}

		ComponentParameters params = new ComponentParameters(getObject(), getMode(), getObjectParameters(), getFieldParameters());
		UIObjectCall call = new UIObjectCall(getContext(), params);

		NavigationHandled handled = Navigation.handle(call, intent);
		if (handled != NavigationHandled.NOT_HANDLED) {
			mWaitForResult = (handled == NavigationHandled.HANDLED_WAIT_FOR_RESULT);
		} else {
			mIsReplace = (CallOptionsHelper.getCallOptions(call.getObject(), call.getMode()).getCallType() == CallType.REPLACE);
			ActivityLauncher.startActivityForResult(getActivity(), intent, RequestCodes.ACTION);
		}
		
		mErrorMessage = "";
		return true;
	}

	@Override
	public String getErrorMessage() {
		return mErrorMessage;
	}

	private Intent getIntentForAction()
	{
		IViewDefinition dataView = getObject();
		if (dataView == null)
		{
			Services.Log.error("WW Action Intent null: DataView not found " + mPattern);
			return null;
		}

		short mode = getMode();
		Map<String, String> fieldParameters = null;
		if (mode != DisplayModes.VIEW)
			fieldParameters = getFieldParameters();

		// Insert does not use the panel's actual parameters. Either it receives a BC (via fieldParameters) or nothing.
		List<String> parameters = new ArrayList<>();
		if (mode != DisplayModes.INSERT)
			parameters = getObjectParameters();

		Intent intent = IntentFactory.getIntent(getContext(), dataView, parameters, mode, fieldParameters);

		if (mode == DisplayModes.INSERT && Strings.hasValue(mBCVariableName))
			intent.putExtra(EXTRA_IS_BC_INSERT, true);

		return intent;
	}

	IViewDefinition getObject()
	{
		WorkWithDefinition workwith = (WorkWithDefinition)Services.Application.getDefinition().getPattern(mPattern);
		if (workwith == null || workwith.getLevels().size() == 0)
			return null;

		// Find the data view inside the instance using the "component" property.
		IDataViewDefinition dataView = null;
		for (IDataViewDefinition wwDataView : workwith.getDataViews())
		{
			if (!Services.Strings.hasValue(mComponent) || Strings.toLowerCase(wwDataView.getName()).endsWith(Strings.toLowerCase(mComponent)))
			{
				dataView = wwDataView;
				break;
			}
 		}

		// Use default it not found (or not specified).
		if (dataView == null)
			dataView = workwith.getLevel(0).getList();

		return dataView;
	}

	private List<String> getObjectParameters()
	{
		return ActionParametersHelper.getParametersForDataView(this);
	}

	private Map<String, String> getFieldParameters()
	{
		return ActionParametersHelper.getParametersForBC(this);
	}

	private short getMode()
	{
		if (getDefinition().optStringProperty("@bcMode").equalsIgnoreCase("Delete"))
			return DisplayModes.DELETE;
		else if (getDefinition().optStringProperty("@bcMode").equalsIgnoreCase("Update"))
			return DisplayModes.EDIT;
		else if (getDefinition().optStringProperty("@bcMode").equalsIgnoreCase("Insert"))
			return DisplayModes.INSERT;
		else if (getDefinition().optStringProperty("@bcMode").equalsIgnoreCase("Edit"))
			return DisplayModes.EDIT;
		else // Default Mode
			return DisplayModes.VIEW;
	}

	@Override
	public boolean catchOnActivityResult()
	{
		return mWaitForResult;
	}

	@Override
	public boolean isActivityEnding()
	{
		return mIsReplace;
	}

	@Override
	public ActionResult afterActivityResult(int requestCode, int resultCode, Intent result)
	{
		boolean updatedData = false;

		if (resultCode != Activity.RESULT_OK)
			return ActionResult.SUCCESS_CONTINUE;

		if (getMode() == DisplayModes.INSERT && Strings.hasValue(mBCVariableName))
		{
			// Get the entity inserted and put it in the variable name.
			Entity updatedEntity = IntentHelper.getObject(result, EXTRA_INSERTED_ENTITY, Entity.class);
			if (updatedEntity != null)
			{
				setOutputValue(mBCVariableName, Value.newBC(updatedEntity));
				updatedData = true;
			}
			else
				Services.Log.warning("WorkWithAction with mode INS didn't return the inserted BC data.");
		}

		// Read output parameters for standard calls.
		// "BC" calls are special -- INS either returns a BC (see above) or nothing, UPD/DEL return nothing.
		if (getMode() == DisplayModes.VIEW)
		{
			List<Object> parameterValues = IntentHelper.getList(result, IntentParameters.PARAMETERS);
			if (parameterValues != null && getObject() != null)
			{
				List<ObjectParameterDefinition> parameterDefinitions = getObject().getParameters();
				for (int i = 0; i < parameterDefinitions.size(); i++)
				{
					ActionParameter callParameter = getDefinition().getParameter(i);
					ObjectParameterDefinition objectParameter = parameterDefinitions.get(i);
					// Read and assign if:
					// 1) the parameter is defined as output in the called object, and
					// 2) it was called with an assignable expression (i.e. a variable), and
					// 3) we actually have a value for that position.
					if (callParameter == null)
					{
						Services.Log.error("afterActivityResult callParameter is null. WWAction");
					}
					if (callParameter != null && callParameter.isAssignable() && objectParameter.isOutput() && parameterValues.size() > i)
					{
						Object parameterValue = parameterValues.get(i);
						if (parameterValue != null)
						{
							setOutputValue(callParameter, Value.newValue(parameterValue));
							updatedData = true;
						}
					}
				}
			}
		}

		return (updatedData ? ActionResult.SUCCESS_CONTINUE_NO_REFRESH : ActionResult.SUCCESS_CONTINUE);
	}
}
