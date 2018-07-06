package com.moemoe.lalala.di.modules;

import com.moemoe.lalala.presenter.DiaryContract;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Administrator on 2018/7/5.
 */
@Module
public class DiaryModule {
    private DiaryContract.View mView;

    public DiaryModule(DiaryContract.View mView) {
        this.mView = mView;
    }

    @Provides
    public DiaryContract.View provideView() {
        return mView;
    }
}
