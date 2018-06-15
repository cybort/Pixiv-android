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
import com.example.administrator.essim.response.SpecialCollectionResponse
import com.example.administrator.essim.utils.Common
import com.example.administrator.essim.utils.GlideUtil
import kotlinx.android.synthetic.main.bottom_refresh_widely.view.*
import kotlinx.android.synthetic.main.recy_special_collec.view.*

class SpecialCollecAdapter(private val mPixivRankItem: SpecialCollectionResponse,
                           private val mContext: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val itemBottom = 2
    private val itemContent = 1
    private var mOnItemClickListener: OnItemClickListener? = null
    private val mLayoutInflater: LayoutInflater = LayoutInflater.from(mContext)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            when (viewType) {
                itemContent -> TagHolder(mLayoutInflater.inflate(R.layout.recy_special_collec, parent, false))
                else -> BottomViewHolder(mLayoutInflater.inflate(R.layout.bottom_refresh_widely, parent, false))
            }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) = when (holder) {
        is TagHolder -> {
            val params: ViewGroup.LayoutParams = holder.itemView.imageView.layoutParams
            params.height = (mContext.resources.displayMetrics.widthPixels - mContext.resources.getDimensionPixelSize(R.dimen.thirty_two_dp)) / 3 * 2
            holder.itemView.imageView.layoutParams = params
            Glide.with(mContext).load(GlideUtil().getHead(mPixivRankItem.body[position].thumbnailUrl))
                    .into(holder.itemView.imageView)
            holder.itemView.text_title.text = mPixivRankItem.body[position].title
            holder.itemView.text_date.text = Common.getTime(mPixivRankItem.body[position].publishDate, 0)
            holder.itemView.setOnClickListener { v -> mOnItemClickListener!!.onItemClick(v, position, 0) }
        }
        else -> (holder as BottomViewHolder).itemView.card_footer
                .setOnClickListener { mOnItemClickListener!!.onItemClick(holder.itemView, -1, 0) }
    }

    override fun getItemViewType(position: Int): Int = when {
        position >= mPixivRankItem.body.size -> itemBottom
        else -> itemContent
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        mOnItemClickListener = onItemClickListener
    }

    override fun getItemCount(): Int = mPixivRankItem.body.size + 1

    private class BottomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    private inner class TagHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
