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
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.jalotsav.mhcarshiner.common.AppConstants;
import com.jalotsav.mhcarshiner.utils.ValidationUtils;

import java.util.concurrent.TimeUnit;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Jalotsav on 5/22/2017.
 */

public class VerifyMobileNo extends AppCompatActivity {

    private static final String TAG = VerifyMobileNo.class.getSimpleName();

    @BindView(R.id.cordntrlyot_verifymobileno) CoordinatorLayout mCrdntrlyot;
    @BindView(R.id.tv_verifymobileno_mobileno) TextView mTvMobileNo;
    @BindView(R.id.prgrsbr_verifymobileno_autodetectmsg) ProgressBar mPrgrsbrAutoDetect;
    @BindView(R.id.prgrsbr_verifymobileno) ProgressBar mPrgrsbrMain;
    @BindView(R.id.appcmptbtn_verifymobileno_resendsms) AppCompatButton mAppcmptbtnResend;
    @BindView(R.id.fab_verifymobileno_submit) FloatingActionButton mFabVerify;
    @BindView(R.id.txtinputlyot_verifymobileno_vrfctncode) TextInputLayout mTxtinptlyotVrfctnCode;
    @BindView(R.id.txtinptet_verifymobileno_vrfctncode) TextInputEditText mTxtinptEtVrfctnCode;

    @BindString(R.string.please_wait_3dots) String mPleaseWait;
    @BindString(R.string.server_problem_sml) String mServerPrblmMsg;
    @BindString(R.string.no_detect_any_mobileno) String mNoDetectMobileNoMsg;
    @BindString(R.string.codesent_msg) String mCodeSentMsg;
    @BindString(R.string.entr_verfctn_code) String mEntrVrfctnCodeMsg;
    @BindString(R.string.invalid_verfctn_code) String mInvalidVrfctnCodeMsg;
    @BindString(R.string.invalid_mobile) String mInvalidMobileNoMsg;
    @BindString(R.string.auto_detect_sms_fail) String mAutoDetectFailMsg;

    static String MOBILE_NO;

    private static final String KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress";
    private static final int STATE_INITIALIZED = 1;
    private static final int STATE_CODE_SENT = 2;
    private static final int STATE_VERIFY_FAILED = 3;
    private static final int STATE_VERIFY_SUCCESS = 4;
    private static final int STATE_SIGNIN_FAILED = 5;
    private static final int STATE_SIGNIN_SUCCESS = 6;

