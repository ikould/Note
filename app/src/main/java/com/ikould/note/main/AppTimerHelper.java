package com.ikould.note.main;

import android.util.Log;


import com.ikould.note.main.bean.AppTimerData;

import org.litepal.crud.DataSupport;

import java.util.List;


/**
 * AppTimerData 数据库帮助类
 * <p>
 * Created by ikould on 2017/6/7.
 */

public class AppTimerHelper {

    // ======== 单例 ==========
    private static AppTimerHelper instance;

    public static AppTimerHelper getInstance() {
        if (instance == null)
            synchronized (AppTimerHelper.class) {
                if (instance == null)
                    instance = new AppTimerHelper();
            }
        return instance;
    }

    private AppTimerHelper() {
    }

    // ======== 操作 =======

    /**
     * 插入
     */
    public void insert(AppTimerData appTimerData) {
        Log.d("AppTimerHelper", "insert: appTimerData = " + appTimerData);
        // 当前业务 -> 若有 playId path 相同的则不插入
        //  AppTimerData sameAppTimerData = DataSupport.where("packName = ?", appTimerData.getPackName()).findFirst(AppTimerData.class);
        List <AppTimerData> list = DataSupport.findAll(AppTimerData.class);
        Log.d("AppTimerHelper", "insert: list = " + list);
       /* if (sameAppTimerData == null) {
            appTimerData.save();
            Log.d("AppTimerHelper", "insert: appTimerData1 = " + appTimerData);
           // AppTimerData appTimerData1 = DataSupport.where("packName = ?", appTimerData.getPackName()).findFirst(AppTimerData.class);
        }*/
    }


    /**
     * 通过包名获取AppTimer
     *
     * @param packName 包名
     * @param isOpen   是否是打开
     * @return
     */
    public AppTimerData findByPackName(String packName, boolean isOpen) {
        Log.d("AppTimerHelper", "findByPackName: packName = " + packName);
      /*  if (isOpen)
            return DataSupport.where("packName = ?", packName).findFirst(AppTimerData.class);
        else
            return DataSupport.where("packName = ?", packName).findFirst(AppTimerData.class);*/
        return null;
    }

    /**
     * 刷新
     *
     * @param appTimerData
     */
    public void update(AppTimerData appTimerData) {
        // appTimerData.update(appTimerData.getId());
    }

    /**
     * 查询所有
     *
     * @return 查询结果
     */
    public List <AppTimerData> findAll() {
        return DataSupport.findAll(AppTimerData.class);
    }
}
