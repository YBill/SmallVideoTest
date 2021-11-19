package com.bill.smallvideotest;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.bill.smallvideotest.cache.PreloadManager;
import com.bill.smallvideotest.cache.ProxyCacheManager;
import com.bill.smallvideotest.widget.VerticalViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * author ywb
 * date 2021/10/20
 * desc
 */
public class SmallVideoFragment2 extends Fragment {

    public static final String TAG = "SmallVideoFragment2";

    /*private static final String[] PATHS = new String[]{
            "http://kw-static.cognizepower.com/content/mp4/f3523b69a913c9ab25646708266058db.mp4",
            "http://kw-static.cognizepower.com/content/mp4/2e4f319af8a1c0d93ea63acc001801da.mp4",
            "http://kw-static.cognizepower.com/content/mp4/96ea21568439d688054d4e54385b9920.mp4",
            "http://kw-static.cognizepower.com/content/mp4/5b29f6fc54be8cea508c921769e00f8c.mp4",
            "http://kw-static.cognizepower.com/content/mp4/126049f559f703e28aadaa62d060ca12.mp4",
            "http://kw-static.cognizepower.com/content/mp4/752f4a8c393ed88f0dc95525774c5fdc.mp4",
            "http://kw-static.cognizepower.com/content/mp4/4b4c70c09f8539633c8f2a39729e0854.mp4"
    };
    private static final String[] IMAGES = new String[]{
            "http://kw-static.cognizepower.com/content/jpg/f3523b69a913c9ab25646708266058db.jpg",
            "http://kw-static.cognizepower.com/content/jpg/2e4f319af8a1c0d93ea63acc001801da.jpg",
            "http://kw-static.cognizepower.com/content/jpg/96ea21568439d688054d4e54385b9920.jpg",
            "http://kw-static.cognizepower.com/content/jpg/5b29f6fc54be8cea508c921769e00f8c.jpg",
            "http://kw-static.cognizepower.com/content/jpg/126049f559f703e28aadaa62d060ca12.jpg",
            "http://kw-static.cognizepower.com/content/jpg/752f4a8c393ed88f0dc95525774c5fdc.jpg",
            "http://kw-static.cognizepower.com/content/jpg/4b4c70c09f8539633c8f2a39729e0854.jpg"
    };*/

