package com.miqian.mq.activity;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.miqian.mq.R;
import com.miqian.mq.activity.BaseFragmentActivity;
import com.miqian.mq.views.WFYTitle;

/**
 * Created by Joy on 2015/9/1.
 */
public  abstract  class BaseActivity extends BaseFragmentActivity {
    private LinearLayout mContentView;
    private WFYTitle mTitle;
    private Activity mActivity;

    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_base);
        initCotentView();
        mActivity = this;
        mTitle = (WFYTitle) findViewById(R.id.wFYTitle);
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
}
