package com.sf.sofarmusic.online.artist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import com.sf.base.BaseActivity;
import com.sf.sofarmusic.R;
import com.sf.sofarmusic.online.artist.model.ArtistGroup;

public class ArtistListActivity extends BaseActivity {

  public static final String KEY_ARTIST_GROUP = "artist_group";

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_container);

    ArtistListFragment fragment = new ArtistListFragment();
    Bundle b = new Bundle();
    b.putSerializable(KEY_ARTIST_GROUP, getIntent().getSerializableExtra(KEY_ARTIST_GROUP));
    fragment.setArguments(b);
    getSupportFragmentManager().beginTransaction()
        .replace(R.id.fragment_container, fragment, "fragment_artist_list")
        .commitAllowingStateLoss();
  }

  public static void launch(Context context, ArtistGroup artistGroup) {
    Intent intent = new Intent(context, ArtistListActivity.class);
    intent.putExtra(KEY_ARTIST_GROUP, artistGroup);
    context.startActivity(intent);
  }
}
