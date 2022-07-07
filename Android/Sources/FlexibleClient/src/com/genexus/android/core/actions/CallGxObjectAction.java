package com.genexus.android.core.actions;

import java.util.List;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.util.Pair;

import com.genexus.android.core.activities.ActivityHelper;
import com.genexus.android.core.activities.BTDeviceListActivity;
import com.genexus.android.core.activities.GenexusActivity;
import com.genexus.android.media.MediaUtils;
import com.genexus.android.core.base.application.IGxObject;
import com.genexus.android.core.base.application.OutputResult;
import com.genexus.android.core.base.metadata.ActionDefinition;
import com.genexus.android.core.base.metadata.ActionParameter;
import com.genexus.android.core.base.metadata.AttributeDefinition;
import com.genexus.android.core.base.metadata.DataItem;
import com.genexus.android.core.base.metadata.GxObjectDefinition;
import com.genexus.android.core.base.metadata.IViewDefinition;
import com.genexus.android.core.base.metadata.ObjectParameterDefinition;
import com.genexus.android.core.base.metadata.StructureDataType;
import com.genexus.android.core.base.metadata.StructureDefinition;
import com.genexus.android.core.base.metadata.VariableDefinition;
import com.genexus.android.core.base.metadata.enums.Connectivity;
import com.genexus.android.core.base.metadata.enums.DataTypes;
import com.genexus.android.core.base.metadata.enums.ImageUploadModes;
import com.genexus.android.core.base.metadata.expressions.Expression.Value;
import com.genexus.android.core.base.metadata.expressions.ExpressionValueBridge;
import com.genexus.android.core.base.metadata.layout.LayoutDefinition;
import com.genexus.android.core.base.metadata.layout.LayoutItemDefinition;
import com.genexus.android.core.base.model.Entity;
import com.genexus.android.core.base.model.EntityFactory;
import com.genexus.android.core.base.model.EntityList;
import com.genexus.android.core.base.model.PropertiesObject;
import com.genexus.android.core.base.providers.IApplicationServer;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.ResultDetail;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.common.SecurityHelper;
import com.genexus.android.core.fragments.IDataView;
import com.genexus.android.core.layers.LocalProcedure;

public class CallGxObjectAction extends ActionWithOutput
{
    private OutputResult mOutput;
	private Value mOutputValue;
	private boolean isReportObject = false;
	private IGxObject gxObject = null;

	CallGxObjectAction(UIContext context, ActionDefinition definition, ActionParameters parameters)
	{
		super(context, definition, parameters);
	}

	@Override
	public boolean Do()
	{
		ActionDefinition definition = getDefinition();
		GxObjectDefinition actionObject = Services.Application.getDefinition().getGxObject(definition.getGxObject());
		IApplicationServer server = getApplicationServer(actionObject);
		if (Strings.hasValue(definition.getActionPackage()))
			gxObject = server.getGxObject(definition.getActionPackage(), definition.getGxObject());
		else
			gxObject = server.getGxObject(definition.getGxObject());

		// llamada a select printer
		if (gxObject instanceof LocalProcedure && ((LocalProcedure)gxObject).isPrinterReport())
		{
			BluetoothDevice btDevice = BTDeviceListActivity.getBTDevice();
			if (btDevice == null)
			{
				isReportObject = true;
				Intent btIntent = new Intent(getActivity(), BTDeviceListActivity.class);
				getActivity().startActivityForResult(btIntent, BTDeviceListActivity.REQUEST_CONNECT_BT);
				ActivityHelper.registerActionRequestCode(BTDeviceListActivity.REQUEST_CONNECT_BT);
				return OutputResult.ok().isOk();
			}
		}
		Pair<OutputResult, Value> pair = runGxObject(this, gxObject, actionObject, definition, getParameters());
		mOutput = pair.first;
		mOutputValue = pair.second;
		return mOutput.isOk();
	}

	@Override
	public Activity getActivity()
	{
		return super.getActivity();
	}

	@Override
	public OutputResult getOutput()
	{
		return mOutput;
	}

	public Value getOutputValue() { return mOutputValue; }

