package com.miqian.mq.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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

public class FragmentCurrent extends BasicFragment implements View.OnClickListener {

    private ProductBaseInfo currentInfo;
    private String interestRateString;

    private View view;
    @BindView(R.id.frame_title)
    View frameTitle;
    @BindView(R.id.title)
    TextView titleText;
    @BindView(R.id.tv_mqb_title)
    TextView tv_mqb_title;
    @BindView(R.id.bt_right)
    ImageButton bt_right;
    @BindView(R.id.swipe_refresh)
    MySwipeRefresh swipeRefresh;
    @BindView(R.id.line_progress)
    CircleProgressBar line_progress;

    /**
     * 开标时间
     */
    @BindView(R.id.tv_begin_time)
    TextView tv_begin_time;

    /**
     * 年化收益率
     */
    @BindView(R.id.text_interest)
    TextView textInterest;

    /**
     * 立即认购
     */
    @BindView(R.id.bt_investment)
    TextView btInvestment;

    /**
     * 认购金额
     */
    @BindView(R.id.tv_subscription_amount)
    TextView tv_subscription_amount;

    /**
     * 投资人数
     */
    @BindView(R.id.tv_person_count)
    TextView tv_person_count;

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
        textInterest.setText(interestRateString);
        return view;
    }


    private void initView() {
        frameTitle.setBackgroundColor(getResources().getColor(R.color.white));
        titleText.setText("秒钱宝");
        titleText.setTextColor(getResources().getColor(R.color.black));
        bt_right.setImageResource(R.drawable.icon_mqb_why);


        initInvestmentView(Pref.getString(Pref.CURRENT_STATE, mContext, Constants.PRODUCT_STATUS_DSX));
        btInvestment.setOnClickListener(this);
        swipeRefresh.setOnPullRefreshListener(new MySwipeRefresh.OnPullRefreshListener() {
            @Override
            public void onRefresh() {
                obtainData();
            }
        });
    }

    private void refreshView() {
        if (currentInfo != null) {
            initInvestmentView(currentInfo.getStatus());
            textInterest.setText(interestRateString);
            tv_begin_time.setText(FormatUtil.formatDate(currentInfo.getStartTimeStamp(), "MM月dd日 HH:mm发售"));
            tv_mqb_title.setText(currentInfo.getProductName());
            line_progress.setProgress((int) (currentInfo.getInvestedProgress() * 100));
            if (new BigDecimal(100000).compareTo(currentInfo.getInvestedAmount()) == 1) {
                tv_subscription_amount.setText(FormatUtil.formatAmount(currentInfo.getInvestedAmount()) + "元");
            } else {
                tv_subscription_amount.setText(FormatUtil.formatAmount(currentInfo.getInvestedAmount().divide(new BigDecimal(10000))) + "万");
            }
            tv_person_count.setText(currentInfo.getInvestedCount() + "人");
        }
    }


    private void initInvestmentView(String status) {
        int currentStatus = Constants.getCurrentStatus(status);
        if (Constants.STATUS_DKB == currentStatus) {
            btInvestment.setText("待开标");
            btInvestment.setEnabled(true);
            btInvestment.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_no_begin));
            tv_begin_time.setVisibility(View.VISIBLE);
        } else if (Constants.STATUS_YKB == currentStatus) {
            btInvestment.setText("立即认购");
            btInvestment.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_red));
            btInvestment.setEnabled(true);
            tv_begin_time.setVisibility(View.INVISIBLE);
        } else {
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_investment:
                CurrentDetailActivity.startActivity(mContext, currentInfo.getProductCode());
                break;
            default:
                break;
        }
    }

    @Override
    protected String getPageName() {
        return "首页-秒钱宝";
    }

}
