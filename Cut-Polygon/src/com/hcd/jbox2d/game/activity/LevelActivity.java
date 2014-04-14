package com.hcd.jbox2d.game.activity;

import com.hcd.jbox2d.game.obj.ExitApplication;

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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_level);
		
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);// 全屏
		
		lev1Button = (Button) findViewById(R.id.Lev1Button);
		lev2Button = (Button) findViewById(R.id.Lev2Button);
		lev3Button = (Button) findViewById(R.id.Lev3Button);
		lev4Button = (Button) findViewById(R.id.Lev4Button);
		lev5Button = (Button) findViewById(R.id.Lev5Button);
		
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
