package ir.snappfood.keplertracker;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import static ir.snappfood.keplertracker.Utils.getSimpleClassName;


public class SFragmentLifecycleCallbacks extends FragmentManager.FragmentLifecycleCallbacks {

    @Override
    public void onFragmentResumed(FragmentManager fm, Fragment f) {
        super.onFragmentResumed(fm, f);
        SAnalytics.setScreenName(getSimpleClassName(f.getClass()),
                AnalyticsData.SCREEN_TYPE_FRAGMENT, AnalyticsData.LIFE_CYCLE_TYPE_OPEN);
    }

    @Override
    public void onFragmentPaused(FragmentManager fm, Fragment f) {
        super.onFragmentPaused(fm, f);
        SAnalytics.setScreenName(getSimpleClassName(f.getClass()),
                AnalyticsData.SCREEN_TYPE_FRAGMENT, AnalyticsData.LIFE_CYCLE_TYPE_CLOSE);
    }
}
