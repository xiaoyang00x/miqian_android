package com.miqian.mq.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.miqian.mq.MyApplication;
import com.miqian.mq.R;
import com.miqian.mq.activity.user.MyTicketActivity;
import com.miqian.mq.database.MyDataBaseHelper;
import com.miqian.mq.entity.JpushInfo;
import com.miqian.mq.fragment.FragmentCurrent;
import com.miqian.mq.fragment.FragmentHome;
import com.miqian.mq.fragment.FragmentUser;
import com.miqian.mq.fragment.RegularFragment;
import com.miqian.mq.receiver.JpushHelper;
import com.miqian.mq.utils.ActivityStack;
import com.miqian.mq.utils.Config;
import com.miqian.mq.utils.Constants;
import com.miqian.mq.utils.ExtendOperationController;
import com.miqian.mq.utils.ExtendOperationController.ExtendOperationListener;
import com.miqian.mq.utils.ExtendOperationController.OperationKey;
import com.miqian.mq.utils.Pref;
import com.miqian.mq.utils.UserUtil;
import com.miqian.mq.views.CustomDialog;
import com.umeng.update.UmengUpdateAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2015/5/6.
 * <p/>
 * Main
 */
public class MainActivity extends BaseFragmentActivity implements ExtendOperationListener {

    private final String TAG_HOME = "HOME";
    private final String TAG_CURRENT = "CURRENT";
    private final String TAG_REGULAR = "REGULAR";
    private final String TAG_USER = "USER";

    FragmentTabHost mTabHost;
    Context context;
    TabWidget tabWidget;
    ViewGroup tabIndicator1, tabIndicator2, tabIndicator3, tabIndicator4;
    private List<JpushInfo> jpushInfolist;
    private int current_tab = 0;
    private RefeshDataListener mRefeshDataListener;
    private CustomDialog dialogTips;
    private CustomDialog jpushDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UmengUpdateAgent.update(this);
        context = this;
        ExtendOperationController.getInstance().registerExtendOperationListener(this);
        setContentView(R.layout.activity_main);
        findTabView();
        initTab();
        MyApplication.getInstance().setIsOnMainAcitivity(true);
        MyApplication.getInstance().setIsCurrent(true);
        //设置别名
        JpushHelper.setAlias(this);
//        handleJpush();
        setExsitFragment(true);

        //注册广播
        registerReceiver(mHomeKeyEventReceiver, new IntentFilter(
                Intent.ACTION_CLOSE_SYSTEM_DIALOGS));

        if (getIntent().getBooleanExtra(Constants.VERIFYFAILED, false)) {
            mTabHost.setCurrentTab(3);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Config.init(this);
        //设置在主页的状态
        MyApplication.getInstance().setIsOnMainAcitivity(true);
        MyApplication.setIsBackStage(false);
        if (mTabHost != null && current_tab != mTabHost.getCurrentTab()) {
            mTabHost.setCurrentTab(current_tab);
        }

        //app在当前
        showJushTip();
    }

    private void showJushTip() {

        if (Pref.getBoolean(Pref.IsPush, mContext, false)) {
            return;
        }
        HashMap<String, Boolean> jpushList = MyApplication.getInstance().getPushList();
        if (jpushList.size() > 0) {
            Set set = jpushList.entrySet();
            java.util.Iterator it = jpushList.entrySet().iterator();
            while (it.hasNext()) {
                java.util.Map.Entry entry = (java.util.Map.Entry) it.next();
                boolean hasTip = (boolean) entry.getValue();
                String noticeId = (String) entry.getKey();
                if (!hasTip) {
                    JpushInfo jpushInfo = MyDataBaseHelper.getInstance(mContext).getJpushInfo(noticeId);

                    String jpushToken = jpushInfo.getToken();
                    String uriType = jpushInfo.getUriType();
                    String token = UserUtil.getToken(context);
                    if (TextUtils.isEmpty(uriType)) {
                        break;
                    }
                    //用户登录或者是系统消息，弹出提示框
                    String pushSource = jpushInfo.getPushSource();
                    if ("0".equals(pushSource) || (UserUtil.hasLogin(context) && token.equals(jpushToken))) {
                        showTipDialog(jpushInfo);
                        jpushList.put(noticeId, true);
                        MyApplication.getInstance().setPushList(jpushList);
                    }
                    break;
                }
            }
        }
    }

    @Override
    protected String getPageName() {
        return "首页";
    }

    @Override
    protected void onDestroy() {
        ExtendOperationController.getInstance().unRegisterExtendOperationListener(this);
        //反注册广播
        unregisterReceiver(mHomeKeyEventReceiver);
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //设置不在主页
        MyApplication.getInstance().setIsOnMainAcitivity(false);
    }

