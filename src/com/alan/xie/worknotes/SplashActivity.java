package com.alan.xie.worknotes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/**
 * @author alan.xie
 * @date 2015-1-8 上午10:30:03
 * @Description: 过渡界面
 */
public class SplashActivity extends Activity {
	
	private final int SPLASH_DISPLAY_LENGTH = 3000; //延时3秒跳转

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_app_start);
		
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				Intent intent = new Intent();
				intent.setClass(SplashActivity.this, MainActivity.class);
				startActivity(intent);
				/*
				 * 跳转动画
				 */
				overridePendingTransition(android.R.anim.fade_in,
						android.R.anim.fade_out);
				SplashActivity.this.finish();
			}
		}, SPLASH_DISPLAY_LENGTH);
		
	}
	
}
