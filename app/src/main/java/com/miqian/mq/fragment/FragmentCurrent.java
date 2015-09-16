package com.miqian.mq.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.miqian.mq.R;
import com.miqian.mq.encrypt.RSAUtils;
import com.miqian.mq.entity.CurrentInfo;
import com.miqian.mq.entity.CurrentInfoResult;
import com.miqian.mq.entity.Meta;
import com.miqian.mq.entity.PayOrder;
import com.miqian.mq.entity.PayOrderResult;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.pay.BaseHelper;
import com.miqian.mq.pay.MobileSecurePayer;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.views.WaterWaveView;

import org.json.JSONObject;

public class FragmentCurrent extends Fragment {

    private View view;
    private WaterWaveView waterWaveView;
    private Button btInvestment;
    private TextView titleText;
    private TextView totalMoneyText;
    private TextView totalCountText;

    private static final int BASE_ID = 0;
    private static final int RQF_PAY = BASE_ID + 1;

    private Activity mContext;
    private MyHandler mHandler;

    private CurrentInfo currentInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        if (savedInstanceState == null || view == null) {
            view = inflater.inflate(R.layout.frame_current, null);
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
        findViewById(view);
        mHandler = new MyHandler();
        obtainData();
        return view;
    }

    private void findViewById(View view) {
        waterWaveView = (WaterWaveView) view.findViewById(R.id.wave_view);
        waterWaveView.setmWaterLevel(0.2F);
        waterWaveView.startWave();

        titleText = (TextView) view.findViewById(R.id.title);
        titleText.setText("活期");

        totalCountText = (TextView) view.findViewById(R.id.total_count);
        totalMoneyText = (TextView) view.findViewById(R.id.total_money);

        btInvestment = (Button) view.findViewById(R.id.bt_investment);
        btInvestment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obtainData();
//                Intent intent = new Intent(mContext, OkHttpsTest.class);
//                startActivity(intent);

//                HttpRequest.testHttp(mContext, new ICallback<Meta>() {
//
//                    @Override
//                    public void onSucceed(Meta result) {
//
//                    }
//
//                    @Override
//                    public void onFail(String error) {
//
//                    }
//                }, "18759235288", "123456");

                String custId = "000000000000000000000018597746";
                String amt = "0.1";
                String bankCode = "466";
                String bankNo = "6214855920260037";

//                HttpRequest.setIDCardCheck(mContext, new ICallback<Meta>() {
//
//                    @Override
//                    public void onSucceed(Meta result) {
//
//                    }
//
//                    @Override
//                    public void onFail(String error) {
//
//                    }
//                }, custId, "350421198707071014", "张先鹏");

//                HttpRequest.rollIn(mContext, new ICallback<PayOrderResult>() {
//
//                    @Override
//                    public void onSucceed(PayOrderResult payOrderResult) {
//                        PayOrder newPayOrder = constructPreCardPayOrder(payOrderResult.getData());
//                        String content4Pay = JSON.toJSONString(newPayOrder);
//                        Log.e("", "content4Pay---------- : " + content4Pay);
//                        MobileSecurePayer msp = new MobileSecurePayer();
//                        msp.pay(content4Pay, mHandler, RQF_PAY, mContext, false);
//                    }
//
//                    @Override
//                    public void onFail(String error) {
//                        Uihelper.showToast(mContext, error);
//                    }
//                }, custId, amt, bankCode, bankNo);
            }
        });
    }

    private void refreshView() {
        if (currentInfo != null) {
            totalCountText.setText(currentInfo.getBuyItemCount());
            totalMoneyText.setText(currentInfo.getBuyTotalSum());
        }
    }

    public void obtainData() {
        HttpRequest.getCurrentHome(mContext, new ICallback<CurrentInfoResult>() {
            @Override
            public void onSucceed(CurrentInfoResult result) {
                currentInfo = result.getData();
                refreshView();
            }

            @Override
            public void onFail(String error) {
                Uihelper.showToast(mContext, error);
            }
        });
    }


    private PayOrder constructPreCardPayOrder(PayOrder payOrder) {
        payOrder.setAcct_name(RSAUtils.decryptByPrivate(payOrder.getAcct_name()));
        payOrder.setCard_no(RSAUtils.decryptByPrivate(payOrder.getCard_no()));
        payOrder.setUser_id(RSAUtils.decryptByPrivate(payOrder.getUser_id()));
        payOrder.setId_no(RSAUtils.decryptByPrivate(payOrder.getId_no()));
        return payOrder;
    }

    @Override
    public void onDestroy() {
        waterWaveView.stopWave();
        waterWaveView = null;
        super.onDestroy();
    }

    class MyHandler extends Handler {
//        WeakReference<IntoActivity> weakActivity;

//        public MyHandler(IntoActivity activity) {
//            weakActivity = new WeakReference<IntoActivity>(activity);
//        }

        public MyHandler() {
//            weakActivity = new WeakReference<IntoActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            String strRet = (String) msg.obj;
            switch (msg.what) {
                case 1:
                    JSONObject objContent = BaseHelper.string2JSON(strRet);
                    String retCode = objContent.optString("ret_code");
                    String retMsg = objContent.optString("ret_msg");
                    Log.e("", "retCode : " + retCode);
                    Log.e("", "retMsg : " + retMsg);
                    // //先判断状态码，状态码为 成功或处理中 的需要 验签
//                    if (Constants.RET_CODE_SUCCESS.equals(retCode)) {
//                        String resulPay = objContent.optString("result_pay");
//                        if (Constants.RESULT_PAY_SUCCESS.equalsIgnoreCase(resulPay)) {
//                            // 支付成功后续处理
//                            Intent intent = new Intent(mActivity, IntoResult.class);
//                            intent.putExtra("orderNo", objContent.optString("no_order"));
//                            startActivity(intent);
//                            mActivity.finish();
//                        } else {
//                            UIhelper.showToast(mActivity, retMsg);
//                        }
//
//                    } else if (Constants.RET_CODE_PROCESS.equals(retCode)) {
//                        String resulPay = objContent.optString("result_pay");
//                        if (Constants.RESULT_PAY_PROCESSING.equalsIgnoreCase(resulPay)) {
//                            UIhelper.showToast(mActivity, retMsg);
//                        }
//                    } else if (retCode.equals("1006")) {
//                        UIhelper.showToast(mActivity, "您已取消当前交易");
//                    } else if (retCode.equals("1004")) {
//                        UIhelper.showToast(mActivity, "您的银行卡号有误");
//                    } else {
//                        UIhelper.showToast(mActivity, retMsg);
//                    }
                    break;
            }
            super.handleMessage(msg);
        }
    }
}
