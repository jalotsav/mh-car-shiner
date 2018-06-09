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

package com.jalotsav.mhcarshiner;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jalotsav.mhcarshiner.common.AppConstants;
import com.jalotsav.mhcarshiner.common.GeneralFunctions;
import com.jalotsav.mhcarshiner.common.UserSessionManager;
import com.jalotsav.mhcarshiner.models.MdlUsers;
import com.jalotsav.mhcarshiner.utils.ValidationUtils;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Jalotsav on 5/21/2017.
 */

public class ActvtySignUp extends AppCompatActivity implements AppConstants {

    private static final String TAG = ActvtySignUp.class.getSimpleName();

    @BindView(R.id.cordntrlyot_signup) CoordinatorLayout mCrdntrlyot;

    @BindView(R.id.txtinputlyot_signup_firstname) TextInputLayout mTxtinptlyotFirstName;
    @BindView(R.id.txtinputlyot_signup_lastname) TextInputLayout mTxtinptlyotLastName;
    @BindView(R.id.txtinputlyot_signup_email) TextInputLayout mTxtinptlyotEmail;
    @BindView(R.id.txtinputlyot_signup_mobileno) TextInputLayout mTxtinptlyotMobile;
    @BindView(R.id.txtinputlyot_signup_password) TextInputLayout mTxtinptlyotPaswrd;

    @BindView(R.id.txtinptet_signup_firstname) TextInputEditText mTxtinptEtFirstName;
    @BindView(R.id.txtinptet_signup_lastname) TextInputEditText mTxtinptEtLastName;
    @BindView(R.id.txtinptet_signup_email) TextInputEditText mTxtinptEtEmail;
    @BindView(R.id.txtinptet_signup_mobileno) TextInputEditText mTxtinptEtMobile;
    @BindView(R.id.txtinptet_signup_password) TextInputEditText mTxtinptEtPaswrd;

    @BindView(R.id.prgrsbr_signup) ProgressBar mPrgrsbrMain;

    @BindString(R.string.no_intrnt_cnctn) String mNoInternetConnMsg;
    @BindString(R.string.server_problem_sml) String mServerPrblmMsg;
    @BindString(R.string.internal_problem_sml) String mInternalPrblmMsg;
    @BindString(R.string.entr_firstname_sml) String mEntrFirstName;
    @BindString(R.string.entr_lastname_sml) String mEntrLastName;
    @BindString(R.string.entr_email_sml) String mEntrEmail;
    @BindString(R.string.email_exist) String mEmailExistMsg;
    @BindString(R.string.mobileno_exist) String mMobileExistMsg;
    @BindString(R.string.mobileno_verfd_sucsfly) String mMobileVerifedMsg;
    @BindString(R.string.mobileno_not_verfd) String mMobileNotVerifiedMsg;
    @BindString(R.string.exist_sml) String mExistStr;
    @BindString(R.string.sucsfly_regstr_sml) String mSucsflyRegstrnMsg;

    String mFirstNameVal, mLastNameVal, mEmailVal, mMobileVal, mPasswordVal;
    UserSessionManager session;
    boolean isMobileVerified = false;
    String mPhoneAuthUid = "";
    FirebaseDatabase mDatabase;
    DatabaseReference mRootRef, mUsersRef;
    private FirebaseAuth mFireAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lo_actvty_signup);
        ButterKnife.bind(this);

        ActionBar mActionBar = getSupportActionBar();
        if (mActionBar != null)
            mActionBar.setDisplayHomeAsUpEnabled(true);

        session = new UserSessionManager(this);
        mFireAuth = FirebaseAuth.getInstance();
