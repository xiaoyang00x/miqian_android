package com.miqian.mq.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.miqian.mq.MyApplication;
import com.miqian.mq.R;
import com.miqian.mq.activity.user.MyTicketActivity;
import com.miqian.mq.database.MyDataBaseHelper;
import com.miqian.mq.entity.JpushInfo;
import com.miqian.mq.entity.MaintenanceResult;
import com.miqian.mq.entity.Navigation;
import com.miqian.mq.entity.UpdateInfo;
import com.miqian.mq.entity.UpdateResult;
import com.miqian.mq.fragment.FragmentCurrent;
import com.miqian.mq.fragment.FragmentHome;
import com.miqian.mq.fragment.FragmentUser;
import com.miqian.mq.fragment.RegularFragment;
import com.miqian.mq.listener.HomeAdsListener;
import com.miqian.mq.listener.ListenerManager;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.receiver.JpushHelper;
import com.miqian.mq.utils.ActivityStack;
import com.miqian.mq.utils.Config;
import com.miqian.mq.utils.Constants;
import com.miqian.mq.utils.ExtendOperationController;
import com.miqian.mq.utils.ExtendOperationController.ExtendOperationListener;
import com.miqian.mq.utils.ExtendOperationController.OperationKey;
import com.miqian.mq.utils.JsonUtil;
import com.miqian.mq.utils.Pref;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.utils.UserUtil;
import com.miqian.mq.views.CustomDialog;
import com.miqian.mq.views.DialogTip;
import com.miqian.mq.views.DialogUpdate;
import com.miqian.mq.views.FragmentTabHost;
import com.miqian.mq.views.MyRelativeLayout;
import com.miqian.mq.views.TextViewEx;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

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
public class MainActivity extends BaseFragmentActivity implements ExtendOperationListener, HomeAdsListener {

    private final String TAG_HOME = "HOME";
    private final String TAG_CURRENT = "CURRENT";
    private final String TAG_REGULAR = "REGULAR";
    private final String TAG_USER = "USER";

