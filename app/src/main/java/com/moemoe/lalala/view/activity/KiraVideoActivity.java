package com.moemoe.lalala.view.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.moemoe.lalala.event.RefreshListEvent;
import com.moemoe.lalala.model.entity.DanmakuSend;
import com.moemoe.lalala.model.entity.DownloadEntity;
import com.moemoe.lalala.model.entity.FolderType;
import com.moemoe.lalala.model.entity.KiraVideoEntity;
import com.moemoe.lalala.model.entity.ShareFolderEntity;
import com.moemoe.lalala.model.entity.ShowFolderEntity;
import com.moemoe.lalala.model.entity.UserFollowTagEntity;
import com.moemoe.lalala.presenter.KiraVideoContract;
import com.moemoe.lalala.presenter.KiraVideoPresenter;
import com.moemoe.lalala.utils.AlertDialogUtil;
import com.moemoe.lalala.utils.AndroidBug5497Workaround;
import com.moemoe.lalala.utils.DensityUtil;
import com.moemoe.lalala.utils.ErrorCodeUtils;
import com.moemoe.lalala.utils.FileUtil;
import com.moemoe.lalala.utils.GreenDaoManager;
import com.moemoe.lalala.utils.NoDoubleClickListener;
import com.moemoe.lalala.utils.StorageUtils;
import com.moemoe.lalala.utils.StringUtils;
import com.moemoe.lalala.utils.TasksManager;
import com.moemoe.lalala.view.adapter.TabFragmentPagerAdapter;
import com.moemoe.lalala.view.fragment.BaseFragment;
import com.moemoe.lalala.view.fragment.VideoCommentFragment;
import com.moemoe.lalala.view.fragment.VideoContentFragment;
import com.moemoe.lalala.view.widget.video.KiraVideoListener;
import com.moemoe.lalala.view.widget.video.KiraVideoPlayerV2;
import com.moemoe.lalala.view.widget.view.KiraTabLayout;
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
import com.shuyu.gsyvideoplayer.listener.GSYVideoProgressListener;
import com.shuyu.gsyvideoplayer.listener.LockClickListener;
import com.shuyu.gsyvideoplayer.utils.CommonUtil;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by yi on 2018/2/2.
 */

public class KiraVideoActivity extends BaseAppCompatActivity implements KiraVideoPlayerV2.DanmuPlayerHolder, AppBarLayout.OnOffsetChangedListener, KiraVideoContract.View {

    @BindView(R.id.tab_layout)
    KiraTabLayout mTabLayout;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
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
    @BindView(R.id.fl_cover_root)
    View mCoverRoot;
    @BindView(R.id.ll_video_root)
    View mVideoRoot;
    @BindView(R.id.video_player)
    KiraVideoPlayerV2 mPlayer;
    @BindView(R.id.iv_avatar)
    ImageView mIvAvatar;
    @BindView(R.id.et_danmaku)
    EditText mEtDanmu;
    @BindView(R.id.iv_cover)
    ImageView mIvCover;
    @BindView(R.id.ll_buy_root)
    View mBuyRoot;
    @BindView(R.id.tv_send_danmaku)
    View mTvSendDanmu;
    @BindView(R.id.ll_bottom_root)
    LinearLayout mBottomRoot;

    @Inject
    KiraVideoPresenter mPresenter;

    private TabFragmentPagerAdapter mAdapter;
    private VideoContentFragment videoContentFragment;

    private OrientationUtils mOrientationUtils;
    private boolean isPlay = false;
    private boolean isPause = false;
    private boolean firstPlay = true;
    private String folderId;
    private String fileId;
    private int mCurPage = 1;
    private String mFolderName;
    private String mCover;

    private KiraVideoEntity mVideo;
    private String url;

    @Override
    protected int getLayoutId() {
        return R.layout.ac_video_player;
    }

    public static void startActivity(Context context, String folderId, String fileId, String folderName, String cover) {
        Intent i = new Intent(context, KiraVideoActivity.class);
        i.putExtra("folderId", folderId);
        i.putExtra("fileId", fileId);
        i.putExtra("folderName", folderName);
        i.putExtra("cover", cover);
        context.startActivity(i);
    }

