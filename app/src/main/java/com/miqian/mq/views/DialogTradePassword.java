package com.miqian.mq.views;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.entity.CityInfo;
import com.miqian.mq.utils.FormatUtil;
import com.miqian.mq.utils.MyTextWatcher;
import com.miqian.mq.utils.Uihelper;
import com.umeng.analytics.MobclickAgent;


public abstract class DialogTradePassword extends Dialog {

    private TextView titleText;
    private EditText et_password;
    public final static int TYPE_SETPASSWORD = 0;
    public final static int TYPE_INPUTPASSWORD = 1;
    private int mType;
    private Context mContext;

    /**
     * @param context 内容 * @param position_Text 取消按钮的内容 如：取消，或是其他操作等
     *                确定按钮的内容 如：去认证，确定等
     * @author Tuliangtan
     */
    public DialogTradePassword(Activity context, int type) {
        super(context, R.style.Dialog);
        this.setContentView(R.layout.dialog_tradepassword);
        this.mType = type;
        this.mContext = context;
        initViewCode();
    }

    public abstract void positionBtnClick(String password);

//    public abstract void negativeBtnClick();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void initViewCode() {
        titleText = (TextView) findViewById(R.id.dialog_title);
        et_password = (EditText) findViewById(R.id.et_password);

        if (mType == TYPE_INPUTPASSWORD) {
            titleText.setText("交易密码");
            et_password.setHint("请输入交易密码");
        } else {
            //设置交易密码规则:数字和字母的组合
            String digits = mContext.getResources().getString(R.string.match);
            et_password.setKeyListener(DigitsKeyListener.getInstance(digits));
        }


        Button btNegative = (Button) findViewById(R.id.btn_cancel);
        btNegative.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                if (mType == TYPE_INPUTPASSWORD) {
                    MobclickAgent.onEvent(mContext, "1060");
                } else {
                    MobclickAgent.onEvent(mContext, "1062");
                }
                dismiss();
            }
        });

        Button btn_sure = (Button) findViewById(R.id.btn_sure);
        btn_sure.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                if (mType == TYPE_INPUTPASSWORD) {
                    MobclickAgent.onEvent(mContext, "1061");
                } else {
                    MobclickAgent.onEvent(mContext, "1063");
                }
                String text = et_password.getText().toString();
                if (TextUtils.isEmpty(text)) {
                    Uihelper.showToast(mContext, "密码不能为空");
                } else {
                    positionBtnClick(text);
                }

                et_password.setText("");
            }
        });
    }

    public void setTitle(String title) {
        if (titleText != null) {
            titleText.setText(title);
        }
    }

//	public void setRemarks(String content) {
//		if (remarksText != null) {
//			remarksText.setText(content);
//		}
//	}

//	public void setNegative(int viewCode) {
//		if (btNegative != null) {
//			btNegative.setVisibility(viewCode);
//		}
//	}
//
//	public void setNegative(String text) {
//		if (btNegative != null) {
//			btNegative.setText(text);
//		}
//	}
}
