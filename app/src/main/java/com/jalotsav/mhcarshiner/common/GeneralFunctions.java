/*
 * Copyright (c) 2018 Jalotsav
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jalotsav.mhcarshiner.common;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Jalotsav on 5/22/2017.
 */

public class GeneralFunctions {

    /***
     * Check Internet Connection
     * Mobile device is connect with Internet or not?
     * ***/
    public static boolean isNetConnected(Context context){

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        //This for Wifi.
        NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetwork != null && wifiNetwork.isConnected())
            return true;

        //This for Mobile Network.
        NetworkInfo mobileNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mobileNetwork != null && mobileNetwork.isConnected())
            return true;

        //This for Return true else false for Current status.
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected())
            return true;

        return false;
    }

    /**
     *  Show/Cancel Toast
     **/
    private static Toast TOAST = null;

    public static void showToastSingle(Context context, String message, int toastLength) {

        if(TOAST != null) TOAST.cancel();
        switch (toastLength) {
            case Toast.LENGTH_SHORT:

                TOAST = Toast.makeText(context, message, Toast.LENGTH_SHORT);
                TOAST.show();
                break;
            case Toast.LENGTH_LONG:

                TOAST = Toast.makeText(context, message, Toast.LENGTH_LONG);
                TOAST.show();
                break;
            default:

                TOAST = Toast.makeText(context, message, Toast.LENGTH_SHORT);
                TOAST.show();
                break;
        }
    }

    /***
     * Open native call dialer with given number
     * ***/
    public static void openDialerToCall(Context context, String mobileNo) {

        try {
            context.startActivity(
                    new Intent(Intent.ACTION_DIAL).setData(Uri.parse("tel:" + mobileNo))
            );
        } catch (Exception e) {e.printStackTrace();}
    }

    /***
     * Check Email Address Format is valid or not
     * ***/
    public static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    /***
     * Check Mobile Format is valid or not
     * ***/
    public static boolean isValidMobile(String target) {

//        String mobile = "^\\(?([0-9]{3})\\)?[-.]?([0-9]{3})[-.]?([0-9]{4})$";
        String mobile = "^(\\+\\d{1,3}[- ]?)?\\d{10}$";
        Matcher matcherEmail = Pattern.compile(mobile).matcher(target);
        return matcherEmail.matches();
    }

    /***
     * Check Email Format is valid or not
     * ***/
    public static boolean isValidEmail(String str) {

        String reg_Email = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Matcher matcherEmail = Pattern.compile(reg_Email).matcher(str);
        return matcherEmail.matches();
    }

    /***
     * Enable all views
     * ***/
    public static void enableViews(View... views) {
        for (View v : views) {
            v.setEnabled(true);
        }
    }

    /***
     * Disable all views
     * ***/
    public static void disableViews(View... views) {
        for (View v : views) {
            v.setEnabled(false);
        }
    }

    /***
     * Get Current TimeStamp
     * ***/
    public static String getCurrentTimestamp() {

        return String.valueOf(System.currentTimeMillis() / 1000);
    }

    /***
     * Convert Timestamp to dd-MMM-yyyy Date format
     * ***/
    public static String getDateFromTimestamp(String timestamp) {

        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(Long.parseLong(timestamp) * 1000);
        return DateFormat.format("dd-MMM-yyyy", cal).toString();
    }

    /***
     * Convert Timestamp to dd-MMM-yyyy HH:mm Date format
     * ***/
    public static String getDateTimeFromTimestamp(String timestamp) {

        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(Long.parseLong(timestamp) * 1000);
        return DateFormat.format("dd-MMM-yyyy HH:mm a", cal).toString();
    }
}