    private static final String[] PATHS = new String[]{
            "https://interactive-wallpaper-1252921383.cos.ap-beijing.myqcloud.com/online-earning/answer/video/video_v4/83f98b293a4ca640f69319a2ce53c60b.mp4",
            "https://interactive-wallpaper-1252921383.cos.ap-beijing.myqcloud.com/online-earning/answer/video/video_v4/626e7623305d038e92c45e6b4a791027.mp4",
            "https://interactive-wallpaper-1252921383.cos.ap-beijing.myqcloud.com/online-earning/answer/video/video_v4/38fa92f8dae47b1645aa35d7bb72e682.mp4",
            "https://interactive-wallpaper-1252921383.cos.ap-beijing.myqcloud.com/online-earning/answer/video/video_v4/a2e4910b5da9523b39357ffe0595f2af.mp4",
            "https://interactive-wallpaper-1252921383.cos.ap-beijing.myqcloud.com/online-earning/answer/video/video_v4/ee273fe5e2a9921aa1c1e502fe453f64.mp4",
            "https://interactive-wallpaper-1252921383.cos.ap-beijing.myqcloud.com/online-earning/answer/video/video_v4/6964e5569ea89e3806867eb49850e655.mp4",
            "https://interactive-wallpaper-1252921383.cos.ap-beijing.myqcloud.com/online-earning/answer/video/video_v4/f7379b7211aa24fbfbc773e7d0e35cd0.mp4",
            "https://interactive-wallpaper-1252921383.cos.ap-beijing.myqcloud.com/online-earning/answer/video/video_v4/699c751e772687860806628aa31f8503.mp4",
            "https://interactive-wallpaper-1252921383.cos.ap-beijing.myqcloud.com/online-earning/answer/video/video_v4/30f20e1df0204eaf97da225ff112451a.mp4",
            "https://interactive-wallpaper-1252921383.cos.ap-beijing.myqcloud.com/online-earning/answer/video/video_v4/4c3f1e8b04f7748bfec5e229fb0d53c6.mp4"
    };
    private static final String[] IMAGES = new String[]{
            "https://interactive-wallpaper-1252921383.cos.ap-beijing.myqcloud.com/online-earning/answer/video/video_v4/83f98b293a4ca640f69319a2ce53c60b.mp4",
            "https://interactive-wallpaper-1252921383.cos.ap-beijing.myqcloud.com/online-earning/answer/video/video_v4/626e7623305d038e92c45e6b4a791027.mp4",
            "https://interactive-wallpaper-1252921383.cos.ap-beijing.myqcloud.com/online-earning/answer/video/video_v4/38fa92f8dae47b1645aa35d7bb72e682.mp4",
            "https://interactive-wallpaper-1252921383.cos.ap-beijing.myqcloud.com/online-earning/answer/video/video_v4/a2e4910b5da9523b39357ffe0595f2af.mp4",
            "https://interactive-wallpaper-1252921383.cos.ap-beijing.myqcloud.com/online-earning/answer/video/video_v4/ee273fe5e2a9921aa1c1e502fe453f64.mp4",
            "https://interactive-wallpaper-1252921383.cos.ap-beijing.myqcloud.com/online-earning/answer/video/video_v4/6964e5569ea89e3806867eb49850e655.mp4",
            "https://interactive-wallpaper-1252921383.cos.ap-beijing.myqcloud.com/online-earning/answer/video/video_v4/f7379b7211aa24fbfbc773e7d0e35cd0.mp4",
            "https://interactive-wallpaper-1252921383.cos.ap-beijing.myqcloud.com/online-earning/answer/video/video_v4/699c751e772687860806628aa31f8503.mp4",
            "https://interactive-wallpaper-1252921383.cos.ap-beijing.myqcloud.com/online-earning/answer/video/video_v4/30f20e1df0204eaf97da225ff112451a.mp4",
            "https://interactive-wallpaper-1252921383.cos.ap-beijing.myqcloud.com/online-earning/answer/video/video_v4/4c3f1e8b04f7748bfec5e229fb0d53c6.mp4"
    };

    private VerticalViewPager mViewPager;
    private SmallVideoAdapter2 mAdapter;
    private SmallVideoAdapter2.ViewHolder mCurHolder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_small_video_2, container, false);
        initViews(view);
        configViews();
        return view;
    }

    private void initViews(View view) {
        mViewPager = view.findViewById(R.id.rv_video);
    }

    private void configViews() {
        mViewPager.setOffscreenPageLimit(4);
        mAdapter = new SmallVideoAdapter2(getActivity());
        mViewPager.setAdapter(mAdapter);
        mAdapter.setDataList(getData());
        mViewPager.setOverScrollMode(View.OVER_SCROLL_NEVER);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            private int mCurrentPosition = -1;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (mCurrentPosition == position) return;
                playerVideo(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == VerticalViewPager.SCROLL_STATE_DRAGGING) {
                    mCurrentPosition = mViewPager.getCurrentItem();
                }
            }
        });
    }

    private void playerVideo(int position) {
        if (mCurHolder != null) {
            mCurHolder.mThumbIv.setVisibility(View.VISIBLE);
            mCurHolder.mVideoPlayer.release();
        }
        mCurHolder = null;

        View view = mViewPager.getChildAt(position);
        mCurHolder = (SmallVideoAdapter2.ViewHolder) view.getTag();
        String proxyPath = PreloadManager.getInstance().getPlayUrl(mAdapter.getDataList().get(position).path);
        Log.e("Bill", position + "播放 = " + proxyPath);
        mCurHolder.mVideoPlayer.setVideoPath(proxyPath);
    }

    private List<SmallVideoBean> getData() {
        List<SmallVideoBean> list = new ArrayList<>();
        for (int i = 0; i < PATHS.length; i++) {
            SmallVideoBean bean = new SmallVideoBean();
            bean.path = PATHS[i];
            bean.image = IMAGES[i];
            list.add(bean);
        }
        return list;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        PreloadManager.getInstance().removeAllPreloadTask();
        ProxyCacheManager.getInstance().clearAllCache(); // TODO
    }
}
