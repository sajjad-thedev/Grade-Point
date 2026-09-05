package com.example.gradepoint.data;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "subjects",
        foreignKeys = @ForeignKey(
                entity = Semester.class,
                parentColumns = "id",
                childColumns = "semesterOwnerId",
                onDelete = ForeignKey.CASCADE // Deleting a semester automatically deletes its subjects
        ),
        indices = {@Index("semesterOwnerId")}
)
public class Subject {
    @PrimaryKey(autoGenerate = true)
    private long id;

   private long semesterOwnerId;
   private String name;
   private int creditHours;
   private double gradePoints;

   public Subject(long semesterOwnerId, String name, int creditHours, double gradePoints) {
       this.semesterOwnerId = semesterOwnerId;
       this.name = name;
       this.creditHours = creditHours;
       this.gradePoints = gradePoints;
   }

   // Getters and Setters
    public double getGradePoints() {
        return gradePoints;
    }

    public void setGradePoints(double gradePoints) {
        this.gradePoints = gradePoints;
    }

    public int getCreditHours() {
        return creditHours;
    }

    public void setCreditHours(int creditHours) {
        this.creditHours = creditHours;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSemesterOwnerId() {
        return semesterOwnerId;
    }

    public void setSemesterOwnerId(long semesterOwnerId) {
        this.semesterOwnerId = semesterOwnerId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
