package com.example.administrator.essim.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.essim.R;
import com.example.administrator.essim.activities.ImageDetailActivity;
import com.example.administrator.essim.activities.SearchTagActivity;
import com.example.administrator.essim.activities.ViewPagerActivity;
import com.example.administrator.essim.adapters.IllustCommentAdapter;
import com.example.administrator.essim.anotherproj.CloudMainActivity;
import com.example.administrator.essim.api.AppApiPixivService;
import com.example.administrator.essim.network.RestClient;
import com.example.administrator.essim.response.IllustCommentsResponse;
import com.example.administrator.essim.response.IllustsBean;
import com.example.administrator.essim.utils.DividerItemDecoration;
import com.example.administrator.essim.utils.GlideUtil;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.Objects;

import jp.wasabeef.glide.transformations.BlurTransformation;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;


/**
 * Created by Administrator on 2018/1/18 0018.
 */

public class FragmentPixivItem extends BaseFragment {

    private IllustsBean mIllustsBeans;
    private RecyclerView mRecyclerView;
    private EditText mEditText;
    private SharedPreferences mSharedPreferences;
    private int parentCommentID;

    public static FragmentPixivItem newInstance(IllustsBean illustsBean, int index) {
        Bundle args = new Bundle();
        args.putSerializable("index", index);
        args.putSerializable("illust item", illustsBean);
        FragmentPixivItem fragment = new FragmentPixivItem();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pixiv_item, container, false);
        assert getArguments() != null;
        int index = (int) getArguments().getSerializable("index");
        mIllustsBeans = (IllustsBean) getArguments().getSerializable("illust item");
        if (index == ((ViewPagerActivity) Objects.requireNonNull(getActivity())).getIndexNow()) {
            setUserVisibleHint(true);
        }
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(mActivity, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        reFreshLayout(view);
        getIllustComment();
        return view;
    }

