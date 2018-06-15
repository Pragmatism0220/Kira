package com.moemoe.lalala.di.modules;

import com.moemoe.lalala.presenter.BranchInfoContract;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Hygge on 2018/6/13.
 */
@Module
public class BranchInfoModule {
    private BranchInfoContract.View mView;
    public BranchInfoModule(BranchInfoContract.View view){this.mView = view;}
    @Provides
    public BranchInfoContract.View provideView(){return mView;}
}
