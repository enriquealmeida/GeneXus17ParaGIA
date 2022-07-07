package com.genexus.android.payments.alipay;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;

import com.alipay.sdk.app.EnvUtils;
import com.alipay.sdk.app.PayTask;
import com.genexus.android.core.actions.ApiAction;
import com.genexus.android.core.actions.ExternalObjectEvent;
import com.genexus.android.core.base.model.Entity;
import com.genexus.android.core.base.model.EntityFactory;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.externalapi.ExternalApi;
import com.genexus.android.core.externalapi.ExternalApiResult;

import androidx.annotation.NonNull;

public class AlipayApi extends ExternalApi {

    public static final String NAME = "Alipay.AlipayProvider";

    // private static String m_AppId = "";
    // private static String m_RSAPrivateKey = "";
    // private static String m_RSA2PrivateKey = "";

    private static final int SDK_PAY_FLAG = 1;
    // private static final int SDK_AUTH_FLAG = 2;

    public AlipayApi(ApiAction action) {
        super(action);
    }

    @SuppressLint("HandlerLeak")
    private static Handler mHandler = new Handler(Looper.getMainLooper()) {
        @SuppressWarnings("unused")
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);

                    Entity paymentResult = EntityFactory.newSdt("CommonPay.PaymentResult");
                    //paymentResult.setProperty("Succeed", TextUtils.equals(payResult.getResultStatus(), "9000"));
                    //paymentResult.setProperty("Status", payResult.getResultStatus());
                    //paymentResult.setProperty("Memo", payResult.getMemo());
                    //paymentResult.setProperty("Result", payResult.getResult());
                    paymentResult.setProperty("ErrorCode", payResult.getErrorCode());
                    paymentResult.setProperty("ErrorDescription", payResult.getErrorDescription());
                    paymentResult.setProperty("OrderNumber", payResult.getOutTradeNo());
                    paymentResult.setProperty("AdditionalInfo", TextUtils.concat("status=",payResult.getResultStatus(),";result=",payResult.getResult()));

