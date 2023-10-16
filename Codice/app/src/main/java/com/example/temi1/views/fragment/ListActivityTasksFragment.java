package com.example.temi1.views.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.temi1.adaptor.ActivityListAdapter;
import com.example.temi1.db.TaskTemi.TaskManager;
import com.example.temi1.db.TaskTemi.TemiTaskRepository;
import com.example.temi1.model.TemiTask;
import com.example.temi1.viewmodel.GlobalViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import com.example.temi1.R;
import com.robotemi.sdk.Robot;

public class ListActivityTasksFragment extends Fragment {

    private GlobalViewModel viewModel;
    private TemiTaskRepository repository;
    private Calendar currentCalendar;
    private ImageButton exitBtn;

    private ConstraintLayout parentLayout;
    private TaskManager taskManager;
    private Robot robot;
    private NavController navController;
    private ArrayList<TemiTask> tasks;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        currentCalendar = Calendar.getInstance();

        try {
            repository = new TemiTaskRepository(getActivity().getApplication());
        } catch (Exception e) {
            e.printStackTrace();
        }

        viewModel = new ViewModelProvider(this).get(GlobalViewModel.class);

        taskManager = new TaskManager(requireActivity().getApplicationContext(), new TemiTaskRepository(getActivity().getApplication()));

        tasks = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_tasks, container, false);
    }

    /**
     * Designed to close the keyboard when not in use or when you decide not to use it
     */
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
    }

    private RecyclerView listActivityRv;
    private final String TAG = "ListViewActivityFragment";

    @Override
    public void onViewCreated(@NonNull final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        // get an instance of the robot in order to begin using its features.
        robot = Robot.getInstance();

        listActivityRv = view.findViewById(R.id.listRv);
        tasks = new ArrayList<>(); // Initialize the task list
        initializeRecylerView();
        attachLiveDataToRecyclerView();

        exitBtn = view.findViewById(R.id.btn_back_admin);
        exitBtn.setOnClickListener(v -> button_back());

        taskManager.getAllTasks().observe(getViewLifecycleOwner(), new Observer<List<TemiTask>>() {
            @Override
            public void onChanged(List<TemiTask> tasks) {
                // Ottieni l'orario corrente
                Calendar currentCalendar = Calendar.getInstance();

                if (tasks != null && !tasks.isEmpty()) {
                    // Trova l'attività che deve essere eseguita in base all'orario
                    TemiTask nextTask = null;
                    long minTimeDifference = Long.MAX_VALUE;

                    for (TemiTask task : tasks) {
                        // Ottieni l'orario specificato nell'attività
                        Calendar taskTime = Calendar.getInstance();
                        taskTime.set(Calendar.HOUR_OF_DAY, task.getHour());
                        taskTime.set(Calendar.MINUTE, task.getMinute());

                        // Calcola la differenza di tempo tra l'orario dell'attività e l'orario corrente
                        long timeDifference = taskTime.getTimeInMillis() - currentCalendar.getTimeInMillis();

                        // Se l'attività ha un orario futuro più vicino, aggiorna nextTask
                        if (timeDifference >= 0 && timeDifference < minTimeDifference) {
                            minTimeDifference = timeDifference;
                            nextTask = task;
                        }
                    }

                    // Esegui l'attività più vicina all'orario corrente
                    if (nextTask != null) {
                        taskManager.executeTask(nextTask);
                    }
                }
            }
        });
    }

    private ActivityListAdapter activityListAdapter;

    private void initializeRecylerView() {
        Log.i(TAG, "buildRecyclerView called");
        activityListAdapter = new ActivityListAdapter(tasks);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        listActivityRv.setAdapter(activityListAdapter);
        listActivityRv.setLayoutManager(mLayoutManager);
    }

    private void attachLiveDataToRecyclerView() {
        final Observer<List<TemiTask>> taskListObserver = temiTasks -> {
            Log.i(TAG, "onChanged called");
            if (temiTasks != null) {
                // Update the tasks list and adapter with all tasks
                tasks.clear();
                tasks.addAll(temiTasks);
                activityListAdapter.updateHelper(temiTasks);
                activityListAdapter.notifyDataSetChanged();
            }
        };
        viewModel.getTaskLiveDataFromRepo().observe(getViewLifecycleOwner(), taskListObserver);
    }

    private void button_back(){
        GlobalViewModel viewModel = new ViewModelProvider(getActivity()).get(GlobalViewModel.class);
        viewModel.initializeForRouteCreation();
        navController.navigate(R.id.action_ListActivityTasksFragment_to_RegisterOrViewActivityfragment);
    }

   private boolean isCurrentTimeMatchingTaskTime(Calendar currentCalendar, TemiTask task) {
        // Ottieni l'orario specificato nell'attività
        Calendar taskTime = Calendar.getInstance();
        taskTime.set(Calendar.HOUR_OF_DAY, task.getHour());
        taskTime.set(Calendar.MINUTE, task.getMinute());

        // Verifica se l'orario corrente corrisponde all'orario specificato nell'attività
        return currentCalendar.get(Calendar.HOUR_OF_DAY) == taskTime.get(Calendar.HOUR_OF_DAY)
                && currentCalendar.get(Calendar.MINUTE) == taskTime.get(Calendar.MINUTE);
    }

}
