package com.bill.smallvideotest;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bill.smallvideotest.cache.PreloadManager;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * author ywb
 * date 2021/11/24
 * desc
 */
public abstract class BaseVideoAnswerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected Context mContext;
    protected List<SmallVideoBean> mDataList;

    private int mCurrentPosition = -1;

    public BaseVideoAnswerAdapter(Context context) {
        mContext = context;
    }

    @Override
    public void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        int position = holder.getAdapterPosition();
        if (position == mCurrentPosition) {
            return;
        }

        boolean isReverseScroll = position < mCurrentPosition;
        Log.d("Bill", "当前position = " + position + ", 当前方向 = " + isReverseScroll);

        // 删除，前后各保留五条
        int startPos = position - 5;
        int endPos = position + 5;
        Log.d("Bill", "删除安全范围：" + startPos + " ~ " + endPos);
        PreloadManager.getInstance().removePreloadTaskAndDiskOutOfRange(startPos, endPos);

        if (isReverseScroll) {
            for (int i = position - 1; i >= Math.max(position - 2, 0); i--) {
                String url = getVideoUrl(i);
                if (!TextUtils.isEmpty(url)) {
                    PreloadManager.getInstance().addPreloadTask(url, i, false);
                }
            }
        } else {
            for (int i = position + 1; i <= Math.min(position + 3, getItemCount()); i++) {
                String url = getVideoUrl(i);
                if (!TextUtils.isEmpty(url)) {
                    PreloadManager.getInstance().addPreloadTask(url, i, false);
                    preloadImg(i);
                }
            }
        }

        mCurrentPosition = position;

    }

    private String getVideoUrl(int position) {
        if (position < 0 || position >= mDataList.size()) {
            return null;
        }
        SmallVideoBean videoItemBean = mDataList.get(position);
        return videoItemBean.path;
    }

    private void preloadImg(int position) {
        if (position < 0 || position >= mDataList.size()) {
            return;
        }

        SmallVideoBean videoItemBean = mDataList.get(position);
        Glide.with(mContext).load(videoItemBean.image).preload();
    }

}
