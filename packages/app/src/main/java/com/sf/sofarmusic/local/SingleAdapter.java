package com.sf.sofarmusic.local;

import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sf.base.util.FontUtil;
import com.sf.sofarmusic.R;
import com.sf.sofarmusic.enity.PlayItem;
import com.sf.utility.ToastUtil;

/**
 * Created by sufan on 16/11/23.
 */

public class SingleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<PlayItem> mPlayList;
    private Typeface mIconFont;

    private OnItemClickListener mOnItemClickListener;

    private int mSelectedPos = -1;

    private final  int TYPE_TITLE = 0;
    private final  int TYPE_ITEM = 1;


    public SingleAdapter(Context context, List<PlayItem> playList) {
        mContext = context;
        mPlayList = playList;
        mIconFont = FontUtil.setFont(mContext);

        initSelectedPos();
    }


    //刷新列表状态
    public void refreshList(int i) {
        if (mSelectedPos != i) {
            if (mSelectedPos != -1) {
                mPlayList.get(mSelectedPos).isSelected = (false);
                notifyItemChanged(mSelectedPos + 1);
            }

            //设置新的mSelectedPos
            mSelectedPos = i;
            mPlayList.get(mSelectedPos).isSelected = (true);
            notifyItemChanged(mSelectedPos + 1);
        }

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_TITLE) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.rank_detail_list_head, parent, false);
            return new TitleHolder(view);
        } else if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_single_item, parent, false);
            return new ItemHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof TitleHolder) {
            String text = "(共" + (mPlayList.size()) + "首)";
            ((TitleHolder) holder).count_tv.setText(text);
            if (mOnItemClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        refreshList(0);
                        mOnItemClickListener.OnSingleItem(position);
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
            PlayItem item = mPlayList.get(position - 1);
            ((ItemHolder) holder).name_tv.setText(item.name);
            ((ItemHolder) holder).player_tv.setText(item.artist);


            if (item.isSelected) {
                ((ItemHolder) holder).voice_tv.setVisibility(View.VISIBLE);
            } else {
                ((ItemHolder) holder).voice_tv.setVisibility(View.GONE);
            }

            if (mOnItemClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mSelectedPos != position - 1) {
                            refreshList(position - 1);
                        }
                        mOnItemClickListener.OnSingleItem(position);
                    }
                });
            }

        }
    }

    @Override
    public int getItemCount() {
        return mPlayList.size() + 1;
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

        TextView voice_tv, name_tv, player_tv, dot_tv;

        public ItemHolder(View itemView) {
            super(itemView);
            voice_tv = (TextView) itemView.findViewById(R.id.voice_tv);
            name_tv = (TextView) itemView.findViewById(R.id.name_tv);
            player_tv = (TextView) itemView.findViewById(R.id.player_tv);
            dot_tv = (TextView) itemView.findViewById(R.id.dot_tv);

            voice_tv.setTypeface(mIconFont);
            dot_tv.setTypeface(mIconFont);

        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_TITLE;
        } else {
            return TYPE_ITEM;
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }


    public interface OnItemClickListener {
        void OnSingleItem(int position);
    }

    private void initSelectedPos() {
        for (int i = 0; i < mPlayList.size(); i++) {
            if (mPlayList.get(i).isSelected) {
                mSelectedPos = i;
            }
        }
    }


}
