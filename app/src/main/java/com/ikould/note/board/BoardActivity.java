package com.ikould.note.board;

import android.Manifest;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import com.ikould.frame.activity.BaseActivity;
import com.ikould.frame.activity.OnPermissionResultListener;
import com.ikould.note.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import view.GraffitiView;

/**
 * describe
 * Created by liudong on 2017/9/26.
 */

public class BoardActivity extends BaseActivity {

    @BindView(R.id.graffitiView)
    GraffitiView graffitiView;
    @BindView(R.id.last_step)
    Button       lastStep;
    @BindView(R.id.reset)
    Button       reset;
    @BindView(R.id.paintSize)
    Button       paintSize;
    @BindView(R.id.save)
    Button       save;
    @BindView(R.id.seekbar)
    SeekBar      seekbar;
    @BindView(R.id.recover)
    Button       recover;
    @BindView(R.id.toggle_paint)
    Button       togglePaint;
    @BindView(R.id.paint_color)
    Button       paintColor;

    private boolean isVisiable;

    @Override
    protected void onBaseCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_board);
        ButterKnife.bind(this);
        initListener();
        checkPermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE}, new OnPermissionResultListener() {
            @Override
            public void permissionResult(boolean isSuccess) {

            }
        });
    }

    private void initListener() {
        seekbar.setMax(100);
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    graffitiView.selectPaintSize(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private int nowColorIndex = 0;

    @OnClick({R.id.last_step, R.id.reset, R.id.paintSize, R.id.save, R.id.recover, R.id.toggle_paint, R.id.paint_color})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.last_step:
                graffitiView.undo();
                break;
            case R.id.recover:
                graffitiView.recover();
                break;
            case R.id.reset:
                graffitiView.redo();
                break;
            case R.id.paintSize:
                seekbar.setVisibility(isVisiable ? View.GONE : View.VISIBLE);
                isVisiable = !isVisiable;
                break;
            case R.id.save:
                graffitiView.saveToSDCard();
                break;
            case R.id.toggle_paint:
                graffitiView.togglePaintStyle();
                break;
            case R.id.paint_color:
                nowColorIndex = (++nowColorIndex) % 7;
                graffitiView.selectPaintColor(nowColorIndex);
                break;
        }
    }
}
