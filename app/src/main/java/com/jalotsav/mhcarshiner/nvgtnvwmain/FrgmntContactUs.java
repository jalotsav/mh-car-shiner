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

package com.jalotsav.mhcarshiner.nvgtnvwmain;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jalotsav.mhcarshiner.R;
import com.jalotsav.mhcarshiner.common.AppConstants;
import com.jalotsav.mhcarshiner.common.GeneralFunctions;
import com.jalotsav.mhcarshiner.common.UserSessionManager;
import com.jalotsav.mhcarshiner.models.MdlContactInquiry;
import com.jalotsav.mhcarshiner.utils.ValidationUtils;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Jalotsav on 6/12/2017.
 */

public class FrgmntContactUs extends Fragment implements AppConstants {

    private static final String TAG = "FrgmntContactUs";

    @BindView(R.id.cordntrlyot_frgmnt_contactus) CoordinatorLayout mCrdntrlyot;

    @BindView(R.id.lnrlyot_frgmmt_contactus_email) LinearLayout mLnrlyotEmail;
    @BindView(R.id.lnrlyot_frgmmt_contactus_mobile) LinearLayout mLnrlyotMobile;

    @BindView(R.id.txtinputlyot_frgmnt_contactus_subject) TextInputLayout mTxtinptlyotSubject;
    @BindView(R.id.txtinputlyot_frgmnt_contactus_message) TextInputLayout mTxtinptlyotMessage;

    @BindView(R.id.txtinptet_frgmnt_contactus_subject) TextInputEditText mTxtinptEtSubject;
    @BindView(R.id.txtinptet_frgmnt_contactus_message) TextInputEditText mTxtinptEtMessage;

    @BindView(R.id.prgrsbr_frgmnt_contactus) ProgressBar mPrgrsbrMain;

    @BindString(R.string.no_intrnt_cnctn) String mNoInternetConnMsg;
    @BindString(R.string.server_problem_sml) String mServerPrblmMsg;
    @BindString(R.string.entr_subject_sml) String mEntrSubject;
    @BindString(R.string.entr_subject_sml) String mEntrMessage;
    @BindString(R.string.sucsfly_contact_inquiry_sml) String mSucsflyContctInquiryMsg;

    String mSubjectVal, mMessageVal;
    FirebaseDatabase mDatabase;
    DatabaseReference mRootRef, mContactInquiryRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.lo_frgmnt_contactus, container, false);
        ButterKnife.bind(this, rootView);

        // Firebase Database Initialization and References
        mDatabase = FirebaseDatabase.getInstance();
        mRootRef = mDatabase.getReference().child(ROOT_NAME);
        mContactInquiryRef = mRootRef.child(CHILD_CONTACT_INQUIRY);

        return rootView;
    }

    @OnClick({R.id. lnrlyot_frgmmt_contactus_email, R.id. lnrlyot_frgmmt_contactus_mobile, R.id.fab_frgmnt_contactus_send})
    public void onClickView(View view) {

        switch (view.getId()) {
            case R.id.lnrlyot_frgmmt_contactus_email:

                Intent intntEmail = new Intent(Intent.ACTION_SENDTO);
                intntEmail.setData(Uri.parse("mailto:" + getString(R.string.contact_email_sml)));
                startActivity(intntEmail);
                break;
            case R.id.lnrlyot_frgmmt_contactus_mobile:

                Intent intntDial = new Intent(Intent.ACTION_DIAL);
                intntDial.setData(Uri.parse("tel:" + getString(R.string.contact_mobileno_sml)));
                startActivity(intntDial);
                break;
            case R.id.fab_frgmnt_contactus_send:

                if(mPrgrsbrMain.getVisibility() != View.VISIBLE) {
                    if (GeneralFunctions.isNetConnected(getActivity()))
                        checkAllValidation();
                    else
                        Snackbar.make(mCrdntrlyot, mNoInternetConnMsg, Snackbar.LENGTH_LONG).show();
                }
                break;
        }
    }

    // Check all validation of fields and Firebase call
    private void checkAllValidation() {

        if (!ValidationUtils.validateEmpty(getActivity(), mTxtinptlyotSubject, mTxtinptEtSubject, mEntrSubject)) // Subject
            return;

        if (!ValidationUtils.validateEmpty(getActivity(), mTxtinptlyotMessage, mTxtinptEtMessage, mEntrMessage)) // Message
            return;

        writeNewContactInquiry();
    }

    // Write/Add new Contact Inquiry in Firebase Database
    private void writeNewContactInquiry() {

        mPrgrsbrMain.setVisibility(View.VISIBLE);

        mSubjectVal =  mTxtinptEtSubject.getText().toString().trim();
        mMessageVal = mTxtinptEtMessage.getText().toString().trim();

        UserSessionManager session = new UserSessionManager(getActivity());
        DatabaseReference newContactInquiryRef = mContactInquiryRef.push();
        newContactInquiryRef.setValue(
                new MdlContactInquiry(newContactInquiryRef.getKey(), session.getUserId(), session.getMobile(), mSubjectVal, mMessageVal,
                        GeneralFunctions.getCurrentTimestamp(), session.getUserId(), "", ""))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Log.i(TAG, "onSuccess: writeNewContactInquiry - data successfully inserted.");
                        mPrgrsbrMain.setVisibility(View.GONE);
                        clearUI();
                        Snackbar.make(mCrdntrlyot, mSucsflyContctInquiryMsg, Snackbar.LENGTH_LONG).show();
                    }

                    // Clear UI (user entered data)
                    private void clearUI() {

                        if(isAdded()) {
                            mTxtinptEtSubject.setText("");
                            mTxtinptEtMessage.setText("");
                            ValidationUtils.requestFocus(getActivity(), mTxtinptEtSubject); // Set focus on SUBJECT field
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Log.e(TAG, "onFailure: writeNewContactInquiry - " + e.getMessage());
                        mPrgrsbrMain.setVisibility(View.GONE);
                        Snackbar.make(mCrdntrlyot, mServerPrblmMsg, Snackbar.LENGTH_LONG).show();
                    }
                });
    }
}
