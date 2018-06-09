package com.sf.sofarmusic.demo.view.highlight;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sf.sofarmusic.R;
import com.sf.sofarmusic.demo.list.page.DividePartBean;
import com.sf.sofarmusic.demo.list.page.DividePartSubBean;
import com.sf.sofarmusic.demo.list.page.PageGridView;
import com.sf.sofarmusic.demo.view.highlight.component.ComponentTab1;
import com.sf.sofarmusic.demo.view.highlight.component.ComponentTab2;
import com.sf.sofarmusic.demo.view.highlight.core.Component;
import com.sf.sofarmusic.demo.view.highlight.core.Guide;
import com.sf.sofarmusic.demo.view.highlight.core.GuideBuilder;
import com.sf.sofarmusic.util.DeviceUtil;
import com.sf.sofarmusic.util.ImageUtil;
import com.sf.sofarmusic.util.LogUtil;

import java.util.List;

/**
 * Created by sufan on 17/6/22.
 */

public class GridAdapter extends PageGridView.PagingAdapter<ViewHolder> {

    private List<DividePartSubBean> mData;
    private Context mContext;
    private int mColumn;

    private SparseArray<View> views;   //存放子view

    private int mTag = -1;  //0 首页  1 金融

    private boolean isShowGuideView=true;


    public GridAdapter(Context context, DividePartBean partBean, int tag) {
        mContext = context;
        mColumn = partBean.column;
        mData = partBean.list;
        mTag = tag;
        views=new SparseArray<>();

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_page_grid, parent, false);
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = DeviceUtil.getMetricsWidth(mContext) / mColumn;
        params.width = DeviceUtil.getMetricsWidth(mContext) / mColumn;
        view.setLayoutParams(params);


        return new PageGridViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DividePartSubBean item = mData.get(position);
        if (holder instanceof PageGridViewHolder) {
            if (TextUtils.isEmpty(item.title)) {
                ((PageGridViewHolder) holder).title_tv.setVisibility(View.GONE);
            } else {
                ((PageGridViewHolder) holder).title_tv.setText(item.title);
                ((PageGridViewHolder) holder).title_tv.setVisibility(View.VISIBLE);
            }

            if (TextUtils.isEmpty(item.imgName)) {
                ((PageGridViewHolder) holder).icon_iv.setVisibility(View.GONE);
            } else {
                ((PageGridViewHolder) holder).icon_iv.setVisibility(View.VISIBLE);
                ImageUtil.setDrawableByName(mContext, "demo_menu_" + item.imgName, ((PageGridViewHolder) holder).icon_iv);
            }

            views.put(position,holder.itemView);
        }

        LogUtil.d("onBindViewHolder");
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }


    class PageGridViewHolder extends ViewHolder {
        LinearLayout parent_ll;
        ImageView icon_iv;
        TextView title_tv;

        public PageGridViewHolder(View itemView) {
            super(itemView);
            parent_ll = (LinearLayout) itemView.findViewById(R.id.parent_ll);
            icon_iv = (ImageView) itemView.findViewById(R.id.icon_iv);
            title_tv = (TextView) itemView.findViewById(R.id.title_tv);
        }
    }

    @Override
    public void onViewAttachedToWindow(ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        //在这里展示遮罩层（否则view.post中的run可能不会执行）
        if(views.size()==mData.size()) {
            if (mTag == 0 && isShowGuideView) {
                isShowGuideView = false;
                showGuideView1(mData.size() - 1);
            } else if (mTag == 1 && isShowGuideView) {
                isShowGuideView = false;
                showGuideView2(0);
            }
        }

        LogUtil.d("onViewAttachedToWindow");

    }

    @Override
    public void onViewDetachedFromWindow(ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
    }

    @Override
    public List getData() {
        return mData;
    }

    @Override
    public Object getEmpty() {
        return new DividePartSubBean();
    }


    private void showGuideView1(int position) {

        final DividePartSubBean item = mData.get(position);
        final View targetView = views.get(position);
        targetView.post(new Runnable() {
            @Override
            public void run() {
                GuideBuilder builder = new GuideBuilder();
                builder.setTargetView(targetView)
                        .setAlpha(100)
                        .setHighTargetGraphStyle(Component.CIRCLE)
                        .setHighTargetPadding(0)
                        .setOverlayTarget(false)
                        .setOutsideTouchable(false);
                builder.setOnVisibilityChangedListener(new GuideBuilder.OnVisibilityChangedListener() {
                    @Override
                    public void onShown() {
                    }

                    @Override
                    public void onDismiss() {
                    }
                });

                builder.addComponent(new ComponentTab1(item.title));
                Guide guide = builder.createGuide();
                guide.setShouldCheckLocInWindow(false);
                guide.show((Activity) mContext);
            }
        });
    }

    private void showGuideView2(int position) {

        final DividePartSubBean item = mData.get(position);

        final View targetView = views.get(position);

        targetView.post(new Runnable() {
            @Override
            public void run() {
                GuideBuilder builder = new GuideBuilder();
                builder.setTargetView(targetView)
                        .setAlpha(100)
                        .setHighTargetGraphStyle(Component.CIRCLE)
                        .setHighTargetPadding(0)
                        .setOverlayTarget(false)
                        .setOutsideTouchable(false);
                builder.setOnVisibilityChangedListener(new GuideBuilder.OnVisibilityChangedListener() {
                    @Override
                    public void onShown() {

                    }

                    @Override
                    public void onDismiss() {
                        showGuideView3(mData.size() - 1);
                    }
                });

                builder.addComponent(new ComponentTab2(item.title, 1));
                Guide guide = builder.createGuide();
                guide.setShouldCheckLocInWindow(false);
                guide.show((Activity) mContext);
            }
        });
    }

    private void showGuideView3(int position) {

        final DividePartSubBean item = mData.get(position);
        final View targetView = views.get(position);
        targetView.post(new Runnable() {
            @Override
            public void run() {
                GuideBuilder builder = new GuideBuilder();
                builder.setTargetView(targetView)
                        .setAlpha(100)
                        .setHighTargetGraphStyle(Component.CIRCLE)
                        .setHighTargetPadding(0)
                        .setOverlayTarget(false)
                        .setOutsideTouchable(false);
                builder.setOnVisibilityChangedListener(new GuideBuilder.OnVisibilityChangedListener() {
                    @Override
                    public void onShown() {
                    }

                    @Override
                    public void onDismiss() {
                    }
                });

                builder.addComponent(new ComponentTab2(item.title, 2));
                Guide guide = builder.createGuide();
                guide.setShouldCheckLocInWindow(false);
                guide.show((Activity) mContext);
            }
        });


    }
}
