package ir.snappfood.keplertracker;


import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {AnalyticsData.class}, version = 8)
abstract class AppDatabase extends RoomDatabase {
    abstract AnalyticsDataDao analyticsDataDao();
}