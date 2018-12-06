package com.sf.demo.view.calendar;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sf.demo.R;
import com.sf.utility.DeviceUtil;

/**
 * Created by sufan on 17/7/3.
 */

public class WeekAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<DateEntity> mDateList;

    private int mSelectedPos = -1;  //当前选中的位置
    private OnItemClickListener mListener;
    private String[] weekTitle = {"一", "二", "三", "四", "五", "六","日"};


    public WeekAdapter(Context context, List<DateEntity> dateList) {
        mContext = context;
        mDateList = dateList;
        initSelectedPos();
    }

    public void updateDate(List<DateEntity> dateList) {
        mDateList = dateList;
        initSelectedPos();
        notifyDataSetChanged();
    }

    //局部刷新
    private void refreshList(int i) {
        if (mSelectedPos != i) {
            if (mSelectedPos != -1) {
                mDateList.get(mSelectedPos).isSelected = false;
                notifyItemChanged(mSelectedPos);
            }
            //设置新的mSelectedPos
            mSelectedPos = i;
            mDateList.get(mSelectedPos).isSelected = true;
            notifyItemChanged(mSelectedPos);
        }

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_calendar_week, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final DateEntity item = mDateList.get(position);
        if (holder instanceof ItemViewHolder) {
            //设置布局的宽高
            int width = DeviceUtil.getMetricsWidth(mContext) / 7;
            int height = width;
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width, height);
            ((ItemViewHolder) holder).week_ll.setLayoutParams(lp);


            ((ItemViewHolder) holder).week_tv.setText(weekTitle[position]);
            ((ItemViewHolder) holder).day_tv.setText(item.day);

            if (item.isToday) {
                ((ItemViewHolder) holder).week_ll.setBackgroundResource(R.drawable.circle_green_bg);
                ((ItemViewHolder) holder).day_tv.setTextColor(Color.parseColor("#ffffff"));
                ((ItemViewHolder) holder).week_tv.setTextColor(Color.parseColor("#ffffff"));
            } else if (item.isSelected) {
                ((ItemViewHolder) holder).week_ll.setBackgroundResource(R.drawable.circle_pink_bg);
                ((ItemViewHolder) holder).day_tv.setTextColor(Color.parseColor("#ffffff"));
                ((ItemViewHolder) holder).week_tv.setTextColor(Color.parseColor("#ffffff"));
            } else {
                ((ItemViewHolder) holder).week_ll.setBackgroundColor(Color.parseColor("#ffffff"));
                ((ItemViewHolder) holder).day_tv.setTextColor(Color.parseColor("#333333"));
                ((ItemViewHolder) holder).week_tv.setTextColor(Color.parseColor("#666666"));
            }

            if (mListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!TextUtils.isEmpty(item.date)) {
                            int pos = holder.getLayoutPosition();
                            refreshList(pos);
                            mListener.OnItemClickListener(pos, item);
                        }
                    }
                });
            }
        }

    }

    @Override
    public int getItemCount() {
        return mDateList.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView day_tv, week_tv;
        LinearLayout week_ll;

        public ItemViewHolder(View itemView) {
            super(itemView);
            day_tv = (TextView) itemView.findViewById(R.id.day_tv);
            week_tv = (TextView) itemView.findViewById(R.id.week_tv);
            week_ll = (LinearLayout) itemView.findViewById(R.id.week_ll);
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public interface OnItemClickListener {
        void OnItemClickListener(int pos, DateEntity entity);
    }

    private void initSelectedPos() {
        for (int i = 0; i < mDateList.size(); i++) {
            if (mDateList.get(i).isSelected) {
                mSelectedPos = i;
            }
        }
    }
}
