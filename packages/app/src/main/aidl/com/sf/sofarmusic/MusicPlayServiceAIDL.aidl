// MusicPlayServiceAIDL.aidl
package com.sf.sofarmusic;

// Declare any non-default types here with import statements

interface MusicPlayServiceAIDL {

   void play(String path);

   void pause();

   void stop();
}
