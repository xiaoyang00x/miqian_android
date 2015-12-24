package com.miqian.mq.listener;

import java.lang.ref.WeakReference;
import java.util.HashMap;

/**
 * Created by guolei_wang on 15/12/23.
 *
 * Listener 的管理类，用于监听
 */
public class ListenerManager {

    public static final HashMap<String, WeakReference<HomeDialogListener>> homeDialogListeners = new HashMap<String, WeakReference<HomeDialogListener>>();

    public static void registerHomeDialogListener(String key, HomeDialogListener listener) {
        synchronized (homeDialogListeners) {
            WeakReference<HomeDialogListener> ref = homeDialogListeners.get(key);
            if (ref != null && ref.get() != null) {
                ref.clear();
            }
            homeDialogListeners.put(key, new WeakReference<HomeDialogListener>(listener));
        }
    }

    public static void unregisterHomeDialogListener(String key) {
        synchronized (homeDialogListeners) {
            WeakReference<HomeDialogListener> ref = homeDialogListeners.get(key);
            if (ref != null && ref.get() != null) {
                ref.clear();
            }
            homeDialogListeners.remove(key);
        }
    }
}
