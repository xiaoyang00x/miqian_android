package com.miqian.mq.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentActivity;

import com.miqian.mq.receiver.NetBroadReceiver;

/**
 * Created by Joy on 2015/9/1.
 */
public class BaseFragmentActivity extends FragmentActivity implements NetBroadReceiver.netEventHandler {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onNetChange() {

    }
}
