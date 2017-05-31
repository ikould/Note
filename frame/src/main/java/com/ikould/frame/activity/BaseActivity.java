package com.ikould.frame.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;

import com.ikould.frame.utils.KeyBoardUtils;

/**
 * Activity基类
 * <p>
 * Created by liudong on 2017/5/31.
 */
public abstract class BaseActivity extends AppCompatActivity {

    public View mContentView;

    protected abstract void onBaseCreate(Bundle savedInstanceState);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 隐藏系统标题栏
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.hide();
        onBaseCreate(savedInstanceState);
    }

    /**
     * 获取ContentView
     *
     * @return
     */
    protected void setBaseContentView(@LayoutRes int layoutId) {
        mContentView = LayoutInflater.from(this).inflate(layoutId, null);
        setContentViewConfig();
    }

    /**
     * 获取ContentView
     *
     * @return
     */
    protected void setBaseContentView(View view) {
        mContentView = view;
        setContentViewConfig();
    }

    /**
     * ContentView配置设置
     */
    private void setContentViewConfig() {
        setContentView(mContentView);
    }

    /**
     * 是否全屏
     *
     * @param isFullScreen
     */
    public void setFullScreen(boolean isFullScreen) {
        if (isFullScreen) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            );
        } else {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        }
    }

    /**
     * 设置沉浸式
     */
    public void setImmersive() {
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    /**
     * Activity重定向
     *
     * @param clazz    跳转的Activity
     * @param tfFinish 当前Activity是否finish
     */
    protected void redirectActivity(Class <? extends BaseActivity> clazz, boolean tfFinish) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
        if (tfFinish) {
            finish();
        }
    }

    /**
     * 更换Fragment
     */
    public void replaceFragment(int id, Fragment fragment, String tag) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (tag == null) {
            fragmentTransaction.replace(id, fragment);
        } else {
            fragmentTransaction.replace(id, fragment, tag);
            fragmentTransaction.addToBackStack(tag);
        }
        KeyBoardUtils.closeKeyboard(this);
        fragmentTransaction.commitAllowingStateLoss();
    }
}
