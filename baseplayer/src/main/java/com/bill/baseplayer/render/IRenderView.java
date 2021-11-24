package com.bill.baseplayer.render;

import android.graphics.Bitmap;
import android.view.View;

import androidx.annotation.NonNull;

import com.bill.baseplayer.player.AbstractPlayer;

/**
 * author ywb
 * date 2021/11/24
 * desc
 */
public interface IRenderView {

    public static final int SCREEN_SCALE_DEFAULT = 0;
    public static final int SCREEN_SCALE_16_9 = 1;
    public static final int SCREEN_SCALE_4_3 = 2;
    public static final int SCREEN_SCALE_MATCH_PARENT = 3;
    public static final int SCREEN_SCALE_ORIGINAL = 4;
    public static final int SCREEN_SCALE_CENTER_CROP = 5;

    /**
     * 关联AbstractPlayer
     */
    void attachToPlayer(@NonNull AbstractPlayer player);

    /**
     * 设置视频宽高
     *
     * @param videoWidth  宽
     * @param videoHeight 高
     */
    void setVideoSize(int videoWidth, int videoHeight);

    /**
     * 设置视频旋转角度
     *
     * @param degree 角度值
     */
    void setVideoRotation(int degree);

    /**
     * 设置screen scale type
     *
     * @param scaleType 类型
     */
    void setScaleType(int scaleType);

    /**
     * 获取真实的RenderView
     */
    View getView();

    /**
     * 截图
     */
    Bitmap doScreenShot();

    /**
     * 释放资源
     */
    void release();

}
