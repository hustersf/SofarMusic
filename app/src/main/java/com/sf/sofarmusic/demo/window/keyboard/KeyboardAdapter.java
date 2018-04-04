package com.sf.sofarmusic.demo.window.keyboard;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sf.sofarmusic.R;

import java.util.List;

/**
 * Created by sufan on 17/6/29.
 */

public class KeyboardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<String> mNumList;

    private OnItemClickListener mListener;

    public KeyboardAdapter(Context context, List<String> numList) {
        mContext = context;
        mNumList = numList;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_keyboard, parent, false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        String key = mNumList.get(position);
        if (holder instanceof ItemHolder) {
            if (position == 9) {
                ((ItemHolder) holder).btn_keys.setBackgroundColor(Color.parseColor("#e0e0e0"));
                ((ItemHolder) holder).imgDelete.setVisibility(View.GONE);
                ((ItemHolder) holder).btn_keys.setText(key);
            } else if (position == 11) {
                ((ItemHolder) holder).btn_keys.setBackgroundColor(Color.parseColor("#e0e0e0"));
                ((ItemHolder) holder).imgDelete.setVisibility(View.VISIBLE);
            } else {
                ((ItemHolder) holder).imgDelete.setVisibility(View.GONE);
                ((ItemHolder) holder).btn_keys.setText(key);
            }

            if (mListener != null) {
                ((ItemHolder) holder).btn_keys.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.OnItemClickListener(position);
                    }
                });
            }

        }
    }

    @Override
    public int getItemCount() {
        return mNumList.size();
    }

    class ItemHolder extends RecyclerView.ViewHolder {

        TextView btn_keys;
        RelativeLayout imgDelete;

        public ItemHolder(View itemView) {
            super(itemView);
            btn_keys = (TextView) itemView.findViewById(R.id.btn_keys);
            imgDelete = (RelativeLayout) itemView.findViewById(R.id.imgDelete);
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public interface OnItemClickListener {
        void OnItemClickListener(int position);
    }
}
