package com.moemoe.lalala.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.moemoe.lalala.R;
import com.moemoe.lalala.app.MoeMoeApplication;
import com.moemoe.lalala.di.components.DaggerFeedFollowOther1Component;
import com.moemoe.lalala.di.modules.FeedFollowOther1Module;
import com.moemoe.lalala.event.FeedFollowOtherEvent;
import com.moemoe.lalala.model.entity.FeedFollowOther1Entity;
import com.moemoe.lalala.model.entity.FeedFollowType1Entity;
import com.moemoe.lalala.model.entity.FolderType;
import com.moemoe.lalala.model.entity.ShowFolderEntity;
import com.moemoe.lalala.presenter.FeedFollowOther1Contract;
import com.moemoe.lalala.presenter.FeedFollowOther1Presenter;
import com.moemoe.lalala.utils.DensityUtil;
import com.moemoe.lalala.utils.NoDoubleClickListener;
import com.moemoe.lalala.utils.StringUtils;
import com.moemoe.lalala.utils.TagUtils;
import com.moemoe.lalala.utils.ViewUtils;
import com.moemoe.lalala.view.activity.CommunityV1Activity;
import com.moemoe.lalala.view.activity.FeedShowAllActivity;
import com.moemoe.lalala.view.activity.FileMovieActivity;
import com.moemoe.lalala.view.activity.KiraMusicActivity;
import com.moemoe.lalala.view.activity.KiraVideoActivity;
import com.moemoe.lalala.view.activity.NewDocDetailActivity;
import com.moemoe.lalala.view.activity.NewFileCommonActivity;
import com.moemoe.lalala.view.activity.NewFileManHuaActivity;
import com.moemoe.lalala.view.activity.NewFileXiaoshuoActivity;
import com.moemoe.lalala.view.activity.NewFolderEditActivity;
import com.moemoe.lalala.view.widget.netamenu.BottomMenuFragment;
import com.moemoe.lalala.view.widget.netamenu.MenuItem;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.OnClick;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.CropSquareTransformation;
import jp.wasabeef.glide.transformations.CropTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 *
 * Created by yi on 2018/1/17.
 */

public class FeedFollowOther1Fragment extends BaseFragment implements FeedFollowOther1Contract.View {

    @Inject
    FeedFollowOther1Presenter mPresenter;
    private BottomMenuFragment fragment;
    private String id;
    private int num = 0;