	/**
	 * Calls a GeneXus object on the server and returns its result.
	 * Receives definition and parameters separately because they may differ from those in the action
	 * (e.g. if a custom action is mapped to a procedure call).
	 * However the output is assigned to the "real" action.
	 */
	private static Pair<OutputResult, Value> runGxObject(Action action, IGxObject gxObject, GxObjectDefinition actionObject, ActionDefinition definition, ActionParameters parameters)
	{
		beginWorking();
		try
		{
			// Read definition from action.
			List<ActionParameter> actionParameters = definition.getParameters();

			// Obtain implementation.
			IApplicationServer server = action.getApplicationServer(actionObject);

			// Prepare input parameters.
			ResultDetail<PropertiesObject> prepareResult = prepareCallParameters(server, action, definition, actionObject, parameters.getEntity());
			if (!prepareResult.getResult())
				return new Pair<>(OutputResult.error(prepareResult.getMessage()), null); // Abort because parameters could not be marshaled.

			// Call object.
			PropertiesObject callParameters = prepareResult.getData();
			OutputResult result = gxObject.execute(callParameters);

			Value outputValue = null; // get the last output in this variable
			if (result.isOk())
			{
				// Read output parameters.
				for (int i = 0; i < actionObject.getParameters().size(); i++)
				{
					ObjectParameterDefinition procParameter = actionObject.getParameter(i);
					if (procParameter.isOutput())
					{
						// Read result parameter from object.
						Object outValue = callParameters.getProperty(procParameter.getName());

						if (procParameter.isMediaOrBlob()) {
							// fix blob data type path from offline code.
							outValue = MediaUtils.translateGenexusBlobPathToUri(outValue);
						}
						// If an online object returns a SDT collection it would be JSONArray, those are set as Value of Type.OBJECT and are processed in EntitySerializer.deserializeStructureItem()
						outputValue = Value.newValueObject(outValue);

						// See if we have a local variable to assign it to.
						if (i < actionParameters.size())
						{
							ActionParameter actionParameter = actionParameters.get(i);
							if (actionParameter != null && actionParameter.isAssignable()) {
								action.setOutputValue(actionParameter, outputValue);
							}
						}
					}
				}
			}
			return new Pair<>(result, outputValue);
		}
		finally
		{
			endWorking();
		}
	}

	static ResultDetail<PropertiesObject> prepareCallParameters(IApplicationServer server, Action actionContext, ActionDefinition actionDefinition, GxObjectDefinition actionObject, Entity from)
	{
		PropertiesObject callParameters = new PropertiesObject();
		ResultDetail<Void> blobUploadError = null;

		if (actionObject != null)
		{
			List<ActionParameter> arguments = actionDefinition.getParameters();
			for (int index = 0; index < arguments.size() && index < actionObject.getParameters().size(); index++)
			{
				ObjectParameterDefinition parameter = actionObject.getParameters().get(index);
				if (parameter.isInput())
				{
					Value value = actionContext.getParameterValue(arguments.get(index), from);
					Object objValue = ExpressionValueBridge.convertValueToEntityFormat(value, parameter);
					callParameters.setProperty(parameter.getName(), objValue);

					int maxUploadSizeModeControlOrData = getMaxUploadSizeModeControlOrData(actionContext, arguments.get(index));
					int maxUploadSizeModeCalled = parameter.getMaximumUploadSizeMode();
					int maxUploadSizeMode = ImageUploadModes.resolveModeForUpload(maxUploadSizeModeControlOrData, maxUploadSizeModeCalled);

					// Upload any blobs in object parameters
					ResultDetail<Void> uploadResult = BCMethodHandler.uploadBlobsFromContainer(server, actionObject.getName(), maxUploadSizeMode, callParameters, parameter);
					if (blobUploadError == null && !uploadResult.getResult())
						blobUploadError = uploadResult; // Continue, but remember the error and return it at the end.
				}
			}
		}

		if (blobUploadError == null)
			return ResultDetail.ok(callParameters);
		else
			return ResultDetail.error(blobUploadError.getMessage(), callParameters);
	}

