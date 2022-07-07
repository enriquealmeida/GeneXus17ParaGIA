package com.genexus.android.core.actions;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import androidx.annotation.NonNull;

import com.genexus.android.core.activities.ActivityHelper;
import com.genexus.android.core.adapters.AdaptersHelper;
import com.genexus.android.core.base.metadata.ActionDefinition;
import com.genexus.android.core.base.metadata.ActionParameter;
import com.genexus.android.core.base.metadata.IGxObjectDefinition;
import com.genexus.android.core.base.metadata.enums.Connectivity;
import com.genexus.android.core.base.metadata.expressions.Expression;
import com.genexus.android.core.base.metadata.expressions.Expression.Type;
import com.genexus.android.core.base.metadata.expressions.Expression.Value;
import com.genexus.android.core.base.metadata.expressions.ExpressionErrorException;
import com.genexus.android.core.base.metadata.expressions.ExpressionValueBridge;
import com.genexus.android.core.base.metadata.expressions.IAssignableExpression;
import com.genexus.android.core.base.metadata.expressions.IExpressionContext;
import com.genexus.android.core.base.model.Entity;
import com.genexus.android.core.base.model.EntityList;
import com.genexus.android.core.base.model.ValueCollection;
import com.genexus.android.core.base.providers.IApplicationServer;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.common.DataRequest;
import com.genexus.android.core.common.ExecutionContext;
import com.genexus.android.core.controllers.IDataSourceBoundView;
import com.genexus.android.core.controllers.ViewData;
import com.genexus.android.core.controls.IDataViewHosted;
import com.genexus.android.core.controls.IGxControl;
import com.genexus.android.core.controls.IGxEdit;
import com.genexus.android.core.utils.Cast;

public abstract class Action
{
	private final UIContext mContext;
	private final ActionDefinition mDefinition;
	private final ActionParameters mParameters;

	protected Activity mCurrentActivity;
	private CompositeAction mParentComposite;
	private CompositeAction mCallerEventComposite;

	protected static final String ERROR_VARIABLE_NAME = "&Err";
	protected static final String ERROR_MESSAGE_VARNAME = "&ErrMsg";
	public static final int ERROR_CODE_NONE = 0;
	public static final int ERROR_CODE_GENERIC = 1;
	public static final int ERROR_CODE_USER_CANCEL = 2;
	public static final int ERROR_CODE_INVALID_PARAM = 3;

	protected Action(UIContext context, ActionDefinition definition, ActionParameters parameters)
	{
		if (context == null)
			throw new IllegalArgumentException("Null UIContext passed to Action constructor.");

		mContext = context;
		mDefinition = definition;
		mParameters = (parameters != null ? parameters : ActionParameters.EMPTY);
	}

	protected Action(Action baseAction)
	{
		this(baseAction.getContext(), baseAction.getDefinition(), baseAction.getParameters());
	}

	public UIContext getContext() { return mContext; }

	protected Activity getActivity()
	{
		Activity activity = mContext.getActivity();

		if (activity == null)
			activity = ActivityHelper.getCurrentActivity();

		return activity;
	}

	public Activity getMyActivity()
	{
		if (mCurrentActivity != null && mCurrentActivity != getActivity())
			Services.Log.error("MyActivity different from context Activity. Action: " + mDefinition.getName());

		return (mCurrentActivity != null)? mCurrentActivity : getActivity();
	}

	public ActionDefinition getDefinition() { return mDefinition; }

	protected IApplicationServer getApplicationServer(@NonNull IGxObjectDefinition gxObject)
	{
		if (gxObject.getConnectivitySupport() != Connectivity.Inherit)
			return Services.Application.getApplicationServer(gxObject.getConnectivitySupport());
		else
			return getDefaultApplicationServer();
	}

	protected IApplicationServer getDefaultApplicationServer()
	{
		return Services.Application.getApplicationServer(getContext().getConnectivitySupport());
	}

	protected ActionParameters getParameters() { return mParameters; }
	public Entity getParameterEntity() { return mParameters.getEntity(); }

	void setParentComposite(CompositeAction parent)
	{
		mParentComposite = parent;
	}

	public CompositeAction getParentComposite()
	{
		return mParentComposite;
	}

	@SuppressWarnings("checkstyle:MethodName")
	public abstract boolean Do();

