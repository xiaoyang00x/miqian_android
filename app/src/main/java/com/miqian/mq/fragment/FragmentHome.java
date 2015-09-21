package com.miqian.mq.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.alibaba.fastjson.JSON;
import com.miqian.mq.R;
import com.miqian.mq.adapter.HomeAdapter;
import com.miqian.mq.entity.HomePageInfo;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class FragmentHome extends Fragment {

  private View view;
  //private Button btHttp;
  private Activity mContext;
  private RecyclerView recyclerView;
  private SwipeRefreshLayout swipeRefresh;
  HomeAdapter adapter;
  public static String baseURL = "http://192.168.0.107:9000";//home
  int someVarA;
  String someVarB;
  @Override public void onActivityCreated( Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    myUpdateOperation();
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {

    baseURL = "http://10.0.1.193:9000";//拾财贷
    //baseURL = "27.154.228.194:30001/commonService/getHome";//拾财贷公网
    mContext = getActivity();
    if (savedInstanceState == null || view == null) {
      view = inflater.inflate(R.layout.frame_home, null);
      adapter = null;
    }
    ViewGroup parent = (ViewGroup) view.getParent();
    if (parent != null) {
      parent.removeView(view);
    }
    findViewById(view);

    final LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
    recyclerView.setLayoutManager(layoutManager);

    //adapter = new HomeAdapter(mContext,);
    //adapter.setHasFooter(false);
    //recyclerView.setAdapter(adapter);
    //recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
    //  boolean isPullUp = false;
    //  boolean isRefreshing = false;
    //
    //  @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
    //    super.onScrolled(recyclerView, dx, dy);
    //    isPullUp = dy > 0;
    //  }
    //
    //  @Override public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
    //    //if(newState == RecyclerView.SCROLL_STATE_IDLE){
    //    //  adapter.notifyItemChanged(1);
    //    //  adapter.notifyItemChanged(5);
    //    //
    //    //}
    //    //super.onScrollStateChanged(recyclerView, newState);
    //    //if (isPullUp && !isRefreshing && adapter.hasMoreData()) {
    //    //  int lastPos = layoutManager.findLastVisibleItemPosition();
    //    //  //if (lastPos > mDataList.size() - 2) {//最后一个位置的时候加载更多
    //    //  isRefreshing = true;
    //    //  swipeRefresh.postDelayed(new Runnable() {
    //    //    @Override public void run() {
    //    //      //mSwipeRefreshLayout.setRefreshing(false);
    //    //      //mTvLoadMore.setVisibility(View.GONE);
    //    //      isRefreshing = false;
    //    //      //mAdapter.setHasFooter(false);
    //    //      //mRecyclerView.getAdapter().notifyDataSetChanged();
    //    //      //recyclerView.scrollToPosition(position);
    //    //    }
    //    //  }, 1000);//1秒
    //    //  //}
    //    //}
    //  }
    //});
    return view;
  }

  private Timer timer;// 首页焦点图自动滑动timer
  private TimerTask timerTask;// 首页焦点图自动滑动TimerTask
  public static final int MSG_ACTION_SLIDE_PAGE = 999;

  private class AutoScrollTimerTask extends TimerTask {
    @Override public void run() {
      if (null != adapter) adapter.handler.sendEmptyMessage(MSG_ACTION_SLIDE_PAGE);
    }
  }

  @Override public void onResume() {
    super.onResume();
    if (timer == null) {// 自动滑动
      timer = new Timer();
      timerTask = new AutoScrollTimerTask();
      timer.schedule(timerTask, 5000, 5000);
    }
  }

  @Override public void onDestroy() {
    super.onDestroy();
    adapter = null;
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

      /*
   * Sets up a SwipeRefreshLayout.OnRefreshListener that is invoked when the user
   * performs a swipe-to-refresh gesture.
   */
    swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override public void onRefresh() {
        Log.i("keen", "onRefresh called from SwipeRefreshLayout");

        // This method performs the actual data-refresh operation.
        // The method calls setRefreshing(false) when it's finished.
        myUpdateOperation();
      }
    });
  }

  private void myUpdateOperation() {

    new Thread(new Runnable() {
      @Override public void run() {
        try {
          //network task
          OkHttpClient client = new OkHttpClient();
          //Request request =
          //    CommonHeaders.configRequestBuilder(new Request.Builder()).url(baseURL + "/jsonRes").build();
          RequestBody requestBody = new MultipartBuilder().type(MultipartBuilder.FORM)
              .addPart(Headers.of("Content-Disposition", "form-data; name=\"title\""),
                  RequestBody.create(null, "Sqo===张三"))
              .build();

          Request request =
              new Request.Builder().url(baseURL + "/jsonRes").post(requestBody).build();
          client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Request request, IOException e) {
              e.printStackTrace();
            }

            @Override public void onResponse(final Response response) throws IOException {
              String jsonSTR = response.body().string();
              final HomePageInfo info = JSON.parseObject(jsonSTR, HomePageInfo.class);

              if (null != mContext) {
                mContext.runOnUiThread(new Runnable() {
                  @Override public void run() {
                    if (null == adapter) {
                      adapter = new HomeAdapter(mContext, info);
                      recyclerView.setAdapter(adapter);
                    } else {
                      adapter.notifyDataSetChanged(info);
                    }
                    swipeRefresh.setRefreshing(false);
                  }
                });
              }
            }
          });
        } catch (Exception ex) {
          ex.printStackTrace();
        }
      }
    }).start();
  }

  /**
   * todo 这个方法切换到后台是调用的。切换到其他的fragment时，没有调用。
   * @param outState
   */
  @Override
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putInt("A", 7);
    outState.putString("B", "hellos");
  }



}