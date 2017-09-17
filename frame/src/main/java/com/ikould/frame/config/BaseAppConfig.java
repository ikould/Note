package com.ikould.frame.config;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

import com.ikould.frame.utils.FileUtil;
import com.ikould.frame.utils.SPUtils;

import java.io.File;

/**
 * SharedPreferences文件保存
 * <p>
 * Created by ikould on 2017/6/1.
 */

public class BaseAppConfig {

    /**
     * 当前项目SharedPreferences文件名
     */
    private static final String SHARED_PREFERENCE_NAME = "xMix_preference";

    //根目录
    public static final String ROOT_DIR;
    //缓存目录
    public static final String CACHE_DIR;
    //文件目录
    public static final String FILE_DIR;
    //崩溃日志目录
    public static final String CRASH_DIR;
    //下载文件保存目录
    public static final String DOWNLOAD_DIR;
    //内容发布目录
    public static final String CONTENT_DIR;
    // 临时目录
    public static final String TEMP_DIR;

    static {
        ROOT_DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Android" + File.separator + "data" + File.separator + "cn.poco.pMix" + File.separator + "Files";
        CACHE_DIR = ROOT_DIR + File.separator + "Cache";
        FILE_DIR = ROOT_DIR + File.separator + "File";
        CRASH_DIR = ROOT_DIR + File.separator + "Crash";
        DOWNLOAD_DIR = ROOT_DIR + File.separator + "Download";
        CONTENT_DIR = ROOT_DIR + File.separator + "Content";
        TEMP_DIR = ROOT_DIR + File.separator + "Temp";
    }

    private SharedPreferences sharedPrefs;

    // ================= 单例 ==============

    public static BaseAppConfig instance;

    public static BaseAppConfig getInstance() {
        if (instance == null) {
            synchronized (BaseAppConfig.class) {
                if (instance == null)
                    instance = new BaseAppConfig();
            }
        }
        return instance;
    }

    private BaseAppConfig() {
    }

    /**
     * 初始化
     *
     * @param application
     */
    public void init(Application application) {
        sharedPrefs = application.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        initDir();
    }

    /**
     * 初始化相关文件
     */
    private void initDir() {
        FileUtil.initDirectory(ROOT_DIR);
        FileUtil.initDirectory(CACHE_DIR);
        FileUtil.initDirectory(FILE_DIR);
        FileUtil.initDirectory(CRASH_DIR);
        FileUtil.initDirectory(DOWNLOAD_DIR);
        FileUtil.initDirectory(CONTENT_DIR);
        FileUtil.initDirectory(TEMP_DIR);
    }

    /**
     * 获取SharedPreferences 键值对
     *
     * @param key          关键值
     * @param defaultValue 不存在时默认值
     * @return
     */
    public Object get(String key, Object defaultValue) {
        return SPUtils.get(sharedPrefs, key, defaultValue);
    }

    /**
     * 设置SharedPreferences 键值对
     *
     * @param key   关键值
     * @param value 值
     */
    public void put(String key, Object value) {
        SPUtils.put(sharedPrefs, key, value);
    }

    // ==============  获取和设置 ==================

    private static final String IS_DEBUG_MODE = "is_debug_mode";

    private static final String IS_DEBUG_ENV = "is_debug_env";

    /**
     * 设置调试模式
     *
     * @param tf
     */
    public void setDebugMode(boolean tf) {
        BaseAppConfig.getInstance().put(IS_DEBUG_MODE, tf);
    }

    /**
     * 获取是否调试模式
     *
     * @return
     */
    public Boolean getDebugMode() {
        return (Boolean) BaseAppConfig.getInstance().get(IS_DEBUG_MODE, false);
    }

    /**
     * 设置调试环境
     *
     * @param tf
     */
    public void setDebugEnv(boolean tf) {
        BaseAppConfig.getInstance().put(IS_DEBUG_ENV, tf);
    }

    /**
     * 获取是否调试环境
     *
     * @return
     */
    public Boolean getDebugEnv() {
        return (Boolean) BaseAppConfig.getInstance().get(IS_DEBUG_ENV, false);
    }
}
