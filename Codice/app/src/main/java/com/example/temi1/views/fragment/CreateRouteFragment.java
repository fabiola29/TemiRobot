package com.example.temi1.views.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.temi1.adaptor.CreateRouteAdapter;
import com.example.temi1.adaptor.LocationsAdapter;
import com.example.temi1.model.TemiRoute;
import com.example.temi1.temi.TemiSpeaker;
import com.example.temi1.viewmodel.GlobalViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.example.temi1.R;
import com.google.gson.Gson;
import com.ramotion.fluidslider.FluidSlider;
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

import kotlin.Unit;

import com.google.gson.reflect.TypeToken;

public class CreateRouteFragment extends Fragment implements
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

    private final String TAG = "CreateRouteFragment";
    public static final String ACTION_HOME_WELCOME = "home.welcome", ACTION_HOME_DANCE = "home.dance", ACTION_HOME_SLEEP = "home.sleep";
    public static final String HOME_BASE_LOCATION = "homebase";
    private CreateRouteAdapter createRouteAdapter;
    private LocationsAdapter locationsAdapter;
    private Button saveRouteBtn, continuousPatrolToggleBtn;
    private TextInputLayout routeTitle;
    private RecyclerView createRouteRv, locationsRv;
    private TemiSpeaker temiSpeaker;
    private NavController navController;
    private GlobalViewModel viewModel;
    private FluidSlider slider;
    private ImageButton bt_help;
    private int patrolCount;
    private ConstraintLayout containerLayout;
    private ImageButton exitBtn;
    private String deletedDestination;
    private boolean isContinuousPatrol;
    private FrameLayout parentLayout;
    private Robot robot;
    private int currentUserId; // Variabile per l'ID dell'utente corrente


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


    public CreateRouteFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(getActivity()).get(GlobalViewModel.class);
        temiSpeaker = new TemiSpeaker(viewModel.getTemiRobot());
        deletedDestination = null;
        this.isContinuousPatrol = false;
    }


    /**
     * Designed to close the keyboard when not in use or when you decide not to use it
     */
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(getActivity()).get(GlobalViewModel.class);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_route, container, false);
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        parentLayout = view.findViewById(R.id.createRouteFrameLayout);
        parentLayout.setOnTouchListener((v, motionEvent) -> {
            hideKeyboard(v);
            return true;
        });

        routeTitle = view.findViewById(R.id.fragment_create_route_text_input_location_layout);
        if (viewModel.isRouteUpdate()) {
            routeTitle.getEditText().setText(viewModel.getUpdatingRoute().getRouteTitle());
        }

        saveRouteBtn = view.findViewById(R.id.fragment_create_route_button_sava);
        saveRouteBtn.setOnClickListener(v -> {
            saveCurrentRoute();
        });

        createRouteRv = view.findViewById(R.id.createRouteRv);
        buildRouteRecyclerView();

        ItemTouchHelper myTouchHelper = generateTouchHelper();
        myTouchHelper.attachToRecyclerView(createRouteRv);

        locationsRv = view.findViewById(R.id.locationsRv);
        buildLocationRecyclerView();

        slider = view.findViewById(R.id.patrolCountSlider);
        buildSlider();

        exitBtn = view.findViewById(R.id.btn_back);
        exitBtn.setOnClickListener(v -> button_back());

        continuousPatrolToggleBtn = view.findViewById(R.id.fragment_create_route_button_continuos_patrol);
        continuousPatrolToggleBtn.setOnClickListener(v -> toggleInfinitePatrol());

        containerLayout = view.findViewById(R.id.createRouteParentLayout);

        bt_help  = view.findViewById(R.id.btn_help);
        bt_help.setOnClickListener(v -> button_help());

        robot = Robot.getInstance();  // get an instance of the robot in order to begin using its features.
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


    private void button_back(){
        GlobalViewModel viewModel = new ViewModelProvider(getActivity()).get(GlobalViewModel.class);
        viewModel.initializeForRouteCreation();
        navController.navigate(R.id.action_createRouteFragment_to_homeFragment);
    }


    private void button_help() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),  R.style.AlertDialogStyle);
        builder.setCancelable(true);
        builder.setTitle("Help");
        builder.setMessage(R.string.helperCreateRouteFragment);
        builder.setPositiveButton("I understand", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }


    private void buildRouteRecyclerView() {
        createRouteAdapter =  new CreateRouteAdapter(viewModel.getCreateRouteHelperList());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

        createRouteRv.setLayoutManager(mLayoutManager);
        createRouteRv.setAdapter(createRouteAdapter);
    }


    private void buildLocationRecyclerView() {
        locationsAdapter = new LocationsAdapter(temiSpeaker.getLocationsFromTemi(), viewModel, createRouteAdapter);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

        locationsRv.setLayoutManager(mLayoutManager);
        locationsRv.setAdapter(locationsAdapter);
    }


    private void buildSlider() {
        final int max = 100;
        final int min = 1;
        final int total = max - min;

        slider.setPositionListener(pos -> {
            final String value = String.valueOf( (int)(min + total * pos) );
            slider.setBubbleText(value);
            patrolCount = Integer.parseInt(value);
            return Unit.INSTANCE;
        });

        slider.setPosition(0f);
        slider.setStartText(String.valueOf(min));
        slider.setEndText(String.valueOf(max));
    }


    private void saveCurrentRoute() {
        // Ottieniamo il valore inserito dall'utente
        String route = routeTitle.getEditText().getText().toString().trim();

        if (route.isEmpty()) {
            robot.speak(TtsRequest.create("Please do not leave the Itinerary title field blank", true));
        } else {
            if (viewModel.isRouteUpdate()) {
                TemiRoute updatingRoute = viewModel.getUpdatingRoute();
                updatingRoute.setRouteTitle(routeTitle.getEditText().getText().toString());
                updatingRoute.setDestinations(viewModel.getCreateRouteHelperList());
                if (isContinuousPatrol) {
                    updatingRoute.setPatrolCount(Integer.MAX_VALUE);
                } else {
                    updatingRoute.setPatrolCount(patrolCount);
                }

                Log.i(TAG, "Route updated: " + updatingRoute.toString());
               // viewModel.updateRouteInRepo(updatingRoute);
                if (viewModel.updateRouteInRepo(updatingRoute)) {
                    robot.speak(TtsRequest.create("Route updated successfully", true));
                } else {
                    robot.speak(TtsRequest.create("Failed to update route.", true));
                }
            } else { // New route being created
                if (isContinuousPatrol) {
                    patrolCount = Integer.MAX_VALUE;
                }
                currentUserId = viewModel.getLoggedInUserId();

                int routeIdx = new Random().nextInt(Integer.MAX_VALUE);

                TemiRoute temiRoute = new TemiRoute(routeIdx,routeTitle.getEditText().getText().toString(), viewModel.getCreateRouteHelperList(), patrolCount,currentUserId);
                Log.i(TAG, "route added: " + temiRoute.toString());
                viewModel.insertRouteIntoRepo(temiRoute);

                robot.speak(TtsRequest.create("I've successfully saved the itinerary.", true));
            }
            navController.navigate(R.id.action_createRouteFragment_to_homeFragment);
        }
    }


    private ItemTouchHelper generateTouchHelper() {
        return new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(ItemTouchHelper.START | ItemTouchHelper.END,
                        ItemTouchHelper.UP | ItemTouchHelper.DOWN) {
                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
                                          @NonNull RecyclerView.ViewHolder target) {
                        int from = viewHolder.getAdapterPosition();
                        int to = target.getAdapterPosition();

                        createRouteAdapter.notifyItemMoved(from, to);

                        return true;
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                        int position = viewHolder.getAdapterPosition();
                        ArrayList<String> route = viewModel.getCreateRouteHelperList();
                        deletedDestination = route.get(position);
                        route.remove(position);
                        createRouteAdapter.notifyItemRemoved(position);

                        Snackbar.make(createRouteRv, "Deleted " + deletedDestination, Snackbar.LENGTH_LONG)
                                .setAction("Undo", v -> {
                                    route.add(position, deletedDestination);
                                    createRouteAdapter.notifyItemInserted(position);
                                }).show();
                    }

                    // 1. This callback is called when a ViewHolder is selected.
                    //    We highlight the ViewHolder here.
                    @Override
                    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
                        super.onSelectedChanged(viewHolder, actionState);
                        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
                            viewHolder.itemView.setAlpha(0.5f);
                        }
                    }

                    // 2. This callback is called when the ViewHolder is
                    //    unselected (dropped). We unhighlight the ViewHolder here.
                    @Override
                    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                        super.clearView(recyclerView, viewHolder);
                        viewHolder.itemView.setAlpha(1.0f);
                    }

                    @Override
                    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView,
                                            @NonNull RecyclerView.ViewHolder viewHolder,
                                            float dX, float dY, int actionState,
                                            boolean isCurrentlyActive) {
                        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                    }
                });
    }


    private void toggleInfinitePatrol() {
        this.isContinuousPatrol = !this.isContinuousPatrol;
        toggleMargins();
    }


    private void toggleMargins() {
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(containerLayout);
        if (this.isContinuousPatrol) {
            // Adjusting margins
            constraintSet.clear(R.id.fragment_create_route_button_continuos_patrol, ConstraintSet.START);
            constraintSet.connect(R.id.fragment_create_route_button_continuos_patrol, ConstraintSet.START,
                    R.id.createRouteParentLayout, ConstraintSet.START);
            constraintSet.connect(R.id.fragment_create_route_button_continuos_patrol, ConstraintSet.END,
                    R.id.createRouteParentLayout, ConstraintSet.END);
            constraintSet.setVisibility(R.id.patrolCountSlider, View.GONE);
        } else {
            constraintSet.clear(R.id.fragment_create_route_button_continuos_patrol, ConstraintSet.START);
            constraintSet.connect(R.id.fragment_create_route_button_continuos_patrol, ConstraintSet.START,
                    R.id.patrolCountSlider, ConstraintSet.END);
            constraintSet.connect(R.id.fragment_create_route_button_continuos_patrol, ConstraintSet.END,
                    R.id.createRouteParentLayout, ConstraintSet.END);
            constraintSet.setVisibility(R.id.patrolCountSlider, View.VISIBLE);
        }
        constraintSet.applyTo(containerLayout);
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