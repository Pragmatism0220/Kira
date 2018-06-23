package com.moemoe.lalala.view.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.QuickContactBadge;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.moemoe.lalala.R;
import com.moemoe.lalala.model.api.ApiService;
import com.moemoe.lalala.model.entity.ClothingEntity;
import com.moemoe.lalala.utils.StringUtils;
import com.moemoe.lalala.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by Hygge on 2018/5/9.
 */

@SuppressLint("ValidFragment")
public class ClothingFragment extends BaseFragment {

    @BindView(R.id.iv_cloth_cover)
    ImageView mIvClothCover;

    private ClothingEntity mData;
    private Context context;

    @SuppressLint("ValidFragment")
    public ClothingFragment(Context context, ClothingEntity entities) {
        this.context = context;
        this.mData = entities;
    }

    public static ClothingFragment newInstance(Context context, ClothingEntity entities) {
        return new ClothingFragment(context, entities);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_role;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        Glide.with(context)
                .load(ApiService.URL_QINIU + mData.getImage())
                .error(R.drawable.shape_transparent_background)
                .placeholder(R.drawable.shape_transparent_background)
                .into(mIvClothCover);
    }
}
