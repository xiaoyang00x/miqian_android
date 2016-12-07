package com.miqian.mq.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.growingio.android.sdk.collection.GrowingIO;
import com.miqian.mq.activity.GestureLockSetActivity;
import com.miqian.mq.activity.current.CurrentInvestment;
import com.miqian.mq.encrypt.RSAUtils;
import com.miqian.mq.entity.LoginResult;
import com.miqian.mq.entity.UserInfo;
import com.miqian.mq.listener.HomeAdsListener;
import com.miqian.mq.listener.ListenerManager;
import com.miqian.mq.listener.LoginListener;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.receiver.JpushHelper;
import com.miqian.mq.views.DialogPay;
import com.miqian.mq.views.Dialog_Login;
import com.miqian.mq.views.Dialog_Register;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.Set;


public class UserUtil {

    public static void saveToken(Context context, String token, String userId) {
        Pref.saveString(Pref.TOKEN, token, context);
        Pref.saveString(Pref.USERID, RSAUtils.decryptByPrivate(userId), context);
    }

    public static void saveUserInfo(Context context, UserInfo userInfo) {
        Pref.saveString(Pref.TOKEN, userInfo.getToken(), context);
        Pref.saveString(Pref.USERID, RSAUtils.decryptByPrivate(userInfo.getCustId()), context);
        Pref.saveString(Pref.TELEPHONE, RSAUtils.decryptByPrivate(userInfo.getMobilePhone()), context);
        Pref.saveString(Pref.REAL_NAME, RSAUtils.decryptByPrivate(userInfo.getRealName()), context);
        Pref.saveInt(getPrefKey(context, Pref.PAY_STATUS), Integer.parseInt(userInfo.getPayPwdStatus()), context);
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

    public static void loginActivity(final Activity context, final Class<?> cls, Dialog_Login dialog_login) {
        if (!hasLogin(context)) {
            dialog_login.show();
        } else {
            context.startActivity(new Intent(context, cls));
        }
    }

    public static void loginActivity(Dialog_Login dialog_login) {
        dialog_login.show();
    }

    //  支付时判断是否登录、实名认证
    public static void loginPay(final Activity context, final DialogPay dialogPay) {
        if (!UserUtil.hasLogin(context)) {
            Dialog_Login dialog_login = new Dialog_Login(context) {
                @Override
                public void login(String telephone, String password) {
                    HttpRequest.login(context, new ICallback<LoginResult>() {
                        @Override
                        public void onSucceed(LoginResult result) {
                            dismiss();
                            UserInfo userInfo = result.getData();
                            saveUserInfo(context, userInfo);

                            dialogPay.show();
                            if (Pref.getBoolean(Pref.GESTURESTATE, context, true)) {
                                GestureLockSetActivity.startActivity(context, null);
                            }
                        }

                        @Override
                        public void onFail(String error) {
                            Uihelper.showToast(context, error);
                        }
                    }, telephone, password);
                }
            };
            dialog_login.show();
        } else {
            dialogPay.show();
        }
    }
    //  手Q活动时判断是否登录，未登录弹注册窗口
    public static void registerPay(final Activity context, final DialogPay dialogPay) {
        if (!UserUtil.hasLogin(context)) {
            Dialog_Register dialog_login = new Dialog_Register(context) {
                @Override
                public void toLogin() {
                    dismiss();
                    showLoginDialog(context);
                }

                @Override
                public void registerSuccess() {
                    dismiss();
                    dialogPay.show();

                }
            };
            dialog_login.show();
        } else {
            dialogPay.show();
        }
    }


    //  手Q活动认购时弹注册页
    public static void showRegisterDialog(final Activity context) {
        Dialog_Register dialog_login = new Dialog_Register(context) {

            @Override
            public void toLogin() {
                dismiss();
                showLoginDialog(context);
            }
            @Override
            public void registerSuccess() {
                         dismiss();
            }
        };
        dialog_login.show();
    }

    //  认购时确认是否登录
    public static void showLoginDialog(final Activity context) {
        Dialog_Login dialog_login = new Dialog_Login(context) {
            @Override
            public void login(String telephone, String password) {
                HttpRequest.login(context, new ICallback<LoginResult>() {
                    @Override
                    public void onSucceed(LoginResult result) {
                        dismiss();
                        UserInfo userInfo = result.getData();
                        saveUserInfo(context, userInfo);

                        if (Pref.getBoolean(Pref.GESTURESTATE, context, true)) {
                            GestureLockSetActivity.startActivity(context, null);
                        }
                    }

                    @Override
                    public void onFail(String error) {
                        Uihelper.showToast(context, error);
                    }
                }, telephone, password);
            }
        };
        dialog_login.show();
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
