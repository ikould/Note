package com.ikould.note.welcome;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;

import com.ikould.frame.activity.BaseActivity;
import com.ikould.frame.utils.ToastUtils;
import com.ikould.note.CoreApplication;
import com.ikould.note.R;
import com.ikould.note.main.MainActivity;

/**
 * 欢迎模块
 * <p>
 * Created by ikould on 2017/5/31.
 */

public class WelcomeActivity extends BaseActivity {

    @Override
    protected void onBaseCreate(Bundle savedInstanceState) {
        setBaseContentView(R.layout.activity_welcome);
        initPermissions();
    }

    /**
     * 初始化权限
     */
    private void initPermissions() {
        checkPermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE}, isSuccess -> {
            if (isSuccess) {
                init();
            } else {
                CoreApplication.getInstance().handler.postDelayed(() -> {
                    ToastUtils.show(WelcomeActivity.this, "请在设置中手动打开读写权限");
                    finish();
                }, 1000);
            }
        });
    }

    /**
     * 正式初始化
     */
    private void init() {
        CoreApplication.getInstance().initConfig();
        initConfig();
    }

    /**
     * 初始化配置
     */
    private void initConfig() {
        Log.d("WelcomeActivity", "initConfig: ");
        CoreApplication.getInstance().handler.postDelayed(() -> {
            Log.d("WelcomeActivity", "run: ");
            // 跳转到MainActivity
            goToActivity(MainActivity.class, true);
        }, 2000);
    }
}
