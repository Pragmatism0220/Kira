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
import com.moemoe.lalala.view.base.TestPrincipalInfo;

import java.util.List;

/**
 * Created by zhangyan on 2018/5/15.
 * 主线剧情卡片式  章节选择
 */

public class PrincipalLineAdapter extends RecyclerView.Adapter<PrincipalLineAdapter.PrincipalLineViewHolder> {

    private List<TestPrincipalInfo> mlists;
    private Context mContext;

    public PrincipalLineAdapter(List<TestPrincipalInfo> mlists, Context mContext) {
        this.mlists = mlists;
        this.mContext = mContext;
    }

    @Override
    public PrincipalLineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        PrincipalLineViewHolder holder = new PrincipalLineViewHolder(View.inflate(mContext, R.layout.item_principal_line, null));
        return holder;
    }

    @Override
    public void onBindViewHolder(final PrincipalLineViewHolder holder, final int position) {
        if (mOnItemListener != null) {
            holder.mPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemListener.onItemClick(holder.mPhoto, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mlists.size();
    }


    /**
     * 点击事件
     */
    private OnItemListener mOnItemListener;

    public void setOnItemClickListener(OnItemListener onItemClickListener) {
        mOnItemListener = onItemClickListener;
    }

    class PrincipalLineViewHolder extends RecyclerView.ViewHolder {

        private ImageView mPhoto;//封面素材
        private ImageView mBottomImage;//封面底部角标
        private TextView mBottomTitle;//封面底部标题

        private TextView mName;//名字
        private TextView mNum;//参与人数


        public PrincipalLineViewHolder(View itemView) {
            super(itemView);
            mPhoto = itemView.findViewById(R.id.item_photo);
            mBottomImage = itemView.findViewById(R.id.item_bottom_image);
            mBottomTitle = itemView.findViewById(R.id.item_bottom_title);

            mName = itemView.findViewById(R.id.item_bottom_name);
            mNum = itemView.findViewById(R.id.item_bottom_num);

        }
    }
}

