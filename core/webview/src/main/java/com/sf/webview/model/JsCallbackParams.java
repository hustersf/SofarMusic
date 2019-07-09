package com.sf.webview.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class JsCallbackParams implements Serializable {

    @SerializedName("callback")
    public String callback;
}
