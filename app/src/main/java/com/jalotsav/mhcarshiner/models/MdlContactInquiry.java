package com.jalotsav.mhcarshiner.models;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Jalotsav on 9/5/2018.
 */

@IgnoreExtraProperties
public class MdlContactInquiry {

    private String id, uId, userMobile, subject, message, createdOn, createdBy, modifiedOn, modifiedBy;

    public MdlContactInquiry() {
    }

    public MdlContactInquiry(String id, String uId, String userMobile, String subject, String message, String createdOn, String createdBy, String modifiedOn, String modifiedBy) {
        this.id = id;
        this.uId = uId;
        this.userMobile = userMobile;
        this.subject = subject;
        this.message = message;
        this.createdOn = createdOn;
        this.createdBy = createdBy;
        this.modifiedOn = modifiedOn;
        this.modifiedBy = modifiedBy;
    }

    public String getId() {
        return id;
    }

    public String getuId() {
        return uId;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public String getSubject() {
        return subject;
    }

    public String getMessage() {
        return message;
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
}
