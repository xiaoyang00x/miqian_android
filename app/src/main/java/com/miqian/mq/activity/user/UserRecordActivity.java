package com.miqian.mq.activity.user;

import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.activity.BaseActivity;
import com.miqian.mq.adapter.FragmentAdapter;
import com.miqian.mq.fragment.FragmentCharge;
import com.miqian.mq.fragment.FragmentBuy;
import com.miqian.mq.fragment.FragmentExpire;
import com.miqian.mq.fragment.FragmentRedeem;
import com.miqian.mq.fragment.FragmentRoolout;
import com.miqian.mq.views.WFYTitle;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/6/30.
 */

public class UserRecordActivity extends BaseActivity {
    private List<Fragment> mFragmentList = new ArrayList<Fragment>();
    private FragmentAdapter mFragmentAdapter;
    /**
     * Fragment
     */
    private FragmentCharge frame1;
    private FragmentBuy frame2;
    private FragmentExpire frame3;
    private FragmentRedeem frame4;
    private FragmentRoolout frame5;
    /**
     * ViewPager的当前选中页
     */
    private int currentIndex;
    /**
     * 屏幕的宽度
     */
    private int screenWidth;

    @BindView(R.id.tv_charge)
    TextView  tvcharge;
    @BindView(R.id.tv_buy)
    TextView  tvBuy;
    @BindView(R.id.tv_expired)
    TextView  tvExpired;
    @BindView(R.id.tv_redeem)
    TextView  tvRedeem;
    @BindView(R.id.tv_withraw)
    TextView  tvWithraw;
    @BindView(R.id.id_tab_line_iv)
    ImageView  mTabLineIv;
    @BindView(R.id.id_page_vp)
    ViewPager  mPageVp;

