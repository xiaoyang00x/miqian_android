package com.miqian.mq.adapter;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.miqian.mq.R;
import com.miqian.mq.entity.HomePageInfo;
import com.miqian.mq.views.CircleBar;
import com.miqian.mq.views.MaterialProgressBarSupport;
import com.miqian.mq.views.PageIndicator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by sunyong on 9/5/15.
 */
public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
  HomePageInfo info;//the underlying source data
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

  public HomeAdapter(Activity activity, HomePageInfo info) {
    this.activity = activity;
    this.info = info;
  }

  @Override public int getItemViewType(int position) {
    if (position == 0) {
      return ADS;//轮播
    } else if (null != info && null != info.getNewCustomer() && position == 1) {
      return DEMAND_DEPOSIT;//活期
      //}else if (position == getBasicItemCount() && hasFooter) {
    } else if (hasFooter
        && position == getItemCount() - 1) {//has footer and the position is the last
      return TYPE_FOOTER;
    } else {
      return REGULAR_DEPOSIT;//定期
    }
  }

  /**
   * the count dynamically determined by the server's returned response
   * adImgs will be always be 1;
   * newCustomer will be either 1 or 0;
   * regularDeposit will be that depends.
   *
   *
   * info.getSubjectInfo().length   5
   */
  @Override public int getItemCount() {
    return 1 + (info.getNewCustomer() != null ? 1 : 0) + 5 + (hasFooter ? 1
        : 0);// // TODO: 9/6/15  1：代表footer的item
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
      //case DEMAND_DEPOSIT:
      //  View view =
      //      mInflater.inflate(R.layout.fragment_home_recyclerview_item_demand_deposit, parent,
      //          false);
      //  return new ViewHolder2(view);
      //case TYPE_FOOTER: //// TODO: 9/6/15  should refine this load more function
      //View footer = LayoutInflater.from(parent.getContext())
      //    .inflate(R.layout.item_view_load_more, parent, false);
      //return new FooterViewHolder(footer);

      default:
        View v = mInflater.inflate(R.layout.fragment_home_recyclerview_item_regular_deposit, parent,
            false);
        final CircleBar circleBar = (CircleBar) v.findViewById(R.id.circlebar);
        circleBar.setSweepAngle(0);//todo keen control the angle
        circleBar.setOnClickListener(new View.OnClickListener() {
          @Override public void onClick(View view) {
            circleBar.startCustomAnimation();
          }
        });
        //new Handler().postDelayed(new Runnable() {
        //  @Override public void run() {
        //    circleBar.setText("270");
        //  }
        //}, 500);
        return new ViewHolder2(v);
    }
  }

  @Override public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
    Log.e("keen", "the position is " + position);
    if (1 == position && null != info.getNewCustomer()) {//has object for new customer
      ((ViewHolder2) viewHolder).title.setText(info.getNewCustomer().getTitle());
      ((ViewHolder2) viewHolder).subtitle1.setText(info.getNewCustomer().getSubTitleOne());
      ((ViewHolder2) viewHolder).purchased_percent.setText(
          info.getNewCustomer().getPurchasePercent());
      ((ViewHolder2) viewHolder).purchasers.setText(info.getNewCustomer().getPurchasers() + "");
      ((ViewHolder2) viewHolder).limit.setText(info.getNewCustomer().getLimit() + "");
      ((ViewHolder2) viewHolder).annualized_return.setText(
          info.getNewCustomer().getAnnualizedReturn());
      ((ViewHolder2) viewHolder).section_header.setVisibility(View.VISIBLE);
      ((ViewHolder2) viewHolder).section_header_title_left.setText("new customer");
      ((ViewHolder2) viewHolder).bar.setMSweepAnglePer(300);
      //((ViewHolder2)viewHolder).bar.startCustomAnimation();
    } else if (position != 0) {
      int index = null == info.getNewCustomer() ? position - 1 : position - 2;
      ((ViewHolder2) viewHolder).title.setText((info.getSubjectInfo())[index].getTitle());
      ((ViewHolder2) viewHolder).subtitle1.setText((info.getSubjectInfo())[index].getSubTitleTwo());
      ((ViewHolder2) viewHolder).purchased_percent.setText(
          (info.getSubjectInfo())[index].getPurchasePercent());
      ((ViewHolder2) viewHolder).purchasers.setText(
          (info.getSubjectInfo())[index].getPurchasers() + "");
      ((ViewHolder2) viewHolder).limit.setText((info.getSubjectInfo())[index].getLimit() + "");
      ((ViewHolder2) viewHolder).annualized_return.setText(
          (info.getSubjectInfo())[index].getAnnualizedReturn());
      if (position != 5) ((ViewHolder2) viewHolder).bar.startCustomAnimation();
      ((ViewHolder2) viewHolder).bar.setMSweepAnglePer(300);
      if (0 == index) {
        ((ViewHolder2) viewHolder).section_header.setVisibility(View.VISIBLE);
      }
    }
  }

  class ViewHolder0 extends RecyclerView.ViewHolder {

    public ViewHolder0(View itemView) {
      super(itemView);
      mHomeViewpager = (ViewPager) itemView.findViewById(R.id.vp_home_viewpager);
      //todo 这里设置的数据需要进行
      initAdapter(itemView);
    }
  }

  //regular deposit view
  class ViewHolder2 extends RecyclerView.ViewHolder {
    TextView title, subtitle1, annualized_return, limit, purchased_percent, purchasers,
        section_header_title_left, section_header_title_right;
    RelativeLayout section_header;
    CircleBar bar;

    public ViewHolder2(View itemView) {
      super(itemView);
      title = (TextView) itemView.findViewById(R.id.title);
      subtitle1 = (TextView) itemView.findViewById(R.id.subtitle1);
      annualized_return = (TextView) itemView.findViewById(R.id.annualized_return);
      limit = (TextView) itemView.findViewById(R.id.limit);
      purchased_percent = (TextView) itemView.findViewById(R.id.purchased_percent);
      purchasers = (TextView) itemView.findViewById(R.id.purchasers);
      section_header_title_left = (TextView) itemView.findViewById(R.id.section_header_title_left);
      section_header_title_right =
          (TextView) itemView.findViewById(R.id.section_header_title_right);
      section_header = (RelativeLayout) itemView.findViewById(R.id.section_header);
      bar = (CircleBar) itemView.findViewById(R.id.circlebar);
    }
  }

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
    mHomeViewpagerIndictor.setTotalPageSize(info.getAdImgs().length);
  }

  /**
   * keen
   * 初始化adapter
   */
  public void initAdapter(View view) {
    mAdpagerAdapter = new HomeAdPageAdapter(view.getContext(), Arrays.asList(info.getAdImgs()));
    mHomeViewpager = (ViewPager) view.findViewById(R.id.vp_home_viewpager);
    mHomeViewpager.setAdapter(mAdpagerAdapter);
    initPagerIndicator(view);//pagerindicator  todo
    if (info.getAdImgs().length != 0) {
      int maxSize = 65535;
      int pos = (maxSize / 2) - (maxSize / 2) % info.getAdImgs().length; // 计算初始位置
      mHomeViewpager.setOnPageChangeListener(new MyOnPageChangeListener());
      mHomeViewpager.setCurrentItem(pos, true);
      mAdpagerAdapter.setOnPageItemClickListener(new HomeAdPageAdapter.OnPageItemClickListener() {
        @Override public void onPageItemClick(ViewGroup parent, View item, int position) {
          Toast.makeText(item.getContext(), "postion is " + position + ";;; tag is " + item.getTag(), Toast.LENGTH_SHORT).show();
          //todo open the webview

        }
      });
    }
  }

  public void notifyDataSetChanged(HomePageInfo info) {
    this.info = info;

    notifyDataSetChanged();
  }
}