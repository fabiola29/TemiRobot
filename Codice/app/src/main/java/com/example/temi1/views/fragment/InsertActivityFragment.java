package com.example.temi1.views.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.AdapterView;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.room.Room;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.temi1.R;
import com.example.temi1.db.TaskTemi.TemiTaskDao;
import com.example.temi1.db.TaskTemi.TemiTaskRoomDatabase;
import com.example.temi1.model.TemiTask;
import com.example.temi1.temi.TemiSpeaker;
import com.example.temi1.viewmodel.GlobalViewModel;
import com.google.android.material.textfield.TextInputLayout;
import com.robotemi.sdk.NlpResult;
import com.robotemi.sdk.Robot;
import com.robotemi.sdk.TtsRequest;
import com.robotemi.sdk.activitystream.ActivityStreamPublishMessage;
import com.robotemi.sdk.listeners.OnBeWithMeStatusChangedListener;
import com.robotemi.sdk.listeners.OnConstraintBeWithStatusChangedListener;
import com.robotemi.sdk.listeners.OnDetectionStateChangedListener;
import com.robotemi.sdk.listeners.OnGoToLocationStatusChangedListener;
import com.robotemi.sdk.listeners.OnLocationsUpdatedListener;
import com.robotemi.sdk.listeners.OnRobotReadyListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InsertActivityFragment extends Fragment implements
        Robot.NlpListener,
        OnRobotReadyListener,
        Robot.ConversationViewAttachesListener,
        Robot.WakeupWordListener,
        Robot.ActivityStreamPublishListener,
        Robot.TtsListener,
        OnBeWithMeStatusChangedListener,
        OnGoToLocationStatusChangedListener,
        OnLocationsUpdatedListener,
        OnConstraintBeWithStatusChangedListener,
        OnDetectionStateChangedListener,
        Robot.AsrListener  {

    private ImageButton bt_back;
    private NavController navController;
    private FrameLayout parentLayout;
    private GlobalViewModel viewModel;
    private ImageButton bt_help;
    private TemiSpeaker temiSpeaker;
    private Robot robot;
    public static final String ACTION_HOME_WELCOME = "home.welcome", ACTION_HOME_DANCE = "home.dance", ACTION_HOME_SLEEP = "home.sleep";
    public static final String HOME_BASE_LOCATION = "homebase";
    private Button saveActivityBtn;
    private TextInputLayout  nameTask,activityTask;
    private Spinner spinner_location;
    private ArrayList<String> LocationList = new ArrayList<>();
    private TimePicker timePicker;
    @BindView(R.id.fragment_createTask_recurring)
    CheckBox recurring;
    @BindView(R.id.fragment_createTask_recurring_options)
    LinearLayout recurringDays;
    @BindView(R.id.fragment_createTask_checkMon) CheckBox mon;
    @BindView(R.id.fragment_createTask_checkTue) CheckBox tue;
    @BindView(R.id.fragment_ccreateTask_checkWed) CheckBox wed;
    @BindView(R.id.fragment_createTask_checkThu) CheckBox thu;
    @BindView(R.id.fragment_createTask_checkFri) CheckBox fri;
    @BindView(R.id.fragment_createTask_checkSat) CheckBox sat;
    @BindView(R.id.fragment_createTask_checkSun) CheckBox sun;
    private final String TAG = "InsertActivityFragment";
    private int currentUserId;
    private static final int DEFAULT_HOUR = 8;
    private static final int DEFAULT_MINUTE = 0;


    /**
     * Setting up all the event listeners
     */

    @Override
    public void onStart() {
        super.onStart();

        // Add robot event listeners
        robot.addOnRobotReadyListener(this);
        robot.addNlpListener(this);
        robot.addOnBeWithMeStatusChangedListener(this);
        robot.addOnGoToLocationStatusChangedListener(this);
        robot.addWakeupWordListener(this);
        robot.addTtsListener(this);
        robot.addOnLocationsUpdatedListener(this);
        robot.addOnConstraintBeWithStatusChangedListener(this);
        robot.addOnDetectionStateChangedListener(this);
        robot.addAsrListener(this);
    }


    /**
     * Removing the event listeners upon leaving the app.
     */
    @Override
    public void onStop() {
        super.onStop();

        // Remove robot event listeners
        robot.removeOnRobotReadyListener(this);
        robot.removeNlpListener(this);
        robot.removeOnBeWithMeStatusChangedListener(this);
        robot.removeOnGoToLocationStatusChangedListener(this);
        robot.removeWakeupWordListener(this);
        robot.removeTtsListener(this);
        robot.removeOnLocationsUpdateListener(this);
        robot.removeAsrListener(this);
        robot.stopMovement();
    }


    public InsertActivityFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(getActivity()).get(GlobalViewModel.class);
        temiSpeaker = new TemiSpeaker(viewModel.getTemiRobot());

        robot = Robot.getInstance(); //get an instance of the robot

        LocationList.addAll(robot.getLocations());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        viewModel = new ViewModelProvider(getActivity()).get(GlobalViewModel.class);

        return inflater.inflate(R.layout.fragment_insert_activity, container, false);
    }

    /**
     * Designed to close the keyboard when not in use or when you decide not to use it
     */
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
    }

    @Override
    public void onViewCreated(@NonNull final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        parentLayout = view.findViewById(R.id.fragment_insert_activityFrameLayout);
        parentLayout.setOnTouchListener((v, motionEvent) -> {
            hideKeyboard(v);
            return true;
        });

        nameTask = view.findViewById(R.id.fragment_insert_activity_text_input_layout_nameActivity);
        if (viewModel.isTaskUpdate()) {
            nameTask.getEditText().setText(viewModel.getUpdatingTask().getTaskName());
        }

        bt_back =  view.findViewById(R.id.btn_back);
        bt_back.setOnClickListener(v -> button_back());

        saveActivityBtn = view.findViewById(R.id.fragment_create_route_button_sava);
        saveActivityBtn.setOnClickListener(v -> {
            saveCurrentActivity();
        });

        bt_help  = view.findViewById(R.id.btn_help);
        bt_help.setOnClickListener(v -> button_help());

        spinner_location = view.findViewById(R.id.spinner_location);
        setLocationListInSpinner();
        if (viewModel.isTaskUpdate()) {
            TemiTask updatingTask = viewModel.getUpdatingTask();
            String selectedActivity = updatingTask.getLocation();

            // Set the spinner_activity selection to the previously saved value
            int position = locationSpinnerAdapter.getPosition(selectedActivity);
            spinner_location.setSelection(position);
        }

        activityTask = view.findViewById(R.id.fragment_insert_activity_descriptionActivity_layout);
        if (viewModel.isTaskUpdate()) {
            activityTask.getEditText().setText(viewModel.getUpdatingTask().getTaskActivity());
        }
        listenToTextChangedEvents();

        timePicker = view.findViewById(R.id.fragment_creatTask_timePicker);
        if (viewModel.isTaskUpdate()) {
            int hour = viewModel.getUpdatingTask().getHour();
            int minute = viewModel.getUpdatingTask().getMinute();

            timePicker.setHour(hour);
            timePicker.setMinute(minute);
        }

        ButterKnife.bind(this, view);
        // Resume recurring status
        boolean isRecurring = false; // Defaults to false if you are not updating
        if (viewModel.isTaskUpdate()) {
            TemiTask updatingTask = viewModel.getUpdatingTask();
            isRecurring = updatingTask.isRecurring();

            // Resume the days of the week selected by the task
            mon.setChecked(updatingTask.isMonday());
            tue.setChecked(updatingTask.isTuesday());
            wed.setChecked(updatingTask.isWednesday());
            thu.setChecked(updatingTask.isThursday());
            fri.setChecked(updatingTask.isFriday());
            sat.setChecked(updatingTask.isSaturday());
            sun.setChecked(updatingTask.isSunday());
        }

        recurring.setChecked(isRecurring);

        recurring.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    recurringDays.setVisibility(View.VISIBLE);
                } else {
                    recurringDays.setVisibility(View.GONE);
                }
            }
        });

    }
    private void listenToTextChangedEvents() {
        EditText activityTasks = activityTask.getEditText();
        if (activityTasks != null) {
            activityTasks.addTextChangedListener(new TextWatcher() {

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    onActivityTaskChanged();
                }

                @Override
                public void afterTextChanged(Editable s) {

                }

            });
        }
    }

    public void onActivityTaskChanged() {
        if (getActivityTasks().length() >= 150) {
            setCounterEnabledTextInputDescription(false);
        } else {
           setCounterEnabledTextInputDescription(true);
        }
    }


    public void setCounterEnabledTextInputDescription(boolean enabled) {
        activityTask.setCounterEnabled(enabled);
    }


    public String getActivityTasks() {
        EditText activityTasks = activityTask.getEditText();
        if (activityTasks == null) return "";
        return activityTasks.getText().toString();
    }

    public int getTimePickerHour(TimePicker tp){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return tp.getHour();
        } else {
            return tp.getCurrentHour();
        }
    }

    public  int getTimePickerMinute(TimePicker tp) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return tp.getMinute();
        } else {
            return tp.getCurrentMinute();
        }
    }

    private String selectedLocation = ""; // Variable to hold the selected position
    private ArrayAdapter<String> locationSpinnerAdapter; // Adapter for the positions spinner

    private void setLocationListInSpinner() {
        //Use a custom ArrayAdapter to support single selection
        locationSpinnerAdapter = new ArrayAdapter<String>(requireContext(), R.layout.my_select_item, LocationList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                // Check if the location has been selected
                if (LocationList.get(position).equals(selectedLocation)) {
                    view.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                } else {
                    view.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                }
                return view;
            }
        };

        // Enable the location selection listener
        spinner_location.getBackground().setColorFilter(getResources().getColor(R.color.green), PorterDuff.Mode.SRC_ATOP);
        spinner_location.setAdapter(locationSpinnerAdapter);

        spinner_location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Manage location selection
                selectedLocation = LocationList.get(position);
                locationSpinnerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void saveCurrentActivity() {
        // We get the value entered by the user
        String nameTaskText = nameTask.getEditText().getText().toString().trim();
        String taskActivity = activityTask.getEditText().getText().toString().trim();
        int hour = getTimePickerHour(timePicker);
        int minute = getTimePickerMinute(timePicker);
        String selectedActivity = spinner_location.getSelectedItem().toString();

        if (nameTaskText.isEmpty() || taskActivity.isEmpty() ) {
            robot.speak(TtsRequest.create("Please do not leave fields blank", true));
        }else if (nameTaskText.isEmpty()) {
            robot.speak(TtsRequest.create("Please enter the name of task", true));
        }else if (taskActivity.isEmpty()) {
            robot.speak(TtsRequest.create("Please enter the description of task", true));
        } else {
            if (viewModel.isTaskUpdate()) {
                TemiTask updatingTask = viewModel.getUpdatingTask();
                updatingTask.setTaskName(nameTask.getEditText().getText().toString().trim());
                updatingTask.setTaskActivity(activityTask.getEditText().getText().toString().trim());

                updatingTask.setLocation(selectedActivity);

                // Update the time
                updatingTask.setHour(hour);
                updatingTask.setMinute(minute);

                // Update the time the day of week
                updatingTask.setRecurringDays(mon.isChecked(), tue.isChecked(),
                        wed.isChecked(), thu.isChecked(),
                        fri.isChecked(), sat.isChecked(),
                        sun.isChecked());

                // Reset all fields of the fragment
                nameTask.getEditText().setText("");
                activityTask.getEditText().setText("");
                timePicker.setHour(DEFAULT_HOUR); // Replace DEFAULT_HOUR with your desired default time
                timePicker.setMinute(DEFAULT_MINUTE); // Replace DEFAULT_MINUTE with the desired default minutes
                spinner_location.setSelection(0); // Set the spinner selection to a default value
                mon.setChecked(false); // Reset the days of the week, if any
                tue.setChecked(false);
                wed.setChecked(false);
                thu.setChecked(false);
                fri.setChecked(false);
                sat.setChecked(false);
                sun.setChecked(false);

                Log.i(TAG, "Task updated: " + updatingTask.toString());
                if (viewModel.updateTaskInRepo(updatingTask)) {
                    robot.speak(TtsRequest.create("Task successfully.", true));
                } else {
                    robot.speak(TtsRequest.create("Failed to insert task .", true));
                }
            } else { // New route being created
                saveTask();
            }
            navController.navigate(R.id.action_InsertActivityFragment_to_ActivityFragment);
        }
    }

    private void saveTask() {
        int hour = getTimePickerHour(timePicker);
        int minute = getTimePickerMinute(timePicker);
        String taskName = nameTask.getEditText().getText().toString();
        String taskActivity = activityTask.getEditText().getText().toString();
        String selectedActivity = spinner_location.getSelectedItem().toString();

        Random random = new Random();
        int taskId = random.nextInt(Integer.MAX_VALUE);

        currentUserId = viewModel.getLoggedInUserId();

        TemiTask task = new TemiTask(taskId, hour, minute, taskName, System.currentTimeMillis(), true,
                recurring.isChecked(), mon.isChecked(), tue.isChecked(), wed.isChecked(),
                thu.isChecked(), fri.isChecked(), sat.isChecked(), sun.isChecked(),
                currentUserId, taskActivity, selectedActivity);


        try {
            task.schedule(getContext());
        } catch (Exception e){
            Log.d("createTaskViewModel", "Exception: "+e);
        }

        new InsertTaskDataTask(getContext(), task).execute();

    }

    private void button_back(){
        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalViewModel viewModel = new ViewModelProvider(getActivity()).get(GlobalViewModel.class);
                viewModel.initializeForRouteCreation();
                navController.navigate(R.id.action_InsertActivityFragment_to_ActivityFragment);
            }
        });
    }


    private void button_help() {
        bt_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),  R.style.AlertDialogStyle);
                builder.setCancelable(true);
                builder.setTitle("Help");
                builder.setMessage(R.string.helperInsertActivityFragment);
                builder.setPositiveButton("I understand", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });
    }


    @Override
    public void onPublish(ActivityStreamPublishMessage message) {
        //After the activity stream finished publishing (photo or otherwise).
        //Do what you want based on the message returned.
        robot.speak(TtsRequest.create("Uploaded.", false));
    }


    @Override
    public void onAsrResult(@NonNull String asrResult) {
        Log.d("onAsrResult", "asrResult = " + asrResult);
    }


    @Override
    public void onConversationAttaches(boolean isAttached) {
        if (isAttached) {
            //Do something as soon as the conversation is displayed.
        }
    }


    /**
     * When adding the Nlp Listener to your project you need to implement this method
     * which will listen for specific intents and allow you to respond accordingly.
     * <p>
     * See AndroidManifest.xml for reference on adding each intent.
     */
    @SuppressLint("Range")
    @Override
    public void onNlpCompleted(NlpResult nlpResult) {
        //do something with nlp result. Base the action specified in the AndroidManifest.xml
        Toast.makeText(getActivity(), nlpResult.action, Toast.LENGTH_SHORT).show();

        switch (nlpResult.action) {
            case ACTION_HOME_WELCOME:
                robot.tiltAngle(23, 5.3F);
                break;

            case ACTION_HOME_DANCE:
                long t = System.currentTimeMillis();
                long end = t + 5000;
                while (System.currentTimeMillis() < end) {
                    robot.skidJoy(0F, 1F);
                }
                break;

            case ACTION_HOME_SLEEP:
                robot.goTo(HOME_BASE_LOCATION);
                break;
        }
    }


    @Override
    public void onTtsStatusChanged(TtsRequest ttsRequest) {
        // Do whatever you like upon the status changing. after the robot finishes speaking
    }


    @Override
    public void onWakeupWord(String wakeupWord, int direction) {
        // Do anything on wakeup. Follow, go to location, or even try creating dance moves.
    }


    @Override
    public void onBeWithMeStatusChanged(String status) {
        //  When status changes to "lock" the robot recognizes the user and begin to follow.
        switch (status) {

            case "abort":
                // do something i.e. speak
                robot.speak(TtsRequest.create("Abort", false));
                break;

            case "calculating":
                robot.speak(TtsRequest.create("Calculating", false));
                break;

            case "lock":
                robot.speak(TtsRequest.create("Lock", false));
                break;

            case "search":
                robot.speak(TtsRequest.create("search", false));
                break;

            case "start":
                robot.speak(TtsRequest.create("Start", false));
                break;

            case "track":
                robot.speak(TtsRequest.create("Track", false));
                break;
        }
    }


    @Override
    public void onConstraintBeWithStatusChanged(boolean isConstraint) {
        Log.d("onConstraintBeWith", "status = " + isConstraint);
    }


    @Override
    public void onDetectionStateChanged(int state) {
        Log.d("onDetectionStateChanged", "state = " + state);
        if (state == DETECTED) {
            robot.constraintBeWith();
        } else {
            robot.stopMovement();
        }
    }


    @Override
    public void onGoToLocationStatusChanged(String location, String status, int descriptionId, String description) {
        Log.d("GoToStatusChanged", "descriptionId=" + descriptionId + ", description=" + description);
        switch (status) {
            case "start":
                robot.speak(TtsRequest.create("Starting", false));
                break;

            case "calculating":
                robot.speak(TtsRequest.create("Calculating", false));
                break;

            case "going":
                robot.speak(TtsRequest.create("Going", false));
                break;

            case "complete":
                robot.speak(TtsRequest.create("Completed", false));
                break;

            case "abort":
                robot.speak(TtsRequest.create("Cancelled", false));
                break;
        }
    }


    @Override
    public void onLocationsUpdated(List<String> locations) {
        //Saving or deleting a location will update the list.
        Toast.makeText(getActivity(), "Locations updated :\n" + locations, Toast.LENGTH_LONG).show();
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

    private boolean checkForTimeConflict(int hour, int minute, boolean mon, boolean tue, boolean wed, boolean thu, boolean fri, boolean sat, boolean sun) {
        boolean[] selectedDays = {mon, tue, wed, thu, fri, sat, sun};

        TemiTaskRoomDatabase taskDb = TemiTaskRoomDatabase.getDatabase(getContext());
        TemiTaskDao taskDao = taskDb.taskDao();

        List<TemiTask> tasksWithSameTime = taskDao.getTaskByTime(hour, minute);
        for (TemiTask existingTask : tasksWithSameTime) {
            if (checkDayOverlap(existingTask, selectedDays)) {
                return true; // Time slot overlap found
            }
        }
        return false; // No time slot overlap
    }


    private boolean checkDayOverlap(TemiTask existingTask, boolean[] selectedDays) {
        boolean[] existingDays = {existingTask.isMonday(), existingTask.isTuesday(),
                existingTask.isWednesday(), existingTask.isThursday(),
                existingTask.isFriday(), existingTask.isSaturday(), existingTask.isSunday()};

        for (int i = 0; i < selectedDays.length; i++) {
            if (selectedDays[i] && existingDays[i]) {
                // Check if the overlapping day has the same hour and minute
                if (existingTask.getHour() == getTimePickerHour(timePicker) &&
                        existingTask.getMinute() == getTimePickerMinute(timePicker)) {
                    return true; // Overlapping day and time found
                }
            }
        }
        return false; // No overlapping day
    }


    private class InsertTaskDataTask extends AsyncTask<Void, Void, Boolean> {

        private Context context;
        private TemiTask temiTask;

        public InsertTaskDataTask(Context context, TemiTask temiTask) {
            this.context = context;
            this.temiTask = temiTask;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                TemiTaskRoomDatabase task_db = Room.databaseBuilder(getContext(), TemiTaskRoomDatabase.class, "TemiTaskDatabase").build();
                TemiTaskDao taskDao = task_db.taskDao();

                int hour = temiTask.getHour();
                int minute = temiTask.getMinute();
                String taskName = temiTask.getTaskName();
                boolean mon = temiTask.isMonday();
                boolean tue = temiTask.isTuesday();
                boolean wed = temiTask.isWednesday();
                boolean thu = temiTask.isThursday();
                boolean fri = temiTask.isFriday();
                boolean sat = temiTask.isSaturday();
                boolean sun = temiTask.isSunday();


                // Check for overlapping time and day combinations
                if (checkForTimeConflict(hour, minute, mon, tue, wed, thu, fri, sat, sun)) {
                    return true; // Task slot overlaps
                }


                // Check case 1: Different name, same time and same day
                List<TemiTask> tasksWithSameTimeAndDays = taskDao.getTaskByTimeAndDays(hour, minute, mon, tue, wed, thu, fri, sat, sun);
                for (TemiTask existingTask : tasksWithSameTimeAndDays) {
                    if (!existingTask.getTaskName().equals(taskName)) {
                        // Task simile trovato, non salvare
                        return true;
                    }
                }

                // Check case 2: Same name, same time and same day
                List<TemiTask> existingTasks = taskDao.getTaskByNameAndTimeAndDays(taskName, hour, minute, mon, tue, wed, thu, fri, sat, sun);
                if (existingTasks != null && !existingTasks.isEmpty()) {
                    // Task simile trovato, non salvare
                    return true;
                }

                // Perform database insert
                taskDao.insertTaskIntoDb(temiTask);
                return false; // Task inserted successfully
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Boolean isDuplicate) {
            if (isDuplicate) {

                robot.speak(TtsRequest.create("Slot occupied" ,true));

            } else {

                robot.speak(TtsRequest.create("Activity successfully saved " ,true));

                // After saving, update the task list
                new LoadTaskListTask().execute();
            }
        }
    }

    private class LoadTaskListTask extends AsyncTask<Void, Void, List<TemiTask>> {

        @Override
        protected List<TemiTask> doInBackground(Void... voids) {
            try {
                TemiTaskRoomDatabase task_db = TemiTaskRoomDatabase.getDatabase(getContext());
                TemiTaskDao taskDao = task_db.taskDao();
                return taskDao.getAll(); // Get all task list
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<TemiTask> taskList) {
            showTaskListOnConsole(taskList);
        }
    }

    // Method to show the task list on the console or elsewhere
    private void showTaskListOnConsole(List<TemiTask> taskList) {
        if (taskList != null && !taskList.isEmpty()) {
            for (TemiTask task : taskList) {
                Log.d("Database", "------------In Room Database ------------");
                Log.d("Database", "Task id: " + task.getTaskId());
                Log.d("Database", "Task Name: " + task.getTaskName());
                Log.d("Database", "Location: " + task.getLocation());
                Log.d("Database", "Activity: " + task.getTaskActivity());
                Log.d("Database", "UserId: " + task.getUserId());

            }
        } else {
            System.out.println("There are no activities in the database.");
        }
    }
}