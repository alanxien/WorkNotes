
package com.alan.xie.worknotes.view.pie;

import com.alan.xie.worknotes.R;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.util.AttributeSet;
import android.view.View;


/**
 * @author alan.xie
 * @date 2015-1-20 上午10:34:56
 * @Description: 馅饼图 view
 */
public class PieProgress extends View {
	private final RectF mRect = new RectF();
	private final RectF mRectInner = new RectF();
	private final Paint mPaintForeground = new Paint();
	private final Paint mPaintBackground = new Paint();
	private final Paint mPaintErase = new Paint();
	/*
	 * 颜色渲染，PorterDuff.Mode.CLEAR绘制不会显示在画布上
	 */
	private static final Xfermode PORTER_DUFF_CLEAR = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);
	private int mColorForeground = Color.WHITE;
	private int mColorBackground = Color.BLACK;
	private int mProgress;
	private static final float PADDING = 4;
	private float mPadding;
	private Bitmap mBitmap;
	private static final float INNER_RADIUS_RATIO = 0.84f;

	public PieProgress(Context context) {
		this(context, null);
	}

	public PieProgress(Context context, AttributeSet attrs) {
		super(context, attrs);
		parseAttributes(context.obtainStyledAttributes(attrs, R.styleable.PieProgress));
		
		Resources r = context.getResources();
		float scale = r.getDisplayMetrics().density;
		mPadding = scale * PADDING + r.getDimension(R.dimen.actionbar_vertival_padding);
		mPaintForeground.setColor(mColorForeground);
		/*
		 * 去除锯齿
		 */
		mPaintForeground.setAntiAlias(true);
		mPaintBackground.setColor(mColorBackground);
		mPaintBackground.setAntiAlias(true);
		mPaintErase.setXfermode(PORTER_DUFF_CLEAR);
		mPaintErase.setAntiAlias(true);
	}
	
	
	/**
	 * @author alan.xie
	 * @date 2015-1-20 上午11:18:14
	 * @Description: 解析attrs.xml文件
	 * @param @param a
	 * @return void
	 */
	private void parseAttributes(TypedArray a) {
		mColorForeground = a.getColor(R.styleable.PieProgress_foregroundColor, mColorForeground);
		mColorBackground = a.getColor(R.styleable.PieProgress_backgroundColor, mColorBackground);
		//释放资源
		a.recycle();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawBitmap(mBitmap, getWidth() / 2 - mBitmap.getWidth() / 2, getHeight() / 2
				- mBitmap.getHeight() / 2, null);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		float bitmapWidth = w - 2 * mPadding;
		float bitmapHeight = h - 2 * mPadding;
		float radius = Math.min(bitmapWidth / 2, bitmapHeight / 2);
		mRect.set(0, 0, bitmapWidth, bitmapHeight);
		radius *= INNER_RADIUS_RATIO;
		mRectInner.set(bitmapWidth / 2f - radius, bitmapHeight / 2f - radius, bitmapWidth / 2f
				+ radius, bitmapHeight / 2f + radius);
		updateBitmap();
	}

	/**
	 * @author alan.xie
	 * @date 2015-1-20 上午11:21:08
	 * @Description: 设置前景色
	 * @param @param color
	 * @return void
	 */
	public void setForegroundColor(int color) {
		this.mColorForeground = color;
		mPaintForeground.setColor(color);
		invalidate();
	}

	/* 
	 * 设置背景色
	 */
	public void setBackgroundColor(int color) {
		this.mColorBackground = color;
		mPaintBackground.setColor(color);
		invalidate();
	}

	/**
	 * @author alan.xie
	 * @date 2015-1-20 上午11:21:59
	 * @Description: TODO
	 * @param @param progress
	 * @return void
	 */
	public synchronized void setProgress(int progress) {
		mProgress = progress;
		if (progress > 360) {
			mProgress = 360;
		}
		updateBitmap();
	}
	
	public void reset() {
		mProgress = 0;
		updateBitmap();
	}

	private void updateBitmap() {
		if (mRect == null || mRect.width() == 0) {
			return;
		}
		mBitmap = Bitmap.createBitmap((int) mRect.width(), (int) mRect.height(),
				Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(mBitmap);
		/*
		 * 画扇形
		 */
		canvas.drawArc(mRect, -90, 360, true, mPaintBackground);
		if (mProgress < 0) {
			canvas.drawLine(mRect.width() / 2, mRect.height() / 2, mRect.width() / 2, 0,
					mPaintForeground);
		}
		canvas.drawArc(mRect, -90, mProgress, true, mPaintForeground);
		postInvalidate();
	}
}
