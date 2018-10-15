package com.sf.demo.window.pop;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sf.demo.R;
import com.sf.base.util.ImageUtil;
import com.sf.utility.ToastUtil;

import java.util.List;


/**
 * Created by sufan on 17/6/22.
 */

public class PopListAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<PopInfo> mPopList;

    private OnItemClickListener mOnItemClickListener;

    public PopListAdapter(Context context, List<PopInfo> popList) {
        mContext = context;
        mPopList = popList;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.adapter_pop_list,parent,false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final PopInfo item=mPopList.get(position);
        if(holder instanceof ItemHolder){

            if(!TextUtils.isEmpty(item.name)) {
                ((ItemHolder) holder).menu_tv.setText(item.name);
            }

            ImageUtil.setDrawableByName(mContext,"demo_menu_"+item.imgName,((ItemHolder) holder).menu_iv);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtil.startShort(mContext,item.name);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return mPopList.size();
    }

    class ItemHolder extends RecyclerView.ViewHolder {
        ImageView menu_iv;
        TextView menu_tv;

        public ItemHolder(View itemView) {
            super(itemView);
            menu_iv = (ImageView) itemView.findViewById(R.id.menu_iv);
            menu_tv = (TextView) itemView.findViewById(R.id.menu_tv);
        }
    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        mOnItemClickListener=onItemClickListener;
    }
    public interface OnItemClickListener{
        void OnItemClick(int position, PopInfo item);
    }
}
