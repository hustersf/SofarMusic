package com.sf.sofarmusic.local;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.sf.sofarmusic.R;
import com.sf.sofarmusic.play.core.PlayerBaseActivity;

/**
 * Created by sufan on 16/12/2.
 */

public class LocalDetailActivity extends PlayerBaseActivity {

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.base_play_activity_container);

    SingleFragment fragment = new SingleFragment();
    getSupportFragmentManager().beginTransaction()
        .replace(R.id.fragment_container, fragment, "fragment_local_detail")
        .commitAllowingStateLoss();
  }

  public static void launch(Context context) {
    Intent intent = new Intent(context, LocalDetailActivity.class);
    context.startActivity(intent);
  }
}
