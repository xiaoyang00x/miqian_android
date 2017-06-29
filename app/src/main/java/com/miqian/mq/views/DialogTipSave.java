package com.miqian.mq.views;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.miqian.mq.R;


public abstract class DialogTipSave extends Dialog {


    private final String mTitle;
    private final String mText;
    private TextView tvTip;
    private TextView tvTitle;
    private Button tvSure;

    /**
     * @param context 只有确定按钮的提示框
     * @author Tuliangtan
     */
    public DialogTipSave(Activity context,String title,String text) {
        super(context, R.style.Dialog);
        this.setContentView(R.layout.dialog_tip_into);
        this.mTitle=title;
        this.mText=text;
        initViewCode();
    }

    public abstract void positionBtnClick();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void initViewCode() {
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvTip = (TextView) findViewById(R.id.tv_tip);
        tvSure = (Button) findViewById(R.id.tv_sure);
        tvTitle.setText(mTitle);
        tvTip.setText(mText);
        tvSure.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                dismiss();
                positionBtnClick();
            }
        });
    }
}
