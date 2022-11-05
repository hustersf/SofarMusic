package com.sf.demo.widget;

import android.content.Context;
import android.util.AttributeSet;
import androidx.recyclerview.widget.RecyclerView;


/**
 * 可以判断滑动到底部的RecyclerView
 */
public class BottomRecyclerView extends RecyclerView {
	RecyclerView rv;

	public BottomRecyclerView(Context context) {
		this(context, null);

	}

	public BottomRecyclerView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);

	}

	public BottomRecyclerView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onScrolled(int dx, int dy) {
		super.onScrolled(dx, dy);
		if (isSlideToBottom()) {
			mOnBottomCallback.onBottom();
		}
	}

	public boolean isSlideToBottom() {
		return this != null
				&& this.computeVerticalScrollExtent()
						+ this.computeVerticalScrollOffset() >= this
							.computeVerticalScrollRange();						
	}


	private OnBottomCallback mOnBottomCallback;

	public interface OnBottomCallback{
		void onBottom();
	}

	public void setOnBottomCallback(OnBottomCallback onBottomCallback){
		this.mOnBottomCallback = onBottomCallback;
	}

}
