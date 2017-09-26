package com.ikould.frame.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.ikould.frame.R;
import com.ikould.frame.utils.KeyBoardUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

/**
 * Activity基类
 * <p>
 * Created by ikould on 2017/5/31.
 */
public abstract class BaseActivity extends AppCompatActivity {

    public View contentView;

    // 权限
    private Map<Integer, OnPermissionResultListener> permissionMap;

    protected abstract void onBaseCreate(Bundle savedInstanceState);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 隐藏系统标题栏
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.hide();
        if (lifeListener != null)
            lifeListener.onLifeCall(OnActivityLifeListener.ON_CREATE);
        onBaseCreate(savedInstanceState);
        // 设置竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    /**
     * 获取ContentView
     *
     * @return
     */
    protected void setBaseContentView(@LayoutRes int layoutId) {
        contentView = LayoutInflater.from(this).inflate(layoutId, null);
        setContentViewConfig();
    }

    /**
     * 获取ContentView
     *
     * @return
     */
    protected void setBaseContentView(View view) {
        contentView = view;
        setContentViewConfig();
    }

    /**
     * ContentView配置设置
     */
    private void setContentViewConfig() {
        setContentView(contentView);
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
    public void goToActivity(Class<? extends BaseActivity> clazz, boolean tfFinish) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
        if (tfFinish) {
            finish();
        }
        overridePendingTransition(R.anim.anim_in_right, R.anim.anim_out_left);
    }

    private Stack<Fragment> fragmentStack;

    /**
     * 更换Fragment
     */
    public void replaceFragment(int id, Fragment fragment, boolean isDoAnim) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (fragmentStack == null)
            fragmentStack = new Stack<>();
        if (isDoAnim) {
            fragmentTransaction.setCustomAnimations(R.anim.anim_in_right, R.anim.anim_out_left);
        }
        fragmentTransaction.replace(id, fragment);
        fragmentStack.add(fragment);
        Log.d("BaseActivity", "replaceFragment: fragmentStack.size = " + fragmentStack.size());
        KeyBoardUtils.closeKeyboard(this);
        fragmentTransaction.commitAllowingStateLoss();
    }

    /**
     * 退出回退栈
     *
     * @return 是否有Fragment可以退出
     */
    public boolean popFragment(int id, boolean isDoAnim) {
        Log.d("BaseActivity", "popFragment: fragmentStack.size() = " + fragmentStack.size());
        if (fragmentStack.size() == 1) {
            fragmentStack.remove(fragmentStack.lastElement());
            return false;
        }
        if (fragmentStack.size() == 0)
            return false;
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (isDoAnim) {
            fragmentTransaction.setCustomAnimations(R.anim.anim_in_left, R.anim.anim_out_right);
        }
        fragmentStack.remove(fragmentStack.lastElement());
        fragmentTransaction.replace(id, fragmentStack.lastElement());
        KeyBoardUtils.closeKeyboard(this);
        fragmentTransaction.commitAllowingStateLoss();
        return true;
    }

    /**
     * 主动清空Fragment栈
     */
    public void clearFragmentStack() {
        if (fragmentStack != null)
            fragmentStack.clear();
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (lifeListener != null)
            lifeListener.onLifeCall(OnActivityLifeListener.ON_START);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (lifeListener != null)
            lifeListener.onLifeCall(OnActivityLifeListener.ON_RESTART);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (lifeListener != null)
            lifeListener.onLifeCall(OnActivityLifeListener.ON_RESUME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (lifeListener != null)
            lifeListener.onLifeCall(OnActivityLifeListener.ON_PAUSE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (lifeListener != null)
            lifeListener.onLifeCall(OnActivityLifeListener.ON_STOP);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (lifeListener != null)
            lifeListener.onLifeCall(OnActivityLifeListener.ON_DESTROY);
        lifeListener = null; // 销毁时主动置null
    }

    /**
     * 权限申请
     *
     * @param permissions 权限
     * @param listener    执行完毕后执行
     */
    public void checkPermission(String[] permissions, OnPermissionResultListener listener) {
        boolean isRequest = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 没有读写权限或者读取手机状态的权限则申请
            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                    isRequest = true;
                    break;
                }
            }
            if (isRequest) {
                int code = 100;
                if (permissionMap == null) {
                    permissionMap = new HashMap<>();
                } else {
                    Set<Integer> integerSet = permissionMap.keySet();
                    int max = code;
                    for (int integer : integerSet) {
                        if (integer > max)
                            max = integer;
                    }
                    code = max + 1;// 在原先最大值上加1
                }
                permissionMap.put(code, listener);
                ActivityCompat.requestPermissions(this, permissions, code);
                return;
            }
        }
        if (listener != null)
            listener.permissionResult(true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean isSuccess = true;
        Log.d("PermissionUtil", "permissionResult: grantResults = " + Arrays.toString(grantResults));
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                isSuccess = false;
            }
        }
        if (permissionMap != null && permissionMap.keySet().contains(requestCode)) {
            OnPermissionResultListener listener = permissionMap.get(requestCode);
            if (listener != null)
                listener.permissionResult(isSuccess);
        }
    }

    // ======== 监听 ==========
    private OnActivityLifeListener lifeListener;

    public void setOnActivityLifeListener(OnActivityLifeListener lifeListener) {
        this.lifeListener = lifeListener;
    }
}
