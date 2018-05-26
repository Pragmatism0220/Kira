package com.moemoe.lalala.di.modules;

import com.moemoe.lalala.presenter.FriendsContract;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Sora on 2018/4/2.
 */
@Module
public class FriendsModule {
    private FriendsContract.View mView;

    public FriendsModule(FriendsContract.View view) {
        this.mView = view;
    }

    @Provides
    public FriendsContract.View provideView() {
        return mView;
    }
}
