package com.sf.sofarmusic.menu.profile;


public interface IPullZoom {

  boolean isReadyForPullStart();

  void onPullZooming(int newScrollValue);

  void onPullZoomEnd();

}
