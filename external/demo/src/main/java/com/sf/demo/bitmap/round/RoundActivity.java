package com.sf.demo.bitmap.round;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import com.sf.base.UIRootActivity;
import com.sf.demo.R;
import com.sf.utility.DensityUtil;
import com.sf.widget.bitmap.combine.CombineBitmap;
import com.sf.widget.bitmap.combine.layout.DingLayoutManager;
import com.sf.widget.bitmap.round.RoundImageView;

public class RoundActivity extends UIRootActivity {

  private RoundImageView image1;
  private RoundImageView image2;
  private RoundImageView image3;
  private RoundImageView image4;

  @Override
  protected int getLayoutId() {
    return R.layout.activity_bitmap_round;
  }

  @Override
  protected void initTitle() {
    mHeadTitleTv.setText("圆角图片");
  }

  @Override
  protected void initView() {
    image1 = findViewById(R.id.image1);
    image2 = findViewById(R.id.image2);
    image3 = findViewById(R.id.image3);
    image4 = findViewById(R.id.image4);
  }

  @Override
  protected void initData() {
    CombineBitmap.with().size(DensityUtil.dp2px(this, 100))
        .layoutManager(new DingLayoutManager())
        .gap(DensityUtil.dp2px(this, 3))
        .gapColor(Color.parseColor("#E8E8E8"))
        .load(getBitmaps(3))
        .into(image4);
  }

  @Override
  protected void initEvent() {

  }

  private Bitmap[] getBitmaps(int count) {
    Bitmap[] bitmaps = new Bitmap[count];
    for (int i = 0; i < count; i++) {
      Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.author_head_img);
      bitmaps[i] = bitmap;
    }
    return bitmaps;
  }
}
