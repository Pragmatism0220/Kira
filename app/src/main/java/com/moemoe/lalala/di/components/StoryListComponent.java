package com.moemoe.lalala.di.components;

import com.moemoe.lalala.di.modules.StoryListModule;
import com.moemoe.lalala.di.scopes.UserScope;
import com.moemoe.lalala.view.activity.StoryListActivity;

import dagger.Component;

/**
 * Created by Sora on 2018/4/8.
 */
@UserScope
@Component(modules = StoryListModule.class,dependencies = NetComponent.class)
public interface StoryListComponent {
    void inject(StoryListActivity activity);
}
