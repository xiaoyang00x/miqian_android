package com.miqian.mq.activity.setting;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.activity.BaseActivity;
import com.miqian.mq.encrypt.Encrypt;
import com.miqian.mq.encrypt.RSAUtils;
import com.miqian.mq.entity.BankCard;
import com.miqian.mq.entity.BankCardResult;
import com.miqian.mq.entity.Meta;
import com.miqian.mq.entity.UserInfo;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.views.WFYTitle;

/**
 * Created by Joy on 2015/9/23.
 */
public class SetBankActivity extends BaseActivity {

    private View frame_bank_branch, frame_bank_province;
    private String city, branch, province;
    private TextView textBranch, tv_bank_province;
    private UserInfo userInfo;
    private boolean isChooseCity;
    private BankCard bankCard;
    private String bankCardNo;

    @Override
    public void onCreate(Bundle arg0) {

        Intent intent = getIntent();
        userInfo = (UserInfo) intent.getSerializableExtra("userInfo");
        if (userInfo != null) {
            bankCardNo = userInfo.getBankCardNo();
        }
        super.onCreate(arg0);
    }

    @Override
    public void obtainData() {

        begin();
        HttpRequest.getUserBankCard(mActivity, new ICallback<BankCardResult>() {
            @Override
            public void onSucceed(BankCardResult result) {
                end();
                bankCard = result.getData();
                setData(bankCard);
            }

            @Override
            public void onFail(String error) {
                end();
                Uihelper.showToast(mActivity, error);
            }
        });

    }

    private void setData(BankCard bankCard) {

        if (!TextUtils.isEmpty(bankCard.getCity())) {
            tv_bank_province.setText(bankCard.getCity());
            isChooseCity = true;
            city = bankCard.getCity();
            province = bankCard.getProvince();
            branch = bankCard.getBankOpenName();
        }
        if (!TextUtils.isEmpty(bankCard.getBankOpenName())) {
            textBranch.setText(bankCard.getBankOpenName());
        }

    }

    @Override
    public void initView() {
        textBranch = (TextView) findViewById(R.id.tv_bank_branch);
        tv_bank_province = (TextView) findViewById(R.id.tv_bank_province);
        frame_bank_province = findViewById(R.id.frame_bank_province);
        frame_bank_branch = findViewById(R.id.frame_bank_branch);
        frame_bank_province.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent_city = new Intent(mActivity, CityListActivity.class);
                startActivityForResult(intent_city, 0);
            }
        });

        frame_bank_branch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isChooseCity) {
                    Intent intent_branch = new Intent(mActivity, BankBranchActivity.class);
                    intent_branch.putExtra("city", city);
                    intent_branch.putExtra("province", province);
                    intent_branch.putExtra("bankcode", userInfo.getBankCode());
                    intent_branch.putExtra("fromsetting", true);
                    startActivityForResult(intent_branch, 0);

                } else {
                    Uihelper.showToast(mActivity, "请先选择城市");
                }

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        if (resultCode == 1) {
            branch = data.getStringExtra("branch");
            if (!TextUtils.isEmpty(branch)) {
                textBranch.setText(branch);
            }

        } else if (resultCode == 0) {
            isChooseCity = true;
            city = data.getStringExtra("city");
            province = data.getStringExtra("province");
            if (!TextUtils.isEmpty(city)) {
                tv_bank_province.setText(city);
            }
            textBranch.setText("请选择");
            branch="";
        }

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_setbank;
    }

    @Override
    public void initTitle(WFYTitle mTitle) {
        mTitle.setTitleText("设置银行卡");

    }

    public void btn_click(View v) {

        if (!TextUtils.isEmpty(city)) {
            if (!TextUtils.isEmpty(branch)) {
                if ((!TextUtils.isEmpty(bankCardNo)) && userInfo != null) {
                    //绑定银行卡
                    begin();
                    HttpRequest.bindBank(mActivity, new ICallback<Meta>() {
                        @Override
                        public void onSucceed(Meta result) {
                            end();
                            Uihelper.showToast(mActivity, "设置成功");
                            finish();
                        }

                        @Override
                        public void onFail(String error) {
                            end();
                            Uihelper.showToast(mActivity, error);

                        }
                    }, RSAUtils.decryptByPrivate(bankCardNo), "XG", userInfo.getBankCode(), userInfo.getBankName(), branch, province, city);
                } else {
                    Uihelper.showToast(mActivity, "信息不全");
                }

            } else {
                Uihelper.showToast(mActivity, "支行名称不能为空");
            }
        } else {
            Uihelper.showToast(mActivity, "城市不能为空");

        }


    }

    @Override
    protected String getPageName() {
        return "设置银行卡";
    }
}
