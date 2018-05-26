package com.moemoe.lalala.view.adapter;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.moemoe.lalala.R;
import com.moemoe.lalala.event.RefreshListEvent;
import com.moemoe.lalala.model.entity.BadgeEntity;
import com.moemoe.lalala.model.entity.DocResponse;
import com.moemoe.lalala.model.entity.DynamicContentEntity;
import com.moemoe.lalala.model.entity.FolderType;
import com.moemoe.lalala.model.entity.Image;
import com.moemoe.lalala.model.entity.MessageDynamicEntity;
import com.moemoe.lalala.model.entity.NewDynamicEntity;
import com.moemoe.lalala.model.entity.ProductDyEntity;
import com.moemoe.lalala.model.entity.REPORT;
import com.moemoe.lalala.model.entity.RetweetEntity;
import com.moemoe.lalala.model.entity.ShareArticleEntity;
import com.moemoe.lalala.model.entity.ShareFolderEntity;
import com.moemoe.lalala.model.entity.ShareMovieEntity;
import com.moemoe.lalala.model.entity.ShareMusicEntity;
import com.moemoe.lalala.model.entity.ShowFolderEntity;
import com.moemoe.lalala.model.entity.SimpleUserEntity;
import com.moemoe.lalala.model.entity.UserTopEntity;
import com.moemoe.lalala.utils.BitmapUtils;
import com.moemoe.lalala.utils.DensityUtil;
import com.moemoe.lalala.utils.IntentUtils;
import com.moemoe.lalala.utils.LevelSpan;
import com.moemoe.lalala.utils.NoDoubleClickListener;
import com.moemoe.lalala.utils.PreferenceUtils;
import com.moemoe.lalala.utils.StringUtils;
import com.moemoe.lalala.utils.TagUtils;
import com.moemoe.lalala.utils.ViewUtils;
import com.moemoe.lalala.utils.tag.TagControl;
import com.moemoe.lalala.view.activity.AllSearchActivity;
import com.moemoe.lalala.view.activity.BaseAppCompatActivity;
import com.moemoe.lalala.view.activity.CreateForwardActivity;
import com.moemoe.lalala.view.activity.DynamicActivity;
import com.moemoe.lalala.view.activity.FileMovieActivity;
import com.moemoe.lalala.view.activity.HongBaoListActivity;
import com.moemoe.lalala.view.activity.ImageBigSelectActivity;
import com.moemoe.lalala.view.activity.JuBaoActivity;
import com.moemoe.lalala.view.activity.KiraMusicActivity;
import com.moemoe.lalala.view.activity.KiraVideoActivity;
import com.moemoe.lalala.view.activity.NewDocDetailActivity;
import com.moemoe.lalala.view.activity.NewFileCommonActivity;
import com.moemoe.lalala.view.activity.NewFileManHuaActivity;
import com.moemoe.lalala.view.activity.NewFileXiaoshuoActivity;
import com.moemoe.lalala.view.activity.ShopDetailActivity;
import com.moemoe.lalala.view.widget.adapter.ClickableViewHolder;
import com.moemoe.lalala.view.widget.netamenu.BottomMenuFragment;
import com.moemoe.lalala.view.widget.netamenu.MenuItem;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Locale;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.CropSquareTransformation;
import jp.wasabeef.glide.transformations.CropTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by Sora on 2018/3/8.
 */

public class SearchAllHolder extends ClickableViewHolder {
    public SearchAllHolder(View itemView) {
        super(itemView);
    }

