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
	
	//����ģʽ��ȡΨһ��ExitApplicationʵ��
	public static ExitApplication getInstance() {
		if (instance == null) {
			instance = new ExitApplication();
		}
		return instance;
	}
	//���Activity������
	public void addActivity(Activity activity) {
		activityList.add(activity);
	}
	//��������Activity��finish
	public void exit() {
		for (Activity activity:activityList) {
			activity.finish();
		}
		System.exit(0);
	}
}
