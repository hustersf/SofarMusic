package com.sf.demo.md;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.sf.demo.R;


public class TypeListAdapter extends RecyclerView.Adapter<TypeListAdapter.ItemViewHolder> {

    private Context mContext;
    private List<String> mDatas;
    private List<Integer> mHeights;
    private OnItemClickListener mListner;

    public TypeListAdapter(Context context,List<String> datas) {
        if (datas != null) {
            mDatas = datas;
        } else {
            mDatas = new ArrayList<>();
        }
        mHeights = new ArrayList<>();
        for (int i = 0; i < mDatas.size(); i++) {
            mHeights.add((int) (260 + Math.random() * 100));
        }

        mContext=context;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_type_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, final int position) {
        ViewGroup.LayoutParams params = holder.item_tv.getLayoutParams();
        params.height = mHeights.get(position);
        holder.item_tv.setLayoutParams(params);
        holder.item_tv.setText(mDatas.get(position));
        if (mListner != null) {
            holder.item_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListner.onItemClick(mDatas.get(position), position,holder.itemView);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView item_tv;
        RelativeLayout item_layout;

        public ItemViewHolder(View itemView) {
            super(itemView);
            item_tv=(TextView)itemView.findViewById(R.id.item_tv);
            item_layout=(RelativeLayout)itemView.findViewById(R.id.item_layout);

        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListner = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(String data, int position, View itemView);
    }
}
