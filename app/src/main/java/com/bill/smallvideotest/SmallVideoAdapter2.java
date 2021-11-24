package com.bill.smallvideotest;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.viewpager.widget.PagerAdapter;

import com.bill.smallvideotest.cache.PreloadManager;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * author ywb
 * date 2021/11/19
 * desc
 */
public class SmallVideoAdapter2 extends PagerAdapter {

    private List<View> mViewPool = new ArrayList<>();

    private Context mContext;
    private List<SmallVideoBean> mDataList;

    public SmallVideoAdapter2(Context context) {
        mContext = context;
    }

    public List<SmallVideoBean> getDataList() {
        return mDataList;
    }

    public void setDataList(List<SmallVideoBean> list) {
        mDataList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = null;
        if (mViewPool.size() > 0) {//取第一个进行复用
            view = mViewPool.get(0);
            mViewPool.remove(0);
        }

        ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.adapter_small_video_item, container, false);
            viewHolder = new ViewHolder(view);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        SmallVideoBean mData = mDataList.get(position);
        Glide.with(mContext).load(mData.image).into(viewHolder.mThumbIv);

        String url = mData.path;
        Log.e("Bill", "开始预加载 = " + position);
//        PreloadManager.getInstance().addPreloadTask(url, position);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        View itemView = (View) object;
        container.removeView(itemView);
        //保存起来用来复用
        mViewPool.add(itemView);

        String url = mDataList.get(position).path;
        Log.e("Bill", "取消预加载 = " + position);
        PreloadManager.getInstance().removePreloadTask(url);
    }

    public static class ViewHolder {

        public VideoPlayer mVideoPlayer;
        public AppCompatImageView mThumbIv;

        ViewHolder(View itemView) {
            mVideoPlayer = itemView.findViewById(R.id.video_player);
            mThumbIv = mVideoPlayer.getThumbIv();
            itemView.setTag(this);
        }
    }

}
