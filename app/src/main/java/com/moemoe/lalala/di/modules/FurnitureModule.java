package com.moemoe.lalala.di.modules;

import com.moemoe.lalala.presenter.FurnitureContract;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Administrator on 2018/5/30.
 */
@Module
public class FurnitureModule {
    private FurnitureContract.View mView;

    public FurnitureModule(FurnitureContract.View view) {
        this.mView = view;
    }

    @Provides
    public FurnitureContract.View provideView() {
        return mView;
    }
}
