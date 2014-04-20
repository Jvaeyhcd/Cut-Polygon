package com.hcd.jbox2d.game.activity;

import com.hcd.jbox2d.game.obj.ExitApplication;
import com.hcd.jbox2d.game.view.CustomDialog;
import com.hcd.jbox2d.game.view.Stage4View;

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

public class Stage4Activity extends Activity {

	private Button optionsButton, homeButton, nextButton, retryButton;
	public static int  screenWidth, screenHeight;
	private Stage4View stage4View;
	public Handler mHandler;
	public boolean didShow;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE); // ȥtitle
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);// ȫ��
		
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		screenWidth = metric.widthPixels;
		screenHeight = metric.heightPixels;
		
		setContentView(R.layout.activity_stage4);
		optionsButton = (Button) findViewById(R.id.optionsstage4);
		homeButton = (Button)findViewById(R.id.homestage4);
		nextButton = (Button)findViewById(R.id.nextstage4);
		retryButton = (Button)findViewById(R.id.retrystage4);
		
		didShow = false;
		stage4View = (Stage4View) findViewById(R.id.stage4View);
		mHandler = new Handler();
		mHandler.post(update);
		
		ExitApplication.getInstance().addActivity(this);
	}
	
	private Runnable update = new Runnable() {

		@Override
		public void run() {
			synchronized (this) {
				if (!didShow){
					if (stage4View.gameSuccess){
						optionsButton.setText("Hidden");
						retryButton.setVisibility(0);
						nextButton.setVisibility(0);
						homeButton.setVisibility(0);
						if (stage4View.gameSuccess) {
							nextButton.setEnabled(true);
						} else
							nextButton.setEnabled(false);
						didShow = true;
					}
					mHandler.postDelayed(update, 1000);
				}
			}
		}
	};

	public void optionsClick(View view){
		Log.i("hcd", "�����"+optionsButton.getText()+optionsButton.getVisibility());
		if (optionsButton.getText().toString().equals("Options")){
			optionsButton.setText("Hidden");
			retryButton.setVisibility(0);
			nextButton.setVisibility(0);
			homeButton.setVisibility(0);
			if (stage4View.gameSuccess) {
				nextButton.setEnabled(true);
			} else
				nextButton.setEnabled(false);
		} else {
			optionsButton.setText("Options");
			retryButton.setVisibility(-1);
			nextButton.setVisibility(-1);
			homeButton.setVisibility(-1);
		}
	}
	
	public void  retryClick(View view){
		Intent intent = new Intent();
		// ����Intent��Դ��ַ��Ŀ���ַ
		intent.setClass(getApplicationContext(), Stage4Activity.class);
		// ����startActivity����������ͼ��ϵͳ
		startActivity(intent);
		//�رյ�ǰactivity������˸������û�ͨ��������ؼ����޷����ظ�activity��
		Stage4Activity.this.finish();
	}
	
	public void homeClick(View view) {
		Intent intent = new Intent();
		// ����Intent��Դ��ַ��Ŀ���ַ
		intent.setClass(getApplicationContext(), LevelActivity.class);
		// ����startActivity����������ͼ��ϵͳ
		startActivity(intent);
		//�رյ�ǰactivity������˸������û�ͨ��������ؼ����޷����ظ�activity��
		Stage4Activity.this.finish();
	}
	
	public void nextClick(View view) {
		Intent intent = new Intent();
		// ����Intent��Դ��ַ��Ŀ���ַ
		intent.setClass(getApplicationContext(), Stage5Activity.class);
		// ����startActivity����������ͼ��ϵͳ
		startActivity(intent);
		//�رյ�ǰactivity������˸������û�ͨ��������ؼ����޷����ظ�activity��
		Stage4Activity.this.finish();
	}
	
	public void showAlertDialog() {

		CustomDialog.Builder builder = new CustomDialog.Builder(this);
//		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Are you sure to quite the Cut-Polygen game?");
		builder.setTitle("Message");
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				//������Ĳ�������
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
		 * ǿ�Ʊ�ɺ��������ܱ������
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
