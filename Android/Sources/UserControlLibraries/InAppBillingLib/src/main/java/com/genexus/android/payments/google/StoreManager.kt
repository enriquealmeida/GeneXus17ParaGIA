package com.genexus.android.payments.google

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.Purchase
import com.genexus.android.StoreManagerApi
import com.genexus.android.core.actions.ActionExecution
import com.genexus.android.core.actions.ApiAction
import com.genexus.android.core.base.metadata.expressions.Expression
import com.genexus.android.core.base.model.Entity
import com.genexus.android.core.base.model.EntityFactory
import com.genexus.android.core.base.model.ValueCollection
import com.genexus.android.core.base.services.Services
import com.genexus.android.core.externalapi.ExternalApi.IMethodInvoker
import com.genexus.android.core.externalapi.ExternalApiResult
import com.genexus.android.payments.google.StoreEntitiesFactory.createEmptyPurchaseResult
import com.genexus.android.payments.google.StoreEntitiesFactory.createFailedPurchaseResult
import com.genexus.android.payments.google.StoreEntitiesFactory.createPurchasesInformation
import com.genexus.android.payments.google.StoreEntitiesFactory.createStoreProductCollection
import com.genexus.android.payments.google.StoreEntitiesFactory.createSuccessfulPurchaseResult
import com.genexus.android.payments.google.util.BillingUpdatedListener
import com.genexus.android.payments.google.util.ProductDetail
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import java.util.concurrent.atomic.AtomicBoolean

class StoreManager(action: ApiAction?) : StoreManagerApi(action), LifecycleObserver {
    private val operationInProgress = AtomicBoolean(false)
    private val disposeRequested = AtomicBoolean(false)
    private var purchaseResult: Entity? = null
    private val purchaseUpdatedListener: BillingUpdatedListener = object : BillingUpdatedListener {
        override fun onPurchaseUpdated(result: BillingResult, purchase: Purchase) {
            purchaseResult = if (service.isBillingOk(result))
                createSuccessfulPurchaseResult(purchase.skus[0], purchase.originalJson, purchase.purchaseToken)
            else
                createFailedPurchaseResult(result.debugMessage)

            onOperationEnded()
            action?.let {
                it.setOutputValue(Expression.Value.newSdt(purchaseResult))
                ActionExecution.continueCurrent(activity, true, it)
            }
        }
    }

    private val service = IabService(activity.applicationContext, purchaseUpdatedListener)
    private var lifecycle: Lifecycle? = null

