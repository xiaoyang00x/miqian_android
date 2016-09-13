package com.miqian.mq.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * @author wangduo
 * @description: 可自动滚动(获取焦点)的textview
 * @email: cswangduo@163.com
 * @date: 16/9/8
 */
public class MQMarqueeTextView extends TextView {

    public MQMarqueeTextView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public MQMarqueeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MQMarqueeTextView(Context context, AttributeSet attrs,
                             int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean isFocused() {
        return true;
    }

}