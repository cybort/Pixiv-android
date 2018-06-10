package com.example.administrator.essim.interf

import android.view.View

interface OnListHitokotoClickListener {

    fun onItemClick(view: View, position: Int, code: Int)

    fun onItemLongClick(view: View, position: Int)
}
