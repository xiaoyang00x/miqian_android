package com.miqian.mq.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.activity.current.CurrentInvestment;
import com.miqian.mq.activity.user.LoginActivity;
import com.miqian.mq.entity.CurrentInfo;
import com.miqian.mq.entity.CurrentInfoResult;
import com.miqian.mq.listener.LoginListener;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.utils.Pref;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.utils.UserUtil;
import com.miqian.mq.views.DialogPay;
import com.miqian.mq.views.MySwipeRefresh;
import com.umeng.analytics.MobclickAgent;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentCurrent extends BasicFragment implements View.OnClickListener ,LoginListener{

    private View view;

    private Activity mContext;
    private DialogPay dialogPay;

    private CurrentInfo currentInfo;
    private String interestRateString;

    private BigDecimal downLimit = BigDecimal.ONE;
    private BigDecimal upLimit = new BigDecimal(9999999999L);
    private BigDecimal balance;
    private boolean isCache = false;

    @BindView(R.id.frame_title) View frameTitle;
    @BindView(R.id.title) TextView titleText;
    //年化收益率
    @BindView(R.id.text_interest) TextView textInterest;
    //立即认购
    @BindView(R.id.bt_investment) TextView btInvestment;
    @BindView(R.id.swipe_refresh) MySwipeRefresh swipeRefresh;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        if (savedInstanceState == null || view == null) {
            view = inflater.inflate(R.layout.frame_current, null);
        }

        ButterKnife.bind(this, view);

        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
        interestRateString = Pref.getString(Pref.CURRENT_RATE, mContext, "5");
        initView();
        if (Uihelper.getConfigCache(mContext)) {
            isCache = true;
        } else {
            isCache = false;
        }

        obtainData();
        initInterestView();
        return view;
    }


    private void initView() {
        frameTitle.setBackgroundColor(getResources().getColor(R.color.white));
        titleText.setText("秒钱宝");
        titleText.setTextColor(getResources().getColor(R.color.black));


        initInvestmentView(Pref.getString(Pref.CURRENT_STATE, mContext, ""));
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
            initInvestmentView(currentInfo.getCurrentSwitch());
        }
    }

    private void initInterestView() {
        textInterest.setText(interestRateString);
    }

    private void initInvestmentView(String status) {
        if ("0".equals(status)) {
            btInvestment.setText("已满额");
            btInvestment.setEnabled(false);
        } else {
            btInvestment.setText("立即认购");
            btInvestment.setEnabled(true);
        }
    }

    public void obtainData() {
        HttpRequest.getCurrentHome(mContext, new ICallback<CurrentInfoResult>() {
            @Override
            public void onSucceed(CurrentInfoResult result) {
                swipeRefresh.setRefreshing(false);
                currentInfo = result.getData();
                interestRateString = currentInfo.getCurrentYearRate();
                Pref.saveString(Pref.CURRENT_RATE, interestRateString, mContext);
                Pref.saveString(Pref.CURRENT_STATE, currentInfo.getCurrentSwitch(), mContext);
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

//                UserUtil.loginPay(mContext, dialogPay);
               LoginActivity.start(mContext);

                break;
//            case R.id.frame_image:
//                MobclickAgent.onEvent(mContext, "1006");
//                WebActivity.startActivity(mContext, Urls.web_current);
//                break;
//            case R.id.frame_earning:
//                MobclickAgent.onEvent(mContext, "1006");
//                if (isCache) {
//                    WebActivity.startActivity(getContext(), "file:///android_asset/current_earnings.html");
//                } else {
//                    WebActivity.startActivity(mContext, Urls.web_current_earning);
//                }
//                break;
//            case R.id.frame_safe:
//                MobclickAgent.onEvent(mContext, "1006");
//                if (isCache) {
//                    WebActivity.startActivity(getContext(), "file:///android_asset/current_safeguard.html");
//                } else {
//                    WebActivity.startActivity(mContext, Urls.web_current_safe);
//                }
//                break;
//            case R.id.frame_back:
//                MobclickAgent.onEvent(mContext, "1006");
//                if (isCache) {
//                    WebActivity.startActivity(getContext(), "file:///android_asset/current_redeem.html");
//                } else {
//                    WebActivity.startActivity(mContext, Urls.web_current_back);
//                }
//                break;
            default:
                break;
        }
    }

    @Override
    protected String getPageName() {
        return "首页-秒钱宝";
    }

    @Override
    public void loginSuccess() {
        dialogPay.show();
    }

    @Override
    public void logout() {

    }
}
