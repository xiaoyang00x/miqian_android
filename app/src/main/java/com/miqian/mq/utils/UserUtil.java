package com.miqian.mq.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.growingio.android.sdk.collection.GrowingIO;
import com.miqian.mq.activity.GestureLockSetActivity;
import com.miqian.mq.activity.current.CurrentInvestment;
import com.miqian.mq.activity.user.LoginActivity;
import com.miqian.mq.activity.user.RegisterActivity;
import com.miqian.mq.encrypt.RSAUtils;
import com.miqian.mq.entity.UserInfo;
import com.miqian.mq.listener.HomeAdsListener;
import com.miqian.mq.listener.ListenerManager;
import com.miqian.mq.listener.LoginListener;
import com.miqian.mq.receiver.JpushHelper;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.Set;


public class UserUtil {

    public static void saveToken(Context context, String token, String userId) {
        Pref.saveString(Pref.TOKEN, token, context);
        Pref.saveString(Pref.USERID, RSAUtils.decryptByPrivate(userId), context);
    }

    /**
     * 是否存管之前的老用户
     * @param context
     * @return
     */
    public static boolean isSaveBefore(Context context) {
        if ("1".equals(Pref.getString(getPrefKey(context, Pref.IS_SAVE_BEFORE), context, null))) {
            return true;
        }
        return false;
    }

    /**
     * 用户是否完成存管
     * @param context
     * @return
     */
    public static boolean isFinishSave(Context context) {
        int flag = Pref.getInt(getPrefKey(context, Pref.IS_SAVE_FINISH), context, 0);
        if (flag == 1) {
            return true;
        }
        return false;
    }

    /**
     * @param context
     * @param userInfo 通过用户信息判断是否完成
     * @param isFinish 直接判断是否完成
     */
    public static void saveJxSave(Context context, UserInfo userInfo, boolean isFinish) {
        if ("1".equals(userInfo.getJxAccountStatus()) && "1".equals(userInfo.getJxPayPwdStatus()) && "1".equals(userInfo.getJxAutoClaimsTransferStatus()) && "1".equals(userInfo.getJxAutoSubscribeStatus())) {
            Pref.saveInt(getPrefKey(context, Pref.IS_SAVE_FINISH), 1, context);
        }
        if (null == userInfo && isFinish) {
            Pref.saveInt(getPrefKey(context, Pref.IS_SAVE_FINISH), 1, context);
        }
    }

    public static void saveUserInfo(Context context, UserInfo userInfo) {
        Pref.saveString(Pref.TOKEN, userInfo.getToken(), context);
        Pref.saveString(Pref.USERID, RSAUtils.decryptByPrivate(userInfo.getCustId()), context);
        Pref.saveString(Pref.TELEPHONE, RSAUtils.decryptByPrivate(userInfo.getMobile()), context);
        Pref.saveString(Pref.REAL_NAME, RSAUtils.decryptByPrivate(userInfo.getUserName()), context);
        Pref.saveInt(getPrefKey(context, Pref.PAY_STATUS), Integer.parseInt(userInfo.getJxPayPwdStatus()), context);
        Pref.saveString(Pref.CUSTLEVEL, userInfo.getCustLevel(), context);

        //设置GrowingIO用户信息
        GrowingIO growingIO = GrowingIO.getInstance();
        growingIO.setCS1("user_id", RSAUtils.decryptByPrivate(userInfo.getCustId()));
        growingIO.setCS2("user_class", userInfo.getCustLevel());

        //设置极光别名
        JpushHelper.setAlias(context);
        loginSuccess();
    }

    /**
     * 显示广告跳转
     */
    public static void showWebActivity() {
        synchronized (ListenerManager.adsListeners) {
            Set<String> set = ListenerManager.adsListeners.keySet();
            Iterator<String> it = set.iterator();
            while (it.hasNext()) {
                String key = it.next();
                WeakReference<HomeAdsListener> ref = ListenerManager.adsListeners.get(key);
                if (ref != null && ref.get() != null) {
                    HomeAdsListener listener = ref.get();
                    if (listener != null) {
                        listener.showWeb();
                    }
                }
            }
        }
    }

    /**
     * 登录成功通知监听
     */
    public static void loginSuccess() {
        synchronized (ListenerManager.loginListeners) {
            Set<String> set = ListenerManager.loginListeners.keySet();
            Iterator<String> it = set.iterator();
            while (it.hasNext()) {
                String key = it.next();
                WeakReference<LoginListener> ref = ListenerManager.loginListeners.get(key);
                if (ref != null && ref.get() != null) {
                    LoginListener listener = ref.get();
                    if (listener != null) {
                        listener.loginSuccess();
                    }
                }
            }
        }
    }

