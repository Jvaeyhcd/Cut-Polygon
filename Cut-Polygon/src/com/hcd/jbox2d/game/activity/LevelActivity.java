package com.hcd.jbox2d.game.activity;

import java.util.ArrayList;

import com.hcd.jbox2d.game.db.LevelDataManager;
import com.hcd.jbox2d.game.obj.ExitApplication;
import com.hcd.jbox2d.game.obj.Stage;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class LevelActivity extends Activity {

	private Button lev1Button, lev2Button, lev3Button, lev4Button, lev5Button;
	public static LevelDataManager lvManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_level);
		
		lvManager = new LevelDataManager(this);
		
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);// 全屏
		
		lev1Button = (Button) findViewById(R.id.Lev1Button);
		lev2Button = (Button) findViewById(R.id.Lev2Button);
		lev3Button = (Button) findViewById(R.id.Lev3Button);
		lev4Button = (Button) findViewById(R.id.Lev4Button);
		lev5Button = (Button) findViewById(R.id.Lev5Button);
		
		ArrayList<Stage> stages = lvManager.getAllStages();
		if (stages.size() >= 1) {
			lev1Button.setText(lev1Button.getText()+""+stages.get(0).getScore());
			lev1Button.setEnabled(true);
		}
		if (stages.size() >= 2) {
			lev2Button.setText(lev2Button.getText()+""+stages.get(1).getScore());
			lev2Button.setEnabled(true);
		}
		if (stages.size() >= 3) {
			lev3Button.setText(lev3Button.getText()+""+stages.get(2).getScore());
			lev3Button.setEnabled(true);
		}
		if (stages.size() >= 4) {
			lev4Button.setText(lev4Button.getText()+""+stages.get(3).getScore());
			lev4Button.setEnabled(true);
		}
		if (stages.size() >= 5) {
			lev5Button.setText(lev5Button.getText()+""+stages.get(4).getScore());
			lev5Button.setEnabled(true);
		}
//		lev1Button.setText(lev1Button.getText()+""+stages.get(0).getScore());
//		lev2Button.setText(lev2Button.getText()+""+stages.get(1).getScore());
//		lev3Button.setText(lev3Button.getText()+""+stages.get(2).getScore());
//		lev4Button.setText(lev4Button.getText()+""+stages.get(3).getScore());
//		lev5Button.setText(lev5Button.getText()+""+stages.get(4).getScore());
		
		lev1Button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				// 设置Intent的源地址和目标地址
				intent.setClass(getApplicationContext(), Stage1Activity.class);
				// 调用startActivity方法发送意图给系统
				startActivity(intent);
				//关闭当前activity，添加了该语句后，用户通过点击返回键是无法返回该activity的
				LevelActivity.this.finish();
			}
		});
		lev2Button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				// 设置Intent的源地址和目标地址
				intent.setClass(getApplicationContext(), Stage2Activity.class);
				// 调用startActivity方法发送意图给系统
				startActivity(intent);
				//关闭当前activity，添加了该语句后，用户通过点击返回键是无法返回该activity的
				LevelActivity.this.finish();
			}
		});
		lev3Button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				// 设置Intent的源地址和目标地址
				intent.setClass(getApplicationContext(), Stage3Activity.class);
				// 调用startActivity方法发送意图给系统
				startActivity(intent);
				//关闭当前activity，添加了该语句后，用户通过点击返回键是无法返回该activity的
				LevelActivity.this.finish();
			}
		});
		lev4Button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				// 设置Intent的源地址和目标地址
				intent.setClass(getApplicationContext(), Stage4Activity.class);
				// 调用startActivity方法发送意图给系统
				startActivity(intent);
				//关闭当前activity，添加了该语句后，用户通过点击返回键是无法返回该activity的
				LevelActivity.this.finish();
			}
		});
		lev5Button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				// 设置Intent的源地址和目标地址
				intent.setClass(getApplicationContext(), Stage5Activity.class);
				// 调用startActivity方法发送意图给系统
				startActivity(intent);
				//关闭当前activity，添加了该语句后，用户通过点击返回键是无法返回该activity的
				LevelActivity.this.finish();
			}
		});
		ExitApplication.getInstance().addActivity(this);
	}
	
	@Override
	protected void onResume() {

		/**
		 * 强制变成横屏，不能变成竖屏
		 */
		if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
		super.onResume();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.level, menu);
		return true;
	}

}
