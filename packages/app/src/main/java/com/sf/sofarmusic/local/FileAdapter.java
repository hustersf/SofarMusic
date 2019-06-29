package com.sf.sofarmusic.local;

import android.view.View;
import android.widget.TextView;
import com.sf.sofarmusic.R;
import com.sf.sofarmusic.local.model.FileItem;
import com.sf.widget.recyclerview.RecyclerAdapter;
import com.sf.widget.recyclerview.RecyclerViewHolder;

/**
 * Created by sufan on 16/11/23.
 */

public class FileAdapter extends RecyclerAdapter<FileItem> {

  @Override
  protected int getItemLayoutId(int viewType) {
    return R.layout.adapter_file_item;
  }

  @Override
  protected RecyclerViewHolder onCreateViewHolder(int viewType, View itemView) {
    return new FileItemHolder(itemView);
  }


  class FileItemHolder extends RecyclerViewHolder<FileItem> {

    TextView fileTv;
    TextView fileNameTv, fileCountTv, filePathTv, dotTv, voiceTv;

    public FileItemHolder(View itemView) {
      super(itemView);
    }

    @Override
    protected void onCreateView(View itemView) {
      fileTv = itemView.findViewById(R.id.file_tv);
      fileNameTv = itemView.findViewById(R.id.file_name_tv);
      fileCountTv = itemView.findViewById(R.id.file_count_tv);
      filePathTv = itemView.findViewById(R.id.file_path_tv);
      dotTv = itemView.findViewById(R.id.dot_tv);
      voiceTv = itemView.findViewById(R.id.voice_tv);
    }

    @Override
    protected void onBindData(FileItem item, RecyclerViewHolder holder) {

      fileNameTv.setText(item.name);
      filePathTv.setText(item.parent);
      fileCountTv.setText(item.songs.size() + "首");

      if (item.selected) {
        voiceTv.setVisibility(View.VISIBLE);
        dotTv.setVisibility(View.GONE);
      } else {
        voiceTv.setVisibility(View.GONE);
        dotTv.setVisibility(View.VISIBLE);
      }

      itemView.setOnClickListener(v -> {

      });
    }
  }
}
