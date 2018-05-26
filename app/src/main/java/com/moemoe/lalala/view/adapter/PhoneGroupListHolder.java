package com.moemoe.lalala.view.adapter;

import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.moemoe.lalala.R;
import com.moemoe.lalala.model.entity.GroupEntity;
import com.moemoe.lalala.utils.StringUtils;
import com.moemoe.lalala.view.widget.adapter.ClickableViewHolder;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 *
 * Created by yi on 2017/7/21.
 */

public class PhoneGroupListHolder extends ClickableViewHolder {

    public PhoneGroupListHolder(View itemView) {
        super(itemView);
    }

    public void createItem(final GroupEntity entity){
        int size = (int) context.getResources().getDimension(R.dimen.y90);
        int cor = (int) context.getResources().getDimension(R.dimen.y8);
        Glide.with(context)
                .load(StringUtils.getUrl(context,entity.getCover(),size,size,false,true))
                .override(size,size)
                .error(R.drawable.shape_gray_e8e8e8_background)
                .placeholder(R.drawable.shape_gray_e8e8e8_background)
                .bitmapTransform(new RoundedCornersTransformation(context,cor,0))
                .into((ImageView) $(R.id.iv_cover));

        setText(R.id.tv_name,entity.getGroupName());
        setText(R.id.tv_content,entity.getDesc());
        setText(R.id.tv_time,String.valueOf(entity.getUsers()) + " 人");
    }
}
