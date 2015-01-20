package com.alan.xie.worknotes.activity.customloading;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.alan.xie.worknotes.BaseActivity;
import com.alan.xie.worknotes.R;
import com.alan.xie.worknotes.view.loading.PieProgress;

/**
 * @author alan.xie
 * @date 2015-1-8 上午11:21:57
 * @Description: 自定义加载框
 */
public class CustomLoadingActivity extends BaseActivity implements OnClickListener{
	
	/*
	 * 馅饼图
	 */
	private PieProgress pie_progress;
	boolean pieRunning;
	int pieProgress = 0; //（0-360）

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_custom_loading);
		
		pie_progress = (PieProgress) findViewById(R.id.pie_progress);
		
		pie_progress.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.pie_progress:
			if(!pieRunning){
				pieProgress = 0;
				new Thread(indicatorRunnable).start();
			}
			break;

		default:
			break;
		}
	}
	
	final Runnable indicatorRunnable = new Runnable() {
		public void run() {
			pieRunning = true;
			while (pieProgress < 151) {
				pie_progress.setProgress(pieProgress);
				pieProgress += 2;
				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			pieRunning = false;
		}
	};
}







