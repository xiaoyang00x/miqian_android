package com.miqian.mq.views;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.miqian.mq.R;
import com.miqian.mq.activity.WebActivity;
import com.miqian.mq.entity.GetHomeActivity;
import com.miqian.mq.entity.Meta;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 首页运动活动对话框
 */
public class HomeDialog extends Dialog {

    private ImageView img_content;
    private ImageView btn_confirm;
    private ImageView btn_cancel;

    private GetHomeActivity mData;
    private ImageLoader imageLoader;
    private DisplayImageOptions options;

    public HomeDialog(Context context, GetHomeActivity data) {
        super(context, R.style.Dialog);
        this.setContentView(R.layout.home_dialog);
        mData = data;
        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).build();
        initView();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void initView() {
        img_content = (ImageView)findViewById(R.id.img_content);
        btn_confirm = (ImageView)findViewById(R.id.btn_confirm);
        btn_cancel = (ImageView)findViewById(R.id.btn_cancel);

        Bitmap contentBitmap = imageLoader.loadImageSync(mData.getBackgroundUrl());
        imageLoader.displayImage(mData.getBackgroundUrl(), img_content);
        imageLoader.displayImage(mData.getCloseUrl(), btn_cancel);
        imageLoader.displayImage(mData.getEnterUrl(), btn_confirm);
        btn_confirm.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                WebActivity.startActivity(getContext(), mData.getJumpUrl());
                dismiss();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                dismiss();
            }
        });
    }

}
