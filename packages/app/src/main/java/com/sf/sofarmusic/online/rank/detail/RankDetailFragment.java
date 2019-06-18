package com.sf.sofarmusic.online.rank.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import com.sf.base.mvp.Presenter;
import com.sf.base.network.page.PageList;
import com.sf.base.recycler.RecyclerFragment;
import com.sf.sofarmusic.R;
import com.sf.sofarmusic.online.rank.RankDetailAdapter;
import com.sf.sofarmusic.online.rank.model.RankDetailPageList;
import com.sf.sofarmusic.online.rank.presenter.RankDetailHeadPresenter;
import com.sf.sofarmusic.online.rank.presenter.RankDetailTitlePresenter;
import com.sf.sofarmusic.play.core.PlayEvent;
import com.sf.widget.recyclerview.RecyclerAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class RankDetailFragment extends RecyclerFragment {

  private int type;
  private RankDetailPageList pageList;
  private Presenter presenter = new Presenter();

  @Override
  protected int getLayoutResId() {
    return R.layout.fragment_rank_detail;
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    presenter.destroy();

    EventBus.getDefault().unregister(this);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    type = getArguments().getInt(RankDetailActivity.KEY_RANK_TYPE);
    super.onViewCreated(view, savedInstanceState);
    setRefreshEnable(false);

    presenter.add(new RankDetailTitlePresenter());
    presenter.add(new RankDetailHeadPresenter());
    presenter.create(view);

    EventBus.getDefault().register(this);
  }

  @Override
  protected RecyclerAdapter onCreateAdapter() {
    return new RankDetailAdapter();
  }

  @Override
  protected PageList onCreatePageList() {
    pageList = new RankDetailPageList(type);
    return pageList;
  }

  @Override
  public void onFinishLoading(boolean firstPage, boolean cache) {
    super.onFinishLoading(firstPage, cache);
    presenter.bind(pageList.getPageResponse(), this);
  }

  @Subscribe
  public void onSelectSongEvent(PlayEvent.SelectSongEvent event) {
    if (getOriginAdapter() instanceof RankDetailAdapter) {
      RankDetailAdapter adapter = (RankDetailAdapter) getOriginAdapter();
      for (int i = 0; i < adapter.getList().size(); i++) {
        if (adapter.getList().get(i).songId.equals(event.song.songId)) {
          adapter.selectSong(i);

          //这里可能由于AppbarLayout嵌套导致滑动的位置不对
          getRecyclerView().getLayoutManager().scrollToPosition(i);
          break;
        }
      }
    }
  }
}
