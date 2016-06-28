package com.miqian.mq.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.database.MyDataBaseHelper;
import com.miqian.mq.entity.JpushInfo;
import com.miqian.mq.entity.MessageInfo;
import com.miqian.mq.entity.MessageInfoResult;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.utils.ExtendOperationController;
import com.miqian.mq.utils.Pref;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.views.WFYTitle;

public class AnnounceResultActivity extends BaseActivity {

    private String messsageId = "0";
    private TextView tv_title;
    private TextView tv_content;
    private TextView tv_time;
    private boolean isMessage;
    private int position;
    private MessageInfo detailInfo = new MessageInfo();

    @Override
    public void onCreate(Bundle arg0) {
        Intent intent = getIntent();
        messsageId = intent.getStringExtra("id");
        position = intent.getIntExtra("position", 0);
        isMessage = intent.getBooleanExtra("isMessage", true);
        super.onCreate(arg0);
    }

    @Override
    public void obtainData() {

        boolean isNative = Pref.getBoolean(Pref.FROM_NATIVE + messsageId, this, false);
        if (isNative) {
            JpushInfo jpushInfo = MyDataBaseHelper.getInstance(this).getJpushInfo(messsageId);
            detailInfo.setTitle(jpushInfo.getTitle());
            detailInfo.setContent(jpushInfo.getContent());
            detailInfo.setSendTime(Long.parseLong(jpushInfo.getTime()));
            showContentView();
            setData(detailInfo);
            Pref.saveBoolean(Pref.FROM_NATIVE + jpushInfo.getId(), false, this);
            return;
        }

        begin();
        HttpRequest.getPushDetail(mActivity, new ICallback<MessageInfoResult>() {
            @Override
            public void onSucceed(MessageInfoResult result) {
                end();
                detailInfo = result.getData();
                if (detailInfo != null) {
                    showContentView();
                    setData(detailInfo);
                } else {
                    showEmptyView();
                }
                //更新消息列表
                ExtendOperationController.getInstance().doNotificationExtendOperation(ExtendOperationController.OperationKey.RefeshMessage, position);
            }

            @Override
            public void onFail(String error) {
                end();
                showErrorView();
                Uihelper.showToast(mActivity, error);
            }
        }, isMessage, messsageId + "");
    }

    protected void setData(MessageInfo detailInfo) {

        findViewById(R.id.linear_content).setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(detailInfo.getTitle())) {
            tv_title.setText(detailInfo.getTitle());
        }
        String content = detailInfo.getContent();
        if (!TextUtils.isEmpty(content)) {
            Spanned fromHtml = Html.fromHtml(content);
            tv_content.setText("      " + fromHtml);
        }
        tv_time.setText(Uihelper.timestampToDateStr_other(detailInfo.getSendTime()));

        if (!isMessage){
            boolean isReaded = Pref.getBoolean(Pref.PUSH + detailInfo.getId(), mActivity, false);
            if (!isReaded) {
                Pref.saveBoolean(Pref.PUSH + detailInfo.getId(), true, mActivity);
            }
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

        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_content = (TextView) findViewById(R.id.tv_content);
        tv_time = (TextView) findViewById(R.id.tv_time);

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_announce_detail;
    }

    @Override
    public void initTitle(WFYTitle mTitle) {
        if (isMessage) {
            mTitle.setTitleText("消息详情");
        } else {
            mTitle.setTitleText("公告详情");
        }

    }

    @Override
    protected String getPageName() {
        if (isMessage) {
            return "消息详情";
        } else {
            return "公告详情";
        }
    }
}
