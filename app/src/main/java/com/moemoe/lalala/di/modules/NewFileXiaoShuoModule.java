package com.moemoe.lalala.di.modules;

import com.moemoe.lalala.presenter.NewFolderItemXiaoShuoContract;

import dagger.Module;
import dagger.Provides;

/**
 * Created by yi on 2016/11/29.
 */
@Module
public class NewFileXiaoShuoModule {
    private NewFolderItemXiaoShuoContract.View mView;

    public NewFileXiaoShuoModule(NewFolderItemXiaoShuoContract.View view) {
        this.mView = view;
    }

    @Provides
    public NewFolderItemXiaoShuoContract.View provideView() {
        return mView;
    }
}
