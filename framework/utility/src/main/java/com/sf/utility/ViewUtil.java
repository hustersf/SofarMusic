package com.sf.utility;


import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import androidx.core.view.ViewCompat;
import androidx.viewpager.widget.ViewPager;

public class ViewUtil {

  private static final float LARGE_WIDTH_DEVICE_SCALE = 0.15f;
  private static final float DEFAULT_SCALE = 0.07f;
  private static final int LARGE_SCREEN_DP = 480;

  /**
   * 获取边缘滚动距离
   * 用户是否触发了边缘滚动操作的临界值
   */
  public static float getEdgeSlop(Context context) {
    ViewConfiguration config = ViewConfiguration.get(context);
    float edgeSlop = config.getScaledEdgeSlop();

    WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    if (wm != null) {
      DisplayMetrics displayMetrics = new DisplayMetrics();
      wm.getDefaultDisplay().getMetrics(displayMetrics);
      int screenWidth = displayMetrics.widthPixels;
      float density = context.getResources().getDisplayMetrics().density;
      float deviceDp = (float) Math.floor(screenWidth / density);
      float scale = (edgeSlop * 1.0f / (screenWidth * 1.0f));
      if (deviceDp >= LARGE_SCREEN_DP) {
        if (scale < LARGE_WIDTH_DEVICE_SCALE) {
          edgeSlop = screenWidth * LARGE_WIDTH_DEVICE_SCALE;
        }
      } else {
        if (scale < DEFAULT_SCALE) {
          edgeSlop = screenWidth * DEFAULT_SCALE;
        }
      }
    }
    return edgeSlop;
  }

  /**
   * 扩大View的触摸和点击响应范围,最大不超过其父View范围
   *
   * @param view
   * @param top
   * @param bottom
   * @param left
   * @param right
   */
  public static void expandViewTouchDelegate(final View view, final int top,
    final int bottom, final int left, final int right) {
    ((View) view.getParent()).post(new Runnable() {
      @Override
      public void run() {
        Rect bounds = new Rect();
        view.getHitRect(bounds);
        bounds.top -= top;
        bounds.bottom += bottom;
        bounds.left -= left;
        bounds.right += right;
        TouchDelegate touchDelegate = new TouchDelegate(bounds, view);
        if (View.class.isInstance(view.getParent())) {
          ((View) view.getParent()).setTouchDelegate(touchDelegate);
        }
      }
    });
  }

  /**
   * 创建View
   */
  public static <T extends View> T inflate(ViewGroup parent, int resId, boolean attachToRoot) {
    View view = LayoutInflater.from(parent.getContext()).inflate(resId, parent, attachToRoot);
    return (T) view;
  }

  /**
   * 创建View
   */
  public static <T extends View> T inflate(ViewGroup parent, int resId) {
    return inflate(parent, resId, false);
  }

  /**
   * 创建View
   */
  public static <T extends View> T inflate(Context context, int resId) {
    return (T) LayoutInflater.from(context).inflate(resId, null);
  }

  /**
   * 测量文字的宽度
   */
  public static float measureTextWidth(String text, float textSize) {
    Paint paint = new Paint();
    paint.setTextSize(textSize);
    return paint.measureText(text);
  }

  public static boolean canChildScrollHorizontally(View v, int dx, int x, int y) {
    if (v instanceof ViewGroup) {
      final ViewGroup group = (ViewGroup) v;

      final int count = group.getChildCount();
      // Count backwards - let topmost views consume scroll distance first.
      for (int i = count - 1; i >= 0; i--) {
        final View child = group.getChildAt(i);

        final int childLeft = child.getLeft() + (int) ViewCompat.getTranslationX(child);
        final int childRight = child.getRight() + (int) ViewCompat.getTranslationX(child);
        final int childTop = child.getTop() + (int) ViewCompat.getTranslationY(child);
        final int childBottom = child.getBottom() + (int) ViewCompat.getTranslationY(child);

        if (x >= childLeft && x < childRight && y >= childTop && y < childBottom
          && canChildScrollHorizontally(child, dx, x - childLeft, y - childTop)) {
          return true;
        }
      }
    }
    // ViewPager做特殊处理
    if (v instanceof CanScrollInterface) {
      View view = ((CanScrollInterface) v).getCanScrollView();
      if (view != null) {
        if (view instanceof ViewPager) {
          int currentPosition = ((ViewPager) view).getCurrentItem();
          return currentPosition != 0 || dx > 0;
        } else {
          return ViewCompat.canScrollHorizontally(view, dx);
        }
      }
      return false;
    } else if (v instanceof ViewPager) {
      int currentPosition = ((ViewPager) v).getCurrentItem();
      return currentPosition != 0 || dx > 0;
    } else {
      return ViewCompat.canScrollHorizontally(v, dx);
    }
  }

  public interface CanScrollInterface {
    View getCanScrollView();
  }

}
