package com.genexus.android.payments.wechat

import com.genexus.android.core.actions.ApiAction
import com.genexus.android.core.actions.ExternalObjectEvent
import com.genexus.android.core.base.model.Entity
import com.genexus.android.core.base.services.Services
import com.genexus.android.core.externalapi.ExternalApi
import com.genexus.android.core.externalapi.ExternalApi.IMethodInvoker
import com.genexus.android.core.externalapi.ExternalApiResult
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.modelpay.PayReq
import java.util.Arrays

class WeChatPayApi(action: ApiAction?) : ExternalApi(action) {
    private val payMethod = IMethodInvoker { parameters: List<Any> ->
        val paymentData = parameters[0] as Entity
        WeChatObject.Api(context)?.let {
            orderNumber = paymentData.getProperty("OrderNumber") as String
            val req = PayReq()
            req.appId = WeChatObject.appId
            req.partnerId = paymentData.getProperty("MerchantId") as String
            req.prepayId = paymentData.getProperty("PrePayId") as String
            req.nonceStr = paymentData.getProperty("RandomString") as String
            req.timeStamp = paymentData.getProperty("Timestamp") as String
            req.packageValue = paymentData.getProperty("Package") as String
            req.sign = paymentData.getProperty("Signature") as String
            // req.extData = "app data"; // optional
            if (it.sendReq(req)) {
                Services.Log.info("OKSendReq", "OKSendReq")
            } else {
                Services.Log.info("NoOKSendReq", "NoOKSendReq")
            }
        }
        ExternalApiResult.SUCCESS_CONTINUE
    }

    init {
        addMethodHandler(METHOD_PAY, 1, payMethod)
    }

    companion object {
        const val NAME = "WeChatPay.WeChatPayProvider"
        private const val METHOD_PAY = "Pay"
        var resp: BaseResp? = null
        var orderNumber: String? = null

        fun fireEvent(eventName: String?, vararg eventArgs: Any?) {
            val event = ExternalObjectEvent(NAME, eventName)
            event.fire(Arrays.asList<Any>(*eventArgs))
        }
    }
}
