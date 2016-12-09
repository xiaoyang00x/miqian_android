package com.miqian.mq.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.activity.BaseActivity;
import com.miqian.mq.adapter.AdapterUserRegular;
import com.miqian.mq.entity.RegInvest;
import com.miqian.mq.entity.UserRegular;
import com.miqian.mq.entity.UserRegularResult;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.views.MySwipeRefresh;
import com.miqian.mq.views.WFYTitle;
import com.umeng.analytics.MobclickAgent;
import com.umeng.onlineconfig.OnlineConfigAgent;

import java.util.List;

public class UserRegularActivity extends BaseActivity implements View.OnClickListener, AdapterUserRegular.MyItemClickListener, RadioGroup.OnCheckedChangeListener {

    private Button titleLeft;
    private Button titleRight;
    private RecyclerView mRecyclerView;
    private ImageButton btLeft;
    private MySwipeRefresh swipeRefresh;

    private AdapterUserRegular mAdapter;

    private int pageNo = 1;
    private String pageSize = "10";
    private String isExpiry = "N";//是否结息 N：计息中 Y：已结息
    private String isForce = "1";//是否强制刷新 1强制刷新 0不强制刷新
    private boolean isLoading = false;
    private int mType = 0;

    private UserRegular userRegular;
    private List<RegInvest> regInvestList;
    private RadioGroup radioGroup;
    private boolean launchSuccess;
    private boolean isQQproject;
    private View layoutQQRegular;
    private TextView tvQQRegular;

    @Override
    public void onCreate(Bundle arg0) {
        //手Q开关
        String value = OnlineConfigAgent.getInstance().getConfigParams(mContext, "Crowd");
        if ("YES".equals(value)) {
            isQQproject = true;
        }
        launchSuccess = getIntent().getBooleanExtra("launchSuccess", false);
        if (launchSuccess) {
            isExpiry = "ZR";
            mType = 2;
        }
        super.onCreate(arg0);
    }

    @Override
    public void obtainData() {
        pageNo = 1;
        isForce = "1";
        if (!swipeRefresh.isRefreshing()) {
            begin();
        }
        HttpRequest.getUserRegular(mActivity, new ICallback<UserRegularResult>() {
            @Override
            public void onSucceed(UserRegularResult result) {
                end();
                swipeRefresh.setRefreshing(false);
                userRegular = result.getData();
                regInvestList = userRegular.getRegInvest();
                refreshView();
            }

            @Override
            public void onFail(String error) {
                swipeRefresh.setRefreshing(false);
                end();
                Uihelper.showToast(mActivity, error);
            }
        }, String.valueOf(pageNo), pageSize, isExpiry, isForce);
    }

    private void loadMore() {
        if (!isLoading) {
            if (regInvestList.size() >= userRegular.getPage().getCount()) {
//                mAdapter.notifyItemChanged(regInvestList.size() + 1);
                return;
            }
            isLoading = true;
            pageNo += 1;
            isForce = "0";
            HttpRequest.getUserRegular(mActivity, new ICallback<UserRegularResult>() {
                @Override
                public void onSucceed(UserRegularResult result) {
                    List<RegInvest> tempList = result.getData().getRegInvest();
                    if (regInvestList != null && tempList != null && tempList.size() > 0) {
                        regInvestList.addAll(tempList);
                        mAdapter.notifyItemInserted(regInvestList.size() + 1);
                    }
                    isLoading = false;
                }

                @Override
                public void onFail(String error) {
                    isLoading = false;
                    Uihelper.showToast(mActivity, error);
                }
            }, String.valueOf(pageNo), pageSize, isExpiry, isForce);
        }
    }

