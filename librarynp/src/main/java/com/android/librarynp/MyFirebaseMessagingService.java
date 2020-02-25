package com.android.librarynp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


import java.util.Locale;
import java.util.Random;

public class MyFirebaseMessagingService extends FirebaseMessagingService  {


    private NotificationManager notificationManager;
    private static final String ADMIN_CHANNEL_ID ="admin_channel";
    public String body ;
    public String title ;
    public String icon;
    public String nameClass;
    public static String CLASS_TAG=MyFirebaseMessagingService.class.getSimpleName();
    public StorageController storageController;
    public LibraryMainController libraryMainController;
    public Activity activity;
    private static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 1;
    private static final int MY_PERMISSIONS_REQUEST_READ_PHONE_NUMBER = 2;
    private static final int MY_PERMISSIONS_REQUEST_READ_PHONE_SMS =3 ;

    @Override
    public void onNewToken(String s) {
        /*
            En este método recibimos el 'token' del dispositivo.
            Lo necesitamos si vamos a comunicarnos con el dispositivo directamente.
        */
        super.onNewToken(s);
        Log.e("NEW_TOKEN FOR SEND",s);
        /*
            A partir de aquí podemos hacer lo que queramos con el token como
            enviarlo al servidor para guardarlo en una B.DD.
            Nosotros no haremos nada con el token porque no nos vamos a comunicar con un sólo
            dispositivo.
         */

        SendTokenToBackend(s);
    }

    private void SendTokenToBackend(String s) {
        Log.e(CLASS_TAG,"SEND TOKEN TO BACKEND "+s);
        storageController=StorageController.getInstance(this);
        storageController.saveToken(s);
        createParameters();

    }

    private void createParameters() {
        String type="ANDROID";
        Log.e(CLASS_TAG,"create type "+type);
        Log.e(CLASS_TAG,"create model "+getDeviceName());
        String myVersion = android.os.Build.VERSION.RELEASE; // os
        Log.e(CLASS_TAG,"OS "+ myVersion);
        verifiPermission();

    }

    private void verifiPermission() {
        Log.e(CLASS_TAG,"verifiPermission");
        libraryMainController=LibraryMainController.getInstance(this);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            Log.e(CLASS_TAG,"verifiPermission 1");
            try {
                ActivityCompat.requestPermissions(libraryMainController.getActivity(),
                        new String[]{Manifest.permission.READ_PHONE_STATE},
                        MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
            }catch (NullPointerException e){
                Log.e(CLASS_TAG,"Dont opened yet");
            }

//        }else if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS)
//                != PackageManager.PERMISSION_GRANTED){
//            Log.e(CLASS_TAG,"verifiPermission 2");
//            ActivityCompat.requestPermissions(libraryMainController.getActivity(),
//                    new String[]{Manifest.permission.READ_SMS},
//                    MY_PERMISSIONS_REQUEST_READ_PHONE_SMS);
//
//        }else if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_NUMBERS)
//                != PackageManager.PERMISSION_GRANTED){
//            Log.e(CLASS_TAG,"verifiPermission 3");
//            ActivityCompat.requestPermissions(libraryMainController.getActivity(),
//                    new String[]{Manifest.permission.READ_PHONE_NUMBERS},
//                    MY_PERMISSIONS_REQUEST_READ_PHONE_NUMBER);
        }else{
            Log.e(CLASS_TAG,"ya tengo los permisos");
            verifiSdkVersion();

        }
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

    @SuppressLint("PrivateApi")
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // En este método recibimos el mensaje
        verifiPermission();
        libraryMainController=LibraryMainController.getInstance(this);

        nameClass=libraryMainController.getNameclass();
        Log.e(CLASS_TAG, "NOMBRE DE LA CLASE " + nameClass );

        try {
            Log.e(CLASS_TAG, "EST MENS " + remoteMessage.getData() );
            body = remoteMessage.getNotification().getBody();
            title = remoteMessage.getNotification().getTitle();
            icon = remoteMessage.getNotification().getIcon();

            Log.e(CLASS_TAG, "MENSAJE IN " + body);
            Log.e(CLASS_TAG, "TITULO IN " + title);
            Log.e(CLASS_TAG, "IMAGE IN " + title);

        }catch (NullPointerException e){
            Log.e(CLASS_TAG,"error "+e.getMessage());
            body = remoteMessage.getData().get("message");
            title = remoteMessage.getData().get("title");
            icon = remoteMessage.getData().get("image");
            Log.e(CLASS_TAG, "MENSAJE " + body);
            Log.e(CLASS_TAG, "TITULO " + title);
            Log.e(CLASS_TAG, "IMAGE " + icon);

        }
        Intent notificationIntent=null;

        try {
            notificationIntent = new Intent(this,Class.forName(nameClass));
            Log.e(CLASS_TAG, "PIRMERO " );
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Log.e(CLASS_TAG, "PIRMERO error " );
        }catch (NullPointerException e){
            e.printStackTrace();
            Log.e(CLASS_TAG, "PIRMERO error null " );
            notificationIntent = new Intent("android.intent.action.MAIN");
        }

        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        final PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_ONE_SHOT);
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // Configuramos la notificación para Android Oreo o superior
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            setupChannels();
        }
        int notificationId = new Random().nextInt(60000);
        // Creamos la notificación en si
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, ADMIN_CHANNEL_ID)
                .setSmallIcon(R.drawable.home)  //a resource for your custom small icon
                .setContentTitle(title) //the "title" value you sent in your notification
                .setContentText(body) //ditto
                .setAutoCancel(true)  //dismisses the notification on click
                .setContentIntent(pendingIntent)
                .setSound(defaultSoundUri);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationId /* ID of notification */, notificationBuilder.build());
    }

    private void verifiSdkVersion() {
        int sdkVersion = android.os.Build.VERSION.SDK_INT; // sdk version;
        libraryMainController.setSdkVersion(sdkVersion);
        Log.e(CLASS_TAG, "SDK VERSION "+sdkVersion );
        if(libraryMainController.getSdkVersion()==0||libraryMainController.getSdkVersion()!=sdkVersion){
            libraryMainController.setSdkVersion(sdkVersion);
            Log.e(CLASS_TAG, "SE ACTUALIZO LA VERSION DEL SDK " );

        }else{
            Log.e(CLASS_TAG, "no se actulizo el SDK Version " );
        }
        String lenguaje=Locale.getDefault().getDisplayLanguage();
        Log.e(CLASS_TAG, "DEVICE LENGUAJE "+lenguaje );

        if(libraryMainController.getLenguaje().equals("0")||!libraryMainController.getLenguaje().equals(lenguaje)){
            libraryMainController.setLenguaje(lenguaje);
            Log.e(CLASS_TAG, "SE ACTUALIZO EL LENGUAJE DEL DISPOSITIVO " );

        }else{
            Log.e(CLASS_TAG, "no se actulizo el Lenguaje " );
        }

        libraryMainController.getPhone();
        libraryMainController.getExternalId();


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setupChannels(){
        CharSequence adminChannelName = getString(R.string.notifications_admin_channel_name);
        String adminChannelDescription = getString(R.string.notifications_admin_channel_description);
        NotificationChannel adminChannel;
        adminChannel = new NotificationChannel(ADMIN_CHANNEL_ID, adminChannelName, NotificationManager.IMPORTANCE_LOW);
        adminChannel.setDescription(adminChannelDescription);
        adminChannel.enableLights(true);
        adminChannel.setLightColor(Color.RED);
        adminChannel.enableVibration(true);
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(adminChannel);
        }
    }





}
