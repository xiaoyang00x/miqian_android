package com.miqian.mq.activity.setting;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.miqian.mq.R;
import com.miqian.mq.activity.BaseActivity;
import com.miqian.mq.activity.GestureLockSetActivity;
import com.miqian.mq.activity.SendCaptchaActivity;
import com.miqian.mq.activity.TradePsCaptchaActivity;
import com.miqian.mq.activity.WebActivity;
import com.miqian.mq.encrypt.RSAUtils;
import com.miqian.mq.entity.Meta;
import com.miqian.mq.entity.UpdateInfo;
import com.miqian.mq.entity.UpdateResult;
import com.miqian.mq.entity.UserInfo;
import com.miqian.mq.fragment.FragmentUser;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.net.Urls;
import com.miqian.mq.utils.ExtendOperationController;
import com.miqian.mq.utils.MobileDeviceUtil;
import com.miqian.mq.utils.MobileOS;
import com.miqian.mq.utils.Pref;
import com.miqian.mq.utils.TypeUtil;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.utils.UserUtil;
import com.miqian.mq.views.DialogUpdate;
import com.miqian.mq.views.WFYTitle;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;

import cn.udesk.UdeskConst;
import cn.udesk.UdeskSDKManager;

import static com.miqian.mq.R.id.iv_switch;

