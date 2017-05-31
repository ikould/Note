package com.ikould.frame.utils;

import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

/**
 * 序列化解析
 * <p>
 * Created by liudong on 2016/8/24.
 */
public class SerialUtil {
    /**
     * 存储对象
     *
     * @param object
     * @param tag
     */
    public static void saveObject(Object object, String tag, SharedPreferences sharedPrefs) {
        // 创建字节输出流
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            // 创建对象输出流，并封装字节流
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            // 将对象写入字节流
            oos.writeObject(object);
            // 将字节流编码成base64的字符窜
            String oAuth_Base64 = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
            SharedPreferences.Editor editor = sharedPrefs.edit();
            editor.putString(tag, oAuth_Base64);
            editor.apply();
        } catch (IOException e) {
            Log.d("AppConfig", e.toString());
        }
    }

    /**
     * 读取数据
     *
     * @param tag
     * @return
     */
    public static Object readObject(String tag, SharedPreferences sharedPrefs) {
        Object object = null;
        String productBase64 = sharedPrefs.getString(tag, "");
        //读取字节
        byte[] base64 = Base64.decode(productBase64, Base64.DEFAULT);

        //封装到字节流
        ByteArrayInputStream bais = new ByteArrayInputStream(base64);
        try {
            //再次封装
            ObjectInputStream bis = new ObjectInputStream(bais);
            try {
                //读取对象
                object = bis.readObject();
            } catch (ClassNotFoundException e) {
                Log.d("AppConfig", e.toString());
            }
        } catch (StreamCorruptedException e) {
            Log.d("AppConfig", e.toString());
        } catch (IOException e) {
            Log.d("AppConfig", e.toString());
        }
        return object;
    }
}
