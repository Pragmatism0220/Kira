package com.moemoe.lalala.di.components;

import com.moemoe.lalala.di.modules.VisitorsModule;
import com.moemoe.lalala.di.scopes.UserScope;
import com.moemoe.lalala.view.activity.VisitorsActivity;

import dagger.Component;

/**
 * Created by Administrator on 2018/5/31.
 */

@UserScope
@Component(modules = VisitorsModule.class, dependencies = NetComponent.class)
public interface VisitorsComponent {
    void inject(VisitorsActivity activity);
}
