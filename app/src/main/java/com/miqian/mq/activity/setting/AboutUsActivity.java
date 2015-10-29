package com.miqian.mq.activity.setting;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
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
		findViewById(R.id.tv_telephone).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:4006656191")));
			}
		});

	}

	@Override
	public int getLayoutId() {
		return R.layout.activity_aboutus;
	}

	@Override
	public void initTitle(WFYTitle mTitle) {

		mTitle.setTitleText("关于秒钱");

	}

	@Override
	protected String getPageName() {
		return "关于秒钱";
	}
}
