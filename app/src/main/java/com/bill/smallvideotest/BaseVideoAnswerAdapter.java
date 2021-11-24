package com.bill.smallvideotest;

import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bill.smallvideotest.cache.PreloadManager;

import java.util.List;

/**
 * author ywb
 * date 2021/11/24
 * desc
 */
public abstract class BaseVideoAnswerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public List<SmallVideoBean> mDataList;

    private int mCurrentPosition = -1;

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
            for (int i = position - 1; i >= Math.max(position - 1, 0); i--) {
                String url = getVideoUrl(i);
                if (!TextUtils.isEmpty(url)) {
                    PreloadManager.getInstance().addPreloadTask(url, i, false);
                }
            }
        } else {
            for (int i = position + 1; i <= Math.min(position + 2, getItemCount()); i++) {
                String url = getVideoUrl(i);
                if (!TextUtils.isEmpty(url)) {
                    PreloadManager.getInstance().addPreloadTask(url, i, false);
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

}
