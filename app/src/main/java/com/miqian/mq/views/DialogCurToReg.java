package com.miqian.mq.views;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.miqian.mq.R;

/**
 * Created by TuLiangtan on 2016/1/21.
 * 活期转定期说明
 */
public abstract class DialogCurToReg extends Dialog {


    public DialogCurToReg(Context context) {
        super(context, R.style.Dialog);
        this.setContentView(R.layout.dialog_cur_to_reg);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Button btConfirm = (Button) findViewById(R.id.bt_confirm);
        btConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
