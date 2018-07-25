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
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.transition.TransitionManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jalotsav.mhcarshiner.adapter.PlaceArrayAdapter;
import com.jalotsav.mhcarshiner.adapter.SpnrAdapterVehicleType;
import com.jalotsav.mhcarshiner.adapter.SpnrAdapterWashTiming;
import com.jalotsav.mhcarshiner.common.AppConstants;
import com.jalotsav.mhcarshiner.common.GeneralFunctions;
import com.jalotsav.mhcarshiner.common.UserSessionManager;
import com.jalotsav.mhcarshiner.models.MdlCarWashPackages;
import com.jalotsav.mhcarshiner.models.MdlOrder;
import com.jalotsav.mhcarshiner.models.MdlOrderAddressDetails;
import com.jalotsav.mhcarshiner.models.MdlOrderVehicleDetails;
import com.jalotsav.mhcarshiner.models.MdlVehicleType;
import com.jalotsav.mhcarshiner.models.MdlWashTiming;
import com.jalotsav.mhcarshiner.utils.ValidationUtils;

import java.util.ArrayList;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActvtyBookOrder extends AppCompatActivity implements AppConstants , GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks {

    private static final String TAG = ActvtyBookOrder.class.getSimpleName();

    @BindView(R.id.cordntrlyot_actvty_book_order) CoordinatorLayout mCrdntrlyot;

    @BindView(R.id.toolbar_actvty_book_order) Toolbar mToolbar;

    @BindView(R.id.imgvw_actvty_book_order_packgimage) ImageView mImgvwPackgImage;
    @BindView(R.id.imgvw_actvty_book_order_cardtls_colpsexpnd) ImageView mImgvwCarDtlsColsExpnd;
    @BindView(R.id.imgvw_actvty_book_order_addressdtls_colpsexpnd) ImageView mImgvwAdrsDtlsColsExpnd;

    @BindView(R.id.lnrlyot_actvty_book_order_packgbackgrnd) LinearLayout mLnrlyotPackgBackgrnd;
    @BindView(R.id.lnrlyot_actvty_book_order_cardtls_innervw) LinearLayout mLnrlyotCarDtlsInnervw;
    @BindView(R.id.lnrlyot_actvty_book_order_addressdtls_innervw) LinearLayout mLnrlyotAdrsDtlsInnervw;

    @BindView(R.id.tv_actvty_book_order_packgmonthlywash) TextView mTvPackgMonthlyWash;
    @BindView(R.id.tv_actvty_book_order_packgname) TextView mTvPackgName;
    @BindView(R.id.tv_actvty_book_order_packgprice) TextView mTvPackgPrice;

    @BindView(R.id.cardvw_actvty_book_order_cardtls) CardView mCardvwCarDtls;
    @BindView(R.id.cardvw_actvty_book_order_addressdtls) CardView mCardvwAdrsDtls;

    @BindView(R.id.txtinputlyot_actvty_book_order_cardtls_vehiclename) TextInputLayout mTxtinptlyotVehicleName;
    @BindView(R.id.txtinputlyot_actvty_book_order_cardtls_vehiclenumber) TextInputLayout mTxtinptlyotVehicleNo;
    @BindView(R.id.txtinputlyot_actvty_book_order_addressdtls_flatno) TextInputLayout mTxtinptlyotFlatNo;
    @BindView(R.id.txtinputlyot_actvty_book_order_addressdtls_street) TextInputLayout mTxtinptlyotStreet;
    @BindView(R.id.txtinputlyot_actvty_book_order_addressdtls_pincode) TextInputLayout mTxtinptlyotPincode;
    @BindView(R.id.txtinputlyot_actvty_book_order_addressdtls_city) TextInputLayout mTxtinptlyotCity;

    @BindView(R.id.txtinptet_actvty_book_order_cardtls_vehiclename) TextInputEditText mTxtinptEtVehicleName;
    @BindView(R.id.txtinptet_actvty_book_order_cardtls_vehiclenumber) TextInputEditText mTxtinptEtVehicleNo;
    @BindView(R.id.txtinptet_actvty_book_order_addressdtls_faltno) TextInputEditText mTxtinptEtFlatNo;
    @BindView(R.id.txtinptet_actvty_book_order_addressdtls_street) TextInputEditText mTxtinptEtStreet;
    @BindView(R.id.txtinptet_actvty_book_order_addressdtls_pincode) TextInputEditText mTxtinptEtPincode;
    @BindView(R.id.autocmplttv_actvty_book_order_addressdtls_city) AutoCompleteTextView mAutocmplttvCity;

    @BindView(R.id.spnr_actvty_book_order_cardtls_vehicletype) Spinner mSpnrVehicleType;
    @BindView(R.id.spnr_actvty_book_order_cardtls_washtime) Spinner mSpnrWashTime;

    @BindView(R.id.prgrsbr_actvty_book_order) ProgressBar mPrgrsbrMain;
    @BindView(R.id.prgrsbr_actvty_book_order_cardtls_vehicletype) ProgressBar mPrgrsbrVehicleType;
    @BindView(R.id.prgrsbr_actvty_book_order_cardtls_washtime) ProgressBar mPrgrsbrWashTime;

    @BindString(R.string.no_intrnt_cnctn) String mNoInternetConnMsg;
    @BindString(R.string.server_problem_sml) String mServerPrblmMsg;
    @BindString(R.string.internal_problem_sml) String mInternalPrblmMsg;
    @BindString(R.string.no_data_avlbl_refresh) String mNoDataAvilblMsg;
    @BindString(R.string.refresh_sml) String mRefreshStr;
    @BindString(R.string.entr_vehicle_name_sml) String mEntrVehicleName;
    @BindString(R.string.entr_vehicle_number_sml) String mEntrVehicleNo;
    @BindString(R.string.entr_flatno_buildingname_sml) String mEntrFlatNo;
    @BindString(R.string.entr_locality_area_street_sml) String mEntrStreet;
    @BindString(R.string.entr_pincode_sml) String mEntrPincode;
    @BindString(R.string.entr_valid_city_sml) String mEntrSelectValidCity;

    String mPackageId, mPackageName, mSelectedCityVal,
            mVehicleNameVal, mVehicleNumberVal, mVehicleTypeVal, mWashTimeVal, mFlatNoVal, mStreetVal, mPincodeVal, mOrderBookedDate;
    int mPackagePosition, mPackageMonthlyWash, mPackagePrice, mPreviousOrderNumber;
    boolean mIsExpandedCarDtls, mIsExpandedAddressDtls;
    FirebaseDatabase mDatabase;
    DatabaseReference mRootRef, mVehicleTypeRef, mWashTimingRef, mOrdersRef;
    ArrayList<MdlVehicleType> mArrylstVehicleType;
    ArrayList<MdlWashTiming> mArrylstWashTiming;

    private static final int GOOGLE_API_CLIENT_ID = 0;
    private GoogleApiClient mGoogleApiClient;
    private PlaceArrayAdapter mPlaceArrayAdapter;
    /*private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));*/

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lo_actvty_book_order);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        ActionBar mActionBar = getSupportActionBar();
        if (mActionBar != null)
            mActionBar.setDisplayHomeAsUpEnabled(true);

        initCollapsingToolbar();

        mPackagePosition = getIntent().getIntExtra(PUT_EXTRA_POSITION, 0);
        mPackageId = getIntent().getStringExtra(PUT_EXTRA_WASHPACKAGE_ID);
        mPackageName = getIntent().getStringExtra(PUT_EXTRA_WASHPACKAGE_NAME);
        mPackageMonthlyWash = getIntent().getIntExtra(PUT_EXTRA_WASHPACKAGE_MONTHLYWASH, 0);
        mPackagePrice = getIntent().getIntExtra(PUT_EXTRA_WASHPACKAGE_PRICE, 0);

        // Firebase Database Initialization and References
        mDatabase = FirebaseDatabase.getInstance();
        mRootRef = mDatabase.getReference().child(ROOT_NAME);
        mVehicleTypeRef = mRootRef.child(CHILD_VEHICLE_TYPES);
        mWashTimingRef = mRootRef.child(CHILD_WASH_TIMING);
        mOrdersRef = mRootRef.child(CHILD_ORDERS);

        mArrylstVehicleType = new ArrayList<>();
        mArrylstWashTiming = new ArrayList<>();

        setPackageDetailsUI();

        // EXPAND Car Details view
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mImgvwCarDtlsColsExpnd.performClick();
            }
        }, 500);

        // Google Place API configuration
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(this, GOOGLE_API_CLIENT_ID, this)
                .addConnectionCallbacks(this)
                .build();

        mAutocmplttvCity.setThreshold(3);
        mAutocmplttvCity.setOnItemClickListener(mAutocompleteClickListener);
        AutocompleteFilter autocompleteFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES)
                /*.setCountry("IN")*/
                .build();
        mPlaceArrayAdapter = new PlaceArrayAdapter(this, android.R.layout.simple_list_item_1, null, autocompleteFilter);
        mAutocmplttvCity.setAdapter(mPlaceArrayAdapter);

        if(GeneralFunctions.isNetConnected(this)) {
            fetchVehicleType();
            fetchWashTime();
        } else Snackbar.make(mCrdntrlyot, mNoInternetConnMsg, Snackbar.LENGTH_LONG).show();
    }

    private void initCollapsingToolbar() {

        final CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.colpstoolbar_actvty_book_order);
        collapsingToolbar.setTitle(" ");
