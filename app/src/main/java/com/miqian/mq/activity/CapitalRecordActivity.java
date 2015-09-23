package com.miqian.mq.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import com.miqian.mq.R;
import com.miqian.mq.views.CircleButton;

/**
 * 所有的搜索结果都是在当前界面刷新
 */
public class CapitalRecordActivity extends Activity {
  Animation animHide, animShow;

  CircleButton all, saving, withdraw, buy, redeem, transfer, maturity, other, preSelected;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_capital_record);
    animShow = AnimationUtils.loadAnimation(this, R.anim.view_show);
    animHide = AnimationUtils.loadAnimation(this, R.anim.view_hide);
    final LinearLayout filetr_container = (LinearLayout) findViewById(R.id.filter_container);
    Button test = (Button) findViewById(R.id.test_container_anim);
    test.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (filetr_container.getVisibility() == View.GONE) {
          filetr_container.setVisibility(View.VISIBLE);
          //filetr_container.animate().translationY(filetr_container.getHeight());
          filetr_container.startAnimation(animShow);
        } else {
          filetr_container.startAnimation(animHide);
          filetr_container.setVisibility(View.GONE);
        }
      }
    });
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_capital_record, menu);
    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  private void initView(){


    all = (CircleButton)findViewById(R.id.all);
    saving = (CircleButton)findViewById(R.id.saving);
    withdraw = (CircleButton)findViewById(R.id.withdraw);
    buy = (CircleButton)findViewById(R.id.buy);
    redeem = (CircleButton)findViewById(R.id.redeem);
    transfer = (CircleButton)findViewById(R.id.transfer);
    maturity = (CircleButton)findViewById(R.id.maturity);
    other = (CircleButton)findViewById(R.id.other);

    preSelected = all;
    preSelected.setColor(Color.RED);
  }

  public void searchBtn(View v){
    ((CircleButton) v).setColor(Color.RED);
    preSelected = (CircleButton) v;
    preSelected.setColor(Color.BLUE);
    switch (v.getId()){

      case R.id.all:

        break;

      case R.id.saving:
        break;

      case R.id.buy:
        break;

      case R.id.withdraw:
        break;

      case R.id.redeem:
        break;

      case R.id.transfer:
        break;

      case R.id.maturity:
        break;

      case R.id.other:
        break;
    }

    //todo 到网络获取数据。因为该界面的信息是分页的。


  }

  /**
   * todo 参数要传入类型及分页信息
   */
  private void getData(){

  }
}
