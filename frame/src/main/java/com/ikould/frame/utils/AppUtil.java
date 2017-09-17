package com.ikould.frame.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

/**
 * App相关工具类
 * <p>
 * Created by ikould on 2017/6/14.
 */

public class AppUtil {

    /**
     * 重启App
     *
     * @param context
     */
    public static void restartApp(Context context) {
        Intent intent = context.getPackageManager()
                .getLaunchIntentForPackage(context.getPackageName());
        PendingIntent restartIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, restartIntent); // 1秒钟后重启应用
        System.exit(0);
    }

    /**
     * 清除之前所有数据
     */
    public static void clearHistory(Context context) {
        // /data/user/0/包名/cache
        String cachePath = context.getCacheDir().getAbsolutePath();
        // /storage/emulated/0/Android/data/包名/cache
        String externalCachePath = null;
        if (context.getExternalCacheDir() != null) {
            externalCachePath = context.getExternalCacheDir().getAbsolutePath();
        }
        // /data/data/包名/databases
        String databasePath = "/data/data/" + context.getPackageName() + "/databases";
        // /data/data/包名/shared_prefs
        String spPath = "/data/data/" + context.getPackageName() + "/shared_prefs";
        // /data/user/0/包名/files
        String filesPath = context.getFilesDir().getAbsolutePath();
        FileUtil.deleteFile(cachePath);
        FileUtil.deleteFile(externalCachePath);
        FileUtil.deleteFile(databasePath);
        FileUtil.deleteFile(spPath);
        FileUtil.deleteFile(filesPath);
    }
}
