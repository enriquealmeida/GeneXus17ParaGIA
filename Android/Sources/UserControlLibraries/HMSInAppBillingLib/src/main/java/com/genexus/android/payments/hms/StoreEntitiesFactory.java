package com.genexus.android.payments.hms;

import java.util.List;

import com.genexus.android.core.base.model.Entity;
import com.genexus.android.core.base.model.EntityFactory;
import com.genexus.android.core.base.model.EntityList;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;
import com.huawei.hms.iap.entity.InAppPurchaseData;
import com.huawei.hms.iap.entity.OwnedPurchasesResult;

import org.json.JSONException;

public class StoreEntitiesFactory {

	private static int purchasePlatformHuaweiHms = 3; // 1: Google Play , 2: iTunes, 3: Huawei AppGallery

	public static EntityList createStoreProductCollection(List<ProductDetail> productDetails) {
		EntityList collection = new EntityList();
		for (ProductDetail productDetail : productDetails) {
			Entity item = createStoreProduct(productDetail);
			collection.add(item);
		}
		return collection;
	}

	private static Entity createStoreProduct(ProductDetail productDetails) {
		Entity item = EntityFactory.newSdt("GeneXus.SD.Store.StoreProduct");
		item.setProperty("Identifier", productDetails.getId());
		item.setProperty("LocalizedTitle", productDetails.getTitle());
		item.setProperty("LocalizedDescription", productDetails.getDesc());
		item.setProperty("LocalizedPriceAsString", productDetails.getPrice());
		item.setProperty("Purchased", String.valueOf(productDetails.isPurchased()));
		return item;
	}

	public static Entity createSuccessfulPurchaseResult(String productId, String transactionData, String purchaseId) {
		return createPurchaseResult(true, productId, transactionData, purchaseId);
	}

	public static Entity createFailedPurchaseResult(String message) {
		Services.Log.debug(StoreManager.OBJECT_NAME, String.format("Purchase failed. Error: %s", message));
		return createPurchaseResult(false, Strings.EMPTY, Strings.EMPTY, Strings.EMPTY);
	}

	private static Entity createPurchaseResult(boolean purchaseSuccess, String productId, String transactionData, String purchaseId) {
		Entity purchaseResult = EntityFactory.newSdt("GeneXus.SD.Store.PurchaseResult");
		purchaseResult.setProperty("Success", purchaseSuccess);
		purchaseResult.setProperty("ProductIdentifier", productId);
		purchaseResult.setProperty("TransactionData", transactionData);
		purchaseResult.setProperty("PurchaseId", purchaseId);
		purchaseResult.setProperty("PurchasePlatform", purchasePlatformHuaweiHms);
		return purchaseResult;
	}

	public static Entity createEmptyPurchaseResult() {
		return createPurchaseResult(false, Strings.EMPTY, Strings.EMPTY, Strings.EMPTY);
	}

	private static EntityList createPurchaseResultCollection(OwnedPurchasesResult purchases) {
		EntityList collection = new EntityList();
		if (purchases.getInAppPurchaseDataList() != null) {
			List<String> inAppPurchaseDataList = purchases.getInAppPurchaseDataList();
			List<String> inAppSignature= purchases.getInAppSignature();
			for (int i = 0; i < inAppPurchaseDataList.size(); i++) {
				final String inAppPurchaseData = inAppPurchaseDataList.get(i);
				final String inAppPurchaseDataSignature = inAppSignature.get(i);
				try {
					InAppPurchaseData inAppPurchaseDataBean = new InAppPurchaseData(inAppPurchaseData);
					if (inAppPurchaseDataBean.getPurchaseState() != InAppPurchaseData.PurchaseState.PURCHASED) {
						break;
					}
					String purchaseToken = inAppPurchaseDataBean.getPurchaseToken();
					String productId = inAppPurchaseDataBean.getProductId();
					String purchaseData = inAppPurchaseData;

					int purchaseState = inAppPurchaseDataBean.getPurchaseState(); // 0 (purchased), 1 (canceled), or 2 (refunded).
					Entity item = createPurchaseResult(purchaseState == 0, productId, purchaseData, purchaseToken);
					collection.add(item);

				}
				catch (JSONException e) {
					Services.Log.error("createPurchase:" + e.getMessage());
				}
			}
		}

		return collection;
	}

	public static Entity createPurchasesInformation(OwnedPurchasesResult result) {
		Entity purchasesInfo = EntityFactory.newSdt("GeneXus.SD.Store.PurchasesInformation");
		purchasesInfo.setProperty("Purchases", createPurchaseResultCollection(result));
		purchasesInfo.setProperty("PurchasePlatform", purchasePlatformHuaweiHms);
		return purchasesInfo;
	}

	public static Entity addPurchasesInformation(OwnedPurchasesResult result, Entity purchasesInfo) {
		EntityList collection = createPurchaseResultCollection(result);
		EntityList collection2 = (EntityList)purchasesInfo.getProperty("Purchases");
		for (Entity entity : collection)
		{
			collection2.add(entity);
		}
		purchasesInfo.setProperty("Purchases", collection2);
		return purchasesInfo;
	}

	public static Entity createEmptyPurchasesInformation() {
		Entity purchasesInfo = EntityFactory.newSdt("GeneXus.SD.Store.PurchasesInformation");
		purchasesInfo.setProperty("Purchases", new EntityList());
		purchasesInfo.setProperty("PurchasePlatform", purchasePlatformHuaweiHms);
		return purchasesInfo;
	}

}
