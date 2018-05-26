package com.moemoe.lalala.view.activity;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
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
import com.moemoe.lalala.model.entity.UserTopEntity;
import com.moemoe.lalala.netamusic.data.model.Music;
import com.moemoe.lalala.netamusic.player.AudioPlayer;
import com.moemoe.lalala.netamusic.player.MusicPreferences;
import com.moemoe.lalala.netamusic.player.OnPlayerEventListener;
import com.moemoe.lalala.netamusic.player.PlayModeEnum;
import com.moemoe.lalala.presenter.KiraVideoContract;
import com.moemoe.lalala.presenter.KiraVideoPresenter;
import com.moemoe.lalala.utils.DensityUtil;
import com.moemoe.lalala.utils.FileUtil;
import com.moemoe.lalala.utils.GreenDaoManager;
import com.moemoe.lalala.utils.NoDoubleClickListener;
import com.moemoe.lalala.utils.StorageUtils;
import com.moemoe.lalala.utils.StringUtils;
import com.moemoe.lalala.utils.TasksManager;
import com.moemoe.lalala.utils.ToastUtils;
import com.moemoe.lalala.view.adapter.MusicListAdapter;
import com.moemoe.lalala.view.widget.adapter.BaseRecyclerViewAdapter;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.ColorFilterTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 *
 * Created by yi on 2018/2/6.
 */

public class MusicDetailActivityV2 extends BaseAppCompatActivity implements KiraVideoContract.View,SeekBar.OnSeekBarChangeListener, OnPlayerEventListener {

