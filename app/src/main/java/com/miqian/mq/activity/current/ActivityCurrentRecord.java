package com.miqian.mq.activity.current;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.marshalchen.ultimaterecyclerview.divideritemdecoration.HorizontalDividerItemDecoration;
import com.miqian.mq.R;
import com.miqian.mq.activity.BaseActivity;
import com.miqian.mq.adapter.AdapterCurrrentRecord;
import com.miqian.mq.entity.CurrentRecordResult;
import com.miqian.mq.entity.FundFlow;
import com.miqian.mq.entity.RecordCurrent;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.views.WFYTitle;

import java.util.List;

/**
 * 资金记录\秒钱宝交易记录
 */
public class ActivityCurrentRecord extends BaseActivity {
    public static final String PRODUCT_CODE = "PRODUCT_CODE";
    public static final String BILL_TYPE = "BILL_TYPE";
    private RecyclerView recyclerView;
    private List<FundFlow> dataList;

    private int pageNo = 1;
    private String pageSize = "20";
    private boolean isLoading = false;
    private AdapterCurrrentRecord adapterCurrrentRecord;
    private String billType;                //资金记录类型
    private String productCode;             //资金产品类型


    public static void startActivity(Context context, String billType, String productCode) {
        Intent intent = new Intent(context, ActivityCurrentRecord.class);
        intent.putExtra(BILL_TYPE, billType);
        intent.putExtra(PRODUCT_CODE, productCode);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        billType = getIntent().getStringExtra(BILL_TYPE);
        productCode = getIntent().getStringExtra(PRODUCT_CODE);
    }

    @Override
    public void obtainData() {
        pageNo = 1;
        begin();
        HttpRequest.getFundFlow(mActivity, new ICallback<CurrentRecordResult>() {
            @Override
            public void onSucceed(CurrentRecordResult result) {
                end();
                RecordCurrent data = result.getData();
                if (data != null) {
                    dataList = data.getFundFlowList();
                    if (dataList != null && dataList.size() > 0) {
                        showContentView();
                        refreshView();
                    } else {
                        showEmptyView();
                    }
                }
            }

            @Override
            public void onFail(String error) {
                end();
                Uihelper.showToast(mActivity, error);
                showErrorView();
            }
        }, String.valueOf(pageNo), pageSize, billType, productCode);

    }


    private void refreshView() {
        adapterCurrrentRecord = new AdapterCurrrentRecord(dataList);
        recyclerView.setAdapter(adapterCurrrentRecord);
    }

    @Override
    public void initView() {

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).colorResId(R.color.mq_b4).size(1).marginResId(R.dimen.margin_left_right).build());

        recyclerView.setHasFixedSize(true);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                int totalItemCount = layoutManager.getItemCount();
                if (lastVisibleItem >= totalItemCount - 3) {

                    loadMore();
                }
            }
        });
    }

    private void loadMore() {
        if (!isLoading) {
//            if (dataList.size() >= page.getCount()) {
//                return;
//            }
            isLoading = true;
            pageNo += 1;
            HttpRequest.getFundFlow(mActivity, new ICallback<CurrentRecordResult>() {
                @Override
                public void onSucceed(CurrentRecordResult result) {
                    List<FundFlow> tempList = result.getData().getFundFlowList();
                    if (dataList != null && tempList != null && tempList.size() > 0) {
                        dataList.addAll(tempList);
                        adapterCurrrentRecord.notifyItemInserted(dataList.size());
                    }
                    isLoading = false;
                }

                @Override
                public void onFail(String error) {
                    isLoading = false;
                }
            }, String.valueOf(pageNo), pageSize, billType, productCode);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_current_record;
    }

    @Override
    public void initTitle(WFYTitle mTitle) {

        mTitle.setTitleText("资金记录");
    }

    @Override
    protected String getPageName() {
        return "活期资金记录";
    }
}
