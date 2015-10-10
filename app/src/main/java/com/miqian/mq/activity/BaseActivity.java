package com.miqian.mq.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.miqian.mq.R;
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
    public View mViewnoresult;
    public Dialog mWaitingDialog;
    public ImageLoader imageLoader;
    public DisplayImageOptions options;

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
        mViewnoresult = findViewById(R.id.frame_no_data);
        mWaitingDialog = ProgressDialogView.create(mActivity);
        initTitle(mTitle);
        initView();
        obtainData();
    }

    //获得数据
    public abstract void obtainData();

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
}
