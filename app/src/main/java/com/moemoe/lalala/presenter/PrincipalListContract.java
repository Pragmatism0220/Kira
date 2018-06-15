package com.moemoe.lalala.presenter;

import com.moemoe.lalala.event.NewStoryInfo;
import com.moemoe.lalala.event.NewStoryJsonInfo;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/6/11.
 */

public interface PrincipalListContract {
    interface Presenter extends BasePresenter {
        void getPrincipalListInfo(String id);

        void playStory(String scriptId);
    }

    interface View extends BaseView {
        void getPrincipalListInfoSuccess(ArrayList<NewStoryInfo> newStoryInfos);

        void playStorySuccess(NewStoryJsonInfo newStoryJsonInfo);
    }
}
