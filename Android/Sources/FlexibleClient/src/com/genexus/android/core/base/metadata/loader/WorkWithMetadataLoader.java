package com.genexus.android.core.base.metadata.loader;

import android.content.Context;

import androidx.annotation.Nullable;

import java.util.List;

import com.genexus.android.core.actions.ActionParametersHelper;
import com.genexus.android.core.base.metadata.ActionDefinition;
import com.genexus.android.core.base.metadata.ActionParameter;
import com.genexus.android.core.base.metadata.DataItem;
import com.genexus.android.core.base.metadata.DataSourceDefinition;
import com.genexus.android.core.base.metadata.DataViewDefinition;
import com.genexus.android.core.base.metadata.DetailDefinition;
import com.genexus.android.core.base.metadata.IDataSourceDefinition;
import com.genexus.android.core.base.metadata.IDataViewDefinition;
import com.genexus.android.core.base.metadata.IPatternMetadata;
import com.genexus.android.core.base.metadata.IViewDefinition;
import com.genexus.android.core.base.metadata.ObjectParameterDefinition;
import com.genexus.android.core.base.metadata.SectionDefinition;
import com.genexus.android.core.base.metadata.VariableDefinition;
import com.genexus.android.core.base.metadata.WWLevelDefinition;
import com.genexus.android.core.base.metadata.WWListDefinition;
import com.genexus.android.core.base.metadata.WorkWithDefinition;
import com.genexus.android.core.base.metadata.expressions.Expression;
import com.genexus.android.core.base.metadata.expressions.ExpressionFactory;
import com.genexus.android.core.base.metadata.layout.LayoutDefinition;
import com.genexus.android.core.base.serialization.INodeCollection;
import com.genexus.android.core.base.serialization.INodeObject;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.PlatformHelper;
import com.genexus.android.core.base.utils.Strings;

public class WorkWithMetadataLoader extends MetadataLoader
{
	@Override
	public IPatternMetadata load(Context context, String name)
	{
		INodeObject jsonData = getDefinition(context, Strings.toLowerCase(name));
		if (jsonData == null)
			return null;

		Services.Log.debug(String.format("Loading '%s'.", name));
		return loadJSON(jsonData);
	}

	public WorkWithDefinition loadJSON(INodeObject json)
	{
		WorkWithDefinition wwMetadata = new WorkWithDefinition();
		INodeObject jsonInstance = json.getNode("instance");
		INodeCollection jsonLevels = jsonInstance.optCollection("level");

		/*
		// Read BC (we can have Patterns without businessComponent associated).
		INodeObject bcJson = jsonInstance.getNode("businessComponent");
		readBusinessComponent(wwMetadata, bcJson);
		 */
		String bc = jsonInstance.optString("@businessComponent");
		if (bc.length() > 0)
		{
			/* set BC for ww based in bc */
			//wwMetadata.setBusinessComponent(MetadataLoader.getObjectName(bc));
			wwMetadata.setBusinessComponent(bc);
		}

		//Read Instance Properties
		INodeObject instancePropNode = jsonInstance.getNode("instanceProperties");
		wwMetadata.getInstanceProperties().deserialize(instancePropNode);


		// Read levels
		for (int i = 0; i < jsonLevels.length(); i++)
		{
			INodeObject jsonLevel = jsonLevels.getNode(i);
			WWLevelDefinition level = readLevelDefinition(wwMetadata, jsonLevel);
			wwMetadata.getLevels().add(level);
		}

		return wwMetadata;
	}

