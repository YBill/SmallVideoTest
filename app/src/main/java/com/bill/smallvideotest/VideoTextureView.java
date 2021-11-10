package com.bill.smallvideotest;

import android.content.Context;
import android.util.AttributeSet;
import android.view.TextureView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * author ywb
 * date 2021/10/20
 * desc 重新测量下渲染器
 */
public class VideoTextureView extends TextureView {

    private int mVideoWidth;
    private int mVideoHeight;

    public VideoTextureView(@NonNull Context context) {
        super(context);
    }

    public VideoTextureView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public VideoTextureView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setVideoSize(int videoWidth, int videoHeight) {
        mVideoWidth = videoWidth;
        mVideoHeight = videoHeight;
        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // 获取Mode，这里不判断Mode了，这里是全屏小视频，直接处理了
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);

        // 获取Size
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);


        int width = widthSpecSize;
        int height = heightSpecSize;

        if (mVideoWidth > 0 && mVideoHeight > 0) {

            // 留黑边，类似 fitCenter
            /*if (mVideoWidth * height < width * mVideoHeight) {
                width = height * mVideoWidth / mVideoHeight;
            } else if (mVideoWidth * height > width * mVideoHeight) {
                height = width * mVideoHeight / mVideoWidth;
            }*/

            // 裁切，类似 centerCrop
            if (width * mVideoHeight < height * mVideoWidth) {
                width = mVideoWidth * height / mVideoHeight;
            } else if (width * mVideoHeight > height * mVideoWidth) {
                height = mVideoHeight * width / mVideoWidth;
            }

            // 默认是全屏填满，类似 fitXY

        }

        setMeasuredDimension(width, height);
    }
}
