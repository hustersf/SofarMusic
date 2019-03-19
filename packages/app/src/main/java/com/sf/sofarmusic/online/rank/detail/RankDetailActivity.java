package com.sf.sofarmusic.online.rank.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.sf.sofarmusic.R;
import com.sf.sofarmusic.base.PlayerBaseActivity;

public class RankDetailActivity extends PlayerBaseActivity {

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_container);

    RankDetailFragment fragment = new RankDetailFragment();
    getSupportFragmentManager().beginTransaction()
        .replace(R.id.fragment_container, fragment, "fragment_rank_detail")
        .commitAllowingStateLoss();
  }
}
