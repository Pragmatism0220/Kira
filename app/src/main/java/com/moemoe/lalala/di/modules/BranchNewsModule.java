package com.moemoe.lalala.di.modules;

import android.support.transition.Visibility;

import com.moemoe.lalala.presenter.BranchNewsContract;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Administrator on 2018/7/19.
 */

@Module
public class BranchNewsModule {
    private BranchNewsContract.View mView;

    public BranchNewsModule(BranchNewsContract.View mView) {
        this.mView = mView;
    }

    @Provides
    public BranchNewsContract.View provideView() {
        return mView;
    }
}
