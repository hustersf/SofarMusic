package com.sf.sofarmusic.search.presenter;

import android.view.View;
import android.widget.TextView;
import java.util.List;
import com.sf.base.mvp.Presenter;
import com.sf.sofarmusic.R;
import com.sf.sofarmusic.search.model.SearchInfo;
import com.sf.utility.CollectionUtil;
import com.sf.widget.flowlayout.FlowTagList;

public class SearchHotPresenter extends Presenter<List<SearchInfo>> {

  TextView hotTitle;
  FlowTagList hotTagLayout;

  String[] tags;

  @Override
  protected void onCreate() {
    super.onCreate();
    hotTitle = getView().findViewById(R.id.hot_title);
    hotTagLayout = getView().findViewById(R.id.hot_tags);
  }

  @Override
  protected void onBind(List<SearchInfo> model, Object callerContext) {
    super.onBind(model, callerContext);
    if (CollectionUtil.isEmpty(model)) {
      hotTitle.setVisibility(View.GONE);
      hotTagLayout.removeAllViews();
    } else {
      hotTitle.setVisibility(View.VISIBLE);
      tags = new String[model.size()];
      for (int i = 0; i < model.size(); i++) {
        tags[i] = model.get(i).word;
      }
      hotTagLayout.setTags(tags);
    }
  }
}
