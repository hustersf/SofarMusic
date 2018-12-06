package com.sf.sofarmusic.local;

import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sf.base.util.FontUtil;
import com.sf.sofarmusic.R;
import com.sf.sofarmusic.enity.ArtistItem;

/**
 * Created by sufan on 16/11/23.
 */

public class ArtistAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<ArtistItem> mArtistList;
    private Typeface mIconFont;

    private OnItemClickListener mOnItemClickListener;


    public ArtistAdapter(Context context, List<ArtistItem> artistList) {
        mContext = context;
        mArtistList = artistList;
        mIconFont = FontUtil.setFont(mContext);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_artist_item, parent, false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        ArtistItem item = mArtistList.get(position);
        final int realPosition = holder.getLayoutPosition();

        if (holder instanceof ItemHolder) {

            Glide.with(mContext).load(item.extraLargeUrl)
                    .placeholder(R.drawable.placeholder_disk_210)
                    .error(R.drawable.placeholder_disk_210)
                    .into(((ItemHolder) holder).artist_iv);
            ((ItemHolder) holder).artist_name_tv.setText(item.name);
            ((ItemHolder) holder).artist_count_tv.setText(item.artistList.size() + "首");

            if(item.isSelected){
                ((ItemHolder) holder).voice_tv.setVisibility(View.VISIBLE);
                ((ItemHolder) holder).dot_tv.setVisibility(View.GONE);
            }else {
                ((ItemHolder) holder).voice_tv.setVisibility(View.GONE);
                ((ItemHolder) holder).dot_tv.setVisibility(View.VISIBLE);
            }

            if(mOnItemClickListener!=null){
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mOnItemClickListener.OnArtistItem(realPosition);
                    }
                });
            }
        }

    }

    @Override
    public int getItemCount() {
        return mArtistList.size();
    }


    class ItemHolder extends RecyclerView.ViewHolder {

        ImageView artist_iv;
        TextView artist_name_tv, artist_count_tv, dot_tv,voice_tv;

        public ItemHolder(View itemView) {
            super(itemView);
            artist_iv = (ImageView) itemView.findViewById(R.id.artist_iv);
            artist_name_tv = (TextView) itemView.findViewById(R.id.artist_name_tv);
            artist_count_tv = (TextView) itemView.findViewById(R.id.artist_count_tv);

            dot_tv = (TextView) itemView.findViewById(R.id.dot_tv);
            voice_tv = (TextView) itemView.findViewById(R.id.voice_tv);

            dot_tv.setTypeface(mIconFont);
            voice_tv.setTypeface(mIconFont);

        }
    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }


    public interface OnItemClickListener {
        void OnArtistItem(int position);
    }


}
