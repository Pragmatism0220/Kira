package com.moemoe.lalala.di.modules;

import com.moemoe.lalala.presenter.SelectUpterContract;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Hygge on 2018/7/17.
 */
@Module
public class SelectUpterModule {
    private SelectUpterContract.View mView;

    public SelectUpterModule(SelectUpterContract.View view) {
        this.mView = view;
    }

    @Provides
    public SelectUpterContract.View provideView() {
        return mView;
    }
}
