package com.sf.sofarmusic.demo.picker.city2;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sf.sofarmusic.R;

import java.util.List;


/**
 * Created by sufan on 17/6/15.
 */

public class TextListAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<TextItem> mTextList;

    private int mSelectedPos = -1;

    private OnItemClickListener mOnItemClickListener;
    private int mTag = 0;   //0 代表省   1代表市    2代表县／区


    public TextListAdapter(Context context, List<TextItem> textList) {
        mContext = context;
        mTextList = textList;
        initSelectedPos();
    }

    //局部刷新
    public void refreshList(int i) {
        if (mSelectedPos != i) {
            if (mSelectedPos != -1) {
                mTextList.get(mSelectedPos).isSelected = false;
                notifyItemChanged(mSelectedPos);
            }
            //设置新的mSelectedPos
            mSelectedPos = i;
            mTextList.get(mSelectedPos).isSelected = true;
            notifyItemChanged(mSelectedPos);
        }

    }

    public void updateData(List<TextItem> textList, int tag) {
        mTag = tag;
        mTextList = textList;
        initSelectedPos();
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(
                R.layout.adapter_text_item, parent, false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final TextItem item = mTextList.get(position);
        if (holder instanceof ItemHolder) {
            ((ItemHolder) holder).text_tv.setText(item.text);
            if (item.isSelected) {
                ((ItemHolder) holder).select_iv.setVisibility(View.VISIBLE);
            } else {
                ((ItemHolder) holder).select_iv.setVisibility(View.GONE);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    refreshList(position);
                    //回调给CityPicker
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.OnItemClick(position, item.text, mTag);
                    }
                }
            });


        }


    }

    @Override
    public int getItemCount() {
        return mTextList.size();
    }

    class ItemHolder extends RecyclerView.ViewHolder {
        ImageView select_iv;
        TextView text_tv;

        public ItemHolder(View itemView) {
            super(itemView);
            text_tv = (TextView) itemView.findViewById(R.id.text_tv);
            select_iv = (ImageView) itemView.findViewById(R.id.select_iv);

        }
    }


    private void initSelectedPos() {
        mSelectedPos = -1;
        for (int i = 0; i < mTextList.size(); i++) {
            if (mTextList.get(i).isSelected) {
                mSelectedPos = i;
            }
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void OnItemClick(int position, String text, int tag);
    }

}
