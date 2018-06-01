package com.moemoe.lalala.di.modules;

import com.moemoe.lalala.presenter.PropContract;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Administrator on 2018/5/29.
 */
@Module
public class PropModule {

    private PropContract.View mView;

    public PropModule(PropContract.View view) {
        this.mView = view;
    }

    @Provides
    public PropContract.View provideView() {
        return mView;
    }
}
