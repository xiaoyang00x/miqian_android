package com.miqian.mq.activity.current;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.TextView;

import com.marshalchen.ultimaterecyclerview.divideritemdecoration.HorizontalDividerItemDecoration;
import com.miqian.mq.R;
import com.miqian.mq.activity.BaseActivity;
import com.miqian.mq.adapter.typeadapter.AdapterCurrrentRecord;
import com.miqian.mq.entity.CurSubRecord;
import com.miqian.mq.entity.CurrentRecordResult;
import com.miqian.mq.entity.Page;
import com.miqian.mq.entity.RecordCurrent;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.utils.UserUtil;
import com.miqian.mq.views.WFYTitle;

import java.util.List;

/**
 * Created by Administrator on 2015/10/9.
 */
public class ActivityCurrentRecord extends BaseActivity {
    private TextView tvInterest;
    private RecyclerView recyclerView;
    private List<CurSubRecord> dataList;
    private TextView textHistory;

    private int pageNo = 1;
    private String pageSize = "20";
    private Page page;
    private boolean isLoading = false;
    private AdapterCurrrentRecord adapterCurrrentRecord;
    private boolean isStop;  //activity被finish则停止从网络获取数据的异步操作

    @Override
    public void onCreate(Bundle arg0) {

        String jpushToken = getIntent().getStringExtra("token");
        if (!TextUtils.isEmpty(jpushToken)) {
            //通知进来的情况下，不是当前用户则退出此界面
            String token = UserUtil.getToken(this);
            if (UserUtil.hasLogin(this) && !token.equals(jpushToken)) {
                isStop = true;
                finish();
            }
        }
        super.onCreate(arg0);
    }

    @Override
    public void obtainData() {
        if (isStop) {
            return;
        }
        pageNo = 1;
        mWaitingDialog.show();
        HttpRequest.getMyCurrentRecord(mActivity, new ICallback<CurrentRecordResult>() {
            @Override
            public void onSucceed(CurrentRecordResult result) {
                mWaitingDialog.dismiss();
                RecordCurrent data = result.getData();
                setData(result);
                page = data.getPage();
                if (data != null) {
                    dataList = data.getCurSubRecord();
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
                mWaitingDialog.dismiss();
                Uihelper.showToast(mActivity, error);
                showErrorView();
            }
        }, String.valueOf(pageNo), pageSize, "");

    }


    private void refreshView() {
        adapterCurrrentRecord = new AdapterCurrrentRecord(dataList);
        adapterCurrrentRecord.setMaxItem(page.getCount());
        textHistory.setText("历史赎回收益(元)");
        recyclerView.setAdapter(adapterCurrrentRecord);
    }

    private void setData(CurrentRecordResult result) {

        RecordCurrent currentRecord = result.getData();
        if (!TextUtils.isEmpty(currentRecord.getCurAmt())) {
            tvInterest.setText(currentRecord.getCurAmt());
        }
    }

    @Override
    public void initView() {
        tvInterest = (TextView) findViewById(R.id.tv_interest);
        textHistory = (TextView) findViewById(R.id.text_history);

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
                int lastVisibleItem = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                int totalItemCount = layoutManager.getItemCount();
                if (lastVisibleItem >= totalItemCount - 3) {

                    loadMore();
                }
            }
        });
    }

    private void loadMore() {
        if (!isLoading) {
            if (dataList.size() >= page.getCount()) {
                return;
            }
            isLoading = true;
            pageNo += 1;
            HttpRequest.getMyCurrentRecord(mActivity, new ICallback<CurrentRecordResult>() {
                @Override
                public void onSucceed(CurrentRecordResult result) {
                    List<CurSubRecord> tempList = result.getData().getCurSubRecord();
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
            }, String.valueOf(pageNo), pageSize, "");
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_current_record;
    }

    @Override
    public void initTitle(WFYTitle mTitle) {

        mTitle.setTitleText("认购赎回记录");
//        mTitle.setRightText("查看合同");
//        mTitle.setOnRightClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                WebActivity.startActivity(mActivity, "https://www.baidu.com/");
//            }
//        });
    }

    @Override
    protected String getPageName() {
        return "活期资金记录";
    }
}
