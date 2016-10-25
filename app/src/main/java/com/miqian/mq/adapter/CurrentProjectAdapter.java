package com.miqian.mq.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.activity.CurrentDetailActivity;
import com.miqian.mq.entity.CurrentProjectInfo;
import com.miqian.mq.entity.RegularBase;
import com.miqian.mq.utils.FormatUtil;

import java.util.ArrayList;

/**
 * @author wangduo
 * @description: 活期项目列表数据适配器
 * @email: cswangduo@163.com
 * @date: 16/5/27
 */
public class CurrentProjectAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int ITEM_TYPE_CARD = 0; //
    private final int ITEM_TYPE_TITLE = 1; //
    private final int ITEM_TYPE_CONTENT = 2; //
    private final int ITEM_TYPE_FOOTER = 3; // 加载更多

    private ArrayList<CurrentProjectInfo> mList;
    private boolean isFinished;
    private Context mContext;

    public CurrentProjectAdapter(Context mContext) {
        this.mContext = mContext;
        mList = new ArrayList<>();
    }

    public void addAll(ArrayList<CurrentProjectInfo> list) {
        mList.addAll(list);
    }

    // 是否已经加载完所有数据
    public void hasLoadAllData(boolean isFinished) {
        this.isFinished = isFinished;
    }

    public void clear() {
        mList.clear();
    }

    private int size() {
        return (mList == null || mList.size() <= 0) ? 0 : mList.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case ITEM_TYPE_CARD:
                return new CurrentProjectCardHolder(LayoutInflater.from(mContext).inflate(R.layout.item_current_card, parent, false));
            case ITEM_TYPE_TITLE:
                return new CurrentProjectTitleHolder(LayoutInflater.from(mContext).inflate(R.layout.item_current_title, parent, false));
            case ITEM_TYPE_CONTENT:
                return new CurrentProjectContentHolder(LayoutInflater.from(mContext).inflate(R.layout.item_current_content, parent, false));
            case ITEM_TYPE_FOOTER:
                return new ProgressViewHolder(LayoutInflater.from(mContext).inflate(R.layout.adapter_loading, parent, false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CurrentProjectCardHolder) {
            CurrentProjectCardHolder viewHolder = (CurrentProjectCardHolder) holder;
            viewHolder.bindData(position);
        } else if (holder instanceof CurrentProjectContentHolder) {
            CurrentProjectContentHolder viewHolder = (CurrentProjectContentHolder) holder;
            viewHolder.bindData(position);
        } else if (holder instanceof ProgressViewHolder) {
            ProgressViewHolder viewHolder = (ProgressViewHolder) holder;
            viewHolder.showOrHide();
        }
    }

    @Override
    public int getItemCount() {
        return size() == 0 ? 0 : size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return ITEM_TYPE_CARD;
        } else if (position == 1) {
            return ITEM_TYPE_TITLE;
        } else if (position + 1 == getItemCount()) {
            return ITEM_TYPE_FOOTER;
        }
        return ITEM_TYPE_CONTENT;
    }

    class CurrentProjectCardHolder extends RecyclerView.ViewHolder {

        private View itemView;
        private TextView tv_name; // 项目名称
        private TextView tv_time; // 项目发售时间
        private TextView tv_profit_rate; // 年化收益
        private TextView tv_amount; // 认购金额
        private TextView tv_date_limit; // 项目期限
        private TextView tv_people; // 投资人数
        private Button btn_state; // 标的状态

        public CurrentProjectCardHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            initView();
        }

        private void initView() {
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            tv_profit_rate = (TextView) itemView.findViewById(R.id.tv_profit_rate);
            tv_amount = (TextView) itemView.findViewById(R.id.tv_amount);
            tv_date_limit = (TextView) itemView.findViewById(R.id.tv_date_limit);
            tv_people = (TextView) itemView.findViewById(R.id.tv_people);
            btn_state = (Button) itemView.findViewById(R.id.btn_state);
        }

        public void bindData(final int position) {
            final CurrentProjectInfo info = mList.get(position);
            switch (info.getSubjectStatus()) {
                case RegularBase.STATE_1:
                case RegularBase.STATE_2:
                case RegularBase.STATE_3:
                case RegularBase.STATE_4:
                    tv_time.setTextColor(mContext.getResources().getColor(R.color.mq_bl3_v2));
                    btn_state.setText("待开标");
                    btn_state.setBackgroundColor(mContext.getResources().getColor(R.color.white));
                    break;
                case RegularBase.STATE_5:
                    tv_time.setTextColor(mContext.getResources().getColor(R.color.mq_r1_v2));
                    btn_state.setText("立即认购");
                    btn_state.setBackgroundColor(mContext.getResources().getColor(R.color.mq_r1_v2));
                    break;
                default:
                    tv_time.setTextColor(mContext.getResources().getColor(R.color.mq_b5_v2));
                    btn_state.setText("已满额");
                    btn_state.setBackgroundColor(mContext.getResources().getColor(R.color.mq_b5_v2));
                    break;
            }
            tv_name.setText(info.getSubjectName());
            tv_time.setText(FormatUtil.formatDate(info.getStartTimestamp(), "MM月dd日 HH:mm发售"));
            tv_profit_rate.setText(info.getYearInterest());
            tv_amount.setText(FormatUtil.formatAmount(info.getSubjectTotalPrice()));
            tv_date_limit.setText(info.getLimit());
            tv_people.setText(FormatUtil.formatAmountStr(info.getPersonTime()));
            btn_state.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CurrentDetailActivity.startActivity(
                            mContext, info.getSubjectId());
                }
            });
        }

    }

    class CurrentProjectTitleHolder extends RecyclerView.ViewHolder {

        public CurrentProjectTitleHolder(View itemView) {
            super(itemView);
        }

    }

    class CurrentProjectContentHolder extends RecyclerView.ViewHolder {

        private View itemView;
        private TextView tv_name; // 项目名称
        private TextView tv_amount; // 认购金额
        private TextView tv_date_limit; // 项目期限
        private TextView tv_people; // 投资人数
        private View divider; // 分割线

        public CurrentProjectContentHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            initView();
        }

        private void initView() {
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_amount = (TextView) itemView.findViewById(R.id.tv_amount);
            tv_date_limit = (TextView) itemView.findViewById(R.id.tv_date_limit);
            tv_people = (TextView) itemView.findViewById(R.id.tv_people);
            divider = itemView.findViewById(R.id.divider);
        }

        public void bindData(final int position) {
            final CurrentProjectInfo info = mList.get(position);
            tv_name.setText(info.getSubjectName());
            tv_amount.setText(FormatUtil.formatAmount(info.getSubjectTotalPrice()));
            tv_date_limit.setText(FormatUtil.formatAmountStr(info.getLimit()));
            tv_people.setText(info.getPersonTime());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CurrentDetailActivity.startActivity(
                            mContext, info.getSubjectId());
                }
            });
        }

    }

    private class ProgressViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout frameLoad;

        public ProgressViewHolder(View view) {
            super(view);
            frameLoad = (LinearLayout) view.findViewById(R.id.frame_load);
        }

        public void showOrHide() {
            frameLoad.setVisibility(
                    isFinished ? View.GONE : View.VISIBLE);
        }

    }

}
