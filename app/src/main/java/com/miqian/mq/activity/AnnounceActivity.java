package com.miqian.mq.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.adapter.MessageAdapter;
import com.miqian.mq.entity.MessageInfo;
import com.miqian.mq.entity.Meta;
import com.miqian.mq.entity.UserMessageData;
import com.miqian.mq.entity.UserMessageResult;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.utils.ExtendOperationController;
import com.miqian.mq.utils.JsonUtil;
import com.miqian.mq.utils.MobileOS;
import com.miqian.mq.utils.Pref;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.utils.UserUtil;
import com.miqian.mq.views.MySwipeRefresh;
import com.miqian.mq.views.WFYTitle;
import com.miqian.mq.views.swipemenulistview.SwipeMenu;
import com.miqian.mq.views.swipemenulistview.SwipeMenuCreator;
import com.miqian.mq.views.swipemenulistview.SwipeMenuListView;

import java.util.List;

/**
 * @author Administrator Tuliangtan 3.26
 */
public class AnnounceActivity extends BaseActivity implements OnClickListener, AbsListView.OnScrollListener, ExtendOperationController.ExtendOperationListener {

    private MySwipeRefresh swipeRefresh;
    private MessageAdapter adapter;
    private int count;
    private List<MessageInfo> list;
    private SwipeMenuListView mSwipeMenuListView;
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
        if (!UserUtil.hasLogin(mActivity)) {
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
                    mSwipeMenuListView.setdataList(list);
                    mSwipeMenuListView.setAdapter(adapter);

                } else {
                    showEmptyView();
                }
            }

            @Override
            public void onFail(String error) {
                //判断是否有缓存
                String messageCash = Pref.getString(Pref.DATA_MESSAGE, mActivity, "");
                if (TextUtils.isEmpty(messageCash)) {
                    if (list != null && adapter != null) {
                        list.clear();
                        mSwipeMenuListView.setdataList(list);
                        adapter.notifyDataSetChanged();
                    }
                    showErrorView();
                } else {
                    //显示缓存信息
                    UserMessageResult userMessageResult = JsonUtil.parseObject(messageCash, UserMessageResult.class);
                    list = userMessageResult.getData().getMsgList();
                    adapter = new MessageAdapter(mActivity, list);
                    mSwipeMenuListView.setdataList(list);
                    mSwipeMenuListView.setAdapter(adapter);
                }
                swipeRefresh.setRefreshing(false);
                isLoading = false;
                end();
                Uihelper.showToast(mActivity, error);
            }
        });


    }

    private void checkReadStatus(List<MessageInfo> list) {
        if (list.size() == 0) {
            mTitle.setRightText("");
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
            mTitle.setRightText("全部已读");
        } else {
            mTitle.setRightText("");
        }
    }

    @Override
    protected void onDestroy() {
        ExtendOperationController.getInstance().doNotificationExtendOperation(ExtendOperationController.OperationKey.MessageState, null);
        extendOperationController.unRegisterExtendOperationListener(this);
        super.onDestroy();
    }

    public void initView() {

        footView = LayoutInflater.from(mActivity).inflate(R.layout.adapter_loading, null);
        progressBarLoading = (ProgressBar) footView.findViewById(R.id.progressBar);
        textLoading = (TextView) footView.findViewById(R.id.text_loading);
        swipeRefresh = (MySwipeRefresh) findViewById(R.id.swipe_refresh);
        mSwipeMenuListView = (SwipeMenuListView) findViewById(R.id.listview);
        mSwipeMenuListView.setOnScrollListener(this);
        mSwipeMenuListView.addFooterView(footView);
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
        mSwipeMenuListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public void onMenuItemClick(final int position, SwipeMenu menu, int index) {
                //删除消息
                begin();
                MessageInfo messageInfo = list.get(position);
                HttpRequest.setMessageReaded(mActivity, new ICallback<Meta>() {
                    @Override
                    public void onSucceed(Meta result) {
                        end();
                        Uihelper.showToast(mActivity, "设置成功");
                        checkReadStatus(list);
                        list.get(position).setIsRead(true);
                        mSwipeMenuListView.setdataList(list);
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
                }, messageInfo.getId() + "", "", 0);

            }
        });
        mSwipeMenuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, final int position, long arg3) {
                if (position == list.size() + 1) {
                } else {
                    //消息
                    MessageInfo messageInfo = list.get(position);
                    int msgType = messageInfo.getMsgType();
                    switch (msgType) {
                        // 内置浏览器
                        case 50:
                        case 51:
                        case 52:
                        case 53:
                            WebActivity.startActivity(mContext, messageInfo.getJumpUrl());
                            break;
                        default:
                            Intent intent_other = new Intent(mActivity, AnnounceResultActivity.class);
                            intent_other.putExtra("id", messageInfo.getId());
                            intent_other.putExtra("position", position);
                            intent_other.putExtra("isMessage", true);
                            startActivity(intent_other);
                    }
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_message;
    }

    @Override
    public void initTitle(WFYTitle mTitle) {
        mTitle.setTitleText("消息");
        mTitle.setRightText("");
        mTitle.setOnRightClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
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
                        mSwipeMenuListView.setdataList(list);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFail(String error) {
                        end();
                        Uihelper.showToast(mActivity, error);
                    }
                }, list.get(0).getId() + "", list.get(list.size() - 1).getId() + "", 1);
            }
        });

    }

    @Override
    protected String getPageName() {
        return "消息";
    }


    @Override
    public void onClick(View v) {
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        if (isOver || isLoading) {
            return;
        }

        View firstView = view.getChildAt(firstVisibleItem);
        // 当firstVisibleItem是第0位。如果firstView==null说明列表为空，需要刷新;或者top==0说明已经到达列表顶部, 也需要刷新
        if (firstVisibleItem == 0 && (firstView == null || firstView.getTop() == 0)) {
            swipeRefresh.setEnabled(true);
        } else {
            swipeRefresh.setEnabled(false);
        }
        if (visibleItemCount + firstVisibleItem >= totalItemCount - 1) {
            if (list != null && list.size() < count) {
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
                            mSwipeMenuListView.setdataList(list);
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
        tvTips.setText("暂时没有消息");
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
        if (MobileOS.getNetworkType(mContext) == -1) {
            tvTips.setText("暂时没有网络");
            ivMessageData.setBackgroundResource(R.drawable.nonetwork);
        } else {
            tvTips.setText("数据获取失败，请重新获取");
            ivMessageData.setBackgroundResource(R.drawable.error_data);
        }

    }

    @Override
    public void excuteExtendOperation(int operationKey, Object data) {
        if (operationKey == ExtendOperationController.OperationKey.RefeshMessage) {
            int position = (int) data;
            MessageInfo messageInfo = list.get(position);
            if (!messageInfo.isRead()) {
                list.get(position).setIsRead(true);
                mSwipeMenuListView.setdataList(list);
                adapter.notifyDataSetChanged();
                checkReadStatus(list);
            }
        }
    }

}