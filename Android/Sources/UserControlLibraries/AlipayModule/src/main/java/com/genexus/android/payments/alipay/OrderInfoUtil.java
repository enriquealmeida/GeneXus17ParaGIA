package com.genexus.android.payments.alipay;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import com.genexus.android.core.base.services.Services;

import json.org.json.JSONException;
import json.org.json.JSONObject;

public class OrderInfoUtil {

	public static Map<String, String> buildAuthInfoMap(String pid, String appId, String targetId, boolean rsa2) {
		Map<String, String> keyValues = new HashMap<String, String>();

		// 商户签约拿到的app_id，如：2013081700024223
		keyValues.put("app_id", appId);

		// 商户签约拿到的pid，如：2088102123816631
		keyValues.put("pid", pid);

		// 服务接口名称， 固定值
		keyValues.put("apiname", "com.alipay.account.auth");

		// 商户类型标识， 固定值
		keyValues.put("app_name", "mc");

		// 业务类型， 固定值
		keyValues.put("biz_type", "openservice");

		// 产品码， 固定值
		keyValues.put("product_id", "APP_FAST_LOGIN");

		// 授权范围， 固定值
		keyValues.put("scope", "kuaijie");

		// 商户唯一标识，如：kkkkk091125
		keyValues.put("target_id", targetId);

		// 授权类型， 固定值
		keyValues.put("auth_type", "AUTHACCOUNT");

		// 签名类型
		keyValues.put("sign_type", rsa2 ? "RSA2" : "RSA");

		return keyValues;
	}

	public static Map<String, String> getInfoMapFromURIParameters(String uriParameters) {
		String[] params = uriParameters.split("&", -1);
		Map<String, String> map = new HashMap<String, String>();
		for (String param : params)
		{  String [] p=param.split("=", -1);
			String name = p[0];
			if(p.length>1)  {String value = p[1];
				map.put(name, value);
			}
		}
		return map;
	}

	public static String getOutTradeNumberFromURIParameters(String uriParameters){
		Map<String, String> uriMap = OrderInfoUtil.getInfoMapFromURIParameters(uriParameters);
		String bizContentEncoded = uriMap.get("biz_content");
		try
		{
			String bizContent = URLDecoder.decode(bizContentEncoded, "UTF-8");
			JSONObject jsonObj = new JSONObject(bizContent);
			return jsonObj.getString("out_trade_no");
		}
		catch(UnsupportedEncodingException | JSONException ex)
		{
			return "";
		}
	}

	public static Map<String, String> buildOrderParamMap(String appId, boolean rsa2, String productCode ,Double totalAmount, String subject, String body, String notityURL) {
		Map<String, String> keyValues = new HashMap<String, String>();

		keyValues.put("app_id", appId);

		keyValues.put("biz_content", "{\"timeout_express\":\"30m\"," +
									  "\"product_code\":\"" + productCode + "\"," +
									  "\"total_amount\":\"" + totalAmount.toString() + "\"," +
									  "\"subject\":\"" + subject + "\"," +
									  "\"body\":\"" + body + "\"," +
									  "\"notify_url\":\"" + notityURL + "\"," +
									  "\"out_trade_no\":\"" + getOutTradeNo() +  "\"}");
		
		keyValues.put("charset", "utf-8");

		keyValues.put("method", "alipay.trade.app.pay");

		keyValues.put("sign_type", rsa2 ? "RSA2" : "RSA");

		keyValues.put("timestamp", "2016-07-29 16:55:53");

		keyValues.put("version", "1.0");
		
		return keyValues;
	}

	public static String buildOrderParam(Map<String, String> map) {
		List<String> keys = new ArrayList<String>(map.keySet());

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < keys.size() - 1; i++) {
			String key = keys.get(i);
			String value = map.get(key);
			sb.append(buildKeyValue(key, value, true));
			sb.append("&");
		}

		String tailKey = keys.get(keys.size() - 1);
		String tailValue = map.get(tailKey);
		sb.append(buildKeyValue(tailKey, tailValue, true));

		return sb.toString();
	}

	private static String buildKeyValue(String key, String value, boolean isEncode) {
		StringBuilder sb = new StringBuilder();
		sb.append(key);
		sb.append("=");
		if (isEncode) {
			try {
				sb.append(URLEncoder.encode(value, "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				sb.append(value);
			}
		} else {
			sb.append(value);
		}
		return sb.toString();
	}

	public static String getSign(Map<String, String> map, String rsaKey, boolean rsa2) {
		List<String> keys = new ArrayList<String>(map.keySet());
		// key排序
		Collections.sort(keys);

		StringBuilder authInfo = new StringBuilder();
		for (int i = 0; i < keys.size() - 1; i++) {
			String key = keys.get(i);
			String value = map.get(key);
			authInfo.append(buildKeyValue(key, value, false));
			authInfo.append("&");
		}

		String tailKey = keys.get(keys.size() - 1);
		String tailValue = map.get(tailKey);
		authInfo.append(buildKeyValue(tailKey, tailValue, false));
		Services.Log.info("ParametersToSign", authInfo.toString());

		String oriSign = SignUtils.sign(authInfo.toString(), rsaKey, rsa2);
		String encodedSign = "";

		try {
			encodedSign = URLEncoder.encode(oriSign, "UTF-8");
		} catch (UnsupportedEncodingException e) { }
		return "sign=" + encodedSign;
	}

	private static String getOutTradeNo() {
		SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss", Locale.getDefault());
		Date date = new Date();
		String key = format.format(date);

		Random r = new Random();
		key = key + r.nextInt(Integer.MAX_VALUE);
		key = key.substring(0, 15);
		return key;
	}
}
