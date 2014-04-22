package com.hcd.jbox2d.game.activity;

import com.hcd.jbox2d.game.obj.ExitApplication;
import com.hcd.jbox2d.game.utils.SoundFactory;
import com.hcd.jbox2d.game.view.CustomDialog;
import com.hcd.jbox2d.game.view.Stage1View;

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

public class Stage1Activity extends Activity {
	
	private Button optionsButton, homeButton, nextButton, retryButton;
	public static int  screenWidth, screenHeight;
	public Stage1View stage1View;
	public Handler mHandler;
	public boolean didShow;
	public SoundFactory soundFactory;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE); // ȥtitle
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);// ȫ��
		
		soundFactory = new SoundFactory(this);
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		screenWidth = metric.widthPixels;
		screenHeight = metric.heightPixels;
		didShow = false;
		setContentView(R.layout.activity_stage1);
		optionsButton = (Button) findViewById(R.id.optionsstage1);
		homeButton = (Button)findViewById(R.id.homestage1);
		nextButton = (Button)findViewById(R.id.nextstage1);
		retryButton = (Button)findViewById(R.id.retrystage1);
		//������ӵ�ID��ȡ�Զ������ϷView
		stage1View = (Stage1View) findViewById(R.id.stage1View);
		mHandler = new Handler();
		mHandler.post(update);
		ExitApplication.getInstance().addActivity(this);
	}

	private Runnable update = new Runnable() {

		@Override
		public void run() {
			synchronized (this) {
				if (!didShow){
					if (stage1View.gameOver){
						optionsButton.setText("Hidden");
						retryButton.setVisibility(0);
						nextButton.setVisibility(0);
						homeButton.setVisibility(0);
						if (stage1View.gameOver) {
							nextButton.setEnabled(true);
						} else
							nextButton.setEnabled(false);
						didShow = true;
						soundFactory.playSound(1);
					}
					mHandler.postDelayed(update, 1000);
				}
			}
		}
	};
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.stage1, menu);
		return true;
	}
	
	public void optionsClick(View view){
		Log.i("hcd", "�����"+optionsButton.getText()+optionsButton.getVisibility());
		if (optionsButton.getText().toString().equals("Options")){
			optionsButton.setText("Hidden");
			retryButton.setVisibility(0);
			nextButton.setVisibility(0);
			homeButton.setVisibility(0);
			if (stage1View.gameOver) {
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
		intent.setClass(getApplicationContext(), Stage1Activity.class);
		// ����startActivity����������ͼ��ϵͳ
		startActivity(intent);
		//�رյ�ǰactivity������˸������û�ͨ��������ؼ����޷����ظ�activity��
		Stage1Activity.this.finish();
	}
	
	public void homeClick(View view) {
		Intent intent = new Intent();
		// ����Intent��Դ��ַ��Ŀ���ַ
		intent.setClass(getApplicationContext(), LevelActivity.class);
		// ����startActivity����������ͼ��ϵͳ
		startActivity(intent);
		//�رյ�ǰactivity������˸������û�ͨ��������ؼ����޷����ظ�activity��
		Stage1Activity.this.finish();
	}
	
	public void nextClick(View view) {
		Intent intent = new Intent();
		// ����Intent��Դ��ַ��Ŀ���ַ
		intent.setClass(getApplicationContext(), Stage2Activity.class);
		// ����startActivity����������ͼ��ϵͳ
		startActivity(intent);
		//�رյ�ǰactivity������˸������û�ͨ��������ؼ����޷����ظ�activity��
		Stage1Activity.this.finish();
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
