package com.example.gradepoint.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;

import com.example.gradepoint.data.AppDatabase;
import com.example.gradepoint.data.Semester;
import com.example.gradepoint.data.SemesterDao;
import com.example.gradepoint.data.Subject;
import com.example.gradepoint.data.SubjectDao;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GradeRepository {

    private final SemesterDao semesterDao;
    private final SubjectDao subjectDao;
    private final LiveData<List<Semester>> allSemesters;
    private final LiveData<List<Subject>> allSubjects;

    // ExecutorService for running database write operations on a background thread
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public GradeRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        semesterDao = db.semesterDao();
        subjectDao = db.subjectDao();
        allSemesters = semesterDao.getAllSemesters();
        allSubjects = subjectDao.getAllSubjects();
    }

    // --- Semester Operations ---

    public LiveData<List<Semester>> getAllSemesters() {
        return allSemesters;
    }

    public void insertSemester(Semester semester) {
        executorService.execute(() -> semesterDao.insert(semester));
    }

    public void deleteSemester(Semester semester) {
        executorService.execute(() -> semesterDao.delete(semester));
    }

    // --- Subject Operations ---

    public LiveData<List<Subject>> getAllSubjects() {
        return allSubjects;
    }

    public LiveData<List<Subject>> getSubjectsForSemester(long semesterId) {
        return subjectDao.getSubjectsForSemester(semesterId);
    }

    public void insertSubject(Subject subject) {
        executorService.execute(() -> subjectDao.insert(subject));
    }

    public void deleteSubject(Subject subject) {
        executorService.execute(() -> subjectDao.delete(subject));
    }
}