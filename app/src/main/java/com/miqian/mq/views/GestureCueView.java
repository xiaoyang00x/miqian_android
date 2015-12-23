package com.miqian.mq.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.miqian.mq.R;
import com.miqian.mq.utils.MobileDeviceUtil;


/**
 * 手势密码提示view：小九宫格
 * Created by wangduo on 15/12/4.
 */
public class GestureCueView extends View {

    private Paint mPaint;

    private Bitmap bitmap_circle_nor, bitmap_circle_press;
    private int width, height;
    private int bitmapR;
    private int offsetX, offsetY;

    private GestureLockPoint[][] points;

    private boolean isInit; // 初始化九宫格一次

    public GestureCueView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() { // 初始化资源
        points = new GestureLockPoint[3][3];
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bitmap_circle_nor = BitmapFactory.decodeResource(getResources(), R.drawable.gesture_circle_small_nor);
        bitmap_circle_press = BitmapFactory.decodeResource(getResources(), R.drawable.gesture_circle_small_press);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        initPoints();
        point2Canvas(canvas);
    }

    /**
     * 初始化九宫格point
     */
    private void initPoints() {
        if (isInit) {
            return;
        }
        width = getWidth();
        height = getHeight();
        bitmapR = bitmap_circle_nor.getWidth();
        if (height > width) {
            offsetY = (height - width) / 2;
            height = width;
        } else {
            offsetX = (width - height) / 2;
            width = height;
        }
        for (int i = 0; i < points.length; i++) {
            for (int j = 0; j < points[i].length; j++) {
                points[i][j] = new GestureLockPoint();
                points[i][j].pointX = offsetX + (j + 1) * width / 4;
                points[i][j].pointY = offsetY + (i + 1) * width / 4;
                points[i][j].state = GestureLockPoint.STATE_NOR;
                points[i][j].index = i * 3 + j;
            }
        }
        resize();
        isInit = true;
    }

    // 重新丈量 小圆圈的大小 --- 以720p下切图为标准
    private void resize() {
        int screenWidth = getContext().getResources().getDisplayMetrics().widthPixels;
        final int SIZE = 720;
        if (screenWidth != SIZE) {
            bitmapR = bitmap_circle_nor.getWidth();
            Matrix matrix = new Matrix();
            float scaleX = (float) (1.0 * screenWidth / SIZE);
            matrix.postScale(scaleX, scaleX);
            bitmap_circle_nor = Bitmap.createBitmap(bitmap_circle_nor, 0, 0, bitmapR, bitmapR, matrix, true);
            bitmap_circle_press = Bitmap.createBitmap(bitmap_circle_press, 0, 0, bitmapR, bitmapR, matrix, true);
            bitmapR = bitmap_circle_nor.getWidth();
        }
    }

    /**
     * 画点
     *
     * @param canvas 画布
     */
    private void point2Canvas(Canvas canvas) {
        int screenWidth = MobileDeviceUtil.getInstance(getContext()).getScreenWidth();
        int screenHeight = MobileDeviceUtil.getInstance(getContext()).getScreenHeight();
        for (int i = 0; i < points.length; i++) {
            for (int j = 0; j < points[i].length; j++) {
                Matrix matrix = new Matrix();
                float sx = (float) (1.0 * Math.min(screenHeight, screenWidth) / 720);
                sx = 1.0f;
                matrix.setScale(sx, sx);
                matrix.postTranslate(points[i][j].pointX - bitmapR * sx * 0.5f, points[i][j].pointY - bitmapR * sx * 0.5f);
                if (points[i][j].state == GestureLockPoint.STATE_NOR) {
                    canvas.drawBitmap(bitmap_circle_nor, matrix, mPaint);
//                    canvas.drawBitmap(bitmap_circle_nor, points[i][j].pointX - bitmapR / 2, points[i][j].pointY - bitmapR / 2, mPaint);
                } else if (points[i][j].state == GestureLockPoint.STATE_PRESS) {
                    canvas.drawBitmap(bitmap_circle_press, matrix, mPaint);
//                    canvas.drawBitmap(bitmap_circle_press, points[i][j].pointX - bitmapR / 2, points[i][j].pointY - bitmapR / 2, mPaint);
                }
            }
        }
    }

    /**
     * 根据设置的密码字符串显示已选点
     *
     * @param psw 密码字符串
     */
    public void showSelectedPoint(String psw) {
        int counter = 0;
        int pswLength = psw.length();
        for (int i = 0; i < points.length; i++) {
            for (int j = 0; j < points[i].length; j++) {
                GestureLockPoint point = points[i][j];
                if (psw.contains(String.valueOf(point.index))) {
                    point.state = GestureLockPoint.STATE_PRESS;
                    counter++;
                    if (pswLength == counter) {
                        break;
                    }
                }
            }
        }
        invalidate();
    }

    /**
     * 重置已选点并刷新
     */
    public void resetAndInvalidate() {
        for (int i = 0; i < points.length; i++) {
            for (int j = 0; j < points[i].length; j++) {
                GestureLockPoint point = points[i][j];
                point.state = GestureLockPoint.STATE_NOR;
            }
        }
        invalidate();
    }

}
