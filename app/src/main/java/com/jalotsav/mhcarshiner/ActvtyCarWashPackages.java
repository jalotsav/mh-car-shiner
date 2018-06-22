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

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jalotsav.mhcarshiner.common.AppConstants;
import com.jalotsav.mhcarshiner.models.MdlCarWashPackages;

import java.util.ArrayList;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Jalotsav on 6/20/2017.
 */

public class ActvtyCarWashPackages extends AppCompatActivity implements AppConstants {

    private static final String TAG = ActvtyCarWashPackages.class.getSimpleName();

    @BindView(R.id.cordntrlyot_actvty_carwashpckgs) CoordinatorLayout mCrdntrlyot;
    @BindView(R.id.appcmptbtn_actvty_carwashpckgs_packg1_booknow) AppCompatButton mAppcmptbtnPackg1BookNow;
    @BindView(R.id.appcmptbtn_actvty_carwashpckgs_packg2_booknow) AppCompatButton mAppcmptbtnPackg2BookNow;
    @BindView(R.id.appcmptbtn_actvty_carwashpckgs_packg3_booknow) AppCompatButton mAppcmptbtnPackg3BookNow;
    @BindView(R.id.tv_actvty_carwashpckgs_packg1_monthlywash) TextView mTvPackg1MonthlyWash;
    @BindView(R.id.tv_actvty_carwashpckgs_packg1_name) TextView mTvPackg1Name;
    @BindView(R.id.tv_actvty_carwashpckgs_packg1_price) TextView mTvPackg1Price;
    @BindView(R.id.tv_actvty_carwashpckgs_packg2_monthlywash) TextView mTvPackg2MonthlyWash;
    @BindView(R.id.tv_actvty_carwashpckgs_packg2_name) TextView mTvPackg2Name;
    @BindView(R.id.tv_actvty_carwashpckgs_packg2_price) TextView mTvPackg2Price;
    @BindView(R.id.tv_actvty_carwashpckgs_packg3_monthlywash) TextView mTvPackg3MonthlyWash;
    @BindView(R.id.tv_actvty_carwashpckgs_packg3_name) TextView mTvPackg3Name;
    @BindView(R.id.tv_actvty_carwashpckgs_packg3_price) TextView mTvPackg3Price;
    @BindView(R.id.prgrsbr_actvty_carwashpckgs) ProgressBar mPrgrsbr;

    @BindString(R.string.no_intrnt_cnctn) String mNoInternetConnMsg;
    @BindString(R.string.server_problem_sml) String mServerPrblmMsg;
    @BindString(R.string.internal_problem_sml) String mInternalPrblmMsg;

    FirebaseDatabase mDatabase;
    DatabaseReference mRootRef, mWashPackagesRef;
    ArrayList<MdlCarWashPackages> mArrylstWashPackages;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lo_actvty_carwashpackages);
        ButterKnife.bind(this);

        ActionBar mActionBar = getSupportActionBar();
        if (mActionBar != null)
            mActionBar.setDisplayHomeAsUpEnabled(true);

        // Firebase Database Initialization and References
        mDatabase = FirebaseDatabase.getInstance();
        mRootRef = mDatabase.getReference().child(ROOT_NAME);
        mWashPackagesRef = mRootRef.child(CHILD_WASH_PACKAGES);

        mArrylstWashPackages = new ArrayList<>();
        disableView(mAppcmptbtnPackg1BookNow, mAppcmptbtnPackg2BookNow, mAppcmptbtnPackg3BookNow);
        fetchWashPackages();
    }

    private void fetchWashPackages() {

        mPrgrsbr.setVisibility(View.VISIBLE);
        mWashPackagesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mPrgrsbr.setVisibility(View.GONE);
                mArrylstWashPackages = new ArrayList<>();
                for(DataSnapshot packageSnapshot : dataSnapshot.getChildren()) {

                    mArrylstWashPackages.add(packageSnapshot.getValue(MdlCarWashPackages.class));
                }

                updateUI();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Log.e(TAG, "fetchWashPackages() - onCancelled: " + databaseError.getMessage());
                mPrgrsbr.setVisibility(View.GONE);
                Snackbar.make(mCrdntrlyot, mServerPrblmMsg, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    // Update UI with Packages fetched data
    private void updateUI() {

        if(mArrylstWashPackages.size() > 0) {

            try {
                // Details - Package 1
                mTvPackg1MonthlyWash.setText(getString(R.string.monthly_wash_val, mArrylstWashPackages.get(0).getMonthlyWash()));
                mTvPackg1Name.setText(mArrylstWashPackages.get(0).getName());
                mTvPackg1Price.setText(getString(R.string.rupees_symbl_val, mArrylstWashPackages.get(0).getPrice()));

                // Details - Package 2
                mTvPackg2MonthlyWash.setText(getString(R.string.monthly_wash_val, mArrylstWashPackages.get(1).getMonthlyWash()));
                mTvPackg2Name.setText(mArrylstWashPackages.get(1).getName());
                mTvPackg2Price.setText(getString(R.string.rupees_symbl_val, mArrylstWashPackages.get(1).getPrice()));

                // Details - Package 3
                mTvPackg3MonthlyWash.setText(getString(R.string.monthly_wash_val, mArrylstWashPackages.get(2).getMonthlyWash()));
                mTvPackg3Name.setText(mArrylstWashPackages.get(2).getName());
                mTvPackg3Price.setText(getString(R.string.rupees_symbl_val, mArrylstWashPackages.get(2).getPrice()));

                enableView(mAppcmptbtnPackg1BookNow, mAppcmptbtnPackg2BookNow, mAppcmptbtnPackg3BookNow);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "updateUI: " + e.getMessage());
                Snackbar.make(mCrdntrlyot, mInternalPrblmMsg, Snackbar.LENGTH_LONG).show();
            }
        }
    }

    @OnClick({R.id.appcmptbtn_actvty_carwashpckgs_packg1_booknow, R.id.appcmptbtn_actvty_carwashpckgs_packg2_booknow,
            R.id.appcmptbtn_actvty_carwashpckgs_packg3_booknow})
    public void onClickView(View view) {

        switch (view.getId()) {
            case R.id.appcmptbtn_actvty_carwashpckgs_packg1_booknow:

                break;
            case R.id.appcmptbtn_actvty_carwashpckgs_packg2_booknow:

                break;
            case R.id.appcmptbtn_actvty_carwashpckgs_packg3_booknow:

                break;
        }
    }

    // ENABLE views
    private void enableView(View... views) {
        for(View vw : views) {
            vw.setEnabled(true);
        }
    }

    // DISABLE views
    private void disableView(View... views) {
        for(View vw : views) {
            vw.setEnabled(false);
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
