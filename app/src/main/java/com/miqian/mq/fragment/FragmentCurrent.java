package com.miqian.mq.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.miqian.mq.R;
import com.miqian.mq.encrypt.RSAUtils;
import com.miqian.mq.entity.Meta;
import com.miqian.mq.entity.RegisterResult;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.test.OkHttpsTest;

public class FragmentCurrent extends Fragment {

    private View view;
    private Button btInvestment;
    private Activity mContext;

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
        return view;
    }

    private void findViewById(View view) {
        btInvestment = (Button) view.findViewById(R.id.bt_investment);
        btInvestment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, OkHttpsTest.class);
                startActivity(intent);
                HttpRequest.testHttp(mContext, new ICallback<Meta>() {

                    @Override
                    public void onSucceed(Meta result) {

                    }
                    @Override
                    public void onFail(String error) {

                    }
                }, "18759235288", "123456");
            }
        });
    }

}
