package ir.snappfood.keplertracker;


import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteFullException;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class SAnalytics {

    private static String LOG_TAG = "SNAPPFOOD ANALYTICS";
    public static String SHARED_PREFERENCES_TAG = "SAnalytics_SHARED_PREFERENCES";
    private static String KEY_FIRST_RUN = "KEY_FIRST_RUN1";

    private static SAnalytics ourInstance;

    private AppDatabase appDatabase;
    private Executor dbExecutor;
    private Config config;
    private Application application;
    private String userId = "";
    private String storeName = "";
    private DeviceInfo deviceInfo;
    private SharedPreferences sharedPreferences;
    private final static Object eventLock = new Object();
    private final static Object sendLock = new Object();

    private static SAnalytics getOurInstance() {
        if (ourInstance == null)
            ourInstance = new SAnalytics();
        return ourInstance;
    }

    public static void init(final Application context, Config config) {
        getOurInstance().application = context;
        getOurInstance().config = config;
        getOurInstance().appDatabase = Room.databaseBuilder(context,
                AppDatabase.class, "snappfood-analytics")
                .fallbackToDestructiveMigration()
                .build();
        getOurInstance().dbExecutor = Executors.newFixedThreadPool(2);
        getOurInstance().dbExecutor.execute(new Runnable() {
            @Override
            public void run() {
                synchronized (eventLock) {
                    getOurInstance().deviceInfo = new DeviceInfo(context);
                    checkForFirstRun();
                    //getOurInstance().appDatabase.analyticsDataDao().deleteAll();
                }
            }
        });
        context.registerActivityLifecycleCallbacks(new SActivityLifecycleCallbacks());
        getOurInstance().storeName = config.storeName;
        getOurInstance().userId = config.userId;
        getOurInstance().sharedPreferences =
                context.getSharedPreferences(SHARED_PREFERENCES_TAG, Context.MODE_PRIVATE);
    }

    public static class Config {

        private boolean logEnabled = true;
        private int batchCount = 50;
        private String storeName = "";
        private String userId = "";

        private Config() {
        }

        public static Config create() {
            return new Config();
        }

        public Config setLogEnabled(boolean logEnabled) {
            this.logEnabled = logEnabled;
            return this;
        }

        public Config setBatchCount(int batchCount) {
            this.batchCount = batchCount;
            return this;
        }

        public Config setStoreName(String storeName) {
            this.storeName = storeName;
            return this;
        }

        public Config setUserId(String userId) {
            this.userId = userId;
            return this;
        }

        public boolean isLogEnabled() {
            return logEnabled;
        }
    }

    public static void login(String userId) {
        getOurInstance().userId = userId;
    }

    public static void logout() {
        getOurInstance().userId = "";
    }

    static void setUpFragmentLifeCycleHandler(AppCompatActivity activity) {
        if (!initialized()) {
            log("SnappFood Analytics is not initialized yet");
            return;
        }

        activity.getSupportFragmentManager().registerFragmentLifecycleCallbacks(new SFragmentLifecycleCallbacks(), true);
    }

    public static void setScreenName(final String screenName, final String screenType, final String lifeCycleType) {
        if (!initialized()) {
            log("SnappFood Analytics is not initialized yet");
            return;
        }
        log("setScreenName : " + screenName + " " + screenType + " " + lifeCycleType);
        getOurInstance().dbExecutor.execute(new Runnable() {
            @Override
            public void run() {
                synchronized (eventLock) {
                }
                AnalyticsData analyticsData = new AnalyticsData(AnalyticsData.TYPE_SCREEN_VIEW,
                        screenName, null, getOurInstance().userId, getOurInstance().deviceInfo, false);
                analyticsData.setScreenType(screenType);
                analyticsData.setLifeCycleType(lifeCycleType);
                insertToDb(analyticsData);
            }
        });
    }

    private static void insertToDb(AnalyticsData analyticsData) {
        try {
            getOurInstance().appDatabase.analyticsDataDao().insert(analyticsData);
        } catch (Exception e) {
            e.printStackTrace();
            if (e instanceof SQLiteFullException) {
                getOurInstance().appDatabase.analyticsDataDao().deleteAll();
            }
        }
    }

    public static void trackEvent(final String name, final HashMap<String, String> params, final boolean legacy) {
        if (!initialized()) {
            log("SnappFood Analytics is not initialized yet");
            return;
        }
        log("trackEvent : " + name);
        getOurInstance().dbExecutor.execute(new Runnable() {
            @Override
            public void run() {
                synchronized (eventLock) {
                }
                AnalyticsData analyticsData = new AnalyticsData(
                        AnalyticsData.TYPE_EVENT, name, params, getOurInstance().userId, getOurInstance().deviceInfo, legacy);
                insertToDb(analyticsData);
            }
        });
    }

    private static boolean initialized() {
        return getOurInstance().application != null;
    }

    static void log(String log) {
        if (getOurInstance().config == null || getOurInstance().config.isLogEnabled()) {
            Log.e(LOG_TAG, log);
        }
    }

    public static void sendAnalyticsEvents(final int maxCount, final int threshold) {
        if (!initialized()) {
            log("SnappFood Analytics is not initialized yet");
            return;
        }

        getOurInstance().dbExecutor.execute(new Runnable() {
            @Override
            public void run() {
                synchronized (sendLock) {
                    int batchCount = maxCount != -1 ? maxCount : getOurInstance().config.batchCount;
                    int finalThreshold = (threshold < 1) ? batchCount : threshold;
                    long count = getOurInstance().appDatabase.analyticsDataDao().count();
                    log("check sending data with count = " + count);
                    List<AnalyticsData> events;
                    if (count >= finalThreshold) {
                        events = getOurInstance().appDatabase.analyticsDataDao().fetch(batchCount);
                        if (events != null && events.size() > 0) {
                            StringBuilder body = new StringBuilder();
                            for (AnalyticsData analyticsData : events) {
                                body.append(analyticsData.toString()).append("\n");
                            }
                            String stringBody = body.toString();
                            try {
                                boolean result = Networking.createHttpsURLConnection
                                        (Networking.API_ANALYTICS, stringBody, Networking.POST).isStatus();
                                if (result) {
                                    log("Data successfully sent");
                                    getOurInstance().appDatabase.analyticsDataDao().delete(events);
                                } else {
                                    log("Sending data failed");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                log("Sending data failed");
                            }
                        }
                    }
                }
            }
        });
    }

    private static void checkForFirstRun() {
        if (!getOurInstance().sharedPreferences.getBoolean(KEY_FIRST_RUN, true)) {
            return;
        }
        try {
            Networking.Response<String> ipResponse = Networking.createHttpsURLConnection
                    (Networking.API_IP, "", Networking.GET);
            if (ipResponse.isStatus()) {
                if (ipResponse.getResponse() != null
                        && ipResponse.getResponse().length() > 0) {
                    String ip = ipResponse.getResponse();
                    DeviceInfo deviceInfo = getOurInstance().deviceInfo;
                    FirstRunData firstRunData = new FirstRunData(ip, deviceInfo.getUserAgent(), deviceInfo.getDisplayWidth(),
                            deviceInfo.getDisplayHeight(), deviceInfo.getGpu(),
                            getOurInstance().sharedPreferences.getString(DeviceInfo.INSTALL_REFERRER, ""),
                            deviceInfo.getClient(), getOurInstance().config.storeName,
                            deviceInfo.getAppVersion(), deviceInfo.getDeviceName(),
                            deviceInfo.getDeviceManufacturer(), deviceInfo.getAndroidId(),
                            deviceInfo.getOsVersion());

                    Networking.Response rr = Networking.createHttpsURLConnection
                            (Networking.API_ANALYTICS, firstRunData.toString(), Networking.POST);
                    if (rr.isStatus()) {
                        getOurInstance().sharedPreferences.edit().putBoolean(KEY_FIRST_RUN, false).apply();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