/**
 * Created by Administrator on 2015/9/17.
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener, ExtendOperationController.ExtendOperationListener {

    private UserInfo userInfo;
    private ImageView ivPushState;
    private View frame_login;
    private Button btn_loginout;
    private boolean isPush;
    private ExtendOperationController extendOperationController;
    private TextView tvPhone;
    private TextView tvName;
    private View frameName;
    private ImageView ivSwitch;

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

        tvPhone = (TextView) findViewById(R.id.tv_phone);
        tvName = (TextView) findViewById(R.id.tv_name);


        btn_loginout = (Button) findViewById(R.id.btn_loginout);
        View frame_setting_helpcenter = findViewById(R.id.frame_setting_helpcenter);
        View frame_setting_telephone = findViewById(R.id.frame_telephone);
        View frameuserPhone = findViewById(R.id.frame_userhone);
        View frameDigitalcard = findViewById(R.id.frame_digitalcard);
        frameName = findViewById(R.id.frame_name);

        View frame_setting_suggest = findViewById(R.id.frame_setting_suggest);
        View frame_setting_about = findViewById(R.id.frame_setting_about);
        frame_login = findViewById(R.id.layout_login);
        View frameUpdate = findViewById(R.id.frame_update);
        TextView textVersion = (TextView) findViewById(R.id.text_version);
        textVersion.setText("V" + MobileOS.getAppVersionName(mActivity));
        //通知开关
        ivPushState = (ImageView) findViewById(R.id.iv_push_state);

        frameUpdate.setOnClickListener(this);
        frame_setting_helpcenter.setOnClickListener(this);
        frame_setting_suggest.setOnClickListener(this);
        frame_setting_about.setOnClickListener(this);
        frame_setting_telephone.setOnClickListener(this);
        frameuserPhone.setOnClickListener(this);
        frameDigitalcard.setOnClickListener(this);
        ivPushState.setOnClickListener(this);

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
        }

        if (!TextUtils.isEmpty(userInfo.getMobile())) {
            String phone = RSAUtils.decryptByPrivate(userInfo.getMobile());
            tvPhone.setText(phone.substring(0, 3) + "****" + phone.substring(phone.length() - 4, phone.length()));
        }

        if (!TextUtils.isEmpty(userInfo.getRealNameStatus())) {
            //已认证
            if ("1".equals(userInfo.getRealNameStatus())) {
                if (!TextUtils.isEmpty(userInfo.getUserName())) {
                    frameName.setVisibility(View.VISIBLE);
                    findViewById(R.id.divider_name).setVisibility(View.VISIBLE);
                    tvName.setText(RSAUtils.decryptByPrivate(userInfo.getUserName()));
                }
            }
        }

        /*
        修改登录，交易密码，手势密码
         */
        ivSwitch = (ImageView) findViewById(iv_switch);
        findViewById(R.id.password_login).setOnClickListener(this);
        findViewById(R.id.password_transaction).setOnClickListener(this);
        ivSwitch.setOnClickListener(this);
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
            //电子账户
            case R.id.frame_digitalcard:
                break;
            case R.id.password_login://修改登录密码
                MobclickAgent.onEvent(mActivity, "1027");
                SendCaptchaActivity.enterActivity(mActivity, TypeUtil.SENDCAPTCHA_FORGETPSW, true);

                break;
            case R.id.password_transaction://修改交易密码
                if ("0".equals(userInfo.getPayPwdStatus())) {//未设置
                    Intent intent = new Intent(mActivity, SetPasswordActivity.class);
                    intent.putExtra("type", TypeUtil.TRADEPASSWORD_FIRST_SETTING);
                    startActivity(intent);
                } else {
                    MobclickAgent.onEvent(mActivity, "1028");
                    Intent intent = new Intent(mActivity, TradePsCaptchaActivity.class);
                    intent.putExtra("realNameStatus", userInfo.getRealNameStatus());
                    startActivity(intent);
                }
                break;
            case R.id.iv_switch://手势密码
                if (isGestureLockOpen()) {
                    setGestureLockState(false);
                    changeGestureSwitchState();
                } else {
                    GestureLockSetActivity.startActivity(getBaseContext(), null);
                }
                break;
            //帮助中心
            case R.id.frame_setting_helpcenter:
                MobclickAgent.onEvent(mActivity, "1030");
                WebActivity.startActivity(mActivity, Urls.web_help);
                break;
            //意见反馈
            case R.id.frame_setting_suggest:
                MobclickAgent.onEvent(mActivity, "1029");
                setUdeskUserInfo();
                UdeskSDKManager.getInstance().showRobotOrConversation(SettingActivity.this);

                break;
            //客服电话
            case R.id.frame_telephone:
                if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    MobclickAgent.onEvent(mContext, "1004_5");
                    // TODO: Consider calling
                    Toast.makeText(mContext, "您尚未开启通话权限，请开启后再尝试。", Toast.LENGTH_SHORT).show();
                    return;
                }
                MobclickAgent.onEvent(mContext, "1004_6");
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:4006656191"));
                startActivity(intent);
                break;
            //版本更新
            case R.id.frame_update:
                MobclickAgent.onEvent(mActivity, "1032");
                checkVersion();
                break;
            //了解咪钱
            case R.id.frame_setting_about:
                MobclickAgent.onEvent(mActivity, "1031");
                startActivity(new Intent(mActivity, AboutUsActivity.class));
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

    // 手势密码是否开启
    private boolean isGestureLockOpen() {
        return Pref.getBoolean(Pref.GESTURESTATE, getBaseContext(), false);
    }

    // 设置 手势密码 开关状态
    private void setGestureLockState(boolean isOpen) {
        Pref.saveBoolean(Pref.GESTURESTATE, isOpen, getBaseContext());
    }

    private void changeGestureSwitchState() {
        ivSwitch.setImageResource(isGestureLockOpen() ?
                R.drawable.gesture_swith_open : R.drawable.gesture_switch_close);
    }

    //  版本更新:1:建议更新 2:强制
    private void checkVersion() {
        HttpRequest.forceUpdate(this, new ICallback<UpdateResult>() {
            @Override
            public void onSucceed(UpdateResult result) {
                UpdateInfo updateInfo = result.getData();
                if ("2".equals(updateInfo.getUpgradeSign())) {
                    DialogUpdate dialogUpdate = new DialogUpdate(mActivity, updateInfo) {
                        @Override
                        public void updateClick(String url) {
//                        // 跳转到外部浏览器下载
                            startActivity(Uihelper.getDownIntent(mActivity, url));
                        }
                    };
                    dialogUpdate.setCancelable(false);
                    dialogUpdate.setCanceledOnTouchOutside(false);
                    dialogUpdate.show();
                } else if ("1".equals(updateInfo.getUpgradeSign())) {
                    DialogUpdate dialogUpdate = new DialogUpdate(mActivity, updateInfo) {
                        @Override
                        public void updateClick(String url) {
//                        // 跳转到外部浏览器下载
                            startActivity(Uihelper.getDownIntent(mActivity, url));
                        }

                    };
                    dialogUpdate.show();
                }
            }

            @Override
            public void onFail(String error) {
            }
        });
    }

    //退出账号
    public void btn_click(View view) {
        MobclickAgent.onEvent(mActivity, "1034");
        begin();
        HttpRequest.loginOut(mActivity, new ICallback<Meta>() {
            @Override
            public void onSucceed(Meta result) {
                end();
                FragmentUser.refresh = true;
                UserUtil.clearUserInfo(mActivity);
                finish();
            }

            @Override
            public void onFail(String error) {
                end();
                UserUtil.clearUserInfo(mActivity);
                finish();
            }
        });
        extendOperationController.doNotificationExtendOperation(ExtendOperationController.OperationKey.EXIT_SUCCESS, null);
    }

    @Override
    protected String getPageName() {
        return "设置";
    }

    @Override
    public void excuteExtendOperation(int operationKey, Object data) {

        switch (operationKey) {
            case ExtendOperationController.OperationKey.SETTRADPASSWORD_SUCCESS:
                userInfo.setPayPwdStatus("1");
                break;
            default:
                break;
        }

    }

    /**
     * 设置 udesk 所需用户信息
     */
    private void setUdeskUserInfo() {

        Map<String, String> info = new HashMap<String, String>();
        String userId = "";
        if (UserUtil.hasLogin(mApplicationContext)) {
            userId = UserUtil.getUserId(mApplicationContext);
        } else {
            userId = MobileDeviceUtil.getInstance(mApplicationContext).getMobileImei();
        }
        if (TextUtils.isEmpty(userId)) {
            userId = MobileDeviceUtil.getInstance(mApplicationContext).getUUID();
        }

        info.put(UdeskConst.UdeskUserInfo.USER_SDK_TOKEN, userId);
        //以下注释的字段都是可选的字段， 有邮箱建议填写
        info.put(UdeskConst.UdeskUserInfo.NICK_NAME, Pref.getString(Pref.REAL_NAME, mApplicationContext, ""));
        info.put(UdeskConst.UdeskUserInfo.CELLPHONE, Pref.getString(Pref.TELEPHONE, mApplicationContext, MobileDeviceUtil.getInstance(mApplicationContext).getPhoneNum()));

        UdeskSDKManager.getInstance().setUserInfo(this, userId, info);

    }

}
