/*
               File: MLKitCore.java
        Description: Firebase ML Kit implementation
             Author: Damián Salvia
       Generated on: May 7, 2019

       Copyright © 2019 GeneXus. All rights reserved.
*/

package com.genexus.android.mlkit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;

import com.genexus.android.core.activities.ActivityHelper;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.controls.ProgressDialogFactory;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.languageid.FirebaseLanguageIdentification;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.cloud.FirebaseVisionCloudDetectorOptions;
import com.google.firebase.ml.vision.cloud.landmark.FirebaseVisionCloudLandmark;
import com.google.firebase.ml.vision.cloud.landmark.FirebaseVisionCloudLandmarkDetector;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.common.FirebaseVisionLatLng;
import com.google.firebase.ml.vision.common.FirebaseVisionPoint;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceContour;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceLandmark;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler;
import com.google.firebase.ml.vision.objects.FirebaseVisionObject;
import com.google.firebase.ml.vision.objects.FirebaseVisionObjectDetector;
import com.google.firebase.ml.vision.objects.FirebaseVisionObjectDetectorOptions;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;
import com.google.firebase.ml.vision.text.RecognizedLanguage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.genexus.android.core.utils.FileUtils2.SCHEME_HTTP;

@SuppressWarnings({"unused", "WeakerAccess"})
class MLKitCore {

    public static final String CLASS_NAME = "com.genexus.mlkit.MLKitCore";

    // Methods names
    private static final String METHOD_DETECT_FACES = "getDetectFacesResult";
    private static final String METHOD_DETECT_OBJECTS = "getDetectObjectsResult";
    private static final String METHOD_LABEL_IMAGES = "getLabelImagesResult";
    private static final String METHOD_RECOGNIZE_TEXT = "getRecognizeTextResult";
    private static final String METHOD_RECOGNIZE_LANDMARKS = "getRecognizeLandmarksResult";
    private static final String METHOD_IDENTIFY_LANGUAGE = "getIdentifyLanguageResult";
    private static final String METHOD_TRANSLATE_TEXT = "getTranslateTextResult";

    // Mapping constants -- https://errorprone.info/bugpattern/DoubleBraceInitialization
	@SuppressWarnings("DoubleBraceInitialization")
    private static final Map<Integer, String> CONTOURS = new HashMap<Integer, String>() {{
        put(FirebaseVisionFaceContour.ALL_POINTS, "ALL_POINTS");
        put(FirebaseVisionFaceContour.FACE, "FACE");
        put(FirebaseVisionFaceContour.LEFT_EYE, "LEFT_EYE");
        put(FirebaseVisionFaceContour.LEFT_EYEBROW_BOTTOM, "LEFT_EYEBROW_BOTTOM");
        put(FirebaseVisionFaceContour.LEFT_EYEBROW_TOP, "LEFT_EYEBROW_TOP");
        put(FirebaseVisionFaceContour.LOWER_LIP_BOTTOM, "LOWER_LIP_BOTTOM");
        put(FirebaseVisionFaceContour.LOWER_LIP_TOP, "LOWER_LIP_TOP");
        put(FirebaseVisionFaceContour.NOSE_BOTTOM, "NOSE_BOTTOM");
        put(FirebaseVisionFaceContour.NOSE_BRIDGE, "NOSE_BRIDGE");
        put(FirebaseVisionFaceContour.RIGHT_EYE, "RIGHT_EYE");
        put(FirebaseVisionFaceContour.RIGHT_EYEBROW_BOTTOM, "RIGHT_EYEBROW_BOTTOM");
        put(FirebaseVisionFaceContour.RIGHT_EYEBROW_TOP, "RIGHT_EYEBROW_TOP");
        put(FirebaseVisionFaceContour.UPPER_LIP_BOTTOM, "UPPER_LIP_BOTTOM");
        put(FirebaseVisionFaceContour.UPPER_LIP_TOP, "UPPER_LIP_TOP");
    }};

