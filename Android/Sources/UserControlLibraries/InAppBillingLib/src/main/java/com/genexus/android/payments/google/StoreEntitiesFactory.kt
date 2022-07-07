package com.genexus.android.payments.google

import com.android.billingclient.api.Purchase
import com.genexus.android.core.base.model.Entity
import com.genexus.android.core.base.model.EntityFactory
import com.genexus.android.core.base.model.EntityList
import com.genexus.android.core.base.services.Services
import com.genexus.android.core.base.utils.Strings
import com.genexus.android.payments.google.util.ProductDetail

object StoreEntitiesFactory {
    fun createStoreProductCollection(productDetails: List<ProductDetail>): EntityList {
        val collection = EntityList()
        for (productDetail in productDetails) {
            val item = createStoreProduct(productDetail)
            collection.add(item)
        }
        return collection
    }

    fun createSuccessfulPurchaseResult(
        productId: String,
        transactionData: String,
        purchaseId: String
    ): Entity {
        return createPurchaseResult(true, productId, transactionData, purchaseId)
    }

    fun createFailedPurchaseResult(message: String?): Entity {
        Services.Log.debug(
            StoreManager.OBJECT_NAME,
            String.format("Purchase failed. Error: %s", message)
        )
        return createPurchaseResult(false, Strings.EMPTY, Strings.EMPTY, Strings.EMPTY)
    }

    fun createEmptyPurchaseResult(): Entity {
        return createPurchaseResult(false, Strings.EMPTY, Strings.EMPTY, Strings.EMPTY)
    }

    fun createPurchasesInformation(result: List<Purchase>): Entity {
        val purchasesInfo = EntityFactory.newSdt("GeneXus.SD.Store.PurchasesInformation")
        purchasesInfo.setProperty("Purchases", createPurchaseResultCollection(result))
        purchasesInfo.setProperty("PurchasePlatform", Platform.GooglePlay.value)
        return purchasesInfo
    }

    private fun createStoreProduct(productDetails: ProductDetail): Entity {
        val item = EntityFactory.newSdt("GeneXus.SD.Store.StoreProduct")
        item.setProperty("Identifier", productDetails.id)
        item.setProperty("LocalizedTitle", productDetails.title)
        item.setProperty("LocalizedDescription", productDetails.desc)
        item.setProperty("LocalizedPriceAsString", productDetails.price)
        item.setProperty("Purchased", productDetails.isPurchased.toString())
        return item
    }

    private fun createPurchaseResult(
        purchaseSuccess: Boolean,
        productId: String,
        transactionData: String,
        purchaseId: String
    ): Entity {
        val purchaseResult = EntityFactory.newSdt("GeneXus.SD.Store.PurchaseResult")
        purchaseResult.setProperty("Success", purchaseSuccess)
        purchaseResult.setProperty("ProductIdentifier", productId)
        purchaseResult.setProperty("TransactionData", transactionData)
        purchaseResult.setProperty("PurchaseId", purchaseId)
        purchaseResult.setProperty("PurchasePlatform", Platform.GooglePlay.value)
        return purchaseResult
    }

    private fun createPurchaseResultCollection(purchases: List<Purchase>): EntityList {
        val collection = EntityList()
        for (purchase in purchases) {
            val purchaseState = State.fromInt(purchase.purchaseState)
            val item = createPurchaseResult(
                purchaseState == State.Purchased, purchase.skus[0],
                purchase.originalJson, purchase.purchaseToken
            )
            collection.add(item)
        }
        return collection
    }

    private enum class State(val value: Int) {
        Purchased(0),
        Canceled(1),
        Refunded(2);

        companion object {
            fun fromInt(value: Int) = values().first { it.value == value }
        }
    }

    private enum class Platform(val value: Int) {
        GooglePlay(1),
        iTunes(2)
    }
}
