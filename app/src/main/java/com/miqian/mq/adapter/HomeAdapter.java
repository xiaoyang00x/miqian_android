package com.miqian.mq.adapter;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.miqian.mq.R;
import com.miqian.mq.test.ActivityEntity;
import com.miqian.mq.test.HomeAdPageAdapter;
import com.miqian.mq.test.MaterialProgressBarSupport;
import com.miqian.mq.test.ViewPagerAdapter;
import com.miqian.mq.views.CircleBar;
import com.miqian.mq.views.PageIndicator;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunyong on 9/5/15.
 *
 */
public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private int a[] = { R.drawable.ic_launcher, R.drawable.ic_launcher, R.drawable.ic_launcher };

  ArrayList<String[]> list = new ArrayList<String[]>();
  // private int imageArra1[] = { R.drawable.antartica5,
  // R.drawable.antartica6,
  // R.drawable.antartica7, R.drawable.antartica8 };
  //
  // private int imageArra2[] = { R.drawable.antartica7, R.drawable.antartica8
  // };
  //
  // private int imageArra3[] = { R.drawable.antartica6,
  // R.drawable.antartica7,
  // R.drawable.antartica8 };

  int lenIndicator = 0;// the size of images getting from server

  private ImageView imageView;

  private ImageView[] imageViews;

  private ViewGroup group;

  ViewPager myPager;
  ViewPagerAdapter adapter = null;
  View root = null;
  // Range range;
  /////////////////////////////////////////////

  final int DEMAND_DEPOSIT = 0;
  final int REGULAR_DEPOSIT = 1;
  final int ADS = 2;
  final int TYPE_FOOTER = 3;
  //List<String> list;
  int count;
  Activity activity;

  public HomeAdapter(Activity activity, int count) {
    this.count = count;
    this.activity = activity;
  }

  @Override public int getItemViewType(int position) {
    if (position == 0) {
      return ADS;//轮播
    } else if (position == 1) {
      return DEMAND_DEPOSIT;//活期
      //}else if (position == getBasicItemCount() && hasFooter) {
    } else if (position == 4) {
      return TYPE_FOOTER;
    } else {
      return REGULAR_DEPOSIT;//定期
    }


  }

  @Override public int getItemCount() {
    return count + (hasFooter ? 2 : 1);// // TODO: 9/6/15  2：代表footer与广告   1:只有广告
    //return mValues.size() + (hasFooter ? 1 : 0);
    //return count + 1;//add the advertisements
  }

  @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    LayoutInflater mInflater = LayoutInflater.from(parent.getContext());

    switch (viewType) {
      case ADS:
        ViewGroup vImage =
            (ViewGroup) mInflater.inflate(R.layout.fragment_home_recyclerview_item_advertisement,
                parent, false);
        ViewHolder0 vhImage = new ViewHolder0(vImage);
        return vhImage;
      case DEMAND_DEPOSIT:
        View view =
            mInflater.inflate(R.layout.fragment_home_recyclerview_item_demand_deposit, parent,
                false);
        return new ViewHolder2(view);
      case TYPE_FOOTER: //// TODO: 9/6/15  should refine this load more function
        //View footer = LayoutInflater.from(parent.getContext())
        //    .inflate(R.layout.item_view_load_more, parent, false);
        //return new FooterViewHolder(footer);

      default:
        View v = mInflater.inflate(R.layout.fragment_home_recyclerview_item_regular_deposit, parent,
            false);
        final CircleBar circleBar = (CircleBar) v.findViewById(R.id.circlebar);
        circleBar.setSweepAngle(270);//todo keen control the angle
        //circleBar.setOnClickListener(new View.OnClickListener() {
        //  @Override public void onClick(View view) {
        //    circleBar.startCustomAnimation();
        //  }
        //});
        new Handler().postDelayed(new Runnable() {
          @Override public void run() {
            circleBar.setText("270");
          }
        }, 2000);
        return new ViewHolder2(v);
    }
  }

  @Override public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
    //没有更多数据
    if (hasMoreData) {
      ((FooterViewHolder) viewHolder).mProgressView.setVisibility(View.VISIBLE);
      ((FooterViewHolder) viewHolder).mProgressView.startProgress();
      //((FooterViewHolder) holder).mProgressView.setIndeterminate(true);
      ((FooterViewHolder) viewHolder).mTextView.setText(R.string.app_loading_more);
    } else {
      //((FooterViewHolder) viewHolder).mProgressView.stopProgress();
      //((FooterViewHolder) viewHolder).mProgressView.setVisibility(View.GONE);
      ////((FooterViewHolder) holder).mProgressView.st;
      //((FooterViewHolder) viewHolder).mTextView.setText("没有更多数据了");
    }
  }

  class ViewHolder0 extends RecyclerView.ViewHolder {

    public ViewHolder0(View itemView) {
      super(itemView);
      mHomeViewpager = (ViewPager) itemView.findViewById(R.id.vp_home_viewpager);
      //ViewPagerAdapter adapter = new ViewPagerAdapter(activity, a);
      //vp.setAdapter(adapter);
      //vp.setCurrentItem(0);

      //todo 这里设置的数据需要进行
      initAdapter(itemView);

    }
  }

  class ViewHolder2 extends RecyclerView.ViewHolder {
    public ViewHolder2(View itemView) {
      super(itemView);
    }
  }

  //／／／／／／／／／
  private boolean hasFooter;

  private boolean hasMoreData;

  public boolean hasMoreData() {
    return hasMoreData;
  }

  public void setHassMoreData(boolean isMoreData) {
    if (this.hasMoreData != isMoreData) {
      this.hasMoreData = isMoreData;
      notifyDataSetChanged();
    }
  }

  public void setHasMoreDataAndFooter(boolean hasMoreData, boolean hasFooter) {
    if (this.hasMoreData != hasMoreData || this.hasFooter != hasFooter) {
      this.hasMoreData = hasMoreData;
      this.hasFooter = hasFooter;
      notifyDataSetChanged();
    }
  }

  public boolean hasFooter() {
    return hasFooter;
  }

  public void setHasFooter(boolean hasFooter) {
    if (this.hasFooter != hasFooter) {
      this.hasFooter = hasFooter;
      notifyDataSetChanged();
    }
  }

  private List<String> mValues;//// TODO: 9/6/15 数据源

  public int getBasicItemCount() {
    return 1;
    //return mValues.size();
  }

  public static class FooterViewHolder extends RecyclerView.ViewHolder {
    public final MaterialProgressBarSupport mProgressView;
    public final TextView mTextView;

    public FooterViewHolder(View view) {
      super(view);
      mProgressView = (MaterialProgressBarSupport) view.findViewById(R.id.progress_view);
      mTextView = (TextView) view.findViewById(R.id.tv_content);
    }

    @Override public String toString() {
      return super.toString() + " '" + mTextView.getText();
    }
  }

  //// TODO: 9/6/15  viewpager indicator \\
  private HomeAdPageAdapter mAdpagerAdapter;// 首页焦点图adapter
  private int currentPageState = ViewPager.SCROLL_STATE_IDLE;// Viewpager状态
  public static final int MSG_ACTION_SLIDE_PAGE = 999;
  private LinearLayout mHomeRefreshableLayout;// 舒心区域总布局
  private ViewPager mHomeViewpager;// 首页焦点图Viewpager
  private PageIndicator mHomeViewpagerIndictor;// 首页焦点图滑动标记
  private ImageView mHomeFirstHead;// 首发头条

  /**
   * 首页焦点图滑动监听
   */
  class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

    @Override public void onPageScrollStateChanged(int state) {
      currentPageState = state;
    }

    @Override public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    @Override public void onPageSelected(int index) {

      mHomeViewpagerIndictor.setCurrentPage(index % mAdpagerAdapter.getItemCount());
    }
  }

  public Handler handler = new Handler() {
    public void handleMessage(Message msg) {
      switch (msg.what) {
        case MSG_ACTION_SLIDE_PAGE:
          if (currentPageState == ViewPager.SCROLL_STATE_IDLE) {
            //Fixed by keen: NullPointerException
            if (null != mHomeViewpager) {
              if (mHomeViewpager.getAdapter() != null) {
                int current = mHomeViewpager.getCurrentItem();
                // 获取当前的索引加一，以滑动到下一项
                mHomeViewpager.setCurrentItem((current + 1), true);
              }
            }
          }
          break;
        default:
          break;
      }
    }
  };

  public void initPagerIndicator(View view) {
    mHomeViewpagerIndictor = (PageIndicator) view.findViewById(R.id.pi_home_page_indictor);
    mHomeViewpagerIndictor.setPageOrginal(true);
    mHomeViewpagerIndictor.setTotalPageSize(listEntity.size());
  }

  ArrayList<ActivityEntity> listEntity;

  /**
   * keen
   * 初始化adapter
   */
  public void initAdapter(View view) {
    listEntity = new ArrayList<>();
    ActivityEntity entity = new ActivityEntity();
    entity.activityImgUrl =
        "http://b.hiphotos.baidu.com/image/pic/item/8718367adab44aedbf4ff0f3b01c8701a18bfba0.jpg";
    listEntity.add(entity);
    entity = new ActivityEntity();
    entity.activityImgUrl =
        "http://b.hiphotos.baidu.com/image/pic/item/8718367adab44aedbf4ff0f3b01c8701a18bfba0.jpg";
    listEntity.add(entity);
    entity = new ActivityEntity();
    entity.activityImgUrl =
        "http://b.hiphotos.baidu.com/image/pic/item/8718367adab44aedbf4ff0f3b01c8701a18bfba0.jpg";
    listEntity.add(entity);
    entity = new ActivityEntity();
    entity.activityImgUrl =
        "http://b.hiphotos.baidu.com/image/pic/item/8718367adab44aedbf4ff0f3b01c8701a18bfba0.jpg";
    listEntity.add(entity);

    entity = new ActivityEntity();
    entity.activityImgUrl =
        "http://d.hiphotos.baidu.com/image/pic/item/4a36acaf2edda3cce714562903e93901203f92c3.jpg";
    listEntity.add(entity);

    mAdpagerAdapter = new HomeAdPageAdapter(view.getContext(), listEntity);
    mHomeViewpager = (ViewPager) view.findViewById(R.id.vp_home_viewpager);
    mHomeViewpager.setAdapter(mAdpagerAdapter);
    initPagerIndicator(view);//pagerindicator  todo
    if (listEntity.size() != 0) {
      int maxSize = 65535;
      int pos = (maxSize / 2) - (maxSize / 2) % listEntity.size(); // 计算初始位置
      mHomeViewpager.setOnPageChangeListener(new MyOnPageChangeListener());
      mHomeViewpager.setCurrentItem(pos, true);
      mAdpagerAdapter.setOnPageItemClickListener(new HomeAdPageAdapter.OnPageItemClickListener() {
        @Override public void onPageItemClick(ViewGroup parent, View item, int position) {

          //do nothing
        }
      });
    }
  }
}