    @Override
    public void initView() {

        radioGroup = (RadioGroup) findViewById(R.id.radio_group);
        tvQQRegular = (TextView) findViewById(R.id.tv_qq_nodata);
//        RadioButton btnRight = (RadioButton) findViewById(R.id.bt_right);
//        if (launchSuccess) {
//            btnRight.setChecked(true);
//        }
        radioGroup.setOnCheckedChangeListener(this);
        swipeRefresh = (MySwipeRefresh) findViewById(R.id.swipe_refresh);
        swipeRefresh.setOnPullRefreshListener(new MySwipeRefresh.OnPullRefreshListener() {
            @Override
            public void onRefresh() {
                obtainData();
            }
        });
        layoutQQRegular = findViewById(R.id.layout_qq_regular_nodata);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                int totalItemCount = layoutManager.getItemCount();
                if (lastVisibleItem >= totalItemCount - 2) {
                    loadMore();
                }
            }
        });
    }

    private void refreshView() {
        mAdapter = new AdapterUserRegular(this, regInvestList, userRegular.getReg(), userRegular.getPage(), mType);
        mAdapter.setOnItemClickListener(this);
        mAdapter.setMaxItem(userRegular.getPage().getCount());
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_user_regular;
    }

    @Override
    public void initTitle(WFYTitle mTitle) {
        mTitle.setTitleText("我的定期");
    }

    @Override
    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.title_left:
//                titleLeft.setTextColor(getResources().getColor(R.color.mq_r1));
//                titleLeft.setBackgroundResource(R.drawable.bt_regualr_tab_left_selected);
//                titleRight.setTextColor(getResources().getColor(R.color.white));
//                titleRight.setBackgroundResource(R.drawable.bt_regualr_tab_right);
//                isExpiry = "N";
//                mType = 0;
//                obtainData();
//                break;
//            case R.id.title_right:
//
//                titleLeft.setTextColor(getResources().getColor(R.color.white));
//                titleLeft.setBackgroundResource(R.drawable.bt_regualr_tab_left);
//                titleRight.setTextColor(getResources().getColor(R.color.mq_r1));
//                titleRight.setBackgroundResource(R.drawable.bt_regualr_tab_right_selected);
//                isExpiry = "Y";
//                mType = 1;
//                obtainData();
//                break;
//        }
    }

    @Override
    public void onItemClick(View view, int postion) {
        if (isQQproject) {
            return;
        }
        RegInvest regInvest = regInvestList.get(postion);
        if (regInvest.getBearingStatus().equals("Y")) {
            MobclickAgent.onEvent(mActivity, "1040");
        } else {
            MobclickAgent.onEvent(mActivity, "1039");
        }

        Intent intent = null;
        if (mType == 2) {
            intent = new Intent(mActivity, MyRegularTransferDetailActivity.class);
        } else {
            intent = new Intent(mActivity, UserRegularDetailActivity.class);
        }
        intent.putExtra("investId", regInvest.getId());
        intent.putExtra("clearYn", regInvest.getBearingStatus());
        intent.putExtra("projectType", regInvest.getProdId());
        startActivity(intent);
    }

    @Override
    protected String getPageName() {
        return "我的定期";
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkId) {

        if (checkId == R.id.bt_left) {
            if (isQQproject) {
                layoutQQRegular.setVisibility(View.GONE);
                swipeRefresh.setVisibility(View.VISIBLE);
            }
            isExpiry = "N";
            mType = 0;
            obtainData();
        } else if (checkId == R.id.bt_middle) {
            isExpiry = "Y";
            mType = 1;
            //手Q活动
            if (isQQproject) {
                swipeRefresh.setVisibility(View.GONE);
                layoutQQRegular.setVisibility(View.VISIBLE);
                SpannableString spannableString = new SpannableString("亲爱的财主,春节期间，平台参加手机QQ抢红包活动。因活动期间访问用户过多，暂停已结算标的内容查询,待活动高峰结束后(2月1日)恢复操作,请您谅解。");
                spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mActivity, R.color.mq_r1_v2)), 57, 63, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                tvQQRegular.setText(spannableString);
                return;
            }
            obtainData();
        } else if (checkId == R.id.bt_right) {
            isExpiry = "ZR";
            mType = 2;
            obtainData();
        }
        obtainData();

    }
}
