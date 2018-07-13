package com.moemoe.lalala.di.modules;

import com.moemoe.lalala.presenter.SevenDayLoginContract;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Administrator on 2018/7/12.
 */
@Module
public class SevenDayLoginModule {
    private SevenDayLoginContract.View mView;

    public SevenDayLoginModule(SevenDayLoginContract.View mView) {
        this.mView = mView;
    }

    @Provides
    public SevenDayLoginContract.View provideView() {
        return mView;
    }
}

