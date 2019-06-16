package ir.snappfood.keplertracker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class Utils {
    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'Z";

    public static final SimpleDateFormat dateFormatter = new SimpleDateFormat(DATE_FORMAT, Locale.US);

    public static String replaceSpaces(String input) {
        if (input != null) {
           return Uri.encode(input.replaceAll(" ","_"));
        }
        return "";
    }

    public static String getSimpleClassName(Class aClass) {
        return aClass.getSimpleName().replace("Activity", "").replace("Fragment", "");
    }

    public static Locale getLocale(Configuration configuration) {
        Locale locale = Reflection.getLocaleFromLocaleList(configuration);
        if (locale != null) {
            return locale;
        }
        return Reflection.getLocaleFromField(configuration);
    }

    public static String getPlayAdId(Context context) {
        return Reflection.getPlayAdId(context);
    }

    public static String getAndroidId(Context context) {
        return Reflection.getAndroidId(context);
    }

    public static String[] getSupportedAbis() {
        return Reflection.getSupportedAbis();
    }

    public static String getCpuAbi() {
        return Reflection.getCpuAbi();
    }

    public static String getVmInstructionSet() {
        return Reflection.getVmInstructionSet();
    }

    public static String getMcc(Context context) {
        try {
            TelephonyManager tel = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String networkOperator = tel.getNetworkOperator();

            if (TextUtils.isEmpty(networkOperator)) {
                return null;
            }
            return networkOperator.substring(0, 3);
        } catch (Exception ex) {
            return null;
        }
    }

    public static String getMnc(Context context) {
        try {
            TelephonyManager tel = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String networkOperator = tel.getNetworkOperator();

            if (TextUtils.isEmpty(networkOperator)) {
                return null;
            }
            return networkOperator.substring(3);
        } catch (Exception ex) {
            return null;
        }
    }

    public static int getConnectivityType(Context context) {
        int connectivityType = -1; // default value that will not be send

        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            @SuppressLint("MissingPermission") NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            connectivityType = activeNetwork.getType();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return connectivityType;
    }

    public static int getNetworkType(Context context) {
        int networkType = -1; // default value that will not be send

        try {
            TelephonyManager teleMan =
                    (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            networkType = teleMan.getNetworkType();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return networkType;
    }

}
