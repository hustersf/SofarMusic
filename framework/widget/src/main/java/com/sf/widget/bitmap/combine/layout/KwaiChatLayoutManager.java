package com.sf.widget.bitmap.combine.layout;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;

/**
 * 与微信群聊头像的唯一区别就是 头像与外侧无间距
 */
public class KwaiChatLayoutManager implements ILayoutManager {

  @Override
  public Bitmap combineBitmap(int size, int subSize, int gap, int gapColor, Bitmap[] bitmaps) {
    Bitmap result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(result);
    if (gapColor == 0) {
      gapColor = Color.WHITE;
    }
    canvas.drawColor(gapColor);

    int count = bitmaps.length;
    Bitmap subBitmap;
    for (int i = 0; i < count; i++) {
      if (bitmaps[i] == null) {
        continue;
      }
      subBitmap = Bitmap.createScaledBitmap(bitmaps[i], subSize, subSize, true);
      float x = 0;
      float y = 0;

      if (count == 2) {
        x = i * (subSize + gap);
        y = (size - subSize) / 2.0f;
      } else if (count == 3) {
        if (i == 0) {
          x = (size - subSize) / 2.0f;
          y = 0;
        } else {
          x = (i - 1) * (subSize + gap);
          y = subSize + gap;
        }
      } else if (count == 4) {
        x = (i % 2) * (subSize + gap);
        if (i < 2) {
          y = 0;
        } else {
          y = subSize + gap;
        }
      } else if (count == 5) {
        if (i == 0) {
          x = y = (size - 2 * subSize - gap) / 2.0f;
        } else if (i == 1) {
          x = (size + gap) / 2.0f;
          y = (size - 2 * subSize - gap) / 2.0f;
        } else if (i > 1) {
          x = (i - 2) * (subSize + gap);
          y = (size + gap) / 2.0f;
        }
      } else if (count == 6) {
        x = (i % 3) * (subSize + gap);
        if (i < 3) {
          y = (size - 2 * subSize - gap) / 2.0f;
        } else {
          y = (size + gap) / 2.0f;
        }
      } else if (count == 7) {
        if (i == 0) {
          x = (size - subSize) / 2.0f;
          y = 0;
        } else if (i < 4) {
          x = (i - 1) * (subSize + gap);
          y = subSize + gap;
        } else {
          x = (i - 4) * (subSize + gap);
          y = 2 * (subSize + gap);
        }
      } else if (count == 8) {
        if (i == 0) {
          x = (size - 2 * subSize - gap) / 2.0f;
          y = 0;
        } else if (i == 1) {
          x = (size + gap) / 2.0f;
          y = 0;
        } else if (i < 5) {
          x = (i - 2) * (subSize + gap);
          y = subSize + gap;
        } else {
          x = (i - 5) * (subSize + gap);
          y = 2 * (subSize + gap);
        }
      } else if (count == 9) {
        x = (i % 3) * (subSize + gap);
        if (i < 3) {
          y = 0;
        } else if (i < 6) {
          y = subSize + gap;
        } else {
          y = 2 * (subSize + gap);
        }
      }

      canvas.drawBitmap(subBitmap, x, y, null);
    }
    return result;
  }

  @Override
  public int getSubSize(int size, int gap, ILayoutManager layoutManager, int count) {
    int subSize = 0;
    if (count < 2) {
      subSize = size;
    } else if (count < 5) {
      subSize = (size - gap) / 2;
    } else if (count < 10) {
      subSize = (size - 2 * gap) / 3;
    }
    return subSize;
  }
}
