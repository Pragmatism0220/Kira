package com.moemoe.lalala.view.adapter;

import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.moemoe.lalala.R;
import com.moemoe.lalala.event.FavoriteCommentEvent;
import com.moemoe.lalala.model.api.ApiService;
import com.moemoe.lalala.model.entity.BadgeEntity;
import com.moemoe.lalala.model.entity.CommentV2Entity;
import com.moemoe.lalala.model.entity.CommentV2SecEntity;
import com.moemoe.lalala.model.entity.Image;
import com.moemoe.lalala.model.entity.UserTopEntity;
import com.moemoe.lalala.model.entity.tag.UserUrlSpan;
import com.moemoe.lalala.utils.BitmapUtils;
import com.moemoe.lalala.utils.DensityUtil;
import com.moemoe.lalala.utils.FileUtil;
import com.moemoe.lalala.utils.LevelSpan;
import com.moemoe.lalala.utils.NoDoubleClickListener;
import com.moemoe.lalala.utils.PreferenceUtils;
import com.moemoe.lalala.utils.StringUtils;
import com.moemoe.lalala.utils.ViewUtils;
import com.moemoe.lalala.utils.tag.TagControl;
import com.moemoe.lalala.view.activity.CommentSecListActivity;
import com.moemoe.lalala.view.activity.DynamicActivity;
import com.moemoe.lalala.view.activity.ImageBigSelectActivity;
import com.moemoe.lalala.view.widget.adapter.ClickableViewHolder;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by yi on 2017/7/21.
 */

public class DynamicListHolder extends ClickableViewHolder {

    public DynamicListHolder(View itemView) {
        super(itemView);

    }

