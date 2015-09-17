package com.miqian.mq.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.miqian.mq.R;

/**
 * Created by guolei_wang on 15/9/16.
 */
public class RegularFragment extends BasicFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_regular, container, false);
        return view;
    }
}
