package com.miqian.mq.activity;

import android.app.Dialog;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.utils.TypeUtil;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.views.WFYTitle;


public class ChangePassWordActivity extends BaseActivity {

    private EditText et_originpassword;
    private EditText et_newpassword;
    private EditText et_newpassword2;
    private Dialog dialog;
    private int style;//区分登录密码和交易密码
    private int paypwd_status; // 0为未设置 1，为已设置

    @Override
    public void obtainData() {
    }

    @Override
    public void initView() {

        Intent intent = getIntent();
        style = intent.getIntExtra("style", 0);

        paypwd_status = intent.getIntExtra("paypwd_status", 0);

//		dialog = ProgressDialogView.create(mActivity, R.string.posting);
        et_originpassword = (EditText) findViewById(R.id.et_originpassword);
        et_newpassword = (EditText) findViewById(R.id.et_newpassword);
        et_newpassword2 = (EditText) findViewById(R.id.et_newpassword2);

        TextView tv_originpass = (TextView) findViewById(R.id.tv_originpass);
        TextView tv_newpassword = (TextView) findViewById(R.id.tv_newpassword);
        TextView tv_newpassword2 = (TextView) findViewById(R.id.tv_newpassword2);

        if (style == TypeUtil.PASSWORD_TRADE) {

            // 0为未设置 ,1为已设置
            if (paypwd_status == 0) {
                findViewById(R.id.linear_origin).setVisibility(View.GONE);
                findViewById(R.id.view_origin).setVisibility(View.GONE);
                tv_newpassword.setText("交易密码        ");
                tv_newpassword2.setText("确认交易密码");
                mTitle.setTitleText("设置交易密码");
            } else {

                tv_originpass.setText("原交易密码    ");
                tv_newpassword.setText("新交易密码    ");
                tv_newpassword2.setText("确认交易密码");
                mTitle.setTitleText("修改交易密码");
            }

        }
    }

    public void btn_click(View v) {

        String originpassword = et_originpassword.getText().toString();
        String newpassword = et_newpassword.getText().toString();
        String newpassword2 = et_newpassword2.getText().toString();

        if (style == TypeUtil.PASSWORD_TRADE) {

            if (!TextUtils.isEmpty(originpassword)) {
                if (!TextUtils.isEmpty(newpassword)) {
                    if (newpassword.length() >= 6 && newpassword.length() <= 20) {
                        if (!TextUtils.isEmpty(newpassword2)) {
                            if (newpassword2.equals(newpassword)) {
                                // 提交数据
                                dialog.show();
                                summit(originpassword, newpassword);
                            } else {
                                Uihelper.showToast(mActivity, "两次新密码不一致");
                            }

                        } else {
                            Uihelper.showToast(mActivity, "请再次输入新密码");
                        }

                    } else {
                        Uihelper.showToast(mActivity, R.string.tip_password);
                    }

                } else {
                    Uihelper.showToast(mActivity, "请输入新密码");
                }
            } else {
                Uihelper.showToast(mActivity, "请输入原密码");
            }
        } else if (style == 2) {

            if (paypwd_status == 1) {
                if (!TextUtils.isEmpty(originpassword)) {
                    if (!TextUtils.isEmpty(newpassword)) {
                        if (newpassword.length() >= 6 && newpassword.length() <= 20) {
                            if (!TextUtils.isEmpty(newpassword2)) {
                                if (newpassword2.equals(newpassword)) {
                                    // 提交数据
                                    dialog.show();
                                    summit_tradepassword(originpassword, newpassword);
                                } else {
                                    Uihelper.showToast(mActivity, "两次新密码不一致");
                                }

                            } else {
                                Uihelper.showToast(mActivity, "请再次输入新密码");
                            }

                        } else {
                            Uihelper.showToast(mActivity, R.string.tip_password);
                        }
                    } else {
                        Uihelper.showToast(mActivity, "请输入新密码");
                    }

                } else {
                    Uihelper.showToast(mActivity, "请输入原密码");
                }

            } else {
                if (!TextUtils.isEmpty(newpassword)) {
                    if (newpassword.length() >= 6 && newpassword.length() <= 20) {
                        if (!TextUtils.isEmpty(newpassword2)) {
                            if (newpassword2.equals(newpassword)) {
                                // 提交数据
                                dialog.show();
                                summit_tradepassword("", newpassword);
                            } else {
                                Uihelper.showToast(mActivity, "两次新密码不一致");
                            }

                        } else {
                            Uihelper.showToast(mActivity, "请再次输入新密码");
                        }

                    } else {
                        Uihelper.showToast(mActivity, R.string.tip_password);
                    }
                } else {
                    Uihelper.showToast(mActivity, "请输入新密码");
                }
            }

        }


    }

    private void summit_tradepassword(String originpassword, String newpassword) {

//		HttpRequest.setTradePassword(mActivity, new ICallback<Meta>() {
//
//			@Override
//			public void onSucceed(Meta result) {
//				if (paypwd_status==1) {
//					UIhelper.showToast(mActivity, "修改成功");
//				}else {
//					UIhelper.showToast(mActivity, "设置成功");
//					Intent data=new Intent();
//					setResult(1, data);
//				}
//				finish();
//			}
//
//			@Override
//			public void onFail(String error) {
//				dialog.dismiss();
//				UIhelper.showToast(mActivity, error);
//			}
//		}, originpassword, newpassword);

    }

    // 提交数据
    private void summit(String originpassword, String newpassword) {

//		HttpRequest.setLoginPassword(mActivity, new ICallback<LoginResult>() {
//
//			@Override
//			public void onSucceed(LoginResult result) {
//				dialog.dismiss();
//				UIhelper.showToast(mActivity, "修改成功");
//				LoginInfo loginInfo = result.getResult();
//				UserUtil.saveToken(mActivity, loginInfo.getToken(), loginInfo.getUser_id());
//				finish();
//			}
//
//			@Override
//			public void onFail(String error) {
//				dialog.dismiss();
//				UIhelper.showToast(mActivity, error);
//
//			}
//		}, originpassword, newpassword);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_changepassword;
    }

    @Override
    public void initTitle(WFYTitle mTitle) {

        mTitle.setTitleText("修改登录密码");
    }

}
