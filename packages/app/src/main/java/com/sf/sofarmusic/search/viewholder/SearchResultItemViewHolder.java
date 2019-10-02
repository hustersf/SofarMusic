package com.sf.sofarmusic.search.viewholder;

import android.view.View;
import android.widget.TextView;
import com.sf.base.view.IconTextView;
import com.sf.sofarmusic.R;
import com.sf.sofarmusic.search.model.SearchInfo;
import com.sf.widget.recyclerview.RecyclerViewHolder;

public class SearchResultItemViewHolder extends RecyclerViewHolder<SearchInfo> {

  IconTextView searchIcon;
  TextView searchTitle;

  public SearchResultItemViewHolder(View itemView) {
    super(itemView);
  }

  @Override
  protected void onCreateView(View itemView) {
    searchIcon = itemView.findViewById(R.id.search_icon);
    searchTitle = itemView.findViewById(R.id.search_title);
  }

  @Override
  protected void onBindData(SearchInfo item) {
    if (item.linkType == SearchInfo.ARTIST_LINK_TYPE) {
      searchIcon.setText(R.string.icon_people);
    } else if (item.linkType == SearchInfo.SONG_LINK_TYPE) {
      searchIcon.setText(R.string.icon_music);
    } else if (item.linkType == SearchInfo.ALBUM_LINK_TYPE) {
      searchIcon.setText(R.string.icon_disk);
    }
    searchTitle.setText(item.word);
  }
}
