package com.sf.sofarmusic.demo.list.page;

import android.content.Context;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sf.sofarmusic.R;
import com.sf.sofarmusic.util.DeviceUtil;
import com.sf.sofarmusic.util.ImageUtil;

import java.util.List;

/**
 * Created by sufan on 17/6/22.
 */

public class PageGridAdapter extends PageGridView.PagingAdapter<ViewHolder>{

    private List<DividePartSubBean> mData;
    private Context mContext;
    private int mColumn;


    public PageGridAdapter(Context context,DividePartBean partBean){
        mContext=context;
        mColumn=partBean.column;
        mData=partBean.list;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.adapter_page_grid,parent,false);
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = DeviceUtil.getMetricsWidth(mContext) / mColumn;
        params.width = DeviceUtil.getMetricsWidth(mContext) / mColumn;
        view.setLayoutParams(params);
        return new PageGridViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DividePartSubBean item=mData.get(position);
        if(holder instanceof PageGridViewHolder){
            if(TextUtils.isEmpty(item.title)){
                ((PageGridViewHolder) holder).title_tv.setVisibility(View.GONE);
            }else {
                ((PageGridViewHolder) holder).title_tv.setText(item.title);
                ((PageGridViewHolder) holder).title_tv.setVisibility(View.VISIBLE);
            }

            if(TextUtils.isEmpty(item.imgName)){
                ((PageGridViewHolder) holder).icon_iv.setVisibility(View.GONE);
            }else {
                ((PageGridViewHolder) holder).icon_iv.setVisibility(View.VISIBLE);
                ImageUtil.setDrawableByName(mContext, "demo_menu_" + item.imgName, ((PageGridViewHolder) holder).icon_iv);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }



    class PageGridViewHolder extends ViewHolder{
        LinearLayout parent_ll;
        ImageView icon_iv;
        TextView title_tv;

        public PageGridViewHolder(View itemView) {
            super(itemView);
            parent_ll=(LinearLayout)itemView.findViewById(R.id.parent_ll);
            icon_iv=(ImageView)itemView.findViewById(R.id.icon_iv);
            title_tv=(TextView)itemView.findViewById(R.id.title_tv);
        }
    }

    @Override
    public void onViewAttachedToWindow(ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
    }

    @Override
    public void onViewDetachedFromWindow(ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
    }

    @Override
    public List getData() {
        return mData;
    }

    @Override
    public Object getEmpty() {
        return new DividePartSubBean();
    }
}
