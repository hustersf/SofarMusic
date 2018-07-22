package com.sf.sofarmusic.menu;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.sf.sofarmusic.R;
import com.sf.sofarmusic.data.LocalData;
import com.sf.sofarmusic.demo.photo.PhotoUtil;
import com.sf.sofarmusic.zxing.CodeShowActivity;
import com.sf.sofarmusic.enity.MenuItem;
import com.sf.sofarmusic.enity.SkinItem;
import com.sf.sofarmusic.menu.poweroff.PowerAlert;
import com.sf.sofarmusic.menu.profile.ProfileActivity;
import com.sf.sofarmusic.util.FontUtil;
import com.sf.utility.SharedPreUtil;
import com.sf.sofarmusic.view.CircleImageView;

import java.util.List;

/**
 * Created by sufan on 16/11/4.
 */

public class MenuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private Context mContext;
  private List<MenuItem> mMenuList;
  private Typeface mIconFont;

  private OnItemClickListener mOnItemClickListener;

  private final static int TYPE_HEAD = 0;
  private final static int TYPE_ITEM = 1;

  private String mSkinName; // 读取皮肤包的名字
  private List<SkinItem> mSkinList;

  private int minute;

  public MenuAdapter(Context context, List<MenuItem> menuList) {
    mContext = context;
    mMenuList = menuList;
    mIconFont = FontUtil.setFont(mContext);

    readSkinName();
  }

  private void readSkinName() {
    SharedPreUtil sp = new SharedPreUtil(mContext);
    String skinJson = sp.getToggleString("skinJson");
    if ("".equals(skinJson)) {
      mSkinList = LocalData.getSkinListData();
    } else {
      mSkinList = JSONArray.parseArray(skinJson, SkinItem.class);
    }

    for (int i = 0; i < mSkinList.size(); i++) {
      if (mSkinList.get(i).isSelected) {
        mSkinName = mSkinList.get(i).des;
        return;
      }
    }
  }


  // 刷新正在使用的皮肤包名字
  public void notifySkinName() {
    readSkinName();
    notifyItemChanged(6); // 6是主题换肤
  }


  // 数显定时剩余时间
  public void notifyTime(int minute) {
    this.minute = minute;
    notifyItemChanged(7); // 7是定时停止播放
  }


  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    if (viewType == TYPE_HEAD) {
      View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_menu_head, parent, false);
      return new HeadViewHolder(view);
    } else {
      View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_menu_item, parent, false);
      return new ItemViewHolder(view);
    }
  }

  @Override
  public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
    if (holder instanceof HeadViewHolder) {
      holder.itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          Intent intent = new Intent(mContext, ProfileActivity.class);
          mContext.startActivity(intent);
        }
      });

      ((HeadViewHolder) holder).code_iv.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          Intent intent = new Intent(mContext, CodeShowActivity.class);
          mContext.startActivity(intent);
        }
      });

      ((HeadViewHolder) holder).user_iv.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          PhotoUtil.getPhotoBitmap(mContext, new PhotoUtil.BitmapCallBack() {
            @Override
            public void onBitmap(Bitmap bitmap) {
              ((HeadViewHolder) holder).user_iv.setImageBitmap(bitmap);
            }
          });
        }
      });

    } else if (holder instanceof ItemViewHolder) {
      ((ItemViewHolder) holder).icon.setText(mMenuList.get(position - 1).icon);

      String des = mMenuList.get(position - 1).des;
      if ("主题换肤".equals(des)) {
        ((ItemViewHolder) holder).item_des_tv.setText(mSkinName);
      }

      if ("定时停止播放".equals(des)) {
        if (PowerAlert.hasTask) {
          ((ItemViewHolder) holder).item_des_tv.setText(minute + "分钟");
        } else {
          ((ItemViewHolder) holder).item_des_tv.setText("");
        }
      }

      ((ItemViewHolder) holder).des.setText(des);

      holder.itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          if (mOnItemClickListener != null) {
            int pos = holder.getLayoutPosition();
            String des = mMenuList.get(pos - 1).des;
            mOnItemClickListener.onItemClick(des);
          }
        }
      });
    }
  }

  @Override
  public int getItemCount() {
    return mMenuList.size() + 1;
  }

  @Override
  public int getItemViewType(int position) {
    if (position == 0) {
      return TYPE_HEAD;
    } else {
      return TYPE_ITEM;
    }
  }

  class HeadViewHolder extends RecyclerView.ViewHolder {

    CircleImageView user_iv;
    TextView user_tv;
    ImageView code_iv;

    public HeadViewHolder(View itemView) {
      super(itemView);
      user_iv = (CircleImageView) itemView.findViewById(R.id.user_iv);
      user_tv = (TextView) itemView.findViewById(R.id.user_tv);
      code_iv = (ImageView) itemView.findViewById(R.id.code_iv);

    }
  }

  class ItemViewHolder extends RecyclerView.ViewHolder {

    TextView icon, des, item_des_tv;


    public ItemViewHolder(View itemView) {
      super(itemView);
      icon = (TextView) itemView.findViewById(R.id.icon_tv);
      icon.setTypeface(mIconFont);
      des = (TextView) itemView.findViewById(R.id.des_tv);
      item_des_tv = (TextView) itemView.findViewById(R.id.item_des_tv);

    }
  }

  public interface OnItemClickListener {
    void onItemClick(String des);
  }

  public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
    mOnItemClickListener = onItemClickListener;
  }
}
