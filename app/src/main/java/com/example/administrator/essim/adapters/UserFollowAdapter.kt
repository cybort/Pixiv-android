package com.example.administrator.essim.adapters

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.example.administrator.essim.R
import com.example.administrator.essim.interf.OnItemClickListener
import com.example.administrator.essim.response.SearchUserResponse
import com.example.administrator.essim.utils.Common
import com.example.administrator.essim.utils.GlideUtil
import kotlinx.android.synthetic.main.bottom_refresh.view.*
import kotlinx.android.synthetic.main.user_follow_item.view.*

/**
 * Created by Administrator on 2018/1/15 0015.
 */

class UserFollowAdapter(private val mBooksInfo: List<SearchUserResponse.UserPreviewsBean>,
                        private val mContext: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val itemBottom = 2
    private val itemContent = 1
    private var mOnItemClickListener: OnItemClickListener? = null
    private val mLayoutInflater: LayoutInflater = LayoutInflater.from(mContext)
    private val mSharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            when (viewType) {
                itemContent -> ContentViewHolder(mLayoutInflater.inflate(R.layout.user_follow_item, parent, false))
                else -> BottomViewHolder(mLayoutInflater.inflate(R.layout.bottom_refresh, parent, false))
            }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) = when (holder) {
        is ContentViewHolder -> {
            //加载用户名和头像
            holder.itemView.user_name.text = mBooksInfo[position].user.name
            Glide.with(mContext).load(GlideUtil().getHead(mBooksInfo[position]))
                    .into(holder.itemView.user_head)
            //个别画师的展示图不足三个，所以只有足够的展示图的时候才展示作品
            when {
                mBooksInfo[position].illusts.size == 3 -> {
                    Glide.with(mContext).load(GlideUtil().getMediumImageUrl(mBooksInfo[position].illusts[0]))
                            .into(holder.itemView.user_show_one)
                    Glide.with(mContext).load(GlideUtil().getMediumImageUrl(mBooksInfo[position].illusts[1]))
                            .into(holder.itemView.user_show_two)
                    Glide.with(mContext).load(GlideUtil().getMediumImageUrl(mBooksInfo[position].illusts[2]))
                            .into(holder.itemView.user_show_three)
                }
            }
            when {
                mBooksInfo[position].user.isIs_followed -> holder.itemView.post_like_user.text = "取消关注"
                !mBooksInfo[position].user.isIs_followed -> holder.itemView.post_like_user.text = "+关注"
            }

            holder.itemView.post_like_user.setOnClickListener {
                when {
                    mBooksInfo[position].user.isIs_followed -> {
                        holder.itemView.post_like_user.text = "+关注"
                        Common.postUnFollowUser(mSharedPreferences.getString("Authorization", ""),
                                mBooksInfo[position].user.id, holder.itemView.post_like_user)
                        mBooksInfo[position].user.isIs_followed = false
                    }
                    else -> {
                        holder.itemView.post_like_user.text = "取消关注"
                        Common.postFollowUser(mSharedPreferences.getString("Authorization", ""),
                                mBooksInfo[position].user.id, holder.itemView.post_like_user, "public")
                        mBooksInfo[position].user.isIs_followed = true
                    }
                }
            }
            holder.itemView.post_like_user.setOnLongClickListener {
                when {
                    !mBooksInfo[position].user.isIs_followed -> {
                        holder.itemView.post_like_user.text = "取消关注"
                        Common.postFollowUser(mSharedPreferences.getString("Authorization", ""),
                                mBooksInfo[position].user.id, holder.itemView.post_like_user, "private")
                        mBooksInfo[position].user.isIs_followed = true
                    }
                }
                true
            }
            holder.itemView.setOnClickListener { mOnItemClickListener!!.onItemClick(holder.itemView, position, 0) }
        }
        else -> (holder as BottomViewHolder).itemView.card_footer
                .setOnClickListener { mOnItemClickListener!!.onItemClick(holder.itemView, -1, 0) }
    }

    override fun getItemViewType(position: Int): Int = when {
        position >= mBooksInfo.size -> itemBottom
        else -> itemContent
    }

    override fun getItemCount(): Int = mBooksInfo.size + 1

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        mOnItemClickListener = onItemClickListener
    }

    private class BottomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    private class ContentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
