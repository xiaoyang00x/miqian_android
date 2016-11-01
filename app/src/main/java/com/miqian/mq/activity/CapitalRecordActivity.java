package com.miqian.mq.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.marshalchen.ultimaterecyclerview.divideritemdecoration.HorizontalDividerItemDecoration;
import com.miqian.mq.R;
import com.miqian.mq.adapter.CapitalRecordAdapter;
import com.miqian.mq.entity.CapitalItem;
import com.miqian.mq.entity.CapitalRecordResult;
import com.miqian.mq.entity.Page;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.utils.MobileOS;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.utils.UserUtil;
import com.miqian.mq.views.CircleButton;
import com.miqian.mq.views.WFYTitle;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

/**
 * 所有的搜索结果都是在当前界面刷新
 */
public class CapitalRecordActivity extends BaseActivity {
    Animation animHide, animShow;
    RecyclerView recyclerView;
    CapitalRecordAdapter adapter;
    List<CapitalItem> list = new ArrayList<>();

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
    private RelativeLayout frameHide;

    private int pageNo = 1;
    private String pageSize = "20";
    private Page page;
    private boolean isLoading = false;
    private String mType = "";
    private TextView tvTips;
    private View mViewnoresult_data;
    private boolean isStop;  //activity被finish则停止从网络获取数据的异步操作
    private ImageView ivData;

    @Override
    public void onCreate(Bundle arg0) {

        String jpushToken = getIntent().getStringExtra("token");
        if (!TextUtils.isEmpty(jpushToken)) {
            //通知进来的情况下，不是当前用户则退出此界面
            String token = UserUtil.getToken(this);
            if (UserUtil.hasLogin(this) && !token.equals(jpushToken)) {
                isStop=true;
                finish();
            }
        }
        super.onCreate(arg0);
    }

    @Override
    public void obtainData() {
        if (isStop){
            return;
        }
        pageNo = 1;
        begin();
        //取全部
        HttpRequest.getCapitalRecords(this, new ICallback<CapitalRecordResult>() {

            @Override
            public void onSucceed(CapitalRecordResult result) {
                end();
                list.clear();
                CapitalRecordResult record = result;
                if (record.getData() != null && record.getData().getAssetRecord().size() > 0) {
                    showContentView();
                    page = record.getData().getPage();
                    list.addAll(record.getData().getAssetRecord());
                    refreshView();
                } else {
                    showEmptyView();
                }
            }

            @Override
            public void onFail(String error) {
                end();
                Uihelper.showToast(CapitalRecordActivity.this, error);
                showErrorView();
            }
        }, String.valueOf(pageNo), pageSize, mType);
    }


    private void refreshView() {
        adapter = new CapitalRecordAdapter(list);
        adapter.setMaxItem(page.getCount());
        recyclerView.setAdapter(adapter);
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

        recyclerView = (RecyclerView) findViewById(R.id.ultimate_recycler_view);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
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

        mViewnoresult_data = findViewById(R.id.frame_no_recorddata);
        findViewById(R.id.tv_refreshdata).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showContentView();
                obtainData();
            }
        });
        tvTips = (TextView) findViewById(R.id.tv_recordtip);
        ivData = (ImageView) findViewById(R.id.iv_record_data);
    }

    private void loadMore() {
        if (!isLoading) {
            if (list.size() >= page.getCount()) {
                return;
            }
            isLoading = true;
            pageNo += 1;
            //加载更多
            HttpRequest.getCapitalRecords(this, new ICallback<CapitalRecordResult>() {

                @Override
                public void onSucceed(CapitalRecordResult result) {
                    List<CapitalItem> tempList = result.getData().getAssetRecord();
                    if (list != null && tempList != null && tempList.size() > 0) {
                        list.addAll(tempList);
                        adapter.notifyItemInserted(list.size());
                    }
                    isLoading = false;
                }

                @Override
                public void onFail(String error) {
                    isLoading = false;
                    Uihelper.showToast(mActivity, error);
                }
            }, String.valueOf(pageNo), pageSize, mType);

        }
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
        topLayout.addRightImage(R.drawable.btn_capital_record);
        topLayout.setOnRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showContentView();
                if (filetr_container.isShown()) {
                    if (list == null || list.size() == 0) {
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

    @Override
    protected String getPageName() {
        return "资金记录";
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

                mType = "";
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

                mType = "MQ01";
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

                mType = "MQ03";
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

                mType = "MQ02";
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

                mType = "MQ04";
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

                mType = "MQ05";
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

                mType = "MQ06";
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

                mType = "MQ07";
                break;
            default:
                break;
        }
        filetr_container.startAnimation(animHide);
        filetr_container.setVisibility(View.GONE);
        frameHide.setVisibility(View.GONE);
        obtainData();
    }

    /**
     * 无数据
     */
    protected void showEmptyView() {
        mContentView.setVisibility(View.VISIBLE);
        mViewnoresult_data.setVisibility(View.VISIBLE);
        findViewById(R.id.tv_refreshdata).setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        tvTips.setText("暂时没有数据");
        ivData.setBackgroundResource(R.drawable.nodata);
    }

    /**
     * 显示数据
     */
    protected void showContentView() {
        mContentView.setVisibility(View.VISIBLE);
        mViewnoresult_data.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);

    }

    /**
     * 获取失败，请重新获取
     */
    protected void showErrorView() {

        mContentView.setVisibility(View.VISIBLE);
        mViewnoresult_data.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        findViewById(R.id.tv_refreshdata).setVisibility(View.VISIBLE);
        if (MobileOS.getNetworkType(mContext) == -1) {
            tvTips.setText("暂时没有网络");
            ivData.setBackgroundResource(R.drawable.nonetwork);
        }else {
            tvTips.setText("数据获取失败，请重新获取");
            ivData.setBackgroundResource(R.drawable.error_data);
        }
    }

}
