package com.moemoe.lalala.di.components;

import com.moemoe.lalala.di.modules.CommunityModule;
import com.moemoe.lalala.di.scopes.UserScope;
import com.moemoe.lalala.view.activity.CommunityDetailsActivity;
import com.moemoe.lalala.view.activity.CommunityV1Activity;

import dagger.Component;

/**
 *
 * Created by yi on 2016/11/27.
 */
@UserScope
@Component(modules = CommunityModule.class,dependencies = NetComponent.class)
public interface CommunityV1Component {
    void inject(CommunityV1Activity activity);
    void inject(CommunityDetailsActivity activity);
}
