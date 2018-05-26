package com.moemoe.lalala.di.modules;

import com.moemoe.lalala.presenter.VideoCommentContract;

import dagger.Module;
import dagger.Provides;

/**
 *
 * Created by yi on 2016/11/29.
 */
@Module
public class VideoCommentModule {
    private VideoCommentContract.View mView;

    public VideoCommentModule(VideoCommentContract.View view){
        this.mView = view;
    }

    @Provides
    public VideoCommentContract.View provideView(){return mView;}
}
