package com.moemoe.lalala.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.moemoe.lalala.R;
import com.moemoe.lalala.view.base.BranchInfoBean;

import java.util.List;

/**
 * Created by zhangyan on 2018/5/16.
 */

public class BranchInfoAdapter extends RecyclerView.Adapter<BranchInfoAdapter.BranchInfoViewHolder> {

    private List<BranchInfoBean> mlists;
    private Context mContext;

    public BranchInfoAdapter(List<BranchInfoBean> mlists, Context mContext) {
        this.mlists = mlists;
        this.mContext = mContext;
    }

    @Override

    public BranchInfoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BranchInfoViewHolder holder = new BranchInfoViewHolder(View.inflate(mContext, R.layout.item_branch_info, null));
        return holder;
    }

    @Override
    public void onBindViewHolder(BranchInfoViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mlists.size();
    }


    class BranchInfoViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout mItemBranchInfoBg;

        public BranchInfoViewHolder(View itemView) {
            super(itemView);
            mItemBranchInfoBg = itemView.findViewById(R.id.item_branch_info_bg);
        }
    }
}
