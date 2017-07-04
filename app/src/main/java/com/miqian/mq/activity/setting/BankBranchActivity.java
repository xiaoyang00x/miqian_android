package com.miqian.mq.activity.setting;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import com.marshalchen.ultimaterecyclerview.divideritemdecoration.HorizontalDividerItemDecoration;
import com.miqian.mq.R;
import com.miqian.mq.activity.BaseActivity;
import com.miqian.mq.adapter.BankBranchAdapter;
import com.miqian.mq.entity.BankBranch;
import com.miqian.mq.entity.BankBranchResult;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.views.WFYTitle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joy on 2015/9/23.
 */
public class BankBranchActivity extends BaseActivity implements BankBranchAdapter.MyItemClickListener {

    private RecyclerView recyclerView;
    private List<BankBranch> items;
    private String city, province;
    private String bankName;

    @Override
    public void onCreate(Bundle arg0) {
        Intent intent = getIntent();
        city = intent.getStringExtra("city");
        province = intent.getStringExtra("province");
        bankName = intent.getStringExtra("bankName");
        super.onCreate(arg0);
    }

    @Override
    public void obtainData() {
        begin();
        HttpRequest.getSubBranch(mActivity, new ICallback<BankBranchResult>() {
            @Override
            public void onSucceed(BankBranchResult result) {
                end();
                items = result.getData();
                setView();

            }

            @Override
            public void onFail(String error) {
                end();

            }
        }, province, city, bankName);
    }

    @Override
    public void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        items = new ArrayList<>();
        setView();

    }

    private void setView() {
        final LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).colorResId(R.color.mq_b4).size(1).build());
        BankBranchAdapter bankBranchAdapter = new BankBranchAdapter(items);
        bankBranchAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(bankBranchAdapter);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_bankbranch;
    }

    @Override
    public void initTitle(WFYTitle mTitle) {
        mTitle.setTitleText("选择开户行");
    }


    @Override
    public void onItemClick(View view, int postion) {
        Intent intent = new Intent();
        intent.putExtra("bankUnionNumber", items.get(postion).getBankUnionNumber());
        intent.putExtra("branchName", items.get(postion).getBranchName());
        setResult(1, intent);
        finish();

    }

    @Override
    protected String getPageName() {
        return "选择开户行";
    }
}
