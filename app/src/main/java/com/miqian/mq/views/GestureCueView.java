package com.miqian.mq.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.miqian.mq.R;


/**
 * 手势密码提示view：小九宫格
 * Created by wangduo on 15/12/4.
 */
public class GestureCueView extends View {

    private Paint mPaint;

    private Bitmap bitmap_circle_nor, bitmap_circle_press;
    private int width, height;
    private int bitmapR;

    private GestureLockPoint[][] points;

    private static final int PADIN = 5;
    private int paddingIn; // 九宫格距离父控件左右间距

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
        paddingIn = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, PADIN, getResources().getDisplayMetrics());
        bitmapR = bitmap_circle_nor.getWidth();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = bitmapR + paddingIn * 2 + bitmapR * 3;
        height = width;
        setMeasuredDimension(width, height);
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
        for (int i = 0; i < points.length; i++) {
            for (int j = 0; j < points[i].length; j++) {
                points[i][j] = new GestureLockPoint();
                points[i][j].pointX = (int) ((j + 0.5) * bitmapR + j * paddingIn);
                points[i][j].pointY = (int) ((i + 0.5) * bitmapR + i * paddingIn);
                points[i][j].state = GestureLockPoint.STATE_NOR;
                points[i][j].index = i * 3 + j;
            }
        }
        isInit = true;
    }

    /**
     * 画点
     *
     * @param canvas 画布
     */
    private void point2Canvas(Canvas canvas) {
        for (int i = 0; i < points.length; i++) {
            for (int j = 0; j < points[i].length; j++) {
                Matrix matrix = new Matrix();
//                float sx = (float) (1.0 * Math.min(screenHeight, screenWidth) / 720);
                float sx = 1.0f;
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
