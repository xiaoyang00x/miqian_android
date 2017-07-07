package com.miqian.mq.views;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.activity.WebActivity;
import com.miqian.mq.net.Urls;
import com.miqian.mq.utils.Uihelper;
import com.umeng.analytics.MobclickAgent;


public abstract class DialogTradePassword extends Dialog {

    private TextView titleText;
    private TextView textLaw;
    private EditText et_password;
    private TextView forgetPassword;

    public final static int TYPE_SETPASSWORD = 0;
    public final static int TYPE_INPUTPASSWORD = 1;
    public final static int TYPE_INPUTPASSWORD_LaunchTransfer = 2;//发起转让
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
        textLaw = (TextView) findViewById(R.id.text_law);
        textLaw.setOnClickListener(onClickListener);
        forgetPassword = (TextView) findViewById(R.id.forget_psw);
        forgetPassword.setOnClickListener(onClickListener);
        et_password = (EditText) findViewById(R.id.et_password);
        if (mType == TYPE_INPUTPASSWORD_LaunchTransfer){
            textLaw.setText("《秒钱债权转让协议》");
        }
        if (mType == TYPE_INPUTPASSWORD || mType == TYPE_INPUTPASSWORD_LaunchTransfer) {
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
                int tipId = 0;
                if (mType == TYPE_INPUTPASSWORD || mType == TYPE_INPUTPASSWORD_LaunchTransfer) {
                    MobclickAgent.onEvent(mContext, "1061");
                    tipId = R.string.tip_password_pc;
                } else {
                    tipId = R.string.tip_password;
                    MobclickAgent.onEvent(mContext, "1063");
                }
                String text = et_password.getText().toString();
                if (TextUtils.isEmpty(text)) {
                    Uihelper.showToast(mContext, "密码不能为空");
                } else {
                    if (text.length() >= 6 && text.length() <= 16) {
                        positionBtnClick(text);
                        et_password.setText("");
                    } else {
                        Uihelper.showToast(mContext, tipId);
                    }
                }
            }
        });
    }

    public void setTitle(String title) {
        if (titleText != null) {
            titleText.setText(title);
        }
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.text_law:
//                    if (mType == TYPE_INPUTPASSWORD_LaunchTransfer){
//                        WebActivity.startActivity(mContext, Urls.web_transfer_law);
//                    }else {
//                        WebActivity.startActivity(mContext, Urls.web_recharge_law);
//                    }

                    break;
                case R.id.forget_psw:
//                    Intent intent = new Intent(mContext, TradePsCaptchaActivity.class);
//                    intent.putExtra("realNameStatus", "1");
//                    mContext.startActivity(intent);
                    break;
                default:
                    break;
            }
        }
    };

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
