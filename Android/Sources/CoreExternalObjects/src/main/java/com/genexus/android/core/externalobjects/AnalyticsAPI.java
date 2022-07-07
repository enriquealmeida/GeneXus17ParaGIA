package com.genexus.android.core.externalobjects;

import java.util.List;

import androidx.annotation.NonNull;

import com.genexus.android.core.actions.ApiAction;
import com.genexus.android.analytics.Analytics;
import com.genexus.android.core.base.model.Entity;
import com.genexus.android.core.base.model.EntityList;
import com.genexus.android.core.externalapi.ExternalApi;
import com.genexus.android.core.externalapi.ExternalApiResult;

public class AnalyticsAPI extends ExternalApi {
	public static final String OBJECT_NAME = "GeneXus.Common.Analytics";

	public AnalyticsAPI(ApiAction action) {
		super(action);
	}

	@Override
	public @NonNull ExternalApiResult execute(String method, List<Object> parameters) {
		List<String> parameterValues = toString(parameters);

		if (method.equalsIgnoreCase("setUserId") && parameterValues.size() == 1) {
			Analytics.setUserId(parameterValues.get(0));
		} else if (method.equalsIgnoreCase("trackView") && parameterValues.size() == 1) {
			Analytics.trackView(getActivity(), parameterValues.get(0));
		} else if (method.equalsIgnoreCase("trackPurchase") && parameters.size() == 1) {
			Entity sdt = (Entity) parameters.get(0);
			Analytics.trackPurchase((String) sdt.getProperty("TransactionId"),
					(String) sdt.getProperty("Affiliation"),
					Double.parseDouble((String) sdt.getProperty("Revenue")),
					Double.parseDouble((String) sdt.getProperty("Tax")),
					Double.parseDouble((String) sdt.getProperty("Shipping")),
					(String) sdt.getProperty("CurrencyCode"));

			EntityList items = sdt.getLevel("Items");
			if (items != null) {
				for (Entity item : items) {
					Analytics.trackPurchaseItem((String) item.getProperty("TransactionId"),
							(String) item.getProperty("Id"),
							(String) item.getProperty("Name"),
							(String) item.getProperty("Category"),
							Double.parseDouble((String) item.getProperty("Price")),
							(long) Double.parseDouble((String) item.getProperty("Quantity")),
							(String) item.getProperty("CurrencyCode"));
				}
			}
		} else if (method.equalsIgnoreCase("trackEvent") && parameterValues.size() == 4) {
			Analytics.trackEvent(parameterValues.get(0),
					parameterValues.get(1),
					parameterValues.get(2),
					Long.parseLong(parameterValues.get(3)));
		} else if (method.equalsIgnoreCase("setCommerceTrackerId") && parameterValues.size() == 1) {
			Analytics.setCommerceTrackerId(parameterValues.get(0));
		} else {
			return ExternalApiResult.failureUnknownMethod(this, method);
		}
		return ExternalApiResult.SUCCESS_CONTINUE;
	}
}
