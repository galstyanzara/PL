package com.zara.pl.ui.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.common.eventbus.Subscribe;
import com.zara.pl.R;
import com.zara.pl.db.cursor.CursorReader;
import com.zara.pl.db.entity.Product;
import com.zara.pl.db.handler.PlAsyncQueryHandler;
import com.zara.pl.io.bus.BusProvider;
import com.zara.pl.io.bus.event.ApiEvent;
import com.zara.pl.io.service.PLIntentService;
import com.zara.pl.ui.activity.AddProductActivity;
import com.zara.pl.ui.activity.ProductActivity;
import com.zara.pl.ui.adapter.ProductAdapter;
import com.zara.pl.util.AppUtil;
import com.zara.pl.util.Constant;
import com.zara.pl.util.NetworkUtil;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;


public class ProductListFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener,
        ProductAdapter.OnItemClickListener, View.OnClickListener, PlAsyncQueryHandler.AsyncQueryListener {

    // ===========================================================
    // Constants
    // ===========================================================

    private static final String LOG_TAG = ProductListFragment.class.getSimpleName();
    private static final int REQUEST_CODE = 100;

    // ===========================================================
    // Fields
    // ===========================================================

    private RecyclerView mRecyclerView;
    private FloatingActionButton mFloatingActionButton;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private PlAsyncQueryHandler mPlAsyncQueryHandler;
    private ProductAdapter mRecyclerViewAdapter;
    private ArrayList<Product> mProductList;

    // ===========================================================
    // Constructors
    // ===========================================================

    public static ProductListFragment newInstance() {
        return new ProductListFragment();
    }

    public static ProductListFragment newInstance(Bundle args) {
        ProductListFragment fragment = new ProductListFragment();
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
        View view = inflater.inflate(R.layout.fragment_product_list, container, false);
        BusProvider.register(this);
        findViews(view);
        init();
        setListeners();
        loadData();
        return view;
    }

    @Override
    public void onResume() {
        mPlAsyncQueryHandler.getProducts();
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        BusProvider.unregister(this);
        super.onDestroyView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            Product product = data.getParcelableExtra(Constant.Extra.PRODUCT);
            AppUtil.sendNotification(getContext(),
                    getActivity(),
                    getResources().getString(R.string.app_name),
                    getString(R.string.text_notification_added_product),
                    product.getName()
            );
            mProductList.add(product);
            mRecyclerViewAdapter.notifyDataSetChanged();
        }
    }

    // ===========================================================
    // Click Listeners
    // ===========================================================

    @Override
    public void onItemClick(Product product, int position) {
        Intent intent = new Intent(getActivity(), ProductActivity.class);
        intent.putExtra(Constant.Extra.PRODUCT_ID, product.getId());
        this.startActivity(intent);
    }

    @Override
    public void onItemLongClick(Product product, int position) {
        openDeleteProductDialog(product, position);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fl_btn_product_add:
                Intent intent = new Intent(getActivity(), AddProductActivity.class);
                this.startActivityForResult(intent, REQUEST_CODE);
                break;
        }
    }

    // ===========================================================
    // Other Listeners, methods for/from Interfaces
    // ===========================================================

    @Subscribe
    public void onEventReceived(ApiEvent<Object> apiEvent) {
        if (apiEvent.isSuccess()) {
            mPlAsyncQueryHandler.getProducts();
        } else {
            Toast.makeText(getActivity(), R.string.msg_some_error,
                    Toast.LENGTH_SHORT).show();
        }
        mSwipeRefreshLayout.setRefreshing(false);
    }


    @Override
    public void onRefresh() {
        if (NetworkUtil.getInstance().isConnected(getContext())) {
            PLIntentService.start(
                    getActivity(),
                    Constant.API.PRODUCT_LIST,
                    Constant.RequestType.PRODUCT_LIST
            );
            mSwipeRefreshLayout.setRefreshing(false);

        } else {
            mProductList.clear();
            mRecyclerViewAdapter.notifyDataSetChanged();
            mFloatingActionButton.hide();
            Toast.makeText(getContext(), R.string.msg_network_connection_error, Toast.LENGTH_LONG).show();
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onQueryComplete(int token, Object cookie, Cursor cursor) {
        switch (token) {
            case PlAsyncQueryHandler.QueryToken.GET_PRODUCTS:
                ArrayList<Product> products = CursorReader.parseProducts(cursor);
                mProductList.clear();
                mProductList.addAll(products);
                mRecyclerViewAdapter.notifyDataSetChanged();
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
    }

    // ===========================================================
    // Methods
    // ===========================================================

    private void loadData() {
        if (NetworkUtil.getInstance().isConnected(getActivity())) {
            mSwipeRefreshLayout.setRefreshing(true);
            PLIntentService.start(
                    getActivity(),
                    Constant.API.PRODUCT_LIST,
                    Constant.RequestType.PRODUCT_LIST
            );

        } else {
            mPlAsyncQueryHandler.getProducts();
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    private void setListeners() {
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mFloatingActionButton.setOnClickListener(this);
    }

    private void findViews(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_fragment);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.sw_fragment_product_list);
        mFloatingActionButton = (FloatingActionButton) view.findViewById(R.id.fl_btn_product_add);
    }

    private void init() {
        mPlAsyncQueryHandler = new PlAsyncQueryHandler(getActivity().getApplicationContext(), this);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mProductList = new ArrayList<>();
        mRecyclerViewAdapter = new ProductAdapter(mProductList, this);
        mRecyclerView.setAdapter(mRecyclerViewAdapter);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    private void openDeleteProductDialog(final Product product, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.msg_delete_product)
                .setCancelable(false)
                .setPositiveButton(R.string.text_delete_product_yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mPlAsyncQueryHandler.deleteProduct(product, position);
                        mProductList.remove(position);
                        mRecyclerViewAdapter.notifyItemRemoved(position);
                        dialog.cancel();
                    }
                })
                .setNegativeButton(R.string.text_delete_product_no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================

}




