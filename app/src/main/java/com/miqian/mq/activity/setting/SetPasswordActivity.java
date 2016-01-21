package com.miqian.mq.activity.setting;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.activity.BaseActivity;
import com.miqian.mq.entity.Meta;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.utils.ExtendOperationController;
import com.miqian.mq.utils.TypeUtil;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.views.DialogTradePassword;
import com.miqian.mq.views.WFYTitle;


/**
 * Description:设置密码
 *
 * @author TuLiangtan
 * @created 2015-3-19
 */
public class SetPasswordActivity extends BaseActivity {

    private String phone;
    private String captcha;
    private EditText et_password_confirm;
    private EditText et_password;
    private String idCard;
    private String style;
    private TextView tv_newpassword, tv_comfirmpassword;
    private int mType;
    private String telephone;

    @Override
    public void onCreate(Bundle arg0) {
        // getIntent
        Intent intent = getIntent();
        captcha = intent.getStringExtra("captcha");
        phone = intent.getStringExtra("phone");
        idCard = intent.getStringExtra("idCard");
        telephone = intent.getStringExtra("telephone");
        mType = intent.getIntExtra("type", 0);
        super.onCreate(arg0);
    }

    @Override
    public void obtainData() {

    }

    @Override
    public void initView() {

        et_password = (EditText) findViewById(R.id.et_password);
        et_password_confirm = (EditText) findViewById(R.id.et_password_confirm);
        tv_comfirmpassword = (TextView) findViewById(R.id.tv_comfirmpassword);
        tv_newpassword = (TextView) findViewById(R.id.tv_newpassword);
        if (mType == TypeUtil.PASSWORD_TRADE || mType == TypeUtil.TRADEPASSWORD_FIRST_SETTING) {
            mTitle.setTitleText("设置交易密码");
            tv_newpassword.setText("设置交易密码");
            tv_comfirmpassword.setText("确定交易密码");
            et_password.setHint("请输入交易密码");
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_setpassword;
    }

    public void btn_click(View v) {

        String password = et_password.getText().toString();
        String password_confirm = et_password_confirm.getText().toString();
        if (!TextUtils.isEmpty(password)) {
            if (password.length() < 6 || password.length() > 20) {
                Uihelper.showToast(this, R.string.tip_password);
            } else {
                if (!TextUtils.isEmpty(password_confirm)) {
                    if (password_confirm.equals(password)) {
                        forget_summit(password_confirm);
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

    private void forget_summit(String password_confirm) {
        //设置登录密码
        if (mType == TypeUtil.PASSWORD_LOGIN) {
            begin();
            HttpRequest.getPassword(this, new ICallback<Meta>() {
                @Override
                public void onSucceed(Meta result) {
                    end();
                    Uihelper.showToast(mActivity, "设置密码成功");
                    SetPasswordActivity.this.finish();
                }

                @Override
                public void onFail(String error) {
                    end();
                    Uihelper.showToast(mActivity, error);
                }
            }, phone, password_confirm, password_confirm, captcha);
        }
        //修改交易密码

        else if (mType == TypeUtil.PASSWORD_TRADE) {
            if (TextUtils.isEmpty(idCard)) {
                idCard = "";
            }
            begin();
            HttpRequest.changePayPassword(this, new ICallback<Meta>() {
                @Override
                public void onSucceed(Meta result) {
                    end();
                    Uihelper.showToast(mActivity, "设置密码成功");
                    SetPasswordActivity.this.finish();
                }

                @Override
                public void onFail(String error) {
                    end();
                    Uihelper.showToast(mActivity, error);
                }
            }, "SXJ1", idCard, telephone, captcha, password_confirm, password_confirm);
        }
        //设置交易密码
        else if (mType == TypeUtil.TRADEPASSWORD_FIRST_SETTING) {
            begin();
            HttpRequest.setPayPassword(mActivity, new ICallback<Meta>() {
                @Override
                public void onSucceed(Meta result) {
                    end();
                    Uihelper.showToast(mActivity, "设置成功");
                    ExtendOperationController.getInstance().doNotificationExtendOperation(ExtendOperationController.OperationKey.SETTRADPASSWORD_SUCCESS, null);
                    SetPasswordActivity.this.finish();
                }

                @Override
                public void onFail(String error) {
                    end();
                    Uihelper.showToast(mActivity, error);
                }
            }, password_confirm, password_confirm);
        }
    }


    @Override
    public void initTitle(WFYTitle mTitle) {
        mTitle.setTitleText("设置登录密码");

    }

    @Override
    protected String getPageName() {
        return "设置登录密码";
    }
}
