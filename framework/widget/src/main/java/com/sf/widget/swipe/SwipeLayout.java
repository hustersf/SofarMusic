package com.sf.widget.swipe;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.sf.utility.DeviceUtil;
import com.sf.utility.ViewUtil;
import com.sf.widget.R;


/**
 * 为页面加了一个滑动的监听，目前支持左右,且一次只能选择一个方向可以扩展上下方向
 * 可以扩展上下两个方向，并且同时支持多个方向
 * 用法，自定义一个帮助类，将该布局加到
 */

@SuppressWarnings("deprecation")
public class SwipeLayout extends FrameLayout {

    public static final String DIRECTION_LEFT = "left";
    public static final String DIRECTION_RIGHT = "right";

    private static final float MAX_SWIPE_DISTANCE_FACTOR = .2f;
    private static final int INVALID_POINTER = -1;

    private static final int DRAG_STATE_NONE = 0;// 未在拖动
    private static final int DRAGGING_STATE_HORIZONTAL_RIGHT = 1;// 水平拖动中
    private static final int DRAGGING_STATE_HORIZONTAL_LEFT = 2;// 水平拖动中
    private static final int DRAGGING_STATE_VERTICAL = 3;// 垂直拖动中

    private int mSwipeTriggerDistance = 50;
    private View mTarget;
    private float mEdgeSlop;
    private int mScreenWidth;
    private float mDistanceToTriggerSync = -1;
    private float mInitialMotionX;
    private float mInitialMotionY;
    private int mTempMotionY;
    private int mActivePointerId = INVALID_POINTER;
    private OnSwipedListener mListener;
    private List<View> mIgnoreViews = new ArrayList<>();
    private Direction mDirection; // 滑动方向
    private boolean mOnlyFromEdgeEnable; // 是否要求从屏幕边缘滑动(只能从个边缘滑动)
    private boolean mIgnoreEdge = true;// 是否忽悠边缘(忽略的话即不关心是否从边缘开始滑动)
    private boolean mFromLeftEdge;
    private boolean mFromRightEdge;
    private boolean mAdjustChildScrollHorizontally = true;
    private boolean mRestrictDirection;
    private int mDragState = DRAG_STATE_NONE;

    private FrameLayout mContentView;

    private SwipeHandler mSwipeHandler; // 跟手滑动

    private SwipeEvaluator mSwipeEvaluator;

