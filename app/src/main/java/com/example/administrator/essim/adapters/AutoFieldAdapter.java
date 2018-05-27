package com.example.administrator.essim.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.essim.R;
import com.example.administrator.essim.interf.OnItemClickListener;
import com.example.administrator.essim.response.PixivResponse;

public class AutoFieldAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LayoutInflater mLayoutInflater;
    private OnItemClickListener mOnItemClickListener;
    private PixivResponse mPixivRankItem;

    public AutoFieldAdapter(PixivResponse pixivResponse, Context context) {
        mPixivRankItem = pixivResponse;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TagHolder(mLayoutInflater.inflate(R.layout.item_custom_suggestion, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        ((TagHolder) holder).mTextView.setText(mPixivRankItem.getSearch_auto_complete_keywords().get(position));
        ((TagHolder) holder).itemView.setOnClickListener(view -> mOnItemClickListener.onItemClick(
                ((TagHolder) holder).mTextView, position, 0));
    }

    private int getContentItemCount() {
        return mPixivRankItem.getSearch_auto_complete_keywords().size();
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @Override
    public int getItemCount() {
        return getContentItemCount();
    }

    private static class TagHolder extends RecyclerView.ViewHolder {
        private TextView mTextView;

        private TagHolder(final View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.title);
        }
    }
}
