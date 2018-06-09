package com.sf.sofarmusic.demo.list.sort;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sf.sofarmusic.R;
import com.sf.sofarmusic.demo.enity.BankItem;
import com.sf.sofarmusic.util.ResUtil;
import com.sf.sofarmusic.util.ToastUtil;

import java.util.List;


public class BankListAdapter extends
        RecyclerView.Adapter<ViewHolder> {

    private Context mContext;
    private List<BankItem> mBankList;
    private int mNoHotFirstIndex=-1;   //非热门银行第一次出现的位置


    public BankListAdapter(Context context, List<BankItem> bankList) {
        mBankList = bankList;
        mContext = context;

        //初始化热门银行第一次出现的位置
        for(int i=0;i<mBankList.size();i++){
            if(!mBankList.get(i).isHot){
                mNoHotFirstIndex=i;
                return;
            }
        }
    }

    public void updateList(List<BankItem> bankList){
        mBankList=bankList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        // TODO Auto-generated method stub
        return mBankList.size();
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final BankItem item = mBankList.get(position);

        if (holder instanceof ItemHolder) {
            if (item.isHot) {
                if (item.isShowHotTitle) {
                    ((ItemHolder) holder).letter_tv.setText("热门银行");
                    ((ItemHolder) holder).letter_tv.setVisibility(View.VISIBLE);
                } else {
                    ((ItemHolder) holder).letter_tv.setVisibility(View.GONE);
                }
            } else {
                int section = getSectionForPosition(position);
                if (position == getPositionForSection(section)) {
                    ((ItemHolder) holder).letter_tv.setText(item.letter);
                    ((ItemHolder) holder).letter_tv.setVisibility(View.VISIBLE);
                } else {
                    ((ItemHolder) holder).letter_tv.setVisibility(View.GONE);
                }
            }



            ((ItemHolder) holder).bank_tv.setText(item.name);
            int imgId = ResUtil.getDrawableId(mContext, item.imgName);
            Drawable drawable = null;
            try {
                drawable = mContext.getResources().getDrawable(imgId);
            } catch (Resources.NotFoundException e) {

            }
            if (drawable == null) {
                ((ItemHolder) holder).bank_iv.setImageResource(R.mipmap.ic_launcher);
            } else {
                ((ItemHolder) holder).bank_iv.setImageResource(ResUtil.getDrawableId(mContext, item.imgName));
            }


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtil.startShort(mContext,item.name);
                }
            });

        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_bank_item, parent, false);
        return new ItemHolder(view);

    }


    class ItemHolder extends ViewHolder {
        TextView letter_tv, bank_tv;
        ImageView bank_iv;

        public ItemHolder(View itemView) {
            super(itemView);
            letter_tv = (TextView) itemView.findViewById(R.id.letter_tv);
            bank_tv = (TextView) itemView.findViewById(R.id.bank_tv);
            bank_iv = (ImageView) itemView.findViewById(R.id.bank_iv);
        }

    }

    // 获取当前位置的首字母(int表示ascii码)
    public int getSectionForPosition(int position) {
        return mBankList.get(position).letter.charAt(0);
    }

    // 获取字母首次出现的位置
    public int getPositionForSection(int section) {
        for (int i = mNoHotFirstIndex; i < mBankList.size(); i++) {
            String s = mBankList.get(i).letter;
            char firstChar = s.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }


}
