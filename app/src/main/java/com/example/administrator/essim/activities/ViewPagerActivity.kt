package com.example.administrator.essim.activities

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.example.administrator.essim.R
import com.example.administrator.essim.fragments.FragmentPixivItem
import com.example.administrator.essim.response.Reference
import kotlinx.android.synthetic.main.activity_view_pager.*

class ViewPagerActivity : AppCompatActivity() {

    lateinit var mViewPager : ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = Color.TRANSPARENT
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        setContentView(R.layout.activity_view_pager)

        val intent = intent
        val index = intent.getIntExtra("which one is selected", 0)

        mToolbar.setNavigationOnClickListener { finish() }
        val fragmentManager = supportFragmentManager
        mViewPager = findViewById(R.id.mViewPager)
        mViewPager.adapter = object : FragmentStatePagerAdapter(fragmentManager) {
            override fun getItem(position: Int): Fragment = FragmentPixivItem.newInstance(position)

            override fun getCount(): Int = Reference.sIllustsBeans.size
        }
        mViewPager.currentItem = index
    }

    fun changeTitle() {
        mToolbar.title = Reference.sIllustsBeans[mViewPager.currentItem].title
    }
}
