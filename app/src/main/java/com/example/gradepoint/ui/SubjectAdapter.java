package com.example.gradepoint.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gradepoint.R;
import com.example.gradepoint.data.Subject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.SubjectViewHolder> {

    private List<Subject> subjectList = new ArrayList<>();
    private final OnSubjectDeleteListener deleteListener;

    public interface OnSubjectDeleteListener {
        void onDeleteClick(Subject subject);
    }

    public SubjectAdapter(OnSubjectDeleteListener deleteListener) {
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public SubjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_subject, parent, false);
        return new SubjectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectViewHolder holder, int position) {
        Subject currentSubject = subjectList.get(position);

        holder.tvSubjectName.setText(currentSubject.getName());
        holder.tvSubjectDetails.setText(String.format(
                Locale.getDefault(),
                "%d Credits | Grade Points: %.2f",
                currentSubject.getCreditHours(),
                currentSubject.getGradePoints()
        ));

        holder.btnDeleteSubject.setOnClickListener(v -> {
            if (deleteListener != null) {
                deleteListener.onDeleteClick(currentSubject);
            }
        });
    }

    @Override
    public int getItemCount() {
        return subjectList == null ? 0 : subjectList.size();
    }

    public void setSubjects(List<Subject> subjects) {
        if (subjects != null) {
            this.subjectList = subjects;
        } else {
            this.subjectList = new ArrayList<>();
        }
        notifyDataSetChanged();
    }

    static class SubjectViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvSubjectName;
        private final TextView tvSubjectDetails;
        private final ImageButton btnDeleteSubject;

        public SubjectViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSubjectName = itemView.findViewById(R.id.tvSubjectName);
            tvSubjectDetails = itemView.findViewById(R.id.tvSubjectDetails);
            btnDeleteSubject = itemView.findViewById(R.id.btnDeleteSubject);
        }
    }
}