//        collapsingToolbar.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
        AppBarLayout appBarLayout = findViewById(R.id.appbarlyot_actvty_book_order);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(mPackageName.toUpperCase());
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    // Set selected Package details on UI from Intent extra
    private void setPackageDetailsUI() {

        switch (mPackagePosition) {
            case 0:

                mImgvwPackgImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.img_carwash_byhand_2));
                mLnrlyotPackgBackgrnd.setBackground(ContextCompat.getDrawable(this, R.drawable.drwbl_gradiant_purple_trans));
                break;
            case 1:

                mImgvwPackgImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.img_carwash_car_only_1));
                mLnrlyotPackgBackgrnd.setBackground(ContextCompat.getDrawable(this, R.drawable.drwbl_gradiant_red_trans));
                break;
            case 2:

                mImgvwPackgImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.img_carwash_byhand_1));
                mLnrlyotPackgBackgrnd.setBackground(ContextCompat.getDrawable(this, R.drawable.drwbl_gradiant_cyan_trans));
                break;
        }

        mTvPackgMonthlyWash.setText(getString(R.string.monthly_wash_val, mPackageMonthlyWash));
        mTvPackgName.setText(mPackageName);
        mTvPackgPrice.setText(getString(R.string.rupees_symbl_val, mPackagePrice));
    }

    // ItemClickListener for AutoComplete of City
    private AdapterView.OnItemClickListener mAutocompleteClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            try {
                PlaceArrayAdapter.PlaceAutocomplete item = mPlaceArrayAdapter.getItem(position);
                String placeId = String.valueOf(item.placeId);
                Log.i(TAG, "Selected: " + item.description);
                PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi.getPlaceById(mGoogleApiClient, placeId);
                placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
                Log.i(TAG, "Fetching details for ID: " + item.placeId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback = new ResultCallback<PlaceBuffer>() {

        @Override
        public void onResult(PlaceBuffer places) {

            if (!places.getStatus().isSuccess()) {
                Log.e(TAG, "Place query did not complete. Error: " + places.getStatus().toString());
                return;
            }
            // Selecting the first object buffer.
            final Place place = places.get(0);
            mSelectedCityVal = String.valueOf(Html.fromHtml(place.getAddress() + ""));
        }
    };

    // Fetch VehicleType from Firebase realtime database
    private void fetchVehicleType() {

        mPrgrsbrVehicleType.setVisibility(View.VISIBLE);

        mVehicleTypeRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mPrgrsbrVehicleType.setVisibility(View.GONE);
                for(DataSnapshot vehicleTypeSnapshot: dataSnapshot.getChildren()) {

                    mArrylstVehicleType.add(vehicleTypeSnapshot.getValue(MdlVehicleType.class));
                }
                SpnrAdapterVehicleType adapterVehicleType = new SpnrAdapterVehicleType(
                        ActvtyBookOrder.this, android.R.layout.simple_spinner_dropdown_item, mArrylstVehicleType);
                mSpnrVehicleType.setAdapter(adapterVehicleType);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Log.e(TAG, "fetchVehicleType() - onCancelled: " + databaseError.getMessage());
                mPrgrsbrVehicleType.setVisibility(View.GONE);
                Snackbar.make(mCrdntrlyot, mServerPrblmMsg, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    // Fetch WashTime from Firebase realtime database
    private void fetchWashTime() {

        mPrgrsbrWashTime.setVisibility(View.VISIBLE);

        mWashTimingRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mPrgrsbrWashTime.setVisibility(View.GONE);
                for(DataSnapshot washTimingSnapshot: dataSnapshot.getChildren()) {

                    mArrylstWashTiming.add(washTimingSnapshot.getValue(MdlWashTiming.class));
                }
                SpnrAdapterWashTiming adapterWashTiming = new SpnrAdapterWashTiming(
                        ActvtyBookOrder.this, android.R.layout.simple_spinner_dropdown_item, mArrylstWashTiming);
                mSpnrWashTime.setAdapter(adapterWashTiming);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Log.e(TAG, "fetchWashTiming() - onCancelled: " + databaseError.getMessage());
                mPrgrsbrWashTime.setVisibility(View.GONE);
                Snackbar.make(mCrdntrlyot, mServerPrblmMsg, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    @OnClick({R.id.imgvw_actvty_book_order_cardtls_colpsexpnd, R.id.imgvw_actvty_book_order_addressdtls_colpsexpnd,
            R.id.appcmptbtn_actvty_book_order})
    public void onClickView(View view) {

        switch (view.getId()) {
            case R.id.imgvw_actvty_book_order_cardtls_colpsexpnd:

                rotateAnimation(mIsExpandedCarDtls, mImgvwCarDtlsColsExpnd);

                if (mIsExpandedCarDtls) collapseCarDetails();
                else expandCarDetails();
                break;
            case R.id.imgvw_actvty_book_order_addressdtls_colpsexpnd:

                rotateAnimation(mIsExpandedAddressDtls, mImgvwAdrsDtlsColsExpnd);

                if (mIsExpandedAddressDtls) collapseAddressDetails();
                else expandAddressDetails();
                break;
            case R.id.appcmptbtn_actvty_book_order:

                if(mPrgrsbrMain.getVisibility() != View.VISIBLE) {
                    if (GeneralFunctions.isNetConnected(this))
                        checkAllValidation();
                    else
                        Snackbar.make(mCrdntrlyot, mNoInternetConnMsg, Snackbar.LENGTH_LONG).show();
                }
                break;
        }
    }

    private void rotateAnimation(boolean isExpanded, ImageView imgvwColsExpnd) {

        RotateAnimation arrowAnimationCarDtls = isExpanded ?
                new RotateAnimation(180,0, Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f) :
                new RotateAnimation(0,180, Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f);
        arrowAnimationCarDtls.setDuration(200);
        arrowAnimationCarDtls.setFillAfter(true);
        imgvwColsExpnd.startAnimation(arrowAnimationCarDtls);
    }

    // COLLAPSE car details
    private void collapseCarDetails() {

        mIsExpandedCarDtls = false;
        TransitionManager.endTransitions(mCardvwCarDtls);
        mLnrlyotCarDtlsInnervw.setVisibility(View.GONE);
    }

    // EXPAND car details
    private void expandCarDetails() {

        mIsExpandedCarDtls = true;
        TransitionManager.beginDelayedTransition(mCardvwCarDtls);
        mLnrlyotCarDtlsInnervw.setVisibility(View.VISIBLE);
    }

    // COLLAPSE Address details
    private void collapseAddressDetails() {

        mIsExpandedAddressDtls = false;
        TransitionManager.endTransitions(mCardvwAdrsDtls);
        mLnrlyotAdrsDtlsInnervw.setVisibility(View.GONE);
    }

    // EXPAND Address details
    private void expandAddressDetails() {

        if(mIsExpandedCarDtls) {
            rotateAnimation(mIsExpandedCarDtls, mImgvwCarDtlsColsExpnd);
            collapseCarDetails();
        }

        mIsExpandedAddressDtls = true;
        TransitionManager.beginDelayedTransition(mCardvwAdrsDtls);
        mLnrlyotAdrsDtlsInnervw.setVisibility(View.VISIBLE);
    }

    // Check all validation of fields and Firebase call
    private void checkAllValidation() {

        if(!checkCarDetailsValidation())
            return;

        if(!checkAddressDetailsValidation())
            return;

        fetchPreviousOrderNumber();
    }

    // Validate Car Details fields
    private boolean checkCarDetailsValidation() {

        if(!mIsExpandedCarDtls)
            expandCarDetails();

        if (!ValidationUtils.validateEmpty(this, mTxtinptlyotVehicleName, mTxtinptEtVehicleName, mEntrVehicleName)) // VehicleName
            return false;

        if (!ValidationUtils.validateEmpty(this, mTxtinptlyotVehicleNo, mTxtinptEtVehicleNo, mEntrVehicleNo)) // VehicleNo
            return false;

        if(!validateVehicleType())
            return false;

        if(!validateWashTiming())
            return false;

        if(mIsExpandedCarDtls) {
            rotateAnimation(mIsExpandedCarDtls, mImgvwCarDtlsColsExpnd);
            collapseCarDetails();
        }
        return true;
    }

    // Validate Vehicle Type spinner
    private boolean validateVehicleType() {

        if(mArrylstVehicleType.size() == 0) {

            showRefreshSnackbar();
            return false;
        } else
            return true;
    }

    // Validate Wash Timing spinner
    private boolean validateWashTiming() {

        if(mArrylstWashTiming.size() == 0) {

            showRefreshSnackbar();
            return false;
        } else
            return true;
    }

    // Validate Address Details fields
    private boolean checkAddressDetailsValidation() {

        if(!mIsExpandedAddressDtls)
            expandAddressDetails();

        if (!ValidationUtils.validateEmpty(this, mTxtinptlyotFlatNo, mTxtinptEtFlatNo, mEntrFlatNo)) // Flat No.
            return false;

        if (!ValidationUtils.validateEmpty(this, mTxtinptlyotStreet, mTxtinptEtStreet, mEntrStreet)) // Street
            return false;

        if (!ValidationUtils.validateEmpty(this, mTxtinptlyotPincode, mTxtinptEtPincode, mEntrPincode)) // Pincode
            return false;

        if (!ValidationUtils.validateEmpty(mTxtinptlyotCity, mSelectedCityVal, mEntrSelectValidCity)) // City
            return false;
        mAutocmplttvCity.setText(mSelectedCityVal); // Set selected value from Place API, in case user changed after selection

        return true;
    }

    // Fetch previous Order number from Firebase realtime database
    private void fetchPreviousOrderNumber() {

        mPrgrsbrMain.setVisibility(View.VISIBLE);
        mOrdersRef.orderByKey().limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mPrgrsbrMain.setVisibility(View.GONE);
                if(dataSnapshot.exists()) {

                    try {
                        for(DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
                            mPreviousOrderNumber = orderSnapshot.getValue(MdlOrder.class).getOrderNumber();
                            mPreviousOrderNumber++;
                            writeNewOrder();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e(TAG, "fetchPreviousOrderNumber() - onDataChange: " + e.getMessage());
                        Snackbar.make(mCrdntrlyot, mInternalPrblmMsg, Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    mPreviousOrderNumber = 1;
                    writeNewOrder();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Log.e(TAG, "fetchPreviousOrderNumber() - onCancelled: " + databaseError.getMessage());
                mPrgrsbrMain.setVisibility(View.GONE);
                Snackbar.make(mCrdntrlyot, mServerPrblmMsg, Snackbar.LENGTH_LONG).show();
            }
        });

    }

    // Write/Add new Order in Firebase realtime Database
    private void writeNewOrder() {

        mVehicleNameVal = mTxtinptEtVehicleName.getText().toString().trim();
        mVehicleNumberVal = mTxtinptEtVehicleNo.getText().toString().trim().toUpperCase();
        mVehicleTypeVal = mArrylstVehicleType.get(mSpnrVehicleType.getSelectedItemPosition()).getVehicleType();
        mWashTimeVal = mArrylstWashTiming.get(mSpnrWashTime.getSelectedItemPosition()).getWashTime();
        mFlatNoVal = mTxtinptEtFlatNo.getText().toString().trim();
        mStreetVal = mTxtinptEtStreet.getText().toString().trim();
        mPincodeVal = mTxtinptEtPincode.getText().toString().trim();
        mOrderBookedDate = GeneralFunctions.getCurrentTimestamp();

        UserSessionManager session = new UserSessionManager(this);
        final DatabaseReference newOrderRef = mOrdersRef.push();
        newOrderRef.setValue(
                new MdlOrder(newOrderRef.getKey(), mPreviousOrderNumber, session.getUserId(), session.getMobile(), ORDER_STATUS_PENDING,
                        mOrderBookedDate, session.getUserId(), "", ""))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        try {
                            // Create CHILD with Vehicle Details
                            DatabaseReference newVehicleDetailsRef = mOrdersRef.child(newOrderRef.getKey()).child(CHILD_VEHICLE_DETAILS);
                            newVehicleDetailsRef.setValue(new MdlOrderVehicleDetails(mVehicleNameVal, mVehicleNumberVal, mVehicleTypeVal, mWashTimeVal));

                            // Create CHILD with Address Details
                            DatabaseReference newAddressDetailsRef = mOrdersRef.child(newOrderRef.getKey()).child(CHILD_ADDRESS_DETAILS);
                            newAddressDetailsRef.setValue(new MdlOrderAddressDetails(mFlatNoVal, mStreetVal, mPincodeVal, mSelectedCityVal));

                            // Create CHILD with Wash Package Details
                            DatabaseReference newWashPackageDetailsRef = mOrdersRef.child(newOrderRef.getKey()).child(CHILD_WASH_PACKAGES_DETAILS);
                            newWashPackageDetailsRef.setValue(new MdlCarWashPackages(mPackageId, mPackageName, mPackageMonthlyWash, mPackagePrice));

                            finish();
                            startActivity(new Intent(ActvtyBookOrder.this, ActvtyBookOrderConfirmed.class)
                                    .putExtra(PUT_EXTRA_POSITION, mPackagePosition)
                                    .putExtra(PUT_EXTRA_WASHPACKAGE_PRICE, mPackagePrice)
                                    .putExtra(PUT_EXTRA_WASHPACKAGE_MONTHLYWASH, mPackageMonthlyWash)
                                    .putExtra(PUT_EXTRA_ORDER_NUMBER, mPreviousOrderNumber)
                                    .putExtra(PUT_EXTRA_ORDER_BOOKED_DATE, mOrderBookedDate));
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e(TAG, "writeNewOrder - onSuccess:  " + e.getMessage());
                            Snackbar.make(mCrdntrlyot, mInternalPrblmMsg, Snackbar.LENGTH_LONG).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Log.e(TAG, "writeNewOrder - onFailure: " + e.getMessage());
                        Snackbar.make(mCrdntrlyot, mServerPrblmMsg, Snackbar.LENGTH_LONG).show();
                    }
                });
    }

    // Show SnackBar with Refresh action
    private void showRefreshSnackbar() {

        Snackbar.make(mCrdntrlyot, mNoDataAvilblMsg, Snackbar.LENGTH_LONG)
                .setActionTextColor(ContextCompat.getColor(this, R.color.colorPrimaryAmber))
                .setAction(mRefreshStr, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(GeneralFunctions.isNetConnected(ActvtyBookOrder.this)) {
                            fetchVehicleType();
                            fetchWashTime();
                        } else Snackbar.make(mCrdntrlyot, mNoInternetConnMsg, Snackbar.LENGTH_LONG).show();
                    }
                })
                .show();
    }

    @Override
    public void onConnected(Bundle bundle) {

        mPlaceArrayAdapter.setGoogleApiClient(mGoogleApiClient);
        Log.i(TAG, "Google Places API connected.");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        Log.e(TAG, "Google Places API connection failed with error code: " + connectionResult.getErrorCode());
        Toast.makeText(this,
                "Google Places API connection failed with error code:" + connectionResult.getErrorCode(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mPlaceArrayAdapter.setGoogleApiClient(null);
        Log.e(TAG, "Google Places API connection suspended.");
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
