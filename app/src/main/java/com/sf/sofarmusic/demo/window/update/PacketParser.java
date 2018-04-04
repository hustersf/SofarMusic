package com.sf.sofarmusic.demo.window.update;

import org.json.JSONObject;

/**
 * Created by sufan on 17/8/1.
 */

public class PacketParser {

    public static UpdateInfo parseUpdateInfo(JSONObject json){
        if (json == null) {
            return null;
        }
        UpdateInfo updateInfo = new UpdateInfo();
        if (json.has("ClientVersionId")) {
            updateInfo.clientVersionId=json.optString("ClientVersionId");
        }

        if (json.has("ClientVersionName")) {
            updateInfo.clientVersionName=json.optString("ClientVersionName");
        }
        if (json.has("ClientVersionURL")) {
            updateInfo.clientVersionURL=json.optString("ClientVersionURL");
        }
        if (json.has("ForceUpdate")) {
            updateInfo.forceUpdate=json.optString("ForceUpdate");
        }
        if (json.has("HintMessage")) {
            updateInfo.hintMessage=json.optString("HintMessage");
        }
        if (json.has("ClientUpdate")) {
            updateInfo.clientUpdate=json.optString("ClientUpdate");
        }
        if (json.has("ApkSize")) {
            updateInfo.apkSize=json.optString("ApkSize");
        }
        return updateInfo;
    }
}
