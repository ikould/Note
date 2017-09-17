package com.ikould.frame.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * @创建者 Administrator
 * @创建时间 2017/3/21 15:32
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */

public class NoNetworkDialogUtil {
    private static AlertDialog ad;

    public static void show(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        // 设置图标，使用安卓自带的图标
//        builder.setIcon(android.R.drawable.alert_dark_frame);
        // 设置标题
        builder.setTitle("当前为无网络连接状态");
//        // 设置文本
//        builder.setMessage("");

        // 设置确定按钮
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });

        // 使用创建器,生成一个对话框对象
        ad = builder.create();
        ad.show();
    }

    public static void dismiss() {
        if (ad != null) {
            ad.dismiss();
        }
    }

}
