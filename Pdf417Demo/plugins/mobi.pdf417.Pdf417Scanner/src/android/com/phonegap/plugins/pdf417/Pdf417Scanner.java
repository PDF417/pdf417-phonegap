/**
 * PhoneGap is available under *either* the terms of the modified BSD license *or* the
 * MIT License (2008). See http://opensource.org/licenses/alphabetical for full text.
 *
 * Copyright (c) Matt Kane 2010
 * Copyright (c) 2011, IBM Corporation
 * Copyright (c) 2013, Maciej Nux Jaros
 */
package com.phonegap.plugins.pdf417;

import java.util.HashSet;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import mobi.pdf417.Pdf417MobiSettings;
import mobi.pdf417.activity.Pdf417ScanActivity;
import net.photopay.barcode.BarcodeDetailedData;
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

        /*if (action.equals(ENCODE)) {
            JSONObject obj = args.optJSONObject(0);
            if (obj != null) {
                String type = obj.optString(TYPE);
                String data = obj.optString(DATA);

                // If the type is null then force the type to text
                if (type == null) {
                    type = TEXT_TYPE;
                }

                if (data == null) {
                    callbackContext.error("User did not specify data to encode");
                    return true;
                }

                encode(type, data);
            } else {
                callbackContext.error("User did not specify data to encode");
                return true;
            }
        } */
        
        if (action.equals(SCAN)) {
            Set<String> types = new HashSet<String>();
            
            JSONArray typesArg = args.optJSONArray(0);
            for (int i = 0; i < typesArg.length(); ++i) {
                types.add(typesArg.optString(i));
            }
            
            Boolean beep = true;
            
            if (!args.isNull(1)) {
                beep = args.optBoolean(1);
            }
            
            scan(types, beep);
        } else {
            return false;
        }
        return true;
    }

    /**
     * Starts an intent to scan and decode a barcode.
     */
    public void scan(Set<String> types, Boolean beep) {
        Context context = this.cordova.getActivity().getApplicationContext();
        
        Intent intent = new Intent(context, Pdf417ScanActivity.class);
         
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
            
                // read scanned barcode type (PDF417 or QR code)
                String barcodeType = data.getStringExtra(BaseBarcodeActivity.EXTRAS_BARCODE_TYPE);
                // read the data contained in barcode
                String barcodeData = data.getStringExtra(BaseBarcodeActivity.EXTRAS_RESULT);
                // read raw barcode data
                BarcodeDetailedData rawData = data.getParcelableExtra(BaseBarcodeActivity.EXTRAS_RAW_RESULT);               
                    
                JSONObject obj = new JSONObject();
                try {
                    obj.put(TYPE, barcodeType);
                    obj.put(DATA, barcodeData);
                    //obj.put(RAW_DATA, XXXXX);
                    //obj.put(RAW_DATA, XXXXX);
                    
                    obj.put(CANCELLED, false);
                    
                } catch (JSONException e) {
                    Log.d(LOG_TAG, "This should never happen");
                }
                //this.success(new PluginResult(PluginResult.Status.OK, obj), this.callback);
                this.callbackContext.success(obj);
                    
            } else if (resultCode == BaseBarcodeActivity.RESULT_CANCELED) {
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
}
