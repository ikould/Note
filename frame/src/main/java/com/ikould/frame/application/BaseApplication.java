package com.ikould.frame.application;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.ikould.frame.handler.CrashFileSaveListener;
import com.ikould.frame.handler.CrashHandler;


/**
 * 实现自定义异常处理的Application
 */
public abstract class BaseApplication extends Application implements CrashFileSaveListener {

    protected CrashHandler crashHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        onBaseCreate();
    }

    /**
     * 崩溃拦截
     *
     * @param tf
     */
    protected void setCrashHandlerEnable(boolean tf) {
        if (tf) {
            /**
             * 设置默认异常处理Handler
             */
            crashHandler = CrashHandler.getInstance(this);
            crashHandler.init(getApplicationContext());
        }
    }

    protected abstract void onBaseCreate();

    @Override
    public void crashFileSaveTo(String filePath) {
        // 崩溃文件保存监听
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }
}
