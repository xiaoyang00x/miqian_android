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
import com.miqian.mq.entity.MessageInfo;
import com.miqian.mq.entity.MessageInfoResult;
import com.miqian.mq.entity.UserInfo;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.utils.Pref;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.utils.UserUtil;
import com.miqian.mq.views.WFYTitle;

import java.util.List;

public class AnnounceResultActivity extends BaseActivity {

    private String noticeId;
    private TextView tv_title;
    private TextView tv_content;
    private TextView tv_time;
    private View linear_noresult;
    private String userId;
    private String pushSource;

    @Override
    public void obtainData() {

        // 是否登录
        if (!UserUtil.hasLogin(mActivity)) {
            userId = "";
        } else {
            userId = Pref.getString(Pref.USERID, mActivity, Pref.VISITOR);
        }

        if (!TextUtils.isEmpty(noticeId)) {
            begin();
            HttpRequest.getPushDetail(mActivity, new ICallback<MessageInfoResult>() {

                @Override
                public void onSucceed(MessageInfoResult result) {
                    end();
                    MessageInfo detailInfo = result.getData();
                    if (detailInfo != null) {
                        showContentView();
                        setData(detailInfo);
                    } else {
                        showEmptyView();
                    }
                }

                @Override
                public void onFail(String error) {
                    end();
                    showErrorView();
                }
            }, Integer.valueOf(pushSource), noticeId, userId);
        }else {
            showEmptyView();
        }


    }

    protected void setData(MessageInfo detailInfo) {

        if (!TextUtils.isEmpty(detailInfo.getTitle())) {
            tv_title.setText(detailInfo.getTitle());
        }
        String content = detailInfo.getContent();
        if (!TextUtils.isEmpty(content)) {

            Spanned fromHtml = Html.fromHtml(content);
            tv_content.setText(fromHtml);
        }

        String sendTime = detailInfo.getSendTime();
        if (!TextUtils.isEmpty(sendTime)) {

            String time = Uihelper.timestampToDateStr_other(Double.parseDouble(sendTime));
            tv_time.setText(time);
        }
    }

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
        noticeId = intent.getStringExtra("id");
        pushSource = intent.getStringExtra("pushSource");
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

        Uihelper.trace("xgInfo", "classid==" + noticeId);
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

    @Override
    protected String getPageName() {
        return "消息详情";
    }
}