    private void reFreshLayout(View view) {
        ImageView imageView = view.findViewById(R.id.item_background_img);
        ImageView imageView2 = view.findViewById(R.id.detail_img);
        mRecyclerView = view.findViewById(R.id.recy_comment);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext,
                DividerItemDecoration.VERTICAL_LIST));
        ViewGroup.LayoutParams params = imageView2.getLayoutParams();
        params.height = (((getResources().getDisplayMetrics().widthPixels - getResources().getDimensionPixelSize(R.dimen.thirty_two_dp)) *
                mIllustsBeans.getHeight()) / mIllustsBeans.getWidth());
        imageView2.setLayoutParams(params);
        imageView2.setOnClickListener(view12 -> {
            Intent intent = new Intent(mContext, ImageDetailActivity.class);
            intent.putExtra("illust", mIllustsBeans);
            mContext.startActivity(intent);
        });
        Glide.get(mContext).clearMemory();
        Glide.with(getContext()).load(new GlideUtil().getMediumImageUrl(mIllustsBeans))
                .bitmapTransform(new BlurTransformation(mContext, 20, 2))
                .into(imageView);
        Glide.with(getContext()).load(new GlideUtil().getMediumImageUrl(mIllustsBeans))
                .into(imageView2);
        TextView textView = view.findViewById(R.id.detail_author);
        TextView textView2 = view.findViewById(R.id.detail_img_size);
        TextView textView3 = view.findViewById(R.id.detail_create_time);
        TextView textView4 = view.findViewById(R.id.viewed);
        TextView textView5 = view.findViewById(R.id.liked);
        TextView textView6 = view.findViewById(R.id.illust_id);
        TextView textView7 = view.findViewById(R.id.author_id);
        TextView textView8 = view.findViewById(R.id.all_item_size);
        TagFlowLayout mTagGroup = view.findViewById(R.id.tag_group);
        String allTag[] = new String[mIllustsBeans.getTags().size()];
        for (int i = 0; i < mIllustsBeans.getTags().size(); i++) {
            allTag[i] = mIllustsBeans.getTags().get(i).getName();
        }
        mTagGroup.setAdapter(new TagAdapter<String>(allTag) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) LayoutInflater.from(mContext).inflate(R.layout.tag_textview,
                        mTagGroup, false);
                tv.setText(s);
                return tv;
            }
        });
        mTagGroup.setOnTagClickListener((view15, position, parent) -> {
            Intent intent = new Intent(mContext, SearchTagActivity.class);
            intent.putExtra("what is the keyword", allTag[position]);
            mContext.startActivity(intent);
            return true;
        });
        textView.setText(getString(R.string.string_author,
                mIllustsBeans.getUser().getName()));
        textView.setOnClickListener(view1 -> {
            Intent intent = new Intent(mContext, CloudMainActivity.class);
            intent.putExtra("user id", mIllustsBeans.getUser().getId());
            mContext.startActivity(intent);
        });
        textView2.setText(getString(R.string.string_full_size, mIllustsBeans.getWidth(), mIllustsBeans.getHeight()));
        textView3.setText(getString(R.string.string_create_time, mIllustsBeans.getCreate_date().substring(0,
                mIllustsBeans.getCreate_date().length() - 9)));
        textView4.setText(mIllustsBeans.getTotal_view() >= 1000 ? getString(R.string.string_viewd,
                String.valueOf(mIllustsBeans.getTotal_view() / 1000)) : String.valueOf(mIllustsBeans.getTotal_view()));
        textView5.setText(mIllustsBeans.getTotal_bookmarks() >= 1000 ? getString(R.string.string_viewd,
                String.valueOf(mIllustsBeans.getTotal_bookmarks() / 1000)) : String.valueOf(mIllustsBeans.getTotal_bookmarks()));
        textView6.setText(getString(R.string.illust_id, String.valueOf(mIllustsBeans.getId())));
        textView7.setText(getString(R.string.author_id, String.valueOf(mIllustsBeans.getUser().getId())));
        if (mIllustsBeans.getPage_count() > 1) {
            textView8.setText(String.format("%sP", String.valueOf(mIllustsBeans.getPage_count())));
        } else {
            textView8.setVisibility(View.INVISIBLE);
        }
        mEditText = view.findViewById(R.id.comment_edit);
        ImageView imageView1 = view.findViewById(R.id.img_comment);
        imageView1.setOnClickListener(view13 -> postComment());
        ImageView imageView3 = view.findViewById(R.id.clear_hint);
        imageView3.setOnClickListener(view14 -> {
            if (mEditText.getText().toString().length() == 0) {
                mEditText.setHint("留下你的评论吧~");
                parentCommentID = 0;
            } else {
                mEditText.setText("");
            }
        });
    }

    private void getIllustComment() {
        Call<IllustCommentsResponse> call = new RestClient()
                .getRetrofit_AppAPI()
                .create(AppApiPixivService.class)
                .getIllustComments(mSharedPreferences.getString("Authorization", ""), mIllustsBeans.getId());
        call.enqueue(new Callback<IllustCommentsResponse>() {
            @Override
            public void onResponse(Call<IllustCommentsResponse> call, retrofit2.Response<IllustCommentsResponse> response) {
                IllustCommentsResponse illustCommentsResponse = response.body();
                IllustCommentAdapter adapter = new IllustCommentAdapter(illustCommentsResponse, mContext);
                adapter.setOnItemClickListener((view, position, viewType) -> {
                    if (viewType == 0) {
                        parentCommentID = illustCommentsResponse.getComments().get(position).getId();
                        mEditText.setHint(String.format("回复@%s", illustCommentsResponse.getComments().get(position).getUser().getName()));
                    } else if (viewType == 1) {
                        Intent intent = new Intent(mContext, CloudMainActivity.class);
                        intent.putExtra("user id", illustCommentsResponse.getComments().get(position).getUser().getId());
                        startActivity(intent);
                    }
                });
                mRecyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<IllustCommentsResponse> call, Throwable throwable) {

            }
        });
    }

    private void postComment() {
        if (mEditText.getText().toString().trim().length() != 0) {
            if (parentCommentID == 0) {
                Call<ResponseBody> call = new RestClient()
                        .getRetrofit_AppAPI()
                        .create(AppApiPixivService.class)
                        .postIllustComment(mSharedPreferences.getString("Authorization", ""), mIllustsBeans.getId(),
                                mEditText.getText().toString().trim(), null);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull retrofit2.Response<ResponseBody> response) {
                        Snackbar.make(mEditText, "评论成功~", Snackbar.LENGTH_SHORT).show();
                        mEditText.setText("");
                        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                        // 隐藏软键盘
                        assert imm != null;
                        imm.hideSoftInputFromWindow(Objects.requireNonNull(getActivity())
                                .getWindow().getDecorView().getWindowToken(), 0);
                        getIllustComment();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable throwable) {

                    }
                });
            } else {
                Call<ResponseBody> call = new RestClient()
                        .getRetrofit_AppAPI()
                        .create(AppApiPixivService.class)
                        .postIllustComment(mSharedPreferences.getString("Authorization", ""), mIllustsBeans.getId(),
                                mEditText.getText().toString().trim(), parentCommentID);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull retrofit2.Response<ResponseBody> response) {
                        Snackbar.make(mEditText, "评论成功~", Snackbar.LENGTH_SHORT).show();
                        mEditText.setText("");
                        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                        // 隐藏软键盘
                        assert imm != null;
                        imm.hideSoftInputFromWindow(Objects.requireNonNull(getActivity())
                                .getWindow().getDecorView().getWindowToken(), 0);
                        getIllustComment();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable throwable) {

                    }
                });
            }
        } else {
            Snackbar.make(mEditText, "能打几个字再评论不？！", Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (getActivity() != null) {
                ((ViewPagerActivity) getActivity()).changeTitle();
            }
        }
    }
}