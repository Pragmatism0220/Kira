package com.moemoe.lalala.di.modules;

import com.moemoe.lalala.presenter.FurnitureUpDateContract;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Administrator on 2018/7/19.
 */
@Module
public class FurnitureUpDateMoudle {
    private FurnitureUpDateContract.View mView;

    public FurnitureUpDateMoudle(FurnitureUpDateContract.View mView) {
        this.mView = mView;
    }

    @Provides
    public FurnitureUpDateContract.View provideView() {
        return mView;
    }
}
