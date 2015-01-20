package com.alan.xie.worknotes.activity.pie;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.alan.xie.worknotes.BaseActivity;
import com.alan.xie.worknotes.R;
import com.alan.xie.worknotes.entity.PieItemBean;
import com.alan.xie.worknotes.view.pie.PieChart;
import com.alan.xie.worknotes.view.pie.PieProgress;

/**
 * @author alan.xie
 * @date 2015-1-8 上午11:21:57
 * @Description: 饼图
 */
public class PieActivity extends BaseActivity implements OnClickListener{
	
	/*
	 * 饼图
	 */
	private PieProgress pie_progress;
	boolean pieRunning;
	int pieProgress = 0; //每次画多少度（0-360）
	
	PieChart pieChart;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pie);
		
		pie_progress = (PieProgress) findViewById(R.id.pie_progress);
		pieChart = (PieChart) findViewById(R.id.pie_chart);
		
		pie_progress.setOnClickListener(this);
		
		initPieChart();
	}
	
	/**
	 * @author alan.xie
	 * @date 2015-1-20 下午5:56:47
	 * @Description: 初始化饼图
	 * @param 
	 * @return void
	 */
	private void initPieChart(){
        PieItemBean[] items = new PieItemBean[]{
                new PieItemBean("娱乐", 200),
                new PieItemBean("旅行", 100),
                new PieItemBean("学习", 120),
                new PieItemBean("人际关系", 160),
                new PieItemBean("交通", 100),
                new PieItemBean("餐饮", 480)
        };
        pieChart.setPieItems(items);
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







