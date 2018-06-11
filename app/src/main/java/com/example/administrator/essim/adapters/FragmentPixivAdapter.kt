package com.example.administrator.essim.adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class FragmentPixivAdapter(fm: FragmentManager, private val mFragments: List<Fragment>,
                           private val mFragmentTitles: Array<String>) : FragmentPagerAdapter(fm) {

    override fun getItem(arg0: Int): Fragment {
        return mFragments[arg0]
    }

    override fun getCount(): Int {
        return mFragments.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mFragmentTitles[position]
    }
}
