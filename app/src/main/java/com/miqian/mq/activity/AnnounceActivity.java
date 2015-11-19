package com.miqian.mq.activity;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshSwipeMenuListView;
import com.miqian.mq.R;
import com.miqian.mq.adapter.MessageAdapter;
import com.miqian.mq.database.MyDataBaseHelper;
import com.miqian.mq.entity.JpushInfo;
import com.miqian.mq.utils.ExtendOperationController;
import com.miqian.mq.utils.MobileOS;
import com.miqian.mq.utils.Pref;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.utils.UserUtil;
import com.miqian.mq.views.CustomDialog;
import com.miqian.mq.views.WFYTitle;

import java.util.Collections;
import java.util.List;

/**
 * @author Administrator Tuliangtan 3.26
 */
public class AnnounceActivity extends BaseActivity implements ExtendOperationController.ExtendOperationListener {

    private SwipeMenuListView mSwipeMenuListView;
    private MessageAdapter adapter;
    private List<JpushInfo> jpushInfolist;
    private CustomDialog dialogTips;

    @Override
    public void obtainData() {

        if (!UserUtil.hasLogin(mActivity)) {
            jpushInfolist = MyDataBaseHelper.getInstance(mActivity).getjpushInfo(Pref.VISITOR);
        } else {
            jpushInfolist = MyDataBaseHelper.getInstance(mActivity)
                    .getjpushInfo(Pref.getString(Pref.USERID, mActivity, Pref.VISITOR));
        }
        Collections.reverse(jpushInfolist);
        if (jpushInfolist == null || jpushInfolist.size() == 0) {
            showEmptyView();
            mTitle.setRightText("");
        } else {

        }

        PullToRefreshSwipeMenuListView pullToListView = (PullToRefreshSwipeMenuListView) findViewById(R.id.listview);

        mSwipeMenuListView = (SwipeMenuListView) pullToListView.getRefreshableView();
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem deleteItem = new SwipeMenuItem(mActivity);
                deleteItem.setBackground(getResources().getDrawable(R.drawable.shape_swip_red));
                deleteItem.setIcon(getResources().getDrawable(R.drawable.messsagedelete_selector));
                deleteItem.setWidth(Uihelper.px2dip(mActivity, MobileOS.getScreenWidth(mActivity) / 2));
                menu.addMenuItem(deleteItem);
            }
        };
        mSwipeMenuListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {

                JpushInfo jpushInfo = jpushInfolist.get(position);
                if (jpushInfo.getState().equals("1")) {
                    Uihelper.getMessageCount(-1, mActivity);
                    jpushInfo.setState("2");
                    MyDataBaseHelper.getInstance(mActivity).recordJpush(jpushInfo);
                }
                MyDataBaseHelper.getInstance(mActivity).detetjpushInfo(jpushInfo);
                jpushInfolist.remove(jpushInfo);
                Uihelper.showToast(mActivity, "删除成功");
                obtainData();

                return false;
            }
        });
        mSwipeMenuListView.setMenuCreator(creator);
        pullToListView.setMode(PullToRefreshBase.Mode.DISABLED);
        adapter = new MessageAdapter(this, jpushInfolist);
        pullToListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                if (position == 0) {

                } else {
                    JpushInfo jpushInfo = jpushInfolist.get(position - 1);
                    if (jpushInfo.getState().equals("1")) {
                        Uihelper.getMessageCount(-1, mActivity);
                        jpushInfo.setState("2");
                        MyDataBaseHelper.getInstance(mActivity).recordJpush(jpushInfo);
                    }
                    switch (Integer.valueOf(Integer.valueOf(jpushInfo.getUriType()))) {

                        case 0:
                        case 1:
                        case 2:
                        case 3:
                        case 4:
                        case 5:
                        case 6:
                        case 7:
                        case 8:
                        case 9:
                        case 10:
                        case 11:
                        case 12:
                        case 15:
                            Intent intent = new Intent(mActivity, AnnounceResultActivity.class);
                            intent.putExtra("id", jpushInfo.getId());
                            intent.putExtra("pushSource", jpushInfo.getPushSource());
                            startActivity(intent);
                            break;
                        // 内置浏览器
                        case 50:
                        case 51:
                        case 52:
                        case 53:
                            WebActivity.startActivity(mContext, jpushInfo.getUrl());
                            break;
                        default:
                            Intent intent_other = new Intent(mActivity, AnnounceResultActivity.class);
                            intent_other.putExtra("id", jpushInfo.getId());
                            intent_other.putExtra("pushSource", jpushInfo.getPushSource());
                            startActivity(intent_other);
                            break;
                    }
                }
            }
        });
        mSwipeMenuListView.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        ExtendOperationController.getInstance().unRegisterExtendOperationListener(this);
        super.onDestroy();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        AnnounceActivity.this.setIntent(intent);
        initView();
        obtainData();
    }

    @Override
    public void initView() {
        ExtendOperationController.getInstance().registerExtendOperationListener(this);
    }

    @Override
    protected void onResume() {
        obtainData();
        super.onResume();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_annouce;
    }

    @Override
    public void initTitle(WFYTitle mTitle) {
        mTitle.setTitleText("消息");
        mTitle.setRightText("清空");
        mTitle.setOnRightClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                showDialog();
            }
        });
    }

    @Override
    protected String getPageName() {
        return "消息";
    }

    private void showDialog() {
        initDialog();
        dialogTips.show();
    }

    private void initDialog() {
        if (dialogTips == null) {
            dialogTips = new CustomDialog(this, CustomDialog.CODE_TIPS) {

                @Override
                public void positionBtnClick() {
                    if (!UserUtil.hasLogin(mActivity)) {
                        MyDataBaseHelper.getInstance(mActivity).deleteall(Pref.VISITOR);
                    } else {
                        MyDataBaseHelper.getInstance(mActivity)
                                .deleteall(Pref.getString(Pref.USERID, mActivity, Pref.VISITOR));
                    }
                    Uihelper.getMessageCount(0, mActivity);
                    jpushInfolist.clear();
                    adapter.notifyDataSetChanged();
                    mTitle.setRightText("");
                    showEmptyView();
                    dismiss();
                }

                @Override
                public void negativeBtnClick() {

                }
            };
            dialogTips.setNegative(View.VISIBLE);
            dialogTips.setRemarks("     您确定删除所有消息吗？");
            dialogTips.setNegative("取消");
        }
    }

    @Override
    public void excuteExtendOperation(int operationKey, Object data) {
        switch (operationKey) {
            case ExtendOperationController.OperationKey.RERESH_JPUSH:
                // 更新数据
                obtainData();
                break;
        }
    }

}
