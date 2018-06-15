package com.example.administrator.essim.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.example.administrator.essim.R
import com.example.administrator.essim.interf.OnItemClickListener
import com.example.administrator.essim.response.IllustsBean
import com.example.administrator.essim.utils.GlideUtil
import kotlinx.android.synthetic.main.bottom_refresh.view.*
import kotlinx.android.synthetic.main.pixiv_item_grid.view.*

/**
 * Created by Administrator on 2018/3/23 0023.
 */

class AuthorWorksAdapter(private val mPixivRankItem: List<IllustsBean>,
                         private val mContext: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val itemHead = 3
    private val itemBottom = 2
    private val itemContent = 1
    private var mOnItemClickListener: OnItemClickListener? = null
    private val mLayoutInflater: LayoutInflater = LayoutInflater.from(mContext)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            when (viewType) {
                itemHead -> ItemHolder(mLayoutInflater.inflate(R.layout.head_blank, parent, false))
                itemBottom -> BottomViewHolder(mLayoutInflater.inflate(R.layout.bottom_refresh, parent, false))
                else -> PhotoHolder(mLayoutInflater.inflate(R.layout.pixiv_item_grid, parent, false))
            }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, p: Int) = when (holder) {
        is PhotoHolder -> {
            val position = p - 1
            Glide.with(mContext).load(GlideUtil().getMediumImageUrl(mPixivRankItem[position]))
                    .into(holder.itemView.pixiv_image)
            when {
                mPixivRankItem[position].isIs_bookmarked -> holder.itemView.post_like.setImageResource(R.drawable.ic_favorite_white_24dp)
                else -> holder.itemView.post_like.setImageResource(R.drawable.ic_favorite_border_black_24dp)
            }
            when {
                mPixivRankItem[position].page_count > 1 ->
                    holder.itemView.pixiv_item_size.text = String.format("%sP", mPixivRankItem[position].page_count.toString())
                else -> holder.itemView.pixiv_item_size.visibility = View.INVISIBLE
            }
            //单个item被点击，交给外界处理
            holder.itemView.setOnClickListener { mOnItemClickListener!!.onItemClick(holder.itemView, position, 0) }
            //设置收藏按钮的点击事件
            holder.itemView.post_like.setOnClickListener { v ->
                mOnItemClickListener!!.onItemClick(holder.itemView.post_like, position, 1)
                val anim = ViewAnimationUtils.createCircularReveal(holder.itemView,
                        v.x.toInt(),
                        v.y.toInt(),
                        0f, Math.hypot(holder.itemView.width.toDouble(),
                        holder.itemView.height.toDouble()).toFloat())
                anim.duration = 400
                anim.start()
            }
            //设置收藏按钮的长按事件
            holder.itemView.post_like.setOnLongClickListener { v ->
                mOnItemClickListener!!.onItemLongClick(holder.itemView.post_like, position)
                val anim = ViewAnimationUtils.createCircularReveal(holder.itemView,
                        v.x.toInt(),
                        v.y.toInt(),
                        0f, Math.hypot(holder.itemView.width.toDouble(),
                        holder.itemView.height.toDouble()).toFloat())
                anim.duration = 400
                anim.start()
                true
            }
        }
        is BottomViewHolder -> {
            holder.itemView.get_more_data.text = "加载更多"
            holder.itemView.card_footer.setOnClickListener { mOnItemClickListener!!.onItemClick(holder.itemView, -1, 0) }
        }
        else -> {
        }
    }

    override fun getItemViewType(position: Int): Int = when (position) {
        0 -> itemHead
        else -> if (position > 0 && position < mPixivRankItem.size + 1) {
            itemContent
        } else {
            itemBottom
        }
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        mOnItemClickListener = onItemClickListener
    }

    override fun getItemCount(): Int = when {
        mPixivRankItem.isEmpty() -> 0
        else -> mPixivRankItem.size + 2
    }

    private class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    private class PhotoHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    private class BottomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
