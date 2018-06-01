package com.moemoe.lalala.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.moemoe.lalala.R;

import com.moemoe.lalala.event.*;
import com.moemoe.lalala.model.api.ApiService;
import com.moemoe.lalala.model.entity.RoleInfoEntity;

import java.nio.file.Path;
import java.util.List;

/**
 * Created by zhangyan on 2018/5/8.
 */

public class RoleAdapter extends RecyclerView.Adapter<RoleAdapter.RoleViewHolder> {


    private List<RoleInfoEntity> entities;
    private Context mContext;
    private RoleItemClickListener listener;

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
        final RoleInfoEntity data = entities.get(position);
        Log.i("roleActivity", "onBindViewHolder: " + data);
        if (data != null) {
            if ("official".equals(data.getRoleType())) {
                holder.mItem.setBackgroundResource(R.drawable.role_official_btn);
                holder.mTitle.setImageResource(R.drawable.ic_home_roleschoice_from_official);
                holder.mName.setText(data.getName());
                Glide.with(mContext).load(ApiService.URL_QINIU + data.getHeadIcon()).into(holder.mImage);
                if (!data.getIsUserHadRole()) {
                    //false未拥有
                    holder.mItem.setAlpha(0.5f);
                    holder.mImage.setAlpha(0.5f);
                }
                if (data.getIsPutInHouse()) {
                    holder.mZhai.setVisibility(View.VISIBLE);
                } else {
                    holder.mZhai.setVisibility(View.GONE);
                }
            } else if ("linkage".equals(data.getRoleType())) {
                holder.mImage.setBackgroundResource(R.drawable.role_linkge_btn);
                holder.mTitle.setImageResource(R.drawable.ic_home_roleschoice_from_linkage);
                holder.mName.setText(data.getName());
                Glide.with(mContext).load(ApiService.URL_QINIU + data.getHeadIcon()).into(holder.mImage);
                if (!data.getIsUserHadRole()) {
                    //false未拥有
                    holder.mItem.setAlpha(0.5f);
                    holder.mImage.setAlpha(0.5f);
                }
                if (data.getIsPutInHouse()) {
                    holder.mZhai.setVisibility(View.VISIBLE);
                } else {
                    holder.mZhai.setVisibility(View.GONE);
                }
            } else if ("cameo".equals(data.getRoleType())) {
                holder.mImage.setBackgroundResource(R.drawable.role_guest_btn);
                holder.mTitle.setImageResource(R.drawable.ic_home_roleschoice_from_guest);
                holder.mName.setText(data.getName());
                Glide.with(mContext).load(ApiService.URL_QINIU + data.getHeadIcon()).into(holder.mImage);
                if (!data.getIsUserHadRole()) {
                    //false未拥有
                    holder.mItem.setAlpha(0.5f);
                    holder.mImage.setAlpha(0.5f);
                }
                if (data.getIsPutInHouse()) {
                    holder.mZhai.setVisibility(View.VISIBLE);
                } else {
                    holder.mZhai.setVisibility(View.GONE);
                }
            }
        }
        holder.mItem.setSelected(data.isSelected());
        if (listener != null) {
            holder.mItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(holder.mItem, position, holder.getAdapterPosition());
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
    public interface RoleItemClickListener {
        void onClick(View v, int position, int which);
    }

    public void setOnItemClickListener(RoleItemClickListener listener) {
        this.listener = listener;
    }

    class RoleViewHolder extends RecyclerView.ViewHolder {


        private RelativeLayout mItem;//状态背景
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
