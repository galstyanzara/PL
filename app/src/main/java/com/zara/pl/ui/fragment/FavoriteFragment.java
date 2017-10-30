package com.zara.pl.ui.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.common.eventbus.Subscribe;
import com.zara.pl.R;
import com.zara.pl.db.cursor.CursorReader;
import com.zara.pl.db.entity.Product;
import com.zara.pl.db.handler.PlAsyncQueryHandler;
import com.zara.pl.io.bus.BusProvider;
import com.zara.pl.ui.activity.ProductActivity;
import com.zara.pl.ui.adapter.ProductAdapter;
import com.zara.pl.util.Constant;

import java.util.ArrayList;

public class FavoriteFragment extends BaseFragment implements View.OnClickListener,
        PlAsyncQueryHandler.AsyncQueryListener, ProductAdapter.OnItemClickListener {

    // ===========================================================
    // Constants
    // ===========================================================

    private static final String LOG_TAG = FavoriteFragment.class.getSimpleName();

    // ===========================================================
    // Fields
    // ===========================================================

    private RecyclerView mRecyclerView;
    private ProductAdapter mProductAdapter;
    private ArrayList<Product> mProductList;
    private PlAsyncQueryHandler mPlAsyncQueryHandler;

    // ===========================================================
    // Constructors
    // ===========================================================

    public static FavoriteFragment newInstance() {
        return new FavoriteFragment();
    }

    public static FavoriteFragment newInstance(Bundle args) {
        FavoriteFragment fragment = new FavoriteFragment();
        fragment.setArguments(args);
        return fragment;
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass
    // ===========================================================

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite_list, container, false);
        BusProvider.register(this);
        findViews(view);
        init();
        setListeners();
        loadData();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPlAsyncQueryHandler.getAllFavoriteProducts();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        BusProvider.unregister(this);
    }

    // ===========================================================
    // Click Listeners
    // ===========================================================

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }

    @Override
    public void onItemClick(Product product, int position) {
        Intent intent = new Intent(getContext(), ProductActivity.class);
        intent.putExtra(Constant.Extra.PRODUCT_ID, product.getId());
        this.startActivity(intent);
    }

    @Override
    public void onItemLongClick(Product product, int position) {
        mPlAsyncQueryHandler.deleteProduct(product, position);
    }

    // ===========================================================
    // Other Listeners, methods for/from Interfaces
    // ===========================================================

    @Subscribe
    public void onEventReceived(ArrayList<Product> productArrayList) {
        mProductList.clear();
        mProductList.addAll(productArrayList);
        mProductAdapter.notifyDataSetChanged();
    }

    @Override
    public void onQueryComplete(int token, Object cookie, Cursor cursor) {
        switch (token) {
            case PlAsyncQueryHandler.QueryToken.GET_FAVORITE_PRODUCTS:
                ArrayList<Product> products = CursorReader.parseProducts(cursor);
                mProductList.clear();
                mProductList.addAll(products);
                mProductAdapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    public void onInsertComplete(int token, Object cookie, Uri uri) {

    }

    @Override
    public void onUpdateComplete(int token, Object cookie, int result) {

    }

    @Override
    public void onDeleteComplete(int token, Object cookie, int result) {
        switch (token) {
            case PlAsyncQueryHandler.QueryToken.DELETE_PRODUCT:
                int position = (int) cookie;
                mProductList.remove(position);
                mProductAdapter.notifyItemRemoved(position);
                break;
        }

    }

    // ===========================================================
    // Methods
    // ===========================================================

    private void setListeners() {
    }

    private void findViews(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_fv_list);
    }

    private void loadData() {
        mPlAsyncQueryHandler.getAllFavoriteProducts();
    }

    private void init() {
        mPlAsyncQueryHandler = new PlAsyncQueryHandler(getActivity().getApplicationContext(), this);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

        mProductList = new ArrayList<>();
        mProductAdapter = new ProductAdapter(mProductList, this);
        mRecyclerView.setAdapter(mProductAdapter);
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}