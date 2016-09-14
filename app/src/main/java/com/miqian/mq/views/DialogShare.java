package com.miqian.mq.views;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.entity.Popup;

/**
 * 首页运动活动对话框
 */
public abstract class DialogShare extends Dialog {

    private Button btn_confirm;
    private View btn_close;
    private TextView tv_title;
    private TextView tv_content;
    private RelativeLayout frameTitle;
    private Popup popup;

    public abstract void share();

    public DialogShare(Context context, Popup popup) {
        super(context, R.style.Dialog);
        this.setContentView(R.layout.home_dialog);
        setCancelable(false);
        this.popup = popup;
        initView();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void initView() {
        frameTitle = (RelativeLayout) findViewById(R.id.frame_title);
        frameTitle.setBackgroundResource(R.drawable.bg_promote_top);
        btn_confirm = (Button)findViewById(R.id.btn_confirm);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_content = (TextView) findViewById(R.id.tv_content);
        btn_close = findViewById(R.id.btn_close);

        tv_title.setText(popup.getPopup_title());
        tv_content.setText(popup.getPopup_content());
        btn_confirm.setText(popup.getPopup_button());

        btn_confirm.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                share();
                dismiss();
            }
        });

        btn_close.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                dismiss();
            }
        });
    }

}
