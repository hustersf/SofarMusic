package com.sf.base.network.retrofit.page;

import com.sf.base.network.retrofit.response.ListResponse;

import java.util.List;

public abstract class SofarRetrofitPageList<PAGE extends ListResponse<MODEL>, MODEL>
    extends RetrofitPageList<PAGE, MODEL> {

  @Override
  public void onLoadItemFromResponse(PAGE page, List<MODEL> items) {
    items.addAll(page.getItems());
  }
}
