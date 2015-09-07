package com.miqian.mq.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.miqian.mq.R;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.views.Dialog_Login;

/**
		* Description:��ҳ
				*
		* @author Jackie
				* @created 2015-3-18 ����5:05:49
				*/

		public class FragmentTab extends Fragment {

			private View view;

			@Override
			public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
				if (savedInstanceState == null || view == null) {
					view = inflater.inflate(R.layout.frame_user, null);
				}
				ViewGroup parent = (ViewGroup) view.getParent();
				if (parent != null) {
					parent.removeView(view);
				}

		return view;
	}

}
