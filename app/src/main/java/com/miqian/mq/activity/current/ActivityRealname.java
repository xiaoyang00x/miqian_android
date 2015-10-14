package com.miqian.mq.activity.current;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.miqian.mq.R;
import com.miqian.mq.activity.BaseActivity;
import com.miqian.mq.encrypt.RSAUtils;
import com.miqian.mq.entity.Meta;
import com.miqian.mq.entity.PayOrder;
import com.miqian.mq.entity.PayOrderResult;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.pay.MobileSecurePayer;
import com.miqian.mq.utils.Constants;
import com.miqian.mq.utils.ExtendOperationController;
import com.miqian.mq.utils.FormatUtil;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.views.WFYTitle;

/**
 * Created by Jackie on 2015/9/18.
 */
public class ActivityRealname extends BaseActivity implements View.OnClickListener {

    private Button btRealname;
    private EditText editName;
    private EditText editCardId;

    @Override
    public void obtainData() {

    }

    @Override
    public void initView() {

      Intent intent= getIntent();
        if (intent!=null){
            if (intent.getBooleanExtra("isRegistered",false)){
                mTitle.setRightText("跳过");
                mTitle.setOnRightClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
            }
        }

        btRealname = (Button) findViewById(R.id.bt_realname);
        btRealname.setOnClickListener(this);
        editName = (EditText) findViewById(R.id.edit_name);
        editCardId = (EditText) findViewById(R.id.edit_card_id);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_realname;
    }

    @Override
    public void initTitle(WFYTitle mTitle) {
        mTitle.setTitleText("实名认证");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_realname:
                setIdCardCheck();
                break;
            default:
                break;
        }
    }

    private void setIdCardCheck() {
        final String name = editName.getText().toString();
        if (TextUtils.isEmpty(name)) {
            Uihelper.showToast(mActivity, "姓名不能为空");
            return;
        }
        String idCard = editCardId.getText().toString();
        if (TextUtils.isEmpty(idCard)) {
            Uihelper.showToast(mActivity, "身份证号不能为空");
            return;
        }

        if (idCard.matches(FormatUtil.PATTERN_IDCARD)) {
            mWaitingDialog.show();
            HttpRequest.setIDCardCheck(mActivity, new ICallback<Meta>() {
                @Override
                public void onSucceed(Meta result) {
                    mWaitingDialog.dismiss();
                    Uihelper.showToast(mActivity, "实名认证成功");
                    ExtendOperationController.getInstance().doNotificationExtendOperation(ExtendOperationController.OperationKey.REAL_NAME, name);
                    ActivityRealname.this.finish();

                }

                @Override
                public void onFail(String error) {
                    Uihelper.showToast(mActivity, error);
                    mWaitingDialog.dismiss();
                }
            }, idCard, name);
        } else {
            Uihelper.showToast(mActivity, "身份证号码不正确");
        }
    }
}
