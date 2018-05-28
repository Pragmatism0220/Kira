package com.moemoe.lalala.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.moemoe.lalala.R;
import com.moemoe.lalala.event.OnItemListener;
import com.moemoe.lalala.view.base.BranchItemBean;
import com.moemoe.lalala.view.base.TestPrincipalInfo;

import java.util.List;

/**
 * Created by Administrator on 2018/5/15.
 */

public class BranchFragmentListAdapter extends RecyclerView.Adapter<BranchFragmentListAdapter.BranchViewHolder> {

    private List<BranchItemBean> mlists;
    private Context mContext;


    public BranchFragmentListAdapter(List<BranchItemBean> mlists, Context mContext) {
        this.mlists = mlists;
        this.mContext = mContext;
    }

    @Override
    public BranchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BranchViewHolder holder = new BranchViewHolder(View.inflate(mContext, R.layout.item_branch, null));
        return holder;
    }

    @Override
    public void onBindViewHolder(final BranchViewHolder holder, final int position) {
        holder.mBranchName.setText(new BranchItemBean().getBranchName());

        if (mOnItemListener != null && holder.mBranchPhoto != null) {
            holder.mBranchPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemListener.onItemClick(holder.mBranchPhoto, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mlists.size();
    }

    private OnItemListener mOnItemListener;

    public void setOnItemClickListener(OnItemListener onItemClickListener) {
        mOnItemListener = onItemClickListener;
    }

    class BranchViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout mBranchPhoto;
        private ImageView mBranchBg;
        private TextView mBranchName;
        private TextView mTitle;
        private TextView mName;

        public BranchViewHolder(View itemView) {
            super(itemView);
            mBranchPhoto = itemView.findViewById(R.id.item_branch_photo);
            mBranchBg = itemView.findViewById(R.id.item_branch_bg);
            mBranchName = itemView.findViewById(R.id.item_branch_name);
            mTitle = itemView.findViewById(R.id.item_branch_title);
            mName = itemView.findViewById(R.id.item_branch_num);
        }
    }
}
