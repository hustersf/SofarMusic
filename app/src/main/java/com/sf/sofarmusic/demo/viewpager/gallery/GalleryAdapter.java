package com.sf.sofarmusic.demo.viewpager.gallery;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.sf.sofarmusic.R;
import com.sf.sofarmusic.demo.view.RoundImageView;

import java.util.List;

/**
 * Created by sufan on 17/6/27.
 */

public class GalleryAdapter extends VPAdapter<GalleryInfo> {

    private View view;
    private ViewHolder holder=null;

    public GalleryAdapter(Context context, List<GalleryInfo> datas) {
        super(context, datas);
    }

    @Override
    public View getItemView(GalleryInfo data) {
//        if(view==null){
//            view= LayoutInflater.from(mContext).inflate(R.layout.adapter_gallery,null);
//            holder=new ViewHolder();
//            holder.skin_iv=(RoundImageView)view.findViewById(R.id.skin_iv);
//            holder.skin_tv=(TextView)view.findViewById(R.id.skin_tv);
//            view.setTag(holder);
//        }else {
//            holder=(ViewHolder) view.getTag();
//        }

        view= LayoutInflater.from(mContext).inflate(R.layout.adapter_gallery,null);
        holder=new ViewHolder();
        holder.skin_iv=(RoundImageView)view.findViewById(R.id.skin_iv);
        holder.skin_tv=(TextView)view.findViewById(R.id.skin_tv);

        holder.skin_tv.setText(data.name);
        holder.skin_iv.setImageResource(data.imgId);

        return view;
    }


    class ViewHolder {
        TextView skin_tv;
        RoundImageView skin_iv;
    }

}
