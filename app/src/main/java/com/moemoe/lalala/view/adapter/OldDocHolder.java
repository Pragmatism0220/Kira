package com.moemoe.lalala.view.adapter;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.moemoe.lalala.R;
import com.moemoe.lalala.model.api.ApiService;
import com.moemoe.lalala.model.entity.BadgeEntity;
import com.moemoe.lalala.model.entity.DocResponse;
import com.moemoe.lalala.model.entity.DocTagEntity;
import com.moemoe.lalala.model.entity.Image;
import com.moemoe.lalala.model.entity.REPORT;
import com.moemoe.lalala.model.entity.SimpleUserEntity;
import com.moemoe.lalala.model.entity.TagLikeEntity;
import com.moemoe.lalala.model.entity.TagSendEntity;
import com.moemoe.lalala.utils.AlertDialogUtil;
import com.moemoe.lalala.utils.BitmapUtils;
import com.moemoe.lalala.utils.DensityUtil;
import com.moemoe.lalala.utils.DialogUtils;
import com.moemoe.lalala.utils.FileUtil;
import com.moemoe.lalala.utils.LevelSpan;
import com.moemoe.lalala.utils.NetworkUtils;
import com.moemoe.lalala.utils.NoDoubleClickListener;
import com.moemoe.lalala.utils.StringUtils;
import com.moemoe.lalala.utils.TagUtils;
import com.moemoe.lalala.utils.ToastUtils;
import com.moemoe.lalala.utils.ViewUtils;
import com.moemoe.lalala.utils.tag.TagControl;
import com.moemoe.lalala.view.activity.BaseAppCompatActivity;
import com.moemoe.lalala.view.activity.CommunityV1Activity;
import com.moemoe.lalala.view.activity.CreateCommentV2Activity;
import com.moemoe.lalala.view.activity.DepartmentV3Activity;
import com.moemoe.lalala.view.activity.FeedV3Activity;
import com.moemoe.lalala.view.activity.ImageBigSelectActivity;
import com.moemoe.lalala.view.activity.JuBaoActivity;
import com.moemoe.lalala.view.activity.NewDocDetailActivity;
import com.moemoe.lalala.view.activity.PersonalFavoriteDynamicActivity;
import com.moemoe.lalala.view.activity.PersonalV2Activity;
import com.moemoe.lalala.view.activity.WallBlockActivity;
import com.moemoe.lalala.view.activity.WenQuanActivity;
import com.moemoe.lalala.view.widget.adapter.ClickableViewHolder;
import com.moemoe.lalala.view.widget.netamenu.BottomMenuFragment;
import com.moemoe.lalala.view.widget.netamenu.MenuItem;
import com.moemoe.lalala.view.widget.view.DocLabelView;
import com.moemoe.lalala.view.widget.view.NewDocLabelAdapter;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by yi on 2017/7/21.
 */

@SuppressWarnings("deprecation")
public class OldDocHolder extends ClickableViewHolder {

    public DocLabelView docLabel;
    public NewDocLabelAdapter docLabelAdapter;

    public OldDocHolder(View itemView) {
        super(itemView);
        docLabel = $(R.id.dv_doc_label_root);
        docLabelAdapter = new NewDocLabelAdapter(itemView.getContext(), true);
    }