    public void createItem(final NewDynamicEntity entity, final int position) {
        //from
        if (!TextUtils.isEmpty(entity.getFrom())) {
            setVisible(R.id.rl_from_top, true);
            setText(R.id.tv_from_name, entity.getFrom());
            if (!TextUtils.isEmpty(entity.getFromSchema())) {
                $(R.id.tv_from_name).setOnClickListener(new NoDoubleClickListener() {
                    @Override
                    public void onNoDoubleClick(View v) {
                        Uri uri = Uri.parse(entity.getFromSchema());
                        IntentUtils.toActivityFromUri(itemView.getContext(), uri, v);
                    }
                });
            } else {
                $(R.id.tv_from_name).setOnClickListener(null);
            }
        } else {
            setVisible(R.id.rl_from_top, false);
        }

        //user top
        if (entity.getCreateUser().isVip()) {
            setVisible(R.id.iv_vip, true);
        } else {
            setVisible(R.id.iv_vip, false);
        }
        int size = (int) itemView.getContext().getResources().getDimension(R.dimen.x80);
        Glide.with(itemView.getContext())
                .load(StringUtils.getUrl(itemView.getContext(), entity.getCreateUser().getHeadPath(), size, size, false, true))
                .error(R.drawable.bg_default_circle)
                .placeholder(R.drawable.bg_default_circle)
                .bitmapTransform(new CropCircleTransformation(itemView.getContext()))
                .into((ImageView) $(R.id.iv_avatar));
        $(R.id.iv_avatar).setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                ViewUtils.toPersonal(itemView.getContext(), entity.getCreateUser().getUserId());
            }
        });
        setText(R.id.tv_name, entity.getCreateUser().getUserName());
        setImageResource(R.id.iv_sex, entity.getCreateUser().getSex().equalsIgnoreCase("M") ? R.drawable.ic_user_girl : R.drawable.ic_user_boy);
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
        View[] huizhang = {$(R.id.fl_huizhang_1)};
        TextView[] huizhangT = {$(R.id.tv_huizhang_1)};
        if (entity.getCreateUser().getBadge() != null) {
            ArrayList<BadgeEntity> badgeEntities = new ArrayList<>();
            badgeEntities.add(entity.getCreateUser().getBadge());
            ViewUtils.badge(itemView.getContext(), huizhang, huizhangT, badgeEntities);
        } else {
            huizhang[0].setVisibility(View.GONE);
            huizhangT[0].setVisibility(View.GONE);
        }

        $(R.id.iv_more).setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                showMenu(entity.getCreateUser().getUserName(), entity.getText(), entity.getId());
            }
        });
        setText(R.id.tv_time, StringUtils.timeFormat(entity.getCreateTime()));

        //content
        setText(R.id.tv_content, TagControl.getInstance().paresToSpann(itemView.getContext(), entity.getText()));
        ((TextView) $(R.id.tv_content)).setMovementMethod(LinkMovementMethod.getInstance());
        $(R.id.tv_content).setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                if (!TextUtils.isEmpty(entity.getId())) {
                    DynamicActivity.startActivity(context, entity.getId());
                }
            }
        });

        //extra
        setVisible(R.id.ll_img_root, false);
        ((LinearLayout) $(R.id.ll_img_root)).removeAllViews();
        $(R.id.ll_img_root).setOnClickListener(null);
        boolean showHongbao = false;
        if ("DELETE".equals(entity.getType())) {//已被删除
            setVisible(R.id.ll_img_root, true);
            $(R.id.ll_img_root).setBackgroundColor(Color.TRANSPARENT);
            TextView tv = new TextView(itemView.getContext());
            tv.setText("该内容已被删除");
            tv.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.white));
            tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, itemView.getContext().getResources().getDimension(R.dimen.x36));
            tv.setGravity(Gravity.CENTER);
            tv.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.gray_e8e8e8));
            int h = (int) itemView.getResources().getDimension(R.dimen.y320);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, h);
            tv.setLayoutParams(lp);
            ((LinearLayout) $(R.id.ll_img_root)).addView(tv);
        } else if ("DYNAMIC".equals(entity.getType())) {
            setVisible(R.id.ll_img_root, true);
            $(R.id.ll_img_root).setBackgroundColor(Color.TRANSPARENT);
            DynamicContentEntity dynamicContentEntity = new Gson().fromJson(entity.getDetail(), DynamicContentEntity.class);
            $(R.id.ll_img_root).setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View v) {
                    if (!TextUtils.isEmpty(entity.getId())) {
                        DynamicActivity.startActivity(context, entity.getId());
                    }
                }
            });
            if (dynamicContentEntity.getImages() != null && dynamicContentEntity.getImages().size() > 0) {
                setImg(dynamicContentEntity.getImages());
            } else {
                setVisible(R.id.ll_img_root, false);
            }
        } else if ("FOLDER".equals(entity.getType())) {
            setVisible(R.id.ll_img_root, true);
            $(R.id.ll_img_root).setBackgroundColor(Color.TRANSPARENT);
            final ShareFolderEntity folderEntity = new Gson().fromJson(entity.getDetail(), ShareFolderEntity.class);
            View folder = LayoutInflater.from(itemView.getContext()).inflate(R.layout.item_feed_type_2_v3, null);
            folder.setBackgroundResource(R.drawable.shape_gray_f6f6f6_background_y8);
            TextView tvMark = folder.findViewById(R.id.tv_mark);
            ImageView ivCover = folder.findViewById(R.id.iv_cover);
            TextView tvTitle = folder.findViewById(R.id.tv_title);
            TextView tvTag1 = folder.findViewById(R.id.tv_tag_1);
            TextView tvTag2 = folder.findViewById(R.id.tv_tag_2);
            ImageView ivAvatar = folder.findViewById(R.id.iv_user_avatar);
            TextView tvUserName = folder.findViewById(R.id.tv_user_name);
            TextView tvCoin = folder.findViewById(R.id.tv_coin);
            TextView tvExtra = folder.findViewById(R.id.tv_extra);
            View coverRoot = folder.findViewById(R.id.fl_cover_root);
            folder.findViewById(R.id.iv_play).setVisibility(View.GONE);

            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) coverRoot.getLayoutParams();
            lp.topMargin = getResources().getDimensionPixelSize(R.dimen.y16);
            lp.bottomMargin = getResources().getDimensionPixelSize(R.dimen.y16);
            lp.leftMargin = getResources().getDimensionPixelSize(R.dimen.x16);
            coverRoot.requestLayout();

            int w = context.getResources().getDimensionPixelSize(R.dimen.x222);
            int h = context.getResources().getDimensionPixelSize(R.dimen.y160);

            Glide.with(context)
                    .load(StringUtils.getUrl(context, folderEntity.getFolderCover(), w, h, false, true))
                    .error(R.drawable.shape_gray_e8e8e8_background)
                    .placeholder(R.drawable.shape_gray_e8e8e8_background)
                    .bitmapTransform(new CropTransformation(context, w, h), new RoundedCornersTransformation(context, getResources().getDimensionPixelSize(R.dimen.y8), 0))
                    .into(ivCover);

            tvTitle.setText(folderEntity.getFolderName());

            //tag
            View[] tagsId = {tvTag1, tvTag2};
            tvTag1.setOnClickListener(null);
            tvTag2.setOnClickListener(null);
            if (folderEntity.getFolderTags().size() > 1) {
                tvTag1.setVisibility(View.VISIBLE);
                tvTag2.setVisibility(View.VISIBLE);
            } else if (folderEntity.getFolderTags().size() > 0) {
                tvTag1.setVisibility(View.VISIBLE);
                tvTag2.setVisibility(View.INVISIBLE);
            } else {
                tvTag1.setVisibility(View.INVISIBLE);
                tvTag2.setVisibility(View.INVISIBLE);
            }
            int tagSize = tagsId.length > folderEntity.getFolderTags().size() ? folderEntity.getFolderTags().size() : tagsId.length;
            for (int i = 0; i < tagSize; i++) {
                TagUtils.setBackGround(folderEntity.getFolderTags().get(i), tagsId[i]);
            }

            //user
            int avatarSize = context.getResources().getDimensionPixelSize(R.dimen.y32);
            Glide.with(context)
                    .load(StringUtils.getUrl(context, folderEntity.getCreateUser().getHeadPath(), avatarSize, avatarSize, false, true))
                    .error(R.drawable.bg_default_circle)
                    .placeholder(R.drawable.bg_default_circle)
                    .bitmapTransform(new CropCircleTransformation(context))
                    .into(ivAvatar);
            tvUserName.setText(folderEntity.getCreateUser().getUserName());
            ivAvatar.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View v) {
                    ViewUtils.toPersonal(context, folderEntity.getCreateUser().getUserId());
                }
            });
            tvUserName.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View v) {
                    ViewUtils.toPersonal(context, folderEntity.getCreateUser().getUserId());
                }
            });

            tvMark.setVisibility(View.VISIBLE);
            if (folderEntity.getFolderType().equals(FolderType.ZH.toString())) {
                tvMark.setText("综合");
                tvMark.setBackgroundResource(R.drawable.shape_rect_zonghe);
            } else if (folderEntity.getFolderType().equals(FolderType.TJ.toString())) {
                tvMark.setText("图集");
                tvMark.setBackgroundResource(R.drawable.shape_rect_tuji);
            } else if (folderEntity.getFolderType().equals(FolderType.MH.toString())) {
                tvMark.setText("漫画");
                tvMark.setBackgroundResource(R.drawable.shape_rect_manhua);
            } else if (folderEntity.getFolderType().equals(FolderType.XS.toString())) {
                tvMark.setText("小说");
                tvMark.setBackgroundResource(R.drawable.shape_rect_xiaoshuo);
            } else if (folderEntity.getFolderType().equals(FolderType.SP.toString())) {
                tvMark.setText("视频集");
                tvMark.setBackgroundResource(R.drawable.shape_rect_shipin);
            } else if (folderEntity.getFolderType().equals(FolderType.YY.toString())) {
                tvMark.setText("音乐集");
                tvMark.setBackgroundResource(R.drawable.shape_rect_yinyue);
            } else {
                tvMark.setVisibility(View.GONE);
            }

            tvCoin.setText(folderEntity.getCoin() + "节操");
            tvExtra.setText(folderEntity.getItems() + "项");

            ((LinearLayout) $(R.id.ll_img_root)).addView(folder);

            $(R.id.ll_img_root).setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View v) {
                    if (folderEntity.getFolderType().equals(FolderType.ZH.toString())) {
                        NewFileCommonActivity.startActivity(itemView.getContext(), FolderType.ZH.toString(), folderEntity.getFolderId(), folderEntity.getCreateUser().getUserId());
                    } else if (folderEntity.getFolderType().equals(FolderType.TJ.toString())) {
                        NewFileCommonActivity.startActivity(itemView.getContext(), FolderType.TJ.toString(), folderEntity.getFolderId(), folderEntity.getCreateUser().getUserId());
                    } else if (folderEntity.getFolderType().equals(FolderType.MH.toString())) {
                        NewFileManHuaActivity.startActivity(itemView.getContext(), FolderType.MH.toString(), folderEntity.getFolderId(), folderEntity.getCreateUser().getUserId());
                    } else if (folderEntity.getFolderType().equals(FolderType.XS.toString())) {
                        NewFileXiaoshuoActivity.startActivity(itemView.getContext(), FolderType.XS.toString(), folderEntity.getFolderId(), folderEntity.getCreateUser().getUserId());
                    } else if (folderEntity.getFolderType().equals(FolderType.SP.toString())) {
                        FileMovieActivity.startActivity(context, FolderType.SP.toString(), folderEntity.getFolderId(), entity.getCreateUser().getUserId());
                    } else if (folderEntity.getFolderType().equals(FolderType.YY.toString())) {
                        FileMovieActivity.startActivity(context, FolderType.YY.toString(), folderEntity.getFolderId(), entity.getCreateUser().getUserId());
                    }
                }
            });
        } else if ("ARTICLE".equals(entity.getType())) {
            setVisible(R.id.ll_img_root, true);
            $(R.id.ll_img_root).setBackgroundColor(Color.TRANSPARENT);
            final ShareArticleEntity folderEntity = new Gson().fromJson(entity.getDetail(), ShareArticleEntity.class);
            View article = LayoutInflater.from(itemView.getContext()).inflate(R.layout.item_feed_type_5_v3, null);
            ImageView ivCover = article.findViewById(R.id.iv_cover);
            ImageView ivAvatar = article.findViewById(R.id.iv_user_avatar);
            TextView tvMark = article.findViewById(R.id.tv_mark);
            TextView tvUserName = article.findViewById(R.id.tv_user_name);
            TextView tvTag1 = article.findViewById(R.id.tv_tag_1);
            TextView tvTag2 = article.findViewById(R.id.tv_tag_2);
            TextView tvReadNum = article.findViewById(R.id.tv_read_num);
            TextView tvTitle = article.findViewById(R.id.tv_title);

            int w = DensityUtil.getScreenWidth(context) - getResources().getDimensionPixelSize(R.dimen.x48);
            int h = getResources().getDimensionPixelSize(R.dimen.y400);

            Glide.with(itemView.getContext())
                    .load(StringUtils.getUrl(itemView.getContext(), folderEntity.getCover(), w, h, false, true))
                    .error(R.drawable.shape_gray_e8e8e8_background)
                    .placeholder(R.drawable.shape_gray_e8e8e8_background)
                    .bitmapTransform(new CropTransformation(itemView.getContext(), w, h), new RoundedCornersTransformation(context, getResources().getDimensionPixelSize(R.dimen.y8), 0))
                    .into(ivCover);

            tvMark.setText("文章");
            tvTitle.setText(folderEntity.getTitle());
            size = (int) itemView.getContext().getResources().getDimension(R.dimen.y32);
            Glide.with(itemView.getContext())
                    .load(StringUtils.getUrl(itemView.getContext(), folderEntity.getDocCreateUser().getHeadPath(), size, size, false, true))
                    .error(R.drawable.bg_default_circle)
                    .placeholder(R.drawable.bg_default_circle)
                    .bitmapTransform(new CropCircleTransformation(itemView.getContext()))
                    .into(ivAvatar);

            ivAvatar.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View v) {
                    ViewUtils.toPersonal(itemView.getContext(), folderEntity.getDocCreateUser().getUserId());
                }
            });

            tvUserName.setText(folderEntity.getDocCreateUser().getUserName());
            tvReadNum.setText("阅读" + folderEntity.getReadNum());

            //tag
            View[] tagsId = {tvTag1, tvTag2};
            tvTag1.setOnClickListener(null);
            tvTag2.setOnClickListener(null);
            if (folderEntity.getTexts().size() > 1) {
                tvTag1.setVisibility(View.VISIBLE);
                tvTag2.setVisibility(View.VISIBLE);
            } else if (folderEntity.getTexts().size() > 0) {
                tvTag1.setVisibility(View.VISIBLE);
                tvTag2.setVisibility(View.INVISIBLE);
            } else {
                tvTag1.setVisibility(View.INVISIBLE);
                tvTag2.setVisibility(View.INVISIBLE);
            }
            int tagSize = tagsId.length > folderEntity.getTexts().size() ? folderEntity.getTexts().size() : tagsId.length;
            for (int i = 0; i < tagSize; i++) {
                TagUtils.setBackGround(folderEntity.getTexts().get(i).getText(), tagsId[i]);
            }

            ((LinearLayout) $(R.id.ll_img_root)).addView(article);
            $(R.id.ll_img_root).setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View v) {
                    if (!TextUtils.isEmpty(folderEntity.getDocId())) {
                        Intent i = new Intent(itemView.getContext(), NewDocDetailActivity.class);
                        i.putExtra("uuid", folderEntity.getDocId());
                        itemView.getContext().startActivity(i);
                    }
                }
            });
        } else if ("RETWEET".equals(entity.getType())) {
            setVisible(R.id.ll_img_root, true);
            $(R.id.ll_img_root).setBackgroundColor(ContextCompat.getColor(context, R.color.cyan_eefdff));
            final RetweetEntity retweetEntity = new Gson().fromJson(entity.getDetail(), RetweetEntity.class);
            if (!TextUtils.isEmpty(retweetEntity.getContent())) {
                TextView tv = new TextView(itemView.getContext());
                tv.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.black_1e1e1e));
                tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, itemView.getResources().getDimension(R.dimen.x30));
                tv.setLineSpacing(itemView.getResources().getDimension(R.dimen.y12), 1);
                tv.setMaxLines(10);
                tv.setEllipsize(TextUtils.TruncateAt.END);
                String res = "<at_user user_id=" + retweetEntity.getCreateUserId() + ">" + retweetEntity.getCreateUserName() + ":</at_user>" + retweetEntity.getContent();
                tv.setText(TagControl.getInstance().paresToSpann(context, res));
                tv.setMovementMethod(LinkMovementMethod.getInstance());
                tv.setOnClickListener(new NoDoubleClickListener() {
                    @Override
                    public void onNoDoubleClick(View v) {
                        if (!TextUtils.isEmpty(retweetEntity.getOldDynamicId())) {
                            DynamicActivity.startActivity(context, retweetEntity.getOldDynamicId());
                        }
                    }
                });
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                lp.topMargin = (int) itemView.getResources().getDimension(R.dimen.y24);
                lp.bottomMargin = (int) itemView.getResources().getDimension(R.dimen.y24);
                tv.setLayoutParams(lp);
                ((LinearLayout) $(R.id.ll_img_root)).addView(tv);
                $(R.id.ll_img_root).setOnClickListener(new NoDoubleClickListener() {
                    @Override
                    public void onNoDoubleClick(View v) {
                        if (!TextUtils.isEmpty(retweetEntity.getOldDynamicId())) {
                            DynamicActivity.startActivity(context, retweetEntity.getOldDynamicId());
                        }
                    }
                });
            }
            if (retweetEntity.getImages() != null && retweetEntity.getImages().size() > 0) {
                setImg(retweetEntity.getImages());
            } else {
                if (TextUtils.isEmpty(retweetEntity.getContent())) {
                    setVisible(R.id.ll_img_root, false);
                }
            }
            setText(R.id.tv_retweet_time, StringUtils.timeFormat(retweetEntity.getCreateTime()));
            setVisible(R.id.ll_retweet_bottom_root, true);
            $(R.id.ll_retweet_bottom_root).setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View v) {
                    if (!TextUtils.isEmpty(retweetEntity.getOldDynamicId())) {
                        DynamicActivity.startActivity(context, retweetEntity.getOldDynamicId());
                    }
                }
            });
            if (retweetEntity.getRtNum() == 0) {
                setText(R.id.tv_retweet_forward_num, "转发");
            } else {
                setText(R.id.tv_retweet_forward_num, StringUtils.getNumberInLengthLimit(retweetEntity.getRtNum(), 3));
            }
            if (retweetEntity.getComments() == 0) {
                setText(R.id.tv_retweet_comment_num, "评论");
            } else {
                setText(R.id.tv_retweet_comment_num, StringUtils.getNumberInLengthLimit(retweetEntity.getComments(), 3));
            }
            if (retweetEntity.getLikes() == 0) {
                setText(R.id.tv_retweet_tag_num, "点赞");
            } else {
                setText(R.id.tv_retweet_tag_num, StringUtils.getNumberInLengthLimit(retweetEntity.getLikes(), 3));
            }
            if (retweetEntity.getCoins() > 0) {
                showHongbao = true;
            }
            showHongBao(true, retweetEntity.getCoins(), retweetEntity.getSurplus(), retweetEntity.getOldDynamicId(), retweetEntity.getCreateUserHead(), retweetEntity.getUsers());
        } else if ("MUSIC".equals(entity.getType())) {
            setVisible(R.id.ll_img_root, true);
            $(R.id.ll_img_root).setBackgroundColor(Color.TRANSPARENT);
            final ShareMusicEntity folderEntity = new Gson().fromJson(entity.getDetail(), ShareMusicEntity.class);
            View folder = LayoutInflater.from(itemView.getContext()).inflate(R.layout.item_feed_type_2_v3, null);
            folder.setBackgroundResource(R.drawable.shape_gray_f6f6f6_background_y8);
            folder.findViewById(R.id.tv_mark).setVisibility(View.GONE);
            ImageView ivCover = folder.findViewById(R.id.iv_cover);
            TextView tvTitle = folder.findViewById(R.id.tv_title);
            TextView tvTag1 = folder.findViewById(R.id.tv_tag_1);
            TextView tvTag2 = folder.findViewById(R.id.tv_tag_2);
            ImageView ivAvatar = folder.findViewById(R.id.iv_user_avatar);
            TextView tvUserName = folder.findViewById(R.id.tv_user_name);
            TextView tvCoin = folder.findViewById(R.id.tv_coin);
            TextView tvExtra = folder.findViewById(R.id.tv_extra);
            ImageView ivPlay = folder.findViewById(R.id.iv_play);
            TextView tvPlayNum = folder.findViewById(R.id.tv_play_num);
            folder.findViewById(R.id.tv_danmu_num).setVisibility(View.GONE);
            View coverRoot = folder.findViewById(R.id.fl_cover_root);

            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) coverRoot.getLayoutParams();
            lp.topMargin = getResources().getDimensionPixelSize(R.dimen.y16);
            lp.bottomMargin = getResources().getDimensionPixelSize(R.dimen.y16);
            lp.leftMargin = getResources().getDimensionPixelSize(R.dimen.x16);
            coverRoot.requestLayout();

            int w = context.getResources().getDimensionPixelSize(R.dimen.x222);
            int h = context.getResources().getDimensionPixelSize(R.dimen.y160);
            Glide.with(context)
                    .load(StringUtils.getUrl(context, folderEntity.getFileCover(), w, h, false, true))
                    .error(R.drawable.shape_gray_e8e8e8_background)
                    .placeholder(R.drawable.shape_gray_e8e8e8_background)
                    .bitmapTransform(new CropTransformation(context, w, h), new RoundedCornersTransformation(context, getResources().getDimensionPixelSize(R.dimen.y8), 0))
                    .into(ivCover);

            tvTitle.setText(folderEntity.getFileName());

            tvPlayNum.setText(String.valueOf(folderEntity.getPlayNum()));
            tvPlayNum.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(context, R.drawable.ic_baglist_music_times), null, null, null);
            //tag
            View[] tagsId = {tvTag1, tvTag2};
            tvTag1.setOnClickListener(null);
            tvTag2.setOnClickListener(null);
            if (folderEntity.getFileTags().size() > 1) {
                tvTag1.setVisibility(View.VISIBLE);
                tvTag2.setVisibility(View.VISIBLE);
            } else if (folderEntity.getFileTags().size() > 0) {
                tvTag1.setVisibility(View.VISIBLE);
                tvTag2.setVisibility(View.INVISIBLE);
            } else {
                tvTag1.setVisibility(View.INVISIBLE);
                tvTag2.setVisibility(View.INVISIBLE);
            }
            int tagSize = tagsId.length > folderEntity.getFileTags().size() ? folderEntity.getFileTags().size() : tagsId.length;
            for (int i = 0; i < tagSize; i++) {
                TagUtils.setBackGround(folderEntity.getFileTags().get(i).getText(), tagsId[i]);
            }

            //user
            int avatarSize = context.getResources().getDimensionPixelSize(R.dimen.y32);
            Glide.with(context)
                    .load(StringUtils.getUrl(context, folderEntity.getCreateUser().getHeadPath(), avatarSize, avatarSize, false, true))
                    .error(R.drawable.bg_default_circle)
                    .placeholder(R.drawable.bg_default_circle)
                    .bitmapTransform(new CropCircleTransformation(context))
                    .into(ivAvatar);
            tvUserName.setText(folderEntity.getCreateUser().getUserName());
            ivAvatar.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View v) {
                    ViewUtils.toPersonal(context, folderEntity.getCreateUser().getUserId());
                }
            });
            tvUserName.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View v) {
                    ViewUtils.toPersonal(context, folderEntity.getCreateUser().getUserId());
                }
            });

            tvCoin.setText(folderEntity.getCoin() + "节操");
            tvExtra.setText(folderEntity.getTimestamp());

            ivPlay.setVisibility(View.VISIBLE);
            ivPlay.setImageResource(R.drawable.ic_baglist_music_play);

            ((LinearLayout) $(R.id.ll_img_root)).addView(folder);

            $(R.id.ll_img_root).setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View v) {
                    KiraMusicActivity.startActivity(context, folderEntity.getFolderId(), folderEntity.getFileId(), folderEntity.getFileName(), folderEntity.getFileCover());
                }
            });
        } else if ("MOVIE".equals(entity.getType())) {
            setVisible(R.id.ll_img_root, true);
            $(R.id.ll_img_root).setBackgroundColor(Color.TRANSPARENT);
            final ShareMovieEntity folderEntity = new Gson().fromJson(entity.getDetail(), ShareMovieEntity.class);
            View folder = LayoutInflater.from(itemView.getContext()).inflate(R.layout.item_feed_type_2_v3, null);
            folder.setBackgroundResource(R.drawable.shape_gray_f6f6f6_background_y8);
            folder.findViewById(R.id.tv_mark).setVisibility(View.GONE);
            ImageView ivCover = folder.findViewById(R.id.iv_cover);
            TextView tvTitle = folder.findViewById(R.id.tv_title);
            TextView tvTag1 = folder.findViewById(R.id.tv_tag_1);
            TextView tvTag2 = folder.findViewById(R.id.tv_tag_2);
            ImageView ivAvatar = folder.findViewById(R.id.iv_user_avatar);
            TextView tvUserName = folder.findViewById(R.id.tv_user_name);
            TextView tvCoin = folder.findViewById(R.id.tv_coin);
            TextView tvExtra = folder.findViewById(R.id.tv_extra);
            ImageView ivPlay = folder.findViewById(R.id.iv_play);
            TextView tvPlayNum = folder.findViewById(R.id.tv_play_num);
            TextView tvDanmuNum = folder.findViewById(R.id.tv_danmu_num);
            View coverRoot = folder.findViewById(R.id.fl_cover_root);

            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) coverRoot.getLayoutParams();
            lp.topMargin = getResources().getDimensionPixelSize(R.dimen.y16);
            lp.bottomMargin = getResources().getDimensionPixelSize(R.dimen.y16);
            lp.leftMargin = getResources().getDimensionPixelSize(R.dimen.x16);
            coverRoot.requestLayout();

            int w = context.getResources().getDimensionPixelSize(R.dimen.x222);
            int h = context.getResources().getDimensionPixelSize(R.dimen.y160);

            Glide.with(context)
                    .load(StringUtils.getUrl(context, folderEntity.getFileCover(), w, h, false, true))
                    .error(R.drawable.shape_gray_e8e8e8_background)
                    .placeholder(R.drawable.shape_gray_e8e8e8_background)
                    .bitmapTransform(new CropTransformation(context, w, h), new RoundedCornersTransformation(context, getResources().getDimensionPixelSize(R.dimen.y8), 0))
                    .into(ivCover);

            tvTitle.setText(folderEntity.getFileName());
            tvPlayNum.setText(String.valueOf(folderEntity.getPlayNum()));
            tvPlayNum.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(context, R.drawable.ic_baglist_video_playtimes_gray), null, null, null);
            tvDanmuNum.setVisibility(View.VISIBLE);
            tvDanmuNum.setText(String.valueOf(folderEntity.getBarrageNum()));
            //tag
            View[] tagsId = {tvTag1, tvTag2};
            tvTag1.setOnClickListener(null);
            tvTag2.setOnClickListener(null);
            if (folderEntity.getFileTags().size() > 1) {
                tvTag1.setVisibility(View.VISIBLE);
                tvTag2.setVisibility(View.VISIBLE);
            } else if (folderEntity.getFileTags().size() > 0) {
                tvTag1.setVisibility(View.VISIBLE);
                tvTag2.setVisibility(View.INVISIBLE);
            } else {
                tvTag1.setVisibility(View.INVISIBLE);
                tvTag2.setVisibility(View.INVISIBLE);
            }
            int tagSize = tagsId.length > folderEntity.getFileTags().size() ? folderEntity.getFileTags().size() : tagsId.length;
            for (int i = 0; i < tagSize; i++) {
                TagUtils.setBackGround(folderEntity.getFileTags().get(i).getText(), tagsId[i]);
            }

            //user
            int avatarSize = context.getResources().getDimensionPixelSize(R.dimen.y32);
            Glide.with(context)
                    .load(StringUtils.getUrl(context, folderEntity.getCreateUser().getHeadPath(), avatarSize, avatarSize, false, true))
                    .error(R.drawable.bg_default_circle)
                    .placeholder(R.drawable.bg_default_circle)
                    .bitmapTransform(new CropCircleTransformation(context))
                    .into(ivAvatar);
            tvUserName.setText(folderEntity.getCreateUser().getUserName());
            ivAvatar.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View v) {
                    ViewUtils.toPersonal(context, folderEntity.getCreateUser().getUserId());
                }
            });
            tvUserName.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View v) {
                    ViewUtils.toPersonal(context, folderEntity.getCreateUser().getUserId());
                }
            });

            tvCoin.setText(folderEntity.getCoin() + "节操");
            tvExtra.setText(folderEntity.getTimestamp());

            ivPlay.setVisibility(View.VISIBLE);
            ivPlay.setImageResource(R.drawable.ic_baglist_video_play);

            ((LinearLayout) $(R.id.ll_img_root)).addView(folder);

            $(R.id.ll_img_root).setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View v) {
                    KiraVideoActivity.startActivity(context, folderEntity.getFolderId(), folderEntity.getFileId(), folderEntity.getFileName(), folderEntity.getFileCover());
                }
            });
        } else if ("MESSAGE".equals(entity.getType())) {
            setVisible(R.id.ll_img_root, true);
            $(R.id.ll_img_root).setBackgroundColor(Color.TRANSPARENT);
            final MessageDynamicEntity folderEntity = new Gson().fromJson(entity.getDetail(), MessageDynamicEntity.class);
            View folder = LayoutInflater.from(itemView.getContext()).inflate(R.layout.item_feed_type_6_v3, null);
            ImageView ivAvatar = folder.findViewById(R.id.iv_user_avatar);
            TextView tvUserName = folder.findViewById(R.id.tv_user_name);
            TextView tvContent = folder.findViewById(R.id.tv_content);
            TextView tvDesc = folder.findViewById(R.id.tv_content_desc);

            size = getResources().getDimensionPixelSize(R.dimen.y44);
            Glide.with(context)
                    .load(StringUtils.getUrl(context, folderEntity.getHeadPath(), size, size, false, true))
                    .error(R.drawable.bg_default_circle)
                    .placeholder(R.drawable.bg_default_circle)
                    .bitmapTransform(new CropCircleTransformation(context))
                    .into(ivAvatar);
            tvUserName.setText(folderEntity.getUserName());
            folder.findViewById(R.id.ll_user_root).setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View v) {
                    ViewUtils.toPersonal(context, folderEntity.getUserId());
                }
            });
            tvContent.setText(folderEntity.getShowMsg());
            tvDesc.setText(folderEntity.getDate());
            ((LinearLayout) $(R.id.ll_img_root)).addView(folder);

        } else if ("PRODUCT".equals(entity.getType())) {
            setVisible(R.id.ll_img_root, true);
            $(R.id.ll_img_root).setBackgroundColor(Color.TRANSPARENT);
            final ProductDyEntity folderEntity = new Gson().fromJson(entity.getDetail(), ProductDyEntity.class);
            View folder = LayoutInflater.from(itemView.getContext()).inflate(R.layout.item_feed_type_7_v3, null);
            ImageView ivCover = folder.findViewById(R.id.iv_cover);
            TextView tvTitle = folder.findViewById(R.id.tv_title);
            TextView tvContent = folder.findViewById(R.id.tv_content);
            TextView tv_coin = folder.findViewById(R.id.tv_coin);

            size = getResources().getDimensionPixelSize(R.dimen.y140);
            ViewGroup.LayoutParams params = ivCover.getLayoutParams();
            params.width = size;
            params.height = size;
            ivCover.setLayoutParams(params);
            Glide.with(context)
                    .load(StringUtils.getUrl(context, folderEntity.getIcon(), size, size, false, true))
                    .error(R.drawable.shape_gray_e8e8e8_background)
                    .placeholder(R.drawable.shape_gray_e8e8e8_background)
                    .into(ivCover);
            tvTitle.setText(folderEntity.getProductName());
            tvContent.setText(folderEntity.getDescribe());
            String str = "";
            if (folderEntity.getCoin() > 0) {
                str = folderEntity.getCoin() + "节操";
            }
            if (folderEntity.getRmb() > 0) {
                str += String.format(Locale.getDefault(), "%.2f元", (float) folderEntity.getRmb() / 100);
            }
            tv_coin.setText(str);
            ((LinearLayout) $(R.id.ll_img_root)).addView(folder);
            $(R.id.ll_img_root).setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View v) {
                    Intent i = new Intent(context, ShopDetailActivity.class);
                    i.putExtra("uuid", folderEntity.getProductId());
                    context.startActivity(i);
                }
            });
        }

        //coins
        if (!showHongbao) {
            showHongBao(false, entity.getCoins(), entity.getSurplus(), entity.getId(), entity.getCreateUser().getHeadPath(), entity.getUsers());
        }

        if (!"RETWEET".equals(entity.getType())) {
            setVisible(R.id.ll_retweet_bottom_root, false);
        }

        //bottom
        setVisible(R.id.rl_list_bottom_root, false);
        setVisible(R.id.rl_list_bottom_root_2, true);
        if (entity.getRetweets() == 0) {
            setText(R.id.tv_forward_num_2, "转发");
        } else {
            setText(R.id.tv_forward_num_2, StringUtils.getNumberInLengthLimit(entity.getRetweets(), 3));
        }
        if (entity.getComments() == 0) {
            setText(R.id.tv_comment_num_2, "评论");
        } else {
            setText(R.id.tv_comment_num_2, StringUtils.getNumberInLengthLimit(entity.getComments(), 3));
        }

        $(R.id.iv_like_item).setSelected(entity.isThumb());
        $(R.id.tv_like_item).setSelected(entity.isThumb());

        if (entity.getThumbs() == 0) {
            setVisible(R.id.fl_tag_root_2, true);
            setVisible(R.id.rl_tag_root_2, false);
        } else {
            setVisible(R.id.fl_tag_root_2, false);
            setVisible(R.id.rl_tag_root_2, true);
            setText(R.id.tv_like_num, StringUtils.getNumberInLengthLimit(entity.getThumbs(), 3));
            int trueSize = (int) context.getResources().getDimension(R.dimen.y48);
            int imgSize = (int) context.getResources().getDimension(R.dimen.y44);
            int startMargin = (int) -context.getResources().getDimension(R.dimen.y10);
            int showSize = 4;
            if (entity.getThumbUsers().size() < showSize) {
                showSize = entity.getThumbUsers().size();
            }
            ((LinearLayout) $(R.id.ll_like_user_root)).removeAllViews();
            if (showSize == 4) {
                ImageView iv = new ImageView(context);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(trueSize, trueSize);
                lp.leftMargin = startMargin;
                iv.setLayoutParams(lp);
                iv.setImageResource(R.drawable.btn_feed_like_more);
                ((LinearLayout) $(R.id.ll_like_user_root)).addView(iv);
            }
            for (int i = showSize - 1; i >= 0; i--) {
                final SimpleUserEntity userEntity = entity.getThumbUsers().get(i);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(trueSize, trueSize);
                if (i != 0) {
                    lp.leftMargin = startMargin;
                }
                View likeUser = LayoutInflater.from(itemView.getContext()).inflate(R.layout.item_white_border_img, null);
                likeUser.setLayoutParams(lp);
                ImageView img = likeUser.findViewById(R.id.iv_img);
                Glide.with(context)
                        .load(StringUtils.getUrl(context, userEntity.getUserIcon(), imgSize, imgSize, false, true))
                        .error(R.drawable.bg_default_circle)
                        .placeholder(R.drawable.bg_default_circle)
                        .bitmapTransform(new CropCircleTransformation(context))
                        .into(img);
                img.setOnClickListener(new NoDoubleClickListener() {
                    @Override
                    public void onNoDoubleClick(View v) {
                        ViewUtils.toPersonal(context, userEntity.getUserId());
                    }
                });
                ((LinearLayout) $(R.id.ll_like_user_root)).addView(likeUser);
            }
        }
        $(R.id.fl_forward_root_2).setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                CreateForwardActivity.startActivityForResult(context, entity);
            }
        });
        $(R.id.fl_comment_root_2).setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                DynamicActivity.startActivity(context, entity, true, true);
            }
        });
        $(R.id.fl_tag_root_2).setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                if (context instanceof AllSearchActivity) {
                    ((AllSearchActivity) context).likeDynamic(entity.getId(), entity.isThumb(), position);
                }
            }
        });
        $(R.id.iv_like_item).setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                if (context instanceof AllSearchActivity) {
                    ((AllSearchActivity) context).likeDynamic(entity.getId(), entity.isThumb(), position);
                }
            }
        });
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void setImg(ArrayList<Image> images) {
        if (images.size() == 1) {
            Image image = images.get(0);
            int[] wh;
            if (image.getW() > image.getH()) {
                wh = BitmapUtils.getDocIconSizeFromWItem(image.getW(), image.getH(), (int) itemView.getResources().getDimension(R.dimen.x460));
            } else if (image.getW() == image.getH()) {
                wh = BitmapUtils.getDocIconSizeFromW(image.getW(), image.getH(), (int) itemView.getResources().getDimension(R.dimen.x460));
            } else {
                int[] res = new int[2];
                res[1] = (int) itemView.getResources().getDimension(R.dimen.x460);
                res[0] = image.getW() * (int) itemView.getResources().getDimension(R.dimen.x460) / image.getH();
                wh = res;
//                wh = BitmapUtils.getDocIconSizeFromH(image.getW(), image.getH(), (int) itemView.getResources().getDimension(R.dimen.x460));
            }
            ImageView iv = new ImageView(itemView.getContext());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(wh[0], wh[1]);
            lp.bottomMargin = (int) itemView.getResources().getDimension(R.dimen.y24);
            iv.setLayoutParams(lp);
            Glide.with(itemView.getContext())
                    .load(StringUtils.getUrl(itemView.getContext(), image.getPath(), wh[0], wh[1], false, true))
                    .error(R.drawable.shape_gray_e8e8e8_background)
                    .placeholder(R.drawable.shape_gray_e8e8e8_background)
                    .into(iv);
            showImg(iv, images, 0);
            ((LinearLayout) $(R.id.ll_img_root)).addView(iv);
        } else if (images.size() == 2) {
            LinearLayout layout = new LinearLayout(itemView.getContext());
            layout.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layout.setLayoutParams(lp);
            int w = (int) ((DensityUtil.getScreenWidth(itemView.getContext()) - itemView.getResources().getDimension(R.dimen.x54))) / 2;
            for (int i = 0; i < images.size(); i++) {
                Image image = images.get(i);
                LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(w, w);
                if (i == 0) {
                    lp1.rightMargin = (int) itemView.getResources().getDimension(R.dimen.x6);
                }
                ImageView iv = new ImageView(itemView.getContext());
                iv.setLayoutParams(lp1);
                Glide.with(itemView.getContext())
                        .load(StringUtils.getUrl(itemView.getContext(), image.getPath(), w, w, false, true))
                        .error(R.drawable.shape_gray_e8e8e8_background)
                        .placeholder(R.drawable.shape_gray_e8e8e8_background)
                        .into(iv);
                layout.addView(iv);
                showImg(iv, images, i);
            }
            ((LinearLayout) $(R.id.ll_img_root)).addView(layout);
        } else if (images.size() == 4) {
            int w = (int) ((DensityUtil.getScreenWidth(itemView.getContext()) - itemView.getResources().getDimension(R.dimen.x54))) / 2;
            LinearLayout layout = null;
            for (int i = 0; i < images.size(); i++) {
                Image image = images.get(i);
                LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(w, w);
                if (i == 0 || i == 2) {
                    lp1.rightMargin = (int) itemView.getResources().getDimension(R.dimen.x6);
                    layout = new LinearLayout(itemView.getContext());
                    layout.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    if (i == 2)
                        lp.topMargin = (int) context.getResources().getDimension(R.dimen.y6);
                    layout.setLayoutParams(lp);
                }
                ImageView iv = new ImageView(itemView.getContext());
                iv.setLayoutParams(lp1);
                Glide.with(itemView.getContext())
                        .load(StringUtils.getUrl(itemView.getContext(), image.getPath(), w, w, false, true))
                        .error(R.drawable.shape_gray_e8e8e8_background)
                        .placeholder(R.drawable.shape_gray_e8e8e8_background)
                        .into(iv);
                layout.addView(iv);
                showImg(iv, images, i);
                if (i == 1 || i == 3) {
                    ((LinearLayout) $(R.id.ll_img_root)).addView(layout);
                }
            }
        } else {
            int w = (int) ((DensityUtil.getScreenWidth(itemView.getContext()) - itemView.getResources().getDimension(R.dimen.x60))) / 3;
            LinearLayout layout = null;
            for (int i = 0; i < images.size(); i++) {
                Image image = images.get(i);
                LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(w, w);
                if (i == 0 || i == 3 || i == 6) {
                    layout = new LinearLayout(itemView.getContext());
                    layout.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    if (i == 3 || i == 6) {
                        lp.topMargin = (int) context.getResources().getDimension(R.dimen.y6);
                        layout.setLayoutParams(lp);
                    }
                }
                if (i % 3 != 2) {
                    lp1.rightMargin = (int) itemView.getResources().getDimension(R.dimen.x6);
                }
                ImageView iv = new ImageView(itemView.getContext());
                iv.setLayoutParams(lp1);
                Glide.with(itemView.getContext())
                        .load(StringUtils.getUrl(itemView.getContext(), image.getPath(), w, w, false, true))
                        .error(R.drawable.shape_gray_e8e8e8_background)
                        .placeholder(R.drawable.shape_gray_e8e8e8_background)
                        .into(iv);
                layout.addView(iv);
                showImg(iv, images, i);
                if (i % 3 == 2 || images.size() == i + 1) {
                    ((LinearLayout) $(R.id.ll_img_root)).addView(layout);
                }
            }
        }
    }

    private void showImg(ImageView iv, final ArrayList<Image> list, final int position) {
        iv.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                Intent intent = new Intent(itemView.getContext(), ImageBigSelectActivity.class);
                intent.putExtra(ImageBigSelectActivity.EXTRA_KEY_FILEBEAN, list);
                intent.putExtra(ImageBigSelectActivity.EXTRAS_KEY_FIRST_PHTOT_INDEX, position);
                // 以后可选择 有返回数据
                itemView.getContext().startActivity(intent);
            }
        });
    }

    private void showHongBao(boolean rt, final int coins, int surplus, final String id, final String icon, final int users) {
        if (coins > 0) {
            if (rt) {
                $(R.id.fl_hongbao_root).setBackgroundColor(ContextCompat.getColor(context, R.color.cyan_eefdff));
            } else {
                $(R.id.fl_hongbao_root).setBackgroundColor(ContextCompat.getColor(context, R.color.white));
            }
            setVisible(R.id.fl_hongbao_root, true);
            setText(R.id.tv_hongbao_coin, String.format(context.getString(R.string.label_hongbao_total_coin), coins));
            if (surplus > 0) {
                $(R.id.rl_hongbao_root).setBackgroundColor(ContextCompat.getColor(context, R.color.orange_f2cc2c));
                ((TextView) $(R.id.tv_hongbao_coin)).setTextColor(ContextCompat.getColor(context, R.color.orange_f2cc2c));
                setText(R.id.tv_left_num, "剩余：" + surplus + "个");
                setText(R.id.tv_desc, "转发即可领取红包");
            } else {
                $(R.id.rl_hongbao_root).setBackgroundColor(ContextCompat.getColor(context, R.color.gray_d7d7d7));
                ((TextView) $(R.id.tv_hongbao_coin)).setTextColor(ContextCompat.getColor(context, R.color.gray_d7d7d7));
                setText(R.id.tv_left_num, "已被抢完");
                setText(R.id.tv_desc, users + "领取了红包");
            }
            $(R.id.fl_hongbao_root).setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View v) {
                    HongBaoListActivity.startActivity(context, id, icon, coins, users);
                }
            });
        } else {
            $(R.id.fl_hongbao_root).setOnClickListener(null);
            setVisible(R.id.fl_hongbao_root, false);
        }
    }

    private void showMenu(final String name, final String content, final String id) {
        ArrayList<MenuItem> items = new ArrayList<>();
        MenuItem item = new MenuItem(0, itemView.getContext().getString(R.string.label_jubao));
        items.add(item);
        BottomMenuFragment fragment = new BottomMenuFragment();
        fragment.setShowTop(false);
        fragment.setMenuItems(items);
        fragment.setMenuType(BottomMenuFragment.TYPE_VERTICAL);
        fragment.setmClickListener(new BottomMenuFragment.MenuItemClickListener() {
            @Override
            public void OnMenuItemClick(int itemId) {
                if (itemId == 0) {
                    Intent intent = new Intent(itemView.getContext(), JuBaoActivity.class);
                    intent.putExtra(JuBaoActivity.EXTRA_NAME, name);
                    intent.putExtra(JuBaoActivity.EXTRA_CONTENT, content);
                    intent.putExtra(JuBaoActivity.UUID, id);
                    intent.putExtra(JuBaoActivity.EXTRA_TARGET, REPORT.DOC.toString());
                    itemView.getContext().startActivity(intent);
                }
            }
        });
        fragment.show(((BaseAppCompatActivity) itemView.getContext()).getSupportFragmentManager(), "CommentMenu");
    }

    public void createUserItem(final UserTopEntity entity, final int positionl) {
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
    }

    public void createDocItem(final DocResponse entity, final int positionl) {
        ImageView ivCover = $(R.id.iv_cover);
        TextView tvTitle = $(R.id.tv_title);
        ImageView ivUserAvatar = $(R.id.iv_user_avatar);
        TextView tvUserName = $(R.id.tv_user_name);
        View userRoot = $(R.id.ll_user_root);
        TextView tvTag1 = $(R.id.tv_tag_1);
        TextView tvTag2 = $(R.id.tv_tag_2);

        int w, h;
        w = h = getResources().getDimensionPixelSize(R.dimen.y180);
        Glide.with(context)
                .load(StringUtils.getUrl(context, entity.getImage(), w, h, false, true))
                .error(R.drawable.shape_gray_e8e8e8_background)
                .placeholder(R.drawable.shape_gray_e8e8e8_background)
                .bitmapTransform(new CropSquareTransformation(context))
                .into(ivCover);

        TextView tvExtraContent = $(R.id.tv_extra_content);
        tvExtraContent.setText("阅读 " + entity.getComments() + " · " + StringUtils.timeFormat(entity.getCreateTime()));
        tvTitle.setText(entity.getDesc());

        int size = getResources().getDimensionPixelSize(R.dimen.y32);
        ivUserAvatar.setVisibility(View.VISIBLE);
        Glide.with(context)
                .load(StringUtils.getUrl(context, entity.getIcon(), size, size, false, true))
                .error(R.drawable.bg_default_circle)
                .placeholder(R.drawable.bg_default_circle)
                .bitmapTransform(new CropCircleTransformation(context))
                .into(ivUserAvatar);
        tvUserName.setVisibility(View.VISIBLE);
        tvUserName.setText(entity.getCreateUserName());
        userRoot.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                ViewUtils.toPersonal(context, entity.getCreateUserId());
            }
        });

        if (entity.getTexts() != null && entity.getTexts().size() != 0) {
            //tag
            View[] tags = {tvTag1, tvTag2};
            if (entity.getTexts().size() > 1) {
                tags[0].setVisibility(View.VISIBLE);
                tags[1].setVisibility(View.VISIBLE);
            } else if (entity.getTexts().size() > 0) {
                tags[0].setVisibility(View.VISIBLE);
                tags[1].setVisibility(View.INVISIBLE);
            } else {
                tags[0].setVisibility(View.INVISIBLE);
                tags[1].setVisibility(View.INVISIBLE);
            }
            int size1 = tags.length > entity.getTexts().size() ? entity.getTexts().size() : tags.length;
            for (int i = 0; i < size1; i++) {
                TagUtils.setBackGround(entity.getTexts().get(i).getText(), tags[i]);
                tags[i].setOnClickListener(new NoDoubleClickListener() {
                    @Override
                    public void onNoDoubleClick(View v) {
                        //TODO 跳转标签页
                    }
                });
            }
        }
        itemView.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                Intent i = new Intent(itemView.getContext(), NewDocDetailActivity.class);
                i.putExtra("uuid", entity.getDocId());
                itemView.getContext().startActivity(i);
            }
        });
    }

    public void createBagItem(final ShowFolderEntity entity, final int position) {
        //cover
        int w, h;
        $(R.id.tv_play_num).setVisibility(View.VISIBLE);
        $(R.id.tv_danmu_num).setVisibility(View.VISIBLE);
//        setVisible(R.id.tv_play_num, true);
//        setVisible(R.id.tv_danmu_num, true);
        w = context.getResources().getDimensionPixelSize(R.dimen.x222);
        h = context.getResources().getDimensionPixelSize(R.dimen.y160);
        Glide.with(context)
                .load(StringUtils.getUrl(context, entity.getCover(), w, h, false, true))
                .error(R.drawable.shape_gray_e8e8e8_background)
                .placeholder(R.drawable.shape_gray_e8e8e8_background)
                .bitmapTransform(new CropTransformation(context, w, h), new RoundedCornersTransformation(context, getResources().getDimensionPixelSize(R.dimen.y8), 0))
                .into((ImageView) $(R.id.iv_cover));

        //title
        setText(R.id.tv_title, entity.getFolderName());
        if (entity.getTexts() != null && entity.getTexts().size() != 0) {
            //tag
            int[] tagsId = {R.id.tv_tag_1, R.id.tv_tag_2};
            $(tagsId[0]).setOnClickListener(null);
            $(tagsId[1]).setOnClickListener(null);
            if (entity.getTexts().size() > 1) {
                $(tagsId[0]).setVisibility(View.VISIBLE);
                $(tagsId[1]).setVisibility(View.VISIBLE);
            } else if (entity.getTexts().size() > 0) {
                $(tagsId[0]).setVisibility(View.VISIBLE);
                $(tagsId[1]).setVisibility(View.INVISIBLE);
            } else {
                $(tagsId[0]).setVisibility(View.INVISIBLE);
                $(tagsId[1]).setVisibility(View.INVISIBLE);
            }
            int size = tagsId.length > entity.getTexts().size() ? entity.getTexts().size() : tagsId.length;
            for (int i = 0; i < size; i++) {
                TagUtils.setBackGround(entity.getTexts().get(i), $(tagsId[i]));
                $(tagsId[i]).setOnClickListener(new NoDoubleClickListener() {
                    @Override
                    public void onNoDoubleClick(View v) {
                        //TODO 跳转标签页
                    }
                });
            }
        }
        //user
        int avatarSize = context.getResources().getDimensionPixelSize(R.dimen.y32);
        Glide.with(context)
                .load(StringUtils.getUrl(context, entity.getUserIcon(), avatarSize, avatarSize, false, true))
                .error(R.drawable.bg_default_circle)
                .placeholder(R.drawable.bg_default_circle)
                .bitmapTransform(new CropCircleTransformation(context))
                .into((ImageView) $(R.id.iv_user_avatar));
        setText(R.id.tv_user_name, entity.getCreateUserName());
        $(R.id.iv_user_avatar).setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                ViewUtils.toPersonal(context, entity.getCreateUser());
            }
        });
        $(R.id.tv_user_name).setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                ViewUtils.toPersonal(context, entity.getCreateUser());
            }
        });
        setText(R.id.tv_extra_content, StringUtils.timeFormat(entity.getTime()));

        //special
        if ("MOVIE".equals(entity.getType())) {
            int playNum = entity.getPlayNum();
            int danmuNum = entity.getBarrageNum();
            String stampTime = entity.getTimestamp();
            int coin = entity.getCoin();
            TextView tv_play = $(R.id.tv_play_num);
            tv_play.setText(String.valueOf(playNum));
            tv_play.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(context, R.drawable.ic_baglist_video_playtimes_gray), null, null, null);

            TextView tv_danMu = $(R.id.tv_danmu_num);
            tv_danMu.setVisibility(View.VISIBLE);
            tv_danMu.setText(String.valueOf(danmuNum));

            String coinStr = "免费";
            if (coin > 0) {
                coinStr = coin + "节操";
            }

            setText(R.id.tv_coin, coinStr);
            setText(R.id.tv_extra, stampTime);
            setVisible(R.id.iv_play, true);
            setVisible(R.id.tv_mark, false);
            setImageResource(R.id.iv_play, R.drawable.ic_baglist_video_play);
        } else if ("MUSIC".equals(entity.getType())) {
            int playNum = entity.getPlayNum();
            String stampTime = entity.getTimestamp();
            int coin = entity.getCoin();
            TextView tv_play = $(R.id.tv_play_num);
            tv_play.setText(String.valueOf(playNum));
            tv_play.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(context, R.drawable.ic_baglist_music_times), null, null, null);

            TextView tv_danMu = $(R.id.tv_danmu_num);
            tv_danMu.setVisibility(View.GONE);
            String coinStr = "免费";
            if (coin > 0) {
                coinStr = coin + "节操";
            }
            setText(R.id.tv_coin, coinStr);
            setText(R.id.tv_extra, stampTime);
            setVisible(R.id.iv_play, true);
            setVisible(R.id.tv_mark, false);
            setImageResource(R.id.iv_play, R.drawable.ic_baglist_music_play);
        } else if (FolderType.ZH.toString().equals(entity.getType())
                || FolderType.TJ.toString().equals(entity.getType())
                || FolderType.MH.toString().equals(entity.getType())
                || FolderType.XS.toString().equals(entity.getType())
                || FolderType.SP.toString().equals(entity.getType())
                || FolderType.YY.toString().equals(entity.getType())) {
            int fileNum = entity.getItems();
            int coin = entity.getCoin();
            String type = entity.getType();
            String coinStr = "免费";
            if (coin > 0) {
                coinStr = coin + "节操";
            }
            setText(R.id.tv_coin, coinStr);
            setText(R.id.tv_extra, fileNum + "项");

            setVisible(R.id.iv_play, false);

            setVisible(R.id.tv_mark, true);
            if (type.equals(FolderType.ZH.toString())) {
                setText(R.id.tv_mark, "综合");
                $(R.id.tv_mark).setBackgroundResource(R.drawable.shape_rect_zonghe);
            } else if (type.equals(FolderType.TJ.toString())) {
                setText(R.id.tv_mark, "图集");
                $(R.id.tv_mark).setBackgroundResource(R.drawable.shape_rect_tuji);
            } else if (type.equals(FolderType.MH.toString())) {
                setText(R.id.tv_mark, "漫画");
                $(R.id.tv_mark).setBackgroundResource(R.drawable.shape_rect_manhua);
            } else if (type.equals(FolderType.XS.toString())) {
                setText(R.id.tv_mark, "小说");
                $(R.id.tv_mark).setBackgroundResource(R.drawable.shape_rect_xiaoshuo);
            } else if (type.equals(FolderType.SP.toString())) {
                setText(R.id.tv_mark, "视频集");
                $(R.id.tv_mark).setBackgroundResource(R.drawable.shape_rect_shipin);
            } else if (type.equals(FolderType.YY.toString())) {
                setText(R.id.tv_mark, "音乐集");
                $(R.id.tv_mark).setBackgroundResource(R.drawable.shape_rect_yinyue);
            } else {
                setVisible(R.id.tv_mark, false);
            }
        }
        //history
        if (PreferenceUtils.getFeedLastItem(context, "follow_all").equals(entity.getFolderId())) {
            setVisible(R.id.fl_history_root, true);
            EventBus.getDefault().post(new RefreshListEvent("follow_all"));
        } else {
            setVisible(R.id.fl_history_root, false);
        }
    }

    public void createTopItem(final String entity, final int positionl) {

        setText(R.id.tv_context, entity);
        final String string = entity.toString();
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (string.contains("同学")) {
                    if (context instanceof AllSearchActivity) {
                        ((AllSearchActivity) itemView.getContext()).goItemLoad("user");
                    }
                } else if (string.contains("帖子")) {
                    ((AllSearchActivity) itemView.getContext()).goItemLoad("doc");
                } else if (string.contains("书包")) {
                    ((AllSearchActivity) itemView.getContext()).goItemLoad("folder");
                } else if (string.contains("动态")) {
                    ((AllSearchActivity) itemView.getContext()).goItemLoad("dynamic");
                }
            }
        });

    }
}
