package com.android.supermercadocada;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.librarynp.LibraryMainController;
import com.android.librarynp.StorageController;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback{

    public static String CLASS_TAG = MainActivity.class.getSimpleName();
    public StorageController storageController;
    public LibraryMainController libraryMainController;
    private static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 1;
    private static final int MY_PERMISSIONS_REQUEST_READ_PHONE_NUMBER = 2;
    private static final int MY_PERMISSIONS_REQUEST_READ_PHONE_SMS = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(CLASS_TAG, "on start MainActivity ");
        setContentView(R.layout.activity_main);
        storageController = StorageController.getInstance(this);
        libraryMainController = LibraryMainController.getInstance(this);
        libraryMainController.setActivity(MainActivity.this);

        String apiUrl=libraryMainController.getConfigValue(this,"api_url");
        String apiKey=libraryMainController.getConfigValue(this,"api_key");
        String apiTokenAuth=libraryMainController.getConfigValue(this,"api_token_auth");

        Log.e(CLASS_TAG, "get data config "+apiUrl);
        Log.e(CLASS_TAG, "get data config "+apiKey);

        String apiUrl1 = libraryMainController.getMetaData(this, "api_url");
        String apiKey1 = libraryMainController.getMetaData(this, "api_key");

        Log.e(CLASS_TAG, "get metadata config "+apiUrl1);
        Log.e(CLASS_TAG, "get metadata config "+apiKey1);


//        TelephonyManager tMgr = (TelephonyManager)getApplicationContext()
//                .getSystemService(Context.TELEPHONY_SERVICE);
//        @SuppressLint({"HardwareIds", "MissingPermission"})
//        String mPhoneNumber = tMgr.getLine1Number();
//        Log.e(CLASS_TAG,"my number is "+mPhoneNumber);
//        Log.e(CLASS_TAG,"Lenguaje "+ Locale.getDefault().getDisplayLanguage());
//        Log.e(CLASS_TAG,"model "+ getDeviceName());
//
//        String myVersion = android.os.Build.VERSION.RELEASE; // os
//        int sdkVersion = android.os.Build.VERSION.SDK_INT; // sdk version;
//
//        Log.e(CLASS_TAG,"OS "+ myVersion);
//        Log.e(CLASS_TAG,"SDKVERSION "+ sdkVersion);
//
//        @SuppressLint("MissingPermission") String imsi = tMgr.getSubscriberId();
//        @SuppressLint("MissingPermission") String imei = tMgr.getDeviceId();
//
//        Log.e(CLASS_TAG,"IMSI "+ imsi);
//        Log.e(CLASS_TAG,"IMEI "+ imei);


    }

    public String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.toLowerCase().startsWith(manufacturer.toLowerCase())) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }


    private String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.e(CLASS_TAG, "on start MainActivity ");
        if (storageController.hasToken()) {
            String token = storageController.getToken();
            Log.e(CLASS_TAG, "the last token saved was " + token);
        }

    }

    @Override
    protected void onStop() {
        super.onStop();

        Log.e(CLASS_TAG, "on Stop MainActivity ");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.e(CLASS_TAG,"permission result");
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_PHONE_STATE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Log.e(CLASS_TAG,"permission 1 was granted, yay!");
                    //verifiSdkVersion();
                    libraryMainController.getExternalId();
                    libraryMainController.getPhone();

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Log.e(CLASS_TAG,"permission denied, boo!");
                }

            }
//            case MY_PERMISSIONS_REQUEST_READ_PHONE_SMS:{
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    // permission was granted, yay! Do the
//                    // contacts-related task you need to do.
//                    Log.e(CLASS_TAG,"permission 2 was granted, yay!");
//                    libraryMainController.getExternalId();
//                    libraryMainController.getPhone();
//                    //verifiSdkVersion();
//                } else {
//                    // permission denied, boo! Disable the
//                    // functionality that depends on this permission.
//                    Log.e(CLASS_TAG,"permission denied, boo!");
//                }
//
//            }

//            case MY_PERMISSIONS_REQUEST_READ_PHONE_NUMBER:{
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    // permission was granted, yay! Do the
//                    // contacts-related task you need to do.
//                    Log.e(CLASS_TAG,"permission 3 was granted, yay!");
//                    //verifiSdkVersion();
//                    libraryMainController.getExternalId();
//                    libraryMainController.getPhone();
//                } else {
//                    // permission denied, boo! Disable the
//                    // functionality that depends on this permission.
//                    Log.e(CLASS_TAG,"permission denied, boo!");
//                }
//
//            }            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

}