    FragmentTabHost mTabHost;
    Context context;
    TabWidget tabWidget;
    TabIndicator tabIndicator1, tabIndicator2, tabIndicator3, tabIndicator4;
    private RelativeLayout maintenance;
    private List<JpushInfo> jpushInfolist;
    private int current_tab = 0;
    private RefeshDataListener mRefeshDataListener;
    private DialogTip dialogTips;
    private CustomDialog jpushDialog;
    private ImageView imgRedPointer;
    private DisplayImageOptions options;
    private ImageLoader imageLoader;
    private Navigation navigation;
    private int adsClickStatus;
    private boolean isVerify;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        Intent intent = getIntent();
        adsClickStatus = intent.getIntExtra("onClick", 0);
        checkVersion();
        ListenerManager.registerAdsListener(MainActivity.class.getSimpleName(), this);
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
        registerReceiver(mHomeKeyEventReceiver, new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));

        // app每次重新打开都需打开手势密码界面－如果有手势密码
        isVerify = UserUtil.hasLogin(getBaseContext()) && Pref.getBoolean(Pref.GESTURESTATE, getBaseContext(), false) && !TextUtils.isEmpty(Pref.getString(Pref.GESTUREPSW, getBaseContext(), ""));
        if (isVerify) {
            GestureLockVerifyActivity.startActivity(getBaseContext(), MainActivity.class);
        }
        MyApplication.setIsBackStage(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Config.init(this);
        //设置在主页的状态
        MyApplication.getInstance().setIsOnMainAcitivity(true);
        MyApplication.setIsBackStage(false);
        if (!isVerify) {
            showWeb();
        }
        if (mTabHost != null && current_tab != mTabHost.getCurrentTab()) {
            mTabHost.setCurrentTab(current_tab);
        }
        //app在当前
        showJushTip();
    }

    //  版本更新:1:建议更新 2:强制
    private void checkVersion() {
        HttpRequest.forceUpdate(this, new ICallback<UpdateResult>() {
            @Override
            public void onSucceed(UpdateResult result) {
                UpdateInfo updateInfo = result.getData();
                if ("2".equals(updateInfo.getUpgradeSign())) {
                    DialogUpdate dialogUpdate = new DialogUpdate(context, updateInfo) {
                        @Override
                        public void updateClick(String url) {
//                        // 跳转到外部浏览器下载
                            startActivity(Uihelper.getDownIntent(context, url));
                        }
                    };
                    dialogUpdate.setCancelable(false);
                    dialogUpdate.setCanceledOnTouchOutside(false);
                    dialogUpdate.show();
                } else if ("1".equals(updateInfo.getUpgradeSign())) {
                    if (!Pref.getString(Pref.IGNORE_VERSION, context, "").equals(updateInfo.getVersion())) {
                        DialogUpdate dialogUpdate = new DialogUpdate(context, updateInfo) {
                            @Override
                            public void updateClick(String url) {
//                        // 跳转到外部浏览器下载
                                startActivity(Uihelper.getDownIntent(context, url));
                            }

                        };
                        dialogUpdate.setIgnoreVisility(View.VISIBLE);
                        dialogUpdate.show();
                    }
                }
            }

            @Override
            public void onFail(String error) {
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getBooleanExtra(Constants.VERIFYFAILED, false)) {
            dialogPayDismiss();
            mTabHost.setCurrentTab(3);
        }
    }

    /**
     * 用户退出登录时将认购弹窗隐藏
     */
    private void dialogPayDismiss() {
        if (mTabHost.getCurrentTab() == 1) {
            FragmentCurrent fragmentCurrent = (FragmentCurrent) getSupportFragmentManager().findFragmentByTag(TAG_CURRENT);
            fragmentCurrent.dialogPayDismiss();
        }
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
        ListenerManager.unregisterAdsListener(MainActivity.class.getSimpleName());
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

        int tabImageCount = Pref.getInt(Pref.TAB_IMAGE_COUNT, mApplicationContext, 0);
        final String navigationStr = Pref.getString(Pref.TAB_NAVIGATION_STR, mApplicationContext, "");
        final boolean isChangeTab = Pref.getBoolean(Pref.TAB_NAVIGATION_ON_OFF, mApplicationContext, false);
        if (tabImageCount == 8 && !TextUtils.isEmpty(navigationStr) && isChangeTab) {
            navigation = JsonUtil.parseObject(navigationStr, Navigation.class);
            tabIndicator1 = new TabIndicator(mContext, tw, navigation.getNavigationList().get(0).getImgClick(), R.string.main_tab_home);
            tabIndicator2 = new TabIndicator(mContext, tw, navigation.getNavigationList().get(1).getImg(), R.string.main_tab_current);
            tabIndicator3 = new TabIndicator(mContext, tw, navigation.getNavigationList().get(2).getImg(), R.string.main_tab_regular);
            tabIndicator4 = new TabIndicator(mContext, tw, navigation.getNavigationList().get(3).getImg(), R.string.main_tab_user);


            tabIndicator1.getTabName().setTextColor(Color.parseColor(navigation.getColorClick()));
            tabIndicator2.getTabName().setTextColor(Color.parseColor(navigation.getColor()));
            tabIndicator3.getTabName().setTextColor(Color.parseColor(navigation.getColor()));
            tabIndicator4.getTabName().setTextColor(Color.parseColor(navigation.getColor()));

        } else {
            tabIndicator1 = new TabIndicator(mContext, tw, R.drawable.tab_home_selector, R.string.main_tab_home);
            tabIndicator2 = new TabIndicator(mContext, tw, R.drawable.tab_current_selector, R.string.main_tab_current);
            tabIndicator3 = new TabIndicator(mContext, tw, R.drawable.tab_regular_selector, R.string.main_tab_regular);
            tabIndicator4 = new TabIndicator(mContext, tw, R.drawable.tab_user_selector, R.string.main_tab_user);
        }
        imgRedPointer = tabIndicator4.getImgRedPointer();

        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String s) {
                current_tab = mTabHost.getCurrentTab();
                //当点击财富Tab 隐藏此tab上的红点
                if (current_tab == 3) {
                    imgRedPointer.setVisibility(View.GONE);
                }
                if (!isChangeTab) return;
                if (options == null) {
                    options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).build();
                }
                if (imageLoader == null) {
                    imageLoader = ImageLoader.getInstance();
                }
                if (navigation == null) return;
                switch (current_tab) {
                    case 0:
                        imageLoader.displayImage(navigation.getNavigationList().get(0).getImgClick(), tabIndicator1.getTabIcon());
                        imageLoader.displayImage(navigation.getNavigationList().get(1).getImg(), tabIndicator2.getTabIcon());
                        imageLoader.displayImage(navigation.getNavigationList().get(2).getImg(), tabIndicator3.getTabIcon());
                        imageLoader.displayImage(navigation.getNavigationList().get(3).getImg(), tabIndicator4.getTabIcon());

                        tabIndicator1.getTabName().setTextColor(Color.parseColor(navigation.getColorClick()));
                        tabIndicator2.getTabName().setTextColor(Color.parseColor(navigation.getColor()));
                        tabIndicator3.getTabName().setTextColor(Color.parseColor(navigation.getColor()));
                        tabIndicator4.getTabName().setTextColor(Color.parseColor(navigation.getColor()));
                        break;
                    case 1:
                        imageLoader.displayImage(navigation.getNavigationList().get(0).getImg(), tabIndicator1.getTabIcon());
                        imageLoader.displayImage(navigation.getNavigationList().get(1).getImgClick(), tabIndicator2.getTabIcon());
                        imageLoader.displayImage(navigation.getNavigationList().get(2).getImg(), tabIndicator3.getTabIcon());
                        imageLoader.displayImage(navigation.getNavigationList().get(3).getImg(), tabIndicator4.getTabIcon());

                        tabIndicator1.getTabName().setTextColor(Color.parseColor(navigation.getColor()));
                        tabIndicator2.getTabName().setTextColor(Color.parseColor(navigation.getColorClick()));
                        tabIndicator3.getTabName().setTextColor(Color.parseColor(navigation.getColor()));
                        tabIndicator4.getTabName().setTextColor(Color.parseColor(navigation.getColor()));
                        break;
                    case 2:
                        imageLoader.displayImage(navigation.getNavigationList().get(0).getImg(), tabIndicator1.getTabIcon());
                        imageLoader.displayImage(navigation.getNavigationList().get(1).getImg(), tabIndicator2.getTabIcon());
                        imageLoader.displayImage(navigation.getNavigationList().get(2).getImgClick(), tabIndicator3.getTabIcon());
                        imageLoader.displayImage(navigation.getNavigationList().get(3).getImg(), tabIndicator4.getTabIcon());

                        tabIndicator1.getTabName().setTextColor(Color.parseColor(navigation.getColor()));
                        tabIndicator2.getTabName().setTextColor(Color.parseColor(navigation.getColor()));
                        tabIndicator3.getTabName().setTextColor(Color.parseColor(navigation.getColorClick()));
                        tabIndicator4.getTabName().setTextColor(Color.parseColor(navigation.getColor()));
                        break;
                    case 3:
                        imageLoader.displayImage(navigation.getNavigationList().get(0).getImg(), tabIndicator1.getTabIcon());
                        imageLoader.displayImage(navigation.getNavigationList().get(1).getImg(), tabIndicator2.getTabIcon());
                        imageLoader.displayImage(navigation.getNavigationList().get(2).getImg(), tabIndicator3.getTabIcon());
                        imageLoader.displayImage(navigation.getNavigationList().get(3).getImgClick(), tabIndicator4.getTabIcon());

                        tabIndicator1.getTabName().setTextColor(Color.parseColor(navigation.getColor()));
                        tabIndicator2.getTabName().setTextColor(Color.parseColor(navigation.getColor()));
                        tabIndicator3.getTabName().setTextColor(Color.parseColor(navigation.getColor()));
                        tabIndicator4.getTabName().setTextColor(Color.parseColor(navigation.getColorClick()));
                        break;
                    default:
                        break;
                }
            }
        });
        maintenance = (RelativeLayout) findViewById(R.id.maintenance);
    }

    class TabIndicator {
        private Context ctx;
        private MyRelativeLayout tabIndicator;
        private ImageView tabIcon;
        private TextView tabName;
        private ImageView imgRedPointer;

        private TabIndicator(Context ctx, TabWidget tw, int drawbleId, int nameResId) {
            this.ctx = ctx;
            tabIndicator = (MyRelativeLayout) LayoutInflater.from(ctx).inflate(R.layout.tab_indicator, tw, false);
            tabIcon = (ImageView) tabIndicator.findViewById(R.id.img_tab);
            tabName = (TextView) tabIndicator.findViewById(R.id.tv_name);
            imgRedPointer = (ImageView) tabIndicator.findViewById(R.id.img_red_pointer);

            setView(drawbleId, nameResId);
        }

        private TabIndicator(Context ctx, TabWidget tw, String url, int nameResId) {
            this.ctx = ctx;
            tabIndicator = (MyRelativeLayout) LayoutInflater.from(ctx).inflate(R.layout.tab_indicator, tw, false);
            tabIcon = (ImageView) tabIndicator.findViewById(R.id.img_tab);
            tabName = (TextView) tabIndicator.findViewById(R.id.tv_name);
            imgRedPointer = (ImageView) tabIndicator.findViewById(R.id.img_red_pointer);


            setView(url, nameResId);
        }


        public void setView(int drawbleId, int nameResId) {
            tabIcon.setImageResource(drawbleId);
            tabName.setText(nameResId);
        }

        public void setView(String url, int nameResId) {
            if (options == null) {
                options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).build();
            }
            if (imageLoader == null) {
                imageLoader = ImageLoader.getInstance();
            }
            imageLoader.displayImage(url, tabIcon);
            tabName.setText(nameResId);
        }

        public MyRelativeLayout getTabIndicator() {
            return tabIndicator;
        }

        public void setTabIndicator(MyRelativeLayout tabIndicator) {
            this.tabIndicator = tabIndicator;
        }

        public ImageView getTabIcon() {
            return tabIcon;
        }

        public void setTabIcon(ImageView tabIcon) {
            this.tabIcon = tabIcon;
        }

        public TextView getTabName() {
            return tabName;
        }

        public void setTabName(TextView tabName) {
            this.tabName = tabName;
        }

        public ImageView getImgRedPointer() {
            return imgRedPointer;
        }

        public void setImgRedPointer(ImageView imgRedPointer) {
            this.imgRedPointer = imgRedPointer;
        }
    }

    /**
     * 初始化Tab
     */
    public void initTab() {
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        FragmentTabHost.TabSpec tabSpechome = mTabHost.newTabSpec(TAG_HOME);
        tabSpechome.setIndicator(tabIndicator1.getTabIndicator());
        mTabHost.addTab(tabSpechome, FragmentHome.class, null);

        FragmentTabHost.TabSpec tabSpecCurrent = mTabHost.newTabSpec(TAG_CURRENT);
        tabSpecCurrent.setIndicator(tabIndicator2.getTabIndicator());
        mTabHost.addTab(tabSpecCurrent, FragmentCurrent.class, null);

        FragmentTabHost.TabSpec tabSpecRegular = mTabHost.newTabSpec(TAG_REGULAR);
        tabSpecRegular.setIndicator(tabIndicator3.getTabIndicator());
        mTabHost.addTab(tabSpecRegular, RegularFragment.class, null);

        FragmentTabHost.TabSpec tabSpecUser = mTabHost.newTabSpec(TAG_USER);
        tabSpecUser.setIndicator(tabIndicator4.getTabIndicator());
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
            String string_uritype = jInfo.getUriType();
            int uritype;
            if (TextUtils.isEmpty(string_uritype)) {
                return;
            } else {
                uritype = Integer.valueOf(string_uritype);
            }
            switch (uritype) {
                case 16://找回登录密码
                case 17://修改登录密码
                case 0://其他设备登录
                case 18://手机号修改
                    mTabHost.setCurrentTab(3);
                    break;
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
                    intent.putExtra("id", jInfo.getId());
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

    private void jumpToRegular(JpushInfo jInfo) {
        String ext = jInfo.getExt();
        if (!TextUtils.isEmpty(ext)) {
            try {
                JSONObject jsonObject = new JSONObject(ext);
                if (jsonObject != null) {
                    int prodId = jsonObject.getInt("prodId");
                    String subjectId = jsonObject.getString("subjectId");
                    if (!TextUtils.isEmpty(subjectId)) {
                        RegularDetailActivity.startActivity(mContext, subjectId, prodId);
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
            case OperationKey.BACK_MAIN:
                ActivityStack.getActivityStack().clearActivity();
                if (current_tab != 3) {
                    showRedPointer();
                }
                break;
            case OperationKey.BACK_HOME:
                current_tab = 0;
                ActivityStack.getActivityStack().clearActivity();
                break;
            case OperationKey.BACK_CURRENT:
                current_tab = 1;
                ActivityStack.getActivityStack().clearActivity();
                break;
            case OperationKey.BACK_REGULAR:
                current_tab = 2;
                ActivityStack.getActivityStack().clearActivity();
                break;
            case OperationKey.BACK_USER:
                current_tab = 3;
                ActivityStack.getActivityStack().clearActivity();
                break;
            case OperationKey.CHANGE_TOKEN:
                //清除Token
                JpushInfo jpushInfo = (JpushInfo) data;
                UserUtil.clearUserInfo(this);
                dialogPayDismiss();
                current_tab = 3;
                ActivityStack.getActivityStack().clearActivity();
                boolean currentActivity = MyApplication.getInstance().isOnMainAcitivity();
                if (currentActivity) {
                    if (mTabHost.getCurrentTab() == 3) {
                        MyApplication.getInstance().setShowTips(true);
                        mRefeshDataListener.changeData(jpushInfo);
                    } else {
                        mTabHost.setCurrentTab(current_tab);
                        showDialog(jpushInfo);
                    }
                } else {
                    if (mTabHost.getCurrentTab() == 3) {
                        MyApplication.getInstance().setShowTips(true);
                        mRefeshDataListener.changeData(jpushInfo);
                    } else {
                        showDialog(jpushInfo);
                    }
                }
                break;
            case OperationKey.SYSTEM_MAINTENANCE:
                ActivityStack.getActivityStack().clearActivity();
                mTabHost.setVisibility(View.GONE);
                maintenance.setVisibility(View.VISIBLE);
                ((TextView) maintenance.findViewById(R.id.title)).setText(((MaintenanceResult) data).getTitle());
                ((TextView) maintenance.findViewById(R.id.content)).setText(((MaintenanceResult) data).getContent());
                ((TextView) maintenance.findViewById(R.id.inscription)).setText(((MaintenanceResult) data).getInscription());
                break;
            case OperationKey.ShowTips:
                showJushTip();
                break;
            case OperationKey.ChangeTab:
                if (MyApplication.getInstance().isBackStage()) {
                    current_tab = (int) data;
                    ActivityStack.getActivityStack().clearActivity();
                }
                break;
            default:
                break;
        }
    }

    private void showDialog(JpushInfo jpushInfo) {
        if (dialogTips == null) {
            dialogTips = new DialogTip(MainActivity.this) {
            };
        }
        if (jpushInfo != null) {
            dialogTips.setInfo(jpushInfo.getContent());
            dialogTips.setTitle(jpushInfo.getTitle());
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

    /**
     * 认购成功显示财富tab 上的红点
     */
    public void showRedPointer() {
        if (imgRedPointer != null) {
            imgRedPointer.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void showWeb() {
        if (adsClickStatus == 1) {
            WebActivity.startActivity(context, "file:///android_asset/mq-packet.html");
//            WebActivity.startActivity(context, Pref.getString(Pref.CONFIG_ADS + "JumpUrl", context, ""));
            adsClickStatus = 0;
        }
    }
}
