package com.miqian.mq.listener;

import java.lang.ref.WeakReference;
import java.util.HashMap;

/**
 * Created by guolei_wang on 15/12/23.
 *
 * Listener 的管理类，用于监听
 */
public class ListenerManager {

    public static final HashMap<String, WeakReference<HomeDialogListener>> homeDialogListeners = new HashMap<>();
    public static final HashMap<String, WeakReference< LoginListener>> loginListeners = new HashMap<>();
    public static final HashMap<String, WeakReference< HomeAdsListener>> adsListeners = new HashMap<>();


    public static void registerHomeDialogListener(String key, HomeDialogListener listener) {
        synchronized (homeDialogListeners) {
            WeakReference<HomeDialogListener> ref = homeDialogListeners.get(key);
            if (ref != null && ref.get() != null) {
                ref.clear();
            }
            homeDialogListeners.put(key, new WeakReference<>(listener));
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

    public static void registerLoginListener(String key, LoginListener listener) {
        synchronized (loginListeners) {
            WeakReference<LoginListener> ref = loginListeners.get(key);
            if (ref != null && ref.get() != null) {
                ref.clear();
            }
            loginListeners.put(key, new WeakReference<>(listener));
        }
    }

    public static void unregisterLoginListener(String key) {
        synchronized (loginListeners) {
            WeakReference<LoginListener> ref = loginListeners.get(key);
            if (ref != null && ref.get() != null) {
                ref.clear();
            }
            loginListeners.remove(key);
        }
    }

    public static void registerAdsListener(String key, HomeAdsListener listener) {
        synchronized (adsListeners) {
            WeakReference<HomeAdsListener> ref = adsListeners.get(key);
            if (ref != null && ref.get() != null) {
                ref.clear();
            }
            adsListeners.put(key, new WeakReference<>(listener));
        }
    }

    public static void unregisterAdsListener(String key) {
        synchronized (adsListeners) {
            WeakReference<HomeAdsListener> ref = adsListeners.get(key);
            if (ref != null && ref.get() != null) {
                ref.clear();
            }
            adsListeners.remove(key);
        }
    }
}
