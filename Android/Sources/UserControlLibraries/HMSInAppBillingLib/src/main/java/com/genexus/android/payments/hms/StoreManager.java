package com.genexus.android.payments.hms;

import android.app.Activity;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.genexus.android.core.actions.ApiAction;
import com.genexus.android.core.activities.ActivityHelper;
import com.genexus.android.StoreManagerApi;
import com.genexus.android.core.base.model.Entity;
import com.genexus.android.core.base.model.ValueCollection;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.externalapi.ExternalApiResult;
import com.genexus.android.payments.hms.common.CipherUtil;
import com.genexus.android.payments.hms.common.Constants;
import com.genexus.android.payments.hms.common.IapRequestHelper;
import com.huawei.hms.api.ConnectionResult;
import com.huawei.hms.api.HuaweiApiAvailability;
import com.huawei.hms.iap.Iap;
import com.huawei.hms.iap.IapClient;
import com.huawei.hms.iap.entity.ConsumeOwnedPurchaseResult;
import com.huawei.hms.iap.entity.InAppPurchaseData;
import com.huawei.hms.iap.entity.IsEnvReadyResult;
import com.huawei.hms.iap.entity.OrderStatusCode;
import com.huawei.hms.iap.entity.OwnedPurchasesResult;
import com.huawei.hms.iap.entity.ProductInfo;
import com.huawei.hms.iap.entity.ProductInfoResult;
import com.huawei.hms.iap.entity.PurchaseIntentResult;
import com.huawei.hms.iap.entity.PurchaseResultInfo;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StoreManager extends StoreManagerApi {
	public static final String OBJECT_NAME = "GeneXus.SD.Store.StoreManager";

	private static final String READ_PROPERTY_CAN_MAKE_PURCHASES = "CanMakePurchases";
	private static final String METHOD_GET_PRODUCTS = "GetProducts";
	private static final String METHOD_PURCHASE_PRODUCT = "purchaseProduct";
	private static final String METHOD_CONSUME_PRODUCT = "ConsumeProduct";
	private static final String METHOD_GET_PURCHASES = "GetPurchases";
	@SuppressWarnings("unused")
	private static final String METHOD_IS_PRODUCT_ENABLED = "IsEnabled";
	@SuppressWarnings("unused")
	private static final String METHOD_ENABLE_PRODUCT = "EnableProduct";
	@SuppressWarnings("unused")
	private static final String METHOD_DISABLE_PRODUCT = "DisableProduct";

	private IapClient mIapClient = null;
	private static final String SUBSCRIPTIONNAME = "subscription";
	private static final String SUSCRIPTIONNAME = "suscription";

	private Entity mPurchaseResult;

	public StoreManager(ApiAction action) {
		super(action);
		mIapClient = Iap.getIapClient(this.getActivity());
		addSimpleMethodHandler(READ_PROPERTY_CAN_MAKE_PURCHASES, 0, mPropertyCanMakePurchases);
		addSimpleMethodHandler(METHOD_GET_PRODUCTS, 1, mMethodGetProducts);
		addMethodHandler(METHOD_PURCHASE_PRODUCT, 2, mMethodPurchaseProduct);
		addMethodHandler(METHOD_PURCHASE_PRODUCT, 1, mMethodPurchaseProduct);
		addSimpleMethodHandler(METHOD_GET_PURCHASES, 0, mMethodGetPurchases);
		addSimpleMethodHandler(METHOD_CONSUME_PRODUCT, 1, mMethodConsumeProduct);
	}

	private final ISimpleMethodInvoker mPropertyCanMakePurchases = new ISimpleMethodInvoker() {
		@Override
		public Object invoke(List<Object> parameters) {
			int result = HuaweiApiAvailability.getInstance().isHuaweiMobileServicesAvailable(getContext());
			if (result == ConnectionResult.SUCCESS)
			{
				IsEnvReadyResult envReadyResult = IapRequestHelper.isEnvReadyNow(mIapClient);
				if (envReadyResult!=null)
				{
					return envReadyResult.getReturnCode()==ConnectionResult.SUCCESS;
				}
			}
			return false;
		}
	};

	private final ISimpleMethodInvoker mMethodGetProducts = new ISimpleMethodInvoker() {
		@Override
		public Object invoke(List<Object> parameters) {
			ValueCollection skus = (ValueCollection) parameters.get(0);
			List<ProductDetail> productDetails =null;

			// assume default type as CONSUMABLE
			int type = guessProductsType(skus);
			productDetails = new ArrayList<>();

			ProductInfoResult productInfoResult = IapRequestHelper.obtainProductInfoNow(mIapClient, skus, type );
			OwnedPurchasesResult ownedPurchasesResult = IapRequestHelper.obtainOwnedPurchasesNow(mIapClient, type, null);

			if (productInfoResult!=null && ownedPurchasesResult!=null) {
				List<String> inAppPurchaseDataList = ownedPurchasesResult.getInAppPurchaseDataList();
				List<ProductInfo> productInfos = productInfoResult.getProductInfoList();
				for (ProductInfo productInfo : productInfos) {
					// check if product is purchased
					boolean purchased = false;
					for (String data : inAppPurchaseDataList) {
						try {
							InAppPurchaseData inAppPurchaseData = new InAppPurchaseData(data);
							if (productInfo.getProductId().equals(inAppPurchaseData.getProductId())) {
								purchased = true;
							}
						} catch (JSONException e) {
							Services.Log.error("parse InAppPurchaseData JSONException", e);
						}
					}

					ProductDetail productDetail = new ProductDetail(
						productInfo.getProductId(),
						productInfo.getProductName(),
						productInfo.getProductDesc(),
						productInfo.getPrice(),
						purchased
					);
					productDetails.add(productDetail);
				}
			}
			else
			{
				Services.Log.error("MethodGetProducts, error getting products list");
			}

			return StoreEntitiesFactory.createStoreProductCollection(productDetails);
		}
	};


	private final IMethodInvoker mMethodPurchaseProduct = new IMethodInvokerWithActivityResult() {
		@Override
		public @NonNull ExternalApiResult invoke(List<Object> parameters) {
			String sku = (String) parameters.get(0);

			if (parameters.size() > 1) {
				int productQty = Integer.parseInt(((String) parameters.get(1)));

				// HMS's In-app Billing API only supports buying one product at a time.
				if (productQty != 1) {
					mPurchaseResult = StoreEntitiesFactory.createEmptyPurchaseResult();
					return ExternalApiResult.success(mPurchaseResult);
				}
			}

			mPurchaseResult = null;
			List<String> skus = Collections.singletonList(sku);

			// assume default type as CONSUMABLE
			int type = guessProductsType(skus);

			PurchaseIntentResult purchaseIntentResult = IapRequestHelper.createPurchaseIntentNow(mIapClient, sku, type );

			if (purchaseIntentResult!=null)
			{
				// you should pull up the page to complete the payment process
				ActivityHelper.registerActionRequestCode(Constants.REQ_CODE_BUY);
				IapRequestHelper.startResolutionForResult( getActivity(), purchaseIntentResult.getStatus(), Constants.REQ_CODE_BUY);
				return ExternalApiResult.SUCCESS_WAIT;
			}
			else
			{
				Services.Log.error("GetBuyIntentResult is null");
				mPurchaseResult = StoreEntitiesFactory.createEmptyPurchaseResult();
				return ExternalApiResult.success(mPurchaseResult);
			}


		}

		@Override
		public @NonNull ExternalApiResult handleActivityResult(int requestCode, int resultCode, @Nullable Intent result) {
			Services.Log.debug("PurchaseProduct, handleActivityResult");
			if (requestCode == Constants.REQ_CODE_BUY) {
				if (resultCode == Activity.RESULT_OK) {
					Services.Log.debug("PurchaseProduct, handleActivityResult RESULT_OK");
					PurchaseResultInfo purchaseIntentResult = mIapClient.parsePurchaseResultInfoFromIntent(result);
					switch(purchaseIntentResult.getReturnCode()) {
						case OrderStatusCode.ORDER_STATE_CANCEL:
							//Toast.makeText(this, R.string.ordercancel, Toast.LENGTH_SHORT).show();
							Services.Log.info("PurchaseProduct, ORDER_STATE_CANCEL purchase");
							break;
						case OrderStatusCode.ORDER_STATE_FAILED:
						case OrderStatusCode.ORDER_PRODUCT_OWNED:
							Services.Log.info("PurchaseProduct, ORDER_STATE_FAILED or  ORDER_PRODUCT_OWNED");
							break;
						case OrderStatusCode.ORDER_STATE_SUCCESS:
							Services.Log.info("PurchaseProduct, ORDER_STATE_SUCCESS");
							boolean credible = CipherUtil.doCheck(purchaseIntentResult.getInAppPurchaseData(), purchaseIntentResult.getInAppDataSignature(), CipherUtil.getPublicKey());
							Services.Log.info("PurchaseProduct, doCheck result " + credible);
							if (credible) {
								try {
									InAppPurchaseData inAppPurchaseData = new InAppPurchaseData(purchaseIntentResult.getInAppPurchaseData());

									// check product type to validate
									String sku = inAppPurchaseData.getProductId();
									List<String> skus = Collections.singletonList(sku);
									// assume default type as CONSUMABLE
									int type = guessProductsType(skus);

									// validate subscription
									if (type == IapClient.PriceType.IN_APP_SUBSCRIPTION)
									{
										if (inAppPurchaseData.isSubValid()) {
											Services.Log.info("PurchaseProduct, ORDER_STATE_SUCCESS and VALID SUBVALID");
											mPurchaseResult = StoreEntitiesFactory.createSuccessfulPurchaseResult(inAppPurchaseData.getProductId(), purchaseIntentResult.getInAppPurchaseData(), inAppPurchaseData.getOrderID());
											return ExternalApiResult.successNoRefresh(mPurchaseResult);
										}
										else
											Services.Log.error("PurchaseProduct, inAppPurchaseData isSubValid NOT CORRECT");
									}
									else // validate consumable
									{
										if (inAppPurchaseData.getPurchaseState() == InAppPurchaseData.PurchaseState.PURCHASED)
										{
											Services.Log.info("PurchaseProduct, ORDER_STATE_SUCCESS and VALID PURCHASED");
											mPurchaseResult = StoreEntitiesFactory.createSuccessfulPurchaseResult(inAppPurchaseData.getProductId(), purchaseIntentResult.getInAppPurchaseData(), inAppPurchaseData.getOrderID());
											return ExternalApiResult.successNoRefresh(mPurchaseResult);
										}
										else
											Services.Log.error("PurchaseProduct, inAppPurchaseData getPurchaseState NOT PURCHASED");
									}
								} catch (JSONException e) {
									Services.Log.error("PurchaseProduct, parse InAppPurchaseData JSONException", e);
								}
							} else {
								Services.Log.error("PurchaseProduct, check the data signature fail");
							}
							break;
						default:
							break;
					}
				} else {
					Services.Log.info("PurchaseProduct, cancel purchase");
				}
			}
			mPurchaseResult = StoreEntitiesFactory.createEmptyPurchaseResult();
			return ExternalApiResult.successNoRefresh(mPurchaseResult);
		}

	};

	private ISimpleMethodInvoker mMethodGetPurchases = new ISimpleMethodInvoker() {
		@Override
		public Object invoke(List<Object> parameters) {
			OwnedPurchasesResult ownedPurchasesResult = IapRequestHelper.obtainOwnedPurchasesNow(mIapClient, IapClient.PriceType.IN_APP_CONSUMABLE, null);

			Entity purchasesInfo = StoreEntitiesFactory.createEmptyPurchasesInformation();
			if (ownedPurchasesResult!=null) {
				purchasesInfo = StoreEntitiesFactory.createPurchasesInformation(ownedPurchasesResult);
			}

			OwnedPurchasesResult ownedPurchasesResult2 = IapRequestHelper.obtainOwnedPurchasesNow(mIapClient, IapClient.PriceType.IN_APP_SUBSCRIPTION, null);
			if (ownedPurchasesResult!=null) {
				if (purchasesInfo==null)
					purchasesInfo = StoreEntitiesFactory.createPurchasesInformation(ownedPurchasesResult2);
				else
					purchasesInfo = StoreEntitiesFactory.addPurchasesInformation(ownedPurchasesResult2, purchasesInfo);
			}
			return purchasesInfo;

		}
	};

	private ISimpleMethodInvoker mMethodConsumeProduct = new ISimpleMethodInvoker() {
		@Override
		public Object invoke(List<Object> parameters) {
			String sku = (String) parameters.get(0);
			boolean success = false;

			// only consumable can be consume
			OwnedPurchasesResult ownedPurchasesResult = IapRequestHelper.obtainOwnedPurchasesNow(mIapClient, IapClient.PriceType.IN_APP_CONSUMABLE, null);
			if (ownedPurchasesResult!=null) {
				List<String> inAppPurchaseDataList = ownedPurchasesResult.getInAppPurchaseDataList();

				for (String data : inAppPurchaseDataList) {
					try {
						InAppPurchaseData inAppPurchaseData = new InAppPurchaseData(data);
						if (inAppPurchaseData.getProductId().equalsIgnoreCase(sku)) {
							String purchaseToken = inAppPurchaseData.getPurchaseToken();
							ConsumeOwnedPurchaseResult result = IapRequestHelper.consumeOwnedPurchaseNow(mIapClient, purchaseToken);
							if (result != null)
								success = true;
						}
					} catch (JSONException e) {
						Services.Log.error("parse InAppPurchaseData JSONException", e);
					}
				}
			}
			return success;
		}
	};

	private int guessProductsType(List<String> skus) {
		int type = IapClient.PriceType.IN_APP_CONSUMABLE;
		for (String sku : skus) {
			// use SUBSCRIPTION, if product name contains it.
			if (sku.toLowerCase().contains(SUBSCRIPTIONNAME) || sku.toLowerCase().contains(SUSCRIPTIONNAME))
				type = IapClient.PriceType.IN_APP_SUBSCRIPTION;
		}
		return type;
	}


}