    public static void startActivity(Context context, String folderId, String fileId, String folderName, String cover, String url) {
        Intent i = new Intent(context, KiraVideoActivity.class);
        i.putExtra("folderId", folderId);
        i.putExtra("fileId", fileId);
        i.putExtra("folderName", folderName);
        i.putExtra("cover", cover);
        i.putExtra("path", url);
        context.startActivity(i);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

        DaggerKiraVideoComponent.builder()
                .kiraVideoModule(new KiraVideoModule(this))
                .netComponent(MoeMoeApplication.getInstance().getNetComponent())
                .build()
                .inject(this);
        AndroidBug5497Workaround.assistActivity(this);
        mOrientationUtils = new OrientationUtils(this, mPlayer);
        mOrientationUtils.setEnable(false);

        mFolderName = getIntent().getStringExtra("folderName");
        folderId = getIntent().getStringExtra("folderId");
        fileId = getIntent().getStringExtra("fileId");
        mCover = getIntent().getStringExtra("cover");
        url = getIntent().getStringExtra("path");
        int w = DensityUtil.getScreenWidth(this);
        int h = getResources().getDimensionPixelSize(R.dimen.y420);
        Glide.with(this)
                .load(StringUtils.getUrl(this, mCover, w, h, false, true))
                .error(R.drawable.shape_gray_e8e8e8_background)
                .placeholder(R.drawable.shape_gray_e8e8e8_background)
                .into(mIvCover);
        mPresenter.loadVideoInfo(FolderType.SP.toString(), folderId, fileId);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getCurPlay().onVideoResume();
        mAppBarLayout.addOnOffsetChangedListener(this);
        EventBus.getDefault().register(this);
        isPause = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        getCurPlay().onVideoPause();
        mAppBarLayout.removeOnOffsetChangedListener(this);
        EventBus.getDefault().unregister(this);
        isPause = true;
    }

