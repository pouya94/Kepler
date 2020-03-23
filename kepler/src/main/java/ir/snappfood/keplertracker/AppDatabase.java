package ir.snappfood.keplertracker;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {AnalyticsData.class}, version = 8)
abstract class AppDatabase extends RoomDatabase {
    abstract AnalyticsDataDao analyticsDataDao();
}