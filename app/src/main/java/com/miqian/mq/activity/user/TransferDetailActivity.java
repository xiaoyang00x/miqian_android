package com.miqian.mq.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.marshalchen.ultimaterecyclerview.divideritemdecoration.HorizontalDividerItemDecoration;
import com.miqian.mq.R;
import com.miqian.mq.activity.BaseActivity;
import com.miqian.mq.adapter.AdapterTransferDetail;
import com.miqian.mq.entity.TranferDetailInfo;
import com.miqian.mq.entity.TransferDetailResult;
import com.miqian.mq.entity.TransferInfo;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.views.WFYTitle;

import java.util.List;

/**
 * Created by Jackie on 2015/10/15.
 */
public class TransferDetailActivity extends BaseActivity {

    private TextView textTransferMoney;
    private TextView textTransferingMoney;
    private TextView textTransferLeaveMoney;
    private RecyclerView recyclerView;

    private String investId;
    private String clearYn;

    private List<TranferDetailInfo> mList;
    private TransferInfo transferInfo;

    private AdapterTransferDetail mAdapter;

    @Override
    public void onCreate(Bundle bundle) {
        Intent intent = getIntent();
        investId = intent.getStringExtra("investId");
        clearYn = intent.getStringExtra("clearYn");
        super.onCreate(bundle);
    }

    @Override
    public void obtainData() {
        mWaitingDialog.show();
        HttpRequest.getTransferDeatil(mActivity, new ICallback<TransferDetailResult>() {
            @Override
            public void onSucceed(TransferDetailResult result) {
                mWaitingDialog.dismiss();
                mList = result.getData().getTranslist();
                transferInfo = result.getData().getTransAmt();
                refreshView();
            }

            @Override
            public void onFail(String error) {
                mWaitingDialog.dismiss();
                Uihelper.showToast(mActivity, error);
            }
        }, investId, clearYn);
    }

    @Override
    public void initView() {
        textTransferMoney = (TextView) findViewById(R.id.text_transfer_money);
        textTransferingMoney = (TextView) findViewById(R.id.text_transfering_money);
        textTransferLeaveMoney = (TextView) findViewById(R.id.text_transfer_leave_money);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).colorResId(R.color.mq_b4).size(1).marginResId(R.dimen.margin_left_right).build());
        recyclerView.setHasFixedSize(true);
    }

    private void refreshView() {
        if (transferInfo != null) {
            textTransferMoney.setText(transferInfo.getTransed());
            textTransferingMoney.setText(transferInfo.getTransing());
            textTransferLeaveMoney.setText(transferInfo.getCantransed());
        }
        mAdapter = new AdapterTransferDetail(mList);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_transfer_detail;
    }

    @Override
    public void initTitle(WFYTitle mTitle) {
        mTitle.setTitleText("转让情况");
    }

    @Override
    protected String getPageName() {
        return "转让情况";
    }
}
