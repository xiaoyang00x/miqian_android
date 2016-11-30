package com.miqian.mq.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.miqian.mq.R;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.views.MQPhotoView;
import com.miqian.mq.views.MQPhotoViewAttacher;
import com.miqian.mq.views.WFYTitle;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * @author wangduo
 * @description: ${todo}
 * @email: cswangduo@163.com
 * @date: 16/8/9
 */
public class ShowWebImgActivity extends BaseActivity {

    private static final String URL_IMG = "URL_IMG";

    private MQPhotoView photoView;
    private MQPhotoViewAttacher mAttacher;
    private String url;

    public static void startActivity(Context mContext, String img_url) {
        Intent intent = new Intent(mContext, ShowWebImgActivity.class);
        intent.putExtra(ShowWebImgActivity.URL_IMG, img_url);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle arg0) {
        url = getIntent().getStringExtra(URL_IMG);
        super.onCreate(arg0);
        doubleClickEnable = true;
    }

    @Override
    public void obtainData() {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(url, photoView, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                begin();
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                Uihelper.showToast(mContext, "加载失败...");
                end();
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                end();
                mAttacher.update();
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                end();
            }
        });
    }

    @Override
    public void initView() {
        photoView = (MQPhotoView) findViewById(R.id.photoView);
        mAttacher = new MQPhotoViewAttacher(photoView);
        mAttacher.setOnViewTapListener(mOnViewTapListener);
        mAttacher.setOnPhotoTapListener(mOnPhotoTapListener);
    }

    private MQPhotoViewAttacher.OnViewTapListener mOnViewTapListener = new MQPhotoViewAttacher.OnViewTapListener() {
        // 点击图片外区域 事件
        @Override
        public void onViewTap(View view, float x, float y) {
            onBackPressed();
        }
    };

    private MQPhotoViewAttacher.OnPhotoTapListener mOnPhotoTapListener = new MQPhotoViewAttacher.OnPhotoTapListener() {
        // 点击图片区域 事件
        @Override
        public void onPhotoTap(View view, float x, float y) {
            onBackPressed();
        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.activity_webimg;
    }

    @Override
    public void initTitle(WFYTitle mTitle) {
        mTitle.setVisibility(View.GONE);
    }

    @Override
    protected String getPageName() {
        return null;
    }

}