    public static FeedFollowOther1Fragment newInstance(String id, boolean isAdmin) {
        FeedFollowOther1Fragment fragment = new FeedFollowOther1Fragment();
        Bundle bundle = new Bundle();
        bundle.putString("uuid", id);
        bundle.putBoolean("isAdmin", isAdmin);
        fragment.setArguments(bundle);
        return fragment;
    }
    public static FeedFollowOther1Fragment newInstance(String id, boolean isAdmin,boolean isChanged) {
        FeedFollowOther1Fragment fragment = new FeedFollowOther1Fragment();
        Bundle bundle = new Bundle();
        bundle.putString("uuid", id);
        bundle.putBoolean("isAdmin", isAdmin);
        bundle.putBoolean("isChanged", isChanged);
        fragment.setArguments(bundle);
        return fragment;
    }
    @Override
    protected int getLayoutId() {
        return R.layout.frag_feed_follow_other_item_1;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        DaggerFeedFollowOther1Component.builder()
                .feedFollowOther1Module(new FeedFollowOther1Module(this))
                .netComponent(MoeMoeApplication.getInstance().getNetComponent())
                .build()
                .inject(this);
        id = getArguments().getString("uuid");
        boolean isChanged = getArguments().getBoolean("isChanged");
        if (isChanged){
            $(R.id.iv_to_wen).setVisibility(View.GONE);
        }
        fragment = new BottomMenuFragment();
        initMenu();
        $(R.id.iv_to_wen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment.show(getChildFragmentManager(), "FeedBag");
            }
        });

        mPresenter.loadData(id);
    }

    private void initMenu() {
        ArrayList<MenuItem> items = new ArrayList<>();

        MenuItem item = new MenuItem(1, "综合");
        items.add(item);
        item = new MenuItem(2, "漫画");
        items.add(item);
        item = new MenuItem(3, "图集");
        items.add(item);
        item = new MenuItem(4, "小说");
        items.add(item);
        item = new MenuItem(5, "视频");
        items.add(item);
        item = new MenuItem(6, "音乐");
        items.add(item);

        fragment.setShowTop(false);
        fragment.setMenuItems(items);
        fragment.setMenuType(BottomMenuFragment.TYPE_VERTICAL);
        fragment.setmClickListener(new BottomMenuFragment.MenuItemClickListener() {
            @Override
            public void OnMenuItemClick(int itemId) {
                if (getContext() instanceof  CommunityV1Activity){
                    clickEvent("社团-社团-建书包");
                }
                String mFolderType = FolderType.ZH.toString();
                if (itemId == 1) {
                    mFolderType = FolderType.ZH.toString();
                } else if (itemId == 2) {
                    mFolderType = FolderType.MH.toString();
                } else if (itemId == 3) {
                    mFolderType = FolderType.TJ.toString();
                } else if (itemId == 4) {
                    mFolderType = FolderType.XS.toString();
                } else if (itemId == 5) {
                    mFolderType = FolderType.SP.toString();
                } else if (itemId == 6) {
                    mFolderType = FolderType.YY.toString();
                }
                NewFolderEditActivity.startActivity(getContext(), "create", mFolderType, null);
            }
        });
    }

    @OnClick({R.id.tv_video_show_all, R.id.tv_manga_show_all, R.id.tv_article_show_all, R.id.tv_music_show_all})
    public void onClick(View view) {
        if (getContext() instanceof CommunityV1Activity){
            clickEvent("社团-社团-书包列表点击");
        }
        switch (view.getId()) {
            case R.id.tv_video_show_all:
                FeedShowAllActivity.startActivity(getContext(), "视频", id, FolderType.MOVIE.toString(), FeedShowAllActivity.TYPE_GRID, 2, getArguments().getBoolean("isAdmin"));
                break;
            case R.id.tv_manga_show_all:
                FeedShowAllActivity.startActivity(getContext(), "图集漫画", id, FolderType.MH.toString(), FeedShowAllActivity.TYPE_GRID, 3, getArguments().getBoolean("isAdmin"));
                break;
            case R.id.tv_article_show_all:
                FeedShowAllActivity.startActivity(getContext(), "文章", id, FolderType.WZ.toString(), FeedShowAllActivity.TYPE_VERTICAL, 1, getArguments().getBoolean("isAdmin"));
                break;
            case R.id.tv_music_show_all:
                FeedShowAllActivity.startActivity(getContext(), "音乐", id, FolderType.MUSIC.toString(), FeedShowAllActivity.TYPE_VERTICAL, 1, getArguments().getBoolean("isAdmin"));
                break;
        }
    }

    @Override
    public void onFailure(int code, String msg) {

    }

    @Override
    public void onLoadDataSuccess(FeedFollowOther1Entity entities) {
        num = entities.getVideoNum() + entities.getMusicNum() + entities.getArticleNum() + entities.getFolderNum();

        if (num != 0) {
            EventBus.getDefault().post(new FeedFollowOtherEvent(num,id));
        }
        //video
        TextView mTvVideo = (TextView) $(R.id.tv_video_show_all);
        mTvVideo.setText("显示全部(" + entities.getVideoNum() + ")");
        int size = entities.getVideos().size() > 2 ? 2 : entities.getVideos().size();
        LinearLayout videoRoot = $(R.id.ll_video_item_root);
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                videoRoot.addView(getVideoView(entities.getVideos().get(i), i));
            }
        } else {
            $(R.id.fl_video_root).setVisibility(View.GONE);
            videoRoot.setVisibility(View.GONE);
        }


        //manga
        TextView mTvManga = (TextView) $(R.id.tv_manga_show_all);
        mTvManga.setText("显示全部(" + entities.getFolderNum() + ")");
        size = entities.getMangas().size() > 3 ? 3 : entities.getMangas().size();
        LinearLayout mangaRoot = $(R.id.ll_manga_item_root);
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                mangaRoot.addView(getMangaView(entities.getMangas().get(i), i));
            }
        } else {
            $(R.id.fl_manga_root).setVisibility(View.GONE);
            mangaRoot.setVisibility(View.GONE);
        }


        //article
        TextView mTvArticle = (TextView) $(R.id.tv_article_show_all);
        mTvArticle.setText("显示全部(" + entities.getArticleNum() + ")");
        size = entities.getArticles().size() > 10 ? 10 : entities.getArticles().size();
        LinearLayout articleRoot = $(R.id.ll_article_item_root);
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                articleRoot.addView(getArticleOrMusicView(entities.getArticles().get(i)));
            }
        } else {
            $(R.id.fl_article_root).setVisibility(View.GONE);
            articleRoot.setVisibility(View.GONE);
        }


        //music
        TextView mTvMusic = (TextView) $(R.id.tv_music_show_all);
        mTvMusic.setText("显示全部(" + entities.getMusicNum() + ")");
        size = entities.getMusics().size() > 5 ? 5 : entities.getMusics().size();
        LinearLayout musicRoot = $(R.id.ll_music_item_root);
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                musicRoot.addView(getArticleOrMusicView(entities.getMusics().get(i)));
            }
        } else {
            $(R.id.fl_music_root).setVisibility(View.GONE);
            musicRoot.setVisibility(View.GONE);
        }
    }


    private View getVideoView(final ShowFolderEntity entity, int position) {
        View v = View.inflate(getContext(), R.layout.item_feed_type_3_v3, null);
        ImageView ivCover = v.findViewById(R.id.iv_cover);
        ImageView ivUserAvatar = v.findViewById(R.id.iv_user_avatar);
        TextView tvUserName = v.findViewById(R.id.tv_user_name);
        View userRoot = v.findViewById(R.id.ll_user_root);
        TextView tvMark = v.findViewById(R.id.tv_mark);
        TextView tvPlayNum = v.findViewById(R.id.tv_play_num);
        TextView tvDanmuNum = v.findViewById(R.id.tv_danmu_num);
        TextView tvCoin = v.findViewById(R.id.tv_coin);
        TextView tvTitle = v.findViewById(R.id.tv_title);
        TextView tvTag1 = v.findViewById(R.id.tv_tag_1);
        TextView tvTag2 = v.findViewById(R.id.tv_tag_2);

        int w = (DensityUtil.getScreenWidth(getContext()) - (int) getResources().getDimension(R.dimen.x72)) / 2;
        int h = getResources().getDimensionPixelSize(R.dimen.y250);

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(w, h);
        LinearLayout.LayoutParams lp2;
        if (position == 1) {
            lp2 = new LinearLayout.LayoutParams(w , ViewGroup.LayoutParams.WRAP_CONTENT);
            lp2.setMargins((int) getResources().getDimension(R.dimen.y24),0,0,0);
            v.setPadding(0, 0, 0, 0);
        } else {
            lp2 = new LinearLayout.LayoutParams(w, ViewGroup.LayoutParams.WRAP_CONTENT);

            v.setPadding(0, 0, 0, 0);
        }
        v.setLayoutParams(lp2);
        ivCover.setLayoutParams(lp);
        Glide.with(getContext())
                .load(StringUtils.getUrl(getContext(), entity.getCover(), w, h, false, true))
                .error(R.drawable.shape_gray_e8e8e8_background)
                .placeholder(R.drawable.shape_gray_e8e8e8_background)
                .bitmapTransform(new CropTransformation(getContext(), w, h),
                        new RoundedCornersTransformation(getContext(), getResources().getDimensionPixelSize(R.dimen.y8), 0))
                .into(ivCover);

        int size = getResources().getDimensionPixelSize(R.dimen.y32);
        Glide.with(getContext())
                .load(StringUtils.getUrl(getContext(), entity.getUserIcon(), size, size, false, true))
                .error(R.drawable.bg_default_circle)
                .placeholder(R.drawable.bg_default_circle)
                .bitmapTransform(new CropCircleTransformation(getContext()))
                .into(ivUserAvatar);
        tvUserName.setText(entity.getCreateUserName());
        userRoot.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                ViewUtils.toPersonal(getContext(), entity.getCreateUser());
            }
        });

        tvMark.setText("视频");
        tvPlayNum.setText(String.valueOf(entity.getPlayNum()));
        tvDanmuNum.setText(String.valueOf(entity.getBarrageNum()));
        if (entity.getCoin() == 0) {
            tvCoin.setText("免费");
        } else {
            tvCoin.setText(entity.getCoin() + "节操");
        }
        tvTitle.setText(entity.getFolderName());

        //tag
        View[] tags = {tvTag1, tvTag2};
        if (entity.getTextsV2().size() > 1) {
            tags[0].setVisibility(View.VISIBLE);
            tags[1].setVisibility(View.VISIBLE);
        } else if (entity.getTextsV2().size() > 0) {
            tags[0].setVisibility(View.VISIBLE);
            tags[1].setVisibility(View.INVISIBLE);
        } else {
            tags[0].setVisibility(View.INVISIBLE);
            tags[1].setVisibility(View.INVISIBLE);
        }
        int size1 = tags.length > entity.getTextsV2().size() ? entity.getTextsV2().size() : tags.length;
        for (int i = 0; i < size1; i++) {
            TagUtils.setBackGround(entity.getTextsV2().get(i).getText(), tags[i]);
            tags[i].setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View v) {
                    //TODO 跳转标签页
                }
            });
        }
        v.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                if (getContext() instanceof  CommunityV1Activity){
                    clickEvent("社团-社团-书包列表点击");
                }
                KiraVideoActivity.startActivity(getContext(),entity.getUuid(),entity.getFolderId(),entity.getFolderName(),entity.getCover());
            }
        });
        return v;
    }

    private View getMangaView(final ShowFolderEntity entity, int position) {
        View v = View.inflate(getContext(), R.layout.item_feed_type_4_v3, null);
        ImageView ivCover = v.findViewById(R.id.iv_cover);
        TextView tvMark = v.findViewById(R.id.tv_mark);
        TextView tvCoin = v.findViewById(R.id.tv_coin);
        TextView tvBagNum = v.findViewById(R.id.tv_bag_num);
        TextView tvTitle = v.findViewById(R.id.tv_title);
        ImageView ivUserAvatar = v.findViewById(R.id.iv_user_avatar);
        TextView tvUserName = v.findViewById(R.id.tv_user_name);
        View userRoot = v.findViewById(R.id.ll_user_root);
        TextView tvTag1 = v.findViewById(R.id.tv_tag_1);
        TextView tvTag2 = v.findViewById(R.id.tv_tag_2);

        int w = (DensityUtil.getScreenWidth(getContext()) - (int) getResources().getDimension(R.dimen.x84)) / 3;
        int h = (int) getResources().getDimension(R.dimen.y290);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(w, h);
        LinearLayout.LayoutParams lp2;
        if (position == 1 || position == 2) {
            lp2 = new LinearLayout.LayoutParams(w + (int) getResources().getDimension(R.dimen.x18), ViewGroup.LayoutParams.WRAP_CONTENT);
            v.setPadding((int) getResources().getDimension(R.dimen.x18), 0, 0, 0);
        } else {
            lp2 = new LinearLayout.LayoutParams(w, ViewGroup.LayoutParams.WRAP_CONTENT);
            v.setPadding(0, 0, 0, 0);
        }
        v.setLayoutParams(lp2);
        ivCover.setLayoutParams(lp);
        Glide.with(getContext())
                .load(StringUtils.getUrl(getContext(), entity.getCover(), w, h, false, true))
                .placeholder(R.drawable.shape_gray_e8e8e8_background)
                .error(R.drawable.shape_gray_e8e8e8_background)
                .bitmapTransform(new CropTransformation(getContext(), w, h), new RoundedCornersTransformation(getContext(), (int) getResources().getDimension(R.dimen.y8), 0))
                .into(ivCover);

        if (entity.getType().equals(FolderType.ZH.toString())) {
            tvMark.setText("综合");
            tvMark.setBackgroundResource(R.drawable.shape_rect_zonghe);
        } else if (entity.getType().equals(FolderType.TJ.toString())) {
            tvMark.setText("图集");
            tvMark.setBackgroundResource(R.drawable.shape_rect_tuji);
        } else if (entity.getType().equals(FolderType.MH.toString())) {
            tvMark.setText("漫画");
            tvMark.setBackgroundResource(R.drawable.shape_rect_manhua);
        } else if (entity.getType().equals(FolderType.XS.toString())) {
            tvMark.setText("小说");
            tvMark.setBackgroundResource(R.drawable.shape_rect_xiaoshuo);
        } else if (entity.getType().equals(FolderType.WZ.toString())) {
            tvMark.setText("文章");
            tvMark.setBackgroundResource(R.drawable.shape_rect_zonghe);
        } else if (entity.getType().equals(FolderType.SP.toString())) {
            tvMark.setText("视频集");
            tvMark.setBackgroundResource(R.drawable.shape_rect_shipin);
        } else if (entity.getType().equals(FolderType.YY.toString())) {
            tvMark.setText("音乐集");
            tvMark.setBackgroundResource(R.drawable.shape_rect_shipin);
        } else {
            tvMark.setVisibility(View.GONE);
        }

        if (entity.getCoin() == 0) {
            tvCoin.setText("免费");
        } else {
            tvCoin.setText(entity.getCoin() + "节操");
        }
        tvBagNum.setText(entity.getItems() + "项");
        tvTitle.setText(entity.getFolderName());

        int size = getResources().getDimensionPixelSize(R.dimen.y32);
        ivUserAvatar.setVisibility(View.VISIBLE);
        Glide.with(getContext())
                .load(StringUtils.getUrl(getContext(), entity.getUserIcon(), size, size, false, true))
                .error(R.drawable.bg_default_circle)
                .placeholder(R.drawable.bg_default_circle)
                .bitmapTransform(new CropCircleTransformation(getContext()))
                .into(ivUserAvatar);
        tvUserName.setVisibility(View.VISIBLE);
        tvUserName.setText(entity.getCreateUserName());
        userRoot.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                ViewUtils.toPersonal(getContext(), entity.getCreateUser());
            }
        });

        //tag
        View[] tags = {tvTag1, tvTag2};
        if (entity.getTextsV2().size() > 1) {
            tags[0].setVisibility(View.VISIBLE);
            tags[1].setVisibility(View.VISIBLE);
        } else if (entity.getTextsV2().size() > 0) {
            tags[0].setVisibility(View.VISIBLE);
            tags[1].setVisibility(View.INVISIBLE);
        } else {
            tags[0].setVisibility(View.INVISIBLE);
            tags[1].setVisibility(View.INVISIBLE);
        }
        int size1 = tags.length > entity.getTextsV2().size() ? entity.getTextsV2().size() : tags.length;
        for (int i = 0; i < size1; i++) {
            TagUtils.setBackGround(entity.getTextsV2().get(i).getText(), tags[i]);
            tags[i].setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View v) {
                    //TODO 跳转标签页
                }
            });
        }

        v.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                if (getContext() instanceof  CommunityV1Activity){
                    clickEvent("社团-社团-书包列表点击");
                }
                if(entity.getType().equals(FolderType.ZH.toString())){
                    NewFileCommonActivity.startActivity(getContext(),FolderType.ZH.toString(),entity.getFolderId(),entity.getCreateUser());
                }else if(entity.getType().equals(FolderType.TJ.toString())){
                    NewFileCommonActivity.startActivity(getContext(),FolderType.TJ.toString(),entity.getFolderId(),entity.getCreateUser());
                }else if(entity.getType().equals(FolderType.MH.toString())){
                    NewFileManHuaActivity.startActivity(getContext(),FolderType.MH.toString(),entity.getFolderId(),entity.getCreateUser());
                }else if(entity.getType().equals(FolderType.XS.toString())){
                    NewFileXiaoshuoActivity.startActivity(getContext(),FolderType.XS.toString(),entity.getFolderId(),entity.getCreateUser());
                }else if(entity.getType().equals(FolderType.YY.toString())){
                    FileMovieActivity.startActivity(getContext(),FolderType.YY.toString(),entity.getFolderId(),entity.getCreateUser());
                }else if(entity.getType().equals(FolderType.SP.toString())){
                    FileMovieActivity.startActivity(getContext(),FolderType.SP.toString(),entity.getFolderId(),entity.getCreateUser());
                }else if("MOVIE".equals(entity.getType())){
                    KiraVideoActivity.startActivity(getContext(),entity.getUuid(),entity.getFolderId(),entity.getFolderName(),entity.getCover());
                }else if("MUSIC".equals(entity.getType())){
                    KiraMusicActivity.startActivity(getContext(),entity.getUuid(),entity.getFolderId(),entity.getFolderName(),entity.getCover());
                }
            }
        });
        return v;
    }

    private View getArticleOrMusicView(final FeedFollowType1Entity entity) {
        View v;
        if ("DOC".equals(entity.getType())) {
            v = View.inflate(getContext(), R.layout.item_feed_type_1_v3, null);
        } else {
            v = View.inflate(getContext(), R.layout.item_feed_type_2_v3, null);
        }
        View viewStep = v.findViewById(R.id.view_step);
        ImageView ivCover = v.findViewById(R.id.iv_cover);
        TextView tvTitle = v.findViewById(R.id.tv_title);
        View userRoot = v.findViewById(R.id.ll_user_root);
        ImageView ivUserAvatar = v.findViewById(R.id.iv_user_avatar);
        TextView tvUserName = v.findViewById(R.id.tv_user_name);
        TextView tvExtraContent = v.findViewById(R.id.tv_extra_content);
        TextView tvTag1 = v.findViewById(R.id.tv_tag_1);
        TextView tvTag2 = v.findViewById(R.id.tv_tag_2);

        v.setPadding(0, 0, 0, 0);
        viewStep.setVisibility(View.GONE);

        int w, h;
        if ("DOC".equals(entity.getType())) {
            w = h = getResources().getDimensionPixelSize(R.dimen.y180);
            Glide.with(getContext())
                    .load(StringUtils.getUrl(getContext(), entity.getCover(), w, h, false, true))
                    .error(R.drawable.shape_gray_e8e8e8_background)
                    .placeholder(R.drawable.shape_gray_e8e8e8_background)
                    .bitmapTransform(new CropSquareTransformation(getContext()))
                    .into(ivCover);
        } else {
            w = getResources().getDimensionPixelSize(R.dimen.x222);
            h = getResources().getDimensionPixelSize(R.dimen.y160);
            Glide.with(getContext())
                    .load(StringUtils.getUrl(getContext(), entity.getCover(), w, h, false, true))
                    .error(R.drawable.shape_gray_e8e8e8_background)
                    .placeholder(R.drawable.shape_gray_e8e8e8_background)
                    .bitmapTransform(new CropTransformation(getContext(), w, h)
                            ,new RoundedCornersTransformation(getContext(),getResources().getDimensionPixelSize(R.dimen.y8),0))
                    .into(ivCover);
        }

        tvTitle.setText(entity.getTitle());

        int size = getResources().getDimensionPixelSize(R.dimen.y32);
        Glide.with(getContext())
                .load(StringUtils.getUrl(getContext(), entity.getUserAvatar(), size, size, false, true))
                .error(R.drawable.bg_default_circle)
                .placeholder(R.drawable.bg_default_circle)
                .bitmapTransform(new CropCircleTransformation(getContext()))
                .into(ivUserAvatar);
        tvUserName.setText(entity.getUserName());
        userRoot.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                ViewUtils.toPersonal(getContext(), entity.getUserId());
            }
        });

        try {
            //extra content
            JSONObject json = new JSONObject(entity.getExtra());
            if ("DOC".equals(entity.getType())) {
                int readNum = json.getInt("readNum");
                tvExtraContent.setText("阅读 " + readNum + " · " + StringUtils.timeFormat(entity.getCreateTime()));
            } else {
                tvExtraContent.setText(StringUtils.timeFormat(entity.getCreateTime()));
            }

            //special
            if ("MUSIC".equals(entity.getType())) {
                int playNum = json.getInt("playNum");
//                int stampTime = json.getInt("stampTime");
                String stampTime = json.getString("stampTime");
                int coin = json.getInt("coin");
                TextView tvPlay = v.findViewById(R.id.tv_play_num);
                tvPlay.setText(String.valueOf(playNum));
                tvPlay.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getContext(), R.drawable.ic_baglist_music_times), null, null, null);

                TextView tvDanMu = v.findViewById(R.id.tv_danmu_num);
                tvDanMu.setVisibility(View.GONE);

                TextView tvCoin = v.findViewById(R.id.tv_coin);
                if (coin == 0) {
                    tvCoin.setText("免费");
                } else {
                    tvCoin.setText(coin + "节操");
                }

                TextView tvExtra = v.findViewById(R.id.tv_extra);
