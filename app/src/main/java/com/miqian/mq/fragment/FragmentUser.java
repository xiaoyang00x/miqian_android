package com.miqian.mq.fragment;

import android.app.Dialog;
import android.content.Intent;
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
import com.miqian.mq.activity.AnnounceActivity;
import com.miqian.mq.activity.IntoActivity;
import com.miqian.mq.activity.RolloutActivity;
import com.miqian.mq.activity.SettingActivity;
import com.miqian.mq.encrypt.RSAUtils;
import com.miqian.mq.entity.LoginResult;
import com.miqian.mq.entity.UserInfo;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.utils.UserUtil;
import com.miqian.mq.views.Dialog_Login;
import com.miqian.mq.views.ProgressDialogView;

import org.w3c.dom.Text;

/**
 * Description:
 *
 * @author Jackie
 * @created 2015-3-18
 */

public class FragmentUser extends Fragment implements View.OnClickListener {

    private View view;
    private TextView tv_Current, tv_Regular, tv_Ticket, tv_Redpackage;
    private Dialog mWaitingDialog;
    private UserInfo userInfo;
    private TextView tv_TotalProfit, tv_balance, tv_totalasset;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (savedInstanceState == null || view == null) {
            view = inflater.inflate(R.layout.frame_user, null);
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
        findViewById(view);
        obtainData();
        return view;
    }

    private void obtainData() {
        mWaitingDialog.show();
        HttpRequest.getUserInfo(getActivity(), new ICallback<LoginResult>() {
            @Override
            public void onSucceed(LoginResult result) {
                mWaitingDialog.dismiss();
                userInfo = result.getData();
                if (userInfo != null) {
                    setData();
                }
            }

            @Override
            public void onFail(String error) {
                mWaitingDialog.dismiss();

            }
        });

    }

    private void setData() {

        //历史收益
        if (!TextUtils.isEmpty(userInfo.getTotalProfit())) {
            tv_TotalProfit.setText(userInfo.getTotalProfit());
        } else {
            tv_TotalProfit.setText("0.00");
        }
        //账户余额
        if (!TextUtils.isEmpty(userInfo.getBalance())) {
            tv_balance.setText(userInfo.getBalance());
        } else {
            tv_balance.setText("0.00");
        }
        //我的活期
        if (!TextUtils.isEmpty(userInfo.getCurAmt())) {
            tv_Current.setText(userInfo.getCurAmt() + "元");
        } else {
            tv_Current.setText("0元");
        }
        //我的定期
        if (!TextUtils.isEmpty(userInfo.getRegTotal())) {
            tv_Regular.setText(userInfo.getRegTotal() + "笔");
        } else {
            tv_Regular.setText("0笔");
        }
        //拾财券
        if (!TextUtils.isEmpty(userInfo.getWealthTicket())) {
            tv_Ticket.setText(userInfo.getWealthTicket() + "张");
        } else {
            tv_Ticket.setText("0张");
        }
        //红包
        if (!TextUtils.isEmpty(userInfo.getRedBag())) {
            tv_Redpackage.setText(userInfo.getRedBag() + "个");
        } else {
            tv_Redpackage.setText("0个");
        }
        //总资产
        if (!TextUtils.isEmpty(userInfo.getTotalAsset())) {
            tv_totalasset.setText(userInfo.getTotalAsset());
        } else {
            tv_totalasset.setText("0.00");
        }
    }

    @Override
    public void onResume() {

        super.onResume();
    }

    private void findViewById(View view) {

        TextView tv_Title = (TextView) view.findViewById(R.id.title);
        tv_Title.setText("我的");

        ImageButton btn_message = (ImageButton) view.findViewById(R.id.btn_message);
        btn_message.setImageResource(R.mipmap.account_message);
        btn_message.setOnClickListener(this);

        ImageButton btn_setting = (ImageButton) view.findViewById(R.id.btn_account);
        btn_setting.setImageResource(R.mipmap.account_setting);
        btn_setting.setOnClickListener(this);

        Button btn_RollIn = (Button) view.findViewById(R.id.btn_rollin);
        Button btn_RollOut = (Button) view.findViewById(R.id.btn_rollout);

        btn_RollIn.setOnClickListener(this);
        btn_RollOut.setOnClickListener(this);

        tv_Current = (TextView) view.findViewById(R.id.account_current);
        tv_Regular = (TextView) view.findViewById(R.id.account_regular);
        tv_Ticket = (TextView) view.findViewById(R.id.account_ticket);
        tv_Redpackage = (TextView) view.findViewById(R.id.account_redpackage);

        tv_balance = (TextView) view.findViewById(R.id.tv_balance);
        tv_TotalProfit = (TextView) view.findViewById(R.id.tv_totalProfit);
        tv_totalasset = (TextView) view.findViewById(R.id.tv_totalasset);

        View frame_current = view.findViewById(R.id.frame_account_current);
        View frame_regular = view.findViewById(R.id.frame_regular);
        View frame_record = view.findViewById(R.id.frame_record);
        View frame_ticket = view.findViewById(R.id.frame_ticket);
        View frame_redpackage = view.findViewById(R.id.frame_redpackage);

        frame_current.setOnClickListener(this);
        frame_regular.setOnClickListener(this);
        frame_record.setOnClickListener(this);
        frame_ticket.setOnClickListener(this);
        frame_redpackage.setOnClickListener(this);

        mWaitingDialog = ProgressDialogView.create(getActivity());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //充值
            case R.id.btn_rollin:

                Dialog_Login dialog_login = new Dialog_Login(getActivity()) {
                    @Override
                    public void login(String telephone, String password) {
                        HttpRequest.login(getActivity(), new ICallback<LoginResult>() {
                            @Override
                            public void onSucceed(LoginResult result) {
                                Uihelper.showToast(getActivity(), "登录成功");
                                String name = RSAUtils.decryptByPrivate(result.getData().getRealName());
                                UserInfo userInfo = result.getData();
                                UserUtil.saveUserInfo(getActivity(), userInfo);
                                Uihelper.trace(name);
                            }

                            @Override
                            public void onFail(String error) {
                                Uihelper.showToast(getActivity(), error);
                            }
                        }, telephone, password);
                    }
                };

                dialog_login.show();

                break;
            //取现
            case R.id.btn_rollout:
           Intent intent=new Intent(getActivity(), RolloutActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("userInfo", userInfo);
                intent.putExtras(bundle);
                startActivity(intent);

                break;
            //我的活期
            case R.id.frame_account_current:
                break;
            //我的定期
            case R.id.frame_regular:
                break;
            //资金记录
            case R.id.frame_record:
                break;
            //拾财券
            case R.id.frame_ticket:
                break;
            //我的红包
            case R.id.frame_redpackage:
                break;
            //我的消息
            case R.id.btn_message:
                startActivity(new Intent(getActivity(), AnnounceActivity.class));
                break;
            //我的设置
            case R.id.btn_account:
                startActivity(new Intent(getActivity(), SettingActivity.class));
                break;
        }
    }
}
