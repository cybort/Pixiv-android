package com.example.administrator.essim.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.administrator.essim.R;
import com.example.administrator.essim.activities.ImageDetailActivity;
import com.example.administrator.essim.response.IllustsBean;
import com.example.administrator.essim.utils.GlideUtil;

import java.util.Objects;

public class FragmentImageDetail extends BaseFragment {

    public static FragmentImageDetail newInstance(int position) {
        Bundle args = new Bundle();
        args.putSerializable("index", position);
        FragmentImageDetail fragment = new FragmentImageDetail();
        fragment.setArguments(args);
        return fragment;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_detail, container, false);
        assert getArguments() != null;
        int index = (int) getArguments().getSerializable("index");
        ImageView imageView = view.findViewById(R.id.image);
        IllustsBean illustsBean = ((ImageDetailActivity) Objects.requireNonNull(getActivity())).mIllustsBean;
        Glide.get(mContext).clearMemory();
        Glide.with(mContext).load(new GlideUtil().getLargeImageUrl(illustsBean, index)).into(imageView);
        return view;
    }
}
