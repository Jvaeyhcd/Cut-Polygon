package com.hcd.jbox2d.game.obj;

import java.util.LinkedList;

import android.app.Activity;
import android.app.Application;

public class ExitApplication extends Application {

	private LinkedList<Activity> activityList  = new LinkedList<Activity>();
	private static ExitApplication instance;
	
	public ExitApplication() {
		// TODO Auto-generated constructor stub
	}
	
	//单例模式获取唯一的ExitApplication实例
	public static ExitApplication getInstance() {
		if (instance == null) {
			instance = new ExitApplication();
		}
		return instance;
	}
	//添加Activity到容器
	public void addActivity(Activity activity) {
		activityList.add(activity);
	}
	//遍历所有Activity并finish
	public void exit() {
		for (Activity activity:activityList) {
			activity.finish();
		}
		System.exit(0);
	}
}
