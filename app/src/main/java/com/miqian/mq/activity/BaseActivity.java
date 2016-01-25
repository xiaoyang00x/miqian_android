package com.miqian.mq.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.miqian.mq.MyApplication;
import com.miqian.mq.R;
import com.miqian.mq.utils.ActivityStack;
import com.miqian.mq.utils.MobileOS;
import com.miqian.mq.views.ProgressDialogView;
import com.miqian.mq.views.WFYTitle;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

/**
 * Created by Joy on 2015/9/1.
 */
public abstract class BaseActivity extends BaseFragmentActivity {

    public LinearLayout mContentView;
    public WFYTitle mTitle;
    public Activity mActivity;
    public LinearLayout mViewnoresult;
    public Dialog mWaitingDialog;
    public ImageLoader imageLoader;
    public DisplayImageOptions options;
    private TextView tvTips;
    private ImageView ivData;

    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_base);
        initCotentView();
        mActivity = this;
        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).displayer(new RoundedBitmapDisplayer(0)).build();
        mTitle = (WFYTitle) findViewById(R.id.wFYTitle);
        mWaitingDialog = ProgressDialogView.create(mActivity);

        mViewnoresult = (LinearLayout) findViewById(R.id.frame_no_data);

        findViewById(R.id.tv_reFresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obtainData();
            }
        });
        tvTips = (TextView) findViewById(R.id.tv_tips);
        ivData = (ImageView) findViewById(R.id.iv_data);

        getmTitle().setLeftImage(R.drawable.btn_back_selector);
        getmTitle().setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        initTitle(mTitle);
        initView();
        obtainData();

        //增加栈管理器
        ActivityStack.getActivityStack().pushActivity(this);
        //设置不在主页
        MyApplication.getInstance().setIsOnMainAcitivity(false);
    }

    //获得数据
    public abstract void obtainData(

    );

    public abstract void initView();

    public abstract int getLayoutId();

    public abstract void initTitle(WFYTitle mTitle);

    private void initCotentView() {
        mContentView = (LinearLayout) findViewById(R.id.content);
        if (getLayoutId() == 0) {
            return;
        }
        View contentView = LayoutInflater.from(this).inflate(getLayoutId(), null);
        LinearLayout.LayoutParams lpContent = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        contentView.setLayoutParams(lpContent);
        mContentView.addView(contentView);
    }

    public WFYTitle getmTitle() {
        return mTitle;
    }

    @Override
    protected void onDestroy() {
        //推出Activity
        ActivityStack.getActivityStack().popActivity(this);
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * 显示 loading 对话框
     */
    protected void begin() {
        if (mWaitingDialog != null) {
            mWaitingDialog.show();
        }
    }

    /**
     * 显示 loading 对话框
     */
    protected void end() {
        if (mWaitingDialog != null && mWaitingDialog.isShowing()) {
            mWaitingDialog.dismiss();
        }
    }

    /**
     * 无数据
     */
    protected void showEmptyView() {
        mContentView.setVisibility(View.GONE);
        mViewnoresult.setVisibility(View.VISIBLE);
        findViewById(R.id.tv_reFresh).setVisibility(View.GONE);
        tvTips.setText("暂时没有数据");
        ivData.setBackgroundResource(R.drawable.nodata);
    }

    /**
     * 显示数据
     */
    protected void showContentView() {
        mContentView.setVisibility(View.VISIBLE);
        mViewnoresult.setVisibility(View.GONE);

    }

    /**
     * 获取失败，请重新获取
     */
    protected void showErrorView() {

        mContentView.setVisibility(View.GONE);
        mViewnoresult.setVisibility(View.VISIBLE);
        if (MobileOS.getNetworkType(mContext) == -1) {
            tvTips.setText("暂时没有网络");
            ivData.setBackgroundResource(R.drawable.nonetwork);
        }else {
            tvTips.setText("数据获取失败，请重新获取");
            ivData.setBackgroundResource(R.drawable.error_data);
        }
    }



}
