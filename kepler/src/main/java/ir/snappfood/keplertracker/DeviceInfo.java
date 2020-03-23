package ir.snappfood.keplertracker;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.opengl.GLES20;
import android.os.Build;
import android.util.DisplayMetrics;
import android.webkit.WebSettings;

import java.util.Date;
import java.util.Locale;

class DeviceInfo {

    private final String MALFORMED = "malformed";
    private final String SMALL = "small";
    private final String NORMAL = "normal";
    private final String LONG = "long";
    private final String LARGE = "large";
    private final String XLARGE = "xlarge";
    private final String LOW = "low";
    private final String MEDIUM = "medium";
    private final String HIGH = "high";
    private final String REFERRER = "referrer";
    public static final String INSTALL_REFERRER = "installReferrer";

    private String playAdId;
    private String androidId;
    private String sdkVersion;
    private String packageName;
    private String appVersion;
    private String deviceType;
    private String deviceName;
    private String deviceManufacturer;
    private String osName;
    private String osVersion;
    private String apiLevel;
    private String language;
    private String country;
    private String screenSize;
    private String screenFormat;
    private String screenDensity;
    private String displayWidth;
    private String displayHeight;
    private String hardwareName;
    private String abi;
    private String buildName;
    private String vmInstructionSet;
    private String appInstallTime;
    private String appUpdateTime;
    private String mcc;
    private String mnc;
    private String gpu = "";
    private String userAgent;
    private int connectivity_type;
    private int network_type;

    public DeviceInfo() {

    }

    public DeviceInfo(final Context context) {
        Resources resources = context.getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();
        Locale locale = Utils.getLocale(configuration);
        int screenLayout = configuration.screenLayout;
        packageName = getPackageName(context);
        appVersion = getAppVersion(context);
        deviceType = getDeviceType(screenLayout);
        deviceName = getDeviceName();
        deviceManufacturer = getDeviceManufacturer();
        osName = getOsName();
        osVersion = getOsVersion();
        apiLevel = getApiLevel();
        language = getLanguage(locale);
        country = getCountry(locale);
        screenSize = getScreenSize(screenLayout);
        screenFormat = getScreenFormat(screenLayout);
        screenDensity = getScreenDensity(displayMetrics);
        displayWidth = getDisplayWidth(displayMetrics);
        displayHeight = getDisplayHeight(displayMetrics);
        sdkVersion = BuildConfig.VERSION_NAME;
        hardwareName = getHardwareName();
        abi = getABI();
        buildName = getBuildName();
        vmInstructionSet = getVmInstructionSet();
        appInstallTime = getAppInstallTime(context);
        appUpdateTime = getAppUpdateTime(context);
        mcc = Utils.getMcc(context);
        mnc = Utils.getMnc(context);
        connectivity_type = Utils.getConnectivityType(context);
        network_type = Utils.getNetworkType(context);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            try {
                gpu = getGpuModel();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        playAdId = Utils.getPlayAdId(context);
        androidId = Utils.getAndroidId(context);
        userAgent = getUserAgent(context);

    }

    @Override
    public String toString() {
        return "\"package_name\"=\"" + Utils.replaceSpaces(packageName) + "\"," +
                "\"app_version\"=\"" + Utils.replaceSpaces(appVersion) + "\"," +
                "\"device_type\"=\"" + Utils.replaceSpaces(deviceType) + "\"," +
                "\"gpu\"=\"" + Utils.replaceSpaces(gpu) + "\"," +
                "\"device_name\"=\"" + Utils.replaceSpaces(deviceName) + "\"," +
                "\"device_manufacturer\"=\"" + Utils.replaceSpaces(deviceManufacturer) + "\"," +
                "\"os_name\"=\"" + Utils.replaceSpaces(osName) + "\"," +
                "\"os_version\"=\"" + Utils.replaceSpaces(osVersion) + "\"," +
                "\"api_level\"=\"" + Utils.replaceSpaces(apiLevel) + "\"," +
                "\"language\"=\"" + Utils.replaceSpaces(language) + "\"," +
                "\"country\"=\"" + Utils.replaceSpaces(country) + "\"," +
                "\"screen_size\"=\"" + Utils.replaceSpaces(screenSize) + "\"," +
                "\"screen_format\"=\"" + Utils.replaceSpaces(screenFormat) + "\"," +
                "\"screen_density\"=\"" + Utils.replaceSpaces(screenDensity) + "\"," +
                "\"display_width\"=\"" + Utils.replaceSpaces(displayWidth) + "\"," +
                "\"display_height\"=\"" + Utils.replaceSpaces(displayHeight) + "\"," +
                "\"hardware_name\"=\"" + Utils.replaceSpaces(hardwareName) + "\"," +
                "\"cpu_type\"=\"" + Utils.replaceSpaces(abi) + "\"," +
                "\"os_build\"=\"" + Utils.replaceSpaces(buildName) + "\"," +
                "\"vm_isa\"=\"" + Utils.replaceSpaces(vmInstructionSet) + "\"," +
                "\"mcc\"=\"" + Utils.replaceSpaces(mcc) + "\"," +
                "\"mnc\"=\"" + Utils.replaceSpaces(mnc) + "\"," +
                "\"connectivity_type\"=\"" + Utils.replaceSpaces(String.valueOf(connectivity_type)) + "\"," +
                "\"network_type\"=\"" + Utils.replaceSpaces(String.valueOf(network_type)) + "\"," +
                "\"android_id\"=\"" + Utils.replaceSpaces(androidId) + "\"," +
                "\"installed_at\"=\"" + Utils.replaceSpaces(appInstallTime) + "\"," +
                "\"updated_at\"=\"" + Utils.replaceSpaces(appUpdateTime) + "\"," +
                "\"sdkVersion\"=\"" + Utils.replaceSpaces(sdkVersion) + "\"," +
                "\"gps_adid\"=\"" + Utils.replaceSpaces(playAdId) + "\",";
    }

    public String getAndroidId() {
        return androidId;
    }

    public String getDisplayWidth() {
        return displayWidth;
    }

    public String getDisplayHeight() {
        return displayHeight;
    }

    public String getGpu() {
        return gpu;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public String getClient() {
        return getOsName();
    }

    public String getAppVersion() {
        return appVersion;
    }

    private String getPackageName(Context context) {
        return context.getPackageName();
    }

    private String getAppVersion(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            String name = context.getPackageName();
            PackageInfo info = packageManager.getPackageInfo(name, 0);
            return info.versionName;
        } catch (Exception e) {
            return null;
        }
    }

    private String getDeviceType(int screenLayout) {
        int screenSize = screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;

        switch (screenSize) {
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                return "phone";
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
            case 4:
                return "tablet";
            default:
                return null;
        }
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private String getGpuModel() {
        EglCore eglCore = new EglCore(null, EglCore.FLAG_TRY_GLES3);
        OffscreenSurface surface = new OffscreenSurface(eglCore, 1, 1);
        surface.makeCurrent();

        final String model = GLES20.glGetString(GLES20.GL_RENDERER);

        surface.release();
        eglCore.release();

        return model;
    }

    public String getDeviceName() {
        return Build.MODEL;
    }

    private String getUserAgent(Context context) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                return WebSettings.getDefaultUserAgent(context);
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {

            } catch (Exception e1) {
                e1.printStackTrace();
                return System.getProperty("http.agent");
            }
        }
        return "";
    }

