package com.ecosa.diseasediagnosisapp.util;

import android.util.Log;

/**
 * Created by Lekan Adigun on 12/21/2017.
 */

public final class L {

    /*
    *
    * Manager Log.xxx
    * */

    private static final String TAG = "DiagnosisApp";

    public static void fine(String m) {
        Log.d(TAG, m + "");
    }
    public static void wtf(String m) {
        fine(m);
    }
    public static void wtf(String message, Throwable throwable) {
        Log.d(TAG, message, throwable);
    }
    public static void wtf(Throwable throwable) {
        wtf("", throwable);
    }
}
