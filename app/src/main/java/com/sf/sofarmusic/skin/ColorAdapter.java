package com.sf.sofarmusic.skin;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sf.sofarmusic.R;
import com.sf.sofarmusic.enity.ColorItem;
import com.sf.sofarmusic.util.FontUtil;

import java.util.List;

/**
 * Created by sufan on 16/11/7.
 */

public class ColorAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<ColorItem> mColorList;

    private OnColorSelectedListener mOnColorSelectedListener;
    // private View mItemView;

    public ColorAdapter(Context context, List<ColorItem> colorList) {
        mContext = context;
        mColorList = colorList;
    }

    public void setNewData(List<ColorItem> colorList) {
        mColorList = colorList;
        notifyDataSetChanged();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_color, parent, false);
        return new ItemViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        ColorItem item = mColorList.get(position);
        if (holder instanceof ItemViewHolder) {
            if (position == mColorList.size() - 1) {
                ((ItemViewHolder) holder).img.setBackgroundResource(R.drawable.gradient_background);
            } else {
                ((ItemViewHolder) holder).img.setBackgroundResource(R.drawable.square_background);
                GradientDrawable gradient = (GradientDrawable) ((ItemViewHolder) holder).img.getBackground();
                gradient.setColor(mContext.getResources().getColor(item.color));
            }
            if (item.isSelected) {
                ((ItemViewHolder) holder).select.setVisibility(View.VISIBLE);
            } else {
                ((ItemViewHolder) holder).select.setVisibility(View.GONE);
            }

        }

        ((ItemViewHolder) holder).img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnColorSelectedListener != null) {
                    int pos = holder.getLayoutPosition();
                    mOnColorSelectedListener.onColor(pos);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mColorList.size();
    }


    class ItemViewHolder extends RecyclerView.ViewHolder {

        ImageView img;
        TextView select;
        RelativeLayout color_rl;

        public ItemViewHolder(View itemView) {
            super(itemView);
            color_rl = (RelativeLayout) itemView.findViewById(R.id.color_rl);
            img = (ImageView) itemView.findViewById(R.id.img);
            select = (TextView) itemView.findViewById(R.id.select_tv);
            Typeface iconfont = FontUtil.setFont(mContext);
            select.setTypeface(iconfont);

        }
    }

    public interface OnColorSelectedListener {
        void onColor(int position);
    }

    public void setOnColorSelectedListener(OnColorSelectedListener onColorSelectedListener) {
        mOnColorSelectedListener = onColorSelectedListener;
    }
}
