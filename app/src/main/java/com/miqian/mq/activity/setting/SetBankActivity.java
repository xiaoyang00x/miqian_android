package com.miqian.mq.activity.setting;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.activity.BaseActivity;
import com.miqian.mq.entity.BankCard;
import com.miqian.mq.entity.Meta;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.views.WFYTitle;

/**
 * Created by Joy on 2015/9/23.
 */
public class SetBankActivity extends BaseActivity {

    private View  frame_bank_branch, frame_bank_province;
    private String  city,branch,province;
    private TextView  textBranch, tv_bank_province;
    private BankCard bankCard;
    @Override
    public void obtainData() {

    }

    @Override
    public void initView() {


          Intent intent =getIntent();
          bankCard= (BankCard)intent.getSerializableExtra("bankresult");

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
                Intent intent_branch = new Intent(mActivity, BankBranchActivity.class);
                startActivityForResult(intent_branch, 0);

            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data==null){
            return;
        }
        if (resultCode==1){
            branch = data.getStringExtra("branch");
            if (!TextUtils.isEmpty(branch)) {
                textBranch.setText(branch);
            }

        }else if(resultCode==0){
            city = data.getStringExtra("city");
            province = data.getStringExtra("province");
            if (!TextUtils.isEmpty(city)) {
                tv_bank_province.setText(city);
            }
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

    public void  btn_click(View v){

        mWaitingDialog.show();
//        HttpRequest.bindBank(mActivity, new ICallback<Meta>() {
//            @Override
//            public void onSucceed(Meta result) {
//
//            }
//
//            @Override
//            public void onFail(String error) {
//
//            }
//        },bankCard.getBankNo(),);


    }
}
