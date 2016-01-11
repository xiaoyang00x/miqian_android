package com.miqian.mq.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshSwipeMenuListView;
import com.miqian.mq.R;
import com.miqian.mq.adapter.MessageAdapter;
import com.miqian.mq.entity.MessageInfo;
import com.miqian.mq.entity.Meta;
import com.miqian.mq.entity.PushData;
import com.miqian.mq.entity.PushDataResult;
import com.miqian.mq.entity.UserMessageData;
import com.miqian.mq.entity.UserMessageResult;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.utils.ExtendOperationController;
import com.miqian.mq.utils.MobileOS;
import com.miqian.mq.utils.Pref;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.utils.UserUtil;
import com.miqian.mq.views.CustomDialog;
import com.miqian.mq.views.MySwipeRefresh;
import com.miqian.mq.views.WFYTitle;

import java.util.List;

/**
 * @author Administrator Tuliangtan 3.26
 */
public class AnnounceActivity extends BaseActivity implements OnClickListener, AbsListView.OnScrollListener, ExtendOperationController.ExtendOperationListener {

    private Button titleLeft;
    private Button titleRight;
    private ImageButton btLeft;
    private MySwipeRefresh swipeRefresh;
    private TextView textRight;

    private int messageType = 0; //0为消息，1为公告

    private SwipeMenuListView mSwipeMenuListView;
    private MessageAdapter adapter;
    private CustomDialog dialogTips;
    private int count;
    private List<MessageInfo> list;
    private PullToRefreshSwipeMenuListView pullToListView;
    private SwipeMenuCreator creator;
    private View footView;
    private boolean isOver;
    private boolean isLoading;
    private View mViewnoresult_data;
    private TextView tvTips;
    private TextView view_Refresh;
    private ImageView ivMessageData;
    private boolean hasUnread;
    private ProgressBar progressBarLoading;
    private TextView textLoading;
    private ExtendOperationController extendOperationController;

    @Override
    public void onCreate(Bundle arg0) {
        extendOperationController = ExtendOperationController.getInstance();
        extendOperationController.registerExtendOperationListener(this);
        super.onCreate(arg0);
    }

    @Override
    public void obtainData() {
        progressBarLoading.setVisibility(View.GONE);
        textLoading.setText("");
        if (messageType == 0 && !UserUtil.hasLogin(mActivity)) {
            mViewnoresult_data.setVisibility(View.VISIBLE);
            ivMessageData.setBackgroundResource(R.drawable.nomessage);
            view_Refresh.setVisibility(View.GONE);
            swipeRefresh.setVisibility(View.GONE);
            tvTips.setVisibility(View.VISIBLE);
            tvTips.setText("请登录后查看");
            return;
        }
        if (!swipeRefresh.isRefreshing()) {
            begin();
        }
        if (messageType == 0) {
            isLoading = true;
            HttpRequest.getMessageList(mActivity, "", new ICallback<UserMessageResult>() {
                @Override
                public void onSucceed(UserMessageResult result) {
                    isLoading = false;
                    end();
                    swipeRefresh.setRefreshing(false);
                    UserMessageData userMessageData = result.getData();
                    count = userMessageData.getCount();
                    list = userMessageData.getMsgList();
                    if (list != null && list.size() > 0) {
                        checkReadStatus(list);
                        showContentView();
                        if (list.size() < count) {
                            isOver = false;
                            progressBarLoading.setVisibility(View.VISIBLE);
                            textLoading.setText("加载更多");
                        } else {
                            isOver = true;
                            progressBarLoading.setVisibility(View.GONE);
                            textLoading.setText("");
                        }
                        adapter = new MessageAdapter(mActivity, list);
                        pullToListView.setAdapter(adapter);

                    } else {
                        showEmptyView();
                    }
                }

                @Override
                public void onFail(String error) {
                    if (list != null && adapter != null) {
                        list.clear();
                        adapter.notifyDataSetChanged();
                    }
                    isLoading = false;
                    end();
                    showErrorView();
                    swipeRefresh.setRefreshing(false);
                    Uihelper.showToast(mActivity, error);
                }
            });
        } else {
            HttpRequest.getPushList(mActivity, "", 50 + "", new ICallback<PushDataResult>() {
                @Override
                public void onSucceed(PushDataResult result) {
                    end();
                    swipeRefresh.setRefreshing(false);
                    PushData pushData = result.getData();
                    list = pushData.getPushList();
                    if (list != null && list.size() > 0) {
                        showContentView();
                        //和本地匹配是否已读
                        for (MessageInfo info : list) {
                            int id = info.getId();
                            boolean isReaded = Pref.getBoolean(Pref.PUSH + id, mActivity, false);
                            if (isReaded) {
                                info.setIsRead(true);
                            }
                        }
                        adapter = new MessageAdapter(mActivity, list);
                        pullToListView.setAdapter(adapter);
                    } else {
                        showEmptyView();
                    }
                }

                @Override
                public void onFail(String error) {
                    if (list != null && adapter != null) {
                        list.clear();
                        adapter.notifyDataSetChanged();
                    }
                    end();
                    swipeRefresh.setRefreshing(false);
                    showErrorView();
                    Uihelper.showToast(mActivity, error);
                }
            });
        }
    }

