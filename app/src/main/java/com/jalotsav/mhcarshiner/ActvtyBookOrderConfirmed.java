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
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jalotsav.mhcarshiner.common.AppConstants;
import com.jalotsav.mhcarshiner.common.GeneralFunctions;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Jalotsav on 7/5/2018.
 */
public class ActvtyBookOrderConfirmed extends AppCompatActivity implements AppConstants {

    @BindView(R.id.rltvlyot_actvty_book_order_confirmed_mainbackgrnd) RelativeLayout mRltvlyotMainBackground;

    @BindView(R.id.tv_actvty_book_order_confirmed_ordernumber) TextView mTvOrderNumber;
    @BindView(R.id.tv_actvty_book_order_confirmed_payableamount) TextView mTvPayableAmount;
    @BindView(R.id.tv_actvty_book_order_confirmed_monthlywash) TextView mTvMonthlyWash;
    @BindView(R.id.tv_actvty_book_order_confirmed_bookeddate) TextView mTvOrderDate;

    String mOrderBookedDate;
    int mPackagePosition, mPackagePrice, mPackageMonthlyWash, mOrderNumber;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lo_actvty_book_order_confirmed);
        ButterKnife.bind(this);

        mPackagePosition = getIntent().getIntExtra(PUT_EXTRA_POSITION, 0);
        mPackagePrice = getIntent().getIntExtra(PUT_EXTRA_WASHPACKAGE_PRICE, 0);
        mPackageMonthlyWash = getIntent().getIntExtra(PUT_EXTRA_WASHPACKAGE_MONTHLYWASH, 0);
        mOrderNumber = getIntent().getIntExtra(PUT_EXTRA_ORDER_NUMBER, 0);
        mOrderBookedDate = getIntent().getStringExtra(PUT_EXTRA_ORDER_BOOKED_DATE);

        setOrderDetailsUI();
    }

    private void setOrderDetailsUI() {

        switch (mPackagePosition) {
            case 0:

                mRltvlyotMainBackground.setBackground(ContextCompat.getDrawable(this, R.drawable.drwbl_gradiant_purple_trans));
                break;
            case 1:

                mRltvlyotMainBackground.setBackground(ContextCompat.getDrawable(this, R.drawable.drwbl_gradiant_red_trans));
                break;
            case 2:

                mRltvlyotMainBackground.setBackground(ContextCompat.getDrawable(this, R.drawable.drwbl_gradiant_cyan_trans));
                break;
        }

        mTvOrderNumber.setText(getString(R.string.hashtag_val, mOrderNumber));
        mTvPayableAmount.setText(getString(R.string.rupees_symbl_val, mPackagePrice));
        mTvMonthlyWash.setText(String.valueOf(mPackageMonthlyWash));
        mTvOrderDate.setText(GeneralFunctions.getDateTimeFromTimestamp(mOrderBookedDate));
    }

    @OnClick({R.id.tv_actvty_book_order_success_contactus, R.id.btn_actvty_book_order_success_myorders})
    public void onClickView(View view) {

        switch (view.getId()) {
            case R.id.tv_actvty_book_order_success_contactus:

                startMainActivity(NVGTNVW_CONTACTUS);
                break;
            case R.id.btn_actvty_book_order_success_myorders:

                startMainActivity(NVGTNVW_MYORDER);
                break;
        }
    }

    // Start Main activity with given menu item position, with clear all back stack
    private void startMainActivity(int navigationViewPosition) {

        startActivity(new Intent(this, ActvtyMain.class)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)
                .putExtra(PUT_EXTRA_NVGTNVW_POSTN, navigationViewPosition));
    }
}
