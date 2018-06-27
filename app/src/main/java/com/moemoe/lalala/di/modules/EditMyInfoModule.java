package com.moemoe.lalala.di.modules;

import com.moemoe.lalala.presenter.EditMyInfoContract;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Administrator on 2018/6/27.
 */
@Module
public class EditMyInfoModule {
    private EditMyInfoContract.View mView;

    public EditMyInfoModule(EditMyInfoContract.View mView) {
        this.mView = mView;
    }

    @Provides
    public EditMyInfoContract.View provideView() {
        return mView;
    }
}
