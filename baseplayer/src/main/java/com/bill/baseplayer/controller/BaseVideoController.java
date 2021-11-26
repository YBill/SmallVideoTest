package com.bill.baseplayer.controller;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bill.baseplayer.config.VideoViewManager;
import com.bill.baseplayer.player.VideoView;
import com.bill.baseplayer.util.Utils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * author ywb
 * date 2021/11/24
 * desc
 */
public abstract class BaseVideoController extends FrameLayout implements
        IVideoController, OrientationHelper.OnOrientationChangeListener {

    //播放器包装类，集合了MediaPlayerControl的api和IVideoController的api
    protected ControlWrapper mControlWrapper;

    //保存了所有的控制组件
    protected LinkedHashMap<IControlComponent, Boolean> mControlComponents = new LinkedHashMap<>();

    //是否开启根据屏幕方向进入/退出全屏
    private boolean mEnableOrientation;
    //屏幕方向监听辅助类
    protected OrientationHelper mOrientationHelper;

    public BaseVideoController(@NonNull Context context) {
        this(context, null);
    }

    public BaseVideoController(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseVideoController(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    protected void initView() {
        if (getLayoutId() != 0) {
            LayoutInflater.from(getContext()).inflate(getLayoutId(), this, true);
        }
    }

    /**
     * 设置控制器布局文件
     */
    protected abstract int getLayoutId();

    /**
     * {@link VideoView}调用此方法向控制器设置播放状态
     */
    @CallSuper
    public void setPlayState(int playState) {
        handlePlayStateChanged(playState);
    }

    /**
     * {@link VideoView}调用此方法向控制器设置播放器状态
     */
    @CallSuper
    public void setPlayerState(final int playerState) {
        handlePlayerStateChanged(playerState);
    }

    private void handlePlayStateChanged(int playState) {
    }

    private void handlePlayerStateChanged(int playerState) {
    }

    /**
     * 显示移动网络播放提示
     *
     * @return 返回显示移动网络播放提示的条件，false:不显示, true显示
     * 此处默认根据手机网络类型来决定是否显示，开发者可以重写相关逻辑
     */
    public boolean showNetWarning() {
        return Utils.isMobileNet(getContext())
                && !VideoViewManager.getInstance().playOnMobileNetwork();
    }

    /**
     * 重要：此方法用于将{@link VideoView} 和控制器绑定
     */
    @CallSuper
    public void setMediaPlayer(PlayerControl mediaPlayer) {
        mControlWrapper = new ControlWrapper(mediaPlayer, this);
        //绑定ControlComponent和Controller
        for (Map.Entry<IControlComponent, Boolean> next : mControlComponents.entrySet()) {
            IControlComponent component = next.getKey();
            component.attach(mControlWrapper);
        }
        //开始监听设备方向
        mOrientationHelper.setOnOrientationChangeListener(this);
    }

    /**
     * 改变返回键逻辑，用于activity
     */
    public boolean onBackPressed() {
        return false;
    }

    //////// System Start /////////

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (mControlWrapper.isPlaying()
                && (mEnableOrientation || mControlWrapper.isFullScreen())) {
            if (hasWindowFocus) {
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mOrientationHelper.enable();
                    }
                }, 800);
            } else {
                mOrientationHelper.disable();
            }
        }
    }

    //////// System End /////////


}
