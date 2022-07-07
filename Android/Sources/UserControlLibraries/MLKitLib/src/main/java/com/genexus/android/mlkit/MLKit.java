/*
               File: MLKit.java
        Description: Wrap for ML Kit external object online implementation
             Author: Damián Salvia
       Generated on: April 17, 2019

       Copyright © 2019 GeneXus. All rights reserved.
*/

package com.genexus.android.mlkit;

import java.util.concurrent.ExecutionException;

import com.genexus.android.core.actions.ApiAction;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.externalapi.ExternalApi;
import com.genexus.android.core.externalapi.ExternalApiResult;

import org.json.JSONException;
import org.json.JSONObject;


public class MLKit extends ExternalApi {
    
    static final String OBJECT_NAME = "GeneXusAI.zProvider.MLK.Core.Definition.MLKit";

    // Methods names
	private static final String METHOD_DETECT_FACES = "DetectFaces";
    private static final String METHOD_DETECT_OBJECTS = "DetectObjects";
	private static final String METHOD_LABEL_IMAGES = "LabelImages";
	private static final String METHOD_RECOGNIZE_TEXT = "RecognizeText";
	private static final String METHOD_RECOGNIZE_LANDMARKS = "RecognizeLandmarks";
	private static final String METHOD_IDENTIFY_LANGUAGE = "IdentifyLanguage";
    private static final String METHOD_TRANSLATE_TEXT = "TranslateText";


	public MLKit(ApiAction action) {
		super(action);
		addMethodHandler(METHOD_DETECT_FACES, 3, mMethodDetectFaces);
        addMethodHandler(METHOD_DETECT_OBJECTS, 3, mMethodDetectObjects);
		addMethodHandler(METHOD_LABEL_IMAGES, 3, mMethodImageLabels);
		addMethodHandler(METHOD_RECOGNIZE_TEXT, 3, mMethodRecognizeText);
		addMethodHandler(METHOD_RECOGNIZE_LANDMARKS, 3, mMethodRecognizeLandmarks);
		addMethodHandler(METHOD_IDENTIFY_LANGUAGE, 3, mMethodIdentifyLanguage);
        addMethodHandler(METHOD_TRANSLATE_TEXT, 5, mMethodTranslateText);
	}


	@SuppressWarnings("FieldCanBeLocal")
	private final IMethodInvoker mMethodDetectFaces = parameters -> {
        final String imageLocation = (String) parameters.get(0);
        final String appId = (String) parameters.get(1);
        final String apiKey = (String) parameters.get(2);

        try{
            JSONObject sdtDetectFaces = MLKitCore.getDetectFacesResult(imageLocation,appId,apiKey);
            return ExternalApiResult.success(sdtDetectFaces);
        } catch (JSONException | InterruptedException | ExecutionException e) {
			Services.Log.error(METHOD_DETECT_FACES, e);
        }
        return ExternalApiResult.FAILURE;
    };


    @SuppressWarnings("FieldCanBeLocal")
    private final IMethodInvoker mMethodDetectObjects = parameters -> {
        final String imageLocation = (String) parameters.get(0);
        final String appId = (String) parameters.get(1);
        final String apiKey = (String) parameters.get(2);

        try{
            JSONObject sdtDetectObjects = MLKitCore.getDetectObjectResult(imageLocation,appId,apiKey);
            return ExternalApiResult.success(sdtDetectObjects);
        } catch (JSONException | InterruptedException | ExecutionException e) {
			Services.Log.error(METHOD_DETECT_OBJECTS, e);
        }
        return ExternalApiResult.FAILURE;
    };


	@SuppressWarnings("FieldCanBeLocal")
	private final IMethodInvoker mMethodImageLabels = parameters -> {
        final String imageLocation = (String) parameters.get(0);
        final String appId = (String) parameters.get(1);
        final String apiKey = (String) parameters.get(2);

        try{
            JSONObject sdtImageLabels = MLKitCore.getLabelImagesResult(imageLocation,appId,apiKey);
            return ExternalApiResult.success(sdtImageLabels);
        } catch (JSONException | InterruptedException | ExecutionException e) {
			Services.Log.error(METHOD_LABEL_IMAGES, e);
        }
        return ExternalApiResult.FAILURE;
    };


	@SuppressWarnings("FieldCanBeLocal")
	private final IMethodInvoker mMethodRecognizeText = parameters -> {
        final String imageLocation = (String) parameters.get(0);
        final String appId = (String) parameters.get(1);
        final String apiKey = (String) parameters.get(2);

        try{
            JSONObject sdtRecognizeText = MLKitCore.getRecognizeTextResult(imageLocation,appId,apiKey);
            return ExternalApiResult.success(sdtRecognizeText);
        } catch (JSONException | InterruptedException | ExecutionException e) {
			Services.Log.error(METHOD_RECOGNIZE_TEXT, e);
        }
        return ExternalApiResult.FAILURE;
    };


	@SuppressWarnings("FieldCanBeLocal")
	private final IMethodInvoker mMethodRecognizeLandmarks = parameters -> {
        final String imageLocation = (String) parameters.get(0);
        final String appId = (String) parameters.get(1);
        final String apiKey = (String) parameters.get(2);
        
        try{
            JSONObject sdtRecognizeLandmarks = MLKitCore.getRecognizeLandmarksResult(imageLocation,appId,apiKey);
            return ExternalApiResult.success(sdtRecognizeLandmarks);
        } catch (JSONException | InterruptedException | ExecutionException e) {
			Services.Log.error(METHOD_RECOGNIZE_LANDMARKS, e);
        }
        return ExternalApiResult.FAILURE;
    };


	@SuppressWarnings("FieldCanBeLocal")
	private final IMethodInvoker mMethodIdentifyLanguage = parameters -> {
        final String text = (String) parameters.get(0);
        final String appId = (String) parameters.get(1);
        final String apiKey = (String) parameters.get(2);

        try{
            JSONObject sdtIdentifyLanguage = MLKitCore.getIdentifyLanguageResult(text,appId,apiKey);
            return ExternalApiResult.success(sdtIdentifyLanguage);
        } catch (JSONException | InterruptedException | ExecutionException e) {
            Services.Log.error(METHOD_IDENTIFY_LANGUAGE, e);
        }
        return ExternalApiResult.FAILURE;
    };


    @SuppressWarnings("FieldCanBeLocal")
    private final IMethodInvoker mMethodTranslateText = parameters -> {
        final String text = (String) parameters.get(0);
        final String from = (String) parameters.get(1);
        final String to = (String) parameters.get(2);
        final String appId = (String) parameters.get(3);
        final String apiKey = (String) parameters.get(4);

        try{
            JSONObject sdtTranslateText = MLKitCore.getTranslateTextResult(text,from,to,appId,apiKey);
            return ExternalApiResult.success(sdtTranslateText);
        } catch (JSONException | InterruptedException | ExecutionException e) {
			Services.Log.error(METHOD_TRANSLATE_TEXT, e);
        }
        return ExternalApiResult.FAILURE;
    };

}
