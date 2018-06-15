package com.moemoe.lalala.di.modules;

import com.moemoe.lalala.presenter.BranchContract;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Hygge on 2018/6/12.
 */
@Module
public class BranchModule {
    private BranchContract.View mView;
    public BranchModule(BranchContract.View view){this.mView = view;}
    @Provides
    public BranchContract.View provideView(){return mView;}
}
