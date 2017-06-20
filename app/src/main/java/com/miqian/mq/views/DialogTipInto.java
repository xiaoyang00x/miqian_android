package com.miqian.mq.views;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.miqian.mq.R;


public abstract class DialogTipInto extends Dialog {


//    private TextView tvTip;
//    private TextView tvTitle;
    private Button tvSure;

    /**
     * @param context 只有确定按钮的提示框
     * @author Tuliangtan
     */
    public DialogTipInto(Activity context) {
        super(context, R.style.Dialog);
        this.setContentView(R.layout.dialog_tip_into);
        initViewCode();
    }

    public abstract void positionBtnClick();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void initViewCode() {
//        tvTitle = (TextView) findViewById(R.id.tv_title);
//        tvTip = (TextView) findViewById(R.id.tv_tip);
        tvSure = (Button) findViewById(R.id.tv_sure);

        tvSure.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                dismiss();
                positionBtnClick();
            }
        });
    }
}
