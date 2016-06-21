package com.miqian.mq.views;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.miqian.mq.R;
import com.umeng.analytics.MobclickAgent;


public abstract class DialogTransferTip extends Dialog {


    private final String mInfo;
    private TextViewEx tvTip;

    /**
     * @param context 折让比过高或过低折让比
     * @author Tuliangtan
     */
    public DialogTransferTip(Activity context, String info) {
        super(context, R.style.Dialog);
        this.setContentView(R.layout.dialog_transfer_tip);
        this.mInfo = info;
        initViewCode();
    }

    public abstract void positionBtnClick();

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
                positionBtnClick();
            }
        });
        Button btNegative = (Button) findViewById(R.id.btn_cancel);
        btNegative.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                dismiss();
            }
        });
    }
}