	@SuppressWarnings("DoubleBraceInitialization")
    private static final Map<Integer, String> LANDMARKS = new HashMap<Integer, String>() {{
        put(FirebaseVisionFaceLandmark.LEFT_CHEEK, "LEFT_CHEEK");
        put(FirebaseVisionFaceLandmark.LEFT_EAR, "LEFT_EAR");
        put(FirebaseVisionFaceLandmark.LEFT_EYE, "LEFT_EYE");
        put(FirebaseVisionFaceLandmark.MOUTH_BOTTOM, "MOUTH_BOTTOM");
        put(FirebaseVisionFaceLandmark.MOUTH_LEFT, "MOUTH_LEFT");
        put(FirebaseVisionFaceLandmark.MOUTH_RIGHT, "MOUTH_RIGHT");
        put(FirebaseVisionFaceLandmark.NOSE_BASE, "NOSE_BASE");
        put(FirebaseVisionFaceLandmark.RIGHT_CHEEK, "RIGHT_CHEEK");
        put(FirebaseVisionFaceLandmark.RIGHT_EAR, "RIGHT_EAR");
        put(FirebaseVisionFaceLandmark.RIGHT_EYE, "RIGHT_EYE");
    }};

	@SuppressWarnings("DoubleBraceInitialization")
    private static final Map<Integer, String> OBJECTS = new HashMap<Integer, String>() {{
        put(FirebaseVisionObject.CATEGORY_FASHION_GOOD, "FASHION_GOOD");
        put(FirebaseVisionObject.CATEGORY_FOOD, "FOOD");
        put(FirebaseVisionObject.CATEGORY_HOME_GOOD, "HOME_GOOD");
        put(FirebaseVisionObject.CATEGORY_PLACE, "PLACE");
        put(FirebaseVisionObject.CATEGORY_PLANT, "PLANT");
        put(FirebaseVisionObject.CATEGORY_UNKNOWN, "UNKNOWN");
    }};

	@SuppressWarnings("DoubleBraceInitialization")
    private static final Map<String, String> LANGUAGE = new HashMap<String, String>() {{
        // NOTE: Just contains the mapping for those language codes that changes
        put("\\0", "");
        put("nb", "no");
        put("kr", "ko");
        put("cz", "cs");
    }};

    @SuppressLint("StaticFieldLeak")
    private static FirebaseApp sFirebaseApp;


    private MLKitCore()
    { }

    /**
     * Creates a singleton instance of FirebaseApp that must be
     * instantiated before use any method provided by ML Kit.
     *
     * @param appId  Firebase Application Id.
     * @param apiKey Firebase API Key.
     * @throws IOException in case of an I/O error.
     */
    private static void initFirebaseApp(String appId, String apiKey)
            throws IOException
    {
        if (appId.equals(Strings.EMPTY) || apiKey.equals(Strings.EMPTY))
            throw new IOException("Credential are missing");

        if (sFirebaseApp == null) {

            Services.Log.info(CLASS_NAME, "Instantiating FirebaseApp");

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setApplicationId(appId)
                    .setApiKey(apiKey)
                    .build();

            sFirebaseApp = FirebaseApp.initializeApp(
                    Services.Application.getAppContext(),
                    options
            );
        }
    }


