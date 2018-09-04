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
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.jalotsav.mhcarshiner.R;
import com.jalotsav.mhcarshiner.common.AppConstants;
import com.jalotsav.mhcarshiner.common.GeneralFunctions;
import com.jalotsav.mhcarshiner.common.RecyclerViewEmptySupport;
import com.jalotsav.mhcarshiner.common.UserSessionManager;
import com.jalotsav.mhcarshiner.models.MdlOrder;
import com.jalotsav.mhcarshiner.viewholders.MyOrdersViewHolder;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Jalotsav on 6/12/2017.
 */

public class FrgmntMyOrders extends Fragment implements AppConstants {

    @BindView(R.id.cordntrlyot_frgmnt_myorders) CoordinatorLayout mCrdntrlyot;
    @BindView(R.id.lnrlyot_recyclremptyvw_appearhere) LinearLayout mLnrlyotAppearHere;
    @BindView(R.id.tv_recyclremptyvw_appearhere) TextView mTvAppearHere;
    @BindView(R.id.rcyclrvw_frgmnt_myorders) RecyclerViewEmptySupport mRecyclerView;
    @BindView(R.id.prgrsbr_frgmnt_myorders) ProgressBar mPrgrsbr;

    @BindString(R.string.orders_appear_here) String mOrdersAppearHere;
    @BindString(R.string.no_intrnt_cnctn) String mNoInternetConnMsg;

    UserSessionManager session;
    RecyclerView.LayoutManager mLayoutManager;
    FirebaseRecyclerAdapter<MdlOrder, MyOrdersViewHolder> mAdapter;
    FirebaseDatabase mDatabase;
    DatabaseReference mRootRef, mOrdersRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.lo_frgmnt_myorders, container, false);
        ButterKnife.bind(this, rootView);

        setHasOptionsMenu(true);

        session = new UserSessionManager(getActivity());

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setEmptyView(mLnrlyotAppearHere);

        mTvAppearHere.setText(mOrdersAppearHere);

        // Firebase Database Initialization and References
        mDatabase = FirebaseDatabase.getInstance();
        mRootRef = mDatabase.getReference().child(ROOT_NAME);
        mOrdersRef = mRootRef.child(CHILD_ORDERS);

        if(GeneralFunctions.isNetConnected(getActivity()))
            setupFirebaseAdapter();
        else Toast.makeText(getActivity(), mNoInternetConnMsg, Toast.LENGTH_SHORT).show();

        return rootView;
    }

    // Setup Recycler Adapter using Firebase-UI with Realtime database support.
    private void setupFirebaseAdapter() {

        mPrgrsbr.setVisibility(View.VISIBLE);
        // Query: get only isVideosAvailable = TRUE projects
        Query queryProjectsRef = mOrdersRef.orderByChild(CHILD_USER_MOBILE).equalTo(session.getMobile());
        mAdapter = new FirebaseRecyclerAdapter<MdlOrder, MyOrdersViewHolder>(
                MdlOrder.class,
                R.layout.lo_recyclritem_myorders,
                MyOrdersViewHolder.class,
                queryProjectsRef
        ) {
            @Override
            public void onDataChanged() {
                super.onDataChanged();

                if(mPrgrsbr != null && mPrgrsbr.getVisibility() == View.VISIBLE)
                    mPrgrsbr.setVisibility(View.GONE);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            protected void populateViewHolder(MyOrdersViewHolder viewHolder, final MdlOrder model, final int position) {

                viewHolder.setOrderDetails(getActivity(), model);
                viewHolder.setWashPackagesDetails(getActivity(), model.getWashPackagesDetails());
                viewHolder.setVehicleDetails(model.getVehicleDetails());
                viewHolder.setAddressDetails(getActivity(), model.getAddressDetails());
            }
        };
        mRecyclerView.setAdapter(mAdapter);
    }
}
