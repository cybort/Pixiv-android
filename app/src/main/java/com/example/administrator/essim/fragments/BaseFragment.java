package com.example.administrator.essim.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.essim.utils.Common;

/**
 * Created by Administrator on 2018/3/26 0026.
 */

public class BaseFragment extends Fragment {

    public Context mContext;
    public Activity mActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
        mActivity = getActivity();
    }
}