package com.miqian.mq.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class CircleBar extends View {

  private static final String TAG = "CircleBar";

  private RectF mColorWheelRectangle = new RectF();
  private Paint mDefaultWheelPaint;
  private Paint mColorWheelPaint;
  private Paint textPaint;
  private float mColorWheelRadius;
  private float circleStrokeWidth;
  private float pressExtraStrokeWidth;
  private String mText;
  private int mCount;
  private float mSweepAnglePer;
  private float mSweepAngle;
  private int mTextSize;
  BarAnimation anim;

  public CircleBar(Context context) {
    super(context);
    init(null, 0);
  }

  public CircleBar(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(attrs, 0);
  }

  public CircleBar(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init(attrs, defStyle);
  }

  private void init(AttributeSet attrs, int defStyle) {

    circleStrokeWidth = dip2px(getContext(), 4);
    pressExtraStrokeWidth = dip2px(getContext(), 2);
    mTextSize = dip2px(getContext(), 20);

    mColorWheelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    //mColorWheelPaint.setColor(0xFF29a6f6);
    mColorWheelPaint.setColor(0xFFFF0000);
    mColorWheelPaint.setStyle(Style.STROKE);
    mColorWheelPaint.setStrokeWidth(circleStrokeWidth);

    mDefaultWheelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    mDefaultWheelPaint.setColor(0XFFABBAC2);
    mDefaultWheelPaint.setStyle(Style.STROKE);
    mDefaultWheelPaint.setStrokeWidth(circleStrokeWidth);

    textPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);
    textPaint.setColor(0x33333333);
    textPaint.setStyle(Style.FILL_AND_STROKE);
    textPaint.setTextAlign(Align.LEFT);
    textPaint.setTextSize(mTextSize);

    mText = "0";//
    mSweepAngle = 0;

    anim = new BarAnimation();
    anim.setDuration(2000);
  }

  @Override protected void onDraw(Canvas canvas) {
    canvas.drawArc(mColorWheelRectangle, -90, 360, false, mDefaultWheelPaint);
    canvas.drawArc(mColorWheelRectangle, -90, mSweepAnglePer, false, mColorWheelPaint);
    //Rect bounds = new Rect();
    //String textstr = mCount + "";
    //textPaint.getTextBounds(textstr, 0, textstr.length(), bounds);
    //canvas.drawText(textstr + "",
    //    (mColorWheelRectangle.centerX()) - (textPaint.measureText(textstr) / 2),
    //    mColorWheelRectangle.centerY() + bounds.height() / 2, textPaint);
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
    int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
    int min = Math.min(width, height);
    setMeasuredDimension(min, min);
    mColorWheelRadius = min - circleStrokeWidth - pressExtraStrokeWidth;

    mColorWheelRectangle.set(circleStrokeWidth + pressExtraStrokeWidth,
        circleStrokeWidth + pressExtraStrokeWidth, mColorWheelRadius, mColorWheelRadius);
  }

  @Override public void setPressed(boolean pressed) {
    //// TODO: 9/6/15 暂不支持press
    //Log.i(TAG, "call setPressed ");
    //
    //if (pressed) {
    //  mColorWheelPaint.setColor(0xFF165da6);
    //  mColorWheelPaint.setColor(0xFF0000ff);
    //  textPaint.setColor(0xFF070707);
    //  mColorWheelPaint.setStrokeWidth(circleStrokeWidth + pressExtraStrokeWidth);
    //  mDefaultWheelPaint.setStrokeWidth(circleStrokeWidth + pressExtraStrokeWidth);
    //  textPaint.setTextSize(mTextSize - pressExtraStrokeWidth);
    //} else {
    //  mColorWheelPaint.setColor(0xFF29a6f6);
    //  mColorWheelPaint.setColor(0xFFff0000);
    //  textPaint.setColor(0xFF333333);
    //  mColorWheelPaint.setStrokeWidth(circleStrokeWidth);
    //  mDefaultWheelPaint.setStrokeWidth(circleStrokeWidth);
    //  textPaint.setTextSize(mTextSize);
    //}
    //super.setPressed(pressed);
    //this.invalidate();
  }

  public void startCustomAnimation() {
    this.startAnimation(anim);
  }

  public void setText(String text) {
    mText = text;
    this.startAnimation(anim);
  }

  public void setSweepAngle(float sweepAngle) {
    mSweepAngle = sweepAngle;
    invalidate();
  }

  public class BarAnimation extends Animation {
    /**
     * Initializes expand collapse animation, has two types, collapse (1) and expand (0).
     *
     * size defined in xml.
     * 1 will collapse view and set to gone
     */
    public BarAnimation() {

    }

    @Override protected void applyTransformation(float interpolatedTime, Transformation t) {
      super.applyTransformation(interpolatedTime, t);
      if (interpolatedTime < 1.0f) {
        mSweepAnglePer = interpolatedTime * mSweepAngle;
        mCount = (int) (interpolatedTime * Float.parseFloat(mText));
      } else {
        mSweepAnglePer = mSweepAngle;
        mCount = Integer.parseInt(mText);
      }
      postInvalidate();
    }
  }

  public static int dip2px(Context context, float dipValue) {
    final float scale = context.getResources().getDisplayMetrics().density;
    return (int) (dipValue * scale + 0.5f);
  }

  public void setMSweepAnglePer(float mSweepAnglePer) {
    this.mSweepAnglePer = mSweepAnglePer;
    mSweepAngle = mSweepAnglePer;

  }
}