    public SwipeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SwipeLayout, defStyle, 0);
        String direction = a.getString(R.styleable.SwipeLayout_direction);
        if (DIRECTION_LEFT.equals(direction)) {
            mDirection = Direction.LEFT;
        } else if (DIRECTION_RIGHT.equals(direction)) {
            mDirection = Direction.RIGHT;
        } else { // 默认是Right
            mDirection = Direction.RIGHT;
        }
        mOnlyFromEdgeEnable = a.getBoolean(R.styleable.SwipeLayout_fromEdge, false);
        a.recycle();

        initialize();
    }

    public SwipeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeLayout(Context context) {
        this(context, null, 0);
    }

    private void initialize() {
        mEdgeSlop = ViewUtil.getEdgeSlop(getContext());
        mScreenWidth = DeviceUtil.getMetricsWidth(getContext());
        mSwipeEvaluator = new SwipeEvaluator(getContext(),mScreenWidth / 2);
        setWillNotDraw(true);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        try {
            return super.dispatchTouchEvent(ev);
        } catch (NullPointerException e) {
            e.printStackTrace();
            // https://bugly.qq.com/v2/crash-reporting/crashes/900014602/165342?pid=1
            return true;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        ensureTarget();

        if (mSwipeHandler != null && mSwipeHandler.isTouchEnabled(getClass()) ) {
            return mSwipeHandler.onInterceptTouchEvent(ev);
        }

        final int action = MotionEventCompat.getActionMasked(ev);

        if (!isEnabled() || isInIgnoreArea(ev)) {
            // Fail fast if we're not in a state where a swipe is possible
            return false;
        }

        if (action == MotionEvent.ACTION_DOWN) {
            mSwipeEvaluator.reset();
        }
        mSwipeEvaluator.sample(ev.getX(), ev.getY(), ev.getEventTime());

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mInitialMotionX = ev.getX();
                mInitialMotionY = ev.getY();
                mTempMotionY = (int) mInitialMotionY;
                mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
                mFromLeftEdge = mInitialMotionX <= mEdgeSlop;
                mFromRightEdge = mInitialMotionX >= mScreenWidth - mEdgeSlop;
                mDragState = DRAG_STATE_NONE;
                break;

            case MotionEvent.ACTION_MOVE:
                if (mActivePointerId == INVALID_POINTER) {
                    return false;
                }

                final int pointerIndex = MotionEventCompat.findPointerIndex(ev, mActivePointerId);
                if (pointerIndex < 0) {
                    return false;
                }
                if (mOnlyFromEdgeEnable) {
                    if (mDirection == Direction.RIGHT && mInitialMotionX > mEdgeSlop) {
                        return false;
                    } else if (mDirection == Direction.LEFT && mInitialMotionX < mScreenWidth - mEdgeSlop) {
                        return false;
                    }
                }

                final float x = MotionEventCompat.getX(ev, pointerIndex);
                final float y = MotionEventCompat.getY(ev, pointerIndex);
                final float xDiff = x - mInitialMotionX;
                final float yDiff = y - mInitialMotionY;

                mDragState = computeBeingDragged(xDiff, yDiff, ev);

                if (mDragState == DRAGGING_STATE_VERTICAL) {
                    // 未移动过，往上拉，那么不处理事件
                    if (mContentView.getScrollY() >= 0 && yDiff < 0) {
                        mDragState = DRAG_STATE_NONE;
                    }
                }

                break;

            case MotionEventCompat.ACTION_POINTER_UP:
                onSecondaryPointerUp(ev);
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mDragState = DRAG_STATE_NONE;
                mActivePointerId = INVALID_POINTER;
                mFromLeftEdge = false;
                mFromRightEdge = false;
                mSwipeEvaluator.reset();
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                break;
            default:
                break;
        }

        return mDragState != DRAG_STATE_NONE;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        final int action = MotionEventCompat.getActionMasked(ev);

        if (mSwipeHandler != null && mSwipeHandler.isTouchEnabled(getClass())
                && mSwipeHandler.onTouchEvent(ev)) {
            return true;
        }

        if (!isEnabled() || isInIgnoreArea(ev)) {
            // Fail fast if we're not in a state where a swipe is possible
            return false;
        }

        if (action == MotionEvent.ACTION_DOWN) {
            mSwipeEvaluator.reset();
        }
        mSwipeEvaluator.sample(ev.getX(), ev.getY(), ev.getEventTime());

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mInitialMotionX = ev.getX();
                mInitialMotionY = ev.getY();
                mTempMotionY = (int) mInitialMotionY;
                mFromLeftEdge = mInitialMotionX <= mEdgeSlop;
                mFromRightEdge = mInitialMotionX >= mScreenWidth - mEdgeSlop;
                mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
                mDragState = DRAG_STATE_NONE;
                break;

            case MotionEvent.ACTION_MOVE:
                if (mActivePointerId == INVALID_POINTER) {
                    return false;
                }

                final int pointerIndex = MotionEventCompat.findPointerIndex(ev, mActivePointerId);
                if (pointerIndex < 0) {
                    return false;
                }

                if (mOnlyFromEdgeEnable) {
                    if (mDirection == Direction.RIGHT && mInitialMotionX > mEdgeSlop) {
                        return false;
                    } else if (mDirection == Direction.LEFT && mInitialMotionX < mScreenWidth - mEdgeSlop) {
                        return false;
                    }
                }

                final float x = MotionEventCompat.getX(ev, pointerIndex);
                final float y = MotionEventCompat.getY(ev, pointerIndex);

                final float xDiff = x - mInitialMotionX;
                final float yDiff = y - mInitialMotionY;
                int deltaY = mTempMotionY - (int) y;
                mTempMotionY = (int) y;

                if (mDragState == DRAG_STATE_NONE)// 如果已经判定了滑动态，那么一直遵循(防止既能竖滑，滑动中又执行横滑)
                    mDragState = computeBeingDragged(xDiff, yDiff, ev);

                if (mDragState == DRAGGING_STATE_VERTICAL) {
                    // 向下滑动
                    if (mContentView.getScrollY() + deltaY > 0) {
                        deltaY = -mContentView.getScrollY();
                    }
                    mContentView.scrollBy(0, deltaY);
                }
                break;

            case MotionEventCompat.ACTION_POINTER_DOWN: {
                final int index = MotionEventCompat.getActionIndex(ev);
                mActivePointerId = MotionEventCompat.getPointerId(ev, index);
                break;
            }

            case MotionEventCompat.ACTION_POINTER_UP:
                onSecondaryPointerUp(ev);
                break;

            case MotionEvent.ACTION_UP:
                if (mDragState == DRAGGING_STATE_HORIZONTAL_LEFT
                        || mDragState == DRAGGING_STATE_HORIZONTAL_RIGHT) {
                    // User velocity passed min velocity; trigger a swipe
                    if (!mRestrictDirection || mSwipeEvaluator.isValidSwipe()) {
                        swipe();
                    }
                    mDragState = DRAG_STATE_NONE;
                    mActivePointerId = INVALID_POINTER;
                }
            case MotionEvent.ACTION_CANCEL:
                mDragState = DRAG_STATE_NONE;
                mActivePointerId = INVALID_POINTER;
                mFromLeftEdge = false;
                mFromRightEdge = false;
                mSwipeEvaluator.reset();
                return false;
        }

        return true;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mSwipeHandler != null) {
            mSwipeHandler.handlerComputeScroll();
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (mSwipeHandler != null) {
            mSwipeHandler.handlerDrawShadow(canvas);
        }
    }

    public void setSwipeHandler(SwipeHandler swipeHandler) {
        mSwipeHandler = swipeHandler;
    }

    public void setDirection(Direction direction) {
        mDirection = direction;
    }

    public void setFromEdge(boolean fromEdge) {
        mOnlyFromEdgeEnable = fromEdge;
    }

    public void setIgnoreEdge(boolean ignoreEdge) {
        mIgnoreEdge = ignoreEdge;
    }

    public void setRestrictDirection(boolean restrict) {
        mRestrictDirection = restrict;
    }

    public Direction getDirection() {
        return mDirection;
    }

    /**
     * Set the listener to be notified when a swipe-left is triggered via the swipe gesture.
     */
    public void setOnSwipedListener(OnSwipedListener listener) {
        mListener = listener;
    }

    public void addIgnoreView(View view) {
        mIgnoreViews.add(view);
    }

    public void removeIgnoreView(View view) {
        mIgnoreViews.remove(view);
    }

    public void clearIgnoreViews() {
        mIgnoreViews.clear();
    }

    public void setAdjustChildScrollHorizontally(boolean adjustChildScrollHorizontally) {
        mAdjustChildScrollHorizontally = adjustChildScrollHorizontally;
    }

    private void ensureTarget() {
        // Don't bother getting the parent width if the parent hasn't been laid out yet.
        if (mTarget == null) {
            if (getChildCount() > 1 && !isInEditMode()) {
                throw new IllegalStateException("SwipeLayout can host only one direct child");
            }
            mTarget = getChildAt(0);
        }
        if (mDistanceToTriggerSync == -1) {
            if (getParent() != null && ((View) getParent()).getWidth() > 0) {
                final DisplayMetrics metrics = getResources().getDisplayMetrics();
                mDistanceToTriggerSync =
                        (int) Math.min(((View) getParent()).getWidth() * MAX_SWIPE_DISTANCE_FACTOR,
                                mSwipeTriggerDistance * metrics.density);
            }
        }

        // 获取activity content
        if (mContentView == null && getParent() != null) {
            mContentView = (FrameLayout) ((ViewGroup) getParent()).findViewById(android.R.id.content);
        }
    }

    private boolean canChildScrollHorizontal(MotionEvent ev) {
        // RIGHT, 指的是从左向右滑动
        if (!mAdjustChildScrollHorizontally) {
            return false;
        }
        int dx = mDirection == Direction.RIGHT ? -1 : 0;
        return ViewUtil.canChildScrollHorizontally(mTarget, dx, (int) ev.getX(), (int) ev.getY());
    }

    private void onSecondaryPointerUp(MotionEvent ev) {
        final int pointerIndex = MotionEventCompat.getActionIndex(ev);
        final int pointerId = MotionEventCompat.getPointerId(ev, pointerIndex);
        if (pointerId == mActivePointerId) {
            // This was our active pointer going up. Choose a new
            // active pointer and adjust accordingly.
            final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
            mActivePointerId = MotionEventCompat.getPointerId(ev, newPointerIndex);
        }
    }

    private int computeBeingDragged(float xDiff, float yDiff, MotionEvent ev) {
        float absXDiff = Math.abs(xDiff);
        float absYDiff = Math.abs(yDiff);

        if (mListener == null || canChildScrollHorizontal(ev)) {
            return DRAG_STATE_NONE;
        }

        // 向右滑动
        if ((mDirection == Direction.RIGHT || mDirection == Direction.BOTH) && xDiff > 0) {
            if (absXDiff > mDistanceToTriggerSync && absYDiff * 1 < absXDiff) {
                return DRAGGING_STATE_HORIZONTAL_RIGHT;
            }
        }
        // 向左滑动
        if ((mDirection == Direction.LEFT || mDirection == Direction.BOTH) && xDiff < 0) {
            if (Math.abs(xDiff) > mDistanceToTriggerSync && absYDiff * 1 < absXDiff) {
                return DRAGGING_STATE_HORIZONTAL_LEFT;
            }
        }

        return DRAG_STATE_NONE;
    }

    // add by liuwei. 长图滑动太灵敏，需要调整滑动最小距离
    public void setSwipeTriggerDistance(int newDistance) {
        mSwipeTriggerDistance = newDistance;
        if (mDistanceToTriggerSync > 0) {
            if (getParent() != null && ((View) getParent()).getWidth() > 0) {
                final DisplayMetrics metrics = getResources().getDisplayMetrics();
                mDistanceToTriggerSync =
                        (int) Math.min(((View) getParent()).getWidth() * MAX_SWIPE_DISTANCE_FACTOR,
                                mSwipeTriggerDistance * metrics.density);
            }
        }
    }

    public boolean isInIgnoreArea(MotionEvent event) {
        Rect rect = new Rect();
        for (View view : mIgnoreViews) {
            view.getGlobalVisibleRect(rect);
            if (rect.contains((int) event.getRawX(), (int) event.getRawY())) {
                return true;
            }
        }
        return false;
    }

    private void swipe() {
        OnSwipedListener l = mListener;
        if (l != null) {
            if (mDragState == DRAGGING_STATE_HORIZONTAL_RIGHT) {
                if (mFromLeftEdge && !mIgnoreEdge) {
                    l.onRightSwipedFromEdge();
                } else {
                    l.onRightSwiped();
                }
            } else if (mDragState == DRAGGING_STATE_HORIZONTAL_LEFT) {
                if (mFromRightEdge && !mIgnoreEdge) {
                    l.onLeftSwipedFromEdge();
                } else {
                    l.onLeftSwiped();
                }
            }
        }
    }

    public interface OnSwipedListener {
        void onRightSwiped();

        void onLeftSwiped();

        void onRightSwipedFromEdge();

        void onLeftSwipedFromEdge();
    }

    public abstract static class OnSwipedListenerAdapter implements OnSwipedListener {
        @Override
        public void onRightSwiped() {}

        @Override
        public void onLeftSwiped() {}

        @Override
        public void onRightSwipedFromEdge() {}

        @Override
        public void onLeftSwipedFromEdge() {}
    }

    public enum Direction {
        LEFT,
        RIGHT,
        BOTH
    }
}
