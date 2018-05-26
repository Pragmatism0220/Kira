package com.moemoe.lalala.di.modules;

import com.moemoe.lalala.presenter.BagVisitorContract;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Sora on 2018/3/6.
 */
@Module
public class BagVisitorModule {
    private BagVisitorContract.View mView;

    public BagVisitorModule(BagVisitorContract.View view) {
        this.mView = view;
    }

    @Provides
    public BagVisitorContract.View provideView() {
        return mView;
    }
}
