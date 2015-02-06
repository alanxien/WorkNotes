package com.alan.xie.worknotes.activity.customloading;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alan.xie.worknotes.BaseActivity;
import com.alan.xie.worknotes.R;

/**
 * @author alan.xie
 * @date 2015-1-8 上午11:21:57
 * @Description: 自定义加载框
 */
public class CustomLoadingActivity extends BaseActivity{
	
	private ProgressBar charging_progressBar;
	private TextView tv_charging_percent;
	
	private int progress = 0;
	private int percent = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_custom_loading);
		
		charging_progressBar = (ProgressBar) findViewById(R.id.charging_progressBar);
		tv_charging_percent = (TextView) findViewById(R.id.tv_charging_percent);
		
		new Thread(runnable).start();
	}
	
	final Runnable runnable = new Runnable() {
		
		@Override
		public void run() {
			//Message msg = handler.obtainMessage();
			while(progress < 100){
				progress += 5;
				percent += 5;
				
				Message msg = new Message();
				handler.sendMessage(msg);
				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
	};

	Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			charging_progressBar.setProgress(progress);
			tv_charging_percent.setText(percent+"%");
		};
	};
}







