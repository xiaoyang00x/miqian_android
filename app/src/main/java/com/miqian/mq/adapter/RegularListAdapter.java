package com.miqian.mq.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.DonutProgress;
import com.miqian.mq.R;
import com.miqian.mq.activity.RegularEarnActivity;
import com.miqian.mq.activity.RegularPlanActivity;
import com.miqian.mq.entity.RegularEarn;
import com.miqian.mq.entity.RegularPlan;
import com.miqian.mq.utils.FormatUtil;
import com.miqian.mq.views.DecoratorViewPager;
import com.miqian.mq.views.indicator.CirclePageIndicator;

import java.util.List;

/**
 * Created by guolei_wang on 15/9/17.
 */
public class RegularListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int ITEM_TYPE_HEADER = 0;
    private final int ITEM_TYPE_NORMAL = 1;

    public  final static int MAX_PLAN_NUMBER = 6;   //定期计划显示的最大数量

    private List<RegularEarn> items;
    private List<RegularPlan> planList;
    private Context mContext;
    private View swipeRefreshLayout;
    private boolean isHasHeader = false;

    public RegularListAdapter(List<RegularEarn> items, List<RegularPlan> planList, Context mContext, View swipeRefreshLayout) {
        this.items = items;
        this.mContext = mContext;
        this.planList = planList;
        this.swipeRefreshLayout = swipeRefreshLayout;

        if (planList != null && planList.size() > 0) {
            isHasHeader = true;
        } else {
            isHasHeader = false;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        if (viewType == ITEM_TYPE_HEADER) {
            return new HeaderViewHolder(inflater.inflate(R.layout.fragment_regular_header, parent, false), mContext, swipeRefreshLayout, planList);
        } else {
            return new ViewHolder(inflater.inflate(R.layout.regular_earn_item, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof ViewHolder) {
            final RegularEarn regularEarn = isHasHeader ? items.get(position - 1) : items.get(position);
            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.tv_title.setText(regularEarn.getSubjectName());
            viewHolder.tv_sub_title.setText("项目总额" +  FormatUtil.formatAmount(regularEarn.getSubjectTotalPrice()) + "  " + regularEarn.getPayMode());

            viewHolder.tv_duration.setText(regularEarn.getLimit() + "天");
            viewHolder.circlebar.setProgress((new Float(regularEarn.getPurchasePercent()).intValue()));


            //待开标
            if("00".equals(regularEarn.getSubjectStatus())) {
                viewHolder.tv_annurate_interest_rate.setTextColor(mContext.getResources().getColor(R.color.mq_bl1));
                viewHolder.tv_duration.setTextColor(mContext.getResources().getColor(R.color.mq_bl1));
                viewHolder.tv_buy_now.setTextColor(mContext.getResources().getColor(R.color.mq_bl1));
                viewHolder.tv_progress.setTextColor(mContext.getResources().getColor(R.color.mq_b2));
                viewHolder.circlebar.setUnfinishedStrokeColor(mContext.getResources().getColor(R.color.mq_bl1));
                viewHolder.tv_sale_number.setVisibility(View.GONE);
                viewHolder.tv_add_interest.setBackgroundResource(R.drawable.bg_add_interest_unbegin);

                viewHolder.tv_progress.setText(FormatUtil.formatDate(regularEarn.getStartTimestamp(), "MM月dd日"));
                viewHolder.tv_buy_now.setText(FormatUtil.formatDate(regularEarn.getStartTimestamp(), "hh:mm"));
            }else {
                viewHolder.tv_annurate_interest_rate.setTextColor(mContext.getResources().getColor(R.color.mq_r1));
                viewHolder.tv_duration.setTextColor(mContext.getResources().getColor(R.color.mq_r1));
                viewHolder.tv_buy_now.setTextColor(mContext.getResources().getColor(R.color.mq_r1));
                viewHolder.tv_progress.setTextColor(mContext.getResources().getColor(R.color.mq_r1));
                viewHolder.circlebar.setUnfinishedStrokeColor(mContext.getResources().getColor(R.color.mq_b5));
                viewHolder.tv_sale_number.setVisibility(View.VISIBLE);
                viewHolder.tv_add_interest.setBackgroundResource(R.drawable.bg_add_interest);

                viewHolder.tv_buy_now.setText(R.string.buy_now);
                viewHolder.tv_annurate_interest_rate.setText(regularEarn.getYearInterest() + "%");
                viewHolder.tv_progress.setText(regularEarn.getPurchasePercent() + "%");
                viewHolder.tv_sale_number.setText("已认购" + regularEarn.getPersonTime() + "人");
            }
            if(TextUtils.isEmpty(regularEarn.getPromotionDesc())) {
                viewHolder.tv_description.setVisibility(View.GONE);
            }else {
                viewHolder.tv_description.setVisibility(View.VISIBLE);
                viewHolder.tv_description.setText(regularEarn.getPromotionDesc());
            }

            if("Y".equalsIgnoreCase(regularEarn.getPresentationYesNo())) {
                viewHolder.tv_add_interest.setVisibility(View.VISIBLE);
                viewHolder.tv_add_interest.setText(regularEarn.getPresentationYearInterest());
            }else {
                viewHolder.tv_add_interest.setVisibility(View.GONE);
            }

            if (position > 1) {
                viewHolder.layout_regular_earn_head.setVisibility(View.GONE);
            } else {
                viewHolder.layout_regular_earn_head.setVisibility(View.VISIBLE);
            }

            viewHolder.layout_item.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    RegularEarnActivity.startActivity(mContext, regularEarn.getSubjectId());
                }
            });
        }
    }

    public void addAll(List<RegularEarn> earnList) {
        items.addAll(earnList);
        notifyDataSetChanged();
    }

    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        int itemsCount = items == null ? 0 : items.size();
        return planList == null ? itemsCount : itemsCount + 1;
    }

    @Override
    public int getItemViewType(int position) {
//        return ITEM_TYPE_NORMAL;
        if (planList == null) {
            return ITEM_TYPE_NORMAL;
        }
        return position == 0 ? ITEM_TYPE_HEADER : ITEM_TYPE_NORMAL;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        private View layout_regular_earn_head;
        private View layout_item;
        public TextView tv_lable_name;
        public TextView tv_invest_security;
        public TextView tv_title;
        public TextView tv_sub_title;
        public TextView tv_annurate_interest_rate;
        public TextView tv_duration;
        public TextView tv_progress;
        public TextView tv_buy_now;
        public TextView tv_sale_number;
        public TextView tv_description;
        public TextView tv_add_interest;
        public DonutProgress circlebar;

        public ViewHolder(View itemView) {
            super(itemView);

            layout_item = itemView.findViewById(R.id.layout_item);
            layout_regular_earn_head = itemView.findViewById(R.id.layout_regular_earn_head);
            tv_lable_name = (TextView) itemView.findViewById(R.id.tv_lable_name);
            tv_invest_security = (TextView) itemView.findViewById(R.id.tv_invest_security);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_sub_title = (TextView) itemView.findViewById(R.id.tv_sub_title);
            tv_annurate_interest_rate = (TextView) itemView.findViewById(R.id.tv_annurate_interest_rate);
            tv_duration = (TextView) itemView.findViewById(R.id.tv_duration);
            tv_progress = (TextView) itemView.findViewById(R.id.tv_progress);
            tv_sale_number = (TextView) itemView.findViewById(R.id.tv_sale_number);
            tv_buy_now = (TextView) itemView.findViewById(R.id.tv_buy_now);
            tv_description = (TextView) itemView.findViewById(R.id.tv_description);
            tv_add_interest = (TextView) itemView.findViewById(R.id.tv_add_interest);
            circlebar = (DonutProgress) itemView.findViewById(R.id.circlebar);

            tv_lable_name.setText(R.string.regular_earn);
            tv_invest_security.setText(R.string.principal_interest_security);
        }
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        Context mContext;
        private DecoratorViewPager view_pager;
        private LinearLayout layout_limit_container;

        public HeaderViewHolder(View itemView, final Context mContext, final View swipeRefreshLayout, List<RegularPlan> planList) {
            super(itemView);
            this.mContext = mContext;

            view_pager = (DecoratorViewPager) itemView.findViewById(R.id.view_pager);
            layout_limit_container = (LinearLayout) itemView.findViewById(R.id.layout_limit_container);
            view_pager.setAdapter(new MyPagerAdapter(mContext, planList));
//            view_pager.setAdapter(new RegularViewPagerAdapter(fm));
            // to cache all page, or we will see the right item delayed
            view_pager.setOffscreenPageLimit(3);

            layout_limit_container.removeAllViews();
            if(planList != null && planList.size() > 0) {
                int count = planList.size() < MAX_PLAN_NUMBER? planList.size() : MAX_PLAN_NUMBER;
                layout_limit_container.setWeightSum((count+1) * 2);
                for(int i = 0; i < count; i++) {
                    TextView textView = new TextView(mContext);
                    textView.setText(planList.get(i).getLimit() + "天");
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 2);
                    textView.setGravity(Gravity.CENTER);
                    textView.setTextColor(mContext.getResources().getColor(i == 0 ? R.color.mq_r1 : R.color.mq_b2));
//                    textView.setBackgroundColor(mContext.getResources().getColor(i%2==0?R.color.mq_y:R.color.mq_r1));
                    layout_limit_container.addView(textView, i, params);
                }
            }
            CirclePageIndicator circle_indicator = (CirclePageIndicator) itemView.findViewById(R.id.circle_indicator);
            circle_indicator.setViewPager(view_pager);
            view_pager.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_MOVE:
                            swipeRefreshLayout.setEnabled(false);
                            break;
                        case MotionEvent.ACTION_UP:
                        case MotionEvent.ACTION_CANCEL:
                            swipeRefreshLayout.setEnabled(true);
                            break;
                    }
                    return false;
                }
            });
            circle_indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    int childCount = layout_limit_container.getChildCount();
                    if(childCount < 1) return;
                    for(int i = 0; i < childCount; i++) {
                        TextView textView = (TextView)layout_limit_container.getChildAt(i);
                        textView.setTextColor(mContext.getResources().getColor(i == position?R.color.mq_r1:R.color.mq_b2));
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }


    }

    static class MyPagerAdapter extends PagerAdapter {
        Context mContext;
        List<RegularPlan> planList;

        public MyPagerAdapter(Context mContext, List<RegularPlan> planList) {
            this.mContext = mContext;
            this.planList = planList;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View view = inflater.inflate(R.layout.fragment_regular_plan, null);
            View layout_circle = view.findViewById(R.id.layout_circle);
            TextView tv_annurate_interest_rate = (TextView) view.findViewById(R.id.tv_annurate_interest_rate);
            TextView tv_add_interest = (TextView) view.findViewById(R.id.tv_add_interest);
            TextView tv_status = (TextView) view.findViewById(R.id.tv_status);
            TextView tv_begin_time = (TextView) view.findViewById(R.id.tv_begin_time);
            TextView tv_description = (TextView) view.findViewById(R.id.tv_description);
            TextView lable_annurate_interest_rate = (TextView) view.findViewById(R.id.lable_annurate_interest_rate);
            container.addView(view);

            final RegularPlan plan = planList.get(position);
            tv_annurate_interest_rate.setText(plan.getYearInterest() + "%");
            if("Y".equalsIgnoreCase(plan.getPresentationYesNo())) {
                tv_add_interest.setText(plan.getPresentationYearInterest() + "%");
                tv_add_interest.setVisibility(View.VISIBLE);
            }else {
                tv_add_interest.setVisibility(View.GONE);
            }
            tv_description.setText(plan.getPromotionDesc());

            if("00".equals(plan.getSubjectStatus())) {
                tv_add_interest.setBackgroundResource(R.drawable.bg_add_interest);
                layout_circle.setBackgroundResource(R.drawable.bg_circle_unbegin);
                lable_annurate_interest_rate.setTextColor(mContext.getResources().getColor(R.color.mq_b1));
                tv_annurate_interest_rate.setTextColor(mContext.getResources().getColor(R.color.mq_r1));
                tv_begin_time.setText(FormatUtil.formatDate(plan.getStartTimestamp(), "dd日 hh:mm开售"));
                tv_begin_time.setVisibility(View.VISIBLE);
                tv_status.setVisibility(View.GONE);
            }else if("01".equals(plan.getSubjectStatus())) {
                tv_status.setText("马上认购");
                tv_add_interest.setBackgroundResource(R.drawable.bg_add_interest);
                layout_circle.setBackgroundResource(R.drawable.bg_circle_buy);
                lable_annurate_interest_rate.setTextColor(mContext.getResources().getColor(R.color.mq_b1));
                tv_annurate_interest_rate.setTextColor(mContext.getResources().getColor(R.color.mq_r1));

                tv_begin_time.setVisibility(View.GONE);
                tv_status.setVisibility(View.VISIBLE);
            } else {
                tv_status.setText("售罄");
                tv_add_interest.setBackgroundResource(R.drawable.bg_add_interest_unabled);
                layout_circle.setBackgroundResource(R.drawable.bg_circle_sell_out);
                lable_annurate_interest_rate.setTextColor(mContext.getResources().getColor(R.color.mq_b2));
                tv_annurate_interest_rate.setTextColor(mContext.getResources().getColor(R.color.mq_b2));

                tv_begin_time.setVisibility(View.GONE);
                tv_status.setVisibility(View.VISIBLE);
            }

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    RegularPlanActivity.startActivity(mContext, plan.getSubjectId());
                }
            });
            return view;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return MAX_PLAN_NUMBER;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return (view == object);
        }
    }

}
