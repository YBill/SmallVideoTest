package com.bill.smallvideotest;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

/**
 * author ywb
 * date 2021/10/20
 * desc
 */
public class VideoLayoutManager extends LinearLayoutManager implements RecyclerView.OnChildAttachStateChangeListener {

    //实现page的效果
    private final PagerSnapHelper mPagerSnapHelper;

    private OnViewPagerListener mOnViewPagerListener;

    public VideoLayoutManager(Context context) {
        super(context);
        mPagerSnapHelper = new PagerSnapHelper();
    }

    @Override
    public void onAttachedToWindow(RecyclerView view) {
        view.addOnChildAttachStateChangeListener(this);
        mPagerSnapHelper.attachToRecyclerView(view);
        super.onAttachedToWindow(view);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            super.onLayoutChildren(recycler, state);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    public void setOnViewPagerListener(OnViewPagerListener onViewPagerListener) {
        this.mOnViewPagerListener = onViewPagerListener;
    }

    @Override
    public void onScrollStateChanged(int state) {
        switch (state) {
            case RecyclerView.SCROLL_STATE_IDLE:
                View snapView = mPagerSnapHelper.findSnapView(this);
                int position = 0;
                if (snapView != null) {
                    position = getPosition(snapView);
                }
                if (mOnViewPagerListener != null) {
                    mOnViewPagerListener.onPageSelected(position, getItemCount());
                }
                if (position == getItemCount() - 2 && mOnViewPagerListener != null) {
                    mOnViewPagerListener.preLoad();
                }
                break;
        }
        super.onScrollStateChanged(state);
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        return super.scrollVerticallyBy(dy, recycler, state);
    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        return super.scrollHorizontallyBy(dx, recycler, state);
    }

    @Override
    public boolean canScrollVertically() {
        return true;
    }

    @Override
    public void onChildViewAttachedToWindow(@NonNull View view) {
        if (getChildCount() == 1 && getPosition(view) == 0) {
            if (mOnViewPagerListener != null)
                mOnViewPagerListener.onPageSelected(getPosition(view), getItemCount());
        }
    }

    @Override
    public void onChildViewDetachedFromWindow(@NonNull View view) {
        if (mOnViewPagerListener != null)
            mOnViewPagerListener.onPageRelease(getPosition(view));
    }
}