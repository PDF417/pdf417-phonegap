/**
 * PhoneGap is available under *either* the terms of the modified BSD license *or* the
 * MIT License (2008). See http://opensource.org/licenses/alphabetical for full text.
 *
 * Copyright (c) Matt Kane 2010
 * Copyright (c) 2011, IBM Corporation
 * Copyright (c) 2013, Maciej Nux Jaros
 */
package com.phonegap.plugins.pdf417;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.microblink.MicroblinkSDK;
import com.microblink.activity.BarcodeScanActivity;
import com.microblink.entities.recognizers.Recognizer;
import com.microblink.entities.recognizers.RecognizerBundle;
import com.microblink.entities.recognizers.blinkbarcode.barcode.BarcodeRecognizer;
import com.microblink.entities.recognizers.blinkbarcode.usdl.USDLKeys;
import com.microblink.entities.recognizers.blinkbarcode.usdl.USDLRecognizer;
import com.microblink.hardware.camera.CameraType;
import com.microblink.uisettings.BarcodeUISettings;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Pdf417Scanner extends CordovaPlugin {

    private static final int REQUEST_CODE = 1337;

    private static final String SCAN = "scan";
    private static final String CANCELLED = "cancelled";

    private static final String RESULT_LIST = "resultList";
    private static final String RESULT_TYPE = "resultType";
    private static final String TYPE = "type";
    private static final String DATA = "data";
    private static final String FIELDS = "fields";
    private static final String RAW_DATA = "raw";

    // barcode types
    private static final String TYPE_PDF417 = "PDF417";
    private static final String TYPE_CODE_128 = "Code 128";
    private static final String TYPE_CODE_39 = "Code 39";
    private static final String TYPE_AZTEC = "Aztec";
    private static final String TYPE_DATA_MATRIX = "Data Matrix";
    private static final String TYPE_EAN_13 = "EAN 13";
    private static final String TYPE_EAN_8 = "EAN 8";
    private static final String TYPE_ITF = "ITF";
    private static final String TYPE_QR_CODE = "QR Code";
    private static final String TYPE_UPCA = "UPCA";
    private static final String TYPE_UPCE = "UPCE";
    private static final String TYPE_USDL = "USDL";

    // scanning options
    private static final String OPTION_BEEP_SOUND = "beep";
    private static final String OPTION_NO_DIALOG = "noDialog";
    private static final String OPTION_UNCERTAIN = "uncertain";
    private static final String OPTION_QUIET_ZONE = "quietZone";
    private static final String OPTION_INVERSE_SCANNING = "inverseScanning";
    private static final String OPTION_FRONT_FACE_CAMERA = "frontFace";

    private static final String LOG_TAG = "Pdf417Scanner";

    private CallbackContext callbackContext;

    private static BarcodeRecognizer mBarcodeRecognizer;
    private static USDLRecognizer mUsdlRecognizer;
    private static RecognizerBundle mRecognizerBundle;

    /**
     * Constructor.
     */
    public Pdf417Scanner() {
    }

    /**
     * Executes the request.
     * 
     * This method is called from the WebView thread. To do a non-trivial amount
     * of work, use: cordova.getThreadPool().execute(runnable);
     * 
     * To run on the UI thread, use:
     * cordova.getActivity().runOnUiThread(runnable);
     * 
     * @param action
     *            The action to execute.
     * @param args
     *            The exec() arguments.
     * @param callbackContext
     *            The callback context used when calling back into JavaScript.
     * @return Whether the action was valid.
     * 
     * @sa 
     *     https://github.com/apache/cordova-android/blob/master/framework/src/org
     *     /apache/cordova/CordovaPlugin.java
     */
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) {
        this.callbackContext = callbackContext;

        if (action.equals(SCAN)) {
            Set<String> types = new HashSet<String>();

            // obtain array of recognizer types that will be used for scanning
            JSONArray typesArg = args.optJSONArray(0);
            for (int i = 0; i < typesArg.length(); ++i) {
                types.add(typesArg.optString(i));
            }

            // Default values
            Boolean beep = true, noDialog = null, uncertain = null, quietZone = null, inverseScanning = null, frontFace = null;
            String license = null;

            // obtain scanning options
            if (!args.isNull(1)) {
                JSONObject options = args.optJSONObject(1);

                if (!options.isNull(OPTION_BEEP_SOUND)) {
                    beep = options.optBoolean(OPTION_BEEP_SOUND);
                }
                if (!options.isNull(OPTION_NO_DIALOG)) {
                    noDialog = options.optBoolean(OPTION_NO_DIALOG);
                }
                if (!options.isNull(OPTION_UNCERTAIN)) {
                    uncertain = options.optBoolean(OPTION_UNCERTAIN);
                }
                if (!options.isNull(OPTION_QUIET_ZONE)) {
                    quietZone = options.optBoolean(OPTION_QUIET_ZONE);
                }
                if (!options.isNull(OPTION_INVERSE_SCANNING)) {
                    inverseScanning = options.optBoolean(OPTION_INVERSE_SCANNING);
                }
                if (!options.isNull(OPTION_FRONT_FACE_CAMERA)) {
                    frontFace = options.optBoolean(OPTION_FRONT_FACE_CAMERA);
                }
            }

            // obtain license key
            if (!args.isNull(3)) {
                license = args.optString(3);
            }

            scan(types, beep, noDialog, uncertain, quietZone, inverseScanning, frontFace, license);
        } else {
            return false;
        }
        return true;
    }


    /**
     * Starts an intent from provided class to scan and decode a barcode.
     */
    public void scan(Set<String> types, Boolean beep, Boolean noDialog, Boolean uncertain, Boolean quietZone, Boolean inverseScanning, Boolean frontFace, String license) {


        Context context = this.cordova.getActivity().getApplicationContext();
        FakeR fakeR = new FakeR(this.cordova.getActivity());

        // set the base64 encoded license key - obtain your key at
        // https://microblink.com
        MicroblinkSDK.setLicenseKey(license, context);

        mBarcodeRecognizer = null;
        mUsdlRecognizer = null;
        List<Recognizer<?, ?>> recognizerList = new ArrayList<Recognizer<?, ?>>();

        boolean scanPDF417 = types.contains(TYPE_PDF417);
        boolean scanCode128 = types.contains(TYPE_CODE_128);
        boolean scanCode39 = types.contains(TYPE_CODE_39);
        boolean scanAztec = types.contains(TYPE_AZTEC);
        boolean scanDataMatrix = types.contains(TYPE_DATA_MATRIX);
        boolean scanEan13 = types.contains(TYPE_EAN_13);
        boolean scanEan8 = types.contains(TYPE_EAN_8);
        boolean scanItf = types.contains(TYPE_ITF);
        boolean scanQRCode = types.contains(TYPE_QR_CODE);
        boolean scanUPCA = types.contains(TYPE_UPCA);
        boolean scanUPCE = types.contains(TYPE_UPCE);

        // BarcodeRecognizer can scan various types of 1D and 2D barcodes
        if (scanPDF417 || scanCode128 || scanCode39 || scanAztec || scanDataMatrix || scanEan13
                || scanEan8 || scanItf || scanQRCode || scanUPCA || scanUPCE) {
            mBarcodeRecognizer = new BarcodeRecognizer();
            recognizerList.add(mBarcodeRecognizer);
            mBarcodeRecognizer.setScanPDF417(scanPDF417);
            mBarcodeRecognizer.setScanCode128(scanCode128);
            mBarcodeRecognizer.setScanCode39(scanCode39);
            mBarcodeRecognizer.setScanAztecCode(scanAztec);
            mBarcodeRecognizer.setScanDataMatrixCode(scanDataMatrix);
            mBarcodeRecognizer.setScanEAN13Code(scanEan13);
            mBarcodeRecognizer.setScanEAN8Code(scanEan8);
            mBarcodeRecognizer.setScanITFCode(scanItf);
            mBarcodeRecognizer.setScanQRCode(scanQRCode);
            mBarcodeRecognizer.setScanUPCACode(scanUPCA);
            mBarcodeRecognizer.setScanUPCECode(scanUPCE);

            // Set this to true to scan barcodes which don't have quiet zone (white area) around it
            // Use only if necessary because it drastically slows down the recognition process
            mBarcodeRecognizer.setNullQuietZoneAllowed(quietZone == true);

            // Set this to true to scan even barcode not compliant with standards
            // For example, malformed PDF417 barcodes which were incorrectly encoded
            // Use only if necessary because it slows down the recognition process
            mBarcodeRecognizer.setUncertainDecoding(uncertain != null && uncertain);

            // Enables scanning of barcodes with inverse intensity values
            //(e.g. white barcode on black background)
            mBarcodeRecognizer.setInverseScanning(inverseScanning == true);
        }

        // USDLRecognizer is used for scanning US Driver's Licence barcodes
        // options available are similar to those in BarcodeRecognizer
        if (types.contains(TYPE_USDL)) {
            mUsdlRecognizer = new USDLRecognizer();
            recognizerList.add(mUsdlRecognizer);
            // Set this to true to scan even barcode not compliant with standards
            // For example, malformed PDF417 barcodes which were incorrectly encoded
            // Use only if necessary because it slows down the recognition process
            mUsdlRecognizer.setUncertainDecoding(uncertain == true);
            // disable scanning of barcodes that do not have quiet zone
            // as defined by the standard
            mUsdlRecognizer.setNullQuietZoneAllowed(quietZone == true);
        }

        // finally, when you have defined settings for each recognizer you want to use,
        // you should put them into RecognizerBundle

        Recognizer<?, ?>[] recognizerArray = new Recognizer<?, ?>[recognizerList.size()];
        recognizerArray = recognizerList.toArray(recognizerArray);
        mRecognizerBundle = new RecognizerBundle(recognizerArray);

        // additionally, there are generic settings that are used by all recognizers or the
        // whole recognition process

        // set this to true to enable returning of multiple scan results from single camera frame
        // default is false, which means that as soon as first barcode is found (no matter which type)
        // its contents will be returned.
        mRecognizerBundle.setAllowMultipleScanResultsOnSingleImage(true);

        ScanActivitySettings barcodeActivitySettings = new ScanActivitySettings(mRecognizerBundle);
        // configure showing of dialog when scanning completes
        barcodeActivitySettings.setShowDialogAfterScan(noDialog == false);
        // If you want sound to be played after the scanning process ends,
        // put here the resource ID of your sound file. (optional)
        if (beep == true) {
            barcodeActivitySettings.setBeepSoundResourceID(fakeR.getId("raw", "beep"));
        }
        // front facing camera
        if (frontFace == true) {
            barcodeActivitySettings.setCameraType(CameraType.CAMERA_FRONTFACE);
        }

        Intent intent = new Intent(context, BarcodeScanActivity.class);
        barcodeActivitySettings.saveToIntentPublic(intent);
        cordova.startActivityForResult(this, intent, REQUEST_CODE);
    }

    private static class ScanActivitySettings extends BarcodeUISettings {
        ScanActivitySettings(RecognizerBundle recognizerBundle) {
            super(recognizerBundle);
        }

        public void saveToIntentPublic(@NonNull Intent intent) {
            saveToIntent(intent);
        }
    }


    /**
     * Called when the scanner intent completes.
     * 
     * @param requestCode
     *            The request code originally supplied to
     *            startActivityForResult(), allowing you to identify who this
     *            result came from.
     * @param resultCode
     *            The integer result code returned by the child activity through
     *            its setResult().
     * @param data
     *            An Intent, which can return result data to the caller (various
     *            data can be attached to Intent "extras").
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CODE) {

            if (resultCode == BarcodeScanActivity.RESULT_OK) {

                // obtain results from intent
                mRecognizerBundle.loadFromIntent(data);

                JSONArray resultsList = new JSONArray();

                try {
                    if (mBarcodeRecognizer != null && mBarcodeRecognizer.getResult().getResultState() != Recognizer.Result.State.Empty) {
                        resultsList.put(convertBarcodeResult(mBarcodeRecognizer.getResult()));
                    }
                    if (mUsdlRecognizer != null && mUsdlRecognizer.getResult().getResultState() != Recognizer.Result.State.Empty) {
                        resultsList.put(convertUSDLResult(mUsdlRecognizer.getResult()));
                    }
                } catch (Exception e) {
                    Log.e(LOG_TAG, "Error while converting result for JS");
                }
                
                try {
                    JSONObject root = new JSONObject();
                    root.put(RESULT_LIST, resultsList);             
                    root.put(CANCELLED, false);
                    this.callbackContext.success(root);
                } catch (JSONException e) {
                    Log.e(LOG_TAG, "This should never happen");
                }

            } else if (resultCode == BarcodeScanActivity.RESULT_CANCELED) {
                JSONObject obj = new JSONObject();
                try {
                    obj.put(CANCELLED, true);

                } catch (JSONException e) {
                    Log.e(LOG_TAG, "This should never happen");
                }
                this.callbackContext.success(obj);

            } else {
                this.callbackContext.error("Unexpected error");
            }
        }
    }

    private JSONObject convertBarcodeResult(BarcodeRecognizer.Result recognizerResult) throws JSONException {
        JSONObject result = new JSONObject();
        result.put(RESULT_TYPE, "Barcode result");
        result.put(TYPE, recognizerResult.getBarcodeFormat().name());
        result.put(DATA, recognizerResult.getStringData());
        result.put(RAW_DATA, byteArrayToHex(recognizerResult.getRawData()));
        return result;
    }

    private JSONObject convertUSDLResult(USDLRecognizer.Result recognizerResult) throws JSONException {
        JSONArray fields = new JSONArray();
        USDLKeys[] usdlKeys = USDLKeys.values();
        for (int i = 0; i < usdlKeys.length; i++) {
            String fieldValue = recognizerResult.getField(usdlKeys[i]);
            if (!fieldValue.isEmpty()) {
                fields.put(i, fieldValue);
            }
        }
        JSONObject result = new JSONObject();
        result.put(RESULT_TYPE, "USDL result");
        result.put(FIELDS, fields);
        result.put(RAW_DATA, byteArrayToHex(recognizerResult.getRawData()));
        return result;
    }
    
    private String byteArrayToHex(byte[] data) {
        StringBuilder sb = new StringBuilder();
        for (byte b : data) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}