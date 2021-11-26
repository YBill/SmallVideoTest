package com.bill.baseplayer.config;

/**
 * author ywb
 * date 2021/11/26
 * desc
 */
public class VideoViewConfig {

    public static Builder create() {
        return new Builder();
    }

    public VideoViewConfig(Builder builder) {

    }

    public final static class Builder {

        public VideoViewConfig build() {
            return new VideoViewConfig(this);
        }

    }


}
