package com.miqian.mq.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.miqian.mq.R;
import com.miqian.mq.utils.Config;
import com.miqian.mq.utils.MobileOS;
import com.miqian.mq.utils.Pref;
import com.umeng.update.UmengUpdateAgent;

import cn.jpush.android.api.JPushInterface;
import cn.sharesdk.framework.ShareSDK;

public class SplashActivity extends Activity implements View.OnClickListener {

    private ViewPager mViewPager;
    private LinearLayout framePages;
    private ImageView imageSplash;

    private static final int pageCount = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Config.init(this);
        //      设置友盟渠道号
        String channelId = Pref.getString(Pref.CHANNEL_ID, this, "");
        if (TextUtils.isEmpty(channelId)) {
            Pref.saveString(Pref.CHANNEL_ID, MobileOS.getChannelName(this), this);
        } else {
            UmengUpdateAgent.setChannel(channelId);
        }

        ShareSDK.initSDK(this);

        setContentView(R.layout.activity_splash);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        framePages = (LinearLayout) findViewById(R.id.frame_pages);
        imageSplash = (ImageView) findViewById(R.id.image_splash);

    }

    @Override
    protected void onStart() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadFinish();
            }
        }, 1500);
        super.onStart();
    }

    public void loadFinish() {
        boolean first_use = Pref.getBoolean(Pref.FIRST_LOAD + MobileOS.getAppVersionName(this), this, true);
        if (!first_use) {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            SplashActivity.this.finish();
        } else {
            imageSplash.setVisibility(View.GONE);
            for (int i = 0; i < pageCount; i++) {
                LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                ImageView imagePage = new ImageView(SplashActivity.this);
                if (i == 0) {
                    imagePage.setBackgroundResource(R.drawable.guide_page_on);
                } else {
                    params.leftMargin = (int) (10 * Config.DENSITY);
                    imagePage.setBackgroundResource(R.drawable.guide_page);
                }
                framePages.addView(imagePage, params);
            }

            mViewPager.setAdapter(mPagerAdapter);
            mViewPager.addOnPageChangeListener(new MyOnPageChangeListener());
            mViewPager.setVisibility(View.VISIBLE);
        }
    }

    // 填充ViewPager的数据适配器
    PagerAdapter mPagerAdapter = new PagerAdapter() {

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public int getCount() {
            return pageCount;
        }

        @Override
        public void destroyItem(View container, int position, Object object) {
            ((ViewPager) container).removeView((View) object);
        }

        @Override
        public Object instantiateItem(View container, int position) {
            View view = LayoutInflater.from(SplashActivity.this).inflate(R.layout.screen, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.guide);
            if (position == pageCount - 1) {
                Button btStart = (Button) view.findViewById(R.id.start);
//				if (Config.HEIGHT <= 800) {
//					android.widget.RelativeLayout.LayoutParams paramsButton = (android.widget.RelativeLayout.LayoutParams) btStart.getLayoutParams();
//					paramsButton.bottomMargin = (int) (27 * Config.DENSITY);
//					btStart.setLayoutParams(paramsButton);
//				}
                btStart.setVisibility(View.VISIBLE);
                btStart.setOnClickListener(SplashActivity.this);
            }

            imageView.setImageResource(getScreen(position));
            ((ViewPager) container).addView(view);
            return view;
        }
    };

    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageSelected(int index) {
            if (index < framePages.getChildCount()) {
                ImageView imageView = (ImageView) framePages.getChildAt(index);
                imageView.setBackgroundResource(R.drawable.guide_page_on);
                if (index - 1 >= 0) {
                    imageView = (ImageView) framePages.getChildAt(index - 1);
                    imageView.setBackgroundResource(R.drawable.guide_page);
                }
                if (index < framePages.getChildCount() - 1) {
                    imageView = (ImageView) framePages.getChildAt(index + 1);
                    imageView.setBackgroundResource(R.drawable.guide_page);
                }
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    }

    @Override
    public void onClick(View v) {
        Pref.saveBoolean(Pref.FIRST_LOAD + MobileOS.getAppVersionName(this), false, SplashActivity.this);
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        SplashActivity.this.finish();
    }

    private int getScreen(int index) {
        int id = R.drawable.screen1;
        switch (index) {
            case 0:
                id = R.drawable.screen1;
                break;
            case 1:
                id = R.drawable.screen2;
                break;
            case 2:
                id = R.drawable.screen3;
                break;
            case 3:
                id = R.drawable.screen4;
                break;
        }
        return id;
    }


    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(SplashActivity.this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(SplashActivity.this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ShareSDK.stopSDK(this);
    }
}
