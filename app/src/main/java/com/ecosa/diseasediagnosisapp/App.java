package com.ecosa.diseasediagnosisapp;

import android.app.Application;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Lekan Adigun on 1/10/2018.
 */

public class App extends Application {

    private ExecutorService executorService = Executors.newCachedThreadPool();
    private static App app;
    @Override
    public void onCreate() {
        super.onCreate();

        app = this;
    }

    public static App getApp() {
        return app;
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }
}
