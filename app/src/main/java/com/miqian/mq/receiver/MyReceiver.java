package com.miqian.mq.receiver;

/**
 * Created by Administrator on 2015/8/27.
 */

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;

import com.miqian.mq.MainActivity;
import com.miqian.mq.R;
import com.miqian.mq.entity.JpushInfo;
import com.miqian.mq.utils.JsonUtil;

import java.util.Calendar;

import cn.jpush.android.api.JPushInterface;

import static android.util.Log.d;

/**
 * 自定义接收器
 *
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {
    private static final String TAG = "JPush";
    private JpushInfo response;
    private Intent notificationIntent;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
      if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
            processCustomMessage(context, bundle);
        }

    }
    //send msg to MainActivity
    private void processCustomMessage(Context context, Bundle bundle) {
        String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
            String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        Log.e("processCustomMessage",message);
        if (!TextUtils.isEmpty(message)){
            response = JsonUtil.parseObject(message, JpushInfo.class);
            // 解析数据
            if (response==null){
                return;
            }
            String contentTitle = response.getTitle();
            String contentText = response.getContent();
            String string_uritype = response.getUriType();
            int  noticeId = response.getId();

            notificationIntent = new Intent(context, MainActivity.class);
            int requestCode = (int) System.currentTimeMillis();
            notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent contentIntent = PendingIntent.getActivity(context, requestCode, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            RemoteViews contentViews = new RemoteViews("com.miqian.mq", R.layout.layout_jpush);

            final Calendar mCalendar = Calendar.getInstance();
            int mHour;
            boolean is24HourFormat = android.text.format.DateFormat.is24HourFormat(context);
            if (is24HourFormat) {
                mHour = mCalendar.get(Calendar.HOUR_OF_DAY);
            } else {
                mHour = mCalendar.get(Calendar.HOUR);
            }
            int mMinuts = mCalendar.get(Calendar.MINUTE);

            // 通过控件的Id设置属性
            contentViews.setTextViewText(R.id.titleNo, contentTitle);
            contentViews.setTextViewText(R.id.textNo, contentText);
            String  string_Minutes=""+mMinuts;
            if (mMinuts<10) {
                string_Minutes="0"+mMinuts;
            }
            contentViews.setTextViewText(R.id.timeNo, mHour + ":" + string_Minutes);
            String tickerText = context.getResources().getString(R.string.app_name);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context).setSmallIcon(R.mipmap.ic_launcher).setTicker(tickerText);
            mBuilder.setAutoCancel(true);
            mBuilder.setContentIntent(contentIntent);
            mBuilder.setContent(contentViews);
            mBuilder.setAutoCancel(true);
            mBuilder.setDefaults(Notification.DEFAULT_ALL);

            // 定义NotificationManager
            String ns = Context.NOTIFICATION_SERVICE;
            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(ns);
            mNotificationManager.notify(Integer.valueOf(noticeId), mBuilder.build());
        }
        else {
            Log.e("====MessageReceiver==","==response=nulL=");
        }

        }
    }
