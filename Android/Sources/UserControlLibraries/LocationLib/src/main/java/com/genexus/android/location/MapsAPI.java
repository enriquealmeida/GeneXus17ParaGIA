package com.genexus.android.location;

import com.genexus.android.core.actions.ActionExecution;
import com.genexus.android.core.actions.ApiAction;
import com.genexus.android.LocationAccuracy;
import com.genexus.android.WithBackgroundPermission;
import com.genexus.android.core.base.metadata.expressions.Expression;
import com.genexus.android.core.base.model.Entity;
import com.genexus.android.core.base.model.EntityFactory;
import com.genexus.android.core.base.model.ValueCollection;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.controls.maps.LocationApi;
import com.genexus.android.core.externalapi.ExternalApiResult;
import com.genexus.android.location.geolocation.Constants;

import java.util.ArrayList;
import java.util.List;

public class MapsAPI extends LocationApiBase {

	public static final String OBJECT_NAME = "GeneXus.Common.Maps";
	private static final String METHOD_CALCULATE_DIRECTIONS = "CalculateDirections";
	private static final String METHOD_REQUEST_TEMPORARY_FULL = "RequestTemporaryFullAccuracy";
	private static final String PROPERTY_AUTHORIZED_ACCURACY = "AuthorizedAccuracy";

	private ApiAction mCurrentAction = null;

	public MapsAPI(ApiAction action) {
		super(LocationApi.MAPS, action);
		addMethodHandler(METHOD_CALCULATE_DIRECTIONS, 1, mMethodCalculateDirectionHandler);
		addMethodHandler(METHOD_CALCULATE_DIRECTIONS, 2, mMethodCalculateDirectionHandler);
		addMethodHandler(METHOD_CALCULATE_DIRECTIONS, 4, mMethodCalculateDirectionHandler);
		addMethodHandler(METHOD_REQUEST_TEMPORARY_FULL, 1, mMethodRequestTemporaryFullAccuracyHandler);
		addReadonlyPropertyHandler(PROPERTY_AUTHORIZED_ACCURACY, mPropertyAuthorizedAccuracyHandler);
	}

	private final IMethodInvoker mMethodCalculateDirectionHandler = parameters -> {
		String origin = Strings.EMPTY;
		String destination = Strings.EMPTY;
		String transportType = Strings.EMPTY;
		boolean requestAlternatives = false;
		List<String> waypoints = new ArrayList<>();

		if (parameters.size() == 1) {
			Entity directionsRequestParameters = (Entity) parameters.get(0);
			origin = directionsRequestParameters.optStringProperty(Constants.SDT_DIRECTIONS_REQUEST_PARAMETERS_SOURCE);
			destination = directionsRequestParameters.optStringProperty(Constants.SDT_DIRECTIONS_REQUEST_PARAMETERS_DESTINATION);
			transportType = directionsRequestParameters.optStringProperty(Constants.SDT_DIRECTIONS_REQUEST_PARAMETERS_TRANSPORT_TYPE);
			requestAlternatives = directionsRequestParameters.optBooleanProperty(Constants.SDT_DIRECTIONS_REQUEST_PARAMETERS_REQ_ALTERNATES, false);
			ValueCollection valueCollection = directionsRequestParameters.getProperty(ValueCollection.class, Constants.SDT_DIRECTIONS_REQUEST_PARAMETERS_WAYPOINTS);
			if (valueCollection != null)
				waypoints.addAll(valueCollection);

		} else {
			if (parameters.size() >= 2) {
				origin = (String) parameters.get(0);
				destination = (String) parameters.get(1);
			}

			if (parameters.size() == 4) {
				transportType = (String) parameters.get(2);
				requestAlternatives = Boolean.parseBoolean((String) parameters.get(3));
			}
		}

		//Method is already expecting Geopoint data type instead of old Geolocation
		Entity routesEntity = Services.Maps.calculateDirections(getActivity(), origin, destination, waypoints, transportType, requestAlternatives);
		if (routesEntity == null) routesEntity = EntityFactory.newEntity();
		return ExternalApiResult.success(routesEntity);
	};

	private final IMethodInvoker mMethodRequestTemporaryFullAccuracyHandler = parameters -> {
		String rationale = parameters.get(0).toString();
		mCurrentAction = getAction();
		WithBackgroundPermission<Void> request = new WithBackgroundPermission<>(getActivity(), null, Services.Location.getRequestPermissions());
		request.setAttachToController(true);
		request.setRationale(rationale);
		request.setSuccessCode(() -> continueBackgroundLocationAction(true));
		request.setFailureCode(() -> continueBackgroundLocationAction(false));
		request.run();
		return ExternalApiResult.SUCCESS_WAIT;
	};

	private final IMethodInvoker mPropertyAuthorizedAccuracyHandler = parameters ->
		ExternalApiResult.success(LocationAccuracy.getStatus(getContext()));

	private Void continueBackgroundLocationAction(boolean success) {
		if (mCurrentAction == null)
			return null;

		mCurrentAction.setOutputValue(Expression.Value.newBoolean(success));
		ActionExecution.continueCurrent(getActivity(), true, mCurrentAction);
		mCurrentAction = null;
		return null;
	}
}
