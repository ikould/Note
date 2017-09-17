package com.ikould.frame.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 数据库帮助类
 * <p>
 * Created by ikould on 2017/7/7.
 */

public class DbOpenHelper extends SQLiteOpenHelper {

    //类没有实例化,是不能用作父类构造器的参数,必须声明为静态

    private static final String name = "Note"; //数据库名称

    /**
     * App版本 1.6.0 -> 数据库版本 1
     */
    private static final int version = 1; //数据库版本

    // 单例
    private static DbOpenHelper instance;

    public static DbOpenHelper getInstance() {
        return instance;
    }

    /**
     * 初始化Application调用
     *
     * @param context
     */
    public static void init(Context context) {
        if (instance == null)
            synchronized (DbOpenHelper.class) {
                if (instance == null)
                    instance = new DbOpenHelper(context);
            }
    }

    private DbOpenHelper(Context context) {
        //第三个参数CursorFactory指定在执行查询时获得一个游标实例的工厂类,设置为null,代表使用系统默认的工厂类
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // no do
    }
}
