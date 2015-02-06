package com.alan.xie.worknotes.view.pie;

import com.alan.xie.worknotes.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Paint.Style;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author alan.xie
 * @date 2015-1-29 下午4:56:49
 * @Description: 优化按钮
 */
public class ProgressWheel extends View {
	
	private static final int BAR_COLOR = 0xff3d3d41;
	private static final int RIM_COLOR = 0xff20B2AA;

	// 默认尺寸
	private int layout_height = 0;
	private int layout_width = 0;
	private int fullRadius = 100;
	private int circleRadius = 80;
	private int barLength = 60;
	private int barWidth = 20;
	private int rimWidth = 20;
	private int textSize = 20;

	// 默认边距
	private int paddingTop = 5;
	private int paddingBottom = 5;
	private int paddingLeft = 5;
	private int paddingRight = 5;

	// 默认颜色
	private int barColor = BAR_COLOR;
	private int rimColor = RIM_COLOR;
	private int circleColor = 0xff28282c;
	private int textColor = 0xffffffff;

	// 画笔
	private Paint barPaint = new Paint();
	private Paint circlePaint = new Paint();
	private Paint rimPaint = new Paint();
	private Paint textPaint = new Paint();

	private RectF circleBounds = new RectF();

	private int spinSpeed = 2; //圆弧旋转速度2px
	private int delayMillis = 0; //延时画上去
	private Handler spinHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			invalidate();
			if (isSpinning) {
				progress += spinSpeed;
				
				if (progress > 360) {
					progress = 0;
				}
				spinHandler.sendEmptyMessageDelayed(0, delayMillis);
			}
		}
	};
	
	int progress = 0;
	boolean isSpinning = false; //记录圆弧是否运动

	private String text = "";
	private String[] splitText = {};

	
	/**
	 * 构造函数
	 * @param context
	 * @param attrs
	 */
	public ProgressWheel(Context context, AttributeSet attrs) {
		super(context, attrs);

		parseAttributes(context.obtainStyledAttributes(attrs,
				R.styleable.ProgressWheel));
	}
	
	/**
	 * Use onSizeChanged instead of onAttachedToWindow to get the dimensions of
	 * the view, because this method is called after measuring the dimensions of
	 * MATCH_PARENT & WRAP_CONTENT. Use this dimensions to setup the bounds and
	 * paints.
	 */
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);

		layout_width = w;
		layout_height = h;

		setupBounds();
		setupPaints();
		invalidate();
	}

	/**
	 * @author alan.xie
	 * @date 2015-1-29 下午4:59:19
	 * @Description: 设置画笔属性
	 * @param 
	 * @return void
	 */
	private void setupPaints() {
		barPaint.setColor(barColor);
		barPaint.setAntiAlias(true);
		barPaint.setStyle(Style.STROKE);
		barPaint.setStrokeWidth(barWidth);

		rimPaint.setColor(rimColor);
		rimPaint.setAntiAlias(true);
		rimPaint.setStyle(Style.STROKE);
		rimPaint.setStrokeWidth(rimWidth);

		circlePaint.setColor(circleColor);
		circlePaint.setAntiAlias(true);
		circlePaint.setStyle(Style.FILL);

		textPaint.setColor(textColor);
		textPaint.setStyle(Style.FILL);
		textPaint.setAntiAlias(true);
		textPaint.setTextSize(textSize);
	}

	/**
	 * @author alan.xie
	 * @date 2015-1-29 下午4:59:35
	 * @Description: 设置边界
	 * @param 
	 * @return void
	 */
	private void setupBounds() {
		int minValue = Math.min(layout_width, layout_height);

		int xOffset = layout_width - minValue;
		int yOffset = layout_height - minValue;

		paddingTop = this.getPaddingTop() + (yOffset / 2);
		paddingBottom = this.getPaddingBottom() + (yOffset / 2);
		paddingLeft = this.getPaddingLeft() + (xOffset / 2);
		paddingRight = this.getPaddingRight() + (xOffset / 2);

		circleBounds = new RectF(paddingLeft + barWidth, paddingTop + barWidth,
				this.getLayoutParams().width - paddingRight - barWidth,
				this.getLayoutParams().height - paddingBottom - barWidth);

		fullRadius = (this.getLayoutParams().width - paddingRight - barWidth) / 2;
		circleRadius = (fullRadius - barWidth) + 1;
	}

	/**
	 * @author alan.xie
	 * @date 2015-1-29 下午4:59:49
	 * @Description: 从xml文件中解析属性
	 * @param a
	 * @return void
	 */
	private void parseAttributes(TypedArray a) {
		barWidth = (int) a.getDimension(
				R.styleable.ProgressWheel_barWidth, barWidth);

		rimWidth = (int) a.getDimension(
				R.styleable.ProgressWheel_rimWidth, rimWidth);

		spinSpeed = (int) a.getDimension(
				R.styleable.ProgressWheel_spinSpeed, spinSpeed);

		delayMillis = (int) a.getInteger(
				R.styleable.ProgressWheel_delayMillis, delayMillis);
		if (delayMillis < 0) {
			delayMillis = 0;
		}

		barColor = a.getColor(R.styleable.ProgressWheel_barColor,
				barColor);

		barLength = (int) a.getDimension(
				R.styleable.ProgressWheel_barLength, barLength);

		textSize = (int) a.getDimension(
				R.styleable.ProgressWheel_textSize, textSize);

		textColor = (int) a.getColor(
				R.styleable.ProgressWheel_textColor, textColor);

		//若果text为空，则忽略
		if (a.hasValue(R.styleable.ProgressWheel_text)) {
			setText(a.getString(R.styleable.ProgressWheel_text));
		}

		rimColor = (int) a.getColor(
				R.styleable.ProgressWheel_rimColor, rimColor);

		circleColor = (int) a.getColor(
				R.styleable.ProgressWheel_circleColor, circleColor);

		//循环
		a.recycle();
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// 画边圆弧rim
		canvas.drawArc(circleBounds, 360, 360, false, rimPaint);
		// 画动画圆弧bar
		if (isSpinning) {
			canvas.drawArc(circleBounds, progress - 90, barLength, false,
					barPaint);
		} else {
			canvas.drawArc(circleBounds, -90, progress, false, barPaint);
		}
		//画内部圆
		canvas.drawCircle((circleBounds.width() / 2) + rimWidth + paddingLeft,
				(circleBounds.height() / 2) + rimWidth + paddingTop,
				circleRadius, circlePaint);
		
		//画文本
		for (String s : splitText) {
			float offset = textPaint.measureText(s) / 2;
			canvas.drawText(s, this.getWidth() / 2 - offset, this.getHeight()
					/ 2 + (textSize / 2), textPaint);
		}
	}

	/**
	 * @author alan.xie
	 * @date 2015-1-29 下午5:01:22
	 * @Description: 当用户点击时，重置数据
	 * @param 
	 * @return void
	 */
	public void resetCount() {
		progress = 0;
		this.setBarColor(BAR_COLOR);
		this.setRimColor(RIM_COLOR);
		barPaint.setColor(barColor);
		rimPaint.setColor(rimColor);
		setText("click");
		invalidate();
	}
	
	/**
	 * @author alan.xie
	 * @date 2015-1-29 下午5:01:57
	 * @Description: 停止bar旋转
	 * @param 
	 * @return void
	 */
	public void stopSpinning() {
		isSpinning = false;
		progress = 0;
		spinHandler.removeMessages(0);
	}

	
	public void spin() {
		isSpinning = true;
		spinHandler.sendEmptyMessage(0);
	}

	/**
	 * @author xin.xie
	 */
	public void incrementProgress() {
		isSpinning = false;
		progress++;

		if (progress > 360) {//如果画了一圈，之后交替重绘
			this.setRimColor(BAR_COLOR);
			this.setBarColor(RIM_COLOR);
			barPaint.setColor(barColor);
			rimPaint.setColor(rimColor);
			progress = 0;

		}

		// setText(Math.round(((float) progress / 360) * 100) + "%");
		spinHandler.sendEmptyMessage(0);
	}

	public void setProgress(int i) {
		isSpinning = false;
		progress = i;
		spinHandler.sendEmptyMessage(0);
	}
	
	public void setText(String text) {
		this.text = text;
		splitText = this.text.split("\n");
	}

	public int getCircleRadius() {
		return circleRadius;
	}

	public void setCircleRadius(int circleRadius) {
		this.circleRadius = circleRadius;
	}

	public int getBarLength() {
		return barLength;
	}

	public void setBarLength(int barLength) {
		this.barLength = barLength;
	}

	public int getBarWidth() {
		return barWidth;
	}

	public void setBarWidth(int barWidth) {
		this.barWidth = barWidth;
	}

	public int getTextSize() {
		return textSize;
	}

	public void setTextSize(int textSize) {
		this.textSize = textSize;
	}

	public int getPaddingTop() {
		return paddingTop;
	}

	public void setPaddingTop(int paddingTop) {
		this.paddingTop = paddingTop;
	}

	public int getPaddingBottom() {
		return paddingBottom;
	}

	public void setPaddingBottom(int paddingBottom) {
		this.paddingBottom = paddingBottom;
	}

	public int getPaddingLeft() {
		return paddingLeft;
	}

	public void setPaddingLeft(int paddingLeft) {
		this.paddingLeft = paddingLeft;
	}

	public int getPaddingRight() {
		return paddingRight;
	}

	public void setPaddingRight(int paddingRight) {
		this.paddingRight = paddingRight;
	}

	public int getBarColor() {
		return barColor;
	}

	public void setBarColor(int barColor) {
		this.barColor = barColor;
	}

	public int getCircleColor() {
		return circleColor;
	}

	public void setCircleColor(int circleColor) {
		this.circleColor = circleColor;
	}

	public int getRimColor() {
		return rimColor;
	}

	public void setRimColor(int rimColor) {
		this.rimColor = rimColor;
	}

	public Shader getRimShader() {
		return rimPaint.getShader();
	}

	public void setRimShader(Shader shader) {
		this.rimPaint.setShader(shader);
	}

	public int getTextColor() {
		return textColor;
	}

	public void setTextColor(int textColor) {
		this.textColor = textColor;
	}

	public int getSpinSpeed() {
		return spinSpeed;
	}

	public void setSpinSpeed(int spinSpeed) {
		this.spinSpeed = spinSpeed;
	}

	public int getRimWidth() {
		return rimWidth;
	}

	public void setRimWidth(int rimWidth) {
		this.rimWidth = rimWidth;
	}

	public int getDelayMillis() {
		return delayMillis;
	}

	public void setDelayMillis(int delayMillis) {
		this.delayMillis = delayMillis;
	}

}
