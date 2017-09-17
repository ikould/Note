package com.ikould.frame.utils;

import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.location.Location;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.support.annotation.RequiresPermission;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class CommonUtils {
    /*
     ****************************************************************
     * GPU加速
     ****************************************************************
     */
    public static void LaunchWindowGPU(Activity ac) {
        try {
            Field field = WindowManager.LayoutParams.class.getField("FLAG_HARDWARE_ACCELERATED");
            if (field != null) {
                int FLAG_HARDWARE_ACCELERATED = field.getInt(null);
                Method method = Window.class.getMethod("setFlags", int.class, int.class);
                if (method != null) {
                    method.invoke(ac.getWindow(), FLAG_HARDWARE_ACCELERATED, FLAG_HARDWARE_ACCELERATED);
                }
            }
        } catch (Throwable e) {
        }
    }

    public static void CancelViewGPU(View v) {
        try {
            Field field = View.class.getField("LAYER_TYPE_SOFTWARE");
            if (field != null) {
                int LAYER_TYPE_SOFTWARE = field.getInt(null);
                Method method = View.class.getMethod("setLayerType", int.class, Paint.class);
                if (method != null) {
                    method.invoke(v, LAYER_TYPE_SOFTWARE, null);
                }
            }
        } catch (Throwable e) {
        }
    }

    /**
     * 只有APP开启了GPU加速才有效
     *
     * @param v
     */
    public static void LaunchViewGPU(View v) {
        try {
            Field field = View.class.getField("LAYER_TYPE_HARDWARE");
            if (field != null) {
                int LAYER_TYPE_HARDWARE = field.getInt(null);
                Method method = View.class.getMethod("setLayerType", int.class, Paint.class);
                if (method != null) {
                    method.invoke(v, LAYER_TYPE_HARDWARE, null);
                }
            }
        } catch (Throwable e) {
        }
    }

    /**
     * 打开系统浏览器/打开某应用
     *
     * @param context
     * @param url
     */
    public static void OpenBrowser(Context context, String url) {
        if (context != null && url != null && url.length() > 0) {
            try {
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                context.startActivity(intent);
            } catch (Throwable e) {
                Toast.makeText(context.getApplicationContext(), "打开浏览器失败！", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

    //<?xml version="1.0" encoding="utf-8"?>
    //<selector xmlns:android="http://schemas.android.com/apk/res/android" >
    //    <item android:drawable="@drawable/beautify_compare_btn_over" android:state_pressed="true"/>
    //    <item android:drawable="@drawable/beautify_compare_btn_out" android:state_pressed="false"/>
    //</selector>

    /**
     * 构造XHDPI的资源选择器,用于按钮状态等
     *
     * @param ac
     * @param outRes  res/path/bmp
     * @param overRes
     * @return
     */
//	public static StateListDrawable CreateXHDpiBtnSelector(Activity ac, Object outRes, Object overRes)
//	{
//		StateListDrawable out = new StateListDrawable();
//
//		if(ac != null)
//		{
//			Drawable outDrawable = null;
//			Drawable overDrawable = null;
//			if(outRes instanceof Integer)
//			{
//				outDrawable = ac.getResources().getDrawable((Integer)outRes);
//			}
//			else if(outRes instanceof String)
//			{
//				Bitmap bmp = MakeBmpV2.DecodeXHDpiResource(ac, outRes);
//				if(bmp != null)
//				{
//					outDrawable = new BitmapDrawable(ac.getResources(), bmp);
//				}
//			}
//			else if(outRes instanceof Bitmap)
//			{
//				outDrawable = new BitmapDrawable(ac.getResources(), (Bitmap)outRes);
//			}
//			if(overRes instanceof Integer)
//			{
//				overDrawable = ac.getResources().getDrawable((Integer)overRes);
//			}
//			else if(overRes instanceof String)
//			{
//				Bitmap bmp = MakeBmpV2.DecodeXHDpiResource(ac, overRes);
//				if(bmp != null)
//				{
//					overDrawable = new BitmapDrawable(ac.getResources(), bmp);
//				}
//			}
//			else if(overRes instanceof Bitmap)
//			{
//				overDrawable = new BitmapDrawable(ac.getResources(), (Bitmap)overRes);
//			}
//			out.addState(new int[]{-android.R.attr.state_pressed}, outDrawable);//false用-号表示
//			out.addState(new int[]{android.R.attr.state_pressed}, overDrawable);
//		}
//
//		return out;
//	}

    /**
     * 解压ZIP
     *
     * @param zipFile ZIP路径
     * @param outPath 目标目录(结尾无需加"/")
     * @return
     */
    public static boolean UnZip(String zipFile, String outPath) {
        boolean out = false;

        if (zipFile != null && zipFile.length() > 0 && outPath != null && outPath.length() > 0) {
            out = true;

            File path = new File(outPath);
            path.mkdirs();

            ZipFile zip = null;
            InputStream ins = null;
            FileOutputStream fos = null;
            try {
                zip = new ZipFile(zipFile);
                Enumeration<? extends ZipEntry> entries = zip.entries();
                byte[] buf = new byte[8192];
                while (entries.hasMoreElements()) {
                    ZipEntry ze = entries.nextElement();
                    //System.out.println(ze.getName());
                    if (ze.isDirectory()) {
                        File file = new File(outPath + File.separator + ze.getName());
                        if (file != null) {
                            file.mkdirs();
                        }
                        continue;
                    }
                    File file = new File(outPath + File.separator + ze.getName());
                    if (file != null) {
                        File temp = file.getParentFile();
                        if (temp != null) {
                            temp.mkdirs();
                        }
                        fos = new FileOutputStream(file);
                        ins = zip.getInputStream(ze);
                        int len = 0;
                        while ((len = ins.read(buf)) != -1) {
                            fos.write(buf, 0, len);
                        }
                    }
                    if (fos != null) {
                        fos.flush();
                        fos.close();
                        fos = null;
                    }
                    if (ins != null) {
                        ins.close();
                        ins = null;
                    }
                }
            } catch (Throwable e) {
                out = false;

                e.printStackTrace();
            } finally {
                if (zip != null) {
                    try {
                        if (fos != null) {
                            fos.close();
                            fos = null;
                        }
                        if (ins != null) {
                            ins.close();
                            ins = null;
                        }
                        zip.close();
                        zip = null;
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return out;
    }

    /*
     ****************************************************************
     * 文件读取/文件夹创建
     ****************************************************************
     */
    public static byte[] ReadData(InputStream is) throws Throwable {
        ByteArrayOutputStream os = new ByteArrayOutputStream(2048);

        byte[] buf = new byte[1024];
        int readSize = 0;
        while ((readSize = is.read(buf)) > -1) {
            os.write(buf, 0, readSize);
        }

        return os.toByteArray();
    }

    /**
     * 读数据
     *
     * @param size
     * @param is
     * @return
     * @throws Throwable
     */
    public static byte[] ReadData(int size, InputStream is) throws Throwable {
        byte[] out = new byte[size];

        int currentSize = 0;
        int readSize = 0;
        while ((readSize = is.read(out, currentSize, size - currentSize)) > -1) {
            currentSize += readSize;
            if (currentSize >= size) {
                break;
            }
        }

        return out;
    }

    /**
     * 加载本地文件
     *
     * @param path
     * @return
     */
    public static byte[] ReadFile(String path) {
        byte[] out = null;

        File file = new File(path);
        if (file.exists()) {
            int totalSize = (int) file.length();
            if (totalSize >= 0) {
                FileInputStream is = null;
                try {
                    is = new FileInputStream(file);
                    out = ReadData(totalSize, is);
                } catch (Throwable e) {
                    out = null;
                    e.printStackTrace();
                } finally {
                    if (is != null) {
                        try {
                            is.close();
                            is = null;
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        return out;
    }

    public static void MakeFolder(String path) {
        if (path != null) {
            File file = new File(path);
            if (!(file.exists() && file.isDirectory())) {
                file.mkdirs();
            }
        }
    }

    public static boolean MakeParentFolder(String path) {
        boolean out = false;

        if (path != null) {
            File file = new File(path).getParentFile();
            if (file != null) {
                if (file.exists()) {
                    out = true;
                } else {
                    if (file.mkdirs()) {
                        out = true;
                    }
                }
            }
        }

        return out;
    }

    public static boolean SaveFile(String path, byte[] data) {
        boolean out = false;

        FileOutputStream fos = null;
        try {
            if (path != null && data != null) {
                if (MakeParentFolder(path)) {
                    fos = new FileOutputStream(path);
                    fos.write(data);
                    fos.close();
                    fos = null;
                    out = true;
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (Throwable e) {
                }
                fos = null;
            }
        }

        return out;
    }

	/*
     **************************************************************
	 * EXIF常用功能
	 **************************************************************
	 */

    /**
     * 操作顺序:旋转->翻转->裁剪
     *
     * @param degree 角度,0,90,180,270...
     * @param flip   翻转</br>
     *               MakeBmpV2.FLIP_NONE</br>
     *               MakeBmpV2.FLIP_H</br>
     *               MakeBmpV2.FLIP_V</br>
     * @return
     */
//	public static int MakeExifTag(int degree, int flip)
//	{
//		int out = ExifInterface.ORIENTATION_UNDEFINED;
//
//		degree = degree / 90 * 90;
//		switch(degree % 360)
//		{
//			case 0:
//			{
//				switch(flip)
//				{
//					case MakeBmpV2.FLIP_H:
//						out = ExifInterface.ORIENTATION_FLIP_HORIZONTAL;
//						break;
//
//					case MakeBmpV2.FLIP_V:
//						out = ExifInterface.ORIENTATION_FLIP_VERTICAL;
//						break;
//
//					default:
//						out = ExifInterface.ORIENTATION_NORMAL;
//						break;
//				}
//				break;
//			}
//
//			case 90:
//			{
//				switch(flip)
//				{
//					case MakeBmpV2.FLIP_H:
//						out = ExifInterface.ORIENTATION_TRANSPOSE;
//						break;
//
//					case MakeBmpV2.FLIP_V:
//						out = ExifInterface.ORIENTATION_TRANSVERSE;
//						break;
//
//					default:
//						out = ExifInterface.ORIENTATION_ROTATE_90;
//						break;
//				}
//				break;
//			}
//
//			case 180:
//			{
//				switch(flip)
//				{
//					case MakeBmpV2.FLIP_H:
//						out = ExifInterface.ORIENTATION_FLIP_VERTICAL;
//						break;
//
//					case MakeBmpV2.FLIP_V:
//						out = ExifInterface.ORIENTATION_FLIP_HORIZONTAL;
//						break;
//
//					default:
//						out = ExifInterface.ORIENTATION_ROTATE_180;
//						break;
//				}
//				break;
//			}
//
//			case 270:
//			{
//				switch(flip)
//				{
//					case MakeBmpV2.FLIP_H:
//						out = ExifInterface.ORIENTATION_TRANSVERSE;
//						break;
//
//					case MakeBmpV2.FLIP_V:
//						out = ExifInterface.ORIENTATION_TRANSPOSE;
//						break;
//
//					default:
//						out = ExifInterface.ORIENTATION_ROTATE_270;
//						break;
//				}
//				break;
//			}
//
//			default:
//				break;
//		}
//
//		return out;
//	}

    /**
     * 操作顺序:旋转->翻转->裁剪
     *
     * @param
     * @return [0]角度, [1]翻转
     */
//	public static int[] DecodeExifTag(int tag)
//	{
//		int[] out = {0, MakeBmpV2.FLIP_NONE};
//
//		switch(tag)
//		{
//			case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
//				out[1] = MakeBmpV2.FLIP_H;
//				break;
//			case ExifInterface.ORIENTATION_ROTATE_180:
//				out[0] = 180;
//				break;
//			case ExifInterface.ORIENTATION_FLIP_VERTICAL:
//				out[1] = MakeBmpV2.FLIP_V;
//				break;
//			case ExifInterface.ORIENTATION_TRANSPOSE:
//				out[0] = 90;
//				out[1] = MakeBmpV2.FLIP_H;
//				break;
//			case ExifInterface.ORIENTATION_ROTATE_90:
//				out[0] = 90;
//				break;
//			case ExifInterface.ORIENTATION_TRANSVERSE:
//				out[0] = 90;
//				out[1] = MakeBmpV2.FLIP_V;
//				break;
//			case ExifInterface.ORIENTATION_ROTATE_270:
//				out[0] = 270;
//				break;
//			default:
//				break;
//		}
//
//		return out;
//	}

//	/**
//	 * 操作顺序:旋转->翻转->裁剪
//	 *
//	 * @param path 图片路径
//	 * @return [0]角度, [1]翻转
//	 */
//	public static int[] GetImgInfo(String path)
//	{
//		int[] out = new int[]{0, MakeBmpV2.FLIP_NONE};
//
//		if(path != null)
//		{
//			BitmapFactory.Options opts = new BitmapFactory.Options();
//			opts.inJustDecodeBounds = true;
//			BitmapFactory.decodeFile(path, opts);
//			if(opts.outMimeType != null && opts.outMimeType.equals("image/jpeg"))
//			{
//				try
//				{
//					ExifInterface exif = new ExifInterface(path);
//					String temp = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
//					if(temp != null && temp.length() > 0)
//					{
//						int[] vs = CommonUtils.DecodeExifTag(Integer.parseInt(temp));
//						out[0] = vs[0];
//						out[1] = vs[1];
//					}
//				}
//				catch(Throwable e)
//				{
//					e.printStackTrace();
//				}
//			}
//		}
//
//		return out;
//	}
    @RequiresPermission(allOf = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION})
    public static void AddJpgExifInfo(Context context, String path) {
        if (path != null) {
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, opts);
            if (opts.outMimeType != null && opts.outMimeType.equals("image/jpeg")) {
                try {
                    ExifInterface exif = new ExifInterface(path);

                    LocationManager mgr = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                    Location location = null;
                    if (mgr != null) {
                        location = mgr.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    }
                    if (location != null) {
                        double lat = location.getLatitude();
                        exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE, GpsInfoConvert(lat));
                        exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF, lat > 0 ? "N" : "S");
                        double lon = location.getLongitude();
                        exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, GpsInfoConvert(lon));
                        exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF, lon > 0 ? "E" : "W");
                    }
                    exif.setAttribute(ExifInterface.TAG_MODEL, android.os.Build.MODEL);
                    exif.setAttribute(ExifInterface.TAG_MAKE, android.os.Build.MANUFACTURER);
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss", Locale.CHINA);
                    exif.setAttribute(ExifInterface.TAG_DATETIME, formatter.format(new Date()));
                    exif.saveAttributes();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static String GpsInfoConvert(double gpsInfo) {
        String out = null;

        gpsInfo = Math.abs(gpsInfo);
        String dms = Location.convert(gpsInfo, Location.FORMAT_SECONDS);
        if (dms != null) {
            String[] splits = dms.split(":");
            if (splits.length > 2) {
                String[] arr = (splits[2]).split("\\.");
                String seconds;
                if (arr.length == 0) {
                    seconds = splits[2];
                } else {
                    seconds = arr[0];
                }
                out = splits[0] + "/1," + splits[1] + "/1," + seconds + "/1";
            }
        }

        return out;
    }


    /*
     ***************************************************************
     * 加密,MD5等
     ***************************************************************
     */
    private static final char HEX_DIGITS[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    private static String ToHexStr(byte[] data) {
        StringBuffer buf = new StringBuffer(data.length << 1);

        for (int i = 0; i < data.length; i++) {
            buf.append(HEX_DIGITS[(data[i] >>> 4) & 0x0f]);
            buf.append(HEX_DIGITS[data[i] & 0x0f]);
        }

        return buf.toString();
    }

    public static String Encrypt(String algorithm, byte[] data) {
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            md.update(data);
            return ToHexStr(md.digest());
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String Encrypt(String algorithm, String data) {
        try {
            return Encrypt(algorithm, data.getBytes());
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 使用 HMAC-SHA1 签名方法对对encryptText进行签名
     *
     * @param encryptText 被签名的字符串
     * @param encryptKey  密钥
     */
    public static byte[] HmacSHA1Encrypt(String encryptText, String encryptKey) {
        byte[] out = null;

        try {
            byte[] data = encryptKey.getBytes("UTF-8");
            //根据给定的字节数组构造一个密钥,第二参数指定一个密钥算法的名称
            SecretKey secretKey = new SecretKeySpec(data, "HmacSHA1");
            //生成一个指定 Mac 算法 的 Mac 对象
            Mac mac = Mac.getInstance("HmacSHA1");
            //用给定密钥初始化 Mac 对象
            mac.init(secretKey);

            byte[] text = encryptText.getBytes("UTF-8");
            //完成 Mac 操作
            out = mac.doFinal(text);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return out;
    }

    /*
     ******************************************************************
     * SharedPreferences的常用功能
     ******************************************************************
     */
    public static void SP_SaveMap(Context context, String spName, HashMap<String, String> map) {
        SharedPreferences sp = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
        if (sp != null) {
            SharedPreferences.Editor editor = sp.edit();
            if (editor != null) {
                editor.clear();
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    editor.putString(entry.getKey(), entry.getValue());
                }
            }
            //if(android.os.Build.VERSION.SDK_INT >= 9)
            //{
            //editor.apply();
            //}
            //else
            //{
            editor.commit();//杀进程必须用这个
            //}
        }
    }

    public static HashMap<String, String> SP_ReadSP(Context context, String spName, HashMap<String, String> outMap) {
        HashMap<String, String> out = outMap;
        if (out == null) {
            out = new HashMap<String, String>();
        } else {
            out.clear();
        }

        SharedPreferences sp = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
        if (sp != null) {
            Map<String, ?> temp = sp.getAll();
            Object obj;
            for (Map.Entry<String, ?> entry : temp.entrySet()) {
                obj = entry.getValue();
                if (obj != null) {
                    out.put(entry.getKey(), obj.toString());
                }
            }
        }

        return out;
    }

    public static void SP_AddKeyValue(Context context, String spName, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
        if (sp != null) {
            SharedPreferences.Editor editor = sp.edit();
            if (editor != null) {
                editor.putString(key, value);
            }
            //if(android.os.Build.VERSION.SDK_INT >= 9)
            //{
            editor.apply();
            //}
            //else
            //{
            //	editor.commit();
            //}
        }
    }

    public static String SP_ReadValue(Context context, String spName, String key) {
        String out = null;

        SharedPreferences sp = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
        if (sp != null) {
            out = sp.getString(key, null);
        }

        return out;
    }

    public static void SP_RemoveKey(Context context, String spName, String key) {
        SharedPreferences sp = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
        if (sp != null) {
            SharedPreferences.Editor editor = sp.edit();
            if (editor != null) {
                editor.remove(key);
            }
            //if(android.os.Build.VERSION.SDK_INT >= 9)
            //{
            editor.apply();
            //}
            //else
            //{
            //	editor.commit();
            //}
        }
    }

	/*
     **************************************************************************************
	 * 获取机器信息
	 **************************************************************************************
	 */

    /**
     * 获取手机的IMEI
     *
     * @param context
     * @return
     */
    public static String GetIMEI(Context context) {
        TelephonyManager manager = (TelephonyManager) context
                .getSystemService(Activity.TELEPHONY_SERVICE);
        String imei = "000000000000000";
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            imei = manager.getDeviceId();
        }
        return imei;
    }

    public static byte[] GetLocalMacAddress4Ip() {
        byte[] out = null;

        try {
            out = NetworkInterface.getByInetAddress(InetAddress.getByName(GetLocalIpAddress())).getHardwareAddress();
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return out;
    }

    /**
     * 获取MAC地址
     *
     * @param context
     * @return
     */
    public static String GetLocalMacAddress(Context context) {
        String out = null;
        byte[] mac = GetLocalMacAddress4Ip();
        if (mac != null) {
            out = ToHexStr(mac);
        }
        return out;
    }

    /**
     * 获取IP地址
     *
     * @return
     */
    public static String GetLocalIpAddress() {
        try {
            String ipv4;
            List<NetworkInterface> niList = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface ni : niList) {
                List<InetAddress> iaList = Collections.list(ni.getInetAddresses());
                for (InetAddress address : iaList) {
                    if (!address.isLoopbackAddress() && address instanceof Inet4Address) {
                        ipv4 = address.getHostAddress();
                        return ipv4;
                    }
                }
            }
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 获取真实版本号
     */
    public static String GetAppVer(Context context) {
        String out = null;

        try {
            PackageManager pm = context.getApplicationContext().getPackageManager();
            if (pm != null) {
                PackageInfo pi = pm.getPackageInfo(context.getApplicationContext().getPackageName(), 0);
                if (pi != null) {
                    out = pi.versionName;
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return out;
    }

    /**
     * 获取真实版本号
     */
    public static int GetAppVerCode(Context context) {
        int out = 0;

        try {
            PackageManager pm = context.getApplicationContext().getPackageManager();
            if (pm != null) {
                PackageInfo pi = pm.getPackageInfo(context.getApplicationContext().getPackageName(), 0);
                if (pi != null) {
                    out = pi.versionCode;
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return out;
    }

    public static Bundle getApplicationMetaData(Context context) {
        ApplicationInfo info = null;
        try {
            info = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (info != null) {
            return info.metaData;
        }
        return null;
    }

    public static Bundle getActivityMetaData(Context context) {
        ActivityInfo info = null;
        try {
            info = context.getPackageManager().getActivityInfo(((Activity) context).getComponentName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (info != null) {
            return info.metaData;
        }
        return null;
    }

    public static Bundle getServiceMetaData(Context context, Class<? extends Service> clazz) {
        ComponentName componentName = new ComponentName(context, clazz);
        ServiceInfo info = null;
        try {
            info = context.getPackageManager().getServiceInfo(componentName, PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (info != null) {
            return info.metaData;
        }
        return null;
    }

    /**
     * 用于EditText字数限制
     */
    public static class MyTextWatcher implements TextWatcher {
        protected int                 mMaxWordNum; //最大字数
        protected int                 mMaxByteNum; //最大字节数
        protected TextWatcherCallback mCB;

        public MyTextWatcher(int maxWordNum, int maxByteNum, TextWatcherCallback cb) {
            mMaxWordNum = maxWordNum;
            mMaxByteNum = maxByteNum;
            mCB = cb;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            String str = s.toString();
            //System.out.println(str + " |s " + str.length() + " |b " + str.getBytes().length + " |c " + str.toCharArray().length);
            boolean change = false;
            if (str.length() > mMaxWordNum) {
                str = str.substring(0, mMaxWordNum);
                change = true;
            }
            if (str.getBytes().length > mMaxByteNum) {
                while (str.getBytes().length > mMaxByteNum) {
                    str = str.substring(0, str.length() - 1);
                }
                change = true;
            }
            if (change) {
                //修正
                char[] chars = str.toCharArray();
                if (chars.length > 0) {
                    int count = 0;
                    for (int i = chars.length - 1; i >= 0; i--) {
                        if (Character.getType(chars[i]) != Character.SURROGATE) {
                            break;
                        }
                        count++;
                    }
                    if (count % 2 != 0) {
                        str = str.substring(0, str.length() - 1);
                    }
                }

                s.clear();
                s.append(str);

                if (mCB != null) {
                    mCB.OutOfBounds();
                }
            }
        }

        public void ClearAll() {
            mCB = null;
        }
    }

    public interface TextWatcherCallback {
        void OutOfBounds();
    }

//	/**
//	 * 获取屏幕截屏
//	 */
//	public static Bitmap GetScreenBmp(Activity ac, int w, int h)
//	{
//		Bitmap out = null;
//
//		if(ac != null && w > 0 && h > 0)
//		{
//			ShareData.InitData(ac);
//			View view = ac.getWindow().getDecorView();
//			if(view.getWidth() > 0 && view.getHeight() > 0)
//			{
//				out = Bitmap.createBitmap(view.getWidth(), ShareData.m_screenHeight, Bitmap.Config.ARGB_8888);
//				Canvas canvas = new Canvas(out);
//				view.draw(canvas);
//				out = MakeBmp.CreateBitmap(out, w, h, -1, 0, Bitmap.Config.ARGB_8888);
//			}
//			/*boolean oldFlag = view.isDrawingCacheEnabled();
//			if(!oldFlag)
//			{
//				view.setDrawingCacheEnabled(true);
//			}*/
////			view.invalidate();
////			out = view.getDrawingCache();
////			view.setDrawingCacheEnabled(oldFlag);
//		}
//
//		return out;
//	}

    /**
     * 获取sd卡可用size(MB)
     *
     * @return
     */
    public static int GetSDCardAvailableSize() {
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getAbsolutePath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return (int) (blockSize * availableBlocks / 1048576);
    }

    /**
     * 组装请求参数
     *
     * @param isEnc
     * @param paramJson
     * @return
     */
    public static String MakeProtocolJson(boolean isEnc, String version, JSONObject paramJson, Context context) {
        JSONObject postJson = new JSONObject();
        try {
            if (paramJson == null) {
                paramJson = new JSONObject();
            }
            String param = new StringBuilder().append("poco_").append(paramJson.toString()).append("_app").toString();
            String signStr = CommonUtils.Encrypt("MD5", param);
            String signCode = signStr.substring(5, (signStr.length() - 8));

            postJson.put("version", version);
            postJson.put("os_type", "android");
            postJson.put("ctime", System.currentTimeMillis());
            postJson.put("app_name", "art_camera_android");
            if (isEnc) {
                postJson.put("is_enc", 1);
            } else {
                postJson.put("is_enc", 0);
            }
            postJson.put("sign_code", signCode);
            postJson.put("param", paramJson);
            postJson.put("imei", GetIMEI(context));
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return postJson.toString();
    }


    public static String MakeProtocolJsonBase64(boolean isEnc, String version, JSONObject paramJson, Context context) {
        String s = MakeProtocolJson(isEnc, version, paramJson, context);
        return getBase64(s);
    }


    public static String getBase64(String str) {
        String result = "";
        if (str != null) {
            try {
                result = new String(Base64.encode(str.getBytes("utf-8"), Base64.NO_WRAP), "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

}
