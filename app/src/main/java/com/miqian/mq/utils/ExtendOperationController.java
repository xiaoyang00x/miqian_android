package com.miqian.mq.utils;

import java.util.LinkedList;
import java.util.List;

/**
 * 额外操作监听器 ~
 * 
 * 
 */
public class ExtendOperationController {

	/**
	 * 操作KEY
	 */
	public static interface OperationKey {

		/** 登录成功 */
		public static final int LOGIN_SUCCESS = 1001;
		/**
		 * 退出主页面
		 */
		public static final int EXIT_MAIN = 2001;

		/** 登录成功 */
		public static final int RERESH_XGPUSH = 3001;

		/** 修改昵称 */
		public static final int MODIDYNICKNAME = 4001;

		/** 修改绑定手机 */
		public static final int MODIFYPHONE = 5001;

		/** 返回首页 */
		public static final int BACK_HOME = 1001;
		/** 返回我的 */
		public static final int BACK_USER = 1004;
		/** 实名认证 */
		public static final int REAL_NAME = 1005;

	}

	/**
	 * 额外操作监听器
	 * 
	 * 
	 */
	public static interface ExtendOperationListener {
		public void excuteExtendOperation(int operationKey, Object data);
	}

	List<ExtendOperationListener> mListeners;
	static ExtendOperationController mControll;

	public ExtendOperationController() {
		mListeners = new LinkedList<ExtendOperationListener>();
	}

	public static ExtendOperationController getInstance() {
		if (mControll == null) {
			mControll = new ExtendOperationController();
		}
		return mControll;
	}

	public void registerExtendOperationListener(ExtendOperationListener listener) {

		if (!mListeners.contains(listener)) {
			mListeners.add(listener);
		}

	}

	public void unRegisterExtendOperationListener(ExtendOperationListener listener) {
		if (mListeners.contains(listener)) {
			mListeners.remove(listener);
		}

	}

	/**
	 * 通知执行操作
	 * 
	 * @param
	 */
	public void doNotificationExtendOperation(int operationKey, Object object) {
		if (mListeners == null)
			return;

		for (int i = 0; i < mListeners.size(); i++) {
			ExtendOperationListener listener = mListeners.get(i);
			try {
				if (listener != null) {
					listener.excuteExtendOperation(operationKey, object);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

}
