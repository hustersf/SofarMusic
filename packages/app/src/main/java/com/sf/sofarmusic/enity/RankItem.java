package com.sf.sofarmusic.enity;

import java.io.Serializable;
import java.util.List;


/**
 * Created by sufan on 16/11/18.
 * 榜单实体
 */
public class RankItem implements Serializable, Comparable<RankItem> {

  public String name; // 榜单名
  public boolean isTitle; // 是否时标题
  public List<PlayItem> playList; // 榜单列表
  public String imgUrl; // 榜单图片url
  public String bigImgUrl; // 榜单大图片
  public int order; // 排序用
  public int count; // 歌曲数量
  public String type; // 请求对应的地址编号，例子type=1代表新歌


  @Override
  public int compareTo(RankItem rank) {
    if (this.order > rank.order) {
      return 1;
    } else {
      return -1;
    }

  }
}
