package com.example.administrator.essim.fragments

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.administrator.essim.R
import com.example.administrator.essim.activities.ViewPagerActivity
import com.example.administrator.essim.adapters.PixivAdapterGrid
import com.example.administrator.essim.interf.OnItemClickListener
import com.example.administrator.essim.network.AppApiPixivService
import com.example.administrator.essim.network.OAuthSecureService
import com.example.administrator.essim.network.RestClient
import com.example.administrator.essim.response.IllustsBean
import com.example.administrator.essim.response.PixivOAuthResponse
import com.example.administrator.essim.response.RecommendResponse
import com.example.administrator.essim.response.Reference
import com.example.administrator.essim.utils.Common
import kotlinx.android.synthetic.main.fragment_pixiv_left.*
import retrofit2.Call
import retrofit2.Callback
import java.util.*

class FragmentPixivLeft : BaseFragment() {

    private var nextDataUrl: String? = null
    private var mPixivAdapter: PixivAdapterGrid? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_pixiv_left, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        getData()
    }

    private fun initView() {
        mProgressbar.visibility = View.INVISIBLE
        val gridLayoutManager = GridLayoutManager(mContext, 2)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when {
                    mPixivAdapter!!.getItemViewType(position) == 2 -> gridLayoutManager.spanCount
                    else -> 1
                }
            }
        }
        mRecyclerView.layoutManager = gridLayoutManager
        mRecyclerView.setHasFixedSize(true)
    }

    private fun getData() {
        mProgressbar.visibility = View.VISIBLE
        val call = RestClient()
                .retrofit_AppAPI
                .create(AppApiPixivService::class.java)
                .getRecommend(Common.getLocalDataSet().getString("Authorization", "")!!)
        call.enqueue(object : Callback<RecommendResponse> {
            override fun onResponse(call: Call<RecommendResponse>, response: retrofit2.Response<RecommendResponse>) {
                try {
                    nextDataUrl = response.body()!!.next_url
                    initAdapter(response.body()!!.illusts)
                    mProgressbar.visibility = View.INVISIBLE
                    //通知FragmentPixivRight 加载热门标签
                    ((parentFragment as FragmentPixiv).mFragments[1] as FragmentPixivRight).getHotTags()
                } catch (e: Exception) {
                    reLogin()
                }
            }

            override fun onFailure(call: Call<RecommendResponse>, throwable: Throwable) {}
        })
    }

    private fun getNextData() {
        mProgressbar.visibility = View.VISIBLE
        val call = RestClient()
                .retrofit_AppAPI
                .create(AppApiPixivService::class.java)
                .getNext(Common.getLocalDataSet().getString("Authorization", "")!!, nextDataUrl!!)
        call.enqueue(object : Callback<RecommendResponse> {
            override fun onResponse(call: Call<RecommendResponse>, response: retrofit2.Response<RecommendResponse>) {
                nextDataUrl = response.body()!!.next_url
                initAdapter(response.body()!!.illusts)
                mProgressbar.visibility = View.INVISIBLE
            }

            override fun onFailure(call: Call<RecommendResponse>, throwable: Throwable) {}
        })
    }

    private fun initAdapter(illustsBeans: List<IllustsBean>) {
        mPixivAdapter = PixivAdapterGrid(illustsBeans, mContext)
        mPixivAdapter!!.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(view: View, position: Int, viewType: Int) {
                when {
                    position == -1 -> getNextData()
                    viewType == 0 -> {
                        Reference.sIllustsBeans = illustsBeans
                        val intent = Intent(mContext, ViewPagerActivity::class.java)
                        intent.putExtra("which one is selected", position)
                        mContext.startActivity(intent)
                    }
                    viewType == 1 -> if (!illustsBeans[position].isIs_bookmarked) {
                        (view as ImageView).setImageResource(R.drawable.ic_favorite_white_24dp)
                        view.startAnimation(Common.getAnimation())
                        Common.postStarIllust(position, illustsBeans,
                                Common.getLocalDataSet().getString("Authorization", ""), mContext, "public")
                    } else {
                        (view as ImageView).setImageResource(R.drawable.ic_favorite_border_black_24dp)
                        view.startAnimation(Common.getAnimation())
                        Common.postUnstarIllust(position, illustsBeans,
                                Common.getLocalDataSet().getString("Authorization", ""), mContext)
                    }
                }
            }

            override fun onItemLongClick(view: View, position: Int) {
                when {
                    !illustsBeans[position].isIs_bookmarked -> {
                        (view as ImageView).setImageResource(R.drawable.ic_favorite_white_24dp)
                        Common.postStarIllust(position, illustsBeans,
                                Common.getLocalDataSet().getString("Authorization", ""), mContext, "private")
                    }
                }
            }
        })
        mRecyclerView.adapter = mPixivAdapter
    }

    private fun reLogin() {
        Snackbar.make(mRecyclerView, "获取登录信息, 请稍候", Snackbar.LENGTH_SHORT).show()
        val localHashMap = HashMap<String, Any>()
        localHashMap["client_id"] = "KzEZED7aC0vird8jWyHM38mXjNTY"
        localHashMap["client_secret"] = "W9JZoJe00qPvJsiyCGT3CCtC6ZUtdpKpzMbNlUGP"
        localHashMap["grant_type"] = "password"
        localHashMap["username"] = Common.getLocalDataSet().getString("useraccount", "")
        localHashMap["password"] = Common.getLocalDataSet().getString("password", "")
        val call = RestClient().getretrofit_OAuthSecure().create(OAuthSecureService::class.java).postAuthToken(localHashMap)
        call.enqueue(object : Callback<PixivOAuthResponse> {
            override fun onResponse(call: Call<PixivOAuthResponse>, response: retrofit2.Response<PixivOAuthResponse>) {
                val pixivOAuthResponse = response.body()
                val editor = Common.getLocalDataSet().edit()
                try {
                    val localStringBuilder = "Bearer " + pixivOAuthResponse!!.response.access_token
                    editor.putString("Authorization", localStringBuilder)
                    editor.putBoolean("ispremium", pixivOAuthResponse.response.user.isIs_premium)
                    editor.apply()
                    getData()
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }

            override fun onFailure(call: Call<PixivOAuthResponse>, throwable: Throwable) {}
        })
    }

    override fun onResume() {
        super.onResume()
        when (mPixivAdapter) {
            null -> return
            else -> mPixivAdapter!!.notifyDataSetChanged()
        }
    }
}
