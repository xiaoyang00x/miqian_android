package com.miqian.mq.activity.setting;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.miqian.mq.R;
import com.miqian.mq.activity.BaseActivity;
import com.miqian.mq.adapter.BankBranchAdapter;
import com.miqian.mq.entity.BankBranch;
import com.miqian.mq.entity.BankBranchResult;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.views.WFYTitle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joy on 2015/9/23.
 */
public class BankBranchActivity extends BaseActivity implements BankBranchAdapter.MyItemClickListener {

    private EditText et_bankbranch;
    private RecyclerView recyclerView;
    private List<BankBranch> items;
    private String city,province;

    @Override
    public void obtainData() {
        mWaitingDialog.show();
        HttpRequest.getSubBranch(mActivity, new ICallback<BankBranchResult>() {
            @Override
            public void onSucceed(BankBranchResult result) {
                mWaitingDialog.dismiss();
                items = result.getData();
                setView();

            }

            @Override
            public void onFail(String error) {
                mWaitingDialog.dismiss();

            }
        }, province, city, "300");
    }

    @Override
    public void initView() {

        Intent intent = getIntent();
        city = intent.getStringExtra("city");
        province = intent.getStringExtra("province");

        et_bankbranch = (EditText) findViewById(R.id.et_bankbranch);
        et_bankbranch.setFocusable(false);
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        items = new ArrayList<>();
        setView();

    }

    private void setView() {
        final LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

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
        mTitle.setRightText("完成");
        mTitle.setOnRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String branch = et_bankbranch.getText().toString();
                if (!TextUtils.isEmpty(branch)) {
                    Intent data = new Intent();
                    data.putExtra("branch", branch);
                    setResult(1, data);
                    finish();
                } else {
                    Uihelper.showToast(mActivity, "请选择或输入支行");
                }

            }
        });
    }

    @Override
    public void onItemClick(View view, int postion) {

        et_bankbranch.setText(items.get(postion).getShortBranchName());

    }
}
