package com.sf.demo.adapter;

import java.util.List;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sf.demo.R;
import com.sf.demo.enity.PayInfo;


/**
 * Created by sufan on 17/6/15.
 */

public class PayListAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<PayInfo> mPayList;
    private OnItemCliclkListener mOnItemCliclkListener;

    public PayListAdapter(Context context, List<PayInfo> payList) {
        mContext = context;
        mPayList = payList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(
                R.layout.adapter_pay_list, parent, false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final PayInfo item = mPayList.get(position);
        final int realPosition = holder.getLayoutPosition();
        if (holder instanceof ItemHolder) {

            String cardNo4=item.bankCard.substring(item.bankCard.length()-4);
            String text=item.bankName+item.bankType+"("+cardNo4+")";
            ((ItemHolder) holder).pay_tv.setText(text);

            if(item.isSelected){
                ((ItemHolder) holder).select_iv.setVisibility(View.VISIBLE);
                ((ItemHolder) holder).unselect_iv.setVisibility(View.GONE);
            }else{
                ((ItemHolder) holder).select_iv.setVisibility(View.GONE);
                ((ItemHolder) holder).unselect_iv.setVisibility(View.VISIBLE);
            }

            if (mOnItemCliclkListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        mOnItemCliclkListener.OnItemClick(item);
                    }
                });
            }
        }


    }

    @Override
    public int getItemCount() {
        return mPayList.size();
    }

    class ItemHolder extends RecyclerView.ViewHolder {
        ImageView pay_iv,select_iv,unselect_iv;
        TextView pay_tv;
        public ItemHolder(View itemView) {
            super(itemView);
            pay_tv = (TextView) itemView.findViewById(R.id.pay_tv);
            pay_iv=(ImageView)itemView.findViewById(R.id.pay_iv);
            select_iv=(ImageView)itemView.findViewById(R.id.select_iv);
            unselect_iv=(ImageView)itemView.findViewById(R.id.unselect_iv);
        }
    }


    public interface OnItemCliclkListener {
        public void OnItemClick(PayInfo item);
    }

    public void setOnItemClickListener(OnItemCliclkListener onItemCliclkListener) {
        mOnItemCliclkListener = onItemCliclkListener;
    }
}
