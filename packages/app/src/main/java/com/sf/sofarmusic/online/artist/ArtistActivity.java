package com.sf.sofarmusic.online.artist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;

import com.sf.sofarmusic.R;
import com.sf.sofarmusic.model.Artist;
import com.sf.sofarmusic.play.core.PlayerBaseActivity;

public class ArtistActivity extends PlayerBaseActivity {

  public static final String KEY_ARTIST = "artist";

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.base_play_activity_container);

    ArtistFragment fragment = new ArtistFragment();
    Bundle b = new Bundle();
    b.putSerializable(KEY_ARTIST, getIntent().getSerializableExtra(KEY_ARTIST));
    fragment.setArguments(b);
    getSupportFragmentManager().beginTransaction()
        .replace(R.id.fragment_container, fragment, "fragment_artist")
        .commitAllowingStateLoss();
  }

  public static void launch(Context context, Artist artist) {
    Intent intent = new Intent(context, ArtistActivity.class);
    intent.putExtra(KEY_ARTIST, artist);
    context.startActivity(intent);
  }
}
