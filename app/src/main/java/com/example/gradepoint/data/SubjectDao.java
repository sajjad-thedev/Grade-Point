package com.example.gradepoint.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface SubjectDao {
    @Insert
    long insert(Subject subject);

    @Update
    void update(Subject subject);

    @Delete
    void delete(Subject subject);

    @Query("SELECT * FROM subjects WHERE semesterOwnerId = :semesterId")
    LiveData<List<Subject>> getSubjectsForSemester(long semesterId);

    @Query("SELECT * FROM subjects")
    LiveData<List<Subject>> getAllSubjects();

}
