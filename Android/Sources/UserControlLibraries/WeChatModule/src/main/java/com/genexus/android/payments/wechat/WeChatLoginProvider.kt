package com.genexus.android.payments.wechat

import android.app.Activity
import com.genexus.android.core.actions.Action
import com.genexus.android.core.actions.ActionExecution
import com.genexus.android.core.actions.UIContext
import com.genexus.android.core.base.services.ILoginProvider
import com.genexus.android.core.base.services.Services
import com.tencent.mm.opensdk.modelmsg.SendAuth
import kotlin.concurrent.thread

private const val USER_INFO_SCOPE = "snsapi_userinfo"

class WeChatLoginProvider : ILoginProvider {
    override val name = "wechat"

    private var activity: Activity? = null
    private var action: Action? = null
    private var token: String? = null
    private var state: String? = null

    override fun login(context: UIContext, activity: Activity, action: Action) {
        this.activity = activity
        this.action = action
        this.token = null
        WeChatObject.Api(context)?.let {
            state = randomString()
            val req = SendAuth.Req()
            req.scope = USER_INFO_SCOPE
            req.state = state
            if (it.sendReq(req))
                Services.Log.info(WeChatModule.TAG, "SendReq OK")
            else
                Services.Log.error(WeChatModule.TAG, "SendReq Failed")
        }
    }

    override fun loginToken(): String? {
        return token
    }

    fun response(resp: SendAuth.Resp, entryActivity: Activity) {
        thread {
            if (resp.state != state) {
                Services.Log.error(WeChatModule.TAG, "Incorrect state after response")
                ActionExecution.cancelCurrent(null)
            } else {
                token = resp.code
                ActionExecution.continueCurrent(activity, false, action)
            }
            activity = null
            entryActivity.finish()
        }
    }

    private fun randomString(): String {
        val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        val randomString = (1..20) // 20 characters
            .map { kotlin.random.Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("")
        return randomString
    }
}