//                tvExtra.setText(StringUtils.getMinute(stampTime));
                tvExtra.setText(stampTime);

                ImageView ivPlay = v.findViewById(R.id.iv_play);
                ivPlay.setVisibility(View.VISIBLE);
                ivPlay.setImageResource(R.drawable.ic_baglist_music_play);
                v.findViewById(R.id.tv_mark).setVisibility(View.GONE);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //tag
        View[] tags = {tvTag1, tvTag2};
        if (entity.getTags().size() > 1) {
            tags[0].setVisibility(View.VISIBLE);
            tags[1].setVisibility(View.VISIBLE);
        } else if (entity.getTags().size() > 0) {
            tags[0].setVisibility(View.VISIBLE);
            tags[1].setVisibility(View.INVISIBLE);
        } else {
            tags[0].setVisibility(View.INVISIBLE);
            tags[1].setVisibility(View.INVISIBLE);
        }
        int size1 = tags.length > entity.getTags().size() ? entity.getTags().size() : tags.length;
        for (int i = 0; i < size1; i++) {
            TagUtils.setBackGround(entity.getTags().get(i).getText(), tags[i]);
            tags[i].setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View v) {
                    //TODO 跳转标签页
                }
            });
        }

        v.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                if (getContext() instanceof CommunityV1Activity){
                    clickEvent("社团-社团-书包列表点击");
                }
                if("DOC".equals(entity.getType())){
                    Intent i = new Intent(getContext(), NewDocDetailActivity.class);
                    i.putExtra("uuid", entity.getId());
                    getContext().startActivity(i);
                }else {
                    KiraMusicActivity.startActivity(getContext(),entity.getFolderId(),entity.getId(),entity.getTitle(),entity.getCover());
                }
            }
        });
        return v;
    }
}
