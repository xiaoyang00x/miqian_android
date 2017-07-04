package com.miqian.mq.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dinuscxj.progressbar.CircleProgressBar;
import com.miqian.mq.R;
import com.miqian.mq.activity.CurrentDetailActivity;
import com.miqian.mq.activity.user.LoginActivity;
import com.miqian.mq.entity.ProductBaseInfo;
import com.miqian.mq.entity.MqResult;
import com.miqian.mq.listener.LoginListener;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.utils.Constants;
import com.miqian.mq.utils.FormatUtil;
import com.miqian.mq.utils.Pref;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.views.DialogPay;
import com.miqian.mq.views.MySwipeRefresh;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentCurrent extends BasicFragment implements View.OnClickListener ,LoginListener{

    private View view;

    private Activity mContext;
    private DialogPay dialogPay;

    private ProductBaseInfo currentInfo;
    private String interestRateString;

    private BigDecimal downLimit = BigDecimal.ONE;
    private BigDecimal upLimit = new BigDecimal(9999999999L);

    @BindView(R.id.frame_title) View frameTitle;
    @BindView(R.id.title) TextView titleText;
    @BindView(R.id.tv_mqb_title) TextView tv_mqb_title;
    @BindView(R.id.tv_begin_time) TextView tv_begin_time;
    //年化收益率
    @BindView(R.id.text_interest) TextView textInterest;
    //立即认购
    @BindView(R.id.bt_investment) TextView btInvestment;
    @BindView(R.id.swipe_refresh) MySwipeRefresh swipeRefresh;
    @BindView(R.id.line_progress) CircleProgressBar line_progress;

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
        obtainData();
        initInterestView();
        return view;
    }


    private void initView() {
        frameTitle.setBackgroundColor(getResources().getColor(R.color.white));
        titleText.setText("秒钱宝");
        titleText.setTextColor(getResources().getColor(R.color.black));


        initInvestmentView(Pref.getString(Pref.CURRENT_STATE, mContext, Constants.PRODUCT_STATUS_DSX));
        btInvestment.setOnClickListener(this);

//        dialogPay = new DialogPay(mContext) {
//            @Override
//            public void positionBtnClick(String moneyString) {
//                MobclickAgent.onEvent(mContext, "1057");
//                if (!TextUtils.isEmpty(moneyString)) {
//                    BigDecimal money = new BigDecimal(moneyString);
//                    if (money.compareTo(downLimit) < 0) {
//                        this.setTipText("提示：" + downLimit + "元起投");
//                    } else if (money.compareTo(upLimit) > 0) {
//                        this.setTipText("提示：请输入小于等于" + upLimit + "元");
//                    } else {
//                        UserUtil.currenPay(mContext, moneyString, CurrentInvestment.PRODID_CURRENT, CurrentInvestment.SUBJECTID_CURRENT, TextUtils.isEmpty(interestRateString) ? "" : interestRateString + "%");
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
//                MobclickAgent.onEvent(mContext, "1056");
//                this.setEditMoney("");
//                this.setTipTextVisibility(View.GONE);
//            }
//        };
        swipeRefresh.setOnPullRefreshListener(new MySwipeRefresh.OnPullRefreshListener() {
            @Override
            public void onRefresh() {
                obtainData();
            }
        });
    }

    private void refreshView() {
        if (currentInfo != null) {
//            downLimit = currentInfo.getMinInvestAmount();
//            upLimit = currentInfo.getMaxInvestAmount();
            initInterestView();
            initInvestmentView(currentInfo.getStatus());
            tv_begin_time.setText(FormatUtil.formatDate(currentInfo.getStartTimeStamp(), "MM月dd日 HH:mm发售"));
            tv_mqb_title.setText(currentInfo.getProductName());
            line_progress.setProgress((int)(currentInfo.getInvestedProgress()*100));
        }
    }

    private void initInterestView() {
        textInterest.setText(interestRateString);
    }

    private void initInvestmentView(String status) {
       int currentStatus = Constants.getCurrentStatus(status);
        if (Constants.STATUS_DKB == currentStatus) {
            btInvestment.setText("待开标");
            btInvestment.setEnabled(true);
            btInvestment.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_no_begin));
            tv_begin_time.setVisibility(View.VISIBLE);
        } else if(Constants.STATUS_YKB == currentStatus) {
            btInvestment.setText("立即认购");
            btInvestment.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_red));
            btInvestment.setEnabled(true);
            tv_begin_time.setVisibility(View.INVISIBLE);
        }else {
            btInvestment.setText("已满额");
            btInvestment.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_red));
            btInvestment.setEnabled(false);
            tv_begin_time.setVisibility(View.INVISIBLE);
        }
    }

    public void obtainData() {
        HttpRequest.getCurrentHome(mContext, new ICallback<MqResult<ProductBaseInfo>>() {
            @Override
            public void onSucceed(MqResult<ProductBaseInfo> result) {
                swipeRefresh.setRefreshing(false);
                currentInfo = result.getData();
                interestRateString = currentInfo.getYearRate();
                Pref.saveString(Pref.CURRENT_RATE, interestRateString, mContext);
                Pref.saveString(Pref.CURRENT_STATE, currentInfo.getStatus(), mContext);
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
//                MobclickAgent.onEvent(mContext, "1007");
//                if (balance != null && balance.compareTo(downLimit) >= 0) {
//                    dialogPay.setEditMoneyHint("可用余额" + balance + "元");
//                } else {
//                    dialogPay.setEditMoneyHint(downLimit + "元起投");
//                }

//                UserUtil.loginPay(mContext, dialogPay);
//               LoginActivity.start(mContext);

                CurrentDetailActivity.startActivity(mContext, currentInfo.getProductCode());

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
