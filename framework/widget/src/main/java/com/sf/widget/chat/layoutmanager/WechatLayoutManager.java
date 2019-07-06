package com.sf.widget.chat.layoutmanager;

import android.graphics.Point;

public class WechatLayoutManager implements ILayoutManager {

  @Override
  public Point getPoint(int i, int size, int subSize, int gap, int count) {
    int x = 0;
    int y = 0;

    if (count == 2) {
      x = gap + i * (subSize + gap);
      y = (size - subSize) / 2;
    } else if (count == 3) {
      if (i == 0) {
        x = (size - subSize) / 2;
        y = gap;
      } else {
        x = gap + (i - 1) * (subSize + gap);
        y = subSize + 2 * gap;
      }
    } else if (count == 4) {
      x = gap + (i % 2) * (subSize + gap);
      if (i < 2) {
        y = gap;
      } else {
        y = subSize + 2 * gap;
      }
    } else if (count == 5) {
      if (i == 0) {
        x = y = (size - 2 * subSize - gap) / 2;
      } else if (i == 1) {
        x = (size + gap) / 2;
        y = (size - 2 * subSize - gap) / 2;
      } else if (i > 1) {
        x = gap + (i - 2) * (subSize + gap);
        y = (size + gap) / 2;
      }
    } else if (count == 6) {
      x = gap + (i % 3) * (subSize + gap);
      if (i < 3) {
        y = (size - 2 * subSize - gap) / 2;
      } else {
        y = (size + gap) / 2;
      }
    } else if (count == 7) {
      if (i == 0) {
        x = (size - subSize) / 2;
        y = gap;
      } else if (i < 4) {
        x = gap + (i - 1) * (subSize + gap);
        y = subSize + 2 * gap;
      } else {
        x = gap + (i - 4) * (subSize + gap);
        y = gap + 2 * (subSize + gap);
      }
    } else if (count == 8) {
      if (i == 0) {
        x = (size - 2 * subSize - gap) / 2;
        y = gap;
      } else if (i == 1) {
        x = (size + gap) / 2;
        y = gap;
      } else if (i < 5) {
        x = gap + (i - 2) * (subSize + gap);
        y = subSize + 2 * gap;
      } else {
        x = gap + (i - 5) * (subSize + gap);
        y = gap + 2 * (subSize + gap);
      }
    } else if (count == 9) {
      x = gap + (i % 3) * (subSize + gap);
      if (i < 3) {
        y = gap;
      } else if (i < 6) {
        y = subSize + 2 * gap;
      } else {
        y = gap + 2 * (subSize + gap);
      }
    }

    return new Point(x, y);
  }

  @Override
  public int getSubSize(int size, int gap, int count) {
    int subSize = 0;
    if (count < 2) {
      subSize = size;
    } else if (count < 5) {
      subSize = (size - 3 * gap) / 2;
    } else if (count < 10) {
      subSize = (size - 4 * gap) / 3;
    }
    return subSize;
  }
}
