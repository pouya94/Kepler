package ir.snappfood.keplertracker;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface AnalyticsDataDao {
    @Insert
    void insert(AnalyticsData... data);

    @Query("Select * FROM AnalyticsData LIMIT :limit")
    List<AnalyticsData> fetch(int limit);

    @Query("Select Count(*) FROM AnalyticsData")
    int count();

    @Delete
    void delete(List<AnalyticsData> events);

    @Query("Delete FROM AnalyticsData")
    void deleteAll();
}
