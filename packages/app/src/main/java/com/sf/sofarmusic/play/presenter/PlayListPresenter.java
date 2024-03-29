package com.sf.sofarmusic.play.presenter;

import android.content.DialogInterface;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.sf.base.mvp.Presenter;
import com.sf.base.view.SofarBottomSheetDialog;
import com.sf.sofarmusic.R;
import com.sf.sofarmusic.model.Song;
import com.sf.sofarmusic.play.PlayActivity;
import com.sf.sofarmusic.play.core.PlayDataHolder;
import com.sf.sofarmusic.play.core.PlayEvent;
import com.sf.sofarmusic.play.PlayListAdapter;
import com.sf.utility.CollectionUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

public class PlayListPresenter extends Presenter<List<Song>> {

  View moreTv;
  SofarBottomSheetDialog sheetDialog;
  View sheetView;
  RecyclerView playRv;
  TextView listTv;
  TextView clearTv;
  PlayListAdapter playAdapter;

  List<Song> songs;

  @Override
  protected void onDestroy() {
    super.onDestroy();
    EventBus.getDefault().unregister(this);
  }

  @Override
  protected void onCreate() {
    super.onCreate();
    moreTv = getView();
    EventBus.getDefault().register(this);
  }

  @Override
  protected void onBind(List<Song> model, Object callerContext) {
    super.onBind(model, callerContext);
    if (getActivity() == null) {
      return;
    }

    songs = getModel();
    initSheetDialog();
    moreTv.setOnClickListener(v -> {
      updateSheetDialog();
      openBottomSheet();
    });
  }

  private void openBottomSheet() {
    sheetDialog.show();
  }

  private void initSheetDialog() {
    sheetDialog = new SofarBottomSheetDialog(getActivity());
    sheetView = LayoutInflater.from(getActivity()).inflate(R.layout.sheet_play_list, null);
    sheetDialog.setContentView(sheetView);
    final BottomSheetBehavior bottomSheetBehavior =
        BottomSheetBehavior.from((View) sheetView.getParent());
    sheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
      @Override
      public void onDismiss(DialogInterface dialog) {
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
      }
    });


    listTv = sheetView.findViewById(R.id.list_tv);
    clearTv = sheetView.findViewById(R.id.clear_tv);
    clearTv.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        PlayDataHolder.getInstance().clearSongs();
        songs = PlayDataHolder.getInstance().getSongs();
        sheetDialog.dismiss();
        EventBus.getDefault().post(new PlayEvent.ClearSongEvent());
        if (getActivity() instanceof PlayActivity) {
          getActivity().finish();
        }
      }
    });
    playRv = sheetView.findViewById(R.id.play_list_rv);
    playRv.setLayoutManager(new LinearLayoutManager(getActivity()));
    listTv.setText("播放列表（" + songs.size() + "）");
    playAdapter = new PlayListAdapter();
    playAdapter.setListWithRelated(songs);
    playAdapter.notifyDataSetChanged();
    playAdapter.setOnItemClickListener(listener);

    playRv.setAdapter(playAdapter);
  }

  PlayListAdapter.OnItemClickListener listener = new PlayListAdapter.OnItemClickListener() {
    @Override
    public void onItemClick(int position) {

      EventBus.getDefault()
          .post(new PlayEvent.SelectSongEvent(playAdapter.getList().get(position)));
    }

    @Override
    public void onDeleteSong(Song song) {
      listTv.setText("播放列表（" + songs.size() + "）");

      if (songs.size() == 0) {
        sheetDialog.dismiss();
        return;
      }

      EventBus.getDefault().post(new PlayEvent.DeleteSongEvent(song));
      EventBus.getDefault().post(new PlayEvent.SelectSongEvent(
          playAdapter.getList().get(playAdapter.getSelectedPosition())));
    }
  };

  /**
   * 每次打开之前需要更新一下列表状态
   */
  private void updateSheetDialog() {
    if (CollectionUtil.isEmpty(songs)) {
      return;
    }

    int position = 0;
    for (int i = 0; i < songs.size(); i++) {
      if (songs.get(i).play) {
        position = i;
        break;
      }
    }

    playAdapter.setListWithRelated(songs);
    playAdapter.notifyDataSetChanged();
    playRv.getLayoutManager().scrollToPosition(position);
    listTv.setText("播放列表（" + songs.size() + "）");
  }

  @Subscribe
  public void onSelectSongEvent(PlayEvent.SelectSongEvent event) {
    for (int i = 0; i < playAdapter.getList().size(); i++) {
      if (playAdapter.getList().get(i).songId.equals(event.song.songId)) {
        playAdapter.selectSong(i);
        break;
      }
    }
  }
}
