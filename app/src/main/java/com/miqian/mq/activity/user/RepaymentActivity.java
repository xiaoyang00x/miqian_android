package com.miqian.mq.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.miqian.mq.R;
import com.miqian.mq.activity.BaseActivity;
import com.miqian.mq.adapter.AdapterRepayment;
import com.miqian.mq.entity.RepaymentInfo;
import com.miqian.mq.entity.RepaymentResult;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.views.WFYTitle;

import java.util.List;

/**
 * Created by Jackie on 2015/10/15.
 */
public class RepaymentActivity extends BaseActivity {

    private RecyclerView recyclerView;

    private String investId;

    private List<RepaymentInfo> mList;
    private AdapterRepayment mAdapter;

    @Override
    public void onCreate(Bundle bundle) {
        Intent intent = getIntent();
        investId = intent.getStringExtra("investId");
        super.onCreate(bundle);
    }

    @Override
    protected String getPageName() {
        return "还款情况";
    }


    @Override
    public void obtainData() {
        HttpRequest.getRepayment(mActivity, new ICallback<RepaymentResult>() {
            @Override
            public void onSucceed(RepaymentResult result) {
                mList = result.getData().getRepaymentPlan();
                if (mList != null && mList.size() > 0) {
                   showContentView();
                    refreshView();
                } else {
                    showEmptyView();
                }
            }

            @Override
            public void onFail(String error) {
                showErrorView();
            }
        }, investId);
    }

    @Override
    public void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
    }

    private void refreshView() {
        mAdapter = new AdapterRepayment(mList);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_repayment;
    }

    @Override
    public void initTitle(WFYTitle mTitle) {
        mTitle.setTitleText("还款情况");
    }
}
