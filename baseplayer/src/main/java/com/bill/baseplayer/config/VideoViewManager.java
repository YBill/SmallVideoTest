package com.bill.baseplayer.config;

/**
 * author ywb
 * date 2021/11/26
 * desc
 */
public class VideoViewManager {

    private static VideoViewConfig sConfig;

    private static VideoViewManager sInstance;

    /**
     * 是否在移动网络下直接播放视频
     */
    private boolean mPlayOnMobileNetwork = true;

    public static VideoViewManager instance() {
        if (sInstance == null) {
            synchronized (VideoViewManager.class) {
                if (sInstance == null) {
                    sInstance = new VideoViewManager();
                }
            }
        }
        return sInstance;
    }

    public static void setConfig(VideoViewConfig config) {
        if (sConfig == null) {
            synchronized (VideoViewConfig.class) {
                if (sConfig == null) {
                    sConfig = config == null ? VideoViewConfig.create().build() : config;
                }
            }
        }
    }

    public static VideoViewConfig getConfig() {
        setConfig(null);
        return sConfig;
    }

    /**
     * 获取是否在移动网络下直接播放视频配置
     */
    public boolean playOnMobileNetwork() {
        return mPlayOnMobileNetwork;
    }

}
