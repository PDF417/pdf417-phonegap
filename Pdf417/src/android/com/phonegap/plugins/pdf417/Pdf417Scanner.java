/**
 * PhoneGap is available under *either* the terms of the modified BSD license *or* the
 * MIT License (2008). See http://opensource.org/licenses/alphabetical for full text.
 *
 * Copyright (c) Matt Kane 2010
 * Copyright (c) 2011, IBM Corporation
 * Copyright (c) 2013, Maciej Nux Jaros
 */
package com.phonegap.plugins.pdf417;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.os.Parcelable;
import android.os.Bundle;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;


import com.microblink.activity.Pdf417ScanActivity;
import com.microblink.recognizers.barcode.BarcodeType;
import com.microblink.recognizers.barcode.bardecoder.BarDecoderRecognizerSettings;
import com.microblink.recognizers.barcode.bardecoder.BarDecoderScanResult;
import com.microblink.recognizers.barcode.pdf417.Pdf417RecognizerSettings;
import com.microblink.recognizers.barcode.pdf417.Pdf417ScanResult;
import com.microblink.recognizers.barcode.usdl.USDLRecognizerSettings;
import com.microblink.recognizers.barcode.usdl.USDLScanResult;
import com.microblink.recognizers.barcode.zxing.ZXingRecognizerSettings;
import com.microblink.recognizers.barcode.zxing.ZXingScanResult;
import com.microblink.recognizers.settings.GenericRecognizerSettings;
import com.microblink.recognizers.settings.RecognizerSettings;
import com.microblink.results.barcode.BarcodeDetailedData;
import com.microblink.view.recognition.RecognizerView;
import com.microblink.hardware.camera.CameraType;

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

	private static final String LOG_TAG = "Pdf417Scanner";

	private CallbackContext callbackContext;

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

			JSONArray typesArg = args.optJSONArray(0);
			for (int i = 0; i < typesArg.length(); ++i) {
				types.add(typesArg.optString(i));
			}

			// Default values
			Boolean customUI = false;
			Boolean beep = true, noDialog = null, uncertain = null, quietZone = null, inverseScanning = null, frontFace = null;
			String license = null;

			if (!args.isNull(1)) {
				JSONObject options = args.optJSONObject(1);

				if (!options.isNull("beep")) {
					beep = options.optBoolean("beep");
				}
				if (!options.isNull("noDialog")) {
					noDialog = options.optBoolean("noDialog");
				}
				if (!options.isNull("uncertain")) {
					uncertain = options.optBoolean("uncertain");
				}
				if (!options.isNull("quietZone")) {
					quietZone = options.optBoolean("quietZone");
				}
				if (!options.isNull("inverseScanning")) {
					inverseScanning = options.optBoolean("inverseScanning");
				}
				if (!options.isNull("frontFace")) {
					frontFace = options.optBoolean("frontFace");
				}
			}

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

		Intent intent = new Intent(context, Pdf417ScanActivity.class);

        // If you want sound to be played after the scanning process ends, 
        // put here the resource ID of your sound file. (optional)
        intent.putExtra(Pdf417ScanActivity.EXTRAS_BEEP_RESOURCE, fakeR.getId("raw", "beep_pdf417"));

        // set the license key (for commercial versions only) - obtain your key at
        // http://pdf417.mobi
        // after setting the correct license key
        if (license != null) {
        	intent.putExtra(Pdf417ScanActivity.EXTRAS_LICENSE_KEY, license);
		}

		Pdf417RecognizerSettings pdf417RecognizerSettings = null;
		if (types.contains("PDF417")) {
			// Pdf417RecognizerSettings define the settings for scanning plain PDF417 barcodes.
	        pdf417RecognizerSettings = new Pdf417RecognizerSettings();
	        // Set this to true to scan barcodes which don't have quiet zone (white area) around it
	        // Use only if necessary because it drastically slows down the recognition process
	        if (quietZone != null) {
	        	pdf417RecognizerSettings.setNullQuietZoneAllowed(quietZone);
	    	}
	        // Set this to true to scan even barcode not compliant with standards
	        // For example, malformed PDF417 barcodes which were incorrectly encoded
	        // Use only if necessary because it slows down the recognition process
	        if (uncertain != null) {
				pdf417RecognizerSettings.setUncertainScanning(uncertain);
			}
    	}

        // BarDecoderRecognizerSettings define settings for scanning 1D barcodes with algorithms
        // implemented by Microblink team.
        BarDecoderRecognizerSettings oneDimensionalRecognizerSettings = new BarDecoderRecognizerSettings();       

		oneDimensionalRecognizerSettings.setScanCode128(types.contains("Code 128"));
		oneDimensionalRecognizerSettings.setScanCode39(types.contains("Code 39"));
		if (inverseScanning != null) {
			oneDimensionalRecognizerSettings.setInverseScanning(inverseScanning);
		}

		// USDLRecognizerSettings define settings for scanning US Driver's Licence barcodes
        // options available in that settings are similar to those in Pdf417RecognizerSettings
        // if license key does not allow scanning of US Driver's License, this settings will
        // be thrown out from settings array and error will be logged to logcat.
        USDLRecognizerSettings usdlRecognizerSettings = null;
        if (types.contains("USDL")) {
	        usdlRecognizerSettings = new USDLRecognizerSettings();
	        if (uncertain != null) {
	        	usdlRecognizerSettings.setUncertainScanning(uncertain);
	    	}
		    // disable automatic scale detection
		    //sett.setAutoScaleDetection(false);
		    // disable scanning of barcodes that do not have quiet zone
		    // as defined by the standard
		    if (quietZone != null) {
		    	usdlRecognizerSettings.setNullQuietZoneAllowed(quietZone);
			}
		}

        // ZXingRecognizerSettings define settings for scanning barcodes with ZXing library
        // We use modified version of ZXing library to support scanning of barcodes for which
        // we still haven't implemented our own algorithms.
        ZXingRecognizerSettings zXingRecognizerSettings = new ZXingRecognizerSettings();
        // set this to true to enable scanning of QR codes
		zXingRecognizerSettings.setScanAztecCode(types.contains("Aztec"));
		zXingRecognizerSettings.setScanDataMatrixCode(types.contains("Data Matrix"));
		zXingRecognizerSettings.setScanEAN13Code(types.contains("EAN 13"));
		zXingRecognizerSettings.setScanEAN8Code(types.contains("EAN 8"));
		zXingRecognizerSettings.setScanITFCode(types.contains("ITF"));
		zXingRecognizerSettings.setScanQRCode(types.contains("QR Code"));
		zXingRecognizerSettings.setScanUPCACode(types.contains("UPCA"));
		zXingRecognizerSettings.setScanUPCECode(types.contains("UPCE"));	
		if (inverseScanning != null) {
			zXingRecognizerSettings.setInverseScanning(inverseScanning);
		}
		// finally, when you have defined your scanning settings, you should put them into array
        // and send that array over intent to scan activity

        RecognizerSettings[] settArray = new RecognizerSettings[] {pdf417RecognizerSettings, oneDimensionalRecognizerSettings, zXingRecognizerSettings, usdlRecognizerSettings};
        // use Pdf417ScanActivity.EXTRAS_RECOGNIZER_SETTINGS_ARRAY to set array of recognizer settings
        intent.putExtra(Pdf417ScanActivity.EXTRAS_RECOGNIZER_SETTINGS_ARRAY, settArray);


		// additionally, there are generic settings that are used by all recognizers or the
        // whole recognition process
        GenericRecognizerSettings genericSettings = new GenericRecognizerSettings();
        // set this to true to enable returning of multiple scan results from single camera frame
        // default is false, which means that as soons as first barcode is found (no matter which type)
        // its contents will be returned.
        genericSettings.setAllowMultipleScanResultsOnSingleImage(true);
        intent.putExtra(Pdf417ScanActivity.EXTRAS_GENERIC_SETTINGS, genericSettings);


		// If you want sound to be played after the scanning process ends, 
		// put here the resource ID of your sound file. (optional)
		if (beep == true) {
			intent.putExtra(Pdf417ScanActivity.EXTRAS_BEEP_RESOURCE, fakeR.getId("raw", "beep_pdf417"));
		}

		// if you do not want the dialog to be shown when scanning completes, add following extra
        // to intent
        if (noDialog != null) {
        	intent.putExtra(Pdf417ScanActivity.EXTRAS_SHOW_DIALOG_AFTER_SCAN, noDialog);
		}

		// front facing camera
		if (frontFace != null && frontFace == true) {
			intent.putExtra(Pdf417ScanActivity.EXTRAS_CAMERA_TYPE, (Parcelable)CameraType.CAMERA_FRONTFACE);
		}

		this.cordova.startActivityForResult((CordovaPlugin) this, intent, REQUEST_CODE);
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
	 * @param intent
	 *            An Intent, which can return result data to the caller (various
	 *            data can be attached to Intent "extras").
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == REQUEST_CODE) {

			if (resultCode == Pdf417ScanActivity.RESULT_OK) {

				// First, obtain scan results array. If scan was successful, array will contain at least one element.
	            // Multiple element may be in array if multiple scan results from single image were allowed in settings.

	            Parcelable[] resultArray = data.getParcelableArrayExtra(Pdf417ScanActivity.EXTRAS_RECOGNITION_RESULT_LIST);

	            // Each recognition result corresponds to active recognizer. As stated earlier, there are 4 types of
	            // recognizers available (PDF417, Bardecoder, ZXing and USDL), so there are 4 types of results
	            // available.

	            JSONArray resultsList = new JSONArray();	            

				for (Parcelable p : resultArray) {
					try {
		                if (p instanceof Pdf417ScanResult) { // check if scan result is result of Pdf417 recognizer
		                    resultsList.put(parsePdf417((Pdf417ScanResult)p));

		                } else if (p instanceof BarDecoderScanResult) { // check if scan result is result of BarDecoder recognizer	                    
		                   resultsList.put(parseBarDecoder((BarDecoderScanResult)p));

		                } else if (p instanceof ZXingScanResult) { // check if scan result is result of ZXing recognizer
		                   resultsList.put(parseZxing((ZXingScanResult)p));

		                } else if (p instanceof USDLScanResult) { // check if scan result is result of US Driver's Licence recognizer
		                   resultsList.put(parseUSDL((USDLScanResult)p));
		                }
	                } catch (Exception e) {
	                	Log.e(LOG_TAG, "Error parsing " + p.getClass().getName());
	                }
	            }
				
				try {
					JSONObject root = new JSONObject();
					root.put(RESULT_LIST, resultsList);				
					root.put(CANCELLED, false);
					this.callbackContext.success(root);
				} catch (JSONException e) {
					Log.e(LOG_TAG, "This should never happen");
				}

			} else if (resultCode == Pdf417ScanActivity.RESULT_CANCELED) {
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

	private JSONObject parsePdf417(Pdf417ScanResult p) throws JSONException {
        // getStringData getter will return the string version of barcode contents
        String barcodeData = p.getStringData();
        // getRawData getter will return the raw data information object of barcode contents
        BarcodeDetailedData rawData = p.getRawData();
        // BarcodeDetailedData contains information about barcode's binary layout, if you
        // are only interested in raw bytes, you can obtain them with getAllData getter
        byte[] rawDataBuffer = rawData.getAllData();

		JSONObject result = new JSONObject();
		result.put(RESULT_TYPE, "Barcode result");
		result.put(TYPE, "PDF417");
		result.put(DATA, barcodeData);
		result.put(RAW_DATA, byteArrayToHex(rawDataBuffer));
        return result;
	}

	private JSONObject parseBarDecoder(BarDecoderScanResult p) throws JSONException {
        // with getBarcodeType you can obtain barcode type enum that tells you the type of decoded barcode
        BarcodeType type = p.getBarcodeType();
        // as with PDF417, getStringData will return the string contents of barcode
        String barcodeData = p.getStringData();

		JSONObject result = new JSONObject();
		result.put(RESULT_TYPE, "Barcode result");
		result.put(TYPE, type.name());
		result.put(DATA, barcodeData);
        return result;
	}

	private JSONObject parseZxing(ZXingScanResult p) throws JSONException {
		// with getBarcodeType you can obtain barcode type enum that tells you the type of decoded barcode
        BarcodeType type = p.getBarcodeType();
        // as with PDF417, getStringData will return the string contents of barcode
        String barcodeData = p.getStringData();

		JSONObject result = new JSONObject();
		result.put(RESULT_TYPE, "Barcode result");
		result.put(TYPE, type.name());
		result.put(DATA, barcodeData);
	    return result;
	}

	private static final String RECOGNITIONDATA_TYPE = "PaymentDataType";
	private JSONObject parseUSDL(USDLScanResult p) throws JSONException {
		JSONObject fields = new JSONObject();

		Bundle bundle = p.getData();
		for (String key : bundle.keySet()) {
			// Originaly in RecognitionResultConstants.RECOGNITIONDATA_TYPE
			if (RECOGNITIONDATA_TYPE.equals(key)) {
				continue;
			}
			Object value = bundle.get(key);
			if (value instanceof String) {
				fields.put(key, (String)value);
			} else {
				Log.d(LOG_TAG, "Ignoring non string key '" + key + "'");
			}
		}

		JSONObject result = new JSONObject();
		result.put(RESULT_TYPE, "USDL result");
		result.put(FIELDS, fields);
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
