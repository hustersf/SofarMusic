package com.sf.demo.view.highlight;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sf.demo.R;
import com.sf.demo.view.highlight.component.ComponentTab4;
import com.sf.utility.DensityUtil;
import com.sf.widget.highlight.Component;
import com.sf.widget.highlight.Guide;
import com.sf.widget.highlight.GuideBuilder;

/**
 * 遮罩的形状是按宽高绘制的矩形遮罩
 * 如果dialog的背景是不规则的图形，则样式会有问题；可以考虑将dialog的宽高设置成全屏幕的
 */
public class HighLightDialogFragment extends DialogFragment {

  private TextView ruleTv;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    setStyle(android.support.v4.app.DialogFragment.STYLE_NO_TITLE,
        com.sf.widget.R.style.Theme_Dialog_Translucent);
    super.onCreate(savedInstanceState);
  }

  @NonNull
  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    return super.onCreateDialog(savedInstanceState);
  }


  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.dialog_highlight, container, false);
  }


  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    ruleTv = view.findViewById(R.id.tv_rule);

    initData();
  }

  @Override
  public void onStart() {
    super.onStart();
    getDialog().getWindow().setLayout(DensityUtil.dp2px(getActivity(), 300),
        DensityUtil.dp2px(getActivity(), 417));
  }

  private void initData() {
    String ruleStr = "1.已出售物品，请在帖子后标注“已售”字样\n\n" +
        "2.物品描述要完整，清楚，详细。建议对应物品对应图片。发表与话题无关的留言、文字说明过少，一律删贴 \n\n" +
        "3.个人出售的所有物品，请放入一贴中予以整体编辑说明。 \n\n" +
        "4.网络虚拟，请大家保护好个人信息安全。同城交换交易建议携伴同行。 \n\n" +
        "5.广州二手是大家的，请珍惜这来之不易的环境，不允许任何不文明事情发生。 \n\n" +
        "6.淘宝、团购等纯商业的广告，不解释直接删 \n\n" +
        "7.每天顶贴不能超过两次 作弊顶贴者一旦发现 永远封禁 ";

    ruleTv.setText(ruleStr);

    ruleTv.post(() -> {
      showGuide();
    });
  }

  private void showGuide() {
    GuideBuilder builder = new GuideBuilder();
    builder.setTargetView(ruleTv)
        .setAlpha(100)
        .setHighTargetGraphStyle(Component.ROUNDRECT)
        .setOverlayTarget(false)
        .setHighTargetPadding(DensityUtil.dp2px(getContext(), 10))
        .setTargetViewDecoration(true)
        .setHighTargetCorner(DensityUtil.dp2px(getContext(), 16))
        .setOutsideTouchable(false);
    builder.setOnVisibilityChangedListener(new GuideBuilder.OnVisibilityChangedListener() {
      @Override
      public void onShown() {}

      @Override
      public void onDismiss() {}
    });

    builder.addComponent(new ComponentTab4("我是弹窗引导"));
    Guide guide = builder.createGuide();
    guide.setShouldCheckLocInWindow(false);
    guide.show((ViewGroup) getView());
  }
}
