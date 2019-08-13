package com.sf.demo.widget.combine;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.sf.base.UIRootActivity;
import com.sf.demo.R;
import com.sf.widget.chat.ChatHeadLayout;
import java.util.ArrayList;
import java.util.List;

public class CombineActivity extends UIRootActivity {

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
    chatHeadLayout = findViewById(R.id.chat_head);
  }

  @Override
  protected void initData() {

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
}
