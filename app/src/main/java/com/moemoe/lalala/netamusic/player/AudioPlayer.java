package com.moemoe.lalala.netamusic.player;

import android.content.Context;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;

import com.moemoe.lalala.greendao.gen.MusicDao;
import com.moemoe.lalala.netamusic.data.model.Music;
import com.moemoe.lalala.netamusic.data.model.MusicListType;
import com.moemoe.lalala.utils.GreenDaoManager;
import com.moemoe.lalala.utils.ToastUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * Created by yi on 2018/2/6.
 */

public class AudioPlayer implements MediaPlayer.OnCompletionListener{

    private static final int STATE_IDLE = 0;
    private static final int STATE_PREPARING = 1;
    private static final int STATE_PLAYING = 2;
    private static final int STATE_PAUSE = 3;

    private static final long TIME_UPDATE = 300L;
    
    private final NoisyAudioStreamReceiver mNoisyReceiver = new NoisyAudioStreamReceiver();
    private final IntentFilter mNoisyFilter = new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);

    private Context context;
    private AudioFocusManager audioFocusManager;
    private MediaPlayer mediaPlayer;
    private Handler handler;
    private List<Music> musicList;
    private final List<OnPlayerEventListener> listeners = new ArrayList<>();
    private int position;
    private int state = STATE_IDLE;
    private boolean showNotify = true;

    public static AudioPlayer get() {
        return SingletonHolder.instance;
    }

    private static class SingletonHolder {
        private static AudioPlayer instance = new AudioPlayer();
    }

    private AudioPlayer() {
    }

    public void init(Context context) {
        this.context = context.getApplicationContext();
       // musicList = GreenDaoManager.getInstance().getSession().getMusicDao().queryBuilder().build().list();
        musicList = GreenDaoManager.getInstance().getSession().getMusicDao().queryBuilder().where(MusicDao.Properties.ListType.eq(MusicListType.TYPE_NORMAL)).build().list();
       // position = MusicPreferences.getPlayPosition();
        position = 0;
        audioFocusManager = new AudioFocusManager(context);
        mediaPlayer = new MediaPlayer();
        handler = new Handler(Looper.getMainLooper());
        mediaPlayer.setOnCompletionListener(this);
    }

    public void addOnPlayEventListener(OnPlayerEventListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removeOnPlayEventListener(OnPlayerEventListener listener) {
        listeners.remove(listener);
    }

    public int addAndPlay(Music music) {
        int position = musicList.indexOf(music);
        if (position < 0) {
            musicList.add(music);
            if(MusicListType.TYPE_NORMAL.equals(music.getListType())){
                GreenDaoManager.getInstance().getSession().getMusicDao().insert(music);
            }
            position = musicList.size() - 1;
        }
        play(position);
        return position;
    }

    public int add(Music music){
        int position = musicList.indexOf(music);
        if (position < 0) {
            musicList.add(music);
            if(MusicListType.TYPE_NORMAL.equals(music.getListType())){
                GreenDaoManager.getInstance().getSession().getMusicDao().insert(music);
            }
        }
        return position;
    }

    public void addAll(ArrayList<Music> music){
        musicList.addAll(music);
    }

    public void setShowNotify(boolean showNotify){
        this.showNotify = showNotify;
    }

    public void play(int position) {
        if (musicList.isEmpty()) {
            return;
        }

        if (position < 0) {
            position = musicList.size() - 1;
        } else if (position >= musicList.size()) {
            position = 0;
        }

        this.position = position;
        //MusicPreferences.savePlayPosition(this.position);
        Music music = musicList.get(this.position);

        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(music.getPath());
            mediaPlayer.prepareAsync();
            state = STATE_PREPARING;
            mediaPlayer.setOnPreparedListener(mPreparedListener);
            mediaPlayer.setOnBufferingUpdateListener(mBufferingUpdateListener);
            for (OnPlayerEventListener listener : listeners) {
                listener.onChange(music);
            }
            if(showNotify){
                Notifier.get().showPlay(music);
            }
            MediaSessionManager.get().updateMetaData(music);
            MediaSessionManager.get().updatePlaybackState();
        } catch (IOException e) {
            e.printStackTrace();
            ToastUtils.showShortToast(context,"当前歌曲无法播放");
        }
    }

    public void delete(int position) {
        Music music = musicList.remove(position);
        GreenDaoManager.getInstance().getSession().getMusicDao().delete(music);
    }

    public void initList(String type){
        musicList = GreenDaoManager.getInstance().getSession().getMusicDao().queryBuilder().where(MusicDao.Properties.ListType.eq(type)).build().list();
    }

    public void clearList(boolean del){
        if(del){
            GreenDaoManager.getInstance().getSession().getMusicDao().deleteInTx(musicList);
        }
        musicList.clear();
    }

    private MediaPlayer.OnPreparedListener mPreparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
            if (isPreparing()) {
                startPlayer();
            }
        }
    };

    private MediaPlayer.OnBufferingUpdateListener mBufferingUpdateListener = new MediaPlayer.OnBufferingUpdateListener() {
        @Override
        public void onBufferingUpdate(MediaPlayer mp, int percent) {
            for (OnPlayerEventListener listener : listeners) {
                listener.onBufferingUpdate(percent);
            }
        }
    };

    public void playPause() {
        if (isPreparing()) {
            stopPlayer();
        } else if (isPlaying()) {
            pausePlayer();
        } else if (isPausing()) {
            startPlayer();
        } else {
            play(position);
        }
    }

    public void startPlayer() {
        if (!isPreparing() && !isPausing()) {
            return;
        }

        if (audioFocusManager.requestAudioFocus()) {
            mediaPlayer.start();
            state = STATE_PLAYING;
            handler.post(mPublishRunnable);
            if(showNotify){
                Notifier.get().showPlay(musicList.get(position));
            }

            MediaSessionManager.get().updatePlaybackState();
            context.registerReceiver(mNoisyReceiver, mNoisyFilter);

            for (OnPlayerEventListener listener : listeners) {
                listener.onPlayerStart();
            }
        }
    }

    public void pausePlayer() {
        if (!isPlaying()) {
            return;
        }

        mediaPlayer.pause();
        state = STATE_PAUSE;
        handler.removeCallbacks(mPublishRunnable);
        if(showNotify){
            Notifier.get().showPause(musicList.get(position));
        }
        MediaSessionManager.get().updatePlaybackState();
        context.unregisterReceiver(mNoisyReceiver);
        audioFocusManager.abandonAudioFocus();

        for (OnPlayerEventListener listener : listeners) {
            listener.onPlayerPause();
        }
    }

    public void stopPlayer() {
        if (isIdle()) {
            return;
        }

        pausePlayer();
        mediaPlayer.reset();
        position = 0;
        state = STATE_IDLE;
    }

    public void next() {
        if (musicList.isEmpty()) {
            return;
        }

        PlayModeEnum mode = PlayModeEnum.valueOf(MusicPreferences.getPlayMode());
        switch (mode) {
            case SHUFFLE:
                position = new Random().nextInt(musicList.size());
                play(position);
                break;
            case SINGLE:
                play(position);
                break;
            case LOOP:
            default:
                play(position + 1);
                break;
        }
    }

    public void prev() {
        if (musicList.isEmpty()) {
            return;
        }

        PlayModeEnum mode = PlayModeEnum.valueOf(MusicPreferences.getPlayMode());
        switch (mode) {
            case SHUFFLE:
                position = new Random().nextInt(musicList.size());
                play(position);
                break;
            case SINGLE:
                play(position);
                break;
            case LOOP:
            default:
                play(position - 1);
                break;
        }
    }

    /**
     * 跳转到指定的时间位置
     *
     * @param msec 时间
     */
    public void seekTo(int msec) {
        if (isPlaying() || isPausing()) {
            mediaPlayer.seekTo(msec);
            MediaSessionManager.get().updatePlaybackState();
            for (OnPlayerEventListener listener : listeners) {
                listener.onPublish(msec);
            }
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        next();
    }

    private Runnable mPublishRunnable = new Runnable() {
        @Override
        public void run() {
            if (isPlaying()) {
                for (OnPlayerEventListener listener : listeners) {
                    listener.onPublish(mediaPlayer.getCurrentPosition());
                }
            }
            handler.postDelayed(this, TIME_UPDATE);
        }
    };

    public int getAudioSessionId() {
        return mediaPlayer.getAudioSessionId();
    }

    public long getAudioPosition() {
        if (isPlaying() || isPausing()) {
            return mediaPlayer.getCurrentPosition();
        } else {
            return 0;
        }
    }

    public int getPlayPosition() {
        return position;
    }

    public Music getPlayMusic() {
        if (musicList.isEmpty()) {
            return null;
        }
        if (position < 0 || position >= musicList.size()) {
            position = 0;
        }
        return musicList.get(position);
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public List<Music> getMusicList() {
        return musicList;
    }

    public boolean isPlaying() {
        return state == STATE_PLAYING;
    }

    public boolean isPausing() {
        return state == STATE_PAUSE;
    }

    public boolean isPreparing() {
        return state == STATE_PREPARING;
    }

    public boolean isIdle() {
        return state == STATE_IDLE;
    }
}
