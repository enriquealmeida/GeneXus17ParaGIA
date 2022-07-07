package com.genexus.android.core.externalobjects

import com.genexus.android.analytics.Analytics
import com.genexus.android.core.activities.ActivityHelper
import com.genexus.internet.IGxJSONSerializable
import json.org.json.JSONObject
import java.math.BigDecimal

class AnalyticsAPIOffline {
    companion object {
        @JvmStatic
        fun trackView(viewName: String) {
            Analytics.trackView(ActivityHelper.getCurrentActivity(), viewName)
        }

        @JvmStatic
        fun trackEvent(category: String, action: String, label: String, value: BigDecimal) {
            Analytics.trackEvent(category, action, label, value.toLong())
        }

        @JvmStatic
        fun trackPurchase(purchaseInfo: Any) {
            val json = (purchaseInfo as IGxJSONSerializable).GetJSONObject() as JSONObject
            val transactionId = json.getString("TransactionId")
            val affiliation = json.getString("Affiliation")
            val revenue = json.getDouble("Revenue")
            val tax = json.getDouble("Tax")
            val shipping = json.getDouble("Shipping")
            val currencyCode = json.getString("CurrencyCode")
            Analytics.trackPurchase(transactionId, affiliation, revenue, tax, shipping, currencyCode)

            val items = json.getJSONArray("Items")
            for (i in 0 until items.length()) {
                val item = items[i] as JSONObject
                val itemId = item.getString("Id")
                val name = item.getString("Name")
                val category = item.getString("Category")
                val price = item.getDouble("Price")
                val quantity = item.getLong("Quantity")
                val itemCurrencyCode = item.getString("CurrencyCode")
                Analytics.trackPurchaseItem(transactionId, itemId, name, category, price, quantity, itemCurrencyCode)
            }
        }

        @JvmStatic
        fun setUserId(userId: String) {
            Analytics.setUserId(userId)
        }
    }
}
