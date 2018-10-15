package com.sf.demo.list.sort;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;

public class SideBarView extends View {
	private Paint mPaint;
	private int mWidth, mHeight;
	private String[] c = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
			"K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W",
			"X", "Y", "Z", "#" };

	private int textSize;
	private int index;
	private int choose = -1;
	private boolean mFlag;
	private onTouchChangeListener mListener;
	private onTouchListener nListener;
	private VelocityTracker mTracker;
	private GestureDetector mGestureDetector;

	public SideBarView(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}

	public SideBarView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub
	}

	public SideBarView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
		textSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
				14, getResources().getDisplayMetrics());
		mPaint = new Paint();
		mPaint.setTextSize(textSize);
		mPaint.setColor(Color.BLACK);
		mPaint.setAntiAlias(true);
		mTracker = VelocityTracker.obtain();
		
		
	//	OnDoubleTapListener
		
		mGestureDetector=new GestureDetector(context, new OnGestureListener() {
			
			@Override
			public boolean onSingleTapUp(MotionEvent arg0) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public void onShowPress(MotionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
					float arg3) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public void onLongPress(MotionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public boolean onFling(MotionEvent arg0, MotionEvent arg1, float arg2,
					float arg3) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean onDown(MotionEvent arg0) {
				// TODO Auto-generated method stub
				return false;
			}
		});
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		mWidth = getWidth();
		mHeight = getHeight();
		if (mFlag) {
			canvas.drawColor(Color.parseColor("#E5E5E5"));
		}
		float strHeight = (float) (mHeight / c.length);
		for (int i = 0; i < c.length; i++) {
			float x = mWidth / 2 - mPaint.measureText(c[i]) / 2;
			float y = strHeight * i + strHeight;
			if (choose == i) {
				mPaint.setColor(Color.parseColor("#00ffff"));
			} else {
				mPaint.setColor(Color.BLACK);
			}
			canvas.drawText(c[i], x, y, mPaint);
		}
		invalidate();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		mGestureDetector.onTouchEvent(event);
		mTracker.addMovement(event);
		mTracker.computeCurrentVelocity(2000);
		
		float y = event.getY();
		index = (int) (y / mHeight * c.length);
		if (index <= 0)
			index = 0;
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			nListener.setVisibility(View.VISIBLE);
			mListener.setLetter(c[index]);
			choose = index;
			mFlag = true;
			break;
		case MotionEvent.ACTION_MOVE:
			mListener.setLetter(c[index]);
			choose = index;
			mFlag = true;
			break;
		case MotionEvent.ACTION_UP:
			nListener.setVisibility(View.GONE);
			choose = -1;
			mFlag = false;
			int speedX = (int) mTracker.getYVelocity();
		//	Log.i("TAG", speedX + "");
			break;
		}
		return true;
	}

	public void setonTouchChangeListener(onTouchChangeListener listener) {
		mListener = listener;
	}

	public void setonTouchListener(onTouchListener listener) {
		nListener = listener;
	}

	public interface onTouchChangeListener {
		public void setLetter(String letter);
	}

	public interface onTouchListener {
		public void setVisibility(int visibility);
	}

}
