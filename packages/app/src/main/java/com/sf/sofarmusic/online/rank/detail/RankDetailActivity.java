package com.sf.sofarmusic.online.rank.detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.sf.sofarmusic.R;
import com.sf.sofarmusic.play.core.PlayerBaseActivity;

public class RankDetailActivity extends PlayerBaseActivity {

  public static final String KEY_RANK_TYPE = "rank_type";

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.base_play_activity_container);

    RankDetailFragment fragment = new RankDetailFragment();
    Bundle b = new Bundle();
    b.putInt(KEY_RANK_TYPE, getIntent().getIntExtra(KEY_RANK_TYPE, 0));
    fragment.setArguments(b);
    getSupportFragmentManager().beginTransaction()
        .replace(R.id.fragment_container, fragment, "fragment_rank_detail")
        .commitAllowingStateLoss();
  }

  public static void launch(Context context, int type) {
    Intent intent = new Intent(context, RankDetailActivity.class);
    intent.putExtra(KEY_RANK_TYPE, type);
    context.startActivity(intent);
  }
}
