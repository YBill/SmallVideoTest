package com.bill.smallvideotest;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.bill.smallvideotest.cache.PreloadManager;
import com.bumptech.glide.Glide;
import com.danikula.videocache.CacheListener;

import java.io.File;
import java.util.List;

/**
 * author ywb
 * date 2021/10/20
 * desc
 */
public class SmallVideoListAdapter extends RecyclerView.Adapter<SmallVideoListAdapter.VideoHolder> {

    private Context mContext;
    private List<SmallVideoBean> mDataList;

    public SmallVideoListAdapter(Context context) {
        mContext = context;
    }

    public void setDataList(List<SmallVideoBean> list) {
        mDataList = list;
        notifyDataSetChanged();
    }

    public void addDataList(List<SmallVideoBean> list) {
        try {
            int oldLength = mDataList.size();
            mDataList.addAll(list);
            notifyItemRangeInserted(oldLength, list.size());
        } catch (Exception e) {
            e.printStackTrace();
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public VideoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_small_video_item, parent, false);
        return new VideoHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoHolder holder, int position) {
        holder.update(position);
    }

    @Override
    public int getItemCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    public class VideoHolder extends RecyclerView.ViewHolder {

        public VideoPlayer mVideoPlayer;
        public AppCompatImageView mThumbIv;
        public SmallVideoBean mData;

        public VideoHolder(@NonNull View itemView) {
            super(itemView);
            mVideoPlayer = itemView.findViewById(R.id.video_player);
            mThumbIv = mVideoPlayer.getThumbIv();
        }

        private void update(int position) {
            mData = mDataList.get(position);
            Glide.with(mContext).load(mData.image).into(mThumbIv);
        }

        public void releaseCurrentView() {
            mThumbIv.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onViewAttachedToWindow(@NonNull VideoHolder holder) {
        super.onViewAttachedToWindow(holder);
        int position = holder.getAdapterPosition();
        if (position - 5 >= 0) {
            PreloadManager.getInstance().removePreloadTask(mDataList.get(position - 5).path);
        }
        if (position + 5 < getItemCount()) {
            PreloadManager.getInstance().removePreloadTask(mDataList.get(position + 5).path);
        }


        int cacheNum = Math.min(position + 5, getItemCount());
        for (int i = position; i < cacheNum; i++) {
            PreloadManager.getInstance().addPreloadTask(mDataList.get(i).path, i);
        }
        cacheNum = Math.max(position - 2, 0);
        for (int i = position - 1; i >= cacheNum; i--) {
            PreloadManager.getInstance().addPreloadTask(mDataList.get(i).path, i);
        }
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull VideoHolder holder) {
        super.onViewDetachedFromWindow(holder);
    }

    private CacheListener listener = new CacheListener() {
        @Override
        public void onCacheAvailable(File cacheFile, String url, int percentsAvailable) {
            Log.i("Bill", url.substring(url.lastIndexOf("/")) + " = " + percentsAvailable);
        }
    };
}
