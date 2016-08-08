package com.grupomedios.desclub.desclubandroid.common.fragment;

import android.app.Fragment;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.grupomedios.desclub.desclubandroid.DesclubApplication;

/**
 * Created by Matias on 8/8/16.
 */
public abstract class BaseFragment extends Fragment {
    @Override
    public void onResume() {
        super.onResume();
        trackScreen(getScreenName());
    }

    public void trackScreen(String screenName) {
        if (screenName != null && !screenName.isEmpty()) {
            Tracker tracker = ((DesclubApplication) getActivity().getApplication()).getDefaultTracker();
            tracker.setScreenName(screenName);
            tracker.send(new HitBuilders.ScreenViewBuilder().build());
        }
    }

    public abstract String getScreenName();


    public void trackEvent(String eventCategory, String eventName) {
        Tracker tracker = ((DesclubApplication) getActivity().getApplication()).getDefaultTracker();

        tracker.send(new HitBuilders.EventBuilder()
                .setCategory(eventCategory)
                .setAction(eventName)
                .build());
    }
}
