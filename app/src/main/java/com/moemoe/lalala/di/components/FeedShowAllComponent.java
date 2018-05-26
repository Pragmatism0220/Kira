package com.moemoe.lalala.di.components;

import com.moemoe.lalala.di.modules.FeedShowAllModule;
import com.moemoe.lalala.di.scopes.UserScope;
import com.moemoe.lalala.view.activity.FeedShowAllActivity;

import dagger.Component;

/**
 *
 * Created by yi on 2016/11/27.
 */
@UserScope
@Component(modules = FeedShowAllModule.class,dependencies = NetComponent.class)
public interface FeedShowAllComponent {
    void inject(FeedShowAllActivity activity);
}
