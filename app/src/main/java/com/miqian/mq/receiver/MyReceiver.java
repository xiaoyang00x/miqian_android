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
import com.miqian.mq.activity.CapitalRecordActivity;
import com.miqian.mq.activity.MainActivity;
import com.miqian.mq.activity.SplashActivity;
import com.miqian.mq.activity.WebActivity;
import com.miqian.mq.activity.current.ActivityCurrentRecord;
import com.miqian.mq.activity.user.MyTicketActivity;
import com.miqian.mq.activity.user.RedPaperActivity;
import com.miqian.mq.activity.user.UserRegularActivity;
import com.miqian.mq.database.MyDataBaseHelper;
import com.miqian.mq.entity.JpushInfo;
import com.miqian.mq.utils.ExtendOperationController;
import com.miqian.mq.utils.JsonUtil;
import com.miqian.mq.utils.Pref;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.utils.UserUtil;

import java.util.Calendar;

import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 * <p/>
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
        String title = bundle.getString(JPushInterface.EXTRA_TITLE);
        String content = bundle.getString(JPushInterface.EXTRA_MESSAGE);
        String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);

        if (!TextUtils.isEmpty(extra)) {
            Log.e("processCustomMessage", extra);
            response = JsonUtil.parseObject(extra, JpushInfo.class);
            // 解析数据
            if (response == null) {
                return;
            }
            if (TextUtils.isEmpty(title) || TextUtils.isEmpty(content)) {
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
            //通知更新ui
            // 通知消息页面更新

            ExtendOperationController.getInstance().doNotificationExtendOperation(ExtendOperationController.OperationKey.RERESH_JPUSH, null);

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
                        ExtendOperationController.getInstance().doNotificationExtendOperation(ExtendOperationController.OperationKey.CHANGE_TOKEN, null);
                        return;

                    case 1://交易密码修改，到消息列表页
                        notificationIntent= new Intent(context, AnnounceActivity.class);
                        break;
                    case 2://提现受理，跳到资金记录
                        notificationIntent= new Intent(context, CapitalRecordActivity.class);
                        break;
                    case 3://充值成功，到我的
                        notificationIntent= new Intent(context, MainActivity.class);
                        break;
                    case 4://认购 ，到资金记录
                        notificationIntent= new Intent(context, CapitalRecordActivity.class);
                        break;
                    case 5://定期赚到期，到我的定期列表页
                    case 6://定期计划到期，到我的定期列表页
                        notificationIntent= new Intent(context, UserRegularActivity.class);
                        break;
                    case 7://活期赎回，到资金记录
                        notificationIntent= new Intent(context, ActivityCurrentRecord.class);
                        break;
                    case 8://转让被认购完成,跳到资金记录
                        notificationIntent= new Intent(context, CapitalRecordActivity.class);
                        break;
                    case 9:
                        notificationIntent= new Intent(context, RedPaperActivity.class);
                        break;
                    case 10:
                        notificationIntent= new Intent(context, MyTicketActivity.class);
                        break;
                    case 11:
                        notificationIntent= new Intent(context, RedPaperActivity.class);
                        break;
                    case 12:
                        notificationIntent= new Intent(context, MyTicketActivity.class);
                        break;

                    case 50://系统升级,系统维护
                        notificationIntent= new Intent(context, AnnounceActivity.class);
                        break;
                    case 51://活动利好 首页弹框，webView
                    case 52://平台相关新闻 首页弹框，webView
                    case 53://相关项目 首页弹框，webView
                        notificationIntent= WebActivity.getIntent(context, response.getUrl());
                        break;
                    default:
                        break;
                }
            }
            if (TextUtils.isEmpty(noticeId)) {
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
            String string_Minutes = "" + mMinuts;
            if (mMinuts < 10) {
                string_Minutes = "0" + mMinuts;
            }
            contentViews.setTextViewText(R.id.timeNo, mHour + ":" + string_Minutes);
            String tickerText = context.getResources().getString(R.string.app_name);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context).setSmallIcon(R.drawable.icon_jpush).setTicker(tickerText);
            mBuilder.setAutoCancel(true);
            mBuilder.setContentIntent(contentIntent);
            mBuilder.setContent(contentViews);
            mBuilder.setAutoCancel(true);
            mBuilder.setDefaults(Notification.DEFAULT_ALL);

            // 定义NotificationManager
            String ns = Context.NOTIFICATION_SERVICE;
            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(ns);
            //判断是否在后台,在后台则发送通知
            if ((!MyApplication.getInstance().isBackStage())&&MyApplication.getInstance().isCurrent()){

            }else {
                mNotificationManager.notify(Integer.valueOf(noticeId), mBuilder.build());
            }
        } else {
            Log.e("====MessageReceiver==", "==response=nulL=");
        }

    }
}
