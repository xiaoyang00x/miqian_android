package com.miqian.mq.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabWidget;

import com.miqian.mq.MyApplication;
import com.miqian.mq.R;
import com.miqian.mq.database.MyDataBaseHelper;
import com.miqian.mq.entity.JpushInfo;
import com.miqian.mq.fragment.FragmentCurrent;
import com.miqian.mq.fragment.FragmentHome;
import com.miqian.mq.fragment.FragmentUser;
import com.miqian.mq.fragment.RegularFragment;
import com.miqian.mq.receiver.JpushHelper;
import com.miqian.mq.utils.ExtendOperationController;
import com.miqian.mq.utils.ExtendOperationController.ExtendOperationListener;
import com.miqian.mq.utils.ExtendOperationController.OperationKey;
import com.miqian.mq.utils.Pref;
import com.miqian.mq.utils.UserUtil;

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
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mTabHost != null && current_tab != mTabHost.getCurrentTab()) {
            mTabHost.setCurrentTab(current_tab);
        }
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

        tabIndicator1 = initTabView(tw, R.drawable.tab_home);
        tabIndicator2 = initTabView(tw, R.drawable.tab_current);
        tabIndicator3 = initTabView(tw, R.drawable.tab_regular);
        tabIndicator4 = initTabView(tw, R.drawable.tab_user);

        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String s) {
                current_tab = mTabHost.getCurrentTab();
            }
        });
    }

    private LinearLayout initTabView(TabWidget tw, int drawbleId) {
        LinearLayout tabIndicator = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.tab_indicator, tw, false);
        ImageView ivTab1 = (ImageView) tabIndicator.findViewById(R.id.img_tab);
        ivTab1.setImageResource(drawbleId);
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
                case 1:
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
                    startActivity(new Intent(mContext, AnnounceResultActivity.class));
                    break;

                // 内置浏览器
                case 12:
                case 13:
                case 14:
                case 15:
                    startActivity(new Intent(mContext, AnnounceResultActivity.class));
//                        notificationIntent = new Intent(context, WebViewActivity.class);
                    break;
                default:
                    break;

            }
        }
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
        }

    }
}
