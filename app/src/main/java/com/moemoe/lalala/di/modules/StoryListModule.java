package com.moemoe.lalala.di.modules;

import com.moemoe.lalala.presenter.StoryListContract;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Sora on 2018/4/8.
 */

@Module
public class StoryListModule {
    private StoryListContract.View mView;

    public StoryListModule(StoryListContract.View view) {
        this.mView = view;
    }

    @Provides
    public StoryListContract.View provideView() {
        return mView;
    }
}
