package com.miqian.mq.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.activity.RegularDetailActivity;
import com.miqian.mq.activity.WebActivity;
import com.miqian.mq.activity.current.ActivityCurrentRecord;
import com.miqian.mq.activity.current.ActivityRedeem;
import com.miqian.mq.activity.current.SubscriptionRecordsActivity;
import com.miqian.mq.entity.FundFlow;
import com.miqian.mq.entity.RegularBase;
import com.miqian.mq.entity.RegularProjectData;
import com.miqian.mq.entity.RegularProjectHeader;
import com.miqian.mq.entity.RegularProjectInfo;
import com.miqian.mq.entity.RegularProjectList;
import com.miqian.mq.entity.UserCurrentData;
import com.miqian.mq.net.Urls;
import com.miqian.mq.utils.FormatUtil;
import com.miqian.mq.utils.Uihelper;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * @author wgl
 * @description: 我的秒钱宝
 * @date: 16/10/24
 */
public class MyCurrentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int TYPE_HEAD = 0;
    public static final int TYPE_PROJECT = 1;

    private ArrayList<UserCurrentData.MatchProject> mList; // 所有item
    private UserCurrentData userCurrentData;
    private Context mContext;

    public MyCurrentAdapter(Context mContext, UserCurrentData userCurrentData) {
        this.mContext = mContext;
        this.userCurrentData = userCurrentData;
        mList = userCurrentData.getProjectList();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_HEAD:
                return new CurrentHeadHolder(LayoutInflater.from(mContext).inflate(R.layout.item_my_current_header, parent, false));
            default:
                return new CurrentListHolder(LayoutInflater.from(mContext).inflate(R.layout.item_my_current_match, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CurrentHeadHolder) {
            ((CurrentHeadHolder) holder).bindData(userCurrentData.getUserCurrent());
        } else if (holder instanceof CurrentListHolder) {
            ((CurrentListHolder) holder).bindData(mList.get(position - 1));
        }
    }

    @Override
    public int getItemCount() {
        if (null == mList || mList.size() <= 0) {

            //包含head
            return 1;
        }
        return mList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0? TYPE_HEAD : TYPE_PROJECT;
    }


    private class CurrentHeadHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView textEarning;                   //昨日收益
        private TextView textCaptial;                   //在投本金
        private TextView textTotalEarning;              //总收益
        private TextView textInterest;                  //总收益
        private RelativeLayout frameCurrentRecord;      //资金记录
        private RelativeLayout frameProjectMatch;       //匹配项目

        private ImageLoader imageLoader;
        private DisplayImageOptions options;

        public CurrentHeadHolder(View itemView) {
            super(itemView);
            initView(itemView);
            imageLoader = ImageLoader.getInstance();
            options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).build();
        }

        private void initView(View itemView) {
            textEarning = (TextView) itemView.findViewById(R.id.earning);
            textCaptial = (TextView) itemView.findViewById(R.id.captial);
            textTotalEarning = (TextView) itemView.findViewById(R.id.total_earning);
            textInterest = (TextView) itemView.findViewById(R.id.text_interest);

            frameCurrentRecord = (RelativeLayout) itemView.findViewById(R.id.frame_current_record);
            frameProjectMatch = (RelativeLayout) itemView.findViewById(R.id.frame_project_match);

            frameCurrentRecord.setOnClickListener(this);
        }

        public void bindData(UserCurrentData.UserCurrent userCurrent) {
            if(userCurrent == null) return;
            if(userCurrent != null) {
                textEarning.setText(FormatUtil.formatAmount(userCurrent.getYesterdayInterest()));
                textCaptial.setText(FormatUtil.formatAmount(userCurrent.getPrnAmt()));
                textTotalEarning.setText(FormatUtil.formatAmount(userCurrent.getRegIncome()));

                String tempInterest = userCurrent.getYearInterest().toString();
                if (TextUtils.isEmpty(tempInterest)) {
                    textInterest.setVisibility(View.GONE);
                } else {
                    textInterest.setVisibility(View.VISIBLE);
                    textInterest.setText(tempInterest + "%");
                }
            }
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.frame_current_record:
                    MobclickAgent.onEvent(v.getContext(), "1035");
                    ActivityCurrentRecord.startActivity(v.getContext(), FundFlow.BILL_TYPE_ALL, "3");
                    break;
                default:
                    break;
            }
        }

    }

    private class CurrentListHolder extends RecyclerView.ViewHolder {

        private View itemView;
        private TextView tv_name;                               // 名称
        private TextView tv_principal;                          // 本金
        private TextView tv_collect_earnings;                   // 待收收益
        private TextView tv_next_due_date;                      // 下一还款日
        private TextView tv_view_notes;                         // 查看认购记录

        public CurrentListHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            initView();
        }

        private void initView() {
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_principal = (TextView) itemView.findViewById(R.id.tv_principal);
            tv_collect_earnings = (TextView) itemView.findViewById(R.id.tv_collect_earnings);
            tv_next_due_date = (TextView) itemView.findViewById(R.id.tv_next_due_date);
            tv_view_notes = (TextView) itemView.findViewById(R.id.tv_view_notes);
        }

        public void bindData(final UserCurrentData.MatchProject info) {
            if(info == null) return;
            tv_name.setText(info.getProjectName());
            tv_principal.setText(FormatUtil.formatAmount(info.getRemainAmount()));
            tv_collect_earnings.setText(FormatUtil.formatAmount(info.getRemainInterest()));
            tv_next_due_date.setText(TextUtils.concat("下期还款日:", FormatUtil.formatDate(info.getNextRepayDate(), "yyyy年MM月dd日")));
            tv_view_notes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SubscriptionRecordsActivity.startActivity(v.getContext(), info.getProjectCode());
                }
            });
        }

    }

}
