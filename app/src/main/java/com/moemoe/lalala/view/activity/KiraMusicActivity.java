package com.moemoe.lalala.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.util.FileDownloadUtils;
import com.moemoe.lalala.R;
import com.moemoe.lalala.app.MoeMoeApplication;
import com.moemoe.lalala.di.components.DaggerKiraVideoComponent;
import com.moemoe.lalala.di.modules.KiraVideoModule;
import com.moemoe.lalala.event.FavMusicEvent;
import com.moemoe.lalala.model.entity.DownloadEntity;
import com.moemoe.lalala.model.entity.FolderType;
import com.moemoe.lalala.model.entity.KiraVideoEntity;
import com.moemoe.lalala.model.entity.ShareFolderEntity;
import com.moemoe.lalala.model.entity.ShowFolderEntity;
import com.moemoe.lalala.model.entity.StreamFileEntity;
import com.moemoe.lalala.model.entity.UserFollowTagEntity;
import com.moemoe.lalala.netamusic.data.model.Music;
import com.moemoe.lalala.netamusic.data.model.MusicListType;
import com.moemoe.lalala.netamusic.player.AudioPlayer;
import com.moemoe.lalala.presenter.KiraVideoContract;
import com.moemoe.lalala.presenter.KiraVideoPresenter;
import com.moemoe.lalala.utils.AlertDialogUtil;
import com.moemoe.lalala.utils.DensityUtil;
import com.moemoe.lalala.utils.ErrorCodeUtils;
import com.moemoe.lalala.utils.FileUtil;
import com.moemoe.lalala.utils.GreenDaoManager;
import com.moemoe.lalala.utils.NoDoubleClickListener;
import com.moemoe.lalala.utils.StorageUtils;
import com.moemoe.lalala.utils.StringUtils;
import com.moemoe.lalala.utils.TagUtils;
import com.moemoe.lalala.utils.TasksManager;
import com.moemoe.lalala.utils.ToastUtils;
import com.moemoe.lalala.utils.ViewUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.ColorFilterTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.CropTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * 音乐详情界面
 * Created by yi on 2018/2/6.
 */

public class KiraMusicActivity extends BaseAppCompatActivity implements KiraVideoContract.View, AppBarLayout.OnOffsetChangedListener {

