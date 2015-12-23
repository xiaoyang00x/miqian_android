package com.miqian.mq.activity.setting;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.miqian.mq.R;
import com.miqian.mq.activity.BaseActivity;
import com.miqian.mq.activity.TradePsCaptchaActivity;
import com.miqian.mq.activity.WebActivity;
import com.miqian.mq.encrypt.RSAUtils;
import com.miqian.mq.entity.BankCard;
import com.miqian.mq.entity.BankCardResult;
import com.miqian.mq.entity.Meta;
import com.miqian.mq.entity.UserInfo;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.net.Urls;
import com.miqian.mq.utils.ExtendOperationController;
import com.miqian.mq.utils.MobileOS;
import com.miqian.mq.utils.Pref;
import com.miqian.mq.utils.UserUtil;
import com.miqian.mq.views.CustomDialog;
import com.miqian.mq.views.WFYTitle;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

/**
 * Created by Administrator on 2015/9/17.
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener, ExtendOperationController.ExtendOperationListener {

    private UserInfo userInfo;
    private TextView tv_name, tv_card, tv_bindPhone, tv_cardState;
    private ImageView iconBank;
    private CustomDialog dialogTips;
    private ImageView ivPushState;
    private View frame_setting_name;
    private View frame_setting_bankcard;
    private View frame_login;
    private View frame_loginout;
    private Button btn_loginout;
    private boolean isPush;

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

        frame_setting_name = findViewById(R.id.frame_setting_name);
        btn_loginout = (Button) findViewById(R.id.btn_loginout);
        View frame_setting_bindphone = findViewById(R.id.frame_setting_bindphone);
        View frame_setting_security = findViewById(R.id.frame_setting_security);
        View frame_setting_helpcenter = findViewById(R.id.frame_setting_helpcenter);

        View frame_setting_suggest = findViewById(R.id.frame_setting_suggest);
        View frame_setting_about = findViewById(R.id.frame_setting_about);
        View frame_setting_telephone = findViewById(R.id.frame_setting_telephone);
        frame_login = findViewById(R.id.layout_login);
        frame_setting_bankcard = findViewById(R.id.frame_setting_bankcard);
        View frameUpdate = findViewById(R.id.frame_update);
        TextView textVersion = (TextView) findViewById(R.id.text_version);
        textVersion.setText("V" + MobileOS.getAppVersionName(mActivity));

        tv_name = (TextView) findViewById(R.id.tv_setting_name);
        tv_card = (TextView) findViewById(R.id.tv_setting_bankcard);
        tv_cardState = (TextView) findViewById(R.id.tv_setting_bankcardstate);
        tv_bindPhone = (TextView) findViewById(R.id.tv_settting_bindphone);
        iconBank = (ImageView) findViewById(R.id.icon_bank);

        //通知开关
        ivPushState = (ImageView) findViewById(R.id.iv_push_state);

        frameUpdate.setOnClickListener(this);
        frame_setting_name.setOnClickListener(this);
        frame_setting_bindphone.setOnClickListener(this);
        frame_setting_security.setOnClickListener(this);
        frame_setting_helpcenter.setOnClickListener(this);
        frame_setting_suggest.setOnClickListener(this);
        frame_setting_about.setOnClickListener(this);
        frame_setting_telephone.setOnClickListener(this);
        frame_setting_bankcard.setOnClickListener(this);
        ivPushState.setOnClickListener(this);
        ExtendOperationController.getInstance().registerExtendOperationListener(this);

        setData();
    }

    private void setData() {
        //推送开关
        isPush = Pref.getBoolean(Pref.PUSH_STATE, mActivity, true);
        if (isPush) {
            ivPushState.setImageResource(R.drawable.gesture_swith_open);
        } else {
            ivPushState.setImageResource(R.drawable.gesture_switch_close);
        }

        if (UserUtil.hasLogin(mActivity)) {
            btn_loginout.setVisibility(View.VISIBLE);
            if (userInfo == null) {
                return;
            }
            frame_login.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(userInfo.getMobilePhone())) {
                String phone = RSAUtils.decryptByPrivate(userInfo.getMobilePhone());
                tv_bindPhone.setText("****" + phone.substring(phone.length() - 4, phone.length()));
            }

            if (!TextUtils.isEmpty(userInfo.getRealNameStatus())) {
                //已认证
                if ("1".equals(userInfo.getRealNameStatus())) {
                    if (!TextUtils.isEmpty(userInfo.getRealName())) {
                        tv_name.setText(RSAUtils.decryptByPrivate(userInfo.getRealName()));
                    }

                } else {
                    frame_setting_name.setVisibility(View.GONE);
                    findViewById(R.id.divider_name).setVisibility(View.GONE);
                }
            }

            if ("1".equals(userInfo.getBindCardStatus())) {
                frame_setting_bankcard.setVisibility(View.VISIBLE);
                findViewById(R.id.divider_bank).setVisibility(View.VISIBLE);

                String bankNo = RSAUtils.decryptByPrivate(userInfo.getBankNo());
                if (TextUtils.isEmpty(bankNo)) {
                    return;
                }
                if (!TextUtils.isEmpty(userInfo.getBankUrlSmall())) {
                    imageLoader.displayImage(userInfo.getBankUrlSmall(), iconBank, options);
                }
                tv_card.setText("尾号" + bankNo.substring(bankNo.length() - 4, bankNo.length()));
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ExtendOperationController.getInstance().unRegisterExtendOperationListener(this);
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
            //绑定手机
            case R.id.frame_setting_bindphone:
                MobclickAgent.onEvent(mActivity, "1025");
                initTipDialog();
                break;
            //安全设置
            case R.id.frame_setting_security:
                MobclickAgent.onEvent(mActivity, "1026");
                if (userInfo != null && !TextUtils.isEmpty(userInfo.getPayPwdStatus())) {
                    Intent intent = new Intent(mActivity, SecuritySettingActivity.class);
                    intent.putExtra("payPwdStatus", userInfo.getPayPwdStatus());
                    startActivity(intent);
                }

                break;
            //帮助中心
            case R.id.frame_setting_helpcenter:
                MobclickAgent.onEvent(mActivity, "1030");
                WebActivity.startActivity(mActivity, Urls.web_help);
                break;
            //银行卡号
            case R.id.frame_setting_bankcard:
                if (userInfo == null) {
                    return;
                }
                Intent intent_bind = new Intent(mActivity, SetBankActivity.class);
                Bundle extra = new Bundle();
                extra.putSerializable("userInfo", userInfo);
                intent_bind.putExtras(extra);
                startActivity(intent_bind);
                break;
            //意见反馈
            case R.id.frame_setting_suggest:
                MobclickAgent.onEvent(mActivity, "1029");
                Intent feedBackActivity = new Intent(this, CustomFeedBackActivity.class);
                startActivity(feedBackActivity);
                break;
            //版本更新
            case R.id.frame_update:
                MobclickAgent.onEvent(mActivity, "1032");
                UmengUpdateAgent.setUpdateAutoPopup(false);
                UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
                    @Override
                    public void onUpdateReturned(int updateStatus, UpdateResponse updateInfo) {
                        switch (updateStatus) {
                            case UpdateStatus.Yes: // has update
                                UmengUpdateAgent.showUpdateDialog(mContext, updateInfo);
                                break;
                            case UpdateStatus.No: // has no update
                                Toast.makeText(mContext, "当前已是最新版本", Toast.LENGTH_SHORT).show();
                                break;
                            case UpdateStatus.NoneWifi: // none wifi
                                UmengUpdateAgent.showUpdateDialog(mContext, updateInfo);
                                break;
                            case UpdateStatus.Timeout: // time out
                                Toast.makeText(mContext, "请求超时", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });
                UmengUpdateAgent.forceUpdate(mActivity);
                break;
            //了解咪钱
            case R.id.frame_setting_about:
                MobclickAgent.onEvent(mActivity, "1031");
                startActivity(new Intent(mActivity, AboutUsActivity.class));
                break;
            //联系客服
            case R.id.frame_setting_telephone:
                MobclickAgent.onEvent(mActivity, "1033");
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "4006656191")));
                break;
            //推送开关
            case R.id.iv_push_state:

                if (isPush) {
                    isPush = false;
                    Pref.saveBoolean(Pref.PUSH_STATE, false, mActivity);
                    ivPushState.setImageResource(R.drawable.gesture_switch_close);
                } else {
                    isPush = true;
                    Pref.saveBoolean(Pref.PUSH_STATE, true, mActivity);
                    ivPushState.setImageResource(R.drawable.gesture_swith_open);
                }


                break;
        }
    }

    private void initTipDialog() {

        if (dialogTips == null) {
            dialogTips = new CustomDialog(this, CustomDialog.CODE_TIPS) {
                @Override
                public void positionBtnClick() {
                    Intent intent_phone = new Intent(mActivity, TradePsCaptchaActivity.class);
                    intent_phone.putExtra("isModifyPhone", true);
                    startActivity(intent_phone);
                    dismiss();
                }

                @Override
                public void negativeBtnClick() {

                }
            };
            dialogTips.setNegative(View.VISIBLE);
            dialogTips.setRemarks("     是否修改手机号码？");
            dialogTips.setNegative("取消");
        }
        dialogTips.show();
    }

    //退出账号
    public void btn_click(View view) {
        MobclickAgent.onEvent(mActivity, "1034");
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
