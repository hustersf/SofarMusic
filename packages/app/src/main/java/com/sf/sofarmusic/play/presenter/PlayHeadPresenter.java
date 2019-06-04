package com.sf.sofarmusic.play.presenter;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;
import com.sf.base.mvp.Presenter;
import com.sf.sofarmusic.R;
import com.sf.sofarmusic.model.Song;
import com.sf.sofarmusic.play.PlayEvent;
import com.sf.utility.CollectionUtil;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import java.util.List;

public class PlayHeadPresenter extends Presenter<List<Song>> {

  private TextView headBack, headShare;
  private TextView nameTv, authorTv;

  @Override
  protected void onDestroy() {
    super.onDestroy();
    EventBus.getDefault().unregister(this);
  }

  @Override
  protected void onCreate() {
    super.onCreate();
    headBack = getView().findViewById(R.id.head_back);
    headShare = getView().findViewById(R.id.head_share);
    nameTv = getView().findViewById(R.id.music_name_tv);
    authorTv = getView().findViewById(R.id.music_author_tv);
    headBack.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (getCallerContext() instanceof Activity) {
          ((Activity) getCallerContext()).finish();
        }
      }
    });
    EventBus.getDefault().register(this);
  }

  @Override
  protected void onBind(List<Song> model, Object callerContext) {
    super.onBind(model, callerContext);
    if (CollectionUtil.isEmpty(model)) {
      return;
    }

    for (int i = 0; i < model.size(); i++) {
      if (model.get(i).play) {
        nameTv.setText(model.get(i).name);
        authorTv.setText(model.get(i).author);
        break;
      }
    }
  }

  @Subscribe
  public void onChangeSongEvent(PlayEvent.ChangeSongEvent event) {
    Song song = getModel().get(event.position);
    nameTv.setText(song.name);
    authorTv.setText(song.author);
  }
}