	public int getErrorCode() { // override if different return code is needed
		return Strings.hasValue(getErrorMessage()) ? ERROR_CODE_GENERIC : ERROR_CODE_NONE;
	}

	public abstract String getErrorMessage();

	/**
	 * Indicates that the action will wait for (and handle) the parent activity's OnActivityResult().
	 * Needed for actions that need to pause for user input by calling another activity.
	 */
	public boolean catchOnActivityResult() { return false; }

	/**
	 * Indicates that the current activity will finish when the action is executed
	 * (e.g. a call to another panel with CallOptions.Type = Replace).
	 */
	public boolean isActivityEnding() { return false; }

	/**
	 * Called after activity's onActivityResult() to process result.
	 * @param result Intent returned by the called activity.
	 * @return Desired action after processing intent.
	 */
	public ActionResult afterActivityResult(int requestCode, int resultCode, Intent result) { return ActionResult.SUCCESS_CONTINUE; }

	public ActionResult afterRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
	{
		return ActionResult.SUCCESS_CONTINUE;
	}

	/**
	 * Gets the list of parameters values of the action (evaluated on the
	 * context of the parameter entity).
	 */
	public List<Value> getParameterValues()
	{
		List<Value> parameterValues = new ArrayList<>();
		for (ActionParameter parameter : mDefinition.getParameters())
		{
			Value value = getParameterValue(parameter);
			parameterValues.add(value);
		}

		return parameterValues;
	}

	/**
	 * Evaluates a parameter to get its value (on the context of the parameter entity).
	 * Uses the expression (if supplied), otherwise the value.
	 */
	public @NonNull Value getParameterValue(ActionParameter parameter)
	{
		return getParameterValue(parameter, null);
	}

	private ExpressionContext mWaitingContext;
	protected void setActivityResultParameters(int requestCode, int resultCode, Intent result)
	{
		if (mWaitingContext != null)
			mWaitingContext.setActivityResultParameters(requestCode, resultCode, result);
	}

	protected void setRequestPermissionsResultParameters(int requestCode, String[] permissions, int[] grantResults)
	{
		if (mWaitingContext != null)
			mWaitingContext.setRequestPermissionsResultParameters(requestCode, permissions, grantResults);
	}

	/**
	 * Evaluates a parameter to get its value (on the context of the supplied entity).
	 * Uses the expression (if supplied), otherwise the value.
	 */
	protected @NonNull Value getParameterValue(ActionParameter parameter, Entity contextEntity)
	{
		if (contextEntity == null)
			contextEntity = getParameterEntity();

		if (parameter.getExpression() != null)
		{
			// Expressions are included for all code included in the events
			ExpressionContext context = mWaitingContext;
			if (context == null)
				context = new ExpressionContext(contextEntity);
			Value value;
			try {
				value = context.eval(parameter.getExpression());
			} catch (@SuppressWarnings("checkstyle:IllegalCatch") ClassCastException | IllegalArgumentException | NullPointerException ex) { // IllegalCatch is ignored to allow NullPointerException which is important to catch
				throw new ExpressionErrorException(parameter.getExpression(), contextEntity, ex);
			}
			mWaitingContext = value.getType() == Type.WAIT ? context : null;
			return value;
		}
		else
		{
			// For other constructed parameters, i.e. default action of grid
			// Value evaluation, supports constants and attributes/variables.
			return ActionParametersHelper.getParameterValue(contextEntity, parameter.getValue());
		}
	}

	/**
	 * Sets a variable with the value of the action output.
	 * e.g. after the action "&Var = Proc1.Udp()" or "&Var = SDActions.ScanBarcode()"
	 */
	protected void setOutputValue(final ActionParameter target, Value value)
	{
		if (target.getExpression() instanceof IAssignableExpression)
		{
			IAssignableExpression expression = (IAssignableExpression)target.getExpression();
			Entity entity = getParameterEntity();
			ExpressionContext context = new ExpressionContext(entity);

			// Set the value, then get the name of the "affected" variable/attribute to refresh its UI.
			if (expression.setValue(context, value))
			{
				String fieldName = expression.getFieldName();
				if (Strings.hasValue(fieldName))
					updateUIAfterOutput(entity, fieldName);
			}
			else
				Services.Log.warning("IAssignableExpression.setValue() failed. Expression is: " + expression.toString());
		}
		else
			setOutputValue(target.getValue(), value);
	}

