package com.miqian.mq.activity.setting;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.activity.BaseActivity;
import com.miqian.mq.entity.CityInfo;
import com.miqian.mq.entity.CityInfoResult;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.views.WFYTitle;
import com.miqian.mq.views.sortlist.CharacterParser;
import com.miqian.mq.views.sortlist.ClearEditText;
import com.miqian.mq.views.sortlist.PinyinComparator;
import com.miqian.mq.views.sortlist.SideBar;
import com.miqian.mq.views.sortlist.SortAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CityListActivity extends BaseActivity {

  private ListView sortListView;
  private SideBar sideBar;
  private TextView dialog;
  private SortAdapter adapter;
  private ClearEditText mClearEditText;

  // private List<CityInfo> mList;
  /**
   * 汉字转换成拼音的类
   */
  private CharacterParser characterParser;
  private List<CityInfo> sourceDateList;

  /**
   * 根据拼音来排列ListView里面的数据类
   */
  private PinyinComparator pinyinComparator;

  private String bankId = "";
  private String bankName = "";

  private String branch;
  private String province;
  private String city;

  /**
   * 根据输入框中的值来过滤数据并更新ListView
   */
  private void filterData(String filterStr) {
    List<CityInfo> filterDateList = new ArrayList<CityInfo>();

    if (TextUtils.isEmpty(filterStr)) {
      if (sourceDateList != null) {
        filterDateList = sourceDateList;
      }
    } else {
      filterDateList.clear();
      if (sourceDateList == null || sourceDateList.size() == 0) {
        return;
      }
      for (CityInfo sortModel : sourceDateList) {
        String name = sortModel.getCity();
        if (name.indexOf(filterStr.toString()) != -1 || characterParser.getSelling(name)
            .startsWith(filterStr.toString())) {
          filterDateList.add(sortModel);
        }
      }
    }

    // 根据a-z进行排序
    Collections.sort(filterDateList, pinyinComparator);
    adapter.updateListView(filterDateList);
  }

  @Override public void obtainData() {
    mWaitingDialog.show();
    HttpRequest.getAllCity(CityListActivity.this, new ICallback<CityInfoResult>() {

      @Override public void onSucceed(CityInfoResult result) {
        mWaitingDialog.dismiss();
        sourceDateList = result.getData();
        initListView();
      }

      @Override public void onFail(String error) {
        mWaitingDialog.dismiss();
      }
    });
  }

  @Override public void initView() {
    // 实例化汉字转拼音类
    characterParser = CharacterParser.getInstance();

    pinyinComparator = new PinyinComparator();

    sideBar = (SideBar) findViewById(R.id.sidrbar);
    dialog = (TextView) findViewById(R.id.dialog);
    sideBar.setTextView(dialog);

    sortListView = (ListView) findViewById(R.id.country_lvcountry);
    sortListView.setOnItemClickListener(new OnItemClickListener() {

      @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // 这里要利用adapter.getItem(position)来获取当前position所对应的对象
        CityInfo cityInfo = (CityInfo) parent.getAdapter().getItem(position);
        //
        //				Intent intent = new Intent(CityListActivity.this, BankListAcivity.class);
        //				intent.putExtra("bankId", bankId);
        //				intent.putExtra("bankName", bankName);
        //				intent.putExtra("city", cityInfo.getCity());
        //				intent.putExtra("province", cityInfo.getProvince());
        //				startActivityForResult(intent, RolloutActivity.REQUEST_CODE);
        Intent data = new Intent();
        data.putExtra("city", cityInfo.getCity());
        data.putExtra("province", cityInfo.getProv());
        setResult(0, data);
        finish();
      }
    });

    mClearEditText = (ClearEditText) findViewById(R.id.filter_edit);

    // 根据输入框输入值的改变来过滤搜索
    mClearEditText.addTextChangedListener(new TextWatcher() {

      @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
        // 当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
        filterData(s.toString().toLowerCase());
      }

      @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override public void afterTextChanged(Editable s) {
      }
    });
  }

  @Override public int getLayoutId() {
    return R.layout.activity_city_list;
  }

  @Override public void initTitle(WFYTitle mTitle) {
    mTitle.setTitleText("选择城市");
  }

  private void initListView() {
    // 根据a-z进行排序源数据
    Collections.sort(sourceDateList, pinyinComparator);
    adapter = new SortAdapter(this, sourceDateList);
    sortListView.setAdapter(adapter);

    // 设置右侧触摸监听
    sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

      @Override public void onTouchingLetterChanged(String s) {
        // 该字母首次出现的位置
        int position = adapter.getPositionForSection(s.charAt(0));
        if (position != -1) {
          sortListView.setSelection(position);
        }
      }
    });
  }
}
