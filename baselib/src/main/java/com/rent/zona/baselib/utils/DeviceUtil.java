package com.rent.zona.baselib.utils;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;

import com.rent.zona.baselib.log.LibLogger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * @name：
 * @author： liuyun
 * @phone： 15723310658
 * @createTime： 2017/8/14
 * @modifyTime： 2017/8/14
 * @explain：说明
 */


public class DeviceUtil {
    private static String sVersionName;
    private static int sVersionCode;

    private static void loadVersionIfNeeded(Context cxt) {
        if (sVersionName == null) {
            PackageManager manager = cxt.getPackageManager();
            int versionCode = -1;
            try {
                PackageInfo info = manager.getPackageInfo(cxt.getPackageName(), 0);
                sVersionCode = info.versionCode;
                sVersionName = info.versionName;
            } catch (PackageManager.NameNotFoundException e) {
            }
        }
    }

    public static int getVersionCode(Context cxt) {
        loadVersionIfNeeded(cxt);
        return sVersionCode;
    }

    public static String getVersionName(Context cxt) {
        loadVersionIfNeeded(cxt);
        return sVersionName;
    }

    public static String getCustomUserAgent(Context cxt) {
        return "QFT/" + sVersionName + " " + "lan/" + getLanguage(cxt);
    }
    public static String getLanguage(Context context) {
        Locale locale = context.getResources().getConfiguration().locale;
        String language = locale.getLanguage().toLowerCase(locale);
        String country = locale.getCountry().toLowerCase(locale);
        if (TextUtils.isEmpty(country)) {
            return language;
        } else {
            return language + "_" + country;
        }
    }
    public static String getSystemProperty(String propName) {
        String line;
        BufferedReader input = null;
        try {
            Process p = Runtime.getRuntime().exec("getprop " + propName);
            input = new BufferedReader(new InputStreamReader(p.getInputStream()), 1024);
            line = input.readLine();
            input.close();
        } catch (IOException ex) {
            return null;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                }
            }
        }
        return line;
    }
    public static boolean isMIUIRom(){
        String property = getSystemProperty("ro.miui.ui.version.name");
        System.out.println("00000000000000>>>>>>>>>>"+property);
        return !TextUtils.isEmpty(property);
    }
    /**
     * 获取当前手机系统语言。
     *
     * @return 返回当前系统语言。例如：当前设置的是“中文-中国”，则返回“zh-CN”
     */
    public static String getSystemLanguage() {
        return Locale.getDefault().getLanguage();
    }

    /**
     * 获取当前系统上的语言列表(Locale列表)
     *
     * @return  语言列表
     */
    public static Locale[] getSystemLanguageList() {
        return Locale.getAvailableLocales();
    }

    /**
     * 获取当前手机系统版本号
     *
     * @return  系统版本号
     */
    public static String getSystemVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * 获取手机型号
     *
     * @return  手机型号
     */
    public static String getSystemModel() {
        return android.os.Build.MODEL;
    }

    /**
     * 获取手机厂商
     *
     * @return  手机厂商
     */
    public static String getDeviceBrand() {
        return android.os.Build.BRAND;
    }

    /**
     * 获取手机IMEI(需要“android.permission.READ_PHONE_STATE”权限)
     *
     * @return  手机IMEI
     */
