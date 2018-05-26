package com.moemoe.lalala.di.components;

import com.moemoe.lalala.di.modules.SavaDescribeModule;
import com.moemoe.lalala.di.scopes.UserScope;
import com.moemoe.lalala.view.activity.CommunityEditActivity;

import dagger.Component;

/**
 *
 * Created by yi on 2016/11/27.
 */
@UserScope
@Component(modules = SavaDescribeModule.class,dependencies = NetComponent.class)
public interface SaveDescribeComponent {
    void inject(CommunityEditActivity activity);
}
