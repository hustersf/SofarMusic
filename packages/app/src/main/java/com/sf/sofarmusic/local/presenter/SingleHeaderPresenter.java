package com.sf.sofarmusic.local.presenter;

import android.widget.TextView;

import com.sf.base.mvp.Presenter;
import com.sf.sofarmusic.R;
import com.sf.sofarmusic.local.SingleFragment;
import com.sf.sofarmusic.model.Song;

import java.util.List;

public class SingleHeaderPresenter extends Presenter<List<Song>> {

  TextView playTv, countTv, moreTv;

  private SingleFragment fragment;

  public SingleHeaderPresenter(SingleFragment fragment) {
    this.fragment = fragment;
  }

  @Override
  protected void onCreate() {
    super.onCreate();
    playTv = getView().findViewById(R.id.play_tv);
    countTv = getView().findViewById(R.id.count_tv);
    moreTv = getView().findViewById(R.id.more_tv);
  }

  @Override
  protected void onBind(List<Song> songs, Object callerContext) {
    super.onBind(songs, callerContext);

    String text = "(共" + (songs.size()) + "首)";
    countTv.setText(text);

    getView().setOnClickListener(v -> {
        fragment.playAll();
    });
  }
}
