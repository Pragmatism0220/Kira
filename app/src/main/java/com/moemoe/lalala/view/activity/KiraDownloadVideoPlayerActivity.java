package com.moemoe.lalala.view.activity;

import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewCompat;
import android.transition.Transition;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.moemoe.lalala.R;
import com.moemoe.lalala.event.SwitchVideoModel;
import com.moemoe.lalala.utils.StorageUtils;
import com.moemoe.lalala.view.widget.video.SampleVideo;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Hygge on 2018/5/16.
 */

public class KiraDownloadVideoPlayerActivity extends BaseAppCompatActivity {

    public final static String IMG_TRANSITION = "IMG_TRANSITION";
    public final static String TRANSITION = "TRANSITION";

    @BindView(R.id.video_player)
    SampleVideo videoPlayer;

    private boolean isTransition;
    OrientationUtils orientationUtils;

    private Transition transition;
    /**
     * 播放路径
     */
    private String mPath;
    private String folderName;

    @Override
    protected int getLayoutId() {
        return R.layout.ac_download_video_player;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        // 防止锁屏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mPath = getIntent().getStringExtra("path");
        folderName = getIntent().getStringExtra("folderName");
        isTransition = getIntent().getBooleanExtra(TRANSITION, false);
        videoPlayer.requestLayout();
        init();
    }
    private void init() {
        String url = "https://res.exexm.com/cw_145225549855002";
        File file = new File(StorageUtils.getVideoRootPath() , folderName+".mp4");
        //String url = "http://7xse1z.com1.z0.glb.clouddn.com/1491813192";
        //需要路径的
        //videoPlayer.setUp(url, true, new File(FileUtils.getPath()), "");

        //借用了jjdxm_ijkplayer的URL
//        String source1 = "http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f20.mp4";
        String source1 = file.getAbsolutePath();
        String name = "普通";
        SwitchVideoModel switchVideoModel = new SwitchVideoModel(name, source1);

        String source2 = "";
        String name2 = "清晰";
        SwitchVideoModel switchVideoModel2 = new SwitchVideoModel(name2, source2);

        List<SwitchVideoModel> list = new ArrayList<>();
        list.add(switchVideoModel);
        list.add(switchVideoModel2);

        videoPlayer.setUp(list, true, folderName);

        //增加封面
        ImageView imageView = new ImageView(this);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageResource(R.drawable.bg_cardbg_nopic);
        videoPlayer.setThumbImageView(imageView);

        //增加title
        videoPlayer.getTitleTextView().setVisibility(View.VISIBLE);
        //videoPlayer.setShowPauseCover(false);

        //videoPlayer.setSpeed(2f);

        //设置返回键
        videoPlayer.getBackButton().setVisibility(View.VISIBLE);

        //设置旋转
        orientationUtils = new OrientationUtils(this, videoPlayer);

        //设置全屏按键功能,这是使用的是选择屏幕，而不是全屏
        videoPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orientationUtils.resolveByClick();
            }
        });

        //videoPlayer.setBottomProgressBarDrawable(getResources().getDrawable(R.drawable.video_new_progress));
        //videoPlayer.setDialogVolumeProgressBar(getResources().getDrawable(R.drawable.video_new_volume_progress_bg));
        //videoPlayer.setDialogProgressBar(getResources().getDrawable(R.drawable.video_new_progress));
        //videoPlayer.setBottomShowProgressBarDrawable(getResources().getDrawable(R.drawable.video_new_seekbar_progress),
        //getResources().getDrawable(R.drawable.video_new_seekbar_thumb));
        //videoPlayer.setDialogProgressColor(getResources().getColor(R.color.colorAccent), -11);

        //是否可以滑动调整
        videoPlayer.setIsTouchWiget(true);

        //设置返回按键功能
        videoPlayer.getBackButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //过渡动画
        initTransition();
    }
    @Override
    protected void initToolbar(Bundle savedInstanceState) {

    }

    @Override
    protected void initListeners() {

    }

    @Override
    protected void initData() {

    }

    private void initTransition() {
        if (isTransition && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            postponeEnterTransition();
            ViewCompat.setTransitionName(videoPlayer, IMG_TRANSITION);
//            addTransitionListener();
            startPostponedEnterTransition();
        } else {
            videoPlayer.startPlayLogic();
        }
    }

    @Override
    public void onBackPressed() {
        //先返回正常状态
        if (orientationUtils.getScreenType() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            videoPlayer.getFullscreenButton().performClick();
            return;
        }
        //释放所有
        videoPlayer.setVideoAllCallBack(null);
        GSYVideoManager.releaseAllVideos();
        if (isTransition && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            super.onBackPressed();
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                    overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                }
            }, 500);
        }
    }
//    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//    private boolean addTransitionListener() {
//        transition = getWindow().getSharedElementEnterTransition();
//        if (transition != null) {
//            transition.addListener(new OnTransitionListener(){
//                @Override
//                public void onTransitionEnd(Transition transition) {
//                    super.onTransitionEnd(transition);
//                    videoPlayer.startPlayLogic();
//                    transition.removeListener(this);
//                }
//            });
//            return true;
//        }
//        return false;
//    }

}
