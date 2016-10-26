package com.miqian.mq.views;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.miqian.mq.R;

/**
 * 汇付账户升级提示
 */
public abstract class DialogHfTip extends Dialog {

    private TextViewEx tvTip;
    private TextView tvSure;
    private TextView tvTitle;
    private ImageView imageTip;

    /**
     * @param context 只有确定按钮的提示框
     * @author Tuliangtan
     */
    public DialogHfTip(Activity context) {
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
        imageTip = (ImageView) findViewById(R.id.image_tip);
        imageTip.setVisibility(View.GONE);

        tvSure.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                dismiss();
            }
        });

        setTitle("账户升级");
        setInfo("为保障您的账户资金安全，秒钱已全面接入资金存管，需您对账户进行升级，以保证您的资金安全。");
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
