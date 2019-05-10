package com.sf.widget.danmu;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.sf.utility.CollectionUtil;
import com.sf.utility.DensityUtil;
import com.sf.utility.LogUtil;

import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * 弹幕View
 */
public class DanmuView extends View {

  private static final String TAG = "DanmuView";

  private int maxRow = 3; // 最多几条弹道
  private int hSpace = 40; // 水平方向两个弹幕之间的距离,dp
  private int vSpace = 15; // 竖直方向两个弹幕之间的距离,dp
  private int itemHeight; // 每个item的高度,dp

  private int pickItemInterval = 1000;// 每隔多长时间取出一条弹幕来播放.
  private long previousTime = 0;

  // 弹幕播放列表，每条弹道对应一个弹幕列表
  private HashMap<Integer, ArrayList<IDanmuItem>> channelMap;
  private int[] channelY; // 每条弹道Y轴的偏移量
  private Deque<IDanmuItem> waitingItems = new LinkedList<>(); // 弹幕列表
  private int currentChannel = -1; // 当前弹道(0~maxRow)

  // 状态控制
  private static final int STATUS_RUNNING = 1;
  private static final int STATUS_PAUSE = 2;
  private static final int STATUS_STOP = 3;
  private int status = STATUS_STOP;

  public DanmuView(Context context) {
    this(context, null);
  }

  public DanmuView(Context context, @Nullable AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public DanmuView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  private void init() {
    hSpace = DensityUtil.dp2px(getContext(), hSpace);
    vSpace = DensityUtil.dp2px(getContext(), vSpace);
    initChannelMap();
  }

  private void initChannelMap() {
    channelMap = new HashMap<>();
    for (int i = 0; i < maxRow; i++) {
      ArrayList<IDanmuItem> runningRow = new ArrayList<>();
      channelMap.put(i, runningRow);
    }
  }

  private void initChannelY() {
    if (channelY == null) {
      channelY = new int[maxRow];
    }

    for (int i = 0; i < maxRow; i++) {
      if (i == 0) {
        channelY[i] = getPaddingTop();
      } else {
        channelY[i] = channelY[i - 1] + itemHeight + vSpace;
      }
    }
  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    if (status == STATUS_RUNNING) {
      try {

        canvas.drawColor(Color.TRANSPARENT);

        // 绘制正在播放的弹幕
        for (int i = 0; i < channelMap.size(); i++) {
          ArrayList<IDanmuItem> danmuItems = channelMap.get(i);
          // for (IDanmuItem item : danmuItems) {
          // if (item.isOut()) {
          // danmuItems.remove(item);
          // } else {
          // item.doDraw(canvas);
          // }
          // }
          for (Iterator<IDanmuItem> it = danmuItems.iterator(); it.hasNext();) {
            IDanmuItem item = it.next();
            if (item.isOut()) {
              it.remove();
            } else {
              item.doDraw(canvas);
            }
          }
        }

        // 检查是否播放下一个弹幕
        if (System.currentTimeMillis() - previousTime > pickItemInterval) {
          previousTime = System.currentTimeMillis();
          IDanmuItem item = waitingItems.pollFirst();
          if (item != null) {
            int channelIndex = findChannel(item);
            if (channelIndex >= 0) {
              item.setStartPosition(getStartX(canvas, channelIndex), channelY[channelIndex]);
              item.doDraw(canvas);
              // 加入弹幕播放列表
              channelMap.get(channelIndex).add(item);
            } else {
              // 重新回归弹道列表
              addItemToHead(item);
            }
          } else {
            // no item 弹道播放完毕
          }
        }
      } catch (Exception e) {
        LogUtil.d(TAG, "e:" + e.toString());
      }
      invalidate();
    } else {
      canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
    }
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    int width = MeasureSpec.getSize(widthMeasureSpec);
    int height = measureHeight(heightMeasureSpec);
    setMeasuredDimension(width, height);

    initChannelY();
  }

  private int measureHeight(int heightMeasureSpec) {

    int result = 0;
    int mode = MeasureSpec.getMode(heightMeasureSpec);
    int size = MeasureSpec.getSize(heightMeasureSpec);


    if (mode == MeasureSpec.EXACTLY) {
      result = size;
    } else {
      int h = getPaddingTop() + getPaddingBottom() + maxRow * itemHeight + (maxRow - 1) * vSpace;
      result = h;
      if (mode == MeasureSpec.AT_MOST) {
        result = Math.min(size, h);
      }
    }
    return result;
  }

  public void addItemToHead(IDanmuItem item) {
    if (item == null) {
      return;
    }
    waitingItems.offerFirst(item);
    measureView(item);
  }

  public void addItem(IDanmuItem item) {
    if (item == null) {
      return;
    }
    waitingItems.add(item);
    measureView(item);
  }

  public void addItemList(List<IDanmuItem> items) {
    if (CollectionUtil.isEmpty(items)) {
      return;
    }
    waitingItems.addAll(items);
    measureView(items.get(0));
  }

  private void measureView(IDanmuItem item) {
    if (itemHeight != item.getHeight()) {
      itemHeight = item.getHeight();
      requestLayout();
    }
  }

  /**
   * 播放显示弹幕
   * loop 表示是否循环播放
   */
  public void show() {
    status = STATUS_RUNNING;
    invalidate();
  }

  /**
   * 隐藏弹幕
   */
  public void hide() {
    status = STATUS_PAUSE;
    invalidate();
  }


  /**
   * 清空弹幕
   */
  public void clear() {
    status = STATUS_STOP;
    clearItems();
    invalidate();
  }

  private void clearItems() {
    if (!CollectionUtil.isEmpty(channelMap)) {
      channelMap.clear();
    }
    if (CollectionUtil.isEmpty(waitingItems)) {
      waitingItems.clear();
    }
  }


  /**
   * 寻找当前弹幕所在的弹道
   * 后续可以改成随机弹道
   */
  private int findChannel(IDanmuItem item) {
    currentChannel++;
    currentChannel = currentChannel % maxRow;
    return currentChannel;
  }

  /**
   * 保证同一弹道的两条弹幕之间的间距为hSpace
   */
  private int getStartX(Canvas canvas, int row) {
    // 获取当前弹道的弹幕
    int extraX = 0;
    ArrayList<IDanmuItem> runningItems = channelMap.get(row);
    if (!CollectionUtil.isEmpty(runningItems)) {
      IDanmuItem lastItem = runningItems.get(runningItems.size() - 1);
      extraX = lastItem.getCurrX() + lastItem.getWidth() - canvas.getWidth() + hSpace;
      LogUtil.d(TAG, "lastCurrx:" + lastItem.getCurrX() + " lastWidth:" + lastItem.getWidth()
          + "  width:" + canvas.getWidth());
    }

    if (extraX < 0) {
      extraX = 0;
    }
    return canvas.getWidth() + extraX;
  }

}