	private WWLevelDefinition readLevelDefinition(WorkWithDefinition wwMetadata, INodeObject levelJson)
	{
		String levelId = levelJson.optString("@id");
		WWLevelDefinition levelDefinition = new WWLevelDefinition(wwMetadata, levelId);

		String levelName = levelJson.optString("@name", Strings.EMPTY);
		levelDefinition.setName(levelName);

		levelDefinition.setDescription(levelJson.optString("@description", Strings.EMPTY));

		INodeObject listJson = levelJson.getNode("list");
		if (listJson != null)
		{
			WWListDefinition list = readList(wwMetadata, levelDefinition, listJson);
			levelDefinition.setList(list);
		}

		INodeObject jsonDetail = levelJson.getNode("detail");
		if (jsonDetail != null)
			readDetail(wwMetadata, levelDefinition, jsonDetail);

		return levelDefinition;
	}

	private void readDetail(WorkWithDefinition wwMetadata, WWLevelDefinition wwLevelDefinition, INodeObject jsonDetail)
	{
		DetailDefinition detail = new DetailDefinition(wwLevelDefinition);
		detail.deserialize(jsonDetail);

		readSections(wwMetadata, wwLevelDefinition, detail, jsonDetail);
		readFormParameters(detail.getParameters(), jsonDetail);
		readData(jsonDetail, detail);
		readVariables(detail, jsonDetail);
		readRules(detail, jsonDetail);
		copyVariablesProperties(detail);

		readPanelActions(detail, jsonDetail, detail.getActions());
		readLayouts(detail, jsonDetail);

		wwLevelDefinition.setDetail(detail);
	}

	//Read FormMetadata Parameters
	private static void readFormParameters(List<ObjectParameterDefinition> parameters, INodeObject viewJson)
	{
		INodeObject jsonParameters = viewJson.getNode("parameters");
		if (jsonParameters != null)
			readObjectParameterList(parameters, jsonParameters);
	}

	public static void readObjectParameterList(List<ObjectParameterDefinition> parameters, INodeObject jsonParameters)
	{
		for (INodeObject jsonParameter : jsonParameters.optCollection("parameter"))
		{
			String name = jsonParameter.getString("@name");
			String mode = jsonParameter.optString("@accessor");

			ObjectParameterDefinition parameter = new ObjectParameterDefinition(name, mode);
			parameter.readDataType(jsonParameter);
			parameters.add(parameter);
		}
	}

	private static void readSections(WorkWithDefinition wwMetadata, WWLevelDefinition levelDefinition, DetailDefinition detail, INodeObject viewJson)
	{
		INodeObject sectionsJson = viewJson.getNode("sections");
		if (sectionsJson != null)
		{
			INodeCollection sectionsArray = sectionsJson.optCollection("section");
			for (int i = 0; i < sectionsArray.length() ; i++)
			{
				INodeObject sectionJson = sectionsArray.getNode(i);
				readSection(wwMetadata, detail, sectionJson);
			}
		}
	}

	private static void readSection(WorkWithDefinition wwMetadata, DetailDefinition detail, INodeObject sectionJson)
	{
		SectionDefinition section = new SectionDefinition(detail);
		section.deserialize(sectionJson);

		readFormParameters(section.getParameters(), sectionJson);
		readData(sectionJson, section);
		readVariables(section, sectionJson);
		readRules(section, sectionJson);
		copyVariablesProperties(section);

		readPanelActions(section, sectionJson, section.getActions());
		readLayouts(section, sectionJson);

		detail.getSections().add(section);
	}

	private static void readListData(WorkWithDefinition wwMetadata, INodeObject jsonComponent, IDataViewDefinition component)
	{
		readData(jsonComponent, component);
		if (!Services.Strings.hasValue(wwMetadata.getBusinessComponentName()) )
		{
			//get bc from first collection datasource.
			for (IDataSourceDefinition ds : component.getDataSources())
			{
				if (ds.isCollection())
					wwMetadata.setBusinessComponent(ds.getAssociatedBCName());
			}
		}
	}

