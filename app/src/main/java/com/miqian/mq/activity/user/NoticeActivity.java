package com.miqian.mq.activity.user;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.activity.AnnounceResultActivity;
import com.miqian.mq.activity.BaseActivity;
import com.miqian.mq.activity.WebActivity;
import com.miqian.mq.adapter.MessageAdapter;
import com.miqian.mq.entity.MessageInfo;
import com.miqian.mq.entity.PushData;
import com.miqian.mq.entity.PushDataResult;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.utils.JsonUtil;
import com.miqian.mq.utils.MobileOS;
import com.miqian.mq.utils.Pref;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.views.MySwipeRefresh;
import com.miqian.mq.views.WFYTitle;
import com.miqian.mq.views.swipemenulistview.SwipeMenuListView;

import java.util.List;

/** 公告列表
 * @author Tuliangtan 2016.5.23
 */
public class NoticeActivity extends BaseActivity {

    private MySwipeRefresh swipeRefresh;
    private MessageAdapter adapter;
    private List<MessageInfo> list;
    private SwipeMenuListView mSwipeMenuListView;
    private View mViewnoresult_data;
    private TextView tvTips;
    private TextView view_Refresh;
    private ImageView ivMessageData;

    @Override
    public void obtainData() {
        if (!swipeRefresh.isRefreshing()) {
            begin();
        }
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
                    mSwipeMenuListView.setAdapter(adapter);
                } else {
                    showEmptyView();
                }
            }

            @Override
            public void onFail(String error) {
                //判断是否有缓存
                String messageCash = Pref.getString(Pref.DATA_PUSH, mActivity, "");
                if (TextUtils.isEmpty(messageCash)) {
                    if (list != null && adapter != null) {
                        list.clear();
                        adapter.notifyDataSetChanged();
                    }
                    showErrorView();
                } else {
                    //显示缓存信息
                    PushDataResult pushDataResult = JsonUtil.parseObject(messageCash, PushDataResult.class);
                    list = pushDataResult.getData().getPushList();
                    adapter = new MessageAdapter(mActivity, list);
                    mSwipeMenuListView.setAdapter(adapter);
                }
                end();
                swipeRefresh.setRefreshing(false);
                Uihelper.showToast(mActivity, error);
            }
        });
    }

    public void initView() {

        swipeRefresh = (MySwipeRefresh) findViewById(R.id.swipe_refresh);
        mSwipeMenuListView = (SwipeMenuListView) findViewById(R.id.listview);
        swipeRefresh.setOnPullRefreshListener(new MySwipeRefresh.OnPullRefreshListener() {
            @Override
            public void onRefresh() {
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
        mSwipeMenuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, final int position, long arg3) {
                if (position == list.size() + 1) {
                } else {
                    MessageInfo messageInfo = list.get(position);
                    int msgType = messageInfo.getMsgType();
                    boolean isReaded = Pref.getBoolean(Pref.PUSH + list.get(position).getId(), mActivity, false);
                    if (!isReaded) {
                        Pref.saveBoolean(Pref.PUSH + list.get(position).getId(), true, mActivity);
                        list.get(position).setIsRead(true);
                        adapter.notifyDataSetChanged();
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
                            intent_other.putExtra("position", position);
                            intent_other.putExtra("isMessage", false);
                            startActivity(intent_other);
                    }
                }
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_annouce;
    }

    @Override
    public void initTitle(WFYTitle mTitle) {
        mTitle.setTitleText("公告");
    }

    @Override
    protected String getPageName() {
        return "公告";
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
        tvTips.setText("暂时没有公告");
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
}