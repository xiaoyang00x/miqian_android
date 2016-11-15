package com.miqian.mq.activity.setting;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.activity.BaseActivity;
import com.miqian.mq.activity.WebActivity;
import com.miqian.mq.entity.Meta;
import com.miqian.mq.entity.UpdateInfo;
import com.miqian.mq.entity.UpdateResult;
import com.miqian.mq.entity.UserInfo;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.net.Urls;
import com.miqian.mq.utils.MobileDeviceUtil;
import com.miqian.mq.utils.MobileOS;
import com.miqian.mq.utils.Pref;
import com.miqian.mq.utils.UserUtil;
import com.miqian.mq.views.DialogUpdate;
import com.miqian.mq.views.WFYTitle;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;

import cn.udesk.UdeskConst;
import cn.udesk.UdeskSDKManager;

/**
 * Created by Administrator on 2015/9/17.
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener {

    private UserInfo userInfo;
    private ImageView ivPushState;
    private View frame_login;
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

        btn_loginout = (Button) findViewById(R.id.btn_loginout);
        View frame_setting_security = findViewById(R.id.frame_setting_security);
        View frame_setting_helpcenter = findViewById(R.id.frame_setting_helpcenter);
        View frame_setting_accountinfo = findViewById(R.id.frame_setting_accountinfo);

        View frame_setting_suggest = findViewById(R.id.frame_setting_suggest);
        View frame_setting_about = findViewById(R.id.frame_setting_about);
        frame_login = findViewById(R.id.layout_login);
        View frameUpdate = findViewById(R.id.frame_update);
        TextView textVersion = (TextView) findViewById(R.id.text_version);
        textVersion.setText("V" + MobileOS.getAppVersionName(mActivity));
        //通知开关
        ivPushState = (ImageView) findViewById(R.id.iv_push_state);

        frameUpdate.setOnClickListener(this);
        frame_setting_accountinfo.setOnClickListener(this);
        frame_setting_security.setOnClickListener(this);
        frame_setting_helpcenter.setOnClickListener(this);
        frame_setting_suggest.setOnClickListener(this);
        frame_setting_about.setOnClickListener(this);
        ivPushState.setOnClickListener(this);
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
            //账户信息
            case R.id.frame_setting_accountinfo:
                if (userInfo == null) {
                    return;
                }
                Intent intent_bind = new Intent(mActivity, AccountInfoActivity.class);
                Bundle extra = new Bundle();
                extra.putSerializable("userInfo", userInfo);
                intent_bind.putExtras(extra);
                startActivity(intent_bind);
                break;
            //安全设置
            case R.id.frame_setting_security:
                MobclickAgent.onEvent(mActivity, "1026");
                if (userInfo != null) {
                    Intent intent = new Intent(mActivity, SecuritySettingActivity.class);
                    startActivity(intent);
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
//                Intent feedBackActivity = new Intent(this, CustomFeedBackActivity.class);
//                startActivity(feedBackActivity);

                setUdeskUserInfo();
                UdeskSDKManager.getInstance().showRobotOrConversation(SettingActivity.this);

                break;
            //版本更新
            case R.id.frame_update:
                MobclickAgent.onEvent(mActivity, "1032");
                checkVersion();
//                UmengUpdateAgent.setUpdateAutoPopup(false);
//                UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
//                    @Override
//                    public void onUpdateReturned(int updateStatus, UpdateResponse updateInfo) {
//                        switch (updateStatus) {
//                            case UpdateStatus.Yes: // has update
//                                UmengUpdateAgent.showUpdateDialog(mContext, updateInfo);
//                                break;
//                            case UpdateStatus.No: // has no update
//                                Toast.makeText(mContext, "当前已是最新版本", Toast.LENGTH_SHORT).show();
//                                break;
//                            case UpdateStatus.NoneWifi: // none wifi
//                                UmengUpdateAgent.showUpdateDialog(mContext, updateInfo);
//                                break;
//                            case UpdateStatus.Timeout: // time out
//                                Toast.makeText(mContext, "请求超时", Toast.LENGTH_SHORT).show();
//                                break;
//                        }
//                    }
//                });
//                UmengUpdateAgent.forceUpdate(mActivity);
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
                            Uri uri = Uri.parse(url);
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            startActivity(intent);
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
                            Uri uri = Uri.parse(url);
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            startActivity(intent);
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
                UserUtil.clearUserInfo(getApplicationContext());
                finish();
            }

            @Override
            public void onFail(String error) {
                end();
                UserUtil.clearUserInfo(getApplicationContext());
                finish();
            }
        });
    }

    @Override
    protected String getPageName() {
        return "设置";
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
        if(TextUtils.isEmpty(userId)) {
            userId = MobileDeviceUtil.getInstance(mApplicationContext).getUUID();
        }
        info.put(UdeskConst.UdeskUserInfo.USER_SDK_TOKEN, userId);
        //以下注释的字段都是可选的字段， 有邮箱建议填写
        info.put(UdeskConst.UdeskUserInfo.NICK_NAME, Pref.getString(UserUtil.getPrefKey(mApplicationContext, Pref.REAL_NAME), mApplicationContext, ""));
        info.put(UdeskConst.UdeskUserInfo.CELLPHONE, Pref.getString(Pref.TELEPHONE, mApplicationContext, MobileDeviceUtil.getInstance(mApplicationContext).getPhoneNum()));

        UdeskSDKManager.getInstance().setUserInfo(this, userId, info);

    }

}
