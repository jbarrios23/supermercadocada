package com.android.librarynp;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class StorageController {

    private static final String CLASS_TAG = StorageController.class.getName();

    private static StorageController mInstance;
    private static Context contexto;
    private SharedPreferences mSharedPreferences;

    public StorageController(Context context){

        this.contexto=context;
        this.mSharedPreferences = contexto.getApplicationContext().getSharedPreferences("StorageCallback", 0);

    }

    public static synchronized StorageController getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new StorageController(context);
        }
        contexto = context;
        return mInstance;
    }

    public static void reset() {
        mInstance = null;
    }

    public void saveToken(String token){

        SharedPreferences.Editor datosuser=mSharedPreferences.edit();
        datosuser.putString("Token",token);
        datosuser.apply();
        Log.d(CLASS_TAG,"TOKEN SAVED");

    }

    public boolean hasToken(){
        boolean hasToken = false;
        String token=mSharedPreferences.getString("Token","");
        if(token.length()>0&&!token.equals("")){
            hasToken=true;

        }
        Log.d("has token",""+hasToken);
        return hasToken;
    }

    public String getToken(){

        String token=mSharedPreferences.getString("Token","");
        Log.d("getToken",""+token);
        return token;
    }

    public void deleteToken(){
        mSharedPreferences=contexto.getSharedPreferences("StorageCallback", 0);
        SharedPreferences.Editor editorlogin = mSharedPreferences.edit();
        editorlogin.clear();
        editorlogin.commit();
    }

    public void saveAuthToken(String token){

        SharedPreferences.Editor datosuser=mSharedPreferences.edit();
        datosuser.putString("AuthToken",token);
        datosuser.apply();
        Log.e(CLASS_TAG,"AUTH TOKEN SAVED");

    }

    public boolean hasAuthToken(){
        boolean hasToken = false;
        String token=mSharedPreferences.getString("AuthToken","");
        if(token.length()>0&&!token.equals("")){
            hasToken=true;

        }
        Log.d("has AuthToken",""+hasToken);
        return hasToken;
    }

    public String getAuthToken(){
        String token=mSharedPreferences.getString("AuthToken","");
        Log.d("getAuthToken",""+token);
        return token;
    }

    public void deleteAuthToken(){
        mSharedPreferences=contexto.getSharedPreferences("StorageCallback", 0);
        SharedPreferences.Editor editorlogin = mSharedPreferences.edit();
        editorlogin.clear();
        editorlogin.commit();
    }

}
