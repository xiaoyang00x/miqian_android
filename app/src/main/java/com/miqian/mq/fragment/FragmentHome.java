package com.miqian.mq.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.miqian.mq.R;
import com.miqian.mq.test.OkHttpsTest;

public class FragmentHome extends Fragment {

	private View view;
	private Button btHttp;
	private Activity mContext;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mContext = getActivity();
		if (savedInstanceState == null || view == null) {
			view = inflater.inflate(R.layout.frame_home, null);
		}
		ViewGroup parent = (ViewGroup) view.getParent();
		if (parent != null) {
			parent.removeView(view);
		}
		findViewById(view);
		return view;
	}

	private void findViewById(View view) {
		btHttp = (Button) view.findViewById(R.id.http);
		btHttp.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, OkHttpsTest.class);
				startActivity(intent);
			}
		});
	}

}
