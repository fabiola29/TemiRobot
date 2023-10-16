package com.example.temi1.adaptor;

import static com.example.temi1.utils.CustomAnimation.collapse;
import static com.example.temi1.utils.CustomAnimation.expand;
import static com.example.temi1.utils.CustomAnimation.toggleArrow;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.temi1.R;
import com.example.temi1.model.TemiTask;

import java.util.ArrayList;
import java.util.List;

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.TaskViewHolder> {
    private final String TAG = "ActivityAdapter";
    private List<TemiTask> tasks;
    private OnTaskClickListener onTaskClickListener;
    private boolean[] expandedArrHelper;
    private Context context;
    private final OnSwitchTaskListener listener;


    public ActivityAdapter(Context context,ArrayList<TemiTask> tasks, OnSwitchTaskListener listener, OnTaskClickListener onTaskClickListener) {
        this.context = context;
        this.tasks = tasks != null ? tasks : new ArrayList<>();
        this.onTaskClickListener = onTaskClickListener;
        expandedArrHelper = new boolean[this.tasks.size()];
        this.listener =listener;
    }


    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.view_list_activity_card_item, parent, false);
        return new TaskViewHolder(v, listener, onTaskClickListener);
    }


    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.setText(position);

        holder.dropDownBtn.setOnClickListener(v -> {
            boolean show = toggleLayout(!expandedArrHelper[position], v, holder.expandLayout);
            expandedArrHelper[position] = show;
        });

        holder.descriptionTaskTv.setOnClickListener(v -> {
            showDescriptionDialog(tasks.get(position).getTaskActivity());
        });

        holder.deleteTaskBtn.setOnClickListener(v -> {
            onTaskClickListener.onTaskDeleteClick(position);
            boolean show = toggleLayout(!expandedArrHelper[position], v, holder.expandLayout);
            expandedArrHelper[position] = show;
        });

        TemiTask currentTask = tasks.get(position);
        holder.bind(currentTask);

        holder.editTaskBtn.setOnClickListener(v -> {
            onTaskClickListener.onTaskEditClick(position);
        });
    }

    @Override
    public void onViewRecycled(@NonNull TaskViewHolder holder) {
        super.onViewRecycled(holder);
        holder.executeTaskBtn.setOnCheckedChangeListener(null);
    }


    private void showDescriptionDialog(String description) {
        if (context != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.CustomAlertDialogStyle);
            builder.setTitle("Description")
                    .setMessage(description)
                    .setPositiveButton("Close", (dialog, which) -> {
                        dialog.dismiss();
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            Log.e(TAG, "The context is null");
        }
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView nameTv, locationTv,TimeTv,dayTv,descriptionTaskTv;
        ImageView dropDownBtn;
        SwitchCompat executeTaskBtn;
        CardView deleteTaskBtn, editTaskBtn;
        ConstraintLayout expandLayout;
        OnTaskClickListener onTaskClickListener;
        private final OnSwitchTaskListener listener;


        public TaskViewHolder(@NonNull View itemView, OnSwitchTaskListener listener, OnTaskClickListener onTaskClickListener) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.nameTv);
            locationTv = itemView.findViewById(R.id.location);
            TimeTv = itemView.findViewById(R.id.SchedulerTv);
            descriptionTaskTv = itemView.findViewById(R.id.descriptionTaskTv);
            dayTv = itemView.findViewById(R.id.dayTv);
            dropDownBtn = itemView.findViewById(R.id.dropDownBtn);
            expandLayout = itemView.findViewById(R.id.expandLayout);
            deleteTaskBtn = itemView.findViewById(R.id.deleteBtn);
            executeTaskBtn = itemView.findViewById(R.id.item_task_started);
            editTaskBtn = itemView.findViewById(R.id.editBtn);
            this.onTaskClickListener = onTaskClickListener;

            this.listener = listener;

        }


        void setText(int position) {
            nameTv.setText(tasks.get(position).getTaskName());
            dayTv.setText(tasks.get(position).getRecurringDaysText());
            int hour = tasks.get(position).getHour();
            int minute = tasks.get(position).getMinute();
            TimeTv.setText(hour + ":" + minute); // Combine hour and minute to set them as text
            locationTv.setText(tasks.get(position).getLocation());

        }

        public void bind(TemiTask temiTask) {
            // Sets the initial state of the switch
            executeTaskBtn.setOnCheckedChangeListener(null); // Remove the listener temporarily to avoid a loop
            executeTaskBtn.setChecked(temiTask.isStarted());
            executeTaskBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    // When the state of the switch changes, call the update method
                    listener.update(temiTask);
                    Log.d("update", "TaskViewHolder Listener");
                }
            });
        }
    }


    private boolean toggleLayout(boolean isExpanded, View v, ConstraintLayout layoutExpand) {
        toggleArrow(v, isExpanded);
        if (isExpanded) {
            expand(layoutExpand);
        } else {
            collapse(layoutExpand);
        }
        return isExpanded;
    }


    public void updateHelper(List<TemiTask> temiTasks) {
        Log.i(TAG, "task.size() = " + temiTasks.size());
        tasks.clear(); // Removes current items from tasks
        tasks.addAll(temiTasks); // Update the task list with the new data
        expandedArrHelper = new boolean[tasks.size()]; // Update the size of the array
        notifyDataSetChanged(); // Update the adapter to reflect the new data
    }


    public interface OnTaskClickListener {
        void onTaskExecuteClick(int position);
        void onTaskDeleteClick(int position);
        void onTaskEditClick(int position);
    }
}
