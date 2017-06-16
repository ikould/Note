package com.ikould.note;

import android.app.Application;

import org.litepal.LitePal;

/**
 * describe
 * Created by ikould on 2017/6/16.
 */

public class CoreApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        LitePal.initialize(this);
    }
}
