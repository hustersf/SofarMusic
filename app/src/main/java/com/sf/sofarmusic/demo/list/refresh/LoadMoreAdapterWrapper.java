package com.sf.sofarmusic.demo.list.refresh;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sf.sofarmusic.R;

/**
 * Created by sufan on 2017/11/6.
 * 加载更多的包装类
 */

public class LoadMoreAdapterWrapper extends RecyclerView.Adapter {

    private RecyclerView.Adapter mAdapter;

    private final int TYPE_ITEM = 0;
    private final int TYPE_FOOTER = 1;

    private int loadState = 1;
    public static final int LOADING = 0;   //正在加载
    public static final int LOADING_FINISH = 1;   //加载完成
    public static final int LOADING_NONE = 2;    //没有更多数据


    public LoadMoreAdapterWrapper(RecyclerView.Adapter adapter) {
        mAdapter = adapter;
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_FOOTER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_recycler_footer, parent, false);
            return new FooterViewHolder(view);
        } else {
            return mAdapter.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof FooterViewHolder) {
            FooterViewHolder footerViewHolder = (FooterViewHolder) holder;
            switch (loadState) {
                case LOADING:
                    setVisibility(true, footerViewHolder.itemView);
                    footerViewHolder.tv_more.setText("正在加载...");
                    break;
                case LOADING_FINISH:
                    setVisibility(false, footerViewHolder.itemView);
                    break;
                case LOADING_NONE:
                    setVisibility(true, footerViewHolder.itemView);
                    footerViewHolder.tv_more.setText("------我是有底线的------");
                    break;
            }
        } else {
            mAdapter.onBindViewHolder(holder, position);
        }

    }

    @Override
    public int getItemCount() {
        return mAdapter.getItemCount() + 1;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) manager;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return getItemViewType(position) == TYPE_FOOTER ? gridLayoutManager.getSpanCount() : 1;
                }
            });
        }
    }

    class FooterViewHolder extends RecyclerView.ViewHolder {
        TextView tv_more;

        public FooterViewHolder(View itemView) {
            super(itemView);
            tv_more = (TextView) itemView.findViewById(R.id.tv_more);
        }
    }

    public void updateLoadStatus(int loadState) {
        this.loadState = loadState;
        notifyDataSetChanged();
    }


    //解决明明是GONE,为毛是INVISIBLE的效果
    public void setVisibility(boolean isVisible, View itemView) {
        RecyclerView.LayoutParams param = (RecyclerView.LayoutParams) itemView.getLayoutParams();
        if (isVisible) {
            param.height = RelativeLayout.LayoutParams.WRAP_CONTENT;// 这里注意使用自己布局的根布局类型
            param.width = RelativeLayout.LayoutParams.MATCH_PARENT;// 这里注意使用自己布局的根布局类型
            itemView.setVisibility(View.VISIBLE);
        } else {
            itemView.setVisibility(View.GONE);
            param.height = 1;
            param.width = 1;

            // 当最后一一个ItemView高度为0的时候,recyclerView.canScrollVertically(1)就不能判断是否已经滑到底部了！
        }
        itemView.setLayoutParams(param);
    }
}
