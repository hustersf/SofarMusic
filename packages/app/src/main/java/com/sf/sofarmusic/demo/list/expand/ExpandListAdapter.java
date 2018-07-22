package com.sf.sofarmusic.demo.list.expand;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sf.sofarmusic.R;
import com.sf.utility.DensityUtil;
import com.sf.sofarmusic.util.ImageUtil;
import com.sf.utility.ToastUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sufan on 17/6/27.
 */

public class ExpandListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<MenuParent> mDatas;
    private Map<Integer, Boolean> mUpMap;

    public ExpandListAdapter(Context context, List<MenuParent> datas) {
        mContext = context;
        mDatas = datas;
        initMap();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_expand_list, parent, false);
        return new ListHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final MenuParent item = mDatas.get(position);
        if (holder instanceof ListHolder) {
            addChildView(position, item, holder);
            ((ListHolder) holder).up_down_rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    doUpAndDown(pos, item, holder);
                }
            });
            ((ListHolder) holder).parent_tv.setText(item.name);
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class ListHolder extends RecyclerView.ViewHolder {
        RelativeLayout parent_rl, up_down_rl;
        TextView parent_tv;
        ImageView up_down_iv;
        LinearLayout child_ll;

        public ListHolder(View itemView) {
            super(itemView);
            parent_rl = (RelativeLayout) itemView.findViewById(R.id.parent_rl);
            up_down_rl = (RelativeLayout) itemView.findViewById(R.id.up_down_rl);
            parent_tv = (TextView) itemView.findViewById(R.id.parent_tv);
            up_down_iv = (ImageView) itemView.findViewById(R.id.up_down_iv);
            child_ll = (LinearLayout) itemView.findViewById(R.id.child_ll);
        }
    }


    /**
     * 记录每个parent的状态(上还是下)
     * true  上  铺开
     * false 下  收起
     */
    private void initMap() {
        mUpMap = new HashMap<>();
        for (int i = 0; i < mDatas.size(); i++) {
            mUpMap.put(i, false);
        }
    }

    private void doUpAndDown(int position, MenuParent item, RecyclerView.ViewHolder holder) {
        ListHolder listHolder = (ListHolder) holder;
        if (mUpMap.get(position)) {
            listHolder.child_ll.setVisibility(View.GONE);
            listHolder.up_down_iv.setImageResource(R.drawable.demo_expand_down);
            mUpMap.put(position, false);
        } else {
            listHolder.child_ll.setVisibility(View.VISIBLE);
            listHolder.up_down_iv.setImageResource(R.drawable.demo_expand_up);
            mUpMap.put(position, true);
        }

    }

    private void addChildView(final int position, MenuParent item, RecyclerView.ViewHolder holder) {
        ListHolder listHolder = (ListHolder) holder;
        List<MenuChild> childList = item.list;


        for (int i = 0; i < childList.size(); i++) {
            final MenuChild child = childList.get(i);

            View view = LayoutInflater.from(mContext).inflate(R.layout.layout_expand_child, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.menu_iv);
            TextView textView = (TextView) view.findViewById(R.id.menu_tv);
            ((ListHolder) holder).child_ll.addView(view);


            ImageUtil.setDrawableByName(mContext, "demo_menu_" + child.imgName, imageView);
            textView.setText(child.name);


            final int childPos = i;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtil.startShort(mContext, "groupPos:" + position + " childPos:" + childPos + " name:" + child.name);
                }
            });

        }

        ImageView line = new ImageView(mContext);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                DensityUtil.dp2px(mContext, 1));
        lp.setMargins(DensityUtil.dp2px(mContext, 10), 0, 0, 0);
        line.setLayoutParams(lp);
        int color = mContext.getResources().getColor(R.color.half_light_gray);
        line.setBackgroundColor(color);
        listHolder.child_ll.addView(line);

    }

}
