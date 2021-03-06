package com.moemoe.lalala.view.adapter;

import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.moemoe.lalala.R;
import com.moemoe.lalala.model.entity.GroupNoticeEntity;
import com.moemoe.lalala.utils.NoDoubleClickListener;
import com.moemoe.lalala.utils.StringUtils;
import com.moemoe.lalala.view.fragment.PhoneMsgListV2Fragment;
import com.moemoe.lalala.view.widget.adapter.ClickableViewHolder;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 *
 * Created by yi on 2017/7/21.
 */

public class PhoneMsgListHolder extends ClickableViewHolder {

    public PhoneMsgListHolder(View itemView) {
        super(itemView);
    }

    public void createItem(final GroupNoticeEntity entity, boolean showDeal, final int position, final PhoneMsgListV2Fragment fragment){
        int size = (int) context.getResources().getDimension(R.dimen.y90);
        int cor = (int) context.getResources().getDimension(R.dimen.y8);
        Glide.with(context)
                .load(StringUtils.getUrl(context,entity.getCover(),size,size,false,true))
                .override(size,size)
                .error(R.drawable.shape_gray_e8e8e8_background)
                .placeholder(R.drawable.shape_gray_e8e8e8_background)
                .bitmapTransform(new RoundedCornersTransformation(context,cor,0))
                .into((ImageView) $(R.id.iv_cover));

        setText(R.id.tv_name,entity.getName());
        setText(R.id.tv_content,entity.getMessage());
        setVisible(R.id.iv_right,entity.isState());
        setText(R.id.tv_extra,entity.getExtra());
        setVisible(R.id.ll_bottom,showDeal);

        if(entity.isState()){
            setVisible(R.id.iv_red_msg,true);
        }else {
            setVisible(R.id.iv_red_msg,false);
        }

        $(R.id.tv_reject).setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                fragment.responseNotice(false,entity.getId(),position);
            }
        });
        $(R.id.tv_accept).setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                fragment.responseNotice(true,entity.getId(),position);
            }
        });
    }
}