    public void createItem(final CommentV2Entity entity, final int position, final String parentId, boolean showFavorite) {
        int size = (int) itemView.getResources().getDimension(R.dimen.x72);
        Glide.with(itemView.getContext())
                .load(StringUtils.getUrl(itemView.getContext(), entity.getCreateUser().getHeadPath(), size, size, false, true))
                .error(R.drawable.bg_default_circle)
                .placeholder(R.drawable.bg_default_circle)
                .bitmapTransform(new CropCircleTransformation(itemView.getContext()))
                .into((ImageView) $(R.id.iv_avatar));
        $(R.id.iv_avatar).setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                ViewUtils.toPersonal(context, entity.getCreateUser().getUserId());
            }
        });
        setText(R.id.tv_name, entity.getCreateUser().getUserName());
        LevelSpan levelSpan = new LevelSpan(ContextCompat.getColor(itemView.getContext(), R.color.white), itemView.getContext().getResources().getDimension(R.dimen.x12));
        final String content = "LV" + entity.getCreateUser().getLevel();
        String colorStr = "LV";
        SpannableStringBuilder style = new SpannableStringBuilder(content);
        style.setSpan(levelSpan, content.indexOf(colorStr), content.indexOf(colorStr) + colorStr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        setText(R.id.tv_level, style);
        float radius2 = itemView.getContext().getResources().getDimension(R.dimen.y4);
        float[] outerR2 = new float[]{radius2, radius2, radius2, radius2, radius2, radius2, radius2, radius2};
        RoundRectShape roundRectShape2 = new RoundRectShape(outerR2, null, null);
        ShapeDrawable shapeDrawable2 = new ShapeDrawable();
        shapeDrawable2.setShape(roundRectShape2);
        shapeDrawable2.getPaint().setStyle(Paint.Style.FILL);
        shapeDrawable2.getPaint().setColor(StringUtils.readColorStr(entity.getCreateUser().getLevelColor(), ContextCompat.getColor(itemView.getContext(), R.color.main_cyan)));
        $(R.id.tv_level).setBackgroundDrawable(shapeDrawable2);
        if (showFavorite) {
            setVisible(R.id.tv_favorite, true);
            $(R.id.tv_favorite).setSelected(entity.isLike());
            setText(R.id.tv_favorite, entity.getLikes() + "");
            $(R.id.tv_favorite).setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View v) {
                    EventBus.getDefault().post(new FavoriteCommentEvent(entity.isLike(), entity.getCommentId(), position));
                }
            });
        } else {
            setVisible(R.id.tv_favorite, false);
        }
        setText(R.id.tv_comment, TagControl.getInstance().paresToSpann(itemView.getContext(), entity.getContent()));
        ((TextView) $(R.id.tv_comment)).setMovementMethod(LinkMovementMethod.getInstance());
        if (entity.getImages().size() > 0) {
            setVisible(R.id.ll_comment_img, true);
            ((LinearLayout) $(R.id.ll_comment_img)).removeAllViews();
            for (int i = 0; i < entity.getImages().size(); i++) {
                final int pos = i;
                Image image = entity.getImages().get(i);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.topMargin = (int) context.getResources().getDimension(R.dimen.y10);
                if (FileUtil.isGif(image.getPath())) {
                    ImageView imageView = new ImageView(context);
                    setGif(image, imageView, params);
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, ImageBigSelectActivity.class);
                            intent.putExtra(ImageBigSelectActivity.EXTRA_KEY_FILEBEAN, entity.getImages());
                            intent.putExtra(ImageBigSelectActivity.EXTRAS_KEY_FIRST_PHTOT_INDEX,
                                    pos);
                            context.startActivity(intent);
                        }
                    });
                    ((LinearLayout) $(R.id.ll_comment_img)).addView(imageView, ((LinearLayout) $(R.id.ll_comment_img)).getChildCount(), params);
                } else {
                    ImageView imageView = new ImageView(context);
                    setImage(image, imageView, params);
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, ImageBigSelectActivity.class);
                            intent.putExtra(ImageBigSelectActivity.EXTRA_KEY_FILEBEAN, entity.getImages());
                            intent.putExtra(ImageBigSelectActivity.EXTRAS_KEY_FIRST_PHTOT_INDEX,
                                    pos);
                            context.startActivity(intent);
                        }
                    });
                    ((LinearLayout) $(R.id.ll_comment_img)).addView(imageView, ((LinearLayout) $(R.id.ll_comment_img)).getChildCount(), params);
                }
            }
        } else {
            setVisible(R.id.ll_comment_img, false);
        }

        if (entity.getIdx() == 0) {
            setText(R.id.tv_comment_time, StringUtils.timeFormat(entity.getCreateTime()));

        } else {
            setText(R.id.tv_comment_time, entity.getIdx() + "楼 · " + StringUtils.timeFormat(entity.getCreateTime()));
        }

        //sec comment
        if (entity.getHotComments() != null && entity.getHotComments().size() > 0) {
            setVisible(R.id.ll_comment_root, true);
            ((LinearLayout) $(R.id.ll_comment_root)).removeAllViews();
            for (final CommentV2SecEntity secEntity : entity.getHotComments()) {
                TextView tv = new TextView(itemView.getContext());
                tv.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.gray_444444));
                tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, itemView.getResources().getDimension(R.dimen.x30));

                String retweetContent = "@" + secEntity.getCreateUser().getUserName();
                if (!TextUtils.isEmpty(secEntity.getCommentTo())) {
                    retweetContent += " 回复 " + "@" + secEntity.getCommentToName();
                }
                retweetContent += ": " + secEntity.getContent();
                String retweetColorStr = "@" + secEntity.getCreateUser().getUserName();
                SpannableStringBuilder style1 = new SpannableStringBuilder(retweetContent);
                UserUrlSpan span = new UserUrlSpan(itemView.getContext(), secEntity.getCreateUser().getUserId(), null);
                style1.setSpan(span, retweetContent.indexOf(retweetColorStr), retweetContent.indexOf(retweetColorStr) + retweetColorStr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                if (!TextUtils.isEmpty(secEntity.getCommentTo())) {
                    String retweetColorStr1 = "@" + secEntity.getCommentToName();
                    UserUrlSpan span1 = new UserUrlSpan(itemView.getContext(), secEntity.getCommentTo(), null);
                    style1.setSpan(span1, retweetContent.indexOf(retweetColorStr1), retweetContent.indexOf(retweetColorStr1) + retweetColorStr1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                lp.topMargin = (int) context.getResources().getDimension(R.dimen.y4);
                tv.setLayoutParams(lp);
                tv.setText(style1);
                tv.setMovementMethod(LinkMovementMethod.getInstance());
//                tv.setOnClickListener(new NoDoubleClickListener() {
//                    @Override
//                    public void onNoDoubleClick(View v) {
//                        CreateCommentActivity.startActivityForResult(context,entity.getCommentId(),true,secEntity.getCreateUser().getUserId(),false);
//                    }
//                });
                ((LinearLayout) $(R.id.ll_comment_root)).addView(tv);
            }
            if (entity.getComments() > entity.getHotComments().size()) {
                TextView tv = new TextView(itemView.getContext());
                tv.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.main_cyan));
                tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, itemView.getResources().getDimension(R.dimen.x30));
                tv.setText("全部" + StringUtils.getNumberInLengthLimit(entity.getComments(), 3) + "条回复");
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                lp.topMargin = (int) context.getResources().getDimension(R.dimen.y4);
                tv.setLayoutParams(lp);
