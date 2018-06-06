package com.example.administrator.essim.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.example.administrator.essim.R;
import com.example.administrator.essim.activities.ImageDetailActivity;
import com.example.administrator.essim.response.IllustsBean;
import com.example.administrator.essim.utils.GlideUtil;
import com.github.ybq.android.spinkit.style.Wave;

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
        ProgressBar progressBar = view.findViewById(R.id.try_login);
        Wave wave = new Wave();
        progressBar.setIndeterminateDrawable(wave);
        Glide.get(mContext).clearMemory();
        Glide.with(mContext).load(new GlideUtil().getLargeImageUrl(illustsBean, index))
                .into(new GlideDrawableImageViewTarget(imageView) {
                    @Override
                    public void onResourceReady(GlideDrawable drawable, GlideAnimation anim) {
                        progressBar.setVisibility(View.INVISIBLE);
                        super.onResourceReady(drawable, anim);
                    }
                });
        return view;
    }
}
