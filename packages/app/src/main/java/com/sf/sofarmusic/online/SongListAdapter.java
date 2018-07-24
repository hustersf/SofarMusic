package com.sf.sofarmusic.online;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.sf.libskin.config.SkinConfig;
import com.sf.sofarmusic.R;
import com.sf.sofarmusic.enity.PlayItem;
import com.sf.base.util.FontUtil;
import com.sf.utility.ToastUtil;

import java.util.List;

/**
 * Created by sufan on 16/11/23.
 */

public class SongListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<PlayItem> mPlayList;
    private Typeface mIconFont;
    private String mImgUrl;
    private int mHeadType;

    private OnItemClickListener mOnItemClickListener;
    private ColorCallback mCallback;


    private int mColor;
    private int mSelectedPos = -1;

    private final static int TYPE_HEAD1 = 0;
    private final static int TYPE_HEAD2 = 1;
    private final static int TYPE_TITLE = 2;
    private final static int TYPE_ITEM = 3;


    public SongListAdapter(Context context, List<PlayItem> playList, String imgUrl, int headType) {
        mContext = context;
        mPlayList = playList;
        mImgUrl = imgUrl;
        mIconFont = FontUtil.setFont(mContext);
        mHeadType = headType;

        initmSelectedPos();
    }

    public void refreshList(int i) {
        if (mSelectedPos != i) {
            if (mSelectedPos != -1) {
                mPlayList.get(mSelectedPos).isSelected = false;
                notifyItemChanged(mSelectedPos + 2);
            }

            //设置新的mSelectedPos
            mSelectedPos = i;
            mPlayList.get(mSelectedPos).isSelected = true;
            notifyItemChanged(mSelectedPos + 2);
        }

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEAD1) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_songlist_head1, parent, false);
            return new HeadHolder1(view);
        }
        if (viewType == TYPE_HEAD2) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_songlist_head2, parent, false);
            return new HeadHolder2(view);
        } else if (viewType == TYPE_TITLE) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_songlist_title, parent, false);
            return new TitleHolder(view);
        } else if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_songlist_item, parent, false);
            return new ItemHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        final int realPosition = holder.getLayoutPosition();
        if (holder instanceof HeadHolder1) {

            Glide.with(mContext).load(mImgUrl).asBitmap().placeholder(R.drawable.placeholder_disk_210).into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {

                    ((HeadHolder1) holder).rank_head_bg.setImageBitmap(bitmap);
                    Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                        @Override
                        public void onGenerated(Palette palette) {
                            int defaultColor = mContext.getResources().getColor(R.color.transparent);
                            int dominantColor = palette.getDominantColor(defaultColor);
                            mColor = dominantColor;
                            if(mCallback!=null){
                                mCallback.OnColor(mColor);
                            }
                        }
                    });

                }
            });

        } else if (holder instanceof HeadHolder2) {

            Glide.with(mContext).load(mImgUrl).asBitmap().placeholder(R.drawable.placeholder_disk_210).into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {

                    ((HeadHolder2) holder).rank_head_bg.setImageBitmap(bitmap);
                    Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                        @Override
                        public void onGenerated(Palette palette) {
                            int defaultColor = mContext.getResources().getColor(R.color.transparent);
                            int dominantColor = palette.getDominantColor(defaultColor);
                            mColor = dominantColor;
                            holder.itemView.setBackgroundColor(mColor);
                            if(mCallback!=null){
                                mCallback.OnColor(mColor);
                            }
                        }
                    });
                }
            });

        } else if (holder instanceof TitleHolder) {
            String text = "(共" + (mPlayList.size()) + "首)";
            ((TitleHolder) holder).count_tv.setText(text);
            if (mOnItemClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mSelectedPos != -1) {
                            mPlayList.get(mSelectedPos).isSelected = false;
                            notifyItemChanged(mSelectedPos + 2);
                        }
                        //设置新的mSelectedPos
                        mSelectedPos = 0;
                        mPlayList.get(mSelectedPos).isSelected = true;
                        notifyItemChanged(mSelectedPos + 2);
                        mOnItemClickListener.OnRankListItem(realPosition);
                    }
                });
            }

            ((TitleHolder) holder).more_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ToastUtil.startShort(mContext, "more");
                }
            });

        } else if (holder instanceof ItemHolder) {
            PlayItem item = mPlayList.get(position - 2);
            String order = (position - 1) + "";
            ((ItemHolder) holder).order_tv.setText(order);
            ((ItemHolder) holder).name_tv.setText(item.name);
            ((ItemHolder) holder).artist_tv.setText(item.artist);
            if (!item.isImport) {
                ((ItemHolder) holder).order_tv.setTextColor(mContext.getResources().getColor(R.color.text_gray));
            } else {
                ((ItemHolder) holder).order_tv.setTextColor(SkinConfig.skinColor);
            }

            if (item.isSelected) {
                ((ItemHolder) holder).order_tv.setVisibility(View.GONE);
                ((ItemHolder) holder).voice_tv.setVisibility(View.VISIBLE);
            } else {
                ((ItemHolder) holder).order_tv.setVisibility(View.VISIBLE);
                ((ItemHolder) holder).voice_tv.setVisibility(View.GONE);
            }

            if (mOnItemClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mSelectedPos != realPosition - 2) {
                            if (mSelectedPos != -1) {
                                mPlayList.get(mSelectedPos).isSelected = false;
                                notifyItemChanged(mSelectedPos + 2);
                            }

                            //设置新的mSelectedPos
                            mSelectedPos = realPosition - 2;
                            mPlayList.get(mSelectedPos).isSelected = true;
                            notifyItemChanged(mSelectedPos + 2);
                        }
                        mOnItemClickListener.OnRankListItem(realPosition);

                    }
                });
            }

        }
    }

    @Override
    public int getItemCount() {
        return mPlayList.size() + 2;
    }


    class HeadHolder1 extends RecyclerView.ViewHolder {
        ImageView rank_head_bg;

        public HeadHolder1(View itemView) {
            super(itemView);
            rank_head_bg = (ImageView) itemView.findViewById(R.id.rank_head_bg);
        }
    }

    class HeadHolder2 extends RecyclerView.ViewHolder {
        ImageView rank_head_bg;

        public HeadHolder2(View itemView) {
            super(itemView);
            rank_head_bg = (ImageView) itemView.findViewById(R.id.rank_head_bg);
        }
    }

    class TitleHolder extends RecyclerView.ViewHolder {
        TextView play_tv, count_tv, more_tv;

        public TitleHolder(View itemView) {
            super(itemView);
            play_tv = (TextView) itemView.findViewById(R.id.play_tv);
            count_tv = (TextView) itemView.findViewById(R.id.count_tv);
            more_tv = (TextView) itemView.findViewById(R.id.more_tv);

            play_tv.setTypeface(mIconFont);
            more_tv.setTypeface(mIconFont);
        }
    }

    class ItemHolder extends RecyclerView.ViewHolder {

        TextView order_tv, voice_tv, name_tv, artist_tv, dot_tv;

        public ItemHolder(View itemView) {
            super(itemView);
            order_tv = (TextView) itemView.findViewById(R.id.order_tv);
            voice_tv = (TextView) itemView.findViewById(R.id.voice_tv);
            name_tv = (TextView) itemView.findViewById(R.id.name_tv);
            artist_tv = (TextView) itemView.findViewById(R.id.artist_tv);
            dot_tv = (TextView) itemView.findViewById(R.id.dot_tv);

            voice_tv.setTypeface(mIconFont);
            dot_tv.setTypeface(mIconFont);

        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            if (mHeadType == 1) {
                return TYPE_HEAD1;
            } else if (mHeadType == 2) {
                return TYPE_HEAD2;
            }
            return 0;
        } else if (position == 1) {
            return TYPE_TITLE;
        } else {
            return TYPE_ITEM;
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }


    public interface OnItemClickListener {
        void OnRankListItem(int position);
    }

    private void initmSelectedPos() {
        for (int i = 0; i < mPlayList.size(); i++) {
            if (mPlayList.get(i).isSelected) {
                mSelectedPos = i;
            }
        }
    }

    public interface ColorCallback{
        public void OnColor(int color);
    }

    public void setOnColorCallback(ColorCallback callback){
        mCallback=callback;
    }



}
