package com.zara.pl.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.zara.pl.R;
import com.zara.pl.io.bus.BusProvider;
import com.zara.pl.util.Preference;

public class AuthActivity extends BaseActivity implements View.OnClickListener {

    // ===========================================================
    // Constants
    // ===========================================================

    private static final String LOG_TAG = AuthActivity.class.getSimpleName();

    // ===========================================================
    // Fields
    // ===========================================================

    private Button mBtnSign;
    private TextInputLayout mTilEmail;
    private TextInputEditText mTietEmail;
    private TextInputLayout mTilPass;
    private TextInputEditText mTietPass;

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
    protected void onDestroy() {
        super.onDestroy();
        BusProvider.unregister(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BusProvider.register(this);
        findViews();
        setListeners();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_auth;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // ===========================================================
    // Other Listeners, methods for/from Interfaces
    // ===========================================================

    // ===========================================================
    // Click Listeners
    // ===========================================================

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_auth_sign:
                String mail = mTietEmail.getText().toString();
                String pass = mTietPass.getText().toString();
                grabDataAndSingIn(mail, pass);
                break;
        }
    }

    // ===========================================================
    // Methods
    // ===========================================================

    private void setListeners() {
        mBtnSign.setOnClickListener(this);
    }

    private void findViews() {
        mTilEmail = (TextInputLayout) findViewById(R.id.til_auth_email);
        mTietEmail = (TextInputEditText) findViewById(R.id.tiet_auth_email);
        mTilPass = (TextInputLayout) findViewById(R.id.til_auth_pass);
        mTietPass = (TextInputEditText) findViewById(R.id.tiet_auth_pass);
        mBtnSign = (Button) findViewById(R.id.btn_auth_sign);
    }

    private void grabDataAndSingIn(String mail, String pass) {
        boolean isValidationSucceeded = true;

        // validate password (empty or not)
        if (mail.trim().length() == 0) {
            isValidationSucceeded = false;
            mTilEmail.setError(getString(R.string.msg_authActivity_error_mail_edt));
        }

        // validate email (empty or not)
        if (pass.trim().length() == 0) {
            isValidationSucceeded = false;
            mTilPass.setError(getString(R.string.msg_authActivity_error_pass_edt));
        }

        // if required fields are filled up then proceed with login
        if (isValidationSucceeded) {

            mTilEmail.setErrorEnabled(false);
            mTilPass.setErrorEnabled(false);

            Preference.getInstance(this).setUserMail(mail);
            Preference.getInstance(this).setUserPass(pass);

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================

}