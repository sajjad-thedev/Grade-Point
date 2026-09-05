package com.example.gradepoint.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "semesters")
public class Semester {
    @PrimaryKey(autoGenerate = true)
    private long id;

    private String title;

    public Semester(String title) {
        this.title = title;
    }

    // Getters and Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
