package com.miqian.mq.views;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.activity.SendCaptchaActivity;
import com.miqian.mq.activity.WebActivity;
import com.miqian.mq.entity.LoginResult;
import com.miqian.mq.entity.UserInfo;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.net.Urls;
import com.miqian.mq.utils.MobileOS;
import com.miqian.mq.utils.MyTextWatcher;
import com.miqian.mq.utils.Pref;
import com.miqian.mq.utils.TypeUtil;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.utils.UserUtil;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by Administrator on 2015/9/1.
 */
public abstract class Dialog_Login extends Dialog {

    private final Activity mContext;
    private Dialog mWaitingDialog;

    public Dialog_Login(Activity context) {
        super(context, R.style.Dialog);
        mContext = context;
        this.setContentView(R.layout.dialog_login);
        initView();
    }

    private void initView() {
        mWaitingDialog = ProgressDialogView.create(mContext);
        final EditText editTelephone = (EditText) findViewById(R.id.edit_telephone);
        final EditText editPassword = (EditText) findViewById(R.id.edit_password);

        String telphone = Pref.getString(Pref.TELEPHONE, mContext, "");
        editTelephone.setText(telphone);
        editTelephone.setSelection(telphone.length());

        final Button btnLogin = (Button) findViewById(R.id.btn_login);
        CheckBox checkBoxLaw = (CheckBox) findViewById(R.id.check_law_dialog_login);

        TextView tvLaw = (TextView) findViewById(R.id.text_law);
        SpannableString spanLaw = new SpannableString("我已阅读并同意《网络借贷风险");
        spanLaw.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.mq_b2)), 0, 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvLaw.setText(spanLaw);
        checkBoxLaw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    btnLogin.setEnabled(true);
                } else {
                    btnLogin.setEnabled(false);
                }
            }
        });

        findViewById(R.id.layout_net_law).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WebActivity.startActivity(mContext, Urls.web_register_law_net);
            }
        });


        findViewById(R.id.tv_login_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MobclickAgent.onEvent(mContext, "1048");
                //跳到注册页
                dismiss();
                UserUtil.showRegisterDialog(mContext);

            }
        });
        editTelephone.addTextChangedListener(new MyTextWatcher() {
            @Override
            public void myAfterTextChanged(Editable arg0) {
                String phone = editTelephone.getText().toString();
                if (TextUtils.isEmpty(phone)) {
                    editPassword.setText("");
                }
            }
        });
        findViewById(R.id.tv_login_forgetpw).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
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
                                login(telephone, password);
                            } else {
                                Uihelper.showToast(mContext, R.string.tip_password_login);
                            }

                        } else {
                            Uihelper.showToast(mContext, "密码不能为空");
                        }
                    } else {
                        Uihelper.showToast(mContext, R.string.phone_noeffect);
                    }
                } else {
                    Uihelper.showToast(mContext, "手机号码不能为空");
                }

            }
        });
    }

    public abstract void loginSuccess();

    public void login(String telephone, String password) {
        begin();
        HttpRequest.login(mContext, new ICallback<LoginResult>() {
            @Override
            public void onSucceed(LoginResult result) {
                UserInfo userInfo = result.getData();
                UserUtil.saveUserInfo(mContext, userInfo);
                loginSuccess();
                end();
            }

            @Override
            public void onFail(String error) {
                end();
                Uihelper.showToast(mContext, error);
            }
        }, telephone, password);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * 显示 loading 对话框
     */
    protected void begin() {
        if (mWaitingDialog != null) {
            mWaitingDialog.show();
        }
    }

    /**
     * 显示 loading 对话框
     */
    protected void end() {
        if (mWaitingDialog != null && mWaitingDialog.isShowing()) {
            mWaitingDialog.dismiss();
        }
    }
}
