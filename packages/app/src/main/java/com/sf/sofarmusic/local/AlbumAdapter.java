package com.sf.sofarmusic.local;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sf.sofarmusic.R;
import com.sf.sofarmusic.enity.AlbumItem;
import com.sf.sofarmusic.util.FontUtil;

import java.util.List;

/**
 * Created by sufan on 16/11/23.
 */

public class AlbumAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<AlbumItem> mAlbumList;
    private Typeface mIconFont;

    private OnItemClickListener mOnItemClickListener;


    public AlbumAdapter(Context context, List<AlbumItem> albumList) {
        mContext = context;
        mAlbumList = albumList;
        mIconFont = FontUtil.setFont(mContext);

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_album_item, parent, false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        AlbumItem item = mAlbumList.get(position);
        final int realPosition = holder.getLayoutPosition();

        if (holder instanceof ItemHolder) {
            Glide.with(mContext).load(item.imgUri).error(R.drawable.placeholder_disk_210).into(((ItemHolder) holder).album_iv);
            ((ItemHolder) holder).album_name_tv.setText(item.albumName);
            ((ItemHolder) holder).album_artist_tv.setText(item.artistName);
            ((ItemHolder) holder).album_count_tv.setText(item.albumList.size() + "首");


            if (item.isSelected) {
                ((ItemHolder) holder).voice_tv.setVisibility(View.VISIBLE);
                ((ItemHolder) holder).dot_tv.setVisibility(View.GONE);
            } else {
                ((ItemHolder) holder).voice_tv.setVisibility(View.GONE);
                ((ItemHolder) holder).dot_tv.setVisibility(View.VISIBLE);
            }


            if (mOnItemClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mOnItemClickListener.OnAlbumItem(realPosition);
                    }
                });
            }
        }

    }

    @Override
    public int getItemCount() {
        return mAlbumList.size();
    }


    class ItemHolder extends RecyclerView.ViewHolder {

        ImageView album_iv;
        TextView album_name_tv, album_count_tv, album_artist_tv, dot_tv, voice_tv;

        public ItemHolder(View itemView) {
            super(itemView);
            album_iv = (ImageView) itemView.findViewById(R.id.album_iv);
            album_name_tv = (TextView) itemView.findViewById(R.id.album_name_tv);
            album_count_tv = (TextView) itemView.findViewById(R.id.album_count_tv);
            album_artist_tv = (TextView) itemView.findViewById(R.id.album_artist_tv);
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
        void OnAlbumItem(int position);
    }


}
