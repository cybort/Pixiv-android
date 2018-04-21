package com.example.administrator.essim.interfaces;

import android.view.View;

public interface OnTagListItemClickListener {
    void onItemClick(View view, int position);

    void onSearch(View view, String searchKey, int position);
}
