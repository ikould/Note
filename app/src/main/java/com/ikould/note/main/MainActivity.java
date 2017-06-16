package com.ikould.note.main;

import android.os.Bundle;
import android.util.Log;

import com.ikould.frame.activity.BaseActivity;
import com.ikould.note.R;
import com.ikould.note.main.task.AppStartTask;

/**
 * 主界面，调度模块
 * <p>
 * Created by ikould on 2017/5/31.
 */

public class MainActivity extends BaseActivity {

    @Override
    protected void onBaseCreate(Bundle savedInstanceState) {
        setBaseContentView(R.layout.activity_welcome);
        Log.d("MainActivity", "onBaseCreate: ");
        AppStartTask.getInstance().initTime();
    }
}
