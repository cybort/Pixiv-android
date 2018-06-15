package com.example.administrator.essim.fragments

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.view.*
import android.widget.ImageView
import com.example.administrator.essim.R
import com.example.administrator.essim.activities.MainActivity
import com.example.administrator.essim.activities.SearchActivity
import com.example.administrator.essim.activities.SettingsActivity
import com.example.administrator.essim.activities.ViewPagerActivity
import com.example.administrator.essim.adapters.PixivAdapterGrid
import com.example.administrator.essim.interf.OnItemClickListener
import com.example.administrator.essim.network.AppApiPixivService
import com.example.administrator.essim.network.RestClient
import com.example.administrator.essim.response.*
import com.example.administrator.essim.utils.Common
import com.nightonke.boommenu.BoomButtons.TextInsideCircleButton
import com.nightonke.boommenu.Util
import kotlinx.android.synthetic.main.fragment_rank.*
import retrofit2.Call
import retrofit2.Callback


/**
 * Created by Administrator on 2018/1/15 0015.
 */

class FragmentRank : BaseFragment() {

    private val arrayOfRankMode = arrayOf("动态", "日榜", "周榜", "月榜", "新人", "原创", "男性向", "女性向")
    private val modeList = arrayOf("day", "week", "month", "week_rookie", "week_original", "day_male", "day_female")
    private var currentDataType = -1
    private var nextDataUrl: String? = null
    private var mPixivAdapter: PixivAdapterGrid? = null
    private val clickListener = { index: Int ->
        when (index) {
            0 -> if (currentDataType != -1) {
                currentDataType = -1
                getFollowUserNewIllust()
            }
            1 -> if (currentDataType != 0) {
                currentDataType = 0
                getRankList(currentDataType)
            }
            2 -> if (currentDataType != 1) {
                currentDataType = 1
                getRankList(currentDataType)
            }
            3 -> if (currentDataType != 2) {
                currentDataType = 2
                getRankList(currentDataType)
            }
            4 -> if (currentDataType != 3) {
                currentDataType = 3
                getRankList(currentDataType)
            }
            5 -> if (currentDataType != 4) {
                currentDataType = 4
                getRankList(currentDataType)
            }
            6 -> if (currentDataType != 5) {
                currentDataType = 5
                getRankList(currentDataType)
            }
            7 -> if (currentDataType != 6) {
                currentDataType = 6
                getRankList(currentDataType)
            }
            else -> {
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_rank, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        getFollowUserNewIllust()
    }

    private fun initView() {
        val gridLayoutManager = GridLayoutManager(mContext, 2)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when {
                    mPixivAdapter!!.getItemViewType(position) == 2 -> gridLayoutManager.spanCount
                    else -> 1
                }
            }
        }
        (activity as AppCompatActivity).setSupportActionBar(mToolbar)
        mToolbar.setNavigationOnClickListener { (activity as MainActivity).drawer.openDrawer(Gravity.START, true) }
        mToolbar.title = "动态"
        mRecyclerView.layoutManager = gridLayoutManager
        mRecyclerView.setHasFixedSize(true)
        mFab.isUse3DTransformAnimation = true
        mFab.showDuration = 400
        mFab.hideDuration = 400
        mFab.frames = 60
        mFab.normalColor = resources.getColor(R.color.colorAccent)
        for (i in 0 until mFab.piecePlaceEnum.pieceNumber()) {
            val builder = TextInsideCircleButton.Builder()
                    .normalImageRes(R.drawable.ic_card_giftcard_black_24dp)
                    .imagePadding(Rect(20, 20, 20, 60))
                    .normalText(arrayOfRankMode[i])
                    .textRect(Rect(Util.dp2px(15f), Util.dp2px(42f), Util.dp2px(65f), Util.dp2px(72f)))
                    .textSize(16)
                    .listener(clickListener)
            mFab.addBuilder(builder)
        }
    }

    private fun getFollowUserNewIllust() {
        mProgressbar.visibility = View.VISIBLE
        val call = RestClient()
                .retrofit_AppAPI
                .create(AppApiPixivService::class.java)
                .getFollowIllusts(Common.getLocalDataSet().getString("Authorization", "")!!, "public")
        call.enqueue(object : Callback<IllustfollowResponse> {
            override fun onResponse(call: Call<IllustfollowResponse>,
                                    response: retrofit2.Response<IllustfollowResponse>) {
                val illustfollowResponse = response.body()
                nextDataUrl = illustfollowResponse!!.next_url
                initAdapter(illustfollowResponse.illusts)
                mToolbar.title = "动态"
                mProgressbar.visibility = View.INVISIBLE
            }

            override fun onFailure(call: Call<IllustfollowResponse>, throwable: Throwable) {}
        })
    }

    private fun getRankList(dataType: Int) {
        mProgressbar.visibility = View.VISIBLE
        val call = RestClient()
                .retrofit_AppAPI
                .create(AppApiPixivService::class.java)
                .getIllustRanking(Common.getLocalDataSet().getString("Authorization", "")!!,
                        modeList[dataType], null)
        call.enqueue(object : Callback<IllustRankingResponse> {
            override fun onResponse(call: Call<IllustRankingResponse>,
                                    response: retrofit2.Response<IllustRankingResponse>) {
                nextDataUrl = response.body()!!.next_url
                initAdapter(response.body()!!.illusts)
                mToolbar.title = arrayOfRankMode[currentDataType + 1]
                mProgressbar.visibility = View.INVISIBLE
            }

            override fun onFailure(call: Call<IllustRankingResponse>, throwable: Throwable) {}
        })
    }

    private fun getNextData() {
        if (nextDataUrl != null) {
            mProgressbar.visibility = View.VISIBLE
            val call = RestClient()
                    .retrofit_AppAPI
                    .create(AppApiPixivService::class.java)
                    .getNext(Common.getLocalDataSet().getString("Authorization", "")!!, nextDataUrl!!)
            call.enqueue(object : Callback<RecommendResponse> {
                override fun onResponse(call: Call<RecommendResponse>,
                                        response: retrofit2.Response<RecommendResponse>) {
                    nextDataUrl = response.body()!!.next_url
                    initAdapter(response.body()!!.illusts)
                    mProgressbar.visibility = View.INVISIBLE
                }

                override fun onFailure(call: Call<RecommendResponse>, throwable: Throwable) {}
            })
        } else {
            Snackbar.make(mProgressbar, "再怎么找也找不到了~", Snackbar.LENGTH_SHORT).show()
        }
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
                if (!illustsBeans[position].isIs_bookmarked) {
                    (view as ImageView).setImageResource(R.drawable.ic_favorite_white_24dp)
                    Common.postStarIllust(position, illustsBeans,
                            Common.getLocalDataSet().getString("Authorization", ""), mContext, "private")
                }
            }
        })
        mRecyclerView.adapter = mPixivAdapter
    }

    override fun onCreate(b: Bundle?) {
        super.onCreate(b)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater!!.inflate(R.menu.main_pixiv, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val intent: Intent
        return when (item!!.itemId) {
            R.id.action_search -> {
                intent = Intent(mContext, SearchActivity::class.java)
                mContext.startActivity(intent)
                true
            }
            R.id.action_settings -> {
                intent = Intent(mContext, SettingsActivity::class.java)
                mContext.startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {
        super.onResume()
        if (mPixivAdapter != null) {
            mPixivAdapter!!.notifyDataSetChanged()
        }
    }
}