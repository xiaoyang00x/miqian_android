package com.miqian.mq;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.miqian.mq.fragment.FragmentMain;
import com.miqian.mq.fragment.FragmentMenu;
import com.miqian.mq.receiver.JpushHelper;

public class MainActivity extends SlidingFragmentActivity {

    private static final String TAG = "MainActivity";

    private Activity mContext;
    private SlidingMenu slidingMenu;
    private Fragment mContent;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_main);
        setMenuView(savedInstanceState);

        //设置别名
        JpushHelper.setAlias(this);
    }

    public void setMenuView(Bundle savedInstanceState) {

        slidingMenu = getSlidingMenu();
        if (findViewById(R.id.frame_menu) == null) {
            setBehindContentView(R.layout.frame_menu);
            slidingMenu.setSlidingEnabled(false);
        }
        // 设置主页的View
        if (savedInstanceState != null) {
            mContent = getSupportFragmentManager().getFragment(savedInstanceState, "mContent");
        }
        if (mContent == null) {
            mContent = new FragmentMain();
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, mContent).commitAllowingStateLoss();

        // 设置菜单View
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_menu, new FragmentMenu()).commitAllowingStateLoss();

        // 设置菜单样式
//		slidingMenu.setBehindOffset(Config.WIDTH / 4); // 菜单偏移的距离
        slidingMenu.setShadowWidth(10); // 菜单阴影的宽度
        slidingMenu.setBehindScrollScale(0.25f);
        slidingMenu.setFadeDegree(0.25f);

    }

}
