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

import com.miqian.mq.MyApplication;
import com.miqian.mq.R;
import com.miqian.mq.activity.AnnounceActivity;
import com.miqian.mq.activity.AnnounceResultActivity;
import com.miqian.mq.activity.MainActivity;
import com.miqian.mq.activity.SplashActivity;
import com.miqian.mq.database.MyDataBaseHelper;
import com.miqian.mq.entity.JpushInfo;
import com.miqian.mq.utils.JsonUtil;
import com.miqian.mq.utils.Pref;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.utils.UserUtil;

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
    private int uritype;
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
      if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            processCustomMessage(context, bundle);
        }

    }
    //send msg to MainActivity
    private void processCustomMessage(Context context, Bundle bundle) {
        String title=bundle.getString(JPushInterface.EXTRA_TITLE);
        String content = bundle.getString(JPushInterface.EXTRA_MESSAGE);
        String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);

        if (!TextUtils.isEmpty(extra)){
            Log.e("processCustomMessage",extra);
            response = JsonUtil.parseObject(extra, JpushInfo.class);
            // 解析数据
            if (response==null){
                return;
            }
            if (TextUtils.isEmpty(title)||TextUtils.isEmpty(content)){
                return;
            }
            response.setTitle(title);
            response.setContent(content);

            String userId = "";

            // 是否登录
            if (!UserUtil.hasLogin(context)) {
                userId = Pref.VISITOR;
            } else {
                userId = Pref.getString(Pref.USERID, context, Pref.VISITOR);
            }
            response.setUserId(userId);
            // 设为未读状态
            response.setState("1");
            Uihelper.getMessageCount(1, context);

            // 保存到数据库
            MyDataBaseHelper.getInstance(context).recordJpush(response);

            // 解析数据
            String contentTitle = response.getTitle();
            String contentText = response.getContent();
            String string_uritype = response.getUriType();
            String noticeId = response.getId();

            if (!MyApplication.getInstance().isCurrent()) {
                notificationIntent = new Intent(context, SplashActivity.class);
                Pref.saveBoolean(Pref.IsPush, true, context);
            } else {

                if (TextUtils.isEmpty(string_uritype)) {
                    return;
                } else {
                    uritype = Integer.valueOf(string_uritype);
                }
                switch (uritype) {

                    case 0:
                        notificationIntent = new Intent(context, MainActivity.class);
                        break;
                    case 1:
                        notificationIntent = new Intent(context, AnnounceActivity.class);
                        break;
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                    case 7:
                    case 8:
                    case 9:
                    case 10:
                        //具体页面
                        notificationIntent = new Intent(context, AnnounceResultActivity.class);
                        break;

                    // 内置浏览器
                    case 12:
                    case 13:
                    case 14:
                    case 15:
                        notificationIntent = new Intent(context, AnnounceResultActivity.class);
//                        notificationIntent = new Intent(context, WebViewActivity.class);
                        break;
                    default:
                        notificationIntent = new Intent(context, MainActivity.class);
                        break;
                }
            }
            if (TextUtils.isEmpty(noticeId)){
                return;
            }

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
