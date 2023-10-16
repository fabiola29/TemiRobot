package com.example.temi1.adaptor;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.temi1.R;
import com.example.temi1.model.TemiTask;

import java.util.ArrayList;
import java.util.List;

public class ActivityListAdapter extends RecyclerView.Adapter<ActivityListAdapter.ActivityViewHolder> {

    private final String TAG = "ActivityListAdapter";
    private List<TemiTask> tasks;
    private boolean[] expandedArrHelper;


    public ActivityListAdapter(ArrayList<TemiTask> tasks) {
        this.tasks = tasks;
    }


    @NonNull
    @Override
    public ActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.tasks_execution_card_item, parent, false);
        return new ActivityViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull ActivityViewHolder holder, int position) {

        holder.setText(position);
    }


    @Override
    public int getItemCount() {
        return tasks.size();
    }


    public class ActivityViewHolder extends RecyclerView.ViewHolder {
        TextView taskListTitleTv, taskListTimeTv,taskListDayTv,taskListLocationTv;


        public ActivityViewHolder(@NonNull View itemView) {
            super(itemView);
            taskListTitleTv = itemView.findViewById(R.id.taskListTitleTv);
            taskListTimeTv = itemView.findViewById(R.id.taskListTimeTv);
            taskListDayTv = itemView.findViewById(R.id.taskListDayTv);
            taskListLocationTv = itemView.findViewById(R.id.taskListLocationTv);
        }


        void setText(int position) {
            taskListTitleTv.setText(tasks.get(position).getTaskName());
            taskListDayTv.setText(tasks.get(position).getRecurringDaysText());
            int hour = tasks.get(position).getHour();
            int minute = tasks.get(position).getMinute();
            taskListTimeTv.setText(hour + ":" + minute); // Combine hour and minute to set them as text
            taskListLocationTv.setText(tasks.get(position).getLocation());

        }
    }

    public void updateHelper(List<TemiTask> temiTasks) {
        Log.i(TAG, "task.size() = " + temiTasks.size());
        tasks.clear(); // Removes current items from tasks
        tasks.addAll(temiTasks); // Update the task list with the new data
        expandedArrHelper = new boolean[tasks.size()]; // Update the size of the array
        notifyDataSetChanged(); // Update the adapter to reflect the new data
    }

}