    public void createItem(final DocResponse entity, final int paposition) {
        //from
        setVisible(R.id.rl_from_top, false);


        if ("USER".equals(entity.getFrom())) {
            String timeShow = "关注人 · " + StringUtils.timeFormat(entity.getCreateTime());
            setText(R.id.tv_time, timeShow);
        } else {
            if (!TextUtils.isEmpty(entity.getFrom())) {
                String temp = "社团 " + entity.getFrom() + " · " + StringUtils.timeFormat(entity.getCreateTime());
                ForegroundColorSpan span = new ForegroundColorSpan(ContextCompat.getColor(context, TagUtils.getColor(entity.getFrom())));
                SpannableStringBuilder style = new SpannableStringBuilder(temp);
                style.setSpan(span, temp.indexOf(entity.getFrom()), temp.indexOf(entity.getFrom()) + entity.getFrom().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                setText(R.id.tv_time, style);
            } else {
                if (entity.getManager()) {
                    String manager = "部长 · " + StringUtils.timeFormat(entity.getCreateTime());
                    ForegroundColorSpan span = new ForegroundColorSpan(ContextCompat.getColor(context, TagUtils.getColor(entity.getFrom())));
                    SpannableStringBuilder style = new SpannableStringBuilder(manager);
                    style.setSpan(span, manager.indexOf("部长"), manager.indexOf("部长") + ("部长").length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    setText(R.id.tv_time, style);
                } else {
                    setText(R.id.tv_time, StringUtils.timeFormat(entity.getCreateTime()));
                }
            }
        }
        $(R.id.tv_time).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(entity.getTagId())) {
                    Intent intent = new Intent(context, CommunityV1Activity.class);
                    String id = entity.getTagId();
                    String text = entity.getFrom();
                    intent.putExtra("uuid", id + "");
                    intent.putExtra("title", text + "");
                    context.startActivity(intent);
                }
            }
        });

        //user top

        if (entity.getCreateUser() != null && entity.getCreateUser().isVip()) {
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
                showMenu(entity.getCreateUser().getUserName(), entity.getContent(), entity.getId());
            }
        });
        //content
        if (!TextUtils.isEmpty(entity.getTitle())) {
            setVisible(R.id.rl_title, true);
            setVisible(R.id.tv_title, true);
            setText(R.id.tv_title, entity.getTitle());
            if (entity.getExcellent()) {
                setVisible(R.id.tv_boutique, true);
            } else {
                setVisible(R.id.tv_boutique, false);
            }
        } else {
            setVisible(R.id.rl_title, false);
            setVisible(R.id.tv_title, false);
        }
        if (TextUtils.isEmpty(entity.getContent())) {
            setVisible(R.id.tv_content, false);
        } else {
            setVisible(R.id.tv_content, true);
            setText(R.id.tv_content, TagControl.getInstance().paresToSpann(itemView.getContext(), entity.getContent()));
        }
        ((TextView) $(R.id.tv_content)).setMovementMethod(LinkMovementMethod.getInstance());
        ((TextView) $(R.id.tv_content)).setMaxLines(10);
        ((TextView) $(R.id.tv_content)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(itemView.getContext(), NewDocDetailActivity.class);
                i.putExtra("uuid", entity.getId());
                if (context instanceof FeedV3Activity) {
                    i.putExtra("title", "社团");
                } else if (context instanceof CommunityV1Activity) {
                    String componentName = ((CommunityV1Activity) context).getCommunityName();
                    i.putExtra("title", componentName);
                } else if (context instanceof PersonalV2Activity) {
                    i.putExtra("title", "个人中心");
                }
                context.startActivity(i);
            }
        });
        //extra
        setVisible(R.id.ll_img_root, false);
        ((LinearLayout) $(R.id.ll_img_root)).removeAllViews();
        $(R.id.ll_img_root).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(itemView.getContext(), NewDocDetailActivity.class);
                i.putExtra("uuid", entity.getId());
                if (context instanceof FeedV3Activity) {
                    i.putExtra("title", "社团");
                } else if (context instanceof CommunityV1Activity) {
                    String componentName = ((CommunityV1Activity) context).getCommunityName();
                    i.putExtra("title", componentName);
                } else if (context instanceof PersonalV2Activity) {
                    i.putExtra("title", "个人中心");
                }
                context.startActivity(i);
            }
        });

        setVisible(R.id.ll_img_root, true);
        $(R.id.ll_img_root).setBackgroundColor(Color.WHITE);
        if (entity.getImages() != null && entity.getImages().size() > 0) {
            setImg(entity.getImages());
        } else {
            setVisible(R.id.ll_img_root, false);
        }
        //label
        if (docLabel != null && entity.getTags() != null) {
            docLabel.setDocLabelAdapter(docLabelAdapter);
            docLabelAdapter.setData(entity.getTags(), false);
            if (entity.getTags().size() > 0) {
                docLabel.setVisibility(View.VISIBLE);
            } else {
                docLabel.setVisibility(View.GONE);
            }
            docLabel.setItemClickListener(new DocLabelView.LabelItemClickListener() {
                @Override
                public void itemClick(int position) {
                    if (!NetworkUtils.checkNetworkAndShowError(context)) {
                        return;
                    }
                    if (entity != null) {
                        if (DialogUtils.checkLoginAndShowDlg(context)) {
                            final DocTagEntity tagBean = entity.getTags().get(position);
                            TagLikeEntity bean = new TagLikeEntity(entity.getId(), tagBean.getId());
                            ((BaseAppCompatActivity) context).createDialog();
                            if (context instanceof FeedV3Activity) {
                                ((FeedV3Activity) context).likeTag(tagBean.isLiked(), position, bean, paposition);
                            } else if (context instanceof WenQuanActivity) {
                                ((WenQuanActivity) context).likeTag(tagBean.isLiked(), position, bean, paposition);
                            } else if (context instanceof PersonalV2Activity) {
                                ((PersonalV2Activity) context).likeTag(tagBean.isLiked(), position, bean, paposition);
                            } else if (context instanceof CommunityV1Activity) {
                                ((CommunityV1Activity) context).likeTag(tagBean.isLiked(), position, bean, paposition);
                            }
                        }
                    }
                }
            });
        } else {
            if (docLabel != null) docLabel.setVisibility(View.GONE);
        }

        //tag
        if (entity.getTexts() != null && entity.getTexts().size() > 0) {
            setVisible(R.id.ll_tag_root, true);
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
            size = tagsId.length > entity.getTexts().size() ? entity.getTexts().size() : tagsId.length;
            for (int i = 0; i < size; i++) {
                TagUtils.setBackGround(entity.getTexts().get(i).getText(), $(tagsId[i]));
                $(tagsId[i]).setOnClickListener(new NoDoubleClickListener() {
                    @Override
                    public void onNoDoubleClick(View v) {
                        //TODO 跳转标签页
                    }
                });
            }
        } else {
            setVisible(R.id.ll_tag_root, false);
        }


        //bottom
        setVisible(R.id.rl_list_bottom_root, false);
        setVisible(R.id.rl_list_bottom_root_2, true);

        TextView forwardNum = $(R.id.tv_forward_num_2);
        forwardNum.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(context, R.drawable.btn_feed_tab_blue), null, null, null);
        forwardNum.setCompoundDrawablePadding((int) context.getResources().getDimension(R.dimen.x10));
        forwardNum.setTextColor(getResources().getColor(R.color.main_cyan));

        if (entity.getTagLikes() == 0) {
            forwardNum.setText("标签");
        } else {
            forwardNum.setText(StringUtils.getNumberInLengthLimit(entity.getTagLikes(), 3));
        }

        if (entity.getComments() == 0) {
            setText(R.id.tv_comment_num_2, "评论");
        } else {
            setText(R.id.tv_comment_num_2, StringUtils.getNumberInLengthLimit(entity.getComments(), 4));
        }


        $(R.id.tv_like_item).setSelected(false);

        if (entity.getThumbs() == 0) {
            setVisible(R.id.fl_tag_root_2, true);
            setVisible(R.id.rl_tag_root_2, false);
        } else {
            setVisible(R.id.fl_tag_root_2, false);
            setVisible(R.id.rl_tag_root_2, true);
            $(R.id.iv_like_item).setSelected(entity.getThumb());
            setText(R.id.tv_like_num, StringUtils.getNumberInLengthLimit(entity.getThumbs(), 4));
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
//                        ViewUtils.toPersonal(context, userEntity.getUserId());
                    }
                });
                ((LinearLayout) $(R.id.ll_like_user_root)).addView(likeUser);
            }
        }
        $(R.id.fl_forward_root_2).setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {

                final AlertDialogUtil alertDialogUtil = AlertDialogUtil.getInstance();
                alertDialogUtil.createDocEditDialog(itemView.getContext());
                alertDialogUtil.setOnClickListener(new AlertDialogUtil.OnClickListener() {
                    @Override
                    public void CancelOnClick() {
                        alertDialogUtil.dismissDialog();
                    }

                    @Override
                    public void ConfirmOnClick() {
                        if (DialogUtils.checkLoginAndShowDlg(itemView.getContext())) {
                            String content = alertDialogUtil.getEditTextContent();
                            if (!TextUtils.isEmpty(content)) {
                                TagSendEntity bean = new TagSendEntity(entity.getId(), content);
                                if (itemView.getContext() instanceof FeedV3Activity) {
                                    ((FeedV3Activity) context).createLabel(bean, paposition);
                                } else if (itemView.getContext() instanceof DepartmentV3Activity) {
                                    ((DepartmentV3Activity) context).createLabel(bean, paposition);
                                } else if (itemView.getContext() instanceof PersonalV2Activity) {
                                    ((PersonalV2Activity) context).createLabel(bean, paposition);
                                } else if (itemView.getContext() instanceof WenQuanActivity) {
                                    ((WenQuanActivity) context).createLabel(bean, paposition);
                                } else if (itemView.getContext() instanceof CommunityV1Activity) {
                                    ((CommunityV1Activity) context).createLabel(bean, paposition);
                                }
                                alertDialogUtil.dismissDialog();
                            } else {
                                ToastUtils.showShortToast(itemView.getContext(), "标签名不能哟！");
                            }
                        }
                    }
                });
                alertDialogUtil.showDialog();

            }
        });

        $(R.id.fl_comment_root_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent i = new Intent(itemView.getContext(), NewDocDetailActivity.class);
//                i.putExtra("uuid", entity.getId());
//                i.putExtra("isPinLun", true);
//                if (context instanceof FeedV3Activity) {
//                    i.putExtra("title", "社团");
//                } else if (context instanceof CommunityV1Activity) {
//                    String componentName = ((CommunityV1Activity) context).getCommunityName();
//                    i.putExtra("title", componentName);
//                } else if (context instanceof PersonalV2Activity) {
//                    i.putExtra("title", "个人中心");
//                }
//                context.startActivity(i);

                CreateCommentV2Activity.startActivity(context, entity.getId(), false, "", 0, entity.getId());
            }
        });
        $(R.id.fl_tag_root_2).setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                if (context instanceof WallBlockActivity) {
                    ((WallBlockActivity) context).likeDynamic(entity.getId(), entity.getThumb(), paposition);
                } else if (context instanceof PersonalFavoriteDynamicActivity) {
                    ((PersonalFavoriteDynamicActivity) context).likeDynamic(entity.getId(), entity.getThumb(), paposition);
                } else if (context instanceof FeedV3Activity) {
                    ((FeedV3Activity) context).likeDoc(entity.getId(), entity.getThumb(), paposition);
                } else if (context instanceof CommunityV1Activity) {
                    ((CommunityV1Activity) context).likeDoc(entity.getId(), entity.getThumb(), paposition);
                } else if (context instanceof WenQuanActivity) {
                    ((WenQuanActivity) context).likeDoc(entity.getId(), entity.getThumb(), paposition);
                } else if (context instanceof DepartmentV3Activity) {
                    ((DepartmentV3Activity) context).likeDoc(entity.getId(), entity.getThumb(), paposition);
                } else if (context instanceof PersonalV2Activity) {
                    ((PersonalV2Activity) context).likeDoc(entity.getId(), entity.getThumb(), paposition);
                }
            }
        });
        $(R.id.rl_tag_root_2).setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                if (context instanceof WallBlockActivity) {
                    ((WallBlockActivity) context).likeDynamic(entity.getId(), entity.getThumb(), paposition);
                } else if (context instanceof PersonalFavoriteDynamicActivity) {
                    ((PersonalFavoriteDynamicActivity) context).likeDynamic(entity.getId(), entity.getThumb(), paposition);
                } else if (context instanceof FeedV3Activity) {
                    ((FeedV3Activity) context).likeDoc(entity.getId(), entity.getThumb(), paposition);
                } else if (context instanceof CommunityV1Activity) {
                    ((CommunityV1Activity) context).likeDoc(entity.getId(), entity.getThumb(), paposition);
                } else if (context instanceof WenQuanActivity) {
                    ((WenQuanActivity) context).likeDoc(entity.getId(), entity.getThumb(), paposition);
                } else if (context instanceof DepartmentV3Activity) {
                    ((DepartmentV3Activity) context).likeDoc(entity.getId(), entity.getThumb(), paposition);
                } else if (context instanceof PersonalV2Activity) {
                    ((PersonalV2Activity) context).likeDoc(entity.getId(), entity.getThumb(), paposition);
                }
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
            if (FileUtil.isGif(image.getPath())) {
                ViewGroup.LayoutParams layoutParams = iv.getLayoutParams();
                layoutParams.width = wh[0];
                layoutParams.height = wh[1];
                iv.setLayoutParams(layoutParams);
                Glide.with(itemView.getContext())
                        .load(ApiService.URL_QINIU + image.getPath())
                        .asGif()
                        .override(wh[0], wh[1])
                        .dontAnimate()
                        .placeholder(R.drawable.shape_gray_e8e8e8_background)
                        .error(R.drawable.shape_gray_e8e8e8_background)
                        .into(iv);
            } else {
                Glide.with(itemView.getContext())
                        .load(StringUtils.getUrl(itemView.getContext(), image.getPath(), wh[0], wh[1], false, true))
                        .error(R.drawable.shape_gray_e8e8e8_background)
                        .placeholder(R.drawable.shape_gray_e8e8e8_background)
                        .into(iv);
            }
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
}
