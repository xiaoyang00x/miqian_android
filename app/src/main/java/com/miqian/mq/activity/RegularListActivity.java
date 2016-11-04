package com.miqian.mq.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.adapter.RegularListAdapter;
import com.miqian.mq.entity.GetRegularResult;
import com.miqian.mq.entity.RegularBaseData;
import com.miqian.mq.entity.SubjectCategoryData;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.utils.Constants;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.views.WFYTitle;

import java.util.ArrayList;

/**
 * 使用红包跳转到的定期列表
 * Created by guolei_wang on 16/1/23.
 */
public class RegularListActivity extends BaseActivity {

    private RegularListAdapter mAdapter;
    private String promId; //促销 ID

    private RecyclerView recyclerView;
    private TextView tv_description;

    @Override
    public void obtainData() {
        getFitSubject();
    }

    public static void startActivity(Context context, String promId) {
        Intent intent = new Intent(context, RegularListActivity.class);
        intent.putExtra(Constants.PROMID, promId);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        promId = getIntent().getStringExtra(Constants.PROMID);
        super.onCreate(savedInstanceState);
        showDefaultView();
    }

    @Override
    public void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        tv_description = (TextView) findViewById(R.id.tv_description);

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new RegularListAdapter(mApplicationContext);
        recyclerView.setAdapter(mAdapter);
    }

    private boolean inProcess = false;
    private final Object mLock = new Object();

    private void getFitSubject() {
        if (inProcess) {
            return;
        }
        synchronized (mLock) {
            inProcess = true;
        }
        begin();
        HttpRequest.getFitSubject(RegularListActivity.this, promId, new ICallback<GetRegularResult>() {

            @Override
            public void onSucceed(GetRegularResult result) {
                synchronized (mLock) {
                    inProcess = false;
                }
                end();
                showContentView();
                handleSuccessResult(result);
            }

            @Override
            public void onFail(String error) {
                synchronized (mLock) {
                    inProcess = false;
                }
                end();
                showErrorView();
                Uihelper.showToast(RegularListActivity.this, error);
            }
        });
    }

    private ArrayList<RegularBaseData> mList;

    private void handleSuccessResult(GetRegularResult result) {
        if (result == null || result.getData() == null ||
                (mList = result.getData().getSubjectData()) == null ||
                mList.size() <= 0) {
            return;
        }
        for (RegularBaseData info : mList) {
            mAdapter.add(info);
        }
        tv_description.setText(result.getData().getFitSubjectDesc());
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_regular_project;
    }

    @Override
    public void initTitle(WFYTitle mTitle) {
        mTitle.setTitleText("红包/卡使用");
    }

    @Override
    protected String getPageName() {
        return "红包/卡使用";
    }
}