    public void findTabView() {
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        tabWidget = (TabWidget) findViewById(android.R.id.tabs);
        LinearLayout layout = (LinearLayout) mTabHost.getChildAt(0);
        TabWidget tw = (TabWidget) layout.getChildAt(1);

        tabIndicator1 = initTabView(tw, R.drawable.tab_home_selector, R.string.main_tab_home);
        tabIndicator2 = initTabView(tw, R.drawable.tab_current_selector, R.string.main_tab_current);
        tabIndicator3 = initTabView(tw, R.drawable.tab_regular_selector, R.string.main_tab_regular);
        tabIndicator4 = initTabView(tw, R.drawable.tab_user_selector, R.string.main_tab_user);

        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String s) {
                current_tab = mTabHost.getCurrentTab();
            }
        });
    }

    private ViewGroup initTabView(TabWidget tw, int drawbleId, int nameResId) {
        ViewGroup tabIndicator = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.tab_indicator, tw, false);
        ImageView ivTab1 = (ImageView) tabIndicator.findViewById(R.id.img_tab);
        TextView tv_name = (TextView) tabIndicator.findViewById(R.id.tv_name);
        ivTab1.setImageResource(drawbleId);
        tv_name.setText(nameResId);
        return tabIndicator;
    }

    /**
     * 初始化Tab
     */
    public void initTab() {
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        FragmentTabHost.TabSpec tabSpechome = mTabHost.newTabSpec(TAG_HOME);
        tabSpechome.setIndicator(tabIndicator1);
        mTabHost.addTab(tabSpechome, FragmentHome.class, null);

        FragmentTabHost.TabSpec tabSpecCurrent = mTabHost.newTabSpec(TAG_CURRENT);
        tabSpecCurrent.setIndicator(tabIndicator2);
        mTabHost.addTab(tabSpecCurrent, FragmentCurrent.class, null);

        FragmentTabHost.TabSpec tabSpecRegular = mTabHost.newTabSpec(TAG_REGULAR);
        tabSpecRegular.setIndicator(tabIndicator3);
        mTabHost.addTab(tabSpecRegular, RegularFragment.class, null);

        FragmentTabHost.TabSpec tabSpecUser = mTabHost.newTabSpec(TAG_USER);
        tabSpecUser.setIndicator(tabIndicator4);
        mTabHost.addTab(tabSpecUser, FragmentUser.class, null);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            MyApplication.getInstance().setIsCurrent(false);
            MyApplication.getInstance().setIsBackStage(true);
        }
        return super.onKeyDown(keyCode, event);
    }

    private void handleJpush() {
        //判断是否是是极光推送
        boolean isPush = Pref.getBoolean(Pref.IsPush, mContext, false);
        if (isPush) {
            Pref.saveBoolean(Pref.IsPush, false, mContext);
            String userId = null;
            // 是否登录
            if (!UserUtil.hasLogin(mContext)) {
                jpushInfolist = MyDataBaseHelper.getInstance(mContext).getjpushInfo(Pref.VISITOR);
            } else {
                jpushInfolist = MyDataBaseHelper.getInstance(mContext).getjpushInfo(Pref.getString(Pref.USERID, mContext, Pref.VISITOR));
            }
            if (jpushInfolist.size() < 1) {
                return;
            }
            Collections.reverse(jpushInfolist);
            JpushInfo jInfo = jpushInfolist.get(0);
            if (jInfo == null) {
                return;
            }
            //用户登录或者是系统消息，弹出提示框
            String jpushToken = jInfo.getToken();
            String pushSource = jInfo.getPushSource();
            String token = UserUtil.getToken(context);
            if ("0".equals(pushSource) || (UserUtil.hasLogin(context) && token.equals(jpushToken))) {
                String string_uritype = jInfo.getUriType();
                int uritype;
                if (TextUtils.isEmpty(string_uritype)) {
                    return;
                } else {
                    uritype = Integer.valueOf(string_uritype);
                }
                switch (uritype) {
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
                        startActivity(new Intent(context, AnnounceActivity.class));
                        break;
                    case 9://收到红包
                    case 10://收到拾财券
                    case 11://红包即将到期
                    case 12://拾财券即将到期
                        startActivity(new Intent(context, MyTicketActivity.class));
                        break;
                    case 51://活动利好 webView
                    case 52://平台相关新闻 webView
                    case 53://相关项目 webView
                        try {
                            String ext = jInfo.getExt();
                            JSONObject jsonObject = new JSONObject(ext);
                            if (jsonObject != null) {
                                String url = jsonObject.getString("url");
                                if (!TextUtils.isEmpty(url)) {
                                    WebActivity.startActivity(context, url);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    case 54://运营公告文本
                    case 55://产品公告文本
                    case 56://活动公告文本
                        Intent intent = new Intent(context, AnnounceResultActivity.class);
                        intent.putExtra("id", Integer.parseInt(jInfo.getId()));
                        intent.putExtra("isMessage", false);
                        startActivity(intent);
                        break;
                    case 57://跳首页
                        mTabHost.setCurrentTab(0);
                        break;
                    case 58://跳活期首页
                        mTabHost.setCurrentTab(1);
                        break;
                    case 59://跳定期首页
                        mTabHost.setCurrentTab(2);
                        break;
                    case 60://跳我的页面
                        mTabHost.setCurrentTab(3);
                        break;
                    case 62://跳标的详情
                        jumpToRegular(jInfo);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private void jumpToRegular(JpushInfo jInfo) {
        String ext = jInfo.getExt();
        if (!TextUtils.isEmpty(ext)) {
            try {
                JSONObject jsonObject = new JSONObject(ext);
                if (jsonObject != null) {
                    String prodId = jsonObject.getString("prodId");
                    String subjectId = jsonObject.getString("subjectId");
                    if (!TextUtils.isEmpty(prodId) && !TextUtils.isEmpty(subjectId)) {
                        if ("3".equals(prodId)) {//定期赚
                            RegularEarnActivity.startActivity(mContext, subjectId);
                        } else if ("4".equals(prodId)) {//定期计划
                            RegularPlanActivity.startActivity(mContext, subjectId);
                        }
                        MyApplication.getInstance().getPushList().clear();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    private void showTipDialog(final JpushInfo jpush) {
        if (jpushDialog == null) {
            jpushDialog = new CustomDialog(context, CustomDialog.CODE_TIPS) {
                @Override
                public void positionBtnClick() {
                    dismiss();
                    jumpToRegular(jpush);
                }

                @Override
                public void negativeBtnClick() {
                }
            };
        }
        jpushDialog.setTitle(jpush.getTitle());
        jpushDialog.setRemarks(jpush.getContent());
        jpushDialog.setNegative(View.VISIBLE);
        jpushDialog.setNegative("取消");
        jpushDialog.show();

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            handleJpush();
        }
    }

    public interface RefeshDataListener {
        void changeData(JpushInfo jpushInfo);
    }

    public void setReshListener(RefeshDataListener refeshDataListener) {
        mRefeshDataListener = refeshDataListener;
    }


    @Override
    public void excuteExtendOperation(int operationKey, Object data) {
        switch (operationKey) {
            case OperationKey.BACK_HOME:
                ActivityStack.getActivityStack().clearActivity();
                current_tab = 0;
                break;
            case OperationKey.BACK_USER:
                ActivityStack.getActivityStack().clearActivity();
                current_tab = 3;
                break;
            case OperationKey.CHANGE_TOKEN:
                //清除Token
                JpushInfo jpushInfo = (JpushInfo) data;
                UserUtil.clearUserInfo(this);
                ActivityStack.getActivityStack().clearActivity();
                current_tab = 3;
                boolean currentActivity = MyApplication.getInstance().isOnMainAcitivity();
                if (currentActivity) {
                    mTabHost.setCurrentTab(current_tab);
                    showDialog(jpushInfo);
                } else {
                    if (mTabHost.getCurrentTab() == 3) {
                        MyApplication.getInstance().setShowTips(true);
                        mRefeshDataListener.changeData(jpushInfo);
                    } else {
                        showDialog(jpushInfo);
                    }
                }

                break;
            case OperationKey.ShowTips:
                showJushTip();
                break;
            case OperationKey.ChangeTab:
                ActivityStack.getActivityStack().clearActivity();
                current_tab = (int) data;
                break;
            default:
                break;
        }

    }

    private void showDialog(JpushInfo jpushInfo) {
        if (dialogTips == null) {
            dialogTips = new CustomDialog(this, CustomDialog.CODE_TIPS) {

                @Override
                public void positionBtnClick() {
                    dismiss();
                }

                @Override
                public void negativeBtnClick() {

                }
            };
        }
        if (jpushInfo != null) {
            dialogTips.setRemarks(jpushInfo.getContent());
            dialogTips.show();
        }
    }

    public BroadcastReceiver mHomeKeyEventReceiver = new BroadcastReceiver() {
        String SYSTEM_REASON = "reason";
        String SYSTEM_HOME_KEY = "homekey";
        String SYSTEM_HOME_KEY_LONG = "recentapps";

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                String reason = intent.getStringExtra(SYSTEM_REASON);
                if (TextUtils.equals(reason, SYSTEM_HOME_KEY)) {
                    // 设置为在后台运行的标志
                    // 表示按了home键,程序到了后台
                    MyApplication.getInstance().setIsBackStage(true);
                    //设置不在主页
                    MyApplication.getInstance().setIsOnMainAcitivity(false);

                }
            }
        }
    };

}
