package com.ikould.note.edit.activity;

import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.view.Display;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ikould.frame.activity.BaseActivity;
import com.ikould.frame.utils.DensityUtil;
import com.ikould.note.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import view.GraffitiView;

public class EditActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.edit_ll_menu)
    LinearLayout         editLlMenu;
    @BindView(R.id.edit_hsl_bottom)
    HorizontalScrollView editHslBottom;
    @BindView(R.id.edit_fl_main)
    FrameLayout          editFlMain;

    private ArrayList<ImageView> mBackImgList;

    @Override
    protected void onBaseCreate(Bundle savedInstanceState) {
        setBaseContentView(R.layout.activity_edit);
        ButterKnife.bind(this);
        initConfig();
        initView();
    }

    /**
     * 初始化配置
     */
    private void initConfig() {
        // 设置全屏
        setFullScreen(true);
        mBackImgList = new ArrayList <>();
        mBackImgList.add(createImageView(R.drawable.meterial_test1));
        mBackImgList.add(createImageView(R.drawable.meterial_test2));
    }

    /**
     * 初始化控件
     */
    private void initView() {
        initGraffitiView();
        for (ImageView imageView : mBackImgList) {
            editLlMenu.addView(imageView);
            imageView.setOnClickListener(this);
        }
    }

    /**
     * 初始化涂鸦View
     */
    private void initGraffitiView() {
        //虽然此时获取的是屏幕宽高，但是我们可以通过控制framlayout来实现控制涂鸦板大小
        Display defaultDisplay = getWindowManager().getDefaultDisplay();
        int screenWidth = defaultDisplay.getWidth();
        int screenHeight = defaultDisplay.getHeight();
        GraffitiView graffitiView = new GraffitiView(this, screenWidth, screenHeight);
        editFlMain.addView(graffitiView);
        graffitiView.requestFocus();
        graffitiView.selectPaintSize(30);
    }

    /**
     * 创建ImageView
     *
     * @param imgId
     * @return
     */
    private ImageView createImageView(@DrawableRes int imgId) {
        ImageView imageView = new ImageView(this);
        imageView.setLayoutParams(new LinearLayout.LayoutParams(DensityUtil.dip2px(this, 100), LinearLayout.LayoutParams.WRAP_CONTENT));
        imageView.setImageResource(imgId);
        imageView.setId(mBackImgList != null ? mBackImgList.size() : 0);
        return imageView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case 0:
                editFlMain.setBackgroundResource(R.drawable.meterial_test1);
                break;
            case 1:
                editFlMain.setBackgroundResource(R.drawable.meterial_test2);
                break;
        }
    }
}
