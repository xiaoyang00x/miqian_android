package com.miqian.mq.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.miqian.mq.R;
import com.miqian.mq.entity.RegularBase;
import com.miqian.mq.entity.RegularDetailResult;
import com.miqian.mq.entity.RegularProjectFeature;
import com.miqian.mq.entity.RegularProjectInfo;
import com.miqian.mq.entity.RegularProjectMatch;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.utils.Constants;
import com.miqian.mq.utils.FormatUtil;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.utils.UserUtil;
import com.miqian.mq.views.WFYTitle;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * @author wangduo
 * @description: 定期计划详情
 * @email: cswangduo@163.com
 * @date: 16/10/21
 */
public class RegularPlanDetailActivity extends RegularDetailActivity {

    public static void startActivity(Context context, String subjectId) {
        Intent intent = new Intent(context, RegularPlanDetailActivity.class);
        intent.putExtra(Constants.SUBJECTID, subjectId);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle arg0) {
        prodId = RegularBase.REGULAR_PLAN;
        super.onCreate(arg0);
    }

    @Override
    public void initTitle(WFYTitle mTitle) {
        mTitle.setTitleText("定期计划");
    }

    @Override
    public String getPageName() {
        return "定期计划详情";
    }

    /**
     * 标的更多信息
     */
    @Override
    public void updateMoreInfo() {
        ArrayList<RegularProjectMatch> mList = mInfo.getMatchItem();

        if (null == viewDetail) {
            viewstub_detail.setLayoutResource(R.layout.regular_plan_detail);
            viewDetail = viewstub_detail.inflate();
        }
        LinearLayout content = (LinearLayout) viewDetail.findViewById(R.id.llyt_content);
        TextView tv_seemore = (TextView) viewDetail.findViewById(R.id.tv_seemore);
        tv_seemore.setOnClickListener(mOnclickListener);

        content.removeAllViews();

        if (mList == null || mList.size() <= 0) {
            viewDetail.findViewById(R.id.llyt1).setVisibility(View.GONE);
            return;
        }

        LayoutInflater mInflater = LayoutInflater.from(getBaseContext());
        // 匹配项目(默认展示三条)。如不足三条，则有几条展示几条
        int count = mList.size() > 3 ? 3 : mList.size();
        for (int index = 0; index < count; index++) {
            RegularProjectMatch projectMatch = mList.get(index);
            View mView = mInflater.inflate(R.layout.item_project_detail, null);
            ((TextView) mView.findViewById(R.id.tv_left)).setText(projectMatch.getName());
            ((TextView) mView.findViewById(R.id.tv_right)).setText(
                    new StringBuilder("金额:￥")
                            .append(FormatUtil.formatAmount(projectMatch.getOccupyAmount()))
                            .append("元"));
            content.addView(mView);
        }
    }

}
