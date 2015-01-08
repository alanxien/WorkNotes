package com.alan.xie.worknotes.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.alan.xie.worknotes.R;
import com.alan.xie.worknotes.LockScreenActivity;

/**
 * @author alan.xie
 * @date 2014-12-1 下午12:01:54
 * @Description: 锁屏
 */
public class SliderRelativeLayout extends RelativeLayout{
	private final static String TAG = "SliderRelativeLayout";
	private Context context;
	private Bitmap dragBitmap = null; //拖拽图片
	private int locationX = 0; //bitmap初始绘图位置，足够大，可以认为看不见
	private ImageView heartView = null; //主要是获取相对布局高度
	private ImageView leftRingView = null;
	private ImageView rightRingView = null;
	private Handler handler = null; //信息传递
	private static int BACK_DURATION = 10 ;   // 回滚动画时间间隔20ms
	private static float VE_HORIZONTAL = 0.9f ;  // 水平方向前进速率 0.1dip/ms
	
	public SliderRelativeLayout(Context context) {
		super(context); 
		SliderRelativeLayout.this.context = context;
		intiDragBitmap();
	}
	
	public SliderRelativeLayout(Context context, AttributeSet attrs) {
		super(context, attrs, 0);
		SliderRelativeLayout.this.context = context;
		intiDragBitmap();
	}

	public SliderRelativeLayout(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		SliderRelativeLayout.this.context = context;
		intiDragBitmap();
	}

	
	private void intiDragBitmap() {
		if(dragBitmap == null){
			dragBitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.icon_action_circle_frame);
		}
	}
	
	
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		heartView = (ImageView) findViewById(R.id.loveView);
		leftRingView = (ImageView) findViewById(R.id.leftRing);
		rightRingView = (ImageView) findViewById(R.id.rightRing);
	}

	/**
	 * 对拖拽图片不同的点击事件处理
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int X = (int) event.getX();

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN: 
			//dragBitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.icon_slide_circle_n);
			locationX = (int) event.getX();
			Log.i(TAG, "是否点击到位=" + isActionDown(event));
			return isActionDown(event);//判断是否点击了滑动区域
			
		case MotionEvent.ACTION_MOVE: //保存x轴方向，绘制图画
			locationX = X;
			invalidate(); //重新绘图
			return true;
			
		case MotionEvent.ACTION_UP: //判断是否解锁成功
			//dragBitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.icon_action_circle_frame);
			if(!unLockLeft() && !unLockRight()){ //没有解锁成功,动画应该回滚
				handleActionUpEvent(event); //动画回滚
			}
			return true;
		}
		return super.onTouchEvent(event);
	}
	
	/**
	 * @author alan.xie
	 * @date 2014-12-1 上午11:59:59
	 * @Description: 回滚动画
	 * @param @param event
	 * @return void
	 */
	private void handleActionUpEvent(MotionEvent event) {
		int x = (int) event.getX();
		if( x < getScreenWidth()/2){
			locationX = getScreenWidth()/2 - x; 
		}
		if(x > getScreenWidth()/2){
			locationX = x - getScreenWidth()/2;
		}
		
		if(locationX > 0){
			handler.postDelayed(ImageBack, BACK_DURATION); //回�?
		}
	}

	/**
	 * 未解锁时，图片回滚
	 */
	private Runnable ImageBack = new Runnable() {
		@Override
		public void run() {
			locationX = locationX - (int) (VE_HORIZONTAL*BACK_DURATION);
			if(locationX > 0){
				handler.postDelayed(ImageBack, BACK_DURATION); //回滚
				invalidate();
			}
		}
	};
	
	/**
	 * 判断是否点击到了滑动区域
	 * @param event
	 * @return
	 */
	private boolean isActionDown(MotionEvent event) {
		Rect rect = new Rect();
		heartView.getHitRect(rect);
		boolean isIn = rect.contains((int)event.getX(), (int)event.getY());
		if(isIn){
			heartView.setVisibility(View.GONE);
			return true;
		}
		return false;
	}
	
	/**
	 * 绘制拖动时的图片
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		invalidateDragImg(canvas);
	}
	
	
	/**
	 * @author alan.xie
	 * @date 2014-12-1 上午11:59:38
	 * @Description: 图片随手势移懂
	 * @param @param canvas
	 * @return void
	 */
	private void invalidateDragImg(Canvas canvas) { 
		int drawX = locationX - heartView.getWidth()/2;
		int drawY = heartView.getTop();
		
		if(drawX < -30){ 
			heartView.setVisibility(View.VISIBLE);
			return;
		}else {
			if(locationX < leftRingView.getWidth()){ //向左解锁
				return;
			}
			if(locationX > (getScreenWidth() - rightRingView.getWidth()) || locationX < leftRingView.getWidth()){ //向右解锁
				return;
			}
			heartView.setVisibility(View.GONE);
			if(drawX > leftRingView.getWidth()-10){
				canvas.drawBitmap(dragBitmap,drawX,drawY,null);
			}
			
			
		}	
	}
	
	/**
	 * @author alan.xie
	 * @Description: 向左解锁
	 * @return
	 */
	private boolean unLockLeft(){
		if(locationX < leftRingView.getWidth()){
			//leftRingView.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_lock_screen_share_s));
			handler.obtainMessage(LockScreenActivity.MSG_LOCK_SUCCESS_L).sendToTarget();
			Log.i(TAG, "-------------------left----------");
			return true;
		}
		return false;
	}
	
	/**
	 * @author alan.xie
	 * @Description: 向右解锁
	 * @return
	 */
	private boolean unLockRight(){
		if(locationX > (getScreenWidth() - rightRingView.getWidth())){
			//rightRingView.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_action_unlock_s));
			handler.obtainMessage(LockScreenActivity.MSG_LOCK_SUCCESS_R).sendToTarget();
			Log.i(TAG, "-------------------right----------");
			return true;
		}
		return false;
	}
	
	/**
	 * @author alan.xie
	 * @date 2014-12-1 上午11:56:14
	 * @Description: 获取屏幕宽度
	 * @param @return
	 * @return int
	 */
	private int getScreenWidth(){
		WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		int width = manager.getDefaultDisplay().getWidth();
		return width;
	}
	
	/**
	 * @author alan.xie
	 * @date 2014-12-1 上午11:56:35
	 * @Description: 与主activity通信
	 * @param @param handler
	 * @return void
	 */
	public void setMainHandler(Handler handler){
		this.handler = handler;
	}

}
