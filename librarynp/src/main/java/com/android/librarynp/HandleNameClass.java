package com.android.librarynp;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessaging;

public class HandleNameClass {

    public static String CLASS_TAG=HandleNameClass.class.getSimpleName();

    public static String NameClass;



    public static String getNameClass() {
        Log.e(CLASS_TAG," get Nombre de la clase ");
        return NameClass;
    }

    public static void setNameClass(String nameClass) {
        NameClass = nameClass;
        Log.e(CLASS_TAG,"Nombre de la clase "+NameClass);
    }

    public static void setFirebaseTopic(){
        Log.e(CLASS_TAG,"subscribeToTopic");
        FirebaseMessaging.getInstance().subscribeToTopic("topic_general");
    }




}
