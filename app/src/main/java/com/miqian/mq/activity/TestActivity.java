package com.miqian.mq.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import com.miqian.mq.R;
import com.miqian.mq.views.CircleBar;
import java.util.ArrayList;
import java.util.List;

public class TestActivity extends Activity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_test);

    LinearLayoutManager llm = new LinearLayoutManager(this);
    RecyclerView rv = (RecyclerView)findViewById(R.id.recyclerView);
    rv.setLayoutManager(llm);
    MyAdapter ad = new MyAdapter();
    rv.setAdapter(ad);
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_test, menu);
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
class MyAdapter extends RecyclerView.Adapter<MyViewHolder>{
List<String > list = new ArrayList<>();
  public MyAdapter(){
    String a = "a";
    list.add(a);
    list.add(a);

    list.add(a);
    list.add(a);
    list.add(a);

    list.add(a);
    list.add(a);

    list.add(a);
    list.add(a);
    list.add(a);
  }

  @Override public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
   LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
    View v = mInflater.inflate(R.layout.circle_bar_test, parent, false);
    final CircleBar bar = (CircleBar)v.findViewById(R.id.circlebar_test);
    //bar.setText("120");
    bar.setSweepAngle(270);//todo keen control the angle

    new Handler().postDelayed(new Runnable() {
      @Override public void run() {
        bar.setText("270");
      }
    }, 500);
    return new MyViewHolder(v);
  }

  @Override public void onBindViewHolder(MyViewHolder holder, int position) {
    Log.e("keen", "" + position);
//holder.bar.setMSweepAnglePer(270);
    holder.bar.setText("890");
  }

  @Override public int getItemCount() {
    return 20;
  }
}
class MyViewHolder extends RecyclerView.ViewHolder {
  CircleBar bar;
  public MyViewHolder(View view){
    super(view);
    bar = (CircleBar)view.findViewById(R.id.circlebar_test);
  }
}