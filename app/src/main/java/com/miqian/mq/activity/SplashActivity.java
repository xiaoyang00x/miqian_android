package com.miqian.mq.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSON;
import com.growingio.android.sdk.collection.GrowingIO;
import com.miqian.mq.R;
import com.miqian.mq.entity.Advert;
import com.miqian.mq.entity.ConfigInfo;
import com.miqian.mq.entity.ConfigResult;
import com.miqian.mq.entity.Navigation;
import com.miqian.mq.entity.TabInfo;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.utils.Config;
import com.miqian.mq.utils.MobileOS;
import com.miqian.mq.utils.Pref;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.Timer;
import java.util.TimerTask;

import cn.jpush.android.api.JPushInterface;
import cn.sharesdk.framework.ShareSDK;

public class SplashActivity extends Activity implements View.OnClickListener {

    private Timer timer;

    private LinearLayout framePages;
    private RelativeLayout frameAds;
    private Button btSkip;
    private ImageView imageAds;
    private int adsClickStatus;

    private static final int pageCount = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GrowingIO.startTracing(this, "aabcdf9bad412e4f");
        GrowingIO.setScheme("growing.f20b1d4a79b7e6e4");

        setContentView(R.layout.activity_splash);
        Config.init(this);
        ShareSDK.initSDK(this);

