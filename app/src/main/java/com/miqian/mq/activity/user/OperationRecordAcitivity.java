package com.miqian.mq.activity.user;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.activity.BaseActivity;
import com.miqian.mq.entity.Operation;
import com.miqian.mq.entity.OperationResult;
import com.miqian.mq.entity.RecordInfo;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.views.WFYTitle;

import java.util.List;

/**
 * Created by Administrator on 2016/6/15.
 */
public class OperationRecordAcitivity extends BaseActivity {
    private TextView tvDateFirst;
    private TextView tvTimeFirst;
    private TextView tvContentFirst;
    private LinearLayout linearLayoutRecord;
    private List<Operation> mOperationList;
    private String investId;
    private LinearLayout linearLayoutFirst;

    @Override
    public void onCreate(Bundle arg0) {
        investId = getIntent().getStringExtra("investId");
        super.onCreate(arg0);
    }

    @Override
    public void obtainData() {
        begin();
        HttpRequest.findInvestInfo(this, investId, new ICallback<OperationResult>() {
            @Override
            public void onSucceed(OperationResult result) {
                end();
                RecordInfo data = result.getData();
                if (data != null) {
                    mOperationList = data.getOperation();
                    reReshView();
                }
            }

            @Override
            public void onFail(String error) {
                end();
                Uihelper.showToast(OperationRecordAcitivity.this, error);
            }
        });

    }

    private void reReshView() {

        if (mOperationList == null) {
            return;
        }
        linearLayoutFirst.setVisibility(View.VISIBLE);
        //标的相关记录
        if (mOperationList.size() > 0) {
            tvContentFirst.setText(mOperationList.get(0).getOperationContent());
            String operationDt = mOperationList.get(0).getOperationDt();
            if (!TextUtils.isEmpty(operationDt)) {
                String dt = Uihelper.timestampToDateStr_other(Long.parseLong(operationDt));
                String[] split = dt.split(" ");
                tvDateFirst.setText(split[0]);
                tvTimeFirst.setText(split[1]);
            }
        }
        if (mOperationList.size() > 1) {
            findViewById(R.id.view_grey).setVisibility(View.VISIBLE);
            for (int i = 1; i < mOperationList.size(); i++) {
                View itemRecord = LayoutInflater.from(this).inflate(R.layout.item_record, null);
                TextView tvDate = (TextView) itemRecord.findViewById(R.id.tv_date);
                TextView tvTime = (TextView) itemRecord.findViewById(R.id.tv_time);
                TextView tvContent = (TextView) itemRecord.findViewById(R.id.tv_content);
                View view = (View) itemRecord.findViewById(R.id.view);
                String operationDt = mOperationList.get(i).getOperationDt();
                if (!TextUtils.isEmpty(operationDt)) {
                    String dt = Uihelper.timestampToDateStr_other(Long.parseLong(operationDt));
                    String[] split = dt.split(" ");
                    tvDate.setText(split[0]);
                    tvTime.setText(split[1]);
                }
                tvContent.setText(mOperationList.get(i).getOperationContent());
                if (i == mOperationList.size() - 1) {
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(Uihelper.dip2px(mContext, 3), Uihelper.dip2px(mContext, 25));
                    view.setLayoutParams(layoutParams);
                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP | RelativeLayout.CENTER_HORIZONTAL);//addRule参数对应RelativeLayout XML布局的属性
                }
                linearLayoutRecord.addView(itemRecord);
            }
        }

    }

    @Override
    public void initView() {

        tvDateFirst = (TextView) findViewById(R.id.tv_date_first);
        tvTimeFirst = (TextView) findViewById(R.id.tv_time_first);
        tvContentFirst = (TextView) findViewById(R.id.tv_content_first);
        linearLayoutRecord = (LinearLayout) findViewById(R.id.linear_record);
        linearLayoutFirst = (LinearLayout) findViewById(R.id.layout_first);

    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_operationrecord;
    }

    @Override
    public void initTitle(WFYTitle mTitle) {
        mTitle.setTitleText("标的相关记录");

    }

    @Override
    protected String getPageName() {
        return "标的相关记录";
    }
}
