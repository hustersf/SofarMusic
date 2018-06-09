package com.sf.sofarmusic.demo.window.update;

import java.io.Serializable;

/**
 * Created by sufan on 17/8/1.
 */

public class UpdateInfo implements Serializable {

    public String clientVersionId;
    public String clientVersionName;
    public String clientVersionURL;
    public String forceUpdate;
    public String clientUpdate;
    public String hintMessage;
    public String apkSize;
}