    private FirebaseAuth mFireAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private boolean mVerificationInProgress = false;
    private String mVerificationId;
    Handler mHndlr;
    Runnable mRunnable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lo_actvty_verify_mobileno);
        ButterKnife.bind(this);
        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }

        MOBILE_NO = getIntent().getStringExtra(AppConstants.CHILD_MOBILE);

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d(TAG, "onVerificationCompleted:" + credential);
                mVerificationInProgress = false;

                // Update the UI and attempt sign in with the phone credential
                updateUI(STATE_VERIFY_SUCCESS, credential);
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e);
                mVerificationInProgress = false;

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    Toast.makeText(VerifyMobileNo.this, mInvalidMobileNoMsg, Toast.LENGTH_SHORT).show();
                    onBackPressed();
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    Snackbar.make(mCrdntrlyot, "Quota exceeded.", Snackbar.LENGTH_SHORT).show();
                }

                // Show a message and update the UI
                updateUI(STATE_VERIFY_FAILED);
            }

            @Override
            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:" + verificationId);

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;

                // Update UI
                updateUI(STATE_CODE_SENT);
            }
        };

        if (!TextUtils.isEmpty(MOBILE_NO)) {

            mTvMobileNo.setText(MOBILE_NO);
            mFireAuth = FirebaseAuth.getInstance();

            startPhoneNumberVerification(MOBILE_NO);
            updateUI(STATE_INITIALIZED);
        } else {

            Toast.makeText(this, mNoDetectMobileNoMsg, Toast.LENGTH_SHORT).show();
            onBackPressed();
        }
    }

    @OnClick({R.id.appcmptbtn_verifymobileno_resendsms, R.id.fab_verifymobileno_submit})
    public void onClickView(View view) {

        switch (view.getId()) {
            case R.id.appcmptbtn_verifymobileno_resendsms:

                updateUI(STATE_INITIALIZED);
                resendVerificationCode(MOBILE_NO, mResendToken);
                break;
            case R.id.fab_verifymobileno_submit:

                String etVerfctnCodeStr = mTxtinptEtVrfctnCode.getText().toString().trim();
                if (TextUtils.isEmpty(etVerfctnCodeStr)) {

                    mTxtinptlyotVrfctnCode.setErrorEnabled(true);
                    mTxtinptlyotVrfctnCode.setError(mEntrVrfctnCodeMsg);
                } else
                    verifyPhoneNumberWithCode(mVerificationId, etVerfctnCodeStr);
                break;
        }
    }

    private void startPhoneNumberVerification(String phoneNumber) {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,                 // Timeout duration
                TimeUnit.SECONDS,
                this,
                mCallbacks);
        mVerificationInProgress = true;
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void resendVerificationCode(String phoneNumber, PhoneAuthProvider.ForceResendingToken token) {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,                 // Timeout duration
                TimeUnit.SECONDS,
                this,
                mCallbacks,
                token);             // ForceResendingToken from callbacks
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {

        mPrgrsbrMain.setVisibility(View.VISIBLE);

        mFireAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        mPrgrsbrMain.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.i(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            updateUI(STATE_SIGNIN_SUCCESS, user);
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                mTxtinptlyotVrfctnCode.setErrorEnabled(true);
                                mTxtinptlyotVrfctnCode.setError(mInvalidVrfctnCodeMsg);
                                ValidationUtils.requestFocus(VerifyMobileNo.this, mTxtinptEtVrfctnCode);
                            }

                            // Update UI
                            updateUI(STATE_SIGNIN_FAILED);
                        }
                    }
                });
    }

    private void updateUI(int uiState) {
        updateUI(uiState, mFireAuth.getCurrentUser(), null);
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            updateUI(STATE_SIGNIN_SUCCESS, user);
        } else {
            updateUI(STATE_INITIALIZED);
        }
    }

    private void updateUI(int uiState, FirebaseUser user) {
        updateUI(uiState, user, null);
    }

    private void updateUI(int uiState, PhoneAuthCredential cred) {
        updateUI(uiState, null, cred);
    }

    private void updateUI(int uiState, FirebaseUser user, PhoneAuthCredential cred) {
        switch (uiState) {
            case STATE_INITIALIZED:

                mPrgrsbrAutoDetect.setVisibility(View.VISIBLE);
                mAppcmptbtnResend.setVisibility(View.GONE);

                mHndlr = new Handler();
                mRunnable = new Runnable() {
                    @Override
                    public void run() {

                        mPrgrsbrAutoDetect.setVisibility(View.GONE);
                        mAppcmptbtnResend.setVisibility(View.VISIBLE);
                    }
                };
                mHndlr.postDelayed(mRunnable, 120000);
                break;
            case STATE_CODE_SENT:

                // Code sent state
                Snackbar.make(mCrdntrlyot, mCodeSentMsg, Snackbar.LENGTH_SHORT).show();
                break;
            case STATE_VERIFY_FAILED:

                // Verification has failed, show all options
                mTxtinptlyotVrfctnCode.setErrorEnabled(true);
                mTxtinptlyotVrfctnCode.setError(mInvalidVrfctnCodeMsg);
                ValidationUtils.requestFocus(this, mTxtinptEtVrfctnCode);

                mPrgrsbrAutoDetect.setVisibility(View.GONE);
                mAppcmptbtnResend.setVisibility(View.VISIBLE);
                break;
            case STATE_VERIFY_SUCCESS:

                // Verification has succeeded, proceed to firebase sign in
                mHndlr.removeCallbacks(mRunnable);
                mPrgrsbrAutoDetect.setVisibility(View.GONE);
                mTxtinptlyotVrfctnCode.setError(null);
                mTxtinptlyotVrfctnCode.setErrorEnabled(false);

                // Set the verification text based on the credential
                if (cred != null) {
                    if (cred.getSmsCode() != null) {
                        mTxtinptEtVrfctnCode.setText(cred.getSmsCode());
                    }/* else {
                        AUTO-DETECT by Google Play service (Without sending SMS)
                    }*/
                }
                break;
            case STATE_SIGNIN_FAILED:

                Toast.makeText(this, mServerPrblmMsg, Toast.LENGTH_SHORT).show();
                setResult(RESULT_CANCELED);
                finish();
                break;
            case STATE_SIGNIN_SUCCESS:

                // Np-op, handled by sign-in check
                mTxtinptlyotVrfctnCode.setError(null);
                mTxtinptlyotVrfctnCode.setErrorEnabled(false);
                break;
        }

        if (user != null) {

            mTxtinptEtVrfctnCode.setText(null);

            Intent mIntent = new Intent();
            mIntent.putExtra(AppConstants.CHILD_UID, user.getUid());
            setResult(RESULT_OK, mIntent);
            finish();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // Check if user is signed in (non-null) and update UI accordingly.
        /*FirebaseUser currentUser = mFireAuth.getCurrentUser();
        updateUI(currentUser);

        if (mVerificationInProgress) {
            startPhoneNumberVerification(MOBILE_NO);
        }*/
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_VERIFY_IN_PROGRESS, mVerificationInProgress);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mVerificationInProgress = savedInstanceState.getBoolean(KEY_VERIFY_IN_PROGRESS);
    }
}
