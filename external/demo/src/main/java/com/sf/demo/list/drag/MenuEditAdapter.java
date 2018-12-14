package com.sf.demo.list.drag;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sf.base.util.ImageUtil;
import com.sf.demo.R;
import com.sf.demo.enity.MenuItem;
import com.sf.utility.DensityUtil;
import com.sf.utility.DeviceUtil;
import com.sf.utility.ToastUtil;


/**
 * Created by sufan on 16/11/5.
 */

public class MenuEditAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<MenuItem> mMenuList;
    private OnItemLongClickListener mOnItemLongClickListener;

    private static final int TYPE_TITLE = 0;
    private static final int TYPE_LIST = 1;
    private static final int TYPE_LIST_DASHED = 2;

    private List<MenuItem> mMyList;

    public MenuEditAdapter(Context context, List<MenuItem> menuList) {
        mContext = context;
        mMenuList = menuList;

        initMyList();

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_TITLE) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_menu_title, parent, false);
            return new TitleViewHolder(view);
        } else if (viewType == TYPE_LIST_DASHED) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_menu_dashed, parent, false);
            return new DashedViewHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_menu_list, parent, false);
            return new ListViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final MenuItem item = mMenuList.get(position);
        if (holder instanceof TitleViewHolder) {
            ((TitleViewHolder) holder).title_tv.setText(item.name);

            if ("我的应用".equals(item.name)) {
                ((TitleViewHolder) holder).subtitle_tv.setVisibility(View.VISIBLE);
                ((TitleViewHolder) holder).subtitle_tv.setText("完成");
            } else {
                ((TitleViewHolder) holder).subtitle_tv.setVisibility(View.GONE);
            }

        } else if (holder instanceof ListViewHolder) {

            ImageUtil.setDrawableByName(mContext, "demo_menu_" + item.imgName, ((ListViewHolder) holder).menu_iv);
            ((ListViewHolder) holder).menu_tv.setText(item.name);

            if (item.isAdd) {
                ((ListViewHolder) holder).add_iv.setVisibility(View.VISIBLE);
                ((ListViewHolder) holder).delete_iv.setVisibility(View.GONE);
            } else {
                ((ListViewHolder) holder).add_iv.setVisibility(View.GONE);
                ((ListViewHolder) holder).delete_iv.setVisibility(View.VISIBLE);
            }


            ((ListViewHolder) holder).add_iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos=holder.getLayoutPosition();
                    addMenu(pos, item);
                }
            });

            ((ListViewHolder) holder).delete_iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos=holder.getLayoutPosition();
                    deleteMenu(pos, item);
                }
            });

            ((ListViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos=holder.getLayoutPosition();
                    ToastUtil.startShort(mContext, pos + "-" + item.name);
                }
            });

            if (mOnItemLongClickListener != null) {
                if (position > 0 && position < mMyList.size()) {
                    holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            int pos=holder.getLayoutPosition();
                            mOnItemLongClickListener.OnItemLongClick(pos, item, holder, mMyList.size());
                            return true;
                        }
                    });
                }
            }

        } else if (holder instanceof DashedViewHolder) {
            int width = (DeviceUtil.getMetricsWidth(mContext) - DensityUtil.dp2px(mContext, 40)) / 4;
            int height = DensityUtil.dp2px(mContext, 80);
            int margin = DensityUtil.dp2px(mContext, 5);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width, height);
            lp.setMargins(margin, margin, margin, margin);
            ((DashedViewHolder) holder).dashed_rl.setLayoutParams(lp);
        }


    }

    @Override
    public int getItemCount() {
        return mMenuList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mMenuList.get(position).isTitle) {
            return TYPE_TITLE;
        } else if (mMenuList.get(position).isDashed) {
            return TYPE_LIST_DASHED;
        } else {
            return TYPE_LIST;
        }
    }


    class TitleViewHolder extends RecyclerView.ViewHolder {

        TextView title_tv, subtitle_tv;

        public TitleViewHolder(View itemView) {
            super(itemView);
            title_tv = (TextView) itemView.findViewById(R.id.title_tv);
            subtitle_tv = (TextView) itemView.findViewById(R.id.subtitle_tv);
        }
    }

    class ListViewHolder extends RecyclerView.ViewHolder {

        ImageView menu_iv, add_iv, delete_iv;
        TextView menu_tv;

        public ListViewHolder(View itemView) {
            super(itemView);
            menu_iv = (ImageView) itemView.findViewById(R.id.menu_iv);
            add_iv = (ImageView) itemView.findViewById(R.id.add_iv);
            delete_iv = (ImageView) itemView.findViewById(R.id.delete_iv);
            menu_tv = (TextView) itemView.findViewById(R.id.menu_tv);
        }
    }

    class DashedViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout dashed_rl;

        public DashedViewHolder(View itemView) {
            super(itemView);
            dashed_rl = (RelativeLayout) itemView.findViewById(R.id.dashed_rl);
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
                    return getItemViewType(position) == TYPE_TITLE
                            ? gridManager.getSpanCount() : 1;
                }
            });
        }
    }


    //添加菜单时的操作
    private void addMenu(int position, MenuItem item) {
        for (int i = 0; i < mMyList.size(); i++) {
            if (item.name.equals(mMyList.get(i).name)) {
                ToastUtil.startShort(mContext, "该功能已经添加");
                return;
            }
        }

        if (mMyList.size() >= 12) {
            ToastUtil.startShort(mContext, "最多只能添加11个功能");
            return;
        }

        int index = mMyList.size();
        MenuItem addItem = new MenuItem();
        addItem.isAdd = false;
        addItem.name = item.name;
        addItem.imgName = item.imgName;

        mMenuList.add(index, addItem);
        notifyItemInserted(index);
        ToastUtil.startShort(mContext, item.name + "添加成功");
        initMyList();

    }

    private void deleteMenu(int position, MenuItem item) {
        if (mMyList.size() <= 4) {
            ToastUtil.startShort(mContext, "我的应用不能小于3个");
          return;
        }

        mMenuList.remove(position);
        notifyItemRemoved(position);
        ToastUtil.startShort(mContext, item.name + "删除成功");
        initMyList();
    }

    private void initMyList() {
        if(mMyList==null) {
            mMyList = new ArrayList<>();
        }
        mMyList.clear();
        for (MenuItem item : mMenuList) {
            if (!item.isAdd) {
                mMyList.add(item);
            }
        }

        Log.i("TAG","myList:"+mMyList.size());
    }


    public interface OnItemLongClickListener {
        void OnItemLongClick(int position, MenuItem item, RecyclerView.ViewHolder holder, int dashedPosition);
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        mOnItemLongClickListener = onItemLongClickListener;
    }

}
