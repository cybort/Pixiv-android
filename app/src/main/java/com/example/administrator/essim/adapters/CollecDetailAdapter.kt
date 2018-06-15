package com.example.administrator.essim.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.example.administrator.essim.R
import com.example.administrator.essim.interf.OnItemClickListener
import com.example.administrator.essim.response.CollectionResponse
import com.example.administrator.essim.utils.GlideUtil
import kotlinx.android.synthetic.main.pixiv_item.view.*

class CollecDetailAdapter(private val mPixivRankItem: CollectionResponse,
                          private val mContext: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val mLayoutInflater: LayoutInflater = LayoutInflater.from(mContext)
    private var mOnItemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            TagHolder(mLayoutInflater.inflate(R.layout.pixiv_item, parent, false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        //为大图设置宽高，使大图的宽高相同
        val params: ViewGroup.LayoutParams = (holder as TagHolder).itemView.pixiv_image.layoutParams
        params.height = mContext.resources.displayMetrics.widthPixels - mContext.resources.getDimensionPixelSize(R.dimen.fourty_eight_dip)
        holder.itemView.pixiv_image.layoutParams = params
        //加载用户头像和图片
        Glide.with(mContext).load(GlideUtil().getMediumImageUrl(mPixivRankItem.body[0].illusts[position].illust_id,
                mPixivRankItem.body[0].illusts[position].url.`_$1200x1200`)).into(holder.itemView.pixiv_image)
        Glide.with(mContext).load(GlideUtil().getHead(java.lang.Long.parseLong(mPixivRankItem.body[0].illusts[position].illust_user_id),
                mPixivRankItem.body[0].illusts[position].user_icon)).into(holder.itemView.pixiv_head)
        //加载标题和作者名
        holder.itemView.pixiv_title.text = mPixivRankItem.body[0].illusts[position].illust_title
        holder.itemView.pixiv_author.text = mPixivRankItem.body[0].illusts[position].user_name
        //设置大图和头像的点击事件
        holder.itemView.setOnClickListener { v -> mOnItemClickListener!!.onItemClick(v, position, 0) }
        holder.itemView.pixiv_head.setOnClickListener { v -> mOnItemClickListener!!.onItemClick(v, position, 1) }
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        mOnItemClickListener = onItemClickListener
    }

    override fun getItemCount(): Int = mPixivRankItem.body[0].illusts.size

    private inner class TagHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