    public String getDeviceManufacturer() {
        return Build.MANUFACTURER;
    }

    private String getOsName() {
        return "android";
    }

    public String getOsVersion() {
        return Build.VERSION.RELEASE;
    }

    private String getApiLevel() {
        return "" + Build.VERSION.SDK_INT;
    }

    private String getLanguage(Locale locale) {
        return locale.getLanguage();
    }

    private String getCountry(Locale locale) {
        return locale.getCountry();
    }

    private String getBuildName() {
        return Build.ID;
    }

    private String getHardwareName() {
        return Build.DISPLAY;
    }

    private String getScreenSize(int screenLayout) {
        int screenSize = screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;

        switch (screenSize) {
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                return SMALL;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                return NORMAL;
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                return LARGE;
            case 4:
                return XLARGE;
            default:
                return null;
        }
    }

    private String getScreenFormat(int screenLayout) {
        int screenFormat = screenLayout & Configuration.SCREENLAYOUT_LONG_MASK;

        switch (screenFormat) {
            case Configuration.SCREENLAYOUT_LONG_YES:
                return LONG;
            case Configuration.SCREENLAYOUT_LONG_NO:
                return NORMAL;
            default:
                return null;
        }
    }

    private String getScreenDensity(DisplayMetrics displayMetrics) {
        int density = displayMetrics.densityDpi;
        int low = (DisplayMetrics.DENSITY_MEDIUM + DisplayMetrics.DENSITY_LOW) / 2;
        int high = (DisplayMetrics.DENSITY_MEDIUM + DisplayMetrics.DENSITY_HIGH) / 2;

        if (density == 0) {
            return null;
        } else if (density < low) {
            return LOW;
        } else if (density > high) {
            return HIGH;
        }
        return MEDIUM;
    }

    private String getDisplayWidth(DisplayMetrics displayMetrics) {
        return String.valueOf(displayMetrics.widthPixels);
    }

    private String getDisplayHeight(DisplayMetrics displayMetrics) {
        return String.valueOf(displayMetrics.heightPixels);
    }

    private String getABI() {
        String[] SupportedABIS = Utils.getSupportedAbis();

        // SUPPORTED_ABIS is only supported in API level 21
        // get CPU_ABI instead
        if (SupportedABIS == null || SupportedABIS.length == 0) {
            return Utils.getCpuAbi();
        }

        return SupportedABIS[0];
    }

    private String getVmInstructionSet() {
        String instructionSet = Utils.getVmInstructionSet();
        return instructionSet;
    }

    private String getAppInstallTime(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), PackageManager.GET_PERMISSIONS);

            String appInstallTime = Utils.dateFormatter.format(new Date(packageInfo.firstInstallTime));

            return appInstallTime;
        } catch (Exception ex) {
            return null;
        }
    }

    private String getAppUpdateTime(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), PackageManager.GET_PERMISSIONS);

            String appInstallTime = Utils.dateFormatter.format(new Date(packageInfo.lastUpdateTime));

            return appInstallTime;
        } catch (Exception ex) {
            return null;
        }
    }
}