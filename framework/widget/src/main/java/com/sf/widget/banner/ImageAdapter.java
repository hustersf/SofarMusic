package com.sf.widget.banner;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.sf.utility.DensityUtil;
import com.sf.utility.ToastUtil;

/**
 * Created by sufan on 17/6/24.
 */
public class ImageAdapter extends LoopVPAdapter<BannerInfo> {


  public ImageAdapter(Context context, List<BannerInfo> datas) {
    super(context, datas);
  }


  @Override
  public View getItemView(final BannerInfo bannerInfo) {

    RelativeLayout layout = new RelativeLayout(mContext);
    ImageView imageView = new ImageView(mContext);
    RelativeLayout.LayoutParams ivLp = new RelativeLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    imageView.setImageResource(bannerInfo.imgId);
    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
    imageView.setLayoutParams(ivLp);
    layout.addView(imageView);

    String des = bannerInfo.name;
    if (!TextUtils.isEmpty(des)) {
      TextView tv = new TextView(mContext);
      RelativeLayout.LayoutParams tvLp = new RelativeLayout.LayoutParams(
          ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
      tvLp.topMargin = DensityUtil.dp2px(mContext, 72);
      tvLp.rightMargin = DensityUtil.dp2px(mContext, 72);
      tvLp.leftMargin = DensityUtil.dp2px(mContext, 72);
      tv.setGravity(Gravity.CENTER);
      tv.setLayoutParams(tvLp);
      tv.setShadowLayer(DensityUtil.dp2px(mContext, 6f), 0,
          DensityUtil.dp2px(mContext, 1f), 0x4d000000);
      tv.setText(des);
      tv.setTextColor(Color.parseColor("#FFFFFF"));
      tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 23);
      tv.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
      layout.addView(tv);
    }

    imageView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        ToastUtil.startShort(mContext, bannerInfo.name);
      }
    });
    return layout;
  }
}
