package ir.snappfood.keplertracker;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import static ir.snappfood.keplertracker.Utils.getSimpleClassName;


public class SActivityLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {
    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        if (activity instanceof AppCompatActivity) {
            SAnalytics.setUpFragmentLifeCycleHandler((AppCompatActivity) activity);
        }
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        SAnalytics.setScreenName(getSimpleClassName(activity.getClass()),
                AnalyticsData.SCREEN_TYPE_ACTIVITY, AnalyticsData.LIFE_CYCLE_TYPE_OPEN);
        SAnalytics.sendAnalyticsEvents(-1, -1);
    }

    @Override
    public void onActivityPaused(Activity activity) {
        SAnalytics.setScreenName(getSimpleClassName(activity.getClass()),
                AnalyticsData.SCREEN_TYPE_ACTIVITY, AnalyticsData.LIFE_CYCLE_TYPE_CLOSE);
    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }
}
