package com.moemoe.lalala.di.modules;

import com.moemoe.lalala.presenter.SearchAllContract;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Sora on 2018/3/7.
 */
@Module
public class SearchAllModule {
    private SearchAllContract.View mView;

    public SearchAllModule(SearchAllContract.View view) {
        this.mView = view;
    }

    @Provides
    public SearchAllContract.View provideView() {
        return mView;
    }
}
