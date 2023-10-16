package com.example.temi1.views.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.temi1.R;
import com.example.temi1.adaptor.LocationAdapter;
import com.example.temi1.model.LocationData;
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

public class InsertLocationOrPointInterestFragment extends Fragment implements
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
        Robot.AsrListener {

    public static final String ACTION_HOME_WELCOME = "home.welcome", ACTION_HOME_DANCE = "home.dance", ACTION_HOME_SLEEP = "home.sleep";
    public static final String HOME_BASE_LOCATION = "homebase";
    private Robot robot;
    private ImageButton bt_back;
    private ImageButton btn_help_insert_point_interest;
    private TextInputLayout insert_pointInterest;
    private Button bt_insert_point_interest,bt_followMe;
    private NavController navController;
    private FrameLayout parentLayout;
    private ListView listView;
    ArrayList<LocationData> locationDataList;


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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_insert_location_or_point_interest, container, false);
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        parentLayout = view.findViewById(R.id.fragment_insert_location_or_point_interest);
        parentLayout.setOnTouchListener((v, motionEvent) -> {
            hideKeyboard(v);
            return true;
        });

        insert_pointInterest = view.findViewById(R.id.activity_insert_text_input_pointInterest_layout);
        insert_pointInterest.setVisibility(View.INVISIBLE);

        bt_insert_point_interest = view.findViewById(R.id.activity_insert_button_sava_point_interest);
        bt_insert_point_interest.setOnClickListener(v ->  button_save_location());
        bt_insert_point_interest.setVisibility(View.INVISIBLE);

        bt_followMe= view.findViewById(R.id.activity_insert_button_follow_me);
        bt_followMe.setOnClickListener(v ->  button_followMe());

        btn_help_insert_point_interest =view.findViewById(R.id.btn_help_dialog_insert_point_interest);
        btn_help_insert_point_interest.setOnClickListener(v -> button_help());

        bt_back = view.findViewById(R.id.btn_back);
        bt_back.setOnClickListener(v -> button_back());

        listView = view.findViewById(R.id.listView);

        robot = Robot.getInstance();  // get an instance of the robot
    }


    @Override
    public void onRobotReady(boolean isReady) {
        if (isReady) {
            try {
                InitializeLocationData();
                final LocationAdapter locationAdapter = new LocationAdapter(getActivity(), locationDataList);
                listView.setAdapter(locationAdapter);

                final ActivityInfo activityInfo = getActivity().getPackageManager().getActivityInfo(getActivity().getComponentName(), PackageManager.GET_META_DATA);
                robot.onStart(activityInfo);
            } catch (PackageManager.NameNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }


    private void InitializeLocationData(){
        locationDataList = new ArrayList<LocationData>();
        List<String> locations = robot.getLocations();
        for(int i=0; i<locations.size(); i++){
            locationDataList.add(new LocationData(locations.get(i)));
        }
    }


    /**
     * Designed to close the keyboard when not in use or when you decide not to use it
     */
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager)  getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
    }


    private void button_back(){
        GlobalViewModel viewModel = new ViewModelProvider(getActivity()).get(GlobalViewModel.class);
        viewModel.initializeForRouteCreation();
        navController.navigate(R.id.action_InsertLocationOrPointInterest_to_OptionLocationFragment);
    }


    private void button_help() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),  R.style.AlertDialogStyle);
        builder.setCancelable(true);
        builder.setTitle("Help");
        builder.setMessage(R.string.helperFragmentInsertLocationOrPointInterest);
        builder.setPositiveButton("I understand", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }


    /**
     * This method to save a new location for temi.
     * Locate temi at the location you wish to save and give it a name,
     * the location coordinates are extracted and passed automatically in the request.
     */
    public void saveLocation(View view) {
        String locations = insert_pointInterest.getEditText().getText().toString().toLowerCase().trim();
        boolean result = robot.saveLocation(locations);
        if (result) {
            robot.speak(TtsRequest.create("I've successfully saved the " + locations + " location.",true));
        } else {
            robot.speak(TtsRequest.create("Saved the " + locations + " location failed.",true));
        }
    }


    private void button_followMe(){
         robot.beWithMe();
         bt_followMe.setVisibility(View.INVISIBLE);
         insert_pointInterest.setVisibility(View.VISIBLE);
         bt_insert_point_interest.setVisibility(View.VISIBLE);
    }


    private void button_save_location(){
        // Get the value entered by the user
        String insert_location_or_pointInterest = insert_pointInterest.getEditText().getText().toString().trim();
        if(insert_location_or_pointInterest.isEmpty()){
             robot.speak(TtsRequest.create("Please fill any empty",true));
         } else {
             saveLocation(insert_pointInterest);
             navController.navigate(R.id.action_InsertLocationOrPointInterest_to_OptionLocationFragment);
         }
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
}