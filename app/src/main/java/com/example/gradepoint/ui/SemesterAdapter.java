package com.example.gradepoint.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gradepoint.R;
import com.example.gradepoint.data.Semester;

import java.util.ArrayList;
import java.util.List;

public class SemesterAdapter extends RecyclerView.Adapter<SemesterAdapter.SemesterViewHolder> {

    private List<Semester> semesterList = new ArrayList<>();
    private final OnSemesterClickListener listener;

    // Interface for click events back to MainActivity
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

        // Placeholder summaries until dynamic subject counts/GPAs are populated
        holder.tvSemesterCredits.setText("0 Credit Hours");
        holder.tvSemesterGpa.setText("GPA: 0.00");

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

    // Called when LiveData emits a new semester list from Room
    public void setSemesters(List<Semester> semesters) {
        if (semesters != null) {
            this.semesterList = semesters;
        } else {
            this.semesterList = new ArrayList<>();
        }
        notifyDataSetChanged();
    }

    // ViewHolder holds direct references to item_semester.xml views
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