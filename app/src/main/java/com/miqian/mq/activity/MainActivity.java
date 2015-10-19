package com.miqian.mq.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.miqian.mq.MyApplication;
import com.miqian.mq.R;
import com.miqian.mq.activity.current.ActivityCurrentRecord;
import com.miqian.mq.activity.current.ActivityRedPacket;
import com.miqian.mq.activity.user.MyTicketActivity;
import com.miqian.mq.activity.user.RedPaperActivity;
import com.miqian.mq.activity.user.UserRegularActivity;
import com.miqian.mq.database.MyDataBaseHelper;
import com.miqian.mq.entity.JpushInfo;
import com.miqian.mq.fragment.FragmentCurrent;
import com.miqian.mq.fragment.FragmentHome;
import com.miqian.mq.fragment.FragmentUser;
import com.miqian.mq.fragment.RegularFragment;
import com.miqian.mq.receiver.JpushHelper;
import com.miqian.mq.utils.ActivityStack;
import com.miqian.mq.utils.ExtendOperationController;
import com.miqian.mq.utils.ExtendOperationController.ExtendOperationListener;
import com.miqian.mq.utils.ExtendOperationController.OperationKey;
import com.miqian.mq.utils.Pref;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.utils.UserUtil;
import com.miqian.mq.views.CustomDialog;

import java.util.Collections;
import java.util.List;

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
    TabWidget tabWidget;
    LinearLayout tabIndicator1, tabIndicator2, tabIndicator3, tabIndicator4;
    private List<JpushInfo> jpushInfolist;
    private int current_tab = 0;
    private RefeshDataListener mRefeshDataListener;
    private CustomDialog dialogTips;
    private CustomDialog jpushDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ExtendOperationController.getInstance().registerExtendOperationListener(this);
        setContentView(R.layout.activity_main);

        findTabView();
        initTab();
        MyApplication.getInstance().setIsCurrent(true);
        //设置别名
        JpushHelper.setAlias(this);
        handleJpush();

        setExsitFragment(true);
    }

    @Override
    protected void onResume() {

        //设置在主页的状态
//        MyApplication.getInstance().setIsOnMainAcitivity(true);
        if (mTabHost != null && current_tab != mTabHost.getCurrentTab()) {
            mTabHost.setCurrentTab(current_tab);
        }
        super.onResume();
    }

    @Override
    protected String getPageName() {
        return "首页";
    }

    @Override
    protected void onDestroy() {
        ExtendOperationController.getInstance().unRegisterExtendOperationListener(this);
        super.onDestroy();
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

    private LinearLayout initTabView(TabWidget tw, int drawbleId, int nameResId) {
        LinearLayout tabIndicator = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.tab_indicator, tw, false);
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
            String string_uritype = jInfo.getUriType();
            int uritype;
            if (TextUtils.isEmpty(string_uritype)) {
                return;
            } else {
                uritype = Integer.valueOf(string_uritype);
            }
            switch (uritype) {
                case 1://交易密码修改，到消息列表页
                    startActivity(new Intent(mContext, AnnounceActivity.class));
                    break;
                case 2://提现受理，跳到资金记录
                    startActivity(new Intent(mContext, CapitalRecordActivity.class));
                    break;
                case 3://充值成功，到我的
                    mTabHost.setCurrentTab(3);
                    break;
                case 4://认购 ，到资金记录
                    startActivity(new Intent(mContext, CapitalRecordActivity.class));
                    break;
                case 5://定期赚到期，到我的定期列表页
                    startActivity(new Intent(mContext, UserRegularActivity.class));
                    break;
                case 6://定期计划到期，到我的定期列表页
                    startActivity(new Intent(mContext, UserRegularActivity.class));
                    break;
                case 7://活期赎回，到资金记录
                    startActivity(new Intent(mContext, ActivityCurrentRecord.class));
                    break;
                case 8://转让被认购完成,跳到资金记录
                    startActivity(new Intent(mContext, CapitalRecordActivity.class));
                    break;
                case 9:
                case 10:
                case 11:
                case 12://收到红包 弹框
                    showTipDialog(jInfo);
                    break;

                case 50://系统升级,系统维护
                    startActivity(new Intent(mContext, AnnounceActivity.class));
                    break;
                case 51://活动利好 首页弹框，webView
                case 52://平台相关新闻 首页弹框，webView
                case 53://相关项目 首页弹框，webView
                    showTipDialog(jInfo);
                    break;
                default:
                    break;

            }
        }
    }

    private void showTipDialog(final JpushInfo jpush) {
        final int type = Integer.valueOf(jpush.getUriType());

        if (jpushDialog == null) {
            jpushDialog = new CustomDialog(mContext, CustomDialog.CODE_TIPS) {
                @Override
                public void positionBtnClick() {

                    switch (type) {
                        case 9:
                            startActivity(new Intent(mContext, RedPaperActivity.class));
                            break;
                        case 10:
                            startActivity(new Intent(mContext, MyTicketActivity.class));
                            break;
                        case 11:
                            startActivity(new Intent(mContext, RedPaperActivity.class));
                            break;
                        case 12:
                            startActivity(new Intent(mContext, MyTicketActivity.class));
                            break;
                        case 51:
                        case 52:
                        case 53:

                            WebViewActivity.doIntent(mContext, jpush.getUrl(), true, null);

                            break;
                        default:
                            break;
                    }

                }

                @Override
                public void negativeBtnClick() {

                }
            };
        }

        jpushDialog.setTitle(jpush.getTitle());
        jpushDialog.setRemarks(jpush.getContent());

    }

    public interface RefeshDataListener {
        void changeData();
    }

    public void setReshListener(RefeshDataListener refeshDataListener) {
        mRefeshDataListener = refeshDataListener;
    }


    @Override
    public void excuteExtendOperation(int operationKey, Object data) {
        switch (operationKey) {
            case OperationKey.BACK_HOME:
                current_tab = 0;
                break;
            case OperationKey.BACK_USER:
                current_tab = 3;
                break;
            case OperationKey.CHANGE_TOKEN:
                //清除Token
                UserUtil.clearUserInfo(this);
                ActivityStack.getActivityStack().clearActivity();
                current_tab = 3;
                boolean currentActivity = MyApplication.getInstance().isOnMainAcitivity();
                if (currentActivity) {
                    if (mTabHost.getCurrentTab() == 3) {
                        mRefeshDataListener.changeData();

                    } else {
                        mTabHost.setCurrentTab(current_tab);
                    }
                    showDialog();
                } else {
                    if (mTabHost.getCurrentTab() == 3) {

                        MyApplication.getInstance().setShowTips(true);
                        mRefeshDataListener.changeData();
                    }
                }

                break;
        }

    }

    private void showDialog() {
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
        dialogTips.setRemarks("  账户信息已变更，请重新登录");
        dialogTips.show();
    }

}
