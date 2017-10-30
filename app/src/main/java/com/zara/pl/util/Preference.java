package com.zara.pl.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Preference {

    // ===========================================================
    // Constants
    // ===========================================================

    private static final String PREF_USER_MAIL = "PREF_USER_MAIL";
    private static final String PREF_USER_PASS = "PREF_USER_PASS";

    // ===========================================================
    // Fields
    // ===========================================================

    private static Preference sInstance;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    // ===========================================================
    // Constructors
    // ===========================================================

    private Preference(Context context) {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        mEditor = mSharedPreferences.edit();
    }

    public static Preference getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new Preference(context);
        }

        return sInstance;
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    public void setUserMail(String userMail) {
        mEditor.putString(PREF_USER_MAIL, userMail);
        mEditor.apply();
    }
    public void setUserFave(String key,Boolean userFave) {
        mEditor.putBoolean(key, userFave);
        mEditor.apply();
    }
    public void deleteUserFave(String key) {
        mEditor.remove(key);
        mEditor.apply();
    }

    public String getUserMail() {
        return mSharedPreferences.getString(PREF_USER_MAIL, null);
    }

    public Boolean getUserFavorites(String key) {
        return mSharedPreferences.getBoolean(key, false);
    }

    public void setUserPass(String userName) {
        mEditor.putString(PREF_USER_PASS, userName);
        mEditor.apply();
    }

    public String getUserPass() {
        return mSharedPreferences.getString(PREF_USER_PASS, null);
    }

    // ===========================================================
    // Listeners
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    // ===========================================================
    // Methods
    // ===========================================================

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================

}
