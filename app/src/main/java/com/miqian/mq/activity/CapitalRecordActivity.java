package com.miqian.mq.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import com.miqian.mq.R;

/**
 * 所有的搜索结果都是在当前界面刷新
 */
public class CapitalRecordActivity extends Activity {
Animation animHide,animShow;
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_capital_record);
    animShow = AnimationUtils.loadAnimation(this, R.anim.view_show);
    animHide = AnimationUtils.loadAnimation( this, R.anim.view_hide);
    final LinearLayout filetr_container = (LinearLayout) findViewById(R.id.filter_container);
    Button test = (Button) findViewById(R.id.test_container_anim);
    test.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if(filetr_container.getVisibility() == View.GONE){
          filetr_container.setVisibility(View.VISIBLE);
          //filetr_container.animate().translationY(filetr_container.getHeight());
          filetr_container.startAnimation( animShow );
        }else{
          filetr_container.startAnimation( animHide );
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
}
