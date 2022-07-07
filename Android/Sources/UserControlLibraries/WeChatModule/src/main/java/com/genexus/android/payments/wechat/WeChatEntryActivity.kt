package com.genexus.android.payments.wechat

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import com.genexus.android.core.base.model.EntityFactory
import com.genexus.android.core.base.services.Services
import com.tencent.mm.opensdk.constants.ConstantsAPI
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.modelmsg.SendAuth
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler

open class WeChatEntryActivity : Activity(), IWXAPIEventHandler {
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WeChatObject.api?.handleIntent(intent, this)
    }

    override fun onReq(req: BaseReq) {
        Services.Log.info(WeChatModule.TAG, "Request openid = " + req.openId)
    }

    override fun onResp(resp: BaseResp) {
        if (resp.type == ConstantsAPI.COMMAND_SENDAUTH) {
            loginProvider?.response(resp as SendAuth.Resp, this)
        } else {
            if (resp.type == ConstantsAPI.COMMAND_PAY_BY_WX) {
                WeChatPayApi.resp = resp
                Services.Log.info(WeChatModule.TAG, "Payment Result: " + resp.errCode)
            }
            onBackPressed()
        }
    }

    public override fun onDestroy() {
        super.onDestroy()

        if (WeChatPayApi.resp?.type == ConstantsAPI.COMMAND_PAY_BY_WX) {
            val paymentResult = EntityFactory.newSdt("CommonPay.PaymentResult")
            paymentResult.setProperty("ErrorCode", WeChatPayApi.resp?.errCode)
            paymentResult.setProperty("ErrorDescription", WeChatPayApi.resp?.errStr)
            paymentResult.setProperty("OrderNumber", WeChatPayApi.orderNumber)
            WeChatPayApi.fireEvent("OnPaymentFinished", paymentResult)
        }
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        var loginProvider: WeChatLoginProvider? = null
    }
}
