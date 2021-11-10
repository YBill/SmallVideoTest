package com.bill.smallvideotest;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * author ywb
 * date 2021/10/20
 * desc
 */
public class SmallVideoFragment extends Fragment {

    public static final String TAG = "SmallVideoFragment";

    private static final String[] PATHS = new String[]{
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
    };

    private RecyclerView mVideoRv;
    private SmallVideoListAdapter mAdapter;
    private PlayerHelper mPlayerHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_small_video, container, false);
        initViews(view);
        configViews();
        return view;
    }

    private void initViews(View view) {
        mVideoRv = view.findViewById(R.id.rv_video);
    }

    private void configViews() {
        mVideoRv.setLayoutManager(new VideoLayoutManager(getActivity()));
        mAdapter = new SmallVideoListAdapter(getActivity());
        mAdapter.setDataList(getData());
        mVideoRv.setAdapter(mAdapter);

        mPlayerHelper = new PlayerHelper(mVideoRv);
        mPlayerHelper.setOnPreLoadListener(new PlayerHelper.OnPreLoadListener() {
            @Override
            public void onLoad() {
                Log.e("Bill", "开始预加载数据");
                mAdapter.addDataList(getData());
            }
        });
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
        mPlayerHelper.release();
    }
}
