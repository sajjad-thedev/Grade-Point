package com.example.gradepoint;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gradepoint.R;
import com.example.gradepoint.data.Semester;
import com.example.gradepoint.data.Subject;
import com.example.gradepoint.ui.SemesterAdapter;
import com.example.gradepoint.ui.SemesterDetailActivity;
import com.example.gradepoint.viewmodel.MainViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements SemesterAdapter.OnSemesterClickListener {

    private MainViewModel mainViewModel;
    private SemesterAdapter adapter;
    private TextView tvCgpaValue;
    private TextView tvTotalCredits;
    private View emptyStateView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. Bind Views
        RecyclerView rvSemesters = findViewById(R.id.rvSemesters);
        tvCgpaValue = findViewById(R.id.tvCgpaValue);
        tvTotalCredits = findViewById(R.id.tvTotalCredits);
        emptyStateView = findViewById(R.id.emptyStateView);
        FloatingActionButton fabAddSemester = findViewById(R.id.fabAddSemester);

        // 2. Setup RecyclerView
        rvSemesters.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SemesterAdapter(this);
        rvSemesters.setAdapter(adapter);

        // 3. Initialize ViewModel
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        // 4. Observe Semesters LiveData
        mainViewModel.getAllSemesters().observe(this, semesters -> {
            adapter.setSemesters(semesters);
            if (semesters == null || semesters.isEmpty()) {
                emptyStateView.setVisibility(View.VISIBLE);
                rvSemesters.setVisibility(View.GONE);
            } else {
                emptyStateView.setVisibility(View.GONE);
                rvSemesters.setVisibility(View.VISIBLE);
            }
        });

        // 5. Observe Subjects LiveData to calculate CGPA dynamically
        mainViewModel.getAllSubjects().observe(this, this::calculateCgpa);

        // 6. FAB click to add new semester
        fabAddSemester.setOnClickListener(v -> showAddSemesterDialog());
    }

    @Override
    public void onSemesterClick(Semester semester) {
        // Navigate to SemesterDetailActivity and pass the semester ID
        Intent intent = new Intent(MainActivity.this, SemesterDetailActivity.class);
        intent.putExtra("EXTRA_SEMESTER_ID", semester.getId());
        intent.putExtra("EXTRA_SEMESTER_TITLE", semester.getTitle());
        startActivity(intent);
    }

    private void showAddSemesterDialog() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setTitle("Add New Semester");

        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_add_semester, null);
        final EditText input = viewInflated.findViewById(R.id.etSemesterTitle);
        builder.setView(viewInflated);

        builder.setPositiveButton("Add", (dialog, which) -> {
            String title = input.getText().toString().trim();
            if (!TextUtils.isEmpty(title)) {
                Semester newSemester = new Semester(title);
                mainViewModel.insertSemester(newSemester);
            } else {
                Toast.makeText(MainActivity.this, "Title cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void calculateCgpa(List<Subject> subjects) {
        if (subjects == null || subjects.isEmpty()) {
            tvCgpaValue.setText("0.00");
            tvTotalCredits.setText("0 Total Credits");
            return;
        }

        double totalGradePointsTimesCredits = 0;
        int totalCredits = 0;

        for (Subject subject : subjects) {
            totalCredits += subject.getCreditHours();
            totalGradePointsTimesCredits += (subject.getGradePoints() * subject.getCreditHours());
        }

        if (totalCredits > 0) {
            double cgpa = totalGradePointsTimesCredits / totalCredits;
            tvCgpaValue.setText(String.format(Locale.getDefault(), "%.2f", cgpa));
            tvTotalCredits.setText(String.format(Locale.getDefault(), "%d Total Credits", totalCredits));
        } else {
            tvCgpaValue.setText("0.00");
            tvTotalCredits.setText("0 Total Credits");
        }
    }
}