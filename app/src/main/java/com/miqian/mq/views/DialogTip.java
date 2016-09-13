package com.miqian.mq.views;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.miqian.mq.R;


public abstract class DialogTip extends Dialog {


    private TextViewEx tvTip;
    private TextView tvSure;
    private TextView tvTitle;

    /**
     * @param context 只有确定按钮的提示框
     * @author Tuliangtan
     */
    public DialogTip(Activity context) {
        super(context, R.style.Dialog);
        this.setContentView(R.layout.dialog_tip);
        initViewCode();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void initViewCode() {
        tvTip = (TextViewEx) findViewById(R.id.tv_tip);
        tvSure = (TextView) findViewById(R.id.tv_sure);
        tvTitle = (TextView) findViewById(R.id.tv_title);

        tvSure.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                dismiss();
            }
        });
    }

    public void setInfo(String info) {
        tvTip.setText(info, true);
    }

    public void setSureInfo(String mSureInfo) {
        tvSure.setText(mSureInfo);
    }
    public void setTitle(String title) {
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(title);
    }
}
