package com.moemoe.lalala.di.modules;

import com.moemoe.lalala.presenter.NewDormitioryContract;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Administrator on 2018/6/11.
 */
@Module
public class NewDormitorModule {
    private NewDormitioryContract.View mView;

    public NewDormitorModule(NewDormitioryContract.View mView) {
        this.mView = mView;
    }

    @Provides
    public NewDormitioryContract.View provideView() {
        return mView;
    }
}
