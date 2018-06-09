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
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Jalotsav on 5/21/2017.
 */

public class ActvtySplash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lo_actvty_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

//                int navDrwrPostn = getIntent().getIntExtra(AppConstants.PUT_EXTRA_NAVDRWER_POSTN, AppConstants.NAVDRWER_DASHBOARD);
                startActivity(new Intent(ActvtySplash.this, ActvtySignIn.class));
//                        .putExtra(AppConstants.PUT_EXTRA_NAVDRWER_POSTN, navDrwrPostn));
                finish();
            }
        }, 3000);
    }
}
