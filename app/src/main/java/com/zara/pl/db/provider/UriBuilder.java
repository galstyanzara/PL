package com.zara.pl.db.provider;


import android.net.Uri;

public class UriBuilder {

    public static Uri buildProductUri(long id) {
        return Uri.parse("content://" + PlProvider.AUTHORITY + "/"
                + PlProvider.Path.PRODUCT_LOCATION + "/" + id);
    }

    public static Uri buildProductUri() {
        return Uri.parse("content://" + PlProvider.AUTHORITY + "/"
                + PlProvider.Path.PRODUCT_LOCATION);
    }

}