    /**
     * Use Firebase VisionFaceDetector in order to detect faces on an image.
     *
     * @param imageLocation Image location (filepath or URL).
     * @param appId Firebase Application Id.
     * @param apiKey Firebase API Key.
     * @return @return JSON structure with the detected faces (the id, a confidence, the bounding-box and additional data) and errors.
     * @throws JSONException in case of a parsing error.
     * @throws InterruptedException,ExecutionException in case of task cannot be completed.
     */
    @SuppressWarnings("FieldCanBeLocal")
    public static JSONObject getDetectFacesResult(String imageLocation, String appId, String apiKey)
            throws JSONException, InterruptedException, ExecutionException
    {
        JSONObject sdtDetectFaces = new JSONObject();

        try {

            initFirebaseApp(appId, apiKey);

            FirebaseVisionImage image = getFirebaseImage(imageLocation);

            FirebaseVisionFaceDetectorOptions options = new FirebaseVisionFaceDetectorOptions.Builder()
                    .setPerformanceMode(FirebaseVisionFaceDetectorOptions.ACCURATE)
                    .setLandmarkMode(FirebaseVisionFaceDetectorOptions.ALL_LANDMARKS)
                    .setMinFaceSize(0.15f)
                    .enableTracking()
                    .build();

            FirebaseVisionFaceDetector detector = FirebaseVision.getInstance(sFirebaseApp)
                    .getVisionFaceDetector(options);

            // Execute face detection
            Task<List<FirebaseVisionFace>> analysis = detector.detectInImage(image)
                    .addOnSuccessListener(
                            faces -> Services.Log.info(CLASS_NAME, METHOD_DETECT_FACES + " :: SUCCESS: " + faces.toString()))
                    .addOnFailureListener(
                            e -> Services.Log.info(CLASS_NAME, METHOD_DETECT_FACES + " :: FAILED: " + e.toString()));

            List<FirebaseVisionFace> faces = Tasks.await(analysis);

            if (analysis.getException() != null)
                throw new IOException(analysis.getException().getLocalizedMessage() + "(at " + METHOD_DETECT_FACES + ")");

            if (faces == null)
                throw new IOException("Analysis result is null (at " + METHOD_DETECT_FACES + ")");

            detector.close();

            JSONArray result = new JSONArray();

            for (FirebaseVisionFace face : faces) {

                JSONObject resultItem = new JSONObject();

                resultItem.put("trackingId", face.getTrackingId());

                Rect bbox = face.getBoundingBox();
                JSONObject boundingBox = new JSONObject();
                boundingBox.put("top", bbox.top);
                boundingBox.put("left", bbox.left);
                boundingBox.put("bottom", bbox.bottom);
                boundingBox.put("right", bbox.right);
                resultItem.put("boundingBox", boundingBox);

                float leftEyeOpenProbability = face.getLeftEyeOpenProbability();
                if (leftEyeOpenProbability != FirebaseVisionFace.UNCOMPUTED_PROBABILITY)
                    resultItem.put("leftEyeOpenProbability", leftEyeOpenProbability);

                float rightEyeOpenProbability = face.getRightEyeOpenProbability();
                if (rightEyeOpenProbability != FirebaseVisionFace.UNCOMPUTED_PROBABILITY)
                    resultItem.put("rightEyeOpenProbability", rightEyeOpenProbability);

                float smilingProbability = face.getSmilingProbability();
                if (smilingProbability != FirebaseVisionFace.UNCOMPUTED_PROBABILITY)
                    resultItem.put("smilingProbability", smilingProbability);

                resultItem.put("headEulerAngleY", face.getHeadEulerAngleY());
                resultItem.put("headEulerAngleZ", face.getHeadEulerAngleZ());

                JSONArray landmarks = new JSONArray();
                for (Map.Entry<Integer, String> entry : LANDMARKS.entrySet()) {
                    FirebaseVisionFaceLandmark mlkLandmark = face.getLandmark(entry.getKey());
                    if (mlkLandmark != null) { // NOTE: Add only if it is available
                        JSONObject landmarkItem = new JSONObject();
                        landmarkItem.put("type", entry.getValue());
                        JSONObject landmarkPosition = new JSONObject();
                        landmarkPosition.put("x", mlkLandmark.getPosition().getX());
                        landmarkPosition.put("y", mlkLandmark.getPosition().getY());
                        landmarkPosition.put("z", mlkLandmark.getPosition().getZ());
                        landmarkItem.put("position", landmarkPosition);
                        landmarks.put(landmarkItem);
                    }
                }
                resultItem.put("landmarks", landmarks);

                JSONArray contours = new JSONArray();
                for (Map.Entry<Integer, String> entry : CONTOURS.entrySet()) {
                    List<FirebaseVisionPoint> mlkPoints = face.getContour(entry.getKey()).getPoints();
                    if (!mlkPoints.isEmpty()) { // NOTE: Add only if it is available
                        JSONObject contourItem = new JSONObject();
                        contourItem.put("type", entry.getValue());
                        JSONArray points = new JSONArray();
                        for (FirebaseVisionPoint point : mlkPoints) {
                            JSONObject pointItem = new JSONObject();
                            pointItem.put("x", point.getX());
                            pointItem.put("y", point.getY());
                            pointItem.put("z", point.getZ());
                            points.put(pointItem);
                        }
                        contourItem.put("points", points);
                        contours.put(contourItem);
                    }
                }
                resultItem.put("contours", contours);

                result.put(resultItem);

            }

            sdtDetectFaces.put("result", result);

        } catch (IOException e) {
            JSONObject error = new JSONObject();
            error.put("code", "InputError");
            error.put("message", e.getLocalizedMessage());
            sdtDetectFaces.put("error", error);
        }

        Services.Log.info(CLASS_NAME, METHOD_DETECT_FACES + " :: RESULT:" + sdtDetectFaces.toString());
        return sdtDetectFaces;
    }

