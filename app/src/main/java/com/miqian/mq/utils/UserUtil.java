package com.miqian.mq.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.miqian.mq.encrypt.RSAUtils;
import com.miqian.mq.entity.LoginResult;
import com.miqian.mq.entity.UserInfo;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.views.Dialog_Login;


public class UserUtil {

    private static Context context;

    public static void saveToken(Context context, String token, String userId) {
        Pref.saveString(Pref.TOKEN, token, context);
        Pref.saveString(Pref.USERID, userId, context);
    }

    public static String getToken(Context context) {
        return Pref.getString(Pref.TOKEN, context, "");
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

    public static boolean isLogin(final Activity context, final Class<?> cls) {
        if (!hasLogin(context)) {
            Dialog_Login dialog_login = new Dialog_Login(context) {
                @Override
                public void login(String telephone, String password) {
                    HttpRequest.login(context, new ICallback<LoginResult>() {
                        @Override
                        public void onSucceed(LoginResult result) {
                            String name = RSAUtils.decryptByPrivate(result.getData().getRealName());
                            UserInfo userInfo = result.getData();
                            UserUtil.saveToken(context, userInfo.getToken(), userInfo.getCustId());
                            Uihelper.trace(name);

                            Intent intent = new Intent(context, cls);
                            context.startActivity(intent);
                        }

                        @Override
                        public void onFail(String error) {

                        }
                    }, telephone, password);
                }
            };
            dialog_login.show();
            return false;
        } else {
            return true;
        }
    }

}
