package com.android.librarynp;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessaging;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static android.content.ContentValues.TAG;

public class LibraryMainController {

    private static final String CLASS_TAG = LibraryMainController.class.getSimpleName();
    private static LibraryMainController mInstance;
    private static Context contexto;
    public String Nameclass;
    public int sdkVersion;
    public String lenguaje;

    public String externalId;
    public String email;
    public String phone;
    public Activity activity;

    public LibraryMainController(Context context){
        contexto=context;
        this.sdkVersion=0;
        this.lenguaje="0";

    }

    public static synchronized LibraryMainController getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new LibraryMainController(context);
        }
        contexto = context;
        return mInstance;
    }

    public static void reset() {
        mInstance = null;
    }

    public String getNameclass() {
        return Nameclass;
    }

    public void setNameclass(String nameclass) {
        Nameclass = nameclass;
    }

    public void setFirebaseTopic(){
        Log.e(CLASS_TAG,"subscribeToTopic");
        FirebaseMessaging.getInstance().subscribeToTopic("topic_general");
    }
    public int getSdkVersion() {
        return sdkVersion;
    }

    public void setSdkVersion(int sdkVersion) {
        Log.e(CLASS_TAG,"SET SDK VERSION");
        this.sdkVersion = sdkVersion;
    }

    public String getLenguaje() {
        return lenguaje;
    }

    public void setLenguaje(String lenguaje) {
        this.lenguaje = lenguaje;
    }

    @SuppressLint("MissingPermission")
    public String getExternalId() {
        TelephonyManager tMgr = (TelephonyManager)contexto
                .getSystemService(Context.TELEPHONY_SERVICE);

        externalId=tMgr.getDeviceId();
        Log.e(CLASS_TAG," SEND IMEI "+ externalId);
        return externalId;
    }

    public String getEmail() {
        return email;
    }

    @SuppressLint("MissingPermission")
    public String getPhone() {
        TelephonyManager tMgr = (TelephonyManager)contexto
                .getSystemService(Context.TELEPHONY_SERVICE);

        phone=tMgr.getLine1Number();
        Log.e(CLASS_TAG,"SEND PHONE NUMBER "+ phone);
        return phone;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public String getConfigValue(Context context, String name) {
        Resources resources = context.getResources();

        try {
            InputStream rawResource = resources.openRawResource(R.raw.config);
            Properties properties = new Properties();
            properties.load(rawResource);
            return properties.getProperty(name);
        } catch (Resources.NotFoundException e) {
            Log.e(CLASS_TAG, "Unable to find the config file: " + e.getMessage());
        } catch (IOException e) {
            Log.e(CLASS_TAG, "Failed to open config file.");
        }

        return null;
    }

    public  String getMetaData(Context context, String name) {
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            return bundle.getString(name);
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "Unable to load meta-data: " + e.getMessage());
        }
        return null;
    }

}
