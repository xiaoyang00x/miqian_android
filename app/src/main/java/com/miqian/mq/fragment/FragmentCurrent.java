package com.miqian.mq.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.activity.WebActivity;
import com.miqian.mq.activity.current.CurrentInvestment;
import com.miqian.mq.entity.CurrentInfo;
import com.miqian.mq.entity.CurrentInfoResult;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.net.Urls;
import com.miqian.mq.utils.Pref;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.utils.UserUtil;
import com.miqian.mq.views.DialogPay;
import com.miqian.mq.views.MySwipeRefresh;
import com.miqian.mq.views.WaterWaveView;
import com.umeng.analytics.MobclickAgent;
import com.umeng.onlineconfig.OnlineConfigAgent;

import java.math.BigDecimal;

public class FragmentCurrent extends BasicFragment implements View.OnClickListener {

    private View view;
    private WaterWaveView waterWaveView;
    private Button btInvestment;
    private TextView titleText;
    private RelativeLayout frameImage;
    private TextView textInterest;
    private MySwipeRefresh swipeRefresh;
    private RelativeLayout frameEarning;
    private RelativeLayout frameSafe;
    private RelativeLayout frameBack;
    private LinearLayout frameCrowd;

    private Activity mContext;
    private DialogPay dialogPay;

    private CurrentInfo currentInfo;
    private String interestRateString;

    private BigDecimal downLimit = BigDecimal.ONE;
    private BigDecimal upLimit = new BigDecimal(9999999999L);
    private BigDecimal balance;
    private boolean isCache = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        if (savedInstanceState == null || view == null) {
            view = inflater.inflate(R.layout.frame_current, null);
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
        interestRateString = Pref.getString(Pref.CURRENT_RATE, mContext, "6");
        findViewById(view);
        String valueCache = OnlineConfigAgent.getInstance().getConfigParams(mContext, "Cache");
        if ("YES".equals(valueCache)) {
            isCache = true;
        } else {
            isCache = false;
        }

        String value = OnlineConfigAgent.getInstance().getConfigParams(mContext, "Crowd");
        if ("YES".equals(value)) {
            swipeRefresh.setVisibility(View.GONE);
            frameCrowd.setVisibility(View.VISIBLE);
        } else {
            obtainData();
        }
        initInterestView();
        return view;
    }

    private void findViewById(View view) {
        frameCrowd = (LinearLayout) view.findViewById(R.id.frame_crowd);
        waterWaveView = (WaterWaveView) view.findViewById(R.id.wave_view);
        waterWaveView.startWave();

        titleText = (TextView) view.findViewById(R.id.title);
        titleText.setText("活期理财");

        frameImage = (RelativeLayout) view.findViewById(R.id.frame_image);
        frameImage.setOnClickListener(this);
        textInterest = (TextView) view.findViewById(R.id.text_interest);

        frameEarning = (RelativeLayout) view.findViewById(R.id.frame_earning);
        frameSafe = (RelativeLayout) view.findViewById(R.id.frame_safe);
        frameBack = (RelativeLayout) view.findViewById(R.id.frame_back);
        frameEarning.setOnClickListener(this);
        frameSafe.setOnClickListener(this);
        frameBack.setOnClickListener(this);

        btInvestment = (Button) view.findViewById(R.id.bt_investment);
        btInvestment.setOnClickListener(this);

        dialogPay = new DialogPay(mContext) {
            @Override
            public void positionBtnClick(String moneyString) {
                MobclickAgent.onEvent(mContext, "1057");
                if (!TextUtils.isEmpty(moneyString)) {
                    BigDecimal money = new BigDecimal(moneyString);
                    if (money.compareTo(downLimit) < 0) {
                        this.setTipText("提示：" + downLimit + "元起投");
                    } else if (money.compareTo(upLimit) > 0) {
                        this.setTipText("提示：请输入小于等于" + upLimit + "元");
                    } else {
                        UserUtil.currenPay(mContext, moneyString, CurrentInvestment.PRODID_CURRENT, CurrentInvestment.SUBJECTID_CURRENT, TextUtils.isEmpty(interestRateString) ? "" : interestRateString + "%");
                        this.setEditMoney("");
                        this.setTipTextVisibility(View.GONE);
                        this.dismiss();
                    }
                } else {
                    this.setTipText("提示：请输入金额");
                }
            }

            @Override
            public void negativeBtnClick() {
                MobclickAgent.onEvent(mContext, "1056");
                this.setEditMoney("");
                this.setTipTextVisibility(View.GONE);
            }
        };
        swipeRefresh = (MySwipeRefresh) view.findViewById(R.id.swipe_refresh);
        swipeRefresh.setOnPullRefreshListener(new MySwipeRefresh.OnPullRefreshListener() {
            @Override
            public void onRefresh() {
                obtainData();
            }
        });
    }

