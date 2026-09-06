package com.example.gradepoint.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gradepoint.R;
import com.example.gradepoint.data.Subject;
import com.example.gradepoint.viewmodel.MainViewModel;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Locale;

public class SemesterDetailActivity extends AppCompatActivity implements SubjectAdapter.OnSubjectDeleteListener {

    private MainViewModel mainViewModel;
    private SubjectAdapter adapter;
    private TextView tvSemesterGpaDetail;
    private TextView tvSemesterCreditsDetail;
    private View emptyStateSubjects;

    private long semesterId = -1;
    private String semesterTitle = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_semester_detail);

        // Retrieve Intent Extras
        if (getIntent() != null) {
            semesterId = getIntent().getLongExtra("EXTRA_SEMESTER_ID", -1);
            semesterTitle = getIntent().getStringExtra("EXTRA_SEMESTER_TITLE");
        }

        // 1. Setup Toolbar
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(semesterTitle != null ? semesterTitle : "Semester Details");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        // 2. Bind Views
        RecyclerView rvSubjects = findViewById(R.id.rvSubjects);
        tvSemesterGpaDetail = findViewById(R.id.tvSemesterGpaDetail);
        tvSemesterCreditsDetail = findViewById(R.id.tvSemesterCreditsDetail);
        emptyStateSubjects = findViewById(R.id.emptyStateSubjects);
        FloatingActionButton fabAddSubject = findViewById(R.id.fabAddSubject);

        // 3. Setup RecyclerView
        rvSubjects.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SubjectAdapter(this);
        rvSubjects.setAdapter(adapter);

        // 4. Initialize ViewModel and Observe Subjects for this Semester
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        if (semesterId != -1) {
            mainViewModel.getAllSubjects().observe(this, subjects -> {
                // Filter subjects specifically belonging to this semester
                if (subjects != null) {
                    List<Subject> semesterSubjects = filterSubjectsForSemester(subjects, semesterId);
                    adapter.setSubjects(semesterSubjects);

                    if (semesterSubjects.isEmpty()) {
                        emptyStateSubjects.setVisibility(View.VISIBLE);
                        rvSubjects.setVisibility(View.GONE);
                    } else {
                        emptyStateSubjects.setVisibility(View.GONE);
                        rvSubjects.setVisibility(View.VISIBLE);
                    }

                    calculateSemesterGpa(semesterSubjects);
                }
            });
        }

        // 5. FAB Click Listener
        fabAddSubject.setOnClickListener(v -> showAddSubjectDialog());
    }

    private List<Subject> filterSubjectsForSemester(List<Subject> allSubjects, long semesterId) {
        java.util.List<Subject> filteredList = new java.util.ArrayList<>();
        for (Subject subject : allSubjects) {
            if (subject.getSemesterOwnerId() == semesterId) {
                filteredList.add(subject);
            }
        }
        return filteredList;
    }

    private void showAddSubjectDialog() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setTitle("Add Subject");

        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_add_subject, null);
        final EditText etSubjectName = viewInflated.findViewById(R.id.etSubjectName);
        final EditText etCreditHours = viewInflated.findViewById(R.id.etCreditHours);
        final android.widget.AutoCompleteTextView actGrade = viewInflated.findViewById(R.id.actGrade);

        // Define standard university letter grades
        String[] grades = new String[]{"A", "A-", "B+", "B", "B-", "C+", "C", "C-", "D", "F"};
        android.widget.ArrayAdapter<String> gradeAdapter = new android.widget.ArrayAdapter<>(
                this, R.layout.item_dropdown_grade, grades
        );
        actGrade.setAdapter(gradeAdapter);

        builder.setView(viewInflated);

        builder.setPositiveButton("Add", (dialog, which) -> {
            String name = etSubjectName.getText().toString().trim();
            String creditsStr = etCreditHours.getText().toString().trim();
            String selectedGrade = actGrade.getText().toString().trim();

            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(creditsStr) || TextUtils.isEmpty(selectedGrade)) {
                Toast.makeText(SemesterDetailActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                int creditHours = Integer.parseInt(creditsStr);
                double gradePoints = convertGradeToPoints(selectedGrade);

                Subject newSubject = new Subject(semesterId, name, creditHours, gradePoints);
                mainViewModel.insertSubject(newSubject);
            } catch (NumberFormatException e) {
                Toast.makeText(SemesterDetailActivity.this, "Invalid credit hours", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    // Utility method to convert Letter Grades to Grade Points
    private double convertGradeToPoints(String grade) {
        switch (grade.toUpperCase()) {
            case "A":  return 4.0;
            case "A-": return 3.7;
            case "B+": return 3.3;
            case "B":  return 3.0;
            case "B-": return 2.7;
            case "C+": return 2.3;
            case "C":  return 2.0;
            case "C-": return 1.7;
            case "D":  return 1.0;
            case "F":
            default:   return 0.0;
        }
    }

    private void calculateSemesterGpa(List<Subject> subjects) {
        if (subjects == null || subjects.isEmpty()) {
            tvSemesterGpaDetail.setText("0.00");
            tvSemesterCreditsDetail.setText("0");
            return;
        }

        double totalGradePointsTimesCredits = 0;
        int totalCredits = 0;

        for (Subject subject : subjects) {
            totalCredits += subject.getCreditHours();
            totalGradePointsTimesCredits += (subject.getGradePoints() * subject.getCreditHours());
        }

        if (totalCredits > 0) {
            double gpa = totalGradePointsTimesCredits / totalCredits;
            tvSemesterGpaDetail.setText(String.format(Locale.getDefault(), "%.2f", gpa));
            tvSemesterCreditsDetail.setText(String.valueOf(totalCredits));
        } else {
            tvSemesterGpaDetail.setText("0.00");
            tvSemesterCreditsDetail.setText("0");
        }
    }

    @Override
    public void onDeleteClick(Subject subject) {
        new MaterialAlertDialogBuilder(this)
                .setTitle("Delete Subject")
                .setMessage("Are you sure you want to delete " + subject.getName() + "?")
                .setPositiveButton("Delete", (dialog, which) -> mainViewModel.deleteSubject(subject))
                .setNegativeButton("Cancel", null)
                .show();
    }
}