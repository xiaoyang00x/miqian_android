package com.miqian.mq.views;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.miqian.mq.R;


public abstract class DialogTip extends Dialog {


    private final String mInfo;
    private TextViewEx tvTip;

    /**
     * @param context 只有确定按钮的提示框
     * @author Tuliangtan
     */
    public DialogTip(Activity context, String info) {
        super(context, R.style.Dialog);
        this.setContentView(R.layout.dialog_tip);
        this.mInfo = info;
        initViewCode();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void initViewCode() {
        tvTip = (TextViewEx) findViewById(R.id.tv_tip);
        tvTip.setText(mInfo, true);
        Button btn_sure = (Button) findViewById(R.id.btn_sure);
        btn_sure.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                dismiss();
            }
        });
    }
}
