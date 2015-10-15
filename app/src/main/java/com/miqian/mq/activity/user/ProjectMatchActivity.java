package com.miqian.mq.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.miqian.mq.R;
import com.miqian.mq.activity.BaseActivity;
import com.miqian.mq.entity.UserRegularDetailResult;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.views.WFYTitle;

/**
 * Created by Jackie on 2015/10/15.
 * 项目匹配
 */
public class ProjectMatchActivity extends BaseActivity {

    private String pageNo = "1";
    private String pageSize = "30";
    private String peerCustId = "";//默认不填,(活期赚不填写) 定期计划的匹配项目填:1372

    private RecyclerView recyclerView;

    @Override
    public void onCreate(Bundle bundle) {
        Intent intent =getIntent();
        if (!TextUtils.isEmpty(intent.getStringExtra("peerCustId"))) {
            peerCustId = intent.getStringExtra("peerCustId");
        } else {
            peerCustId = "";
        }
        super.onCreate(bundle);
    }

    @Override
    public void obtainData() {
        HttpRequest.projectMatch(mActivity, new ICallback<UserRegularDetailResult>() {
            @Override
            public void onSucceed(UserRegularDetailResult result) {

            }

            @Override
            public void onFail(String error) {

            }
        }, pageNo, pageSize, peerCustId);
    }

    @Override
    public void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_project_match;
    }

    @Override
    public void initTitle(WFYTitle mTitle) {
        mTitle.setTitleText("项目匹配");
    }
}
