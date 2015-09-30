package com.miqian.mq.activity;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.miqian.mq.R;
import com.miqian.mq.adapter.CapitalRecordAdapter;
import com.miqian.mq.entity.CapitalRecord;
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
  SwipeRefreshLayout srl;
  CapitalRecordAdapter adapter;
  List<CapitalRecord.Item> list = new ArrayList<>();
  FrameLayout data_view;
  TextView empty_view;
  Dialog mWaitingDialog;

  CircleButton all, saving, withdraw, buy, redeem, transfer, maturity, other, preSelected;
  TextView all_t, saving_t, withdraw_t, buy_t, redeem_t, transfer_t, maturity_t, other_t,
      preSelected_t;
  String preTxt;
  LinearLayout filetr_container;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    //setContentView(R.layout.activity_capital_record);
    animShow = AnimationUtils.loadAnimation(this, R.anim.view_show);
    animHide = AnimationUtils.loadAnimation(this, R.anim.view_hide);
    filetr_container = (LinearLayout) findViewById(R.id.filter_container);
    filetr_container.setOnTouchListener(new View.OnTouchListener() {
      @Override public boolean onTouch(View v, MotionEvent event) {
        return true;
      }
    });
    mWaitingDialog = ProgressDialogView.create(this);
    mWaitingDialog.show();
    mWaitingDialog.setCanceledOnTouchOutside(false);
    updateData("custId", "pageNum", "pageSize", "startDate", "endDate", "operationType", true);
  }

  @Override public void obtainData() {

  }

  private void showEmptyView() {
    if (list.isEmpty()) {
      empty_view.setVisibility(View.VISIBLE);
      data_view.setVisibility(View.GONE);
    } else {
      empty_view.setVisibility(View.GONE);
      data_view.setVisibility(View.VISIBLE);
    }
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_capital_record, menu);
    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  @Override public void initView() {
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
    //
    preSelected.setColor(Color.RED);

    rv = (RecyclerView) findViewById(R.id.ultimate_recycler_view);

    LinearLayoutManager layoutManager = new LinearLayoutManager(this);
    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
    rv.setLayoutManager(layoutManager);
    rv.setVerticalScrollBarEnabled(true);
    srl = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
    srl.setColorSchemeResources(R.color.blue,R.color.grey, R.color.red,R.color.green);
    srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override public void onRefresh() {
        updateData("custId", "pageNum", "pageSize", "startDate", "endDate", "operationType", false);
        //srl.setRefreshing(false);
      }
    });
    adapter = new CapitalRecordAdapter(list, rv);
    adapter.setOnLoadMoreListener(new CapitalRecordAdapter.OnLoadMoreListener() {
      @Override public void onLoadMore() {
        //add null , so the adapter will check view_type and show progress bar at bottom
        list.add(null);
        adapter.notifyItemInserted(list.size() - 1);
        handler.postDelayed(new Runnable() {
          @Override public void run() {
            updateData("custId", "pageNum", "pageSize", "startDate", "endDate", "operationType",
                true);
          }
        }, 3000);
      }
    });
    rv.setAdapter(adapter);
  }

  @Override public int getLayoutId() {
    return R.layout.activity_capital_record;
  }

  /**
   * 需要根据用户的筛选结果动态修改
   */
  @Override public void initTitle(WFYTitle topLayout) {
    topLayout.setTitleText("资金记录");
    topLayout.addRightImage(R.drawable.ic_launcher);
    topLayout.setOnRightClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        Toast.makeText(CapitalRecordActivity.this, "jjjj", Toast.LENGTH_SHORT).show();
        if (filetr_container.getVisibility() == View.GONE) {
          filetr_container.setVisibility(View.VISIBLE);
          filetr_container.startAnimation(animShow);
        } else {
          filetr_container.startAnimation(animHide);
          filetr_container.setVisibility(View.GONE);
        }
      }
    });
  }

  public void searchBtn(View v) {
    switch (v.getId()) {
      case R.id.all:
        Toast.makeText(this, "all", Toast.LENGTH_SHORT).show();
        preSelected.setColor(0xdddddd);
        preSelected = all;

        preSelected.setColor(Color.WHITE);
        preSelected.setColor(Color.RED);
        getmTitle().setTitleText("资金记录");

        //white text
        preSelected_t.setTextColor(0xff505050);
        all_t.setTextColor(Color.WHITE);
        preSelected_t = all_t;
        break;
      case R.id.saving:
        Toast.makeText(this, "saving", Toast.LENGTH_SHORT).show();
        preSelected.setColor(0xdddddd);
        preSelected = saving;
        preSelected.setColor(Color.RED);
        getmTitle().setTitleText("充值");


        //white text
        preSelected_t.setTextColor(0xff505050);
        saving_t.setTextColor(Color.WHITE);
        preSelected_t = saving_t;
        break;

      case R.id.buy:
        Toast.makeText(this, "buy", Toast.LENGTH_SHORT).show();
        preSelected.setColor(0xdddddd);
        preSelected = buy;
        preSelected.setColor(Color.RED);
        getmTitle().setTitleText("认购");

        //white text
        preSelected_t.setTextColor(0xff505050);
        buy_t.setTextColor(Color.WHITE);
        preSelected_t = buy_t;
        break;
      case R.id.withdraw:
        Toast.makeText(this, "withdraw", Toast.LENGTH_SHORT).show();
        preSelected.setColor(0xdddddd);
        preSelected = withdraw;
        preSelected.setColor(Color.RED);
        getmTitle().setTitleText("提现");

        preSelected_t.setTextColor(0xff505050);
        withdraw_t.setTextColor(Color.WHITE);
        preSelected_t = withdraw_t;
        break;


      case R.id.redeem:
        Toast.makeText(this, "redeem", Toast.LENGTH_SHORT).show();
        preSelected.setColor(0xdddddd);
        preSelected = redeem;

        preSelected_t.setTextColor(0xff505050);
        preSelected_t = redeem_t;
        preSelected_t.setTextColor(0xffffffff);

        preSelected.setColor(Color.RED);
        getmTitle().setTitleText("赎回");
        break;

      case R.id.transfer:
        Toast.makeText(this, "transfer", Toast.LENGTH_SHORT).show();
        preSelected.setColor(0xdddddd);
        preSelected = transfer;
        preSelected.setColor(Color.RED);
        getmTitle().setTitleText("转让");

        preSelected_t.setTextColor(0xff505050);
        preSelected_t = transfer_t;
        preSelected_t.setTextColor(0xffffffff);
        break;

      case R.id.maturity:
        Toast.makeText(this, "maturity", Toast.LENGTH_SHORT).show();
        preSelected.setColor(0xdddddd);
        preSelected = maturity;
        preSelected.setColor(Color.RED);
        getmTitle().setTitleText("到期");
        preSelected_t.setTextColor(0xff505050);
        preSelected_t = maturity_t;
        preSelected_t.setTextColor(0xffffffff);
        break;

      case R.id.other:
        Toast.makeText(this, "other", Toast.LENGTH_SHORT).show();
        preSelected.setColor(0xdddddd);
        preSelected = other;

        preSelected.setColor(Color.RED);
        getmTitle().setTitleText("其他");
        preSelected_t.setTextColor(0xff505050);
        preSelected_t = other_t;
        preSelected_t.setTextColor(0xffffffff);
        break;
    }
    filetr_container.startAnimation(animHide);
    filetr_container.setVisibility(View.GONE);

    //todo 到网络获取数据。因为该界面的信息是分页的。
    updateData("custId", "pageNum", "pageSize", "startDate", "endDate", "operationType", false);
  }

  private void setPreSelected(CircleButton curC, TextView curT, String txt) {
    curC.setColor(Color.RED);
    curT.setText(txt);
    curT.setTextColor(Color.WHITE);

    //preSelected.setColor(Color.WHITE);//set up the previous CircleButton
    //preSelected_t.setText(preTxt);//set up the previous selected TextView's text content
    //preSelected_t.setTextColor(0x505050);//set up the previous selected TextView's text color
    //preSelected.setBackgroundColor(0);

    preSelected = curC;
    //preSelected_t = curT;
  }

  protected Handler handler = new Handler();

  //custId
  //    客户ID
  //是
  //    核心数据库中客户的唯一标识
  //pageNum
  //    页码
  //是
  //    查询条件
  //pageSize
  //    每页条数
  //是
  //    查询条件
  //startDate
  //    起始时间
  //否
  //查询条件，时间区间的起始值
  //    endDate
  //结束时间
  //    否
  //查询条件，时间区间的结束值
  //    operateType
  private void updateData(String custId, String pageNum, String pageSize, String startDate,
      String endDate, String operationType, boolean loadMore) {
    final boolean isMore = loadMore;
    HttpRequest.getCapitalRecords(this, new ICallback<CapitalRecord>() {

      @Override public void onSucceed(CapitalRecord result) {
        CapitalRecord record = result;
        if (list.size() > 0 && isMore) {
          list.remove(list.size() - 1);
          adapter.notifyItemRemoved(list.size());
        }
        Log.e("keen", "" + record.getData().length);
        if (!isMore) list.clear();//todo load more时，直接追加
        list.addAll(Arrays.asList(record.getData()));
        //list.add(record.getData()[0]);

        srl.setRefreshing(false);
        mWaitingDialog.dismiss();
        adapter.setLoaded();
        showEmptyView();
        adapter.notifyDataSetChanged();
        Uihelper.showToast(CapitalRecordActivity.this, "getCapitalRecords");
      }

      @Override public void onFail(String error) {
        Uihelper.showToast(CapitalRecordActivity.this, error);
        //TODO mock the success response
        //getData();
        //srl.setRefreshing(false);
        //mWaitingDialog.dismiss();
        //showEmptyView();
        //adapter.notifyDataSetChanged();
      }
    }, custId, pageNum, pageSize, startDate, endDate, operationType);
  }
}