	private static void readData(INodeObject jsonComponent, IDataViewDefinition component)
	{
		String mainDataSourceName = jsonComponent.optString("@DataProvider");
		IDataSourceDefinition mainDataSource = null;

		// Read all data providers for this component.
		INodeCollection jsonDatas = jsonComponent.optCollection("data");
		for (int i = 0; i < jsonDatas.length(); i++)
		{
			IDataSourceDefinition dataSource = new DataSourceDefinition(component, jsonDatas.getNode(i));
			component.getDataSources().add(dataSource);

			if (dataSource.getName().equals(mainDataSourceName))
				mainDataSource = dataSource;
		}

		((DataViewDefinition)component).setMainDataSource(mainDataSource);
	}

	public static void readVariables(IViewDefinition dataView, INodeObject jsonDataView)
	{
		INodeObject jsonVariables = jsonDataView.getNode("variables");
		if (jsonVariables != null)
		{
			for (INodeObject jsonVariable : jsonVariables.optCollection("variable"))
			{
				VariableDefinition variable = new VariableDefinition(jsonVariable);
				dataView.getVariables().add(variable);
			}
		}
	}

	private static void readRules(IDataViewDefinition parent, INodeObject parentJson)
	{
		INodeObject jsonRules = parentJson.getNode("rules");
		if (jsonRules != null)
			parent.getRules().deserialize(jsonRules);
	}

	private static void copyVariablesProperties(DataViewDefinition dataViewDefinition) {
		for (ObjectParameterDefinition parameter : dataViewDefinition.getParameters()) {
			DataItem item = getDataItemFromViewDefinition(dataViewDefinition, parameter.getName());
			if (item == null)
				continue;

			parameter.setInternalProperties(item.getInternalProperties());
			parameter.setIsCollection(item.isCollection());
			parameter.setDataType(item.getBaseType());
		}
	}

	private static void readPanelActions(IDataViewDefinition parent, INodeObject parentJson, List<ActionDefinition> vector) {
		INodeObject actionsJson = parentJson.getNode("actions");
		if (actionsJson != null) {
			INodeCollection actionsArray = actionsJson.optCollection("action");
			readActionsArray(parent, actionsArray, vector);
		}
	}

	private static void readNextActions(IDataViewDefinition parent, INodeObject parentJson, List<ActionDefinition> vector) {
		INodeObject actionsJson = parentJson.getNode("components");
		if (actionsJson != null) {
			INodeCollection actionsArray = actionsJson.optCollection("action");
			readActionsArray(parent, actionsArray, vector);
		}
	}

	private static void readInnerActions(IDataViewDefinition parent, INodeObject parentJson, List<ActionDefinition> vector) {
		INodeCollection actionsArray = parentJson.optCollection("block");
		if (actionsArray.length() == 0)
			actionsArray = parentJson.optCollection("ForInStatementBlock");
		if (actionsArray.length() == 0)
			actionsArray = parentJson.optCollection("ForEachLineStatementBlock");
		readActionsArray(parent, actionsArray, vector);
	}

	private static void readActionsArray(IDataViewDefinition parent, INodeCollection actionsArray, List<ActionDefinition> vector) {
		if (actionsArray != null) {
			for (int k = 0; k < actionsArray.length() ; k++) {
				ActionDefinition actionDef = readAction(parent, actionsArray.getNode(k));
				if (actionDef != null)
					vector.add(actionDef);
			}
		}
	}

	private static ActionDefinition readAction(IDataViewDefinition parent, INodeObject nodeObject)
	{
		if (nodeObject == null)
			return null;

		ActionDefinition action = new ActionDefinition(parent);

		action.deserialize(nodeObject);

		// read actions Object and Parameters in Actions Definition itself (lazy loading)

		readNextActions(parent, nodeObject, action.getNextActions());
		readInnerActions(parent, nodeObject, action.getInnerActions());

		return action;
	}


	public static void readEventParameters(IViewDefinition parent, List<ActionParameter> eventParameterList, INodeObject jsonAction)
	{
		INodeObject jsonParameterList = jsonAction.getNode("eventParameters");
		if (jsonParameterList != null)
			readActionParameterList(parent, eventParameterList, jsonParameterList);
	}

