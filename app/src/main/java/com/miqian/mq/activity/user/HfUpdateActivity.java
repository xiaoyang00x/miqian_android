package com.miqian.mq.activity.user;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.activity.BaseActivity;
import com.miqian.mq.activity.WebActivity;
import com.miqian.mq.activity.current.CurrentInvestment;
import com.miqian.mq.encrypt.RSAUtils;
import com.miqian.mq.entity.LoginResult;
import com.miqian.mq.entity.UserInfo;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.net.Urls;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.views.WFYTitle;

/**
 * 汇付存管界面升级界面
 */

public class HfUpdateActivity extends BaseActivity implements View.OnClickListener {

    private Button btUpdate;
    private ImageView imageRegister;
    private ImageView imageBind;
    private ImageView imageAuto;
    private TextView registerTitle;
    private TextView bindTitle;
    private TextView autoTitle;
    private TextView registerContent;
    private TextView bindContent;
    private TextView autoContent;

    private UserInfo userInfo;

    private static int state = 0;

    public static final int REQUEST_CODE_ROLLIN = 0;
    public static final int REQUEST_CODE_ROLLOUT = 1;
    public static final int REQUEST_CODE_REGISTER = 2;
    public static final int REQUEST_CODE_ACTIVATE = 3;
    public static final int REQUEST_CODE_AUTO = 4;

    @Override
    public void obtainData() {
        getData();
    }

    private void getData() {
        begin();
        HttpRequest.getHfUserInfo(mActivity, new ICallback<LoginResult>() {
            @Override
            public void onSucceed(LoginResult result) {
                end();
                userInfo = result.getData();
                refreshView();
            }

            @Override
            public void onFail(String error) {
                end();
                btUpdate.setEnabled(false);
                Uihelper.showToast(mActivity, error);
            }
        });
    }

    private void refreshView() {
        btUpdate.setEnabled(true);
        initState();
        setView();
    }


    private void setView() {
        if (state >= 2) {
            imageRegister.setImageResource(R.drawable.hf_update_register);
            registerTitle.setTextColor(ContextCompat.getColor(mContext, R.color.mq_y4_v2));
            registerContent.setTextColor(ContextCompat.getColor(mContext, R.color.mq_y4_v2));
        }

        if (state >= 3) {
            imageBind.setImageResource(R.drawable.hf_update_bind);
            bindTitle.setTextColor(ContextCompat.getColor(mContext, R.color.mq_y4_v2));
            bindContent.setTextColor(ContextCompat.getColor(mContext, R.color.mq_y4_v2));
        }

        if (state >= 4) {
            imageAuto.setImageResource(R.drawable.hf_update_auto);
            autoTitle.setTextColor(ContextCompat.getColor(mContext, R.color.mq_y4_v2));
            autoContent.setTextColor(ContextCompat.getColor(mContext, R.color.mq_y4_v2));
        }

        if (state < 2) {
            btUpdate.setText("开始升级");
        } else if (state < 4) {
            btUpdate.setText("下一步");
        } else {
            btUpdate.setText("账户升级完成");
        }

    }

    private void initState() {
        if (userInfo == null) {
            return;
        }
        if (!userInfo.isHfAccountStatus()) {
            state = 0;
        } else if (!userInfo.isStatus()) {
            state = 1;
        } else if (!userInfo.isBindCardStatus()) {
            state = 2;
        } else if (!userInfo.isHfAutoTenderPlanStatus()) {
            state = 3;
        } else {
            state = 4;
        }
    }


    @Override
    public void initView() {
        btUpdate = (Button) findViewById(R.id.bt_update);
        btUpdate.setOnClickListener(this);

        imageRegister = (ImageView) findViewById(R.id.image_register);
        imageBind = (ImageView) findViewById(R.id.image_bind);
        imageAuto = (ImageView) findViewById(R.id.image_auto);
        registerTitle = (TextView) findViewById(R.id.text_register_title);
        bindTitle = (TextView) findViewById(R.id.text_bind_title);
        autoTitle = (TextView) findViewById(R.id.text_auto_title);
        registerContent = (TextView) findViewById(R.id.text_register_content);
        bindContent = (TextView) findViewById(R.id.text_bind_content);
        autoContent = (TextView) findViewById(R.id.text_auto_content);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_hf_update;
    }

    @Override
    public void initTitle(WFYTitle mTitle) {
        mTitle.setTitleText("账户升级");
        mTitle.setIvLeftVisiable(View.GONE);
        mTitle.setRightImage(R.drawable.hf_update_question);
        mTitle.setOnRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebActivity.startActivity(mActivity, Urls.web_hf_help);
            }
        });
    }

    @Override
    protected String getPageName() {
        return "汇付账户升级";
    }

    /**
     * 进入汇付账户升级页面
     *
     * @param context
     */
    public static void startActivity(Context context) {
        Intent intent = new Intent(context, HfUpdateActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == REQUEST_CODE_REGISTER) {
        if (resultCode == CurrentInvestment.SUCCESS) {
            getData();
        } else {
        }
//        }
    }


    @Override
    public void onClick(View v) {
        if (state == 0) {
            RealNameActivity.startActivity(mActivity);
        } else if (state == 1) {
            HttpRequest.activateHf(mActivity, RSAUtils.decryptByPrivate(userInfo.getHfCustId()));
        } else if (state == 2) {
            HttpRequest.rollinHf(mActivity, RSAUtils.decryptByPrivate(userInfo.getHfCustId()), "1");
        } else if (state == 3) {
            HttpRequest.autoHf(mActivity);
        } else if (state == 4) {
            finish();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
