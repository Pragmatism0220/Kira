package com.moemoe.lalala.di.components;

import com.moemoe.lalala.di.modules.NewVisitorModule;
import com.moemoe.lalala.di.scopes.UserScope;
import com.moemoe.lalala.view.activity.NewVisitorActivity;

import dagger.Component;

/**
 * Created by Administrator on 2018/6/6.
 */
@UserScope
@Component(modules = NewVisitorModule.class, dependencies = NetComponent.class)
public interface NewVisitorsComponent {

    void inject(NewVisitorActivity activity);
}
