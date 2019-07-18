package com.sf.sofarmusic.online.artist.album;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import com.sf.sofarmusic.R;
import com.sf.sofarmusic.model.Album;
import com.sf.sofarmusic.play.core.PlayerBaseActivity;

public class AlbumDetailActivity extends PlayerBaseActivity {

  public static final String KEY_ALBUM = "album";

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.base_play_activity_container);

    AlbumDetailFragment fragment = new AlbumDetailFragment();
    Bundle b = new Bundle();
    b.putSerializable(KEY_ALBUM, getIntent().getSerializableExtra(KEY_ALBUM));
    fragment.setArguments(b);
    getSupportFragmentManager().beginTransaction()
        .replace(R.id.fragment_container, fragment, "fragment_album_detail")
        .commitAllowingStateLoss();
  }

  public static void launch(Context context, Album album) {
    Intent intent = new Intent(context, AlbumDetailActivity.class);
    intent.putExtra(KEY_ALBUM, album);
    context.startActivity(intent);
  }
}
