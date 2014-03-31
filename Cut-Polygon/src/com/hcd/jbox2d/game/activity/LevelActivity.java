package com.hcd.jbox2d.game.activity;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class LevelActivity extends Activity {

	private Button lev1Button, lev2Button, lev3Button, lev4Button;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_level);
		
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);// ȫ��
		
		lev1Button = (Button) findViewById(R.id.Lev1Button);
		lev2Button = (Button) findViewById(R.id.Lev2Button);
		lev3Button = (Button) findViewById(R.id.Lev3Button);
		lev4Button = (Button) findViewById(R.id.Lev4Button);
		
		lev1Button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				// ����Intent��Դ��ַ��Ŀ���ַ
				intent.setClass(getApplicationContext(), FirstActivity.class);
				// ����startActivity����������ͼ��ϵͳ
				startActivity(intent);
				//�رյ�ǰactivity������˸������û�ͨ��������ؼ����޷����ظ�activity��
				LevelActivity.this.finish();
			}
		});
		lev2Button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				// ����Intent��Դ��ַ��Ŀ���ַ
				intent.setClass(getApplicationContext(), SecondActivity.class);
				// ����startActivity����������ͼ��ϵͳ
				startActivity(intent);
				//�رյ�ǰactivity������˸������û�ͨ��������ؼ����޷����ظ�activity��
				LevelActivity.this.finish();
			}
		});
		lev3Button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				// ����Intent��Դ��ַ��Ŀ���ַ
				intent.setClass(getApplicationContext(), ThirdActivity.class);
				// ����startActivity����������ͼ��ϵͳ
				startActivity(intent);
				//�رյ�ǰactivity������˸������û�ͨ��������ؼ����޷����ظ�activity��
				LevelActivity.this.finish();
			}
		});
		lev4Button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				// ����Intent��Դ��ַ��Ŀ���ַ
				intent.setClass(getApplicationContext(), FourthActivity.class);
				// ����startActivity����������ͼ��ϵͳ
				startActivity(intent);
				//�رյ�ǰactivity������˸������û�ͨ��������ؼ����޷����ظ�activity��
				LevelActivity.this.finish();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.level, menu);
		return true;
	}

}
