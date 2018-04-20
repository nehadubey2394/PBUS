package com.pbus.utility;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.crashlytics.android.Crashlytics;
import com.pbus.helper.SessionManager;

import io.fabric.sdk.android.Fabric;

/**
 * Created by mindiii on 5/4/18.
 */

public class PBUS extends Application {

    public static PBUS instance = null;
    public static SessionManager sessionManager;
    // public static final String TAG = Impress.class.getSimpleName();

    public static synchronized PBUS getInstance() {
        if (instance != null) {
            return instance;
        }
        return new PBUS();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());

        instance = this;
        sessionManager = new SessionManager(instance.getApplicationContext());

    }



}