    @BindView(R.id.iv_bg)
    ImageView mIvBg;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_user_name)
    TextView mTvUserName;
    @BindView(R.id.iv_cover)
    ImageView mIvCover;
    @BindView(R.id.tv_fav)
    TextView mTvFav;
    @BindView(R.id.tv_current)
    TextView mTvCur;
    @BindView(R.id.tv_total)
    TextView mTvTotal;
    @BindView(R.id.progress)
    SeekBar mProgress;
    @BindView(R.id.iv_mode)
    ImageView mIvSwitchMode;
    @BindView(R.id.iv_music_control)
    ImageView mIvControl;
    @BindView(R.id.ll_list_root)
    View mListRoot;
    @BindView(R.id.list)
    RecyclerView mList;

    @Inject
    KiraVideoPresenter mPresenter;

    private Music mCurMusic;
    private int mLastProgress;
    private boolean isDraggingProgress;
    private MusicListAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.ac_music_detail_v2;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        DaggerKiraVideoComponent.builder()
                .kiraVideoModule(new KiraVideoModule(this))
                .netComponent(MoeMoeApplication.getInstance().getNetComponent())
                .build()
                .inject(this);
        initPlayMode();
        onChangeImpl(AudioPlayer.get().getPlayMusic());
        AudioPlayer.get().addOnPlayEventListener(this);
        mAdapter = new MusicListAdapter();
        mList.setLayoutManager(new LinearLayoutManager(this));
        mList.setAdapter(mAdapter);
        mAdapter.setList((ArrayList<Music>) AudioPlayer.get().getMusicList());
        mAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                AudioPlayer.get().play(position);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        if(mPresenter != null) mPresenter.release();
        AudioPlayer.get().removeOnPlayEventListener(this);
        super.onDestroy();
    }

    @Override
    protected void initToolbar(Bundle savedInstanceState) {

    }

    @Override
    protected void initListeners() {
        mToolbar.setNavigationOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                finish();
            }
        });
        mProgress.setOnSeekBarChangeListener(this);
    }

    @Override
    protected void initData() {

    }

    private void initPlayMode() {
        int mode = MusicPreferences.getPlayMode();
        mIvSwitchMode.setImageLevel(mode);
    }

    private void onChangeImpl(Music music) {
        if (music == null) {
            return;
        }
        mCurMusic = music;
        mTvTitle.setText(music.getTitle());
        mTvUserName.setText("上传者：" + music.getArtist());
        mProgress.setProgress((int) AudioPlayer.get().getAudioPosition());
        mProgress.setSecondaryProgress(0);
        mProgress.setMax((int) music.getDuration());
        mTvFav.setSelected(music.isFav());
        mLastProgress = 0;
        mTvCur.setText("00:00");
        mTvTotal.setText(StringUtils.getMinute((int) music.getDuration()));
        setCoverAndBg(music);
        if (AudioPlayer.get().isPlaying() || AudioPlayer.get().isPreparing()) {
            mIvControl.setSelected(true);
        } else {
            mIvControl.setSelected(false);
        }
    }

    private void play() {
        AudioPlayer.get().playPause();
    }

    private void next() {
        AudioPlayer.get().next();
    }

    private void prev() {
        AudioPlayer.get().prev();
    }

    private void setCoverAndBg(Music music) {
        Glide.with(this)
                .load(StringUtils.getUrl(this,music.getCoverPath(), DensityUtil.getScreenWidth(this),DensityUtil.getScreenHeight(this),false,true))
                .bitmapTransform(new BlurTransformation(this,10,4),new ColorFilterTransformation(this,R.color.alpha_40))
                .into(mIvBg);
        int size = getResources().getDimensionPixelSize(R.dimen.y300);
        Glide.with(this)
                .load(StringUtils.getUrl(this,music.getCoverPath(),size,size,false,true))
                .bitmapTransform(new CropCircleTransformation(this))
                .into(mIvCover);
    }

    @OnClick({R.id.tv_fav,R.id.tv_download,R.id.tv_forward,R.id.iv_pre,R.id.iv_next,R.id.iv_music_list,R.id.tv_clear_list,R.id.view_close_list,R.id.tv_close_list,R.id.iv_mode
        ,R.id.iv_music_control})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.tv_fav:
                if(mCurMusic != null){
                    try {
                        JSONObject o = new JSONObject(mCurMusic.getAttr());
                        mPresenter.favMovie(FolderType.YY.toString(),mCurMusic.isFav(),o.getString("folderId"),mCurMusic.getFileId());
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.tv_download:
                if(mCurMusic != null) downloadRaw();
                break;
            case R.id.tv_forward:
                if(mCurMusic != null){
                    rtMusic();
                }
                break;
            case R.id.iv_pre:
                prev();
                break;
            case R.id.iv_next:
                next();
                break;
            case R.id.iv_music_list:
                mListRoot.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_clear_list:
                AudioPlayer.get().stopPlayer();
                AudioPlayer.get().clearList(true);
                mAdapter.notifyDataSetChanged();
                mCurMusic = null;
                break;
            case R.id.view_close_list:
                mListRoot.setVisibility(View.GONE);
                break;
            case R.id.tv_close_list:
                mListRoot.setVisibility(View.GONE);
                break;
            case R.id.iv_mode:
                switchPlayMode();
                break;
            case R.id.iv_music_control:
                play();
                break;
        }
    }

    public void rtMusic(){
        ShareFolderEntity entity = new ShareFolderEntity();
        entity.setFolderCover(mCurMusic.getCoverPath());
        try {
            JSONObject o = new JSONObject(mCurMusic.getAttr());
            entity.setFolderId(o.getString("folderId"));
            entity.setFolderName(mCurMusic.getFileName());
            ArrayList<String> tag = new ArrayList<>();
            if(!TextUtils.isEmpty(mCurMusic.getTag1())){
                tag.add(mCurMusic.getTag1());
            }
            if(!TextUtils.isEmpty(mCurMusic.getTag2())){
                tag.add(mCurMusic.getTag2());
            }
            entity.setFolderTags(tag);
            entity.setFolderType(FolderType.MUSIC.toString());
            entity.setUpdateTime(mCurMusic.getUpdateTime());
            UserTopEntity entity1 = new UserTopEntity();
            entity1.setUserId(mCurMusic.getUserId());
            entity1.setUserName(mCurMusic.getArtist());
            entity1.setSex(mCurMusic.getUserSex());
            entity.setCreateUser(entity1);
            CreateForwardActivity.startActivity(this,CreateForwardActivity.TYPE_MUSIC,entity,mCurMusic.getFileId());
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void switchPlayMode() {
        PlayModeEnum mode = PlayModeEnum.valueOf(MusicPreferences.getPlayMode());
        switch (mode) {
            case LOOP:
                mode = PlayModeEnum.SHUFFLE;
                showToast("随机播放");
                break;
            case SHUFFLE:
                mode = PlayModeEnum.SINGLE;
                showToast("单曲循环");
                break;
            case SINGLE:
                mode = PlayModeEnum.LOOP;
                showToast("列表循环");
                break;
        }
        MusicPreferences.savePlayMode(mode.value());
        initPlayMode();
    }

    public void downloadRaw(){
        final File file = new File(StorageUtils.getMusicRootPath(),mCurMusic.getFileName());
        if(!FileUtil.isExists(file.getAbsolutePath())){
            DownloadEntity entity1 = TasksManager.getImpl().getById(FileDownloadUtils.generateId(mCurMusic.getPath(), StorageUtils.getMusicRootPath() + mCurMusic.getFileName()));
            if(entity1 != null){
                showToast("已存在相同文件");
                return;
            }

            BaseDownloadTask task = FileDownloader.getImpl().create(StringUtils.getUrl(mCurMusic.getPath()))
                    .setPath(StorageUtils.getMusicRootPath() + mCurMusic.getFileName())
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
            downloadEntity.setUrl(mCurMusic.getPath());
            downloadEntity.setDirPath(StorageUtils.getMusicRootPath());
            downloadEntity.setFileName(mCurMusic.getFileName());
            downloadEntity.setPath(StorageUtils.getMusicRootPath() + mCurMusic.getFileName());
            downloadEntity.setType(FolderType.MUSIC.toString());
            downloadEntity.setFileId(mCurMusic.getFileId());
            downloadEntity.setId((long) taskId);
            downloadEntity.setAttr(mCurMusic.getAttr());
            GreenDaoManager.getInstance().getSession().getDownloadEntityDao().insertOrReplace(downloadEntity);
            TasksManager.getImpl().add(downloadEntity);
            task.start();
        }else {
            showToast(getString(R.string.msg_register_to_music_success, file.getAbsolutePath()));
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (seekBar == mProgress) {
            if (Math.abs(progress - mLastProgress) >= DateUtils.SECOND_IN_MILLIS) {
                mTvCur.setText(StringUtils.getMinute(progress));
                mLastProgress = progress;
            }
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        if (seekBar == mProgress) {
            isDraggingProgress = true;
        }
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (seekBar == mProgress) {
            isDraggingProgress = false;
            if (AudioPlayer.get().isPlaying() || AudioPlayer.get().isPausing()) {
                int progress = seekBar.getProgress();
                AudioPlayer.get().seekTo(progress);
            } else {
                seekBar.setProgress(0);
            }
        }
    }

    @Override
    public void onChange(Music music) {
        onChangeImpl(music);
    }

    @Override
    public void onPlayerStart() {
        mIvControl.setSelected(true);
    }

    @Override
    public void onPlayerPause() {
        mIvControl.setSelected(false);
    }

    @Override
    public void onPublish(int progress) {
        if (!isDraggingProgress) {
            mProgress.setProgress(progress);
        }
    }

    @Override
    public void onBufferingUpdate(int percent) {
        if(percent != 0) mProgress.setSecondaryProgress(mProgress.getMax() * 100 / percent);
    }

    @Override
    public void onFailure(int code, String msg) {

    }

    @Override
    public void onLoadVideoInfoSuccess(KiraVideoEntity entity) {

    }

    @Override
    public void onLoadVideoInfoFail() {

    }

    @Override
    public void onReFreshSuccess(ArrayList<ShowFolderEntity> entities) {

    }

    @Override
    public void onLoadDanmakuSuccess(String uri) {

    }

    @Override
    public void onSendDanmakuSuccess() {

    }

    @Override
    public void onFavMovieSuccess() {
        mCurMusic.setFav(!mCurMusic.isFav());
        mTvFav.setSelected(!mTvFav.isSelected());
        EventBus.getDefault().post(new FavMusicEvent(mCurMusic.isFav(),mCurMusic.getFileId()));
        if(mTvFav.isSelected()){
            showToast("收藏成功");
        }else {
            showToast("取消收藏成功");
        }
    }

    @Override
    public void onBuyFileSuccess() {

    }
}
