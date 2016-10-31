package com.miqian.mq.activity.user;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.miqian.mq.R;
import com.miqian.mq.activity.BaseActivity;
import com.miqian.mq.activity.current.CurrentInvestment;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.utils.FormatUtil;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.views.WFYTitle;

/**
 * 填写身份证页面
 */

public class RealNameActivity extends BaseActivity implements View.OnClickListener {

    private Button btConfirm;
    private EditText editCardId;
    private EditText editName;

    @Override
    public void obtainData() {
    }


    @Override
    public void initView() {
        btConfirm = (Button) findViewById(R.id.bt_confirm);
        btConfirm.setOnClickListener(this);

        editName = (EditText) findViewById(R.id.edit_name);
        editCardId = (EditText) findViewById(R.id.edit_card_id);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_real_name;
    }

    @Override
    public void initTitle(WFYTitle mTitle) {
        mTitle.setTitleText("开通存管");
    }

    @Override
    protected String getPageName() {
        return "开通存管_身份证";
    }

    /**
     * 进入填写身份证号码
     *
     * @param context
     */
    public static void startActivity(Context context) {
        Intent intent = new Intent(context, RealNameActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == HfUpdateActivity.REQUEST_CODE_REGISTER) {
            if (resultCode == CurrentInvestment.SUCCESS) {
                Intent intent = new Intent();
                setResult(CurrentInvestment.SUCCESS, intent);
                finish();
            } else {
                Uihelper.showToast(mActivity, "开户失败");
            }
        }
    }


    @Override
    public void onClick(View v) {
        openHf();
    }

    /**
     * 开通汇付
     */
    private void openHf() {
        String realName = editName.getText().toString();
        String idCard = editCardId.getText().toString();
        if (TextUtils.isEmpty(realName)) {
            Uihelper.showToast(mActivity, "姓名不能为空");
            return;
        }
        if (realName.length() < 2) {
            Uihelper.showToast(mActivity, "姓名输入有误");
            return;
        }
        if (TextUtils.isEmpty(idCard)) {
            Uihelper.showToast(mActivity, "身份证号码不能为空");
            return;
        }

        if (!idCard.matches(FormatUtil.PATTERN_IDCARD)) {
            Uihelper.showToast(mActivity, "身份证号码不正确");
            return;
        }
        HttpRequest.registerHf(this, realName, idCard);
    }
}
