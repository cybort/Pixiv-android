package com.example.administrator.essim.activities

import android.os.Bundle
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity

import com.example.administrator.essim.R
import com.example.administrator.essim.fragments.FragmentSpecialCollec

class SpecialCollectionActivity : AppCompatActivity() {

    lateinit var mFragmentSpecialCollec: FragmentSpecialCollec

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_special_collection)

        mFragmentSpecialCollec = FragmentSpecialCollec()
        val transaction = supportFragmentManager.beginTransaction()
        if (!mFragmentSpecialCollec.isAdded) {
            transaction.add(R.id.special_collec_container, mFragmentSpecialCollec)
        }
        transaction.show(mFragmentSpecialCollec).commit()
    }
}
