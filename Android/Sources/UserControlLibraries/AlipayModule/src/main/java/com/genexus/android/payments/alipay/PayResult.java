package com.genexus.android.payments.alipay;

import java.util.Map;

import android.text.TextUtils;

public class PayResult {
	private String resultStatus;
	private String result;
	private String memo;
	private String outTradeNo;
	private String errorCode;
	private String errorDescription;

	public PayResult(Map<String, String> rawResult) {
		if (rawResult == null) {
			return;
		}

		for (String key : rawResult.keySet()) {
			if (TextUtils.equals(key, "resultStatus"))
				resultStatus = rawResult.get(key);
			else if (TextUtils.equals(key, "result"))
				result = rawResult.get(key);
			else if (TextUtils.equals(key, "memo"))
				memo = rawResult.get(key);
			else if (TextUtils.equals(key, "outTradeNo"))
				outTradeNo = rawResult.get(key);
			else if (TextUtils.equals(key, "errorCode"))
				errorCode = rawResult.get(key);
			else if (TextUtils.equals(key, "errorDescription"))
				errorDescription = rawResult.get(key);
		}

		if (TextUtils.isEmpty(errorCode)) {
			errorCode = resultStatus;
		}
		if (TextUtils.isEmpty(errorDescription)) {
			errorDescription = memo;
		}
		if (TextUtils.equals(errorCode,"9000")) {
			errorCode = "0";
			errorDescription = "OK";
		}
	}

	@Override
	public String toString() {
		return "resultStatus={" + resultStatus + "};memo={" + memo
				+ "};result={" + result + "}" + "};outTradeNo={" + outTradeNo + "}"
				+ "};errorCode={" + errorCode + "}" + "};errorDescription={" + errorDescription + "}";
	}

	/**
	 * @return the resultStatus
	 */
	public String getResultStatus() {
		return resultStatus;
	}

	/**
	 * @return the memo
	 */
	public String getMemo() {
		return memo;
	}

	/**
	 * @return the result
	 */
	public String getResult() {
		return result;
	}

	public String getOutTradeNo() {
		return outTradeNo ;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public String getErrorDescription() {
		return errorDescription;
	}
}
