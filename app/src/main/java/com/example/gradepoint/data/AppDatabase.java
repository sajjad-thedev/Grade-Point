package com.example.gradepoint.data;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Semester.class, Subject.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public static volatile AppDatabase INSTANCE;

    public abstract SemesterDao semesterDao();

    public abstract SubjectDao subjectDao();

    public static AppDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "gradepoint_database"
                    ).build();
                }
            }
        }
        return INSTANCE;
    }
}
