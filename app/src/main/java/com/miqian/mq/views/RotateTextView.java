package com.miqian.mq.views;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by guolei_wang on 16/5/19.
 * 旋转的 TextView
 */
public class RotateTextView extends TextView {
    public RotateTextView(Context context) {
        super(context);
    }

    public RotateTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RotateTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //倾斜度45,上下左右居中
//        canvas.rotate(45, 0,0);
        canvas.rotate(45, getMeasuredWidth()/3, getMeasuredHeight()/2);
        super.onDraw(canvas);
    }
}
