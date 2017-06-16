package com.ikould.note.main.task;

import android.content.Context;
import android.util.Log;


import com.ikould.note.main.AppTimerHelper;
import com.ikould.note.main.bean.AppTimerData;

import java.util.Calendar;

/**
 * App启动任务类
 * <p> 当前只处理一条数据 </p>
 */
public class AppStartTask {

    private static final String WAKE_PACKAGE_NAME = "com.alibaba.android.rimet";

    private static AppStartTask singleton;
    private        AppTimerData openAppTimerData;

    private AppStartTask() {
        initTime();
    }

    public static AppStartTask getInstance() {
        if (singleton == null) {
            synchronized (AppStartTask.class) {
                if (singleton == null) {
                    singleton = new AppStartTask();
                }
            }
        }
        return singleton;
    }

    /**
     * 设置时间
     */
    public void initTime() {
      //  boolean isFirst = AppConfig.getInstance().getIsFirstAppTimer();
        //if (isFirst) {
            insertDefaultAppTimer();
     //       AppConfig.getInstance().setIsFirstAppTimer(false);
      //  }
        openAppTimerData = AppTimerHelper.getInstance().findByPackName(WAKE_PACKAGE_NAME, true);
    }

    /**
     * 获取打开的AppTimer
     *
     * @return
     */
    public AppTimerData getOpenAppTimerData() {
        return openAppTimerData;
    }

    /**
     * 获取AppTimer
     *
     * @return
     */
    public AppTimerData getCloseAppTimer() {
        return null;
    }

    /**
     * 更新数据
     */
    public void updateInfo() {
        AppTimerHelper.getInstance().update(openAppTimerData);
    }

    /**
     * 插入默认信息
     * 钉钉 9:00 - 9:30 开启
     */
    private void insertDefaultAppTimer() {
        AppTimerData appTimerData = new AppTimerData();
        appTimerData.setAutoClose(false);
        appTimerData.setName("钉钉");
        appTimerData.setPackName(WAKE_PACKAGE_NAME);
        appTimerData.setAutoOpen(true);
        appTimerData.setStartHour(9);
        appTimerData.setEndHour(9);
        appTimerData.setStartMin(0);
        appTimerData.setEndMin(30);
        AppTimerHelper.getInstance().insert(appTimerData);
    }

    /**
     * 唤醒APP
     *
     * @param context
     */
    public void wakeApp(Context context) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        Log.d("TimeChangeReceiver", "onReceive: hour = " + hour + " min = " + min + " second = " + second);
        Log.d("AppStartTask", "wakeApp: openAppTimerData = " + openAppTimerData.toString());
        if (openAppTimerData.isAutoOpen()
                && hour > openAppTimerData.getStartHour() && hour < openAppTimerData.getEndHour()
                && min < openAppTimerData.getStartMin() && min > openAppTimerData.getEndMin()) {
          /*  CoreApplication.getInstance().handler.post(() -> {
                // 是否已经唤醒
                if (!AppUtils.isRunning(context, openAppTimerData.getPackName())) {
                    Log.d("AppStartTask", "wakeApp: ");
                    // 唤醒APP
                    AppUtils.startOtherApp(context, openAppTimerData.getPackName());
                }
            });*/
        }
    }
}