    @BindView(R.id.appbar)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout mTitleView;
    @BindView(R.id.tv_toolbar_title)
    TextView mTvTitle;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.iv_menu_list)
    ImageView mMenuList;
    @BindView(R.id.iv_cover)
    ImageView mIvCover;
    @BindView(R.id.iv_bg)
    ImageView mIvBg;
    @BindView(R.id.tv_title)
    TextView mTvTitle2;
    @BindView(R.id.tv_play_num)
    TextView mTvPlayNum;
    @BindView(R.id.tv_time)
    TextView mTvTime;
    @BindView(R.id.tv_add_to_list)
    TextView mTvAddToList;
    @BindView(R.id.ll_buy_root)
    View mBuyRoot;
    @BindView(R.id.tv_buy_coin)
    TextView mTvCoin;
    @BindView(R.id.tv_fav)
    TextView mTvFav;

    @Inject
    KiraVideoPresenter mPresenter;

    private String folderId;
    private String fileId;
    private int mCurPage = 1;
    private String mFolderName;
    private String mCover;
    private boolean isChanged;
    private boolean isFav;
    private String mUserId;

    public static void startActivity(Context context, String folderId, String fileId, String folderName, String cover) {
        Intent i = new Intent(context, KiraMusicActivity.class);
        i.putExtra("folderId", folderId);
        i.putExtra("fileId", fileId);
        i.putExtra("folderName", folderName);
        i.putExtra("cover", cover);
        context.startActivity(i);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.ac_music_file;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        DaggerKiraVideoComponent.builder()
                .kiraVideoModule(new KiraVideoModule(this))
                .netComponent(MoeMoeApplication.getInstance().getNetComponent())
                .build()
                .inject(this);
        mFolderName = getIntent().getStringExtra("folderName");
        folderId = getIntent().getStringExtra("folderId");
        fileId = getIntent().getStringExtra("fileId");
        mCover = getIntent().getStringExtra("cover");
        int w = DensityUtil.getScreenWidth(this);
        int h = getResources().getDimensionPixelSize(R.dimen.y420);
        Glide.with(this)
                .load(StringUtils.getUrl(this, mCover, w, h, false, true))
                .error(R.drawable.shape_gray_e8e8e8_background)
                .placeholder(R.drawable.shape_gray_e8e8e8_background)
                .bitmapTransform(new CropTransformation(this, w, h)
                        , new BlurTransformation(this, 10, 4)
                        , new ColorFilterTransformation(this, ContextCompat.getColor(this, R.color.alpha_20)))
                .into(mIvBg);
        int size = getResources().getDimensionPixelSize(R.dimen.y164);
        Glide.with(this)
                .load(StringUtils.getUrl(this, mCover, size, size, false, true))
                .error(R.drawable.bg_default_circle)
                .placeholder(R.drawable.bg_default_circle)
                .bitmapTransform(new CropCircleTransformation(this))
                .into(mIvCover);
        mPresenter.loadVideoInfo(FolderType.YY.toString(), folderId, fileId);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        if (mPresenter != null) mPresenter.release();
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @OnClick({R.id.tv_download, R.id.tv_fav, R.id.tv_forward, R.id.tv_to_bag, R.id.tv_add_to_list, R.id.ll_buy_root, R.id.rl_top_root})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_download:
                if (mVideo != null) downloadRaw();
                break;
            case R.id.tv_fav:
                mPresenter.favMovie(FolderType.YY.toString(), isFav, folderId, fileId);
                break;
            case R.id.tv_forward:
                if (mVideo != null) {
                    rtMusic();
                }
                break;
            case R.id.tv_to_bag:
                if (!TextUtils.isEmpty(mUserId)) {
                    Intent i2 = new Intent(this, NewBagActivity.class);
                    i2.putExtra("uuid", mUserId);
                    startActivity(i2);
                }
                break;
            case R.id.tv_add_to_list:
                if (mVideo != null && mVideo.isBuy()) {
                    addToList();
                }

                break;
            case R.id.rl_top_root:
                if (mVideo != null && mVideo.isBuy()) {
                    addToList();
                    Intent i = new Intent(this, MusicDetailActivityV2.class);
                    startActivity(i);
                }
                break;
            case R.id.ll_buy_root:
                if (mVideo != null) {
                    String coin;
                    if ("FOLDER".equals(mVideo.getBuyType())) {
                        coin = "需要支付" + mVideo.getCoin() + "节操，购买文件夹";
                    } else {
                        coin = "需要支付" + mVideo.getCoin() + "节操，购买此音乐";
                    }
                    final AlertDialogUtil alertDialogUtil = AlertDialogUtil.getInstance();
                    alertDialogUtil.createNormalDialog(this, coin);
                    alertDialogUtil.setOnClickListener(new AlertDialogUtil.OnClickListener() {
                        @Override
                        public void CancelOnClick() {
                            alertDialogUtil.dismissDialog();
                        }

                        @Override
                        public void ConfirmOnClick() {
                            mPresenter.buyFile(folderId, FolderType.MUSIC.toString(), fileId);
                            alertDialogUtil.dismissDialog();
                        }
                    });
                    alertDialogUtil.showDialog();
                }
                break;
        }
    }

    private void addToList() {
        if (mVideo != null) {
            Music music = new Music();
            final File file = new File(StorageUtils.getMusicRootPath(), mVideo.getFileName());
            if (!FileUtil.isExists(file.getAbsolutePath())) {
                music.setType(Music.Type.ONLINE);
                music.setPath(StringUtils.getUrl(mVideo.getPath()));
            } else {
                music.setType(Music.Type.LOCAL);
                music.setPath(file.getAbsolutePath());
            }
            music.setUserId(mVideo.getUser().getUserId());
            music.setUserSex(mVideo.getUser().getSex());
            music.setUpdateTime(mVideo.getUpdateTime());
            music.setFav(mVideo.isFav());
            music.setFileId(fileId);
            music.setTitle(mVideo.getFileName());
            music.setFileName(mVideo.getFileName());
            music.setListType(MusicListType.TYPE_NORMAL);
            music.setCoverPath(mVideo.getCover());
            if (mVideo.getTexts().size() > 0) {
                music.setTag1(mVideo.getTexts().get(0).getText());
                if (mVideo.getTexts().size() > 1) {
                    music.setTag2(mVideo.getTexts().get(1).getText());
                }
            }
            if (mVideo.getAttr().has("timestamp")) {
                music.setDuration(mVideo.getAttr().get("timestamp").getAsLong());
            }
            music.setArtist(mVideo.getUser().getUserName());
            try {
                JSONObject object = new JSONObject();
                object.put("folderId", folderId);
                object.put("folderName", mFolderName);
                object.put("cover", mCover);
                music.setAttr(object.toString());
                int pos;
                if (AudioPlayer.get().isPlaying()) {
                    pos = AudioPlayer.get().add(music);
                } else {
                    pos = AudioPlayer.get().addAndPlay(music);
                }
                if (pos < 0) {
                    ToastUtils.showShortToast(this, "已存在列表中");
                } else {
                    ToastUtils.showShortToast(this, "已添加到后台播放器，点击系统通知栏进入");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private KiraVideoEntity mVideo;

    public void rtMusic() {
        ShareFolderEntity entity = new ShareFolderEntity();
        entity.setFolderCover(mCover);
        entity.setFolderId(folderId);
        entity.setFolderName(mVideo.getFileName());
        ArrayList<String> tag = new ArrayList<>();
        for (UserFollowTagEntity tagEntity : mVideo.getTexts()) {
            tag.add(tagEntity.getText());
        }
        entity.setFolderTags(tag);
        entity.setFolderType(FolderType.MUSIC.toString());
        entity.setUpdateTime(mVideo.getUpdateTime());
        entity.setCreateUser(mVideo.getUser());
        CreateForwardActivity.startActivity(this, CreateForwardActivity.TYPE_MUSIC, entity, fileId);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAppBarLayout.addOnOffsetChangedListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAppBarLayout.removeOnOffsetChangedListener(this);
    }

    @Override
    protected void initToolbar(Bundle savedInstanceState) {
        mToolbar.setNavigationOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                onBackPressed();
            }
        });

        mMenuList.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                //TODO 显示菜单
            }
        });
    }

    @Override
    protected void initListeners() {

    }

    @Override
    protected void initData() {

    }

    @Override
    public void onFailure(int code, String msg) {
        ErrorCodeUtils.showErrorMsgByCode(this, code, msg);
    }

    @Override
    public void onLoadVideoInfoSuccess(final KiraVideoEntity entity) {
        mVideo = entity;
        mTvTitle.setText(entity.getFileName());
        mTvTitle2.setText(entity.getFileName());
        if (entity.isBuy()) {
            mTvAddToList.setVisibility(View.VISIBLE);
        } else {
            mBuyRoot.setVisibility(View.VISIBLE);
            String coin;
            if ("FOLDER".equals(entity.getBuyType())) {
                coin = "需要支付" + entity.getCoin() + "节操，购买文件夹";
            } else {
                coin = "需要支付" + entity.getCoin() + "节操，购买此项歌曲";
            }
            mTvCoin.setText(coin);
        }

        mTvPlayNum.setText(entity.getPlayNum() + "");

        //tag
        int[] tagsIds = {R.id.tv_tag_1, R.id.tv_tag_2};
        if (entity.getTexts().size() > 1) {
            $(tagsIds[0]).setVisibility(View.VISIBLE);
            $(tagsIds[1]).setVisibility(View.VISIBLE);
        } else if (entity.getTexts().size() > 0) {
            $(tagsIds[0]).setVisibility(View.VISIBLE);
            $(tagsIds[1]).setVisibility(View.INVISIBLE);
        } else {
            $(tagsIds[0]).setVisibility(View.INVISIBLE);
            $(tagsIds[1]).setVisibility(View.INVISIBLE);
        }
        int size1 = tagsIds.length > entity.getTexts().size() ? entity.getTexts().size() : tagsIds.length;
        for (int i = 0; i < size1; i++) {
            TagUtils.setBackGround(entity.getTexts().get(i).getText(), $(tagsIds[i]));
            $(tagsIds[i]).setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View v) {
                    //TODO 跳转标签页
                }
            });
        }
        Glide.with(this)
                .load(StringUtils.getUrl(this, entity.getUser().getHeadPath(), getResources().getDimensionPixelSize(R.dimen.y80), getResources().getDimensionPixelSize(R.dimen.y80), false, true))
                .error(R.drawable.bg_default_circle)
                .placeholder(R.drawable.bg_default_circle)
                .bitmapTransform(new CropCircleTransformation(this))
                .into((ImageView) $(R.id.iv_avatar));
        $(R.id.iv_avatar).setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                ViewUtils.toPersonal(KiraMusicActivity.this, entity.getUser().getUserId());
            }
        });
        ((TextView) $(R.id.tv_user_name)).setText(entity.getUser().getUserName());

        //video
        int size = entity.getItemList().size() > 2 ? 2 : entity.getItemList().size();
        LinearLayout videoRoot = $(R.id.ll_video_root);
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                videoRoot.addView(getVideoView(entity.getItemList().get(i), i));
            }
        } else {
            videoRoot.setVisibility(View.GONE);
        }

        $(R.id.tv_refresh).setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                mPresenter.refreshRecommend(mFolderName, mCurPage, folderId);
            }
        });

        isFav = entity.isFav();
        mTvFav.setSelected(entity.isFav());
        mPresenter.refreshRecommend(mFolderName, mCurPage, folderId);
    }

    private View getVideoView(final StreamFileEntity entity, int position) {
        View v = View.inflate(this, R.layout.item_feed_type_3_v3, null);
        ImageView ivCover = v.findViewById(R.id.iv_cover);
        ImageView ivUserAvatar = v.findViewById(R.id.iv_user_avatar);
        TextView tvUserName = v.findViewById(R.id.tv_user_name);
        View userRoot = v.findViewById(R.id.ll_user_root);
        TextView tvMark = v.findViewById(R.id.tv_mark);
        TextView tvPlayNum = v.findViewById(R.id.tv_play_num);
        v.findViewById(R.id.tv_danmu_num).setVisibility(View.GONE);
        TextView tvCoin = v.findViewById(R.id.tv_coin);
        TextView tvTitle = v.findViewById(R.id.tv_title);
        TextView tvTag1 = v.findViewById(R.id.tv_tag_1);
        TextView tvTag2 = v.findViewById(R.id.tv_tag_2);

        int w = (DensityUtil.getScreenWidth(this) - (int) getResources().getDimension(R.dimen.x72)) / 2;
        int h = getResources().getDimensionPixelSize(R.dimen.y250);

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(w, h);
        LinearLayout.LayoutParams lp2;
        if (position == 1) {
            lp2 = new LinearLayout.LayoutParams(w + (int) getResources().getDimension(R.dimen.x24), ViewGroup.LayoutParams.WRAP_CONTENT);
            v.setPadding((int) getResources().getDimension(R.dimen.x24), 0, 0, 0);
        } else {
            lp2 = new LinearLayout.LayoutParams(w, ViewGroup.LayoutParams.WRAP_CONTENT);
            v.setPadding(0, 0, 0, 0);
        }
        v.setLayoutParams(lp2);
        ivCover.setLayoutParams(lp);
        Glide.with(this)
                .load(StringUtils.getUrl(this, entity.getCover(), w, h, false, true))
                .error(R.drawable.shape_gray_e8e8e8_background)
                .placeholder(R.drawable.shape_gray_e8e8e8_background)
                .bitmapTransform(new CropTransformation(this, w, h),
                        new RoundedCornersTransformation(this, getResources().getDimensionPixelSize(R.dimen.y8), 0))
                .into(ivCover);

        int size = getResources().getDimensionPixelSize(R.dimen.y32);
        Glide.with(this)
                .load(StringUtils.getUrl(this, entity.getIcon(), size, size, false, true))
                .error(R.drawable.bg_default_circle)
                .placeholder(R.drawable.bg_default_circle)
                .bitmapTransform(new CropCircleTransformation(this))
                .into(ivUserAvatar);
        tvUserName.setText(entity.getUserName());
        mUserId = entity.getUserId();
        userRoot.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                ViewUtils.toPersonal(KiraMusicActivity.this, entity.getUserId());
            }
        });

        tvMark.setText("音乐");
        tvPlayNum.setText(String.valueOf(entity.getPlayNum()));
        tvPlayNum.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(this, R.drawable.ic_baglist_music_times_white), null, null, null);
        if (entity.getCoin() == 0) {
            tvCoin.setText("免费");
        } else {
            tvCoin.setText(entity.getCoin() + "节操");
        }
        tvTitle.setText(entity.getFileName());

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
        v.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                KiraMusicActivity.startActivity(KiraMusicActivity.this, folderId, entity.getId(), entity.getFileName(), entity.getCover());
            }
        });
        return v;
    }

    @Override
    public void onLoadVideoInfoFail() {
        showToast("加载音乐信息失败");
        finish();
    }

    @Override
    public void onReFreshSuccess(ArrayList<ShowFolderEntity> entities) {
        addBottomList(entities);
    }

    @Override
    public void onLoadDanmakuSuccess(String uri) {

    }

    @Override
    public void onSendDanmakuSuccess() {

    }

    @Override
    public void onFavMovieSuccess() {
        isFav = !isFav;
        mTvFav.setSelected(!mTvFav.isSelected());
        if (isFav) {
            showToast("收藏成功");
        } else {
            showToast("取消收藏成功");
        }
    }

    @Override
    public void onBuyFileSuccess() {
        showToast("购买成功");
        mVideo.setBuy(true);
        mTvAddToList.setVisibility(View.VISIBLE);
        mBuyRoot.setVisibility(View.GONE);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void favChange(FavMusicEvent event) {
        if (mVideo != null && mVideo.getId().equals(event.getId())) {
            isFav = event.isFav();
            mTvFav.setSelected(isFav);
        }
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        int temp = getResources().getDimensionPixelSize(R.dimen.y267) - getResources().getDimensionPixelSize(R.dimen.status_bar_height);
        float percent = (float) Math.abs(verticalOffset) / temp;
        if (percent > 0.4) {
            if (!isChanged) {
                mToolbar.setNavigationIcon(R.drawable.btn_back_blue_normal_unity);
                mMenuList.setImageResource(R.drawable.btn_menu_normal);
                isChanged = true;
            }
            mToolbar.setAlpha(percent);
            mMenuList.setAlpha(percent);
            mTvTitle.setAlpha(percent);
            mTvTitle.setVisibility(View.VISIBLE);
        } else {
            if (isChanged) {
                mToolbar.setNavigationIcon(R.drawable.btn_back_white_normal);
                mMenuList.setImageResource(R.drawable.btn_menu_white_normal);
                isChanged = false;
            }
            mToolbar.setAlpha(1 - percent);
            mMenuList.setAlpha(1 - percent);
            mTvTitle.setAlpha(1 - percent);
            mTvTitle.setVisibility(View.GONE);
        }
        mTvTitle.setAlpha(percent);
    }

    private void addBottomList(ArrayList<ShowFolderEntity> entities) {
        LinearLayout recommendRoot = findViewById(R.id.ll_recommend_root);
        recommendRoot.removeAllViews();
        if (entities.size() > 0) {
            recommendRoot.setVisibility(View.VISIBLE);
            for (int n = 0; n < entities.size(); n++) {
                final ShowFolderEntity item = entities.get(n);
                View v = LayoutInflater.from(this).inflate(R.layout.item_feed_type_2_v3, null);

                ImageView ivCover = v.findViewById(R.id.iv_cover);
                TextView tvMark = v.findViewById(R.id.tv_mark);
                TextView tvCoin = v.findViewById(R.id.tv_coin);
                TextView tvExtra = v.findViewById(R.id.tv_extra);
                ImageView ivPlay = v.findViewById(R.id.iv_play);
                TextView tvTitle = v.findViewById(R.id.tv_title);
                ImageView ivAvatar = v.findViewById(R.id.iv_user_avatar);
                TextView tvUserName = v.findViewById(R.id.tv_user_name);
                TextView tvExtraContent = v.findViewById(R.id.tv_extra_content);
                TextView tvTag1 = v.findViewById(R.id.tv_tag_1);
                TextView tvTag2 = v.findViewById(R.id.tv_tag_2);
                TextView tvPlayNum = v.findViewById(R.id.tv_play_num);
                TextView tvDanmuNum = v.findViewById(R.id.tv_danmu_num);

                int w = getResources().getDimensionPixelSize(R.dimen.x222);
                int h = getResources().getDimensionPixelSize(R.dimen.y160);
                Glide.with(this)
                        .load(StringUtils.getUrl(this, item.getCover(), w, h, false, true))
                        .error(R.drawable.shape_gray_e8e8e8_background)
                        .placeholder(R.drawable.shape_gray_e8e8e8_background)
                        .bitmapTransform(new CropTransformation(this, w, h)
                                , new RoundedCornersTransformation(this, getResources().getDimensionPixelSize(R.dimen.y8), 0))
                        .into(ivCover);

                tvExtra.setText(item.getItems() + "项");
                tvExtraContent.setText(StringUtils.timeFormat(item.getTime()));

                if (item.getType().equals(FolderType.ZH.toString())) {
                    tvMark.setVisibility(View.VISIBLE);
                    ivPlay.setVisibility(View.GONE);
                    tvMark.setText("综合");
                    tvMark.setBackgroundResource(R.drawable.shape_rect_zonghe);
                } else if (item.getType().equals(FolderType.TJ.toString())) {
                    tvMark.setVisibility(View.VISIBLE);
                    ivPlay.setVisibility(View.GONE);
                    tvMark.setText("图集");
                    tvMark.setBackgroundResource(R.drawable.shape_rect_tuji);
                } else if (item.getType().equals(FolderType.MH.toString())) {
                    tvMark.setVisibility(View.VISIBLE);
                    ivPlay.setVisibility(View.GONE);
                    tvMark.setText("漫画");
                    tvMark.setBackgroundResource(R.drawable.shape_rect_manhua);
                } else if (item.getType().equals(FolderType.XS.toString())) {
                    tvMark.setVisibility(View.VISIBLE);
                    ivPlay.setVisibility(View.GONE);
                    tvMark.setText("小说");
                    tvMark.setBackgroundResource(R.drawable.shape_rect_xiaoshuo);
                } else if (item.getType().equals(FolderType.WZ.toString())) {
                    tvMark.setVisibility(View.VISIBLE);
                    ivPlay.setVisibility(View.GONE);
                    tvMark.setText("文章");
                    tvMark.setBackgroundResource(R.drawable.shape_rect_zonghe);
                } else if (item.getType().equals(FolderType.SP.toString())) {
                    tvMark.setVisibility(View.VISIBLE);
                    ivPlay.setVisibility(View.GONE);
                    tvMark.setText("视频集");
                    tvMark.setBackgroundResource(R.drawable.shape_rect_shipin);
                } else if (item.getType().equals(FolderType.YY.toString())) {
                    tvMark.setVisibility(View.VISIBLE);
                    ivPlay.setVisibility(View.GONE);
                    tvMark.setText("音乐集");
                    tvMark.setBackgroundResource(R.drawable.shape_rect_yinyue);
                } else if ("MOVIE".equals(item.getType())) {
                    tvMark.setVisibility(View.GONE);
                    ivPlay.setVisibility(View.VISIBLE);
                    ivPlay.setImageResource(R.drawable.ic_baglist_video_play);
                    tvExtra.setText(item.getTimestamp());
                    ivPlay.setVisibility(View.VISIBLE);
                    tvPlayNum.setText(String.valueOf(item.getPlayNum()));
                    tvPlayNum.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(this, R.drawable.ic_baglist_video_playtimes_gray), null, null, null);
                    tvDanmuNum.setVisibility(View.VISIBLE);
                    tvDanmuNum.setText(String.valueOf(item.getBarrageNum()));
                } else if ("MUSIC".equals(item.getType())) {
                    tvMark.setVisibility(View.GONE);
                    ivPlay.setImageResource(R.drawable.ic_baglist_music_play);
                    tvExtra.setText(item.getTimestamp());
                    tvDanmuNum.setVisibility(View.GONE);
                    tvPlayNum.setText(String.valueOf(item.getPlayNum()));
                    tvPlayNum.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(this, R.drawable.ic_baglist_music_times), null, null, null);
                }

                tvTitle.setText(item.getFolderName());

                //tag
                View[] tagsId = {tvTag1, tvTag2};
                tvTag1.setOnClickListener(null);
                tvTag2.setOnClickListener(null);
                if (item.getTextsV2().size() > 1) {
                    tvTag1.setVisibility(View.VISIBLE);
                    tvTag2.setVisibility(View.VISIBLE);
                } else if (item.getTextsV2().size() > 0) {
                    tvTag1.setVisibility(View.VISIBLE);
                    tvTag2.setVisibility(View.INVISIBLE);
                } else {
                    tvTag1.setVisibility(View.INVISIBLE);
                    tvTag2.setVisibility(View.INVISIBLE);
                }
                int size = tagsId.length > item.getTextsV2().size() ? item.getTextsV2().size() : tagsId.length;
                for (int i = 0; i < size; i++) {
                    TagUtils.setBackGround(item.getTextsV2().get(i).getText(), tagsId[i]);
                    tagsId[i].setOnClickListener(new NoDoubleClickListener() {
                        @Override
                        public void onNoDoubleClick(View v) {
                            //TODO 跳转标签页
                        }
                    });
                }

                //user
                int avatarSize = getResources().getDimensionPixelSize(R.dimen.y32);
                Glide.with(this)
                        .load(StringUtils.getUrl(this, item.getUserIcon(), avatarSize, avatarSize, false, true))
                        .error(R.drawable.bg_default_circle)
                        .placeholder(R.drawable.bg_default_circle)
                        .bitmapTransform(new CropCircleTransformation(this))
                        .into(ivAvatar);
                tvUserName.setText(item.getCreateUserName());
                ivAvatar.setOnClickListener(new NoDoubleClickListener() {
                    @Override
                    public void onNoDoubleClick(View v) {
                        ViewUtils.toPersonal(KiraMusicActivity.this, item.getCreateUser());
                    }
                });
                tvUserName.setOnClickListener(new NoDoubleClickListener() {
                    @Override
                    public void onNoDoubleClick(View v) {
                        ViewUtils.toPersonal(KiraMusicActivity.this, item.getCreateUser());
                    }
                });

                if (item.getCoin() > 0) {
                    tvCoin.setText(item.getCoin() + "节操");
                } else {
                    tvCoin.setText("免费");
                }

                v.setOnClickListener(new NoDoubleClickListener() {
                    @Override
                    public void onNoDoubleClick(View v) {
                        if (item.getType().equals(FolderType.ZH.toString())) {
                            NewFileCommonActivity.startActivity(KiraMusicActivity.this, FolderType.ZH.toString(), item.getFolderId(), item.getCreateUser());
                        } else if (item.getType().equals(FolderType.TJ.toString())) {
                            NewFileCommonActivity.startActivity(KiraMusicActivity.this, FolderType.TJ.toString(), item.getFolderId(), item.getCreateUser());
                        } else if (item.getType().equals(FolderType.MH.toString())) {
                            NewFileManHuaActivity.startActivity(KiraMusicActivity.this, FolderType.MH.toString(), item.getFolderId(), item.getCreateUser());
                        } else if (item.getType().equals(FolderType.XS.toString())) {
                            NewFileXiaoshuoActivity.startActivity(KiraMusicActivity.this, FolderType.XS.toString(), item.getFolderId(), item.getCreateUser());
                        } else if (item.getType().equals(FolderType.YY.toString())) {
                            FileMovieActivity.startActivity(KiraMusicActivity.this, FolderType.YY.toString(), item.getFolderId(), item.getCreateUser());
                        } else if (item.getType().equals(FolderType.SP.toString())) {
                            FileMovieActivity.startActivity(KiraMusicActivity.this, FolderType.SP.toString(), item.getFolderId(), item.getCreateUser());
                        } else if ("MOVIE".equals(item.getType())) {
                            KiraVideoActivity.startActivity(KiraMusicActivity.this, item.getUuid(), item.getFolderId(), item.getFolderName(), item.getCover());
                        } else if ("MUSIC".equals(item.getType())) {
                            KiraMusicActivity.startActivity(KiraMusicActivity.this, item.getUuid(), item.getFolderId(), item.getFolderName(), item.getCover());
                        }
                    }
                });

                recommendRoot.addView(v);
            }
        } else {
            recommendRoot.setVisibility(View.GONE);
        }
    }

    public void downloadRaw() {
        final File file = new File(StorageUtils.getMusicRootPath(), mVideo.getFileName());
        if (!FileUtil.isExists(file.getAbsolutePath())) {
            DownloadEntity entity1 = TasksManager.getImpl().getById(FileDownloadUtils.generateId(mVideo.getPath(), StorageUtils.getMusicRootPath() + mVideo.getFileName()));
            if (entity1 != null) {
                showToast("已存在相同文件");
                return;
            }

            BaseDownloadTask task = FileDownloader.getImpl().create(StringUtils.getUrl(mVideo.getPath()))
                    .setPath(StorageUtils.getMusicRootPath() + mVideo.getFileName())
                    .setCallbackProgressTimes(100)
                    .setListener(new FileDownloadListener() {
                        @Override
                        protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        }

                        @Override
                        protected void started(BaseDownloadTask task) {
                            showToast("开始下载");
                        }

                        @Override
                        protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {

                        }

                        @Override
                        protected void completed(BaseDownloadTask task) {

                        }

                        @Override
                        protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {

                        }

                        @Override
                        protected void error(BaseDownloadTask task, Throwable e) {

                        }

                        @Override
                        protected void warn(BaseDownloadTask task) {

                        }
                    });
            int taskId = task.getId();
            DownloadEntity downloadEntity = new DownloadEntity();
            downloadEntity.setUrl(mVideo.getPath());
            downloadEntity.setDirPath(StorageUtils.getMusicRootPath());
            downloadEntity.setFileName(mVideo.getFileName());
            downloadEntity.setPath(StorageUtils.getMusicRootPath() + mVideo.getFileName());
            downloadEntity.setType(FolderType.MUSIC.toString());
            downloadEntity.setFileId(mVideo.getId());
            downloadEntity.setId((long) taskId);
            try {
                JSONObject object = new JSONObject();
                object.put("folderId", folderId);
                object.put("folderName", mFolderName);
                object.put("cover", mCover);
                downloadEntity.setAttr(object.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            GreenDaoManager.getInstance().getSession().getDownloadEntityDao().insertOrReplace(downloadEntity);
            TasksManager.getImpl().add(downloadEntity);
            task.start();
        } else {
            showToast(getString(R.string.msg_register_to_music_success, file.getAbsolutePath()));
        }
    }
}
