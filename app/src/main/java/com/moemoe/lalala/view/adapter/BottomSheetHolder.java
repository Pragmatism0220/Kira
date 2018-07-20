package com.moemoe.lalala.view.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.moemoe.lalala.R;
import com.moemoe.lalala.model.entity.upDateEntity;
import com.moemoe.lalala.view.widget.adapter.ClickableViewHolder;

/**
 * Created by Administrator on 2018/7/17.
 */

public class BottomSheetHolder extends ClickableViewHolder {

    private ImageView mIamge;
    private TextView mName;
    private TextView mLevel;
    private TextView mContext;
    private ImageView mLike;
    private TextView mFloor;
    private TextView mTime;


    public BottomSheetHolder(View itemView) {
        super(itemView);
        mIamge = itemView.findViewById(R.id.item_image);
        mName = itemView.findViewById(R.id.item_name);
        mLevel = itemView.findViewById(R.id.item_level);
        mContext = itemView.findViewById(R.id.item_content);
        mLike = itemView.findViewById(R.id.item_like);
        mFloor = itemView.findViewById(R.id.item_floor_num);
        mTime = itemView.findViewById(R.id.item_time);
    }


    public void create(final upDateEntity entity) {

    }
}
