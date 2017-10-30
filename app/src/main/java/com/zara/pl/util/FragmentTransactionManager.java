package com.zara.pl.util;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

public class FragmentTransactionManager {

    public static void displayFragment(FragmentManager fragmentManager, Fragment fragment,
                                       int view, boolean mustAddToBackStack) {
        if (mustAddToBackStack) {
            fragmentManager.beginTransaction()
                    .addToBackStack(null)
                    .replace(view, fragment, fragment.getClass().getSimpleName())
                    .commitAllowingStateLoss();

        } else {
            fragmentManager.beginTransaction()
                    .replace(view, fragment, fragment.getClass().getSimpleName())
                    .commitAllowingStateLoss();
        }
    }
}