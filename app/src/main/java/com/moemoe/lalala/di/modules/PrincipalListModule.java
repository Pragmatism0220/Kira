package com.moemoe.lalala.di.modules;

import com.moemoe.lalala.presenter.PrincipalListContract;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Administrator on 2018/6/11.
 */
@Module
public class PrincipalListModule {
    private PrincipalListContract.View mView;

    public PrincipalListModule(PrincipalListContract.View mView) {
        this.mView = mView;
    }

    @Provides
    public PrincipalListContract.View provideView() {
        return mView;
    }
}
