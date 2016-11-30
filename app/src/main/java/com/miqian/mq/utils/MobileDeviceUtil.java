package com.miqian.mq.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import java.util.UUID;


public class MobileDeviceUtil {
    /**
     * 上下文对象
     */
    private Context context;
    /**
     * 手机IMEI号
     */
    private String imei;
    /**
     * 手机MAC地址
     */
    private String mac;
    /**
     * 获取手机号
     */
    private String phoneNo;
    /**
     * 手机设备号
     */
    private String deviceID;
    /**
     * 手机imsi号
     */
    private String imsi;
    /**
     * AndroidId
     */
    private String androidId;
    /**
     * 手机型号
     */
    private String mobileModel;
    /**
     * 系统版本号
     */
    private String systemVersion;
    /**
     * 运营商
     */
    private String operator;
    /**
     * 屏幕宽
     */
    private int screenWidth;
    /**
     * 屏幕高
     */
    private int screenHight;
    /**
     * 屏幕密度
     */
    private float density;
    /**
     * 手机制造商
     */
    private String mobileProduct;
    /**
     * 软件版本号
     */
    private String versonCode;
    /**
     * 软件版本名称
     */
    private String versonName;
    /**
     * 生成唯一的UUID
     */
    private String uuid;
    /**
     * 获取渠道名称
     */
    private String channalName;
    /**
     * 屏幕密度Dpi
     */
    private int densityDpi;
    /**
     * 单例对象实例
     */
    private static MobileDeviceUtil instance = null;

    private MobileDeviceUtil(Context context) {
        this.context = context;
    }

    /**
     * 获取手机系统信息的单例类
     * 
     * @param context
     * @return
     */
    public static MobileDeviceUtil getInstance(Context context) {
        if (instance == null) {
            instance = new MobileDeviceUtil(context);
        }
        return instance;
    }

