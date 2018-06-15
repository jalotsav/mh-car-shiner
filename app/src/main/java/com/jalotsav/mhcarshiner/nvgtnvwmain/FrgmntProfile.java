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

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.jalotsav.mhcarshiner.R;
import com.jalotsav.mhcarshiner.common.UserSessionManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Jalotsav on 6/12/2017.
 */

public class FrgmntProfile extends Fragment {

    @BindView(R.id.cordntrlyot_frgmnt_profile) CoordinatorLayout mCrdntrlyot;

    @BindView(R.id.tv_frgmnt_profile_firstcharname) TextView mTvFirstCharName;
    @BindView(R.id.tv_frgmnt_profile_fullname) TextView mTvFullName;
    @BindView(R.id.tv_frgmnt_profile_mobile) TextView mTvmobile;
    @BindView(R.id.tv_frgmnt_profile_email) TextView mTvEmail;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.lo_frgmnt_profile, container, false);
        ButterKnife.bind(this, rootView);

        UserSessionManager session = new UserSessionManager(getActivity());
        mTvFirstCharName.setText(session.getFirstName().substring(0,1).toUpperCase());
        mTvFullName.setText(session.getFirstName().concat(" ").concat(session.getLastName()));
        mTvmobile.setText(session.getMobile());
        mTvEmail.setText(session.getEmail());

        return rootView;
    }

    @OnClick({R.id.fab_frgmnt_profile_edit})
    public void onClickView(View view) {

        switch (view.getId()) {
            case R.id.fab_frgmnt_profile_edit:

                Toast.makeText(getActivity(), "The work is under way.", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
