package com.miqian.mq.adapter;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Toast;

public class ViewPagerAdapter extends PagerAdapter {

    Activity activity;
    public int imageArray[];

    public ViewPagerAdapter(Activity act, int[] imgArra) {
        imageArray = imgArra;
        activity = act;
    }

    @Override
    public int getCount() {
        // Random r = new Random();
        // return r.nextInt(imageArray.length);

        return imageArray.length;
    }

    @Override
    public Object instantiateItem(View collection, int position) {
        ImageView view = new ImageView(activity);
        view.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
                LayoutParams.FILL_PARENT));
        view.setScaleType(ScaleType.FIT_XY);
        view.setImageBitmap(
            BitmapFactory.decodeResource(activity.getResources(), imageArray[position]));
        // ///////////////
        ((ViewPager) collection).addView(view, 0);
        final int pos = position;
        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                activity.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        Toast.makeText(activity, "the pos is " + pos, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        return view;
    }

    @Override
    public void destroyItem(View arg0, int arg1, Object arg2) {
        ((ViewPager) arg0).removeView((View) arg2);
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == ((View) arg1);
    }

    @Override
    public Parcelable saveState() {
        return null;
    }
}
