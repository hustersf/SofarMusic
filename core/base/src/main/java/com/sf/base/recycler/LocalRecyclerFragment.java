package com.sf.base.recycler;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.sf.base.BaseFragment;
import com.sf.base.R;
import com.sf.widget.recyclerview.RecyclerAdapter;
import com.sf.widget.recyclerview.RecyclerHeaderFooterAdapter2;

import java.util.List;

/**
 * 封装通用的列表页（非网络数据请求）
 */
public abstract class LocalRecyclerFragment<MODEL> extends BaseFragment {

  private SwipeRefreshLayout mRefreshLayout;
  private View mRootView;

  protected RecyclerView mRecyclerView;
  private RecyclerAdapter<MODEL> mOriginAdapter;
  private RecyclerHeaderFooterAdapter2 mHeaderFooterAdapter;

  private List<MODEL> mDatas;


  protected int getLayoutResId() {
    return R.layout.layout_base_recycler_fragment;
  }

  protected RecyclerView.LayoutManager onCreateLayoutManager() {
    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
    return layoutManager;
  }

  protected abstract RecyclerAdapter<MODEL> onCreateAdapter();

  protected abstract List<MODEL> onCreateModelList();

  protected List<View> onCreateHeaderViews() {
    return null;
  }

  protected List<View> onCreateFooterViews() {
    return null;
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    mRootView = inflater.inflate(getLayoutResId(), container, false);
    mRefreshLayout = mRootView.findViewById(R.id.refresh_layout);
    mRecyclerView = mRootView.findViewById(R.id.recycler_view);
    return mRootView;
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    initRecyclerView();
    initRefreshLayout();

    mDatas = onCreateModelList();
    if (autoLoad()) {
      refresh();
    }
  }

  private void initRecyclerView() {
    mRecyclerView.setLayoutManager(onCreateLayoutManager());
    mOriginAdapter = onCreateAdapter();
    mHeaderFooterAdapter = new RecyclerHeaderFooterAdapter2(mOriginAdapter, onCreateHeaderViews(),
        onCreateFooterViews());
    mRecyclerView.setAdapter(mHeaderFooterAdapter);
  }

  private void initRefreshLayout() {
    mRefreshLayout.setOnRefreshListener(() -> {
      refresh();
    });
    // 默认无下拉刷新
    setRefreshEnable(false);
  }

  private void refresh() {
    mOriginAdapter.setList(mDatas);
    mOriginAdapter.notifyDataSetChanged();
  }

  public RecyclerAdapter<MODEL> getOriginAdapter() {
    return mOriginAdapter;
  }

  public RecyclerView getRecyclerView() {
    return mRecyclerView;
  }

  public void setRefreshEnable(boolean enable) {
    mRefreshLayout.setEnabled(enable);
  }

  protected boolean autoLoad() {
    return true;
  }
}
