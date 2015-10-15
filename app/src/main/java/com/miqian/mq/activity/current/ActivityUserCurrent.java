package com.miqian.mq.activity.current;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.activity.BaseActivity;
import com.miqian.mq.activity.current.ActivityRedeem;
import com.miqian.mq.activity.user.ProjectMatchActivity;
import com.miqian.mq.entity.UserCurrent;
import com.miqian.mq.entity.UserCurrentResult;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.views.WFYTitle;

/**
 * Created by Jackie on 2015/9/30.
 */
public class ActivityUserCurrent extends BaseActivity implements View.OnClickListener {

    private TextView textEarning;
    private TextView textCaptial;
    private TextView textTotalEarning;
    private RelativeLayout frameCurrentRecord;
    private RelativeLayout frameProjectMatch;
    private Button btRedeem;//赎回
    private Button btSubscribe;//认购

    private UserCurrent userCurrent;

    @Override
    public void obtainData() {
        mWaitingDialog.show();
        HttpRequest.getUserCurrent(mActivity, new ICallback<UserCurrentResult>() {
            @Override
            public void onSucceed(UserCurrentResult result) {
                mWaitingDialog.dismiss();
                userCurrent = result.getData();
                refreshView();
            }

            @Override
            public void onFail(String error) {
                mWaitingDialog.dismiss();
                refreshView();
                Uihelper.showToast(mActivity, error);
            }
        });
    }

    private void refreshView() {
        if (userCurrent != null) {
            textEarning.setText(userCurrent.getCurYesterDayAmt());
            textCaptial.setText(userCurrent.getCurAsset());
            textTotalEarning.setText(userCurrent.getCurAmt());
            double  money=Float.parseFloat(userCurrent.getCurAsset());
            if (money<=0){
                btRedeem.setEnabled(false);
                btRedeem.setTextColor(getResources().getColor(R.color.mq_b5));
            }


        }else {
            btRedeem.setEnabled(false);
            btRedeem.setTextColor(getResources().getColor(R.color.mq_b5));
        }

    }

    @Override
    public void initView() {
        textEarning = (TextView) findViewById(R.id.earning);
        textCaptial = (TextView) findViewById(R.id.captial);
        textTotalEarning = (TextView) findViewById(R.id.total_earning);

        frameCurrentRecord = (RelativeLayout) findViewById(R.id.frame_current_record);
        frameProjectMatch = (RelativeLayout) findViewById(R.id.frame_project_match);
        btRedeem = (Button) findViewById(R.id.bt_redeem);
        btSubscribe = (Button) findViewById(R.id.bt_subscribe);

        frameCurrentRecord.setOnClickListener(this);
        frameProjectMatch.setOnClickListener(this);
        btRedeem.setOnClickListener(this);
        btSubscribe.setOnClickListener(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.user_current;
    }

    @Override
    public void initTitle(WFYTitle mTitle) {
        mTitle.setTitleText("我的活期");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.frame_current_record:
                startActivity(new Intent(mActivity,ActivityCurrentRecord.class));
                break;
            case R.id.frame_project_match:
                Intent intent = new Intent(mActivity, ProjectMatchActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_redeem:
                intent = new Intent(mActivity, ActivityRedeem.class);
                //本金
                intent.putExtra("capital",userCurrent.getCurAsset());
                intent.putExtra("totalEaring",userCurrent.getCurAmt());
                startActivity(intent);
                break;
            case R.id.bt_subscribe:
                break;
        }
    }
}
