package com.sf.sofarmusic.play.presenter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.sf.base.mvp.Presenter;
import com.sf.sofarmusic.R;
import com.sf.sofarmusic.model.Song;
import com.sf.sofarmusic.play.RenderBlur;

import java.util.List;

public class PlayBackgroundPresenter extends Presenter<List<Song>> {

  ImageView playBgIv;

  Bitmap sourceBt;
  Bitmap blurBt;

  @Override
  protected void onDestroy() {
    super.onDestroy();
    recycleBitmap();
  }

  @Override
  protected void onCreate() {
    super.onCreate();
    playBgIv = getView().findViewById(R.id.play_bg_iv);

    // 初始化背景
    sourceBt =
        BitmapFactory.decodeResource(getContext().getResources(), R.drawable.play_default_bg);
    blurBt = RenderBlur.gaussianBlur(getContext(), sourceBt, 25);
    playBgIv.setImageBitmap(blurBt);
  }

  @Override
  protected void onBind(List<Song> model, Object callerContext) {
    super.onBind(model, callerContext);
  }

  private void recycleBitmap() {
    if (sourceBt != null) {
      sourceBt.recycle();
      sourceBt = null;
    }

    if (blurBt != null) {
      blurBt.recycle();
      blurBt = null;
    }
  }

}
