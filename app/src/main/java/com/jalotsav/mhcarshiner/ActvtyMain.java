package com.jalotsav.mhcarshiner;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * Created by Jalotsav on 5/21/2017.
 */

public class ActvtyMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lo_actvty_main);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

//                int navDrwrPostn = getIntent().getIntExtra(AppConstants.PUT_EXTRA_NAVDRWER_POSTN, AppConstants.NAVDRWER_DASHBOARD);
                startActivity(new Intent(ActvtyMain.this, ActvtySignIn.class));
//                        .putExtra(AppConstants.PUT_EXTRA_NAVDRWER_POSTN, navDrwrPostn));
                finish();
            }
        }, 3000);
    }
}
