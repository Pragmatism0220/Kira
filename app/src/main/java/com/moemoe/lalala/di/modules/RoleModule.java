package com.moemoe.lalala.di.modules;

import com.moemoe.lalala.presenter.RoleContract;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Administrator on 2018/5/25.
 */
@Module
public class RoleModule {
    private RoleContract.View mView;

    public RoleModule(RoleContract.View view) {
        this.mView = view;
    }

    @Provides
    public RoleContract.View provideView() {
        return mView;
    }
}
