package com.moemoe.lalala.di.modules;

import com.moemoe.lalala.presenter.StorageContract;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Administrator on 2018/6/2.
 */

@Module
public class StorageModule {
    private StorageContract.View mView;

    public StorageModule(StorageContract.View view) {
        this.mView = view;
    }

    @Provides
    public StorageContract.View provideView() {
        return mView;
    }
}
