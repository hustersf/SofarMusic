package com.sf.sofarmusic.online;

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
import com.sf.sofarmusic.enity.PlayItem;
import com.sf.sofarmusic.enity.RankItem;

/**
 * Created by sufan on 16/11/5.
 */

public class RankListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<RankItem> mRankList;
    private OnItemClickListener mOnItemClickListener;
    private Typeface mIconFont;


    private final static int TYPE_THEME = 0;
    private final static int TYPE_LIST = 1;


    public RankListAdapter(Context context, List<RankItem> rankList) {
        mContext = context;
        mRankList = rankList;
        mIconFont = FontUtil.setFont(mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_THEME) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_skin_theme, parent, false);
            return new ThemeViewHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_rank_list, parent, false);
            return new ListViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        RankItem item = mRankList.get(position);
        if (holder instanceof ThemeViewHolder) {
            ((ThemeViewHolder) holder).theme.setText(item.name);
        } else if (holder instanceof ListViewHolder) {
            Glide.with(mContext).load(item.imgUrl).into(((ListViewHolder) holder).rank_iv);
            List<PlayItem> playList = item.playList;
            if(playList.size()>0) {
                String name1 = playList.get(0).name;
                String player1 = playList.get(0).artist;
                ((ListViewHolder) holder).first_tv.setText("1." + name1 + "-" + player1);
            }

            if(playList.size()>1) {
                String name2 = playList.get(1).name;
                String player2 = playList.get(1).artist;
                ((ListViewHolder) holder).second_tv.setText("2." + name2 + "-" + player2);
            }

            if(playList.size()>2) {
                String name3 = playList.get(2).name;
                String player3 = playList.get(2).artist;
                ((ListViewHolder) holder).third_tv.setText("3." + name3 + "-" + player3);
            }

            final int realPosition = holder.getLayoutPosition();
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onRankItem(realPosition,((ListViewHolder) holder).rank_iv);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mRankList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mRankList.get(position).isTitle) {
            return TYPE_THEME;
        } else {
            return TYPE_LIST;
        }
    }


    class ThemeViewHolder extends RecyclerView.ViewHolder {

        TextView theme;
        TextView icon_tv;

        public ThemeViewHolder(View itemView) {
            super(itemView);
            theme = (TextView) itemView.findViewById(R.id.theme);
            icon_tv = (TextView) itemView.findViewById(R.id.icon_tv);

            icon_tv.setTypeface(mIconFont);

        }
    }

    class ListViewHolder extends RecyclerView.ViewHolder {

        ImageView rank_iv;
        TextView first_tv, second_tv, third_tv;

        public ListViewHolder(View itemView) {
            super(itemView);
            rank_iv = (ImageView) itemView.findViewById(R.id.rank_iv);
            first_tv = (TextView) itemView.findViewById(R.id.first_tv);
            second_tv = (TextView) itemView.findViewById(R.id.second_tv);
            third_tv = (TextView) itemView.findViewById(R.id.third_tv);
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onRankItem(int position,View view);
    }

}
