package com.example.administrator.essim.fragments;

import android.Manifest;
import android.animation.Animator;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.administrator.essim.R;
import com.example.administrator.essim.activities.CommentActivity;
import com.example.administrator.essim.activities.ImageDetailActivity;
import com.example.administrator.essim.activities.SearchTagActivity;
import com.example.administrator.essim.activities.UserDetailActivity;
import com.example.administrator.essim.activities.ViewPagerActivity;
import com.example.administrator.essim.download.DownloadTask;
import com.example.administrator.essim.download.SDDownloadTask;
import com.example.administrator.essim.response.Reference;
import com.example.administrator.essim.utils.Common;
import com.example.administrator.essim.utils.GlideUtil;
import com.sdsmdg.tastytoast.TastyToast;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.io.File;
import java.util.Objects;

import jp.wasabeef.glide.transformations.BlurTransformation;


/**
 * Created by Administrator on 2018/1/18 0018.
 */

public class FragmentPixivItem extends BaseFragment implements View.OnClickListener {

    public static Bitmap sGlideDrawable;
    private int index;
    private ProgressBar mProgressBar;
    private FloatingActionButton mFloatingActionButton;
    private Bitmap mBitmap;

    public static FragmentPixivItem newInstance(int index) {
        Bundle args = new Bundle();
        args.putSerializable("index", index);
        FragmentPixivItem fragment = new FragmentPixivItem();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pixiv_item, container, false);
        assert getArguments() != null;
        index = (int) getArguments().getSerializable("index");
        if (index == ((ViewPagerActivity) Objects.requireNonNull(getActivity())).mViewPager.getCurrentItem()) {
            setUserVisibleHint(true);
        }
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) mContext, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        reFreshLayout(view);
        return view;
    }

    private void reFreshLayout(View view) {
        ImageView imageView = view.findViewById(R.id.item_background_img);
        ImageView imageView2 = view.findViewById(R.id.detail_img);
        ViewGroup.LayoutParams params = imageView2.getLayoutParams();
        params.height = (((getResources().getDisplayMetrics().widthPixels - getResources().getDimensionPixelSize(R.dimen.thirty_two_dp)) *
                Reference.sIllustsBeans.get(index).getHeight()) / Reference.sIllustsBeans.get(index).getWidth());
        imageView2.setLayoutParams(params);
        imageView2.setOnClickListener(this);
        Glide.get(mContext).clearMemory();
        Glide.with(getContext()).load(new GlideUtil().getMediumImageUrl(Reference.sIllustsBeans.get(index)))
                .bitmapTransform(new BlurTransformation(mContext, 20, 2))
                .into(imageView);
        mProgressBar = view.findViewById(R.id.try_login);
        mProgressBar.setIndeterminateDrawable(Common.getLoaderAnimation(mContext));
        if (Common.getLocalDataSet(mContext).getBoolean("is_origin_pic", true)) {
            //如果按原图画质加载，则保存bitmap备用
            Glide.with(mContext).load(new GlideUtil().getLargeImageUrl(Reference.sIllustsBeans.get(index), 0))
                    .asBitmap().into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    mProgressBar.setVisibility(View.INVISIBLE);
                    mBitmap = resource;
                    imageView2.setImageBitmap(resource);
                }
            });
        } else {
            Glide.with(mContext).load(new GlideUtil().getMediumImageUrl(Reference.sIllustsBeans.get(index)))
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(new GlideDrawableImageViewTarget(imageView2) {
                        @Override
                        public void onResourceReady(GlideDrawable drawable, GlideAnimation anim) {
                            mProgressBar.setVisibility(View.INVISIBLE);
                            super.onResourceReady(drawable, anim);
                        }
                    });
        }
        TextView textView = view.findViewById(R.id.detail_author);
        TextView textView2 = view.findViewById(R.id.detail_img_size);
        TextView textView3 = view.findViewById(R.id.detail_create_time);
        TextView textView4 = view.findViewById(R.id.viewed);
        TextView textView5 = view.findViewById(R.id.liked);
        TextView textView6 = view.findViewById(R.id.illust_id);
        TextView textView7 = view.findViewById(R.id.author_id);
        TextView textView8 = view.findViewById(R.id.all_item_size);
        TagFlowLayout mTagGroup = view.findViewById(R.id.tag_group);
        String allTag[] = new String[Reference.sIllustsBeans.get(index).getTags().size()];
        for (int i = 0; i < Reference.sIllustsBeans.get(index).getTags().size(); i++) {
            allTag[i] = Reference.sIllustsBeans.get(index).getTags().get(i).getName();
        }
        mTagGroup.setAdapter(new TagAdapter<String>(allTag) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) LayoutInflater.from(mContext).inflate(R.layout.tag_textview,
                        mTagGroup, false);
                tv.setText(s);
                tv.setOnClickListener(v -> {
                    Intent intent = new Intent(mContext, SearchTagActivity.class);
                    intent.putExtra("what is the keyword", allTag[position]);
                    mContext.startActivity(intent);
                });
                tv.setOnLongClickListener(v -> {
                    Common.copyMessage(mContext, s);
                    return true;
                });
                return tv;
            }
        });
        mFloatingActionButton = view.findViewById(R.id.fab_like);
        textView.setText(getString(R.string.string_author,
                Reference.sIllustsBeans.get(index).getUser().getName()));
        textView.setOnClickListener(this);
        textView2.setText(getString(R.string.string_full_size, Reference.sIllustsBeans.get(index).getWidth(),
                Reference.sIllustsBeans.get(index).getHeight()));
        textView3.setText(getString(R.string.string_create_time, Reference.sIllustsBeans.get(index).getCreate_date().
                substring(0, Reference.sIllustsBeans.get(index).getCreate_date().length() - 9)));
        textView4.setText(Reference.sIllustsBeans.get(index).getTotal_view() >= 1000 ? getString(R.string.string_viewd,
                String.valueOf(Reference.sIllustsBeans.get(index).getTotal_view() / 1000)) :
                String.valueOf(Reference.sIllustsBeans.get(index).getTotal_view()));
        textView5.setText(Reference.sIllustsBeans.get(index).getTotal_bookmarks() >= 1000 ? getString(R.string.string_viewd,
                String.valueOf(Reference.sIllustsBeans.get(index).getTotal_bookmarks() / 1000)) :
                String.valueOf(Reference.sIllustsBeans.get(index).getTotal_bookmarks()));
        textView6.setText(getString(R.string.illust_id, String.valueOf(Reference.sIllustsBeans.get(index).getId())));
        textView7.setText(getString(R.string.author_id, String.valueOf(Reference.sIllustsBeans.get(index).getUser().getId())));
        if (Reference.sIllustsBeans.get(index).getPage_count() > 1) {
            textView8.setText(String.format("%sP", String.valueOf(Reference.sIllustsBeans.get(index).getPage_count())));
        } else {
            textView8.setVisibility(View.INVISIBLE);
        }
        mFloatingActionButton.setImageResource(Reference.sIllustsBeans.get(index).isIs_bookmarked() ?
                R.drawable.ic_favorite_white_24dp : R.drawable.ic_favorite_border_black_24dp);
        mFloatingActionButton.setOnClickListener(this);
        mFloatingActionButton.setOnLongClickListener(view1 -> {
            if (!Reference.sIllustsBeans.get(index).isIs_bookmarked()) {
                mFloatingActionButton.setImageResource(R.drawable.ic_favorite_white_24dp);
                mFloatingActionButton.startAnimation(Common.getAnimation());
                Reference.sIllustsBeans.get(index).setIs_bookmarked(true);
                Common.postStarIllust(index, Reference.sIllustsBeans,
                        Common.getLocalDataSet(mContext).getString("Authorization", ""), mContext, "private");

                Animator anim = ViewAnimationUtils.createCircularReveal(getView(), (int) mFloatingActionButton.getX(),
                        (int) mFloatingActionButton.getY(),
                        0, (float) Math.hypot(getView().getWidth(), getView().getHeight()));
                anim.setDuration(600);
                anim.start();
            }
            return true;
        });
        CardView cardView = view.findViewById(R.id.card_left);
        cardView.setOnClickListener(this);
        CardView cardView2 = view.findViewById(R.id.card_right);
        cardView2.setOnClickListener(this);
        Common.showLog(Reference.sIllustsBeans.get(index).getMeta_pages().size());
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.detail_img:
                sGlideDrawable = mBitmap;
                intent = new Intent(mContext, ImageDetailActivity.class);
                intent.putExtra("illust", Reference.sIllustsBeans.get(index));
                mContext.startActivity(intent);
                break;
            case R.id.detail_author:
                intent = new Intent(mContext, UserDetailActivity.class);
                intent.putExtra("user id", Reference.sIllustsBeans.get(index).getUser().getId());
                mContext.startActivity(intent);
                break;
            case R.id.card_left:
                File parentFile = new File(Common.getLocalDataSet(mContext).getString("download_path", "/storage/emulated/0/PixivPictures"));
                if (!parentFile.exists()) {
                    parentFile.mkdir();
                    TastyToast.makeText(mContext, "文件夹创建成功~",
                            TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
                }
                File realFile = new File(parentFile.getPath(), Reference.sIllustsBeans.get(index).getTitle() + "_" +
                        Reference.sIllustsBeans.get(index).getId() + "_" + String.valueOf(0) + ".jpeg");
                if (realFile.exists()) {
                    TastyToast.makeText(mContext, "该文件已存在~",
                            TastyToast.LENGTH_SHORT, TastyToast.CONFUSING).show();
                } else {
                    if (mBitmap != null) {  //如果bitmap不为空，说明加载了原图，直接保存bitmap到本地
                        Common.saveBitmap(mContext, realFile, mBitmap, Reference.sIllustsBeans.get(index), 0);
                    } else {
                        if (Common.getLocalDataSet(mContext).getString("download_path", "/storage/emulated/0/PixivPictures").contains("emulated")) {
                            //下载至内置SD存储介质，使用传统文件模式;
                            new DownloadTask(realFile, mContext, Reference.sIllustsBeans.get(index))
                                    .execute(Reference.sIllustsBeans.get(index).getMeta_single_page().getOriginal_image_url());
                        } else {//下载至可插拔SD存储介质，使用SAF 框架，DocumentFile文件模式;
                            new SDDownloadTask(realFile, mContext, Reference.sIllustsBeans.get(index), Common.getLocalDataSet(mContext))
                                    .execute(Reference.sIllustsBeans.get(index).getMeta_single_page().getOriginal_image_url());
                        }
                    }
                }
                break;
            case R.id.card_right:
                intent = new Intent(mContext, CommentActivity.class);
                intent.putExtra("title", Reference.sIllustsBeans.get(index).getTitle());
                intent.putExtra("id", Reference.sIllustsBeans.get(index).getId());
                mContext.startActivity(intent);
                break;
            case R.id.fab_like:
                if (Reference.sIllustsBeans.get(index).isIs_bookmarked()) {
                    mFloatingActionButton.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                    mFloatingActionButton.startAnimation(Common.getAnimation());
                    Reference.sIllustsBeans.get(index).setIs_bookmarked(false);
                    Common.postUnstarIllust(index, Reference.sIllustsBeans,
                            Common.getLocalDataSet(mContext).getString("Authorization", ""), mContext);
                } else {
                    mFloatingActionButton.setImageResource(R.drawable.ic_favorite_white_24dp);
                    mFloatingActionButton.startAnimation(Common.getAnimation());
                    Reference.sIllustsBeans.get(index).setIs_bookmarked(true);
                    Common.postStarIllust(index, Reference.sIllustsBeans,
                            Common.getLocalDataSet(mContext).getString("Authorization", ""), mContext, "public");
                }
                Animator anim = ViewAnimationUtils.createCircularReveal(getView(), (int) mFloatingActionButton.getX(),
                        (int) mFloatingActionButton.getY(),
                        0, (float) Math.hypot(getView().getWidth(), getView().getHeight()));
                anim.setDuration(600);
                anim.start();
                break;
            default:
                break;
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