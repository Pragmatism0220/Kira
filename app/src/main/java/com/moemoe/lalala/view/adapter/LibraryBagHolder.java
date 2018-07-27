package com.moemoe.lalala.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.moemoe.lalala.R;
import com.moemoe.lalala.model.entity.FolderType;
import com.moemoe.lalala.model.entity.ShowFolderEntity;
import com.moemoe.lalala.utils.DensityUtil;
import com.moemoe.lalala.utils.NoDoubleClickListener;
import com.moemoe.lalala.utils.StringUtils;
import com.moemoe.lalala.utils.ViewUtils;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.CropTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by Hygge on 2018/7/19.
 */

public class LibraryBagHolder extends ClubChildHolder {

    private int[] mBackGround = {R.drawable.shape_rect_label_cyan,
            R.drawable.shape_rect_label_yellow,
            R.drawable.shape_rect_label_orange,
            R.drawable.shape_rect_label_pink,
            R.drawable.shape_rect_border_green_y4,
            R.drawable.shape_rect_label_purple,
            R.drawable.shape_rect_label_tab_blue};

    public LibraryBagHolder(View itemView) {
        super(itemView);
    }

    public void createItem(final ShowFolderEntity entity, int position) {
        int size = (int) context.getResources().getDimension(R.dimen.y32);
        setVisible(R.id.iv_user_head, true);
        setVisible(R.id.tv_user_name, true);
        setText(R.id.tv_user_name, entity.getCreateUserName());
        $(R.id.ll_user_root).setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                ViewUtils.toPersonal(context, entity.getCreateUser());
            }
        });
        Glide.with(context)
                .load(StringUtils.getUrl(context, entity.getUserIcon(), size, size, false, true))
                .error(R.drawable.bg_default_circle)
                .placeholder(R.drawable.bg_default_circle)
                .bitmapTransform(new CropCircleTransformation(context))
                .into((ImageView) $(R.id.iv_user_head));

        setText(R.id.tv_bag_title, entity.getFolderName());
        setVisible(R.id.tv_bag_title, false);
        setVisible(R.id.tv_title, true);
        ((TextView) $(R.id.tv_title)).setMaxLines(1);
        if (entity.getTexts().size() == 2) {
            setVisible(R.id.tv_tag_1, true);
            setVisible(R.id.tv_tag_2, true);
            int index = StringUtils.getHashOfString(entity.getTexts().get(0), mBackGround.length);
            setText(R.id.tv_tag_1, entity.getTexts().get(0));
            $(R.id.tv_tag_1).setBackgroundResource(mBackGround[index]);
            int index2 = StringUtils.getHashOfString(entity.getTexts().get(1), mBackGround.length);
            setText(R.id.tv_tag_2, entity.getTexts().get(1));
            $(R.id.tv_tag_2).setBackgroundResource(mBackGround[index2]);
        } else if (entity.getTexts().size() == 1) {
            setVisible(R.id.tv_tag_1, true);
            setVisible(R.id.tv_tag_2, false);
            int index = StringUtils.getHashOfString(entity.getTexts().get(0), mBackGround.length);
            setText(R.id.tv_tag_1, entity.getTexts().get(0));
            $(R.id.tv_tag_1).setBackgroundResource(mBackGround[index]);
        } else {
            setVisible(R.id.tv_tag_1, false);
            setVisible(R.id.tv_tag_2, false);
        }
        setVisible(R.id.tv_mark, true);
        if (entity.getType().equals(FolderType.ZH.toString())) {
            setText(R.id.tv_mark, "综合");
            $(R.id.tv_mark).setBackgroundResource(R.drawable.shape_rect_zonghe);
        } else if (entity.getType().equals(FolderType.TJ.toString())) {
            setText(R.id.tv_title, entity.getFolderName());
            setText(R.id.tv_bag_coin, "");
            setText(R.id.tv_mark, "图集");
            $(R.id.tv_mark).setBackgroundResource(R.drawable.shape_rect_tuji);
        } else if (entity.getType().equals(FolderType.MH.toString())) {
            setText(R.id.tv_title, entity.getParentFolderName());
            setText(R.id.tv_bag_coin, entity.getFolderName());
            setText(R.id.tv_mark, "漫画");
            $(R.id.tv_mark).setBackgroundResource(R.drawable.shape_rect_manhua);
        } else if (entity.getType().equals(FolderType.XS.toString())) {
            setText(R.id.tv_title, entity.getParentFolderName());
            setText(R.id.tv_bag_coin, entity.getFolderName());
            setText(R.id.tv_mark, "小说");
            $(R.id.tv_mark).setBackgroundResource(R.drawable.shape_rect_xiaoshuo);
        } else if (entity.getType().equals(FolderType.SP.toString())) {
            setText(R.id.tv_mark, "视频集");
            $(R.id.tv_mark).setBackgroundResource(R.drawable.shape_rect_shipin);
        } else if (entity.getType().equals(FolderType.YY.toString())) {
            setText(R.id.tv_mark, "音乐集");
            $(R.id.tv_mark).setBackgroundResource(R.drawable.shape_rect_yinyue);
        } else {
            setVisible(R.id.tv_mark, false);
        }

        int width = (DensityUtil.getScreenWidth(itemView.getContext()) - (int) context.getResources().getDimension(R.dimen.x84)) / 3;
        int height = (int) context.getResources().getDimension(R.dimen.y290);

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(width, height);
        $(R.id.iv_cover).setLayoutParams(lp);

        RecyclerView.LayoutParams lp1 = new RecyclerView.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        $(R.id.root).setLayoutParams(lp1);

        Glide.with(itemView.getContext())
                .load(StringUtils.getUrl(itemView.getContext(), entity.getCover(), width, height, false, true))
                .placeholder(R.drawable.shape_gray_e8e8e8_background)
                .error(R.drawable.shape_gray_e8e8e8_background)
                .bitmapTransform(new CropTransformation(itemView.getContext(), width, height), new RoundedCornersTransformation(itemView.getContext(), (int) itemView.getContext().getResources().getDimension(R.dimen.y8), 0))
                .into((ImageView) $(R.id.iv_cover));
    }


    public void createItem(final ShowFolderEntity entity, int position, String type) {
        int size = (int) context.getResources().getDimension(R.dimen.y32);
        setVisible(R.id.iv_user_avatar, true);
        setVisible(R.id.tv_user_name, true);
        setText(R.id.tv_user_name, entity.getCreateUserName());
//        $(R.id.ll_user_root).setOnClickListener(new NoDoubleClickListener() {
//            @Override
//            public void onNoDoubleClick(View v) {
////                ViewUtils.toPersonal(context, entity.getCreateUser());
//            }
//        });
        Glide.with(context)
                .load(StringUtils.getUrl(context, entity.getUserIcon(), size, size, false, true))
                .error(R.drawable.bg_default_circle)
                .placeholder(R.drawable.bg_default_circle)
                .bitmapTransform(new CropCircleTransformation(context))
                .into((ImageView) $(R.id.iv_user_avatar));


        setVisible(R.id.tv_title, true);

        setText(R.id.tv_title, entity.getParentFolderName());

        if (entity.getTexts().size() == 2) {
            setVisible(R.id.tv_tag_1, true);
            setVisible(R.id.tv_tag_2, true);
            int index = StringUtils.getHashOfString(entity.getTexts().get(0), mBackGround.length);
            setText(R.id.tv_tag_1, entity.getTexts().get(0));
            $(R.id.tv_tag_1).setBackgroundResource(mBackGround[index]);
            int index2 = StringUtils.getHashOfString(entity.getTexts().get(1), mBackGround.length);
            setText(R.id.tv_tag_2, entity.getTexts().get(1));
            $(R.id.tv_tag_2).setBackgroundResource(mBackGround[index2]);
        } else if (entity.getTexts().size() == 1) {
            setVisible(R.id.tv_tag_1, true);
            setVisible(R.id.tv_tag_2, false);
            int index = StringUtils.getHashOfString(entity.getTexts().get(0), mBackGround.length);
            setText(R.id.tv_tag_1, entity.getTexts().get(0));
            $(R.id.tv_tag_1).setBackgroundResource(mBackGround[index]);
        } else {
            setVisible(R.id.tv_tag_1, false);
            setVisible(R.id.tv_tag_2, false);
        }
        setVisible(R.id.tv_mark, true);
        if (entity.getType().equals(FolderType.ZH.toString())) {
            setText(R.id.tv_mark, "综合");
            $(R.id.tv_mark).setBackgroundResource(R.drawable.shape_rect_zonghe);
        } else if (entity.getType().equals(FolderType.TJ.toString())) {
            setVisible(R.id.tv_last_time_frist, false);
            setVisible(R.id.tv_last_time_delits, true);
            setText(R.id.tv_last_time_delits, "继续观看");
            setText(R.id.tv_mark, "图集");
            $(R.id.tv_mark).setBackgroundResource(R.drawable.shape_rect_tuji);
        } else if (entity.getType().equals(FolderType.MH.toString())) {
            setVisible(R.id.tv_last_time_frist, true);
            setVisible(R.id.tv_last_time_delits, true);
            setText(R.id.tv_last_time_delits, entity.getFolderName());
            setText(R.id.tv_mark, "漫画");
            $(R.id.tv_mark).setBackgroundResource(R.drawable.shape_rect_manhua);
        } else if (entity.getType().equals(FolderType.XS.toString())) {
            setVisible(R.id.tv_last_time_frist, true);
            setVisible(R.id.tv_last_time_delits, true);
            setText(R.id.tv_last_time_delits, entity.getFolderName());
            setText(R.id.tv_mark, "小说");
            $(R.id.tv_mark).setBackgroundResource(R.drawable.shape_rect_xiaoshuo);
        } else if (entity.getType().equals(FolderType.SP.toString())) {
            setText(R.id.tv_mark, "视频集");
            $(R.id.tv_mark).setBackgroundResource(R.drawable.shape_rect_shipin);
        } else if (entity.getType().equals(FolderType.YY.toString())) {
            setText(R.id.tv_mark, "音乐集");
            $(R.id.tv_mark).setBackgroundResource(R.drawable.shape_rect_yinyue);
        } else {
            setVisible(R.id.tv_mark, false);
        }

        int width = (int) context.getResources().getDimension(R.dimen.x120);
        int height = (int) context.getResources().getDimension(R.dimen.y160);

        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(width, height);
        $(R.id.iv_cover).setLayoutParams(lp);


        Glide.with(itemView.getContext())
                .load(StringUtils.getUrl(itemView.getContext(), entity.getCover(), width, height, false, true))
                .placeholder(R.drawable.shape_gray_e8e8e8_background)
                .error(R.drawable.shape_gray_e8e8e8_background)
                .bitmapTransform(new CropTransformation(itemView.getContext(), width, height), new RoundedCornersTransformation(itemView.getContext(), (int) itemView.getContext().getResources().getDimension(R.dimen.y8), 0))
                .into((ImageView) $(R.id.iv_cover));

        setVisible(R.id.tv_last_time, true);
        setText(R.id.tv_last_time, "上次观看：" + StringUtils.timeFormat(entity.getLastReadTime()));

    }
}
