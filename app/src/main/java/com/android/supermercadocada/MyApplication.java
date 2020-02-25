package com.android.supermercadocada;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.librarynp.LibraryMainController;

public class MyApplication extends Application {
    public static String CLASS_TAG=MyApplication.class.getSimpleName();
    public LibraryMainController mainController;
    private static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 1;
    private static final int MY_PERMISSIONS_REQUEST_READ_PHONE_NUMBER = 2;
    private static final int MY_PERMISSIONS_REQUEST_READ_PHONE_SMS =3 ;

    @Override
    public void onCreate() {
        super.onCreate();
        mainController=LibraryMainController.getInstance(this);
        Log.e(CLASS_TAG,"name of Aplication "
                +getPackageName()+"."+MainActivity.class.getSimpleName());
        String name=getPackageName()+"."+MainActivity.class.getSimpleName();
        mainController.setNameclass(name);
        mainController.setFirebaseTopic();
        //mainController.getEmail();
    }

}

