package com.miqian.mq.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.activity.current.CurrentInvestment;
import com.miqian.mq.entity.CurrentInfo;
import com.miqian.mq.entity.CurrentInfoResult;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.pay.BaseHelper;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.utils.UserUtil;
import com.miqian.mq.views.DialogPay;
import com.miqian.mq.views.WaterWaveView;

import org.json.JSONObject;

public class FragmentCurrent extends Fragment {

    private View view;
    private WaterWaveView waterWaveView;
    private Button btInvestment;
    private TextView titleText;
    private TextView totalMoneyText;
    private TextView totalCountText;

    private Activity mContext;
//    private MyHandler mHandler;

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
//        mHandler = new MyHandler();
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
                UserUtil.loginPay(mContext, new DialogPay(mContext) {
                    @Override
                    public void positionBtnClick(String s) {
                        if (!TextUtils.isEmpty(s)) {
                            float money = Float.parseFloat(s);
                            if (money < 1) {
                                this.setTitle("提示：输入请大于一元");
                            } else {
                                UserUtil.currenPay(mContext, CurrentInvestment.class);
                                this.dismiss();
                            }
                        } else {
                            this.setTitle("提示：请输入金额");
                        }
                    }
                });

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


    @Override
    public void onDestroy() {
        waterWaveView.stopWave();
        waterWaveView = null;
        super.onDestroy();
    }
}
