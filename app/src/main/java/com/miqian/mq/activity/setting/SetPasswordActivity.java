package com.miqian.mq.activity.setting;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.miqian.mq.R;
import com.miqian.mq.activity.BaseActivity;
import com.miqian.mq.entity.Meta;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.utils.ExtendOperationController;
import com.miqian.mq.utils.TypeUtil;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.views.WFYTitle;


/**
 * Description:设置密码
 *
 * @author TuLiangtan
 * @created 2015-3-19
 */
public class SetPasswordActivity extends BaseActivity {

    private String captcha;
    private EditText et_password_confirm;
    private EditText et_password;
    private int mType;
    private String phone;
    private EditText et_oldpassword;

    @Override
    public void onCreate(Bundle arg0) {
        // getIntent
        Intent intent = getIntent();
        mType = intent.getIntExtra("type", 0);
        captcha = intent.getStringExtra("captcha");
        phone = intent.getStringExtra("phone");
        super.onCreate(arg0);
    }

    @Override
    public void obtainData() {

    }

    @Override
    public void initView() {
        et_password = (EditText) findViewById(R.id.et_password);
        et_oldpassword = (EditText) findViewById(R.id.et_oldpassword);
        et_password_confirm = (EditText) findViewById(R.id.et_password_confirm);
        if (mType == TypeUtil.CAPTHCA_MODIFYLOGINPW) {
            findViewById(R.id.layout_oldpassword).setVisibility(View.VISIBLE);
            findViewById(R.id.divider_oldpassword).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_setpassword;
    }

    public void btn_click(View v) {

        String password = et_password.getText().toString();
        String oldpassword = et_oldpassword.getText().toString();
        String password_confirm = et_password_confirm.getText().toString();
        if (mType == TypeUtil.CAPTHCA_MODIFYLOGINPW) {
            if (TextUtils.isEmpty(oldpassword)) {
                Uihelper.showToast(this, "请输入旧密码");
                return;
            } else {
                if (password.length() < 6 || password.length() > 20) {
                    Uihelper.showToast(this, R.string.tip_password);
                    return;
                }
            }
        }
        if (!TextUtils.isEmpty(password)) {
            if (password.length() < 6 || password.length() > 20) {
                Uihelper.showToast(this, R.string.tip_password);
            } else {
                if (!TextUtils.isEmpty(password_confirm)) {
                    if (password_confirm.equals(password)) {
                        forget_summit(oldpassword, password_confirm);
                    } else {
                        Uihelper.showToast(this, "两次密码不一致，请重新输入");
                    }

                } else {
                    Uihelper.showToast(this, "请再次输入新密码");
                }
            }
        } else {
            Uihelper.showToast(this, "请输入新密码");
        }

    }

    private void forget_summit(String oldpassword, String password_confirm) {
        //设置登录密码
        if (mType == TypeUtil.CAPTCHA_FINDPASSWORD) {
            begin();
            HttpRequest.getPassword(this, new ICallback<Meta>() {
                @Override
                public void onSucceed(Meta result) {
                    end();
                    Uihelper.showToast(mActivity, "设置密码成功");
                    SetPasswordActivity.this.finish();
                    ExtendOperationController.getInstance().doNotificationExtendOperation(ExtendOperationController.OperationKey.BACK_USER, null);
                    ExtendOperationController.getInstance().doNotificationExtendOperation(ExtendOperationController.OperationKey.CHANGE_TOKEN, null);
                }

                @Override
                public void onFail(String error) {
                    end();
                    Uihelper.showToast(mActivity, error);
                }
            }, phone, password_confirm, password_confirm, captcha);
        }

        //修改登录密码
        else {
            begin();
            HttpRequest.changePassword(this, new ICallback<Meta>() {
                @Override
                public void onSucceed(Meta result) {
                    end();
                    Uihelper.showToast(mActivity, "设置密码成功");
                    SetPasswordActivity.this.finish();
                    ExtendOperationController.getInstance().doNotificationExtendOperation(ExtendOperationController.OperationKey.BACK_USER, null);
                    ExtendOperationController.getInstance().doNotificationExtendOperation(ExtendOperationController.OperationKey.CHANGE_TOKEN, null);
                }

                @Override
                public void onFail(String error) {
                    end();
                    Uihelper.showToast(mActivity, error);
                }
            }, oldpassword, password_confirm, password_confirm, captcha);
        }
    }


    @Override
    public void initTitle(WFYTitle mTitle) {

        if (mType == TypeUtil.CAPTCHA_FINDPASSWORD) {
            mTitle.setTitleText("设置登录密码");
        } else {
            mTitle.setTitleText("修改登录密码");
        }

    }

    @Override
    protected String getPageName() {
        if (mType == TypeUtil.CAPTCHA_FINDPASSWORD) {
            return "设置登录密码";
        } else {
            return "修改登录密码";
        }
    }
}
