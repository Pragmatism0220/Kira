package com.moemoe.lalala.di.modules;

import com.moemoe.lalala.presenter.NewVisitorsContract;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Administrator on 2018/6/6.
 */
@Module
public class NewVisitorModule {
    private NewVisitorsContract.View mView;

    public NewVisitorModule(NewVisitorsContract.View mView) {
        this.mView = mView;
    }

    @Provides
    public NewVisitorsContract.View provideView() {
        return mView;
    }
}
