package com.miqian.mq.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.activity.RegularPlanActivity;
import com.miqian.mq.adapter.holder.RegularEarnViewHoder;
import com.miqian.mq.entity.GetRegularInfo;
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
    private GetRegularInfo mData;
    private Context mContext;
    private View swipeRefreshLayout;
    private boolean isHasHeader = false;

    public RegularListAdapter(GetRegularInfo mData, Context mContext, View swipeRefreshLayout) {
        this.mContext = mContext;
        this.mData = mData;
        this.items = mData.getRegList();
        this.planList = mData.getPlanList();
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
            return new RegularEarnViewHoder(inflater.inflate(R.layout.regular_earn_item, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof RegularEarnViewHoder) {
            final RegularEarn regularEarn = isHasHeader ? items.get(position - 1) : items.get(position);
            RegularEarnViewHoder viewHolder = (RegularEarnViewHoder) holder;
            viewHolder.bindView(mContext, regularEarn, position > 1 ? false : true);
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
            return planList.size() < MAX_PLAN_NUMBER? planList.size() : MAX_PLAN_NUMBER;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return (view == object);
        }
    }

}