    /**
     * Use Firebase FirebaseVisionObjectDetector in order to recognize
     * objects on the image.
     *
     * @param imageLocation Image location (filepath or URL).
     * @param appId Firebase Application Id.
     * @param apiKey Firebase API Key.
     * @return JSON structure with the detected objects (the category, a confidence and the bounding-box) and errors.
     * @throws JSONException in case of a parsing error.
     * @throws InterruptedException,ExecutionException in case of task cannot be completed.
     */
    @SuppressWarnings("FieldCanBeLocal")
    public static JSONObject getDetectObjectResult(String imageLocation, String appId, String apiKey)
            throws JSONException, InterruptedException, ExecutionException
    {
        JSONObject sdtDetectObjects = new JSONObject();

        try {

            initFirebaseApp(appId, apiKey);

            FirebaseVisionImage image = getFirebaseImage(imageLocation);

            FirebaseVisionObjectDetectorOptions options =
                    new FirebaseVisionObjectDetectorOptions.Builder()
                            .setDetectorMode(FirebaseVisionObjectDetectorOptions.SINGLE_IMAGE_MODE)
                            .enableMultipleObjects()
                            .enableClassification()  // Optional
                            .build();

            FirebaseVisionObjectDetector detector = FirebaseVision.getInstance(sFirebaseApp)
                    .getOnDeviceObjectDetector(options);

            // Execute object detection
            Task<List<FirebaseVisionObject>> analysis = detector.processImage(image)
                    .addOnSuccessListener(
                            landmarks -> Services.Log.info(CLASS_NAME, METHOD_DETECT_OBJECTS + " :: SUCCESS: " + landmarks.toString()))
                    .addOnFailureListener(
                            e -> Services.Log.info(CLASS_NAME, METHOD_DETECT_OBJECTS + " :: FAILED: " + e.toString()));

            List<FirebaseVisionObject> objects = Tasks.await(analysis);

            if (analysis.getException() != null)
                throw new IOException(analysis.getException().getLocalizedMessage() + "(at " + METHOD_DETECT_OBJECTS + ")");

            if (objects == null)
                throw new IOException("Analysis result is null (at " + METHOD_DETECT_OBJECTS + ")");

            detector.close();

            JSONArray result = new JSONArray();

            for (FirebaseVisionObject object : objects) {
                JSONObject resultItem = new JSONObject();

                resultItem.put("category", OBJECTS.get(object.getClassificationCategory()));
                resultItem.put("confidence", object.getClassificationConfidence());
                resultItem.put("trackId", object.getTrackingId());

                Rect bbox = object.getBoundingBox();
                JSONObject boundingBox = new JSONObject();
                boundingBox.put("top", bbox.top);
                boundingBox.put("left", bbox.left);
                boundingBox.put("bottom", bbox.bottom);
                boundingBox.put("right", bbox.right);
                resultItem.put("boundingBox", boundingBox);

                result.put(resultItem);
            }

            sdtDetectObjects.put("result", result);

        } catch (IOException e) {
            JSONObject error = new JSONObject();
            error.put("code", "InputError");
            error.put("message", e.getLocalizedMessage());
            sdtDetectObjects.put("error", error);
        }

        Services.Log.info(CLASS_NAME, METHOD_DETECT_OBJECTS + " :: RESULT:" + sdtDetectObjects.toString());
        return sdtDetectObjects;
    }


    /**
     * Use Firebase DeviceImageLabeler in order to tag the image.
     *
     * @param imageLocation Image location (filepath or URL).
     * @param appId Firebase Application Id.
     * @param apiKey Firebase API Key.
     * @return JSON structure with the labels assigned (the label itself and a confidence) and errors.
     * @throws JSONException in case of a parsing error.
     * @throws InterruptedException,ExecutionException in case of task cannot be completed.
     */
    @SuppressWarnings("FieldCanBeLocal")
    public static JSONObject getLabelImagesResult(String imageLocation, String appId, String apiKey)
            throws JSONException, InterruptedException, ExecutionException
    {
        JSONObject sdtLabelImages = new JSONObject();

        try {

            initFirebaseApp(appId, apiKey);

            FirebaseVisionImage image = getFirebaseImage(imageLocation);

            FirebaseVisionImageLabeler labeler = FirebaseVision.getInstance(sFirebaseApp)
                    .getOnDeviceImageLabeler();

            // Execute image labeling
            Task<List<FirebaseVisionImageLabel>> analysis = labeler.processImage(image)
                    .addOnSuccessListener(
                            labels -> Services.Log.info(CLASS_NAME, METHOD_LABEL_IMAGES + " :: SUCCESS: " + labels.toString()))
                    .addOnFailureListener(
                            e -> Services.Log.info(CLASS_NAME, METHOD_LABEL_IMAGES + " :: FAILED: " + e.toString()));

            List<FirebaseVisionImageLabel> labels = Tasks.await(analysis);

            if (analysis.getException() != null)
                throw new IOException(analysis.getException().getLocalizedMessage() + "(at " + METHOD_LABEL_IMAGES + ")");

            if (labels == null)
                throw new IOException("Analysis result is null (at " + METHOD_LABEL_IMAGES + ")");

            labeler.close();

            JSONArray result = new JSONArray();

            for (FirebaseVisionImageLabel label : labels) {
                JSONObject resultItem = new JSONObject();

                resultItem.put("text", label.getText());
                resultItem.put("confidence", label.getConfidence());
                resultItem.put("entityId", label.getEntityId());

                result.put(resultItem);
            }

            sdtLabelImages.put("result", result);

        } catch (IOException e) {
            JSONObject error = new JSONObject();
            error.put("code", "InputError");
            error.put("message", e.getLocalizedMessage());
            sdtLabelImages.put("error", error);
        }
        Services.Log.info(CLASS_NAME, METHOD_LABEL_IMAGES + " :: RESULT:" + sdtLabelImages.toString());
        return sdtLabelImages;
    }