	/**
	 * Sets a variable with the value of the action output.
	 * e.g. after the action "&Var = Proc1.Udp()" or "&Var = SDActions.ScanBarcode()"
	 */
	protected void setOutputValue(String name, Value value)
	{
		if (Strings.hasValue(name) && value != null)
		{
			final Entity entity = getParameterEntity();

			if (entity != null)
			{
				Object objValue = ExpressionValueBridge.convertValueToEntityFormat(value, entity.getPropertyDefinition(name));
				entity.setProperty(name, objValue);
				updateUIAfterOutput(entity, name);
			}
		}
	}

	/**
	 * Refresh the screen control(s) associated to the output, if any.
	 */
	private void updateUIAfterOutput(final Entity entity, final String fieldName)
	{
		if (getContext() == null || getActivity() == null)
			return;

		Services.Device.runOnUiThread(() -> {
			// Update individual fields.
			for (IGxEdit edit : findControlsBoundTo(getContext(), fieldName))
				AdaptersHelper.setEditValue(edit, entity);

			// Update (SDT) grids.
			String outputName = fieldName;
			int index = outputName.indexOf('.');
			if (index >= 0)
				outputName = outputName.substring(0, index);
			updateGridAfterOutput(entity, outputName);
		});
	}

	private void updateGridAfterOutput(Entity entity, String outputName)
	{
		if (Strings.hasValue(outputName))
		{
			List<IDataSourceBoundView> grids = findGridsAffectedBy(getContext(), outputName);
			for (IDataSourceBoundView grid : grids)
			{
				Object data = entity.getProperty(grid.getDataSourceMember());
				EntityList gridData = Cast.as(EntityList.class, data);
				if (gridData == null) {
					ValueCollection values = Cast.as(ValueCollection.class, data);
					if (values != null)
						gridData = AdaptersHelper.convertToEntityList(values);
				}

				if (gridData != null)
					grid.update(ViewData.customData(gridData, DataRequest.RESULT_SOURCE_SERVER));
			}
		}
	}

	private static List<IGxEdit> findControlsBoundTo(UIContext context, String name)
	{
		if (context == null)
			return new ArrayList<>();

		List<IGxEdit> boundControls = context.findControlsBoundTo(name);
		if (boundControls.size() != 0)
			return boundControls;

		return findControlsBoundTo(context.getParent(), name);
	}

	private static List<IDataSourceBoundView> findGridsAffectedBy(UIContext context, String assignedExpression)
	{
		ArrayList<IDataSourceBoundView> list = new ArrayList<>();

		while (context != null)
		{
			for (IDataSourceBoundView grid : context.findBoundGrids())
			{
				if (context.getDataView() != null && grid instanceof IDataViewHosted && context.getDataView() != ((IDataViewHosted)grid).getHost())
					continue; // Skip grids belonging to subcomponents.

				boolean isMatch = false;
				String gridBoundExpression = grid.getDataSourceMember();

				if (Strings.hasValue(gridBoundExpression)) // An SDT-bound grid.
				{
					// Case #1 - Exact match
					// Ex: &collection = <value>, with grid bound to &collection
					if (gridBoundExpression.equalsIgnoreCase(assignedExpression))
						isMatch = true;

					// Case #2 - Grid is superset of modified data
					// Ex: &collection.item(42).field = <value>, with grid bound to &collection
					if (Strings.starsWithIgnoreCase(assignedExpression, gridBoundExpression) && assignedExpression.contains(Strings.DOT))
						isMatch = true;

					// Case #3 - Grid is subset of modified data
					// Ex: &complexSdt = <value>, with grid bound to &complexSdt.subitem.collection
					if (Strings.starsWithIgnoreCase(gridBoundExpression, assignedExpression) && gridBoundExpression.contains(Strings.DOT))
						isMatch = true;
				}

				if (isMatch && !list.contains(grid))
					list.add(grid);
			}

			if (list.size() != 0)
				break;

			context = context.getParent();
		}

		return list;
	}

	protected IGxControl findControl(String name)
	{
		IGxControl control = findControl(mContext, name);

		if (control == null)
			Services.Log.warning(String.format("Control '%s' not found in the form.", name));

		return control;
	}

