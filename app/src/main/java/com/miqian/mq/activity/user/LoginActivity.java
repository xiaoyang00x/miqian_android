package com.miqian.mq.activity.user;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.activity.GestureLockSetActivity;
import com.miqian.mq.encrypt.RSAUtils;
import com.miqian.mq.entity.Login;
import com.miqian.mq.entity.LoginResult;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.utils.ExtendOperationController;
import com.miqian.mq.utils.MobileOS;
import com.miqian.mq.utils.Pref;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.utils.UserUtil;
import com.miqian.mq.views.ProgressDialogView;
import com.miqian.mq.views.SystemBarTintManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 登录
 * Created by Tlt on 2017/6/9.
 */
public class LoginActivity extends Activity {

    @BindView(R.id.edit_telephone)
    EditText editPhone;
    @BindView(R.id.edit_password)
    EditText editPassword;
    @BindView(R.id.tv_2rigister)
    TextView tvTorigister;
    private Dialog mWaitingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_login);
        if (MobileOS.isKitOrNewer()) {
            // 创建状态栏的管理实例
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            // 激活状态栏设置
            tintManager.setStatusBarTintEnabled(true);
            // 设置一个颜色给系统栏
            tintManager.setStatusBarTintResource(R.color.base_background);
        }
        ButterKnife.bind(this);
        initView();
    }

    @Override
    public void finish() {
        super.finish();
        this.overridePendingTransition(R.anim.activity_anim_scenic_no, R.anim.activity_anim_scenic_out);
    }

    private void initView() {
        mWaitingDialog = ProgressDialogView.create(this);
        tvTorigister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                finish();
                overridePendingTransition(R.anim.activity_anim_scenic_in, R.anim.activity_anim_scenic_no);
            }
        });
    }

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, LoginActivity.class);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.activity_anim_scenic_in, R.anim.activity_anim_scenic_no);
    }

    @OnClick(R.id.btn_back) //返回按钮
    public void back() {
        this.finish();
    }

    @OnClick(R.id.btn_login)  //登录
    public void login() {

        String telephone = editPhone.getText().toString();
        String password = editPassword.getText().toString();
        if (!TextUtils.isEmpty(telephone)) {
            if (MobileOS.isMobileNO(telephone) && telephone.length() == 11) {
                if (!TextUtils.isEmpty(password)) {
                    if (password.length() >= 6 && password.length() <= 16) {
                        login(telephone, password);
                    } else {
                        Uihelper.showToast(LoginActivity.this, R.string.tip_password_login);
                    }
                } else {
                    Uihelper.showToast(LoginActivity.this, "密码不能为空");
                }
            } else {
                Uihelper.showToast(LoginActivity.this, R.string.phone_noeffect);
            }
        } else {
            Uihelper.showToast(LoginActivity.this, "手机号码不能为空");
        }

    }

    private void login(String telephone, String password) {
        begin();
        HttpRequest.login(LoginActivity.this, new ICallback<LoginResult>() {
            @Override
            public void onSucceed(LoginResult result) {
                end();
                Login userInfo = result.getData();
                //保存用户信息
                Pref.saveString(Pref.TOKEN, userInfo.getToken(), LoginActivity.this);
                Pref.saveString(Pref.USERID, RSAUtils.decryptByPrivate(userInfo.getCustId()), LoginActivity.this);
                Pref.saveString(Pref.TELEPHONE, RSAUtils.decryptByPrivate(userInfo.getMobile()), LoginActivity.this);
                if (Pref.getBoolean(Pref.GESTURESTATE, LoginActivity.this, true)) {
                    GestureLockSetActivity.startActivity(LoginActivity.this, null);
                }
                Uihelper.showToast(LoginActivity.this, "登录成功");
                ExtendOperationController.getInstance().doNotificationExtendOperation(ExtendOperationController.OperationKey.LOGIN_SUCCESS,null);
                UserUtil.loginSuccess();
                finish();
            }

            @Override
            public void onFail(String error) {
                end();
                Uihelper.showToast(LoginActivity.this, error);
            }
        }, telephone, password);
    }

    @OnClick(R.id.btn_findpassword) //找回登录密码
    public void findPassWord() {

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
     * 隐藏 loading 对话框
     */
    protected void end() {
        if (mWaitingDialog != null && mWaitingDialog.isShowing()) {
            mWaitingDialog.dismiss();
        }
    }
}
