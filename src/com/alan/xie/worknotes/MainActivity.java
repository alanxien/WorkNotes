package com.alan.xie.worknotes;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.alan.xie.worknotes.common.Constant;
import com.alan.xie.worknotes.service.LockScreenService;


/**
 * @author alan.xie
 * @date 2015-1-8 上午10:25:43
 * @Description: 主界面
 */
public class MainActivity extends BaseActivity implements OnClickListener{
	
	private TextView tv_lock_screen;		//锁屏
	private TextView tv_custom_loading; 	//自定义加载框
	
	private Intent intent;
	
	private static Boolean isExit = false; 	//双击退出
	private Timer exit = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		initView();
		initData();
	}

	/**
	 * @author alan.xie
	 * @date 2015-1-8 上午10:26:32
	 * @Description: 初始化view
	 * @param 
	 * @return void
	 */
	private void initView(){
		tv_lock_screen = (TextView) findViewById(R.id.tv_lock_screen);
		tv_custom_loading = (TextView) findViewById(R.id.tv_custom_loading);
		
		tv_lock_screen.setOnClickListener(this);
		tv_custom_loading.setOnClickListener(this);
		
		intent = new Intent();
		if(exit == null){
			exit = new Timer();
		}
	}
	

	/**
	 * @author alan.xie
	 * @date 2015-1-8 上午10:26:41
	 * @Description: 初始化数据
	 * @param 
	 * @return void
	 */
	private void initData(){
		if(!pref.getBoolean(Constant.IS_LOCK_SCREEN, false)){
			tv_lock_screen.setText("开启锁屏");
		}else{
			intent.setClass(this, LockScreenService.class);
			editor.putBoolean(Constant.IS_LOCK_SCREEN, true);
			editor.putInt(Constant.SLIDER, 0);
			editor.commit();
			startService(intent);
			tv_lock_screen.setText("关闭锁屏");
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_lock_screen:
			intent.setClass(this, LockScreenService.class);
			if(!pref.getBoolean(Constant.IS_LOCK_SCREEN, false)){
				editor.putBoolean(Constant.IS_LOCK_SCREEN, true);
				editor.putInt(Constant.SLIDER, 0);
				startService(intent);
				tv_lock_screen.setText("关闭锁屏");
				Toast.makeText(this, "锁屏已开启", Toast.LENGTH_SHORT).show();
			}else{
				editor.putBoolean(Constant.IS_LOCK_SCREEN, false);
				stopService(intent);
				tv_lock_screen.setText("开启锁屏");
				Toast.makeText(this, "锁屏已关闭", Toast.LENGTH_SHORT).show();
			}
			editor.commit();
			break;
		case R.id.tv_custom_loading:
			intent.setClass(this, CustomLoading.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			//双击退出
			exitBy2Click();
		}
		return false;
	}
	
	
	/**
	 * @author alan.xie
	 * @date 2015-1-8 上午10:27:24
	 * @Description: 双击退出
	 * @param 
	 * @return void
	 */
	private void exitBy2Click(){
		
		if(isExit == false){
			isExit = true;
			Toast.makeText(this, "双击退出！", Toast.LENGTH_SHORT).show();
			exit.schedule(new TimerTask() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					isExit = false;
				}
			},2000); //延时2秒后执行
		}else {
			this.finish();
			//System.exit(0);
		}
	}
}







