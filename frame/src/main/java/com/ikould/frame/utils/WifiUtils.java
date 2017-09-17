package com.ikould.frame.utils;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

/**
 * Description  Wifi设置工具类,建议在Application中进行实例化后全局使用
 * Created by chenqiao on 2015/11/11.
 */
public class WifiUtils {
    private WifiManager mWifiManager;
    private WifiInfo mWifiInfo;
    private List<ScanResult> mWifiList = null;
    private List<WifiConfiguration> mWifiConfiguration;
    private WifiManager.WifiLock mWifiLock;
    private DhcpInfo dhcpInfo;

    public WifiUtils(Context context) {
        mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        mWifiInfo = mWifiManager.getConnectionInfo();
        mWifiList = new ArrayList<>();
    }

    public boolean isWifiEnable() {
        return mWifiManager.isWifiEnabled();
    }

    public void openWifi() {
        //打开wifi
        if (!mWifiManager.isWifiEnabled()) {
            mWifiManager.setWifiEnabled(true);
        }
    }

    public void closeWifi() {
        if (mWifiManager.isWifiEnabled()) {
            mWifiManager.setWifiEnabled(false);
        }
    }

    public int checkState() {
        return mWifiManager.getWifiState();
    }

    public void acquireWifiLock() {//锁定wifiLock
        mWifiLock.acquire();
    }

    public void releaseWifiLock() {//解锁wifiLock
        if (mWifiLock.isHeld()) {
            mWifiLock.acquire();
        }
    }

    public void creatWifiLock() {
        mWifiLock = mWifiManager.createWifiLock("Test");
    }

    public List<WifiConfiguration> getConfiguration() {
        return mWifiConfiguration;
    }

    public void connectConfiguration(int index) {//指定配置好的网络进行连接
        if (index > mWifiConfiguration.size()) {
            return;
        }
        mWifiManager.enableNetwork(mWifiConfiguration.get(index).networkId, true);
    }

    public void startScan() {//wifi扫描
        boolean scan = mWifiManager.startScan();
        mWifiList = mWifiManager.getScanResults();
        mWifiConfiguration = mWifiManager.getConfiguredNetworks();

        if (mWifiList != null) {
            for (int i = 0; i < mWifiList.size(); i++) {
                ScanResult result = mWifiList.get(i);
            }
        }
    }

    public List<ScanResult> getWifiList() {
        return mWifiList = mWifiManager.getScanResults();
    }

    public List<ScanResult> getWifiListWithFilter() {
        mWifiList = mWifiManager.getScanResults();
        List<ScanResult> filterWifiLists = new ArrayList<>();
        boolean tf;
        for (ScanResult result : mWifiList) {
            tf = false;
            for (ScanResult r : filterWifiLists) {
                if (r.SSID.equals(result.SSID)) {
                    tf = true;
                    int level1 = WifiManager.calculateSignalLevel(r.level, 5);
                    int level2 = WifiManager.calculateSignalLevel(result.level, 5);
                    if (level1 < level2) {
                        filterWifiLists.remove(r);
                        filterWifiLists.add(result);
                    }
                    break;
                }
            }
            if (!tf) {
                filterWifiLists.add(result);
            }
        }
        return filterWifiLists;
    }

    public StringBuilder lookUpScan() {// 查看扫描结果
        StringBuilder stringBuilder = new StringBuilder();
        mWifiList = mWifiManager.getScanResults();
        for (int i = 0; i < mWifiList.size(); i++) {
            stringBuilder.append("Index_" + Integer.valueOf(i + 1).toString() + ":");
            stringBuilder.append((mWifiList.get(i)).toString());
            stringBuilder.append("/n");
        }
        return stringBuilder;
    }

