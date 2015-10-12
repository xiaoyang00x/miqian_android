package com.miqian.mq.activity.user;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.miqian.mq.R;
import com.miqian.mq.adapter.MyRegularDepositAdapter;

import java.util.ArrayList;
import java.util.List;

public class MyRegualrDepositActivity extends Activity {

    private RecyclerView mRecyclerView;
    private MyRegularDepositAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    //private TextView tvEmptyView;
    private List<MyRegularDepositAdapter.Item> studentList;

    protected Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_regualr_deposit);
        studentList = new ArrayList<MyRegularDepositAdapter.Item>();
        handler = new Handler();
        loadData();

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        //tvEmptyView = (TextView)findViewById(R.id.empty_view);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);

        // use a linear layout manager
        mRecyclerView.setLayoutManager(mLayoutManager);

        // create an Object for Adapter
        mAdapter = new MyRegularDepositAdapter(studentList, mRecyclerView);

        // set the adapter object to the Recyclerview
        mRecyclerView.setAdapter(mAdapter);
        //	 mAdapter.notifyDataSetChanged();

        //
        //if (studentList.isEmpty()) {
        //  mRecyclerView.setVisibility(View.GONE);
        //  tvEmptyView.setVisibility(View.VISIBLE);
        //
        //} else {
        //  mRecyclerView.setVisibility(View.VISIBLE);
        //  tvEmptyView.setVisibility(View.GONE);
        //}

        mAdapter.setOnLoadMoreListener(new MyRegularDepositAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                //add null , so the adapter will check view_type and show progress bar at bottom
                studentList.add(null);
                mAdapter.notifyItemInserted(studentList.size() - 1);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //   remove progress item
                        studentList.remove(studentList.size() - 1);
                        mAdapter.notifyItemRemoved(studentList.size());
                        //add items one by one
                        int start = studentList.size();
                        int end = start + 20;

                        for (int i = start + 1; i <= end; i++) {
                            studentList.add(new MyRegularDepositAdapter.Item("Item " + i, "AndroidStudent" + i + "@gmail.com"));
                            mAdapter.notifyItemInserted(studentList.size());
                        }
                        mAdapter.setLoaded();
                        //or you can add all at once but do not forget to call mAdapter.notifyDataSetChanged();
                    }
                }, 2000);
            }
        });
    }

    private void loadData() {
        for (int i = 1; i <= 20; i++) {
            studentList.add(new MyRegularDepositAdapter.Item("Item " + i, "androidstudent" + i + "@gmail.com"));
        }
    }
}
