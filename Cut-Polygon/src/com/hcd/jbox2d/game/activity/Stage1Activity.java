package com.hcd.jbox2d.game.activity;

import com.hcd.jbox2d.game.view.CustomDialog;

import android.opengl.Visibility;
import android.os.Bundle;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class Stage1Activity extends Activity {
	
	private Button optionsButton, homeButton, nextButton, retryButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE); // 去title
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);// 全屏
		setContentView(R.layout.activity_stage1);
		optionsButton = (Button) findViewById(R.id.optionsstage1);
		homeButton = (Button)findViewById(R.id.homestage1);
		nextButton = (Button)findViewById(R.id.nextstage1);
		retryButton = (Button)findViewById(R.id.retrystage1);
	}

	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.stage1, menu);
		return true;
	}
	
	public void optionsClick(View view){
		Log.i("hcd", "点击了"+optionsButton.getText()+optionsButton.getVisibility());
		if (optionsButton.getText().toString().equals("Options")){
			optionsButton.setText("Hidden");
			retryButton.setVisibility(0);
			nextButton.setVisibility(0);
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
		intent.setClass(getApplicationContext(), Stage1Activity.class);
		// 调用startActivity方法发送意图给系统
		startActivity(intent);
		//关闭当前activity，添加了该语句后，用户通过点击返回键是无法返回该activity的
		Stage1Activity.this.finish();
	}

	public void showAlertDialog(View view) {

		CustomDialog.Builder builder = new CustomDialog.Builder(this);
//		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("这个就是自定义的提示框");
		builder.setTitle("提示");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				//设置你的操作事项
			}
		});

		builder.setNegativeButton("取消",
				new android.content.DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});

		builder.create().show();

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
}
