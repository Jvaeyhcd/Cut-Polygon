package com.hcd.jbox2d.game.activity;

import com.hcd.jbox2d.game.obj.ExitApplication;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;

public class StartActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_home);
		
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);// 全屏
		
//		Button play = (Button) findViewById(R.id.play);
//		play.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				Intent intent = new Intent();
//				// 设置Intent的源地址和目标地址
//				intent.setClass(getApplicationContext(), LevelActivity.class);
//				// 调用startActivity方法发送意图给系统
//				startActivity(intent);
//				//关闭当前activity，添加了该语句后，用户通过点击返回键是无法返回该activity的
//				StartActivity.this.finish();
//			}
//		});
		final View view = View.inflate(this, R.layout.start, null);
        setContentView(view);
                                                                      
        //渐变展示启动屏
        AlphaAnimation aa = new AlphaAnimation(0.3f,1.0f);
        aa.setDuration(5000);
        view.startAnimation(aa);
        aa.setAnimationListener(new AnimationListener()
        {
        	@Override
            public void onAnimationEnd(Animation arg0) {
                redirectTo();
            }
        	@Override
            public void onAnimationRepeat(Animation animation) {}
        	@Override
            public void onAnimationStart(Animation animation) {}
                                                                          
        });
		ExitApplication.getInstance().addActivity(this);
	}

	/**
     * 跳转到...
     */
    private void redirectTo(){       
    	Intent intent = new Intent();
		// 设置Intent的源地址和目标地址
		intent.setClass(getApplicationContext(), LevelActivity.class);
		// 调用startActivity方法发送意图给系统
		startActivity(intent);
		//关闭当前activity，添加了该语句后，用户通过点击返回键是无法返回该activity的
		StartActivity.this.finish();
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}
	
//	@Override
//	protected void onResume() {
//		/**
//		 * 强制变成竖屏，不能变成横屏
//		 */
//		if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
//			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//		}
//		super.onResume();
//	}

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
