package com.sf.sofarmusic.search.presenter;

import android.view.View;
import android.widget.TextView;

import com.sf.base.BaseActivity;
import com.sf.base.mvp.Presenter;
import com.sf.sofarmusic.R;
import com.sf.sofarmusic.db.search.SearchRecordManager;
import com.sf.sofarmusic.search.SearchDataHolder;
import com.sf.sofarmusic.search.model.SearchInfo;
import com.sf.utility.CollectionUtil;
import com.sf.widget.dialog.SofarDialog;
import com.sf.widget.dialog.SofarDialogController;
import com.sf.widget.dialog.SofarDialogFragment;
import com.sf.widget.flowlayout.FlowTagList;
import java.util.List;

public class SearchHistoryPresenter extends Presenter {

  View historyHeader;
  TextView clearTv;
  FlowTagList historyTagLayout;

  List<SearchInfo> searchInfos;
  String[] tags;

  @Override
  protected void onDestroy() {
    SearchRecordManager.getInstance().asyncReplaceSearchList(searchInfos);
    super.onDestroy();
  }

  @Override
  protected void onCreate() {
    super.onCreate();
    historyHeader = getView().findViewById(R.id.history_header);
    clearTv = getView().findViewById(R.id.clear_tv);
    historyTagLayout = getView().findViewById(R.id.history_tags);

    clearTv.setOnClickListener(v -> {
      showClearDialog();
    });
  }

  @Override
  protected void onBind(Object model, Object callerContext) {
    super.onBind(model, callerContext);
    searchInfos = SearchDataHolder.getInstance().getSearchInfos();
    updateHistoryTag();
  }

  private void updateHistoryTag() {
    if (CollectionUtil.isEmpty(searchInfos)) {
      historyHeader.setVisibility(View.GONE);
      historyTagLayout.removeAllViews();
    } else {
      historyHeader.setVisibility(View.VISIBLE);
      tags = new String[searchInfos.size()];
      for (int i = 0; i < searchInfos.size(); i++) {
        tags[i] = searchInfos.get(i).word;
      }
      historyTagLayout.setTags(tags);
    }
  }

  private void showClearDialog() {
    SofarDialogFragment dialogFragment = new SofarDialogFragment.Builder(getContext())
        .setMessage("确定清空全部历史记录？")
        .setPositiveButton("清空", dialog -> {
          SearchDataHolder.getInstance().clear(true);
          updateHistoryTag();
        })
        .setNegativeButton("取消", null)
        .build();
    SofarDialogController.show((BaseActivity) getActivity(), dialogFragment);
  }
}
