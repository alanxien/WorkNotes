package com.alan.xie.worknotes;

import java.util.Calendar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alan.xie.worknotes.R;
import com.alan.xie.worknotes.common.Constant;
import com.alan.xie.worknotes.service.LockScreenService;
import com.alan.xie.worknotes.view.SliderRelativeLayout;

/**
 * @author alan.xie
 * @date 2014-12-1 上午11:54:24
 * @Description: 锁屏
 */
public class LockScreenActivity extends BaseActivity{
	private final String TAG = "LockScreenActivity";
	private SliderRelativeLayout sliderRelativeLayout;
	public static int MSG_LOCK_SUCCESS_L = 1; //向左滑动
	public static int MSG_LOCK_SUCCESS_R = 2; //向右滑动
	public static int MSG_DOWNLOAD_SUCCESS = 3;//从服务器下载数据成功
	
	public static final int FLAG_HOMEKEY_DISPATCHED = 0x80000000; //自己定义标志
	private TextView tv_time;
	private TextView tv_date;
	private TextView tv_weeks;
	private RelativeLayout rl_main;
	private Intent intent;
	
	private Calendar calendar = null;
	
	private int[] wallpaper = {R.drawable.w2,R.drawable.w3,R.drawable.w4,R.drawable.w5, 
							   R.drawable.w6,R.drawable.w7,R.drawable.w8,R.drawable.w9,R.drawable.w10,R.drawable.w11,
			                   R.drawable.w12,R.drawable.w13, R.drawable.w14,R.drawable.w15, R.drawable.w16};
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE); //不要title
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
				WindowManager.LayoutParams.FLAG_FULLSCREEN); //全屏显示
		
		this.getWindow().setFlags(FLAG_HOMEKEY_DISPATCHED, FLAG_HOMEKEY_DISPATCHED);//关键代码
		
		setContentView(R.layout.activity_lock_screen);
		
		tv_time = (TextView) findViewById(R.id.tv_time);
		tv_date = (TextView) findViewById(R.id.tv_date);
		tv_weeks = (TextView) findViewById(R.id.tv_weeks);
		rl_main = (RelativeLayout) findViewById(R.id.rl_main);
		
		int ran = (int)(Math.random()*15); //随机0-14
		rl_main.setBackground(getResources().getDrawable(wallpaper[ran]));
		initData();
		
		sliderRelativeLayout = (SliderRelativeLayout) findViewById(R.id.sliderLayout);
		sliderRelativeLayout.setMainHandler(handler);
	}
	@Override
	protected void onRestart() {
		Log.i(TAG, "success---onRestart");
		super.onRestart();
	}
	@Override
	protected void onResume() {
		Log.i(TAG, "success---onResume");
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		if(pref.getBoolean(Constant.IS_KEY_HOME, false)){
			editor.putBoolean(Constant.IS_KEY_HOME, false);
			editor.commit();
			this.finish();
		}
		super.onPause();
	}
	
	@Override
	protected void onDestroy() {
		editor.putInt(Constant.SLIDER, 0);
		editor.commit();
		Log.i(TAG, "onDestroy----"+pref.getInt(Constant.SLIDER, 0));
		super.onDestroy();
	}
	
	@Override
	protected void onUserLeaveHint() {
		if(!pref.getBoolean(Constant.IS_KEY_HOME, false)){
			editor.putBoolean(Constant.IS_KEY_HOME, true);
			editor.commit();
		}
		super.onUserLeaveHint();
	}
	
	private void initData(){
		
		String dayOfWeeks = "";
		String minute = "";
		String hour = "";
		
		calendar = Calendar.getInstance();
		int tm = calendar.get(Calendar.MINUTE);
		int th = calendar.get(Calendar.HOUR_OF_DAY);
		
		if(th == 0){
			hour = "00";
		}else if(th < 10){
			hour = "0"+th;
		}else {
			hour = th+"";
		}
		
		if(tm == 0){
			minute="00";
		}else if(tm<10){
			minute = "0"+tm;
		}else{
			minute = tm+"";
		}
		tv_time.setText(hour+":"+minute);
		tv_date.setText((calendar.get(Calendar.MONTH)+1)+"月"+calendar.get(Calendar.DAY_OF_MONTH)+"日");
		switch (calendar.get(Calendar.DAY_OF_WEEK)) {
		case 1:
			dayOfWeeks="天";
			break;
		case 2:
			dayOfWeeks="一";
			break;
		case 3:
			dayOfWeeks="二";
			break;
		case 4:
			dayOfWeeks="三";
			break;
		case 5:
			dayOfWeeks="四";
			break;
		case 6:
			dayOfWeeks="五";
			break;
		case 7:
			dayOfWeeks="六";
			break;

		default:
			break;
		}
		tv_weeks.setText(" 星期"+dayOfWeeks);
	}
	
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			if(MSG_LOCK_SUCCESS_L == msg.what){
				finish();
				editor.putInt(Constant.SLIDER, 0);
				editor.commit();
			}else if(MSG_LOCK_SUCCESS_R == msg.what) {
				finish();
				editor.putInt(Constant.SLIDER, 0);
				editor.commit();
			}
		}
	};
	
	/**
	 * @author alan.xie
	 * @date 2014-12-1 上午11:54:41
	 * @Description: 震动
	 * @param 
	 * @return void
	 */
	private void virbate(){
		Vibrator vibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
		vibrator.vibrate(200);
	}
	
	
	/**
	 * 屏蔽掉返回键
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(event.getKeyCode() == KeyEvent.KEYCODE_BACK){
			return true;
		}else {
			return super.onKeyDown(keyCode, event);
		}
	}
}






