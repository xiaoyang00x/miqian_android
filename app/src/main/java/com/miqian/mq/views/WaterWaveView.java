package com.miqian.mq.views;

/**
 * Created by Jackie on 2015/12/21.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;


public class WaterWaveView extends View {

    private Handler mHandler;
    private long c = 0L;
    private boolean mStarted = false;
    private final float f = 0.033f;
    private final int mColor = Color.WHITE;
    private float mAmplitude = 16.0f; // 振幅
    private final Paint mPaint = new Paint();
    private final Paint mPaint1 = new Paint();
    private Path mPath;

    public WaterWaveView(Context paramContext) {
        super(paramContext);
        init();
    }

    public WaterWaveView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        init();
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


    private void init() {
        mPaint.setStrokeWidth(1.0f);
        mPaint.setAntiAlias(true);
        mPaint.setColor(mColor);
        mPaint.setAlpha(100);

        mPaint1.setStrokeWidth(1.0f);
        mPaint1.setAntiAlias(true);
        mPaint1.setColor(mColor);
        mPaint1.setAlpha(255);
        mPath = new Path();
        mHandler = new Handler() {
            @Override
            public void handleMessage(android.os.Message msg) {
                if (msg.what == 0) {
                    invalidate();
                    if (mStarted) {
                        mHandler.sendEmptyMessageDelayed(0, 100L);
                    }
                }
            }
        };
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();
        if ((!mStarted) || (width == 0) || (height == 0)) {
            return;
        }
        if (this.c >= 8388607L) {
            this.c = 0L;
        }
        this.c = (1L + this.c);
        float f1 = height - mAmplitude;
        mPath.reset();
        setPath(width, height, f1, 1.6f);
        canvas.drawPath(mPath, mPaint);
        setPath(width, height, f1, 1.8f);
        canvas.drawPath(mPath, mPaint1);
        super.onDraw(canvas);
    }

    private void setPath(int width, int height, float f1, float radin){
        mPath.reset();
        int x;
        for (int i = 0; i < width; i++) {
            x = i;
            float y = (float) (f1 - mAmplitude * Math.sin(Math.PI * (radin * (x + this.c * width * this.f)) / width));
            if (i == 0) {
                mPath.moveTo(x, y);
            }
            mPath.quadTo(x, y, x + 1, y);
        }
        mPath.lineTo(width, height + 1);
        mPath.lineTo(0, height + 1);
        mPath.close();
    }
}
