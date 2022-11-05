package com.sf.sofarmusic.menu;

import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.sf.base.util.FontUtil;
import com.sf.sofarmusic.R;
import com.sf.sofarmusic.enity.MenuItem;
import com.sf.utility.ToastUtil;

/**
 * Created by sufan on 16/11/24.
 */

public class PopMenuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<MenuItem> mMenuList;
    private Typeface mIconfont;

    public PopMenuAdapter(Context context, List<MenuItem> menuList) {
        mContext = context;
        mMenuList = menuList;
        mIconfont= FontUtil.setFont(mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_pop_menu, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final MenuItem item=mMenuList.get(position);
        if(holder instanceof MyViewHolder){
            ((MyViewHolder) holder).icon_tv.setText(item.icon);
            ((MyViewHolder) holder).des_tv.setText(item.des);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ToastUtil.startShort(mContext,item.des);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mMenuList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView icon_tv, des_tv;

        public MyViewHolder(View itemView) {
            super(itemView);
            icon_tv = (TextView) itemView.findViewById(R.id.icon_tv);
            des_tv = (TextView) itemView.findViewById(R.id.des_tv);

            icon_tv.setTypeface(mIconfont);
        }
    }
}
