/**
 * PhoneGap is available under *either* the terms of the modified BSD license *or* the
 * MIT License (2008). See http://opensource.org/licenses/alphabetical for full text.
 *
 * Copyright (c) Matt Kane 2010
 * Copyright (c) 2011, IBM Corporation
 * Copyright (c) 2013, Maciej Nux Jaros
 */
package com.phonegap.plugins.pdf417;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.util.Base64;
import android.util.Log;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import mobi.pdf417.Pdf417MobiSettings;
import mobi.pdf417.activity.Pdf417ScanActivity;
import net.photopay.barcode.BarcodeDetailedData;
import net.photopay.barcode.BarcodeElement;
import net.photopay.base.BaseBarcodeActivity;

public class Pdf417Scanner extends CordovaPlugin {
	
    private static final int REQUEST_CODE = 1337;

    private static final String SCAN = "scan";
    private static final String CANCELLED = "cancelled";
    
    private static final String TYPE = "type";
    private static final String DATA = "data";
    private static final String RAW_DATA = "raw_data";
    private static final String ELEMENTS = "elements";

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
     * This method is called from the WebView thread. To do a non-trivial amount of work, use:
     *     cordova.getThreadPool().execute(runnable);
     *
     * To run on the UI thread, use:
     *     cordova.getActivity().runOnUiThread(runnable);
     *
     * @param action          The action to execute.
     * @param args            The exec() arguments.
     * @param callbackContext The callback context used when calling back into JavaScript.
     * @return                Whether the action was valid.
     *
     * @sa https://github.com/apache/cordova-android/blob/master/framework/src/org/apache/cordova/CordovaPlugin.java
     */
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) {
        this.callbackContext = callbackContext;
        
        if (action.equals(SCAN)) {
        	cordova.getActivity().runOnUiThread(new Runnable() {
                public void run() {
                	scan();
                }
            });
        	            
        } else {
            return false;
        }
        return true;
    }

    /**
     * Starts an intent to scan and decode a barcode.
     */
    public void scan() {
		Context context = this.cordova.getActivity().getApplicationContext();
		
		Intent intent = new Intent(context, Pdf417ScanActivity.class);
		 
		Pdf417MobiSettings sett = new Pdf417MobiSettings();
		// set this to true to enable PDF417 scanning
        sett.setPdf417Enabled(true);
        // set this to true to enable QR code scanning
        sett.setQrCodeEnabled(true); 
        // set this to true to prevent showing dialog after successful scan
        sett.setDontShowDialog(false);
		// if license permits this, remove Pdf417.mobi logo overlay on scan activity
        // if license forbids this, this option has no effect
        sett.setRemoveOverlayEnabled(true);
        
        // put settings as intent extra
        intent.putExtra(BaseBarcodeActivity.EXTRAS_SETTINGS, sett);
     
        this.cordova.startActivityForResult((CordovaPlugin) this, intent, REQUEST_CODE);
    }

    /**
     * Called when the scanner intent completes.
     *
     * @param requestCode The request code originally supplied to startActivityForResult(),
     *                       allowing you to identify who this result came from.
     * @param resultCode  The integer result code returned by the child activity through its setResult().
     * @param intent      An Intent, which can return result data to the caller (various data can be attached to Intent "extras").
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if (requestCode == REQUEST_CODE) {
			
			if (resultCode == BaseBarcodeActivity.RESULT_OK) {
				Log.d(LOG_TAG, "Activity result OK");
				
				// read scanned barcode type (PDF417 or QR code)
				String barcodeType = data.getStringExtra(BaseBarcodeActivity.EXTRAS_BARCODE_TYPE);
				// read the data contained in barcode
				String barcodeData = data.getStringExtra(BaseBarcodeActivity.EXTRAS_RESULT);
				// read raw barcode data
				BarcodeDetailedData rawData = data.getParcelableExtra(BaseBarcodeActivity.EXTRAS_RAW_RESULT);			
				
				final JSONObject obj = new JSONObject();
				try {
					obj.put(TYPE, barcodeType);
					obj.put(DATA, barcodeData);
					
					if (rawData != null) {
						// Add raw data and elements if the exist
						if (rawData.getAllData() != null) {
							String rawDataBase64 = Base64.encodeToString(rawData.getAllData(), Base64.DEFAULT);
							obj.put(RAW_DATA, rawDataBase64);
						}
						if (rawData.getElements() != null) {
							JSONArray elements = new JSONArray();
							for (int i = 0; i < rawData.getElements().size(); i++) {
								BarcodeElement element = rawData.getElements().get(i);
							
								JSONObject jsonElement = new JSONObject();
								String rawDataBase64 = Base64.encodeToString(element.getElementBytes(), Base64.DEFAULT);
								jsonElement.put(RAW_DATA, rawDataBase64);
								jsonElement.put(TYPE, element.getElementType());
								elements.put(i, jsonElement);
							}
							obj.put(ELEMENTS, elements);
						}
					}
					
					obj.put(CANCELLED, false);
					
				} catch (JSONException e) {
					Log.d(LOG_TAG, "This should never happen");
				}
				
				cordova.getActivity().runOnUiThread(new Runnable() {
		            public void run() {
		            	callbackContext.success(obj);
		            }
		        });
					
			} else if (resultCode == BaseBarcodeActivity.RESULT_CANCELED) {
				final JSONObject obj = new JSONObject();
                try {
                    obj.put(CANCELLED, true);
                    
                } catch (JSONException e) {
                    Log.d(LOG_TAG, "This should never happen");
                }
                
                cordova.getActivity().runOnUiThread(new Runnable() {
		            public void run() {
		            	callbackContext.success(obj);
		            }
		        });
                
			} else {
				this.callbackContext.error("Unexpected error");
			}
		}
    }
}
