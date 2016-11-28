package com.miqian.mq.activity.current;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.miqian.mq.R;
import com.miqian.mq.activity.BaseActivity;
import com.miqian.mq.adapter.MyCurrentAdapter;
import com.miqian.mq.entity.UserCurrentData;
import com.miqian.mq.entity.UserCurrentResult;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.utils.ExtendOperationController;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.views.MySwipeRefresh;
import com.miqian.mq.views.WFYTitle;
import com.umeng.analytics.MobclickAgent;

import java.math.BigDecimal;

/**
 * Created by Jackie on 2015/9/30.
 * 我的秒钱宝
 */
public class ActivityUserCurrent extends BaseActivity implements View.OnClickListener, ExtendOperationController.ExtendOperationListener {

    private Button btRedeem;//赎回
    private Button btSubscribe;//认购
    private MySwipeRefresh swipeRefresh;
    private RecyclerView recyclerView;


    private UserCurrentData userCurrentData;
    private MyCurrentAdapter myCurrentAdapter;
    private ExtendOperationController operationController;
    private boolean reFresh;//赎回成功返回，不弹加载框和下拉刷新只更新数据，重新刷新页面标识

    @Override
    public void onCreate(Bundle arg0) {
        operationController = ExtendOperationController.getInstance();
        operationController.registerExtendOperationListener(this);
        super.onCreate(arg0);
    }

    @Override
    protected void onDestroy() {
        if (operationController != null) {
            operationController.unRegisterExtendOperationListener(this);
        }
        super.onDestroy();
    }

    private boolean inProcess = false;
    private final Object mLock = new Object();

    @Override
    public void obtainData() {

        if (inProcess) {
            return;
        }
        synchronized (mLock) {
            inProcess = true;
        }
        if (!reFresh) {
            begin();
            swipeRefresh.setRefreshing(true);
        }
        HttpRequest.getUserCurrent(mActivity, new ICallback<UserCurrentResult>() {
            @Override
            public void onSucceed(UserCurrentResult result) {
                synchronized (mLock) {
                    inProcess = false;
                    reFresh = false;
                }
                swipeRefresh.setRefreshing(false);
                end();
                if (result == null || result.getData() == null) {
                    return;
                }
                userCurrentData = result.getData();
//                interestRateString = userCurrentData.getUserCurrent().getYearInterest().toString();
                refreshView();
            }

            @Override
            public void onFail(String error) {
                synchronized (mLock) {
                    inProcess = false;
                    reFresh = false;
                }
                swipeRefresh.setRefreshing(false);
                end();
                refreshView();
                Uihelper.showToast(mActivity, error);
            }
        });
    }

    private void refreshView() {
        if (userCurrentData != null) {
            myCurrentAdapter = new MyCurrentAdapter(mContext, userCurrentData);
            recyclerView.setAdapter(myCurrentAdapter);


            if (userCurrentData.getUserRedeem() != null
                    && userCurrentData.getUserRedeem().getCurDayResidue() != null
                    && BigDecimal.ZERO.compareTo(userCurrentData.getUserRedeem().getCurDayResidue()) >= 0) {
                btRedeem.setEnabled(false);
                btRedeem.setTextColor(getResources().getColor(R.color.mq_b5));
            }

        } else {
            btRedeem.setEnabled(false);
            btRedeem.setTextColor(getResources().getColor(R.color.mq_b5));
        }

    }

    private void initListener() {
        swipeRefresh.setOnPullRefreshListener(mOnPullRefreshListener);
    }

    @Override
    public void initView() {
        btRedeem = (Button) findViewById(R.id.bt_redeem);
        btSubscribe = (Button) findViewById(R.id.bt_subscribe);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        swipeRefresh = (MySwipeRefresh) findViewById(R.id.swipe_refresh);


        btRedeem.setOnClickListener(this);
        btSubscribe.setOnClickListener(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        initListener();

    }


    private MySwipeRefresh.OnPullRefreshListener mOnPullRefreshListener = new MySwipeRefresh.OnPullRefreshListener() {
        @Override
        public void onRefresh() {
            obtainData();
        }
    };


    @Override
    public int getLayoutId() {
        return R.layout.user_current;
    }

    @Override
    public void initTitle(WFYTitle mTitle) {
        mTitle.setTitleText("我的秒钱宝");
    }

    @Override
    protected String getPageName() {
        return "我的秒钱宝";
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_redeem:
                if (userCurrentData != null) {
                    MobclickAgent.onEvent(mActivity, "1038");
                    Intent intent = new Intent(mActivity, ActivityRedeem.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("userCurrentData", userCurrentData);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                break;
            case R.id.bt_subscribe:
                MobclickAgent.onEvent(mActivity, "1037");
                ExtendOperationController.getInstance().doNotificationExtendOperation(ExtendOperationController.OperationKey.BACK_CURRENT, null);
                break;
        }
    }

    @Override
    public void excuteExtendOperation(int operationKey, Object data) {

        if (operationKey == ExtendOperationController.OperationKey.REFRESH_CURRENTINFO) {
            reFresh = true;
            obtainData();
        }

    }
}
