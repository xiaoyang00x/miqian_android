package com.miqian.mq.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.miqian.mq.listener.HomeDialogListener;
import com.miqian.mq.listener.ListenerManager;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by guolei_wang on 15/12/23.
 * 用于接收首页弹出运营对话框的广播
 */
public class HomeDialogReceiver extends BroadcastReceiver {
    public static final String ACTION_SHOW_DIALOG = "com.miqian.mq.receiver.show_dialog";

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent == null) return;
        if(ACTION_SHOW_DIALOG.equals(intent.getAction())) {
            showHomeDialog();
        }
    }

    /**
     * 通知已经注册监听的类进行相应的处理
     */
    public void showHomeDialog() {
        synchronized (ListenerManager.homeDialogListeners) {
            Set<String> set = ListenerManager.homeDialogListeners.keySet();
            for (String key : set) {
                WeakReference<HomeDialogListener> ref = ListenerManager.homeDialogListeners.get(key);
                if (ref != null && ref.get() != null) {
                    HomeDialogListener listener = ref.get();
                    if (listener != null) {
                        listener.show();
                    }
                }
            }
        }
    }
}
