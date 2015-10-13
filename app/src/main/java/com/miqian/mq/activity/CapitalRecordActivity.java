package com.miqian.mq.activity;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.marshalchen.ultimaterecyclerview.divideritemdecoration.HorizontalDividerItemDecoration;
import com.miqian.mq.R;
import com.miqian.mq.adapter.CapitalRecordAdapter;
import com.miqian.mq.entity.CapitalRecord;
import com.miqian.mq.entity.CapitalRecordResult;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.views.CircleButton;
import com.miqian.mq.views.ProgressDialogView;
import com.miqian.mq.views.WFYTitle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 所有的搜索结果都是在当前界面刷新
 */
public class CapitalRecordActivity extends BaseActivity {
    Animation animHide, animShow;
    RecyclerView rv;
    CapitalRecordAdapter adapter;
    List<CapitalRecord.CapitalItem> list = new ArrayList<>();
    FrameLayout data_view;
    TextView empty_view;
    Dialog mWaitingDialog;

    CircleButton all;
    CircleButton saving;
    CircleButton withdraw;
    CircleButton buy;
    CircleButton redeem;
    CircleButton transfer;
    CircleButton maturity;
    CircleButton other;
    CircleButton preSelected;
    TextView all_t;
    TextView saving_t;
    TextView withdraw_t;
    TextView buy_t;
    TextView redeem_t;
    TextView transfer_t;
    TextView maturity_t;
    TextView other_t;
    TextView preSelected_t;
    String preTxt;
    RelativeLayout filetr_container;
    RelativeLayout relaHide;


    @Override
    public void obtainData() {

        updateData("1", "99999", "", "", "", false);
    }

    private void showEmptyView() {
        if (list.isEmpty()) {
            empty_view.setVisibility(View.VISIBLE);
        } else {
            empty_view.setVisibility(View.GONE);
        }
    }

    @Override
    public void initView() {

        animShow = AnimationUtils.loadAnimation(this, R.anim.view_show);
        animHide = AnimationUtils.loadAnimation(this, R.anim.view_hide);
        filetr_container = (RelativeLayout) findViewById(R.id.filter_container);
        relaHide = (RelativeLayout) findViewById(R.id.rela_hide);
        relaHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filetr_container.getVisibility()==View.VISIBLE){
                    filetr_container.startAnimation(animHide);
                    filetr_container.setVisibility(View.GONE);
                }
            }
        });


        mWaitingDialog = ProgressDialogView.create(this);
        mWaitingDialog.show();
        mWaitingDialog.setCanceledOnTouchOutside(false);


        data_view = (FrameLayout) findViewById(R.id.data_view);
        empty_view = (TextView) findViewById(R.id.empty_view);

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

        rv = (RecyclerView) findViewById(R.id.ultimate_recycler_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(layoutManager);
        rv.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).colorResId(R.color.mq_b4).size(1).marginResId(R.dimen.margin_left_right).build());
        rv.setVerticalScrollBarEnabled(true);
        adapter = new CapitalRecordAdapter(list, rv);
