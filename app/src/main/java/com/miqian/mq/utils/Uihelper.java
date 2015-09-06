package com.miqian.mq.utils;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * Created by Administrator on 2015/9/4.
 */
public class Uihelper {


    private static Toast mToast;

    private static void initToast(Context context) {
        if (mToast == null) {
            mToast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
        }
    }

    public static void showToast(final Context context, final String content) {
        initToast(context);
        if (!TextUtils.isEmpty(content)) {
            mToast.setText(content);
            mToast.show();
        }
    }

    public static void showToast(final Activity context, int id) {
        initToast(context);
        mToast.setText(id);
        mToast.show();
    }
}
