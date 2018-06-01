package com.moemoe.lalala.di.modules;

import com.moemoe.lalala.presenter.RoleContract;
import com.moemoe.lalala.presenter.VisitorsContract;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Administrator on 2018/5/31.
 */
@Module
public class VisitorsModule {
    private VisitorsContract.View mView;

    public VisitorsModule(VisitorsContract.View mView) {
        this.mView = mView;
    }

    @Provides
    public VisitorsContract.View provideView() {
        return mView;
    }
}
