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
 * Created by Jalotsav on 5/24/2018.
 */

@IgnoreExtraProperties
public class MdlUsers {

    private String uId, firstname, lastname, email, mobile, passphrase, deviceType;
    private boolean active;

    public MdlUsers() {
    }

    public MdlUsers(String uId, String firstname, String lastname, String email, String mobile, String passphrase, String deviceType, boolean active) {
        this.uId = uId;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.mobile = mobile;
        this.passphrase = passphrase;
        this.deviceType = deviceType;
        this.active = active;
    }

    public String getuId() {
        return uId;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getEmail() {
        return email;
    }

    public String getMobile() {
        return mobile;
    }

    public String getPassphrase() {
        return passphrase;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public boolean isActive() {
        return active;
    }
}
