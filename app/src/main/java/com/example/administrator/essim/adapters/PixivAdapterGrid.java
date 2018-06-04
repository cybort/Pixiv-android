package com.example.administrator.essim.adapters;

import android.animation.Animator;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.essim.R;
import com.example.administrator.essim.interf.OnItemClickListener;
import com.example.administrator.essim.response.IllustsBean;
import com.example.administrator.essim.utils.Common;
import com.example.administrator.essim.utils.GlideUtil;

import java.util.List;

/**
 * Created by Administrator on 2018/3/23 0023.
 */
public class PixivAdapterGrid extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int ITEM_TYPE_BOTTOM = 2;
    private final int ITEM_TYPE_CONTENT = 1;
    private Context mContext;
    private int mBottomCount = 1;
    private LayoutInflater mLayoutInflater;
    private List<IllustsBean> mPixivRankItem;
    private OnItemClickListener mOnItemClickListener;

    public PixivAdapterGrid(List<IllustsBean> item, Context context) {
        mPixivRankItem = item;
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_BOTTOM) {
            return new BottomViewHolder(mLayoutInflater.inflate(R.layout.bottom_refresh, parent, false));
        } else {
            return new PhotoHolder(mLayoutInflater.inflate(R.layout.pixiv_item_grid, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PhotoHolder) {
            Glide.with(mContext).load(new GlideUtil().getMediumImageUrl(mPixivRankItem.get(position)))
                    .into(((PhotoHolder) holder).mImageView);
            if (mPixivRankItem.get(position).isIs_bookmarked()) {
                ((PhotoHolder) holder).mImageView2.setImageResource(R.drawable.ic_favorite_white_24dp);
            } else {
                ((PhotoHolder) holder).mImageView2.setImageResource(R.drawable.ic_favorite_border_black_24dp);
            }
            if (mPixivRankItem.get(position).getPage_count() > 1) {
                ((PhotoHolder) holder).mTextView.setText(String.format("%sP", String.valueOf(mPixivRankItem.get(position).getPage_count())));
            } else {
                ((PhotoHolder) holder).mTextView.setVisibility(View.INVISIBLE);
            }
            if (mOnItemClickListener != null) {
                ((PhotoHolder) holder).itemView.setOnClickListener(v -> mOnItemClickListener.onItemClick(holder.itemView, position, 0));
                ((PhotoHolder) holder).mImageView2.setOnClickListener(v -> {
                    mOnItemClickListener.onItemClick(((PhotoHolder) holder).mImageView2, position, 1);
                    Animator anim = ViewAnimationUtils.createCircularReveal(((PhotoHolder) holder).itemView,
                            (int) v.getX(),
                            (int) v.getY(),
                            0, (float) Math.hypot(((PhotoHolder) holder).itemView.getWidth(),
                                    ((PhotoHolder) holder).itemView.getHeight()));
                    anim.setDuration(400);
                    anim.start();
                });
                ((PhotoHolder) holder).mImageView2.setOnLongClickListener(v -> {
                    mOnItemClickListener.onItemLongClick(((PhotoHolder) holder).mImageView2, position);
                    Animator anim = ViewAnimationUtils.createCircularReveal(((PhotoHolder) holder).itemView,
                            (int) v.getX(),
                            (int) v.getY(),
                            0, (float) Math.hypot(((PhotoHolder) holder).itemView.getWidth(),
                                    ((PhotoHolder) holder).itemView.getHeight()));
                    anim.setDuration(400);
                    anim.start();
                    return true;
                });
            }
        } else {
            ((BottomViewHolder) holder).mCardView.setOnClickListener(v -> mOnItemClickListener.onItemClick(holder.itemView, -1, 0));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mBottomCount != 0 && position >= mPixivRankItem.size()) {
            return ITEM_TYPE_BOTTOM;
        } else {
            return ITEM_TYPE_CONTENT;
        }
    }

    private int getContentItemCount() {
        return mPixivRankItem.size();
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @Override
    public int getItemCount() {
        return getContentItemCount() + mBottomCount;
    }

    private static class BottomViewHolder extends RecyclerView.ViewHolder {
        private CardView mCardView;

        private BottomViewHolder(View itemView) {
            super(itemView);
            mCardView = itemView.findViewById(R.id.card_footer);
        }
    }

    private class PhotoHolder extends RecyclerView.ViewHolder {
        private ImageView mImageView, mImageView2;
        private TextView mTextView;

        private PhotoHolder(final View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.pixiv_image);
            mImageView2 = itemView.findViewById(R.id.post_like);
            mTextView = itemView.findViewById(R.id.pixiv_item_size);
        }
    }
}