//
        // Firebase Database Initialization and References
        mDatabase = FirebaseDatabase.getInstance();
        mRootRef = mDatabase.getReference().child(ROOT_NAME);
        mUsersRef = mRootRef.child(CHILD_USERS);
    }

    @OnClick({R.id.appcmptbtn_signup})
    public void onClickView(View view) {

        switch (view.getId()) {
            case R.id.appcmptbtn_signup:

                if(mPrgrsbrMain.getVisibility() != View.VISIBLE) {
                    if (GeneralFunctions.isNetConnected(this))
                        checkAllValidation();
                    else
                        Snackbar.make(mCrdntrlyot, mNoInternetConnMsg, Snackbar.LENGTH_LONG).show();
                }
                break;
        }
    }

    // Check all validation of fields and call API
    private void checkAllValidation() {

        if (!ValidationUtils.validateEmpty(this, mTxtinptlyotFirstName, mTxtinptEtFirstName, mEntrFirstName)) // FirstName
            return;

        if (!ValidationUtils.validateEmpty(this, mTxtinptlyotLastName, mTxtinptEtLastName, mEntrLastName)) // LastName
            return;

        if (!ValidationUtils.validateEmpty(this, mTxtinptlyotEmail, mTxtinptEtEmail, mEntrEmail)) // Email
            return;

        if (!ValidationUtils.validateEmailFormat(this, mTxtinptlyotEmail, mTxtinptEtEmail)) { // Email Format
            return;
        }

        if (!ValidationUtils.validateMobile(this, mTxtinptlyotMobile, mTxtinptEtMobile)) // Mobile
            return;

        if (!ValidationUtils.validatePassword(this, mTxtinptlyotPaswrd, mTxtinptEtPaswrd)) // Password
            return;

        checkEmailExist();
    }

    // Check for Email is already exist in our Firebase Database
    private void checkEmailExist() {

        mPrgrsbrMain.setVisibility(View.VISIBLE);
        mEmailVal = mTxtinptEtEmail.getText().toString().trim();
        // Check Email is Exist in database
        mUsersRef.orderByChild(CHILD_EMAIL).equalTo(mEmailVal)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        mPrgrsbrMain.setVisibility(View.GONE);
                        if(dataSnapshot.exists())
                            Snackbar.make(mCrdntrlyot, mEmailExistMsg, Snackbar.LENGTH_LONG).show();
                        else
                            checkMobileExist();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                        Log.e(TAG, "checkEmailExist - onCancelled: " + databaseError.getMessage());
                        mPrgrsbrMain.setVisibility(View.GONE);
                        Snackbar.make(mCrdntrlyot, mServerPrblmMsg, Snackbar.LENGTH_LONG).show();
                    }
                });
    }

    // Check for Mobile number is already exist in our Firebase Database
    private void checkMobileExist() {

        mPrgrsbrMain.setVisibility(View.VISIBLE);
        mMobileVal = mTxtinptEtMobile.getText().toString().trim();
        // Check Mobile number is Exist in database
        mUsersRef.orderByChild(CHILD_MOBILE).equalTo(mMobileVal)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        mPrgrsbrMain.setVisibility(View.GONE);
                        if(dataSnapshot.exists())
                            Snackbar.make(mCrdntrlyot, mMobileExistMsg, Snackbar.LENGTH_LONG).show();
                        else {

                            if(!isMobileVerified) {

                                Intent intntVerifyMobileNo = new Intent(ActvtySignUp.this, VerifyMobileNo.class);
                                intntVerifyMobileNo.putExtra(AppConstants.CHILD_MOBILE, mTxtinptEtMobile.getText().toString().trim());
                                startActivityForResult(intntVerifyMobileNo, AppConstants.REQUEST_VERFCTN_MOBILENO);
                            } else
                                writeNewUser();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                        Log.e(TAG, "checkMobileExist - onCancelled: " + databaseError.getMessage());
                        mPrgrsbrMain.setVisibility(View.GONE);
                        Snackbar.make(mCrdntrlyot, mServerPrblmMsg, Snackbar.LENGTH_LONG).show();
                    }
                });
    }

    // Write/Add new user in Firebase Database
    private void writeNewUser() {

        mFirstNameVal =  mTxtinptEtFirstName.getText().toString().trim();
        mFirstNameVal = Character.toUpperCase(mFirstNameVal.charAt(0))+mFirstNameVal.substring(1); // First Character Uppercase
        mLastNameVal = mTxtinptEtLastName.getText().toString().trim();
        mLastNameVal = Character.toUpperCase(mLastNameVal.charAt(0)) + mLastNameVal.substring(1); // First Character Uppercase
        mEmailVal = mTxtinptEtEmail.getText().toString().trim();
        mMobileVal = mTxtinptEtMobile.getText().toString().trim();
        mPasswordVal = mTxtinptEtPaswrd.getText().toString().trim();

        DatabaseReference newUsersRef = mUsersRef.child(mPhoneAuthUid);
        newUsersRef.setValue(
                new MdlUsers(mPhoneAuthUid, mFirstNameVal, mLastNameVal, mEmailVal, mMobileVal, mPasswordVal, DEVICE_TYPE_ANDROID, true));

        session.setUserId(mPhoneAuthUid);
        session.setFirstName(mFirstNameVal);
        session.setLastName(mLastNameVal);
        session.setMobile(mMobileVal);
        session.setEmail(mEmailVal);

        Toast.makeText(ActvtySignUp.this, mSucsflyRegstrnMsg, Toast.LENGTH_SHORT).show();
        finish();
        startActivity(new Intent(ActvtySignUp.this, ActvtyMain.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == AppConstants.REQUEST_VERFCTN_MOBILENO) {

            if(resultCode == RESULT_OK) {

                isMobileVerified = true;
                Snackbar.make(mCrdntrlyot, mMobileVerifedMsg, Snackbar.LENGTH_SHORT).show();

                mPhoneAuthUid = data.getStringExtra(AppConstants.CHILD_UID);
                writeNewUser();
            } else {

                isMobileVerified = false;
                mFireAuth.signOut();
                Snackbar.make(mCrdntrlyot, mMobileNotVerifiedMsg, Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:

                onBackPressed();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
