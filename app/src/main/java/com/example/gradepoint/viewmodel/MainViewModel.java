package com.example.gradepoint.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.gradepoint.data.Semester;
import com.example.gradepoint.data.Subject;
import com.example.gradepoint.repository.GradeRepository;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private final GradeRepository repository;
    private final LiveData<List<Semester>> allSemesters;
    private final LiveData<List<Subject>> allSubjects;

    public MainViewModel(@NonNull Application application) {
        super(application);
        repository = new GradeRepository(application);
        allSemesters = repository.getAllSemesters();
        allSubjects = repository.getAllSubjects();
    }

    public LiveData<List<Semester>> getAllSemesters() {
        return allSemesters;
    }

    public LiveData<List<Subject>> getAllSubjects() {
        return allSubjects;
    }

    public void insertSemester(Semester semester) {
        repository.insertSemester(semester);
    }

    public void deleteSemester(Semester semester) {
        repository.deleteSemester(semester);
    }
}