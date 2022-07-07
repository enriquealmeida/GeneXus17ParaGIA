package com.genexus.android.core.common.okhttp;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.nio.charset.Charset;

import org.json.JSONException;
import org.json.JSONObject;

import com.genexus.android.core.base.services.LogCategory;
import com.genexus.android.core.base.services.LogLevel;
import com.genexus.android.core.base.services.Services;

import kotlin.Pair;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.GzipSource;


import static java.nio.charset.StandardCharsets.UTF_8;

public class NetworkLogger
{
	private static final String LOG_TAG = "Genexus-HTTP";

	public static void logRequest(Request request)
	{
		int logLevel = Services.Log.getLevel(LogCategory.DATA_HTTP);

		if (logLevel >= LogLevel.INFO)
		{
			String uri = request.url().toString();
			String method = request.method();

			if (logLevel >= LogLevel.DEBUG)
			{
				// Detailed logging
				try
				{
					JSONObject jsonRequest = new JSONObject();
					jsonRequest.put("url", uri);
					jsonRequest.put("method", method);

					// Headers
					addHeaders(jsonRequest, request.headers());

					// Log Body
					logRequestBody(request, jsonRequest);

					log("request", jsonRequest, false);
				}
				catch (JSONException ignored) { }
			}
			else
			{
				// Just log request URL.
				log(String.format("Request (%s) to %s ", method, uri), false);
			}
		}
	}

	public static void logResponse(Request request, Response response)
	{
		int logLevel = Services.Log.getLevel(LogCategory.DATA_HTTP);

		if (logLevel >= LogLevel.INFO)
		{
			String uri = request.url().toString();
			int statusCode = response.code();

			if (logLevel >= LogLevel.DEBUG)
			{
				// Detailed logging
				try
				{
					JSONObject jsonResponse = new JSONObject();
					jsonResponse.put("url", uri);
					jsonResponse.put("statusCode", statusCode);

					// Headers
					addHeaders(jsonResponse, response.headers());

					// Log Body
					logResponseBody(response, jsonResponse);

					log("response", jsonResponse, isHttpError(statusCode));
				}
				catch (JSONException ignored) { }
			}
			else
			{
				// Just log request URL.
				log(String.format("Response (%s) from %s", statusCode, uri), isHttpError(statusCode));
			}
		}
	}

	private static void logRequestBody(Request request, JSONObject jsonRequest)
	{
		RequestBody requestBody = request.body();

		// from sample logger:
		// https://github.com/square/okhttp/blob/master/okhttp-logging-interceptor/
		//

		try
		{
			if (requestBody != null) {
				if (bodyHasUnknownEncoding(request.headers())) {
		    		jsonRequest.put("body", "(encoded body omitted)");
				} else if (requestBody.isDuplex()) {
					jsonRequest.put("body", "(duplex request body omitted)");
				} else if (requestBody.isOneShot()) {
					jsonRequest.put("body", "(one-shot body omitted)");
				// not read binary body
				} else if (requestBody instanceof ProgressInputStreamRequestBody)
					jsonRequest.put("body", "(binary stream body omitted)");
				else {
					Buffer buffer = new Buffer();
					requestBody.writeTo(buffer);

					MediaType contentType = requestBody.contentType();
					Charset charset = UTF_8;
					if (contentType != null) charset = contentType.charset(UTF_8);

					if (OkHttpBufferUtf8.Companion.isProbablyUtf8(buffer)) {
						jsonRequest.put("body", buffer.readString(charset));
					} else {
						jsonRequest.put("body", "(byte body omitted)");
					}
				}
			}
		}
		catch (IOException | JSONException ignored) { }
	}

