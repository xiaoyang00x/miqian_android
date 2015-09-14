package com.miqian.mq.activity;

import android.graphics.Color;

import com.miqian.mq.R;
import com.miqian.mq.entity.BankInfo;
import com.miqian.mq.utils.Pref;
import com.miqian.mq.views.WFYTitle;
import com.miqian.mq.views.wheelview.ListWheelAdapter;
import com.miqian.mq.views.wheelview.WheelView;

import java.util.ArrayList;

/**
 * Created by Joy on 2015/9/10.
 */
public class IntoActivity extends BaseActivity {
    private WheelView bankWheel;
    private ArrayList<BankInfo> bankList;
    private ListWheelAdapter bankAdapter;

    @Override
    public void obtainData() {

        BankInfo bankinto=new BankInfo();
        bankinto.setBankID("1");
        bankinto.setValue("建设银行");

        BankInfo bankinto2=new BankInfo();
        bankinto2.setBankID("2");
        bankinto2.setValue("工商银行");

        BankInfo bankinto3=new BankInfo();
        bankinto3.setBankID("3");
        bankinto3.setValue("农业银行");

        BankInfo bankinto4=new BankInfo();
        bankinto4.setBankID("3");
        bankinto4.setValue("农业银行");

        BankInfo bankinto5=new BankInfo();
        bankinto5.setBankID("3");
        bankinto5.setValue("农业银行");

        BankInfo bankinto6=new BankInfo();
        bankinto6.setBankID("3");
        bankinto6.setValue("农业银行");

        BankInfo bankinto7=new BankInfo();
        bankinto7.setBankID("3");
        bankinto7.setValue("农业银行");

        bankList= new ArrayList<>();
        bankList.add(bankinto);
        bankList.add(bankinto2);
        bankList.add(bankinto3);
        bankList.add(bankinto4);
        bankList.add(bankinto5);
        bankList.add(bankinto6);
        bankList.add(bankinto7);
        initWheel();
    }


        private void initWheel() {
            if (bankAdapter == null) {
                bankAdapter = new ListWheelAdapter(bankList);
                bankWheel.setAdapter(bankAdapter);
                bankWheel.setVisibleItems(7);

            }
//            int bankIndex = Pref.getInt(keyIndex, mActivity, -1);
//            if (bankIndex < 0) {
//                bankName.setTextColor(Color.rgb(200, 200, 200));
//            } else {
//                bankName.setTextColor(Color.rgb(80, 80, 80));
//                String bankString = bankList.get(bankIndex).getValue();
//                bankName.setText(bankString);
//            }
//            bankWheel.setCurrentItem(Pref.getInt(keyIndex, mActivity, 0));
        }


    @Override
    public void initView() {
        bankWheel = (WheelView) findViewById(R.id.bank_wheel);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_into;
    }

    @Override
    public void initTitle(WFYTitle mTitle) {

        mTitle.setTitleText("充值");

    }
}
