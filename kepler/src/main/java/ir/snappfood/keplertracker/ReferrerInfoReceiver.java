package ir.snappfood.keplertracker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class ReferrerInfoReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if ("com.android.vending.INSTALL_REFERRER".equals(action)) {
            SharedPreferences sharedPref = context.getSharedPreferences(SAnalytics.SHARED_PREFERENCES_TAG, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(DeviceInfo.INSTALL_REFERRER,
                    intent.getStringExtra("referrer")).commit();
        }
    }
}
