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
	public interface OperationKey {

		/** 登录成功 */
		int LOGIN_SUCCESS = 1001;
		/**
		 * 退出主页面
		 */
		int EXIT_MAIN = 2001;


		/** 修改昵称 */
		int MODIDYNICKNAME = 4001;

		/** 修改绑定手机 */
		int MODIFYPHONE = 5001;

		/** 返回首页 */
		int BACK_HOME = 1001;
		/** 返回秒钱宝首页 */
		int BACK_CURRENT = 1002;
		/** 返回定期首页 */
		int BACK_REGULAR = 1003;
		/** 返回我的 */
		int BACK_USER = 1004;
		/** 返回原始Tab */
		int BACK_MAIN = 1005;

		/** token改变 */
		int CHANGE_TOKEN = 1006;
		/** 极光推送更新ui */
		int RERESH_JPUSH = 1007;
		/** 首页弹框 */
		int ShowTips = 1008;
		/** 切换tab*/
		int ChangeTab = 1009;
		/** 更新消息*/
		int RefeshMessage = 1010;
		/** 消息状态*/
		int MessageState = 1011;
		/**刷新秒钱宝数据*/
		int REFRESH_CURRENTINFO = 1013;
		/**系统维护*/
		int SYSTEM_MAINTENANCE = 1014;
		/**汇付用户升级*/
		int HF_UPDATE = 1015;
	}

	/**
	 * 额外操作监听器
	 * 
	 * 
	 */
	public interface ExtendOperationListener {
		void excuteExtendOperation(int operationKey, Object data);
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
