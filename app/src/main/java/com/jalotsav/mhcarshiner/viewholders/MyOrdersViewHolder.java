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

package com.jalotsav.mhcarshiner.viewholders;

import android.app.Dialog;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jalotsav.mhcarshiner.R;
import com.jalotsav.mhcarshiner.common.AppConstants;
import com.jalotsav.mhcarshiner.common.GeneralFunctions;
import com.jalotsav.mhcarshiner.models.MdlCarWashPackages;
import com.jalotsav.mhcarshiner.models.MdlOrder;
import com.jalotsav.mhcarshiner.models.MdlOrderAddressDetails;
import com.jalotsav.mhcarshiner.models.MdlOrderVehicleDetails;

/**
 * Created by Jalotsav on 8/3/2018.
 */
public class MyOrdersViewHolder extends RecyclerView.ViewHolder implements AppConstants {

    private View mItemView;
    private ImageView mImgvwPackgImage;
    private LinearLayout mLnrlyotPackgBackgrnd;
    private TextView mTvPackgMonthlyWash, mTvPackgName, mTvPackgPrice, mTvOrderNumber, mTvOrderBookedDate,
            mTvVehicleName, mTvVehicleType, mTvVehicleNumber, mTvFlatNo, mTvStreet, mTvPincode, mTvCity;
    private AppCompatButton mAppcmptbtnOrderStatus, mAppcmptbtnWashTime, mAppcmptbtnLocation, mAppcmptbtnDialogPositive;
    private Dialog mDialogLocation;

    public MyOrdersViewHolder(View itemView) {
        super(itemView);
        this.mItemView = itemView;
        mImgvwPackgImage = itemView.findViewById(R.id.imgvw_recylrvw_item_myorders_packgimage);
        mLnrlyotPackgBackgrnd = itemView.findViewById(R.id.lnrlyot_recylrvw_item_myorders_packgbackgrnd);
        mTvPackgMonthlyWash = itemView.findViewById(R.id.tv_recylrvw_item_myorders_monthlywash);
        mTvPackgName = itemView.findViewById(R.id.tv_recylrvw_item_myorders_packgname);
        mTvPackgPrice = itemView.findViewById(R.id.tv_recylrvw_item_myorders_packgprice);
        mTvOrderNumber = itemView.findViewById(R.id.tv_recylrvw_item_myorders_ordernumber);
        mTvOrderBookedDate = itemView.findViewById(R.id.tv_recylrvw_item_myorders_bookeddate);
        mTvVehicleName = itemView.findViewById(R.id.tv_recylrvw_item_myorders_vehiclename);
        mTvVehicleType = itemView.findViewById(R.id.tv_recylrvw_item_myorders_vehicletype);
        mTvVehicleNumber = itemView.findViewById(R.id.tv_recylrvw_item_myorders_vehiclenumber);
        mAppcmptbtnOrderStatus = itemView.findViewById(R.id.appcmptbtn_recylrvw_item_myorders_status);
        mAppcmptbtnWashTime = itemView.findViewById(R.id.appcmptbtn_recylrvw_item_myorders_washtime);
        mAppcmptbtnLocation = itemView.findViewById(R.id.appcmptbtn_recylrvw_item_myorders_location);

        mAppcmptbtnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mDialogLocation != null && !mDialogLocation.isShowing())
                    mDialogLocation.show();
            }
        });
    }

    // Set Wash Package Details
    public void setWashPackagesDetails(Context context, MdlCarWashPackages mdlCarWashPackages) {

        switch (mdlCarWashPackages.getName()) {
            case WASH_PACKAGE_SILVER:

                mImgvwPackgImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.img_carwash_byhand_2));
                mLnrlyotPackgBackgrnd.setBackground(ContextCompat.getDrawable(context, R.drawable.drwbl_gradiant_purple_trans));
                break;
            case WASH_PACKAGE_GOLD:

                mImgvwPackgImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.img_carwash_car_only_1));
                mLnrlyotPackgBackgrnd.setBackground(ContextCompat.getDrawable(context, R.drawable.drwbl_gradiant_red_trans));
                break;
            case WASH_PACKAGE_DIAMOND:

                mImgvwPackgImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.img_carwash_byhand_1));
                mLnrlyotPackgBackgrnd.setBackground(ContextCompat.getDrawable(context, R.drawable.drwbl_gradiant_cyan_trans));
                break;
        }

        mTvPackgName.setText(mdlCarWashPackages.getName());
        mTvPackgPrice.setText(context.getString(R.string.rupees_symbl_val, mdlCarWashPackages.getPrice()));
        mTvPackgMonthlyWash.setText(context.getString(R.string.monthly_wash_val, mdlCarWashPackages.getMonthlyWash()));
    }

    // Set Order Details
    public void setOrderDetails(Context context, MdlOrder mdlOrder) {

        mTvOrderNumber.setText(context.getString(R.string.order_hashtag_val, mdlOrder.getOrderNumber()));
        mTvOrderBookedDate.setText(GeneralFunctions.getDateTimeFromTimestamp(mdlOrder.getCreatedOn()));

        switch (mdlOrder.getStatus()) {
            case ORDER_STATUS_PENDING:

                mAppcmptbtnOrderStatus.setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryAmber));
                break;
            case ORDER_STATUS_APPROVED:

                mAppcmptbtnOrderStatus.setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryBlue));
                break;
            case ORDER_STATUS_REJECTED:

                mAppcmptbtnOrderStatus.setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryRed));
                break;
            case ORDER_STATUS_COMPLETED:

                mAppcmptbtnOrderStatus.setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryGreen));
                break;
        }
        mAppcmptbtnOrderStatus.setText(mdlOrder.getStatus());
    }

    // Set Vehicle Details
    public void setVehicleDetails(MdlOrderVehicleDetails mdlOrderVehicleDetails) {

        mTvVehicleName.setText(mdlOrderVehicleDetails.getVehicleName());
        mTvVehicleType.setText(mdlOrderVehicleDetails.getVehicleType());
        mTvVehicleNumber.setText(mdlOrderVehicleDetails.getVehicleNumber());
        mAppcmptbtnWashTime.setText(mdlOrderVehicleDetails.getWashTime());
    }

    // Set Address Details
    public void setAddressDetails(Context context, MdlOrderAddressDetails mdlOrderAddressDetails) {

        mDialogLocation = new Dialog(context);
        mDialogLocation.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialogLocation.setContentView(R.layout.lo_dialog_myorders_addressdtls);
        mTvFlatNo = mDialogLocation.findViewById(R.id.tv_dialog_myorders_addressdtls_faltno);
        mTvStreet = mDialogLocation.findViewById(R.id.tv_dialog_myorders_addressdtls_street);
        mTvPincode = mDialogLocation.findViewById(R.id.tv_dialog_myorders_addressdtls_pincode);
        mTvCity = mDialogLocation.findViewById(R.id.tv_dialog_myorders_addressdtls_city);
        mAppcmptbtnDialogPositive = mDialogLocation.findViewById(R.id.appcmptbtn_dialog_myorders_addressdtls_positive);

        mTvFlatNo.setText(mdlOrderAddressDetails.getFlatNo());
        mTvStreet.setText(mdlOrderAddressDetails.getStreetArea());
        mTvPincode.setText(mdlOrderAddressDetails.getPincode());
        mTvCity.setText(mdlOrderAddressDetails.getCity());

        mAppcmptbtnDialogPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mDialogLocation.dismiss();
            }
        });
    }
}
