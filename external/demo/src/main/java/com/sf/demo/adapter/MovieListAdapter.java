package com.sf.demo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sf.demo.R;
import com.sf.demo.enity.MovieItem;

import java.util.List;

/**
 * Created by sufan on 17/3/23.
 */

public class MovieListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<MovieItem> mMovieList;

    public MovieListAdapter(Context context, List<MovieItem> movieList){
        mContext=context;
        mMovieList=movieList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.adapter_movie_list,parent,false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        MovieItem movie=mMovieList.get(position);
        int realPosition=holder.getLayoutPosition();
        if(holder instanceof ItemHolder){
             Glide.with(mContext).load(movie.getImgUrl()).into(((ItemHolder) holder).movie_iv);
            ((ItemHolder) holder).chName_tv.setText(movie.getChName());
            ((ItemHolder) holder).enName_tv.setText(movie.getEnName());
            ((ItemHolder) holder).showTime_tv.setText("上映时间:"+movie.getShowTime());
        }

    }

    @Override
    public int getItemCount() {
        return mMovieList.size();
    }

    class ItemHolder extends RecyclerView.ViewHolder{
        ImageView movie_iv;
        TextView chName_tv,enName_tv,showTime_tv;

        public ItemHolder(View itemView) {
            super(itemView);
            movie_iv=(ImageView)itemView.findViewById(R.id.movie_iv);
            chName_tv=(TextView)itemView.findViewById(R.id.chName_tv);
            enName_tv=(TextView)itemView.findViewById(R.id.enName_tv);
            showTime_tv=(TextView)itemView.findViewById(R.id.showTime_tv);
        }
    }
}