    private void checkReadStatus(List<MessageInfo> list) {
        if (list.size() == 0) {
            textRight.setText("");
            return;
        }
        for (MessageInfo messageInfo : list) {
            if (!messageInfo.isRead()) {
                hasUnread = true;
                break;
            } else {
                hasUnread = false;
            }
        }
        if (hasUnread) {
            textRight.setText("全部已读");
        } else {
            textRight.setText("全部清空");
        }
    }

    @Override
    protected void onDestroy() {
        ExtendOperationController.getInstance().doNotificationExtendOperation(ExtendOperationController.OperationKey.MessageState,null);
        extendOperationController.unRegisterExtendOperationListener(this);
        super.onDestroy();
    }

    public void initView() {

        footView = LayoutInflater.from(mActivity).inflate(R.layout.adapter_loading, null);
        progressBarLoading = (ProgressBar) footView.findViewById(R.id.progressBar);
        textLoading = (TextView) footView.findViewById(R.id.text_loading);


        btLeft = (ImageButton) findViewById(R.id.bt_left);
        btLeft.setOnClickListener(this);

        titleLeft = (Button) findViewById(R.id.title_left);
        titleRight = (Button) findViewById(R.id.title_right);

        textRight = (TextView) findViewById(R.id.tv_clearall);
        textRight.setOnClickListener(this);

        titleLeft.setOnClickListener(this);
        titleRight.setOnClickListener(this);

        pullToListView = (PullToRefreshSwipeMenuListView) findViewById(R.id.listview);
        pullToListView.setMode(PullToRefreshBase.Mode.DISABLED);
        pullToListView.setOnScrollListener(this);
        pullToListView.getRefreshableView().addFooterView(footView);

        swipeRefresh = (MySwipeRefresh) findViewById(R.id.swipe_refresh);
        swipeRefresh.setOnPullRefreshListener(new MySwipeRefresh.OnPullRefreshListener() {
            @Override
            public void onRefresh() {
                isOver = false;
                obtainData();
            }
        });

        mViewnoresult_data = findViewById(R.id.frame_no_messagedata);
        view_Refresh = (TextView) findViewById(R.id.tv_refresh);
        tvTips = (TextView) findViewById(R.id.tv_tip);
        ivMessageData = (ImageView) findViewById(R.id.iv_messagedata);

        view_Refresh.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showContentView();
                obtainData();


            }
        });
        initPullToListView();
    }

    private void initPullToListView() {

        mSwipeMenuListView = (SwipeMenuListView) pullToListView.getRefreshableView();
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // TODO Auto-generated method stub
                switch (menu.getViewType()) {
                    case 0:
                        createMenu1(menu);
                        break;
                }
            }
        };
        mSwipeMenuListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {
                //删除消息
                begin();
                MessageInfo messageInfo = list.get(position);
                HttpRequest.deleteMessage(mActivity, new ICallback<Meta>() {
                    @Override
                    public void onSucceed(Meta result) {
                        end();
                        Uihelper.showToast(mActivity, "删除成功");
                        list.remove(position);
                        checkReadStatus(list);
                        adapter.notifyDataSetChanged();
                        //没有数据
                        if (list.size() == 0) {
                            showEmptyView();
                        }
                    }

                    @Override
                    public void onFail(String error) {
                        end();
                        Uihelper.showToast(mActivity, error);
                    }
                }, messageInfo.getId(), "", 0);

                return false;
            }
        });
        mSwipeMenuListView.setMenuCreator(creator);
        pullToListView.setMode(PullToRefreshBase.Mode.DISABLED);
        pullToListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, final int position, long arg3) {
                if (position == 0) {
                } else {
                    //消息
                    MessageInfo messageInfo = list.get(position - 1);
                    int msgType = messageInfo.getMsgType();
                    if (messageType == 1) {
                        //公告
                        boolean isReaded = Pref.getBoolean(Pref.PUSH + list.get(position - 1).getId(), mActivity, false);
                        if (!isReaded) {
                            Pref.saveBoolean(Pref.PUSH + list.get(position - 1).getId(), true, mActivity);
                            list.get(position - 1).setIsRead(true);
                            adapter.notifyDataSetChanged();
                        }
                    }
                    switch (msgType) {
                        // 内置浏览器
                        case 51:
                        case 52:
                        case 53:
                            WebActivity.startActivity(mContext, messageInfo.getJumpUrl());
                            break;
                        default:
                            Intent intent_other = new Intent(mActivity, AnnounceResultActivity.class);
                            intent_other.putExtra("id", messageInfo.getId());
                            intent_other.putExtra("position", position - 1);
                            if (messageType == 0) {
                                intent_other.putExtra("isMessage", true);
                            } else {
                                intent_other.putExtra("isMessage", false);
                            }
                            startActivity(intent_other);
                    }
                }
            }
        });
    }

    private void createMenu1(SwipeMenu menu) {
        SwipeMenuItem item1 = new SwipeMenuItem(getApplicationContext());
        item1.setBackground(getResources().getDrawable(R.drawable.shape_swip_red));
        item1.setWidth(Uihelper.dip2px(mActivity, 90));
        item1.setIcon(getResources().getDrawable(R.drawable.messsagedelete_selector));
        menu.addMenuItem(item1);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_annouce;
    }

    @Override
    public void initTitle(WFYTitle mTitle) {
        View parentView = (View) mTitle.getParent();
        parentView.setVisibility(View.GONE);
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
                    dismiss();
                    begin();
                    //清空消息
                    HttpRequest.deleteMessage(mActivity, new ICallback<Meta>() {
                        @Override
                        public void onSucceed(Meta result) {
                            end();
                            Uihelper.showToast(mActivity, "删除成功");
                            list.clear();
                            adapter.notifyDataSetChanged();
                            textRight.setText("");
                            showEmptyView();
                        }

                        @Override
                        public void onFail(String error) {
                            end();
                            Uihelper.showToast(mActivity, error);
                        }
                    }, list.get(0).getId(), list.get(list.size() - 1).getId() + "", 1);
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
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.title_left:
                titleLeft.setTextColor(getResources().getColor(R.color.mq_r1));
                titleLeft.setBackgroundResource(R.drawable.bt_regualr_tab_left_selected);
                titleRight.setTextColor(getResources().getColor(R.color.white));
                titleRight.setBackgroundResource(R.drawable.bt_regualr_tab_right);
                //消息
                isOver = false;
                mSwipeMenuListView.setMenuCreator(creator);
                messageType = 0;
                if (list != null && adapter != null) {
                    list.clear();
                    adapter.notifyDataSetChanged();
                }
                obtainData();
                textRight.setText("");
                break;
            case R.id.title_right:

                titleLeft.setTextColor(getResources().getColor(R.color.white));
                titleLeft.setBackgroundResource(R.drawable.bt_regualr_tab_left);
                titleRight.setTextColor(getResources().getColor(R.color.mq_r1));
                titleRight.setBackgroundResource(R.drawable.bt_regualr_tab_right_selected);
                //公告
                mSwipeMenuListView.setMenuCreator(null);
                messageType = 1;
                if (list != null && adapter != null) {
                    list.clear();

                    adapter.notifyDataSetChanged();
                }
                obtainData();
                textRight.setText("");
                break;
            case R.id.bt_left:
                AnnounceActivity.this.finish();
                break;
            case R.id.tv_clearall://清空消息
                if (hasUnread) {
                    //设置全部已读
                    begin();
                    HttpRequest.setMessageReaded(mActivity, new ICallback<Meta>() {
                        @Override
                        public void onSucceed(Meta result) {
                            end();
                            for (MessageInfo messageInfo : list) {
                                messageInfo.setIsRead(true);
                            }
                            checkReadStatus(list);
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onFail(String error) {
                            end();
                            Uihelper.showToast(mActivity, error);
                        }
                    }, list.get(0).getId() + "", list.get(list.size() - 1).getId() + "", 1);


                } else {
                    showDialog();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        View firstView = view.getChildAt(firstVisibleItem);
        // 当firstVisibleItem是第0位。如果firstView==null说明列表为空，需要刷新;或者top==0说明已经到达列表顶部, 也需要刷新
        if (firstVisibleItem == 0 && (firstView == null || firstView.getTop() == 0)) {
            swipeRefresh.setEnabled(true);
        } else {
            swipeRefresh.setEnabled(false);
        }
        if (messageType == 0) {
            if (visibleItemCount + firstVisibleItem >= totalItemCount - 1 && !isOver && !isLoading) {
                if (list.size() < count) {
                    isOver = true;
                    isLoading = true;
                    HttpRequest.getMessageList(mActivity, list.get(list.size() - 1).getId() + "", new ICallback<UserMessageResult>() {
                        @Override
                        public void onSucceed(UserMessageResult result) {
                            isLoading = false;
                            UserMessageData userMessageData = result.getData();
                            count = userMessageData.getCount();
                            List<MessageInfo> tempList = userMessageData.getMsgList();
                            if (tempList != null && tempList.size() > 0) {
                                list.addAll(tempList);
                                checkReadStatus(list);
                                adapter.notifyDataSetChanged();
                            }
                            if (list.size() < count) {
                                isOver = false;
                            } else {
                                progressBarLoading.setVisibility(View.GONE);
                                textLoading.setText("没有更多");
                            }
                        }

                        @Override
                        public void onFail(String error) {
                            isLoading = false;
                            Uihelper.showToast(mActivity, error);
                        }
                    });
                }
            }
        }
    }

    /**
     * 无数据
     */
    protected void showEmptyView() {
        mViewnoresult_data.setVisibility(View.VISIBLE);
        if (view_Refresh != null) {
            view_Refresh.setVisibility(View.GONE);
        }
        swipeRefresh.setVisibility(View.GONE);
        tvTips.setVisibility(View.VISIBLE);
        tvTips.setText("暂时没有数据");
        ivMessageData.setBackgroundResource(R.drawable.nomessage);
    }

    /**
     * 显示数据
     */
    protected void showContentView() {
        mViewnoresult_data.setVisibility(View.GONE);
        swipeRefresh.setVisibility(View.VISIBLE);

    }

    /**
     * 获取失败，请重新获取
     */
    protected void showErrorView() {

        mViewnoresult_data.setVisibility(View.VISIBLE);
        swipeRefresh.setVisibility(View.GONE);
        view_Refresh.setVisibility(View.VISIBLE);
        tvTips.setVisibility(View.VISIBLE);
        tvTips.setText("数据获取失败，请重新获取");
        ivMessageData.setBackgroundResource(R.drawable.error_data);

    }

    @Override
    public void excuteExtendOperation(int operationKey, Object data) {
        if (operationKey == ExtendOperationController.OperationKey.RefeshMessage) {
            int position = (int) data;
            MessageInfo messageInfo = list.get(position);
            if (!messageInfo.isRead()) {
                list.get(position).setIsRead(true);
                adapter.notifyDataSetChanged();
                checkReadStatus(list);
            }
        }
    }

}
