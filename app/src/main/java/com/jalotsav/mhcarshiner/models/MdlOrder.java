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
public class MdlOrder {

    private String id, uId, userMobile, status, createdOn, createdBy, modifiedOn, modifiedBy;
    private int orderNumber;

    public MdlOrder() {
    }

    public MdlOrder(String id, int orderNumber, String uId, String userMobile, String status, String createdOn, String createdBy, String modifiedOn, String modifiedBy) {
        this.id = id;
        this.orderNumber = orderNumber;
        this.uId = uId;
        this.userMobile = userMobile;
        this.status = status;
        this.createdOn = createdOn;
        this.createdBy = createdBy;
        this.modifiedOn = modifiedOn;
        this.modifiedBy = modifiedBy;
    }

    public String getId() {
        return id;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public String getuId() {
        return uId;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public String getStatus() {
        return status;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getModifiedOn() {
        return modifiedOn;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setId(String id) {
        this.id = id;
    }
}
