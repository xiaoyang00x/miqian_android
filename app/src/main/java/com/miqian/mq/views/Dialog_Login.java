package com.miqian.mq.views;

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
import com.miqian.mq.activity.RegisterActivity;
import com.miqian.mq.activity.SendCaptchaActivity;
import com.miqian.mq.utils.MobileOS;
import com.miqian.mq.utils.TypeUtil;

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
                //跳到注册页
                dismiss();
                mContext.startActivity(new Intent(mContext, RegisterActivity.class));

            }
        });
        findViewById(R.id.tv_login_forgetpw).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendCaptchaActivity.enterActivity(mContext, TypeUtil.SENDCAPTCHA_FORGETPSW);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String telephone = editTelephone.getText().toString();
                String password = editPassword.getText().toString();
                if (!TextUtils.isEmpty(telephone)) {
                    if (MobileOS.isMobileNO(telephone) && telephone.length() == 11) {
                        if (!TextUtils.isEmpty(password)) {
                            dismiss();
                            login(telephone, password);
                        } else {
                            Toast.makeText(mContext, "密码不能为空", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(mContext, R.string.phone_noeffect, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(mContext, "电话号码不能为空", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public abstract void login(String telephone, String password);

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

}
