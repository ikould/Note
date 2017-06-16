package com.ikould.note.welcome;

import android.os.Bundle;
import android.util.Log;

import com.ikould.frame.activity.BaseActivity;
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
        initConfig();
    }

    /**
     * 初始化配置
     */
    private void initConfig() {
        Log.d("WelcomeActivity", "initConfig: ");
        mContentView.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d("WelcomeActivity", "run: ");
                // 跳转到MainActivity
                redirectActivity(MainActivity.class, true);
            }
        }, 2000);
    }
}
