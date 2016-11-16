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
import com.miqian.mq.utils.Uihelper;
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
    private String telephone;
    private View layoutOldpassword;
    private View dividerOldpassword;
    private boolean isModify;
    private EditText et_oldpassword;

    @Override
    public void onCreate(Bundle arg0) {
        // getIntent
        Intent intent = getIntent();
        captcha = intent.getStringExtra("captcha");
        phone = intent.getStringExtra("phone");
        idCard = intent.getStringExtra("idCard");
        telephone = intent.getStringExtra("telephone");
        isModify = intent.getBooleanExtra("isModify", false);
        super.onCreate(arg0);
    }

    @Override
    public void obtainData() {
        if (!isModify) {
            layoutOldpassword.setVisibility(View.GONE);
            dividerOldpassword.setVisibility(View.GONE);
        }
    }

    @Override
    public void initView() {
        et_password = (EditText) findViewById(R.id.et_password);
        et_password_confirm = (EditText) findViewById(R.id.et_password_confirm);
        et_oldpassword = (EditText) findViewById(R.id.et_old_password);
        layoutOldpassword = findViewById(R.id.layout_oldpassword);
        dividerOldpassword = findViewById(R.id.divider_oldpassword);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_setpassword;
    }

    public void btn_click(View v) {

        String password = et_password.getText().toString();
        String oldpassword = et_oldpassword.getText().toString();
        String password_confirm = et_password_confirm.getText().toString();
        if (isModify) {
            if (TextUtils.isEmpty(oldpassword)) {
                Uihelper.showToast(mActivity, "请填写旧密码");
                return;
            }
        }
        if (!TextUtils.isEmpty(password)) {
            if (password.length() < 6 || password.length() > 20) {
                Uihelper.showToast(this, R.string.tip_password);
            } else {
                if (!TextUtils.isEmpty(password_confirm)) {
                    if (password_confirm.equals(password)) {
                        summit(oldpassword, password);
                    } else {
                        Uihelper.showToast(this, "两次密码不一致，请重新填写");
                    }

                } else {
                    Uihelper.showToast(this, "请再次填写新密码");
                }
            }
        } else {
            Uihelper.showToast(this, "请填写新密码");
        }

    }

    private void summit(String oldpassword, String password) {
        //设置登录密码
        if (!isModify) {
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
            }, phone, password, password, captcha);
        } else {
            begin();
            HttpRequest.changePassword(this, new ICallback<Meta>() {
                @Override
                public void onSucceed(Meta result) {
                    end();
                    Uihelper.showToast(mActivity, "成功修改密码");
                    ExtendOperationController.getInstance().doNotificationExtendOperation(ExtendOperationController.OperationKey.CHANGE_TOKEN, null);
                    SetPasswordActivity.this.finish();
                }

                @Override
                public void onFail(String error) {
                    end();
                    Uihelper.showToast(mActivity, error);
                }
            }, oldpassword, password, password, captcha);
        }
    }


    @Override
    public void initTitle(WFYTitle mTitle) {
        if (isModify) {
            mTitle.setTitleText("修改登录密码");
        } else {
            mTitle.setTitleText("设置登录密码");
        }

    }

    @Override
    protected String getPageName() {
        if (isModify) {
            return "修改登录密码";
        } else {
            return "设置登录密码";
        }

    }
}
