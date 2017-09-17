package com.ikould.frame.utils;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;


public class PhoneTools {

    private static PhoneTools instance;

    private static final String FILE_MEMORY = "/proc/meminfo";
    private static final String FILE_CPU    = "/proc/cpuinfo";
    public String  mIMEI;//手机的IMEI;
    public int     mPhoneType;//手机的制式类型，GSM OR CDMA 手机
    public int     mSdkVersion;//SDK版本;
    public String  mOsVersion;//手机OS版本;
    public String  mAppName;//APP版本;
    public String  mAppVersion;//APP版本;
    public String  mNetWorkCountryIso;//手机网络国家编码
    public String  mNetWorkOperator;//手机网络运营商ID
    public String  mNetWorkOperatorName;//手机网络运营商名称
    public String  mNetWorkType;//手机的数据链接类型
    public boolean mIsOnLine;//是否有可用数据链接
    public String  mConnectTypeName;//当前的数据链接类型
    public long    mFreeMem;//手机剩余内存
    public long    mTotalMem;//手机总内存
    public String  mCupInfo;//手机CPU型号
    public String  mProductName;//手机名称
    public String  mModelName;//手机型号
    public String  mManufacturerName;//手机设备制造商名称
    public String  mBasebandVersion;//基带版本;
    public String  mFingerprint;//手机指纹,也就是唯一标识;
    public String  mMac; // mac地址
    public String  mNetType;// 联网方式
    public String  mAndroidId;// AndroidId, 16位
    public String  mUA; // User-agent

    private PhoneTools() {
    }

    /**
     * String工具,
     * 去掉"-"和空格" ";
     *
     * @param strParams
     * @return
     */
    public static String replaceX(String strParams) {
        if (!TextUtils.isEmpty(strParams)) {
//    		strParams=strParams.trim().replace("-","_").replace(" ", "_");
            strParams = strParams.trim().replace("-", "_").replace(" ", "_").replace("/", "_");
            return strParams.toLowerCase();
        } else {
            return null;
        }
    }

