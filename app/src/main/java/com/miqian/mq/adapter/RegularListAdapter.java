package com.miqian.mq.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.activity.RegularPlanActivity;
import com.miqian.mq.activity.WebActivity;
import com.miqian.mq.adapter.holder.RegularEarnViewHoder;
import com.miqian.mq.entity.GetRegularInfo;
import com.miqian.mq.entity.RegularBaseData;
import com.miqian.mq.entity.SubjectCategoryData;
import com.miqian.mq.utils.FormatUtil;
import com.miqian.mq.views.DecoratorViewPager;
import com.miqian.mq.views.indicator.CirclePageIndicator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guolei_wang on 15/9/17.
 */
public class RegularListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final String TAG = RegularListAdapter.class.getSimpleName();
    private final int ITEM_TYPE_HEADER = 0;
    private final int ITEM_TYPE_NORMAL = 1;


    private ArrayList<RegularBaseData> planList = new ArrayList<>();    //定期计划header
    private ArrayList<RegularBaseData> regularBaseDatas = new ArrayList<>();
    private Context mContext;
    private View swipeRefreshLayout;
    private boolean isHasHeader = false;
    private int subjectInfoSize = 0;     //定期项目的数量，不包括圆形的定期计划

    public RegularListAdapter(GetRegularInfo info, Context mContext, View swipeRefreshLayout) {
        this.mContext = mContext;
        this.swipeRefreshLayout = swipeRefreshLayout;

        subjectInfoSize = 0;
        if (info.getSubjectData() != null) {
            for (int i = 0; i < info.getSubjectData().size(); i++) {
                SubjectCategoryData categoryData = info.getSubjectData().get(i);
                if (categoryData != null && categoryData.getSubjectInfo().size() > 0) {
                    categoryData.getSubjectInfo().get(0).setShowLable(true);
                    categoryData.getSubjectInfo().get(0).setSubjectCategoryName(categoryData.getSubjectCategoryName());
                    categoryData.getSubjectInfo().get(0).setSubjectCategoryIconUrl(categoryData.getSubjectCategoryIconUrl());
                    categoryData.getSubjectInfo().get(0).setSubjectCategoryDesc(categoryData.getSubjectCategoryDesc());
                    categoryData.getSubjectInfo().get(0).setSubjectCategoryDescUrl(categoryData.getSubjectCategoryDescUrl());
                    if (i == 0) {
                        planList.addAll(categoryData.getSubjectInfo());   //Api 接口默认第一个为 header的数据
                    } else {
                        subjectInfoSize += categoryData.getSubjectInfo().size();
                        regularBaseDatas.addAll(categoryData.getSubjectInfo());
                    }
                }
            }
        }
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
            final RegularBaseData regularEarn = regularBaseDatas.get(isHasHeader ? position - 1 : position);
            RegularEarnViewHoder viewHolder = (RegularEarnViewHoder) holder;
            viewHolder.bindView(mContext, regularEarn, regularEarn.isShowLable());
        }
    }

    @Override
    public int getItemCount() {
        int itemsCount = regularBaseDatas == null ? 0 : regularBaseDatas.size();
        return !isHasHeader ? itemsCount : itemsCount + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (!isHasHeader) {
            return ITEM_TYPE_NORMAL;
        }
        return position == 0 ? ITEM_TYPE_HEADER : ITEM_TYPE_NORMAL;
    }


    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        Context mContext;
        private DecoratorViewPager view_pager;
        private LinearLayout layout_limit_container;
        private View layout_regular_earn_head;
        private TextView tv_lable_name;
        private TextView tv_category_description;
        private ImageView img_category;
        private ImageLoader imageLoader;
        private DisplayImageOptions options;
        private int defalutIndicatorPosition = 0;  //viewpager 默认显示的页面

        public HeaderViewHolder(View itemView, final Context mContext, final View swipeRefreshLayout, List<RegularBaseData> planList) {
            super(itemView);
            this.mContext = mContext;
            imageLoader = ImageLoader.getInstance();
            options = new DisplayImageOptions.Builder().
                    cacheInMemory(true).cacheOnDisk(true)
                    .considerExifParams(true).showImageOnLoading(R.drawable.icon_category_default)
                    .showImageForEmptyUri(R.drawable.icon_category_default).showImageOnFail(R.drawable.icon_category_default)
                    .build();

            view_pager = (DecoratorViewPager) itemView.findViewById(R.id.view_pager);
            layout_limit_container = (LinearLayout) itemView.findViewById(R.id.layout_limit_container);
            if(planList.size() > 1) {
                defalutIndicatorPosition = 1;
            }else {
                defalutIndicatorPosition = 0;
            }
            view_pager.setAdapter(new MyPagerAdapter(mContext, planList, defalutIndicatorPosition));
            view_pager.setCurrentItem(defalutIndicatorPosition);


            layout_regular_earn_head = itemView.findViewById(R.id.layout_regular_earn_head);
            tv_lable_name = (TextView) itemView.findViewById(R.id.tv_lable_name);
            tv_category_description = (TextView) itemView.findViewById(R.id.tv_category_description);
            img_category = (ImageView) itemView.findViewById(R.id.img_category);
//            view_pager.setAdapter(new RegularViewPagerAdapter(fm));
            // to cache all page, or we will see the right item delayed
            view_pager.setOffscreenPageLimit(3);

            tv_lable_name.setText(R.string.regular_plan);
            tv_category_description.setText(R.string.principal_interest_security);

            layout_limit_container.removeAllViews();
            if (planList != null && planList.size() > 0) {
                int count = planList.size();
                layout_limit_container.setWeightSum(count);
//                layout_limit_container.setWeightSum((count + 1) * 2);
                for (int i = 0; i < count; i++) {
                    final RegularBaseData regularEarn = planList.get(i);
                    TextView textView = new TextView(mContext);
                    textView.setText(regularEarn.getLimit() + "天");
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
                    textView.setGravity(Gravity.CENTER);
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mContext.getResources().getDimensionPixelSize(R.dimen.mq_font4));
                    textView.setTextColor(mContext.getResources().getColor(i == 0 ? R.color.mq_r1 : R.color.mq_b2));
                    layout_limit_container.addView(textView, i, params);


                    if (i == 0) {
                        tv_lable_name.setText(regularEarn.getSubjectCategoryName());
                        layout_regular_earn_head.setVisibility(View.VISIBLE);

                        tv_category_description.setText(regularEarn.getSubjectCategoryDesc());
                        if (TextUtils.isEmpty(regularEarn.getSubjectCategoryDescUrl())) {
                            tv_category_description.setText(regularEarn.getSubjectCategoryDesc());
                        } else {
                            tv_category_description.setText(regularEarn.getSubjectCategoryDesc() + ">>");
                            tv_category_description.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    WebActivity.startActivity(mContext, regularEarn.getSubjectCategoryDescUrl());
                                }
                            });
                        }

                        if (TextUtils.isEmpty(regularEarn.getSubjectCategoryIconUrl())) {
                            img_category.setVisibility(View.GONE);
                        } else {
                            imageLoader.displayImage(regularEarn.getSubjectCategoryIconUrl(), img_category, options);
                            img_category.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
            HorizontalScrollView viewPager_HorizonScrollView = (HorizontalScrollView) itemView.findViewById(R.id.viewPager_HorizonScrollView);
            CirclePageIndicator circle_indicator = (CirclePageIndicator) itemView.findViewById(R.id.circle_indicator);
            circle_indicator.setViewPager(view_pager);
//            circle_indicator.setIndicatorContainer(layout_limit_container);
            circle_indicator.setScrollView(viewPager_HorizonScrollView);
            viewPager_HorizonScrollView.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    return true;
                }
            });

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
                    MobclickAgent.onEvent(mContext, "1009");
                    int childCount = layout_limit_container.getChildCount();
                    if (childCount < 1) return;
                    for (int i = 0; i < childCount; i++) {
                        TextView textView = (TextView) layout_limit_container.getChildAt(i);
                        textView.setTextColor(mContext.getResources().getColor(i == position ? R.color.mq_r1 : R.color.mq_b2));

                    }

                    for (int j = 0; j < view_pager.getChildCount(); j++) {
                        View view = view_pager.getChildAt(j);
                        if (view != null) {
                            view.setAlpha(0.3f);
                        }
                    }
                    view_pager.findViewById(position).setAlpha(1f);

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }


    }

    static class MyPagerAdapter extends PagerAdapter {
        Context mContext;
        List<RegularBaseData> planList;
        int defalutIndicatorPosition;

        public MyPagerAdapter(Context mContext, List<RegularBaseData> planList, int defalutIndicatorPosition) {
            this.mContext = mContext;
            this.planList = planList;
            this.defalutIndicatorPosition = defalutIndicatorPosition;
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
            TextView lable_annurate_interest_rate = (TextView) view.findViewById(R.id.lable_annurate_interest_rate);
            container.addView(view);

            final RegularBaseData plan = planList.get(position);
            tv_annurate_interest_rate.setText(plan.getYearInterest() + "%");
            if (plan.getPresentationYesNo() == 1) {
                tv_add_interest.setText(" +" + plan.getPresentationYearInterest() + "% ");
                tv_add_interest.setVisibility(View.VISIBLE);
            } else {
                tv_add_interest.setVisibility(View.GONE);
            }

            if ("00".equals(plan.getSubjectStatus())) {
                tv_add_interest.setBackgroundResource(R.drawable.bg_add_interest);
                layout_circle.setBackgroundResource(R.drawable.bg_circle_unbegin);
                lable_annurate_interest_rate.setTextColor(mContext.getResources().getColor(R.color.mq_b1));
                tv_annurate_interest_rate.setTextColor(mContext.getResources().getColor(R.color.mq_r1));
                tv_begin_time.setText(FormatUtil.formatDate(plan.getStartTimestamp(), "dd日 HH:mm开售"));
                tv_begin_time.setVisibility(View.VISIBLE);
                tv_status.setVisibility(View.GONE);
            } else if ("01".equals(plan.getSubjectStatus())) {
                tv_status.setText("马上认购");
                tv_add_interest.setBackgroundResource(R.drawable.bg_add_interest);
                layout_circle.setBackgroundResource(R.drawable.bg_circle_buy);
                lable_annurate_interest_rate.setTextColor(mContext.getResources().getColor(R.color.mq_b1));
                tv_annurate_interest_rate.setTextColor(mContext.getResources().getColor(R.color.mq_r1));

                tv_begin_time.setVisibility(View.GONE);
                tv_status.setVisibility(View.VISIBLE);
            } else {
                tv_status.setText("已满额");
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
            if (position == defalutIndicatorPosition) {
                view.setAlpha(1f);
            } else {
                view.setAlpha(0.3f);
            }
            view.setId(position);
            return view;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            view.setAlpha(0.3f);
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return planList != null ? planList.size() : 0;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return (view == object);
        }
    }

}
