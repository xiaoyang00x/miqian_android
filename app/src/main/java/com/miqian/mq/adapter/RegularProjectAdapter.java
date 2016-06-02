package com.miqian.mq.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.activity.WebActivity;
import com.miqian.mq.entity.ProjectInfo;
import com.miqian.mq.entity.RegularBase;
import com.miqian.mq.entity.RegularProjectData;
import com.miqian.mq.entity.RegularProjectHeader;
import com.miqian.mq.entity.RegularProjectInfo;
import com.miqian.mq.entity.RegularProjectList;
import com.miqian.mq.entity.RegularTransferInfo;
import com.miqian.mq.utils.FormatUtil;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * @author wangduo
 * @description: 定期项目列表数据适配器
 * @email: cswangduo@163.com
 * @date: 16/5/27
 */
public class RegularProjectAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<RegularBase> mList; // 所有item
    private Context mContext;

    public RegularProjectAdapter(Context mContext) {
        this.mContext = mContext;
        mList = new ArrayList<>();
    }

    public void addAll(RegularProjectList mData) {
        // 定期首页 最上面的card 目前版本最多只有一个 可能没有
        if (null != mData.getFeatureData()) {
            RegularProjectInfo info = mData.getFeatureData().get(0);
            if (null != info) {
                info.setType(RegularBase.ITEM_TYPE_CARD);
                mList.add(info);
            }
        }

        if (null != mData.getRegularData()) {
            for (RegularProjectData regularProjectData : mData.getRegularData()) {
                if (null != regularProjectData.getSubjectData() && regularProjectData.getSubjectData().size() > 0) {

                    RegularProjectHeader regularProjectHeader = new RegularProjectHeader();
                    regularProjectHeader.setType(RegularBase.ITEM_TYPE_TITLE);
                    regularProjectHeader.setName(regularProjectData.getName());
                    regularProjectHeader.setTitle(regularProjectData.getTitle());
                    regularProjectHeader.setJumpUrl(regularProjectData.getJumpUrl());
                    mList.add(regularProjectHeader);

                    int size = regularProjectData.getSubjectData().size();
                    for (int index = 0; index < size; index++) {
                        RegularProjectInfo info = regularProjectData.getSubjectData().get(index);
                        info.setType(RegularBase.ITEM_TYPE_LIST);
                        mList.add(info);
                    }
                }
            }
        }
    }

    public void clear() {
        mList.clear();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case RegularBase.ITEM_TYPE_CARD:
                return new RegularCardHolder(LayoutInflater.from(mContext).inflate(R.layout.item_regular_card, parent, false));
            case RegularBase.ITEM_TYPE_TITLE:
                return new RegularTitleHolder(LayoutInflater.from(mContext).inflate(R.layout.item_regular_title, parent, false));
            case RegularBase.ITEM_TYPE_LIST:
                return new RegularListHolder(LayoutInflater.from(mContext).inflate(R.layout.item_regular_content, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof RegularCardHolder) {
            RegularCardHolder viewHolder = (RegularCardHolder) holder;
            viewHolder.bindData(position);
        } else if (holder instanceof RegularTitleHolder) {
            RegularTitleHolder viewHolder = (RegularTitleHolder) holder;
            viewHolder.bindData(position);
        } else if (holder instanceof RegularListHolder) {
            RegularListHolder viewHolder = (RegularListHolder) holder;
            viewHolder.bindData(position);
        }
    }

    @Override
    public int getItemCount() {
        if (null == mList || mList.size() <= 0) {
            return 0;
        }
        return mList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mList.get(position).getType();
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    private class RegularCardHolder extends RecyclerView.ViewHolder {

        private TextView tv_name; // 名称
        private TextView tv_profit_rate; // 年利率
        private TextView tv_profit_rate_unit; // 年利率单位:%, 有加息的话如:+0.5%
        private TextView tv_time_limit; // 期限
        private TextView tv_remain_amount; // 剩余可购金额
        private Button btn_buy;

        public RegularCardHolder(View itemView) {
            super(itemView);
            initView(itemView);
        }

        private void initView(View itemView) {
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_profit_rate = (TextView) itemView.findViewById(R.id.tv_profit_rate);
            tv_profit_rate_unit = (TextView) itemView.findViewById(R.id.tv_profit_rate_unit);
            tv_time_limit = (TextView) itemView.findViewById(R.id.tv_time_limit);
            tv_remain_amount = (TextView) itemView.findViewById(R.id.tv_remain_amount);
            btn_buy = (Button) itemView.findViewById(R.id.btn_buy);
        }

        public void bindData(final int position) {
            RegularProjectInfo info = (RegularProjectInfo) mList.get(position);
            tv_name.setText(info.getSubjectName());
            tv_profit_rate.setText(info.getYearInterest());
            tv_time_limit.setText(info.getLimit());
            tv_remain_amount.setText(
                    new StringBuilder("剩余金额:￥").
                            append(FormatUtil.formatAmount(info.getResidueAmt())).
                            append("/").
                            append(FormatUtil.formatAmount(info.getSubjectTotalPrice()))
                            .toString());

            if (info.getPresentationYesNo().equals("N")) {
                tv_profit_rate_unit.setText("%");
            } else {
                tv_profit_rate_unit.setText(
                        new StringBuilder("+").
                                append(info.getPresentationYearInterest()).
                                append("%").toString());
            }

            btn_buy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

    }

    private class RegularTitleHolder extends RecyclerView.ViewHolder {

        private TextView tv_name; // 栏目名称
        private TextView tv_description; // 栏目描述

        public RegularTitleHolder(View itemView) {
            super(itemView);
            initView(itemView);
        }

        private void initView(View itemView) {
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_description = (TextView) itemView.findViewById(R.id.tv_description);
        }

        public void bindData(final int position) {
            final RegularProjectHeader info = (RegularProjectHeader) mList.get(position);
            tv_name.setText(info.getTitle());
            if (!TextUtils.isEmpty(info.getName())) {
                if (!TextUtils.isEmpty(info.getJumpUrl())) {
                    tv_description.setText(new StringBuilder(info.getName()).append("    >").toString());
                    tv_description.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            WebActivity.startActivity(mContext, info.getJumpUrl());
                        }
                    });
                } else {
                    tv_description.setText(new StringBuilder(info.getName()).append("  ").toString());
                }
            } else {
                tv_description.setText("");
            }
        }

    }

    private class RegularListHolder extends RecyclerView.ViewHolder {

        private TextView tv_name; // 名称
        private TextView tv_profit_rate; // 年利率
        private TextView tv_profit_rate_unit; // 年利率单位:%, 有加息的话如:+0.5%
        private TextView tv_time_limit; // 期限
        private TextView tv_time_limit_unit; // 期限单位:元
        private TextView tv_begin_time; // 开始时间
        private TextView tv_remain_amount; // 剩余可购金额
        private Button btn_state; // 立即购买(已售罄)按钮
        private View divider;

        public RegularListHolder(View itemView) {
            super(itemView);
            initView(itemView);
        }

        private void initView(View itemView) {
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_profit_rate = (TextView) itemView.findViewById(R.id.tv_profit_rate);
            tv_profit_rate_unit = (TextView) itemView.findViewById(R.id.tv_profit_rate_unit);
            tv_time_limit = (TextView) itemView.findViewById(R.id.tv_time_limit);
            tv_time_limit_unit = (TextView) itemView.findViewById(R.id.tv_time_limit_unit);
            tv_begin_time = (TextView) itemView.findViewById(R.id.tv_begin_time);
            tv_remain_amount = (TextView) itemView.findViewById(R.id.tv_remain_amount);
            btn_state = (Button) itemView.findViewById(R.id.btn_state);
            divider = itemView.findViewById(R.id.divider);
        }

        public void bindData(final int position) {
            final RegularProjectInfo info = (RegularProjectInfo) mList.get(position);
            tv_name.setText(info.getSubjectName());
            tv_profit_rate.setText(info.getYearInterest());
            tv_profit_rate_unit.setText("%");
            if (!"N".equals(info.getPresentationYesNo())) {
                tv_profit_rate_unit.setText(
                        new StringBuilder("+").
                                append(info.getPresentationYearInterest()).
                                append("%").toString());
            }
            tv_time_limit.setText(info.getLimit());
            tv_remain_amount.setText(
                    new StringBuilder("剩余金额:￥").
                            append(FormatUtil.formatAmount(info.getResidueAmt())).
                            append("/").
                            append(FormatUtil.formatAmount(info.getSubjectTotalPrice()))
                            .toString());
            if (position + 1 == getItemCount()) {
                divider.setVisibility(View.GONE);
            } else if (position + 1 < getItemCount() &&
                    mList.get(position + 1).getType() == RegularBase.ITEM_TYPE_TITLE) {
                divider.setVisibility(View.GONE);
            } else {
                divider.setVisibility(View.VISIBLE);
            }
            if (info.getSubjectStatus().equals(ProjectInfo.STATE_02)) {
                tv_profit_rate.setTextColor(mContext.getResources().getColor(R.color.mq_b5_v2));
                tv_profit_rate_unit.setTextColor(mContext.getResources().getColor(R.color.mq_b5_v2));
                tv_time_limit.setTextColor(mContext.getResources().getColor(R.color.mq_b5_v2));
                tv_time_limit_unit.setTextColor(mContext.getResources().getColor(R.color.mq_b5_v2));
                tv_begin_time.setVisibility(View.GONE);
                btn_state.setBackgroundResource(R.drawable.btn_has_done);
                btn_state.setText("已满额");
            } else if (info.getSubjectStatus().equals(ProjectInfo.STATE_01)) {
                tv_profit_rate.setTextColor(mContext.getResources().getColor(R.color.mq_r1_v2));
                tv_profit_rate_unit.setTextColor(mContext.getResources().getColor(R.color.mq_r1_v2));
                tv_time_limit.setTextColor(mContext.getResources().getColor(R.color.mq_r1_v2));
                tv_time_limit_unit.setTextColor(mContext.getResources().getColor(R.color.mq_r1_v2));
                tv_begin_time.setVisibility(View.GONE);
                btn_state.setBackgroundResource(R.drawable.btn_default_selector);
                btn_state.setText("立即认购");
                btn_state.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnItemClickListener != null) {
                            mOnItemClickListener.onItemClick(position);
                        }
                    }
                });
            } else if (info.getSubjectStatus().equals(ProjectInfo.STATE_00)) {
                tv_profit_rate.setTextColor(mContext.getResources().getColor(R.color.mq_r1_v2));
                tv_profit_rate_unit.setTextColor(mContext.getResources().getColor(R.color.mq_r1_v2));
                tv_time_limit.setTextColor(mContext.getResources().getColor(R.color.mq_r1_v2));
                tv_time_limit_unit.setTextColor(mContext.getResources().getColor(R.color.mq_r1_v2));
                tv_begin_time.setText(info.getStartTimestamp());
                tv_begin_time.setVisibility(View.VISIBLE);
                btn_state.setBackgroundResource(R.drawable.btn_no_begin);
                btn_state.setText("待开标");
                btn_state.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnItemClickListener != null) {
                            mOnItemClickListener.onItemClick(position);
                        }
                    }
                });
            }
        }

    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

}