    /**
     * Use Firebase DeviceTextRecognizer in order to recognize text the image.
     *
     * @param imageLocation Image location (filepath or URL).
     * @param appId Firebase Application Id.
     * @param apiKey Firebase API Key.
     * @return JSON structure with the recognized text (the text itself, a confidence, the bounding-box and additional data) and errors.
     * @throws JSONException in case of a parsing error.
     * @throws InterruptedException,ExecutionException in case of task cannot be completed.
     */
    @SuppressWarnings("FieldCanBeLocal")
    public static JSONObject getRecognizeTextResult(String imageLocation, String appId, String apiKey)
            throws JSONException, InterruptedException, ExecutionException
    {
        JSONObject sdtRecognizeText = new JSONObject();

        try {

            initFirebaseApp(appId, apiKey);

            FirebaseVisionImage image = getFirebaseImage(imageLocation);

            FirebaseVisionTextRecognizer detector = FirebaseVision.getInstance(sFirebaseApp)
                    .getOnDeviceTextRecognizer();

            // Execute text recognition
            Task<FirebaseVisionText> analysis = detector.processImage(image)
                    .addOnSuccessListener(
                            ocr -> Services.Log.info(CLASS_NAME, METHOD_RECOGNIZE_TEXT + " :: SUCCESS: " + ocr.getTextBlocks().toString()))
                    .addOnFailureListener(
                            e -> Services.Log.info(CLASS_NAME, METHOD_RECOGNIZE_TEXT + " :: FAILED: " + e.toString()));

            FirebaseVisionText mlkBlocks = Tasks.await(analysis);

            if (analysis.getException() != null)
                throw new IOException(analysis.getException().getLocalizedMessage() + "(at " + METHOD_RECOGNIZE_TEXT + ")");

            if (mlkBlocks == null)
                throw new IOException("Analysis result is null (at " + METHOD_RECOGNIZE_TEXT + ")");

            detector.close();

            JSONObject result = new JSONObject();

            String text = mlkBlocks.getText();
            result.put("text", text);

            JSONArray blocks = new JSONArray();
            for (FirebaseVisionText.TextBlock block : mlkBlocks.getTextBlocks()) {

                JSONObject blockItem = new JSONObject();

                blockItem.put("text", block.getText());

                Float confidence = block.getConfidence();
                confidence = (confidence == null) ? Float.valueOf((float) 1.0) : confidence;
                blockItem.put("confidence", confidence);

                Rect bbox = block.getBoundingBox();
                if (bbox != null) { // NOTE: Add only if not null
                    JSONObject boundingBox = new JSONObject();
                    boundingBox.put("top", block.getBoundingBox().top);
                    boundingBox.put("left", block.getBoundingBox().left);
                    boundingBox.put("bottom", block.getBoundingBox().bottom);
                    boundingBox.put("right", block.getBoundingBox().right);
                    blockItem.put("boundingBox", boundingBox);
                }

                List<RecognizedLanguage> languages = block.getRecognizedLanguages();
                if (languages.size() > 0) { // NOTE: Add only there is at least one identified language
                    JSONArray recognizedLanguages = new JSONArray();
                    for (RecognizedLanguage lang : languages)
                        recognizedLanguages.put(lang.getLanguageCode());
                    blockItem.put("recognizedLanguages", recognizedLanguages);
                }

                blocks.put(blockItem);

            }
            result.put("blocks", blocks);

            sdtRecognizeText.put("result", result);

        } catch (IOException e) {
            JSONObject error = new JSONObject();
            error.put("code", "InputError");
            error.put("message", e.getLocalizedMessage());
            sdtRecognizeText.put("error", error);
        }

        Services.Log.info(CLASS_NAME, METHOD_RECOGNIZE_TEXT + " :: RESULT:" + sdtRecognizeText.toString());
        return sdtRecognizeText;
    }