	private static IGxControl findControl(UIContext context, String name)
	{
		if (context == null)
			return null;

		IGxControl control = context.findControl(name);
		if (control != null)
			return control;

		// Search in parent context (e.g. update form control property from grid item action).
		return findControl(context.getParent(), name);
	}

	private class ExpressionContext implements IExpressionContext
	{
		private final Entity mExpressionContextEntity;

		private ExpressionContext(Entity expressionContextEntity)
		{
			mExpressionContextEntity = expressionContextEntity;
		}

		private Entity getExpressionContextEntity()
		{
			if (mExpressionContextEntity != null)
				return mExpressionContextEntity;

			return getParameterEntity();
		}

		@Override
		public ExecutionContext getExecutionContext()
		{
			return ExecutionContext.inAction(Action.this);
		}

		@Override
		public Value getValue(String name, Type expectedType)
		{
			return ExpressionValueBridge.convertEntityFormatToValue(getExpressionContextEntity(), name, expectedType);
		}

		@Override
		public void setValue(@NonNull String name, Value value)
		{
			Entity entity = getExpressionContextEntity();
			Object objValue = ExpressionValueBridge.convertValueToEntityFormat(value, entity.getPropertyDefinition(name));
			entity.setProperty(name, objValue);
		}

		@Override
		public IGxControl getControl(String name)
		{
			return findControl(name);
		}

		@Override
		public void updateUIAfterOutput(String name)
		{
			Action.this.updateUIAfterOutput(getExpressionContextEntity(), name);
		}

		@Override
		public Action getAction()
		{
			return Action.this;
		}

		private Map<Expression,Value> mCalculatedValue;
		private Expression mWaitExpression;
		private int mRequestCode;
		private int mResultCode;
		private Intent mResult;
		private String[] mPermissions;
		private int[] mGrantResults;
		private boolean mOnRequestPermissionsResult;

		protected void setActivityResultParameters(int requestCode, int resultCode, Intent result)
		{
			mRequestCode = requestCode;
			mResultCode = resultCode;
			mResult = result;
			mOnRequestPermissionsResult = false;
		}

		protected void setRequestPermissionsResultParameters(int requestCode, String[] permissions, int[] grantResults)
		{
			mRequestCode = requestCode;
			mPermissions = permissions;
			mGrantResults = grantResults;
			mOnRequestPermissionsResult = true;
		}

		@Override
		public @NonNull Value eval(Expression expression) {
			if (mCalculatedValue == null)
				mCalculatedValue = new IdentityHashMap<>();
			Value value = mCalculatedValue.get(expression);
			if (value == null) {
				value = expression.eval(this);
				if (mWaitExpression == null)
					mCalculatedValue.put(expression, value);
			}
			else if (value.getType() == Type.WAIT) {
				value = expression.eval(this);
				mCalculatedValue.put(expression, value);
				mWaitExpression = null;
			}

			if (mWaitExpression == null && value.getType() == Type.WAIT)
				mWaitExpression = expression;
			return value;
		}

		@Override
		public boolean isActivityResult(Expression expression) {
			return mWaitExpression == expression && !mOnRequestPermissionsResult;
		}

		@Override
		public boolean isRequestPermissionsResult(Expression expression) {
			return mWaitExpression == expression && mOnRequestPermissionsResult;
		}

		@Override
		public int requestCode() {
			return mRequestCode;
		}

		@Override
		public int resultCode() {
			return mResultCode;
		}

		@Override
		public Intent result() {
			return mResult;
		}

		@Override
		public String[] permissions() {
			return mPermissions;
		}

		@Override
		public int[] grantResults() {
			return mGrantResults;
		}
	}

	public enum ThreadPreference
	{
		MAIN_THREAD,
		BACKGROUND_THREAD
	}

	public @NonNull ThreadPreference getThreadPreference()
	{
		return ThreadPreference.BACKGROUND_THREAD;
	}

	// for sub rutine info
	public void setCallerEventComposite(CompositeAction parent)
	{
		mCallerEventComposite = parent;
	}

	public CompositeAction getCallerEventComposite()
	{
		return mCallerEventComposite;
	}

	public static boolean isPredefinedErrorVariable(String name) {
		return name.equalsIgnoreCase(ERROR_VARIABLE_NAME) || name.equalsIgnoreCase(ERROR_MESSAGE_VARNAME);
	}
}
