package com.bill.baseplayer.player;

/**
 * author ywb
 * date 2021/11/26
 * desc 播放进度管理器，继承此接口实现自己的进度管理器。
 */
public abstract class ProgressManager {

    /**
     * 此方法用于实现保存进度的逻辑
     *
     * @param url      播放地址
     * @param progress 播放进度
     */
    public abstract void saveProgress(String url, long progress);

    /**
     * 此方法用于实现获取保存的进度的逻辑
     *
     * @param url 播放地址
     * @return 保存的播放进度
     */
    public abstract long getSavedProgress(String url);

}
