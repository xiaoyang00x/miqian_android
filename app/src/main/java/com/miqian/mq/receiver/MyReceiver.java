package com.miqian.mq.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import com.miqian.mq.MyApplication;
import com.miqian.mq.R;
import com.miqian.mq.activity.AnnounceActivity;
import com.miqian.mq.activity.AnnounceResultActivity;
import com.miqian.mq.activity.MainActivity;
import com.miqian.mq.activity.RegularPlanDetailActivity;
import com.miqian.mq.activity.RegularProjectDetailActivity;
import com.miqian.mq.activity.SplashActivity;
import com.miqian.mq.activity.WebActivity;
import com.miqian.mq.activity.user.MyTicketActivity;
import com.miqian.mq.database.MyDataBaseHelper;
import com.miqian.mq.entity.JpushInfo;
import com.miqian.mq.entity.RegularBase;
import com.miqian.mq.utils.Constants;
import com.miqian.mq.utils.ExtendOperationController;
import com.miqian.mq.utils.JsonUtil;
import com.miqian.mq.utils.LogUtil;
import com.miqian.mq.utils.Pref;
import com.miqian.mq.utils.UserUtil;

import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 * <p/>
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {
    private JpushInfo response;
    private Intent notificationIntent;
    private int uritype;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            boolean isPush = Pref.getBoolean(Pref.PUSH_STATE, context, true);
            if (isPush) {
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
            if (response == null || TextUtils.isEmpty(title) || TextUtils.isEmpty(content)) {
                return;
            }
            response.setTitle(title);
            response.setContent(content);
            String jpushToken = response.getToken();

            String userId;

            // 是否登录
            if (!UserUtil.hasLogin(context)) {
                userId = Pref.VISITOR;
            } else {
                userId = Pref.getString(Pref.USERID, context, Pref.VISITOR);
            }
            response.setUserId(userId);
            // 保存到数据库
            MyDataBaseHelper.getInstance(context).recordJpush(response);
            if ("1".equals(response.getPushSource())) {
                // 通知消息更新
                ExtendOperationController.getInstance().doNotificationExtendOperation(ExtendOperationController.OperationKey.RERESH_JPUSH, null);
            }
            // 解析数据
            String string_uritype = response.getUriType();
            String noticeId = response.getId();
            String ext = response.getExt();
            if (TextUtils.isEmpty(noticeId)) {
                return;
            }
            if (!MyApplication.isCurrent()) {
                notificationIntent = new Intent(context, SplashActivity.class);
                Pref.saveBoolean(Pref.IsPush, true, context);
                if ("2".equals(response.getPushSource())) {//未登录的用户,从本地读取消息
                    Pref.saveBoolean(Pref.FROM_NATIVE + response.getId(), true, context);
                }
            } else {
                if (TextUtils.isEmpty(string_uritype)) {
                    return;
                } else {
                    uritype = Integer.valueOf(string_uritype);
                }
                switch (uritype) {
                    case 16://找回登录密码
                    case 17://修改登录密码
                    case 18://手机号修改
                    case 0://其他设备登录
                        notificationIntent = new Intent(context, MainActivity.class);
                        ExtendOperationController.getInstance().doNotificationExtendOperation(ExtendOperationController.OperationKey.CHANGE_TOKEN, response);
                        break;
                    case 1://交易密码修改，到消息列表页
                    case 2://提现受理，跳到资金记录
                    case 3://充值成功，到我的
                    case 4://认购 ，到资金记录
                    case 5://定期赚到期，到我的定期列表页
                    case 6://定期计划到期，到我的定期列表页
                    case 7://秒钱宝赎回，到资金记录
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
                        if (TextUtils.isEmpty(ext)) {
                            notificationIntent = new Intent(context, MainActivity.class);
                        } else {
                            try {
                                JSONObject jsonObject = new JSONObject(ext);
                                String url = jsonObject.getString("url");
                                notificationIntent = WebActivity.getIntent(context, url);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    case 54://运营公告文本
                    case 55://产品公告文本
                    case 56://活动公告文本

                        if ("2".equals(response.getPushSource())) {//未登录的用户,从本地读取消息
                            Pref.saveBoolean(Pref.FROM_NATIVE + response.getId(), true, context);
                        }
                        notificationIntent = new Intent(context, AnnounceResultActivity.class);
                        notificationIntent.putExtra("id", response.getId());
                        notificationIntent.putExtra("isMessage", false);

                        break;
                    case 57://跳首页
                        ExtendOperationController.getInstance().doNotificationExtendOperation(ExtendOperationController.OperationKey.ChangeTab, 0);
                        notificationIntent = new Intent(context, MainActivity.class);
                        break;
                    case 58://跳秒钱宝首页
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
                        if (!MyApplication.isBackStage()) {
                            MyApplication.getInstance().addJpushList(noticeId, false);
                        }
                        if (MyApplication.isOnMainAcitivity()) {
                            ExtendOperationController.getInstance().doNotificationExtendOperation(ExtendOperationController.OperationKey.ShowTips, null);
                        }
                        if (!TextUtils.isEmpty(ext)) {
                            try {
                                JSONObject jsonObject = new JSONObject(ext);
                                int prodId = jsonObject.getInt("prodId");
                                String subjectId = jsonObject.getString("subjectId");
                                if (!TextUtils.isEmpty(subjectId)) {
                                    if (RegularBase.REGULAR_PROJECT == prodId) {
                                        notificationIntent = new Intent(context, RegularProjectDetailActivity.class);
                                    } else if (RegularBase.REGULAR_PLAN == prodId) {
                                        notificationIntent = new Intent(context, RegularPlanDetailActivity.class);
                                    }
                                    notificationIntent.putExtra(Constants.SUBJECTID, subjectId);
                                    notificationIntent.putExtra(Constants.PRODID, prodId);
                                } else {
                                    notificationIntent = new Intent(context, MainActivity.class);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            notificationIntent = new Intent(context, MainActivity.class);
                        }
                        break;
                    default:
                        break;
                }
            }
            if (notificationIntent != null) {
                if (!TextUtils.isEmpty(jpushToken)) {
                    notificationIntent.putExtra("token", jpushToken);
                }
                int requestCode = (int) System.currentTimeMillis();
                notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent contentIntent = PendingIntent.getActivity(context, requestCode, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
                //设置通知栏标题,内容，通知栏点击意图
                mBuilder.setContentTitle(title);
                mBuilder.setContentText(content);
                mBuilder.setContentIntent(contentIntent);
                mBuilder.setTicker("秒钱有新通知"); //通知首次出现在通知栏，带上升动画效果的
                mBuilder.setWhen(System.currentTimeMillis());
                mBuilder.setPriority(Notification.PRIORITY_DEFAULT); //设置该通知优先级
                mBuilder.setSmallIcon(R.drawable.icon_jpush);//设置通知小ICON
                mBuilder.setContentIntent(contentIntent);
                mBuilder.setAutoCancel(true);
                mBuilder.setDefaults(Notification.DEFAULT_ALL);

                // 定义NotificationManager
                NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                //判断是否在后台,在后台则发送通知
                boolean isBackStage = MyApplication.isBackStage();
                boolean isCurrent = MyApplication.isCurrent();
                if (isBackStage || !isCurrent) {
                    mNotificationManager.notify(Integer.valueOf(noticeId), mBuilder.build());
                }
            }
        } else {
            LogUtil.e("====MessageReceiver==", "==response=nulL=");
        }

    }
}
