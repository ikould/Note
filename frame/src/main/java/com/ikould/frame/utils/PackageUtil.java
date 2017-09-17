package com.ikould.frame.utils;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;

import java.io.File;

/**
 * 包相关的查询工具
 * <p>
 * Created by ikould on 2016/7/28.
 */
public class PackageUtil {

    private static String versionName;

    /**
     * 判断当前应用是否在前台
     *
     * @param context 上下文
     * @return
     */
    public static boolean isCurrentAppOnForeground(Context context) {
        String currentPackageName = context.getPackageName();
        return isPackageOnForeground(context, currentPackageName);
    }

    /**
     * 判断应用是否在前台
     *
     * @param context 上下文
     * @param pckName 包名
     * @return
     */
    public static boolean isPackageOnForeground(Context context, String pckName) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.RunningTaskInfo info = manager.getRunningTasks(1).get(0);
        String packageName = info.topActivity.getPackageName();
        return pckName.equals(packageName);
    }

    /**
     * 获取当前应用版本名
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        if (TextUtils.isEmpty(versionName)) {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packInfo = null;
            try {
                packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
                versionName = packInfo.versionName;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return versionName;
    }

    /**
     * 获取当前应用版本号
     *
     * @param application
     * @return
     */
    public static int getVersionCode(Application application) {
        PackageManager packageManager = application.getPackageManager();
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(application.getPackageName(), 0);
            return packInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取当前正在运行的Activity名字
     *
     * @param context
     * @return
     */
    public static String getRunningActivityName(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        return activityManager.getRunningTasks(1).get(0).topActivity.getClassName();
    }

    /**
     * install package normal by system intent
     *
     * @param context
     * @param filePath file path of package
     * @return whether apk exist
     */
    public static boolean installNormal(Context context, String filePath) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        File file = new File(filePath);
        if (!file.exists() || !file.isFile() || file.length() <= 0) {
            return false;
        }
        i.setDataAndType(Uri.parse(filePath), "application/vnd.android.package-archive");
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
        return true;
    }
}
