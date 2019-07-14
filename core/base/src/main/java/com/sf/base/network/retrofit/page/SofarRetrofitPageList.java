package com.sf.base.network.retrofit.page;

import com.sf.base.network.retrofit.response.ListResponse;
import com.sf.utility.CollectionUtil;

import java.util.List;

public abstract class SofarRetrofitPageList<PAGE extends ListResponse<MODEL>, MODEL>
    extends RetrofitPageList<PAGE, MODEL> {

  @Override
  public void onLoadItemFromResponse(PAGE page, List<MODEL> items) {

    if(isFirstPage()){
      items.clear();
    }

    List<MODEL> newItems = page.getItems();
    if (CollectionUtil.isEmpty(newItems)) {
      return;
    }

    if (allowDuplicate()) {
      items.addAll(newItems);
      return;
    }

    for (MODEL item : newItems) {
      if (!items.contains(item)) {
        items.add(item);
      }
    }
  }

  protected boolean allowDuplicate() {
    return true;
  }
}
