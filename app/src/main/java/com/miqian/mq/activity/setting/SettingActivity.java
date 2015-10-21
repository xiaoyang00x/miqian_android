package com.miqian.mq.activity.setting;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.miqian.mq.R;
import com.miqian.mq.activity.BaseActivity;
import com.miqian.mq.activity.TradePsCaptchaActivity;
import com.miqian.mq.activity.current.ActivityRealname;
import com.miqian.mq.encrypt.RSAUtils;
import com.miqian.mq.entity.BankCard;
import com.miqian.mq.entity.BankCardResult;
import com.miqian.mq.entity.Meta;
import com.miqian.mq.entity.UserInfo;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.utils.ExtendOperationController;
import com.miqian.mq.utils.UserUtil;
import com.miqian.mq.views.WFYTitle;
import com.umeng.fb.FeedbackAgent;

/**
 * Created by Administrator on 2015/9/17.
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener, ExtendOperationController.ExtendOperationListener {

    private UserInfo userInfo;
    private TextView tv_name, tv_card, tv_bindPhone, tv_cardState;
    private BankCard bankCard;
    private ImageView iconBank;

    @Override
    public void obtainData() {
        //获取银行信息
        mWaitingDialog.show();
        HttpRequest.getUserBankCard(mActivity, new ICallback<BankCardResult>() {
            @Override
            public void onSucceed(BankCardResult result) {
                mWaitingDialog.dismiss();
                bankCard = result.getData();
                String bankOpenName = bankCard.getBankOpenName();
                //未绑定银行卡
                if ("0".equals(userInfo.getBindCardStatus())) {
                    tv_card.setText("银行卡未绑定");
                    tv_cardState.setText("");
                } else {

                    String bankNo = RSAUtils.decryptByPrivate(bankCard.getBankNo());
                    if (TextUtils.isEmpty(bankNo)) {
                        return;
                    }

                    if (!TextUtils.isEmpty(bankCard.getBankUrlSmall())) {
                        imageLoader.displayImage(userInfo.getBankUrlSmall(), iconBank, options);
                    }
                    tv_card.setText("尾号" + bankNo.substring(bankNo.length() - 4, bankNo.length()));
                    //已绑定支行
                    if (!TextUtils.isEmpty(bankOpenName)) {
                        tv_cardState.setText("已完善");
                    }
                    //未绑定支行
                    else {
                        tv_cardState.setText("未完善");
                    }
                }
            }

            @Override
            public void onFail(String error) {
                tv_card.setText("银行卡未绑定");
                tv_cardState.setText("");
                mWaitingDialog.dismiss();
            }
        });

    }

    @Override
    public void initView() {

        Intent intent = getIntent();
        userInfo = (UserInfo) intent.getSerializableExtra("userInfo");

        View frame_setting_name = findViewById(R.id.frame_setting_name);
        View frame_setting_bindphone = findViewById(R.id.frame_setting_bindphone);
        View frame_setting_security = findViewById(R.id.frame_setting_security);
        View frame_setting_helpcenter = findViewById(R.id.frame_setting_helpcenter);

        View frame_setting_suggest = findViewById(R.id.frame_setting_suggest);
        View frame_setting_about = findViewById(R.id.frame_setting_about);
        View frame_setting_telephone = findViewById(R.id.frame_setting_telephone);
        View frame_setting_bankcard = findViewById(R.id.frame_setting_bankcard);

        tv_name = (TextView) findViewById(R.id.tv_setting_name);
        tv_card = (TextView) findViewById(R.id.tv_setting_bankcard);
        tv_cardState = (TextView) findViewById(R.id.tv_setting_bankcardstate);
        tv_bindPhone = (TextView) findViewById(R.id.tv_settting_bindphone);
        iconBank = (ImageView) findViewById(R.id.icon_bank);

        frame_setting_name.setOnClickListener(this);
        frame_setting_bindphone.setOnClickListener(this);
        frame_setting_security.setOnClickListener(this);
        frame_setting_helpcenter.setOnClickListener(this);
        frame_setting_suggest.setOnClickListener(this);
        frame_setting_about.setOnClickListener(this);
        frame_setting_telephone.setOnClickListener(this);
        frame_setting_bankcard.setOnClickListener(this);

        if (userInfo == null) {
            return;
        }
        if (!TextUtils.isEmpty(userInfo.getMobilePhone())) {
            String phone = RSAUtils.decryptByPrivate(userInfo.getMobilePhone());
            tv_bindPhone.setText("****" + phone.substring(phone.length() - 4, phone.length()));
        }

        if (!TextUtils.isEmpty(userInfo.getRealNameStatus())) {
            //未认证
            if ("1".equals(userInfo.getRealNameStatus())) {
                if (!TextUtils.isEmpty(userInfo.getRealName())) {
                    tv_name.setText(RSAUtils.decryptByPrivate(userInfo.getRealName()));
                }
                findViewById(R.id.arrow_1).setVisibility(View.INVISIBLE);
            }
        }

        if ("0".equals(userInfo.getBindCardStatus())) {
            frame_setting_bankcard.setVisibility(View.GONE);
            findViewById(R.id.divider_bank).setVisibility(View.GONE);
        }

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    public void initTitle(WFYTitle mTitle) {
        mTitle.setTitleText("设置");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //姓名
            case R.id.frame_setting_name:

                if (!TextUtils.isEmpty(userInfo.getRealNameStatus())) {
                    //未认证
                    if ("0".equals(userInfo.getRealNameStatus())) {
                        Intent intent = new Intent(mActivity, ActivityRealname.class);
                        startActivity(intent);
                    }
                }

                break;
            //绑定手机
            case R.id.frame_setting_bindphone:
                Intent intent_phone = new Intent(mActivity, TradePsCaptchaActivity.class);
                intent_phone.putExtra("isModifyPhone", true);
                startActivity(intent_phone);

                break;
            //安全设置
            case R.id.frame_setting_security:
                startActivity(new Intent(mActivity, SecuritySettingActivity.class));
                break;
            //帮助中心
            case R.id.frame_setting_helpcenter:
                break;
            //银行卡号
            case R.id.frame_setting_bankcard:

                //若是绑定的银行卡支持连连支付，则不跳入绑定银行卡页面，直接到选择支行页面
                String supportStatus = userInfo.getSupportStatus();
                if (TextUtils.isEmpty(supportStatus)) {
                    supportStatus = "0";
                }
                if (supportStatus.equals("0")) {
                    Intent intent_bind = new Intent(mActivity, BindCardActivity.class);
                    Bundle extra = new Bundle();
                    extra.putSerializable("userInfo", userInfo);
                    intent_bind.putExtras(extra);
                    startActivity(intent_bind);
                }   //若是绑定的银行卡支持连连支付，则不跳入绑定银行卡页面，直接到选择支行页面
                else {
                    Intent intent_bind = new Intent(mActivity, SetBankActivity.class);
                    Bundle extra = new Bundle();
                    extra.putSerializable("userInfo", userInfo);
                    if (bankCard != null) {
                        if (!TextUtils.isEmpty(bankCard.getBankNo())) {
                            intent_bind.putExtra("cardNo", RSAUtils.decryptByPrivate(bankCard.getBankNo()));
                        }

                    }
                    intent_bind.putExtras(extra);
                    startActivity(intent_bind);
                }
                break;
            //意见反馈
            case R.id.frame_setting_suggest:
                Intent feedBackActivity = new Intent(this, CustomFeedBackActivity.class);
                startActivity(feedBackActivity);
                break;
            //关于咪钱
            case R.id.frame_setting_about:
                startActivity(new Intent(mActivity, AboutUsActivity.class));
                break;
            //联系客服
            case R.id.frame_setting_telephone:


                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "4000010520")));

                break;
        }

    }


    //退出账号
    public void btn_click(View view) {
        mWaitingDialog.show();
        HttpRequest.loginOut(mActivity, new ICallback<Meta>() {
            @Override
            public void onSucceed(Meta result) {
                mWaitingDialog.dismiss();
                UserUtil.clearUserInfo(mActivity);
                finish();
            }

            @Override
            public void onFail(String error) {
                mWaitingDialog.dismiss();
                UserUtil.clearUserInfo(mActivity);
                finish();
            }
        });
    }

    @Override
    public void excuteExtendOperation(int operationKey, Object data) {

        switch (operationKey) {
            case ExtendOperationController.OperationKey.MODIFYPHONE:
                String phone = (String) data;
                if (!TextUtils.isEmpty(phone)) {
                    tv_bindPhone.setText("****" + phone.substring(phone.length() - 4, phone.length()));
                }
                break;
            case ExtendOperationController.OperationKey.REAL_NAME:
                String name = (String) data;
                if (!TextUtils.isEmpty(name)) {
                    userInfo.setRealNameStatus("1");
                    tv_name.setText(name);
                }
                break;
            default:
                break;
        }

    }

    @Override
    protected String getPageName() {
        return "设置";
    }
}