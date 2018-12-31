package com.sf.demo.system.notification;

import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.sf.base.UIRootActivity;
import com.sf.demo.R;

/**
 * 展示通知内容
 */
public class NotifyContentActivity extends UIRootActivity {

  private RecyclerView mNotifyRecyclerView;
  private NotifyContentAdapter mContentAdapter;
  private NotifyListenerHelper mHelper;

  @Override
  protected int getLayoutId() {
    return R.layout.activity_system_notify_content;
  }

  @Override
  protected void initTitle() {
    mHeadTitleTv.setText("通知栏内容");
  }

  @Override
  protected void initView() {
    mNotifyRecyclerView = findViewById(R.id.rv_notify);
    LinearLayoutManager layoutManager = new LinearLayoutManager(this);
    mNotifyRecyclerView.setLayoutManager(layoutManager);
    mContentAdapter = new NotifyContentAdapter(this);
    mNotifyRecyclerView.setAdapter(mContentAdapter);

    DividerItemDecoration itemDecoration =
        new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
    mNotifyRecyclerView.addItemDecoration(itemDecoration);
  }

  @Override
  protected void initData() {
    mHelper = new NotifyListenerHelper(this, mContentAdapter);
    if (!mHelper.isEnabled()) {
      mHelper.openSetting();
    }
    mHelper.toggleNotificationListenerService();
  }

  @Override
  protected void initEvent() {

  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    mHelper.unRegistBroadCast();
  }
}