    /**
     * 获取手机imei号
     * 
     * @return
     */
    public String getImei() {
        if (TextUtils.isEmpty(imei)) {
            try {
                TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                imei = tm.getDeviceId();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return imei;

    }

    /**
     * 获取mac
     * 
     * @return
     */
    public String getMacAddress() {
        if (TextUtils.isEmpty(mac)) {
            try {
                WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo info = wifi.getConnectionInfo();
                mac = info.getMacAddress();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return mac;
    }

    /**
     * 获取手机号码Emei号
     * 
     * @return
     */
    public String getPhoneNum() {
        try {
            if (TextUtils.isEmpty(phoneNo)) {
                TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                if (null != tm) {
                    phoneNo = tm.getLine1Number();
                }
                if (TextUtils.isEmpty(phoneNo))
                    phoneNo = "";
            }
        }catch (Exception e) {
            e.printStackTrace();
            phoneNo = "";
        }

        return phoneNo;

    }

    /**
     * 返回设备号
     * 
     * @return
     */
    public String getMobileImei() {
        if (TextUtils.isEmpty(deviceID)) {
            try {
                TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                deviceID = tm.getDeviceId();// String
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return deviceID;
    }

    /**
     * 获取手机的imsi信息
     * 
     * @return
     */
    public String getMobileImsi() {
        if (TextUtils.isEmpty(imsi)) {
            try {
                TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                imsi = tm.getSubscriberId();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return imsi;
    }

    /**
     * 获取androidId
     * 
     * @return
     */
    public String getAndroidId() {
        if (TextUtils.isEmpty(androidId)) {
            try {
                androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return androidId;
    }

    /**
     * 获取手机型号
     * 
     * @return
     */
    public String getMobileModel() {
        if (TextUtils.isEmpty(mobileModel)) {
            try {
                mobileModel = Build.MODEL;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return mobileModel;

    }

    /**
     * 获取系统版本号
     *
     * @return
     */
    public String getSystemVersion() {
        if (TextUtils.isEmpty(systemVersion)) {
            try {
                systemVersion = Build.VERSION.RELEASE;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return systemVersion;
    }

    /**
     * 获取运营商
     * 
     * @return
     */
    public String getOperator() {
        if (TextUtils.isEmpty(operator)) {
            try {
                TelephonyManager telephonyManager = (TelephonyManager) context
                        .getSystemService(Context.TELEPHONY_SERVICE);
                String IMSI = telephonyManager.getSubscriberId();
                if (TextUtils.isEmpty(IMSI)) {
                    operator = "";
                } else {
                    // IMSI号前面3位460是国家，紧接着后面2位00 02是中国移动，01是中国联通，03是中国电信。
                    if (IMSI.startsWith("46000") || IMSI.startsWith("46002")|| IMSI.startsWith("46007")) {
                        operator = "中国移动";
                    } else if (IMSI.startsWith("46001")|| IMSI.startsWith("46006")) {
                        operator = "中国联通";
                    } else if (IMSI.startsWith("46003")|| IMSI.startsWith("46005")) {
                        operator = "中国电信";
                    } else if(IMSI.startsWith("46020")) {
                        operator = "中国铁通";
                    }else{
                        operator = "";  
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return operator;

    }

    /**
     * 获取运营商类型 0=不限制，1=移动，2=联通，3=电信
     * 
     * @return
     */
    public String getOperatorType() {
        String operatorType = "0";
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String IMSI = telephonyManager.getSubscriberId();
            if (TextUtils.isEmpty(IMSI)) {
                operatorType = "0";
            } else {
                // IMSI号前面3位460是国家，紧接着后面2位00 02是中国移动，01是中国联通，03是中国电信。
                if (IMSI.startsWith("46000") || IMSI.startsWith("46002")|| IMSI.startsWith("46007")) {
                    operatorType = "1";
                } else if (IMSI.startsWith("46001")|| IMSI.startsWith("46006")) {
                    operatorType = "2";
                } else if (IMSI.startsWith("46003")|| IMSI.startsWith("46005")) {
                    operatorType = "3";
                } else {
                    operatorType = "0";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return operatorType;
    }

    /**
     * 获取网络类型
     * 
     */
    public String getNetType() {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI); // wifi
        NetworkInfo gprs = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE); // gprs
        if (wifi != null && wifi.getState() == State.CONNECTED) {
            return "WIFI";
        } else if (gprs != null && gprs.getState() == State.CONNECTED) {
            return "GPRS";
        } else {
            return "";
        }

    }

    /**
     * 获取屏幕宽
     * 
     * @return
     */
    public int getScreenWidth() {
        if (screenWidth == 0) {
            try {
                DisplayMetrics dm = context.getResources().getDisplayMetrics();
                screenWidth = dm.widthPixels;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return screenWidth;

    }

    /**
     * 获取屏幕高
     * 
     * @return
     */
    public int getScreenHeight() {
        if (screenHight == 0) {
            try {
                DisplayMetrics dm = context.getResources().getDisplayMetrics();
                screenHight = dm.heightPixels;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return screenHight;

    }

    /**
     * 获取屏幕密度
     * 
     * @return
     */
    public float getScreenDensity() {
        if (density == 0) {
            try {
                DisplayMetrics dm = context.getResources().getDisplayMetrics();
                density = dm.density;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return density;

    }

    /**
     * 获取屏幕DPI
     * 
     *
     * @return
     */
    public int getScreenDensityDpi() {
        if (densityDpi == 0) {
            try {
                DisplayMetrics dm = context.getResources().getDisplayMetrics();
                densityDpi = dm.densityDpi;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return densityDpi;

    }

    /**
     * 获取设备类型
     * 
     *
     * @return
     */
    public String getDeviceType() {
        return "Android";
    }

    /**
     * 获取手机制造商
     * 
     *
     * @return
     */
    public String getMobileProduct() {
        if (TextUtils.isEmpty(mobileProduct)) {
            try {
                mobileProduct = Build.PRODUCT;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return mobileProduct;
    }

    /**
     * 获取版本号
     * 
     * @return
     */
    public String getVersonCode() {
        if (TextUtils.isEmpty(versonCode)) {
            try {
                PackageInfo pinfo = context.getPackageManager().getPackageInfo(context.getPackageName(),
                        PackageManager.GET_CONFIGURATIONS);
                versonCode = pinfo.versionCode + "";
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return versonCode;
    }

    /**
     * 获取版本名称
     * 
     * @return
     */
    public String getVersonName() {
        if (TextUtils.isEmpty(versonName)) {
            versonName = "no";
            try {
                PackageInfo pinfo = context.getPackageManager().getPackageInfo(context.getPackageName(),
                        PackageManager.GET_CONFIGURATIONS);
                versonName = pinfo.versionName;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return versonName;
    }

    /**
     * 自动生成一个UUID来记录安装量
     */
    public String getUUID(){
        uuid = Pref.getString("sysCacheMap",context, "");

        if(TextUtils.isEmpty(uuid)){
            uuid = UUID.randomUUID().toString();
            Pref.saveString("sysCacheMap", uuid, context);
        }

        return uuid;
    }

     /**
     * 读取渠道名称
     * 
     * @return
     */
    public String getChannalName() {
        //请根据生成渠道包的方式进行获取
        return channalName;
    }

}
