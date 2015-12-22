package com.miqian.mq.views;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.miqian.mq.R;
import com.miqian.mq.activity.user.RegisterActivity;
import com.miqian.mq.activity.SendCaptchaActivity;
import com.miqian.mq.utils.MobileOS;
import com.miqian.mq.utils.TypeUtil;
import com.miqian.mq.utils.Uihelper;
import com.umeng.analytics.MobclickAgent;
import com.umeng.onlineconfig.OnlineConfigAgent;

/**
 * Created by Administrator on 2015/9/1.
 */
public abstract class Dialog_Login extends Dialog {
    private final Context mContext;

    public Dialog_Login(Context context) {
        super(context, R.style.Dialog);
        mContext = context;
        this.setContentView(R.layout.dialog_login);
        initView();
    }

    private void initView() {
        final View relaTelephone = findViewById(R.id.rela_telephone);
        final View relaPassword = findViewById(R.id.rela_password);
        final EditText editTelephone = (EditText) findViewById(R.id.edit_telephone);
        final EditText editPassword = (EditText) findViewById(R.id.edit_password);
        Button btnLogin = (Button) findViewById(R.id.btn_login);
        findViewById(R.id.tv_login_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MobclickAgent.onEvent(mContext, "1048");
                //跳到注册页
                dismiss();
                mContext.startActivity(new Intent(mContext, RegisterActivity.class));

            }
        });
        findViewById(R.id.tv_login_forgetpw).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MobclickAgent.onEvent(mContext, "1047");
                SendCaptchaActivity.enterActivity(mContext, TypeUtil.SENDCAPTCHA_FORGETPSW, false);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MobclickAgent.onEvent(mContext, "1046");
                String telephone = editTelephone.getText().toString();
                String password = editPassword.getText().toString();
                if (!TextUtils.isEmpty(telephone)) {
                    if (MobileOS.isMobileNO(telephone) && telephone.length() == 11) {
                        if (!TextUtils.isEmpty(password)) {
                            if (password.length() >= 6 && password.length() <= 16) {
                                dismiss();
                                login(telephone, password);
                            } else {
                                Uihelper.showToast((Activity) mContext, R.string.tip_password_login);
                            }

                        } else {
                            Uihelper.showToast(mContext, "密码不能为空");
                        }
                    } else {
                        Uihelper.showToast((Activity) mContext, R.string.phone_noeffect);
                    }
                } else {
                    Uihelper.showToast(mContext, "手机号码不能为空");
                }

            }
        });
        View view_QQredBag = findViewById(R.id.layout_qq_redbag);
        //在线参数
        OnlineConfigAgent.getInstance().updateOnlineConfig(mContext);
        OnlineConfigAgent.getInstance().setDebugMode(false);
        String value = OnlineConfigAgent.getInstance().getConfigParams(mContext, "ShowQQRedBag");
        if ("YES".equals(value)) {
            view_QQredBag.setVisibility(View.VISIBLE);
            view_QQredBag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContext.startActivity(new Intent(mContext, RegisterActivity.class));
                }
            });
        }

    }

    public abstract void login(String telephone, String password);

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

}
