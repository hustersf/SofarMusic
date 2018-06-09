package com.sf.sofarmusic.play;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sf.libskin.config.SkinConfig;
import com.sf.sofarmusic.R;
import com.sf.sofarmusic.enity.PlayItem;
import com.sf.sofarmusic.util.FontUtil;
import com.sf.sofarmusic.util.LogUtil;

import java.util.List;

/**
 * Created by sufan on 16/11/18.
 */

public class PlayListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<PlayItem> mPlayList;
    private OnItemClickListener mOnItemClickListener;

    private int mSelectedPos = -1;


    public PlayListAdapter(Context context, List<PlayItem> playList) {
        mContext = context;
        mPlayList = playList;
        initSelectedPos();
    }

    public void refreshList(int position) {

        if (mSelectedPos != -1) {
            if (mSelectedPos != position) {
                mPlayList.get(mSelectedPos).isSelected = false;
                notifyItemChanged(mSelectedPos);

                //设置新的mSelectedPos
                mSelectedPos = position;
                mPlayList.get(mSelectedPos).isSelected = true;
                notifyItemChanged(mSelectedPos);
            }
        }
    }

    public void setNewData(List<PlayItem> playList) {
        mPlayList = playList;
        initSelectedPos();
        notifyDataSetChanged();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_sheet_play_list, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        PlayItem item = mPlayList.get(position);
        if (holder instanceof ItemViewHolder) {
            ((ItemViewHolder) holder).content_tv.setText(item.name + "-" + item.artist);

            ((ItemViewHolder) holder).xx_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        //删除某一条item
                        mPlayList.get(position).isSelected = false;  //删除之前先将其置为false,很重要
                        mPlayList.remove(position);
                        notifyItemRemoved(position);  //该方法不会使position及其之后位置的vitemiew重新onBindViewHolder
                        notifyItemRangeChanged(0, mPlayList.size()); //很重要，否则当前position之后的item得不到正确的postion值

                        //每删除一个，必须要重新确定mSelectedPos的值
                        initSelectedPos();
                        //如果是最后一首选中第一首
                        //      Log.i("TAG","mSelectedPos:"+mSelectedPos+"  realPosition："+realPosition+" size:"+mList.size());
                        if (mPlayList.size() == 0) {
                            //             mOnItemClickListener.onItemClick(realPosition, "three");

                        } else if (mSelectedPos == mPlayList.size()) {
                            mSelectedPos = 0;
                            mPlayList.get(mSelectedPos).isSelected = true;
                            notifyItemChanged(mSelectedPos);
                        }

                        //删除之后，自动选中下一首
                        else if (mSelectedPos == position) {
                            mSelectedPos = position;
                            mPlayList.get(mSelectedPos).isSelected = true;
                            notifyItemChanged(mSelectedPos);
                        }
                        mOnItemClickListener.onItemClick(position);
                    }
                }
            });

            //优雅的实现单选
            ((ItemViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        refreshList(position);
                        mOnItemClickListener.onItemClick(position);
                    }
                }
            });

            if (item.isSelected) {
                ((ItemViewHolder) holder).img_tv.setVisibility(View.VISIBLE);
                ((ItemViewHolder) holder).content_tv.setTextColor(SkinConfig.skinColor);
            } else {
                ((ItemViewHolder) holder).img_tv.setVisibility(View.GONE);
                ((ItemViewHolder) holder).content_tv.setTextColor(mContext.getResources().getColor(R.color.text_gray));
            }
        }


    }

    @Override
    public int getItemCount() {
        return mPlayList.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView img_tv, xx_tv, content_tv;

        public ItemViewHolder(View itemView) {
            super(itemView);
            img_tv = (TextView) itemView.findViewById(R.id.img_tv);
            xx_tv = (TextView) itemView.findViewById(R.id.xx_tv);
            content_tv = (TextView) itemView.findViewById(R.id.content_tv);

            Typeface iconfont = FontUtil.setFont(mContext);
            img_tv.setTypeface(iconfont);
            xx_tv.setTypeface(iconfont);
        }
    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;

    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    private void initSelectedPos() {
        for (int i = 0; i < mPlayList.size(); i++) {
            if (mPlayList.get(i).isSelected) {
                mSelectedPos = i;
            }
        }
    }

    private void test() {
        for (int i = 0; i < mPlayList.size(); i++) {
            if (mPlayList.get(i).isSelected) {
                LogUtil.d("adapter-selectI:" + i);
            }
        }
    }
}
