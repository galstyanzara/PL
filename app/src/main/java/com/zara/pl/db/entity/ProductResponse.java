package com.zara.pl.db.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


public class ProductResponse {

    @SerializedName("products")
    private ArrayList<Product> productList;

    public ProductResponse() {
    }

    public ProductResponse(ArrayList<Product> productList) {
        this.productList = productList;
    }

    public ArrayList<Product> getProductList() {
        return productList;
    }

    public void setProductList(ArrayList<Product> productList) {
        this.productList = productList;
    }
}
