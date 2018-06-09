package com.sf.sofarmusic.demo.list.slide;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sf.sofarmusic.R;
import com.sf.sofarmusic.demo.enity.MessageItem;
import com.sf.sofarmusic.demo.list.slide.itemtouchhelperextension.Extension;
import com.sf.sofarmusic.util.ToastUtil;

import java.util.List;

/**
 * Created by sufan on 17/6/19.
 */

public class MessageListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<MessageItem> mMessageList;

    private OnItemClickListener mOnItemClickListener;

    public MessageListAdapter(Context context, List<MessageItem> messageList) {
        mContext = context;
        mMessageList = messageList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_message_item, parent, false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final MessageItem item = mMessageList.get(position);
        if (holder instanceof ItemHolder) {
            ((ItemHolder) holder).title_tv.setText(item.title);
            ((ItemHolder) holder).content_tv.setText(item.content);
            ((ItemHolder) holder).time_tv.setText(item.time);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    ToastUtil.startShort(mContext, pos + ":" + item.title);
                }
            });


            ((ItemHolder) holder).delete_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    doDelete(holder.getLayoutPosition());
                }
            });

        }
    }

    private void doDelete(int position) {
        mMessageList.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }


    class ItemHolder extends ViewHolder implements Extension {

        RelativeLayout item_rl;
        ImageView icon_iv;
        TextView title_tv, content_tv, time_tv, delete_tv;

        public ItemHolder(View itemView) {
            super(itemView);
            item_rl = (RelativeLayout) itemView.findViewById(R.id.item_rl);
            icon_iv = (ImageView) itemView.findViewById(R.id.icon_iv);
            title_tv = (TextView) itemView.findViewById(R.id.title_tv);
            content_tv = (TextView) itemView.findViewById(R.id.content_tv);
            time_tv = (TextView) itemView.findViewById(R.id.time_tv);

            delete_tv = (TextView) itemView.findViewById(R.id.delete_tv);

        }

        @Override
        public float getActionWidth() {
            return delete_tv.getWidth();
        }
    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void OnItemClickListener(ViewHolder holder);
    }
}