//        adapter.setOnLoadMoreListener(new CapitalRecordAdapter.OnLoadMoreListener() {
//            @Override
//            public void onLoadMore() {
//                //add null , so the adapter will check view_type and show progress bar at bottom
//                list.add(null);
//                adapter.notifyItemInserted(list.size() - 1);
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        updateData("pageNum", "pageSize", "startDate", "endDate", "operationType", true);
//                    }
//                }, 3000);
//            }
//        });
        rv.setAdapter(adapter);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_capital_record;
    }

    /**
     * 需要根据用户的筛选结果动态修改
     */
    @Override
    public void initTitle(WFYTitle topLayout) {
        topLayout.setTitleText("资金记录");
        topLayout.addRightImage(R.drawable.record_select);
        topLayout.setOnRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filetr_container.getVisibility() == View.VISIBLE) {
                    filetr_container.setVisibility(View.GONE);
                    filetr_container.startAnimation(animHide);
                } else {
                    filetr_container.startAnimation(animShow);
                    filetr_container.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public void searchBtn(View v) {
        String  type="";
        switch (v.getId()) {
            case R.id.all:
                preSelected.setColor(0xdddddd);
                preSelected = all;

                preSelected.setColor(Color.WHITE);
                preSelected.setColor(Color.RED);
                getmTitle().setTitleText("资金记录");

                //white text
                preSelected_t.setTextColor(0xff505050);
                all_t.setTextColor(Color.WHITE);
                preSelected_t = all_t;

                type="";
                break;
            case R.id.saving:
                preSelected.setColor(0xdddddd);
                preSelected = saving;
                preSelected.setColor(Color.RED);
                getmTitle().setTitleText("充值");


                //white text
                preSelected_t.setTextColor(0xff505050);
                saving_t.setTextColor(Color.WHITE);
                preSelected_t = saving_t;

                type="MQ01";
                break;

            case R.id.buy:
                preSelected.setColor(0xdddddd);
                preSelected = buy;
                preSelected.setColor(Color.RED);
                getmTitle().setTitleText("认购");

                //white text
                preSelected_t.setTextColor(0xff505050);
                buy_t.setTextColor(Color.WHITE);
                preSelected_t = buy_t;

                type="MQ03";
                break;
            case R.id.withdraw:
                preSelected.setColor(0xdddddd);
                preSelected = withdraw;
                preSelected.setColor(Color.RED);
                getmTitle().setTitleText("提现");

                preSelected_t.setTextColor(0xff505050);
                withdraw_t.setTextColor(Color.WHITE);
                preSelected_t = withdraw_t;

                type="MQ02";
                break;


            case R.id.redeem:
                preSelected.setColor(0xdddddd);
                preSelected = redeem;

                preSelected_t.setTextColor(0xff505050);
                preSelected_t = redeem_t;
                preSelected_t.setTextColor(0xffffffff);

                preSelected.setColor(Color.RED);
                getmTitle().setTitleText("赎回");

                type="MQ04";
                break;

            case R.id.transfer:
                preSelected.setColor(0xdddddd);
                preSelected = transfer;
                preSelected.setColor(Color.RED);
                getmTitle().setTitleText("转让");

                preSelected_t.setTextColor(0xff505050);
                preSelected_t = transfer_t;
                preSelected_t.setTextColor(0xffffffff);

                type="MQ05";
                break;

            case R.id.maturity:
                preSelected.setColor(0xdddddd);
                preSelected = maturity;
                preSelected.setColor(Color.RED);
                getmTitle().setTitleText("到期");
                preSelected_t.setTextColor(0xff505050);
                preSelected_t = maturity_t;
                preSelected_t.setTextColor(0xffffffff);

                type="MQ06";
                break;

            case R.id.other:
                preSelected.setColor(0xdddddd);
                preSelected = other;

                preSelected.setColor(Color.RED);
                getmTitle().setTitleText("其他");
                preSelected_t.setTextColor(0xff505050);
                preSelected_t = other_t;
                preSelected_t.setTextColor(0xffffffff);

                type="MQ07";
                break;
        }
        filetr_container.startAnimation(animHide);
        filetr_container.setVisibility(View.GONE);

        //todo 到网络获取数据。因为该界面的信息是分页的。

        updateData("1", "99999", "", "", type, false);
    }


    protected Handler handler = new Handler();

    /**
     * @param pageNo        页码
     * @param pageSize      每页条数
     * @param startDate     起始时间
     * @param endDate       结束时间
     * @param operationType operateType MQ01充值,MQ02提现,MQ03认购,MQ04赎回,MQ05转让,MQ06到期,MQ07其他,不传查询所有
     * @param loadMore
     */
    private void updateData(String pageNo, String pageSize, String startDate, String endDate, String operationType, boolean loadMore) {
        final boolean isMore = loadMore;
        HttpRequest.getCapitalRecords(this, new ICallback<CapitalRecordResult>() {

            @Override
            public void onSucceed(CapitalRecordResult result) {
                CapitalRecordResult record = result;
                if (list.size() > 0 && isMore) {
                    list.remove(list.size() - 1);
                    adapter.notifyItemRemoved(list.size());
                }
                //todo load more时，直接追加
                if (!isMore) {
                    list.clear();
                }
                list.addAll(record.getData().getAssetRecord());
                mWaitingDialog.dismiss();
                adapter.setLoaded();
                showEmptyView();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFail(String error) {
                Uihelper.showToast(CapitalRecordActivity.this, error);
                showEmptyView();
                mWaitingDialog.dismiss();
                //TODO mock the success response
                //getData();
                //srl.setRefreshing(false);
                //mWaitingDialog.dismiss();
                //showEmptyView();
                //adapter.notifyDataSetChanged();
            }
        }, pageNo, pageSize, startDate, endDate, operationType);
    }
}