    private val propertyCanMakePurchases = IMethodInvoker {
        val result = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context) == ConnectionResult.SUCCESS && service.isAvailable()
        ExternalApiResult.success(result)
    }

    private val methodGetProducts = IMethodInvoker { parameters: List<Any?> ->
        val skus = parameters[0] as ValueCollection
        var productDetails: List<ProductDetail>
        try {
            onOperationStarted()
            val inventory = service.getSkuDetails(skus)
            productDetails = ArrayList()
            for (details in inventory) {
                val productDetail = ProductDetail(
                    details.sku, details.title,
                    details.description, details.price, service.hasPurchase(details.sku)
                )

                productDetails.add(productDetail)
            }
        } catch (e: IllegalStateException) {
            productDetails = emptyList()
        } finally {
            onOperationEnded()
        }
        ExternalApiResult.success(createStoreProductCollection(productDetails))
    }

    private val methodPurchaseProduct = IMethodInvoker { parameters ->
        val sku = parameters[0] as String
        if (parameters.size > 1) {
            val productQty = (parameters[1] as String).toInt()

            // Google's In-app Billing API only supports buying one product at a time.
            if (productQty != 1)
                return@IMethodInvoker ExternalApiResult.success(createEmptyPurchaseResult())
        }

        /* In case we have the sku registered as purchased in our inventory, the onPurchaseFinishedListener
			is called directly by launchPurchaseFlow. So after it finishes we already have our PurchaseResult
			ready and want to return the result immediately.
			Otherwise, onPurchaseFinishedListener is called by handleActivityResult which should be called on
			ActivityResult, so we want to defer returning our PurchaseResult to afterActivityResult.
		 */
        try {
            onOperationStarted()
            if (!service.launchPurchaseFlow(activity, sku))
                throw IllegalStateException("BillingClient is null")

            return@IMethodInvoker ExternalApiResult.SUCCESS_WAIT
        } catch (e: IllegalStateException) {
            Services.Log.debug(OBJECT_NAME, String.format("PurchaseProduct failed. Reason: %s", e.message))
            return@IMethodInvoker ExternalApiResult.success(createEmptyPurchaseResult())
        } finally {
            onOperationEnded()
        }
    }

    private val methodGetPurchases = IMethodInvoker {
        var purchases: List<Purchase>
        try {
            onOperationStarted()
            purchases = service.getPurchases()
        } catch (e: IllegalStateException) {
            purchases = emptyList()
            Services.Log.debug(OBJECT_NAME, String.format("GetPurchases failed. Reason: %s", e.message))
        } finally {
            onOperationEnded()
        }
        ExternalApiResult.success(createPurchasesInformation(purchases))
    }

    private val methodConsumeProduct = IMethodInvoker { parameters: List<Any?> ->
        val sku = parameters[0] as String
        val success: Boolean = try {
            onOperationStarted()
            if (!service.hasPurchase(sku))
                false
            else {
                val purchases = service.getPurchases(sku)
                if (purchases.isEmpty())
                    false
                else
                    service.consumeOrAcknowledgePurchase(purchases[0])
            }
        } catch (e: IllegalStateException) {
            Services.Log.debug(OBJECT_NAME, String.format("Consume failed. Reason: %s", e.message))
            false
        } finally {
            onOperationEnded()
        }
        ExternalApiResult.success(success)
    }

    private val methodRestorePurchases = IMethodInvoker {
        ExternalApiResult.success(EntityFactory.newEntity())
    }

    private fun onOperationStarted() {
        operationInProgress.compareAndSet(false, true)
    }

    private fun onOperationEnded() {
        operationInProgress.compareAndSet(true, false)
        if (disposeRequested.get()) {
            service.dispose()
            disposeRequested.compareAndSet(true, false)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private fun onActivityDestroyed() {
        if (operationInProgress.get())
            disposeRequested.compareAndSet(false, true)
        else
            service.dispose()

        lifecycle?.let { Services.Device.runOnUiThread { it.removeObserver(this) } }
    }

    companion object {
        const val OBJECT_NAME = "GeneXus.SD.Store.StoreManager"
        private const val READ_PROPERTY_CAN_MAKE_PURCHASES = "CanMakePurchases"
        private const val METHOD_GET_PRODUCTS = "GetProducts"
        private const val METHOD_PURCHASE_PRODUCT = "PurchaseProduct"
        private const val METHOD_CONSUME_PRODUCT = "ConsumeProduct"
        private const val METHOD_GET_PURCHASES = "GetPurchases"
        private const val METHOD_RESTORE_PURCHASES = "RestorePurchases"
    }

    init {
        addMethodHandler(READ_PROPERTY_CAN_MAKE_PURCHASES, 0, propertyCanMakePurchases)
        addMethodHandler(METHOD_GET_PRODUCTS, 1, methodGetProducts)
        addMethodHandler(METHOD_PURCHASE_PRODUCT, 2, methodPurchaseProduct)
        addMethodHandler(METHOD_PURCHASE_PRODUCT, 1, methodPurchaseProduct)
        addMethodHandler(METHOD_GET_PURCHASES, 0, methodGetPurchases)
        addMethodHandler(METHOD_CONSUME_PRODUCT, 1, methodConsumeProduct)
        addMethodHandler(METHOD_RESTORE_PURCHASES, 0, methodRestorePurchases)

        (activity as? LifecycleOwner)?.lifecycle?.let {
            Services.Device.runOnUiThread { it.addObserver(this) }
            lifecycle = it
        }
    }
}
