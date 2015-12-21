package com.miqian.mq.views;

/**
 * Created by Jackie on 2015/12/21.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;


public class WaterWaveView extends View {

    private Handler mHandler;
    private long c = 0L;
    private boolean mStarted = false;
    private final float f = 0.033f;
    private int mAlpha = 100;
    private final int mColor = Color.WHITE;
    private float mAmplitude = 16.0f; // 振幅
    private final Paint mPaint = new Paint();
    private final Paint mPaint1 = new Paint();
    private float mWateLevel = 0.19f;
//    private Path mPath;

    public WaterWaveView(Context paramContext) {
        super(paramContext);
        init(paramContext);
    }

    public WaterWaveView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        init(paramContext);
    }

    /**
     * @category 开始波动
     */
    public void startWave() {
        if (!mStarted) {
            this.c = 0L;
            mStarted = true;
            this.mHandler.sendEmptyMessage(0);
        }
    }

    /**
     * @category 停止波动
     */
    public void stopWave() {
        if (mStarted) {
            this.c = 0L;
            mStarted = false;
            this.mHandler.removeMessages(0);
        }
    }


    private void init(Context context) {
        mPaint.setStrokeWidth(1.0F);
        mPaint.setColor(mColor);
        mPaint.setAlpha(mAlpha);

        mPaint1.setStrokeWidth(1.0F);
        mPaint1.setColor(mColor);
        mPaint1.setAlpha(255);
//        mPath = new Path();
        mHandler = new Handler() {
            @Override
            public void handleMessage(android.os.Message msg) {
                if (msg.what == 0) {
                    invalidate();
                    if (mStarted) {
                        mHandler.sendEmptyMessageDelayed(0, 60L);
                    }
                }
            }
        };
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        int width = getWidth();
        int height = getHeight();
        if ((!mStarted) || (width == 0) || (height == 0)) {
            return;
        }
        if (this.c >= 8388607L) {
            this.c = 0L;
        }
        this.c = (1L + this.c);
//        float f1 = height * (1.0F - mWateLevel);
//        int top = (int) (f1 + mAmplitude);
        float f1 = height - mAmplitude;
        int top = height;
//        int n = (int) (f1 - this.mAmplitude
//                * Math.sin(Math.PI * (2.0F * (0.0F + this.c * width * this.f)) / width));
        int startX = 0;
        int stopX = 0;
//        while (stopX < width) {
//            int startY = (int) (f1 - mAmplitude
//                    * Math.sin(Math.PI * (2.0F * (stopX + this.c * width * this.f))
//                    / width));
//            canvas.drawLine(startX, n, stopX, startY, mPaint);
//            canvas.drawLine(stopX, startY, stopX, top, mPaint);
//            int i4 = stopX + 1;
//            startX = stopX;
//            stopX = i4;
//            n = startY;
//        }
//
//        int startX, endX;
//        if (mWaterLevel > 0.50F) {
//            startX = (int) (centerX - (mScreenWidth + offsetWidth) / 4 + offsetWidth);
//            endX = (int) (centerX + (mScreenWidth + offsetWidth) / 4 - offsetWidth);
//        } else {
//            startX = (int) (centerX - (mScreenWidth + offsetWidth) / 4 + offsetWidth - mAmplitude);
//            endX = (int) (centerX + (mScreenWidth + offsetWidth) / 4 - offsetWidth + mAmplitude);
//        }
        // 波浪效果
        while (startX < width) {
            int startY = (int)
                    (f1 - mAmplitude * Math.sin(Math.PI * (1.6F * (startX + this.c * width * this.f)) / width));
            canvas.drawLine(startX, startY, startX, top, mPaint);
            startX++;
        }

        //起始振动X坐标，结束振动X坐标
        int startX1 = 0;

        // 波浪效果
        while (startX1 < width) {
            int startY1 = (int)
                    (f1 - mAmplitude * Math.sin(Math.PI * (1.8F * (startX1 + this.c * width * this.f)) / width));
            canvas.drawLine(startX1, startY1, startX1, top, mPaint1);
            startX1++;
        }


        canvas.restore();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
//        HardwareAccUtils.setLayerTypeAsSoftware(this);
    }

    public void setAmplitude(float amplitued) {
        mAmplitude = amplitued;
    }

    public void setWaterAlpha(float alpha) {
        this.mAlpha = ((int) (255.0F * alpha));
        mPaint.setAlpha(this.mAlpha);
    }

    public void setWaterLevel(float paramFloat) {
        mWateLevel = paramFloat;
    }
}
