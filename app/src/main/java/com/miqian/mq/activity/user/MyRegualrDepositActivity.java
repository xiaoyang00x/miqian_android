package com.miqian.mq.activity.user;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.miqian.mq.R;
import com.miqian.mq.activity.BaseActivity;
import com.miqian.mq.adapter.MyRegularDepositAdapter;
import com.miqian.mq.entity.UserRegular;
import com.miqian.mq.entity.UserRegularResult;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.views.WFYTitle;

import java.util.List;

public class MyRegualrDepositActivity extends BaseActivity implements View.OnClickListener {

    private Button titleLeft;
    private Button titleRight;
    private RecyclerView mRecyclerView;

    private MyRegularDepositAdapter mAdapter;
//    private List<MyRegularDepositAdapter.Item> studentList;

    private String isExpiry = "N";//是否结息 N：计息中 Y：已结息
    private String isForce = "0";//是否强制刷新 1强制刷新 0不强制刷新

    private UserRegular userRegular;
    private List<UserRegular.RegInvest> regInvestList;

    @Override
    public void obtainData() {
        HttpRequest.getUserRegular(mActivity, new ICallback<UserRegularResult>() {
            @Override
            public void onSucceed(UserRegularResult result) {
                userRegular = result.getData();
                regInvestList = userRegular.getRegInvest();
                refreshView();
            }

            @Override
            public void onFail(String error) {

            }
        }, "1", "20", isExpiry, isForce);
    }

    @Override
    public void initView() {
        titleLeft = (Button) findViewById(R.id.title_left);
        titleRight = (Button) findViewById(R.id.title_right);

        titleLeft.setOnClickListener(this);
        titleRight.setOnClickListener(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

//        mAdapter = new MyRegularDepositAdapter(studentList, mRecyclerView);
//        mRecyclerView.setAdapter(mAdapter);
//
//        mAdapter.setOnLoadMoreListener(new MyRegularDepositAdapter.OnLoadMoreListener() {
//            @Override
//            public void onLoadMore() {
//                //add null , so the adapter will check view_type and show progress bar at bottom
//                studentList.add(null);
//                mAdapter.notifyItemInserted(studentList.size() - 1);
//
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        //   remove progress item
//                        studentList.remove(studentList.size() - 1);
//                        mAdapter.notifyItemRemoved(studentList.size());
//                        //add items one by one
//                        int start = studentList.size();
//                        int end = start + 20;
//
//                        for (int i = start + 1; i <= end; i++) {
//                            studentList.add(new MyRegularDepositAdapter.Item("Item " + i, "AndroidStudent" + i + "@gmail.com"));
//                            mAdapter.notifyItemInserted(studentList.size());
//                        }
//                        mAdapter.setLoaded();
//                        //or you can add all at once but do not forget to call mAdapter.notifyDataSetChanged();
//                    }
//                }, 2000);
//            }
//        });
    }

    private void refreshView() {
        if (mAdapter == null) {
            mAdapter = new MyRegularDepositAdapter(regInvestList, mRecyclerView);
            mRecyclerView.setAdapter(mAdapter);
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_my_regualr_deposit;
    }

    @Override
    public void initTitle(WFYTitle mTitle) {
        View parentView = (View) mTitle.getParent();
        parentView.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_left:
                titleLeft.setTextColor(getResources().getColor(R.color.mq_r1));
                titleLeft.setBackgroundResource(R.drawable.bt_regualr_tab_left_selected);
                titleRight.setTextColor(getResources().getColor(R.color.white));
                titleRight.setBackgroundResource(R.drawable.bt_regualr_tab_right);
                isExpiry = "N";
                obtainData();
                break;
            case R.id.title_right:
                titleLeft.setTextColor(getResources().getColor(R.color.white));
                titleLeft.setBackgroundResource(R.drawable.bt_regualr_tab_left);
                titleRight.setTextColor(getResources().getColor(R.color.mq_r1));
                titleRight.setBackgroundResource(R.drawable.bt_regualr_tab_right_selected);
                isExpiry = "Y";
                obtainData();
                break;
        }
    }
}
