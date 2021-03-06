package com.hcd.jbox2d.game.activity;

import com.hcd.jbox2d.game.obj.ExitApplication;
import com.hcd.jbox2d.game.utils.SoundFactory;
import com.hcd.jbox2d.game.view.CustomDialog;
import com.hcd.jbox2d.game.view.Stage5View;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class Stage5Activity extends Activity {

	private Button optionsButton, homeButton, nextButton, retryButton;
	public static int  screenWidth, screenHeight;
	public Stage5View stage5View;
	public Handler mHandler;
	public boolean didShow;
	public SoundFactory soundFactory;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE); // 去title
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);// 全屏
		
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		screenWidth = metric.widthPixels;
		screenHeight = metric.heightPixels;
		
		setContentView(R.layout.activity_stage5);
		optionsButton = (Button) findViewById(R.id.optionsstage5);
		homeButton = (Button)findViewById(R.id.homestage5);
		nextButton = (Button)findViewById(R.id.nextstage5);
		retryButton = (Button)findViewById(R.id.retrystage5);
		
		soundFactory = new SoundFactory(this);
		didShow = false;
		stage5View = (Stage5View) findViewById(R.id.stage5View);
		mHandler = new Handler();
		mHandler.post(update);
		
		ExitApplication.getInstance().addActivity(this);
	}
	
	private Runnable update = new Runnable() {

		@Override
		public void run() {
			synchronized (this) {
				if (!didShow){
					if (stage5View.gameOver){
						optionsButton.setText("Hidden");
						retryButton.setVisibility(0);
						homeButton.setVisibility(0);
						nextButton.setVisibility(0);
						nextButton.setEnabled(false);
//						if (stage5View.gameSuccess) {
//							nextButton.setEnabled(true);
//						} else
//							nextButton.setEnabled(false);
						didShow = true;
						soundFactory.playSound(1);
					}
					mHandler.postDelayed(update, 1000);
				}
			}
		}
	};

	public void optionsClick(View view){
		Log.i("hcd", "点击了"+optionsButton.getText()+optionsButton.getVisibility());
		if (optionsButton.getText().toString().equals("Options")){
			optionsButton.setText("Hidden");
			retryButton.setVisibility(0);
			nextButton.setVisibility(0);
			nextButton.setEnabled(false);
			homeButton.setVisibility(0);
		} else {
			optionsButton.setText("Options");
			retryButton.setVisibility(-1);
			nextButton.setVisibility(-1);
			homeButton.setVisibility(-1);
		}
	}
	
	public void  retryClick(View view){
		Intent intent = new Intent();
		// 设置Intent的源地址和目标地址
		intent.setClass(getApplicationContext(), Stage5Activity.class);
		// 调用startActivity方法发送意图给系统
		startActivity(intent);
		//关闭当前activity，添加了该语句后，用户通过点击返回键是无法返回该activity的
		Stage5Activity.this.finish();
	}
	
	public void homeClick(View view) {
		Intent intent = new Intent();
		// 设置Intent的源地址和目标地址
		intent.setClass(getApplicationContext(), LevelActivity.class);
		// 调用startActivity方法发送意图给系统
		startActivity(intent);
		//关闭当前activity，添加了该语句后，用户通过点击返回键是无法返回该activity的
		Stage5Activity.this.finish();
	}
	
	public void nextClick(View view) {
		Intent intent = new Intent();
		// 设置Intent的源地址和目标地址
		intent.setClass(getApplicationContext(), Stage5Activity.class);
		// 调用startActivity方法发送意图给系统
		startActivity(intent);
		//关闭当前activity，添加了该语句后，用户通过点击返回键是无法返回该activity的
		Stage5Activity.this.finish();
	}
	
	public void showAlertDialog() {

		CustomDialog.Builder builder = new CustomDialog.Builder(this);
//		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Are you sure to quite the Cut-Polygen game?");
		builder.setTitle("Message");
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				//设置你的操作事项
				ExitApplication.getInstance().exit();
			}
		});

		builder.setNegativeButton("CANCEL",
				new android.content.DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		builder.create().show();
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.stage4, menu);
		return true;
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
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			showAlertDialog();
		}
		return super.onKeyDown(keyCode, event);
	}

}
