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
    String BUILD_TYPE_DEBUG = "debug";

    // FireDB Object
    String ROOT_NAME = "mhcarshinerApp";
    String CHILD_USERS = "users";
    String CHILD_UID = "uid";
    String CHILD_FIRST_NAME = "firstname";
    String CHILD_LAST_NAME = "lastname";
    String CHILD_EMAIL = "email";
    String CHILD_MOBILE = "mobile";
    String CHILD_WASH_PACKAGES = "washPackages";
    String CHILD_VEHICLE_TYPES = "vehicleTypes";
    String CHILD_WASH_TIMING = "washTiming";
    String CHILD_ORDERS = "orders";
    String CHILD_VEHICLE_DETAILS = "vehicleDetails";
    String CHILD_ADDRESS_DETAILS = "addressDetails";
    String CHILD_WASH_PACKAGES_DETAILS = "washPackagesDetails"; // used under ORDER child
    String CHILD_USER_MOBILE = "userMobile";

    // Order Status
    String ORDER_STATUS_PENDING = "Pending";
    String ORDER_STATUS_APPROVED = "Approved";
    String ORDER_STATUS_REJECTED = "Rejected";
    String ORDER_STATUS_COMPLETED = "Completed";

    // Wash Package
    String WASH_PACKAGE_SILVER = "Silver";
    String WASH_PACKAGE_GOLD = "Gold";
    String WASH_PACKAGE_DIAMOND = "Diamond";

    // PutExtra Keys
    String PUT_EXTRA_COME_FROM = "comeFrom";
    String PUT_EXTRA_NVGTNVW_POSTN = "nvgtnvwPosition";
    String PUT_EXTRA_POSITION = "position";
    String PUT_EXTRA_WASHPACKAGE_ID = "washPackageId";
    String PUT_EXTRA_WASHPACKAGE_NAME = "washPackageName";
    String PUT_EXTRA_WASHPACKAGE_MONTHLYWASH = "washPackageMonthlyWash";
    String PUT_EXTRA_WASHPACKAGE_PRICE = "washPackagePrice";
    String PUT_EXTRA_ORDER_NUMBER = "orderNumber";
    String PUT_EXTRA_ORDER_BOOKED_DATE = "orderBookedDate";

    // NavigationView Drawer MenuItem position check for direct open that fragment
    int NVGTNVW_HOME = 21;
    int NVGTNVW_MYORDER = 22;
    int NVGTNVW_CONTACTUS = 23;

    // Request Keys
    int REQUEST_VERFCTN_MOBILENO = 101;
    int REQUEST_APP_PERMISSION = 102;

    // Others
    String DEVICE_TYPE_ANDROID = "Android";
}