    public String getMacAddress() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                NetworkInterface networkInterface = NetworkInterface.getByName("wlan0");
                byte[] mac = networkInterface.getHardwareAddress();
                return macBytesToString(mac);
            } catch (SocketException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            mWifiInfo = mWifiManager.getConnectionInfo();
            return (mWifiInfo == null) ? "" : mWifiInfo.getMacAddress();
        }
    }

    /**
     * bytes转换成十六进制字符串
     *
     * @param mac byte数组
     * @return String
     */
    private String macBytesToString(byte[] mac) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (mac == null || mac.length <= 0) {
            return null;
        }
        for (int i = 0; i < mac.length; i++) {
            int v = mac[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            if (i != mac.length - 1) {
                stringBuilder.append(hv).append(":");
            } else {
                stringBuilder.append(hv);
            }
        }
        return stringBuilder.toString();
    }

    public String getSSID() {
        mWifiInfo = mWifiManager.getConnectionInfo();
        return (mWifiInfo == null) ? "" : mWifiInfo.getSSID().substring(1, mWifiInfo.getSSID().length() - 1);
    }

    public String getBSSID() {
        mWifiInfo = mWifiManager.getConnectionInfo();
        return (mWifiInfo == null) ? "" : mWifiInfo.getBSSID();
    }

    public DhcpInfo getDhcpInfo() {
        return dhcpInfo = mWifiManager.getDhcpInfo();
    }

    public int getIPAddress() {
        mWifiInfo = mWifiManager.getConnectionInfo();
        return (mWifiInfo == null) ? 0 : mWifiInfo.getIpAddress();
    }

    public String getIPAddressForString() {
        return intToIp(getIPAddress());
    }

    private String intToIp(int i) {
        return (i & 0xFF) + "." +
                ((i >> 8) & 0xFF) + "." +
                ((i >> 16) & 0xFF) + "." +
                (i >> 24 & 0xFF);
    }

    public int getNetworkId() {
        mWifiInfo = mWifiManager.getConnectionInfo();
        return (mWifiInfo == null) ? 0 : mWifiInfo.getNetworkId();
    }

    public WifiInfo getWifiInfo() {
        mWifiInfo = mWifiManager.getConnectionInfo();
        return mWifiInfo;
    }

    public int addNetworkAndSave(WifiConfiguration wcg) { // 添加一个网络配置并连接
        int wcgID = mWifiManager.addNetwork(wcg);
        boolean b = mWifiManager.enableNetwork(wcgID, true);
        mWifiManager.saveConfiguration();
        System.out.println("addNetwork:" + wcgID);
        System.out.println("enableNetwork:" + b);
        return wcgID;
    }

    public boolean removeConfiguredWifi(int netId) {
        boolean result = mWifiManager.removeNetwork(netId);
        mWifiManager.saveConfiguration();
        return result;
    }

    public void disconnectWifi() {
        mWifiManager.disconnect();
    }

    public void disableWifi(int netId) {
        mWifiManager.disableNetwork(netId);
        mWifiManager.disconnect();
    }

    public WifiConfiguration createWifiInfo(String SSID, String password, int type) {
        WifiConfiguration config = new WifiConfiguration();
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();
        config.SSID = "\"" + SSID + "\"";

        WifiConfiguration tempConfig = this.isExists(SSID);

        if (tempConfig != null) {
            mWifiManager.removeNetwork(tempConfig.networkId);
        }
        if (type == 1) { // WIFICIPHER_NOPASS
            config.wepKeys[0] = "\"" + "\"";
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.wepTxKeyIndex = 0;
        }
        if (type == 2) {// WIFICIPHER_WEP
            config.hiddenSSID = true;
            config.wepKeys[0] = "\"" + password + "\"";
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.wepTxKeyIndex = 0;
        }
        if (type == 3) { // WIFICIPHER_WPA
            config.preSharedKey = "\"" + password + "\"";
            config.hiddenSSID = true;
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
//             config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            config.status = WifiConfiguration.Status.ENABLED;
        }
        return config;
    }

    /**
     * 开启Wifi-AP热点
     * <uses-permission android:name="android.permission.CHANGE_NETWORK_STATE" />
     *
     * @param configuration 热点配置
     * @param enable        是否开启AP
     * @return 是否成功
     */
    public boolean enableWifiAp(WifiConfiguration configuration, boolean enable) {
        if (enable) {
            closeWifi();
        }
        try {
            Method method = mWifiManager.getClass().getDeclaredMethod("setWifiApEnabled", WifiConfiguration.class, Boolean.TYPE);
            boolean result = (boolean) method.invoke(mWifiManager, configuration, enable);
            System.out.println("WifiAp result:" + result);
            return result;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            System.out.println("WifiAp Error:" + e.toString());
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            System.out.println("WifiAp Error:" + e.toString());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            System.out.println("WifiAp Error:" + e.toString());
        }
        return false;
    }

    private WifiConfiguration isExists(String SSID) { // 查看以前是否已经配置过该SSID
        List<WifiConfiguration> existingConfigs = mWifiManager.getConfiguredNetworks();
        if (existingConfigs == null) {
            return null;
        }
        for (WifiConfiguration existingConfig : existingConfigs) {
            if (existingConfig.SSID.equals("\"" + SSID + "\"")) {
                return existingConfig;
            }
        }
        return null;
    }
}
