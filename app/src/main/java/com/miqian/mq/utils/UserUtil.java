package com.miqian.mq.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.miqian.mq.activity.GestureLockSetActivity;
import com.miqian.mq.activity.current.CurrentInvestment;
import com.miqian.mq.encrypt.RSAUtils;
import com.miqian.mq.entity.LoginResult;
import com.miqian.mq.entity.UserInfo;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.receiver.JpushHelper;
import com.miqian.mq.views.DialogPay;
import com.miqian.mq.views.Dialog_Login;


public class UserUtil {

    public static void saveToken(Context context, String token, String userId) {
        Pref.saveString(Pref.TOKEN, token, context);
        Pref.saveString(Pref.USERID, RSAUtils.decryptByPrivate(userId), context);
    }

    public static void saveUserInfo(Context context, UserInfo userInfo) {
        Pref.saveString(Pref.TOKEN, userInfo.getToken(), context);
        Pref.saveString(Pref.USERID, RSAUtils.decryptByPrivate(userInfo.getCustId()), context);
        Pref.saveString(Pref.TELEPHONE, RSAUtils.decryptByPrivate(userInfo.getMobilePhone()), context);
        Pref.saveInt(getPrefKey(context, Pref.PAY_STATUS), Integer.parseInt(userInfo.getPayPwdStatus()), context);
        //设置极光别名
        JpushHelper.setAlias(context);
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
        if (TextUtils.isEmpty(token)) {
            return false;
        } else {
            return true;
        }
    }

    public static void clearUserInfo(Context context) {
        // 清空 手势密码 和 token 和 UseId
        Pref.saveBoolean(Pref.GESTURESTATE, false, context);
        Pref.saveString(Pref.GESTUREPSW, null, context);
        Pref.saveString(Pref.TOKEN, "", context);
        Pref.saveString(Pref.USERID, "", context);
    }

    public static boolean isLogin(final Activity context, final Class<?> cls) {
        if (!hasLogin(context)) {
            Dialog_Login dialog_login = new Dialog_Login(context) {
                @Override
                public void login(String telephone, String password) {
                    // TODO: 2015/10/10 Loading 
                    HttpRequest.login(context, new ICallback<LoginResult>() {
                        @Override
                        public void onSucceed(LoginResult result) {
                            UserInfo userInfo = result.getData();
                            UserUtil.saveUserInfo(context, userInfo);
                            GestureLockSetActivity.startActivity(context, cls);
                        }

                        @Override
                        public void onFail(String error) {
                            Uihelper.showToast(context, error);
                        }
                    }, telephone, password);
                }
            };
            dialog_login.show();
            return false;
        } else {
            context.startActivity(new Intent(context, cls));
            return true;
        }
    }

    public static void loginWebView(final Activity context, final Class<?> cls, final String url) {
        if (!hasLogin(context)) {
            Dialog_Login dialog_login = new Dialog_Login(context) {
                @Override
                public void login(String telephone, String password) {
                    // TODO: 2015/10/10 Loading
                    HttpRequest.login(context, new ICallback<LoginResult>() {
                        @Override
                        public void onSucceed(LoginResult result) {
                            context.finish();
                            UserInfo userInfo = result.getData();
                            UserUtil.saveUserInfo(context, userInfo);
                            GestureLockSetActivity.startWebActivity(context, cls, url);
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
                            GestureLockSetActivity.startActivity(context, null);
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

    /**
     * 跳转认购页
     *
     * @param money              认购金额
     * @param prodId             0:充值产品  1:活期赚 2:活期转让赚 3:定期赚 4:定期转让赚 5: 定期计划 6: 计划转让
     * @param subjectId          标的id，活期默认为0
     * @param interestRateString 定期计划和定期赚的利率和期限
     */
    public static void currenPay(Activity activity, String money, String prodId, String subjectId, String interestRateString) {
        Intent intent = new Intent(activity, CurrentInvestment.class);
        intent.putExtra("money", money);
        intent.putExtra("prodId", prodId);
        intent.putExtra("subjectId", subjectId);
        intent.putExtra("interestRateString", interestRateString);
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
