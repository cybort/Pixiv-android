package com.example.administrator.essim.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar

import com.example.administrator.essim.R
import com.example.administrator.essim.adapters.IllustCommentAdapter
import com.example.administrator.essim.interf.OnItemClickListener
import com.example.administrator.essim.network.AppApiPixivService
import com.example.administrator.essim.network.RestClient
import com.example.administrator.essim.response.IllustCommentsResponse
import com.example.administrator.essim.utils.Common
import com.example.administrator.essim.utils.DividerItemDecoration
import com.sdsmdg.tastytoast.TastyToast
import kotlinx.android.synthetic.main.activity_commetn.*

import java.util.ArrayList

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback

class CommentActivity : AppCompatActivity() {

    private var title: String? = null
    private var illustID: Int = 0
    private var mContext: Context? = null
    private var parentCommentID: Int = 0
    private var isLoadingMore: Boolean = false
    private var illustCommentAdapter: IllustCommentAdapter? = null
    private var mIllustCommentsResponse: IllustCommentsResponse? = null
    private val mCommentsBeanList = ArrayList<IllustCommentsResponse.CommentsBean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_commetn)

        mContext = this
        val intent = intent
        title = intent.getStringExtra("title")
        illustID = intent.getIntExtra("id", 0)
        initView()
        getIllustComment()
    }

    private fun initView() {
        mToolbar.setNavigationOnClickListener { finish() }
        mToolbar.title = title!! + "的评论"
        val linearLayoutManager = LinearLayoutManager(mContext)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.layoutManager = linearLayoutManager
        mProgressbar.visibility = View.INVISIBLE
        mRecyclerView.addItemDecoration(DividerItemDecoration(mContext!!, DividerItemDecoration.VERTICAL_LIST))
        mRecyclerView.setOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition()
                val totalItemCount = illustCommentAdapter!!.itemCount
                when {
                    lastVisibleItem >= totalItemCount - 1 && dy > 0 && !isLoadingMore -> {
                        getMoreComment()
                        isLoadingMore = true
                    }
                }
            }
        })
        sendComment.setOnClickListener { postComment() }
        clearHint.setOnClickListener {
            when {
                mEditText.text.toString().trim { it <= ' ' }.isNotEmpty() -> mEditText.setText("")
                else -> when {
                    parentCommentID != 0 -> {
                        parentCommentID = 0
                        mEditText.hint = "留下你的评论吧~"
                    }
                }
            }
        }
    }

    private fun postComment() {
        if (mEditText.text.toString().trim { it <= ' ' }.isNotEmpty()) {
            when (parentCommentID) {
                //没有父评论，就是普通评论
                0 -> {
                    val call = RestClient()
                            .retrofit_AppAPI
                            .create(AppApiPixivService::class.java)
                            .postIllustComment(Common.getLocalDataSet().getString("Authorization", "")!!, illustID.toLong(),
                                    mEditText.text.toString().trim { it <= ' ' }, null)
                    call.enqueue(object : Callback<ResponseBody> {
                        override fun onResponse(call: Call<ResponseBody>, response: retrofit2.Response<ResponseBody>) {
                            TastyToast.makeText(mContext, "评论成功~", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show()
                            getIllustComment()
                        }

                        override fun onFailure(call: Call<ResponseBody>, throwable: Throwable) {}
                    })
                }
                //有父评论则是在回复他人
                else -> {
                    val call = RestClient()
                            .retrofit_AppAPI
                            .create(AppApiPixivService::class.java)
                            .postIllustComment(Common.getLocalDataSet().getString("Authorization", "")!!, illustID.toLong(),
                                    mEditText.text.toString().trim { it <= ' ' }, parentCommentID)
                    call.enqueue(object : Callback<ResponseBody> {
                        override fun onResponse(call: Call<ResponseBody>, response: retrofit2.Response<ResponseBody>) {
                            TastyToast.makeText(mContext, "评论成功~", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show()
                            getIllustComment()
                            mEditText.hint = "留下你的评论吧~"
                        }

                        override fun onFailure(call: Call<ResponseBody>, throwable: Throwable) {}
                    })
                }
            }
            mEditText.setText("")
            val imm = mContext!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            // 隐藏软键盘
            imm.hideSoftInputFromWindow((mContext as Activity).window.decorView.windowToken, 0)
        } else {
            TastyToast.makeText(mContext, "评论不能为空~", TastyToast.LENGTH_SHORT, TastyToast.CONFUSING).show()
        }
    }

    private fun getIllustComment() {
        mProgressbar.visibility = View.VISIBLE
        val call = RestClient()
                .retrofit_AppAPI
                .create(AppApiPixivService::class.java)
                .getIllustComments(Common.getLocalDataSet().getString("Authorization", "")!!, illustID.toLong())
        call.enqueue(object : Callback<IllustCommentsResponse> {
            override fun onResponse(call: Call<IllustCommentsResponse>, response: retrofit2.Response<IllustCommentsResponse>) {
                mIllustCommentsResponse = response.body()
                mCommentsBeanList.clear()
                mCommentsBeanList.addAll(mIllustCommentsResponse!!.comments)
                illustCommentAdapter = IllustCommentAdapter(mCommentsBeanList, mContext!!)
                illustCommentAdapter!!.setOnItemClickListener(object : OnItemClickListener {
                    override fun onItemClick(view: View, position: Int, viewType: Int) {
                        when (viewType) {
                            0 -> if (mEditText.text.toString().trim { it <= ' ' }.isEmpty()) {
                                parentCommentID = mCommentsBeanList[position].id
                                mEditText.hint = String.format("回复@%s", mCommentsBeanList[position].user.name)
                            }
                            1 -> {
                                val intent = Intent(mContext, UserDetailActivity::class.java)
                                intent.putExtra("user id", mCommentsBeanList[position].user.id)
                                startActivity(intent)
                            }
                        }
                    }

                    override fun onItemLongClick(view: View, position: Int) {}
                })
                mRecyclerView.adapter = illustCommentAdapter
                mProgressbar.visibility = View.INVISIBLE
            }

            override fun onFailure(call: Call<IllustCommentsResponse>, throwable: Throwable) {}
        })
    }

    private fun getMoreComment() {
        if (mIllustCommentsResponse!!.next_url != null) {
            mProgressbar.visibility = View.VISIBLE
            val call = RestClient()
                    .retrofit_AppAPI
                    .create(AppApiPixivService::class.java)
                    .getNextComment(Common.getLocalDataSet().getString("Authorization", "")!!,
                            mIllustCommentsResponse!!.next_url)
            call.enqueue(object : Callback<IllustCommentsResponse> {
                override fun onResponse(call: Call<IllustCommentsResponse>, response: retrofit2.Response<IllustCommentsResponse>) {
                    mIllustCommentsResponse = response.body()
                    mCommentsBeanList.addAll(mIllustCommentsResponse!!.comments)
                    illustCommentAdapter!!.notifyDataSetChanged()
                    mProgressbar.visibility = View.INVISIBLE
                    isLoadingMore = false
                }

                override fun onFailure(call: Call<IllustCommentsResponse>, throwable: Throwable) {}
            })
        } else {
            Snackbar.make(mRecyclerView, "再怎么找也找不到了~", Snackbar.LENGTH_SHORT).show()
        }
    }
}
