package com.miqian.mq.activity.current;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.marshalchen.ultimaterecyclerview.divideritemdecoration.HorizontalDividerItemDecoration;
import com.miqian.mq.R;
import com.miqian.mq.activity.BaseActivity;
import com.miqian.mq.adapter.AdapterCurrrentRecord;
import com.miqian.mq.entity.CurrentRecordResult;
import com.miqian.mq.entity.FundFlow;
import com.miqian.mq.entity.Page;
import com.miqian.mq.entity.RecordCurrent;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.views.CircleButton;
import com.miqian.mq.views.WFYTitle;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

/**
 * 资金记录\秒钱宝交易记录
 */
public class ActivityCurrentRecord extends BaseActivity {
    public static final String PRODUCT_CODE = "PRODUCT_CODE";
    public static final String BILL_TYPE = "BILL_TYPE";
    private Animation animHide;
    private Animation animShow;
    private CircleButton all;
    private CircleButton saving;
    private CircleButton withdraw;
    private CircleButton buy;
    private CircleButton redeem;
    private CircleButton transfer;
    private CircleButton maturity;
    private CircleButton other;
    private CircleButton preSelected;
    private TextView all_t;
    private TextView saving_t;
    private TextView withdraw_t;
    private TextView buy_t;
    private TextView redeem_t;
    private TextView transfer_t;
    private TextView maturity_t;
    private TextView other_t;
    private TextView preSelected_t;
    private RelativeLayout filetr_container;
    private RelativeLayout frameHide;
    private RecyclerView recyclerView;
    private List<FundFlow> dataList;

    private int pageNo = 1;
    private String pageSize = "20";
    private Page page;
    private boolean isLoading = false;
    private AdapterCurrrentRecord adapterCurrrentRecord;
    private String billType;                //资金记录类型
    private String productCode;             //资金产品类型
    private boolean isCurrent = false;      //是否为秒钱宝交易记录


    public static void startActivity(Context context, String billType, String productCode) {
        Intent intent = new Intent(context, ActivityCurrentRecord.class);
        intent.putExtra(BILL_TYPE, billType);
        intent.putExtra(PRODUCT_CODE, productCode);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle arg0) {
        billType = getIntent().getStringExtra(BILL_TYPE);
        productCode = getIntent().getStringExtra(PRODUCT_CODE);
        isCurrent = "3".equals(productCode);
        super.onCreate(arg0);
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
                        page = result.getData().getPage();
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
        adapterCurrrentRecord.setMaxItem(page.getTotalRows());
        recyclerView.setAdapter(adapterCurrrentRecord);
    }

