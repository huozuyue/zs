package com.hzy.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by hzy on 2018/7/15.
 */

public class checkPemission {

    public static void checkPemissionGrant(Context context, Activity activity) {
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission
                .ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission
                    .ACCESS_FINE_LOCATION}, 1);
        }
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.GET_ACCOUNTS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission
                    .GET_ACCOUNTS}, 1);
        }
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission
                    .READ_CONTACTS}, 1);
        }
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.INTERNET) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission
                    .INTERNET}, 1);
        }
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission
                .WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission
                    .WRITE_EXTERNAL_STORAGE}, 1);
        }
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission
                .ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission
                    .ACCESS_NETWORK_STATE}, 1);
        }
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission
                .ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission
                    .ACCESS_WIFI_STATE}, 1);
        }

        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission
                .READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission
                    .READ_PHONE_STATE}, 1);
        }
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission
                .ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission
                    .ACCESS_COARSE_LOCATION}, 1);
        }
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission
                .CHANGE_WIFI_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission
                    .CHANGE_WIFI_STATE}, 1);
        }
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission
                .ACCESS_LOCATION_EXTRA_COMMANDS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission
                    .ACCESS_LOCATION_EXTRA_COMMANDS}, 1);
        }


        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.BLUETOOTH) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission
                    .BLUETOOTH}, 1);
        }
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission
                .BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission
                    .BLUETOOTH_ADMIN}, 1);
        }


    }


}
