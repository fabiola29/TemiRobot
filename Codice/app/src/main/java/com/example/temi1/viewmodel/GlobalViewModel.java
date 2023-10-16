package com.example.temi1.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.temi1.db.PetrolTemi.TemiPatrolRepository;
import com.example.temi1.db.TaskTemi.TemiTaskRepository;
import com.example.temi1.db.UserTemi.TemiUserRepository;
import com.example.temi1.model.TemiRoute;
import com.example.temi1.model.TemiTask;
import com.robotemi.sdk.Robot;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GlobalViewModel extends AndroidViewModel {

    private final String TAG = "GlobalViewModel";
    private static int MAX_THREAD_COUNT = 5;
    private TemiPatrolRepository temiPatrolRepo;
    private TemiTaskRepository temiTaskRepository;
    private LiveData<List<TemiRoute>> routeLiveData;
    private LiveData<List<TemiTask>> taskLiveData;
    private ExecutorService executorService;
    private volatile HashMap<String, String> fileIdMap;
    private Robot temiRobot;
    private ArrayList<String> createRouteHelperList;
    private TemiRoute selectedRoute;
    private TemiRoute updatingRoute;
    private boolean isRouteUpdate;
    private int userId;
    private int routeIdx;
    private ArrayList<String> createTaskHelperList;
    private boolean isTaskUpdate;
    private TemiTask updatingTask;
    private TemiUserRepository temiUserRepository;
    private TemiTask selectedTask;
    private int loggedInUserId = -1; // -1 indicates that no user is logged in
    private int selectedRouteId = -1; // Initialized with a value representing "no path selected"


    public GlobalViewModel(Application application) {
        super(application);
        temiPatrolRepo = new TemiPatrolRepository(application);
        routeLiveData = temiPatrolRepo.getAllRoutesFromDb();
        temiTaskRepository = new TemiTaskRepository(application);
        createRouteHelperList = new ArrayList<>(); // Initialize createRouteHelperList as a new ArrayList
        createTaskHelperList = new ArrayList<>();  // Initialize createTaskHelperList as a new ArrayList
        taskLiveData = temiTaskRepository.getAllTaskFromDb();
        temiUserRepository = TemiUserRepository.getInstance(application);
    }


    public void initialize() {
        initializeTemiRobot();
        initializeExecutorService();
        fileIdMap = new HashMap<>();
    }


    private void initializeExecutorService() {
        executorService = Executors.newFixedThreadPool(MAX_THREAD_COUNT);
    }


    public void initializeTemiRobot() {
        temiRobot = Robot.getInstance();
        temiRobot.setDetectionModeOn(false);
    }


    public void initializeForRouteCreation() {
        isRouteUpdate = false;
        createRouteHelperList = new ArrayList<>();
    }


    public void initializeForRouteEdit(TemiRoute temiRoute) {
        createRouteHelperList = new ArrayList<>();

        isRouteUpdate = true;
        this.updatingRoute = temiRoute;
        createRouteHelperList.addAll(updatingRoute.getDestinations());
    }


    public void insertLocationIntoHelper(String location) {
        createRouteHelperList.add(location);
    }


    public ArrayList<String> getCreateRouteHelperList() {
        return createRouteHelperList;
    }


    public ExecutorService getExecutorService() {
        return executorService;
    }


    public void insertRouteIntoRepo(TemiRoute temiRoute) {
        temiPatrolRepo.insertTemiRouteIntoDb(temiRoute);
    }


    public void deleteRouteFromRepo(TemiRoute temiRoute) {
        temiPatrolRepo.deleteTemiRouteFromDb(temiRoute);
    }


    public boolean updateRouteInRepo(TemiRoute route) {
        try {
            updateRouteInRepository(route);
            return true; // Return true if the update is successful
        } catch (Exception e) {
            e.printStackTrace();
            return false; // Return false if an error occurs during the update
        }
    }


    private void updateRouteInRepository(TemiRoute route) {
       // Perform the actual update in the repository
       temiPatrolRepo.updateRouteInDb(route);
    }


    public LiveData<List<TemiRoute>> getRouteLiveDataFromRepo() {
        return temiPatrolRepo.getAllRoutesFromDb();
    }


    public LiveData<List<TemiTask>> getTaskLiveDataFromRepo() {
        return temiTaskRepository.getAllTaskFromDb();
    }


    public TemiRoute getSelectedRoute() {
        return selectedRoute;
    }


    public void setSelectedRoute(TemiRoute selectedRoute) {
        this.selectedRoute = selectedRoute;
    }


    public Robot getTemiRobot() {
        return temiRobot;
    }


    public boolean isRouteUpdate() {
        return isRouteUpdate;
    }


    public TemiRoute getUpdatingRoute() {
        return updatingRoute;
    }


    public void setUserId(int userId) {
        this.userId = userId;
    }


    public int getUserId() {
        return userId;
    }


    public int getRouteIdx() {
        return routeIdx;
    }


    public void setRouteIdx(int routeIdx) {
        this.routeIdx = routeIdx;
    }


    public void deleteTaskFromRepo(TemiTask temiTask) {
        temiTaskRepository.deleteTemiTaskFromDb(temiTask);
    }


    public void initializeForTaskEdit(TemiTask temiTask) {
        createTaskHelperList = new ArrayList<>();

        isTaskUpdate = true;
        this.updatingTask = temiTask;
    }


    public void initializeForTaskCreation() {
        isTaskUpdate = false;
        createTaskHelperList = new ArrayList<>();
    }


    public boolean isTaskUpdate() {
        return isTaskUpdate;
    }


    public TemiTask getUpdatingTask() {
        return updatingTask;
    }


    public boolean updateTaskInRepo(TemiTask task) {
        try {
            updateTaskInRepository(task);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    private void updateTaskInRepository(TemiTask task) {
        // Perform the actual update in the repository
        temiTaskRepository.updateTaskInDb(task);
    }


    // Method to set the logged in user ID
    public void setLoggedInUserId(int userId) {
        loggedInUserId = userId;
    }


    // Method to retrieve the logged in user ID
    public int getLoggedInUserId() {
        return loggedInUserId;
    }


    // Method to set the ID of the selected route
    public void setSelectedRouteId(int routeId) {
        selectedRouteId = routeId;
    }


    public void  update(TemiTask temiTask){
        temiTaskRepository.update(temiTask);
    }


    public void setSelectedTask(TemiTask selectedTask) {
        this.selectedTask = selectedTask;
    }
}
