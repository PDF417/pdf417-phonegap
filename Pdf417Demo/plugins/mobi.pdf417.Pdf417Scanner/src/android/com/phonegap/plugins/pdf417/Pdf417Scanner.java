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

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import mobi.pdf417.Pdf417MobiSettings;
import mobi.pdf417.Pdf417MobiScanData;
import mobi.pdf417.activity.Pdf417ScanActivity;
import net.photopay.barcode.BarcodeDetailedData;
import net.photopay.hardware.camera.CameraType;

public class Pdf417Scanner extends CordovaPlugin {

	private static final int REQUEST_CODE = 1337;

	private static final String SCAN = "scan";
	private static final String CANCELLED = "cancelled";

	private static final String RESULT_LIST = "resultList";
	private static final String TYPE = "type";
	private static final String DATA = "data";
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
			Boolean beep = true, noDialog = false, removeOverlay = false, uncertain = false, quietZone = false, highRes = false, frontFace = false;
			String license = null;

			if (!args.isNull(1)) {
				JSONObject options = args.optJSONObject(1);

				if (!options.isNull("beep")) {
					beep = options.optBoolean("beep");
				}
				if (!options.isNull("noDialog")) {
					noDialog = options.optBoolean("noDialog");
				}
				if (!options.isNull("removeOverlay")) {
					removeOverlay = options.optBoolean("removeOverlay");
				}
				if (!options.isNull("uncertain")) {
					uncertain = options.optBoolean("uncertain");
				}
				if (!options.isNull("quietZone")) {
					quietZone = options.optBoolean("quietZone");
				}
				if (!options.isNull("highRes")) {
					highRes = options.optBoolean("highRes");
				}
				if (!options.isNull("frontFace")) {
					frontFace = options.optBoolean("frontFace");
				}
				if (!options.isNull("customUI")) {
					customUI = options.optBoolean("customUI");
				}
			}

			if (!args.isNull(3)) {
				license = args.optString(3);
			}

