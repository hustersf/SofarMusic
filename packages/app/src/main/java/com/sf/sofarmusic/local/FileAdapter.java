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
import com.sf.sofarmusic.enity.FileItem;

/**
 * Created by sufan on 16/11/23.
 */

public class FileAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<FileItem> mFileList;
    private Typeface mIconFont;

    private OnItemClickListener mOnItemClickListener;


    public FileAdapter(Context context, List<FileItem> fileList) {
        mContext = context;
        mFileList = fileList;
        mIconFont = FontUtil.setFont(mContext);

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_file_item, parent, false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        FileItem item = mFileList.get(position);
        final int realPosition = holder.getLayoutPosition();

        if (holder instanceof ItemHolder) {
            ((ItemHolder) holder).file_name_tv.setText(item.name);
            ((ItemHolder) holder).file_path_tv.setText(item.parent);
            ((ItemHolder) holder).file_count_tv.setText(item.fileList.size() + "首");


            if (item.isSelected) {
                ((ItemHolder) holder).voice_tv.setVisibility(View.VISIBLE);
                ((ItemHolder) holder).dot_tv.setVisibility(View.GONE);
            } else {
                ((ItemHolder) holder).voice_tv.setVisibility(View.GONE);
                ((ItemHolder) holder).dot_tv.setVisibility(View.VISIBLE);
            }


            if (mOnItemClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mOnItemClickListener.OnFileItem(realPosition);
                    }
                });
            }
        }

    }

    @Override
    public int getItemCount() {
        return mFileList.size();
    }


    class ItemHolder extends RecyclerView.ViewHolder {

        TextView file_tv;
        TextView file_name_tv, file_count_tv, file_path_tv, dot_tv, voice_tv;

        public ItemHolder(View itemView) {
            super(itemView);
            file_tv = (TextView) itemView.findViewById(R.id.file_tv);
            file_name_tv = (TextView) itemView.findViewById(R.id.file_name_tv);
            file_count_tv = (TextView) itemView.findViewById(R.id.file_count_tv);
            file_path_tv = (TextView) itemView.findViewById(R.id.file_path_tv);
            dot_tv = (TextView) itemView.findViewById(R.id.dot_tv);
            voice_tv = (TextView) itemView.findViewById(R.id.voice_tv);

            file_tv.setTypeface(mIconFont);
            dot_tv.setTypeface(mIconFont);
            voice_tv.setTypeface(mIconFont);

        }
    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }


    public interface OnItemClickListener {
        void OnFileItem(int position);
    }


}