    @Override
    public void initView() {
        animShow = AnimationUtils.loadAnimation(this, R.anim.view_show);
        animHide = AnimationUtils.loadAnimation(this, R.anim.view_hide);
        filetr_container = (RelativeLayout) findViewById(R.id.filter_container);
        frameHide = (RelativeLayout) findViewById(R.id.frame_hide);
        frameHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filetr_container.isShown() || frameHide.isShown()) {
                    filetr_container.startAnimation(animHide);
                    filetr_container.setVisibility(View.GONE);
                    frameHide.setVisibility(View.GONE);
                }
            }
        });

        all = (CircleButton) findViewById(R.id.all);
        all.setColor(0xdddddd);
        saving = (CircleButton) findViewById(R.id.saving);
        saving.setColor(0xdddddd);
        withdraw = (CircleButton) findViewById(R.id.withdraw);
        withdraw.setColor(0xdddddd);
        buy = (CircleButton) findViewById(R.id.buy);
        buy.setColor(0xdddddd);
        redeem = (CircleButton) findViewById(R.id.redeem);
        redeem.setColor(0xdddddd);
        transfer = (CircleButton) findViewById(R.id.transfer);
        transfer.setColor(0xdddddd);
        maturity = (CircleButton) findViewById(R.id.maturity);
        maturity.setColor(0xdddddd);
        other = (CircleButton) findViewById(R.id.other);
        other.setColor(0xdddddd);

        all_t = (TextView) findViewById(R.id.all_t);
        saving_t = (TextView) findViewById(R.id.saving_t);
        withdraw_t = (TextView) findViewById(R.id.withdraw_t);
        buy_t = (TextView) findViewById(R.id.buy_t);
        redeem_t = (TextView) findViewById(R.id.redeem_t);
        transfer_t = (TextView) findViewById(R.id.transfer_t);
        maturity_t = (TextView) findViewById(R.id.maturity_t);
        other_t = (TextView) findViewById(R.id.other_t);

        preSelected = all;
        preSelected_t = all_t;
        preSelected_t.setTextColor(Color.WHITE);
        preSelected.setColor(Color.RED);

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
            if (dataList.size() >= page.getTotalRows()) {
                return;
            }
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

        mTitle.setTitleText(isCurrent? "交易记录" : "资金记录");
        if(!isCurrent) {
            mTitle.addRightImage(R.drawable.btn_capital_record);
            mTitle.setOnRightClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    showContentView();
                    if (filetr_container.isShown()) {
                        if (dataList == null || dataList.size() == 0) {
                            showEmptyView();
                        }
                        filetr_container.setVisibility(View.GONE);
                        filetr_container.startAnimation(animHide);
                        frameHide.setVisibility(View.GONE);
                    } else {
                        filetr_container.startAnimation(animShow);
                        filetr_container.setVisibility(View.VISIBLE);
                        frameHide.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
    }

    @Override
    protected String getPageName() {
        return isCurrent? "秒钱宝交易记录" : "资金记录";
    }

    public void searchBtn(View v) {

        switch (v.getId()) {
            case R.id.all:
                MobclickAgent.onEvent(mActivity, "1041");
                preSelected.setColor(0xdddddd);
                preSelected = all;

                preSelected.setColor(Color.WHITE);
                preSelected.setColor(Color.RED);
                getmTitle().setTitleText("资金记录");

                //white text
                preSelected_t.setTextColor(0xff505050);
                all_t.setTextColor(Color.WHITE);
                preSelected_t = all_t;

                billType = FundFlow.BILL_TYPE_ALL;
                break;
            case R.id.saving:
                MobclickAgent.onEvent(mActivity, "1041_1");
                preSelected.setColor(0xdddddd);
                preSelected = saving;
                preSelected.setColor(Color.RED);
                getmTitle().setTitleText("充值");


                //white text
                preSelected_t.setTextColor(0xff505050);
                saving_t.setTextColor(Color.WHITE);
                preSelected_t = saving_t;

                billType = FundFlow.BILL_TYPE_CZ;
                break;

            case R.id.buy:
                MobclickAgent.onEvent(mActivity, "1041_3");
                preSelected.setColor(0xdddddd);
                preSelected = buy;
                preSelected.setColor(Color.RED);
                getmTitle().setTitleText("认购");

                //white text
                preSelected_t.setTextColor(0xff505050);
                buy_t.setTextColor(Color.WHITE);
                preSelected_t = buy_t;

                billType = FundFlow.BILL_TYPE_RG;
                break;
            case R.id.withdraw:
                MobclickAgent.onEvent(mActivity, "1041_2");
                preSelected.setColor(0xdddddd);
                preSelected = withdraw;
                preSelected.setColor(Color.RED);
                getmTitle().setTitleText("提现");

                preSelected_t.setTextColor(0xff505050);
                withdraw_t.setTextColor(Color.WHITE);
                preSelected_t = withdraw_t;

                billType = FundFlow.BILL_TYPE_TX;
                break;


            case R.id.redeem:
                MobclickAgent.onEvent(mActivity, "1041_4");
                preSelected.setColor(0xdddddd);
                preSelected = redeem;

                preSelected_t.setTextColor(0xff505050);
                preSelected_t = redeem_t;
                preSelected_t.setTextColor(0xffffffff);

                preSelected.setColor(Color.RED);
                getmTitle().setTitleText("赎回");

                billType = FundFlow.BILL_TYPE_SH;
                break;

            case R.id.transfer:
                MobclickAgent.onEvent(mActivity, "1041_5");
                preSelected.setColor(0xdddddd);
                preSelected = transfer;
                preSelected.setColor(Color.RED);
                getmTitle().setTitleText("转让");

                preSelected_t.setTextColor(0xff505050);
                preSelected_t = transfer_t;
                preSelected_t.setTextColor(0xffffffff);

                billType = FundFlow.BILL_TYPE_ZR;
                break;

            case R.id.maturity:
                MobclickAgent.onEvent(mActivity, "1041_6");
                preSelected.setColor(0xdddddd);
                preSelected = maturity;
                preSelected.setColor(Color.RED);
                getmTitle().setTitleText("到期");
                preSelected_t.setTextColor(0xff505050);
                preSelected_t = maturity_t;
                preSelected_t.setTextColor(0xffffffff);

                billType = FundFlow.BILL_TYPE_HK;
                break;

            case R.id.other:
                MobclickAgent.onEvent(mActivity, "1041_7");
                preSelected.setColor(0xdddddd);
                preSelected = other;

                preSelected.setColor(Color.RED);
                getmTitle().setTitleText("其他");
                preSelected_t.setTextColor(0xff505050);
                preSelected_t = other_t;
                preSelected_t.setTextColor(0xffffffff);

                billType = FundFlow.BILL_TYPE_OTHER;
                break;
            default:
                break;
        }
        filetr_container.startAnimation(animHide);
        filetr_container.setVisibility(View.GONE);
        frameHide.setVisibility(View.GONE);
        obtainData();
    }
}
