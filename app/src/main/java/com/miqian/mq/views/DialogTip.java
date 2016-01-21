package com.miqian.mq.views;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.entity.UpdateInfo;

/**
 * Created by TuLiangtan on 2016/1/21.
 * 活期转定期说明
 */
public abstract class DialogTip extends Dialog {


    public DialogTip(Context context) {
        super(context, R.style.Dialog);
        this.setContentView(R.layout.dialog_tip);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImageView ivClose = (ImageView) findViewById(R.id.iv_closetip);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
