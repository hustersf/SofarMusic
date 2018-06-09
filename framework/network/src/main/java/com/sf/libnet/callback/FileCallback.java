package com.sf.libnet.callback;

import java.io.File;

public abstract class FileCallback {
	public  String mDir;
	public String mFileName;
	
	public FileCallback(String dir,String fileName){
		mDir=dir;
		mFileName=fileName;
	}

	public abstract void OnSuccess(File file);

	public abstract void OnError(Object obj);
	
	public abstract void Progress(float progress);

}
