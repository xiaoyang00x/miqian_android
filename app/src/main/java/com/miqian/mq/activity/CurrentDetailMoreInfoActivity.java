package com.miqian.mq.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.entity.CurrentDetailMoreInfoItem;
import com.miqian.mq.views.WFYTitle;

import java.util.ArrayList;

/**
 * @author wangduo
 * @description: 秒钱宝详情 更多信息页面
 * @email: cswangduo@163.com
 * @date: 16/10/24
 */
public class CurrentDetailMoreInfoActivity extends BaseActivity {

    private LinearLayout llyt_content;

    private static ArrayList<CurrentDetailMoreInfoItem> mList;

    public static void startActivity(Context context, ArrayList<CurrentDetailMoreInfoItem> list) {
        Intent intent = new Intent(context, CurrentDetailMoreInfoActivity.class);
        mList = list;
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public void obtainData() {
        if (null == mList || mList.size() <= 0) {
            finish();
            return;
        }
        int size = mList.size();
        LayoutInflater mInflater = LayoutInflater.from(getBaseContext());
        for (int index = 0; index < size; index++) {
            CurrentDetailMoreInfoItem item = mList.get(index);
            View mView = mInflater.inflate(R.layout.item_project_detail, null);
            ((TextView) mView.findViewById(R.id.tv_left)).setText(item.getTitle());
            if (!TextUtils.isEmpty(item.getContent())) {
                ((TextView) mView.findViewById(R.id.tv_right)).setText(item.getContent());
            }
            llyt_content.addView(mView);
        }
    }

    @Override
    public void initView() {
        llyt_content = (LinearLayout) findViewById(R.id.llyt_content);
    }

    @Override
    public int getLayoutId() {
        return R.layout.current_project_detail_more;
    }

    @Override
    public void initTitle(WFYTitle mTitle) {
        mTitle.setTitleText("更多信息");
    }

    @Override
    protected String getPageName() {
        return null;
    }

}
