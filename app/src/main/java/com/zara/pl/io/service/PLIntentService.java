package com.zara.pl.io.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;
import com.zara.pl.db.entity.Product;
import com.zara.pl.db.entity.ProductResponse;
import com.zara.pl.db.handler.PlQueryHandler;
import com.zara.pl.io.bus.BusProvider;
import com.zara.pl.io.bus.event.ApiEvent;
import com.zara.pl.io.server.HttpRequestManager;
import com.zara.pl.io.server.HttpResponseUtil;
import com.zara.pl.util.Constant;
import com.zara.pl.util.Preference;

import java.net.HttpURLConnection;
import java.util.ArrayList;


public class PLIntentService extends IntentService {

    private static final String LOG_TAG = PLIntentService.class.getSimpleName();

    // ===========================================================
    // Fields
    // ===========================================================

    // ===========================================================
    // Constructors
    // ===========================================================

    public PLIntentService() {
        super(PLIntentService.class.getName());
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Listeners, methods for/from Interfaces
    // ===========================================================

    // ===========================================================
    // Start/stop commands
    // ===========================================================

    /**
     * @param url         - calling api url
     * @param requestType - string constant that helps us to distinguish what request it is
     * @param postEntity  - POST request entity (json string that must be sent on server)
     */
    public static void start(Context context, String url, String postEntity,
                             int requestType) {
        Intent intent = new Intent(context, PLIntentService.class);
        intent.putExtra(Constant.Extra.URL, url);
        intent.putExtra(Constant.Extra.REQUEST_TYPE, requestType);
        intent.putExtra(Constant.Extra.POST_ENTITY, postEntity);
        context.startService(intent);
    }

    public static void start(Context context, String url,
                             int requestType) {
        Intent intent = new Intent(context, PLIntentService.class);
        intent.putExtra(Constant.Extra.URL, url);
        intent.putExtra(Constant.Extra.REQUEST_TYPE, requestType);
        context.startService(intent);
    }

    // ===========================================================
    // Methods for/from SuperClass
    // ===========================================================

    @Override
    protected void onHandleIntent(Intent intent) {
        String url = intent.getExtras().getString(Constant.Extra.URL);
        String data = intent.getExtras().getString(Constant.Extra.POST_ENTITY);
        int requestType = intent.getExtras().getInt(Constant.Extra.REQUEST_TYPE);
        Log.i(LOG_TAG, requestType + Constant.Symbol.SPACE + url);

        HttpURLConnection connection;

        switch (requestType) {
            case Constant.RequestType.PRODUCT_LIST:

                // calling API
                connection = HttpRequestManager.executeRequest(
                        url,
                        Constant.RequestMethod.GET,
                        null
                );

                // parse API result to get json string
                String jsonList = HttpResponseUtil.parseResponse(connection);

                // deserialize json string to model
                ProductResponse productResponse = new Gson().fromJson(jsonList, ProductResponse.class);

                // check server data (null if something went wrong)
                if (productResponse != null) {

                    // get all products
                    ArrayList<Product> products = productResponse.getProductList();

                    // restore favorites
                    for (Product product : products) {
                        if (Preference.getInstance(getApplicationContext()).
                                getUserFavorites(String.valueOf(product.getId()))) {
                            product.setFavorite(true);
                        }
                    }

                    // add all products into db
                    PlQueryHandler.addProducts(this, products);

                    // post to UI
                    BusProvider.getInstance().post(new ApiEvent<>(ApiEvent.EventType.PRODUCT_LIST_LOADED, true, products));

                } else {
                    BusProvider.getInstance().post(new ApiEvent<>(ApiEvent.EventType.PRODUCT_LIST_LOADED, false));
                }

                break;
            case Constant.RequestType.PRODUCT_ITEM:

                connection = HttpRequestManager.executeRequest(
                        url,
                        Constant.RequestMethod.GET,
                        null
                );

                String jsonItem = HttpResponseUtil.parseResponse(connection);

                Product product = new Gson().fromJson(jsonItem, Product.class);

                if (product != null) {
                    PlQueryHandler.updateProductDescription(this, product);
                    BusProvider.getInstance().post(new ApiEvent<>(ApiEvent.EventType.PRODUCT_ITEM_LOADED, true, product));

                } else {
                    BusProvider.getInstance().post(new ApiEvent<>(ApiEvent.EventType.PRODUCT_LIST_LOADED, false));
                }
                break;
        }
    }

    // ===========================================================
    // Methods
    // ===========================================================

    // ===========================================================
    // Util
    // ===========================================================

}