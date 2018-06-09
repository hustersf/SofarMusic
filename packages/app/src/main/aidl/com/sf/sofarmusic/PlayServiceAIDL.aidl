// PlayServiceAIDL.aidl
package com.sf.sofarmusic;

// Declare any non-default types here with import statements
import com.sf.sofarmusic.PlayCallback;

interface PlayServiceAIDL {

   void play();   //播放歌曲
   void playNext();   //播放下一曲
   void playPre();    //播放上一曲
   void pause();    //暂停歌曲
   void seekTo(int progress);
   void clear();      //清空列表
   void destroy();    //销毁服务

//   void registerCallback(PlayCallback callback);
//   void unregisterCallback(PlayCallback callback);

}
