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

/**
 * Created by Jalotsav on 5/22/2017.
 */

public interface AppConstants {

    // Build Type
    String DEBUG_BUILD_TYPE = "debug";

    // Log Type
    int LOGTYPE_VERBOSE = 1;
    int LOGTYPE_DEBUG = 2;
    int LOGTYPE_INFO = 3;
    int LOGTYPE_WARN = 4;
    int LOGTYPE_ERROR = 5;

    // FireDB Object
    String ROOT_NAME = "mhcarshinerApp";
    String CHILD_USERS = "users";
    String CHILD_UID = "uid";
    String CHILD_FIRST_NAME = "firstname";
    String CHILD_LAST_NAME = "lastname";
    String CHILD_EMAIL = "email";
    String CHILD_MOBILE = "mobile";
    String CHILD_PASSPHRASE = "passphrase";
    String CHILD_ISACTIVE = "isActive";

    // PutExtra Keys
    String PUT_EXTRA_COME_FROM = "comeFrom";

    // Navigation Drawer MenuItem position check for direct open that fragment
    int NAVDRWER_DASHBOARD = 21;

    // Request Keys
    int REQUEST_VERFCTN_MOBILENO = 101;
    int REQUEST_APP_PERMISSION = 102;
}
