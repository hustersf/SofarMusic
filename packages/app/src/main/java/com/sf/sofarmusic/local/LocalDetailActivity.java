package com.sf.sofarmusic.local;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.widget.TextView;

import com.sf.sofarmusic.R;
import com.sf.sofarmusic.play.core.PlayerBaseActivity;

/**
 * Created by sufan on 16/12/2.
 */

public class LocalDetailActivity extends PlayerBaseActivity {

  private TextView headBack, headTitle;

  private final static String EXTRA_TITLE = "title";

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.local_detail_activity);

    initHead();

    SingleFragment fragment = new SingleFragment();
    getSupportFragmentManager().beginTransaction()
        .replace(R.id.fragment_container, fragment, "fragment_local_detail")
        .commitAllowingStateLoss();
  }

  public static void launch(Context context, String title) {
    Intent intent = new Intent(context, LocalDetailActivity.class);
    intent.putExtra(LocalDetailActivity.EXTRA_TITLE, title);
    context.startActivity(intent);
  }

  private void initHead() {
    headBack = findViewById(R.id.head_back);
    headTitle = findViewById(R.id.head_title);

    String title = getIntent().getStringExtra(LocalDetailActivity.EXTRA_TITLE);
    if (TextUtils.isEmpty(title)) {
      title = "本地音乐";
    }
    headTitle.setText(title);

    headBack.setOnClickListener(v -> {
      finish();
    });
  }
}
