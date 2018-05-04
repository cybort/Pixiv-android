package com.example.administrator.essim.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.essim.R;
import com.example.administrator.essim.interfaces.OnItemClickListener;
import com.example.administrator.essim.models.PixivRankItem;
import com.example.administrator.essim.utils.Common;

/**
 * Created by Administrator on 2018/3/23 0023.
 */
public class PixivAdapterGrid extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int ITEM_TYPE_CONTENT = 1;
    private final int ITEM_TYPE_BOTTOM = 2;
    private PixivRankItem mPixivRankItem;
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private int mBottomCount = 1;
    private OnItemClickListener mOnItemClickListener;

    public PixivAdapterGrid(PixivRankItem item, Context context) {
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
            Glide.with(mContext).load(mPixivRankItem.response.get(0).works.get(position).work.image_urls.getPx_480mw())
                    .into(((PhotoHolder) holder).mImageView);
            if (mPixivRankItem.response.get(0).works.get(position).work.getPage_count() > 1) {
                Common.showLog(mPixivRankItem.response.get(0).works.get(position).work.getPage_count());
                ((PhotoHolder) holder).mTextView.setVisibility(View.VISIBLE);
                ((PhotoHolder) holder).mTextView.setText(String.format("%sP", String.valueOf(mPixivRankItem.response.get(0).works.get(position).work.getPage_count())));
            } else {
                ((PhotoHolder) holder).mTextView.setVisibility(View.INVISIBLE);
            }
            if (mOnItemClickListener != null) {
                ((PhotoHolder) holder).itemView.setOnClickListener(v -> mOnItemClickListener.onItemClick(holder.itemView, position));
            }
        } else {
            ((BottomViewHolder) holder).mCardView.setOnClickListener(v -> mOnItemClickListener.onItemClick(holder.itemView, -1));
        }
    }

    @Override
    public int getItemViewType(int position) {
        int dataItemCount = getContentItemCount();
        if (mBottomCount != 0 && position >= dataItemCount) {
            return ITEM_TYPE_BOTTOM;
        } else {
            return ITEM_TYPE_CONTENT;
        }
    }

    private int getContentItemCount() {
        return mPixivRankItem.response.get(0).works.size();
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @Override
    public int getItemCount() {
        return getContentItemCount() + mBottomCount;
    }

    private class PhotoHolder extends RecyclerView.ViewHolder {
        private ImageView mImageView;
        private TextView mTextView;

        private PhotoHolder(final View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.pixiv_image);
            mTextView = itemView.findViewById(R.id.pixiv_item_size);
        }
    }

    private static class BottomViewHolder extends RecyclerView.ViewHolder {
        private CardView mCardView;

        private BottomViewHolder(View itemView) {
            super(itemView);
            mCardView = itemView.findViewById(R.id.card_footer);
        }
    }
}
