package com.miqian.mq.adapter.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.DonutProgress;
import com.miqian.mq.R;
import com.miqian.mq.activity.RegularEarnActivity;
import com.miqian.mq.activity.RegularPlanActivity;
import com.miqian.mq.activity.WebActivity;
import com.miqian.mq.entity.RegularBaseData;
import com.miqian.mq.utils.FormatUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by guolei_wang on 15/10/13.
 */
public class RegularEarnViewHoder extends RecyclerView.ViewHolder {
    private View layout_regular_earn_head;
    private View layout_item;
    private TextView tv_lable_name;
    private TextView tv_category_description;
    private TextView tv_title;
    private ImageView img_category;
    private TextView tv_annurate_interest_rate;
    private TextView tv_duration;
    private TextView tv_progress;
    private TextView tv_buy_now;
    private TextView tv_add_interest;
    private DonutProgress circlebar;
    private ImageLoader imageLoader;
    private DisplayImageOptions options;

    public RegularEarnViewHoder(View itemView) {
        super(itemView);
        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder().
                cacheInMemory(true).cacheOnDisk(true)
                .considerExifParams(true).showImageOnLoading(R.drawable.icon_category_default)
                .showImageForEmptyUri(R.drawable.icon_category_default).showImageOnFail(R.drawable.icon_category_default)
                .build();

        layout_item = itemView.findViewById(R.id.layout_item);
        layout_regular_earn_head = itemView.findViewById(R.id.layout_regular_earn_head);
        tv_lable_name = (TextView) itemView.findViewById(R.id.tv_lable_name);
        tv_category_description = (TextView) itemView.findViewById(R.id.tv_category_description);
        tv_title = (TextView) itemView.findViewById(R.id.tv_title);
        img_category = (ImageView) itemView.findViewById(R.id.img_category);
        tv_annurate_interest_rate = (TextView) itemView.findViewById(R.id.tv_annurate_interest_rate);
        tv_duration = (TextView) itemView.findViewById(R.id.tv_duration);
        tv_progress = (TextView) itemView.findViewById(R.id.tv_progress);
//        tv_sale_number = (TextView) itemView.findViewById(R.id.tv_sale_number);
        tv_buy_now = (TextView) itemView.findViewById(R.id.tv_buy_now);
        tv_add_interest = (TextView) itemView.findViewById(R.id.tv_add_interest);
        circlebar = (DonutProgress) itemView.findViewById(R.id.circlebar);

        tv_lable_name.setText(R.string.regular_earn);
        tv_category_description.setText(R.string.principal_interest_security);
    }

    /**
     * 设置 lable
     *
     * @param name
     */
    public void setLableName(String name) {
        tv_lable_name.setText(name);
    }

    public void bindView(final Context mContext, final RegularBaseData regularEarn, boolean showLable) {

        tv_title.setText(regularEarn.getSubjectName());

        tv_duration.setText(regularEarn.getLimit() + "天");
        circlebar.setProgress((new Float(regularEarn.getPurchasePercent()).intValue()));

        //待开标
        if ("00".equals(regularEarn.getSubjectStatus())) {
            tv_annurate_interest_rate.setTextColor(mContext.getResources().getColor(R.color.mq_bl1));
            tv_duration.setTextColor(mContext.getResources().getColor(R.color.mq_bl1));
            tv_buy_now.setTextColor(mContext.getResources().getColor(R.color.mq_bl1));
            tv_progress.setTextColor(mContext.getResources().getColor(R.color.mq_b2));
            circlebar.setUnfinishedStrokeColor(mContext.getResources().getColor(R.color.mq_bl1));
            tv_add_interest.setBackgroundResource(R.drawable.bg_add_interest_unbegin);

            tv_progress.setText(FormatUtil.formatDate(regularEarn.getStartTimestamp(), "MM月dd日"));
            tv_buy_now.setText(FormatUtil.formatDate(regularEarn.getStartTimestamp(), "HH:mm"));
            tv_progress.setVisibility(View.VISIBLE);
        } else if ("01".equals(regularEarn.getSubjectStatus())) {
            tv_annurate_interest_rate.setTextColor(mContext.getResources().getColor(R.color.mq_r1));
            tv_duration.setTextColor(mContext.getResources().getColor(R.color.mq_r1));
            tv_buy_now.setTextColor(mContext.getResources().getColor(R.color.mq_r1));
            tv_progress.setTextColor(mContext.getResources().getColor(R.color.mq_r1));
            circlebar.setUnfinishedStrokeColor(mContext.getResources().getColor(R.color.mq_b5));
            tv_add_interest.setBackgroundResource(R.drawable.bg_add_interest);

            tv_buy_now.setText(R.string.buy_now);
            tv_progress.setVisibility(View.VISIBLE);
            tv_progress.setText(Float.valueOf(regularEarn.getPurchasePercent()).intValue() + "%");
        } else {
            tv_annurate_interest_rate.setTextColor(mContext.getResources().getColor(R.color.mq_b2));
            tv_duration.setTextColor(mContext.getResources().getColor(R.color.mq_b2));
            tv_buy_now.setTextColor(mContext.getResources().getColor(R.color.mq_b2));
            tv_progress.setTextColor(mContext.getResources().getColor(R.color.mq_b2));
            circlebar.setUnfinishedStrokeColor(mContext.getResources().getColor(R.color.mq_b2));
            circlebar.setProgress(0);
            tv_add_interest.setBackgroundResource(R.drawable.bg_add_interest_unabled);

            tv_buy_now.setText("已满额");
            tv_progress.setVisibility(View.GONE);
            tv_progress.setText(Float.valueOf(regularEarn.getPurchasePercent()).intValue() + "%");
        }
        tv_annurate_interest_rate.setText(regularEarn.getYearInterest() + "%");
//        if(TextUtils.isEmpty(regularEarn.getPromotionDesc())) {
//            tv_description.setVisibility(View.GONE);
//        }else {
//            tv_description.setVisibility(View.VISIBLE);
//            tv_description.setText(regularEarn.getPromotionDesc());
//        }

        if (regularEarn.getPresentationYesNo() == 1) {
            tv_add_interest.setVisibility(View.VISIBLE);
            tv_add_interest.setText(" +" + regularEarn.getPresentationYearInterest() + "% ");
        } else {
            tv_add_interest.setVisibility(View.GONE);
        }

        if (!showLable) {
            layout_regular_earn_head.setVisibility(View.GONE);
        } else {
            setLableName(regularEarn.getSubjectCategoryName());
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

        layout_item.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (RegularBaseData.PRODID_REGULAR_PLAN.equals(regularEarn.getProdId())) {
                    RegularPlanActivity.startActivity(mContext, regularEarn.getSubjectId());
                } else {
                    RegularEarnActivity.startActivity(mContext, regularEarn.getSubjectId());
                }
            }
        });
    }
}
