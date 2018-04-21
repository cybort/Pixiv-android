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

/**
 * Created by Administrator on 2018/3/23 0023.
 */
public class PixivAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int ITEM_TYPE_CONTENT = 1;
    private final int ITEM_TYPE_BOTTOM = 2;
    private PixivRankItem mPixivRankItem;
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private int mBottomCount = 1;
    private OnItemClickListener mOnItemClickListener;

    public PixivAdapter(PixivRankItem item, Context context) {
        mPixivRankItem = item;
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_BOTTOM) {
            return new BottomViewHolder(mLayoutInflater.inflate(R.layout.bottom_refresh, parent, false));
        } else {
            return new PhotoHolder(mLayoutInflater.inflate(R.layout.pixiv_item, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int p) {
        int position = holder.getAdapterPosition();
        if (holder instanceof PhotoHolder) {
            Glide.with(mContext).load(mPixivRankItem.response.get(0).works.get(holder.getAdapterPosition()).work.image_urls.getPx_480mw())
                    .into(((PhotoHolder) holder).mImageView);
            ((PhotoHolder) holder).mTextView.setText(mPixivRankItem.response.get(0).works.get(position).work.getTitle());
            ((PhotoHolder) holder).mTextView2.setText(mContext.getString(R.string.string_author,
                    mPixivRankItem.response.get(0).works.get(position).work.user.getName()));
            ((PhotoHolder) holder).mTextView3.setText(mContext.getString(R.string.string_viewd,
                    mPixivRankItem.response.get(0).works.get(position).work.stats.getViews_count().substring(0, mPixivRankItem.response.get(0).works.get(position)
                            .work.stats.getViews_count().length() - 3)));
            if (mPixivRankItem.response.get(0).works.get(position).work.stats.getScored_count().length() <= 3) {
                ((PhotoHolder) holder).mTextView4.setText(mPixivRankItem.response.get(0).works
                        .get(position).work.stats.getScored_count());
            } else {
                ((PhotoHolder) holder).mTextView4.setText(mContext.getString(R.string.string_viewd,
                        mPixivRankItem.response.get(0).works.get(position)
                                .work.stats.getScored_count().substring(0, mPixivRankItem.response.get(0).works.get(position)
                                .work.stats.getScored_count().length() - 3)));
            }
            if (mOnItemClickListener != null) {
                ((PhotoHolder) holder).itemView.setOnClickListener(v -> mOnItemClickListener.onItemClick(holder.itemView, position));
            }
        } else {
            ((BottomViewHolder) holder).mCardView.setOnClickListener(v -> mOnItemClickListener.onItemClick(holder.itemView, position));
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

    private static class PhotoHolder extends RecyclerView.ViewHolder {
        private ImageView mImageView;
        private TextView mTextView, mTextView2, mTextView3, mTextView4;

        private PhotoHolder(final View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.pixiv_image);
            mTextView = itemView.findViewById(R.id.pixiv_title);
            mTextView2 = itemView.findViewById(R.id.pixiv_author);
            mTextView3 = itemView.findViewById(R.id.viewed);
            mTextView4 = itemView.findViewById(R.id.liked);
        }
    }

    private class BottomViewHolder extends RecyclerView.ViewHolder {
        private CardView mCardView;

        private BottomViewHolder(View itemView) {
            super(itemView);
            mCardView = itemView.findViewById(R.id.card_footer);
        }
    }
}
