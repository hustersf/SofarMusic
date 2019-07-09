package com.sf.webview.model;

import com.google.gson.annotations.SerializedName;

public class JsDialogParams extends JsCallbackParams {

  @SerializedName("title")
  public String title;

  @SerializedName("positiveButton")
  public String positiveButton;

  @SerializedName("negativeButton")
  public String negativeButton;

}
