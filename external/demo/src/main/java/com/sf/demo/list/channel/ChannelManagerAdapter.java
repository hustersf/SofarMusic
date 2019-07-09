package com.sf.demo.list.channel;

import android.view.View;

import com.sf.widget.recyclerview.RecyclerAdapter;
import com.sf.widget.recyclerview.RecyclerViewHolder;

import java.util.List;

public class ChannelManagerAdapter extends RecyclerAdapter<ChannelInfo> {

  private List<ChannelInfo> mMyChannels; // 我的频道
  private List<ChannelInfo> mOtherChannels; // 其它频道

  private static final int MY_CHANNEL_HEAD_COUNT = 1; // 我的频道头部数量
  private static final int OTHER_CHANNEL_HEAD_COUNT = 1; // 其它频道头部数量

  private static final int TYPE_MY_CHANNEL_HEADER = 0; // 我的频道头部
  private static final int TYPE_MY_CHANNEL = 1; // 我的频道
  private static final int TYPE_OTHER_CHANNEL_HEADER = 2; // 其它频道头部
  private static final int TYPE_OTHER_CHANNEL = 3; // 其它频道
  private static final int TYPE_MY_CHANNEL_FIXED = 4; // 我的频道不可删除

  public ChannelManagerAdapter(List<ChannelInfo> myChannels, List<ChannelInfo> otherChannels) {
    mMyChannels = myChannels;
    mOtherChannels = otherChannels;
  }

  @Override
  public int getItemViewType(int position) {
    if (position == 0) {
      return TYPE_MY_CHANNEL_HEADER;
    } else if (position > 0 && position <= mMyChannels.size()) {
      ChannelInfo ci = mMyChannels.get(position - MY_CHANNEL_HEAD_COUNT);
      if (ci != null && ci.isFixed) {
        return TYPE_MY_CHANNEL_FIXED;
      } else {
        return TYPE_MY_CHANNEL;
      }
    } else if (position == mMyChannels.size() + TYPE_MY_CHANNEL_HEADER) {
      return TYPE_OTHER_CHANNEL_HEADER;
    } else {
      return TYPE_OTHER_CHANNEL;
    }
  }

  @Override
  public int getItemCount() {
    return mMyChannels.size() + mOtherChannels.size() + MY_CHANNEL_HEAD_COUNT
        + OTHER_CHANNEL_HEAD_COUNT;
  }

  @Override
  protected int getItemLayoutId(int viewType) {
    return 0;
  }

  @Override
  protected RecyclerViewHolder onCreateViewHolder(int viewType, View itemView) {
    return null;
  }
}