	public static void readActionParameters(IViewDefinition parent, List<ActionParameter> parameterList, INodeObject jsonAction)
	{
		// Read Parameters in Order
		// "Parameters" is lower-case in WWSD, upper-case in Dashboard.
		INodeObject jsonParameterList = jsonAction.getNode("parameters");
		if (jsonParameterList == null)
			jsonParameterList = jsonAction.getNode("Parameters");

		if (jsonParameterList != null)
			readActionParameterList(parent, parameterList, jsonParameterList);
	}

	public static void readActionParameterList(IViewDefinition parent, List<ActionParameter> parameterList, INodeObject jsonParameterList)
	{
		if (jsonParameterList != null)
		{
			// Read parameters
			INodeCollection parArray = jsonParameterList.optCollection("parameter");
			for (int k = 0; k < parArray.length() ; k++)
			{
				ActionParameter parameter = readOneActionParameter(parent, parArray.getNode(k));
				parameterList.add(parameter);
			}
		}
	}

	private static ActionParameter readOneActionParameter(IViewDefinition parent, INodeObject oneParJson)
	{
		String name = oneParJson.optString("@name");
		String value = oneParJson.optString("@value");
		INodeObject expression = oneParJson.getNode("expression");

		ActionParameter parameter = newActionParameter(name, value, expression);
		DataItem dataItem = getDataItemFromViewDefinition(parent, value);
		if (dataItem != null)
			parameter.setValueDefinition(dataItem);

		return parameter;
	}

	public static ActionParameter newActionParameter(String jsonName, String jsonValue, INodeObject jsonExpression)
	{
		if (jsonValue != null)
			jsonValue = jsonValue.trim(); // Bug in IDE: Event parameters may contain leading/trailing spaces.

		Expression expression = null;
		if (jsonExpression != null)
			expression = ExpressionFactory.parse(jsonExpression);

		return new ActionParameter(jsonName, jsonValue, expression);
	}


	private WWListDefinition readList(WorkWithDefinition wwMetadata, WWLevelDefinition levelDefinition, INodeObject jsonList)
	{
		WWListDefinition list = new WWListDefinition(levelDefinition);
		list.deserialize(jsonList);

		readFormParameters(list.getParameters(), jsonList);
		readListData(wwMetadata, jsonList, list);
		readVariables(list, jsonList);
		readRules(list, jsonList);
		copyVariablesProperties(list);

		readPanelActions(list, jsonList, list.getActions());
		readLayouts(list, jsonList);

		return list;
	}

	private static @Nullable DataItem getDataItemFromViewDefinition(IViewDefinition definition, String name) {
		if (definition == null || ActionParametersHelper.isConstantExpression(name))
			return null;

		DataItem dataItem = definition.getVariable(name);
		if (dataItem != null)
			return dataItem;

		if (definition instanceof IDataViewDefinition) {
			for (IDataSourceDefinition ds : ((IDataViewDefinition) definition).getDataSources()) {
				dataItem = ds.getDataItem(name);
				if (dataItem != null)
					return dataItem;
			}
		}

		return null;
	}

	//SectionDefinition

	private static void readLayouts(IDataViewDefinition dataView, INodeObject jsonDataView)
	{
		INodeCollection jsonLayouts = jsonDataView.optCollection("layout");
		for (int i = 0; i < jsonLayouts.length(); i++)
		{
			LayoutDefinition layout = readLayout(dataView, jsonLayouts.getNode(i));

			if (PlatformHelper.isValidLayout(layout))
				dataView.getLayouts().add(layout);
		}
	}

	//Layout WWSelectionDefinition

	private static LayoutDefinition readLayout(IDataViewDefinition parent, INodeObject jsonLayout)
	{
		LayoutDefinition layoutDef = new LayoutDefinition(parent);
		layoutDef.setContent(jsonLayout);
		return layoutDef;
	}
}
