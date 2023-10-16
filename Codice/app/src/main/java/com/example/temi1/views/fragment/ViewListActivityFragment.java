package com.example.temi1.views.fragment;

import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.temi1.R;
import com.example.temi1.adaptor.ActivityAdapter;
import com.example.temi1.adaptor.OnSwitchTaskListener;
import com.example.temi1.model.TemiTask;
import com.example.temi1.viewmodel.GlobalViewModel;
import com.robotemi.sdk.Robot;
import com.robotemi.sdk.TtsRequest;
import com.robotemi.sdk.listeners.OnRobotReadyListener;

import java.util.ArrayList;
import java.util.List;

public class ViewListActivityFragment extends Fragment implements ActivityAdapter.OnTaskClickListener, Robot.TtsListener, OnRobotReadyListener, OnSwitchTaskListener {

    private ImageButton bt_back;
    private NavController navController;
    private GlobalViewModel viewModel;
    private ArrayList<TemiTask> tasks;
    private RecyclerView listActivityRv;
    private final String TAG = "ViewListActivityFragment";
    private ActivityAdapter activityAdapter;
    private Robot robot;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(getActivity()).get(GlobalViewModel.class);
        tasks = new ArrayList<>();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_view_list_activity, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        listActivityRv = view.findViewById(R.id.listActivityRv);
        tasks = new ArrayList<>(); // Initialize the task list
        initializeRecylerView();
        attachLiveDataToRecyclerView();

        bt_back =  view.findViewById(R.id.btn_back_view_Location);
        bt_back.setOnClickListener(v -> button_back());

        robot = Robot.getInstance(); // get an instance of the robot
    }


    private void button_back(){
        GlobalViewModel viewModel = new ViewModelProvider(getActivity()).get(GlobalViewModel.class);
        viewModel.initializeForTaskCreation();
        navController.navigate(R.id.action_ViewActivityFragment_to_ActivityFragment);
    }


    private void initializeRecylerView() {
        Log.i(TAG, "buildRecyclerView called");
        activityAdapter = new ActivityAdapter(requireContext(), tasks, this, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        listActivityRv.setAdapter(activityAdapter);
        listActivityRv.setLayoutManager(mLayoutManager);
    }


    private void attachLiveDataToRecyclerView() {
        final int userId = viewModel.getLoggedInUserId(); // Get the logged-in user's ID
        final Observer<List<TemiTask>> taskListObserver = temiTasks -> {
            Log.i(TAG, "onChanged called");
            if (temiTasks != null) {
                // Filter tasks by user ID
                List<TemiTask> filteredTasks = new ArrayList<>();
                for (TemiTask task : temiTasks) {
                    if (task.getUserId() == userId) {
                        filteredTasks.add(task);
                    }
                }

                // Update the tasks list and adapter with the filtered tasks
                tasks.clear();
                tasks.addAll(filteredTasks);
                activityAdapter.updateHelper(filteredTasks);
                activityAdapter.notifyDataSetChanged();
            }
        };
        viewModel.getTaskLiveDataFromRepo().observe(getViewLifecycleOwner(), taskListObserver);
    }


    /**
     * Setting up all the event listeners
     */
    @Override
    public void onStart() {
        super.onStart();

        robot.addOnRobotReadyListener(this);
        robot.addTtsListener(this);
    }


    /**
     * Removing the event listeners upon leaving the app.
     */
    @Override
    public void onStop() {
        super.onStop();

        robot.removeTtsListener(this);
        robot.removeOnRobotReadyListener(this);
    }


    @Override
    public void onRobotReady(boolean isReady) {
        if (isReady) {
            try {
                final ActivityInfo activityInfo = getActivity().getPackageManager().getActivityInfo(getActivity().getComponentName(), PackageManager.GET_META_DATA);
                robot.onStart(activityInfo);
            } catch (PackageManager.NameNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }


    @Override
    public void onTaskExecuteClick(int position) {
    }


    @Override
    public void onTaskDeleteClick(int position) {
        TemiTask selectedTask = tasks.get(position);
        viewModel.deleteTaskFromRepo(selectedTask);
    }


    @Override
    public void onTaskEditClick(int position) {
        Log.i(TAG, "Edit clicked");
        TemiTask selectedTask = tasks.get(position);
        Log.i(TAG, "Selected Task: " + selectedTask.toString());
        viewModel.initializeForTaskEdit(selectedTask);
        navController.navigate(R.id.action_ViewActivityFragment_to_InsertActivityFragment);
    }


    @Override
    public void onResume() {
        super.onResume();

        List<TemiTask> taskData = viewModel.getTaskLiveDataFromRepo().getValue();

        if (taskData != null) {
            tasks.clear();
            tasks.addAll(taskData);
            activityAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onTtsStatusChanged(@NonNull TtsRequest ttsRequest) {
        // Do whatever you like upon the status changing. after the robot finishes speaking
    }


   @Override
    public void update(TemiTask task) {
        Log.d("update", "TaskFragment update");

        try {
            if (task.isStarted()) {
                Log.d("update", "Task Started: " + task.isStarted());
                task.setStarted(false);
                task.cancelTask(getContext());
            } else {
                Log.d("update", "Task Started: " + task.isStarted());
                task.setStarted(true);
                task.schedule(getContext());
            }

            viewModel.update(task);
        } catch (Exception e) {
            Log.d("Task", "Exception during task update: " + e);
        }

        String toastText = String.format("Activity status changed");
        Toast.makeText(getContext(), toastText, Toast.LENGTH_SHORT).show();
    }
}
