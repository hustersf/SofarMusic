package com.sf.sofarmusic.online.recommend;

import android.content.Context;
import android.view.View;

import com.sf.sofarmusic.R;
import com.sf.sofarmusic.online.recommend.model.Feed;
import com.sf.sofarmusic.online.recommend.presenter.SceneItemPresenter;
import com.sf.utility.ViewUtil;
import com.sf.widget.banner.LoopVPAdapter;

import java.util.List;

public class SceneAdapter extends LoopVPAdapter<Feed> {

  public SceneAdapter(Context context, List<Feed> list) {
    super(context, list);
  }

  @Override
  public View getItemView(Feed data) {
    View view = ViewUtil.inflate(mContext, R.layout.feed_item_scene);
    SceneItemPresenter presenter = new SceneItemPresenter();
    presenter.create(view);
    presenter.bind(data, mContext);
    return view;
  }
}
