package com.miqian.mq.activity.current;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.activity.BaseActivity;
import com.miqian.mq.activity.WebActivity;
import com.miqian.mq.entity.UserCurrent;
import com.miqian.mq.entity.UserCurrentResult;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.net.Urls;
import com.miqian.mq.utils.ExtendOperationController;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.utils.UserUtil;
import com.miqian.mq.views.DialogPay;
import com.miqian.mq.views.WFYTitle;
import com.umeng.analytics.MobclickAgent;

import java.math.BigDecimal;

/**
 * Created by Jackie on 2015/9/30.
 */
public class ActivityUserCurrent extends BaseActivity implements View.OnClickListener, ExtendOperationController.ExtendOperationListener {

    private TextView textEarning;
    private TextView textCaptial;
    private TextView textTotalEarning;
    private RelativeLayout frameCurrentRecord;
    private RelativeLayout frameProjectMatch;
    private LinearLayout frameTip;
    private Button btRedeem;//赎回
    private Button btSubscribe;//认购
    private TextView textInterest;

    private UserCurrent userCurrent;
    private BigDecimal downLimit = BigDecimal.ONE;
    private BigDecimal upLimit = new BigDecimal(9999999999L);
    private BigDecimal balance;

    private String interestRateString = "";
    private ExtendOperationController operationController;
    private boolean isCrowd = false;

    @Override
    public void onCreate(Bundle arg0) {
        operationController = ExtendOperationController.getInstance();
        operationController.registerExtendOperationListener(this);
        if (Uihelper.getConfigCrowd(mContext)) {
            isCrowd = true;
        } else {
            isCrowd = false;
        }
        super.onCreate(arg0);
    }

    @Override
    protected void onDestroy() {
        if (operationController != null) {
            operationController.unRegisterExtendOperationListener(this);
        }
        super.onDestroy();
    }

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
            textCaptial.setText(userCurrent.getCurAsset() + "");
            textTotalEarning.setText(userCurrent.getCurAmt() + "");
            BigDecimal money = userCurrent.getCurAsset();
            downLimit = userCurrent.getCurrentBuyDownLimit();
            upLimit = userCurrent.getCurrentBuyUpLimit();
            balance = userCurrent.getBalance();
            if (money != null && money.compareTo(BigDecimal.ZERO) <= 0) {
                btRedeem.setEnabled(false);
                btRedeem.setTextColor(getResources().getColor(R.color.mq_b5));
            }
            if ("0".equals(userCurrent.getCurrentSwitch())) {//关闭认购开关
                btSubscribe.setEnabled(false);
                btSubscribe.setTextColor(getResources().getColor(R.color.mq_b5));
            }
            String tempInterest = userCurrent.getCurrentYearRate();
            if (TextUtils.isEmpty(tempInterest)) {
                textInterest.setVisibility(View.GONE);
            } else {
                textInterest.setVisibility(View.VISIBLE);
                textInterest.setText(tempInterest + "%");
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
        frameTip = (LinearLayout) findViewById(R.id.frame_tip);

        frameCurrentRecord = (RelativeLayout) findViewById(R.id.frame_current_record);
        frameProjectMatch = (RelativeLayout) findViewById(R.id.frame_project_match);
        btRedeem = (Button) findViewById(R.id.bt_redeem);
        btSubscribe = (Button) findViewById(R.id.bt_subscribe);

        textInterest = (TextView) findViewById(R.id.text_interest);

        frameCurrentRecord.setOnClickListener(this);
        frameProjectMatch.setOnClickListener(this);
        btRedeem.setOnClickListener(this);
        btSubscribe.setOnClickListener(this);

//        dialogPay = new DialogPay(mActivity) {
//            @Override
//            public void positionBtnClick(String moneyString) {
//                if (!TextUtils.isEmpty(moneyString)) {
//                    BigDecimal money = new BigDecimal(moneyString);
//                    if (money.compareTo(downLimit) < 0) {
//                        this.setTipText("提示：" + downLimit + "元起投");
//                    } else if (money.compareTo(upLimit) > 0) {
//                        this.setTipText("提示：请输入小于等于" + upLimit + "元");
//                    } else {
//                        MobclickAgent.onEvent(mActivity, "1037_1");
//                        UserUtil.currenPay(mActivity, moneyString, CurrentInvestment.PRODID_CURRENT, CurrentInvestment.SUBJECTID_CURRENT, TextUtils.isEmpty(interestRateString) ? "" : interestRateString + "%");
//                        this.setEditMoney("");
//                        this.setTipTextVisibility(View.GONE);
//                        this.dismiss();
//                    }
//                } else {
//                    this.setTipText("提示：请输入金额");
//                }
//            }
//
//            @Override
//            public void negativeBtnClick() {
//                MobclickAgent.onEvent(mActivity, "1037_2");
//                this.setEditMoney("");
//                this.setTipTextVisibility(View.GONE);
//            }
//        };
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
                if (isCrowd) {
                    Uihelper.showToast(mActivity, R.string.qq_project_current_record);
                } else {
                    startActivity(new Intent(mActivity, ActivityCurrentRecord.class));
                }
                break;
            case R.id.frame_project_match:
                MobclickAgent.onEvent(mActivity, "1036");
                //项目匹配
                if (isCrowd) {
                    Uihelper.showToast(mActivity, R.string.qq_project_match);
                } else {
                    WebActivity.startActivity(mActivity, Urls.project_match + "0");
                }
                break;
            case R.id.bt_redeem:
                if ("0".equals(userCurrent.getRedeemSwitch())) {
                    Uihelper.showToast(mActivity, R.string.qq_project_redeem);
                } else if (userCurrent != null && userCurrent.getCurAsset() != null) {
                    MobclickAgent.onEvent(mActivity, "1038");
                    Intent intent = new Intent(mActivity, ActivityRedeem.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("userCurrent", userCurrent);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                break;
            case R.id.bt_subscribe:
                MobclickAgent.onEvent(mActivity, "1037");
                if (isCrowd) {
                    Uihelper.showToast(mActivity, R.string.qq_project_subscribe);
                } else {
                    ExtendOperationController.getInstance().doNotificationExtendOperation(ExtendOperationController.OperationKey.BACK_CURRENT, null);
                }
                break;
        }
    }

    @Override
    public void excuteExtendOperation(int operationKey, Object data) {

        if (operationKey == ExtendOperationController.OperationKey.REFRESH_CURRENTINFO) {
            obtainData();
        }

    }
}
