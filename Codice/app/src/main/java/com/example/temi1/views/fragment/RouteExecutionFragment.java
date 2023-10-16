package com.example.temi1.views.fragment;

import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.temi1.adaptor.RouteAdapter;
import com.example.temi1.model.TemiRoute;
import com.example.temi1.viewmodel.GlobalViewModel;
import com.example.temi1.R;
import com.robotemi.sdk.Robot;
import com.robotemi.sdk.TtsRequest;
import com.robotemi.sdk.listeners.OnRobotReadyListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RouteExecutionFragment extends Fragment implements RouteAdapter.OnRouteClickListener, Robot.TtsListener, OnRobotReadyListener{

    private final String TAG = "RouteExecutionFragment";
    private NavController navController;
    private RecyclerView routeRv;
    private RouteAdapter routeAdapter;
    private HashMap<String, TemiRoute> routeMap;
    private ArrayList<TemiRoute> routes;
    private GlobalViewModel viewModel;
    private ImageButton exitBtn;
    private Robot robot;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(getActivity()).get(GlobalViewModel.class);
        routeMap = new HashMap<>();
        routes = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.route_execution_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        routeRv = view.findViewById(R.id.routeRv);
        initializeRecylerView();
        attachLiveDataToRecyclerView();

        exitBtn = view.findViewById(R.id.btn_back_view_Location);
        exitBtn.setOnClickListener(view1 -> button_back());

        //Get robot instance
        robot = Robot.getInstance();
    }


    private void button_back(){
        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalViewModel viewModel = new ViewModelProvider(getActivity()).get(GlobalViewModel.class);
                viewModel.initializeForRouteCreation();
                navController.navigate(R.id.HomeFragment);
            }
        });
    }


    private void initializeRecylerView() {
        Log.i(TAG, "buildRecyclerView called");
        routeAdapter =  new RouteAdapter(routes, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        routeRv.setAdapter(routeAdapter);
        routeRv.setLayoutManager(mLayoutManager);
    }


    private void attachLiveDataToRecyclerView() {
        // Get logged in user ID from global variable or user session
        int userId = viewModel.getLoggedInUserId();
        final Observer<List<TemiRoute>> routeListObserver = temiRoutes -> {
            Log.i(TAG, "onChanged called");
            routes.clear();
            for (TemiRoute route : temiRoutes) {
                if (route.getUserId() == userId) {
                    routes.add(route);
                }
            }
            routeAdapter.updateHelper(routes);
            routeAdapter.notifyDataSetChanged();
        };
        viewModel.getRouteLiveDataFromRepo().observe(getActivity(), routeListObserver);
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
    public void onRouteDeleteClick(int position) {
        TemiRoute selectedRoute = routes.get(position);
        viewModel.deleteRouteFromRepo(selectedRoute);
    }

    @Override
    public void onRouteExecuteClick(int position) {
        TemiRoute selectedRoute = routes.get(position);
        Log.i(TAG, "Selected Route: " + selectedRoute.toString());
        viewModel.setSelectedRoute(selectedRoute);
        navController.navigate(R.id.action_routeExecutionFragment_to_patrolFragment);
    }

    @Override
    public void onRouteEditClick(int position) {
        Log.i(TAG, "Edit clicked");
        TemiRoute selectedRoute = routes.get(position);
        Log.i(TAG, "Selected Route: " + selectedRoute.toString());
        viewModel.initializeForRouteEdit(selectedRoute);
        navController.navigate(R.id.action_routeExecutionFragment_to_createRouteFragment);
    }


    @Override
    public void onTtsStatusChanged(@NonNull TtsRequest ttsRequest) {
        // Do whatever you like upon the status changing. after the robot finishes speaking
    }
}