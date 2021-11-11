package com.bill.smallvideotest;

import android.content.Context;
import android.view.View;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.recyclerview.widget.RecyclerView;

/**
 * author ywb
 * date 2021/10/20
 * desc
 */
public class PlayerHelper implements LifecycleObserver {

    private final RecyclerView mRecyclerView;
    private final VideoLayoutManager mLayoutManager;

    private SmallVideoListAdapter.VideoHolder mCurHolder;
    private int mCurrentPosition = -1;

    private OnPreLoadListener mOnPreLoadListener;

    public PlayerHelper(Context context, RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
        mLayoutManager = (VideoLayoutManager) mRecyclerView.getLayoutManager();
        if (mLayoutManager == null)
            return;
        mLayoutManager.setOnViewPagerListener(new OnViewPagerListener() {

            @Override
            public void onPageRelease(int position) {
                if (mCurrentPosition != position) return;
                releaseVideo(position);
            }

            @Override
            public void onPageSelected(int position, boolean isBottom) {
                if (mCurrentPosition == position) return;
                playerVideo();
            }

            @Override
            public void preLoad() {
                if (mOnPreLoadListener != null)
                    mOnPreLoadListener.onLoad();
            }
        });
    }

    //播放视频
    private void playerVideo() {
        int visibleItemPosition = mLayoutManager.findLastCompletelyVisibleItemPosition();
        if (visibleItemPosition >= 0 && mCurrentPosition != visibleItemPosition) {
            stopCurVideoView(); //停止上一个视频
            mCurrentPosition = visibleItemPosition;
            mCurHolder = null;
            View holderView = mLayoutManager.findViewByPosition(mCurrentPosition);
            if (holderView != null) {
                mCurHolder = (SmallVideoListAdapter.VideoHolder) mRecyclerView.getChildViewHolder(holderView);
                startCurVideoView(); //开始播放视频
            }
        }
    }

    //停止视频
    private void releaseVideo(int position) {
        View holderView = mLayoutManager.findViewByPosition(position);
        if (holderView == null) return;
        final VideoPlayer videoView = holderView.findViewById(R.id.video_player);
        if (videoView != null)
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
            mCurHolder.mVideoPlayer.setVideoPath(mCurHolder.mData.path);
        }
    }

    public void setOnPreLoadListener(OnPreLoadListener mOnPreLoadListener) {
        this.mOnPreLoadListener = mOnPreLoadListener;
    }

    public interface OnPreLoadListener {
        void onLoad();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    void onLifecycleReceived(LifecycleOwner source, Lifecycle.Event event) {
        if (mCurHolder == null) {
            return;
        }

        if (event == Lifecycle.Event.ON_RESUME) {
            mCurHolder.mVideoPlayer.play();
        } else if (event == Lifecycle.Event.ON_PAUSE) {
            mCurHolder.mVideoPlayer.pause();
        } else if (event == Lifecycle.Event.ON_STOP) {
            mCurHolder.releaseCurrentView();
            mCurHolder.mVideoPlayer.release();
        } else if (event == Lifecycle.Event.ON_DESTROY) {

        }

    }

}