	private static int getMaxUploadSizeModeControlOrData(Action actionContext, ActionParameter argument) {
		if (argument.isAssignable()) { // This filter some of the non valid argument
			String argumentValue = argument.getValue();
			IDataView dataView = actionContext.getContext().getDataView();
			if (dataView != null) {
				LayoutDefinition layoutDefinition = dataView.getLayout();
				if (layoutDefinition!=null)
				{
					LayoutItemDefinition layoutItemDefinition = layoutDefinition.getDataControl(argumentValue);
					if (layoutItemDefinition != null)
						return layoutItemDefinition.getMaximumUploadSizeMode();

					VariableDefinition variableDefinition = layoutDefinition.getParent().getVariable(argumentValue);
					if (variableDefinition != null)
						return variableDefinition.getMaximumUploadSizeMode();
				}
				else
				{
					// in dasboard has only view definition
					IViewDefinition definition = dataView.getDefinition();
					VariableDefinition variableDefinition = definition.getVariable(argumentValue);
					if (variableDefinition != null)
						return variableDefinition.getMaximumUploadSizeMode();

				}
			}

			AttributeDefinition attributeDefinition = Services.Application.getDefinition().getAttribute(argumentValue);
			if (attributeDefinition != null)
				return attributeDefinition.getMaximumUploadSizeMode();

			// sdt items
			if (dataView != null) {
				int index = argumentValue.indexOf(Strings.DOT);
				if (index != -1) {
					String varName = argumentValue.substring(0, index);
					String fieldSpecifier = argumentValue.substring(index + 1);
					VariableDefinition variableDefinition = null;
					if (dataView.getLayout()!=null)
					{
						variableDefinition = dataView.getLayout().getParent().getVariable(varName);
					}
					else
					{
						variableDefinition = dataView.getDefinition().getVariable(varName);
					}
					if (variableDefinition != null && variableDefinition.getType() != null && variableDefinition.getType().equalsIgnoreCase(DataTypes.SDT))
					{
						StructureDataType sdt = Services.Application.getDefinition().getSDT(variableDefinition.getName());
						if (sdt != null)
						{
							DataItem item = sdt.getItem(fieldSpecifier);
							if (item != null)
								return item.getMaximumUploadSizeMode();
						}
					}
				}
			}
		}
		return ImageUploadModes.ACTUALSIZE; // It will use the other since it chooses the smaller
	}

	@Override
	public boolean catchOnActivityResult()
	{
		return isReportObject;
	}

	@Override
	public ActionResult afterActivityResult(int requestCode, int resultCode, Intent result)
	{
		if (requestCode == BTDeviceListActivity.REQUEST_CONNECT_BT) {
			if (resultCode == Activity.RESULT_OK) {
				// guardar selected printer
				BluetoothDevice btDevice = BTDeviceListActivity.getBTDevice();
				if (btDevice != null) {
					ActionDefinition definition = getDefinition();
					GxObjectDefinition actionObject = Services.Application.getDefinition().getGxObject(definition.getGxObject());
					IApplicationServer server = getApplicationServer(actionObject);
					mOutput = runGxObject( this, gxObject, actionObject, definition, getParameters()).first;
					return mOutput.isOk()? ActionResult.SUCCESS_CONTINUE:ActionResult.FAILURE;
				}

			}
			else
            {
				// cancelar call a proc.
				return ActionResult.FAILURE;
			}
		}
		return ActionResult.SUCCESS_CONTINUE;
	}



	private static int sWorkingCount = 0;
	private static final Object WORKING_LOCK = new Object();

	public static boolean isWorking()
	{
		synchronized (WORKING_LOCK) { return (sWorkingCount > 0); }
	}

	private static void beginWorking()
	{
		synchronized (WORKING_LOCK) { sWorkingCount++; }
	}

	private static void endWorking()
	{
		synchronized (WORKING_LOCK) { sWorkingCount--; }
	}


