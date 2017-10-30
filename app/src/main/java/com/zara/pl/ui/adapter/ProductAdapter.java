package com.zara.pl.ui.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zara.pl.R;
import com.zara.pl.db.entity.Product;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    // ===========================================================
    // Constants
    // ===========================================================

    private static final String LOG_TAG = ProductAdapter.class.getSimpleName();

    // ===========================================================
    // Fields
    // ===========================================================

    private ArrayList<Product> mProductList;
    private OnItemClickListener mOnItemClickListener;

    // ===========================================================
    // Constructors
    // ===========================================================

    public ProductAdapter(ArrayList<Product> mProductList, OnItemClickListener onItemClickListener) {
        this.mProductList = mProductList;
        this.mOnItemClickListener = onItemClickListener;
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass
    // ===========================================================

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.adapter_item_product, parent, false);
        return new ProductViewHolder(view, mProductList, mOnItemClickListener);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        holder.bind();
    }

    @Override
    public int getItemCount() {
        return mProductList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    // ===========================================================
    // Other Listeners, methods for/from Interfaces
    // ===========================================================

    // ===========================================================
    // Click Listeners
    // ===========================================================

    // ===========================================================
    // Methods
    // ===========================================================

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================

    static class ProductViewHolder extends RecyclerView.ViewHolder {

        Context context;
        TextView tvProductTitle;
        TextView tvProductPrice;
        ImageView ivProductImage;
        LinearLayout llItemContainer;
        OnItemClickListener onItemClickListener;
        ArrayList<Product> productArrayList;


        ProductViewHolder(View itemView, ArrayList<Product> productArrayList,
                          OnItemClickListener onItemClickListener) {
            super(itemView);
            this.productArrayList = productArrayList;
            this.onItemClickListener = onItemClickListener;
            this.context = itemView.getContext();
            findViews(itemView);
        }

        void findViews(View view) {
            llItemContainer = (LinearLayout) view.findViewById(R.id.ll_product_item_container);
            ivProductImage = (ImageView) view.findViewById(R.id.iv_product_item_logo);
            tvProductTitle = (TextView) view.findViewById(R.id.tv_product_item_title);
            tvProductPrice = (TextView) view.findViewById(R.id.tv_product_item_desc);
        }

        void bind() {
            tvProductPrice.setText(String.valueOf(productArrayList.get(getAdapterPosition()).getPrice()));
            tvProductTitle.setText(productArrayList.get(getAdapterPosition()).getName());

            Glide.with(context)
                    .load(productArrayList.get(getAdapterPosition()).getImage())
                    .into(ivProductImage);

            llItemContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(productArrayList.get(getAdapterPosition()), getAdapterPosition());
                }
            });

            llItemContainer.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onItemClickListener.onItemLongClick(productArrayList.get(getAdapterPosition()), getAdapterPosition());
                    return true;
                }
            });
        }
    }

    public interface OnItemClickListener {

        void onItemClick(Product product, int position);

        void onItemLongClick(Product product, int position);

    }

}
