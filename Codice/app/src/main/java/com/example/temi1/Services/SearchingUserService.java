package com.example.temi1.Services;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.CheckResult;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.temi1.model.TemiTask;
import com.example.temi1.viewmodel.GlobalViewModel;
import com.example.temi1.views.activity.NotificationActivity;
import com.robotemi.sdk.Robot;
import com.robotemi.sdk.TtsRequest;
import com.robotemi.sdk.listeners.OnDetectionDataChangedListener;
import com.robotemi.sdk.listeners.OnDetectionStateChangedListener;
import com.robotemi.sdk.listeners.OnGoToLocationStatusChangedListener;
import com.robotemi.sdk.listeners.OnUserInteractionChangedListener;
import com.robotemi.sdk.model.DetectionData;
import com.robotemi.sdk.permission.Permission;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/* SearchUserService will be triggered by TaskBroadcastReceiver via method
    startTaskService() when the start time of a saved and active task arrives.
    This service will search for the user in the locations selected in the route.
    If no one is detected, the robot moves to the next location.
    If it finds someone, it will approach the person and notify them of the task.
    Through NotificationsActivity the robot can stay with the person or return
    to the docking station. If no one is found at the end of the route,
    the robot returns to the docking station.
*/
public class SearchingUserService extends Service  implements OnDetectionStateChangedListener,
        OnUserInteractionChangedListener,
        OnDetectionDataChangedListener,
        OnGoToLocationStatusChangedListener{

    private Robot robot;
    private volatile int detectionState = 0; //Changed from stateDetection to detectionState
    private volatile  int detectionAngle;
    private List<String> locations;
    private int interactionCounter = 0;
    private String taskInfo, taskLocation ;
    private int userId;
    private static final int REQUEST_CODE_START_DETECTION_WITH_DISTANCE = 6;
    private static final int NOTIFICATION_ID = 1;
    private static final String CHANNEL_ID = "SearchingUserServiceChannel";
    public static final String TASK_INFO ="TASKINFO";
    public static final String TASK_LOCATION ="TASK_LOCATION";
    public static final String TASK_IDUSER = "TASKIDUSER";
    private String taskName,selectedActivity;
    private int currentUserId; // Variable for the current user ID
    private GlobalViewModel viewModel;
    private final String TAG = "SearchingUserService";


    public SearchingUserService() {

    }
    @SuppressLint("InvalidWakeLockTag")
    @Override
    public void onCreate() {
        super.onCreate();

        Log.d("SearchingUserService", "Service start");

        
        //--------------Robot Instance--------------
        robot = Robot.getInstance();
        locations = robot.getLocations();

        //--------------Robot Listeners--------------
        robot.addOnDetectionDataChangedListener(this);
        robot.addOnUserInteractionChangedListener(this);
        robot.addOnDetectionStateChangedListener(this);
        robot.addOnGoToLocationStatusChangedListener(this);

        // Create notification so you can call startForeground
        createNotificationChannel();
        Notification notification = buildNotification();

        // Call startForeground with the notification ID and the notification itself
        startForeground(NOTIFICATION_ID, notification);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Canale del servizio di ricerca",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


    private Notification buildNotification() {
        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Servizio in esecuzione")
                .setContentText("Tocca per aprire l'activity")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        userId = intent.getIntExtra(TASK_IDUSER, 0);

        Log.d("SearchingTaskService", "Service starting ");
        Log.d("SearchingTaskService", "TASK_INFO: "+intent.getStringExtra(TASK_INFO));
        Log.d("SearchingTaskService", "TASK_IDUSER: " + userId);

        taskInfo = intent.getStringExtra(TASK_INFO);
        taskLocation = intent.getStringExtra(TASK_LOCATION);

        navigateToLocation(taskLocation);
        startDetection();

        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        robot.removeOnDetectionDataChangedListener(this);
        robot.removeOnUserInteractionChangedListener(this);
        robot.removeOnDetectionStateChangedListener(this);
        robot.removeOnGoToLocationStatusChangedListener(this);
        try{
            Intent intentService = new Intent(getApplicationContext(), TemiTask.class);
            getApplicationContext().stopService(intentService);
        } catch (Exception e){
            Log.e("NotificationActivity", "Exception: "+e);
        }
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) { return null; }


    /*-------------------------------------- User Quest functions --------------------------------------*/

    /* This method starts the discovery process, which appears to involve a path
       default that the robot must follow.
    */
    private void startDetection() {
        if (requestPermissionIfNeeded(Permission.SETTINGS, REQUEST_CODE_START_DETECTION_WITH_DISTANCE)) {
            return;
        }
        navigateToLocation(taskLocation);
        Log.d("StartDetection", "detectionMode: "+robot.isDetectionModeOn());
    }


    private void navigateToLocation(String location) {
        // Check if the location is valid
        if (location != null && !location.isEmpty()) {
            // Make the robot move to the specified location
            robot.goTo(location);
        } else {
            // Handle the situation where the location is invalid
            Log.e(TAG, "Invalid position");
        }
    }


    /* This method appears to handle the actual detection. Set the robot to mode
        detection and then rotates the robot in 45 degree increments until
        detects a user or makes a full 360 degree rotation. When a user comes
        detected or the robot completes the rotation, actions are taken accordingly.
     */
    private void detecting(){
        if (!robot.isDetectionModeOn()){
            robot.setDetectionModeOn(true);
            Log.d("Detecting", "Mode: "+robot.isDetectionModeOn());
        }

        detectionAngle = 0;
        ScheduledExecutorService scheduleTaskExecutor = Executors.newScheduledThreadPool(1);

        scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    if (detectionState != OnDetectionStateChangedListener.DETECTED && detectionAngle < 360) {
                        turnBy();
                        detectionAngle = detectionAngle + 45;
                    } else if (detectionState == OnDetectionStateChangedListener.DETECTED) {
                        detectionAngle = 360;
                    }
                } catch (Exception e) {
                    Log.d("Robot", "Exception: " + e);
                }

                if (detectionAngle == 360) {
                    if (detectionState != OnDetectionStateChangedListener.DETECTED) {
                        // No users detected
                        robot.speak(TtsRequest.create("No users detected, activity will repeat in 10 minutes", true));

                        Log.d("searchForUserElsewhere", "No users detected, activity is repeated...");
                       // robot.goTo("home base");

                        rescheduleTask();
                    }
                    Log.d("detectingProces", "Stop turning around, ScheduleTaskExecutor stopped");
                    scheduleTaskExecutor.shutdown();
                }
            }
        }, 0, 5000, TimeUnit.MILLISECONDS);
    }


    /* This method rotates the robot 45 degrees */
    private void turnBy(){
        if (detectionState != OnDetectionStateChangedListener.DETECTED) {
            robot.turnBy(45);
            Log.d("TurnBy", "Robot turning around");
        }
    }


    @CheckResult
    private boolean requestPermissionIfNeeded(Permission permission, int requestCode) {
        if (robot.checkSelfPermission(permission) == Permission.GRANTED) {
            return false;
        }
        robot.requestPermissions(Collections.singletonList(permission), requestCode);
        return true;
    }


    /* This method manages the user's interactions with the robot. In particular,
        appears to stop the robot's movement and turns off tracking mode
        when a user is interacting.
    */
    private void interacting(){
        Log.d("Interaction", "Temi is interacting with a user");
        if ( detectionState == OnDetectionStateChangedListener.DETECTED){
            robot.stopMovement();
            robot.setDetectionModeOn(false);
        }
    }

    //--------------------------------------Temi Listeners--------------------------------------

    @Override
    public void onUserInteraction(boolean isInteracting) {
        if (isInteracting && interactionCounter<1){
            interactionCounter++;
            interacting();
        }
    }


    @Override
    public void onDetectionDataChanged(@NotNull DetectionData detectionData) {
    }


    @Override
    public void onDetectionStateChanged(int state) {
        Log.d("DetectionActivity", "onDetectionStateChanged - State:" + state);
        if (state == OnDetectionStateChangedListener.DETECTED) {
            // The user has been found, take the necessary actions
            robot.speak(TtsRequest.create("Hi, you probably have a saved event", true));

            Intent notificationIntent = new Intent(this, NotificationActivity.class);
            notificationIntent.putExtra(TASK_INFO, taskInfo);
            notificationIntent.putExtra(NotificationActivity.TASK_IDUSER, userId);

            notificationIntent.setFlags(Intent.FLAG_RECEIVER_FOREGROUND);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(notificationIntent);

        } else if (state == IDLE) {
            detectionState = state;
            Log.d("onDetectionStateChanged", "Nobody detected");
        } else if (state == OnDetectionStateChangedListener.LOST) {
            detectionState = state;
            robot.turnBy(45);
            robot.turnBy(-90);
        }
    }


    /* Directly checking if location is equal to the location specified in taskLocation.
       If so, we call the detecting() method to start the detection. Also, let's check
       if location is equal to "home base" and if detectionState is equal to 0 to handle the case in
       which the robot returns to its home base.
    */
    @Override
    public void onGoToLocationStatusChanged(@NotNull String location, String status, int descriptionId, @NotNull String description) {
        switch (status) {
            case OnGoToLocationStatusChangedListener.COMPLETE:
                Log.d("onGoToLocationStatus", "COMPLETE");
                Log.d("onGoToLocationStatus", "Location: " + location);

                if (location.equals(taskLocation)) {
                    Log.d("onGoToLocationStatus", "Detecting...");
                    detecting();
                } else if (location.equals("home base") && detectionState == 0) {
                    // Handle logic when the robot reaches the home base
                    rescheduleTask();
                }
                break;
            case OnGoToLocationStatusChangedListener.START:
                // Handle logic when the robot starts moving to a location
                break;
            case OnGoToLocationStatusChangedListener.CALCULATING:
                // Handle logic when the robot is calculating the path
                break;
            case OnGoToLocationStatusChangedListener.GOING:
                // Handle logic while the robot is en route to a location
                break;
            case OnGoToLocationStatusChangedListener.ABORT:
                // Handle logic when the robot's movement is aborted (e.g., due to an error)
                // You can add your specific actions or error handling code here
                Log.e("onGoToLocationStatus", "Movement aborted");
                break;
        }
    }


    private void rescheduleTask() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.MINUTE, 5);
        currentUserId = viewModel.getLoggedInUserId();

        TemiTask task = new TemiTask (
                new Random().nextInt(Integer.MAX_VALUE),
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                taskName,
                System.currentTimeMillis(),
                true,
                false,
                false,
                false,
                false,
                false,
                false,
                false,
                false,
                currentUserId,
                taskInfo,
                selectedActivity
        );

        task.schedule(getApplicationContext());
        Intent intentService = new Intent(getApplicationContext(),TemiTask.class);
        getApplicationContext().stopService(intentService);
    }
}
