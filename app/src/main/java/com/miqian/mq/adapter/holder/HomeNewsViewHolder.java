package com.miqian.mq.adapter.holder;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.activity.WebActivity;
import com.miqian.mq.entity.HomeNewsData;
import com.miqian.mq.entity.HomePageInfo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by guolei_wang on 16/5/24.
 * 首页新闻动态
 */
public class HomeNewsViewHolder extends HomeBaseViewHolder {

    private TextView tv_lable;
    private LinearLayout layout_container;
    private LayoutInflater inflater;
    private ImageLoader imageLoader;
    private DisplayImageOptions options;

    public HomeNewsViewHolder(View itemView) {
        super(itemView);
        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).build();
        inflater = LayoutInflater.from(itemView.getContext());

        tv_lable = (TextView)itemView.findViewById(R.id.tv_lable);
        layout_container = (LinearLayout) itemView.findViewById(R.id.layout_container);

    }

    /**
     * 初始化项目控件
     * @param data
     * @return
     */
    private View initProjectView(HomeNewsData data) {
        View projectView = inflater.inflate(R.layout.item_home_single_news, null);
        //名称
        TextView tv_title = (TextView)projectView.findViewById(R.id.tv_title);
        //描述
        TextView tv_description = (TextView)projectView.findViewById(R.id.tv_description);
        //图片
        final ImageView img_news = (ImageView) projectView.findViewById(R.id.img_news);

        tv_title.setText(data.getTitle());
        tv_description.setText(data.getDesc());

        imageLoader.displayImage(data.getImgUrl(), img_news, options, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {
                ((ImageView)view).setImageResource(R.drawable.bg_news_default);
            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {
                ((ImageView)view).setImageResource(R.drawable.bg_news_default);
            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {

            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        });
        img_news.setScaleType(ImageView.ScaleType.FIT_XY);
        if(!TextUtils.isEmpty(data.getJumpUrl())) {
            img_news.setTag(data.getJumpUrl());// 绑定imageview 视图
            projectView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    MobclickAgent.onEvent(v.getContext(), "1004_3");
                WebActivity.startActivity(img_news.getContext(), img_news.getTag().toString());
                }
            });
        }
        return projectView;
    }

    @Override
    public void bindView(HomePageInfo mData) {
        layout_container.removeAllViews();
        if(mData != null && mData.getHotNewsData() != null) {
            if(TextUtils.isEmpty(mData.getTitle())) {
                tv_lable.setVisibility(View.GONE);
            }else {
                tv_lable.setText(mData.getTitle());
                tv_lable.setVisibility(View.VISIBLE);
            }

            if(mData.getHotNewsData().size() > 0) {
                for(int i = 0; i < mData.getHotNewsData().size(); i++) {
                    layout_container.addView(initProjectView(mData.getHotNewsData().get(i)));
                }
            }
        }
    }
}
