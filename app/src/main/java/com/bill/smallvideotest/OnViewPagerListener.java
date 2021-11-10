package com.bill.smallvideotest;

/**
 * author ywb
 * date 2021/10/20
 * desc
 */
public interface OnViewPagerListener {

    void onPageRelease(int position);

    void onPageSelected(int position, boolean isBottom);

    void preLoad();

}
