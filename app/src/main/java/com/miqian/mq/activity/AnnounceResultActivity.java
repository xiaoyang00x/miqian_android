package com.miqian.mq.activity;

import android.content.Intent;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;


import com.miqian.mq.R;
import com.miqian.mq.database.MyDataBaseHelper;
import com.miqian.mq.entity.JpushInfo;
import com.miqian.mq.utils.Pref;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.utils.UserUtil;
import com.miqian.mq.views.WFYTitle;

import java.util.List;

public class AnnounceResultActivity extends BaseActivity {

	private String classid;
	private String noticeId;
	private TextView tv_title;
	private TextView tv_content;
	private TextView tv_time;
	private View linear_noresult;
	private String userId;

	@Override
	public void obtainData() {

//		if (!TextUtils.isEmpty(classid)) {
//			mWaitingDialog.show();
//			HttpRequest.notice_detail(mActivity, new ICallback<AnnounceDetailResult>() {
//
//				@Override
//				public void onSucceed(AnnounceDetailResult result) {
//					mWaitingDialog.dismiss();
//					AnnouceDetailInfo detailInfo = result.getResult();
//					setData(detailInfo);
//				}
//
//				@Override
//				public void onFail(String error) {
//					mWaitingDialog.dismiss();
//					linear_noresult.setVisibility(View.GONE);
//					mView_noresult.setVisibility(View.VISIBLE);
//					UIhelper.trace(error);
//				}
//			}, classid);
//		}

	}

//	protected void setData(AnnouceDetailInfo detailInfo) {
//
//		if (!TextUtils.isEmpty(detailInfo.getName())) {
//			tv_title.setText(detailInfo.getName());
//		}
//		String content = detailInfo.getContent();
//		if (!TextUtils.isEmpty(content)) {
//
//			Spanned fromHtml = Html.fromHtml(content);
//			tv_content.setText(fromHtml);
//		}
//		if (!TextUtils.isEmpty(detailInfo.getPublish())) {
//			tv_time.setText(detailInfo.getPublish());
//		}
//	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		this.setIntent(intent);
		initView();
		obtainData();

	}

	@Override
	public void initView() {
		
		
		Intent intent = getIntent();
		classid = intent.getStringExtra("classid");
		noticeId = intent.getStringExtra("noticeId");
		// 是否登录
		if (!UserUtil.hasLogin(mActivity)) {
			userId = Pref.VISITOR;
		} else {
			userId = Pref.getString(Pref.USERID, mActivity, Pref.VISITOR);
		}
		List<JpushInfo> mList = MyDataBaseHelper.getInstance(mActivity).getjpushInfo(userId);
		for (JpushInfo jpushInfo : mList) {
			if (jpushInfo.getId().equals(noticeId)) {
				jpushInfo.setId(noticeId);
				if (jpushInfo.getState().equals("1")) {
					Uihelper.getMessageCount(-1, mActivity);
					jpushInfo.setState("2");
					MyDataBaseHelper.getInstance(mActivity).recordJpush(jpushInfo);
					break;
				}
			}
		}

		Uihelper.trace("", "xgmessage:classid" + intent.getStringExtra("classid") + noticeId);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_content = (TextView) findViewById(R.id.tv_content);
		tv_time = (TextView) findViewById(R.id.tv_time);
		linear_noresult = findViewById(R.id.linear_noresult);

	}

	@Override
	public int getLayoutId() {
		return R.layout.activity_announce_detail;
	}

	@Override
	public void initTitle(WFYTitle mTitle) {
		mTitle.setTitleText("消息详情");

	}

}
