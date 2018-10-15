package com.sf.demo.view;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.sf.demo.R;

public class PanelView extends View {
	private int mFirstColor;
	private int mSecondColor;
	private int mMarkCount;
	private int mOutCircleWidth;
	private int mInCircleWidth;
	private int mSpace;
	private int mSmallSpace;
	private int mTextSize;

	private Paint mPaint;
	private RectF oval;
	private int lineWidth = 5;
	private float mProgress = (float) (20 * 1.0f * 2.7);
	private int mWidth, mHeight;

	public PanelView(Context context) {
		super(context, null);
		// TODO Auto-generated constructor stub
	}

	public PanelView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub
	}

	public PanelView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
		TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
				R.styleable.PanelView, defStyleAttr, 0);
		mFirstColor = a.getColor(R.styleable.PanelView_aFirstColor, Color.BLUE);
		mSecondColor = a
				.getColor(R.styleable.PanelView_aSecondColor, Color.RED);
		mMarkCount = a.getInt(R.styleable.PanelView_markCount, 12);
		mSpace = a.getDimensionPixelSize(R.styleable.PanelView_space,
				(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
						10, getResources().getDisplayMetrics()));
		mSmallSpace = a.getDimensionPixelSize(R.styleable.PanelView_smallSpace,
				(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
						10, getResources().getDisplayMetrics()));
		mOutCircleWidth = a.getDimensionPixelSize(
				R.styleable.PanelView_outCircleWidth, (int) TypedValue
						.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20,
								getResources().getDisplayMetrics()));
		mInCircleWidth = a.getDimensionPixelSize(
				R.styleable.PanelView_inCircleWidth, (int) TypedValue
						.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10,
								getResources().getDisplayMetrics()));
		mTextSize = a.getDimensionPixelSize(R.styleable.PanelView_textSize,
				(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 20,
						getResources().getDisplayMetrics()));
		a.recycle();
		mPaint = new Paint();
		oval = new RectF();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		// int center = getWidth() / 2;
		int center = mWidth / 2; // 两者都可以

		// 画最外面的刻度圈
		mPaint.setAntiAlias(true);
		mPaint.setStyle(Style.STROKE);
		mPaint.setColor(mFirstColor);
		mPaint.setStrokeWidth(lineWidth);
		oval.left = lineWidth;
		oval.top = lineWidth;
		oval.right = 2 * center - lineWidth;
		oval.bottom = 2 * center - lineWidth;
		canvas.drawArc(oval, 144, 252, false, mPaint);

		drawMark(canvas, mMarkCount, center);

		// 画外圆弧(第一圈)
		mPaint.setColor(mSecondColor);
		mPaint.setStrokeWidth(mOutCircleWidth);
		int radius = center - mOutCircleWidth / 2 - mSpace;
		oval.left = center - radius;
		oval.top = center - radius;
		oval.right = center + radius;
		oval.bottom = center + radius;
		canvas.drawArc(oval, 135, 270, false, mPaint);// 135度开始

		// 画外圆弧(第二圈)
		mPaint.setColor(mFirstColor);
		mPaint.setStrokeWidth(mOutCircleWidth);
		oval.left = center - radius;
		oval.top = center - radius;
		oval.right = center + radius;
		oval.bottom = center + radius;
		canvas.drawArc(oval, 135, mProgress, false, mPaint);

		// 画内圈的圆
		mPaint.setColor(mFirstColor);
		mPaint.setStrokeWidth(lineWidth);
		int smallRadius = mOutCircleWidth;
		oval.left = center - smallRadius;
		oval.top = center - smallRadius;
		oval.right = center + smallRadius;
		oval.bottom = center + smallRadius;
		canvas.drawArc(oval, 0, 360, false, mPaint);

		// 画内圈的圆
		mPaint.setColor(mSecondColor);
		mPaint.setStrokeWidth(mInCircleWidth);
		oval.left = center - mSmallSpace;
		oval.top = center - mSmallSpace;
		oval.right = center + mSmallSpace;
		oval.bottom = center + mSmallSpace;
		canvas.drawArc(oval, 0, 360, false, mPaint);

		// 画指针
		canvas.save();
		mPaint.setColor(mSecondColor);
		mPaint.setStrokeWidth(lineWidth);
		canvas.rotate(135 + mProgress, center, center);
		canvas.drawLine(center, center, 2 * center - mSpace, center, mPaint);
		canvas.restore();

		// 画中心点
		mPaint.setColor(mFirstColor);
		mPaint.setStrokeWidth(lineWidth);
		mPaint.setStyle(Style.FILL);
		canvas.drawCircle(center, center, lineWidth, mPaint);

		// 画说明
		int width = mOutCircleWidth;
		int height = mInCircleWidth;
		int r = center - mSpace - mOutCircleWidth;
		oval.left = center - mOutCircleWidth;
		oval.top = center + r - mInCircleWidth;
		oval.right = center + mOutCircleWidth;
		oval.bottom = center + r + mInCircleWidth;
		canvas.drawRect(oval, mPaint);

		// 画文字
		mPaint.setColor(mSecondColor);
		mPaint.setTextSize(mTextSize);
		canvas.drawText("已完成", center - mOutCircleWidth, oval.bottom
				+ mOutCircleWidth, mPaint);
	}

	private void drawMark(Canvas canvas, int count, int center) {
		// TODO Auto-generated method stub
		canvas.drawLine(center, 0, center, mSpace * 3 / 4, mPaint);
		float degree = (float) 252 / mMarkCount;
		// 画右边的刻度
		canvas.save();
		for (int i = 0; i < mMarkCount / 2; i++) {
			canvas.rotate(degree, center, center);
			canvas.drawLine(center, 0, center, mSpace * 3 / 4, mPaint);
		}
		canvas.restore();

		// 画左边的刻度
		canvas.save();
		for (int i = 0; i < mMarkCount / 2; i++) {
			canvas.rotate(-degree, center, center);
			canvas.drawLine(center, 0, center, mSpace * 3 / 4, mPaint);
		}
		canvas.restore();
	}

	public void setProgress(float progress) {
		mProgress = progress*2.7f;
		// postInvalidate();
		invalidate(); // 两者均可以
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		// 该重写没有用到
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		if (widthMode == MeasureSpec.EXACTLY) { // 对应match_parent和确定值
			mWidth = widthSize;
		} else {
			mWidth = 400; // MeasureSpec.AT_MOST对应wrap_content
		}

		if (heightMode == MeasureSpec.EXACTLY) {
			mHeight = heightSize;
		} else {
			mHeight = (int) TypedValue.applyDimension(
					TypedValue.COMPLEX_UNIT_DIP, 250, getResources()
							.getDisplayMetrics());
		}
		setMeasuredDimension(mWidth, mHeight);
	}
}
