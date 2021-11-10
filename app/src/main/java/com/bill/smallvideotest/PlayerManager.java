package com.bill.smallvideotest;

import android.media.MediaPlayer;
import android.util.Log;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * author ywb
 * date 2021/11/5
 * desc
 */
public class PlayerManager {

    private static class SingletonHolder {
        private static PlayerManager instance = new PlayerManager();
    }

    public static PlayerManager getInstance() {
        return SingletonHolder.instance;
    }

    private final byte[] mAvailableLocker = new byte[0];
    private final static int PLAYER_NUM = 4;
    private final ExecutorService mExecutorService = Executors.newScheduledThreadPool(PLAYER_NUM);
    private final Queue<MediaPlayer> mPlayerQueue = new ArrayDeque<>(PLAYER_NUM);
    private final Queue<MediaPlayer> mRecycleQueue = new ArrayDeque<>(PLAYER_NUM);
    private MediaPlayer mPlayer;

    private PlayerManager() {
    }

    public MediaPlayer getAvailableMediaPlayer() {
        Log.i("Bill", "available = " + mPlayerQueue.size() + " , recycle = " + mRecycleQueue.size()
                + " , current = " + (mPlayer == null ? 0 : 1));
        if (currentPlayerNumLegal() || mPlayerQueue.isEmpty()) {
            MediaPlayer player = new MediaPlayer();
            offerPlayer(mPlayerQueue, player);
        }

        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.setSurface(null);
            offerPlayer(mRecycleQueue, mPlayer);
        }

        mPlayer = pollPlayer(mPlayerQueue);

        recyclePlayer();

        return mPlayer;
    }

    private void offerPlayer(Queue<MediaPlayer> queue, MediaPlayer player) {
        synchronized (mAvailableLocker) {
            queue.offer(player);
        }
    }

    private MediaPlayer pollPlayer(Queue<MediaPlayer> queue) {
        synchronized (mAvailableLocker) {
            return queue.poll();
        }
    }

    private void recyclePlayer() {
        if (mRecycleQueue.size() > 0) {
            mExecutorService.execute(() -> {
                MediaPlayer player = pollPlayer(mRecycleQueue);
                player.reset();
                offerPlayer(mPlayerQueue, player);
            });
        }
    }

    private boolean currentPlayerNumLegal() {
        synchronized (mAvailableLocker) {
            return mPlayerQueue.size() + mRecycleQueue.size() < PLAYER_NUM;
        }
    }

    private void recycle(Queue<MediaPlayer> queue) {
        for (MediaPlayer mediaPlayer : mPlayerQueue) {
            recycle(mediaPlayer);
        }
        queue.clear();
    }

    private void recycle(MediaPlayer player) {
        if (player != null) {
            player.stop();
            player.setSurface(null);
            player.release();
            player = null;
        }
    }

    public void recycle() {
        recycle(mPlayer);
        recycle(mPlayerQueue);
        recycle(mRecycleQueue);
        if (mExecutorService != null && !mExecutorService.isShutdown()) {
            mExecutorService.shutdown();
        }
    }
}
