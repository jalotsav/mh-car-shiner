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

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

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

public class ActvtySignIn extends AppCompatActivity implements AppConstants {

    private static final String TAG = ActvtySignIn.class.getSimpleName();

    @BindView(R.id.cordntrlyot_signin) CoordinatorLayout mCrdntrlyot;

    @BindView(R.id.txtinputlyot_signin_mobileno) TextInputLayout mTxtinptlyotMobile;
    @BindView(R.id.txtinputlyot_signin_password) TextInputLayout mTxtinptlyotPaswrd;
    @BindView(R.id.txtinptet_signin_mobileno) TextInputEditText mTxtinptEtMobile;
    @BindView(R.id.txtinptet_signin_password) TextInputEditText mTxtinptEtPaswrd;
    @BindView(R.id.prgrsbr_signin) ProgressBar mPrgrsbrMain;

    @BindString(R.string.no_intrnt_cnctn) String mNoInternetConnMsg;
    @BindString(R.string.server_problem_sml) String mServerPrblmMsg;
    @BindString(R.string.internal_problem_sml) String mInternalPrblmMsg;
    @BindString(R.string.invalid_login_crdntls) String mInvldCredentialsMsg;
    @BindString(R.string.access_denied_sml) String mAccessDeniedTitle;
    @BindString(R.string.access_denied_msg) String mAccessDeniedMsg;

    UserSessionManager session;
    FirebaseDatabase mDatabase;
    DatabaseReference mRootRef, mUsersRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lo_actvty_signin);
        ButterKnife.bind(this);

        session = new UserSessionManager(this);
//
        // Firebase Database Initialization and References
        mDatabase = FirebaseDatabase.getInstance();
        mRootRef = mDatabase.getReference().child(ROOT_NAME);
        mUsersRef = mRootRef.child(CHILD_USERS);
    }

    @OnClick({R.id.appcmptbtn_signin, R.id.appcmptbtn_signin_signup})
    public void onClickView(View view) {

        switch (view.getId()) {
            case R.id.appcmptbtn_signin:

                checkAllValidation();
                break;
            case R.id.appcmptbtn_signin_signup:

                startActivity(new Intent(this, ActvtySignUp.class));
                break;
        }
    }

    // Check all validation of fields and call API
    private void checkAllValidation() {

        if (!ValidationUtils.validateMobile(this, mTxtinptlyotMobile, mTxtinptEtMobile))
            return;

        if (!ValidationUtils.validatePassword(this, mTxtinptlyotPaswrd, mTxtinptEtPaswrd))
            return;

        if(GeneralFunctions.isNetConnected(this))
            signInWithCredential();
        else Snackbar.make(mCrdntrlyot, mNoInternetConnMsg, Snackbar.LENGTH_LONG).show();
    }

    private void signInWithCredential() {

        mPrgrsbrMain.setVisibility(View.VISIBLE);
        String mobileVal = mTxtinptEtMobile.getText().toString().trim();
        final String passwordVal = mTxtinptEtPaswrd.getText().toString().trim();

        /*
        * Check MOBILE NUMBER is exist in database
        * If YES, then get dataSnapshot by using UId and match PASSPHRASE
        * */
        mUsersRef.orderByChild(CHILD_MOBILE).equalTo(mobileVal)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        mPrgrsbrMain.setVisibility(View.GONE);
                        if(dataSnapshot.exists()) {

                            for(DataSnapshot usersSnapshot: dataSnapshot.getChildren()) {

                                MdlUsers objMdlUsers = usersSnapshot.getValue(MdlUsers.class);
                                if (objMdlUsers != null) {
                                    if(objMdlUsers.isActive()) {
                                        if (objMdlUsers.getPassphrase().equals(passwordVal)) {

                                            session.setUserId(objMdlUsers.getuId());
                                            session.setFirstName(objMdlUsers.getFirstname());
                                            session.setLastName(objMdlUsers.getLastname());
                                            session.setMobile(objMdlUsers.getMobile());
                                            session.setEmail(objMdlUsers.getEmail());

                                            startActivity(new Intent(ActvtySignIn.this, ActvtyMain.class));
                                            finish();
                                        } else
                                            Snackbar.make(mCrdntrlyot, mInvldCredentialsMsg, Snackbar.LENGTH_LONG).show();
                                    } else
                                        accessDeniedAlertDialog();
                                } else
                                    Snackbar.make(mCrdntrlyot, mServerPrblmMsg, Snackbar.LENGTH_LONG).show();
                            }
                        } else
                            Snackbar.make(mCrdntrlyot, mInvldCredentialsMsg, Snackbar.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                        Log.e(TAG, "signInWithCredential - onCancelled: " + databaseError.getMessage());
                        mPrgrsbrMain.setVisibility(View.GONE);
                        Snackbar.make(mCrdntrlyot, mServerPrblmMsg, Snackbar.LENGTH_LONG).show();
                    }
                });
    }

    // Show AlertDialog for Access Denied
    private void accessDeniedAlertDialog() {

        AlertDialog.Builder alrtDlg = new AlertDialog.Builder(this, R.style.MDTheme_Light_Dialog);
        alrtDlg.setTitle(mAccessDeniedTitle);
        alrtDlg.setMessage(mAccessDeniedMsg);
        alrtDlg.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();
            }
        });

        alrtDlg.show();
    }
}