    /**
     * Use Firebase VisionLandmarkDetector (on the cloud) in order to
     * recognize landmarks the image. Requires Google CloudVision API activated.
     *
     * @param imageLocation Image location (filepath or URL).
     * @param appId Firebase Application Id.
     * @param apiKey Firebase API Key.
     * @return JSON structure with the recognized landmarks (the label, confidence, bounding-box and additional data) and errors.
     * @throws JSONException in case of a parsing error.
     * @throws InterruptedException,ExecutionException in case of task cannot be completed.
     */
    @SuppressWarnings("FieldCanBeLocal")
    public static JSONObject getRecognizeLandmarksResult(String imageLocation, String appId, String apiKey)
            throws JSONException, InterruptedException, ExecutionException
    {
        JSONObject sdtRecognizeLandmarks = new JSONObject();

        try {

            initFirebaseApp(appId, apiKey);

            FirebaseVisionImage image = getFirebaseImage(imageLocation);

            // WARN: Only cloud-based -- Needs 'Google Vision service' enabled
            FirebaseVisionCloudDetectorOptions options = new FirebaseVisionCloudDetectorOptions.Builder()
                    .setModelType(FirebaseVisionCloudDetectorOptions.LATEST_MODEL)
                    .setMaxResults(15)
                    .build();

            FirebaseVisionCloudLandmarkDetector detector = FirebaseVision.getInstance(sFirebaseApp)
                    .getVisionCloudLandmarkDetector(options);

            // Execute landmark recognition
            Task<List<FirebaseVisionCloudLandmark>> analysis = detector.detectInImage(image)
                    .addOnSuccessListener(
                            landmarks -> Services.Log.info(CLASS_NAME, METHOD_RECOGNIZE_LANDMARKS + " :: SUCCESS: " + landmarks.toString()))
                    .addOnFailureListener(
                            e -> Services.Log.info(CLASS_NAME, METHOD_RECOGNIZE_LANDMARKS + " :: FAILED: " + e.toString()));

            List<FirebaseVisionCloudLandmark> landmarks = Tasks.await(analysis);

            if (analysis.getException() != null)
                throw new IOException(analysis.getException().getLocalizedMessage() + "(at " + METHOD_RECOGNIZE_LANDMARKS + ")");

            if (landmarks == null)
                throw new IOException("Analysis result is null (at " + METHOD_RECOGNIZE_LANDMARKS + ")");

            detector.close();

            JSONArray result = new JSONArray();

            for (FirebaseVisionCloudLandmark landmark : landmarks) {
                JSONObject resultItem = new JSONObject();

                resultItem.put("landmark", landmark.getLandmark());
                resultItem.put("entityId", landmark.getEntityId());
                resultItem.put("confidence", landmark.getConfidence());

                Rect bbox = landmark.getBoundingBox();
                if (bbox != null) { // NOTE: Add only if not null
                    JSONObject boundingBox = new JSONObject();
                    boundingBox.put("top", bbox.top);
                    boundingBox.put("left", bbox.left);
                    boundingBox.put("bottom", bbox.bottom);
                    boundingBox.put("right", bbox.right);
                    resultItem.put("boundingBox", boundingBox);
                }

                List<FirebaseVisionLatLng> mlkLocations = landmark.getLocations();
                if (!mlkLocations.isEmpty()) { // NOTE: Add only if non-empty
                    JSONArray locations = new JSONArray();
                    for (FirebaseVisionLatLng mlkLocation : mlkLocations) {
                        JSONObject location = new JSONObject();
                        location.put("longitude", mlkLocation.getLongitude());
                        location.put("latitude", mlkLocation.getLatitude());
                        locations.put(location);
                    }
                    resultItem.put("locations", locations);
                }

                result.put(resultItem);
            }

            sdtRecognizeLandmarks.put("result", result);

        } catch (IOException e) {
            JSONObject error = new JSONObject();
            error.put("code", "InputError");
            error.put("message", e.getLocalizedMessage());
            sdtRecognizeLandmarks.put("error", error);

        } catch (ExecutionException e) {
        	String msg = e.getLocalizedMessage();
			if(msg == null)
				throw e;
			String json = msg.substring(msg.indexOf("{"));
            JSONObject error = new JSONObject(json);
            if (error.opt("message") == null)
                throw e;
            sdtRecognizeLandmarks.put("error", error);
        }

        Services.Log.info(CLASS_NAME, METHOD_RECOGNIZE_LANDMARKS + " :: RESULT:" + sdtRecognizeLandmarks.toString());
        return sdtRecognizeLandmarks;
    }

