package com.example.gradepoint.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gradepoint.R;
import com.example.gradepoint.data.Semester;
import com.example.gradepoint.data.Subject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SemesterAdapter extends RecyclerView.Adapter<SemesterAdapter.SemesterViewHolder> {

    private List<Semester> semesterList = new ArrayList<>();
    private List<Subject> subjectList = new ArrayList<>();
    private final OnSemesterClickListener listener;

    public interface OnSemesterClickListener {
        void onSemesterClick(Semester semester);
    }

    public SemesterAdapter(OnSemesterClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public SemesterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_semester, parent, false);
        return new SemesterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SemesterViewHolder holder, int position) {
        Semester currentSemester = semesterList.get(position);
        holder.tvSemesterTitle.setText(currentSemester.getTitle());

        // Calculate GPA and credits specifically for this semester
        int semesterCredits = 0;
        double totalPointsTimesCredits = 0.0;

        if (subjectList != null) {
            for (Subject subject : subjectList) {
                if (subject.getSemesterOwnerId() == currentSemester.getId()) {
                    semesterCredits += subject.getCreditHours();
                    totalPointsTimesCredits += (subject.getGradePoints() * subject.getCreditHours());
                }
            }
        }

        if (semesterCredits > 0) {
            double gpa = totalPointsTimesCredits / semesterCredits;
            holder.tvSemesterGpa.setText(String.format(Locale.getDefault(), "GPA: %.2f", gpa));
            holder.tvSemesterCredits.setText(String.format(Locale.getDefault(), "%d Credit Hours", semesterCredits));
        } else {
            holder.tvSemesterGpa.setText("GPA: 0.00");
            holder.tvSemesterCredits.setText("0 Credit Hours");
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onSemesterClick(currentSemester);
            }
        });
    }

    @Override
    public int getItemCount() {
        return semesterList == null ? 0 : semesterList.size();
    }

    public void setSemesters(List<Semester> semesters) {
        this.semesterList = (semesters != null) ? semesters : new ArrayList<>();
        notifyDataSetChanged();
    }

    public void setSubjects(List<Subject> subjects) {
        this.subjectList = (subjects != null) ? subjects : new ArrayList<>();
        notifyDataSetChanged();
    }

    static class SemesterViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvSemesterTitle;
        private final TextView tvSemesterCredits;
        private final TextView tvSemesterGpa;

        public SemesterViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSemesterTitle = itemView.findViewById(R.id.tvSemesterTitle);
            tvSemesterCredits = itemView.findViewById(R.id.tvSemesterCredits);
            tvSemesterGpa = itemView.findViewById(R.id.tvSemesterGpa);
        }
    }
}