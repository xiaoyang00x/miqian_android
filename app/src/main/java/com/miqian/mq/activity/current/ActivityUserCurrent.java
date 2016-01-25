package com.miqian.mq.activity.current;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.activity.BaseActivity;
import com.miqian.mq.activity.user.ProjectMatchActivity;
import com.miqian.mq.entity.UserCurrent;
import com.miqian.mq.entity.UserCurrentResult;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.utils.UserUtil;
import com.miqian.mq.views.DialogPay;
import com.miqian.mq.views.WFYTitle;
import com.umeng.analytics.MobclickAgent;

import java.math.BigDecimal;

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
    private ImageView imageInterest;
    private TextView textInterest;

    private UserCurrent userCurrent;
    private BigDecimal downLimit = BigDecimal.ONE;
    private BigDecimal upLimit = new BigDecimal(9999999999L);
    private BigDecimal balance;
    private DialogPay dialogPay;

    private String interestRateString = "";

    @Override
    public void obtainData() {
        begin();
        HttpRequest.getUserCurrent(mActivity, new ICallback<UserCurrentResult>() {
            @Override
            public void onSucceed(UserCurrentResult result) {
                end();
                userCurrent = result.getData();
                interestRateString = userCurrent.getCurrentYearRate();
                refreshView();
            }

            @Override
            public void onFail(String error) {
                end();
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
            BigDecimal money = new BigDecimal(userCurrent.getCurAsset());
            downLimit = userCurrent.getCurrentBuyDownLimit();
            upLimit = userCurrent.getCurrentBuyUpLimit();
            balance = userCurrent.getBalance();
            if (money.compareTo(BigDecimal.ZERO) <= 0) {
                btRedeem.setEnabled(false);
                btRedeem.setTextColor(getResources().getColor(R.color.mq_b5));
            }
            if ("0".equals(userCurrent.getCurrentSwitch())) {//关闭认购开关
                btSubscribe.setEnabled(false);
                btSubscribe.setTextColor(getResources().getColor(R.color.mq_b5));
            }
            String tempInterest = userCurrent.getCurrentYearRate();
            if (TextUtils.isEmpty(tempInterest)) {
                imageInterest.setVisibility(View.VISIBLE);
                textInterest.setVisibility(View.GONE);
            } else {
                imageInterest.setVisibility(View.GONE);
                textInterest.setVisibility(View.VISIBLE);
                textInterest.setText(tempInterest);
            }
        } else {
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

        imageInterest = (ImageView) findViewById(R.id.image_interest);
        textInterest = (TextView) findViewById(R.id.text_interest);

        frameCurrentRecord.setOnClickListener(this);
        frameProjectMatch.setOnClickListener(this);
        btRedeem.setOnClickListener(this);
        btSubscribe.setOnClickListener(this);

        dialogPay = new DialogPay(mActivity) {
            @Override
            public void positionBtnClick(String moneyString) {
                if (!TextUtils.isEmpty(moneyString)) {
                    BigDecimal money = new BigDecimal(moneyString);
                    if (money.compareTo(downLimit) < 0) {
                        this.setTitle("提示：" + downLimit + "元起投");
                        this.setTitleColor(getResources().getColor(R.color.mq_r1));
                    } else if (money.compareTo(upLimit) > 0) {
                        this.setTitle("提示：请输入小于等于" + upLimit + "元");
                        this.setTitleColor(getResources().getColor(R.color.mq_r1));
                    } else {
                        UserUtil.currenPay(mActivity, moneyString, CurrentInvestment.PRODID_CURRENT, CurrentInvestment.SUBJECTID_CURRENT, TextUtils.isEmpty(interestRateString) ? "" : interestRateString + "%");
                        this.setEditMoney("");
                        this.setTitle("认购金额");
                        this.setTitleColor(getResources().getColor(R.color.mq_b1));
                        this.dismiss();
                    }
                } else {
                    this.setTitle("提示：请输入金额");
                    this.setTitleColor(getResources().getColor(R.color.mq_r1));
                }
            }

            @Override
            public void negativeBtnClick() {
                this.setEditMoney("");
                this.setTitle("认购金额");
                this.setTitleColor(getResources().getColor(R.color.mq_b1));
            }
        };
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
    protected String getPageName() {
        return "我的活期";
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.frame_current_record:
                MobclickAgent.onEvent(mActivity, "1035");
                startActivity(new Intent(mActivity, ActivityCurrentRecord.class));
                break;
            case R.id.frame_project_match:
                MobclickAgent.onEvent(mActivity, "1036");
                Intent intent = new Intent(mActivity, ProjectMatchActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_redeem:
                MobclickAgent.onEvent(mActivity, "1038");
                intent = new Intent(mActivity, ActivityRedeem.class);
                startActivity(intent);
                break;
            case R.id.bt_subscribe:
                MobclickAgent.onEvent(mActivity, "1037");
                if (balance != null && balance.compareTo(downLimit) >= 0) {
                    dialogPay.setEditMoneyHint("可用余额" + balance + "元");
                } else {
                    dialogPay.setEditMoneyHint(downLimit + "元起投");
                }
                UserUtil.loginPay(mActivity, dialogPay);
                break;
        }
    }
}
