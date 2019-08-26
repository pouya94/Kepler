package ir.snappfood.keplertracker;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import java.util.HashMap;

@Entity
public class AnalyticsData {

    public static String TYPE_EVENT = "event";
    public static String TYPE_SCREEN_VIEW = "screen";

    public static String LIFE_CYCLE_TYPE_OPEN = "open";
    public static String LIFE_CYCLE_TYPE_CLOSE = "close";

    public static String SCREEN_TYPE_ACTIVITY = "activity";
    public static String SCREEN_TYPE_FRAGMENT = "fragment";


    @PrimaryKey(autoGenerate = true)
    private int id;
    private Long timeStamp;
    private String type;
    private String name;
    private String lifeCycleType;
    private String screenType;
    @TypeConverters({ParamsConverter.class})
    private HashMap<String, String> params;
    private String userId;
    @TypeConverters({ParamsConverter.class})
    private DeviceInfo deviceInfo;
    private boolean legacy;


    public AnalyticsData(String type, String name, HashMap<String, String> params, String userId, DeviceInfo deviceInfo, boolean legacy) {
        this.timeStamp = System.currentTimeMillis();
        this.type = type;
        this.name = name;
        this.params = params;
        this.userId = userId;
        this.deviceInfo = deviceInfo;
        this.legacy = legacy;
    }

    @Override
    public String toString() {
        StringBuilder stringParams = new StringBuilder();
        if (params != null && params.size() > 0) {
            for (String st : params.keySet()) {
                stringParams.append("\"param_").append(Utils.replaceSpaces(st)).append("\"=\"").append(Utils.replaceSpaces(params.get(st))).append("\",");
            }
        }
        String dbName = "applog";
        if (legacy) {
            dbName += "_old";
        }
        return dbName + "," +
                "\"timeStamp\"=\"" + timeStamp + "\"," +
                deviceInfo.toString() +
                stringParams.toString() +
                "\"client_id\"=\"" + id + "\"," +
                "\"type\"=\"" + Utils.replaceSpaces(type) + "\"," +
                "\"name\"=\"" + Utils.replaceSpaces(name) + "\"," +
                "\"lifeCycleType\"=\"" + Utils.replaceSpaces(lifeCycleType) + "\"," +
                "\"screenType\"=\"" + Utils.replaceSpaces(screenType) + "\" " +
                "\"userId\"=\"" + userId + "\"";
    }

    public String getScreenType() {
        return screenType;
    }

    public void setScreenType(String screenType) {
        this.screenType = screenType;
    }

    public String getLifeCycleType() {
        return lifeCycleType;
    }

    public void setLifeCycleType(String lifeCycleType) {
        this.lifeCycleType = lifeCycleType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HashMap<String, String> getParams() {
        return params;
    }

    public void setParams(HashMap<String, String> params) {
        this.params = params;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public DeviceInfo getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(DeviceInfo deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public boolean isLegacy() {
        return legacy;
    }

    public void setLegacy(boolean legacy) {
        this.legacy = legacy;
    }

}
