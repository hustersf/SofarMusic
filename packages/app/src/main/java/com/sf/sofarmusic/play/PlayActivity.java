package com.sf.sofarmusic.play;

import java.util.List;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.sf.base.mvp.Presenter;
import com.sf.sofarmusic.R;
import com.sf.sofarmusic.play.core.PlayDataHolder;
import com.sf.sofarmusic.play.core.PlayEvent;
import com.sf.sofarmusic.play.core.PlayerBaseActivity;
import com.sf.sofarmusic.model.Song;
import com.sf.sofarmusic.play.presenter.PlayBackgroundPresenter;
import com.sf.sofarmusic.play.presenter.PlayControlPresenter;
import com.sf.sofarmusic.play.presenter.PlayHeadPresenter;
import com.sf.sofarmusic.play.presenter.PlayPanelPresenter;
import org.greenrobot.eventbus.EventBus;


/**
 * Created by sufan on 16/11/21.
 */
public class PlayActivity extends PlayerBaseActivity {

  private List<Song> songs;
  private Presenter presenter = new Presenter();

  public static void launch(Context context) {
    Intent intent = new Intent(context, PlayActivity.class);
    context.startActivity(intent);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_play);

    songs = PlayDataHolder.getInstance().getSongs();
    presenter.add(new PlayHeadPresenter());
    presenter.add(new PlayPanelPresenter());
    presenter.add(new PlayControlPresenter());
    presenter.add(new PlayBackgroundPresenter());
    presenter.create(getWindow().getDecorView());
    presenter.bind(songs, this);

  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    presenter.destroy();
  }

}
