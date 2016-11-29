package com.miqian.mq.utils;

import java.util.Stack;

import android.app.Activity;

public class ActivityStack {
	private static Stack<Activity> activityStack;
	private static ActivityStack instance;

	private ActivityStack() {
	}

	public static ActivityStack getActivityStack() {
		if (instance == null) {
			instance = new ActivityStack();
		}
		return instance;
	}

	public void popActivity() {
		Activity activity = activityStack.lastElement();
		if (activity != null) {
			activity.finish();
			activityStack.remove(activity);
			activity = null;
		}
	}

	public void clearActivity() {
		if (activityStack == null) {
			return;
		}
		for (int i = activityStack.size() - 1; i >= 0; i--) {
			Activity actvitiy = activityStack.get(i);
			popActivity(actvitiy);
		}
	}

	public void popActivity(Activity activity) {
		if (activity != null) {
			activity.finish();
			activityStack.remove(activity);
			activity = null;
		}
	}

	public Activity currentActivity() {
		Activity activity = activityStack.lastElement();
		return activity;
	}

	public void pushActivity(Activity activity) {
		if (activityStack == null) {
			activityStack = new Stack<>();
		}
		activityStack.add(activity);
	}

	public void popAllActivityExceptOne(Class<?> cls) {
		while (true) {
			Activity activity = currentActivity();
			if (activity == null) {
				break;
			}
			if (activity.getClass().equals(cls)) {
				break;
			}
			popActivity(activity);
		}
	}

	public void finishActivity(int times) {
		int size = activityStack.size();
		int time = times;
		while (time > 0) {
			Activity activity = activityStack.get(size - times);
			if (activity != null) {
				activity.finish();
				activityStack.remove(activity);
				activity = null;
			}
			time--;
		}

	}
}