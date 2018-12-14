package com.sf.sofarmusic.base;

import com.sf.libskin.base.SkinBaseApplication;
import com.sf.libskin.config.SkinConfig;
import com.sf.sofarmusic.db.PlayStatus;
import com.sf.sofarmusic.skin.attr.CollapsingToolbarLayoutAttr;
import com.sf.sofarmusic.skin.attr.FabAttr;
import com.sf.sofarmusic.skin.attr.FlowLayoutAttr;
import com.sf.sofarmusic.skin.attr.LoadViewAttr;
import com.sf.sofarmusic.skin.attr.LoadViewTextColorAttr;
import com.sf.sofarmusic.skin.attr.LrcViewAttr;
import com.sf.sofarmusic.skin.attr.MusicProgressAttr;
import com.sf.sofarmusic.skin.attr.TabLayoutIndicatorAttr;
import com.sf.sofarmusic.skin.attr.TabLayoutTextColorAttr;
import com.sf.sofarmusic.skin.attr.TimeProgressAttr;

/**
 * Created by sufan on 17/2/28.
 */

public class BaseApplication extends SkinBaseApplication {


  @Override
  public void onCreate() {
    super.onCreate();

    // 增加换肤的自定义属性
    SkinConfig.addSupportAttr("reachColor", new MusicProgressAttr());
    SkinConfig.addSupportAttr("loadColor", new LoadViewAttr());
    SkinConfig.addSupportAttr("loadTextColor", new LoadViewTextColorAttr());
    SkinConfig.addSupportAttr("tabLayoutIndicator", new TabLayoutIndicatorAttr());
    SkinConfig.addSupportAttr("tabLayoutTextColor", new TabLayoutTextColorAttr());
    SkinConfig.addSupportAttr("currentColor", new LrcViewAttr());
    SkinConfig.addSupportAttr("tagColor", new FlowLayoutAttr());
    SkinConfig.addSupportAttr("contentScrimColor", new CollapsingToolbarLayoutAttr());
    SkinConfig.addSupportAttr("fabColor", new FabAttr());
    SkinConfig.addSupportAttr("timeReachColor", new TimeProgressAttr());


    // 异常初始化
    // CrashHandler.getInstance().init(getApplicationContext());

    // 初始状态为暂停
    PlayStatus.getInstance(getApplicationContext()).setStatus(PlayStatus.PAUSE);

    // 检测内存泄漏
    // LeakCanary.install(this);

  }
}
