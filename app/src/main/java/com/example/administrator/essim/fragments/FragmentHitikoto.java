package com.example.administrator.essim.fragments;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.example.administrator.essim.R;
import com.example.administrator.essim.activities.MainActivity;
import com.example.administrator.essim.models.HitoModel;
import com.example.administrator.essim.models.HitokotoType;
import com.example.administrator.essim.utils.Common;
import com.google.gson.Gson;
import com.sdsmdg.tastytoast.TastyToast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Administrator on 2018/1/15 0015.
 */

public class FragmentHitikoto extends BaseFragment{

    private final String url_head = "https://v1.hitokoto.cn/?c=";
    private TextView mTextView1, mTextView2, mTextView3, mTextView4, mTextView5, mTextView6;
    private Button mButton, mButton2;
    private Toolbar mToolbar;
    private CardView mCardView;
    private String responseData, finalResponseData, catname, type;
    private Gson gson = new Gson();
    private HitoModel mHitoModel;
    private AppCompatSpinner mAppCompatSpinner;
    public static boolean need_to_refresh = false;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_hitikoto, container, false);
        initView(v);
        return v;
    }

    private void initView(View view) {
        mCardView = view.findViewById(R.id.card_hitokoto);
        mTextView1 = view.findViewById(R.id.hitokoto_text);
        mTextView2 = view.findViewById(R.id.hitokoto_author);
        mTextView3 = view.findViewById(R.id.hitokoto_date);
        mTextView4 = view.findViewById(R.id.hitokoto_catname);
        mTextView5 = view.findViewById(R.id.hitokoto_resouce);
        mTextView6 = view.findViewById(R.id.toolbar_title_two);
        mTextView6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.sDrawerLayout.openDrawer(Gravity.START, true);
            }
        });
        mButton = view.findViewById(R.id.refresh);
        mButton2 = view.findViewById(R.id.collect_it);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData(url_head + catname);
            }
        });
        mButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HitoModel hitoModel = mHitoModel;
                hitoModel.save();
                need_to_refresh = true;
                TastyToast.makeText(mContext, "已添加到收藏~",
                        TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
            }
        });
        mAppCompatSpinner = view.findViewById(R.id.spinner);
        mAppCompatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        catname = "";
                        break;
                    case 1:
                        catname = "a";
                        break;
                    case 2:
                        catname = "b";
                        break;
                    case 3:
                        catname = "c";
                        break;
                    case 4:
                        catname = "d";
                        break;
                    case 5:
                        catname = "e";
                        break;
                    case 6:
                        catname = "f";
                        break;
                    case 7:
                        catname = "g";
                        break;
                }
                getData(url_head + catname);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        mToolbar = view.findViewById(R.id.toolbar_hitokoto);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.sDrawerLayout.openDrawer(Gravity.START, true);
            }
        });
    }

    private void getData(String address) {
        Common.sendOkhttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                responseData = response.body().string();
                finalResponseData = responseData.replaceAll("from", "from_where");
                mHitoModel = gson.fromJson(finalResponseData, HitoModel.class);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mTextView1.setText(mHitoModel.getHitokoto());
                        mTextView2.setText("作者：" + mHitoModel.getCreator());
                        mTextView3.setText(Common.getTime(mHitoModel.getCreated_at()));
                        mTextView4.setText(mHitoModel.getFrom_where());
                        mTextView4.requestFocus();
                        mTextView5.setText(HitokotoType.getType(mHitoModel.getType()));
                    }
                });
            }
        });
    }

    @Override
    public void onHiddenChanged(boolean hide) {
        if (hide) {

        } else {
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_hitokoto, menu);
    }

    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        setHasOptionsMenu(true);
    }
}