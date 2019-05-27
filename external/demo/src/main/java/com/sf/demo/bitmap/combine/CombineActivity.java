package com.sf.demo.bitmap.combine;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.widget.ImageView;

import com.sf.base.UIRootActivity;
import com.sf.demo.R;
import com.sf.utility.DensityUtil;
import com.sf.widget.bitmap.combine.CombineBitmap;
import com.sf.widget.bitmap.combine.layout.WechatLayoutManager;

public class CombineActivity extends UIRootActivity {

  private ImageView image1;
  private ImageView image2;
  private ImageView image3;

  @Override
  protected int getLayoutId() {
    return R.layout.activity_bitmap_combine;
  }

  @Override
  protected void initTitle() {
    mHeadTitleTv.setText("仿微信群聊头像");
  }

  @Override
  protected void initView() {
    image1 = findViewById(R.id.image1);
    image2 = findViewById(R.id.image2);
    image3 = findViewById(R.id.image3);
  }

  @Override
  protected void initData() {

    CombineBitmap.with().size(DensityUtil.dp2px(this, 180))
        .layoutManager(new WechatLayoutManager())
        .gap(DensityUtil.dp2px(this, 3))
        .gapColor(Color.parseColor("#E8E8E8"))
        .load(getBitmaps(2))
        .into(image1);

    CombineBitmap.with().size(DensityUtil.dp2px(this, 180))
        .layoutManager(new WechatLayoutManager())
        .gap(DensityUtil.dp2px(this, 3))
        .gapColor(Color.parseColor("#E8E8E8"))
        .load(getBitmaps(5))
        .into(image2);

    CombineBitmap.with().size(DensityUtil.dp2px(this, 180))
        .layoutManager(new WechatLayoutManager())
        .gap(DensityUtil.dp2px(this, 3))
        .gapColor(Color.parseColor("#E8E8E8"))
        .load(getBitmaps(9))
        .into(image3);
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
