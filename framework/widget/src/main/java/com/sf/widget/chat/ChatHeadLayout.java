package com.sf.widget.chat;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.sf.utility.CollectionUtil;
import com.sf.utility.DensityUtil;
import com.sf.widget.chat.layoutmanager.ILayoutManager;
import com.sf.widget.chat.layoutmanager.KwaiChatLayoutManager;
import java.util.List;

/**
 * 群聊头像布局
 */
public class ChatHeadLayout extends ViewGroup {

  private final int MAX_COUNT = 9;
  private final int DEFAULT_GAP = 3; // dp
  private int gapWidth;
  private ILayoutManager layoutManager;

  private Path roundPath;
  private RectF rectF;
  private float roundLayoutRadius;

  public ChatHeadLayout(Context context) {
    this(context, null);
  }

  public ChatHeadLayout(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public ChatHeadLayout(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  private void init() {
    gapWidth = DensityUtil.dp2px(getContext(), DEFAULT_GAP);
    layoutManager = new KwaiChatLayoutManager();

    setWillNotDraw(false);
    rectF = new RectF();
    roundPath = new Path();
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
  }

  @Override
  protected void onLayout(boolean changed, int l, int t, int r, int b) {

    int childCount = getChildCount();
    if (layoutManager == null || childCount == 0) {
      return;
    }
    int size = getMeasuredWidth();
    int subSize = layoutManager.getSubSize(size, gapWidth, childCount);

    for (int i = 0; i < childCount; i++) {
      ImageView imageView = (ImageView) getChildAt(i);
      MarginLayoutParams lp = new MarginLayoutParams(subSize, subSize);
      imageView.setLayoutParams(lp);
      Point point = layoutManager.getPoint(i, size, subSize, gapWidth, childCount);
      imageView.layout(point.x, point.y, subSize + point.x, subSize + point.y);
    }

    rectF.set(0f, 0f, size, size);
    setRoundPath();
  }

  @Override
  protected void onDraw(Canvas canvas) {
    if (roundLayoutRadius > 0) {
      canvas.clipPath(roundPath);
    }
    super.onDraw(canvas);
  }

  /**
   * 决定头像的布局方式
   */
  public void setLayoutManager(ILayoutManager layoutManager) {
    this.layoutManager = layoutManager;
  }

  /**
   * 头像之间的间隔，dp
   */
  public void setGapWidth(int gap) {
    this.gapWidth = DensityUtil.dp2px(getContext(), gap);
  }


  /**
   * 设置圆角，dp
   */
  public void setRoundLayoutRadius(float roundLayoutRadius) {
    this.roundLayoutRadius = DensityUtil.dp2px(getContext(), roundLayoutRadius);
    setRoundPath();
    postInvalidate();
  }

  /**
   * 头像资源
   */
  public void setResIds(List<Integer> resIds) {
    if (CollectionUtil.isEmpty(resIds)) {
      return;
    }

    removeAllViews();
    for (int i = 0; i < resIds.size(); i++) {
      if (i < MAX_COUNT) {
        ImageView imageView = new ImageView(getContext());
        imageView.setImageResource(resIds.get(i));
        addView(imageView);
      } else {
        break;
      }
    }
  }

  private void setRoundPath() {
    // 添加一个圆角矩形到path中, 如果要实现任意形状的View, 只需要手动添加path就行
    roundPath.addRoundRect(rectF, roundLayoutRadius, roundLayoutRadius, Path.Direction.CW);
  }

  /**
   * 用于生成和此容器相匹配的布局参数
   */
  @Override
  public LayoutParams generateLayoutParams(AttributeSet attrs) {
    return new MarginLayoutParams(getContext(), attrs);
  }
}
