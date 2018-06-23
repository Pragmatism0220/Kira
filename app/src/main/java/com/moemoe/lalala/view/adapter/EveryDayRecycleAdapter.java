package com.moemoe.lalala.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.moemoe.lalala.R;
import com.moemoe.lalala.model.entity.OnItemListener;
import com.moemoe.lalala.view.base.EveryDayBean;

import java.util.List;

/**
 * Created by Administrator on 2018/5/16.
 */

public class EveryDayRecycleAdapter extends RecyclerView.Adapter<EveryDayRecycleAdapter.EveryDayViewHolder> {

    private Context mContext;
    private List<EveryDayBean> mlists;

    public EveryDayRecycleAdapter(Context mContext, List<EveryDayBean> mlists) {
        this.mContext = mContext;
        this.mlists = mlists;
    }

    @Override
    public EveryDayViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        EveryDayViewHolder holder = new EveryDayViewHolder(View.inflate(mContext, R.layout.item_principal_line, null));
        return holder;
    }

    @Override
    public void onBindViewHolder(final EveryDayViewHolder holder, final int position) {
//        holder.mChapterTitle.setText(new EveryDayBean().getTitleName());
//        holder.mPartNum.setText("参与人次" + new EveryDayBean().getNumber());

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


    private OnItemListener mOnItemListener;

    public void setOnItemClickListener(OnItemListener onItemClickListener) {
        mOnItemListener = onItemClickListener;
    }

    class EveryDayViewHolder extends RecyclerView.ViewHolder {


        private ImageView mPhoto;//封面素材
        private ImageView mBottomImage;//封面底部角标
        private TextView mBottomTitle;//封面底部标题

        private TextView mName;//名字
        private TextView mNum;//参与人数

        public EveryDayViewHolder(View itemView) {
            super(itemView);
            mPhoto = itemView.findViewById(R.id.item_photo);
            mBottomImage = itemView.findViewById(R.id.item_bottom_image);
            mBottomTitle = itemView.findViewById(R.id.item_bottom_title);

            mName = itemView.findViewById(R.id.item_bottom_name);
            mNum = itemView.findViewById(R.id.item_bottom_num);

        }
    }
}
