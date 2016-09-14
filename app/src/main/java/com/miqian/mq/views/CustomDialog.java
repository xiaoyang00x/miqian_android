package com.miqian.mq.views;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.miqian.mq.R;


public abstract class CustomDialog extends Dialog {

    public static final int CODE_TIPS = 2; // 提示

    private TextView titleText;
    private TextView remarksText;
    private Button btNegative;
    private Button btn_sure;

    /**
     * @param context 内容 * @param position_Text 取消按钮的内容 如：取消，或是其他操作等
     * @param code    确定按钮的内容 如：去认证，确定等
     * @author Tuliangtan
     */
    public CustomDialog(Context context, int code) {
        super(context, R.style.Dialog);
        this.setContentView(R.layout.dialog);
        initViewCode(code);
    }

    public abstract void positionBtnClick();

    public abstract void negativeBtnClick();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void initViewCode(int code) {
        titleText = (TextView) findViewById(R.id.title);
        remarksText = (TextView) findViewById(R.id.remarks);

        btNegative = (Button) findViewById(R.id.btn_cancel);
        btNegative.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                negativeBtnClick();
                dismiss();
            }
        });

        btn_sure = (Button) findViewById(R.id.btn_sure);
        btn_sure.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                positionBtnClick();
            }
        });
    }

    public void setTitle(String title) {
        if (titleText != null) {
            titleText.setText(title);
        }
    }

    public void setRemarks(String content) {
        if (remarksText != null) {
            remarksText.setText(content);
        }
    }

    public void setRemarksVisibility(int visibility) {
        if (remarksText != null) {
            remarksText.setVisibility(visibility);
        }
    }

    public void setNegative(int viewCode) {
        if (btNegative != null) {
            btNegative.setVisibility(viewCode);
        }
    }

    public void setNegative(String text) {
        if (btNegative != null) {
            btNegative.setText(text);
        }
    }

    public void setPositive(int visibility) {
        if (btn_sure != null) {
            btn_sure.setVisibility(visibility);
        }
    }

    public void setPositive(String text) {
        if (btn_sure != null) {
            btn_sure.setText(text);
        }
    }
}
