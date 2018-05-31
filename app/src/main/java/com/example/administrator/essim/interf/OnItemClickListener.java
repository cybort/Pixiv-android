package com.example.administrator.essim.interf;

import android.view.View;

public interface OnItemClickListener {
    void onItemClick(View view, int position, int viewType);
    void onItemLongClick(View view, int position);
}
