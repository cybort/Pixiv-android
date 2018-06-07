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
        if (index == 0) {    //加载第一张图的时候很有可能可以调用内存中缓存好的bitmap
            if (FragmentPixivItem.sGlideDrawable != null) { //有bitmap就拿来用
                progressBar.setVisibility(View.INVISIBLE);
                imageView.setImageBitmap(FragmentPixivItem.sGlideDrawable);
            } else {     //没有bitmap就加载网络中的
                Glide.with(mContext).load(new GlideUtil().getLargeImageUrl(illustsBean, index))
                        .into(new GlideDrawableImageViewTarget(imageView) {
                            @Override
                            public void onResourceReady(GlideDrawable drawable, GlideAnimation anim) {
                                progressBar.setVisibility(View.INVISIBLE);
                                super.onResourceReady(drawable, anim);
                            }
                        });
            }
        } else {  //其他页面直接老老实实加载网络中的
            Glide.with(mContext).load(new GlideUtil().getLargeImageUrl(illustsBean, index))
                    .into(new GlideDrawableImageViewTarget(imageView) {
                        @Override
                        public void onResourceReady(GlideDrawable drawable, GlideAnimation anim) {
                            progressBar.setVisibility(View.INVISIBLE);
                            super.onResourceReady(drawable, anim);
                        }
                    });
        }
        return view;
    }
}
