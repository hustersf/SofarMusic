package com.sf.sofarmusic.demo.viewpager.banner;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sf.sofarmusic.util.ToastUtil;

import java.util.List;

/**
 * Created by sufan on 17/6/24.
 */

public class ImageAdapter extends LoopVPAdapter<BannerInfo> {

    private ViewGroup.LayoutParams layoutParams;

    public ImageAdapter(Context context, List<BannerInfo> datas, ViewPager viewPager, BannerIndicator indicator) {
        super(context, datas, viewPager,indicator);
    }


    @Override
    public View getItemView(final BannerInfo bannerInfo) {

        if (layoutParams == null) {
            layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
        ImageView imageView = new ImageView(mContext);
        imageView.setLayoutParams(layoutParams);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageResource(bannerInfo.imgId);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.startShort(mContext,bannerInfo.name);
            }
        });
        return imageView;
    }
}
