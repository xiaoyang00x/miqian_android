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


		/** 修改昵称 */
		public static final int MODIDYNICKNAME = 4001;

		/** 修改绑定手机 */
		public static final int MODIFYPHONE = 5001;

		/** 返回首页 */
		public static final int BACK_HOME = 1001;
		/** 返回活期首页 */
		public static final int BACK_CURRENT = 1002;
		/** 返回定期首页 */
		public static final int BACK_REGULAR = 1003;
		/** 返回我的 */
		public static final int BACK_USER = 1004;
		/** 实名认证 */
		public static final int REAL_NAME = 1005;

		/** token改变 */
		public static final int CHANGE_TOKEN = 1006;
		/** 极光推送更新ui */
		public static final int RERESH_JPUSH = 1007;
		/** 首页弹框 */
		public static final int ShowTips = 1008;
		/** 切换tab*/
		public static final int ChangeTab = 1009;
		/** 更新消息*/
		public static final int RefeshMessage = 1010;
		/** 消息状态*/
		public static final int MessageState = 1011;
		/** 设置交易密码成功*/
		public static final int SETTRADPASSWORD_SUCCESS = 1012;
		/**刷新活期数据*/
		public static final int REFRESH_CURRENTINFO = 1013;
		/**系统维护*/
		public static final int SYSTEM_MAINTENANCE = 1014;
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
