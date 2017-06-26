package com.miqian.mq.views;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.miqian.mq.R;


public abstract class DialogJxSave extends Dialog {


    private Button btnSure;

    public DialogJxSave(Activity context) {
        super(context, R.style.Dialog);
        this.setContentView(R.layout.dialog_jx_save);
        initViewCode();
    }

    public abstract void positionBtnClick();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setCancelable(false);
        this.setCanceledOnTouchOutside(false);
    }

    private void initViewCode() {
        btnSure = (Button) findViewById(R.id.btn_sure);

        btnSure.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                dismiss();
                positionBtnClick();
            }
        });
    }
}
