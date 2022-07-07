package com.genexus.android.payments.google.util

import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.Purchase

interface BillingUpdatedListener {
    fun onPurchaseUpdated(result: BillingResult, purchase: Purchase)
}