    /**
     * 获取手机的IMEI
     *
     * @param context
     * @return
     */
    public static String getIMEI(Context context) {
        TelephonyManager manager = (TelephonyManager) context
                .getSystemService(Activity.TELEPHONY_SERVICE);
        String imei = "000000000000000";
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            imei = manager.getDeviceId();
        }
        return imei;
    }

    /**
     * 获取网络制式,like :GSM/CDMA/unKnow;
     *
     * @param context
     * @return
     */
    public static int getPhoneType(Context context) {
        TelephonyManager manager = (TelephonyManager) context
                .getSystemService(Activity.TELEPHONY_SERVICE);
        return manager.getPhoneType();
    }

    /**
     * 获取联网方式
     * “0”->unknow，“1”->wifi，“2”->2G，‘3’->3G，‘4’->4G
     *
     * @return
     */
    public static String getNetType(Context context) {
        String strNetworkType = "0";
        NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                strNetworkType = "1"; // wifi
            } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                String _strSubTypeName = networkInfo.getSubtypeName();
                // TD-SCDMA   networkType is 17
                int networkType = networkInfo.getSubtype();
                switch (networkType) {
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN: //api<8 : replace by 11
                        strNetworkType = "2"; // 2G
                        break;
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B: //api<9 : replace by 14
                    case TelephonyManager.NETWORK_TYPE_EHRPD:  //api<11 : replace by 12
                    case TelephonyManager.NETWORK_TYPE_HSPAP:  //api<13 : replace by 15
                        strNetworkType = "3"; // 3G
                        break;
                    case TelephonyManager.NETWORK_TYPE_LTE:    //api<11 : replace by 13
                        strNetworkType = "4"; // 4G
                        break;
                    default:
                        // http://baike.baidu.com/item/TD-SCDMA 中国移动 联通 电信 三种3G制式
                        if (_strSubTypeName.equalsIgnoreCase("TD-SCDMA") || _strSubTypeName.equalsIgnoreCase("WCDMA") || _strSubTypeName.equalsIgnoreCase("CDMA2000")) {
                            strNetworkType = "3"; // 3G
                        } else {
                            strNetworkType = _strSubTypeName;
                        }
                        break;
                }
            }
        }
        return strNetworkType;
    }

    /**
     * 获取系统OS版本;
     *
     * @return
     */
    public static String getOsVersion() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取手机系统SDK版本;
     *
     * @return
     */
    public static int getSdkVersion() {
        return Build.VERSION.SDK_INT;
    }

    /**
     * 返回国家代码;
     *
     * @param context
     * @return
     */
    public static String getNetWorkCountryIso(Context context) {
        TelephonyManager manager = (TelephonyManager) context
                .getSystemService(Activity.TELEPHONY_SERVICE);
        return manager.getNetworkCountryIso();
    }

    /**
     * 返回手机网络运营商ID
     *
     * @param context
     * @return
     */
    public static String getNetWorkOperator(Context context) {
        TelephonyManager manager = (TelephonyManager) context
                .getSystemService(Activity.TELEPHONY_SERVICE);
        return manager.getNetworkOperator();
    }

    /**
     * 返回手机网络运营商名称
     *
     * @param context
     * @return
     */
    public static String getNetWorkOperatorName(Context context) {
        TelephonyManager manager = (TelephonyManager) context
                .getSystemService(Activity.TELEPHONY_SERVICE);
        return manager.getNetworkOperatorName();
    }

    /**
     * 返回当前网络cnwap/cnnet/wifi;
     *
     * @param context
     * @return
     */
    public static String getNetworkType(Context context) {
        String networkType = "unKnow";
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mobNetInfo != null) {
            networkType = mobNetInfo.getExtraInfo();
        }
        return networkType;
    }

    /**
     * 获取User-Agent
     *
     * @param context
     * @return
     */
    public static String getUserAgent(Context context) {
        return new WebView(context).getSettings().getUserAgentString();
    }

    /**
     * 判断当前网络是否可用;
     *
     * @param context
     * @return
     */
    public static boolean isOnline(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context
                .getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        return info != null && info.isConnected();
    }

    /**
     * 返回当前的数据链接类型;
     *
     * @param context
     * @return
     */
    public static String getConnectTypeName(Context context) {
        if (!isOnline(context)) {
            return "OFFLINE";
        }
        ConnectivityManager manager = (ConnectivityManager) context
                .getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        if (info != null) {
            return info.getTypeName();
        } else {
            return "OFFLINE";
        }
    }

    /**
     * 返回剩余内存;
     *
     * @param context
     * @return
     */
    public static long getFreeMem(Context context) {
        ActivityManager manager = (ActivityManager) context
                .getSystemService(Activity.ACTIVITY_SERVICE);
        MemoryInfo info = new MemoryInfo();
        manager.getMemoryInfo(info);
        long free = info.availMem / 1024 / 1024;
        return free;
    }

    /**
     * 返回可用内存;
     *
     * @param context
     * @return
     */
    public static long getTotalMem(Context context) {
        try {
            FileReader fr = new FileReader(FILE_MEMORY);
            BufferedReader br = new BufferedReader(fr);
            String text = br.readLine();
            String[] array = text.split("\\s+");
            return Long.valueOf(array[1]) / 1024;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 获取CPU信息;
     *
     * @return
     */
    public static String getCpuInfo() {
        try {
            FileReader fr = new FileReader(FILE_CPU);
            BufferedReader br = new BufferedReader(fr);
            String text = br.readLine();
            String[] array = text.split(":\\s+", 2);
            for (int i = 0; i < array.length; i++) {
            }
            return array[1];
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取设备名字;
     *
     * @return
     */
    public static String getProductName() {
        return Build.PRODUCT;
    }

    /**
     * 获取基带版本;
     *
     * @return
     */
    public static String getBasebandVersion() {
        return Build.VERSION.INCREMENTAL;
    }

    /**
     * 获取手机指纹识别
     *
     * @return
     */
    public static String getFingerprint() {
        return Build.FINGERPRINT;
    }

    /**
     * 获取手机型号;
     *
     * @return
     */
    public static String getModelName() {
        return Build.MODEL;
    }

    /**
     * 返回制造商名;
     *
     * @return
     */
    public static String getManufacturerName() {
        return Build.MANUFACTURER;
    }


    /**
     * 获取应用的版本;
     *
     * @param context
     * @return
     */
    public static String getAppVersion(Context context) {
        String appVer = "null";
        PackageManager pm = context.getPackageManager();
        if (pm != null) {
            PackageInfo pi;
            try {
                pi = pm.getPackageInfo(context.getPackageName(), 0);
                if (pi != null) {
                    appVer = pi.versionName;
                }
            } catch (NameNotFoundException e) {
                e.printStackTrace();
            }

        }
        return appVer;
    }

    /**
     * 获取应用程序名;
     *
     * @param context
     * @return
     */
    public static String getAppName(Context context) {
        Resources res = context.getResources();
        String appName = context.getApplicationInfo().loadLabel(context.getPackageManager()).toString();
        if (TextUtils.isEmpty(appName)) {
            appName = "Patui";
        }
        return appName;
    }

    /**
     * 获取Ip地址
     *
     * @return
     */
    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e("PhoneTools", "getLocalIpAddress: e = " + ex);
        }
        return null;
    }

    /**
     * 获取本地Mac
     *
     * @return
     */
    public static String getMacAddr() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;
                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }
                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:", b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
            Log.e("PhoneTools", "getMacAddr: ex = " + ex);
        }
        return "02:00:00:00:00:00";
    }

    /**
     * 获取AndroidId
     *
     * @param context
     * @return
     */
    public static String getAndroidId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    /**
     * 返回一个PhoneInfo实例;
     *
     * @param context
     * @return
     */
    public static PhoneTools getPhoneInfo(Context context) {
        if (instance == null) {
            instance = new PhoneTools();
            instance.mIMEI = getIMEI(context);
            instance.mPhoneType = getPhoneType(context);
            instance.mSdkVersion = getSdkVersion();
            instance.mOsVersion = getOsVersion();
            instance.mAppName = getAppName(context);
            instance.mAppVersion = getAppVersion(context);
            instance.mNetWorkCountryIso = getNetWorkCountryIso(context);
            instance.mNetWorkOperator = getNetWorkOperator(context);
            instance.mNetWorkOperatorName = getNetWorkOperatorName(context);
            instance.mNetWorkType = getNetworkType(context);
            instance.mIsOnLine = isOnline(context);
            instance.mConnectTypeName = getConnectTypeName(context);
            instance.mFreeMem = getFreeMem(context);
            instance.mTotalMem = getTotalMem(context);
            instance.mCupInfo = getCpuInfo();
            instance.mProductName = getProductName();
            instance.mModelName = getModelName();
            instance.mManufacturerName = getManufacturerName();
            instance.mBasebandVersion = getBasebandVersion();
            instance.mFingerprint = getFingerprint();
            instance.mMac = getMacAddr();
            instance.mNetType = getNetType(context);
            instance.mAndroidId = getAndroidId(context);
            instance.mUA = getUserAgent(context);
        }
        return instance;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("\nmIMEI : " + mIMEI + "\n");
        builder.append("mAppName: " + mAppName + "\n");
        builder.append("mAppVersion : " + mAppVersion + "\n");
        builder.append("mManufacturerName : " + mManufacturerName + "\n");
        builder.append("mProductName : " + mProductName + "\n");
        builder.append("mModelName : " + mModelName + "\n");
        builder.append("mSysVersion : " + mSdkVersion + "\n");
        builder.append("mOsVersion : " + mOsVersion + "\n");
        builder.append("mCupInfo : " + mCupInfo + "\n");
        builder.append("mFreeMem : " + mFreeMem + "M\n");
        builder.append("mTotalMem : " + mTotalMem + "M\n");
        builder.append("mPhoneType : " + mPhoneType + "\n");
        builder.append("mNetWorkCountryIso : " + mNetWorkCountryIso + "\n");
        builder.append("mNetWorkOperator : " + mNetWorkOperator + "\n");
        builder.append("mNetWorkOperatorName : " + mNetWorkOperatorName + "\n");
        builder.append("mNetWorkType : " + mNetWorkType + "\n");
        builder.append("mIsOnLine : " + mIsOnLine + "\n");
        builder.append("mConnectTypeName : " + mConnectTypeName + "\n");
        builder.append("mMac : " + mMac + "\n");
        builder.append("mNetType : " + mNetType + "\n");
        builder.append("mAndroidId : " + mAndroidId + "\n");
        builder.append("mBasebandVersion : " + mBasebandVersion + "\n");
        builder.append("mFingerprint : " + mFingerprint + "\n");
        return builder.toString();
    }

    public HashMap<String, String> toHashMap() {
        HashMap<String, String> mAllMap = new HashMap<String, String>();
        mAllMap.put("mMd5", mIMEI);
        mAllMap.put("mAppName", mAppName);
        mAllMap.put("mAppVersion", mAppVersion);
        mAllMap.put("mManufacturerName", mManufacturerName);
        mAllMap.put("mProductName", mProductName);
        mAllMap.put("mModelName", mModelName);
        mAllMap.put("mSdkVersion", String.valueOf(mSdkVersion));
        mAllMap.put("mOsVersion", mOsVersion);
        mAllMap.put("mCupInfo", mCupInfo);
        mAllMap.put("mFreeMem", mFreeMem + "M");
        mAllMap.put("mTotalMem", mTotalMem + "M");
        mAllMap.put("mPhoneType", String.valueOf(mPhoneType));
        mAllMap.put("mNetWorkCountryIso", mNetWorkCountryIso);
        mAllMap.put("mNetWorkOperator", mNetWorkOperator);
        mAllMap.put("mNetWorkOperatorName", mNetWorkOperatorName);
        mAllMap.put("mNetWorkType", mNetWorkType);
        mAllMap.put("mIsOnLine", String.valueOf(mIsOnLine));
        mAllMap.put("mConnectTypeName", mConnectTypeName);
        mAllMap.put("mBasebandVersion", String.valueOf(mBasebandVersion));
        mAllMap.put("mFingerprint", mFingerprint);
        return mAllMap;
    }


}