	private static void logResponseBody(Response response, JSONObject jsonResponse)
	{
		try
		{
			ResponseBody responseBody = response.body();
			if (responseBody != null) {

				// Read response body.
				if (bodyHasUnknownEncoding(response.headers()))
				{
					jsonResponse.put("data", "(encoded body omitted)");
				} else {
					BufferedSource source = responseBody.source();
					source.request(Long.MAX_VALUE); // Buffer the entire body.
					Buffer buffer = source.getBuffer();

					Long contentLength = responseBody.contentLength();
					Long gzippedLength = null;
					String contentEncoding = response.header("Content-Encoding");
					if ("gzip".equalsIgnoreCase(contentEncoding))
					{
						gzippedLength = buffer.size();
						try (GzipSource gzipSource = new GzipSource(buffer.clone())) {
							buffer = new Buffer();
							buffer.writeAll(gzipSource);
						}
					}

					MediaType contentType = responseBody.contentType();
					Charset charset = UTF_8;
					if (contentType != null) charset = contentType.charset(UTF_8);

					// log size
					jsonResponse.put("bytes", buffer.size());
					if (gzippedLength != null)
						jsonResponse.put("gzipped-byte", gzippedLength);

					// log content
					if (!OkHttpBufferUtf8.Companion.isProbablyUtf8(buffer)) {
						jsonResponse.put("data", "(byte body omitted)");
				  		return;
					}
					if (contentLength != 0L) {
						jsonResponse.put("data", buffer.clone().readString(charset));
					}

				}
			}
		}
		catch (IOException | JSONException ignored) { }
	}

	private static boolean bodyHasUnknownEncoding(Headers headers)
	{
		String contentEncoding = headers.get("Content-Encoding");
	    if (contentEncoding==null) return false;
		return !contentEncoding.equalsIgnoreCase("identity") && !contentEncoding.equalsIgnoreCase("gzip");
	}

	public static void logException(Request request, IOException exception)
	{
		int logLevel = Services.Log.getLevel(LogCategory.DATA_HTTP);

		if (logLevel >= LogLevel.ERROR)
		{
			String uri = request.url().toString();
			String exceptionClass = exception.getClass().getName();

			if (logLevel >= LogLevel.DEBUG)
			{
				// Detailed logging
				try
				{
					JSONObject jsonException = new JSONObject();
					jsonException.put("url", request.url().toString());

					// Exception detail.
					JSONObject jsonError = new JSONObject();
					jsonError.put("class", exceptionClass);
					jsonError.put("message", exception.getMessage());
					jsonException.put("error", jsonError);

					jsonException.put("localizedDescription", exception.getLocalizedMessage());

					log("requestFail", jsonException, true);
				}
				catch (JSONException ignored) { }
			}
			else
			{
				String logMessage = String.format("Error (%s) from %s", exceptionClass, uri);
				Services.Log.error(LogCategory.DATA_HTTP, LOG_TAG, logMessage, exception);
			}
		}
	}


	private static void log(String name, JSONObject entry, boolean isError)
	{
		try
		{
			JSONObject enclosing = new JSONObject();
			enclosing.put(name, entry);
			log(enclosing.toString(), isError);
		}
		catch (JSONException ignored) { }
	}

	private static void log(String text, boolean isError)
	{
		if (isError)
			Services.Log.error(LogCategory.DATA_HTTP, LOG_TAG, text);
		else
			Services.Log.debug(LogCategory.DATA_HTTP, LOG_TAG, text);
	}

	private static void addHeaders(JSONObject entry, Headers headers) throws JSONException
	{
		// Headers
		JSONObject jsonHeaders = new JSONObject();
		for (Pair<? extends String, ? extends String> header : headers)
			jsonHeaders.put(header.getFirst(), header.getSecond());

		entry.put("headers", jsonHeaders);
	}

	private static boolean isHttpError(int statusCode)
	{
		// Take anything as "unexpected" except for the following "normal" statuses.
		return !(statusCode == HttpURLConnection.HTTP_OK ||
				statusCode == HttpURLConnection.HTTP_CREATED ||
				statusCode == HttpURLConnection.HTTP_NOT_MODIFIED ||
				statusCode == HttpURLConnection.HTTP_SEE_OTHER ||
				statusCode == HttpURLConnection.HTTP_ACCEPTED);
	}
}
