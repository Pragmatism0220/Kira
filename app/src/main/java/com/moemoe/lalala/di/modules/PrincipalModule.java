package com.moemoe.lalala.di.modules;

import com.moemoe.lalala.presenter.PrincipalContract;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Administrator on 2018/6/11.
 */
@Module
public class PrincipalModule {

    private PrincipalContract.View mView;

    public PrincipalModule(PrincipalContract.View mView) {
        this.mView = mView;
    }

    @Provides
    public PrincipalContract.View provideView() {
        return mView;
    }
}