    /**
     * Use Firebase FirebaseNaturalLanguage in order to identify the language
     * of a written text.
     *
     * @param text   Input text to be analyzed.
     * @param appId  Firebase Application Id.
     * @param apiKey Firebase API Key.
     * @return JSON structure with the identified language and errors.
     * @throws JSONException in case of a parsing error.
     * @throws InterruptedException,ExecutionException in case of task cannot be completed.
     */
    @SuppressWarnings("FieldCanBeLocal")
    public static JSONObject getIdentifyLanguageResult(String text, String appId, String apiKey)
            throws JSONException, InterruptedException, ExecutionException
    {
        JSONObject sdtIdentifyLanguage = new JSONObject();

        try {

            initFirebaseApp(appId, apiKey);

            FirebaseLanguageIdentification identifier = FirebaseNaturalLanguage.getInstance(sFirebaseApp)
                    .getLanguageIdentification();

            // Execute identification
            Task<String> analysis = identifier.identifyLanguage(text)
                    .addOnSuccessListener(
                            lang -> Services.Log.info(CLASS_NAME, METHOD_IDENTIFY_LANGUAGE + " :: SUCCESS: " + lang))
                    .addOnFailureListener(
                            e -> Services.Log.info(CLASS_NAME, METHOD_IDENTIFY_LANGUAGE + " :: FAILED: " + e.toString()));

            String result = Tasks.await(analysis);

            if (analysis.getException() != null)
                throw new IOException(analysis.getException().getLocalizedMessage() + "(at " + METHOD_IDENTIFY_LANGUAGE + ")");

            identifier.close();

            sdtIdentifyLanguage.put("result", result);

        } catch (IOException e) {
            JSONObject error = new JSONObject();
            error.put("code", "InputError");
            error.put("message", e.getLocalizedMessage());
            sdtIdentifyLanguage.put("error", error);
        }

        Services.Log.info(CLASS_NAME, METHOD_IDENTIFY_LANGUAGE + " :: RESULT: " + sdtIdentifyLanguage.toString());
        return sdtIdentifyLanguage;
    }