//                tv.setOnClickListener(new NoDoubleClickListener() {
//                    @Override
//                    public void onNoDoubleClick(View v) {
//                        CommentSecListActivity.startActivityForResult(context,entity,parentId);
//                    }
//                });
                ((LinearLayout) $(R.id.ll_comment_root)).addView(tv);
            }
            $(R.id.ll_comment_root).setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View v) {
                    CommentSecListActivity.startActivity(context, entity, parentId);
                }
            });
        } else {
            setVisible(R.id.ll_comment_root, false);
        }


        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ((DynamicActivity) context).setShowMenu(position);
                return true;
            }
        });
//        itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ((DynamicActivity) context).showCommentMenu(entity, position);
//            }
//        });
    }

    private void setGif(Image image, ImageView gifImageView, LinearLayout.LayoutParams params) {
        final int[] wh = BitmapUtils.getDocIconSizeFromW(image.getW() * 2, image.getH() * 2, (int) (DensityUtil.getScreenWidth(context) - context.getResources().getDimension(R.dimen.x168)));
        params.width = wh[0];
        params.height = wh[1];
        Glide.with(context)
                .load(ApiService.URL_QINIU + image.getPath())
                .asGif()
                .override(wh[0], wh[1])
                .placeholder(R.drawable.shape_gray_e8e8e8_background)
                .error(R.drawable.shape_gray_e8e8e8_background)
                .into(gifImageView);
    }

    private void setImage(Image image, final ImageView imageView, LinearLayout.LayoutParams params) {
        final int[] wh = BitmapUtils.getDocIconSizeFromW(image.getW() * 2, image.getH() * 2, (int) (DensityUtil.getScreenWidth(context) - context.getResources().getDimension(R.dimen.x168)));
        params.width = wh[0];
        params.height = wh[1];
        Glide.with(context)
                .load(StringUtils.getUrl(context, ApiService.URL_QINIU + image.getPath(), wh[0], wh[1], true, true))
                .override(wh[0], wh[1])
                .placeholder(R.drawable.shape_gray_e8e8e8_background)
                .error(R.drawable.shape_gray_e8e8e8_background)
                .into(imageView);
    }

    public void createItem(final UserTopEntity entity, final int position) {
        int size = (int) itemView.getContext().getResources().getDimension(R.dimen.x80);
        Glide.with(itemView.getContext())
                .load(StringUtils.getUrl(itemView.getContext(), entity.getHeadPath(), size, size, false, true))
                .error(R.drawable.bg_default_circle)
                .placeholder(R.drawable.bg_default_circle)
                .bitmapTransform(new CropCircleTransformation(itemView.getContext()))
                .into((ImageView) $(R.id.iv_avatar));
        setText(R.id.tv_name, entity.getUserName());
        setImageResource(R.id.iv_sex, entity.getSex().equalsIgnoreCase("M") ? R.drawable.ic_user_girl : R.drawable.ic_user_boy);
        LevelSpan levelSpan = new LevelSpan(ContextCompat.getColor(itemView.getContext(), R.color.white), itemView.getContext().getResources().getDimension(R.dimen.x12));
        final String content = "LV" + entity.getLevel();
        String colorStr = "LV";
        SpannableStringBuilder style = new SpannableStringBuilder(content);
        style.setSpan(levelSpan, content.indexOf(colorStr), content.indexOf(colorStr) + colorStr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        setText(R.id.tv_level, style);

        $(R.id.fl_huizhang_1).setVisibility(View.VISIBLE);

        float radius2 = itemView.getContext().getResources().getDimension(R.dimen.y4);
        float[] outerR2 = new float[]{radius2, radius2, radius2, radius2, radius2, radius2, radius2, radius2};
        RoundRectShape roundRectShape2 = new RoundRectShape(outerR2, null, null);
        ShapeDrawable shapeDrawable2 = new ShapeDrawable();
        shapeDrawable2.setShape(roundRectShape2);
        shapeDrawable2.getPaint().setStyle(Paint.Style.FILL);
        shapeDrawable2.getPaint().setColor(StringUtils.readColorStr(entity.getLevelColor(), ContextCompat.getColor(itemView.getContext(), R.color.main_cyan)));
        $(R.id.tv_level).setBackgroundDrawable(shapeDrawable2);

        View[] huizhang = {$(R.id.fl_huizhang_1)};
        TextView[] huizhangT = {$(R.id.tv_huizhang_1)};
        BadgeEntity badge = entity.getBadge();
        if (badge != null) {
            ArrayList<BadgeEntity> badgeEntities = new ArrayList<>();
            badgeEntities.add(entity.getBadge());
            ViewUtils.badge(itemView.getContext(), huizhang, huizhangT, badgeEntities);
        } else {
            huizhang[0].setVisibility(View.GONE);
            huizhangT[0].setVisibility(View.GONE);
        }


        setText(R.id.tv_contxt, entity.getSignature());
        $(R.id.iv_avatar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewUtils.toPersonal(context, entity.getUserId());
            }
        });
        ((TextView) $(R.id.tv_time)).setVisibility(View.VISIBLE);
        ((TextView) $(R.id.tv_time)).setText(StringUtils.timeFormat(entity.getCreateTime()));
        TextView mTvFollow = (TextView) $(R.id.tv_follow);
        int focus = entity.getFocus();
        if (entity.getUserId().equals(PreferenceUtils.getUUid())) {
            mTvFollow.setVisibility(View.GONE);
        } else {
            mTvFollow.setVisibility(View.VISIBLE);
        }
        if (focus == -1) {
            mTvFollow.setBackgroundResource(R.drawable.btn_rect_corner_cyan_follow_y30);
            mTvFollow.setText("互相关注");
            mTvFollow.setTextColor(ContextCompat.getColor(context, R.color.white));
            mTvFollow.setSelected(true);
        } else if (focus == 1) {
            mTvFollow.setText("已关注");
            mTvFollow.setTextColor(ContextCompat.getColor(context, R.color.white));
            mTvFollow.setBackgroundResource(R.drawable.btn_rect_corner_cyan_y30);
            mTvFollow.setSelected(true);
        } else {
            mTvFollow.setText("未关注");
            mTvFollow.setTextColor(ContextCompat.getColor(context, R.color.main_cyan));
            mTvFollow.setBackgroundResource(R.drawable.btn_rect_corner_cyan_y30);
            mTvFollow.setSelected(false);
        }

    }
}
