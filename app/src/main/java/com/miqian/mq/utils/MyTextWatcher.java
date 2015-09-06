package com.miqian.mq.utils;

import android.text.Editable;
import android.text.TextWatcher;

public abstract class MyTextWatcher implements TextWatcher {

	public abstract void myAfterTextChanged(Editable arg0);

	@Override
	public void afterTextChanged(Editable arg0) {
		myAfterTextChanged(arg0);
	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

	}

	@Override
	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

	}

}
