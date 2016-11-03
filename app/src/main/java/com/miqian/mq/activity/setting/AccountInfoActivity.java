package com.miqian.mq.activity.setting;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.activity.BaseActivity;
import com.miqian.mq.activity.PhoneCaptchaActivity;
import com.miqian.mq.encrypt.RSAUtils;
import com.miqian.mq.entity.UserInfo;
import com.miqian.mq.utils.ExtendOperationController;
import com.miqian.mq.views.WFYTitle;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by Administrator on 2016/1/21.
 */
public class AccountInfoActivity extends BaseActivity implements View.OnClickListener, ExtendOperationController.ExtendOperationListener {
    private TextView tvName;
    private UserInfo userInfo;
    private View frame_name;
    private TextView tvTelephone;
    private ExtendOperationController extendOperationController;

    @Override
    public void onCreate(Bundle arg0) {
        Intent intent = getIntent();
        userInfo = (UserInfo) intent.getSerializableExtra("userInfo");
        super.onCreate(arg0);
    }

    @Override
    public void obtainData() {

    }

    @Override
    public void initView() {

        View frameTelephone = findViewById(R.id.frame_telephone);
        frame_name = findViewById(R.id.frame_name);
        tvName = (TextView) findViewById(R.id.tv_name);
        tvTelephone = (TextView) findViewById(R.id.tv_phone);

        frameTelephone.setOnClickListener(this);
        extendOperationController = ExtendOperationController.getInstance();
        extendOperationController.registerExtendOperationListener(this);
        setData();

    }

    @Override
    protected void onDestroy() {
        if (extendOperationController != null) {
            extendOperationController.unRegisterExtendOperationListener(this);
        }
        super.onDestroy();
    }

    private void setData() {

        if (!TextUtils.isEmpty(userInfo.getMobile())) {
            String phone = RSAUtils.decryptByPrivate(userInfo.getMobile());
            tvTelephone.setText(phone.substring(0, 3) + "****" + phone.substring(phone.length() - 4, phone.length()));
        }

        if (!TextUtils.isEmpty(userInfo.getUserName())) {
            frame_name.setVisibility(View.VISIBLE);
            findViewById(R.id.divider_name).setVisibility(View.VISIBLE);
            tvName.setText(RSAUtils.decryptByPrivate(userInfo.getUserName()));
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_account_info;
    }

    @Override
    public void initTitle(WFYTitle mTitle) {

        mTitle.setTitleText("账户信息");
    }

    @Override
    protected String getPageName() {
        return "账户信息";
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.frame_telephone://修改绑定手机

                MobclickAgent.onEvent(mActivity, "1025");
                Intent intent_phone = new Intent(mActivity, PhoneCaptchaActivity.class);
                startActivity(intent_phone);

                break;
            default:
                break;
        }
    }

    @Override
    public void excuteExtendOperation(int operationKey, Object data) {

        switch (operationKey) {
            case ExtendOperationController.OperationKey.MODIFYPHONE:
                String phone = (String) data;
                if (!TextUtils.isEmpty(phone)) {
                    tvTelephone.setText(phone.substring(0, 3) + "****" + phone.substring(phone.length() - 4, phone.length()));
                }
                break;
            default:
                break;
        }

    }
}
