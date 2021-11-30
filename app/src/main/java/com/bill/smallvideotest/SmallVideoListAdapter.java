package com.bill.smallvideotest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.bill.smallvideotest.cache.PreloadManager;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * author ywb
 * date 2021/10/20
 * desc
 */
public class SmallVideoListAdapter extends BaseVideoAnswerAdapter {

    public SmallVideoListAdapter(Context context) {
        super(context);
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
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((VideoHolder) holder).update(position);
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

            mVideoPlayer.setPlayListener(new VideoPlayer.OnPlayListener() {
                @Override
                public void repeatNum(int repeatNum) {
                    // 播放超过一遍自动跳到下一个视频
                    if (repeatNum > 2 && position + 1 < getItemCount()) {
                        if (mPlayerListener != null)
                            mPlayerListener.playNext();
                    }
                }

                @Override
                public void prepareFinish(int position, boolean isReverseScroll) {
                    PreloadManager.getInstance().resumePreload(position, isReverseScroll);
                }
            });
        }

        public void releaseCurrentView() {
            mThumbIv.setVisibility(View.VISIBLE);
        }
    }

    private OnPlayerListener mPlayerListener;

    public void setPlayerListener(OnPlayerListener mPlayerListener) {
        this.mPlayerListener = mPlayerListener;
    }

    public interface OnPlayerListener {
        void playNext();
    }
}
