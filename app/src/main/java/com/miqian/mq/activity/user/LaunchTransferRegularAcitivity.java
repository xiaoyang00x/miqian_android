package com.miqian.mq.activity.user;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.miqian.mq.R;
import com.miqian.mq.activity.BaseActivity;
import com.miqian.mq.entity.RegTransDetail;
import com.miqian.mq.entity.RegTransDetailResult;
import com.miqian.mq.entity.UserRegularDetail;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.views.WFYTitle;

import java.io.Serializable;

/**
 * Created by 涂良坛 on 2016/6/6.
 * 发起定期转让
 */
public class LaunchTransferRegularAcitivity extends BaseActivity implements View.OnLayoutChangeListener, View.OnClickListener {

    private TextView textProject;
    private TextView textCapitalMoney;
    private TextView textLimit;
    private TextView textInterestRate;
    private TextView textDateStart;
    private TextView textDateEnd;
    private TextView textRepayment;
    private TextView textPrnTransSa; //可转让金额
    private TextView textRemindDay;  //剩余期限
    private EditText et_TransferRate;//转让后的年化收益
    private EditText etMoney;  //转让金额
    private TextView textChangeMoney; //加价，减价
    private TextView textDiscountRate;//转让折让比
    private RegTransDetail regTransDetail;
    private String investId;
    private int screenHeight;
    private int keyHeight;
    private View activityRootView;
    private TextView btnClick;
    private ScrollView scrollview;

    @Override
    public void onCreate(Bundle arg0) {

        //获取屏幕高度
        screenHeight = this.getWindowManager().getDefaultDisplay().getHeight();
        //阀值设置为屏幕高度的1/3
        keyHeight = screenHeight / 3;
        investId = getIntent().getStringExtra("investId");
        super.onCreate(arg0);
    }

    @Override
    protected void onResume() {
        //添加layout大小发生改变监听器
        activityRootView.addOnLayoutChangeListener(this);
        super.onResume();
    }

    @Override
    public void obtainData() {
        begin();
        HttpRequest.getRegTransDetail(this, investId, "N", new ICallback<RegTransDetailResult>() {
            @Override
            public void onSucceed(RegTransDetailResult result) {
                end();
                regTransDetail = result.getData();
                reRfeshView();
            }

            @Override
            public void onFail(String error) {
                end();
                Uihelper.showToast(LaunchTransferRegularAcitivity.this, error);
            }
        });
    }

    private void reRfeshView() {
        if (regTransDetail != null) {
            textLimit.setText(regTransDetail.getLimitCnt());
            textCapitalMoney.setText(regTransDetail.getRegAmt());
            textProject.setText(regTransDetail.getBdNm());
            textDateStart.setText("认购日期:" + regTransDetail.getCrtDt());
            textDateEnd.setText("结束日期:" + regTransDetail.getDueDt());
            textInterestRate.setText(regTransDetail.getRealInterest());
            textRepayment.setText(regTransDetail.getPayMeansName());
            textPrnTransSa.setText(regTransDetail.getPrnTransSa());
            textRemindDay.setText(regTransDetail.getLimitCnt());
        }

    }

    @Override
    public void initView() {

        textProject = (TextView) findViewById(R.id.text_project);
        textCapitalMoney = (TextView) findViewById(R.id.text_capital_money);
        textLimit = (TextView) findViewById(R.id.text_limit);
        textInterestRate = (TextView) findViewById(R.id.text_interest_rate);
        textDateStart = (TextView) findViewById(R.id.text_date_start);
        textDateEnd = (TextView) findViewById(R.id.text_date_end);
        textRepayment = (TextView) findViewById(R.id.text_repayment);
        textPrnTransSa = (TextView) findViewById(R.id.text_prnTransSa);
        textRemindDay = (TextView) findViewById(R.id.text_remindday);
        textChangeMoney = (TextView) findViewById(R.id.tv_changemoney);
        textDiscountRate = (TextView) findViewById(R.id.tv_discountrate);
        btnClick = (TextView) findViewById(R.id.btn_click);

        etMoney = (EditText) findViewById(R.id.et_money);
        et_TransferRate = (EditText) findViewById(R.id.et_transfer_rate);

        activityRootView = findViewById(R.id.layout_main);
        scrollview = (ScrollView)findViewById(R.id.scrollview);


        btnClick.setOnClickListener(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.user_launch_transfer;
    }

    @Override
    public void initTitle(WFYTitle mTitle) {
    }

    @Override
    protected String getPageName() {
        return "发起转让";
    }

    @Override
    public void onLayoutChange(View v, int left, int top, int right,
                               int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        //old是改变前的左上右下坐标点值，没有old的是改变后的左上右下坐标点值
        //现在认为只要控件将Activity向上推的高度超过了1/3屏幕高，就认为软键盘弹起
        if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > keyHeight)) {
            btnClick.postDelayed(new Runnable() {
                @Override
                public void run() {
                    scrollview.scrollTo(0,Uihelper.dip2px(mContext, 160));
                }
            },200);
        } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > keyHeight)) {
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_click:
                break;
            default:
                break;
        }

    }
}