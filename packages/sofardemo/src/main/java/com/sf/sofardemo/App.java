package com.sf.sofardemo;

import com.sf.libskin.base.SkinBaseApplication;
import com.sf.libskin.config.SkinConfig;
import com.sf.sofardemo.attr.CollapsingToolbarLayoutAttr;
import com.sf.sofardemo.attr.FabAttr;
import com.sf.sofardemo.attr.FlowLayoutAttr;
import com.sf.sofardemo.attr.LoadViewAttr;
import com.sf.sofardemo.attr.LoadViewTextColorAttr;
import com.sf.sofardemo.attr.TabLayoutIndicatorAttr;
import com.sf.sofardemo.attr.TabLayoutTextColorAttr;

import io.flutter.view.FlutterMain;

/**
 * 由于所依赖的demo模块，支持了换肤，因此app需要继承SkinBaseApplication
 */
public class App extends SkinBaseApplication{

    @Override
    public void onCreate() {
        super.onCreate();

        //增加换肤的自定义属性
        SkinConfig.addSupportAttr("loadColor",new LoadViewAttr());
        SkinConfig.addSupportAttr("loadTextColor",new LoadViewTextColorAttr());
        SkinConfig.addSupportAttr("tabLayoutIndicator",new TabLayoutIndicatorAttr());
        SkinConfig.addSupportAttr("tabLayoutTextColor",new TabLayoutTextColorAttr());
        SkinConfig.addSupportAttr("tagColor",new FlowLayoutAttr());
        SkinConfig.addSupportAttr("contentScrimColor",new CollapsingToolbarLayoutAttr());
        SkinConfig.addSupportAttr("fabColor",new FabAttr());

        FlutterMain.startInitialization(this);
    }
}
