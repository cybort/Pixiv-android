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
import com.example.administrator.essim.response.IllustCommentsResponse
import com.example.administrator.essim.utils.GlideUtil
import kotlinx.android.synthetic.main.comment_item.view.*

class IllustCommentAdapter(private val mPixivRankItem: List<IllustCommentsResponse.CommentsBean>,
                           private val mContext: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val mLayoutInflater: LayoutInflater = LayoutInflater.from(mContext)
    private var mOnItemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            TagHolder(mLayoutInflater.inflate(R.layout.comment_item, parent, false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Glide.with(mContext).load<GlideUrl>(GlideUtil().getHead(mPixivRankItem[position].user.id.toLong(),
                mPixivRankItem[position].user.profile_image_urls.medium))
                .into((holder as TagHolder).itemView.comment_head)
        holder.itemView.comment_content.text = mPixivRankItem[position].comment
        holder.itemView.comment_name.text = mPixivRankItem[position].user.name
        holder.itemView.comment_time.text = mPixivRankItem[position].date.substring(0, mPixivRankItem[position].date.length - 15)
        when {
            mPixivRankItem[position].parent_comment.comment == null -> holder.itemView.rela_parent.visibility = View.GONE
            else -> {
                holder.itemView.rela_parent.visibility = View.VISIBLE
                holder.itemView.parent_comment_name.text = String.format("@%s", mPixivRankItem[position].parent_comment.user.name)
                holder.itemView.parent_comment.text = mPixivRankItem[position].parent_comment.comment
            }
        }
        holder.itemView.setOnClickListener { view -> mOnItemClickListener!!.onItemClick(view, position, 0) }
        holder.itemView.comment_head.setOnClickListener { view -> mOnItemClickListener!!.onItemClick(view, position, 1) }
        holder.itemView.comment_name.setOnClickListener { view -> mOnItemClickListener!!.onItemClick(view, position, 1) }
    }


    fun setOnItemClickListener(mOnItemClickListener: OnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener
    }

    override fun getItemCount(): Int = mPixivRankItem.size

    private class TagHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