        Pref.saveString(Pref.TAB_NAVIGATION_STR, "", getApplicationContext());
        Pref.saveInt(Pref.TAB_IMAGE_COUNT, 0, getApplicationContext());
        Pref.saveBoolean(Pref.TAB_NAVIGATION_ON_OFF, false, getApplicationContext());
        start();
    }

    private void start() {
        loadConfig();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadFinish();
            }
        }, 1500);
    }

    private void loadConfig() {
        HttpRequest.getConfig(SplashActivity.this, new ICallback<ConfigResult>() {
            @Override
            public void onSucceed(ConfigResult result) {
                ConfigInfo configInfo = result.getData();
                if (configInfo != null) {
                    Advert advert = configInfo.getAdvert();
                    if (advert != null && advert.getIsShow() == 1) {
                        String imgUrl;
                        if (Config.HEIGHT <= 1280) {
                            imgUrl = advert.getImgUrl_android1x();
                        } else {
                            imgUrl = advert.getImgUrl_android2x();
                        }
                        Pref.saveString(Pref.CONFIG_ADS + "ImgUrl", imgUrl, SplashActivity.this);
                        Pref.saveString(Pref.CONFIG_ADS + "JumpUrl", advert.getJumpUrl(), SplashActivity.this);
                        loadImageAds(imgUrl, SplashActivity.this);
                    } else {
                        Pref.saveInt(Pref.CONFIG_ADS, 0, SplashActivity.this);
                    }
                    Navigation navigation = configInfo.getNavigation();
                    if (navigation != null && navigation.isNavigationOnOff() && navigation.getNavigationList() != null && navigation.getNavigationList().size() > 0) {
                        for (int i = 0; i < navigation.getNavigationList().size(); i++) {
                            TabInfo tabInfo = navigation.getNavigationList().get(i);
                            if (tabInfo == null) return;
                            loadImage(tabInfo.getImg(), SplashActivity.this.getApplicationContext());
                            loadImage(tabInfo.getImgClick(), SplashActivity.this.getApplicationContext());
                        }

                        String navigationStr = JSON.toJSONString(navigation);
                        Pref.saveString(Pref.TAB_NAVIGATION_STR, navigationStr, SplashActivity.this.getApplicationContext());
                        Pref.saveBoolean(Pref.TAB_NAVIGATION_ON_OFF, navigation.isNavigationOnOff(), getApplicationContext());
                    }
                }
            }

            @Override
            public void onFail(String error) {

            }
        });
    }

    private DisplayImageOptions options;
    private ImageLoader imageLoader;

    private void initImageLoader() {
        if (options == null) {
            options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).build();
        }
        if (imageLoader == null) {
            imageLoader = ImageLoader.getInstance();
        }
    }

    public void loadImage(String url, final Context context) {
        initImageLoader();
        imageLoader.loadImage(url, options, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {

            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                int tabImageCount = Pref.getInt(Pref.TAB_IMAGE_COUNT, context, 0);
                Pref.saveInt(Pref.TAB_IMAGE_COUNT, ++tabImageCount, context);
            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        });
    }

    public void loadImageAds(String url, final Context context) {
        initImageLoader();
        imageLoader.loadImage(url, options, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {

            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                Pref.saveInt(Pref.CONFIG_ADS, 1, SplashActivity.this);
            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        });
    }

    private void loadFinish() {
        boolean first_use = Pref.getBoolean(Pref.FIRST_LOAD + MobileOS.getAppVersionName(this), this, true);
        if (!first_use) {
            loadAds();
        } else {
            final ViewPager mViewPager = (ViewPager) findViewById(R.id.viewpager);
            ImageView imageSplash = (ImageView) findViewById(R.id.image_splash);
            framePages = (LinearLayout) findViewById(R.id.frame_pages);
            imageSplash.setVisibility(View.GONE);
            if (framePages.getChildCount() < pageCount) {
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
            }
            mViewPager.setAdapter(mPagerAdapter);
            mViewPager.addOnPageChangeListener(new MyOnPageChangeListener());
            mViewPager.setVisibility(View.VISIBLE);
        }
    }

    private void loadAds() {
//        int adsInt = Pref.getInt(Pref.CONFIG_ADS, SplashActivity.this, 0);
        int adsInt = 1;//此版本广告一直存在
        if (adsInt == 1) {
            frameAds = (RelativeLayout) findViewById(R.id.frame_ads);
            btSkip = (Button) findViewById(R.id.bt_skip);
            btSkip.setOnClickListener(this);
            imageAds = (ImageView) findViewById(R.id.image_ads);
            imageAds.setBackgroundResource(R.drawable.icon_splash_ads);
            imageAds.setOnClickListener(this);
            timer = new Timer();
            TimerTask timerTask = new TimerTask() {
                int i = 3;

                @Override
                public void run() {
                    handler.sendEmptyMessage(i--);
                }
            };
            timer.schedule(timerTask, 0, 1000);
        } else {
            startActivity(new Intent(getBaseContext(), MainActivity.class));
            SplashActivity.this.finish();
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what <= 0) {
                timer.cancel();
                if (adsClickStatus == 0) {
                    startActivity(new Intent(getBaseContext(), MainActivity.class));
                    SplashActivity.this.finish();
                }
            } else {
                btSkip.setText(msg.what + " 跳过");
                frameAds.setVisibility(View.VISIBLE);
                String imgUrl = Pref.getString(Pref.CONFIG_ADS + "ImgUrl", SplashActivity.this, "");
                if (imageLoader == null) {
                    initImageLoader();
                }
//                imageLoader.displayImage(imgUrl, imageAds);
            }
            super.handleMessage(msg);
        }
    };

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
                float f1 = (float) Config.HEIGHT / Config.WIDTH;
                if (f1 < 1.77 || Config.HEIGHT <= 854) {
                    android.widget.RelativeLayout.LayoutParams paramsButton = (android.widget.RelativeLayout.LayoutParams) btStart.getLayoutParams();
                    paramsButton.bottomMargin = (int) (27 * Config.DENSITY);
                    btStart.setLayoutParams(paramsButton);
                }
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
        if (btSkip != null && imageAds != null) {
            btSkip.setEnabled(false);
            imageAds.setEnabled(false);
        }
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        adsClickStatus = 1;
        switch (v.getId()) {
            case R.id.image_ads:
                intent.putExtra("onClick", 1);
            default:
                Pref.saveBoolean(Pref.FIRST_LOAD + MobileOS.getAppVersionName(this), false, SplashActivity.this);
                startActivity(intent);
                SplashActivity.this.finish();
        }
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
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        ShareSDK.stopSDK(this);
    }
}
