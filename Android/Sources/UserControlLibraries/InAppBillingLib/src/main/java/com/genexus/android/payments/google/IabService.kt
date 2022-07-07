package com.genexus.android.payments.google

import android.app.Activity
import android.content.Context
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ConsumeParams
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.SkuDetails
import com.android.billingclient.api.SkuDetailsParams
import com.genexus.android.core.base.metadata.expressions.Expression
import com.genexus.android.core.base.model.ValueCollection
import com.genexus.android.core.base.services.Services
import com.genexus.android.payments.google.util.BillingUpdatedListener
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class IabService(context: Context, private val apiListener: BillingUpdatedListener) : PurchasesUpdatedListener {
    private var billingClient: BillingClient? = null

    fun isAvailable(): Boolean {
        return billingClient != null
    }

    fun launchPurchaseFlow(activity: Activity, sku: String): Boolean {
        if (billingClient == null)
            return false

        val values = ValueCollection(Expression.Type.STRING).apply { add(sku) }
        val skuDetails = getSkuDetails(values)
        if (skuDetails.size != 1) {
            Services.Log.error(TAG, "Zero or more than one product found for SKU '$sku'")
            return false
        }

        val billingFlowParams = BillingFlowParams.newBuilder()
            .setSkuDetails(skuDetails[0])
            .build()
        billingClient?.launchBillingFlow(activity, billingFlowParams)
        return true
    }

    fun getSkuDetails(skus: ValueCollection): List<SkuDetails> {
        val skuDetails = mutableListOf<SkuDetails>()
        if (billingClient == null)
            return skuDetails

        skuDetails.addAll(getInAppSkuDetails(skus))
        skuDetails.addAll(getSubscriptionSkuDetails(skus))
        return skuDetails
    }

    private fun getInAppSkuDetails(skus: ValueCollection): List<SkuDetails> {
        return getSkuDetails(skus, BillingClient.SkuType.INAPP)
    }

    private fun getSubscriptionSkuDetails(skus: ValueCollection): List<SkuDetails> {
        return getSkuDetails(skus, BillingClient.SkuType.SUBS)
    }

    private fun getSkuDetails(skus: ValueCollection, skuType: String): List<SkuDetails> {
        val skuDetailsParams = SkuDetailsParams.newBuilder().setType(skuType).setSkusList(skus).build()
        return runBlocking {
            suspendCoroutine {
                billingClient?.querySkuDetailsAsync(skuDetailsParams) { billingResult: BillingResult, list: List<SkuDetails>? ->
                    if (isBillingOk(billingResult) && list != null)
                        it.resume(list)
                    else
                        it.resume(emptyList())
                }
            }
        }
    }

    fun getPurchases(sku: String): List<Purchase> {
        val purchases = mutableListOf<Purchase>()
        if (billingClient == null)
            return purchases

        for (p in getPurchases())
            if (p.skus.contains(sku))
                purchases.add(p)

        return purchases
    }

    fun getPurchases(): List<Purchase> {
        val purchases = mutableListOf<Purchase>()
        if (billingClient == null)
            return purchases

        purchases.addAll(getInAppPurchases())
        purchases.addAll(getSubscriptionPurchases())
        return purchases
    }

    private fun getInAppPurchases(): List<Purchase> {
        return getPurchasesByType(BillingClient.SkuType.INAPP)
    }

    private fun getSubscriptionPurchases(): List<Purchase> {
        return getPurchasesByType(BillingClient.SkuType.SUBS)
    }

    private fun getPurchasesByType(skuType: String): List<Purchase> {
        return runBlocking {
            suspendCoroutine {
                billingClient?.queryPurchasesAsync(skuType) { billingResult: BillingResult, list: List<Purchase>? ->
                    if (isBillingOk(billingResult) && list != null)
                        it.resume(list)
                    else
                        it.resume(emptyList())
                }
            }
        }
    }

    fun consumeOrAcknowledgePurchase(purchase: Purchase): Boolean {
        if (billingClient == null)
            return false

        when (purchase.purchaseState) {
            Purchase.PurchaseState.PURCHASED -> {}
            else -> {
                Services.Log.debug(TAG, "Purchase ${purchase.orderId} is not ready to be consumed")
                return false
            }
        }

        val productSku = purchase.skus[0]
        val skus = ValueCollection(Expression.Type.STRING).apply { add(productSku) }
        val skuDetails = getSkuDetails(skus)
        if (skuDetails.size != 1) {
            Services.Log.error(TAG, "Zero or more than one product found for SKU '$productSku'")
            return false
        }

        return if (skuDetails[0].type == BillingClient.SkuType.INAPP)
            consumePurchase(purchase)
        else
            acknowledgePurchase(purchase)
    }

    private fun consumePurchase(purchase: Purchase): Boolean {
        val consumeParams = ConsumeParams.newBuilder().setPurchaseToken(purchase.purchaseToken).build()
        return runBlocking {
            suspendCoroutine {
                billingClient?.consumeAsync(consumeParams) { billingResult: BillingResult, s: String? ->
                    Services.Log.debug(TAG, "Purchase consumed: $s")
                    it.resume(isBillingOk(billingResult))
                }
            }
        }
    }

    private fun acknowledgePurchase(purchase: Purchase): Boolean {
        if (purchase.isAcknowledged)
            return false

        val acknowledgeParams = AcknowledgePurchaseParams.newBuilder().setPurchaseToken(purchase.purchaseToken).build()
        return runBlocking {
            suspendCoroutine {
                billingClient?.acknowledgePurchase(acknowledgeParams) { billingResult ->
                    it.resume(isBillingOk(billingResult))
                }
            }
        }
    }

    fun hasPurchase(sku: String): Boolean {
        return getPurchases(sku).isNotEmpty()
    }

    private fun setupSync() {
        runBlocking {
            suspendCoroutine<Unit> {
                billingClient?.startConnection(object : BillingClientStateListener {
                    override fun onBillingSetupFinished(billingResult: BillingResult) {
                        if (!isBillingOk(billingResult))
                            billingClient = null

                        it.resume(Unit)
                    }

                    override fun onBillingServiceDisconnected() {
                        val message = Services.Strings.getResource(
                            R.string.common_google_play_services_enable_text,
                            Services.Strings.getResource(R.string.app_name)
                        )
                        Services.Messages.showMessage(message)
                        billingClient = null
                        it.resume(Unit)
                    }
                })
            }
        }

        checkNotNull(billingClient) { "IAB helper setup failed." }
    }

    fun dispose() {
        synchronized(this) {
            billingClient?.endConnection()
            billingClient = null
            Services.Log.debug(TAG, "IabHelper disposed.")
        }
    }

    override fun onPurchasesUpdated(billingResult: BillingResult, list: List<Purchase>?) {
        if (list != null) {
            for (p in list)
                apiListener.onPurchaseUpdated(billingResult, p)
        }
    }

    fun isBillingOk(result: BillingResult): Boolean {
        return result.responseCode == BillingClient.BillingResponseCode.OK
    }

    companion object {
        private const val TAG = "IabService"
    }

    init {
        try {
            billingClient = BillingClient.newBuilder(context).enablePendingPurchases().setListener(this).build()
            setupSync()
        } catch (ex: IllegalStateException) {
            Services.Log.error(TAG, "Cannot build BillingClient for InAppBilling")
        }
    }
}
