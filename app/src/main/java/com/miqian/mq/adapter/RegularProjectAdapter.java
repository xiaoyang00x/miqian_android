package com.miqian.mq.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.activity.RegularDetailActivity;
import com.miqian.mq.activity.WebActivity;
import com.miqian.mq.entity.ProductRegularBaseInfo;
import com.miqian.mq.entity.RegularBase;
import com.miqian.mq.entity.RegularProjectData;
import com.miqian.mq.entity.RegularProjectList;
import com.miqian.mq.utils.Constants;
import com.miqian.mq.utils.FormatUtil;
import com.miqian.mq.utils.Uihelper;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * @author wangduo
 * @description: 定期项目列表数据适配器
 * @email: cswangduo@163.com
 * @date: 16/5/27
 */
public class RegularProjectAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<ProductRegularBaseInfo> mList; // 所有item
    private Context mContext;

    public RegularProjectAdapter(Context mContext) {
        this.mContext = mContext;
        mList = new ArrayList<>();
    }

    public void addAll(RegularProjectList mData) {
        // 定期首页 最上面的card 目前版本最多只有一个 可能没有
        if (null != mData.getRecommendProductDto()) {
            ProductRegularBaseInfo info = mData.getRecommendProductDto();
            info.setType(RegularBase.ITEM_TYPE_CARD);
            mList.add(info);
        }

        if (null == mData.getRegularDatas() || mData.getRegularDatas().size() <= 0) {
            return;
        }

        for (RegularProjectData regularProjectData : mData.getRegularDatas()) {
            if (regularProjectData.getSubjectDatas() == null || regularProjectData.getSubjectDatas().size() <= 0) {
                continue;
            }
            ProductRegularBaseInfo regularProjectHeader = new ProductRegularBaseInfo();
            regularProjectHeader.setType(RegularBase.ITEM_TYPE_TITLE);
            regularProjectHeader.setName(regularProjectData.getName());
            regularProjectHeader.setTitle(regularProjectData.getTitle());
            regularProjectHeader.setJumpUrl(regularProjectData.getJumpUrl());
            regularProjectHeader.setIconUrl(regularProjectData.getIconUrl());
            mList.add(regularProjectHeader);

            for (ProductRegularBaseInfo info : regularProjectData.getSubjectDatas()) {
                info.setType(RegularBase.ITEM_TYPE_LIST);
                mList.add(info);
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
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof RegularCardHolder) {
            ((RegularCardHolder) holder).bindData(position);
        } else if (holder instanceof RegularTitleHolder) {
            ((RegularTitleHolder) holder).bindData(position);
        } else if (holder instanceof RegularListHolder) {
            ((RegularListHolder) holder).bindData(position);
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

    private class RegularCardHolder extends RecyclerView.ViewHolder {

        private View itemView;
        private TextView tv_name; // 名称
        private ImageView iv_tag;
        private TextView tv_profit_rate; // 年利率
        private TextView tv_profit_rate_unit; // 年利率单位:%, 有加息的话如:+0.5%
        private TextView tv_time_limit; // 期限
        private TextView tv_remain_amount; // 剩余可购金额
        private Button btn_buy;

        public RegularCardHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            initView();
        }

        private void initView() {
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            iv_tag = (ImageView) itemView.findViewById(R.id.iv_tag);
            tv_profit_rate = (TextView) itemView.findViewById(R.id.tv_profit_rate);
            tv_profit_rate_unit = (TextView) itemView.findViewById(R.id.tv_profit_rate_unit);
            tv_time_limit = (TextView) itemView.findViewById(R.id.tv_time_limit);
            tv_remain_amount = (TextView) itemView.findViewById(R.id.tv_remain_amount);
            btn_buy = (Button) itemView.findViewById(R.id.btn_buy);
        }

        public void bindData(final int position) {
            final ProductRegularBaseInfo info = (ProductRegularBaseInfo) mList.get(position);
            tv_name.setText(info.getProductName());
            tv_profit_rate.setText(info.getProductRate());
            tv_profit_rate_unit.setText("%");
            tv_time_limit.setText(info.getProductTerm());
            tv_remain_amount.setText(
                    new StringBuilder("可认购金额:￥").
                            append(FormatUtil.formatAmount(info.getRemainAmount())).
                            append("/￥").
                            append(FormatUtil.formatAmount(info.getBidAmount())));
            if (Constants.BID_TYPE_SBB.equals(info.getBidType())) {
                tv_profit_rate.setText(info.getYearRate());
            } else if ((new BigDecimal(info.getAddInterestRate()).compareTo(BigDecimal.ZERO)) == 1) {
                tv_profit_rate_unit.setText(
                        new StringBuilder("+").
                                append(info.getAddInterestRate()).
                                append("%"));
            }
            if (info.getBidType().equals(Constants.BID_TYPE_SBB)) {
                iv_tag.setImageResource(R.drawable.double_rate_card_normal);
            } else if (info.getBidType().equals(Constants.BID_TYPE_SBSYK)) {
                iv_tag.setImageResource(R.drawable.double_card_normal);
            } else {
                iv_tag.setImageResource(0);
            }
            btn_buy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MobclickAgent.onEvent(mContext, "1010_1");
                    RegularDetailActivity.startActivity(
                            mContext, info.getProductCode(), info.getProductType());
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MobclickAgent.onEvent(mContext, "1010_1");
                    RegularDetailActivity.startActivity(
                            mContext, info.getProductCode(), info.getProductType());
                }
            });
        }

    }

    private class RegularTitleHolder extends RecyclerView.ViewHolder {

        private ImageView iv_left; // 栏目图标
        private TextView tv_name; // 栏目名称
        private TextView tv_description; // 栏目描述

        private ImageLoader imageLoader;
        private DisplayImageOptions options;

        public RegularTitleHolder(View itemView) {
            super(itemView);
            initView(itemView);
            imageLoader = ImageLoader.getInstance();
            options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).build();
        }

        private void initView(View itemView) {
            iv_left = (ImageView) itemView.findViewById(R.id.iv_left);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_description = (TextView) itemView.findViewById(R.id.tv_description);
        }

        public void bindData(final int position) {
            final ProductRegularBaseInfo info = (ProductRegularBaseInfo) mList.get(position);
            tv_name.setText(info.getTitle());
            if (!TextUtils.isEmpty(info.getName())) {
                if (!TextUtils.isEmpty(info.getJumpUrl())) {
                    tv_description.setText(new StringBuilder(info.getName()).append("  >"));
                    tv_description.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            WebActivity.startActivity(mContext, info.getJumpUrl());
                        }
                    });
                } else {
                    tv_description.setText(new StringBuilder(info.getName()).append(" "));
                }
            } else {
                tv_description.setText("");
            }
            if (!TextUtils.isEmpty(info.getIconUrl())) {
                imageLoader.displayImage(info.getIconUrl(), iv_left, options);
            }
        }

    }

    private class RegularListHolder extends RecyclerView.ViewHolder {

        private View itemView;
        private TextView tv_name; // 名称
        private ImageView iv_tag;
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
            this.itemView = itemView;
            initView();
        }

        private void initView() {
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            iv_tag = (ImageView) itemView.findViewById(R.id.iv_tag);
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
            final ProductRegularBaseInfo info = (ProductRegularBaseInfo) mList.get(position);
            tv_name.setText(info.getProductName());
            tv_profit_rate.setText(info.getProductRate());
            tv_profit_rate_unit.setText("%");

            if (Constants.BID_TYPE_SBB.equals(info.getBidType())) {
                tv_profit_rate.setText(info.getYearRate());
            } else if ((new BigDecimal(info.getAddInterestRate()).compareTo(BigDecimal.ZERO)) == 1) {
                tv_profit_rate_unit.setText(
                        new StringBuilder("+").
                                append(info.getAddInterestRate()).
                                append("%"));
            }


            tv_time_limit.setText(info.getProductTerm());
            tv_remain_amount.setText(
                    new StringBuilder("可认购金额:￥").
                            append(FormatUtil.formatAmount(info.getRemainAmount())).
                            append("/￥").
                            append(FormatUtil.formatAmount(info.getBidAmount())));
            if (position + 1 == getItemCount()) {
                divider.setVisibility(View.GONE);
            } else if (position + 1 < getItemCount() &&
                    mList.get(position + 1).getType() == RegularBase.ITEM_TYPE_TITLE) {
                divider.setVisibility(View.GONE);
            } else {
                divider.setVisibility(View.VISIBLE);
            }
            switch (Constants.getCurrentStatus(info.getStatus())) {
                case Constants.STATUS_DKB:
                    tv_profit_rate.setTextColor(mContext.getResources().getColor(R.color.mq_r1_v2));
                    tv_profit_rate_unit.setTextColor(mContext.getResources().getColor(R.color.mq_r1_v2));
                    tv_time_limit.setTextColor(mContext.getResources().getColor(R.color.mq_r1_v2));
                    tv_time_limit_unit.setTextColor(mContext.getResources().getColor(R.color.mq_r1_v2));
                    tv_begin_time.setText(Uihelper.timeToDateRegular(info.getStartTimeStamp()));
                    tv_begin_time.setVisibility(View.VISIBLE);
                    btn_state.setBackgroundResource(R.drawable.btn_no_begin);
                    btn_state.setText("待开标");
                    if (info.getBidType().equals(Constants.BID_TYPE_SBB)) {
                        iv_tag.setImageResource(R.drawable.double_rate_card_normal);
                    } else if (info.getBidType().equals(Constants.BID_TYPE_SBSYK)) {
                        iv_tag.setImageResource(R.drawable.double_card_normal);
                    } else {
                        iv_tag.setImageResource(0);
                    }
                    break;
                case Constants.STATUS_YKB:
                    tv_profit_rate.setTextColor(mContext.getResources().getColor(R.color.mq_r1_v2));
                    tv_profit_rate_unit.setTextColor(mContext.getResources().getColor(R.color.mq_r1_v2));
                    tv_time_limit.setTextColor(mContext.getResources().getColor(R.color.mq_r1_v2));
                    tv_time_limit_unit.setTextColor(mContext.getResources().getColor(R.color.mq_r1_v2));
                    tv_begin_time.setVisibility(View.GONE);
                    btn_state.setBackgroundResource(R.drawable.btn_default_selector);
                    btn_state.setText("立即认购");
                    if (info.getBidType().equals(Constants.BID_TYPE_SBB)) {
                        iv_tag.setImageResource(R.drawable.double_rate_card_normal);
                    } else if (info.getBidType().equals(Constants.BID_TYPE_SBSYK)) {
                        iv_tag.setImageResource(R.drawable.double_card_normal);
                    } else {
                        iv_tag.setImageResource(0);
                    }
                    break;
                default:
                    tv_profit_rate.setTextColor(mContext.getResources().getColor(R.color.mq_b5_v2));
                    tv_profit_rate_unit.setTextColor(mContext.getResources().getColor(R.color.mq_b5_v2));
                    tv_time_limit.setTextColor(mContext.getResources().getColor(R.color.mq_b5_v2));
                    tv_time_limit_unit.setTextColor(mContext.getResources().getColor(R.color.mq_b5_v2));
                    tv_begin_time.setVisibility(View.GONE);
                    btn_state.setBackgroundResource(R.drawable.btn_has_done);
                    btn_state.setText("已满额");
                    if (info.getBidType().equals(Constants.BID_TYPE_SBB)) {
                        iv_tag.setImageResource(R.drawable.double_rate_card_normal);
                    } else if (info.getBidType().equals(Constants.BID_TYPE_SBSYK)) {
                        iv_tag.setImageResource(R.drawable.double_card_normal);
                    } else {
                        iv_tag.setImageResource(0);
                    }
                    break;
            }
            btn_state.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MobclickAgent.onEvent(mContext, "1014");
                    RegularDetailActivity.startActivity(
                            mContext, info.getProductCode(), info.getProductType());
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MobclickAgent.onEvent(mContext, "1014");
                    RegularDetailActivity.startActivity(
                            mContext, info.getProductCode(), info.getProductType());
                }
            });
        }

    }

}