    @Override
    public void obtainData() {
        init();
        initTabLineWidth();
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);

    }
    @OnClick(R.id.tv_charge)
    public void charge(){
        mPageVp.setCurrentItem(0);

    }
    @OnClick(R.id.tv_buy)
    public void buy(){
        mPageVp.setCurrentItem(1);
    }
    @OnClick(R.id.tv_expired)
    public void expired(){
        mPageVp.setCurrentItem(2);
    }
    @OnClick(R.id.tv_redeem)
    public void redeem(){
        mPageVp.setCurrentItem(3);
    }
    @OnClick(R.id.tv_withraw)
    public void withraw(){
        mPageVp.setCurrentItem(4);
    }

    private void init() {
        frame1 = new FragmentCharge();
        frame2 = new FragmentBuy();
        frame3 = new FragmentExpire();
        frame4 = new FragmentRedeem();
        frame5 = new FragmentRoolout();
        mFragmentList.add(frame1);
        mFragmentList.add(frame2);
        mFragmentList.add(frame3);
        mFragmentList.add(frame4);
        mFragmentList.add(frame5);

        mFragmentAdapter = new FragmentAdapter(
                this.getSupportFragmentManager(), mFragmentList);
        mPageVp.setAdapter(mFragmentAdapter);
        mPageVp.setCurrentItem(0);

        mPageVp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            /**
             * state滑动中的状态 有三种状态（0，1，2） 1：正在滑动 2：滑动完毕 0：什么都没做。
             */
            @Override
            public void onPageScrollStateChanged(int state) {

            }

            /**
             * position :当前页面，及你点击滑动的页面 offset:当前页面偏移的百分比
             * offsetPixels:当前页面偏移的像素位置
             */
            @Override
            public void onPageScrolled(int position, float offset,
                                       int offsetPixels) {
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mTabLineIv
                        .getLayoutParams();

                Log.e("offset:", offset + "");
                /**
                 * 利用currentIndex(当前所在页面)和position(下一个页面)以及offset来
                 * 设置mTabLineIv的左边距 滑动场景：
                 * 记3个页面,
                 * 从左到右分别为0,1,2,3,4
                 * 0->1;1->2;2->3;3->4;4->3;3->2;2->1;1->0
                 */
                if (currentIndex == 0 && position == 0)// 0->1
                {
                    lp.leftMargin = (int) (offset * (screenWidth * 1.0 / 5) + currentIndex
                            * (screenWidth / 5));

                } else if (currentIndex == 1 && position == 1) // 1->2
                {
                    lp.leftMargin = (int) (offset * (screenWidth * 1.0 / 5) + currentIndex
                            * (screenWidth / 5));
                } else if (currentIndex == 2 && position == 2) // 2->3
                {
                    lp.leftMargin = (int) (offset * (screenWidth * 1.0 / 5) + currentIndex
                            * (screenWidth / 5));
                } else if (currentIndex == 3 && position == 3) // 3->4
                {
                    lp.leftMargin = (int) (offset * (screenWidth * 1.0 / 5) + currentIndex
                            * (screenWidth / 5));
                } else if (currentIndex == 4 && position == 3) // 4->3
                {
                    lp.leftMargin = (int) (-(1 - offset)
                            * (screenWidth * 1.0 / 5) + currentIndex
                            * (screenWidth / 5));
                } else if (currentIndex == 3 && position == 2) // 3->2
                {
                    lp.leftMargin = (int) (-(1 - offset)
                            * (screenWidth * 1.0 / 5) + currentIndex
                            * (screenWidth / 5));
                } else if (currentIndex == 2 && position == 1) // 2->1
                {
                    lp.leftMargin = (int) (-(1 - offset)
                            * (screenWidth * 1.0 / 5) + currentIndex
                            * (screenWidth / 5));
                } else if (currentIndex == 1 && position == 0) // 1->0
                {
                    lp.leftMargin = (int) (-(1 - offset)
                            * (screenWidth * 1.0 / 5) + currentIndex
                            * (screenWidth / 5));

                }
                mTabLineIv.setLayoutParams(lp);
            }

            @Override
            public void onPageSelected(int position) {
                resetTextView();
                switch (position) {
                    case 0:
                        tvcharge.setTextColor(ContextCompat.getColor(mActivity, R.color.mq_r1_v2));
                        break;
                    case 1:
                        tvBuy.setTextColor(ContextCompat.getColor(mActivity, R.color.mq_r1_v2));
                        break;
                    case 2:
                        tvExpired.setTextColor(ContextCompat.getColor(mActivity, R.color.mq_r1_v2));
                        break;
                    case 3:
                        tvRedeem.setTextColor(ContextCompat.getColor(mActivity, R.color.mq_r1_v2));
                        break;
                    case 4:
                        tvWithraw.setTextColor(ContextCompat.getColor(mActivity, R.color.mq_r1_v2));
                        break;
                }
                currentIndex = position;
            }
        });

    }

    /**
     * 设置滑动条的宽度为屏幕的1/5(根据Tab的个数而定)
     */
    private void initTabLineWidth() {
        DisplayMetrics dpMetrics = new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay()
                .getMetrics(dpMetrics);
        screenWidth = dpMetrics.widthPixels;
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mTabLineIv
                .getLayoutParams();
        lp.width = screenWidth / 5;
        mTabLineIv.setLayoutParams(lp);
    }

    /**
     * 重置颜色
     */
    private void resetTextView() {
        tvcharge.setTextColor(ContextCompat.getColor(mActivity, R.color.mq_b5_v2));
        tvBuy.setTextColor(ContextCompat.getColor(mActivity, R.color.mq_b5_v2));
        tvExpired.setTextColor(ContextCompat.getColor(mActivity, R.color.mq_b5_v2));
        tvRedeem.setTextColor(ContextCompat.getColor(mActivity, R.color.mq_b5_v2));
        tvWithraw.setTextColor(ContextCompat.getColor(mActivity, R.color.mq_b5_v2));
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_userrecord;
    }

    @Override
    public void initTitle(WFYTitle mTitle) {
        mTitle.setTitleText("资金记录");

    }

    @Override
    protected String getPageName() {
        return "资金记录";
    }
}
