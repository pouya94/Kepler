package ir.snappfood.keplertracker;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {AnalyticsData.class}, version = 6)
public abstract class AppDatabase extends RoomDatabase {
    public abstract AnalyticsDataDao analyticsDataDao();
}