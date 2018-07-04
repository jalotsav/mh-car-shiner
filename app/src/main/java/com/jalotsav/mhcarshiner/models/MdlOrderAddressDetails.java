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

package com.jalotsav.mhcarshiner.models;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Jalotsav on 7/3/2018.
 */

@IgnoreExtraProperties
public class MdlOrderAddressDetails {

    private String flatNo, streetArea, pincode, city;

    public MdlOrderAddressDetails() {
    }

    public MdlOrderAddressDetails(String flatNo, String streetArea, String pincode, String city) {
        this.flatNo = flatNo;
        this.streetArea = streetArea;
        this.pincode = pincode;
        this.city = city;
    }

    public String getFlatNo() {
        return flatNo;
    }

    public String getStreetArea() {
        return streetArea;
    }

    public String getPincode() {
        return pincode;
    }

    public String getCity() {
        return city;
    }
}
