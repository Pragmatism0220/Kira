package com.moemoe.lalala.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.moemoe.lalala.R;
import com.moemoe.lalala.model.entity.BannerEntity;
import com.youth.banner.loader.ImageLoader;

/**
 * Created by yi on 2017/7/18.
 */

public class BannerImageLoader extends ImageLoader {

    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        BannerEntity bean = (BannerEntity) path;
        Glide.with(context)
                .load(StringUtils.getUrl(context, bean.getBg().getPath(), DensityUtil.getScreenWidth(context),context.getResources().getDimensionPixelSize(R.dimen.y300), false, true))
                .override( DensityUtil.getScreenWidth(context),context.getResources().getDimensionPixelSize(R.dimen.y300))
                .placeholder(R.drawable.shape_gray_e8e8e8_background)
                .error(R.drawable.shape_gray_e8e8e8_background)
                .into(imageView);
    }
}
