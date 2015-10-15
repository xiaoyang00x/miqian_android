package com.miqian.mq.activity;

import android.content.Intent;
import android.text.TextUtils;
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
public class AnnounceActivity extends BaseActivity
        implements ExtendOperationController.ExtendOperationListener {

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
            //			mView_noresult.setVisibility(View.VISIBLE);
            mTitle.setRightText("");
        } else {
            //			if (mView_noresult != null) {
            //
            //				mView_noresult.setVisibility(View.GONE);
            //				mTitle.setRightText("清空");
            //			}
        }
        Uihelper.trace("" + jpushInfolist.size());

        PullToRefreshSwipeMenuListView pullToListView =
                (PullToRefreshSwipeMenuListView) findViewById(R.id.listview);

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
                MyDataBaseHelper.getInstance(mActivity).detetjpushInfo(jpushInfo);
                jpushInfolist.remove(jpushInfo);
                Uihelper.getMessageCount(0, mActivity);
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
                    String uritype = jpushInfo.getUriType();
                    if (!TextUtils.isEmpty(uritype)) {
//                        switch (Integer.valueOf(Integer.valueOf(uritype))) {

//                            // 详情页
//                            case 1:
//                            case 2:
//                            case 3:
                                Intent intent = new Intent(mActivity, AnnounceResultActivity.class);
                                intent.putExtra("id", jpushInfo.getId());
                                intent.putExtra("pushSource", jpushInfo.getPushSource());
                                startActivity(intent);
//                                break;
//                            // 内置浏览器
//                            case 4:
//                                //							WebViewActivity.doIntent(mActivity, jpushInfo.getUrl(), true, null);
//                                break;
//                            default:
//                                break;
                        }
                        uritype = null;
//                    } else {
//                        Intent intent = new Intent(mActivity, AnnounceResultActivity.class);
//                        //						intent.putExtra("classid", jpushInfo.getClassid());
//                        intent.putExtra("noticeId", jpushInfo.getId());
//                        startActivity(intent);
//                    }
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
        this.setIntent(intent);
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
                    //					mView_noresult.setVisibility(View.VISIBLE);
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
            case ExtendOperationController.OperationKey.RERESH_XGPUSH:
                // 更新数据
                obtainData();
                break;
        }
    }
}
