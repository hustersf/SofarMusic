package com.sf.demo.bitmap.combine;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.widget.ImageView;

import com.sf.base.UIRootActivity;
import com.sf.demo.R;
import com.sf.utility.DensityUtil;
import com.sf.widget.bitmap.combine.CombineBitmap;
import com.sf.widget.bitmap.combine.layout.KwaiChatLayoutManager;
import com.sf.widget.bitmap.combine.layout.WechatLayoutManager;
import com.sf.widget.chat.ChatHeadLayout;

import java.util.ArrayList;
import java.util.List;

public class CombineActivity extends UIRootActivity {

  private ImageView wechat_image1;
  private ImageView wechat_image2;
  private ImageView wechat_image3;
  private ImageView wechat_image4;
  private ImageView wechat_image5;
  private ImageView wechat_image6;
  private ImageView wechat_image7;
  private ImageView wechat_image8;
  private ImageView wechat_image9;

  private ImageView kwai_image1;
  private ImageView kwai_image2;
  private ImageView kwai_image3;
  private ImageView kwai_image4;
  private ImageView kwai_image5;
  private ImageView kwai_image6;
  private ImageView kwai_image7;
  private ImageView kwai_image8;
  private ImageView kwai_image9;

  private List<ImageView> wechat_imageViews = new ArrayList<>();
  private List<ImageView> kwai_imageViews = new ArrayList<>();

  private ChatHeadLayout chatHeadLayout;

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
    wechat_image1 = findViewById(R.id.wechat_image1);
    wechat_image2 = findViewById(R.id.wechat_image2);
    wechat_image3 = findViewById(R.id.wechat_image3);
    wechat_image4 = findViewById(R.id.wechat_image4);
    wechat_image5 = findViewById(R.id.wechat_image5);
    wechat_image6 = findViewById(R.id.wechat_image6);
    wechat_image7 = findViewById(R.id.wechat_image7);
    wechat_image8 = findViewById(R.id.wechat_image8);
    wechat_image9 = findViewById(R.id.wechat_image9);
    wechat_imageViews.add(wechat_image1);
    wechat_imageViews.add(wechat_image2);
    wechat_imageViews.add(wechat_image3);
    wechat_imageViews.add(wechat_image4);
    wechat_imageViews.add(wechat_image5);
    wechat_imageViews.add(wechat_image6);
    wechat_imageViews.add(wechat_image7);
    wechat_imageViews.add(wechat_image8);
    wechat_imageViews.add(wechat_image9);


    kwai_image1 = findViewById(R.id.kwai_image1);
    kwai_image2 = findViewById(R.id.kwai_image2);
    kwai_image3 = findViewById(R.id.kwai_image3);
    kwai_image4 = findViewById(R.id.kwai_image4);
    kwai_image5 = findViewById(R.id.kwai_image5);
    kwai_image6 = findViewById(R.id.kwai_image6);
    kwai_image7 = findViewById(R.id.kwai_image7);
    kwai_image8 = findViewById(R.id.kwai_image8);
    kwai_image9 = findViewById(R.id.kwai_image9);
    kwai_imageViews.add(kwai_image1);
    kwai_imageViews.add(kwai_image2);
    kwai_imageViews.add(kwai_image3);
    kwai_imageViews.add(kwai_image4);
    kwai_imageViews.add(kwai_image5);
    kwai_imageViews.add(kwai_image6);
    kwai_imageViews.add(kwai_image7);
    kwai_imageViews.add(kwai_image8);
    kwai_imageViews.add(kwai_image9);


    chatHeadLayout = findViewById(R.id.chat_head);

  }

  @Override
  protected void initData() {

    for (int i = 1; i <= 9; i++) {
      CombineBitmap.with().size(DensityUtil.dp2px(this, 180))
          .layoutManager(new WechatLayoutManager())
          .gap(DensityUtil.dp2px(this, 3))
          .gapColor(Color.parseColor("#E8E8E8"))
          .load(getBitmaps(i))
          .into(wechat_imageViews.get(i - 1));
    }

    for (int i = 1; i <= 9; i++) {
      CombineBitmap.with().size(DensityUtil.dp2px(this, 180))
          .layoutManager(new KwaiChatLayoutManager())
          .gap(DensityUtil.dp2px(this, 3))
          .gapColor(Color.parseColor("#E8E8E8"))
          .load(getBitmaps(i))
          .into(kwai_imageViews.get(i - 1));
    }

    List<Integer> drawableIds = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
      drawableIds.add(R.drawable.author_head_img);
    }
    chatHeadLayout.setGapWidth(2);
    chatHeadLayout.setResIds(drawableIds);
    chatHeadLayout.setRoundLayoutRadius(16);
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
