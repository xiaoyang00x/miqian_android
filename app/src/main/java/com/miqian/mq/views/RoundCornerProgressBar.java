package com.miqian.mq.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.LinearLayout;
import com.miqian.mq.R;

/**
 * @author keen
 */
public class RoundCornerProgressBar extends BaseRoundCornerProgressBar {
  @SuppressLint("NewApi") public RoundCornerProgressBar(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  protected int initProgressBarLayout() {
    return R.layout.round_corner_layout;
  }

  @Override protected void setup(TypedArray typedArray, DisplayMetrics metrics) {
  }

  @Override public void setBackgroundLayoutSize(LinearLayout layoutBackground) {
    if (layoutBackground != null) {
      int height, width;
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
        width = layoutBackground.getMeasuredWidth();
        height = layoutBackground.getMeasuredHeight();
      } else {
        width = layoutBackground.getWidth();
        height = layoutBackground.getHeight();
      }
      if (height - (getPadding() * 2) == 0) {
        height = (int) dp2px(DEFAULT_PROGRESS_BAR_HEIGHT);
      }
      setBackgroundWidth(width);
      setBackgroundHeight(height);
    }
  }

  @Override protected void setGradientRadius(GradientDrawable gradient) {
    int radius = getRadius() - (getPadding() / 2);
    gradient.setCornerRadii(
        new float[] { radius, radius, radius, radius, radius, radius, radius, radius });
  }

  @Override protected void onLayoutMeasured() {
  }

  @Override protected float setLayoutProgressWidth(float ratio) {
    return (ratio > 0) ? (int) ((getBackgroundWidth() - (getPadding() * 2)) / ratio) : 0;
  }

  @Override protected float setSecondaryLayoutProgressWidth(float ratio) {
    return (ratio > 0) ? (int) ((getBackgroundWidth() - (getPadding() * 2)) / ratio) : 0;
  }
}
