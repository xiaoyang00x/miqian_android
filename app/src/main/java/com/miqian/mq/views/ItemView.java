package com.miqian.mq.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.miqian.mq.R;

/**
 * Created by Joy on 2015/9/9.
 */
public class ItemView extends LinearLayout {

    private Context mContext;

    public ItemView(Context context) {
        super(context);
        mContext = context;
    }

    public ItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        this.setOrientation(HORIZONTAL);
//        this.setBackgroundResource(R.drawable.list_click);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ItemView, defStyleAttr, 0);
        float itemHeight = ta.getDimension(R.styleable.ItemView_item_Height, 50);
//        LayoutParams itemParams = new LayoutParams(LayoutParams.MATCH_PARENT, dp2px(mContext, itemHeight));
//        this.setLayoutParams(itemParams);
        int leftImage = ta.getResourceId(R.styleable.ItemView_itemLeftImage, -1);
        if (leftImage!=-1) {
            addLeftImage(leftImage);
        }
        String leftText = ta.getString(R.styleable.ItemView_itemLeftText);
        if (!TextUtils.isEmpty(leftText)){
            addLeftText(leftText);
        }
        Boolean hasRightArrow = ta.getBoolean(R.styleable.ItemView_itemHasRightArrow, false);
        if (hasRightArrow){
            addArrow();
        }

        String rightText = ta.getString(R.styleable.ItemView_itemRightText);
        int rightTextColor=ta.getColor(R.styleable.ItemView_itemRightTextColor, getResources().getColor(R.color.miqian_gray));
        if (!TextUtils.isEmpty(rightText)){
            addRightText(rightText, rightTextColor);
        }
        ta.recycle();
    }

    private void addRightText(String rightText, int rightTextColor) {


        TextView tvRight = new TextView(mContext);
        LayoutParams itemParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//        itemParams.gravity=Gravity.CENTER_VERTICAL|Gravity.RIGHT;
        tvRight.setLayoutParams(itemParams);
        tvRight.setText(rightText);
        tvRight.setTextColor(rightTextColor);
        tvRight.setPadding(0, 0, dp2px(mContext, 14), 0);
        addView(tvRight);

    }


    private void addArrow (){


        ImageView arrowImageView = new ImageView(mContext);
        LayoutParams itemParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//        itemParams.gravity=Gravity.CENTER_VERTICAL|Gravity.RIGHT;
        arrowImageView.setLayoutParams(itemParams);
        arrowImageView.setImageResource(R.drawable.arrow_right);
        arrowImageView.setPadding(0, 0, dp2px(mContext, 14), 0);
        addView(arrowImageView);


    }


    private void addLeftText(String leftText) {

        TextView tvLeft = new TextView(mContext);
        LayoutParams itemParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        tvLeft.setLayoutParams(itemParams);
        tvLeft.setGravity(Gravity.CENTER);
        tvLeft.setText(leftText);
        tvLeft.setTextColor(getResources().getColor(R.color.miqian_black));
        tvLeft.setPadding(dp2px(mContext, 14), 0, 0, 0);
        addView(tvLeft);


    }
    private void addLeftImage(int leftImage) {

        ImageView imageView = new ImageView(mContext);
        LayoutParams itemParams = new LayoutParams(dp2px(mContext, 60), dp2px(mContext, 25));
//        itemParams.gravity=Gravity.CENTER_VERTICAL|Gravity.LEFT;
        imageView.setLayoutParams(itemParams);
        imageView.setBackgroundResource(R.drawable.trans);
        imageView.setImageResource(leftImage);
        addView(imageView);
    }

    private int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
