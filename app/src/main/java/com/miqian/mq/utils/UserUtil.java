package com.miqian.mq.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.miqian.mq.activity.current.ActivityRealname;
import com.miqian.mq.encrypt.RSAUtils;
import com.miqian.mq.entity.LoginResult;
import com.miqian.mq.entity.UserInfo;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.receiver.JpushHelper;
import com.miqian.mq.views.DialogPay;
import com.miqian.mq.views.Dialog_Login;


public class UserUtil {

//    private static Context context;
//    private static DialogPay dialogPay;

    public static void saveToken(Context context, String token, String userId) {
        Pref.saveString(Pref.TOKEN, token, context);
        Pref.saveString(Pref.USERID, RSAUtils.decryptByPrivate(userId), context);
    }

    public static void saveUserInfo(Context context, UserInfo userInfo) {
        Pref.saveString(Pref.TOKEN, userInfo.getToken(), context);
        Pref.saveString(Pref.USERID, RSAUtils.decryptByPrivate(userInfo.getCustId()), context);
        Pref.saveString(getPrefKey(context, Pref.REALNAME_STATUS), userInfo.getRealNameStatus(), context);
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
        // 清空token 和UseId
        Pref.saveString(Pref.TOKEN, "", context);
        Pref.saveString(Pref.USERID, "", context);
    }

    public static boolean isLogin(final Activity context, final Class<?> cls) {
        if (!hasLogin(context)) {
            Dialog_Login dialog_login = new Dialog_Login(context) {
                @Override
                public void login(String telephone, String password) {
                    HttpRequest.login(context, new ICallback<LoginResult>() {
                        @Override
                        public void onSucceed(LoginResult result) {
                            UserInfo userInfo = result.getData();
                            UserUtil.saveUserInfo(context, userInfo);
                            Intent intent = new Intent(context, cls);
                            context.startActivity(intent);
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
            Intent intent = new Intent(context, cls);
            context.startActivity(intent);
            return true;
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

                            if (userInfo.getRealNameStatus().equals("0")) {
                                realName(context);
                            } else {
                                dialogPay.show();
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
            String realnameStatus = Pref.getString(getPrefKey(context, Pref.REALNAME_STATUS), context, "0");
            if (realnameStatus.equals("0")) {
                HttpRequest.getUserInfo(context, new ICallback<LoginResult>() {
                    @Override
                    public void onSucceed(LoginResult result) {
                        UserInfo userInfo = result.getData();
                        if (userInfo.getRealNameStatus().equals("0")) {
                            realName(context);
                        } else {
                            Pref.saveString(getPrefKey(context, Pref.REALNAME_STATUS), userInfo.getRealNameStatus(), context);
                            dialogPay.show();
                        }
                    }

                    @Override
                    public void onFail(String error) {
                        Uihelper.showToast(context, error);
                    }
                });
            } else {
                dialogPay.show();
            }
        }
    }

    //  跳转实名认证页
    public static void realName(Activity activity) {
        Intent intent = new Intent(activity, ActivityRealname.class);
        activity.startActivity(intent);
    }
    //  跳转活期认购页
    public static void currenPay(Activity activity,  final Class<?> cls, String money) {
        Intent intent = new Intent(activity, cls);
        intent.putExtra("money", money);
        intent.putExtra("prodId", "1");//0:充值产品  1:活期赚 2:活期转让赚 3:定期赚 4:定期转让赚 5: 定期计划 6: 计划转让
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
