package com.miqian.mq.activity.setting;

import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.activity.BaseActivity;
import com.miqian.mq.utils.MobileOS;
import com.miqian.mq.views.WFYTitle;


public class AboutUsActivity extends BaseActivity {
	private TextView tvVersion;

	@Override
	public void obtainData() {
		tvVersion.setText("版本号" + MobileOS.getAppVersionName(this));
	}

	@Override
	public void initView() {
		tvVersion = (TextView) findViewById(R.id.tvVersion);

	}

	@Override
	public int getLayoutId() {
		return R.layout.activity_aboutus;
	}

	@Override
	public void initTitle(WFYTitle mTitle) {

		mTitle.setTitleText("关于秒钱");

	}

}
