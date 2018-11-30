package com.sf.sofarmusic.menu.profile;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sf.sofarmusic.R;
import com.sf.utility.DeviceUtil;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;


/**
 * Created by sufan on 16/11/5.
 */

public class ProfileAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private Context mContext;
  private List<Photo> mPhotoList;

  public ProfileAdapter(Context context, List<Photo> photoList) {
    mContext = context;
    mPhotoList = photoList;
  }


  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_profile_item, parent, false);
    ViewGroup.LayoutParams params = view.getLayoutParams();
    params.height = DeviceUtil.getMetricsWidth(mContext) / 3;
    view.setLayoutParams(params);
    return new ItemViewHolder(view);

  }

  @Override
  public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
    if (holder instanceof ItemViewHolder) {


    }
  }

  @Override
  public int getItemCount() {
    return mPhotoList.size();
  }



  class ItemViewHolder extends RecyclerView.ViewHolder {

    public ItemViewHolder(View itemView) {
      super(itemView);
    }
  }


  @Retention(RetentionPolicy.SOURCE)
  @IntDef({Style.HEAD, Style.ITEM})
  public @interface Style {
    int HEAD = 0;
    int ITEM = 1;
  }

}
