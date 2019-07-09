package com.sf.widget.swipe;

import android.content.Context;
import android.view.ViewConfiguration;

/**
 * 评估一次swipe：
 * 1.任何时刻速度大于阈值
 * 2.方向变化比较平滑且没有急转、反复
 * 3.主方向(x)幅度大于副方向(y)
 */
public class SwipeEvaluator {
  private float px, py;
  private float startPx, startPy;
  private float lastX = -1, lastY = -1;
  private float firstX = -1, firstY = -1;
  private long firstTime;
  private long lastTime;
  private boolean isInited = false;
  private boolean isInvalid = false;

  private float velocityThres = 300;
  private float accelerateDuration = ViewConfiguration.getTapTimeout();
  private float touchSlop = 10;

  /**
   * @param thres pixels per second
   */
  public SwipeEvaluator(Context context,float thres) {
    velocityThres = thres / 1000f;
    touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
  }

  public void sample(float x, float y, long time) {
    if (time == lastTime) { // 过滤同一个event
      return;
    }
    if (time - lastTime < 10 && x - lastX == 0 && y - lastY == 0) { // 过滤采样过快的情况
      lastTime = time;
      return;
    }
    if (isInvalid) {
      return;
    }
    if (!isInited) {
      if (lastX == -1 && lastY == -1) {
        lastX = x;
        lastY = y;
        lastTime = time;
      } else if (Math.hypot(x - lastX, y - lastY) > touchSlop) {
        px = (x - lastX) / (time - lastTime) / velocityThres;
        py = (y - lastY) / (time - lastTime) / velocityThres;
        lastTime = time;
        lastX = x;
        lastY = y;
        isInited = true;
        firstTime = time;
        firstX = x;
        firstY = y;
        startPx = px;
        startPy = py;
      }
    } else {
      float npx = (x - lastX) / (time - lastTime) / velocityThres;
      float npy = (y - lastY) / (time - lastTime) / velocityThres;
      float v = npx * px + npy * py;
      if (v <= 0 && npx != 0) { // 检查实时方向偏移是否小于90度
        isInvalid = true;
        return;
      }
      px = (px + npx) / 2;
      py = (py + npy) / 2;
      lastTime = time;
      lastX = x;
      lastY = y;

      float factor =
          accelerateDuration == 0 ? 1 : Math.min(1f, (time - firstTime) / accelerateDuration);

      if (px > 0) {
        // 检查右划实时速度是否大于阈值
        isInvalid = (x - firstX) <= velocityThres * (time - firstTime) * factor * factor;
      } else if (px < 0) {
        // 检查左划实时速度是否大于阈值
        isInvalid = (x - firstX) >= -velocityThres * (time - firstTime) * factor * factor;
      } else {
        // 初始一段时间没有移动
        isInvalid = true;
        return;
      }
      // 检查Y方向幅度是否小于X方向幅度
      if (Math.abs(y - firstY) > velocityThres * (time - firstTime)
          && Math.abs(y - firstY) > Math.abs(x - firstX)) {
        isInvalid = true;
      }
      // 检查相对初始方向偏移是否小于60度
      if ((px * startPx + py * startPy) * (px * startPx + py * startPy) * 2f < (px * px + py * py)
          * (startPx * startPx + startPy * startPy)) {
        isInvalid = true;
      }
    }
  }

  public boolean isValidSwipe() {
    return isInited && !isInvalid
        && Math.abs(px) > 1;
  }

  public void reset() {
    px = py = 0;
    lastX = lastY = -1;
    isInited = false;
    isInvalid = false;
    lastTime = 0;
    firstX = firstY = -1;
    firstTime = 0;
    startPx = 0;
    startPy = 0;
  }
}