	/**
	 * Calls a GeneXus object on the server and returns its result.
	 * Receives definition and parameters
	 * Return the output as PropertiesObject.
	 */
	public static PropertiesObject runGxObjectFromProcedure(String objectToCall, PropertiesObject parameters)
	{
		GxObjectDefinition gxObjectDefinition = Services.Application.getDefinition().getGxObject(objectToCall);
		IApplicationServer server = Services.Application.getApplicationServer(Connectivity.Online);
		IGxObject gxObject = server.getGxObject(objectToCall);

		// get parameters definition for call this procedure
		List<ObjectParameterDefinition> actionParameters = gxObjectDefinition.getParameters();

		// Prepare input parameters.
		ResultDetail<PropertiesObject> prepareResult = prepareCallParametersFromProcedure(server, gxObjectDefinition, parameters);
		if (!prepareResult.getResult())
			return null; // if fails return null, caller should ignore result

		// Call object.
		PropertiesObject callParameters = prepareResult.getData();
		OutputResult result = gxObject.execute(callParameters);

		if (result.isOk())
		{
			// out are read with it index in out parameters , start in 1
			int count =1;
			PropertiesObject outParamteres = new PropertiesObject();

			StructureDefinition def = gxObjectDefinition.getParametersStructure();
			Entity parameterEntity = EntityFactory.newEntity(def);

			// Read output parameters.
			for (int index = 0; index < gxObjectDefinition.getParameters().size(); index++)
			{
				ObjectParameterDefinition procParameter = gxObjectDefinition.getParameter(index);
				if (procParameter.isOutput())
				{
					// Read result parameter from object.
					Object outValue = callParameters.getProperty(procParameter.getName());

					// convert using parameter entity to get the correct type.
					parameterEntity.setProperty(procParameter.getName(),outValue );

					outParamteres.setProperty( String.valueOf(count).toString(), parameterEntity.getProperty(procParameter.getName()));
					count++;

				}
			}
			return outParamteres;
		}
		else {
			// check for security error and redirect to login if necessary.
			if (ActivityHelper.getCurrentActivity() instanceof GenexusActivity) {
				GenexusActivity genexusActivity = (GenexusActivity) ActivityHelper.getCurrentActivity();

				String loginObjectName = Services.Application.get().getLoginObject();
				String objectName = null;
				if (genexusActivity.getMainDefinition()!=null)
					objectName = genexusActivity.getMainDefinition().getObjectName();

				// Don't launch another GAM Login panel if we're already in it
				if (objectName != null && !objectName.equals(loginObjectName)) {

					// If error message is "token expired", redirect to login.
					SecurityHelper.handleSecurityError(genexusActivity.getUIContext(), result.getStatusCode(), result.getErrorText(), null);
				}
			}
		}
		return null; // if fails return null, caller should ignore result
	}

	public static ResultDetail<PropertiesObject> prepareCallParametersFromProcedure(IApplicationServer server, GxObjectDefinition gxObjectDefinition, PropertiesObject from)
	{
		PropertiesObject callParameters = new PropertiesObject();
		ResultDetail<Void> blobUploadError = null;

		if (gxObjectDefinition != null)
		{
			// input parameter como with it index in all parameters
			List<ObjectParameterDefinition> arguments = gxObjectDefinition.getParameters();
			for (int index = 0; index < arguments.size() && index < gxObjectDefinition.getParameters().size(); index++)
			{
				ObjectParameterDefinition parameter = gxObjectDefinition.getParameters().get(index);
				if (parameter.isInput())
				{
					// value in numeric array index
					Object value = ActionParametersHelper.getParameterValueFromEntity(from, String.valueOf(index).toString());

					// if value is LinkedList convert to EntityList
					//this is for the tojson works ok later. need a basecollection
					// Just create a new wrapper EntityList if an unsupported List type arrives.
					// TODO: Remove this when generator creates an EntityList.
					if (value instanceof List<?>)
					{
						List<?> otherList = (List<?>)value;

						// Check for first item of correct type, assume all others match too.
						// list of Entities of the same type / definition
						if (otherList.size() == 0 || otherList.get(0) instanceof Entity)
							value = new EntityList((Iterable<Entity>)otherList, ((Entity)otherList.get(0)).getDefinition());
					}

					// set values in parameters names to call procedure
					callParameters.setProperty(parameter.getName(), value);

					//int maxUploadSizeModeControlOrData = getMaxUploadSizeModeControlOrData(actionContext, arguments.get(index));
					int maxUploadSizeModeControlOrData = ImageUploadModes.LARGE;
					int maxUploadSizeModeCalled = parameter.getMaximumUploadSizeMode();
					int maxUploadSizeMode = ImageUploadModes.resolveModeForUpload(maxUploadSizeModeControlOrData, maxUploadSizeModeCalled);

					// Upload any blobs in object parameters
					ResultDetail<Void> uploadResult = BCMethodHandler.uploadBlobsFromContainer(server, gxObjectDefinition.getName(), maxUploadSizeMode, callParameters, parameter);
					if (blobUploadError == null && !uploadResult.getResult())
						blobUploadError = uploadResult; // Continue, but remember the error and return it at the end.
				}
			}
		}

		if (blobUploadError == null)
			return ResultDetail.ok(callParameters);
		else
			return ResultDetail.error(blobUploadError.getMessage(), callParameters);
	}

}