    @Override
    protected void initToolbar(Bundle savedInstanceState) {
        mToolbar.setNavigationOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                onBackPressed();
            }
        });

        mPlayer.getMenu().setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                //TODO 显示菜单
            }
        });
        mMenuList.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                //TODO 显示菜单
            }
        });
        mMenuList.setVisibility(View.GONE);

    }

    @Override
    protected void initListeners() {

    }

    @Override
    protected void initData() {

    }

    @Override
    public void onSendDanmu(DanmakuSend send) {
        send.folderId = folderId;
        send.videoId = fileId;
        mEtDanmu.setText("");
        mPresenter.sendDanmaku(send);
    }

    @Override
    public void onSavePlayHistory(int position) {
        //TODO 保存播放进度
    }

    @Override
    public void onComplete() {

    }

    @OnClick({R.id.tv_send_danmaku, R.id.iv_play, R.id.tv_buy, R.id.tv_toolbar_title})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_send_danmaku:
                String danmu = mEtDanmu.getText().toString();
                if (!TextUtils.isEmpty(danmu)) {
                    mPlayer.sendDanmu(danmu);
                }
                break;
            case R.id.tv_toolbar_title:
            case R.id.iv_play:
                playVideo();
                break;
            case R.id.tv_buy:
                if (mVideo != null) {
                    String coin;
                    if ("FOLDER".equals(mVideo.getBuyType())) {
                        coin = "需要支付" + mVideo.getCoin() + "节操，购买文件夹";
                    } else {
                        coin = "需要支付" + mVideo.getCoin() + "节操，购买此视频";
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
                            mPresenter.buyFile(folderId, FolderType.MOVIE.toString(), fileId);
                            alertDialogUtil.dismissDialog();
                        }
                    });
                    alertDialogUtil.showDialog();
                }

                break;
        }
    }

    private void playVideo() {
        mToolbar.setVisibility(View.GONE);
        mCoverRoot.setVisibility(View.GONE);
        mVideoRoot.setVisibility(View.VISIBLE);

        AppBarLayout.LayoutParams lp = (AppBarLayout.LayoutParams) mTitleView.getLayoutParams();
        lp.setScrollFlags(0);
        mAppBarLayout.setExpanded(true);
        mTitleView.setLayoutParams(lp);

        CoordinatorLayout.LayoutParams lp2 = (CoordinatorLayout.LayoutParams) mBottomRoot.getLayoutParams();
        lp2.topMargin = getResources().getDimensionPixelSize(R.dimen.y504);
        lp2.setBehavior(null);
        mBottomRoot.setLayoutParams(lp2);
//        play();
//        mTvSendDanmu.setEnabled(true);
        if (mVideo.isBuy()) {
            play();
            mTvSendDanmu.setEnabled(true);
        } else {
            mBuyRoot.setVisibility(View.VISIBLE);
            mTvSendDanmu.setEnabled(false);
        }
    }

    private void play() {
        mBuyRoot.setVisibility(View.GONE);
        mPlayer.setVisibility(View.VISIBLE);
        CommonUtil.hideSupportActionBar(this, true, true);
        mPlayer.getStartButton().performClick();
        mPresenter.loadDanmaku(fileId);


//        final File file = new File(StorageUtils.getVideoRootPath(), mFolderName);
//        if (FileUtil.isExists(file.getAbsolutePath())) {
//            DownloadEntity entity1 = TasksManager.getImpl().getById(FileDownloadUtils.generateId(url,
//                    StorageUtils.getVideoRootPath() + mFolderName));
//            mPlayer.setUpDanmu(file.getPath());
//        }
    }

    private boolean isChanged = false;

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


    /**
     * 初始化播放器
     */
    private void setupPlayer() {
        File file = new File(StorageUtils.getVideoRootPath(), mFolderName);
        GSYVideoOptionBuilder gsyVideoOption = new GSYVideoOptionBuilder();
        gsyVideoOption.setIsTouchWiget(true)
                .setRotateViewAuto(false)
                .setLockLand(false)
                .setShowFullAnimation(false)
                .setNeedLockFull(true)
                .setSeekRatio(1)
                .setUrl(StringUtils.getUrl(mVideo.getPath()))
//                .setCachePath()
//                .setUrl(url)
                .setCacheWithPlay(false)
//                .setVideoTitle(mFolderName)
                .setVideoTitle(mVideo.getFileName())
                .setStandardVideoAllCallBack(new KiraVideoListener() {
                    @Override
                    public void onPrepared(String url, Object... objects) {
                        super.onPrepared(url, objects);
                        //开始播放了才能旋转和全屏
                        mOrientationUtils.setEnable(true);
                        isPlay = true;
                    }

                    @Override
                    public void onEnterFullscreen(String url, Object... objects) {
                        super.onEnterFullscreen(url, objects);
                    }

                    @Override
                    public void onAutoComplete(String url, Object... objects) {
                        super.onAutoComplete(url, objects);
                    }

                    @Override
                    public void onClickStartError(String url, Object... objects) {
                        super.onClickStartError(url, objects);
                    }

                    @Override
                    public void onQuitFullscreen(String url, Object... objects) {
                        super.onQuitFullscreen(url, objects);
                        if (mOrientationUtils != null) {
                            mOrientationUtils.backToProtVideo();
                        }
                    }
                })
                .setLockClickListener(new LockClickListener() {
                    @Override
                    public void onClick(View view, boolean lock) {
                        if (mOrientationUtils != null) {
                            //配合下方的onConfigurationChanged
                            mOrientationUtils.setEnable(!lock);
                        }
                    }
                })
                .setGSYVideoProgressListener(new GSYVideoProgressListener() {
                    @Override
                    public void onProgress(int progress, int secProgress, int currentPosition, int duration) {
                    }
                })
                .build(mPlayer);

        mPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //直接横屏
                mOrientationUtils.resolveByClick();
                //第一个true是否需要隐藏actionbar，第二个true是否需要隐藏statusbar
                mPlayer.startWindowFullscreen(KiraVideoActivity.this, true, true);
            }
        });

        mPlayer.getBackButton().setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onFailure(int code, String msg) {
        ErrorCodeUtils.showErrorMsgByCode(this, code, msg);
    }

    @Override
    public void onLoadVideoInfoSuccess(KiraVideoEntity entity) {
        mVideo = entity;
        ((TextView) findViewById(R.id.tv_buy)).setText("支付" + mVideo.getCoin() + "节操");
        int size = getResources().getDimensionPixelSize(R.dimen.y50);
        Glide.with(this)
                .load(StringUtils.getUrl(this, mVideo.getUser().getHeadPath(), size, size, false, true))
                .error(R.drawable.bg_default_circle)
                .placeholder(R.drawable.bg_default_circle)
                .bitmapTransform(new CropCircleTransformation(this))
                .into(mIvAvatar);
        ArrayList<String> title = new ArrayList<>();
        title.add("简介");
        title.add("评论" + entity.getCommentNum());
        ArrayList<BaseFragment> fragments = new ArrayList<>();
        fragments.add(videoContentFragment = VideoContentFragment.newInstance(folderId, mVideo));
        fragments.add(VideoCommentFragment.newInstance(fileId, entity.getUser().getUserId()));
        mAdapter = new TabFragmentPagerAdapter(getSupportFragmentManager(), fragments, title);
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setViewPager(mViewPager);
        setupPlayer();
        playVideo();
    }

    @Override
    public void onLoadVideoInfoFail() {
        showToast("加载视频信息失败");
//        setupPlayer();
//        playVideo();
        finish();
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void refreshRecommend(RefreshListEvent event) {
        if (fileId.equals(event.getTypeId())) {
            mPresenter.refreshRecommend(mFolderName, mCurPage, folderId);
        }
    }

    @Override
    public void onReFreshSuccess(ArrayList<ShowFolderEntity> entities) {
        if (videoContentFragment != null) {
            videoContentFragment.bindRefresh(entities);
        }
    }

    @Override
    public void onLoadDanmakuSuccess(String uri) {
        mPlayer.setUpDanmu(uri);
    }

    @Override
    public void onSendDanmakuSuccess() {

    }

    public void rtMovie() {
        ShareFolderEntity entity = new ShareFolderEntity();
        entity.setFolderCover(mCover);
        entity.setFolderId(folderId);
        entity.setFolderName(mVideo.getFileName());
        ArrayList<String> tag = new ArrayList<>();
        for (UserFollowTagEntity tagEntity : mVideo.getTexts()) {
            tag.add(tagEntity.getText());
        }
        entity.setFolderTags(tag);
        entity.setFolderType(FolderType.MOVIE.toString());
        entity.setUpdateTime(mVideo.getUpdateTime());
        entity.setCreateUser(mVideo.getUser());
        CreateForwardActivity.startActivity(this, CreateForwardActivity.TYPE_MOVIE, entity, fileId);
    }

    public void favMovie(boolean isFav) {
        mPresenter.favMovie(FolderType.SP.toString(), isFav, folderId, fileId);
    }

    @Override
    public void onFavMovieSuccess() {
        if (videoContentFragment != null) videoContentFragment.changeFav();
    }

    @Override
    public void onBuyFileSuccess() {
        mVideo.setBuy(true);
        play();
        mTvSendDanmu.setEnabled(true);
    }

    @Override
    protected void onDestroy() {
        if (mPresenter != null) mPresenter.release();
        mPlayer.onVideoDestroy();
        if (isPlay) {
            getCurPlay().release();
        }
        if (mOrientationUtils != null)
            mOrientationUtils.releaseListener();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (mOrientationUtils != null) {
            mOrientationUtils.backToProtVideo();
        }
        if (StandardGSYVideoPlayer.backFromWindowFull(this)) {
            return;
        }
        super.onBackPressed();
    }

    private GSYVideoPlayer getCurPlay() {
        if (mPlayer.getFullWindowPlayer() != null) {
            return mPlayer.getFullWindowPlayer();
        }
        return mPlayer;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //如果旋转了就全屏
        if (isPlay && !isPause) {
            mPlayer.onConfigurationChanged(this, newConfig, mOrientationUtils);
        }
    }

    public void downloadRaw() {
        final File file = new File(StorageUtils.getVideoRootPath(), mVideo.getFileName());
        if (!FileUtil.isExists(file.getAbsolutePath())) {
            DownloadEntity entity1 = TasksManager.getImpl().getById(FileDownloadUtils.generateId(mVideo.getPath(),
                    StorageUtils.getVideoRootPath() + mVideo.getFileName()));
            if (entity1 != null) {
                showToast("已存在相同文件");
                return;
            }

            BaseDownloadTask task = FileDownloader.getImpl().create(StringUtils.getUrl(mVideo.getPath()))
                    .setPath(StorageUtils.getVideoRootPath() + mVideo.getFileName() + ".mp4")
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
            downloadEntity.setDirPath(StorageUtils.getVideoRootPath());
            downloadEntity.setFileName(mVideo.getFileName());
            downloadEntity.setPath(StorageUtils.getVideoRootPath() + mVideo.getFileName() + ".mp4");
            downloadEntity.setType(FolderType.MOVIE.toString());
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
            showToast(getString(R.string.msg_register_to_video_success, file.getAbsolutePath()));
        }
    }
}
