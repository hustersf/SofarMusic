package com.sf.sofarmusic.demo.list.refresh;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sf.sofarmusic.R;

import java.util.List;

/**
 * Created by sufan on 2017/11/6.
 */

public class LoadMoreAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<String> mDatas;

    public LoadMoreAdapter(Context context, List<String> datas) {
        mContext = context;
        mDatas = datas;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_load_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            ((ItemViewHolder) holder).tv_letter.setText(mDatas.get(position));
        }

    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tv_letter;

        public ItemViewHolder(View itemView) {
            super(itemView);
            tv_letter = (TextView) itemView.findViewById(R.id.tv_letter);
        }
    }
}
