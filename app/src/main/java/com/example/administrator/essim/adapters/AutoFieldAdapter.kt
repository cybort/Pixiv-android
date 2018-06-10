package com.example.administrator.essim.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.administrator.essim.R
import com.example.administrator.essim.interf.OnItemClickListener
import com.example.administrator.essim.response.PixivResponse
import kotlinx.android.synthetic.main.item_custom_suggestion.view.*

class AutoFieldAdapter(private val mPixivRankItem: PixivResponse,
                       context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val mLayoutInflater: LayoutInflater = LayoutInflater.from(context)
    private var mOnItemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            TagHolder(mLayoutInflater.inflate(R.layout.item_custom_suggestion, parent, false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as TagHolder).itemView.title.text = mPixivRankItem.search_auto_complete_keywords[position]
        holder.itemView.setOnClickListener { mOnItemClickListener!!.onItemClick(holder.itemView.title, position, 0) }
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        mOnItemClickListener = onItemClickListener
    }

    override fun getItemCount(): Int = mPixivRankItem.search_auto_complete_keywords.size

    private class TagHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