//    public static String getIMEI(Context ctx) {
//        if(PermissionHelper.checkSelfPermission(ctx, Manifest.permission.READ_PHONE_STATE)) {//如果授权
//            TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Activity.TELEPHONY_SERVICE);
//            if (tm != null) {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                    return tm.getImei();
//                }else{
//                    return tm.getDeviceId();
//                }
//            }
//        }
//        return "";
//    }
//
//    /**
//     * 获取手机IMSI
//     * @param ctx
//     * @return
//     */
//    public static String getIMSI(Context ctx) {
//        if(PermissionHelper.checkSelfPermission(ctx, Manifest.permission.READ_PHONE_STATE)) {//如果授权
//            TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Activity.TELEPHONY_SERVICE);
//            if (tm != null) {
//                return TextUtils.isEmpty(tm.getSubscriberId()) ? "" : tm.getSubscriberId();
//            }
//        }
//        return "";
//    }

    /**
     * 获取蓝牙mac地址
     * @return
     */
    public static String getBluetoothMacAddress() {
        BluetoothAdapter btAda = BluetoothAdapter.getDefaultAdapter();
        //开启蓝牙
        if (btAda.isEnabled() == false) {
            if (btAda.enable()) {
                while (btAda.getState() == BluetoothAdapter.STATE_TURNING_ON
                        || btAda.getState() != BluetoothAdapter.STATE_ON) {
                    try {
                        Thread.sleep(100);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return TextUtils.isEmpty(btAda.getAddress())?"":btAda.getAddress();
    }
//    public static String getUUID(Context context){
//        return new DeviceUuidFactory(context).getDeviceUuid().toString();
//    }
    public static String getUUID(Context context){
        return new DeviceUuidFactory(context).getDeviceUuid();
    }
    //

    /**
     * 根据Wifi信息获取本地Mac
     * @param context
     * @return
     */
    public static String getWifiMacAddress(Context context){
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        return TextUtils.isEmpty(info.getMacAddress())?"":info.getMacAddress();
    }
    private static final String marshmallowMacAddress = "02:00:00:00:00:00";
    private static final String fileAddressMac = "/sys/class/net/wlan0/address";

    public static String getAdresseMAC(Context context) {
        WifiManager wifiMan = (WifiManager)context.getSystemService(Context.WIFI_SERVICE) ;
        WifiInfo wifiInf = wifiMan.getConnectionInfo();

        if(wifiInf !=null && marshmallowMacAddress.equals(wifiInf.getMacAddress())){
            String result = null;
            try {
                result= getAdressMacByInterface();
                if (result != null){
                    return result;
                } else {
                    result = getAddressMacByFile(wifiMan);
                    return result;
                }
            } catch (IOException e) {
                LibLogger.de("MobileAccess", "Erreur lecture propriete Adresse MAC");
            } catch (Exception e) {
                LibLogger.de("MobileAcces", "Erreur lecture propriete Adresse MAC ");
            }
        } else{
            if (wifiInf != null && wifiInf.getMacAddress() != null) {
                return wifiInf.getMacAddress();
            } else {
                return "";
            }
        }
        return marshmallowMacAddress;
    }

    private static String getAdressMacByInterface(){
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (nif.getName().equalsIgnoreCase("wlan0")) {
                    byte[] macBytes = nif.getHardwareAddress();
                    if (macBytes == null) {
                        return "";
                    }

                    StringBuilder res1 = new StringBuilder();
                    for (byte b : macBytes) {
                        res1.append(String.format("%02X:",b));
                    }

                    if (res1.length() > 0) {
                        res1.deleteCharAt(res1.length() - 1);
                    }
                    return res1.toString();
                }
            }

        } catch (Exception e) {
            LibLogger.de("MobileAcces", "Erreur lecture propriete Adresse MAC ");
        }
        return null;
    }
    private static String getAddressMacByFile(WifiManager wifiMan) throws Exception {
        String ret;
        int wifiState = wifiMan.getWifiState();

        wifiMan.setWifiEnabled(true);
        File fl = new File(fileAddressMac);
        FileInputStream fin = new FileInputStream(fl);
        ret = crunchifyGetStringFromStream(fin);
        fin.close();

        boolean enabled = WifiManager.WIFI_STATE_ENABLED == wifiState;
        wifiMan.setWifiEnabled(enabled);
        return ret;
    }

    private static String crunchifyGetStringFromStream(InputStream crunchifyStream) throws IOException {
        if (crunchifyStream != null) {
            Writer crunchifyWriter = new StringWriter();

            char[] crunchifyBuffer = new char[2048];
            try {
                Reader crunchifyReader = new BufferedReader(new InputStreamReader(crunchifyStream, "UTF-8"));
                int counter;
                while ((counter = crunchifyReader.read(crunchifyBuffer)) != -1) {
                    crunchifyWriter.write(crunchifyBuffer, 0, counter);
                }
            } finally {
                crunchifyStream.close();
            }
            return crunchifyWriter.toString();
        } else {
            return "No Contents";
        }
    }
}
