/*
               File: MLKitOffline.java
        Description: Wrap for ML Kit external object offline implementation
             Author: Damián Salvia
       Generated on: April 30, 2019

       Copyright © 2019 GeneXus. All rights reserved.
*/

package com.genexus.android.mlkit;

import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.ReflectionHelper;
import com.genexus.xml.GXXMLSerializable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

@SuppressWarnings({"unused"})
class MLKitOffline {

    public static final String CLASS_NAME = "com.genexus.mlkit.MLKitOffline";

    // Methods names
    private static final String METHOD_DETECT_FACES = "DetectFaces";
    private static final String METHOD_DETECT_OBJECTS = "DetectObjects";
    private static final String METHOD_LABEL_IMAGES = "LabelImages";
    private static final String METHOD_RECOGNIZE_TEXT = "RecognizeText";
    private static final String METHOD_RECOGNIZE_LANDMARKS = "RecognizeLandmarks";
    private static final String METHOD_IDENTIFY_LANGUAGE = "IdentifyLanguage";
    private static final String METHOD_TRANSLATE_TEXT = "TranslateText";


    private MLKitOffline()
    { }


    public static Object detectFaces(String imageLocation, String appId, String apiKey)
    {
        try{
            
            GXXMLSerializable result = newResult(METHOD_DETECT_FACES);
    
            JSONObject sdtDetectFacesJson = MLKitCore.getDetectFacesResult(imageLocation,appId,apiKey);
            result.fromJSonString(sdtDetectFacesJson.toString());
    
            return result;
    
        } catch (InterruptedException | ExecutionException | JSONException e) {
            Services.Log.error(METHOD_DETECT_FACES, e);
        }
        return null;
    }


    public static Object detectObjects(String imageLocation, String appId, String apiKey)
    {
        try{
            
            GXXMLSerializable result = newResult(METHOD_DETECT_OBJECTS);
    
            JSONObject sdtDetectObjectsJson = MLKitCore.getDetectObjectResult(imageLocation,appId,apiKey);
            result.fromJSonString(sdtDetectObjectsJson.toString());
    
            return result;
    
        } catch (InterruptedException | ExecutionException | JSONException e) {
			Services.Log.error(METHOD_DETECT_OBJECTS, e);
        }
        return null;
    }


    public static Object labelImages(String imageLocation, String appId, String apiKey)
    {
        try{
            
            GXXMLSerializable result = newResult(METHOD_LABEL_IMAGES);
    
            JSONObject sdtLabelImagesJson = MLKitCore.getLabelImagesResult(imageLocation,appId,apiKey);
            result.fromJSonString(sdtLabelImagesJson.toString());

            return result;

        } catch (InterruptedException | ExecutionException | JSONException e) {
			Services.Log.error(METHOD_LABEL_IMAGES, e);
        }
        return null;
    }


    public static Object recognizeText(String imageLocation, String appId, String apiKey)
    {
        try{
            
            GXXMLSerializable result = newResult(METHOD_RECOGNIZE_TEXT);

            JSONObject sdtRecognizeTextJson = MLKitCore.getRecognizeTextResult(imageLocation,appId,apiKey);
            result.fromJSonString(sdtRecognizeTextJson.toString());

            return result;
    
        } catch (InterruptedException | ExecutionException | JSONException e) {
			Services.Log.error(METHOD_RECOGNIZE_TEXT, e);
        }
        return null;
    }


    public static Object recognizeLandmarks(String imageLocation, String appId, String apiKey)
    {
        try{

            GXXMLSerializable result = newResult(METHOD_RECOGNIZE_LANDMARKS);

            JSONObject sdtRecognizeLandmarksJson = MLKitCore.getRecognizeLandmarksResult(imageLocation,appId,apiKey);
            result.fromJSonString(sdtRecognizeLandmarksJson.toString());

            return result;

        } catch (InterruptedException | ExecutionException | JSONException e) {
			Services.Log.error(METHOD_RECOGNIZE_LANDMARKS, e);
        }
        return null;
    }


    public static Object identifyLanguage(String text, String appId, String apiKey)
    {
        try {

            GXXMLSerializable result = newResult(METHOD_IDENTIFY_LANGUAGE);

            JSONObject sdtIdentifyLanguageJson = MLKitCore.getIdentifyLanguageResult(text, appId, apiKey);
            result.fromJSonString(sdtIdentifyLanguageJson.toString());

            return result;

        } catch (InterruptedException | ExecutionException | JSONException e) {
			Services.Log.error(METHOD_IDENTIFY_LANGUAGE, e);
        }
        return null;
    }


    public static Object translateText(String text, String from, String to, String appId, String apiKey)
    {
        try{

            GXXMLSerializable result = newResult(METHOD_TRANSLATE_TEXT);

            JSONObject sdtTranslateTextJson = MLKitCore.getTranslateTextResult(text,from,to,appId,apiKey);
            result.fromJSonString(sdtTranslateTextJson.toString());

            return result;

        } catch (InterruptedException | ExecutionException | JSONException e) {
			Services.Log.error(METHOD_TRANSLATE_TEXT, e);
        }
        return null;
    }


    /* HELPERS methods
    *******************/
    private static GXXMLSerializable newResult(String className)
    {
        String classFull = "com.genexusai.genexusai.zprovider.mlk.core.definition.Sdt"+className;

        Class<?> clazz = ReflectionHelper.getClass(Object.class, classFull);
        if (clazz == null)
            throw new IllegalStateException(String.format("%s class could not be loaded by reflection (at com.genexusai.MLKitOffline.newResult)",classFull));

        Object obj = ReflectionHelper.createDefaultInstance(clazz,true);
        if (obj == null)
            throw new IllegalStateException(String.format("%s class could not be instantiated (at com.genexusai.MLKitOffline.newResult)",classFull));

        return (GXXMLSerializable) obj;
    }

}
