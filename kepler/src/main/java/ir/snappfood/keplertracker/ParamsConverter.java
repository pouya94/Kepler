package ir.snappfood.keplertracker;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;

public class ParamsConverter {

    private static Gson gson = new Gson();

    @TypeConverter
    public static HashMap<String, String> StringToToppingGroup(String serializedToppings) {
        Type listType = new TypeToken<HashMap<String, String>>() {
        }.getType();
        return gson.fromJson(serializedToppings, listType);
    }

    @TypeConverter
    public static String ToppingGroupToString(HashMap<String, String> toppings) {
        return gson.toJson(toppings);
    }

    @TypeConverter
    public static DeviceInfo StringToDeviceInfo(String serializedInfo) {
        return gson.fromJson(serializedInfo, DeviceInfo.class);
    }

    @TypeConverter
    public static String DeviceInfoToString(DeviceInfo deviceInfo) {
        return gson.toJson(deviceInfo);
    }
}