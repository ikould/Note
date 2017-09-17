package com.ikould.note.welcome;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.ikould.frame.activity.BaseActivity;
import com.ikould.frame.utils.PLog;
import com.ikould.frame.utils.ToastUtils;
import com.ikould.note.CoreApplication;
import com.ikould.note.R;
import com.ikould.note.main.MainActivity;

import java.util.Arrays;

/**
 * 欢迎模块
 * <p>
 * Created by ikould on 2017/5/31.
 */

public class WelcomeActivity extends BaseActivity {

    public static final int REQUEST_WRITE = 100;

    @Override
    protected void onBaseCreate(Bundle savedInstanceState) {
        setBaseContentView(R.layout.activity_welcome);
        initPermissions();
    }

    /**
     * 初始化权限
     */
    private void initPermissions() {
        PLog.d("WelcomeActivity", "initPermissions: ");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 没有读写权限或者读取手机状态的权限则申请
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE}, REQUEST_WRITE);
                return;
            }
        }
        init();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        PLog.d("WelcomeActivity", "onRequestPermissionsResult: 权限结果" + Arrays.toString(grantResults));
        if (requestCode == REQUEST_WRITE) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 权限请求成功的操作
                init();
            } else { // 读写权限如果被拒绝，直接关闭
                // 权限请求失败的操作
                CoreApplication.getInstance().handler.postDelayed(() -> {
                    ToastUtils.show(WelcomeActivity.this, "请在设置中手动打开读写权限");
                    finish();
                }, 1000);
            }
        }
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
