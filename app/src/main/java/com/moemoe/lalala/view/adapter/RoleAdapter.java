package com.moemoe.lalala.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.moemoe.lalala.R;

import com.moemoe.lalala.event.*;
import com.moemoe.lalala.model.api.ApiService;
import com.moemoe.lalala.model.entity.RoleInfoEntity;
import com.moemoe.lalala.service.TestRole;

import java.util.List;

/**
 * Created by Administrator on 2018/5/8.
 */

public class RoleAdapter extends RecyclerView.Adapter<RoleAdapter.RoleViewHolder> {


    private List<RoleInfoEntity> entities;
    private Context mContext;

    public RoleAdapter(List<RoleInfoEntity> roles, Context mContext) {
        this.entities = roles;
        this.mContext = mContext;
    }

    @Override
    public RoleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RoleViewHolder holder = new RoleViewHolder(View.inflate(mContext, R.layout.item_choose_role, null));
        return holder;
    }

    @Override
    public void onBindViewHolder(final RoleViewHolder holder, final int position) {
        RoleInfoEntity data = entities.get(position);
        if (data != null) {
            if ("official".equals(data.getRoleType())) {
                holder.mItem.setBackgroundResource(R.drawable.bg_home_roles_choice_back_official);
                holder.mTitle.setImageResource(R.drawable.ic_home_roleschoice_from_official);
                holder.mName.setText(data.getName());
                Glide.with(mContext).load(ApiService.URL_QINIU + data.getHeadIcon()).into(holder.mImage);
                if (!data.isUserHadRole()) {
                    //false未拥有
                    holder.mItem.setAlpha(0.5f);
                    holder.mImage.setAlpha(0.5f);
                }
            } else if ("linkage".equals(data.getRoleType())) {
                holder.mImage.setBackgroundResource(R.drawable.bg_home_roles_choice_back_linkage);
                holder.mTitle.setImageResource(R.drawable.ic_home_roleschoice_from_linkage);
                holder.mName.setText(data.getName());
                Glide.with(mContext).load(ApiService.URL_QINIU + data.getHeadIcon()).into(holder.mImage);
                if (!data.isUserHadRole()) {
                    //false未拥有
                    holder.mItem.setAlpha(0.5f);
                    holder.mImage.setAlpha(0.5f);
                }
            } else if ("cameo".equals(data.getRoleType())) {
                holder.mImage.setBackgroundResource(R.drawable.bg_home_roles_choice_back_guest);
                holder.mTitle.setImageResource(R.drawable.ic_home_roleschoice_from_guest);
                holder.mName.setText(data.getName());
                Glide.with(mContext).load(ApiService.URL_QINIU + data.getHeadIcon()).into(holder.mImage);
                if (!data.isUserHadRole()) {
                    //false未拥有
                    holder.mItem.setAlpha(0.5f);
                    holder.mImage.setAlpha(0.5f);
                }
            }
        }

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
        return entities.size();
    }

    /**
     * 是声明点击事件接口
     */
    private OnItemListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    class RoleViewHolder extends RecyclerView.ViewHolder {


        private LinearLayout mItem;//状态背景
        private ImageView mImage;//人物头像
        private TextView mName;//人物名称
        private ImageView mTitle;//联动  官方
        private ImageView mZhai;//宅

        private RoleViewHolder(View view) {
            super(view);
            mItem = view.findViewById(R.id.item_main);
            mImage = view.findViewById(R.id.item_choose_role_image);
            mName = view.findViewById(R.id.item_choose_role_name);
            mTitle = view.findViewById(R.id.item_choose_role_title);
            mZhai = view.findViewById(R.id.item_choose_role_zhai);
        }
    }
}
