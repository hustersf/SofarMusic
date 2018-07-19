package com.sf.sofarmusic.skin;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sf.sofarmusic.R;
import com.sf.sofarmusic.enity.SkinItem;
import com.sf.sofarmusic.util.FontUtil;
import com.sf.sofarmusic.widget.recyclerview.CommonDiffCallback;

import java.util.List;


/**
 * Created by sufan on 16/11/5.
 */

public class SkinAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<SkinItem> mSkinList;

    private OnSkinItemClickListener mOnSkinItemClickListener;

    private static final int TYPE_THEME = 0;
    private static final int TYPE_LIST = 1;


    private int mSelectedPos = -1;

    public SkinAdapter(Context context, List<SkinItem> skinList) {
        mContext = context;
        mSkinList = skinList;

        initmSelectedPos();
    }

    //局部刷新
    public void refreshList(int i) {
        if (mSelectedPos != i) {
            if (mSelectedPos != -1) {
                mSkinList.get(mSelectedPos).isSelected = false;
                mSkinList.get(mSelectedPos).status = "";
                notifyItemChanged(mSelectedPos);
            }
            //设置新的mSelectedPos
            mSelectedPos = i;
            mSkinList.get(mSelectedPos).isSelected = true;
            mSkinList.get(mSelectedPos).status = "使用中";
            notifyItemChanged(mSelectedPos);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_THEME) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_skin_theme, parent, false);
            return new ThemeViewHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_skin_list, parent, false);
            return new ListViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final SkinItem item = mSkinList.get(position);
        final int realPosition = holder.getLayoutPosition();
        if (holder instanceof ThemeViewHolder) {
            ((ThemeViewHolder) holder).theme.setText(item.des);

            ((ThemeViewHolder) holder).theme.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnSkinItemClickListener != null) {
                        mOnSkinItemClickListener.onSkin(item.des);
                    }
                }
            });

        } else if (holder instanceof ListViewHolder) {
            int imgId = mContext.getResources().getIdentifier(item.imgName, "drawable", mContext.getPackageName());
            ((ListViewHolder) holder).img.setBackgroundResource(imgId);
            //   ((ListViewHolder) holder).img.setImageResource(item.img);
            ((ListViewHolder) holder).des.setText(item.des);

            if (item.isSelected) {
                ((ListViewHolder) holder).select_tv.setVisibility(View.VISIBLE);
            } else {
                ((ListViewHolder) holder).select_tv.setVisibility(View.GONE);
            }

            if ("".equals(item.status) || item.status == null) {
                ((ListViewHolder) holder).status_tv.setVisibility(View.GONE);
            } else {
                ((ListViewHolder) holder).status_tv.setVisibility(View.VISIBLE);
                ((ListViewHolder) holder).status_tv.setText(item.status);
            }


            if (mOnSkinItemClickListener != null) {
                ((ListViewHolder) holder).img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (realPosition != 3) {
                            refreshList(realPosition);
                        }
                        mOnSkinItemClickListener.onSkin(item.des);

                    }

                });
            }

        }


    }

    @Override
    public int getItemCount() {
        return mSkinList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mSkinList.get(position).isTitle) {
            return TYPE_THEME;
        } else {
            return TYPE_LIST;
        }
    }


    class ThemeViewHolder extends RecyclerView.ViewHolder {

        TextView theme;
        //        ImageView icon_iv;
        TextView icon_tv;

        public ThemeViewHolder(View itemView) {
            super(itemView);
            theme = (TextView) itemView.findViewById(R.id.theme);
//            icon_iv = (ImageView) itemView.findViewById(icon_iv);
            icon_tv = (TextView) itemView.findViewById(R.id.icon_tv);

            Typeface iconfont = FontUtil.setFont(mContext);
            icon_tv.setTypeface(iconfont);

        }
    }

    class ListViewHolder extends RecyclerView.ViewHolder {

        ImageView img;
        TextView des, select_tv, status_tv;

        public ListViewHolder(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.img);
            des = (TextView) itemView.findViewById(R.id.des);
            status_tv = (TextView) itemView.findViewById(R.id.status_tv);
            select_tv = (TextView) itemView.findViewById(R.id.select_tv);

            Typeface iconfont = FontUtil.setFont(mContext);
            select_tv.setTypeface(iconfont);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return getItemViewType(position) == TYPE_THEME
                            ? gridManager.getSpanCount() : 1;
                }
            });
        }
    }

    public interface OnSkinItemClickListener {
        void onSkin(String des);
    }

    public void setOnSkinItemClickListener(OnSkinItemClickListener onSkinItemClickListener) {
        mOnSkinItemClickListener = onSkinItemClickListener;
    }

    private void initmSelectedPos() {
        for (int i = 0; i < mSkinList.size(); i++) {
            if (mSkinList.get(i).isSelected) {
                mSelectedPos = i;
            }
        }
    }
}
