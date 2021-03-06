package com.moemoe.lalala.di.components;

import com.moemoe.lalala.di.modules.AllMemberModule;
import com.moemoe.lalala.di.scopes.UserScope;
import com.moemoe.lalala.view.activity.CommunityAllMemberActivity;

import dagger.Component;

/**
 *
 * Created by yi on 2016/11/27.
 */
@UserScope
@Component(modules = AllMemberModule.class,dependencies = NetComponent.class)
public interface AllMemberComponent {
    void inject(CommunityAllMemberActivity activity);
}
