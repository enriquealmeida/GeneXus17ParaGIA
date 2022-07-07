window.addEventListener("load", () => {
	console.log("load")
	const conversationalDesigner = document.querySelector("gxcf-conversational-designer");
	console.log(conversationalDesigner);
	window.conversationalDesigner = {
		SetLanguage: function (language) {
			document.getElementsByTagName("html")[0].setAttribute("lang", language);
		},
		Render: function (instance) {
			conversationalDesigner.instance = JSON.parse(instance);
		}
	}

	conversationalDesigner.addEventListener(
		"selectConversationalObject",
		({
			detail:
			{
				flowName
			}
		}) => {
			window.external.SelectConversationalObject(flowName);
		}
	);

	conversationalDesigner.addEventListener(
		"addFlow",
		() => {
			window.external.AddFlow();
		}
	);

	conversationalDesigner.addEventListener(
		"moveFlow",
		({
			detail:
			{
				source,
				target,
				moveType
			}
		}) => {
			window.external.MoveFlow(source, target, moveType);
		}
	);

	conversationalDesigner.addEventListener(
		"setFlowCategory",
		({
			detail:
			{
				flowName,
				category
			}
		}) => {
			window.external.SetFlowCategory(flowName, category);
		}
	);

	conversationalDesigner.addEventListener(
		"modifyFlowName",
		({
			detail:
			{
				currentFlowName,
				newFlowName
			}
		}) => {
			window.external.ModifyFlowName(currentFlowName, newFlowName);
		}
	);

	conversationalDesigner.addEventListener(
		"setTriggers",
		({
			detail:
			{
				flowName,
				triggerMessages
			}
		}) => {
			window.external.SetTriggers(flowName, triggerMessages);
		}
	);

	conversationalDesigner.addEventListener(
		"setAskMessages",
		({
			detail:
			{
				flowName,
				userInput,
				askMessages
			}
		}) => {
			window.external.SetAskMessages(flowName, userInput, askMessages);
		}
	);

	conversationalDesigner.addEventListener(
		"setResponseMessages",
		({
			detail:
			{
				flowName,
				responseIndex,
				responseMessages
			}
		}) => {
			window.external.SetResponseMessages(flowName, responseIndex, responseMessages);
		}
	);

	conversationalDesigner.addEventListener(
		"addUserInput",
		({
			detail:
			{
				flowName
			}
		}) => {
			window.external.AddUserInput(flowName);
		}
	);

	conversationalDesigner.addEventListener(
		"addResponse",
		({
			detail:
			{
				flowName
			}
		}) => {
			window.external.AddResponse(flowName);
		}
	);

	conversationalDesigner.addEventListener(
		"setOnErrorMessages",
		({
			detail:
			{
				flowName,
				userInput,
				errorMessages
			}
		}) => {
			window.external.SetErrorMessages(flowName, userInput, errorMessages);
		}
	);

	conversationalDesigner.addEventListener(
		"selectValidationProcedure",
		({
			detail:
			{
				flowName,
				userInput
			}
		}) => {
			window.external.SelectValidationProcedure(flowName, userInput);
		}
	);

	conversationalDesigner.addEventListener(
		"changeCondition",
		({
			detail:
			{
				flowName,
				userInput,
				newCondition
			}
		}) => {
			window.external.SetUserInputRequiredCondition(flowName, userInput, newCondition);
		}
	);

	conversationalDesigner.addEventListener(
		"changeTryLimit",
		({
			detail:
			{
				flowName,
				userInput,
				value
			}
		}) => {
			window.external.SetTryLimit(flowName, userInput, value);
		}
	);

	conversationalDesigner.addEventListener(
		"changeResponseStyle",
		({
			detail:
			{
				flowName,
				responseIndex,
				style
			}
		}) => {
			window.external.SetStyle(flowName, responseIndex, style);
		}
	);

	conversationalDesigner.addEventListener(
		"addRedirection",
		({
			detail:
			{
				flowName,
				userInput
			}
		}) => {
			window.external.AddNewRedirection(flowName, userInput);
		}
	);

	conversationalDesigner.addEventListener(
		"changeUserInputRedirectCondition",
		({
			detail:
			{
				flowName,
				userInput,
				value,
				index
			}
		}) => {
			window.external.SetRedirectionCondition(flowName, userInput, value, index);
		}
	);

	conversationalDesigner.addEventListener(
		"changeUserInputRedirectTo",
		({
			detail:
			{
				flowName,
				userInput,
				value,
				index
			}
		}) => {
			window.external.SetRedirectTo(flowName, userInput, value, index);
		}
	);

	conversationalDesigner.addEventListener(
		"changeResponseCondition",
		({
			detail:
			{
				flowName,
				index,
				condition
			}
		}) => {
			window.external.SetResponseCondition(flowName, index, condition);
		}
	);

	conversationalDesigner.addEventListener(
		"changeResponseRedirectTo",
		({
			detail:
			{
				flowName,
				index,
				redirectTo
			}
		}) => {
			window.external.SetResponseRedirectTo(flowName, index, redirectTo);
		}
	);

	conversationalDesigner.addEventListener(
		"changeComponentType",
		({
			detail:
			{
				flowName,
				index,
				componentType
			}
		}) => {
			window.external.SetComponentType(flowName, index, componentType);
		}
	);

	conversationalDesigner.addEventListener(
		"changeWebComponent",
		({
			detail:
			{
				flowName,
				index
			}
		}) => {
			window.external.SetWebComponent(flowName, index);
		}
	);

	conversationalDesigner.addEventListener(
		"changeSDComponent",
		({
			detail:
			{
				flowName,
				index
			}
		}) => {
			window.external.SetSDComponent(flowName, index);
		}
	);

	conversationalDesigner.addEventListener(
		"deleteFlow",
		({
			detail:
			{
				flowName
			}
		}) => {
			window.external.DeleteFlow(flowName);
		}
	);

	conversationalDesigner.addEventListener(
		"deleteUserInput",
		({
			detail:
			{
				flowName,
				userInput
			}
		}) => {
			window.external.DeleteUserInput(flowName, userInput);
		}
	);

	conversationalDesigner.addEventListener(
		"deleteResponse",
		({
			detail:
			{
				flowName,
				index
			}
		}) => {
			window.external.DeleteResponse(flowName, index);
		}
	);

	conversationalDesigner.addEventListener(
		"changeResponseName",
		({
			detail:
			{
				flowName,
				index,
				value
			}
		}) => {
			window.external.SetResponseName(flowName, index, value);
		}
	);

	conversationalDesigner.addEventListener(
		"setCleanContextValue",
		({
			detail:
			{
				flowName,
				userInput,
				value
			}
		}) => {
			window.external.SetCleanContextValue(flowName, userInput, value);
		}
	);

	conversationalDesigner.addEventListener(
		"setUserInputEntity",
		({
			detail:
			{
				flowName,
				userInput,
				value
			}
		}) => {
			window.external.SetUserInputEntity(flowName, userInput, value);
		}
	);
	
	conversationalDesigner.addEventListener(
		"clickOnUserInputName",
		({
			detail:
			{
				flowName,
				userInput
			}
		}) => {
			window.external.SetUserInput(flowName, userInput);
		}
	);

	conversationalDesigner.addEventListener(
		"selectUserInput",
		({
			detail:
			{
				flowName,
				userInput
			}
		}) => {
			window.external.SelectUserInput(flowName, userInput);
		}
	);

	conversationalDesigner.addEventListener(
		"selectResponse",
		({
			detail:
			{
				flowName,
				index
			}
		}) => {
			window.external.SelectResponse(flowName, index);
		}
	);

	conversationalDesigner.addEventListener(
		"selectFlow",
		({
			detail:
			{
				flowName
			}
		}) => {
			window.external.SelectFlow(flowName);
		}
	);

	conversationalDesigner.addEventListener(
		"selectRoot",
		() => {
			window.external.SelectRoot();
		}
	);

	conversationalDesigner.addEventListener(
		"switchResponseParameter",
		({
			detail:
			{
				flowName,
				responseParameter
			}
		}) => {
			window.external.SwitchResponseParameter(flowName, responseParameter);
		}
	);

	conversationalDesigner.addEventListener(
		"changeCategoryName",
		({
			detail:
			{
				oldCategoryName,
				newCategoryName
			}
		}) => {
			window.external.ChangeCategoryName(oldCategoryName, newCategoryName);
		}
	);
});