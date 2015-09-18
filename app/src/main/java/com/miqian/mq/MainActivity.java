package com.miqian.mq;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.KeyEvent;

import com.miqian.mq.activity.AnnounceActivity;
import com.miqian.mq.activity.AnnounceResultActivity;
import com.miqian.mq.database.MyDataBaseHelper;
import com.miqian.mq.entity.JpushInfo;
import com.miqian.mq.fragment.FragmentMain;
import com.miqian.mq.receiver.JpushHelper;
import com.miqian.mq.utils.Pref;
import com.miqian.mq.utils.UserUtil;

public class MainActivity extends FragmentActivity {

    private static final String TAG = "MainActivity";

    private Activity mContext;
    private Fragment mContent;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_main);
        setMainView(savedInstanceState);
        MyApplication.getInstance().setIsCurrent(true);
        //设置别名
        JpushHelper.setAlias(this);
        handleJpush();
    }

    private void handleJpush() {

        //判断是否是是极光推送
        boolean isPush = Pref.getBoolean(Pref.IsPush, mContext, false);
        if (isPush) {
            String userId = null;
            // 是否登录
            if (!UserUtil.hasLogin(mContext)) {
                userId = Pref.VISITOR;
            } else {
                userId = Pref.getString(Pref.USERID, mContext, Pref.VISITOR);
            }
            JpushInfo jInfo = MyDataBaseHelper.getInstance().getjpushInfo(userId).get(0);
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
                case 2:   //列表页
                    startActivity(new Intent(mContext, AnnounceActivity.class));
                    break;
                case 3:  //详情页
                    Intent intent = new Intent(mContext, AnnounceResultActivity.class);
//							intent.putExtra("classid", jpushInfo.getClassid());
                    intent.putExtra("noticeId", jInfo.getId());
                    startActivity(intent);
                    break;

                // 内置浏览器
                case 4:
//                        notificationIntent = new Intent(context, WebViewActivity.class);
                    break;

                default:
                    break;
            }
        }
    }

    public void setMainView(Bundle savedInstanceState) {
        // 设置主页的View
        if (savedInstanceState != null) {
            mContent = getSupportFragmentManager().getFragment(savedInstanceState, "mContent");
        }
        if (mContent == null) {
            mContent = new FragmentMain();
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, mContent).commitAllowingStateLoss();

        // 设置菜单View
//        getSupportFragmentManager().beginTransaction().replace(R.id.frame_menu, new FragmentMenu()).commitAllowingStateLoss();

        // 设置菜单样式
//		slidingMenu.setBehindOffset(Config.WIDTH / 4); // 菜单偏移的距离
//        slidingMenu.setShadowWidth(10); // 菜单阴影的宽度
//        slidingMenu.setBehindScrollScale(0.25f);
//        slidingMenu.setFadeDegree(0.25f);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            MyApplication.getInstance().setIsCurrent(false);
        }
        return super.onKeyDown(keyCode, event);
    }

}
