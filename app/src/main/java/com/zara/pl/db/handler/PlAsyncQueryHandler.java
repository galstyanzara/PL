package com.zara.pl.db.handler;


import android.content.AsyncQueryHandler;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.zara.pl.db.PlDataBase;
import com.zara.pl.db.entity.Product;
import com.zara.pl.db.provider.UriBuilder;
import com.zara.pl.util.AppUtil;

import java.lang.ref.WeakReference;

public class PlAsyncQueryHandler extends AsyncQueryHandler {

    // ===========================================================
    // Constants
    // ===========================================================

    private final static String LOG_TAG = PlAsyncQueryHandler.class.getSimpleName();

    public static class QueryToken {
        public static final int GET_PRODUCT = 100;
        public static final int GET_PRODUCTS = 101;
        public static final int ADD_PRODUCT = 102;
        public static final int UPDATE_PRODUCT = 104;
        public static final int DELETE_PRODUCT = 105;
        public static final int DELETE_PRODUCTS = 106;
        public static final int GET_FAVORITE_PRODUCTS = 107;
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================

    public interface AsyncQueryListener {
        void onQueryComplete(int token, Object cookie, Cursor cursor);

        void onInsertComplete(int token, Object cookie, Uri uri);

        void onUpdateComplete(int token, Object cookie, int result);

        void onDeleteComplete(int token, Object cookie, int result);
    }

    // ===========================================================
    // Fields
    // ===========================================================

    private WeakReference<AsyncQueryListener> mQueryListenerReference;

    // ===========================================================
    // Constructors
    // ===========================================================

    public PlAsyncQueryHandler(Context context,
                               AsyncQueryListener queryListenerReference) {
        super(context.getContentResolver());
        mQueryListenerReference = new WeakReference<>(queryListenerReference);
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass
    // ===========================================================

    @Override
    protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
        final AsyncQueryListener queryListener = mQueryListenerReference.get();
        if (queryListener != null) {
            queryListener.onQueryComplete(token, cookie, cursor);
        } else if (cursor != null) {
            cursor.close();
        }
    }

    @Override
    protected void onInsertComplete(int token, Object cookie, Uri uri) {
        final AsyncQueryListener queryListener = mQueryListenerReference.get();
        if (queryListener != null) {
            queryListener.onInsertComplete(token, cookie, uri);
        }
    }

    @Override
    protected void onUpdateComplete(int token, Object cookie, int result) {
        final AsyncQueryListener queryListener = mQueryListenerReference.get();
        if (queryListener != null) {
            queryListener.onUpdateComplete(token, cookie, result);
        }
    }

    @Override
    protected void onDeleteComplete(int token, Object cookie, int result) {
        final AsyncQueryListener queryListener = mQueryListenerReference.get();
        if (queryListener != null) {
            queryListener.onDeleteComplete(token, cookie, result);
        }
    }

    // ===========================================================
    // Listeners, methods for/from Interfaces
    // ===========================================================

    // ===========================================================
    // Methods systems
    // ===========================================================

    // ===========================================================
    // Methods controls
    // ===========================================================

    /**
     * PRODUCT Methods
     *************************************************************/

    public synchronized void getProduct(long id) {
        startQuery(
                QueryToken.GET_PRODUCT,
                null,
                UriBuilder.buildProductUri(),
                PlDataBase.Projection.PRODUCT,
                PlDataBase.PRODUCT_ID + "=?",
                new String[]{String.valueOf(id)},
                null
        );
    }

    public synchronized void getProducts() {
        startQuery(
                QueryToken.GET_PRODUCTS,
                null,
                UriBuilder.buildProductUri(),
                PlDataBase.Projection.PRODUCT,
                null,
                null,
                null
        );
    }

    public synchronized void addProduct(Product product) {
        startInsert(
                QueryToken.ADD_PRODUCT,
                null,
                UriBuilder.buildProductUri(),
                PlDataBase.composeValues(product, PlDataBase.ContentValuesType.PRODUCTS)
        );
    }

    public synchronized void updateProduct(Product product) {
        startUpdate(
                QueryToken.UPDATE_PRODUCT,
                null,
                UriBuilder.buildProductUri(),
                PlDataBase.composeValues(product, PlDataBase.ContentValuesType.PRODUCTS),
                PlDataBase.PRODUCT_ID + "=?",
                new String[]{String.valueOf(product.getId())}
        );
    }

    public synchronized void deleteProduct(Product product, Object cookie) {
        startDelete(
                QueryToken.DELETE_PRODUCT,
                null,
                UriBuilder.buildProductUri(),
                PlDataBase.PRODUCT_ID + "=?",
                new String[]{String.valueOf(product.getId())}
        );
    }

    public synchronized void deleteProducts() {
        startDelete(
                QueryToken.DELETE_PRODUCTS,
                null,
                UriBuilder.buildProductUri(),
                null,
                null
        );
    }

    public void getAllFavoriteProducts() {
        startQuery(
                QueryToken.GET_FAVORITE_PRODUCTS,
                null,
                UriBuilder.buildProductUri(),
                PlDataBase.Projection.PRODUCT,
                PlDataBase.PRODUCT_FAVORITE + "=?",
                new String[]{String.valueOf(AppUtil.booleanToInt(true))},
                null
        );
    }

    // ===========================================================
    // Methods helpers
    // ===========================================================

}
