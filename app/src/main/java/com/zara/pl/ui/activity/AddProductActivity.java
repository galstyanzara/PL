package com.zara.pl.ui.activity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.zara.pl.R;
import com.zara.pl.db.entity.Product;
import com.zara.pl.db.handler.PlAsyncQueryHandler;
import com.zara.pl.util.Constant;

public class AddProductActivity extends BaseActivity implements View.OnClickListener, PlAsyncQueryHandler.AsyncQueryListener {

    // ===========================================================
    // Constants
    // ===========================================================

    private static final String LOG_TAG = AddProductActivity.class.getSimpleName();

    // ===========================================================
    // Fields
    // ===========================================================

    private Button mBtnAdd;
    private ImageView mIvProductLogo;
    private EditText mEdtName;
    private EditText mEdtPrice;
    private EditText mEdtDescription;
    private Product mProduct;
    private PlAsyncQueryHandler mPlAsyncQueryHandler;

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
        init();
        setListeners();
        customizeActionBar();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_add_product;
    }

    // ===========================================================
    // Click Listeners
    // ===========================================================

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_product_add:
                addProduct();
        }
    }

    // ===========================================================
    // Other Listeners, methods for/from Interfaces
    // ===========================================================

    @Override
    public void onQueryComplete(int token, Object cookie, Cursor cursor) {
    }

    @Override
    public void onInsertComplete(int token, Object cookie, Uri uri) {
        switch (token) {
            case PlAsyncQueryHandler.QueryToken.ADD_PRODUCT:
                Intent intent = new Intent();
                intent.putExtra(Constant.Extra.PRODUCT, mProduct);
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
    }

    @Override
    public void onUpdateComplete(int token, Object cookie, int result) {
    }

    @Override
    public void onDeleteComplete(int token, Object cookie, int result) {
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    // ===========================================================
    // Methods
    // ===========================================================

    private void setListeners() {
        mBtnAdd.setOnClickListener(this);
    }

    private void findViews() {
        mBtnAdd = (Button) findViewById(R.id.btn_product_add);
        mIvProductLogo = (ImageView) findViewById(R.id.iv_product_add_logo);
        mEdtName = (EditText) findViewById(R.id.edt_product_add_name);
        mEdtPrice = (EditText) findViewById(R.id.edt_product_add_price);
        mEdtDescription = (EditText) findViewById(R.id.edt_product_add_desc);
    }

    private void init() {
        mPlAsyncQueryHandler = new PlAsyncQueryHandler(AddProductActivity.this, this);

        Glide.with(this)
                .load(Constant.API.PRODUCT_ITEM_STATIC_IMAGE)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mIvProductLogo);
    }

    private void addProduct() {
        if (String.valueOf(mEdtName.getText()).trim().isEmpty()
                || String.valueOf(mEdtPrice.getText()).trim().isEmpty()
                || String.valueOf(mEdtDescription.getText()).trim().isEmpty()) {

            Toast.makeText(this, "Wrong Data", Toast.LENGTH_SHORT).show();

        } else {
            mProduct = new Product();
            mProduct.setId(System.currentTimeMillis());
            mProduct.setName(mEdtName.getText().toString());
            mProduct.setPrice(Long.parseLong(mEdtPrice.getText().toString()));
            mProduct.setDescription(mEdtDescription.getText().toString());
            mProduct.setImage(Constant.API.PRODUCT_ITEM_STATIC_IMAGE);
            mProduct.setFromUser(true);
            mPlAsyncQueryHandler.addProduct(mProduct);
        }
    }

    private void customizeActionBar() {
        setActionBarTitle(getString(R.string.text_add_product_name));
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================


}
