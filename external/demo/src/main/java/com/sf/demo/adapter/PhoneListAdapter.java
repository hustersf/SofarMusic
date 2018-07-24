package com.sf.demo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sf.demo.R;

import java.util.List;


/**
 * Created by sufan on 17/6/15.
 */

public class PhoneListAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<String> mPhoneList;
    private OnItemCliclkListener mOnItemCliclkListener;

    public PhoneListAdapter(Context context, List<String> phoneList) {
        mContext = context;
        mPhoneList = phoneList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(
                R.layout.adapter_phone_list, parent, false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final String phone = mPhoneList.get(position);
        final int realPosition = holder.getLayoutPosition();
        if (holder instanceof ItemHolder) {

            ((ItemHolder) holder).phone_tv.setText(phone);

            if (mOnItemCliclkListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        mOnItemCliclkListener.OnItemClick(phone);
                    }
                });
            }
        }


    }

    @Override
    public int getItemCount() {
        return mPhoneList.size();
    }

    class ItemHolder extends RecyclerView.ViewHolder {
        TextView phone_tv;

        public ItemHolder(View itemView) {
            super(itemView);
            phone_tv = (TextView) itemView.findViewById(R.id.phone_tv);
        }
    }


    public interface OnItemCliclkListener {
        public void OnItemClick(String phone);
    }

    public void setOnItemClickListener(OnItemCliclkListener onItemCliclkListener) {
        mOnItemCliclkListener = onItemCliclkListener;
    }
}
