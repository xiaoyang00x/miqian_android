package com.miqian.mq.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.adapter.RegularListAdapter;
import com.miqian.mq.entity.GetRegularInfo;
import com.miqian.mq.entity.GetRegularResult;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.views.MySwipeRefresh;
import com.miqian.mq.views.WFYTitle;

/**
 * Created by guolei_wang on 16/1/23.
 */
public class RegularListActivity extends BaseActivity {
    public static final String KEY_PROMID = "KEY_PROMID";
    RegularListAdapter mAdapter;
    private GetRegularInfo mData;
    private String promId; //促销 ID

    private RecyclerView recyclerView;
    private MySwipeRefresh swipeRefresh;
    private TextView tv_description;
    @Override
    public void obtainData() {
        getFitSubject();
    }

    public static void startActivity(Context context, String subjectId) {
        Intent intent = new Intent(context, RegularListActivity.class);
        intent.putExtra(KEY_PROMID, subjectId);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        promId = getIntent().getStringExtra(KEY_PROMID);
        super.onCreate(savedInstanceState);

    }

    @Override
    public void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        tv_description = (TextView) findViewById(R.id.tv_description);
        swipeRefresh = (MySwipeRefresh) findViewById(R.id.swipe_refresh);
        swipeRefresh.setOnPullRefreshListener(new MySwipeRefresh.OnPullRefreshListener() {
            @Override
            public void onRefresh() {
                getFitSubject();
            }
        });

        final LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            //用来标记是否正在向最后一个滑动，既是否向右滑动或向下滑动
//            boolean isSlidingToLast = false;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                // 当不滚动时
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    //获取最后一个完全显示的ItemPosition
                    int lastVisibleItem = manager.findLastCompletelyVisibleItemPosition();
                    int totalItemCount = manager.getItemCount();

                    // 判断是否滚动到底部，并且是向右滚动
                    if (lastVisibleItem == (totalItemCount - 1)) {
//                        Toast.makeText(mActivity, "加载更多", Toast.LENGTH_SHORT).show();
                    }
                }
            }

        });

        if (mData != null) {
            mAdapter = new RegularListAdapter(mData, mApplicationContext, swipeRefresh);
            recyclerView.setAdapter(mAdapter);
            tv_description.setText(mData.getFitSubjectDesc());
        }
    }

    private boolean inProcess = false;
    private final Object mLock = new Object();

    private void getFitSubject() {
        if (inProcess) {
            return;
        }
        synchronized (mLock) {
            inProcess = true;
        }
        begin();
        swipeRefresh.setRefreshing(true);
        HttpRequest.getFitSubject(RegularListActivity.this, promId, new ICallback<GetRegularResult>() {

            @Override
            public void onSucceed(GetRegularResult result) {
                synchronized (mLock) {
                    inProcess = false;
                }
                end();
                swipeRefresh.setRefreshing(false);
                if (result == null) return;
                mData = result.getData();
                if (mData == null) return;
                mAdapter = new RegularListAdapter(mData, mApplicationContext, swipeRefresh);
                recyclerView.setAdapter(mAdapter);
                tv_description.setText(mData.getFitSubjectDesc());
            }

            @Override
            public void onFail(String error) {
                synchronized (mLock) {
                    inProcess = false;
                }
                end();
                swipeRefresh.setRefreshing(false);
                Uihelper.showToast(RegularListActivity.this, error);
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.regular_list_activity;
    }

    @Override
    public void initTitle(WFYTitle mTitle) {
        mTitle.setTitleText("红包/券使用");
    }

    @Override
    protected String getPageName() {
        return "红包/券使用";
    }
}
