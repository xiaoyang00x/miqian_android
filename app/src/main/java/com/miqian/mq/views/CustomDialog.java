package com.miqian.mq.views;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputFilter.LengthFilter;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.utils.MobileOS;
import com.miqian.mq.utils.MyTextWatcher;


public abstract class CustomDialog extends Dialog {

	public static final int CODE_IDCARD = 0; // 身份认证
	public static final int CODE_PASSWORD = 1; // 交易密码
	public static final int CODE_TIPS = 2; // 提示

	private TextView titleText;
	private TextView remarksText;
	private Button btNegative;
	private Button btn_sure;

	/**
	 * @author Tuliangtan
	 * @param context
	 *            内容 * @param position_Text 取消按钮的内容 如：取消，或是其他操作等
	 * @param code
	 *            确定按钮的内容 如：去认证，确定等
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
		EditText editFirst = (EditText) findViewById(R.id.edit_1);
		EditText editSecond = (EditText) findViewById(R.id.edit_2);
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
		switch (code) {
		case CODE_IDCARD:
			titleText.setText("实名认证");
			editFirst.setHint("请输入姓名");
			editFirst.setInputType(InputType.TYPE_CLASS_TEXT);
			editSecond.setHint("请输入身份证号");
			
			editSecond.addTextChangedListener(new MyTextWatcher() {

				@Override
				public void myAfterTextChanged(Editable s) {
					try {
						String temp = s.toString();
						if (temp.matches("^\\d{1,17}([0-9]|[Xx])?$")) {
							return;
						}
						s.delete(temp.length() - 1, temp.length());
					} catch (Exception e) {
					}
				}
			});
			String deviceName = MobileOS.getDeviceModel();
//			vivo X3t
			if (deviceName.contains("vivo")) {
				editSecond.setInputType(InputType.TYPE_CLASS_TEXT);
			} else {
				editSecond.setInputType(InputType.TYPE_CLASS_NUMBER);
				editSecond.setKeyListener(DigitsKeyListener.getInstance("0123456789Xx"));
			}
			btn_sure.setText("下一步");
			remarksText.setText("为了您的资金安全，请先完成实名认证.");
			break;
		case CODE_PASSWORD:
			titleText.setText("设置交易密码");
			editFirst.setHint("请输入6-20位交易密码");
			editSecond.setHint("请再次输入交易密码");
			editFirst.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
			editSecond.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
			InputFilter[] filters = { new LengthFilter(20) };
			editFirst.setFilters(filters);
			editSecond.setFilters(filters);
			remarksText.setVisibility(View.GONE);
			btn_sure.setText("下一步");
			// btNegative.setVisibility(View.GONE);
			break;
		case CODE_TIPS:
			titleText.setText("提示");
			editFirst.setVisibility(View.GONE);
			editSecond.setVisibility(View.GONE);
			btNegative.setText("重新输入");
			btNegative.setVisibility(View.GONE);
			break;
		default:
			break;
		}
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
	public void setPositive(int viewCode) {
		if (btn_sure != null) {
			btn_sure.setVisibility(viewCode);
		}
	}

	public void setPositive(String text) {
		if (btn_sure != null) {
			btn_sure.setText(text);
		}
	}
}
