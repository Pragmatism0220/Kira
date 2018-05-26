package com.moemoe.lalala.di.modules;

import com.moemoe.lalala.presenter.FeedContract;
import com.moemoe.lalala.presenter.NewFeedContract;

import dagger.Module;
import dagger.Provides;

/**
 * Created by yi on 2016/11/29.
 */
@Module
public class NewFeedModule {
    private NewFeedContract.View mView;

    public NewFeedModule(NewFeedContract.View view){
        this.mView = view;
    }

    @Provides
    public NewFeedContract.View provideView(){return mView;}
}
