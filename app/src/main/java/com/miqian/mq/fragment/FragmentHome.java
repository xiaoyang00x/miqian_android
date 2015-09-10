package com.miqian.mq.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.miqian.mq.R;
import com.miqian.mq.adapter.HomeAdapter;
import java.util.Timer;
import java.util.TimerTask;

public class FragmentHome extends Fragment {

  private View view;
  //private Button btHttp;
  private Activity mContext;
  private RecyclerView recyclerView;
  private SwipeRefreshLayout swipeRefresh;
  HomeAdapter adapter;

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mContext = getActivity();
    if (savedInstanceState == null || view == null) {
      view = inflater.inflate(R.layout.frame_home, null);
    }
    ViewGroup parent = (ViewGroup) view.getParent();
    if (parent != null) {
      parent.removeView(view);
    }
    findViewById(view);

    final LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
    recyclerView.setLayoutManager(layoutManager);

    adapter = new HomeAdapter(mContext, 3);
    adapter.setHasFooter(true);
    recyclerView.setAdapter(adapter);
    recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
      boolean isPullUp = false;
      boolean isRefreshing = false;

      @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        isPullUp = dy > 0;
      }

      @Override public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        //super.onScrollStateChanged(recyclerView, newState);
        if (isPullUp && !isRefreshing && adapter.hasMoreData()) {
          int lastPos = layoutManager.findLastVisibleItemPosition();
          //if (lastPos > mDataList.size() - 2) {//最后一个位置的时候加载更多 todo 数据源中的元素的位置
          adapter.setHasFooter(false);
          isRefreshing = true;
          swipeRefresh.postDelayed(new Runnable() {
            @Override public void run() {
              //mSwipeRefreshLayout.setRefreshing(false);
              //mTvLoadMore.setVisibility(View.GONE);
              isRefreshing = false;
              //mAdapter.setHasFooter(false);
              //mRecyclerView.getAdapter().notifyDataSetChanged();
              //recyclerView.scrollToPosition(position);
            }
          }, 1000);//1秒
          //}  todo 暂时屏蔽 if
        }
      }
    });
    return view;
  }

  private Timer timer;// 首页焦点图自动滑动timer
  private TimerTask timerTask;// 首页焦点图自动滑动TimerTask
  public static final int MSG_ACTION_SLIDE_PAGE = 999;
  private class AutoScrollTimerTask extends TimerTask {
    @Override public void run() {
      adapter.handler.sendEmptyMessage(MSG_ACTION_SLIDE_PAGE);
    }
  }
  @Override public void onResume() {
    if (timer == null) {// 自动滑动
      timer = new Timer();
      timerTask = new AutoScrollTimerTask();
      timer.schedule(timerTask, 5000, 5000);
    }
    super.onResume();
  }
  private void findViewById(View view) {
    //btHttp = (Button) view.findViewById(R.id.http);
    //btHttp.setOnClickListener(new View.OnClickListener() {
    //	@Override
    //	public void onClick(View v) {
    //		Intent intent = new Intent(mContext, OkHttpsTest.class);
    //		startActivity(intent);
    //	}
    //});
    recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
    swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh);
  }
}
