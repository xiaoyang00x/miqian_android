package com.miqian.mq.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;

import com.miqian.mq.activity.WebActivity;
import com.miqian.mq.entity.AdvertisementImg;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class HomeAdPageAdapter extends PagerAdapter {
    private ArrayList<AdvertisementImg> list = new ArrayList<>();
    private Recylcer recylcer;
    private LayoutParams lp =
            new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    private ImageLoader imageLoader;
    private DisplayImageOptions options;
    private OnPageItemClickListener onPageItemClickListener;
    Context ctx;

    public class Recylcer {

        private LinkedList<View> viewList = new LinkedList<>();
        private Context context;

        public Recylcer(Context context) {
            this.context = context;
        }

        public View requestView() {
            if (viewList.size() > 0) {
                return viewList.removeFirst();
            } else {
                return new ImageView(context);
            }
        }

        public void releaseView(View view) {
            viewList.addLast(view);
        }
    }

    public HomeAdPageAdapter(Context context, List<AdvertisementImg> recommends) {
        for (AdvertisementImg recommend : recommends) {
            list.add(recommend);
        }
        ctx = context;
        recylcer = new Recylcer(context);
        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).build();
    }

    public void reload(ArrayList<AdvertisementImg> recommends) {
        list.clear();
        for (AdvertisementImg recommend : recommends) {
            list.add(recommend);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        // 定义count为无限大值，以循环滑动
        if (list.size() != 0) {
            return Integer.MAX_VALUE;
        } else {
            return 0;
        }
    }

    public int getItemCount() {
        return list.size();
    }

    public AdvertisementImg getItem(int position) {
        // 对position取余数
        return list.get(position % list.size());
    }

    public boolean isViewFromObject(View view, Object object) {
        return view == ((ImageView) object);
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        final ImageView imageView = (ImageView) recylcer.requestView();
        String url = list.get(position % list.size()).getImgUrl();
        imageLoader.displayImage(url, imageView, options);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setTag(list.get(position % list.size()).getJumpUrl());// 绑定imageview 视图
        container.addView(imageView, lp);
        imageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                MobclickAgent.onEvent(imageView.getContext(), "1002");
                WebActivity.startActivity(imageView.getContext(), imageView.getTag().toString());
            }
        });
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object item) {
        ImageView imageView = (ImageView) item;
        container.removeView(imageView);
        imageView.setImageBitmap(null);
        imageView.setTag(null);
        imageView.setOnClickListener(null);
    }

    public interface OnPageItemClickListener {
        void onPageItemClick(ViewGroup parent, View item, int position);
    }

    public ArrayList<AdvertisementImg> getList() {
        return list;
    }
}