                    fireEvent("OnPaymentFinished", paymentResult);
                    break;
                }
                // Not used yet
                /*case SDK_AUTH_FLAG: {
                    @SuppressWarnings("unchecked")
                    AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);

                    Entity authorizationResult = EntityFactory.newSdt("Alipay.AuthorizationResult");
                    authorizationResult.setProperty("Succeed", TextUtils.equals(authResult.getResultStatus(), "9000"));
                    authorizationResult.setProperty("Status", authResult.getResultStatus());
                    authorizationResult.setProperty("Memo", authResult.getMemo());
                    authorizationResult.setProperty("Result", authResult.getResult());
                    authorizationResult.setProperty("OpenId", authResult.getAlipayOpenId());
                    authorizationResult.setProperty("AuthorizationCode", authResult.getAuthCode());

                    fireEvent("OnAuthorizationResult", authorizationResult);
                    break;
                }*/
                default:
                    break;
            }
        }
    };

    @Override
    public @NonNull ExternalApiResult execute(String method, List<Object> params) {
        List<String> parameterValues = toString(params);
        if (method.equalsIgnoreCase("Pay") && parameterValues.size() == 1)
        {
            //Entity connectionData = (Entity)params.get(0);
            //String uriParameters = (String)params.get(1);

            Entity paymentInformation = (Entity)params.get(0);

            //Entity invocationResult = pay(connectionData, uriParameters);
            pay(paymentInformation);
            //return ExternalApiResult.success(invocationResult);
            return ExternalApiResult.SUCCESS_CONTINUE;
        }
        // Not used
        /* else if (method.equalsIgnoreCase("SignAndPay") && parameterValues.size() == 2)
        {
            /*
            final String productCode = parameterValues.get(0);
            final long totalAmount = Long.parseLong(parameterValues.get(1));
            final String subject = parameterValues.get(2);
            final String body = parameterValues.get(3);
            */

        /*    Entity connectionData = (Entity)params.get(0);
            Entity paymentData = (Entity)params.get(1);

            //Entity invocationResult = pay(productCode ,totalAmount ,subject ,body);
            Entity invocationResult = pay(connectionData, paymentData);
            return ExternalApiResult.success(invocationResult);
        }
        else if (method.equalsIgnoreCase("SignAndAuthorize") && parameterValues.size() == 2)
        {
            /*
            final long pId = Long.parseLong(parameterValues.get(0));
            final String targetId = parameterValues.get(1);
            */

        /*    final Entity connectionData = (Entity)params.get(0);
            final Entity authorizationData = (Entity)params.get(1);

            //Entity invocationResult = authorize(pId, targetId);
            Entity invocationResult = authorize(connectionData, authorizationData);
            return ExternalApiResult.success(invocationResult);
        }
        else if (method.equalsIgnoreCase("SetAppId"))
        {
            m_AppId = parameterValues.get(0);
            return ExternalApiResult.SUCCESS_CONTINUE;
        }
        else if (method.equalsIgnoreCase("SetRSAPrivateKey"))
        {
            m_RSAPrivateKey = parameterValues.get(0);
            return ExternalApiResult.SUCCESS_CONTINUE;
        }
        else if (method.equalsIgnoreCase("SetRSA2PrivateKey"))
        {
            m_RSA2PrivateKey = parameterValues.get(0);
            return ExternalApiResult.SUCCESS_CONTINUE;
        } */
        else
            return ExternalApiResult.failureUnknownMethod(this, method);
    }

    // Not used
    /*
    public Entity pay(Entity connectionData, Entity paymentData)

                      //String productCode, long totalAmount, String subject, String body)
    {
        loadConnectionData(connectionData);

        Entity invocationResult = EntityFactory.newSdt("AliPay.InvocationResult");

        String productCode = (String)paymentData.getProperty("ProductCode");
        Double totalAmount = Double.parseDouble((String)paymentData.getProperty("TotalAmount"));
        String subject = (String)paymentData.getProperty("Subject");
        String body = (String)paymentData.getProperty("Body");
        String notifyURL =  (String)paymentData.getProperty("NotifyURL");

        if (!TextUtils.isEmpty(m_AppId) &&
                (!TextUtils.isEmpty(m_RSAPrivateKey) ||
                 !TextUtils.isEmpty(m_RSA2PrivateKey)))
        {
            invocationResult.setProperty("Succeed", true);

            boolean rsa2 = (m_RSA2PrivateKey.length() > 0);
            Map<String, String> params = OrderInfoUtil.buildOrderParamMap(m_AppId, rsa2, productCode, totalAmount, subject, body, notifyURL);
            String orderParam = OrderInfoUtil.buildOrderParam(params);

            String privateKey = rsa2 ? m_RSA2PrivateKey : m_RSAPrivateKey;
            String sign = OrderInfoUtil.getSign(params, privateKey, rsa2);
            final String orderInfo = orderParam + "&" + sign;
            final String outTradeNo = OrderInfoUtil.getOutTradeNumberFromURIParameters(orderInfo);
            Log.i("URIParameters", orderInfo);

            Runnable payRunnable = new Runnable() {

                @Override
                public void run() {
                    PayTask alipay = new PayTask(getActivity());
                    Map<String, String> result = alipay.payV2(orderInfo, true);
                    result.put("outTradeNo", outTradeNo);
                    Log.i("msp", result.toString());

                    Message msg = new Message();
                    msg.what = SDK_PAY_FLAG;
                    msg.obj = result;
                    mHandler.sendMessage(msg);
                }
            };

            Thread payThread = new Thread(payRunnable);
            payThread.start();
        }
        else
        {
            invocationResult.setProperty("Succeed", false);

            if (TextUtils.isEmpty(m_AppId))
                invocationResult.setProperty("Message", "AppId in ConnectionData is Empty.");
            else if(TextUtils.isEmpty(m_RSAPrivateKey) || TextUtils.isEmpty(m_RSA2PrivateKey))
                invocationResult.setProperty("Message", "RSAPrivateKey or RSA2PrivateKey are mandatory in ConnectionData.");
        }

        return invocationResult;
    }
    */

    //public Entity pay(Entity connectionData, String uriParameters)
    public void pay(Entity paymentInformation)
    //String productCode, long totalAmount, String subject, String body)
    {
        String environment = (String)paymentInformation.getProperty("Environment");
        // String appId = (String)paymentInformation.getProperty("AppId");
        loadConnectionData(environment);

        // Entity invocationResult = EntityFactory.newSdt("AliPay.InvocationResult");

        String uriParameters = (String)paymentInformation.getProperty("Parameters");
        if (!TextUtils.isEmpty(uriParameters))
        {
			Services.Log.info("URIParameters", uriParameters);
            // invocationResult.setProperty("Succeed", true);
            final String orderInfo  = uriParameters;
            final String outTradeNo = OrderInfoUtil.getOutTradeNumberFromURIParameters(orderInfo);

            Runnable payRunnable = new Runnable() {

                @Override
                public void run() {
                    PayTask alipay = new PayTask(getActivity());
                    Map<String, String> result = alipay.payV2(orderInfo, true);
                    result.put("outTradeNo", outTradeNo);
                    //Log.i("msp", result.toString());

                    Message msg = new Message();
                    msg.what = SDK_PAY_FLAG;
                    msg.obj = result;
                    mHandler.sendMessage(msg);
                }
            };

            Thread payThread = new Thread(payRunnable);
            payThread.start();
        }
        else
        {
            //Map<String,String> resultError = new HashMap<String,String>();
            //resultError.put("ErrorCode", "10");
            //resultError.put("ErrorDescription", "Parameters cannot be Empty");
            //invocationResult.setProperty("Succeed", false);
            //invocationResult.setProperty("Message", "Parameters cannot be Empty.");
        }

        //return invocationResult;
    }

    //private void loadConnectionData(Entity connectionData)
    private void loadConnectionData(String environment)
    {
        // m_AppId = appId; //(String)connectionData.getProperty("AppId");
        // m_RSAPrivateKey = (String)connectionData.getProperty("RSAPrivateKey");
        // m_RSA2PrivateKey = (String)connectionData.getProperty("RSA2PrivateKey");

        // if (!((String)connectionData.getProperty("Environment")).equalsIgnoreCase("OnLine"))
        if (!environment.equalsIgnoreCase("Production"))
            EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);
        else
            EnvUtils.setEnv(EnvUtils.EnvEnum.ONLINE);
    }

    // Not used
    /* public Entity authorize(Entity connectionData, Entity authorizationData)
                            //long pId, String targetId)
    {
        loadConnectionData(connectionData);

        Entity invocationResult = EntityFactory.newSdt("AliPay.InvocationResult");

        String pId = (String)authorizationData.getProperty("MerchantId");
        String targetId = (String)authorizationData.getProperty("TargetId");

        if (!TextUtils.isEmpty(m_AppId) &&
            !TextUtils.isEmpty(pId) &&
            !TextUtils.isEmpty(targetId) &&
                (!TextUtils.isEmpty(m_RSAPrivateKey) ||
                        !TextUtils.isEmpty(m_RSA2PrivateKey)))
        {
            invocationResult.setProperty("Succeed", true);

            boolean rsa2 = (m_RSA2PrivateKey.length() > 0);
            Map<String, String> authInfoMap = OrderInfoUtil.buildAuthInfoMap(pId, m_AppId, targetId, rsa2);
            String info = OrderInfoUtil.buildOrderParam(authInfoMap);

            String privateKey = rsa2 ? m_RSA2PrivateKey : m_RSAPrivateKey;
            String sign = OrderInfoUtil.getSign(authInfoMap, privateKey, rsa2);
            final String authInfo = info + "&" + sign;
            Runnable authRunnable = new Runnable() {

                @Override
                public void run() {
                    AuthTask authTask = new AuthTask(getActivity());
                    Map<String, String> result = authTask.authV2(authInfo, true);

                    Message msg = new Message();
                    msg.what = SDK_AUTH_FLAG;
                    msg.obj = result;
                    mHandler.sendMessage(msg);
                }
            };

            Thread authThread = new Thread(authRunnable);
            authThread.start();
        }
        else
        {
            invocationResult.setProperty("Succeed", false);

            if (TextUtils.isEmpty(m_AppId))
                invocationResult.setProperty("Message", "AppId in ConnectionData is Empty.");
            else if (!TextUtils.isEmpty(pId))
                invocationResult.setProperty("Message", "MerchantId in AuthorizationData is Empty.");
            else if (!TextUtils.isEmpty(targetId))
                invocationResult.setProperty("Message", "TargetId in AuthorizationData is Empty.");
            else if(TextUtils.isEmpty(m_RSAPrivateKey) || TextUtils.isEmpty(m_RSA2PrivateKey))
                invocationResult.setProperty("Message", "RSAPrivateKey or RSA2PrivateKey are mandatory in ConnectionData.");
        }

        return invocationResult;
    }
    */

    private static void fireEvent(String eventName, Object... eventArgs)
    {
        ExternalObjectEvent event = new ExternalObjectEvent(NAME, eventName);
        event.fire(Arrays.asList(eventArgs));
    }
}

