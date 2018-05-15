package com.example.administrator.essim.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.essim.R;
import com.example.administrator.essim.activities.OnItemClickListener;
import com.example.administrator.essim.response.IllustCommentsResponse;
import com.example.administrator.essim.utils.GlideUtil;

import de.hdodenhof.circleimageview.CircleImageView;

public class IllustCommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LayoutInflater mLayoutInflater;
    private OnItemClickListener mOnItemClickListener;
    private IllustCommentsResponse mPixivRankItem;
    private Context mContext;

    public IllustCommentAdapter(IllustCommentsResponse pixivResponse, Context context) {
        mPixivRankItem = pixivResponse;
        mLayoutInflater = LayoutInflater.from(context);
        mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TagHolder(mLayoutInflater.inflate(R.layout.comment_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        Glide.with(mContext).load(new GlideUtil().getHead(mPixivRankItem.getComments().get(position).getUser().getId(),
                mPixivRankItem.getComments().get(position).getUser().getProfile_image_urls().getMedium()))
                .into(((TagHolder) holder).mCircleImageView);
        ((TagHolder) holder).mTextView.setText(mPixivRankItem.getComments().get(position).getComment());
        ((TagHolder) holder).mTextView2.setText(mPixivRankItem.getComments().get(position).getUser().getName());
        ((TagHolder) holder).mTextView3.setText(mPixivRankItem.getComments().get(position).getDate().substring(0,
                mPixivRankItem.getComments().get(position).getDate().length() - 15));
        if (mPixivRankItem.getComments().get(position).getParent_comment().getComment() == null) {
            ((TagHolder) holder).mRelativeLayout.setVisibility(View.GONE);
        } else {
            ((TagHolder) holder).mTextView4.setText(String.format("@%s",
                    mPixivRankItem.getComments().get(position).getParent_comment().getUser().getName()));
            ((TagHolder) holder).mTextView5.setText(mPixivRankItem.getComments().get(position).getParent_comment().getComment());
        }
        ((TagHolder) holder).itemView.setOnClickListener(view -> mOnItemClickListener.onItemClick(view, position, 0));
        ((TagHolder) holder).mCircleImageView.setOnClickListener(view -> mOnItemClickListener.onItemClick(view, position, 1));
        ((TagHolder) holder).mTextView2.setOnClickListener(view -> mOnItemClickListener.onItemClick(view, position, 1));
    }


    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @Override
    public int getItemCount() {
        return mPixivRankItem.getComments().size();
    }

    private static class TagHolder extends RecyclerView.ViewHolder {

        private RelativeLayout mRelativeLayout;
        private CircleImageView mCircleImageView;
        private TextView mTextView, mTextView2, mTextView3, mTextView4, mTextView5;

        private TagHolder(final View itemView) {
            super(itemView);
            mCircleImageView = itemView.findViewById(R.id.comment_head);
            mTextView = itemView.findViewById(R.id.comment_content);
            mTextView2 = itemView.findViewById(R.id.comment_name);
            mTextView3 = itemView.findViewById(R.id.comment_time);
            mRelativeLayout = itemView.findViewById(R.id.rela_parent);
            mTextView4 = itemView.findViewById(R.id.parent_comment_name);
            mTextView5 = itemView.findViewById(R.id.parent_comment);
        }
    }
}
