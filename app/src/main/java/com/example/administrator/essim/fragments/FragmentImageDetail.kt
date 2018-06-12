package com.example.administrator.essim.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget
import com.example.administrator.essim.R
import com.example.administrator.essim.activities.ImageDetailActivity
import com.example.administrator.essim.utils.GlideUtil
import com.github.ybq.android.spinkit.style.Wave
import kotlinx.android.synthetic.main.fragment_image_detail.*

class FragmentImageDetail : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_image_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val index = arguments!!.getSerializable("index") as Int
        val illustsBean = ((activity) as ImageDetailActivity).mIllustsBean
        val wave = Wave()
        mProgressbar.indeterminateDrawable = wave
        Glide.get(mContext).clearMemory()
        if (index == 0) {    //加载第一张图的时候很有可能可以调用内存中缓存好的bitmap
            if (FragmentPixivItem.sGlideDrawable != null) { //有bitmap就拿来用
                mProgressbar.visibility = View.INVISIBLE
                originalImage.setImageBitmap(FragmentPixivItem.sGlideDrawable)
            } else {     //没有bitmap就加载网络中的
                Glide.with(mContext).load<GlideUrl>(GlideUtil().getLargeImageUrl(illustsBean, index))
                        .into<GlideDrawableImageViewTarget>(object : GlideDrawableImageViewTarget(originalImage) {
                            override fun onResourceReady(drawable: GlideDrawable?, animation: GlideAnimation<in GlideDrawable>?) {
                                mProgressbar.visibility = View.INVISIBLE
                                super.onResourceReady(drawable, animation)
                            }
                        })
            }
        } else {  //其他页面直接老老实实加载网络中的
            Glide.with(mContext).load<GlideUrl>(GlideUtil().getLargeImageUrl(illustsBean, index))
                    .into<GlideDrawableImageViewTarget>(object : GlideDrawableImageViewTarget(originalImage) {
                        override fun onResourceReady(drawable: GlideDrawable?, animation: GlideAnimation<in GlideDrawable>?) {
                            mProgressbar.visibility = View.INVISIBLE
                            super.onResourceReady(drawable, animation)
                        }
                    })
        }
    }

    companion object {
        fun newInstance(position: Int): FragmentImageDetail {
            val args = Bundle()
            args.putSerializable("index", position)
            val fragment = FragmentImageDetail()
            fragment.arguments = args
            return fragment
        }
    }
}
