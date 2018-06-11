package com.example.administrator.essim.fragments

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import com.example.administrator.essim.R
import com.example.administrator.essim.activities.MainActivity
import com.example.administrator.essim.activities.SearchActivity
import com.example.administrator.essim.activities.SettingsActivity
import com.example.administrator.essim.adapters.FragmentPixivAdapter
import kotlinx.android.synthetic.main.fragment_pixiv.*
import java.util.*


/**
 * Created by Administrator on 2018/1/15 0015.
 */

class FragmentPixiv : BaseFragment() {

    private val mFragments = ArrayList<Fragment>()
    private val mTitles = arrayOf("为你推荐", "热门标签")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_pixiv, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        (activity as MainActivity).setSupportActionBar(mToolbar)
        mToolbar.setNavigationOnClickListener { (activity as MainActivity).drawer.openDrawer(Gravity.START, true) }
        mFragments.add(FragmentPixivLeft())
        mFragments.add(FragmentPixivRight())
        val adapter = FragmentPixivAdapter(fragmentManager!!, mFragments, mTitles)
        mViewPager.adapter = adapter
        mTabLayout.setupWithViewPager(mViewPager)
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
}