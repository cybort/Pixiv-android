package com.example.administrator.essim.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.essim.R;
import com.example.administrator.essim.interfaces.OnItemClickListener;
import com.example.administrator.essim.models.AuthorWorks;


public class AuthorWorksAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int ITEM_TYPE_HEADER = 0;
    private final int ITEM_TYPE_CONTENT = 1;
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private AuthorWorks mBooksInfo;
    private String mString;
    private OnItemClickListener mOnItemClickListener;

    public AuthorWorksAdapter(AuthorWorks booksInfo, Context context, String adapterType) {
        mBooksInfo = booksInfo;
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mString = adapterType;
    }

    @Override
    public int getItemViewType(int position) {
        if (position < 1) {
            return ITEM_TYPE_HEADER;
        } else {
            return ITEM_TYPE_CONTENT;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mString.equals("searchResult")) {
            // no need to set headLayout
            return new ContentViewHolder(mLayoutInflater.inflate(R.layout.author_detail_item, parent, false));
        } else {
            if (viewType == ITEM_TYPE_HEADER) {
                return new ItemHolder(mLayoutInflater.inflate(R.layout.head_blank, parent, false));
            } else {
                return new ContentViewHolder(mLayoutInflater.inflate(R.layout.author_detail_item, parent, false));
            }
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (mString.equals("searchResult")) {
            // no need to set headLayout
            bindData(holder, position);
        } else {
            if (holder instanceof ItemHolder) {
                //create a empty headLayout
            } else {
                bindData(holder, position - 1);
            }
        }
    }

    private void bindData(final RecyclerView.ViewHolder holder, final int position) {
        ((ContentViewHolder) holder).mTextView.setText(mBooksInfo.response.get(position).getTitle());
        if (mBooksInfo.response.get(position).stats.getViews_count().length() <= 3) {
            ((ContentViewHolder) holder).mTextView4.setText(mBooksInfo.response.get(position).stats.getViews_count());
        } else {
            ((ContentViewHolder) holder).mTextView4.setText(mContext.getString(R.string.string_viewd,
                    mBooksInfo.response.get(position).stats.getViews_count().substring(0,
                            mBooksInfo.response.get(position).stats.getViews_count().length() - 3)));
        }
        Glide.with(mContext).load(mBooksInfo.response.get(position).image_urls.getPx_480mw())
                .into(((ContentViewHolder) holder).mImageView);
        ((ContentViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemClickListener.onItemClick(view, position);
            }
        });
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @Override
    public int getItemCount() {
        return mBooksInfo.response.size();
    }

    public static class ContentViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView;
        TextView mTextView4;
        ImageView mImageView;
        Button mButton;

        private ContentViewHolder(View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.author_item_title);
            mTextView4 = itemView.findViewById(R.id.viewed);
            mImageView = itemView.findViewById(R.id.author_item_img);
            mButton = itemView.findViewById(R.id.check_detail);
        }
    }

    private class ItemHolder extends RecyclerView.ViewHolder {
        private ItemHolder(View itemView) {
            super(itemView);
        }
    }
}