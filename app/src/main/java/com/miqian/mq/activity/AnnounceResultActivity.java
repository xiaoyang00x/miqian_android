package com.miqian.mq.activity;

import android.content.Intent;
import android.os.Bundle;
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
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.utils.ExtendOperationController;
import com.miqian.mq.utils.Pref;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.utils.UserUtil;
import com.miqian.mq.views.WFYTitle;

import java.util.List;

public class AnnounceResultActivity extends BaseActivity {

    private int messsageId;
    private TextView tv_title;
    private TextView tv_content;
    private TextView tv_time;
    private boolean isMessage;
    private int position;

    @Override
    public void onCreate(Bundle arg0) {
        Intent intent = getIntent();
        messsageId = intent.getIntExtra("id", 0);
        position = intent.getIntExtra("position", 0);
        isMessage = intent.getBooleanExtra("isMessage", true);
        super.onCreate(arg0);

    }

    @Override
    public void obtainData() {
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
        if (!TextUtils.isEmpty(detailInfo.getTitle())) {
            tv_title.setText(detailInfo.getTitle());
        }
        String content = detailInfo.getContent();
        if (!TextUtils.isEmpty(content)) {

            Spanned fromHtml = Html.fromHtml(content);
            tv_content.setText("      " + fromHtml);
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
