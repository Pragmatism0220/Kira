package com.moemoe.lalala.di.modules;

import com.moemoe.lalala.presenter.NewSettingContract;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Administrator on 2018/6/27.
 */
@Module
public class NewSettingModule {
    private NewSettingContract.View mView;

    public NewSettingModule(NewSettingContract.View mView) {
        this.mView = mView;
    }

    @Provides
    public NewSettingContract.View provideView() {
        return mView;
    }
}
