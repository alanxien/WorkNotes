package com.alan.xie.worknotes.activity.pie;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.alan.xie.worknotes.BaseActivity;
import com.alan.xie.worknotes.R;
import com.alan.xie.worknotes.view.loading.PieProgress;

/**
 * @author alan.xie
 * @date 2015-1-8 上午11:21:57
 * @Description: 馅饼图
 */
public class PieActivity extends BaseActivity implements OnClickListener{
	
	/*
	 * 饼图
	 */
	private PieProgress pie_progress;
	boolean pieRunning;
	int pieProgress = 0; //每次画多少度（0-360）

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pie);
		
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
			while (pieProgress < 361) {
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







