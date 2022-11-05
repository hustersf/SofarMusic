package com.sf.sofarmusic.online.artist.mv;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import androidx.annotation.Nullable;

import com.sf.base.BaseActivity;
import com.sf.base.mvp.Presenter;
import com.sf.sofarmusic.R;
import com.sf.sofarmusic.api.ApiProvider;
import com.sf.sofarmusic.model.MV;
import com.sf.sofarmusic.online.artist.presenter.MVInfoPresenter;
import com.sf.sofarmusic.online.artist.presenter.MVSharePresenter;
import com.sf.sofarmusic.online.artist.presenter.MVVideoPresenter;

public class MVDetailActivity extends BaseActivity {

  public static final String KEY_MV = "mv";
  private MV mv;
  private Presenter mPresenter = new Presenter();

  public static void launch(Context context, MV mv) {
    Intent intent = new Intent(context, MVDetailActivity.class);
    intent.putExtra(KEY_MV, mv);
    context.startActivity(intent);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    if (mPresenter != null) {
      mPresenter.destroy();
    }
  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.mv_detail_activity);
    mv = (MV) getIntent().getSerializableExtra(KEY_MV);

    mPresenter.add(new MVInfoPresenter());
    mPresenter.add(new MVSharePresenter());
    mPresenter.add(new MVVideoPresenter());
    mPresenter.create(getWindow().getDecorView());

    fetchMVDetail();
  }

  private void fetchMVDetail() {
    ApiProvider.getMusicApiService().mvDetail(mv.mvId).map(resultResponse -> resultResponse.result)
        .subscribe(mvDetailResponse -> {
          mPresenter.bind(mvDetailResponse, this);
        }, throwable -> {});
  }

  @Override
  public void onBackPressed() {
    Configuration configuration = getResources().getConfiguration();
    int ori = configuration.orientation;
    if (ori == configuration.ORIENTATION_LANDSCAPE) {
      setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    } else {
      super.onBackPressed();
    }
  }
}
