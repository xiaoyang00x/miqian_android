package com.miqian.mq.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.miqian.mq.R;

import java.util.ArrayList;

/**
 * 手势锁九宫格view
 * Created by wangduo on 15/12/4.
 */
public class GestureLockView extends View {

    private Paint mPaint;

    private Bitmap bitmap_circle_nor, bitmap_circle_press, bitmap_circle_error, bitmap_line_pressed, bitmap_line_error;
    private int movingX, movingY; // 当前手指所在位置坐标
    private boolean isStart; // 绘制开始
    private boolean isFinish; // 绘制结束
    private ArrayList<GestureLockPoint> selectedPointsList;
    private GestureLockPoint[][] points;

    private boolean isInit; // 初始化九宫格一次

    private int width; // view宽度
    private int paddingIn = 30; // 九宫格圈之间间距
    private int bitmapR = 60; // 圆圈的目标直径

    public GestureLockView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() { // 初始化资源
        points = new GestureLockPoint[3][3];
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        selectedPointsList = new ArrayList<>();
        bitmap_circle_nor = BitmapFactory.decodeResource(getResources(), R.drawable.gesture_circle_great_nor);
        bitmap_circle_press = BitmapFactory.decodeResource(getResources(), R.drawable.gesture_circle_great_press);
        bitmap_circle_error = BitmapFactory.decodeResource(getResources(), R.drawable.gesture_circle_great_error);
        bitmap_line_pressed = BitmapFactory.decodeResource(getResources(), R.drawable.gesture_line_press);
        bitmap_line_error = BitmapFactory.decodeResource(getResources(), R.drawable.gesture_line_error);
        paddingIn = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, paddingIn, getResources().getDisplayMetrics());
        bitmapR = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, bitmapR, getResources().getDisplayMetrics());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getMeasuredWidth();
        setMeasuredDimension(width, width);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        initPoints();
        point2Canvas(canvas);
        line2Canvas(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        isFinish = false;
        movingX = (int) event.getX();
        movingY = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (null != onPatterChangeListener) {
                    onPatterChangeListener.onPatterStart();
                }
                reset();
                // 当前手指触碰的点(不在九宫格内则为null)
                GestureLockPoint curPoint = isXYInPoint(movingX, movingY);
                isStart = null != curPoint;
                if (isStart) {
                    mHandler.removeCallbacks(runnable);
                    curPoint.state = GestureLockPoint.STATE_PRESS;
                    selectedPointsList.add(curPoint);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                curPoint = isXYInPoint(movingX, movingY);
                if (isStart && null != curPoint && !selectedPointsList.contains(curPoint)) {
                    curPoint.state = GestureLockPoint.STATE_PRESS;
                    selectedPointsList.add(curPoint);
                }
                break;
            case MotionEvent.ACTION_UP:
                isFinish = true;
                break;
        }
        if (isFinish) {
            if (selectedPointsList.size() <= 1) {
                reset();
            } else if (null != onPatterChangeListener) {
                StringBuilder sb = new StringBuilder();
                for (GestureLockPoint point : selectedPointsList) {
                    sb.append(point.index);
                }
                onPatterChangeListener.onPatterChanged(sb.toString());
            }
        }
        invalidate();
        return true;
    }

    /**
     * 重置已选点
     */
    private void reset() {
        for (GestureLockPoint point : selectedPointsList) {
            point.state = GestureLockPoint.STATE_NOR;
        }
        selectedPointsList.clear();
    }

    /**
     * 重置已选点并刷新
     */
    public void resetAndInvalidate() {
        reset();
        invalidate();
    }

    /**
     * 判断x，y坐标所在的位置是否为九宫格中的点
     *
     * @param x
     * @param y
     * @return
     */
    private GestureLockPoint isXYInPoint(int x, int y) {
       /* for (int i = 0; i < points.length; i++) {
            for (int j = 0; j < points[i].length; j++) {
                if (isXYInPoint(x, y, points[i][j])) {
                    return points[i][j];
                }
            }
        }*/
        for (GestureLockPoint[] pointTemp : points) {
            for (GestureLockPoint point : pointTemp) {
                if (isXYInPoint(x, y, point)) {
                    return point;
                }
            }
        }
        return null;
    }

    /**
     * 判断x，y坐标所在的位置是否在给定point点内
     *
     * @param x
     * @param y
     * @param point
     * @return
     */
    private boolean isXYInPoint(int x, int y, GestureLockPoint point) {
        if (point == null) {
            return false;
        }
        return Math.sqrt((point.pointX - x) * (point.pointX - x) + (point.pointY - y) * (point.pointY - y)) < bitmapR * 0.6;
    }

    /**
     * 画点
     *
     * @param canvas 画布
     */
    private void point2Canvas(Canvas canvas) {
        for (GestureLockPoint[] pointTemp : points) {
            for (GestureLockPoint point : pointTemp) {
                Matrix matrix = new Matrix();
                float sx = 1.0f;
                matrix.setScale(sx, sx);
                matrix.postTranslate(point.pointX - bitmapR * sx * 0.5f, point.pointY - bitmapR * sx * 0.5f);
                if (point.state == GestureLockPoint.STATE_NOR) {
                    canvas.drawBitmap(bitmap_circle_nor, matrix, mPaint);
                } else if (point.state == GestureLockPoint.STATE_PRESS) {
                    canvas.drawBitmap(bitmap_circle_press, matrix, mPaint);
                } else if (point.state == GestureLockPoint.STATE_ERROR) {
                    canvas.drawBitmap(bitmap_circle_error, matrix, mPaint);
                }
            }
        }
        /*for (int i = 0; i < points.length; i++) {
            for (int j = 0; j < points[i].length; j++) {
                Matrix matrix = new Matrix();
                float sx = 1.0f;
                matrix.setScale(sx, sx);
                matrix.postTranslate(points[i][j].pointX - bitmapR * sx * 0.5f, points[i][j].pointY - bitmapR * sx * 0.5f);
                if (points[i][j].state == GestureLockPoint.STATE_NOR) {
                    canvas.drawBitmap(bitmap_circle_nor, matrix, mPaint);
                } else if (points[i][j].state == GestureLockPoint.STATE_PRESS) {
                    canvas.drawBitmap(bitmap_circle_press, matrix, mPaint);
                } else if (points[i][j].state == GestureLockPoint.STATE_ERROR) {
                    canvas.drawBitmap(bitmap_circle_error, matrix, mPaint);
                }
            }
        }*/
    }

    /**
     * 画线 根据限定条件
     *
     * @param canvas
     */
    private void line2Canvas(Canvas canvas) {
        if (selectedPointsList.size() > 0) {
            GestureLockPoint pStart = selectedPointsList.get(0);
            for (GestureLockPoint pEnd : selectedPointsList) {
                if (pStart != pEnd) {
                    line2Canvas(canvas, pStart, pEnd, true);
                    pStart = pEnd;
                }
            }
            if (!isFinish) {
                line2Canvas(canvas, pStart, new GestureLockPoint(movingX, movingY), false);
            }
        }
    }

    /**
     * 画线
     *
     * @param canvas    画布
     * @param pStart    起始点
     * @param pEnd      终结点
     * @param isTwoNode 是否为两个九宫格的结点
     */
    private void line2Canvas(Canvas canvas, GestureLockPoint pStart, GestureLockPoint pEnd, boolean isTwoNode) {
        float degree = GestureLockPoint.degree(pStart, pEnd);
        canvas.rotate(degree, pStart.pointX, pStart.pointY);
        Matrix matrix = new Matrix();
        matrix.setScale(1f + GestureLockPoint.distance(pStart, pEnd) / bitmap_line_pressed.getWidth(), 2);
        matrix.postTranslate(pStart.pointX - bitmap_line_pressed.getWidth() / 2, pStart.pointY - bitmap_line_pressed.getWidth() / 2);
        if (pStart.state == GestureLockPoint.STATE_PRESS) {
            canvas.drawBitmap(bitmap_line_pressed, matrix, mPaint);
        } else if (pStart.state == GestureLockPoint.STATE_ERROR) {
            canvas.drawBitmap(bitmap_line_error, matrix, mPaint);
        }
        canvas.rotate(-degree, pStart.pointX, pStart.pointY);
    }

    /**
     * 初始化九宫格point
     */
    private void initPoints() {
        if (isInit) {
            return;
        }
        int offsetX = (width - paddingIn * 2 - bitmapR * 3) / 2;
        int offsetY = offsetX;
        resize();
        for (int i = 0; i < points.length; i++) {
            for (int j = 0; j < points[i].length; j++) {
                points[i][j] = new GestureLockPoint();
                points[i][j].pointX = (int) (offsetX + (j + 0.5) * bitmapR + j * paddingIn);
                points[i][j].pointY = (int) (offsetY + (i + 0.5) * bitmapR + i * paddingIn);
                points[i][j].state = GestureLockPoint.STATE_NOR;
                points[i][j].index = i * 3 + j;
            }
        }
        isInit = true;
    }

    // 丈量九宫格中每个圈的大小 －－ 根据左右间距和圈之间间距来决定
    private void resize() {
        Matrix matrix = new Matrix();
        int w = bitmap_circle_nor.getWidth();
        float scaleX = (float) (1.0 * bitmapR / w);
        matrix.postScale(scaleX, scaleX);
        bitmap_circle_nor = Bitmap.createBitmap(bitmap_circle_nor, 0, 0, w, w, matrix, true);
        bitmap_circle_press = Bitmap.createBitmap(bitmap_circle_press, 0, 0, w, w, matrix, true);
        bitmap_circle_error = Bitmap.createBitmap(bitmap_circle_error, 0, 0, w, w, matrix, true);
    }

    // 手势绘制有误
    public void showErrorState() {
        for (GestureLockPoint point : selectedPointsList) {
            point.state = GestureLockPoint.STATE_ERROR;
        }
        invalidate();
        mHandler.postDelayed(runnable, 1000L);
    }

    // 延时消失
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            resetAndInvalidate();
        }
    };

    private Handler mHandler = new Handler();

    private OnPatterChangeListener onPatterChangeListener;

    public void setOnPatterChangeListener(OnPatterChangeListener onPatterChangeListener) {
        this.onPatterChangeListener = onPatterChangeListener;
    }

    public interface OnPatterChangeListener {

        void onPatterChanged(String password); // 绘制完成，返回绘制手势

        void onPatterStart(); // 绘制开始

    }

}
