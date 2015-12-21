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
import android.widget.RemoteViews;

import com.miqian.mq.MyApplication;
import com.miqian.mq.R;
import com.miqian.mq.activity.AnnounceActivity;
import com.miqian.mq.activity.AnnounceResultActivity;
import com.miqian.mq.activity.CapitalRecordActivity;
import com.miqian.mq.activity.MainActivity;
import com.miqian.mq.activity.RegularEarnActivity;
import com.miqian.mq.activity.RegularPlanActivity;
import com.miqian.mq.activity.SplashActivity;
import com.miqian.mq.activity.WebActivity;
import com.miqian.mq.activity.current.ActivityCurrentRecord;
import com.miqian.mq.activity.user.MyTicketActivity;
import com.miqian.mq.activity.user.UserRegularActivity;
import com.miqian.mq.database.MyDataBaseHelper;
import com.miqian.mq.entity.JpushInfo;
import com.miqian.mq.utils.ExtendOperationController;
import com.miqian.mq.utils.JsonUtil;
import com.miqian.mq.utils.LogUtil;
import com.miqian.mq.utils.Pref;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.utils.UserUtil;

import org.json.JSONException;
import org.json.JSONObject;

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
            boolean isPush = Pref.getBoolean(Pref.PUSH_STATE, context, true);
            if (isPush){
                processCustomMessage(context, bundle);
            }
        }
    }

    //send msg to MainActivity
    private void processCustomMessage(Context context, Bundle bundle) {
        String title = bundle.getString(JPushInterface.EXTRA_TITLE);
        String content = bundle.getString(JPushInterface.EXTRA_MESSAGE);
        String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);

        if (!TextUtils.isEmpty(extra)) {
            response = JsonUtil.parseObject(extra, JpushInfo.class);
            // 解析数据
            if (response==null||TextUtils.isEmpty(title) || TextUtils.isEmpty(content)) {
                return;
            }
            response.setTitle(title);
            response.setContent(content);
            String jpushToken=response.getToken();

            String userId = "";

            // 是否登录
            if (!UserUtil.hasLogin(context)) {
                userId = Pref.VISITOR;
            } else {
                userId = Pref.getString(Pref.USERID, context, Pref.VISITOR);
            }
            response.setUserId(userId);
            // 保存到数据库
            MyDataBaseHelper.getInstance(context).recordJpush(response);
            // 通知消息页面更新
            ExtendOperationController.getInstance().doNotificationExtendOperation(ExtendOperationController.OperationKey.RERESH_JPUSH, null);

            // 解析数据
            String contentTitle = response.getTitle();
            String contentText = response.getContent();
            String string_uritype = response.getUriType();
            String noticeId = response.getId();
            if (TextUtils.isEmpty(noticeId)) {
                return;
            }

            if (!UserUtil.hasLogin(context) && "1".equals(response.getPushSource())) {//1为个人信息，未登录则收到，不弹出通知
                return;
            }
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
                    case 16://找回登录密码
                    case 17://修改登录密码
                    case 18://其他设备登录
                    case  0://手机号修改
                        ExtendOperationController.getInstance().doNotificationExtendOperation(ExtendOperationController.OperationKey.CHANGE_TOKEN, response);
                        return;
                    case 1://交易密码修改，到消息列表页
                    case 2://提现受理，跳到资金记录
                    case 3://充值成功，到我的
                    case 4://认购 ，到资金记录
                    case 5://定期赚到期，到我的定期列表页
                    case 6://定期计划到期，到我的定期列表页
                    case 7://活期赎回，到资金记录
                    case 8://转让被认购完成,跳到资金记录
                    case 15://提现受理失败
                    case 50://系统升级,系统维护
                        notificationIntent = new Intent(context, AnnounceActivity.class);
                        break;
                    case 9://收到红包
                    case 10://收到拾财券
                    case 11://红包即将到期
                    case 12://拾财券即将到期
                        notificationIntent = new Intent(context, MyTicketActivity.class);
                        break;
                    case 51://活动利好 webView
                    case 52://平台相关新闻 webView
                    case 53://相关项目 webView
                        try {
                            String ext= response.getExt();
                            JSONObject jsonObject =new JSONObject(ext);
                            if (jsonObject!=null){
                                String url= jsonObject.getString("url");
                                if (!TextUtils.isEmpty(url)){
                                    notificationIntent = WebActivity.getIntent(context, url);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    case 54://运营公告文本
                    case 55://产品公告文本
                    case 56://活动公告文本
                        notificationIntent = new Intent(context, AnnounceResultActivity.class);
                        notificationIntent.putExtra("id", Integer.parseInt(response.getId()));
                        notificationIntent.putExtra("isMessage", false);
                        break;
                    case 57://跳首页
                        ExtendOperationController.getInstance().doNotificationExtendOperation(ExtendOperationController.OperationKey.ChangeTab, 0);
                        notificationIntent = new Intent(context, MainActivity.class);
                        break;
                    case 58://跳活期首页
                        ExtendOperationController.getInstance().doNotificationExtendOperation(ExtendOperationController.OperationKey.ChangeTab, 1);
                        notificationIntent = new Intent(context, MainActivity.class);
                        break;
                    case 59://跳定期首页
                        ExtendOperationController.getInstance().doNotificationExtendOperation(ExtendOperationController.OperationKey.ChangeTab, 2);
                        notificationIntent = new Intent(context, MainActivity.class);
                        break;
                    case 60://跳我的页面
                        ExtendOperationController.getInstance().doNotificationExtendOperation(ExtendOperationController.OperationKey.ChangeTab, 3);
                        notificationIntent = new Intent(context, MainActivity.class);
                        break;
                    case 62://跳标的详情
                        MyApplication.getInstance().addJpushList(noticeId, false);
                        if (MyApplication.isOnMainAcitivity()) {
                            ExtendOperationController.getInstance().doNotificationExtendOperation(ExtendOperationController.OperationKey.ShowTips, null);
                        }
                         String ext= response.getExt();
                        if (!TextUtils.isEmpty(ext)){
                            try {
                                JSONObject jsonObject =new JSONObject(ext);
                                if (jsonObject!=null){
                                   String prodId= jsonObject.getString("prodId");
                                   String subjectId= jsonObject.getString("subjectId");
                                    if (!TextUtils.isEmpty(prodId)&&!TextUtils.isEmpty(subjectId)){
                                        if ("3".equals(prodId)){//定期赚
                                            notificationIntent = new Intent(context, RegularEarnActivity.class);
                                            notificationIntent.putExtra("KEY_SUBJECT_ID",subjectId);
                                        }else if("5".equals(prodId)){
                                            notificationIntent = new Intent(context, RegularPlanActivity.class);
                                            notificationIntent.putExtra("KEY_SUBJECT_ID",subjectId);
                                        }
                                    }

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    default:
                        break;
                }
            }
            notificationIntent.putExtra("token",jpushToken);

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
            boolean isBackStage = MyApplication.getInstance().isBackStage();
            boolean isCurrent = MyApplication.getInstance().isCurrent();
            if (!isBackStage && isCurrent) {
            } else {
                mNotificationManager.notify(Integer.valueOf(noticeId), mBuilder.build());
            }
        } else {
            LogUtil.e("====MessageReceiver==", "==response=nulL=");
        }

    }
}