    /**
     * Use Firebase FirebaseNaturalLanguage in order to translate the source
     * text to a target language. If source language it's not specified,
     * automatically infer it by IdentifyLanguage.
     *
     * @param text   Input text to be translated.
     * @param from   Input source language (can be empty, applying language identification).
     * @param to     Input target language.
     * @param appId  Firebase Application Id.
     * @param apiKey Firebase API Key.
     * @return JSON structure with the translation and errors.
     * @throws JSONException in case of a parsing error.
     * @throws InterruptedException,ExecutionException in case of task cannot be completed.
     */
    @SuppressWarnings("FieldCanBeLocal")
    public static JSONObject getTranslateTextResult(String text, String from, String to, String appId, String apiKey)
            throws JSONException, InterruptedException, ExecutionException
    {
        JSONObject sdtTranslateText = new JSONObject();

        try {
            initFirebaseApp(appId, apiKey);

            String fromLang = from;
            fromLang = LANGUAGE.get(fromLang);
            fromLang = (fromLang == null) ? from : fromLang;
            if (fromLang.equals(Strings.EMPTY)) { // Automatic language identification
                JSONObject sdtIdentifyLanguage = getIdentifyLanguageResult(text, appId, apiKey);
                fromLang = sdtIdentifyLanguage.get("result").toString();
            }
            Integer source = FirebaseTranslateLanguage.languageForLanguageCode(fromLang);
            if (source == null)
                throw new IOException("Wrong source language '" + from + "' (at " + METHOD_TRANSLATE_TEXT + ")");

            String toLang = to;
            toLang = LANGUAGE.get(toLang);
            toLang = (toLang == null) ? to : toLang;
            Integer target = FirebaseTranslateLanguage.languageForLanguageCode(toLang);
            if (target == null)
                throw new IOException("Wrong target language '" + to + "' (at " + METHOD_TRANSLATE_TEXT + ")");

            if (source.equals(target)) {
                sdtTranslateText.put("result", text);
                return sdtTranslateText;
            }

            FirebaseTranslatorOptions options =
                    new FirebaseTranslatorOptions.Builder()
                            .setSourceLanguage(source)
                            .setTargetLanguage(target)
                            .build();

            FirebaseTranslator translator = FirebaseNaturalLanguage.getInstance(sFirebaseApp)
                    .getTranslator(options);

			FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder()
					.requireWifi()
					.build();

			Services.Log.info(CLASS_NAME, METHOD_TRANSLATE_TEXT + " :: START DOWNLOADING");
			Task<Void> download = translator.downloadModelIfNeeded(conditions)
					.addOnSuccessListener(
							v -> Services.Log.info(CLASS_NAME, METHOD_TRANSLATE_TEXT + " :: DOWNLOAD SUCCESS"))
					.addOnFailureListener(
							e -> Services.Log.info(CLASS_NAME, METHOD_TRANSLATE_TEXT + " :: DOWNLOAD FAIL " + e.toString()));

			// Show Progress indicator until model download has finished (IMPROVE ME)
			ProgressDialogFactory progressDialogFactory = new ProgressDialogFactory();
			ProgressDialogFactory.ProgressViewProvider currentProgressProvider = progressDialogFactory.getViewProvider();
			currentProgressProvider.setProgressType(
					ActivityHelper.getCurrentActivity(),
					ProgressDialogFactory.TYPE_INDETERMINATE
			);
			currentProgressProvider.showProgressIndicator(
					ActivityHelper.getCurrentActivity(),
					null,
					String.format("%s\n%s model",
							ActivityHelper.getCurrentActivity()
									.getResources()
									.getString(R.string.GXM_Downloading),
							String.format("%s-%s", fromLang, toLang)
					)
			);

			Tasks.await(download);

			// Hide Progress indicator once model has been downloaded (IMPROVE ME)
			currentProgressProvider.hideProgressIndicator(
					ActivityHelper.getCurrentActivity()
			);

			if (!download.isSuccessful() && download.getException() != null)
				throw new IOException(download.getException().getLocalizedMessage() + "(at " + METHOD_TRANSLATE_TEXT + ")");

            // Execute translation
            Task<String> analysis = translator.translate(text)
                    .addOnSuccessListener(
                            translatedText -> Services.Log.info(CLASS_NAME, METHOD_TRANSLATE_TEXT + " :: SUCCESS: " + translatedText))
                    .addOnFailureListener(
                            e -> Services.Log.info(CLASS_NAME, METHOD_TRANSLATE_TEXT + " :: FAILED: " + e.toString()));

            String result = Tasks.await(analysis);

            if (analysis.getException() != null)
                throw new IOException(analysis.getException().getLocalizedMessage() + "(at " + METHOD_TRANSLATE_TEXT + ")");

            translator.close();

            sdtTranslateText.put("result", result);

        } catch (IOException e) {
            JSONObject error = new JSONObject();
            error.put("code", "InputError");
            error.put("message", e.getLocalizedMessage());
            sdtTranslateText.put("error", error);
        }

        Services.Log.info(CLASS_NAME, METHOD_TRANSLATE_TEXT + " :: RESULT: " + sdtTranslateText.toString());
        return sdtTranslateText;
    }


    /************************************
     *             HELPERS              *
     ************************************/
    private static FirebaseVisionImage getFirebaseImage(String imageLocation)
            throws IOException
    {
        final Context appContext = Services.Application.getAppContext();

        FirebaseVisionImage firebaseImage;

        if (imageLocation.startsWith(SCHEME_HTTP)) {
            // Assumes remote file
			Bitmap bitmap = Services.Images.getBitmap(appContext, imageLocation);
			firebaseImage = FirebaseVisionImage.fromBitmap(bitmap);
        } else {
            // Assumes local file
            Uri uri = Uri.parse(imageLocation);
            firebaseImage = FirebaseVisionImage.fromFilePath(appContext, uri);
        }

        Services.Log.info(CLASS_NAME, "IMAGE: " + imageLocation);
        return firebaseImage;
    }

}
