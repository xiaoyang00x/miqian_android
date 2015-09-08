package com.miqian.mq.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.views.Dialog_Login;

/**
 * Description:��ҳ
 *
 * @author Jackie
 * @created 2015-3-18 ����5:05:49
 */

public class FragmentUser extends Fragment implements View.OnClickListener {

    private View view;
    private TextView tv_Current, tv_Regular, tv_Record, tv_Ticket, tv_Redpackage;

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

    }

    private void findViewById(View view) {

        TextView tv_Title = (TextView) view.findViewById(R.id.title);
        tv_Title.setText("我的");

        Button btn_RollIn = (Button) view.findViewById(R.id.btn_rollin);
        Button btn_RollOut = (Button) view.findViewById(R.id.btn_rollout);

        btn_RollIn.setOnClickListener(this);
        btn_RollOut.setOnClickListener(this);

        tv_Current = (TextView) view.findViewById(R.id.account_current);
        tv_Regular = (TextView) view.findViewById(R.id.account_regular);
        tv_Record = (TextView) view.findViewById(R.id.account_record);
        tv_Ticket = (TextView) view.findViewById(R.id.account_ticket);
        tv_Redpackage = (TextView) view.findViewById(R.id.account_redpackage);

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

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //充值
            case R.id.btn_rollin:

                Dialog_Login dialog_login = new Dialog_Login(getActivity()) {
                    @Override
                    public void login(String telephone, String password) {

                    }
                };

                dialog_login.show();

                break;
            //取现
            case R.id.btn_rollout:
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
        }
    }
}
