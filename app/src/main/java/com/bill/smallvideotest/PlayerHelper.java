package com.bill.smallvideotest;

import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * author ywb
 * date 2021/10/20
 * desc
 */
public class PlayerHelper {

    private RecyclerView mRecyclerView;
    private VideoLayoutManager mLayoutManager;

    private SmallVideoListAdapter.VideoHolder mCurHolder;
    private int mCurrentPosition = -1;

    private OnPreLoadListener mOnPreLoadListener;

    public PlayerHelper(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
        mLayoutManager = (VideoLayoutManager) mRecyclerView.getLayoutManager();
        if (mLayoutManager == null)
            return;
        mLayoutManager.setOnViewPagerListener(new OnViewPagerListener() {

            @Override
            public void onPageRelease(int position) {
                releaseVideo(position);
            }

            @Override
            public void onPageSelected(int position, boolean isBottom) {
                playerVideo();
            }

            @Override
            public void preLoad() {
                if (mOnPreLoadListener != null)
                    mOnPreLoadListener.onLoad();
            }
        });
    }

    public void release() {
        stopCurVideoView();
    }

    //播放视频
    private void playerVideo() {
        int visibleItemPosition = mLayoutManager.findLastCompletelyVisibleItemPosition();
        if (visibleItemPosition >= 0 && mCurrentPosition != visibleItemPosition) {
            stopCurVideoView(); //停止上一个视频
            mCurrentPosition = visibleItemPosition;
            View holderView = mRecyclerView.findViewWithTag(mCurrentPosition);
            if (holderView != null) {
                mCurHolder = (SmallVideoListAdapter.VideoHolder) mRecyclerView.getChildViewHolder(holderView);
                startCurVideoView(); //开始播放视频
            }
        }
    }

    //停止视频
    private void releaseVideo(int position) {
        View holderView = mRecyclerView.findViewWithTag(position);
        final VideoPlayer videoView = holderView.findViewById(R.id.video_player);
        videoView.release();
    }

    private void stopCurVideoView() {
        if (mCurHolder != null) {
            mCurHolder.mThumbIv.setVisibility(View.VISIBLE);
            mCurHolder.mVideoPlayer.release();
        }
    }

    private void startCurVideoView() {
        if (mCurHolder == null) return;
        if (!mCurHolder.mVideoPlayer.isPlaying()) {
            Log.e("VideoPlayer", "play path = " + mCurHolder.mData.path);
            mCurHolder.mVideoPlayer.setVideoPath(mCurHolder.mData.path);
        }
    }

    public void setOnPreLoadListener(OnPreLoadListener mOnPreLoadListener) {
        this.mOnPreLoadListener = mOnPreLoadListener;
    }

    public interface OnPreLoadListener {
        void onLoad();
    }

}
