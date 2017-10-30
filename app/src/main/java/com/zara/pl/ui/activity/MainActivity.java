package com.zara.pl.ui.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.zara.pl.R;
import com.zara.pl.ui.fragment.FavoriteFragment;
import com.zara.pl.ui.fragment.ProductListFragment;
import com.zara.pl.util.Constant;
import com.zara.pl.util.FragmentTransactionManager;
import com.zara.pl.util.Preference;


public class MainActivity extends BaseActivity implements View.OnClickListener,
        NavigationView.OnNavigationItemSelectedListener {

    // ===========================================================
    // Constants
    // ===========================================================

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    // ===========================================================
    // Fields
    // ===========================================================

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;

    // ===========================================================
    // Constructors
    // ===========================================================

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass
    // ===========================================================

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findViews();
        setListeners();
        customizeActionBar();
        initDrawer();
        openScreen(
                ProductListFragment.newInstance(),
                R.id.nav_product_list,
                false);
        catchNotificationData();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main;
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    // ===========================================================
    // Observer callback
    // ===========================================================

    // ===========================================================
    // Observer methods
    // ===========================================================

    // ===========================================================
    // Other Listeners, methods for/from Interfaces
    // ===========================================================

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_product_list:
                openScreen(
                        ProductListFragment.newInstance(),
                        R.id.nav_product_list, false
                );
                break;
            case R.id.nav_fv_list:
                openScreen(
                        FavoriteFragment.newInstance(),
                        R.id.nav_fv_list,
                        true
                );
                break;
            case R.id.nav_logout:
                logout();
                break;
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    // ===========================================================
    // Click Listeners
    // ===========================================================

    @Override
    public void onClick(View v) {
    }

    // ===========================================================
    // Methods
    // ===========================================================

    private void setListeners() {
        mNavigationView.setNavigationItemSelectedListener(this);
    }

    private void findViews() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.dl_main);
        mNavigationView = (NavigationView) findViewById(R.id.nav_main);
    }

    private void customizeActionBar() {
        setActionBarTitle(getString(R.string.app_name));
    }

    private void initDrawer() {
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                getToolBar(),
                R.string.msg_navigation_drawer_open,
                R.string.msg_navigation_drawer_close
        );
        mDrawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    private void openScreen(Fragment fragment, int item, boolean addToBackStack) {
        mNavigationView.getMenu().findItem(item).setChecked(true);
        FragmentTransactionManager.displayFragment(
                getSupportFragmentManager(),
                fragment,
                R.id.fl_main_container,
                addToBackStack
        );
    }

    private void catchNotificationData() {
        String notificationMessage = getIntent().getStringExtra(Constant.Extra.NOTIFICATION_DATA);
        if (notificationMessage != null) {
            Toast.makeText(this, notificationMessage, Toast.LENGTH_LONG).show();
        }
    }

    private void logout() {
        Preference.getInstance(this).setUserMail(null);
        Preference.getInstance(this).setUserPass(null);
        Intent intent = new Intent(this, AuthActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================


}
