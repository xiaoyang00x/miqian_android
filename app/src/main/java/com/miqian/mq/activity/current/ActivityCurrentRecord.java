package com.miqian.mq.activity.current;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.TextView;

import com.marshalchen.ultimaterecyclerview.divideritemdecoration.HorizontalDividerItemDecoration;
import com.miqian.mq.R;
import com.miqian.mq.activity.BaseActivity;
import com.miqian.mq.adapter.AdapterPacket;
import com.miqian.mq.adapter.typeadapter.AdapterCurrrentRecord;
import com.miqian.mq.entity.RecordCurrent;
import com.miqian.mq.entity.CurrentRecordResult;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.views.WFYTitle;

import java.util.List;

/**
 * Created by Administrator on 2015/10/9.
 */
public class ActivityCurrentRecord extends BaseActivity {
    private TextView tvInterest;
    private RecyclerView recyclerView;
    private List<RecordCurrent.CurSubRecord> dataList;

    @Override
    public void obtainData() {
        mWaitingDialog.show();
        HttpRequest.getMyCurrentRecord(mActivity, new ICallback<CurrentRecordResult>() {
            @Override
            public void onSucceed(CurrentRecordResult result) {
                mWaitingDialog.dismiss();
                setData(result);
            }

            @Override
            public void onFail(String error) {
                mWaitingDialog.dismiss();
                Uihelper.showToast(mActivity, error);
            }
        }, "1", "99999", "");

    }

    private void setData(CurrentRecordResult result) {


        RecordCurrent currentRecord = result.getData();
        if (!TextUtils.isEmpty(currentRecord.getCurAmt())) {
            tvInterest.setText(currentRecord.getCurAmt());
        }
       dataList= currentRecord.getCurSubRecord();
        if (dataList!=null){

            AdapterCurrrentRecord adapterCurrrentRecord = new AdapterCurrrentRecord(dataList);
            recyclerView.setAdapter(adapterCurrrentRecord);
        }


    }

    @Override
    public void initView() {


        tvInterest = (TextView) findViewById(R.id.tv_interest);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).colorResId(R.color.mq_b4).size(1).marginResId(R.dimen.margin_left_right).build());
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_current_record;
    }

    @Override
    public void initTitle(WFYTitle mTitle) {

        mTitle.setTitleText("活期资金记录");

    }

    @Override
    protected String getPageName() {
        return "活期资金记录";
    }
}
