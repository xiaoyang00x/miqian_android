package com.miqian.mq.activity.setting;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.activity.BaseActivity;
import com.miqian.mq.activity.current.ActivityRealname;
import com.miqian.mq.encrypt.RSAUtils;
import com.miqian.mq.entity.BankCard;
import com.miqian.mq.entity.BankCardResult;
import com.miqian.mq.entity.Meta;
import com.miqian.mq.entity.UserInfo;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.utils.UserUtil;
import com.miqian.mq.views.WFYTitle;

/**
 * Created by Administrator on 2015/9/17.
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener {

    private UserInfo userInfo;
    private TextView tv_name, tv_card, tv_bindPhone, tv_cardState;
    private BankCard bankCard;

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
                    tv_card.setText("银行卡号" + " ***** " + bankNo.substring(bankNo.length() - 3, bankNo.length()));
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

        frame_setting_name.setOnClickListener(this);
        frame_setting_bindphone.setOnClickListener(this);
        frame_setting_security.setOnClickListener(this);
        frame_setting_helpcenter.setOnClickListener(this);
        frame_setting_suggest.setOnClickListener(this);
        frame_setting_about.setOnClickListener(this);
        frame_setting_telephone.setOnClickListener(this);
        frame_setting_bankcard.setOnClickListener(this);

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
                Intent intent = new Intent(mActivity, ActivityRealname.class);
                startActivity(intent);
                break;
            //绑定手机
            case R.id.frame_setting_bindphone:

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
                if (userInfo.getSupportStatus().equals("0")) {
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
                        extra.putSerializable("bankCard", bankCard);
                    }
                    intent_bind.putExtras(extra);
                    startActivity(intent_bind);
                }


                break;
            //意见反馈
            case R.id.frame_setting_suggest:
                break;
            //关于咪钱
            case R.id.frame_setting_about:
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
}
