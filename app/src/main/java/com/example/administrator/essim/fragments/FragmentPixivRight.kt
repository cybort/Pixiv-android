package com.example.administrator.essim.fragments

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.administrator.essim.R
import com.example.administrator.essim.activities.SearchResultActivity
import com.example.administrator.essim.adapters.TrendingtagAdapter
import com.example.administrator.essim.interf.OnItemClickListener
import com.example.administrator.essim.network.AppApiPixivService
import com.example.administrator.essim.network.RestClient
import com.example.administrator.essim.response.TrendingtagResponse
import com.example.administrator.essim.utils.Common
import kotlinx.android.synthetic.main.fragment_pixiv_right.*
import retrofit2.Call
import retrofit2.Callback

class FragmentPixivRight : BaseFragment() {

    private var mPixivAdapter: TrendingtagAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_pixiv_right, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        mProgressbar.visibility = View.INVISIBLE
        val gridLayoutManager = GridLayoutManager(mContext, 3)
        mRecyclerView.layoutManager = gridLayoutManager
        mRecyclerView.setHasFixedSize(true)
    }

    fun getHotTags() {
        mProgressbar.visibility = View.VISIBLE
        val call = RestClient()
                .retrofit_AppAPI
                .create(AppApiPixivService::class.java)
                .getIllustTrendTags(Common.getLocalDataSet(mContext).getString("Authorization", "")!!)
        call.enqueue(object : Callback<TrendingtagResponse> {
            override fun onResponse(call: Call<TrendingtagResponse>, response: retrofit2.Response<TrendingtagResponse>) {
                try {
                    mPixivAdapter = TrendingtagAdapter(response.body()!!.trend_tags, mContext)
                    mPixivAdapter!!.setOnItemClickListener(object : OnItemClickListener {
                        override fun onItemClick(view: View, position: Int, viewType: Int) {
                            val intent = Intent(mContext, SearchResultActivity::class.java)
                            intent.putExtra("what is the keyword", response.body()!!.trend_tags[position].tag)
                            mContext.startActivity(intent)
                        }

                        override fun onItemLongClick(view: View, position: Int) {}
                    })
                    mRecyclerView.adapter = mPixivAdapter
                    mProgressbar.visibility = View.INVISIBLE
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<TrendingtagResponse>, throwable: Throwable) {}
        })
    }
}