    private void refreshView() {
        if (currentInfo != null) {
            downLimit = currentInfo.getCurrentBuyDownLimit();
            upLimit = currentInfo.getCurrentBuyUpLimit();
            balance = currentInfo.getBalance();
            initInterestView();
            if (currentInfo.getCurrentSwitch().equals("0")) {
                btInvestment.setText("已满额");
                btInvestment.setEnabled(false);
            } else {
                btInvestment.setText("立即认购");
                btInvestment.setEnabled(true);
            }
        }
    }

    private void initInterestView() {
        textInterest.setText(interestRateString);
    }

    public void obtainData() {
        HttpRequest.getCurrentHome(mContext, new ICallback<CurrentInfoResult>() {
            @Override
            public void onSucceed(CurrentInfoResult result) {
                swipeRefresh.setRefreshing(false);
                currentInfo = result.getData();
                interestRateString = currentInfo.getCurrentYearRate();
                Pref.saveString(Pref.CURRENT_RATE, interestRateString, mContext);
                refreshView();
            }

            @Override
            public void onFail(String error) {
                swipeRefresh.setRefreshing(false);
                Uihelper.showToast(mContext, error);
            }
        });
    }

    @Override
    public void onDestroy() {
        if (waterWaveView != null) {
            waterWaveView.stopWave();
            waterWaveView = null;
        }
        if (dialogPay != null) {
            dialogPay = null;
        }
        super.onDestroy();
    }

    public void dialogPayDismiss() {
        if (dialogPay != null && dialogPay.isShowing()) {
            dialogPay.dismiss();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_investment:
                MobclickAgent.onEvent(mContext, "1007");
                if (balance != null && balance.compareTo(downLimit) >= 0) {
                    dialogPay.setEditMoneyHint("可用余额" + balance + "元");
                } else {
                    dialogPay.setEditMoneyHint(downLimit + "元起投");
                }
                UserUtil.loginPay(mContext, dialogPay);
                break;
            case R.id.frame_image:
                MobclickAgent.onEvent(mContext, "1006");
                WebActivity.startActivity(mContext, Urls.web_current);
                break;
            case R.id.frame_earning:
                MobclickAgent.onEvent(mContext, "1006");
                if (isCache) {
                    WebActivity.startActivity(getContext(), "file:///android_asset/lookaround1.html");
                } else {
                    WebActivity.startActivity(mContext, Urls.web_current_earning);
                }
                break;
            case R.id.frame_safe:
                MobclickAgent.onEvent(mContext, "1006");
                if (isCache) {
                    WebActivity.startActivity(getContext(), "file:///android_asset/lookaround1.html");
                } else {
                    WebActivity.startActivity(mContext, Urls.web_current_safe);
                }
                break;
            case R.id.frame_back:
                MobclickAgent.onEvent(mContext, "1006");
                MobclickAgent.onEvent(mContext, "1006");
                if (isCache) {
                    WebActivity.startActivity(getContext(), "file:///android_asset/lookaround1.html");
                } else {
                    WebActivity.startActivity(mContext, Urls.web_current_back);
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected String getPageName() {
        return "首页-活期";
    }
}
