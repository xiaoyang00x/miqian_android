package com.miqian.mq.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.activity.current.CurrentInvestment;
import com.miqian.mq.activity.user.ActivityUserCurrent;
import com.miqian.mq.entity.CurrentInfo;
import com.miqian.mq.entity.CurrentInfoResult;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.utils.UserUtil;
import com.miqian.mq.views.DialogPay;
import com.miqian.mq.views.WaterWaveView;

public class FragmentCurrent extends Fragment implements View.OnClickListener {

    private View view;
    private WaterWaveView waterWaveView;
    private Button btInvestment;
    private TextView titleText;
    private TextView totalMoneyText;
    private TextView totalCountText;
    private TextView textDetail;
    private ImageButton btRight;

    private Activity mContext;
    private DialogPay dialogPay;

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
        obtainData();
        return view;
    }

    private void findViewById(View view) {
        waterWaveView = (WaterWaveView) view.findViewById(R.id.wave_view);
        waterWaveView.setmWaterLevel(0.16F);
        waterWaveView.startWave();

        titleText = (TextView) view.findViewById(R.id.title);
        titleText.setText("活期");

        btRight = (ImageButton) view.findViewById(R.id.bt_right);
        btRight.setImageResource(R.drawable.btn_current_right);
        btRight.setOnClickListener(this);

        totalCountText = (TextView) view.findViewById(R.id.total_count);
        totalMoneyText = (TextView) view.findViewById(R.id.total_money);
        textDetail = (TextView) view.findViewById(R.id.text_detail);
        textDetail.setOnClickListener(this);

        btInvestment = (Button) view.findViewById(R.id.bt_investment);
        btInvestment.setOnClickListener(this);

        dialogPay = new DialogPay(mContext) {
            @Override
            public void positionBtnClick(String s) {
                if (!TextUtils.isEmpty(s)) {
                    float money = Float.parseFloat(s);
                    if (money < 1) {
                        this.setTitle("提示：输入请大于一元");
                        this.setTitleColor(getResources().getColor(R.color.mq_r1));
                    } else {
                        UserUtil.currenPay(mContext, CurrentInvestment.class, s);
                        this.setEditMoney("");
                        this.setTitle("认购金额");
                        this.setTitleColor(getResources().getColor(R.color.mq_b1));
                        this.dismiss();
                    }
                } else {
                    this.setTitle("提示：请输入金额");
                    this.setTitleColor(getResources().getColor(R.color.mq_r1));
                }
            }

            @Override
            public void negativeBtnClick() {
                this.setEditMoney("");
                this.setTitle("认购金额");
                this.setTitleColor(getResources().getColor(R.color.mq_b1));
            }
        };
    }

    private void refreshView() {
        if (currentInfo != null) {
            totalCountText.setText(currentInfo.getBuyItemCount());
            totalMoneyText.setText(currentInfo.getBuyTotalSum());
            if (currentInfo.getCurrentSwitch().equals("0")) {
                btInvestment.setText("已售罄");
                btInvestment.setBackgroundResource(R.drawable.btn_cancel);
                btInvestment.setEnabled(false);
            } else {
                btInvestment.setText("马上认购");
                btInvestment.setBackgroundResource(R.drawable.btn_red);
                btInvestment.setEnabled(true);
            }
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_investment:
                UserUtil.loginPay(mContext, dialogPay);
                break;
            case R.id.bt_right:
                UserUtil.isLogin(mContext, ActivityUserCurrent.class);
                break;
            case  R.id.text_detail:
                // TODO: 2015/10/8
                break;
            default:
                break;
        }
    }
}
