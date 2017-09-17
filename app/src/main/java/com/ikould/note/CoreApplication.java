package com.ikould.note;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Environment;
import android.os.Handler;
import android.os.UserManager;
import android.support.multidex.MultiDex;

import com.ikould.frame.application.BaseApplication;
import com.ikould.frame.config.BaseAppConfig;
import com.ikould.frame.database.DbOpenHelper;
import com.ikould.frame.utils.PLog;
import com.ikould.frame.utils.PackageUtil;
import com.ikould.frame.utils.PhoneTools;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * describe
 * Created by ikould on 2017/6/16.
 */

public class CoreApplication extends BaseApplication {

    // App启动Application时间
    public long startAppTime;

    // 整个应用统一调用
    public Handler handler = new Handler();
    // 单一线程池（可用于数据库读写操作）
    public Executor singleExecutor = Executors.newSingleThreadExecutor();
    // 最多含有5个线程的线程池
    public Executor mostExecutor = Executors.newFixedThreadPool(5);
    // 调用Application实体类
    private static CoreApplication instance;

    // 配置是否初始化
    private boolean isConfigInit;

    public static CoreApplication getInstance() {
        return instance;
    }

    @Override
    protected void onBaseCreate() {
        startAppTime = System.currentTimeMillis();
        instance = this;
        initConfig();
    }

    /**
     * 初始化配置，由WelcomeActivity获取权限后统一调用
     */
    public void initConfig() {
        PLog.d("CoreApplication", "initConfig: isConfigInit = " + isConfigInit);
        if (!isConfigInit && (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable())) {
            PLog.d("CoreApplication", "initConfig: ");
            isConfigInit = true;
            // 手机信息初始化
            PhoneTools.getPhoneInfo(this);
            // SharedPreference 初始化
            BaseAppConfig.getInstance().init(this);
            // 数据库初始化
            DbOpenHelper.init(this);
            // 崩溃拦截
            setCrashHandlerEnable(false);
        }
    }
}