    /**
     * 退出成功通知监听
     */
    public static void logout() {
        synchronized (ListenerManager.loginListeners) {
            Set<String> set = ListenerManager.loginListeners.keySet();
            Iterator<String> it = set.iterator();
            while (it.hasNext()) {
                String key = it.next();
                WeakReference<LoginListener> ref = ListenerManager.loginListeners.get(key);
                if (ref != null && ref.get() != null) {
                    LoginListener listener = ref.get();
                    if (listener != null) {
                        listener.logout();
                    }
                }
            }
        }
    }

    public static String getToken(Context context) {
        return Pref.getString(Pref.TOKEN, context, "");
    }

    public static String getUserId(Context context) {
        return Pref.getString(Pref.USERID, context, "");
    }

    public static String getPrefKey(Context context, String name) {
        return name + Pref.getString(Pref.USERID, context, Pref.VISITOR);
    }

    public static boolean hasLogin(Context context) {
        String token = getToken(context);
        return !TextUtils.isEmpty(token);
    }

    public static void clearUserInfo(Context context) {
        // 清空 手势密码 和 token 和 UseId
        Pref.saveString(Pref.GESTUREPSW, null, context);
        Pref.saveString(Pref.TOKEN, "", context);
        Pref.saveString(Pref.USERID, "", context);
        Pref.saveString(Pref.REAL_NAME, "", context);
        //token值为"",表示取消之前设置的别名
        JpushHelper.setAlias(context);
        // 通知消息更新
        ExtendOperationController.getInstance().doNotificationExtendOperation(ExtendOperationController.OperationKey.RERESH_JPUSH, null);

        //设置GrowingIO用户信息
        GrowingIO growingIO = GrowingIO.getInstance();
        growingIO.setCS1("user_id", "");
        growingIO.setCS2("user_class", "");


        logout();
    }

    public static void toLoginActivity(final Activity context) {
        if (!UserUtil.hasLogin(context)) {
            LoginActivity.start(context);
        }
    }
    public static void toRegisterctivity(final Activity context) {
        if (!UserUtil.hasLogin(context)) {
            RegisterActivity.start(context);
        }
    }

    public static void afterLoginActivity(final Activity context, final Class<?> cls) {
        if (!UserUtil.hasLogin(context)) {

            LoginActivity.start(context, new LoginListener() {
                @Override
                public void loginSuccess() {
                    if (Pref.getBoolean(Pref.GESTURESTATE, context, true)) {
                        GestureLockSetActivity.startActivity(context, null);
                    } else if (null != cls) {
                        context.startActivity(new Intent(context, cls));
                    }
                }

                @Override
                public void logout() {

                }
            });

        } else {
            context.startActivity(new Intent(context, cls));
        }

    }

    /**
     * 跳转认购页
     *
     * @param money              认购金额
     * @param prodId             0:充值产品  1:活期赚 2:活期转让赚 3:定期赚 4:定期转让赚 5: 定期计划 6: 计划转让
     * @param subjectId          标的id，活期默认为0
     * @param interestRateString 定期计划和定期赚的利率和期限
     */
    public static void currenPay(Activity activity, String money, String prodId, String subjectId, String interestRateString) {
        currenPay(activity, money, prodId, subjectId, interestRateString, null);
    }

    /**
     * 跳转认购页
     *
     * @param money              认购金额
     * @param prodId             0:充值产品  1:活期赚 2:活期转让赚 3:定期赚 4:定期转让赚 5: 定期计划 6: 计划转让
     * @param subjectId          标的id，活期默认为0
     * @param interestRateString 定期计划和定期赚的利率和期限
     * @param realMoney          实际支付金额（认购转让标的显示）
     */
    public static void currenPay(Activity activity, String money, String prodId, String subjectId, String interestRateString, String realMoney) {
        Intent intent = new Intent(activity, CurrentInvestment.class);
        intent.putExtra("money", money);
        intent.putExtra("prodId", prodId);
        intent.putExtra("subjectId", subjectId);
        intent.putExtra("interestRateString", interestRateString);
        intent.putExtra("realMoney", realMoney);
        activity.startActivity(intent);
    }

    //  显示认购额度
//    public static void showDialog(Activity activity) {
//        initDialog(activity);
//        dialogPay.show();
//    }

//    public static void initDialog(final Activity activity) {
//        if (dialogPay != null) {
//            return;
//        }
//        dialogPay = new DialogPay(activity) {
//            @Override
//            public void positionBtnClick(String s) {
////                currenPay(activity, IntoActivity.class);
//            }
//
//            @Override
//            public void negativeBtnClick() {
//
//            }
//        };
//    }

}
