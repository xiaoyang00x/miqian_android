package com.miqian.mq.views;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.utils.FormatUtil;
import com.miqian.mq.utils.MyTextWatcher;


public abstract class DialogPay extends Dialog {

    private TextView tipText;
    private EditText editMoney;

    /**
     * @param context 内容 * @param position_Text 取消按钮的内容 如：取消，或是其他操作等
     *                确定按钮的内容 如：去认证，确定等
     * @author Tuliangtan
     */
    public DialogPay(Context context) {
        super(context, R.style.Dialog);
        this.setContentView(R.layout.dialog_pay);
        initViewCode();
    }

    public abstract void positionBtnClick(String s);

    public abstract void negativeBtnClick();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void initViewCode() {
        tipText = (TextView) findViewById(R.id.text_tip);
        editMoney = (EditText) findViewById(R.id.edit_money);
        editMoney.addTextChangedListener(new MyTextWatcher() {

            @Override
            public void myAfterTextChanged(Editable s) {
//				setRollEnabled();
                try {
                    String temp = s.toString();
                    if (temp.matches(FormatUtil.PATTERN_MONEY)) {
                        return;
                    }
                    s.delete(temp.length() - 1, temp.length());
                } catch (Exception ignored) {
                }
            }
        });

        Button btNegative = (Button) findViewById(R.id.btn_cancel);
        btNegative.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                negativeBtnClick();
                dismiss();
            }
        });

        Button btn_sure = (Button) findViewById(R.id.btn_sure);
        btn_sure.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                positionBtnClick(editMoney.getText().toString());
//                editMoney.setText("");
            }
        });
    }

    public void setTipText(String title) {
        if (tipText != null) {
            tipText.setVisibility(View.VISIBLE);
            tipText.setText(title);
        }
    }

    public void setTipTextVisibility(int visibility) {
        if (tipText != null) {
            tipText.setVisibility(visibility);
        }
    }

    public void setEditMoney(String money) {
        if (editMoney != null) {
            editMoney.setText(money);
        }
    }

    public void setEditMoneyHint(String hint) {
        if (editMoney != null) {
            editMoney.setHint(hint);
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
