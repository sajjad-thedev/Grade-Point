package com.example.gradepoint.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface SemesterDao {
    @Insert
    long insert(Semester semester);

    @Update
    void update(Semester semester);

    @Delete
    void delete(Semester semester);

    @Query("SELECT * FROM semesters ORDER BY id ASC")
    LiveData<List<Semester>> getAllSemesters();
}
