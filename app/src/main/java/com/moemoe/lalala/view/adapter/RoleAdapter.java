package com.moemoe.lalala.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.moemoe.lalala.R;

import com.moemoe.lalala.event.*;
import com.moemoe.lalala.service.TestRole;

import java.util.List;

/**
 * Created by Administrator on 2018/5/8.
 */

public class RoleAdapter extends RecyclerView.Adapter<RoleAdapter.RoleViewHolder> {

    private List<TestRole> roles;
    private Context mContext;

    public RoleAdapter(List<TestRole> roles, Context mContext) {
        this.roles = roles;
        this.mContext = mContext;
    }

    @Override
    public RoleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RoleViewHolder holder = new RoleViewHolder(View.inflate(mContext, R.layout.item_choose_role, null));
        return holder;
    }

    @Override
    public void onBindViewHolder(final RoleViewHolder holder, final int position) {
//        holder.mName.setText(roles.get(position).getmName());

        if (mOnItemClickListener != null) {
            holder.mItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(holder.mItem, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return roles.size();
    }

    /**
     * 是声明点击事件接口
     */
    private OnItemListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    class RoleViewHolder extends RecyclerView.ViewHolder {

        ImageView mImage;
        TextView mName;
        LinearLayout mItem;

        RoleViewHolder(View view) {
            super(view);
            mImage = view.findViewById(R.id.item_choose_role_image);
            mName = view.findViewById(R.id.item_choose_role_name);
            mItem = view.findViewById(R.id.item_main);
        }
    }
}
