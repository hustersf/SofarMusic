package com.sf.sofarmusic.play;

import java.text.SimpleDateFormat;
import java.util.List;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import com.sf.base.mvp.Presenter;
import com.sf.sofarmusic.R;
import com.sf.sofarmusic.base.Constant;
import com.sf.sofarmusic.base.PlayerBaseActivity;
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

  private BroadcastReceiver receiver;

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

    initData();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    unregisterReceiver(receiver);
    presenter.destroy();
    
    for (int i = 0; i < songs.size(); i++) {
      if (songs.get(i).play) {
        EventBus.getDefault().post(new PlayEvent.SelectSongEvent(songs.get(i)));
        break;
      }
    }
  }


  private void initData() {
    // 注册广播
    IntentFilter intentFilter = new IntentFilter();
    intentFilter.addAction(Constant.RefreshProgress);
    intentFilter.addAction(Constant.RefreshPosition);
    intentFilter.addAction(Constant.NOTIFY_SERVICE_PAUSE);
    intentFilter.addAction(Constant.NOTIFY_SERVICE_PLAY);
    receiver = new BroadcastReceiver() {
      @Override
      public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Constant.RefreshProgress)) {
          Bundle bundle = intent.getExtras();
          int progress = bundle.getInt("progress");
          int secondProgress = bundle.getInt("secondProgress");

          SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
          int current = bundle.getInt("current");
          int total = bundle.getInt("total");
        } else if (intent.getAction().equals(Constant.RefreshPosition)) {
          Bundle bundle = intent.getExtras();
        } else if (intent.getAction().equals(Constant.NOTIFY_SERVICE_PLAY)) {
          // 播放
        } else if (intent.getAction().equals(Constant.NOTIFY_SERVICE_PAUSE)) {
          // 暂停
        }
      }
    };
    registerReceiver(receiver, intentFilter);
  }

}
