package com.example.administrator.essim.interf

import android.view.View

interface OnItemClickListener {

    fun onItemClick(view: View, position: Int, viewType: Int)

    fun onItemLongClick(view: View, position: Int)
}