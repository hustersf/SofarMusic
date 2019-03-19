package com.sf.sofarmusic.online.rank.detail;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sf.base.network.page.PageList;
import com.sf.base.recycler.RecyclerFragment;
import com.sf.sofarmusic.R;
import com.sf.widget.recyclerview.RecyclerAdapter;

public class RankDetailFragment extends RecyclerFragment {

  @Override
  protected int getLayoutResId() {
    return R.layout.fragment_rank_detail;
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return super.onCreateView(inflater, container, savedInstanceState);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
  }

  @Override
  protected RecyclerAdapter onCreateAdapter() {
    return null;
  }

  @Override
  protected PageList onCreatePageList() {
    return null;
  }
}