			if (customUI) {
				scanCustomUI(types, beep, noDialog, removeOverlay, uncertain, quietZone, highRes, frontFace, license);	
			} else {
				scan(types, beep, noDialog, removeOverlay, uncertain, quietZone, highRes, frontFace, license);
			}
		} else {
			return false;
		}
		return true;
	}

	/**
	 * Starts an intent to scan and decode a barcode.
	 */
	public void scan(Set<String> types, Boolean beep, Boolean noDialog, Boolean removeOverlay, Boolean uncertain, Boolean quietZone, Boolean highRes, Boolean frontFace, String license) {
		scan(Pdf417ScanActivity.class, types, beep, noDialog, removeOverlay, uncertain, quietZone, highRes, frontFace, license);
	}

	/**
	 * Starts an intent to scan and decode a barcode with custom UI Activity
	 */
	public void scanCustomUI(Set<String> types, Boolean beep, Boolean noDialog, Boolean removeOverlay, Boolean uncertain, Boolean quietZone, Boolean highRes, Boolean frontFace, String license) {
		//scan(CustomScanActivity.class, types, beep, noDialog, removeOverlay, uncertain, quietZone, highRes, frontFace, license);
		// Create your own custom UI. See examples for Android at: https://github.com/PDF417/pdf417-android
	}

	/**
	 * Starts an intent from provided class to scan and decode a barcode.
	 */
	public void scan(Class clazz, Set<String> types, Boolean beep, Boolean noDialog, Boolean removeOverlay, Boolean uncertain, Boolean quietZone, Boolean highRes, Boolean frontFace, String license) {

		Context context = this.cordova.getActivity().getApplicationContext();
		FakeR fakeR = new FakeR(this.cordova.getActivity());

		Intent intent = new Intent(context, clazz);

		Pdf417MobiSettings sett = new Pdf417MobiSettings();

		sett.setPdf417Enabled(types.contains("PDF417"));
		sett.setQrCodeEnabled(types.contains("QR Code"));
		sett.setCode128Enabled(types.contains("Code 128"));
		sett.setCode39Enabled(types.contains("Code 39"));
		sett.setEan13Enabled(types.contains("EAN 13"));
		sett.setEan8Enabled(types.contains("EAN 8"));
		sett.setItfEnabled(types.contains("ITF"));
		sett.setUpcaEnabled(types.contains("UPCA"));
		sett.setUpceEnabled(types.contains("UPCE"));

		// set this to true to prevent showing dialog after successful scan
		sett.setDontShowDialog(noDialog);
		// if license permits this, remove Pdf417.mobi logo overlay on scan
		// activity
		// if license forbids this, this option has no effect
		sett.setRemoveOverlayEnabled(removeOverlay);

		// Set this to true to scan barcodes which don't have quiet zone (white area) around it
	    // Use only if necessary because it drastically slows down the recognition process 
		sett.setNullQuietZoneAllowed(quietZone);

		// Set this to true to scan even barcode not compliant with standards
	    // For example, malformed PDF417 barcodes which were incorrectly encoded
	    // Use only if necessary because it slows down the recognition process
		sett.setUncertainScanning(uncertain);

		// If you want sound to be played after the scanning process ends, 
		// put here the resource ID of your sound file. (optional)
		if (beep == true) {
			intent.putExtra(Pdf417ScanActivity.EXTRAS_BEEP_RESOURCE, fakeR.getId("raw", "beep_pdf417"));
		}

		// set EXTRAS_ALWAYS_USE_HIGH_RES to true if you want to always use highest 
		// possible camera resolution (enabled by default for all devices that support
		// at least 720p camera preview frame size)
		if (highRes == true) {
			intent.putExtra(Pdf417ScanActivity.EXTRAS_ALWAYS_USE_HIGH_RES, highRes);
		}

		// set EXTRAS_CAMERA_TYPE to use front facing camera
		// Note that front facing cameras do not have autofocus support, so it will not
		// be possible to scan denser and smaller codes.
		if (frontFace == true) {
			intent.putExtra(Pdf417ScanActivity.EXTRAS_CAMERA_TYPE, (Parcelable)CameraType.CAMERA_FRONTFACE);
		}

		// set the license key (for commercial versions only) - obtain your key at
		// http://pdf417.mobi
		if (license != null) {
			intent.putExtra(Pdf417ScanActivity.EXTRAS_LICENSE_KEY, license);
		}

		// put settings as intent extra
		intent.putExtra(Pdf417ScanActivity.EXTRAS_SETTINGS, sett);

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

				// read scan results
				ArrayList<Pdf417MobiScanData> scanDataList = data.getParcelableArrayListExtra(Pdf417ScanActivity.EXTRAS_RESULT_LIST);

				try {	
					
					JSONObject root = new JSONObject();
					
					// First element in object root for backwards compatibility
					setScanData(scanDataList.get(0), root);
					
					// List of all results in a separate element
					JSONArray resultsList = new JSONArray();
					for (Pdf417MobiScanData scanData : scanDataList) {
						JSONObject elem = new JSONObject();
						setScanData(scanData, elem);
						resultsList.put(elem);
					}
					root.put(RESULT_LIST, resultsList);
					
					root.put(CANCELLED, false);
					this.callbackContext.success(root);
					
				} catch (JSONException e) {
					Log.d(LOG_TAG, "This should never happen");
				}			

			} else if (resultCode == Pdf417ScanActivity.RESULT_CANCELED) {
				JSONObject obj = new JSONObject();
				try {
					obj.put(CANCELLED, true);

				} catch (JSONException e) {
					Log.d(LOG_TAG, "This should never happen");
				}
				this.callbackContext.success(obj);

			} else {
				this.callbackContext.error("Unexpected error");
			}
		}
	}
	
	private void setScanData(Pdf417MobiScanData scanData, JSONObject obj) throws JSONException {
		// read scanned barcode type (PDF417 or QR code)
		String barcodeType = scanData.getBarcodeType();

		// read the data contained in barcode
		String barcodeData = scanData.getBarcodeData();

		// read raw barcode data
		BarcodeDetailedData rawData = scanData.getBarcodeRawData();
		
		obj.put(TYPE, barcodeType);
		obj.put(DATA, barcodeData);
		obj.put(RAW_DATA, byteArrayToHex(rawData.getAllData()));
	}

	private String byteArrayToHex(byte[] data) {
		StringBuilder sb = new StringBuilder();
		for (byte b : data) {
			sb.append(String.format("%02x", b));
		}
		return sb.toString();
	}
}
