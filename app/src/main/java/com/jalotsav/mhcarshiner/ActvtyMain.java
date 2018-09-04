package com.jalotsav.mhcarshiner;

import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.jalotsav.mhcarshiner.common.AppConstants;
import com.jalotsav.mhcarshiner.common.UserSessionManager;
import com.jalotsav.mhcarshiner.nvgtnvwmain.FrgmntAboutUs;
import com.jalotsav.mhcarshiner.nvgtnvwmain.FrgmntContactUs;
import com.jalotsav.mhcarshiner.nvgtnvwmain.FrgmntHome;
import com.jalotsav.mhcarshiner.nvgtnvwmain.FrgmntMyOrders;
import com.jalotsav.mhcarshiner.nvgtnvwmain.FrgmntProfile;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Jalotsav on 6/9/2017.
 */

public class ActvtyMain extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar_drwrlyot_appbar_main) Toolbar mToolbar;
    @BindView(R.id.drwrlyot_nvgtndrwr_main) DrawerLayout mDrwrlyot;
    @BindView(R.id.navgtnvw_nvgtndrwr_main) NavigationView mNavgtnVw;

    @BindString(R.string.logout_sml) String mStrLogout;
    @BindString(R.string.logout_alrtdlg_msg) String mStrLogoutMsg;

    TextView mTvFullName, mTvEmailMobile;

    MenuItem mMenuItemHome, mMenuItemMyOrder, mMenuItemAboutUs, mMenuItemContactUs, mMenuItemProfile;
    UserSessionManager session;
    Bundle mBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lo_actvty_main);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        session = new UserSessionManager(this);

        if(session.checkLogin()) {

            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, mDrwrlyot, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            mDrwrlyot.addDrawerListener(toggle);
            toggle.syncState();

            mNavgtnVw.setNavigationItemSelectedListener(this);

            View mVwHeaderlyot = mNavgtnVw.getHeaderView(0);
            mTvFullName = mVwHeaderlyot.findViewById(R.id.tv_drwrlyot_header_fullname);
            mTvEmailMobile = mVwHeaderlyot.findViewById(R.id.tv_drwrlyot_header_emailmobile);
            mTvEmailMobile.setText(session.getMobile());

            mTvFullName.setText(session.getFirstName().concat(" ").concat(session.getLastName()));

            // load Projects fragment by default
            int navgtnPosition = getIntent().getIntExtra(AppConstants.PUT_EXTRA_NVGTNVW_POSTN, AppConstants.NVGTNVW_HOME);
            onNavigationItemSelected(getNavgtnvwPositionMenuItem(navgtnPosition));
        }
    }

    // Get MenuItem from BottomNavigationView Position, which is get from getIntent()
    private MenuItem getNavgtnvwPositionMenuItem(int navgtnPosition) {

        switch (navgtnPosition) {
            case AppConstants.NVGTNVW_HOME:

                return mNavgtnVw.getMenu().findItem(R.id.action_nvgtndrwr_main_home);
            case AppConstants.NVGTNVW_MYORDER:

                return mNavgtnVw.getMenu().findItem(R.id.action_nvgtndrwr_main_myorder);
            case AppConstants.NVGTNVW_CONTACTUS:

                return mNavgtnVw.getMenu().findItem(R.id.action_nvgtndrwr_main_contactus);
            default:

                return mNavgtnVw.getMenu().findItem(R.id.action_nvgtndrwr_main_home);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        Fragment fragment;
        if(mBundle == null) {

            mBundle = new Bundle();
            mBundle.putInt(AppConstants.PUT_EXTRA_COME_FROM, 0);
        }

        switch (item.getItemId()) {
            case R.id.action_nvgtndrwr_main_home:

                fragment = new FrgmntHome();
                mToolbar.setTitle(getString(R.string.home_sml));
                loadFragment(fragment, item);
                return true;
            case R.id.action_nvgtndrwr_main_myorder:

                fragment = new FrgmntMyOrders();
                mToolbar.setTitle(getString(R.string.myorders_sml));
                loadFragment(fragment, item);
                return true;
            case R.id.action_nvgtndrwr_main_aboutus:

                fragment = new FrgmntAboutUs();
                mToolbar.setTitle(getString(R.string.aboutus_sml));
                loadFragment(fragment, item);
                return true;
            case R.id.action_nvgtndrwr_main_contactus:

                fragment = new FrgmntContactUs();
                mToolbar.setTitle(getString(R.string.contactus_sml));
                loadFragment(fragment, item);
                return true;
            case R.id.action_nvgtndrwr_main_share:

                shareApp();
                return false;
            case R.id.action_nvgtndrwr_main_ratereview:

                giveRateAndReview();
                return false;
            case R.id.action_nvgtndrwr_main_profile:

                fragment = new FrgmntProfile();
                mToolbar.setTitle(getString(R.string.profile_sml));
                loadFragment(fragment, item);
                return true;
            case R.id.action_nvgtndrwr_main_logout:

                confirmLogoutAlertDialog();
                return false;
        }
        return false;
    }

    // Load Fragment of selected MenuItem
    private void loadFragment(Fragment fragment, MenuItem item) {

        if(fragment != null) {

            mDrwrlyot.closeDrawer(GravityCompat.START);

            getCurrentCheckedMenuItem().setChecked(false);
            item.setChecked(true);

            fragment.setArguments(mBundle);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.framlyot_drwrlyot_contnt_container, fragment)
                    .commit();
        }
    }

    // Get Current checked MenuItem of NavigationView
    public MenuItem getCurrentCheckedMenuItem() {

        mMenuItemHome = mNavgtnVw.getMenu().findItem(R.id.action_nvgtndrwr_main_home);
        mMenuItemMyOrder = mNavgtnVw.getMenu().findItem(R.id.action_nvgtndrwr_main_myorder);
        mMenuItemAboutUs = mNavgtnVw.getMenu().findItem(R.id.action_nvgtndrwr_main_aboutus);
        mMenuItemContactUs = mNavgtnVw.getMenu().findItem(R.id.action_nvgtndrwr_main_contactus);
        mMenuItemProfile = mNavgtnVw.getMenu().findItem(R.id.action_nvgtndrwr_main_profile);

        MenuItem currntSelctdMenuItem;
        if (mMenuItemHome.isChecked())
            currntSelctdMenuItem = mMenuItemHome;
        else if (mMenuItemMyOrder.isChecked())
            currntSelctdMenuItem = mMenuItemMyOrder;
        else if (mMenuItemAboutUs.isChecked())
            currntSelctdMenuItem = mMenuItemAboutUs;
        else if (mMenuItemContactUs.isChecked())
            currntSelctdMenuItem = mMenuItemContactUs;
        else if (mMenuItemProfile.isChecked())
            currntSelctdMenuItem = mMenuItemProfile;
        else
            currntSelctdMenuItem = mMenuItemHome;

        return currntSelctdMenuItem;
    }

    // Show SHARE select client for Sharing the App
    private void shareApp() {

        mDrwrlyot.closeDrawer(GravityCompat.START);

        Intent intntShare = new Intent(Intent.ACTION_SEND);
        intntShare.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intntShare.setType("text/plain");
        intntShare.putExtra(android.content.Intent.EXTRA_TEXT,
                "Hey, download this app from https://play.google.com/store/apps/details?id=" + this.getPackageName());
        startActivity(intntShare);
    }

    // Open app in Google Play store for RATE & REVIEW
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void giveRateAndReview() {

        mDrwrlyot.closeDrawer(GravityCompat.START);

        Uri uri = Uri.parse("market://details?id=" + this.getPackageName());
        Intent intntGPlayStore = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        intntGPlayStore.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(intntGPlayStore);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + this.getPackageName())));
        }
    }

    // Show AlertDialog for confirm to Logout
    private void confirmLogoutAlertDialog() {

        mDrwrlyot.closeDrawer(GravityCompat.START);

        AlertDialog.Builder alrtDlg = new AlertDialog.Builder(this);
        alrtDlg.setTitle(mStrLogout);
        alrtDlg.setMessage(mStrLogoutMsg);
        alrtDlg.setNegativeButton(getString(R.string.no_sml).toUpperCase(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alrtDlg.setPositiveButton(getString(R.string.logout_sml).toUpperCase(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();
                session.logoutUser();
            }
        });

        alrtDlg.show();
    }

    @Override
    public void onBackPressed() {

        if (mDrwrlyot.isDrawerOpen(GravityCompat.START))
            mDrwrlyot.closeDrawer(GravityCompat.START);
        else if (getCurrentCheckedMenuItem() != mMenuItemHome)
            onNavigationItemSelected(mNavgtnVw.getMenu().getItem(0));
        else
            super.onBackPressed();
    }
}
