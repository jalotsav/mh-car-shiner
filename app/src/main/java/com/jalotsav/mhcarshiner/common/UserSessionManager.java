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
import android.content.SharedPreferences;

import com.jalotsav.mhcarshiner.ActvtySignIn;

public class UserSessionManager implements AppConstants {

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context mContext;

    // All Shared Preferences Keys
    // Is User SignIn or not
    private static final String IS_USER_LOGIN = "isUserLoggedIn";

    // Constructor
    public UserSessionManager(Context context) {

        this.mContext = context;
        String PREFER_NAME = mContext.getPackageName() + "_shrdprfrnc";
        int PRIVATE_MODE = 0;
        pref = mContext.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        editor = pref.edit();
        editor.apply();
    }

    // Create login session
    // Get-Set UserId to SharedPreferences
    public String getUserId() {

        return pref.getString(CHILD_UID, "");
    }

    public void setUserId(String userId) {

        // Storing login value as TRUE
        editor.putBoolean(IS_USER_LOGIN, true);
        editor.putString(CHILD_UID, userId);
        editor.commit();
    }

    // Get-Set FirstName to SharedPreferences
    public String getFirstName() {

        return pref.getString(CHILD_FIRST_NAME, "");
    }

    public void setFirstName(String firstName) {

        editor.putString(CHILD_FIRST_NAME, firstName);
        editor.commit();
    }

    // Get-Set LastName to SharedPreferences
    public String getLastName() {

        return pref.getString(CHILD_LAST_NAME, "");
    }

    public void setLastName(String lastName) {

        editor.putString(CHILD_LAST_NAME, lastName);
        editor.commit();
    }

    // Get-Set Email to SharedPreferences
    public String getMobile() {

        return pref.getString(CHILD_MOBILE, "");
    }

    public void setMobile(String mobile) {

        editor.putString(CHILD_MOBILE, mobile);
        editor.commit();
    }

    // Get-Set Email to SharedPreferences
    public String getEmail() {

        return pref.getString(CHILD_EMAIL, "");
    }

    public void setEmail(String email) {

        editor.putString(CHILD_EMAIL, email);
        editor.commit();
    }

    /**
     * Check login method will check user login status
     * If false it will redirect user to login page
     * Else do anything
     */
    public boolean checkLogin() {

        if (!this.isUserLoggedIn()) {

            Intent i = new Intent(mContext, ActvtySignIn.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            mContext.startActivity(i);
//            ((Activity)_context).overridePendingTransition(0,0);

            return false;
        }
        return true;
    }

    /**
     * Clear session details
     */
    public void logoutUser() {

        // Clearing all user data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to SignIn Activity
        Intent i = new Intent(mContext, ActvtySignIn.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        mContext.startActivity(i);
    }

    // Check for login
    private boolean isUserLoggedIn() {
        return pref.getBoolean(IS_USER_LOGIN, false);
    }
}
