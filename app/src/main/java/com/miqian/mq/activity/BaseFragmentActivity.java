package com.miqian.mq.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.miqian.mq.receiver.NetBroadReceiver;

/**
 * Created by Joy on 2015/9/1.
 */
public class BaseFragmentActivity extends FragmentActivity
    implements NetBroadReceiver.netEventHandler {
  protected Context mContext;
  protected Context mApplicationContext;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mContext = getBaseContext();
    mApplicationContext = getApplicationContext();
  }

  @Override public void onNetChange() {

  }
}
